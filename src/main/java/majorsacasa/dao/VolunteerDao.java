package majorsacasa.dao;


import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        return jdbcTemplate.queryForObject("SELECT * FROM Volunteer WHERE dni=?", new VolunteerRowMapper(), volunteer);

    }

    //añadir
    public void addVolunteer(Volunteer volunteer) {
        jdbcTemplate.update("INSERT INTO Volunteer VALUES(?,?,?,?,?,?,?,?,?,?)", volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getDni(),
                volunteer.getTelefono(), volunteer.getUsuario(), volunteer.getContraseña(), volunteer.getReleaseDate(), volunteer.getDataDown(), volunteer.getBirthday());
    }

    //eliminar
    public void removeVolunteer(String volunteer) {
        jdbcTemplate.update("DELETE FROM Volunteer WHERE dni=?", volunteer);
    }

    //editar
    public void updateVolunteer(Volunteer volunteer) {
        jdbcTemplate.update("UPDATE Volunteer SET name=?, surname=?, address=?, phonenumber=?, user_name=?, password=?, releaseDate=?, dateDown=?, birthday=? WHERE dni=?",
                volunteer.getNombre(), volunteer.getApellidos(), volunteer.getDireccion(), volunteer.getTelefono(), volunteer.getUsuario(),
                volunteer.getContraseña(), volunteer.getReleaseDate(), volunteer.getDataDown(), volunteer.getBirthday(), volunteer.getDni());
    }
}
