package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StayDto {
    private Long id;
    private String hotelName;
    private String address;
    private String checkIn;
    private String checkOut;
    private String rating;
    private Boolean isBreakfastIncluded;
    private Boolean isLunchIncluded;
    private Boolean isDinnerIncluded;
    private List<RoomTypeDto> roomTypes;
}
