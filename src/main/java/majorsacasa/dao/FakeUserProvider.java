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

    public List<Volunteer> getVolunteers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Volunteer",
                    new VolunteerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Volunteer>();
        }
    }

    public FakeUserProvider() {
        List<Volunteer> list = getVolunteers();
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        for (Volunteer v : list) {
            UserDetails user = new UserDetails();
            user.setUsername(v.getUsuario());
            user.setPassword(passwordEncryptor.encryptPassword(v.getContraseña()));
            knownUsers.put(v.getUsuario(), user);

        }
        UserDetails userAlice = new UserDetails();
        userAlice.setUsername("alice");
        userAlice.setPassword(passwordEncryptor.encryptPassword("alice"));
        knownUsers.put("alice", userAlice);

        UserDetails userBob = new UserDetails();
        userBob.setUsername("bob");
        userBob.setPassword(passwordEncryptor.encryptPassword("bob"));
        knownUsers.put("bob", userBob);
    }

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        System.out.println(username + "---" + password);
        UserDetails user = knownUsers.get(username.trim());
        if (user == null)
            return null; // Usuari no trobat
        // Contrasenya
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(password, user.getPassword())) {
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