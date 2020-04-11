package majorsacasa.dao;

import majorsacasa.model.UserDetails;
import majorsacasa.model.Volunteer;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class FakeUserProvider implements UserDao {
    final Map<String, UserDetails> knownUsers = new HashMap<String, UserDetails>();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<UserDetails> getUsers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Volunteer",
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<UserDetails>();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        String tipo="Volunteer";
        List userList = jdbcTemplate.query("SELECT user_name, password FROM Volunteer WHERE user_name=?", new UserRowMapper(), username);
        if (userList.isEmpty()) {
            tipo="ElderlyPeople";
            userList = jdbcTemplate.query("SELECT user_name, password FROM ElderlyPeople WHERE user_name=?", new UserRowMapper(), username);
            if (userList.isEmpty()) {
                tipo="SocialWorker";
                userList = jdbcTemplate.query("SELECT user_name, password FROM SocialWorker WHERE user_name=?", new UserRowMapper(), username);
                if (userList.isEmpty()) {
                    tipo="Company";
                    userList = jdbcTemplate.query("SELECT user_name, password FROM Company WHERE user_name=?", new UserRowMapper(), username);
                    if (userList.isEmpty()) {
                        return null; // Usuari no trobat
                    }
                }
            }
        }
        UserDetails user = (UserDetails) userList.get(0);
        user.setTipo(tipo);

        // Contrasenya
        if (user.getPassword().equals(password)) {
            // Es deuria esborrar de manera segura el camp password abans de tornar-lo
            return user;
        } else {
            return null; // bad login!
        }
    }

    @Override
    public Collection<UserDetails> listAllUsers() {
        return knownUsers.values();
    }
}