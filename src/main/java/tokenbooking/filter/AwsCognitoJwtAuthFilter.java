package tokenbooking.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tokenbooking.config.AwsCognitoIdTokenProcessor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AwsCognitoJwtAuthFilter extends GenericFilter {

    private static Logger LOG = LoggerFactory.getLogger(AwsCognitoJwtAuthFilter.class);
    private AwsCognitoIdTokenProcessor cognitoIdTokenProcessor;

    public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor cognitoIdTokenProcessor) {
        this.cognitoIdTokenProcessor = cognitoIdTokenProcessor;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication;
        try {
            authentication = this.cognitoIdTokenProcessor.authenticate ((HttpServletRequest)request);
            if (authentication != null) {    SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception var6) {
            LOG.error("Cognito ID Token processing error", var6);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}