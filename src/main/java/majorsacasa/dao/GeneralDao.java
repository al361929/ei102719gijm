package majorsacasa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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


}
