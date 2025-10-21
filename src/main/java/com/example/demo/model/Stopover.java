package com.example.demo.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "stopovers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Stopover
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Flight flight;

    @ManyToOne
    @NotNull
    private Airport airport;

    @NotNull
    private LocalDateTime stopoverTime;
}