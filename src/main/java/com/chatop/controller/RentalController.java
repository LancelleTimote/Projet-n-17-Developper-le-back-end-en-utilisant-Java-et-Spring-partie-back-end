package com.chatop.controller;

import com.chatop.dto.RentalDTO;
import com.chatop.model.CustomUserDetails;
import com.chatop.model.Rental;
import com.chatop.service.FileStorageService;
import com.chatop.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.chatop.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<?> getAllRentals() {
        System.out.println("Received request to get all rentals");

        List<RentalDTO> rentalDTOs = rentalService.getAllRentals();
        System.out.println("Number of rentals found: " + rentalDTOs.size());

        rentalDTOs.forEach(rentalDTO -> {
            System.out.println("Rental ID: " + rentalDTO.getId());
            System.out.println("Rental Picture URL: " + rentalDTO.getPicture());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("rentals", rentalDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        System.out.println("Received request to get rental by ID: " + id);

        RentalDTO rentalDTO = rentalService.getRentalDTOById(id);
        if (rentalDTO == null) {
            System.out.println("Rental not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
        System.out.println("Rental found with ID: " + id + ", Picture URL: " + rentalDTO.getPicture());
        return ResponseEntity.ok(rentalDTO);
    }

    @PostMapping
    public ResponseEntity<?> createRental(
            @RequestParam("picture") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("surface") Integer surface,
            @RequestParam("price") Integer price,
            @RequestParam("description") String description) {

        System.out.println("Received request to create rental");

        if (file.isEmpty()) {
            System.out.println("Failed to create rental: File is empty");
            return ResponseEntity.badRequest().body("File is empty");
        }

        String picturePath;
        try {
            picturePath = fileStorageService.storeFile(file);
            System.out.println("File stored successfully: " + picturePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while storing file: " + e.getMessage());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        System.out.println("Authenticated user: " + currentUser.getEmail());

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setPicture(picturePath);
        rental.setOwner(currentUser);

        try {
            Rental createdRental = rentalService.createRental(rental);
            RentalDTO createdRentalDTO = convertToDTO(createdRental);
            System.out.println("Rental created successfully: " + createdRental.getId() + ", Picture URL: " + createdRentalDTO.getPicture());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRentalDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating rental: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surface", required = false) Integer surface,
            @RequestParam(value = "price", required = false) Integer price,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        System.out.println("Received request to update rental with ID: " + id);

        Rental rental = rentalService.getRentalById(id);
        if (rental == null) {
            System.out.println("Rental not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }

        if (file != null && !file.isEmpty()) {
            String picturePath = fileStorageService.storeFile(file);
            rental.setPicture(picturePath);
            System.out.println("Updated rental picture path: " + picturePath);
        }
        if (name != null) rental.setName(name);
        if (surface != null) rental.setSurface(surface);
        if (price != null) rental.setPrice(price);
        if (description != null) rental.setDescription(description);

        Rental updatedRental = rentalService.updateRental(id, rental);
        RentalDTO updatedRentalDTO = convertToDTO(updatedRental);
        System.out.println("Rental updated successfully with ID: " + updatedRental.getId());
        return ResponseEntity.ok(updatedRentalDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        System.out.println("Received request to delete rental with ID: " + id);
        rentalService.deleteRental(id);
        System.out.println("Rental deleted successfully with ID: " + id);
        return ResponseEntity.ok().build();
    }

    private RentalDTO convertToDTO(Rental rental) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String pictureUrl = "http://localhost:3001/uploads/" + rental.getPicture();

        System.out.println("Converting Rental to RentalDTO, Picture URL: " + pictureUrl);

        return new RentalDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                pictureUrl,
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt() != null ? sdf.format(rental.getCreatedAt()) : null,
                rental.getUpdatedAt() != null ? sdf.format(rental.getUpdatedAt()) : null
        );
    }
}
