package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.UserDetails;
import tokenbooking.service.UserDetailsService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("{userId}")
    public UserDetails getUser(@PathVariable Long userId) {
        return userDetailsService.getUser(userId);
    }

    @PostMapping
    public UserDetails saveUserDetails(@RequestBody UserDetails userDetails) {
            return  userDetailsService.saveuserDetails(userDetails);
    }

}