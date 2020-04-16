package majorsacasa.dao;

import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class VolunteerRowMapper implements RowMapper<Volunteer> {
    @Override
    public Volunteer mapRow(ResultSet rs, int i) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setNombre(rs.getString("name"));
        volunteer.setApellidos(rs.getString("surname"));
        volunteer.setDireccion(rs.getString("address"));
        volunteer.setDni(rs.getString("dniVolunteer"));
        volunteer.setTelefono(rs.getString("phonenumber"));
        volunteer.setBirthday(rs.getObject("birthday", LocalDate.class));
        volunteer.setDataDown(rs.getObject("datedown", LocalDate.class));
        volunteer.setReleaseDate(rs.getObject("releasedate", LocalDate.class));
        volunteer.setContraseña(rs.getString("password"));
        volunteer.setUsuario(rs.getString("user_name"));
        volunteer.setEmail(rs.getString("email"));
        return volunteer;
    }
}
