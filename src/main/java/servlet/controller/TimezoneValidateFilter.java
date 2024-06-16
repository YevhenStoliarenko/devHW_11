package servlet.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;

@WebFilter(value = "/time/*")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");
        String timeZoneToCheck = timezone.replace(" ", "+");
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneToCheck);

        if ((timeZoneToCheck.equals("GMT") || !timeZone.getID().equals("GMT")) || timezone.equals("")) {
            chain.doFilter(req, res);
        } else {

            res.setStatus(400);
            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("Invalid timezone");
            res.getWriter().close();

        }
    }
}
