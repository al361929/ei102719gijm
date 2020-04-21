package majorsacasa.dao;



import majorsacasa.model.Elderly;
import majorsacasa.model.Volunteer;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository

public class VolunteerDao {
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
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Volunteer>();
        }
    }

    public Volunteer getVolunteer(String volunteer) {
        return jdbcTemplate.queryForObject("SELECT * FROM Volunteer WHERE dnivolunteer=?", new VolunteerRowMapper(), volunteer);

    }

    //añadir
    public void addVolunteer(Volunteer volunteer) {
        jdbcTemplate.update("INSERT INTO Volunteer VALUES(?,?,?,?,?,?,?,?,?,?,?)", volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getDni(),
                volunteer.getTelefono(), volunteer.getUsuario(), volunteer.getContraseña(), LocalDate.now(), volunteer.getDataDown(), volunteer.getBirthday(), volunteer.getEmail());
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
            return jdbcTemplate.query("select elderlypeople.* from elderlypeople JOIN volunteertime USING(dni) WHERE dnivolunteer=?", new ElderlyRowMapper(), dniVolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
    }

}
