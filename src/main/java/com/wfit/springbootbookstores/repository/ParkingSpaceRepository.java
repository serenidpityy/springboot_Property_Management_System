package com.wfit.springbootbookstores.repository;

import com.wfit.springbootbookstores.entity.ParkingSpace;
import com.wfit.springbootbookstores.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {
    Optional<ParkingSpace> findByParkingId(String parkingId);
    List<ParkingSpace> findByOwner(User owner);
}
