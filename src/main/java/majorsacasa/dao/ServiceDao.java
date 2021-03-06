package majorsacasa.dao;

import majorsacasa.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ServiceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Service> getServices() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Service",
                    new ServiceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public void addService(Service service) {
        jdbcTemplate.update("INSERT INTO Service VALUES(DEFAULT,?,?,?)", service.getServiceType(), service.getPrice(), service.getDescription());
    }

    public Service getService(Integer idService) {
        return jdbcTemplate.queryForObject("SELECT * FROM Service WHERE idService=?", new ServiceRowMapper(), idService);
    }

    public void updateService(Service service) {
        jdbcTemplate.update("UPDATE Service SET serviceType=?, price=?, description=? WHERE idService=?", service.getServiceType(), service.getPrice(), service.getDescription(), service.getIdService());
    }

    public void deleteService(Integer idService) {
        jdbcTemplate.update("DELETE FROM Service WHERE idService=?", idService);
    }

    public List<Service> getElderlyList(String dni) {
        try {
            return jdbcTemplate.query("SELECT * FROM service JOIN request USING(idservice) WHERE dni=? AND state='Aceptada'", new ServiceRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public List<Service> getServiceList(String nif) {
        try {
            return jdbcTemplate.query("SELECT s.* FROM service AS s JOIN offers AS o USING(idservice) WHERE nif=?", new ServiceRowMapper(), nif);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public HashMap<String, String> getMapServiceCompany() {
        List<String> key = jdbcTemplate.queryForList("SELECT o.nif FROM service AS s JOIN offers AS o USING(idservice);", String.class);
        List<String> value = jdbcTemplate.queryForList("SELECT s.description FROM service AS s JOIN offers AS o USING(idservice);", String.class);
        HashMap<String, String> servicio = new HashMap<>();

        for (int i = 0; i < key.size(); i++) {
            servicio.put(key.get(i), value.get(i));
        }
        return servicio;

    }

    public int ultimoIdService() {
        List<String> ids = jdbcTemplate.queryForList("select Max(idService)  from service;", String.class);
        int id = Integer.parseInt(ids.get(0));
        return id;
    }
}
