package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
   /* @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        //session.invalidate();
        if (session.getAttribute("uri") == null) {
            session.setAttribute("uri", request.getRequestURI());
        }
        String usersCaptchaNumber = request.getParameter("captcha");
        if (usersCaptchaNumber != null) {
            String trueCaptchaNumber = (String) session.getAttribute("captchaNumber");
            if (trueCaptchaNumber != null) {
                if (trueCaptchaNumber.equals(usersCaptchaNumber)) {
                    session.setAttribute("confirm", true);
                    response.sendRedirect((String) session.getAttribute("uri"));
                    //super.doFilter(request, response, chain);
                    return;
                }
            }
        }
        if (session.getAttribute("confirm") == null) {
            session.setAttribute("confirm", false);
        }
        Boolean confirmed = (Boolean) session.getAttribute("confirm");
        if (confirmed) {
            chain.doFilter(request, response);
        } else {
            DelayedHttpServletResponse delayedHttpServletResponse = new DelayedHttpServletResponse(response);
            Random random = new Random();
            int randomNumber = random.nextInt(900) + 100;
            session.setAttribute("captchaNumber", Integer.toString(randomNumber));
            String captchaPicture = Base64.getEncoder().encodeToString(ImageUtils.toPng(Integer.toString(randomNumber)));
            String captchaForm = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Captcha</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<img src=\"data:image/png;base64, " + captchaPicture + "\"/>\n" +
                    "<div class=\"captcha-number-form\" style=\"display: block\">\n" +
                    "    <form action=\"\" method=\"get\">\n" +
                    "        <label for=\"captcha-number-form__in\">Enter a number in [100;999]: </label>\n" +
                    "        <input name=\"captcha\" id=\"captcha-number-form__in\">\n" +
                    "    </form>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            chain.doFilter(request, delayedHttpServletResponse);
            response.getWriter().print(captchaForm);
            response.getWriter().flush();
        }

    }*/

}
