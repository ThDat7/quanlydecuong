package com.dat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentInfoDto {
    private Integer id;
    @NotNull(message = "{user.username.nullErr}")
    @Size(max = 50, message = "{user.username.lengthErr}")
    private String username;
    private String password;
    @NotNull(message = "{student.studentCode.nullErr}")
    @Size(min = 5, max = 50, message = "{student.studentCode.lengthErr}")
    private String studentCode;
    @NotNull(message = "{user.name.nullErr}")
    @Size(min = 1, max = 50, message = "{user.name.lengthErr}")
    private String firstName;
    @NotNull(message = "{user.name.nullErr}")
    @Size(min = 1, max = 50, message = "{user.name.lengthErr}")
    private String lastName;
    @NotNull(message = "{user.email.nullErr}")
    @Size(min = 3, max = 50, message = "{user.email.lengthErr}")
    private String email;
    private String phone;
    @NotNull(message = "{student.schoolYear.nullErr}")
    @Min(value = 2000, message = "{student.schoolYear.minErr}")
    private Integer schoolYear;
    @NotNull(message = "{student.majorId.nullErr}")
    @Min(value = 1, message = "{student.major.nullErr}")
    private Integer majorId;
    private MultipartFile avatar;
    private String image;
}
