package tokenbooking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tokenbooking.admin.model.ResponseMessage;
import tokenbooking.admin.model.ResponseStatus;
import tokenbooking.model.UserDetails;
import tokenbooking.service.UserDetailsService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/user/{userId}")
    @CrossOrigin(origins = "http://localhost:4201")
    public UserDetails getUser(@PathVariable UUID userId) {
        return userDetailsService.getUser(userId);
    }

/*    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4201")
    public ResponseEntity<ResponseMessage> register(@RequestBody UserDetails userDetails) {
        try {
            userDetailsService.registerUser(userDetails);
            return new ResponseEntity(new ResponseMessage("Registered successfully", ResponseStatus.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            LOG.warn(e.toString());
            return new ResponseEntity(new ResponseMessage(null,e.getMessage(), ResponseStatus.FAILURE), HttpStatus.OK);
        }
    }*/

    @PostMapping("/saveUserDetails")
    @CrossOrigin(origins = "http://localhost:4201")
    public ResponseEntity<ResponseMessage> saveUserDetails(@RequestBody UserDetails userDetails) {
        try {
            userDetailsService.saveUserDetails(userDetails);
            return new ResponseEntity(new ResponseMessage("Registered successfully", ResponseStatus.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            LOG.warn(e.toString());
            return new ResponseEntity(new ResponseMessage(null,e.getMessage(), ResponseStatus.FAILURE), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    @CrossOrigin(origins = "http://localhost:4201")
    public ResponseEntity<ResponseMessage> logoutUser(Principal principal) {
        LOG.info("Logged out user {} successfully ", principal.getName());
        return new ResponseEntity(new ResponseMessage("Logged out successfully", ResponseStatus.SUCCESS), HttpStatus.OK);
    }

}