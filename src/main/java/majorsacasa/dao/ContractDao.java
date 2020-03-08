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

    public Contract getContract(String nif) {
        return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE nif=?", new ContractRowMapper(), nif);
    }

    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract VALUES(?,?,?,?,?,?,?,?,?,?)", contract.getNcontract(), contract.getFirma(), contract.getDatos(), contract.getPrecio(),
                contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(), contract.getNif(), contract.getDnielderly());
    }

    public void deleteContract(String nif) {
        jdbcTemplate.update("DELETE FROM Contract WHERE nif=?", nif);
    }

    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET ncontract=?, firma=?, data=?, price=?, releaseDate=?, dateDown=?, quantity=?, description=?, dnielderly=?, WHERE nif=?", contract.getNcontract(),
                contract.getFirma(), contract.getDatos(), contract.getPrecio(), contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(),
                contract.getDnielderly(), contract.getNif());
    }
}
