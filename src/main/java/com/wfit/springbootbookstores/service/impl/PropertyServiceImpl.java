package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.Property;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.PropertyRepository;
import com.wfit.springbootbookstores.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Property saveProperty(Property property) {
        // You might want to add business logic here, e.g., check for duplicate house numbers
        return propertyRepository.save(property);
    }

    @Override
    public Optional<Property> getPropertyById(Integer id) {
        return propertyRepository.findById(id);
    }

    @Override
    public void deleteProperty(Integer id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> getPropertiesByOwner(User owner) {
        return propertyRepository.findByOwner(owner);
    }

    @Override
    public Optional<Property> getPropertyByHouseNumber(String houseNumber) {
        return propertyRepository.findByHouseNumber(houseNumber);
    }
}
