package vending.machine.VendingMachine.config.logger;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    @Bean
    public FilterRegistrationBean<CustomLogger> customLoggerFilterRegistration() {
        FilterRegistrationBean<CustomLogger> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CustomLogger());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
