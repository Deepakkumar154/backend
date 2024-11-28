package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTripResponseDto {
    private Long id;
    private String description;
    private Long agentId;
}
