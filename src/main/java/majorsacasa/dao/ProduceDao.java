package majorsacasa.dao;

import majorsacasa.model.Produce;
import majorsacasa.model.Request;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProduceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Produce> getProduces() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Produce", new ProduceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Produce>();
        }
    }

    public void addProduce(Produce produce) {
        jdbcTemplate.update("INSERT INTO Produce VALUES(?,?", produce.getIdInvoice(), produce.getIdRequest());

    }

    public Produce getProduce(int idInvoice, int idRequest) {
        return jdbcTemplate.queryForObject("SELECT * FROM Produce WHERE idInvoice_pro=? AND idRequest_pro=?", new ProduceRowMapper(), idInvoice, idRequest);
    }


    public void deleteProduce(int idInvoice, int idRequest) {
        jdbcTemplate.update("DELETE FROM Produce WHERE idInvoice_pro=? AND idRequest_pro=?", idInvoice, idRequest);
    }
}
