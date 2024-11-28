package org.tragoit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "agent")
public class Agent{
    @Id
    private Long id;  // This will use the same ID as the existing User

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company")
    private String company;

    @Column(name = "contact")
    private String contact;

    @Column(name = "address")
    private String address;

    @Column(name = "found_on")
    private String foundOn;

    @Column(name = "govt_id_type")
    private String govtIdType;

    @Column(name = "govt_id")
    private String govtId;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "policy_document")
    private String policyDocument;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;
}
