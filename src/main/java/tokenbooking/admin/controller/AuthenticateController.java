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
import tokenbooking.model.UserDetails;
import tokenbooking.repository.UserDetailsRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping("/auth")
public class AuthenticateController {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @PostMapping("/authenticate")
    @CrossOrigin(origins = "http://localhost:4201")
    public ResponseEntity<AuthenticationResponse> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationRequest.setUsername(authenticationRequest.getUsername().toLowerCase());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            LOG.debug("User with userId {} login failed", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, "Invalid username and password"));
        }

        LOG.debug("User with userId {} login successful", authenticationRequest.getUsername());

        String jwt = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()));

        UserDetails userDetails = userDetailsRepository.findByLoginId(authenticationRequest.getUsername());

        String role = getRole(userDetails);
        /*Cookie tokenCookie = new Cookie("token",jwt);
        //tokenCookie.setSecure(true);
        //tokenCookie.setHttpOnly(true);
        //tokenCookie.setDomain("http://localhost");
        //tokenCookie.setPath("/");
        response.addCookie(tokenCookie);*/

        return ResponseEntity.ok().body(new AuthenticationResponse(jwt, userDetails.getFullName(), role, null));
    }

    @PostMapping("/encode")
    public String encode(@RequestParam String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    private String getRole(UserDetails userDetails) {
        if (userDetails.getClientId() != null) {
            return "ADMIN";
        } else {
            return "USER";
        }
    }
}
