package com.chatop.controller;

import com.chatop.dto.MessageDTO;
import com.chatop.model.Message;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.service.MessageService;
import com.chatop.service.UserService;
import com.chatop.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody MessageDTO messageDTO) {
        System.out.println("Received messageDTO: " + messageDTO);

        if (messageDTO.getUserId() == null || messageDTO.getRentalId() == null) {
            System.out.println("User ID or Rental ID is null");
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUserById(messageDTO.getUserId());
        Rental rental = rentalService.getRentalById(messageDTO.getRentalId());

        if (user == null || rental == null) {
            System.out.println("User or Rental not found");
            return ResponseEntity.badRequest().build();
        }

        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setUser(user);
        message.setRental(rental);

        Message createdMessage = messageService.createMessage(message);
        System.out.println("Created message: " + createdMessage);

        return ResponseEntity.ok(createdMessage);
    }
}
