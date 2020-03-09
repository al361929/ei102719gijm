package majorsacasa.dao;

import majorsacasa.model.Company;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CompanyRowMapper implements RowMapper<Company> {
    @Override
    public Company mapRow(ResultSet rs, int i) throws SQLException {
        Company company = new Company();
        company.setNombre(rs.getString("name"));
        company.setNombreResponsable(rs.getString("responsiblename"));
        company.setDireccion(rs.getString("address"));
        company.setNif(rs.getString("nif"));
        company.setNumeroTelf(rs.getString("phonenumber"));
        company.setNombreUsuario(rs.getString("user_name"));
        company.setPassword(rs.getString("password"));
        company.setFechaAlta(rs.getDate("releasedate"));
        company.setCuentaBancaria(rs.getString("bankaccount"));

        return company;
    }
}





