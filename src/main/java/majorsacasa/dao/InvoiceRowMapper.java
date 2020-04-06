package majorsacasa.dao;

import majorsacasa.model.Invoice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class InvoiceRowMapper implements RowMapper<Invoice> {
    @Override
    public Invoice mapRow(ResultSet rs, int i) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(rs.getInt("invoicenumber"));
        Date date = rs.getDate("dateinvoice");
        invoice.setDateInvoice(date != null ? date.toLocalDate() : null);
        invoice.setTotalPrice(rs.getInt("totalprice"));
        invoice.setDniElderly(rs.getString("dnielderly"));
        return invoice;
    }
}
