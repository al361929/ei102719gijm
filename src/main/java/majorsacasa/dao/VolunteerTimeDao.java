package majorsacasa.dao;

import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalTime;
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
        jdbcTemplate.update("INSERT INTO VolunteerTime VALUES(?,?,?,?,?,?,?)", volunteerTime.getMes(), volunteerTime.getDia(),
                volunteerTime.getStartTime(), volunteerTime.getEndTime(), volunteerTime.getAvailability(), volunteerTime.getDniVolunteer(), volunteerTime.getDniElderly());

    }

    public VolunteerTime getVolunteerTime(String dniVolunteer, String mes, Integer dia, LocalTime startTime) {
        return jdbcTemplate.queryForObject("SELECT * FROM VolunteerTime WHERE dniVolunteer=? AND mes=? AND dia=? AND startTime=?", new VolunteerTimeRowMapper(), dniVolunteer, mes, dia, startTime);
    }

    public void updateVolunteerTime(VolunteerTime volunteerTime) {
        System.out.println(volunteerTime.toString());
        jdbcTemplate.update("UPDATE VolunteerTime SET dniElderly=?, endTime=?, availability=?, mes=?, dia=?, startTime=? WHERE dniVolunteer=? AND mes=? AND dia=? AND startTime=?", volunteerTime.getDniElderly(), volunteerTime.getEndTime(),
                volunteerTime.getAvailability(), volunteerTime.getMes(), volunteerTime.getDia(), volunteerTime.getStartTime(), volunteerTime.getDniVolunteer(), volunteerTime.getMes(), volunteerTime.getDia(), volunteerTime.getStartTime());
    }

    public void deleteVolunteerTime(String dniVolunteer, String mes, Integer dia, LocalTime startTime) {
        jdbcTemplate.update("DELETE FROM VolunteerTime WHERE dniVolunteer=? AND mes=? AND dia=? AND startTime=?", dniVolunteer, mes, dia, startTime);
    }
}
