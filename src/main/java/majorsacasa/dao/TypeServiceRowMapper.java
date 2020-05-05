package majorsacasa.dao;

import majorsacasa.model.TypeService;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeServiceRowMapper implements RowMapper<TypeService> {
    @Override
    public TypeService mapRow(ResultSet rs, int i) throws SQLException {
        TypeService service = new TypeService();
        service.setIdType(rs.getInt("idType"));
        service.setServiceType(rs.getString("servicetype"));

        return service;
    }
}
