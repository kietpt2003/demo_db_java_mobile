package com.example.connect_db_demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private ConnectionClass connectionClass;

    public DatabaseHelper() {
        connectionClass = new ConnectionClass();
    }

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        Connection con = connectionClass.connectDB();
        if (con != null) {
            String query = "SELECT * FROM food";
            try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    foodList.add(new Food(id, name));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return foodList;
    }

    public boolean addFood(String name) {
        Connection con = connectionClass.connectDB();
        if (con != null) {
            String query = "INSERT INTO food (name) VALUES (?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateFood(int id, String name) {
        Connection con = connectionClass.connectDB();
        if (con != null) {
            String query = "UPDATE food SET name = ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteFood(int id) {
        Connection con = connectionClass.connectDB();
        if (con != null) {
            String query = "DELETE FROM food WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
