package com.chatop.dto;

import lombok.Data;

@Data
public class RentalDTO {
    private Long id;
    private String name;
    private int surface;
    private int price;
    private String picture;
    private String description;
    private Long owner_id;
    private String created_at;
    private String updated_at;

    public RentalDTO(Long id, String name, int surface, int price, String picture, String description, Long owner_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner_id = owner_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
