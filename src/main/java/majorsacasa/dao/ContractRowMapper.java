package majorsacasa.dao;

import majorsacasa.model.Contract;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ContractRowMapper implements RowMapper<Contract> {
    @Override
    public Contract mapRow(ResultSet rs, int i) throws SQLException {
        Contract contract = new Contract();
        contract.setNcontract(rs.getInt("ncontract"));
        contract.setFirma(rs.getString("firma"));
        contract.setDatos(rs.getString("data"));
        contract.setPrecio(rs.getDouble("price"));
        contract.setReleaseDate(rs.getDate("releasedate"));
        contract.setDateDown(rs.getDate("datedown"));
        contract.setCantidad(rs.getInt("quantity"));
        contract.setDescripcion(rs.getString("description"));
        contract.setNifcompany(rs.getString("nifcompany"));
        contract.setDnielderly(rs.getString("dnielderly"));
        return contract;
    }
}
