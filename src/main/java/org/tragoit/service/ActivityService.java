package org.tragoit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tragoit.dto.ActivityDto;
import org.tragoit.model.Activity;
import org.tragoit.model.Itinerary;
import org.tragoit.repository.ActivityRepository;
import org.tragoit.repository.ItineraryRepository;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    public ActivityDto addActivity(ActivityDto activityDTO) {
        Itinerary itinerary = itineraryRepository.findById(activityDTO.getItineraryId())
                .orElseThrow(() -> new RuntimeException("Itinerary not found with id: " + activityDTO.getItineraryId()));

        Activity activity = new Activity();
        activity.setName(activityDTO.getName());
        activity.setDescription(activityDTO.getDescription());

        activity = activityRepository.save(activity);

        activityDTO.setId(activity.getId());
        return activityDTO;
    }
}

