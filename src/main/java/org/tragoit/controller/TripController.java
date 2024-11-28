package org.tragoit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tragoit.dto.SaveTripResponseDto;
import org.tragoit.dto.TripDto;
import org.tragoit.dto.TripRequestDto;
import org.tragoit.service.TripService;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("/api/v1/")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

//    @GetMapping("/trips")
//    List<Trip> getAllTrips() {
//        return tripService.getAllTrips();
//    }
//
//    @GetMapping("/trip/{id}")
//    Trip getTripById(@PathVariable Long id) {
//        return tripService.getTripById(id);
//    }
//
//    @GetMapping("/trip/latest")
//    List<Trip> getLatestTrips() {
//        return tripService.getLatestTrips();
//    }
//
//    @GetMapping(value = "/trip", params = "destination")
//    List<Trip> getTripByDestination(@RequestParam(name = "destination") String destination) {
//        return tripService.getTripByDestination(destination);
//    }
//
//    @GetMapping(value = "/trip", params = "category")
//    List<Trip> getTripByCategory(@RequestParam String category) {
//        return tripService.getTripByCategory(category);
//    }
//
//    @GetMapping(value = "/trip", params = "adventure")
//    List<Trip> getTripByAdventure(@RequestParam String adventure) {
//        return tripService.getTripByAdventure(adventure);
//    }

    @PostMapping("/trip")
    public ResponseEntity<SaveTripResponseDto> addTrip(@RequestBody TripRequestDto trip) {
        log.info("Adding trip: {}", trip);
        SaveTripResponseDto tripResponseDto = tripService.createTrip(trip);
        log.info("Added trip");
        return ResponseEntity.ok(tripResponseDto);
    }

    @PutMapping("/trip/{tripId}")
    public ResponseEntity<TripDto> updateTrip(@PathVariable Long tripId, @RequestBody TripDto trip) {
        TripDto tripForAgent = tripService.updateTrip(tripId, trip);

        return ResponseEntity.ok(tripForAgent);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<TripDto> getTripById(@PathVariable Long tripId) {
        log.info("Getting trip by id: {}", tripId);
        TripDto tripForAgent = tripService.getTripById(tripId);

        return ResponseEntity.ok(tripForAgent);
    }

    @GetMapping("/trip/agent/{agentId}")
    public ResponseEntity<List<TripDto>> getAllTrips(@PathVariable Long agentId) {
        log.info("Getting trip: {}", agentId);
        List<TripDto> tripForAgent = tripService.getTripByAgent(agentId);

        return ResponseEntity.ok(tripForAgent);
    }

    @DeleteMapping("/trip/{tripId}")
    public ResponseEntity<String> deleteTrips(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        log.info("Trip deleted successfully");
        return ResponseEntity.ok("Trip deleted successfully");
    }
}
