package majorsacasa.dao;

import majorsacasa.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public Contract getContract(Integer idContract) {
        return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE idContract=?", new ContractRowMapper(), idContract);
    }

    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract VALUES(DEFAULT,?,?,?,?,?,?,?)", contract.getFirma(), LocalDate.now(), contract.getDateDown(),
                contract.getCantidad(), contract.getDescripcion(), contract.getNifcompany(), contract.getContractPDF());
    }

    public void deleteContract(Integer idContract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE idContract=?", idContract);
    }

    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET  nif=?, firma=?, releaseDate=?, dateDown=?, quantity=?, description=?, pdf=? WHERE idContract=?",
                contract.getNifcompany(), contract.getFirma(), contract.getReleaseDate(), contract.getDateDown(), contract.getCantidad(), contract.getDescripcion(), contract.getContractPDF(), contract.getIdContract());
    }

    public void uploadPDF(Integer idContract, Boolean pdf) {
        jdbcTemplate.update("UPDATE Contract SET pdf=? WHERE idContract=?", pdf, idContract);
    }

    public List<Contract> getElderlyList(String dni) {
        try {
            return jdbcTemplate.query("SELECT * FROM contract WHERE dni=?", new ContractRowMapper(), dni);
        } catch (EnumConstantNotPresentException e) {
            return new ArrayList<Contract>();
        }
    }

    public int getUltimoContrato() {
        List<String> ids = jdbcTemplate.queryForList("Select MAX(idcontract) from Contract;", String.class);
        int id = Integer.parseInt(ids.get(0));
        return id;
    }

    public List<Contract> getContractCompanyActive(String nif) {
        try {
            return jdbcTemplate.query("SELECT * FROM contract WHERE nif=? AND datedown IS NOT NULL", new ContractRowMapper(), nif);
        } catch (EnumConstantNotPresentException e) {
            return new ArrayList<Contract>();
        }
    }
}
