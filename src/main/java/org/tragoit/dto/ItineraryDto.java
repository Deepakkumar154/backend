package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItineraryDto {
    private Long id;
    private String overview;
    private List<ActivityDto> activities;  // List of activities
    private List<ScheduleDto> schedules;    // List of schedules
    private Long tripId;
}
