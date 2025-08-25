package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.ParkingSpace;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.ParkingSpaceRepository;
import com.wfit.springbootbookstores.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public ParkingSpace saveParkingSpace(ParkingSpace parkingSpace) {
        // You might want to add business logic here, e.g., check for duplicate parking IDs
        // 设置分配时间为当前时间
        parkingSpace.setAllocationTime(LocalDateTime.now());
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public Optional<ParkingSpace> getParkingSpaceById(Integer id) {
        return parkingSpaceRepository.findById(id);
    }

    @Override
    public void deleteParkingSpace(Integer id) {
        parkingSpaceRepository.deleteById(id);
    }

    @Override
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }

    @Override
    public List<ParkingSpace> getParkingSpacesByOwner(User owner) {
        return parkingSpaceRepository.findByOwner(owner);
    }

    @Override
    public Optional<ParkingSpace> getParkingSpaceByParkingId(String parkingId) {
        return parkingSpaceRepository.findByParkingId(parkingId);
    }
}
