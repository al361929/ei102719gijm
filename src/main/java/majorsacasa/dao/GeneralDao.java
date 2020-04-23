package majorsacasa.dao;

import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

public class GeneralDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Boolean checkDNI(String dni){
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where dni=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where dnivolunteer=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where dnisocialworker=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where nif=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        return true;

    }
    public Boolean checkUser(String user){
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        return true;

    }

    public HashMap<String,String> getUsersInfo(){

        List<UserDetails> usuarios = jdbcTemplate.query("select * from ElderlyPeople", new UserRowMapper());
        usuarios.addAll(jdbcTemplate.query("select * from volunteer", new UserRowMapper()));
        usuarios.addAll(jdbcTemplate.query("select * from socialworker", new UserRowMapper()));
        usuarios.addAll(jdbcTemplate.query("select * from company", new UserRowMapper()));

        HashMap <String,String> info=new HashMap<>();
        for (UserDetails u: usuarios){
            info.put(u.getDni(),u.getName());

        }
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer","select avg(valoration) from volunteerValoration GROUP BY dniVolunteer");
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer, avg(valoration) from volunteerValoration GROUP BY dniVolunteer");

        return  info;
    }
}
