package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto {
    private Long id;
    private String name;
    private String description;
    private Long itineraryId;  // Associated Itinerary ID
}
