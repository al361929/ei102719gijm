package majorsacasa.dao;

import majorsacasa.model.Elderly;
import majorsacasa.model.SocialWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SocialWorkerDao extends GeneralDao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<SocialWorker> getSocialWorkers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM SocialWorker",
                    new SocialWorkerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<SocialWorker>();
        }
    }

    public void addSocialWorker(SocialWorker socialWorker) {
        jdbcTemplate.update("INSERT INTO SocialWorker VALUES(?,?,?,?,?,?,?)", socialWorker.getDni(), socialWorker.getNombre(), socialWorker.getApellidos(),
                socialWorker.getTelefono(), socialWorker.getUsuario(), socialWorker.getContrasena(), socialWorker.getEmail());

    }

    public SocialWorker getSocialWorker(String dniSocialWorker) {
        return jdbcTemplate.queryForObject("SELECT * FROM SocialWorker WHERE dnisocialworker=?", new SocialWorkerRowMapper(), dniSocialWorker);
    }

    public void updateSocialWorker(SocialWorker socialWorker) {
        jdbcTemplate.update("UPDATE SocialWorker SET name=?, surname=?, phonenumber=?, user_name=?, password=?, email=? WHERE dnisocialworker=?",
                socialWorker.getNombre(), socialWorker.getApellidos(), socialWorker.getTelefono(), socialWorker.getUsuario(), socialWorker.getContrasena(), socialWorker.getEmail(), socialWorker.getDni());
    }

    public void deleteSocialWorker(String dniSocialWorker) {
        jdbcTemplate.update("DELETE FROM SocialWorker WHERE dnisocialworker=?", dniSocialWorker);
    }


    public List<Elderly> getElderlyList(String dniSocialWorker) {
        try {
            return jdbcTemplate.query("SELECT * FROM elderlypeople WHERE dnisocialworker=?", new ElderlyRowMapper(), dniSocialWorker);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
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
