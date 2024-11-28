package org.tragoit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tragoit.dto.ActivityDto;
import org.tragoit.dto.ItineraryDto;
import org.tragoit.dto.ScheduleDto;
import org.tragoit.dto.StayDto;
import org.tragoit.model.Activity;
import org.tragoit.model.Itinerary;
import org.tragoit.model.RoomType;
import org.tragoit.model.Schedule;
import org.tragoit.model.Stay;
import org.tragoit.repository.ActivityRepository;
import org.tragoit.repository.ItineraryRepository;
import org.tragoit.repository.RoomTypeRepository;
import org.tragoit.repository.ScheduleRepository;
import org.tragoit.repository.StayRepository;
import org.tragoit.repository.TripRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public Itinerary addItinerary(ItineraryDto itineraryDTO) {
        Itinerary itinerary = new Itinerary();
        itinerary.setOverview(itineraryDTO.getOverview());

        // Save Itinerary first
        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        // Map and save activities
        if (itineraryDTO.getActivities() != null && !itineraryDTO.getActivities().isEmpty()) {
            List<Activity> activities = getActivities(itineraryDTO, savedItinerary);
            savedItinerary.setActivities(activities);
        }

        // Map and save schedules
        if (itineraryDTO.getSchedules() != null && !itineraryDTO.getSchedules().isEmpty()) {
            List<Schedule> schedules = getSchedules(itineraryDTO, savedItinerary);
            savedItinerary.setSchedules(schedules);
        }
        return savedItinerary;
    }

    private List<Schedule> getSchedules(ItineraryDto itineraryDTO, Itinerary savedItinerary) {
        List<Schedule> schedules = itineraryDTO.getSchedules().stream()
                .map(scheduleDto -> {
                    Schedule schedule = new Schedule();
                    schedule.setDay(scheduleDto.getDay());
                    schedule.setLocation(scheduleDto.getLocation());
                    schedule.setItinerary(savedItinerary);

                    Schedule savedSchedule = scheduleRepository.save(schedule);

                    // Handle Stay entity
                    if (scheduleDto.getStay() != null) {
                        Stay stay = new Stay();
                        stay.setHotelName(scheduleDto.getStay().getHotelName());
                        stay.setAddress(scheduleDto.getStay().getAddress());
                        stay.setCheckIn(scheduleDto.getStay().getCheckIn());
                        stay.setCheckOut(scheduleDto.getStay().getCheckOut());
                        stay.setIsBreakfastIncluded(scheduleDto.getStay().getIsBreakfastIncluded());
                        stay.setIsLunchIncluded(scheduleDto.getStay().getIsLunchIncluded());
                        stay.setIsDinnerIncluded(scheduleDto.getStay().getIsDinnerIncluded());
                        stay.setRating(scheduleDto.getStay().getRating());

                        // Save Stay entity first
                        stay.setSchedule(savedSchedule);
                        Stay savedStay = stayRepository.save(stay);

                        if (scheduleDto.getStay().getRoomTypes() != null && !scheduleDto.getStay().getRoomTypes().isEmpty()) {
                            List<RoomType> roomTypes = getRoomTypes(scheduleDto, savedStay);
                            savedStay.setRoomTypes(roomTypes);
                        }
                        // Save Stay entity
                        savedSchedule.setStay(savedStay);
                        scheduleRepository.save(savedSchedule);
                    }
                    return savedSchedule;
                }).toList();
        scheduleRepository.saveAll(schedules);
        return schedules;
    }

    private List<RoomType> getRoomTypes(ScheduleDto scheduleDto, Stay savedStay) {
        List<RoomType> roomTypes = scheduleDto.getStay().getRoomTypes().stream().map(roomTypeDto -> {
            RoomType roomType = new RoomType();
            roomType.setType(roomTypeDto.getType());
            roomType.setPrice(Double.valueOf(roomTypeDto.getPrice()));
            roomType.setStay(savedStay);
            return roomType;
        }).collect(Collectors.toList());
        roomTypeRepository.saveAll(roomTypes);
        return roomTypes;
    }

    private List<Activity> getActivities(ItineraryDto itineraryDTO, Itinerary savedItinerary) {
        List<Activity> activities = itineraryDTO.getActivities().stream()
                .map(activityDTO -> {
                    Activity activity = new Activity();
                    activity.setDescription(activityDTO.getDescription());
                    activity.setItinerary(savedItinerary);
                    return activity;
                }).toList();
        activityRepository.saveAll(activities);
        return activities;
    }

    public Itinerary updateItinerary(Long itineraryId, ItineraryDto itineraryDto) {
        // Find the existing itinerary by ID
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found with id: " + itineraryId));

        // Update the itinerary overview
        itinerary.setOverview(itineraryDto.getOverview());

        // Update activities
        updateActivities(itinerary, itineraryDto.getActivities());

        // Update schedules and stay information
//        updateSchedules(itinerary, itineraryDto.getSchedules());

        // Save updated itinerary
        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        return savedItinerary;
    }

    private void updateActivities(Itinerary itinerary, List<ActivityDto> newActivityDtos) {
        List<Activity> currentActivities = itinerary.getActivities();

        // Remove activities that are no longer in the DTO
        currentActivities.removeIf(activity ->
                newActivityDtos.stream().noneMatch(dto -> dto.getId() != null && dto.getId().equals(activity.getId())));

        // Add or update existing activities
        for (ActivityDto activityDto : newActivityDtos) {
            Activity activity;
            if (activityDto.getId() != null) {
                // Find existing activity and update it
                activity = currentActivities.stream()
                        .filter(a -> a.getId().equals(activityDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityDto.getId()));
                activity.setDescription(activityDto.getDescription());
            } else {
                // New activity
                activity = new Activity();
                activity.setDescription(activityDto.getDescription());
                activity.setItinerary(itinerary);
                currentActivities.add(activity);
            }
        }
    }

    private void updateSchedules(Itinerary itinerary, List<ScheduleDto> newScheduleDtos) {
        List<Schedule> currentSchedules = itinerary.getSchedules();

        // Remove schedules that are no longer in the DTO
        currentSchedules.removeIf(schedule ->
                newScheduleDtos.stream().noneMatch(dto -> dto.getId() != null && dto.getId().equals(schedule.getId())));

        // Add or update existing schedules
        for (ScheduleDto scheduleDto : newScheduleDtos) {
            Schedule schedule;
            if (scheduleDto.getId() != null) {
                // Find existing schedule and update it
                schedule = currentSchedules.stream()
                        .filter(s -> s.getId().equals(scheduleDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleDto.getId()));
                schedule.setDay(scheduleDto.getDay());
                schedule.setLocation(scheduleDto.getLocation());
            } else {
                // New schedule
                schedule = new Schedule();
                schedule.setDay(scheduleDto.getDay());
                schedule.setLocation(scheduleDto.getLocation());
                schedule.setItinerary(itinerary);
                currentSchedules.add(schedule);
            }

            // Save the schedule first (without the stay)
            schedule = scheduleRepository.save(schedule);

            // Handle the Stay entity after Schedule is saved
            if (scheduleDto.getStay() != null) {
                Stay stay;
                if (schedule.getStay() != null) {
                    stay = schedule.getStay();
                } else {
                    stay = new Stay();
                }

                stay.setHotelName(scheduleDto.getStay().getHotelName());
                stay.setAddress(scheduleDto.getStay().getAddress());
                stay.setCheckIn(scheduleDto.getStay().getCheckIn());
                stay.setCheckOut(scheduleDto.getStay().getCheckOut());
                stay.setIsBreakfastIncluded(scheduleDto.getStay().getIsBreakfastIncluded());
                stay.setIsLunchIncluded(scheduleDto.getStay().getIsLunchIncluded());
                stay.setIsDinnerIncluded(scheduleDto.getStay().getIsDinnerIncluded());
                stay.setRating(scheduleDto.getStay().getRating());

                // Associate the stay with the schedule now that the schedule is persisted
//                stay.setSchedule(schedule);
                stay = stayRepository.save(stay);

                // Update the schedule with the associated stay
                schedule.setStay(stay);
                scheduleRepository.save(schedule); // Save the schedule again with the stay
            }
        }

        itinerary.setSchedules(currentSchedules);
    }


    private void updateStay(Schedule schedule, StayDto stayDto) {
        if (stayDto != null) {
            Stay stay;
            if (schedule.getStay() != null) {
                stay = schedule.getStay(); // Update existing stay
            } else {
                stay = new Stay(); // Create a new stay if one doesn't exist
//                stay.setSchedule(schedule);
            }
            stay.setHotelName(stayDto.getHotelName());
            stay.setAddress(stayDto.getAddress());
            stay.setCheckIn(stayDto.getCheckIn());
            stay.setCheckOut(stayDto.getCheckOut());
            stay.setIsBreakfastIncluded(stayDto.getIsBreakfastIncluded());
            stay.setIsLunchIncluded(stayDto.getIsLunchIncluded());
            stay.setIsDinnerIncluded(stayDto.getIsDinnerIncluded());
            stay.setRating(stayDto.getRating());

            // Ensure the stay is associated with the schedule
            schedule.setStay(stay);
        }
    }
}

//trip - 302
// itinary - 802
// activity - 2102, 2103
// schedule - 1602 - stay - 1502
// schedule - 1603 - stay - 1503


