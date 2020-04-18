package majorsacasa.dao;

import majorsacasa.model.Offer;
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

    public List<Offer> getOffers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Offers",
                    new OffersRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Offer>();
        }
    }

    public void addOffers(Offer offers) {
        jdbcTemplate.update("INSERT INTO Offers VALUES(?,?)", offers.getIdService(), offers.getNif());

    }

    public Offer getOffer(String idService, String nif) {
        return jdbcTemplate.queryForObject("SELECT * FROM Offers WHERE idService=? AND nif=?", new OffersRowMapper(), idService, nif);
    }
    public Offer getType(String idService) {
        return jdbcTemplate.queryForObject("SELECT servicetype FROM Offers WHERE idService=?", new OffersRowMapper(), idService);
    }

    public void deleteOffers(String idService, String nif) {
        jdbcTemplate.update("SELECT * FROM Offers WHERE idService=? AND nif=?", idService, nif);
    }

    public Boolean checkService(String nif,int id) {
        List<String> servicios = jdbcTemplate.queryForList("select idservice from offers where nif=? AND idservice=?", String.class,nif,id);
      // System.out.println(servicios.contains(id)+" id-"+id);
        return servicios.isEmpty();
    }
}
