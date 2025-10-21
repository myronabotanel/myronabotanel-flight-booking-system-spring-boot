package com.example.demo.DTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopoverDTO {
    private Long id;
    private Long flightId;
    private AirportDTO airport;
    private LocalDateTime stopoverTime;
}