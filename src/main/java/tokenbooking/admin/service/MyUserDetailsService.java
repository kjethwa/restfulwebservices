package tokenbooking.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tokenbooking.model.ClientLoginDetail;
import tokenbooking.repository.ClientLoginDetailRepository;

import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    ClientLoginDetailRepository clientLoginDetailRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        ClientLoginDetail clientLoginDetail = clientLoginDetailRepository.findByUserName(userName);
        UserDetails userDetails = new User(clientLoginDetail.getUserName(), clientLoginDetail.getPassword(), clientLoginDetail.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return userDetails;
    }


}
