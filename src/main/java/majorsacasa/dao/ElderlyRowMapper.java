package majorsacasa.dao;

import majorsacasa.model.Elderly;
import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


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
        elderly.setCuentaBancaria(rs.getString("bankaccount"));
        elderly.setBirthday(rs.getDate("brithday").toLocalDate());
        elderly.setDataDown(rs.getDate("dateDown").toLocalDate());
        elderly.setRealaseDate(rs.getDate("realasedate").toLocalDate());
        elderly.setContraseña(rs.getString("password"));
        elderly.setUsuario(rs.getString("user_Name"));

        return elderly;
    }
}
