package majorsacasa.dao;

import majorsacasa.model.Service;
import majorsacasa.model.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TypeServiceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TypeService> getTypeServices() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM ServiceType",
                    new TypeServiceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<TypeService>();
        }
    }

    public void addService(TypeService service) {
        jdbcTemplate.update("INSERT INTO ServiceType VALUES(DEFAULT,?)", service.getServiceType());
    }

    public Service getService(Integer idService) {
        return jdbcTemplate.queryForObject("SELECT * FROM ServiceType WHERE idType=?", new ServiceRowMapper(), idService);
    }

    public void updateService(Service service) {
        jdbcTemplate.update("UPDATE ServiceType SET  description=? WHERE idType=?", service.getServiceType(), service.getPrice(), service.getDescription(), service.getIdService());
    }

    public void deleteService(Integer idService) {
        jdbcTemplate.update("DELETE FROM ServiceType WHERE idType=?", idService);
    }
}
