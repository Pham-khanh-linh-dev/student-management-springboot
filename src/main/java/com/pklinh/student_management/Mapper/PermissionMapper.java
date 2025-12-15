package com.pklinh.student_management.Mapper;

import com.pklinh.student_management.dto.request.PermissionRequest;
import com.pklinh.student_management.dto.response.PermissionResponse;
import com.pklinh.student_management.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
