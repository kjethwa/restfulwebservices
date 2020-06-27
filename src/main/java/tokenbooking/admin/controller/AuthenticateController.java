package tokenbooking.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tokenbooking.admin.model.AuthenticationRequest;
import tokenbooking.admin.model.AuthenticationResponse;
import tokenbooking.admin.service.MyUserDetailsService;
import tokenbooking.admin.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticateController {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;


    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            LOG.debug("User with userId {} login failed", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, "Invalid username and password"));
        }

        LOG.debug("User with userId {} login successful", authenticationRequest.getUsername());

        String jwt = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()));

        Cookie tokenCookie = new Cookie("token",jwt);
        //tokenCookie.setSecure(true);
        tokenCookie.setHttpOnly(true);
        //tokenCookie.setDomain("http://localhost");
        //tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        return ResponseEntity.ok().body(new AuthenticationResponse(jwt));
    }

    @PostMapping("/encode")
    public String encode(@RequestParam String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
