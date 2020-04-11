package tokenbooking.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username and password");
        }

        String jwt = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()));

        return new AuthenticationResponse(jwt);
    }

}
