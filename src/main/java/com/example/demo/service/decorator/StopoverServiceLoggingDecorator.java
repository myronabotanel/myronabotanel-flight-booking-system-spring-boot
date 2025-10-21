package com.example.demo.service.decorator;

import com.example.demo.model.Stopover;
import com.example.demo.service.StopoverService;

import java.util.List;

public class StopoverServiceLoggingDecorator implements StopoverService {

    private final StopoverService decorated;

    public StopoverServiceLoggingDecorator(StopoverService decorated) {
        this.decorated = decorated;
    }

    @Override
    public List<Stopover> getAllStopovers() {
        System.out.println("[LOG] Fetching all stopovers");
        return decorated.getAllStopovers();
    }

    @Override
    public Stopover getStopoverById(Long id) {
        System.out.println("[LOG] Fetching stopover by id: " + id);
        return decorated.getStopoverById(id);
    }

    @Override
    public Stopover addStopover(Stopover stopover) {
        System.out.println("[LOG] Adding stopover: " + stopover);
        Stopover result = decorated.addStopover(stopover);
        System.out.println("[LOG] Stopover added with id: " + result.getId());
        return result;
    }

    @Override
    public Stopover updateStopover(Stopover stopover) {
        System.out.println("[LOG] Updating stopover with id: " + stopover.getId());
        return decorated.updateStopover(stopover);
    }

    @Override
    public void deleteStopover(Long id) {
        System.out.println("[LOG] Deleting stopover with id: " + id);
        decorated.deleteStopover(id);
        System.out.println("[LOG] Stopover deleted with id: " + id);
    }
}
