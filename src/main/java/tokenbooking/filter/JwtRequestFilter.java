package tokenbooking.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tokenbooking.admin.service.MyUserDetailsService;
import tokenbooking.admin.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        ExtractUserName extractUserName = new ExtractUserName(request).invoke();
        String username = extractUserName.getUsername();
        String jwt = extractUserName.getJwt();

        LOG.debug("Filtering request for user {} ", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                LOG.debug("JWT of user {} is valid and setting it in SecurityContext ", username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } else {
            LOG.info("Will this ever be called?? Waiting for this is log....");
        }
        chain.doFilter(request, response);
    }

    private class ExtractUserName {
        private HttpServletRequest request;
        private String username;
        private String jwt;

        public ExtractUserName(HttpServletRequest request) {
            this.request = request;
        }

        public String getUsername() {
            return username;
        }

        public String getJwt() {
            return jwt;
        }

        public ExtractUserName invoke() {
            final String authorizationHeader = request.getHeader("Authorization");
            try {
                username = null;
                jwt = null;

                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    jwt = authorizationHeader.substring(7);
                    username = jwtUtil.extractUsername(jwt);
                }
            } catch (Exception e) {
                LOG.info(e.getMessage());
            }
            return this;
        }
    }
}