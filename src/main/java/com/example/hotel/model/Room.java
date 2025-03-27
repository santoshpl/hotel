package com.example.hotel.model;

public class Room {
    private int id;
    private String type;
    private double price;
    private boolean availability;

    public Room() {}

    public Room(int roomId, String roomType, double roomPrice, boolean availability) {
        this.id = roomId;
        this.type = roomType;
        this.price = roomPrice;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int roomId) {
        this.id = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String roomType) {
        this.type = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double roomPrice) {
        this.price = roomPrice;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
