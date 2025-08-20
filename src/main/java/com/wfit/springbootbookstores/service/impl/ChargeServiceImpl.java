package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.Charge;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.ChargeRepository;
import com.wfit.springbootbookstores.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public Charge saveCharge(Charge charge) {
        return chargeRepository.save(charge);
    }

    @Override
    public Optional<Charge> getChargeById(Integer id) {
        return chargeRepository.findById(id);
    }

    @Override
    public void deleteCharge(Integer id) {
        chargeRepository.deleteById(id);
    }

    @Override
    public List<Charge> getAllCharges() {
        return chargeRepository.findAll();
    }

    @Override
    public List<Charge> getChargesByOwner(User owner) {
        return chargeRepository.findByOwner(owner);
    }

    @Override
    public Charge updatePaymentStatus(Integer chargeId, Charge.PaymentStatus status) {
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new RuntimeException("Charge not found"));
        charge.setPaymentStatus(status);
        return chargeRepository.save(charge);
    }
}
