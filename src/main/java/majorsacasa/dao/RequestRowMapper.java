package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RequestRowMapper implements RowMapper<Request> {
    @Override
    public Request mapRow(ResultSet rs, int i) throws SQLException {
        Request request = new Request();
        request.setDni(rs.getString("dni"));
        request.setNif(rs.getString("nif"));
        request.setState(rs.getString("state"));
        request.setServiceType(rs.getString("servicetype"));
        request.setComments(rs.getString("comments"));
        request.setDateAccept(rs.getDate("dateaccept"));
        request.setDateReject(rs.getDate("datereject"));
        request.setDateRequest(rs.getDate("daterequest"));
        return request;
    }
}
