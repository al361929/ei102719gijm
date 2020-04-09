package majorsacasa.dao;

import majorsacasa.model.SocialWorker;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SocialWorkerRowMapper implements RowMapper<SocialWorker> {
    @Override
    public SocialWorker mapRow(ResultSet rs, int i) throws SQLException {
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setDni(rs.getString("dni"));
        socialWorker.setNombre(rs.getString("name"));
        socialWorker.setApellidos(rs.getString("surname"));
        socialWorker.setTelefono(rs.getString("phoneNumber"));
        socialWorker.setUsuario(rs.getString("user_name"));
        socialWorker.setContrasena(rs.getString("password"));
        socialWorker.setEmail(rs.getString("email"));
        return socialWorker;
    }
}
