package majorsacasa.dao;

import majorsacasa.model.UserDetails;
import majorsacasa.model.Volunteer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class UserRowMapper implements RowMapper<UserDetails> {
    @Override
    public UserDetails mapRow(ResultSet rs, int i) throws SQLException {
        UserDetails user = new UserDetails();
        user.setUsername(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        return user;
    }
}



