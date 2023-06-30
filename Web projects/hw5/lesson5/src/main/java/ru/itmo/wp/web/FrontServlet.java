package ru.itmo.wp.web;

import freemarker.template.*;
//import ru.itmo.wp.web.exception.NotFouFndException;
//import ru.itmo.wp.web.exception.RedirectException;
//import ru.itmo.wp.web.page.IndexPage;
//import ru.itmo.wp.web.page.NotFoundPage;

import ru.itmo.wp.web.exception.*;
import ru.itmo.wp.web.page.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";
    private static final String FREEMARKER_MIMETYPE = ".ftlh";

    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;

    private Configuration newFreemarkerConfiguration(String templateDirName, boolean debug)
            throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir="
                    + templateDir + "]");
        }
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(debug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("WEB-INF/templates"), false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Route route = Route.newRoute(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.newNotFoundRoute(), request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }

    private void process(Route route, HttpServletRequest request, HttpServletResponse response)
            throws NotFoundException, ServletException, IOException {
        HttpSession session = request.getSession();
        String language = request.getParameter("lang");
        if (language != null) {
            if (language.length() == 2 && Character.isLowerCase(language.charAt(0))
                    && Character.isLowerCase(language.charAt(1))) {
                session.setAttribute("lang", language);
            }
        }
        language = (String) session.getAttribute("lang");
        if (language == null) {
            language = "en";
        }
        Class<?> pageClass;
        try {
            pageClass = Class.forName(route.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }

        Method method = null;
        Map<String, Object> view = new HashMap<>();
        Map<List<Class<?>>, Object[]> map = new HashMap<>();
        map.put(Arrays.asList(Map.class, HttpServletRequest.class), new Object[]{request, view});
        map.put(Collections.singletonList(Map.class), new Object[]{view});
        map.put(Collections.singletonList(HttpServletRequest.class), new Object[]{request});
        for (Class<?> clazz = pageClass; method == null && clazz != null; clazz = clazz.getSuperclass()) {
            Method [] allMethods = clazz.getDeclaredMethods(/*route.getAction(), HttpServletRequest.class, Map.class*/);
            for (Method m : allMethods) {
                String methodName = m.getName();
                Class<?>[] parameters = m.getParameterTypes();
                if (methodName.equals(route.getAction())) {
                    if (map.containsKey(Arrays.asList(parameters)) || parameters.length == 0) {
                        method = m;
                    }
                }
            }
        }

        if (method == null) {
            throw new NotFoundException();
        }

        Object page;
        try {
            page = pageClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException e) {
            throw new ServletException("Can't create page [pageClass="
                    + pageClass + "]");
        }

        //Map<String, Object> view = new HashMap<>();
//        if (request.getParameter("cell_00") != null) {
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                    view.put("cell_" + i + j, request.getParameter("cell_" + i + j));
//                }
//            }
//        }
        method.setAccessible(true);
        try {
            //method.invoke(page, request, view);
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 0) {
                method.invoke(page);
            } else {
                Object[] arguments = map.get(Arrays.asList(parameters));
                method.invoke(page, arguments);
            }
        } catch (IllegalAccessException e) {
            throw new ServletException("Can't invoke action method [pageClass="
                    + pageClass + ", method=" + method + "]");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) cause;
                response.sendRedirect(redirectException.getTarget());
                return;
            } else {
                throw new ServletException("Can't invoke action method [pageClass="
                        + pageClass + ", method=" + method + "]", cause);
            }
        }

        Template template = newTemplate(pageClass.getSimpleName(), language);
        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            template.process(view, response.getWriter());
        } catch (TemplateException e) {
            if (sourceConfiguration == null) {
                throw new ServletException("Can't render template [pageClass="
                        + pageClass + ", action=" + method + "]", e);
            }
        }
    }

    private Template newTemplate(String templateName, String language) throws ServletException {
        Template template = null;

        if (language.length() == 2 && !language.equals("en")) {
            template = createTemplate(templateName + "_" + language + FREEMARKER_MIMETYPE);
        }

        if (template == null) {
            template = createTemplate(templateName + FREEMARKER_MIMETYPE);
        }

        if (template == null) {
            throw new ServletException("Can't find template [templateName=" + templateName + "]");
        }

        return template;
    }

    private Template createTemplate(String templateName) throws ServletException {
        Template template = null;
        if (sourceConfiguration != null) {
            try {
                template = sourceConfiguration.getTemplate(templateName, new Locale("en"));
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null && targetConfiguration != null) {
            try {
                template = targetConfiguration.getTemplate(templateName, new Locale("en"));
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }
        return template;
    }

    private static class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }

        private static Route newNotFoundRoute() {
            return new Route(NotFoundPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newIndexRoute() {
            return new Route(IndexPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/"))
                    .filter(part -> !part.isEmpty())
                    .collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return newIndexRoute();
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1,
                    Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }
    }
}
