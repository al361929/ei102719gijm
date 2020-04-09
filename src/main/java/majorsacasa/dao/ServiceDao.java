package majorsacasa.dao;

import majorsacasa.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
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
        jdbcTemplate.update("INSERT INTO Service VALUES(DEFAULT,?,?,?", service.getServiceType(), service.getPrice(), service.getDescription());
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
}
