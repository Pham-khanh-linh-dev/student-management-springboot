package com.pklinh.student_management.service;

import com.pklinh.student_management.Mapper.UserMapper;
import com.pklinh.student_management.dto.request.UserCreationRequest;
import com.pklinh.student_management.dto.request.UserUpdateRequest;
import com.pklinh.student_management.dto.response.UserResponse;
import com.pklinh.student_management.entity.User;
import com.pklinh.student_management.exception.AppException;
import com.pklinh.student_management.exception.ErrorCode;
import com.pklinh.student_management.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor  //Lombok sẽ tự tạo contructor chứa tất cả các field final để phục vụ Contructor Ịnect
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        // Dùng maptruct để map dto sang entity
        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public UserResponse getUser(String id) {

        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user,request);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
