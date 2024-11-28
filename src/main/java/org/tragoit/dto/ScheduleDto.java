package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDto {
    private Long id;
    private String day;
    private String location;
    private StayDto stay;  // Associated Stay ID
    private Long itineraryId;  // Associated Itinerary ID
}
