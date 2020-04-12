package tokenbooking.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tokenbooking.admin.model.AuthenticationRequest;
import tokenbooking.admin.model.AuthenticationResponse;
import tokenbooking.admin.service.MyUserDetailsService;
import tokenbooking.admin.util.JwtUtil;

@RestController
public class AuthenticateController {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            LOG.debug("User with userId {} login failed", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, "Invalid username and password"));
        }

        LOG.debug("User with userId {} login successful", authenticationRequest.getUsername());

        String jwt = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()));

        return ResponseEntity.ok().body(new AuthenticationResponse(jwt));
    }

}
