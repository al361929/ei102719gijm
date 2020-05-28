package majorsacasa.dao;


import majorsacasa.model.ValoracionService;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ValoracionServiceRowMapper implements RowMapper<ValoracionService> {
    @Override
    public ValoracionService mapRow(ResultSet rs, int i) throws SQLException {
        ValoracionService valoracionService = new ValoracionService();
        valoracionService.setIdService(rs.getInt("idService"));
        valoracionService.setDni(rs.getString("dni"));
        valoracionService.setComments(rs.getString("comments"));
        valoracionService.setValoration(rs.getInt("valoration"));
        valoracionService.setDateValoration(rs.getObject("dateValoration", LocalDate.class));
        return valoracionService;
    }
}
