package majorsacasa.dao;

import majorsacasa.model.Service;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServiceRowMapper implements RowMapper<Service> {
    @Override
    public Service mapRow(ResultSet rs, int i) throws SQLException {
        Service service = new Service();
        service.setIdService(rs.getInt("idService"));
        service.setServiceType(rs.getString("serviceType"));
        service.setPrice(rs.getInt("price"));
        service.setDescription(rs.getString("description"));
        return service;
    }
}
