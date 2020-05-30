package majorsacasa.dao;

import majorsacasa.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
        jdbcTemplate.update("INSERT INTO Request VALUES(DEFAULT,?,?,?,?,?,?,?,?,?)", request.getIdService(), LocalDate.now(), "Pendiente",
                request.getDateAccept(), request.getDateReject(), request.getComments(), request.getDni(), request.getNif(), request.getDias());

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
        return servicios.contains(servicio);
    }

    public HashMap<Integer, String> getMapServiceElderly() {
        List<String> key = jdbcTemplate.queryForList("SELECT request.idService FROM service JOIN request  USING(idservice);", String.class);
        List<String> value = jdbcTemplate.queryForList("SELECT service.description FROM service JOIN request USING(idservice);", String.class);
        HashMap<Integer, String> servicio = new HashMap<>();

        for (int i = 0; i < key.size(); i++) {
            servicio.put(Integer.parseInt(key.get(i)), value.get(i));
        }
        return servicio;

    }

    public int ultimoIdRequest() {
        List<String> ids = jdbcTemplate.queryForList("Select MAX(idrequest) from Request;", String.class);
        int id = Integer.parseInt(ids.get(0));
        return id;
    }
}
