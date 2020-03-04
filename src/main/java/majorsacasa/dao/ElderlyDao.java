package majorsacasa.dao;

import majorsacasa.model.Elderly;
import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository

public class ElderlyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<Elderly> getElderlys() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Elderly",
                    new ElderlyRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
    }

    public void addElderly(Elderly elderly) {
    }

    public Object getElderly(String dni) {
        return null;
    }

    public void updateElderly(Elderly elderly) {
    }

    public void deleteElderly(String dni) {
    }
}
