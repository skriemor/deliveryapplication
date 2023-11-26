package hu.torma.deliveryapplication.utility.security;

import hu.torma.deliveryapplication.entity.SecureUser;
import hu.torma.deliveryapplication.repository.SecureUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserDetail implements UserDetailsService {
    Logger log = Logger.getLogger("LOGINLOG");
    @Autowired
    SecureUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecureUser user = userRepo.findByUsername(username).orElse(null);
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user == null) {
            log.info("user does not exist");
            throw new UsernameNotFoundException("User does not exist by Username");
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}