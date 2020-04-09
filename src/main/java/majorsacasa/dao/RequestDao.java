package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Request> getRequests() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Request",
                    new RequestRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Request>();
        }
    }

    public void addRequest(Request request) {
        jdbcTemplate.update("INSERT INTO Request VALUES(DEFAULT,?,?,?,?,?,?)", request.getIdService(), request.getDateRequest(), request.getState(),
                request.getDateAccept(), request.getDateReject(), request.getComments());

    }

    public Request getRequest(int idRequest) {
        return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE idRequest=?", new RequestRowMapper(), idRequest);
    }

    public void updateRequest(Request request) {
        jdbcTemplate.update("UPDATE Request SET idservice=?, state=?, comments=?, dateRequest=?, dateAccept=?, dateReject=? WHERE idrequest=? ",
                request.getIdService(), request.getState(), request.getComments(), request.getDateRequest(), request.getDateAccept(), request.getDateReject(), request.getIdRequest());
    }

    public void deleteRequest(int idRequest) {
        jdbcTemplate.update("DELETE FROM Request WHERE idRequest=?", idRequest);
    }

}