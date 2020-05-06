package majorsacasa.dao;

import majorsacasa.model.Elderly;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ElderlyRowMapper implements RowMapper<Elderly> {
    @Override
    public Elderly mapRow(ResultSet rs, int i) throws SQLException {
        Elderly elderly = new Elderly();
        elderly.setNombre(rs.getString("name"));
        elderly.setApellidos(rs.getString("surname"));
        elderly.setDireccion(rs.getString("address"));
        elderly.setDni(rs.getString("dni"));
        elderly.setTelefono(rs.getString("phonenumber"));
        elderly.setAlergias(rs.getString("allergies"));
        elderly.setAlergias(rs.getString("allergies"));

        elderly.setCuentaBancaria(rs.getString("bankaccount"));
        elderly.setBirthday(rs.getObject("birthday", LocalDate.class));
        elderly.setDateDown(rs.getObject("datedown", LocalDate.class));
        elderly.setReleaseDate(rs.getObject("releasedate", LocalDate.class));
        elderly.setContraseña(rs.getString("password"));
        elderly.setUsuario(rs.getString("user_name"));
        elderly.setSocialWorker(rs.getString("dnisocialWorker"));
        elderly.setEmail(rs.getString("email"));
        return elderly;
    }
}
