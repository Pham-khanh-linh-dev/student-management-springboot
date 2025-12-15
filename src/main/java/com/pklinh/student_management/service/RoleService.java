package com.pklinh.student_management.service;

import com.pklinh.student_management.Mapper.RoleMapper;
import com.pklinh.student_management.dto.request.RoleRequest;
import com.pklinh.student_management.dto.response.RoleResponse;
import com.pklinh.student_management.entity.Permission;
import com.pklinh.student_management.exception.AppException;
import com.pklinh.student_management.exception.ErrorCode;
import com.pklinh.student_management.repository.PermissionRepository;
import com.pklinh.student_management.repository.RoleRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Builder
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor  //Lombok sẽ tự tạo contructor chứa tất cả các field final để phục vụ Contructor Ịnect

public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        var roleList = roleRepository.findAll();
        return roleList.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Transactional
    public RoleResponse updateRole(String roleName, RoleRequest request){
        var role = roleRepository.findByName(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        var permissions = permissionRepository.findAllById(request.getPermissions());
        roleMapper.updateRole(role, request);
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

}
