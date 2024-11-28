package org.tragoit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequestDto {
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty
    private String company;
    @NotEmpty
    private String contact;
    @NotEmpty
    private String address;
    @NotEmpty
    private String foundOn;
    @NotEmpty
    private String govtId;
    @NotEmpty
    private String govtIdType;
    @NotEmpty
    private String gstNumber;
    @NotEmpty
    private String policyDocument;
}
