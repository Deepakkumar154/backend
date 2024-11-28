package org.tragoit.dto;

import lombok.Getter;
import lombok.Setter;
import org.tragoit.model.PickupPoint;

import java.util.List;

@Getter
@Setter
public class TripRequestDto {
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
    private Double price;
    private List<String> imageUrls;
    private List<PickupPoint> pickupPoints;
    private Long agentId;
    private ItineraryDto itinerary;
}
