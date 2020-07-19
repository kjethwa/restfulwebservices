package tokenbooking.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tokenbooking.model.UserRole;
import tokenbooking.model.UserRoleEnum;
import tokenbooking.repository.UserDetailsRepository;
import tokenbooking.repository.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRoleRepository findRolesByUserName;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        tokenbooking.model.UserDetails userDetailsDB = userDetailsRepository.findByLoginId(userName);
        UserDetails userDetails = null;
        if (userDetailsDB != null) {
            List<UserRole> userRoleList = findRolesByUserName.findByUserId(userDetailsDB.getUserId());

            List<SimpleGrantedAuthority> roles = userRoleList.stream().map(userRole -> userRole.getRole().label).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            userDetails = new User(userDetailsDB.getLoginId(), userDetailsDB.getPassword(), roles);
        }
        return userDetails;
    }

}
