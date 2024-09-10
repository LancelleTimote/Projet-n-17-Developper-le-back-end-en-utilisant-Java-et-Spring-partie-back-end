package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageDTO {
    private String message;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("rental_id")
    private Long rentalId;
}
