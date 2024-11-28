package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PickupPointDto {
    private Long id;
    private String location;
    private String pickupTime;
    private String additionalInfo;
}
