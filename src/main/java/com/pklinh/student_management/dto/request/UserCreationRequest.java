package com.pklinh.student_management.dto.request;

import com.pklinh.student_management.validator.DobConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USERNAME_INVALID")
    private String username;
    @Size(min = 2, message = "PASSWORD_INVALID")
    String password;
    String email;
    String mssv;
    Double gpa;

    @DobConstraint(min = 5, message = "DOB_INVALID")
    LocalDate dob;
}
