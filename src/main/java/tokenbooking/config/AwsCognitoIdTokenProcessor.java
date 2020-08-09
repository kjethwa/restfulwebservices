package tokenbooking.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AwsCognitoIdTokenProcessor {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private ConfigurableJWTProcessor configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        if (idToken != null) {
            JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken),null);
            validateIssuer(claims);
            verifyIfIdToken(claims);
            String username = getUserNameFrom(claims);
            if (username != null) {
                List<String> roles = getUserRoles(claims);
                List<GrantedAuthority> grantedAuthorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                User user = new User(username, "", grantedAuthorities);
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }

    private List<String> getUserRoles(JWTClaimsSet claims) throws ParseException {
        List<String> roles = claims.getStringListClaim(this.jwtConfiguration.getGroups());
        if(roles != null && !roles.isEmpty()) {
            List<String> springRole = new ArrayList<>();
            roles.stream().forEach(role -> springRole.add("ROLE_" + role.toUpperCase()));
            return springRole;
        } else {
            return Arrays.asList("ROLE_USER");
        }
    }

    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}
