package com.example.model;

public class Task {
    private int id;
    private String clientId;
    private String status;       // Pending hoặc Finished
    private String fileResult;   // Tên file kết quả

    public Task() {}

    public Task(int id, String clientId, String status, String fileResult) {
        this.id = id;
        this.clientId = clientId;
        this.status = status;
        this.fileResult = fileResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileResult() {
        return fileResult;
    }

    public void setFileResult(String fileResult) {
        this.fileResult = fileResult;
    }
}