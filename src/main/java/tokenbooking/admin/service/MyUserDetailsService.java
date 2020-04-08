package tokenbooking.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tokenbooking.repository.UserDetailsRepository;

import java.util.Collections;

import static tokenbooking.model.Constants.ACTIVE;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        tokenbooking.model.UserDetails dbUser = userDetailsRepository.findUserDetailsByPhoneNumberAndStatus("9988776655", ACTIVE);
        UserDetails userDetails = new User(dbUser.getPhoneNumber(), "pass", Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        return userDetails;
    }


}
