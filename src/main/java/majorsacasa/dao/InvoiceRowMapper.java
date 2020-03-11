package majorsacasa.dao;

import majorsacasa.model.Invoice;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class InvoiceRowMapper implements RowMapper<Invoice> {
    @Override
    public Invoice mapRow(ResultSet rs, int i) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(rs.getInt("invoicerNumber"));
        invoice.setDateInvoice(rs.getDate("dateInvoice"));
        invoice.setTotalPrice(rs.getInt("totalPrice"));
        return invoice;
    }
}
