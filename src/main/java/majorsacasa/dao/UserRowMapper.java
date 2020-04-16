package majorsacasa.dao;

import majorsacasa.model.UserDetails;
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
        try {
            user.setDni(rs.getString("dni"));
        }catch(Exception e){
            try {
                user.setDni(rs.getString("nif"));
            }catch(Exception ex){
                try {
                    user.setDni(rs.getString("dnivolunteer"));
                }catch(Exception ex1){
                    user.setDni(rs.getString("dnisocialworker"));

                }
            }

            }


        return user;
    }
}



