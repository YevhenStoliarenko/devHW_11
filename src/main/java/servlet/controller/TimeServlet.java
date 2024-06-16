package servlet.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@WebServlet(value = "/time/*")
public class TimeServlet extends HttpServlet {

    private static final String TIMEZONE = "timezone";

    private static TemplateEngine templateEngine = new TemplateEngine();

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        Context simpleContext = null;
        Cookie[] cookies = req.getCookies();
        String lastTimezone = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TIMEZONE)) {
                lastTimezone = cookie.getValue();
            } else {
                lastTimezone = null;
            }
        }
        try {
            String localTime = TimeZone.getLocalTime("GMT+2");
            String timezone = req.getParameter(TIMEZONE);
            String replace = timezone.replace(" ", "+");
            if (timezone.equals("") && lastTimezone == null) {
                simpleContext = new Context(req.getLocale(), Map.of("time", localTime, "gmt", "GMT+2"));
            } else if (timezone.equals("") && lastTimezone != null) {
                String resultWithCookie = TimeZone.getLocalTime(lastTimezone);
                simpleContext = new Context(req.getLocale(), Map.of("time", resultWithCookie, "gmt", lastTimezone));
            } else {
                String result = TimeZone.getLocalTime(replace);
                simpleContext = new Context(req.getLocale(), Map.of("time", result, "gmt", replace));
                resp.addCookie(new Cookie(TIMEZONE, replace));
            }
            templateEngine.process("test1", simpleContext, resp.getWriter());
            resp.getWriter().close();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
