package org.tragoit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private Long id;
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
