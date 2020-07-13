package majorsacasa.dao;

import majorsacasa.model.Elderly;
import majorsacasa.model.Request;
import majorsacasa.model.UserDetails;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository

public class ElderlyDao extends GeneralDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Elderly> getElderlys() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM ElderlyPeople",
                    new ElderlyRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
    }

    public void addElderly(Elderly elderly) {
        jdbcTemplate.update("INSERT INTO ElderlyPeople VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getDni(),
                elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(), elderly.getContraseña(), LocalDate.now(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria(), elderly.getSocialWorker(), elderly.getEmail());

    }

    public Elderly getElderly(String elderly) {
        return jdbcTemplate.queryForObject("SELECT * FROM ElderlyPeople WHERE dni=?", new ElderlyRowMapper(), elderly);
    }

    public void updateElderly(Elderly elderly) {
        jdbcTemplate.update("UPDATE ElderlyPeople SET name=?, surname=?, address=?, allergies=?, phonenumber=?, user_name=?, password=?, releasedate=?, datedown=?, birthday=?, bankaccount=?, dnisocialWorker=?, email=? WHERE dni=?",
                elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(), elderly.getContraseña(),
                elderly.getReleaseDate(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria(), elderly.getSocialWorker(), elderly.getEmail(), elderly.getDni());
    }

    public void updateElderlySINpw(Elderly elderly) {
        jdbcTemplate.update("UPDATE ElderlyPeople SET name=?, surname=?, address=?, allergies=?, phonenumber=?, user_name=?,  releasedate=?, datedown=?, birthday=?, bankaccount=?, dnisocialWorker=?, email=? WHERE dni=?",
                elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(),
                elderly.getReleaseDate(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria(), elderly.getSocialWorker(), elderly.getEmail(), elderly.getDni());
    }

    public void updateElderlySinSocialWorker(Elderly elderly) {
        jdbcTemplate.update("UPDATE ElderlyPeople SET name=?, surname=?, address=?, allergies=?, phonenumber=?, user_name=?, password=?, releasedate=?, datedown=?, birthday=?, bankaccount=?,  email=? WHERE dni=?",
                elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(), elderly.getContraseña(),
                elderly.getReleaseDate(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria(), elderly.getEmail(), elderly.getDni());
    }

    public void deleteElderly(String elderly) {
        jdbcTemplate.update("DELETE FROM ElderlyPeople WHERE dni=?", elderly);
    }

    public List<Request> getRequestsElderly(String dni) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Request where dni=?",
                    new RequestRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Request>();
        }
    }

    public List<VolunteerTime> getVolunteerTimeElderly(String dni) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM volunteertime where dni=?",
                    new VolunteerTimeRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public Boolean checkElderly(String dni) {
        List<String> elderlys = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople", String.class);
        return elderlys.contains(dni);
    }

    public Boolean checkDNI(String dni) {
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where dni=?", String.class, dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where dnivolunteer=?", String.class, dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where dnisocialworker=?", String.class, dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where nif=?", String.class, dni);
        return dnis.isEmpty();

    }

    public Boolean checkUser(String user) {
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where user_name=?", String.class, user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where user_name=?", String.class, user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where user_name=?", String.class, user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where user_name=?", String.class, user);
        return dnis.isEmpty();

    }

    public HashMap<String, String> getUsersInfo() {

        List<UserDetails> usuarios = jdbcTemplate.query("SELECT user_name, password,name, dnivolunteer FROM Volunteer", new UserRowMapper());

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, dni,name FROM ElderlyPeople", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password,dnisocialworker,name FROM SocialWorker ", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, nif,name FROM Company", new UserRowMapper()));

        HashMap<String, String> info = new HashMap<>();
        for (UserDetails u : usuarios) {
            info.put(u.getDni(), u.getName());
        }

        info.put("0", " ");

        return info;
    }


}
