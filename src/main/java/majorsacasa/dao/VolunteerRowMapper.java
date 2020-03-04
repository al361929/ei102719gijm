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
        return volunteer;    }
}
