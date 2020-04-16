package majorsacasa.dao;

import majorsacasa.model.Produce;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduceRowMapper implements RowMapper<Produce> {
    @Override
    public Produce mapRow(ResultSet rs, int i) throws SQLException {
        Produce produce = new Produce();
        produce.setIdInvoice(rs.getInt("idInvoice"));
        produce.setIdRequest(rs.getInt("idRequest"));
        return produce;
    }
}
