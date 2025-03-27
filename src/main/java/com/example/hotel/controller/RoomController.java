package com.example.hotel.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hotel.model.Room;

@Controller
public class RoomController {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    // Method to get a connection to the database
    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Method to list all room in the rooms
    @GetMapping("/rooms")
    public String index(Model model) {
        List<Room> rooms = getAllRooms();
        model.addAttribute("room", new Room());
        model.addAttribute("rooms", rooms);
        return "newIndex";  // Corresponds to index.html
    }

    // Method to show the form for adding a new room and return the add-room.html template
    @GetMapping("/add-room")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-room";  // Corresponds to add-room.html
    }

    // Method to handle adding a new room and return the add-room.html template
    @PostMapping("/add-room")
    public String addRoom(@ModelAttribute Room room, Model model) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO room (id, type, price, availability) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, room.getId());
            statement.setString(2, room.getType());
            statement.setDouble(3, room.getPrice());
            statement.setBoolean(4, room.isAvailability());

            System.out.println("Room ID: " + room.getId());
            System.out.println("Room Type: " + room.getType());
            System.out.println("Room Price: " + room.getPrice());
            System.out.println("Room Availability: " + room.isAvailability());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "add-room";
    }

    // Method to get all rooms from the database
    private List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM room";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Room room = new Room(
                    results.getInt("id"),
                    results.getString("type"),
                    results.getDouble("price"),
                    results.getBoolean("availability")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
}