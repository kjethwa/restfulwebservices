package tokenbooking.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableScheduling
class AppConfig {

    @Bean
    ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
        return new ErrorViewResolver() {
            @Override
            public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
                return status == HttpStatus.NOT_FOUND ? new ModelAndView("index.html", Collections.<String, Object>emptyMap(), HttpStatus.OK) : null;
            }
        };
    }
}
