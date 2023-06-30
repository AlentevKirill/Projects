package ru.itmo.web.hw4.web;

import freemarker.template.*;
import ru.itmo.web.hw4.util.DataUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerServlet extends HttpServlet {
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private static final String DEBUG_TEMPLATES_PATH = "../../src/main/webapp/WEB-INF/templates";
    private static final String TEMPLATES_PATH = "/WEB-INF/templates";

    private final Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_31);

    @Override
    public void init() throws ServletException {
        File dir = new File(getServletContext().getRealPath("."), DEBUG_TEMPLATES_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir = new File(getServletContext().getRealPath(TEMPLATES_PATH));
        }

        try {
            freemarkerConfiguration.setDirectoryForTemplateLoading(dir);
        } catch (IOException e) {
            throw new ServletException("Unable to set template directory [dir=" + dir + "].", e);
        }

        freemarkerConfiguration.setDefaultEncoding(UTF_8);
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);

        Template template;
        try {
            String uri = URLDecoder.decode(request.getRequestURI(), UTF_8);
            if (uri.equals("") || uri.equals("/")) {
                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/index");
                dispatcher.forward(request, response);
            }
            template = freemarkerConfiguration.getTemplate(URLDecoder.decode(request.getRequestURI(), UTF_8) + ".ftlh");
        } catch (TemplateNotFoundException ignored) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ServletException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Object> data = getData(request);

        response.setContentType("text/html");
        try {
            template.process(data, response.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> getData(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (e.getValue() != null && e.getValue().length == 1) {
                String key = e.getKey();
                String value = e.getValue()[0];
                if (key.endsWith("_id")) {
                    try {
                        data.put(key, Long.parseLong(value));
                    } catch (NumberFormatException ignored) {
                        //data.put(key, value);
                        data.put(key, -1);
                    }
                } else {
                    data.put(key, value);
                }
            }
        }
        try {
            data.put("realLink", URLDecoder.decode(request.getRequestURI(), UTF_8));
        } catch (UnsupportedEncodingException ignored) {}
        DataUtil.addData(request, data);
        return data;
    }

}
