package com.pklinh.student_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data  // Tự động sinh get,set,toString
@Builder  // Cho phép tạo object bằng builder
@NoArgsConstructor  // Sinh contructor khng có tham số( đây la điều bắt buộc vì JPA tạo object băng reflection -> cần một contructor rỗng để khởi tạo object)
@AllArgsConstructor // sinh contructor với tất cả fields
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //Khai báo Id có kiểu uuid là tối ưu, tránh trùng, scan
    String id;
    String username;
    String password;
    String email;
    String mssv;
    double gpa;
    LocalDate dob;

}
