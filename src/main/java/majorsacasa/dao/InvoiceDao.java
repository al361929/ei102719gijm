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
        jdbcTemplate.update("INSERT INTO Invoice VALUES(DEFAULT,?,?,?,?)", invoice.getDniElderly(), invoice.getDateInvoice(), invoice.getTotalPrice());
    }

    public Invoice getInvoice(Integer idInvoice) {
        return jdbcTemplate.queryForObject("SELECT * FROM Invoice WHERE idInvoice=?", new InvoiceRowMapper(), idInvoice);
    }

    public void updateInvoice(Invoice invoice) {
        jdbcTemplate.update("UPDATE Invoice SET dateinvoice=?, totalprice=?, dni=?, pdf=? WHERE idInvoice=?", invoice.getDateInvoice(), invoice.getTotalPrice(), invoice.getDniElderly(), invoice.getInvoicePDF(), invoice.getInvoiceNumber());
    }

    public void updloadInvoice(Integer idInvoice, Boolean pdf) {
        jdbcTemplate.update("UPDATE Invoice SET pdf=? WHERE idInvoice=?", pdf, idInvoice);
    }

    public void deleteInvoice(Integer invoice) {
        jdbcTemplate.update("DELETE FROM Invoice WHERE idInvoice=?", invoice);
    }

    public List<Invoice> getInvoiceElderly(String nif) {
        try {
            return jdbcTemplate.query(" SELECT * FROM Invoice WHERE dni =?", new InvoiceRowMapper(), nif);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Invoice>();
        }
    }
}
