package majorsacasa.dao;

import majorsacasa.model.VolunteerTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public final class VolunteerTimeRowMapper implements RowMapper<VolunteerTime> {

    @Override
    public VolunteerTime mapRow(ResultSet rs, int i) throws SQLException {
        VolunteerTime volunteerTime = new VolunteerTime();
        volunteerTime.setIdVolunteerTime(rs.getInt("idVolunteerTime"));
        volunteerTime.setDniVolunteer(rs.getString("dniVolunteer"));
        volunteerTime.setDniElderly(rs.getString("dni"));
        volunteerTime.setMes(rs.getString("mes"));
        volunteerTime.setDia(rs.getInt("dia"));
        volunteerTime.setStartTime(rs.getObject("startTime", LocalTime.class));
        volunteerTime.setEndTime(rs.getObject("endTime", LocalTime.class));
        volunteerTime.setAvailability(rs.getString("availability"));
        return volunteerTime;
    }
}
