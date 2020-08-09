package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import tokenbooking.model.UserDetails;
import tokenbooking.model.UserRole;
import tokenbooking.model.UserRoleEnum;
import tokenbooking.repository.UserDetailsRepository;
import tokenbooking.repository.UserRoleRepository;

import java.util.UUID;

@Service
public class UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    public UserDetails getUser(UUID id){
        return userDetailsRepository.findById(id).get();
    }

    public UserDetails registerUser(UserDetails userDetailsDetails) throws Exception {

        validate(userDetailsDetails);

        userDetailsDetails.setLoginId(userDetailsDetails.getLoginId().toLowerCase());
        userDetailsDetails.setPassword(encode(userDetailsDetails.getPassword()));
        userDetailsDetails.setStatus("ACTIVE");

        UserDetails userDetails = userDetailsRepository.save(userDetailsDetails);
        UserRole userRole = new UserRole();
        userRole.setRole(UserRoleEnum.ROLE_USER);
        userRole.setUserId(userDetails.getUserId());

        userRoleRepository.save(userRole);
        return userDetails;
    }

    public void saveUserDetails(UserDetails userDetailsDetails) throws Exception {
        if (!StringUtils.isEmpty(userDetailsDetails.getLoginId())) {
            UserDetails temp = userDetailsRepository.findByLoginId(userDetailsDetails.getLoginId());
            if (temp == null) {
                userDetailsRepository.save(userDetailsDetails);
            }
        }
    }

    private void validate(UserDetails userDetailsDetails) throws Exception {
        UserDetails tempUser = null;
        tempUser = userDetailsRepository.findByLoginId(userDetailsDetails.getLoginId());
        if (tempUser != null) {
            throw new Exception("User with emailid " + userDetailsDetails.getLoginId() + " already exist");
        }
        tempUser = userDetailsRepository.findByPhoneNumber(userDetailsDetails.getPhoneNumber());
        if (tempUser != null) {
            throw new Exception("User with phone number " + userDetailsDetails.getPhoneNumber() + " already exist");
        }
    }

    private String encode(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
