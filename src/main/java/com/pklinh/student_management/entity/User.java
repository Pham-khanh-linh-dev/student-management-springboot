package com.pklinh.student_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@Data  // Tự động sinh get,set,toString
@Builder  // Cho phép tạo object bằng builder
@NoArgsConstructor  // Sinh contructor khng có tham số( đây la điều bắt buộc vì JPA tạo object băng reflection -> cần một contructor rỗng để khởi tạo object)
@AllArgsConstructor // sinh contructor với tất cả fields
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //Khai báo Id có kiểu uuid là tối ưu, tránh trùng, scan
    String id;
    String username;
    String password;
    String email;
    String mssv;
    @Column(nullable = true)
    Double gpa;
    LocalDate dob;
//    @ElementCollection(fetch = FetchType.EAGER)
    @ManyToMany
    Set<Role> roles;
}
