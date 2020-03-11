package majorsacasa.dao;

import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VolunteerTimeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<VolunteerTime> getVolunteersTime() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM VolunteerTime",
                    new VolunteerTimeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public void addVolunteerTime(VolunteerTime volunteerTime) {
        jdbcTemplate.update("INSERT INTO VolunteerTime VALUES(?,?,?,?,?,?", volunteerTime.getDniVolunteer(), volunteerTime.getMes(), volunteerTime.getDia(),
                volunteerTime.getStartTime(), volunteerTime.getEndTime(), volunteerTime.getAvailability());

    }

    public VolunteerTime getVolunteerTime(String dniVolunteerTime) {
        return jdbcTemplate.queryForObject("SELECT * FROM VolunteerTime WHERE dni_v=?", new VolunteerTimeRowMapper(), dniVolunteerTime);
    }

    public void updateVolunteerTime(VolunteerTime volunteerTime) {
        jdbcTemplate.update("UPDATE VolunteerTime SET mes=?, dia=?, startTime=?, endTime=?, availability=? WHERE dni_v=?",
                volunteerTime.getMes(), volunteerTime.getDia(), volunteerTime.getStartTime(), volunteerTime.getEndTime(), volunteerTime.getAvailability(), volunteerTime.getDniVolunteer());
    }

    public void deleteVolunteerTime(String dniVolunteerTime) {
        jdbcTemplate.update("DELETE FROM SocialWorker WHERE dni_v=?", dniVolunteerTime);
    }
}
