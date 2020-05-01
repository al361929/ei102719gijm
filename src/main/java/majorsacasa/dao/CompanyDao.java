package majorsacasa.dao;

import majorsacasa.model.Company;
import majorsacasa.model.Contract;
import majorsacasa.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyDao extends GeneralDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Company> getCompanies() {
        try {
            return jdbcTemplate.query("Select * From Company Where not nif='0'", new CompanyRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Company>();
        }
    }

    public Company getCompany(String nif) {
        return jdbcTemplate.queryForObject("SELECT * FROM Company WHERE nif=?", new CompanyRowMapper(), nif);
    }

    public void addCompany(Company company) {
        jdbcTemplate.update("INSERT INTO Company VALUES(?,?,?,?,?,?,?,?,?,?)", company.getNombre(), company.getNombreResponsable(), company.getDireccion(),
                company.getNif(), company.getNumeroTelf(), company.getNombreUsuario(), company.getPassword(), LocalDate.now(), company.getCuentaBancaria(), company.getEmail());
    }

    public void deleteCompany(String nif) {
        jdbcTemplate.update("DELETE FROM Company WHERE nif=?", nif);
    }

    public void updateCompany(Company company) {
        jdbcTemplate.update("UPDATE Company SET name=?, responsiblename=?, address=?, phonenumber=?, user_name=?, password=?, releaseDate=?, bankaccount=?, email=? WHERE nif=?", company.getNombre(), company.getNombreResponsable(), company.getDireccion(),
                company.getNumeroTelf(), company.getNombreUsuario(), company.getPassword(), company.getFechaAlta(), company.getCuentaBancaria(), company.getEmail(), company.getNif());
    }

    public Boolean checkCompany(String nif) {
        List<String> compañias = jdbcTemplate.queryForList("SELECT nif FROM Company", String.class);
        return compañias.contains(nif);
    }


    public List<Service> getServiceList(String nif) {
        try {
            return jdbcTemplate.query("SELECT s.* FROM service AS s JOIN offers AS o USING(idservice) WHERE nif=?", new ServiceRowMapper(), nif);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public List<Contract> getContractsList(String nif) {
        try {
            return jdbcTemplate.query("SELECT * FROM contract WHERE nif=?", new ContractRowMapper(), nif);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Contract>();
        }
    }

    public List<Company> getCompanyServiceOffer(int idservicio) {
        try {
            return jdbcTemplate.query("SELECT c.* FROM company AS c JOIN offers AS o USING(nif) WHERE o.idService=?", new CompanyRowMapper(), idservicio);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Company>();
        }
    }

    public Boolean checkDNI(String dni){
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where dni=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where dnivolunteer=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where dnisocialworker=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where nif=?", String.class,dni);
        if (!dnis.isEmpty()) return false;
        return true;

    }
    public Boolean checkUser(String user){
        List<String> dnis = jdbcTemplate.queryForList("SELECT dni FROM ElderlyPeople where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnivolunteer FROM volunteer where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT dnisocialworker FROM socialworker where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        dnis = jdbcTemplate.queryForList("SELECT nif FROM company where user_name=?", String.class,user);
        if (!dnis.isEmpty()) return false;
        return true;

    }

}
