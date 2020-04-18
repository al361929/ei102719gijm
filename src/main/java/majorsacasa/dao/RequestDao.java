package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
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
        jdbcTemplate.update("INSERT INTO Request VALUES(DEFAULT,?,?,?,?,?,?,?,?)", request.getIdService(), LocalDate.now(), "Pendiente",
                request.getDateAccept(), request.getDateReject(), request.getComments(), request.getDni(), request.getNif());

    }

    public Request getRequest(int idRequest) {
        return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE idRequest=?", new RequestRowMapper(), idRequest);
    }

    public void updateRequest(Request request) {
        jdbcTemplate.update("UPDATE Request SET idService=?, state=?, comments=?, dateRequest=?, dateAccept=?, dateReject=?, dni=?, nif=? WHERE idRequest=? ",
                request.getIdService(), request.getState(), request.getComments(), request.getDateRequest(), request.getDateAccept(), request.getDateReject(), request.getDni(), request.getNif(), request.getIdRequest());
    }

    public void deleteRequest(int idRequest) {
        jdbcTemplate.update("DELETE FROM Request WHERE idRequest=?", idRequest);
    }
    public List<Request> getRequestsElderly(String dni) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Request where dni=?",
                    new RequestRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Request>();
        }
    }

    public void updateEstado(int id, String estado) {
        jdbcTemplate.update("UPDATE Request SET state=? WHERE idRequest=? ", estado, id);
    }

    public Boolean checkService(String servicio, String dni) {
        List<String> servicios = jdbcTemplate.queryForList("SELECT servicetype FROM Service JOIN request USING(idservice) WHERE dni=? AND NOT state='Cancelada' AND NOT state='Rechazada'", String.class, dni);
        //System.out.println(dni);
       // System.out.println(servicios.toString());
        return servicios.contains(servicio);
    }
}
