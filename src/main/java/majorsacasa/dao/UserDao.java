package majorsacasa.dao;

import majorsacasa.model.UserDetails;

import java.util.Collection;

public interface UserDao {
    UserDetails loadUserByUsername(String username, String password);

    Collection<UserDetails> listAllUsers();

}