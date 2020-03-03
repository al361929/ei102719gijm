package majorsacasa.dao;

import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VolunteerRowMapper implements RowMapper<Volunteer> {
    @Override
    public Volunteer mapRow(ResultSet rs, int i) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setNombre(rs.getString("nombre"));
        volunteer.setApellidos(rs.getString("apellidos"));
        volunteer.setDireccion(rs.getString("direccion"));
        volunteer.setDni(rs.getString("dni"));
        return volunteer;    }
}
