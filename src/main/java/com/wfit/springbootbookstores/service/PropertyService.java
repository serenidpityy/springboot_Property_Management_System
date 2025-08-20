package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.Property;
import com.wfit.springbootbookstores.entity.User;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
    Property saveProperty(Property property);
    Optional<Property> getPropertyById(Integer id);
    void deleteProperty(Integer id);
    List<Property> getAllProperties();
    List<Property> getPropertiesByOwner(User owner);
    Optional<Property> getPropertyByHouseNumber(String houseNumber);
}
