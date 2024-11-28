package org.tragoit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pickup_points")
@Getter
@Setter
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pickup_point_id")
    private Long id;

    @Column(name = "location")
    private String location; // Location of the pickup point

    @Column(name = "pickup_time")
    private String pickupTime; // Time of the pickup

    @Column(name = "additional_info")
    private String additionalInfo; // Any extra details about the pickup point

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // Reference to the Trip
}
