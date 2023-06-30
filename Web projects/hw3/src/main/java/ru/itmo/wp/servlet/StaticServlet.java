package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        uri = uri + "+";
        int left = 1;
        int right = uri.indexOf('+');
        boolean flag = false;
        String contentType = "";
        while (true) {
            String uriN = uri.substring(left, right);
            left = right + 1;
            right = uri.indexOf('+', left);
            File file = new File("./src/main/webapp/static/" + uriN);
            if (file.isFile()) {
                if (!flag) {
                    contentType = Files.probeContentType(file.toPath());
                    flag = true;
                }
                response.setContentType(contentType);
                try (OutputStream outputStream = response.getOutputStream()) {
                    Files.copy(file.toPath(), outputStream);
                }
            } else {
                file = new File(getServletContext().getRealPath("/static/" + uriN));
                if (file.isFile()) {
                    if (!flag) {
                        contentType = getServletContext().getMimeType(file.getName());
                        flag = true;
                    }
                    response.setContentType(contentType);
                    try (OutputStream outputStream = response.getOutputStream()) {
                        Files.copy(file.toPath(), outputStream);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            if (right == -1) {
                break;
            }
        }
    }
}
