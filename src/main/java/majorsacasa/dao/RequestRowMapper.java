package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RequestRowMapper implements RowMapper<Request> {
    @Override
    public Request mapRow(ResultSet rs, int i) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt("id"));
        request.setServiceType(rs.getString("servicetype"));
        request.setDateRequest(rs.getDate("daterequest"));
        request.setState(rs.getString("state"));
        request.setDateAccept(rs.getDate("dateaccept"));
        request.setDateReject(rs.getDate("datereject"));
        request.setComments(rs.getString("comments"));
        request.setDni(rs.getString("dni"));
        request.setNif(rs.getString("nif"));
        return request;
    }
}
