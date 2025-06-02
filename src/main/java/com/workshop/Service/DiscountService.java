package com.workshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Discount;
import com.workshop.Repo.DiscoutRepo;

@Service
public class DiscountService {
    
    @Autowired
    private DiscoutRepo discoutRepo;

    public Discount createDiscount(Discount discount){
        return this.discoutRepo.save(discount);
    }

    public List<Discount> getAllCouponCode(){
        return this.discoutRepo.findAll();
    }

    public Discount getCouponCodeById(int id){
        return this.discoutRepo.findById(id).get();
    }

    public Discount updateCouponCode(Discount discount, int id){
        Discount existing = this.discoutRepo.findById(id).get();
        existing.setCouponCode(discount.getCouponCode());
        existing.setPriceDiscount(discount.getPriceDiscount());
        existing.setIsEnabled(discount.getIsEnabled());
        return this.discoutRepo.save(existing);
    }

    public void deleteCouponCode(int id){
        this.discoutRepo.deleteById(id);
    }
}
