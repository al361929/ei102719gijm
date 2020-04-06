package majorsacasa.dao;

import majorsacasa.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Invoice> getInvoices() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Invoice", new InvoiceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Invoice>();
        }
    }

    public void addInvoice(Invoice invoice) {
        jdbcTemplate.update("INSERT INTO Invoice VALUES(?,?,?,?)", invoice.getInvoiceNumber(), invoice.getDateInvoice(), invoice.getTotalPrice(), invoice.getDniElderly());
    }

    public Invoice getInvoice(Integer idinvoice) {
        return jdbcTemplate.queryForObject("SELECT * FROM Invoice WHERE idinvoice=?", new InvoiceRowMapper(), idinvoice);
    }

    public void updateInvoice(Invoice invoice) {
        jdbcTemplate.update("UPDATE Invoice SET dateinvoice=?, totalprice=?, dnielderly=? WHERE idinvoice=?", invoice.getDateInvoice(), invoice.getTotalPrice(), invoice.getDniElderly(), invoice.getInvoiceNumber());
    }

    public void deleteInvoice(Integer invoice) {
        jdbcTemplate.update("DELETE FROM Invoice WHERE idinvoice=?", invoice);
    }
}
