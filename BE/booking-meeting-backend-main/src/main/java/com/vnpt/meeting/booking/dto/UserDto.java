package com.vnpt.meeting.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpt.meeting.booking.constant.UserStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private Date createdDate;
    private Date modifiedDate;
    private UserStatus status;
    List<RoleDto> roles;
    private String photo;
    private UserDeptDto dept;
}
