package majorsacasa.dao;

import majorsacasa.model.SocialWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SocialWorkerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<SocialWorker> getSocialWorkers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM SocialWorker",
                    new SocialWorkerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<SocialWorker>();
        }
    }

    public void addSocialWorker(SocialWorker socialWorker) {
        jdbcTemplate.update("INSERT INTO SocialWorker VALUES(?,?,?,?,?,?,?", socialWorker.getNombre(), socialWorker.getApellidos(), socialWorker.getDni(),
                socialWorker.getTelefono(), socialWorker.getUsuario(), socialWorker.getContraseña(), socialWorker.getEmail());

    }

    public SocialWorker getSocialWorker(String socialWorker) {
        return jdbcTemplate.queryForObject("SELECT * FROM SocialWorker WHERE dni=?", new SocialWorkerRowMapper(), socialWorker);
    }

    public void updateSocialWorker(SocialWorker socialWorker) {
        jdbcTemplate.update("UPDATE SocialWorker SET name=?, surname=?, phonenumber=?, user_name=?, password=?, email=? WHERE dni=?",
                socialWorker.getNombre(), socialWorker.getApellidos(), socialWorker.getTelefono(), socialWorker.getUsuario(), socialWorker.getContraseña(), socialWorker.getEmail(), socialWorker.getDni());
    }

    public void deleteSocialWorker(String socialWorker) {
        jdbcTemplate.update("DELETE FROM SocialWorker WHERE dni=?", socialWorker);
    }

}
