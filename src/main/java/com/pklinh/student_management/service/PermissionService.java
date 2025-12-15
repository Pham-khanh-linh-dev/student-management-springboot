package com.pklinh.student_management.service;

import com.pklinh.student_management.Mapper.PermissionMapper;
import com.pklinh.student_management.dto.request.PermissionRequest;
import com.pklinh.student_management.dto.response.PermissionResponse;
import com.pklinh.student_management.repository.PermissionRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Builder
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor  //Lombok sẽ tự tạo contructor chứa tất cả các field final để phục vụ Contructor Ịnect

public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        return permissionMapper.toPermissionResponse(
                permissionRepository.save(
                        permissionMapper.toPermission(request)));
    }

    public List<PermissionResponse> getAll(){
        var permissionList = permissionRepository.findAll();
        return permissionList.stream().map(permissionMapper::toPermissionResponse).toList();
    }


    public void delete(@PathVariable String permissionName){
        permissionRepository.deleteById(permissionName);
    }
}
