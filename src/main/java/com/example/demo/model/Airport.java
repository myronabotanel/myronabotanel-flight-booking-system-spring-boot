package com.example.demo.model;


import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "airports")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Airport
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private String country;

    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> departures;

    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> arrivals;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stopover> stopovers;
}