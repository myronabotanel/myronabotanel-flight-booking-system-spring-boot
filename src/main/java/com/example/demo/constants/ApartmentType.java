package com.example.demo.constants;

import java.util.List;

public enum ApartmentType {
    LOFT,
    STUDIO,
    PENTHOUSE;

    private static final List<ApartmentType> VALUES = List.of(values());
}