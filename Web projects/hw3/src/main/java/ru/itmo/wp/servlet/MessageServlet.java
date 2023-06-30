package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Message {

    private String user;
    private String text;

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
    }
}
public class MessageServlet extends HttpServlet {

    private List<Message> messages = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        String uri = request.getRequestURI();
        if (uri.equals("/message/auth")) {
            String user = request.getParameter("user");
            if (user == null) {
                user = "";
            } else {
                session.setAttribute("user", user);
            }
            String json = new Gson().toJson(user);
            response.getWriter().print(json);
            response.getWriter().flush();
        }
        if (uri.equals("/message/add")) {
            String user = (String) session.getAttribute("user");
            String text = request.getParameter("text");
            messages.add(new Message(user, text));
            //response.setHeader("user", user);
            /*String json = new Gson().toJson(user);
            response.getWriter().print(json);
            json = new Gson().toJson(text);
            response.getWriter().print(json);
            response.getWriter().flush();*/
        }
        if (uri.equals("/message/findAll")) {
            String json = new Gson().toJson(messages);
            response.getWriter().print(json);
            response.getWriter().flush();
        }
    }
}
