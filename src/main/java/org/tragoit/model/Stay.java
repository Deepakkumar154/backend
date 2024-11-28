package org.tragoit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "stay")
@Getter
@Setter
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stay_id")
    private Long id;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "address")
    private String address;

    @Column(name = "check_in")
    private String checkIn;

    @Column(name = "check_out")
    private String checkOut;

    @Column(name = "rating")
    private String rating;

    @Column(name = "is_breakfast_included")
    private Boolean isBreakfastIncluded;

    @Column(name = "is_lunch_included")
    private Boolean isLunchIncluded;

    @Column(name = "is_dinner_included")
    private Boolean isDinnerIncluded;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes;
}

