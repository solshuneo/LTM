package com.example.controller;

import com.example.service.TaskManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {
    // Thư mục uploads nằm ngay root folder project (vd: /your_project_root/uploads)
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        Part filePart = request.getPart("picture");

        if (username == null || filePart == null || filePart.getSize() == 0) {
            session.setAttribute("uploadMessage", "Thiếu thông tin hoặc không có ảnh.");
            response.sendRedirect("home.jsp");
            return;
        }

        // Lấy phần mở rộng file gốc để giữ lại (vd .jpg, .png)
        String originalFileName = extractFileName(filePart);
        String fileExt = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Xác định đường dẫn thư mục uploads nằm ở root folder của project
        // Lấy đường dẫn tuyệt đối của root folder project (tương đối đến thư mục chạy server)
        String rootPath = System.getProperty("user.dir");
        String uploadPath = rootPath + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // Tạo tên file dạng username_sốThứTự.ext
        int nextIndex = 1;
        File[] userFiles = uploadDir.listFiles((dir, name) -> name.startsWith(username + "_"));
        if (userFiles != null && userFiles.length > 0) {
            for (File f : userFiles) {
                String fname = f.getName();
                try {
                    String numStr = fname.substring(username.length() + 1, fname.lastIndexOf('.'));
                    int num = Integer.parseInt(numStr);
                    if (num >= nextIndex) nextIndex = num + 1;
                } catch (Exception ignored) {
                }
            }
        }

        String newFileName = username + "_" + nextIndex + fileExt;
        String filePath = uploadPath + File.separator + newFileName;

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            session.setAttribute("uploadMessage", "Lỗi lưu file: " + e.getMessage());
            response.sendRedirect("home.jsp");
            return;
        }

        // Gọi TaskManager để lưu tên file mới vào DB
        try {
            boolean created = TaskManager.createTask(username, "Pending", newFileName);
            if (created) {
                session.setAttribute("uploadMessage", "Upload và tạo task thành công!");
            } else {
                session.setAttribute("uploadMessage", "Lỗi khi tạo task trong database!");
            }
        } catch (Exception e) {
            session.setAttribute("uploadMessage", "Lỗi khi tạo task: " + e.getMessage());
        }

        response.sendRedirect("home.jsp");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return null;
        for (String cd : contentDisp.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            }
        }
        return null;
    }
}
