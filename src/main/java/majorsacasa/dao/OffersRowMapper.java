package majorsacasa.dao;

import majorsacasa.model.Offers;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public final class OffersRowMapper implements RowMapper<Offers> {
    @Override
    public Offers mapRow(ResultSet rs, int i) throws SQLException {
        Offers offers = new Offers();
        offers.setIdService(rs.getInt("idService_off"));
        offers.setNif(rs.getString("nif_off"));
        return offers;
    }
}
