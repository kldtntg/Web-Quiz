package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { // this is the class that enables Spring Security to
    //verify if a username (or email,...) exists inside a persistence layer (an H2 database in this app)
    @Autowired
    UserRepository userRepository; //in this particular application, UserRepository extends CrudRepository which has
    // implemented methods under the hood to Create,Read,Update,Delete entities within a specified database

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return new UserDetailsImpl(user);
    }

    public void save(User user){
        userRepository.save(user);
    }


    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public boolean userExist(String email){
        return userRepository.findUserByEmail(email) != null;
    }


}