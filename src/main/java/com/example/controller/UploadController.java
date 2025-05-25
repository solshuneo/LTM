package com.example.controller;

import com.example.service.TaskManager;
import com.example.model.Task;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {
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

        String originalFileName = extractFileName(filePart);
        String fileExt = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String rootPath = System.getProperty("user.dir");
        String uploadPath = rootPath + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        int nextIndex = 1;
        File[] userFiles = uploadDir.listFiles((dir, name) -> name.startsWith(username + "_"));
        if (userFiles != null) {
            for (File f : userFiles) {
                String fname = f.getName();
                try {
                    String numStr = fname.substring(username.length() + 1, fname.lastIndexOf('.'));
                    int num = Integer.parseInt(numStr);
                    if (num >= nextIndex) nextIndex = num + 1;
                } catch (Exception ignored) {}
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

        try {
            boolean created = TaskManager.createTask(username, "Pending", newFileName);
            session.setAttribute("uploadMessage", "Uploaded, Processing");
            if (created) {

                new Thread(() -> {
                    processImage(username, newFileName, uploadPath);
                    session.setAttribute("uploadMessage", "OK");

                }).start();
            } else {
                session.setAttribute("uploadMessage", "Lỗi tạo task trong database!");
            }
        } catch (Exception e) {
            session.setAttribute("uploadMessage", "Lỗi tạo task: " + e.getMessage());
        }

        response.sendRedirect("home.jsp");
    }

    private boolean processImage(String username, String filename, String uploadPath) {
        try {
            Thread.sleep(2000); // dừng 2000 mili-giây = 2 giây
            // update 60-100s random 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            File imageFile = new File(uploadPath, filename);

            // Sử dụng Tesseract OCR
            Tesseract tesseract = new Tesseract();
            String datapath = getServletContext().getRealPath("/tess4j_contents/tessdata");
            tesseract.setDatapath(datapath);
            tesseract.setLanguage("eng");

            String extractedText = tesseract.doOCR(imageFile);

            // Lưu ra file .txt cùng tên
            String textFilename = filename.replaceAll("\\.[^.]+$", ".txt");
            File textFile = new File(uploadPath, textFilename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
                writer.write(extractedText);
            }

            // ✅ Cập nhật database
            return TaskManager.markTaskAsFinished(username, filename);

        } catch (TesseractException | IOException e) {
            System.err.println("Lỗi xử lý ảnh " + filename + ": " + e.getMessage());
            return false;
        }
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
