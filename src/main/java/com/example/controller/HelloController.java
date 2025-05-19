package com.example.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("message", "Hello from Servlet!");
        request.getRequestDispatcher("/hello.jsp").forward(request, response);
    }
}
