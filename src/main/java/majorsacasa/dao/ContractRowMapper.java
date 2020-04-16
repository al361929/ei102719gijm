package majorsacasa.dao;

import majorsacasa.model.Contract;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ContractRowMapper implements RowMapper<Contract> {
    @Override
    public Contract mapRow(ResultSet rs, int i) throws SQLException {
        Contract contract = new Contract();
        contract.setIdContract(rs.getInt("idContract"));
        contract.setFirma(rs.getString("firma"));
        contract.setReleaseDate(rs.getObject("releasedate", LocalDate.class));
        contract.setDateDown(rs.getObject("datedown", LocalDate.class));
        contract.setCantidad(rs.getInt("quantity"));
        contract.setDescripcion(rs.getString("description"));
        contract.setNifcompany(rs.getString("nif"));
        contract.setDnielderly(rs.getString("dni"));
        contract.setContractPDF(rs.getBoolean("pdf"));
        return contract;
    }
}
