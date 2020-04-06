package majorsacasa.dao;

import majorsacasa.model.Offers;
import majorsacasa.model.SocialWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OffersDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Offers> getOffers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Offers",
                    new OffersRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Offers>();
        }
    }

    public void addOffers(Offers offers) {
        jdbcTemplate.update("INSERT INTO Offers VALUES(?,?)", offers.getIdService(), offers.getNif());

    }

    public Offers getOffers(String idService, String nif) {
        return jdbcTemplate.queryForObject("SELECT * FROM Offers WHERE idService_off=? AND nif_off=?", new OffersRowMapper(), idService, nif);
    }

    public void deleteOffers(String idService, String nif) {
        jdbcTemplate.update("SELECT * FROM Offers WHERE idService_off=? AND nif_off=?", idService, nif);
    }
}
