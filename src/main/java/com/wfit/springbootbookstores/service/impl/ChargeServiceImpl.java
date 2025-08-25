package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.Charge;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.ChargeRepository;
import com.wfit.springbootbookstores.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public Charge saveCharge(Charge charge) {
        // 如果是新记录且发布时间为空，则设置发布时间为当前时间
        if (charge.getId() == null && charge.getPublishTime() == null) {
            charge.setPublishTime(LocalDateTime.now());
        }
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
        // 如果状态改为已缴费，设置缴费时间
        if (status == Charge.PaymentStatus.PAID && charge.getPaymentTime() == null) {
            charge.setPaymentTime(LocalDateTime.now());
        }
        return chargeRepository.save(charge);
    }
}
