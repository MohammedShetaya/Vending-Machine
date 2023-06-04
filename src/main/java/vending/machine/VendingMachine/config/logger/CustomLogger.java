package vending.machine.VendingMachine.config.logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLogger implements Filter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Cast the servlet request and response to HttpServletRequest and HttpServletResponse respectively
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Get the current timestamp
        LocalDateTime startTime = LocalDateTime.now();

        // Format the timestamp as string with the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String startTimeString = startTime.format(formatter);

        // Continue the request chain
        filterChain.doFilter(request, response);

        // Log the incoming HTTP request information with colored log messages

        LocalDateTime endTime = LocalDateTime.now();
        long timeTakenMillis = java.time.Duration.between(startTime, endTime).toMillis();

        // Log the response status code and messages with colored log messages
        int statusCode = response.getStatus();
        String responseColor;
        if (statusCode >= 200 && statusCode < 300) {
            responseColor = ANSI_GREEN;
        } else if (statusCode >= 400 && statusCode < 500) {
            responseColor = ANSI_RED;
        } else {
            responseColor = ANSI_YELLOW;
        }

        System.out.println(startTimeString + "  " + responseColor + request.getMethod()+ " "+
                request.getRequestURI()+ " "+request.getProtocol()+ " "  + statusCode + " "+
                timeTakenMillis +" ms"+ ANSI_RESET);

    }

}
