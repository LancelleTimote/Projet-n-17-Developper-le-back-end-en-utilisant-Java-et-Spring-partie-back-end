package com.chatop.service;

import com.chatop.dto.RentalDTO;
import com.chatop.model.Rental;
import com.chatop.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public RentalDTO getRentalDTOById(Long id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        return rental != null ? convertToDTO(rental) : null;
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Long id, Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }

    private RentalDTO convertToDTO(Rental rental) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return new RentalDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt() != null ? sdf.format(rental.getCreatedAt()) : null,
                rental.getUpdatedAt() != null ? sdf.format(rental.getUpdatedAt()) : null
        );
    }
}

