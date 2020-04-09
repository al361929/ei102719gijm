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

    public Contract getContract(Integer ncontract) {
        return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE ncontract=?", new ContractRowMapper(), ncontract);
    }

    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract VALUES(DEFAULT,?,?,?,?,?,?,?,?)", contract.getFirma(), contract.getPrecio(),
                contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(), contract.getNifcompany(), contract.getDnielderly());
    }

    public void deleteContract(Integer ncontract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE idContract=?", ncontract);
    }

    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET nifcompany=?, firma=?, price=?, releaseDate=?, dateDown=?, quantity=?, description=? WHERE ncontract=?", contract.getNifcompany(),
                contract.getFirma(), contract.getPrecio(), contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(),
                contract.getNcontract());
    }
}
