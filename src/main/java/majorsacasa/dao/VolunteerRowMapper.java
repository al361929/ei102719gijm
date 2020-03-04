package majorsacasa.dao;

import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VolunteerRowMapper implements RowMapper<Volunteer> {
    @Override
    public Volunteer mapRow(ResultSet rs, int i) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setNombre(rs.getString("name"));
        volunteer.setApellidos(rs.getString("surname"));
        volunteer.setDireccion(rs.getString("address"));
        volunteer.setDni(rs.getString("dni"));
        volunteer.setTelefono(rs.getString("phonenumber"));
        volunteer.setBirthday(rs.getDate("birthday").toLocalDate());
        volunteer.setDataDown(rs.getDate("dateDown").toLocalDate());
        volunteer.setRealaseDate(rs.getDate("realasedate").toLocalDate());
        volunteer.setContraseña(rs.getString("password"));
        volunteer.setUsuario(rs.getString("user_Name"));
        return volunteer;    }
}
