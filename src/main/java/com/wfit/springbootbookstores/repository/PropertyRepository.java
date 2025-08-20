package com.wfit.springbootbookstores.repository;

import com.wfit.springbootbookstores.entity.Property;
import com.wfit.springbootbookstores.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    Optional<Property> findByHouseNumber(String houseNumber);
    List<Property> findByOwner(User owner);
}
