package com.pklinh.student_management.service;

import com.pklinh.student_management.Mapper.UserMapper;
import com.pklinh.student_management.Role;
import com.pklinh.student_management.dto.request.UserCreationRequest;
import com.pklinh.student_management.dto.request.UserUpdateRequest;
import com.pklinh.student_management.dto.response.ApiResponse;
import com.pklinh.student_management.dto.response.UserResponse;
import com.pklinh.student_management.entity.User;
import com.pklinh.student_management.exception.AppException;
import com.pklinh.student_management.exception.ErrorCode;
import com.pklinh.student_management.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Builder
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor  //Lombok sẽ tự tạo contructor chứa tất cả các field final để phục vụ Contructor Ịnect
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        // Dùng maptruct để map dto sang entity
        User user = userMapper.toUser(request);

        // Dùng Bcrypt mã hóa mật khẩu Buổi #6
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // set role cho user để phân quyền
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);


        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("in method getusers");

//        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();

        return userMapper.toUserResponse(userRepository.findAll());


    }
    // Chỉ lấy được thông tin của người đang đăng nhập
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("in method postAuthorize");

        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user,request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Lấy thông tin người đang đăng nhập
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        log.info("Username: {}", context.getAuthentication().getName());
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }
}
