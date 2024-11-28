package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripDto {
    private Long id;
    private String description;
    private String origin;
    private String destination;
    private String startDate;
    private String endDate;
    private Integer noOfDays;
    private Integer noOfNights;
    private String type;
    private String category;
    private Integer noOfTravelers;
    private List<String> imageUrls;
    private List<PickupPointDto> pickupPoints;
    private AgentDto agentId;
    private ItineraryDto itinerary;
}
