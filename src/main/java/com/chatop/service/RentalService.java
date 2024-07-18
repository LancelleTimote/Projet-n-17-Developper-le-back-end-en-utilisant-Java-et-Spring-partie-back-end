package com.chatop.service;

import com.chatop.model.Rental;
import com.chatop.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Long id, Rental rentalDetails) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if (rental != null) {
            rental.setName(rentalDetails.getName());
            rental.setSurface(rentalDetails.getSurface());
            rental.setPrice(rentalDetails.getPrice());
            rental.setPicture(rentalDetails.getPicture());
            rental.setDescription(rentalDetails.getDescription());
            rental.setUpdatedAt(rentalDetails.getUpdatedAt());
            return rentalRepository.save(rental);
        }
        return null;
    }

    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }
}
