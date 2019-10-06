package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.UserDetails;
import tokenbooking.repository.UserDetailsRepository;

@Service
public class UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    public UserDetails getUser(Long id){
        return userDetailsRepository.findOne(id);
    }

    public UserDetails saveuserDetails(UserDetails userDetailsDetails){
        return userDetailsRepository.save(userDetailsDetails);
    }
}
