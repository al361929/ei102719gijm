package majorsacasa.dao;

import majorsacasa.model.Request;
import majorsacasa.model.SocialWorker;
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
                    "SELECT * FROM Request", new RequestRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Request>();
        }
    }

    public void addRequest(Request request) {
        jdbcTemplate.update("INSERT INTO Request VALUES(?,?,?,?,?,?,?,?)", request.getServiceType(), request.getDateRequest(), request.getState(), request.getDateAccept(),
                request.getDateReject(), request.getComments(), request.getDni(), request.getNif());

    }

    public Request getRequest(String dniRequest) {
        return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE dni=?", new RequestRowMapper(), dniRequest);
    }

    public void updateRequest(Request request) {
        jdbcTemplate.update("UPDATE Request SET state=?, servicetype=?, comments=?, dateaccept=?, datereject=?, email=? WHERE dni=? AND nif=?",
                request.getState(), request.getServiceType(), request.getComments(), request.getDateAccept(), request.getDateReject(), request.getDni(), request.getNif());
    }

    public void deleteRequest(String dniRequest) {
        jdbcTemplate.update("DELETE FROM Request WHERE dni=?", dniRequest);
    }

}
