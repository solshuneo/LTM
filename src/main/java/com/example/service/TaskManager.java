package com.example.service;

import com.example.model.Task;
import com.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    // Lấy danh sách task của user
    public static List<Task> getUserTasks(String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, client_id, status, file_result FROM tasks WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("client_id"),
                        rs.getString("status"),
                        rs.getString("file_result")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Lấy tất cả task (admin)
    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, client_id, status, file_result FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("client_id"),
                        rs.getString("status"),
                        rs.getString("file_result")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Thêm task mới
    public static boolean createTask(String username, String status, String fileResult) {
        String sql = "INSERT INTO tasks (client_id, status, file_result) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, status);
            ps.setString(3, fileResult);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
