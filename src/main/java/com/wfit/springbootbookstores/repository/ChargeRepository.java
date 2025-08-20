package com.wfit.springbootbookstores.repository;

import com.wfit.springbootbookstores.entity.Charge;
import com.wfit.springbootbookstores.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {
    List<Charge> findByOwner(User owner);
}
