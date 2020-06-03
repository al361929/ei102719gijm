package majorsacasa.dao;


import majorsacasa.model.Elderly;
import majorsacasa.model.Volunteer;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository

public class VolunteerDao extends GeneralDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Volunteer> getVolunteers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Volunteer WHERE state='Aceptado'",
                    new VolunteerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Volunteer>();
        }
    }

    public List<Volunteer> getVolunteersAll() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Volunteer",
                    new VolunteerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Volunteer>();
        }
    }

    public Volunteer getVolunteer(String volunteer) {
        return jdbcTemplate.queryForObject("SELECT * FROM Volunteer WHERE dnivolunteer=?", new VolunteerRowMapper(), volunteer);

    }

    //añadir
    public void addVolunteer(Volunteer volunteer) {
        jdbcTemplate.update("INSERT INTO Volunteer VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getDni(),
                volunteer.getTelefono(), volunteer.getUsuario(), volunteer.getContraseña(), LocalDate.now(), volunteer.getDataDown(), volunteer.getBirthday(), volunteer.getEmail(), "Pendiente");
    }

    //eliminar
    public void deleteVolunteer(String volunteer) {
        jdbcTemplate.update("DELETE FROM Volunteer WHERE dnivolunteer=?", volunteer);
    }

    //editar
    public void updateVolunteer(Volunteer volunteer) {
        jdbcTemplate.update("UPDATE Volunteer SET name=?, surname=?, address=?, phonenumber=?, user_name=?, password=?, releaseDate=?, dateDown=?, birthday=?, email=? WHERE dnivolunteer=?",
                volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getTelefono(), volunteer.getUsuario(),
                volunteer.getContraseña(), volunteer.getReleaseDate(), volunteer.getDataDown(), volunteer.getBirthday(), volunteer.getEmail(), volunteer.getDni());
    }

    public void updateVolunteerSINpw(Volunteer volunteer) {
        jdbcTemplate.update("UPDATE Volunteer SET name=?, surname=?, address=?, phonenumber=?, user_name=?, releaseDate=?, dateDown=?, birthday=?, email=? WHERE dnivolunteer=?",
                volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getTelefono(), volunteer.getUsuario(), volunteer.getReleaseDate(), volunteer.getDataDown(), volunteer.getBirthday(), volunteer.getEmail(), volunteer.getDni());
    }

    //editar Estado
    public void updateVolunteerEstado(String dni, String estado) {
        jdbcTemplate.update("UPDATE Volunteer SET state=? WHERE dnivolunteer=?",
                estado, dni);
    }

    //listar horarios para el usuario
    public List<VolunteerTime> getScheduleList(String dnivolunteer) {
        try {
            return jdbcTemplate.query("Select * From volunteertime Where dniVolunteer = ?", new VolunteerTimeRowMapper(), dnivolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public List<Elderly> getElderlyList(String dniVolunteer) {
        try {
            return jdbcTemplate.query("select DISTINCT elderlypeople.* from elderlypeople JOIN volunteertime USING(dni) WHERE dnivolunteer=?", new ElderlyRowMapper(), dniVolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
    }

    public List<Volunteer> getVolunteerAsigned(String dniElderly) {
        try {
            return jdbcTemplate.query("SELECT DISTINCT v.* FROM volunteer AS v JOIN volunteerTime AS vt USING(dnivolunteer) WHERE vt.dni=?", new VolunteerRowMapper(), dniElderly);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Volunteer>();
        }
    }

    public List<VolunteerTime> getScheduleListDisponibles() {
        try {
            return jdbcTemplate.query("select Volunteertime.* from VolunteerTime Join Volunteer USING(dnivolunteer) Where dni IS NULL AND availability= 'True' and state='Aceptado';", new VolunteerTimeRowMapper());

        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public List<VolunteerTime> getMisHorariosElderly(String dniV, String dni) {
        try {
            return jdbcTemplate.query("select * from VolunteerTime Where DNIVolunteer=? AND dni=?", new VolunteerTimeRowMapper(), dniV, dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
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


}
