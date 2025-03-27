package com.example.hotel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hotel.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

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
    @GetMapping("/")
    public String index(Model model) {
        List<Booking> rooms = getAllRooms();
        model.addAttribute("rooms", rooms);
        return "index";  // Corresponds to index.html
    }
 
    // Method to show the form for adding a new booking and return the add-booking.html template
    @GetMapping("/add-booking")
    public String showAddForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "add-booking";  // Corresponds to add-booking.html
    }
    
    // Method to handle adding a new booking and return the add-booking.html template
    @PostMapping("/add-booking")
    public String addBooking(@ModelAttribute Booking booking, Model model) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO hotel (roomId, roomType, roomPrice, availability) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, booking.getRoomId());
            statement.setString(2, booking.getRoomType());
            statement.setDouble(3, booking.getRoomPrice());
            statement.setBoolean(4, booking.isAvailability());

            statement.executeUpdate();
            model.addAttribute("success", "Successfully added booking for room: " + booking.getRoomType());
            return "add-booking";
        } catch (SQLException e) {
            model.addAttribute("error", "Error adding booking: " + e.getMessage());
            e.printStackTrace();
            return "add-booking";
        }
    }
 
    // Method to search bookings by room id
    @GetMapping("/search")
    public String searchRooms(@RequestParam int roomId, Model model) {
        List<Booking> rooms = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM hotel WHERE roomId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setRoomId(resultSet.getInt("roomId"));
                booking.setRoomType(resultSet.getString("roomType"));
                booking.setRoomPrice(resultSet.getDouble("roomPrice"));
                booking.setAvailability(resultSet.getBoolean("availability"));
                rooms.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("rooms", rooms);
        return "index";  // Corresponds to index.html
    }

    // Method to show the form for editing a booking and return the edit-booking.html template
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int roomId, Model model) {
        try {
            Booking booking = getBookingById(roomId);
            model.addAttribute("room", booking);
            return "edit-booking";  // Corresponds to edit-booking.html
        } catch (SQLException e) {
            model.addAttribute("error", "Error retrieving booking: " + e.getMessage());
            e.printStackTrace();
            return "index";
        }
    }
 
    // Method to handle editing a booking and return the edit-booking.html template
    @PostMapping("/edit/{id}")
    public String editBooking(@PathVariable int roomId, @ModelAttribute Booking booking, Model model) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE hotel SET roomType = ?, roomPrice = ?, availability = ? WHERE roomId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, booking.getRoomType());
            statement.setDouble(2, booking.getRoomPrice());
            statement.setBoolean(3, booking.isAvailability());
            statement.setInt(4, roomId);

            statement.executeUpdate();

            model.addAttribute("success", "Successfully updated booking for room: " + booking.getRoomType());
            return "edit-booking";  // Corresponds to edit-booking.html
        } catch (SQLException e) {
            model.addAttribute("error", "Error updating booking: " + e.getMessage());
            e.printStackTrace();
            return "edit-booking";  // Return to the same page in case of an error
        }
    }

 
    // Method to show the form for deleting a booking and return the delete-booking.html template
    @GetMapping("/delete/{roomId}")
    public String showDeleteForm(@PathVariable("roomId") int roomId, Model model) {
        try {
            Booking booking = getBookingById(roomId);
            model.addAttribute("room", booking);
            return "delete-booking";  // Corresponds to delete-booking.html
        } catch (SQLException e) {
            model.addAttribute("error", "Error retrieving booking: " + e.getMessage());
            e.printStackTrace();
            return "index";
        }
    }
 
    // Method to handle deleting a booking and return the delete-booking.html template
    @PostMapping("/delete/{roomId}")
    public String deleteBooking(@PathVariable("roomId") int roomId, Model model, RedirectAttributes redirectAttributes) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM hotel WHERE roomId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, roomId);
            statement.executeUpdate();

            redirectAttributes.addFlashAttribute("success", "Booking for room with ID " + roomId + " has been deleted!");
            return "redirect:/";  // Redirect back to the home page
        } catch (SQLException e) {
            model.addAttribute("error", "Error deleting booking: " + e.getMessage());
            e.printStackTrace();
            return "delete-booking";  // Return to the same page in case of an error
        }
    }
    
    // Helper method to get all roomid from the database
    private List<Booking> getAllRooms() {
        List<Booking> rooms = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM room";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setRoomId(resultSet.getInt("roomId"));
                booking.setRoomType(resultSet.getString("roomType"));
                booking.setRoomPrice(resultSet.getDouble("roomPrice"));
                booking.setAvailability(resultSet.getBoolean("availability"));
                rooms.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // Helper method to get a booking by its roomId 
    private Booking getBookingById(int roomId) throws SQLException {
        Booking booking = new Booking();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM hotel WHERE roomId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                booking.setRoomId(resultSet.getInt("roomId"));
                booking.setRoomType(resultSet.getString("roomType"));
                booking.setRoomPrice(resultSet.getDouble("roomPrice"));
                booking.setAvailability(resultSet.getBoolean("availability"));
            }
        }
        return booking;
    }
     
}
