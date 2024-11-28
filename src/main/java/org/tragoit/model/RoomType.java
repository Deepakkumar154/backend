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
@Table(name = "room_types")
@Getter
@Setter
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_type_id")
    private Long id;

    @Column(name = "type")
    private String type; // e.g., "Quad Share", "Double Share", "Individual Room"

    @Column(name = "price")
    private Double price; // Price for the trip based on this room type

    @ManyToOne
    @JoinColumn(name = "stay_id", nullable = false)
    private Stay stay;
}
