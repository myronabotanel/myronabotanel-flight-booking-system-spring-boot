package com.example.demo.service.impl;

import com.example.demo.model.Stopover;
import com.example.demo.repository.StopoverRepository;
import com.example.demo.service.StopoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StopoverServiceImpl implements StopoverService {

    private final StopoverRepository stopoverRepository;

    @Autowired
    public StopoverServiceImpl(StopoverRepository stopoverRepository) {
        this.stopoverRepository = stopoverRepository;
    }

    @Override
    public List<Stopover> getAllStopovers() {
        return stopoverRepository.findAll();  // Returnează toate stopoverele din DB
    }

    @Override
    public Stopover getStopoverById(Long id) {
        return stopoverRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Stopover with id " + id + " not found"));
    }

    @Override
    public Stopover addStopover(Stopover stopover) {
        return stopoverRepository.save(stopover);  // Adaugă stopoverul în DB
    }

    @Override
    public Stopover updateStopover(Stopover stopover) {
        return stopoverRepository.save(stopover);  // Actualizează stopoverul în DB
    }

    @Override
    public void deleteStopover(Long id) {
        stopoverRepository.deleteById(id);  // Șterge stopoverul din DB
    }
}
