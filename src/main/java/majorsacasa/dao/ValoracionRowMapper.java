package majorsacasa.dao;

import majorsacasa.model.Valoracion;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ValoracionRowMapper implements RowMapper<Valoracion> {
    @Override
    public Valoracion mapRow(ResultSet rs, int i) throws SQLException {
        Valoracion valoracion = new Valoracion();
        valoracion.setDniVolunteer(rs.getString("dniVolunteer"));
        valoracion.setDni(rs.getString("dni"));
        valoracion.setComments(rs.getString("comments"));
        valoracion.setValoration(rs.getInt("valoration"));
        valoracion.setDateValoration(rs.getObject("dateValoration", LocalDate.class));
        return valoracion;
    }
}