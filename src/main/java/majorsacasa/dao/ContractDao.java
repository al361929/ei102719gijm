package majorsacasa.dao;

import majorsacasa.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ContractDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Contract> getContracts() {
        return jdbcTemplate.query("SELECT * FROM Contract", new ContractRowMapper());
    }

    public Contract getContract(String nifcompany) {
        return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE nifcompany=?", new ContractRowMapper(), nifcompany);
    }

    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract VALUES(?,?,?,?,?,?,?,?,?,?)", contract.getNcontract(), contract.getFirma(), contract.getDatos(), contract.getPrecio(),
                contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(), contract.getNifcompany(), contract.getDnielderly());
    }

    public void deleteContract(String nifcompany) {
        jdbcTemplate.update("DELETE FROM Contract WHERE nifcompany=?", nifcompany);
    }

    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET ncontract=?, firma=?, data=?, price=?, releaseDate=?, dateDown=?, quantity=?, description=? WHERE nifcompany=?", contract.getNcontract(),
                contract.getFirma(), contract.getDatos(), contract.getPrecio(), contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(),
                contract.getNifcompany());
    }
}
