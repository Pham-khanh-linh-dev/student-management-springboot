package com.pklinh.student_management.Mapper;

import com.pklinh.student_management.dto.request.UserCreationRequest;
import com.pklinh.student_management.dto.request.UserUpdateRequest;
import com.pklinh.student_management.dto.response.UserResponse;
import com.pklinh.student_management.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
//    UserResponse toUserResponse(User user);

//    @Mapping(source = "mssv", target = "password") // cả mssv và ps đều được map bởi mssv
//    @Mapping(target = "gpa", ignore = true)
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponse(List<User> users);
}
