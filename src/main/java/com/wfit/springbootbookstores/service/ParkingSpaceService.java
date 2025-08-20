package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.ParkingSpace;
import com.wfit.springbootbookstores.entity.User;

import java.util.List;
import java.util.Optional;

public interface ParkingSpaceService {
    ParkingSpace saveParkingSpace(ParkingSpace parkingSpace);
    Optional<ParkingSpace> getParkingSpaceById(Integer id);
    void deleteParkingSpace(Integer id);
    List<ParkingSpace> getAllParkingSpaces();
    List<ParkingSpace> getParkingSpacesByOwner(User owner);
    Optional<ParkingSpace> getParkingSpaceByParkingId(String parkingId);
}
