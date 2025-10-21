package com.example.demo.service;

import com.example.demo.model.Stopover;

import java.util.List;

public interface StopoverService {
    List<Stopover> getAllStopovers();
    Stopover getStopoverById(Long id);
    Stopover addStopover(Stopover stopover);
    Stopover updateStopover(Stopover stopover);
    void deleteStopover(Long id);
}
