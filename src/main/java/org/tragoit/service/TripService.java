package org.tragoit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.tragoit.dto.ActivityDto;
import org.tragoit.dto.AgentDto;
import org.tragoit.dto.ImageDto;
import org.tragoit.dto.ItineraryDto;
import org.tragoit.dto.PickupPointDto;
import org.tragoit.dto.RoomTypeDto;
import org.tragoit.dto.SaveTripResponseDto;
import org.tragoit.dto.ScheduleDto;
import org.tragoit.dto.StayDto;
import org.tragoit.dto.TripDto;
import org.tragoit.dto.TripRequestDto;
import org.tragoit.exception.AppException;
import org.tragoit.model.Agent;
import org.tragoit.model.Image;
import org.tragoit.model.Itinerary;
import org.tragoit.model.PickupPoint;
import org.tragoit.model.Schedule;
import org.tragoit.model.Stay;
import org.tragoit.model.Trip;
import org.tragoit.repository.AgentRepository;
import org.tragoit.repository.ImageRepository;
import org.tragoit.repository.ItineraryRepository;
import org.tragoit.repository.PickupPointRepository;
import org.tragoit.repository.ScheduleRepository;
import org.tragoit.repository.StayRepository;
import org.tragoit.repository.TripRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class TripService {
    private final TripRepository tripRepository;
    private final AgentRepository agentRepository;
    private final ItineraryService itineraryService;
    private final ImageRepository imageRepository;
    private final ItineraryRepository itineraryRepository;
    private final PickupPointRepository pickupPointRepository;
    private final StayRepository stayRepository;
    private final ScheduleRepository scheduleRepository;
//    private final ItineraryRepository itineraryRepository;

//    public List<Trip> getAllTrips() {
//        return tripRepository.getAllTrips();
//    }
//
//    public Trip getTripById(Long id) {
//        return tripRepository.getTripById(id);
//    }
//
//    public List<Trip> getLatestTrips() {
//        return tripRepository.getLatestTrips();
//    }
//
//    public List<Trip> getTripByDestination(String destination) {
//        return tripRepository.getTripByDestination(destination);
//    }
//
//    public List<Trip> getTripByAdventure(String adventures) {
//        return tripRepository.getTripByAdventure(Adventures.valueOf(adventures.toUpperCase()));
//    }
//
//    public List<Trip> getTripByCategory(String category) {
//        return tripRepository.getTripByCategory(TripCategory.valueOf(category.toUpperCase()));
//    }

    @Transactional
    public SaveTripResponseDto createTrip(TripRequestDto tripDTO) {
        Agent agent = agentRepository.findById(tripDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + tripDTO.getAgentId()));

        Trip trip = new Trip();
        trip.setDescription(tripDTO.getDescription());
        trip.setOrigin(tripDTO.getOrigin());
        trip.setDestination(tripDTO.getDestination());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setNoOfDays(tripDTO.getNoOfDays());
        trip.setNoOfNights(tripDTO.getNoOfNights());
        trip.setType(tripDTO.getType());
        trip.setCategory(tripDTO.getCategory());
        trip.setNoOfTravelers(tripDTO.getNoOfTravelers());
        trip.setAgent(agent);

        Itinerary itinerary = itineraryService.addItinerary(tripDTO.getItinerary());
        trip.setItinerary(itinerary);

        // Save the Trip entity
        Trip savedTrip = tripRepository.save(trip);

        // Handle Image entities
        if (tripDTO.getImageUrls() != null && !tripDTO.getImageUrls().isEmpty()) {
            List<Image> images = tripDTO.getImageUrls().stream()
                    .map(imageUrl -> {
                        Image image = new Image();
                        image.setUrl(imageUrl);
                        image.setTrip(savedTrip); // Associate the image with the trip
                        return image;
                    }).toList();
            imageRepository.saveAll(images); // Save all images
            savedTrip.setImages(images); // Set the images in the trip
        }

        //handle pickup points
        if (tripDTO.getPickupPoints() != null && !tripDTO.getPickupPoints().isEmpty()) {
            List<PickupPoint> list = tripDTO.getPickupPoints().stream().map(pickupPoint -> {
                PickupPoint pp = new PickupPoint();
                pp.setLocation(pickupPoint.getLocation());
                pp.setPickupTime(pickupPoint.getPickupTime());
                pp.setAdditionalInfo(pickupPoint.getAdditionalInfo());
                pp.setTrip(savedTrip);
                return pp;
            }).toList();
            pickupPointRepository.saveAll(list);
            savedTrip.setPickupPoints(list);
        }

        // Return the DTO with the new Trip ID
        tripDTO.setId(savedTrip.getId());
        SaveTripResponseDto tripResponseDto = new SaveTripResponseDto();
        tripResponseDto.setId(savedTrip.getId());
        tripResponseDto.setDescription(savedTrip.getDescription());
        tripResponseDto.setAgentId(savedTrip.getAgent().getId());
        return tripResponseDto;
    }

    @Transactional
    public TripDto updateTrip(Long tripId, TripDto tripDto) {
        // Find the existing trip by ID
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));

        // Update basic trip details
        trip.setDescription(tripDto.getDescription());
        trip.setOrigin(tripDto.getOrigin());
        trip.setDestination(tripDto.getDestination());
        trip.setStartDate(tripDto.getStartDate());
        trip.setEndDate(tripDto.getEndDate());
        trip.setNoOfDays(tripDto.getNoOfDays());
        trip.setNoOfNights(tripDto.getNoOfNights());
        trip.setType(tripDto.getType());
        trip.setCategory(tripDto.getCategory());
        trip.setNoOfTravelers(tripDto.getNoOfTravelers());
//        trip.setPrice(tripDto.getPrice());

        // Update Agent if necessary
        if (tripDto.getAgentId() != null && !tripDto.getAgentId().equals(trip.getAgent().getId())) {
            Agent agent = agentRepository.findById(tripDto.getAgentId().getId())
                    .orElseThrow(() -> new RuntimeException("Agent not found with id: " + tripDto.getId()));
            trip.setAgent(agent);
        }

        if (tripDto.getImageUrls() != null && !tripDto.getImageUrls().isEmpty()) {
            List<Image> originalImages = trip.getImages();
            List<ImageDto> newImages = tripDto.getImageUrls().stream()
                    .map(imageUrl -> {
                        ImageDto image = new ImageDto();
                        image.setUrl(imageUrl);
                        return image;
                    }).collect(Collectors.toCollection(ArrayList::new));

            originalImages.removeIf(originalImage -> newImages.stream().noneMatch(image -> image.getUrl().equals(originalImage.getUrl())));

            newImages.forEach(image -> {
                boolean anyMatch = originalImages.stream().anyMatch(originalImage -> originalImage.getUrl().equals(image.getUrl()));
                if (!anyMatch) {
                    Image newImage = new Image();
                    newImage.setUrl(image.getUrl());
                    newImage.setTrip(trip);
                    originalImages.add(newImage);
                }
            });
            imageRepository.saveAll(originalImages);
            trip.setImages(originalImages);
        }

        // Update itinerary
        if (tripDto.getItinerary() != null) {
            Itinerary updatedItinerary = itineraryService.updateItinerary(trip.getItinerary().getId(), tripDto.getItinerary());
            trip.setItinerary(updatedItinerary);
        }

        // Save the updated trip
        Trip updatedTrip = tripRepository.save(trip);

        // Return the updated TripDto
        tripDto.setId(updatedTrip.getId());
        return tripDto;
    }

    public TripDto getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        return getTripDto(trip);
    }

    private TripDto getTripDto(Trip trip) {
        Agent agent = agentRepository.findById(trip.getAgent().getId()).orElseThrow(() -> new RuntimeException("Agent not found with id: " + trip.getAgent().getId()));
        Itinerary itinerary = itineraryRepository.findById(trip.getItinerary().getId()).orElseThrow(() -> new RuntimeException("Itinerary not found with id: " + trip.getId()));
        AgentDto agentDto = null;

        if (agent != null) {
            agentDto = getAgentDto(agent);
        }

        ItineraryDto itineraryDto = null;
        if (itinerary != null) {
            itineraryDto = getItineraryDto(itinerary);
        }

        return mapToDTO(trip, agentDto, itineraryDto);
    }

    private static ItineraryDto getItineraryDto(Itinerary itinerary) {
        ItineraryDto itineraryDto = new ItineraryDto();
        itineraryDto.setId(itinerary.getId());
        itineraryDto.setOverview(itinerary.getOverview());

        if (itinerary.getActivities() != null) {
            ArrayList<ActivityDto> activityDtos = getActivityDtos(itinerary);
            itineraryDto.setActivities(activityDtos);
        } else {
            itineraryDto.setActivities(new ArrayList<>());
        }

        if (itinerary.getSchedules() != null) {
            ArrayList<ScheduleDto> scheduleDtos = getScheduleDtos(itinerary);
            itineraryDto.setSchedules(scheduleDtos);
        } else {
            itineraryDto.setSchedules(new ArrayList<>());
        }
        return itineraryDto;
    }

    private static ArrayList<ScheduleDto> getScheduleDtos(Itinerary itinerary) {
        return itinerary.getSchedules()
                .stream()
                .map(schedule -> {
                    ScheduleDto scheduleDto = new ScheduleDto();
                    scheduleDto.setId(schedule.getId());
                    scheduleDto.setDay(schedule.getDay());

                    if (schedule.getStay() != null) {
                        StayDto stayDto = new StayDto();
                        stayDto.setId(schedule.getStay().getId());
                        stayDto.setHotelName(schedule.getStay().getHotelName());
                        stayDto.setAddress(schedule.getStay().getAddress());
                        stayDto.setCheckIn(schedule.getStay().getCheckIn());
                        stayDto.setCheckOut(schedule.getStay().getCheckOut());
                        stayDto.setIsBreakfastIncluded(schedule.getStay().getIsBreakfastIncluded());
                        stayDto.setIsLunchIncluded(schedule.getStay().getIsLunchIncluded());
                        stayDto.setIsDinnerIncluded(schedule.getStay().getIsDinnerIncluded());
                        stayDto.setRating(schedule.getStay().getRating());

                        if (schedule.getStay().getRoomTypes() != null && !schedule.getStay().getRoomTypes().isEmpty()) {
                            ArrayList<RoomTypeDto> roomTypeDtos = schedule.getStay().getRoomTypes().stream().map(roomType -> {
                                RoomTypeDto roomTypeDto = new RoomTypeDto();
                                roomTypeDto.setId(roomType.getId());
                                roomTypeDto.setType(roomType.getType());
                                roomTypeDto.setPrice(String.valueOf(roomType.getPrice()));
                                return roomTypeDto;
                            }).collect(Collectors.toCollection(ArrayList::new));
                            stayDto.setRoomTypes(roomTypeDtos);
                        }
                        scheduleDto.setStay(stayDto);
                    }
                    scheduleDto.setLocation(schedule.getLocation());
                    return scheduleDto;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<ActivityDto> getActivityDtos(Itinerary itinerary) {
        return itinerary.getActivities().stream().map(activity -> {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setId(activity.getId());
            activityDto.setName(activity.getName());
            activityDto.setDescription(activity.getDescription());
            return activityDto;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private static AgentDto getAgentDto(Agent agent) {
        AgentDto agentDto = new AgentDto();
        agentDto.setId(agent.getId());
        agentDto.setAddress(agent.getAddress());
        agentDto.setCompany(agent.getCompany());
        agentDto.setContact(agent.getContact());
        agentDto.setFoundOn(agent.getFoundOn());
        agentDto.setGovtId(agent.getGovtId());
        agentDto.setGovtIdType(agent.getGovtIdType());
        agentDto.setGstNumber(agent.getGstNumber());
        agentDto.setPolicyDocument(agent.getPolicyDocument());
        return agentDto;
    }


    private TripDto mapToDTO(Trip trip, AgentDto agent, ItineraryDto itineraryDto) {
        TripDto tripDTO = new TripDto();
        tripDTO.setId(trip.getId());
        tripDTO.setDescription(trip.getDescription());
        tripDTO.setOrigin(trip.getOrigin());
        tripDTO.setDestination(trip.getDestination());
        tripDTO.setStartDate(trip.getStartDate());
        tripDTO.setEndDate(trip.getEndDate());
        tripDTO.setNoOfDays(trip.getNoOfDays());
        tripDTO.setNoOfNights(trip.getNoOfNights());
        tripDTO.setType(trip.getType());
        tripDTO.setCategory(trip.getCategory());
        tripDTO.setNoOfTravelers(trip.getNoOfTravelers());
        tripDTO.setPickupPoints(trip.getPickupPoints().stream().map((pickupPoint -> {
            PickupPointDto pickupPointDto = new PickupPointDto();
            pickupPointDto.setId(pickupPoint.getId());
            pickupPointDto.setLocation(pickupPoint.getLocation());
            pickupPointDto.setPickupTime(pickupPoint.getPickupTime());
            pickupPointDto.setAdditionalInfo(pickupPoint.getAdditionalInfo());
            return pickupPointDto;
        })).toList());
        tripDTO.setImageUrls(trip.getImages().stream()
                .map(Image::getUrl)
                .toList());
        if (agent != null) {
            tripDTO.setAgentId(agent);
        }
        if (itineraryDto != null) {
            tripDTO.setItinerary(itineraryDto);
        }
        return tripDTO;
    }

    public List<TripDto> getTripByAgent(Long agentId) {
        List<Trip> trips = tripRepository.findByAgentId(agentId);
        return trips.stream().map(this::getTripDto).toList();
    }

    @Transactional
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new AppException("Trip not found with id: " + tripId, HttpStatus.NOT_FOUND));

        // Break associations to prevent PropertyValueException
        if (trip.getItinerary() != null) {
            Itinerary itinerary = trip.getItinerary();

            if (itinerary.getSchedules() != null) {
                for (Schedule schedule : itinerary.getSchedules()) {
                    if (schedule.getStay() != null) {
                        Stay stay = schedule.getStay();
                        stay.setSchedule(null); // Break association
                        schedule.setStay(null); // Break association
                        stayRepository.save(stay); // Save the updated Stay
                        scheduleRepository.save(schedule); // Save the updated Schedule
                    }
                }
            }
        }

        tripRepository.delete(trip);
    }
}

//402 - trip
//402 - itinerary
//1702 - activity1, 1703 - activity2
//1302 - schedule1, 1303 - schedule2
