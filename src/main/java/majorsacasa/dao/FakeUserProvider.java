package majorsacasa.dao;

import majorsacasa.model.UserDetails;
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
        if (username.equals("Admin") && password.equals("Admin")){
            UserDetails user = new UserDetails();
            user.setUsername(username);
            user.setPassword(password);
            user.setTipo("Admin");
            user.setCode(5);
            return user;

        }
        if (username.equals("casManager") && password.equals("casManager")){
            UserDetails user = new UserDetails();
            user.setUsername(username);
            user.setPassword(password);
            user.setTipo("Admin");
            user.setCode(6);
            return user;

        }
        if (username.equals("casCommitee") && password.equals("casCommitee")){
            UserDetails user = new UserDetails();
            user.setUsername(username);
            user.setPassword(password);
            user.setTipo("Admin");
            user.setCode(7);
            return user;

        }
        if (username.equals("casVolunteer") && password.equals("casVolunteer")){
            UserDetails user = new UserDetails();
            user.setUsername(username);
            user.setPassword(password);
            user.setTipo("Admin");
            user.setCode(8);
            return user;

        }
        String tipo = "Volunteer";
        int code=1;
        List userList = jdbcTemplate.query("SELECT user_name, password,name, dnivolunteer FROM Volunteer WHERE user_name=?", new UserRowMapper(), username);
        if (userList.isEmpty()) {
            tipo="ElderlyPeople"; code=2;
            userList = jdbcTemplate.query("SELECT user_name, password, dni,name FROM ElderlyPeople WHERE user_name=?", new UserRowMapper(), username);
            if (userList.isEmpty()) {
                tipo = "SocialWorker"; code=3;
                userList = jdbcTemplate.query("SELECT user_name, password,dnisocialworker,name FROM SocialWorker WHERE user_name=?", new UserRowMapper(), username);
                if (userList.isEmpty()) {
                    tipo="Company"; code= 4;
                    userList = jdbcTemplate.query("SELECT user_name, password, nif,name FROM Company WHERE user_name=?", new UserRowMapper(), username);
                    if (userList.isEmpty()) {
                        return null; // Usuari no trobat
                    }
                }
            }
        }
        UserDetails user = (UserDetails) userList.get(0);
        user.setTipo(tipo);
        user.setCode(code);
        //System.out.println(user.toString());

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