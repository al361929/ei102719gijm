package majorsacasa.dao;

import majorsacasa.model.Elderly;
import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

}
public final class ElderlyRowMapper implements RowMapper<Elderly> {
    @Override
    public Elderly mapRow(ResultSet rs, int i) throws SQLException {
        Elderly elderly = new Elderly();
        volunteer.setNombre(rs.getString("name"));
        volunteer.setApellidos(rs.getString("surname"));
        volunteer.setDireccion(rs.getString("address"));
        volunteer.setDni(rs.getString("dni"));
        return Elderly;    }
}
