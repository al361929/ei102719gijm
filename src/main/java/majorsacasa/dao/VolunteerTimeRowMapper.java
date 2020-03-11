package majorsacasa.dao;

import majorsacasa.model.VolunteerTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VolunteerTimeRowMapper implements RowMapper<VolunteerTime> {

    @Override
    public VolunteerTime mapRow(ResultSet rs, int i) throws SQLException {
        VolunteerTime volunteerTime = new VolunteerTime();
        volunteerTime.setDniVolunteer(rs.getString("dni_v"));
        volunteerTime.setMes(rs.getString("mes"));
        volunteerTime.setDia(rs.getInt("dia"));
        volunteerTime.setStartTime(rs.getTime("startTime"));
        volunteerTime.setEndTime(rs.getTime("endTime"));
        volunteerTime.setAvailability(rs.getString("availability"));
        return volunteerTime;
    }
}
