package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class RequestRowMapper implements RowMapper<Request> {
    @Override
    public Request mapRow(ResultSet rs, int i) throws SQLException {
        Request request = new Request();
        request.setIdService(rs.getInt("idService"));
        request.setIdRequest(rs.getInt("idRequest"));
        request.setDateRequest(rs.getObject("daterequest", LocalDate.class));
        request.setState(rs.getString("state"));
        request.setDateAccept(rs.getObject("dateaccept", LocalDate.class));
        request.setDateReject(rs.getObject("datereject", LocalDate.class));
        request.setComments(rs.getString("comments"));
        request.setDni(rs.getString("dni"));
        request.setNif(rs.getString("nif"));
        request.setDias(rs.getString("days"));

        return request;
    }
}
