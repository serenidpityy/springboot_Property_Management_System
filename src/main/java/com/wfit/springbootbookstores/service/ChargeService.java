package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.Charge;
import com.wfit.springbootbookstores.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChargeService {
    Charge saveCharge(Charge charge);
    Optional<Charge> getChargeById(Integer id);
    void deleteCharge(Integer id);
    List<Charge> getAllCharges();
    List<Charge> getChargesByOwner(User owner);
    Charge updatePaymentStatus(Integer chargeId, Charge.PaymentStatus status);
}
