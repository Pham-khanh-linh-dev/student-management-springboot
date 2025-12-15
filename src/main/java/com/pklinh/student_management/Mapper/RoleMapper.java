package com.pklinh.student_management.Mapper;

import com.pklinh.student_management.dto.request.RoleRequest;
import com.pklinh.student_management.dto.response.RoleResponse;
import com.pklinh.student_management.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleRequest request);

}
