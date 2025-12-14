package com.pklinh.student_management.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.pklinh.student_management.dto.request.AuthenticationRequest;
import com.pklinh.student_management.dto.request.IntrospectRequest;
import com.pklinh.student_management.dto.response.AuthenticationResponse;
import com.pklinh.student_management.dto.response.IntrospectResponse;
import com.pklinh.student_management.entity.User;
import com.pklinh.student_management.exception.AppException;
import com.pklinh.student_management.exception.ErrorCode;
import com.pklinh.student_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    // Tiêm khóa bí mật từ application.yml
    @NonFinal
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
//    Kiểm tra log-in
    public AuthenticationResponse authenticate(AuthenticationRequest request){
//        check username
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check ps
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

//        true -> tạo jwt cho user
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

//    Kiểm tra JWT có hợp lệ không
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException , ParseException {
        var token = request.getToken();

//        Tạo bộ xác minh JWSVerifier ( cụ thể là MACVerifier) dùng cùng signer key
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//        CHuyển đổi token thành chuỗi đối tượng SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(token);

//        Xem thời gian hết hạn của token
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

//        Kiểm tra xem token có hợp lệ không
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }


// Tạo JWT
    private String generateToken(User user){
//        A. ĐỊnh nghĩa header
//        Khai báo đây là một JSON Web Signature (JWS).
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

//        B.Định nghĩa ClaimSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // Ai là chủ sở hữu Token
                .issuer("pklinh.com") // Đơn vị phát hành
                .issueTime(new Date()) // Thời gian phát hành (iat)
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // Thời gian hết hạn (exp): 1 giờ sau
                ))
                .claim("scope", buildScope(user)) // Thuộc tính bổ sung (optional)
                .build();

//        C. Ký Token
        Payload payload = new Payload((jwtClaimsSet.toJSONObject()));
        // Tạo đối tượng JWS từ header và payload
        JWSObject jwsObject = new JWSObject(header, payload);
        try{
            // Ký bằng MACSigner và Khóa bí mật
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            // Trả về chuỗi Header.Payload.Signature
            return jwsObject.serialize();
        } catch(JOSEException e){
            log.error("Error generating token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
