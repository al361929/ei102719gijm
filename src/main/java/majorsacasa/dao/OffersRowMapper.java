package majorsacasa.dao;

import majorsacasa.model.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public final class OffersRowMapper implements RowMapper<Offer> {
    @Override
    public Offer mapRow(ResultSet rs, int i) throws SQLException {
        Offer offers = new Offer();
        offers.setIdService(rs.getInt("idService_off"));
        offers.setNif(rs.getString("nif_off"));
        return offers;
    }
}
