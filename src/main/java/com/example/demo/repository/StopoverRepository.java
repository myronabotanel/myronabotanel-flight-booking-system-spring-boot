package com.example.demo.repository;

import com.example.demo.model.Stopover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopoverRepository extends JpaRepository<Stopover, Long> {
}
