package org.tragoit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tragoit.dto.ItineraryDto;
import org.tragoit.service.ItineraryService;

@RestController
@RequestMapping("/api/v1/itineraries")

public class ItineraryController {
    @Autowired
    private ItineraryService itineraryService;

//    @PostMapping
//    public ResponseEntity<ItineraryDto> addItinerary(@RequestBody ItineraryDto itineraryDTO) {
//        ItineraryDto createdItinerary = itineraryService.addItinerary(itineraryDTO);
//        return new ResponseEntity<>(createdItinerary, HttpStatus.CREATED);
//    }
}
