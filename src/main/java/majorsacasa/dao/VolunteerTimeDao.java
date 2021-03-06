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
                    "SELECT * FROM VolunteerTime WHERE availability = 'True'",
                    new VolunteerTimeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public void addVolunteerTime(VolunteerTime volunteerTime) {
        jdbcTemplate.update("INSERT INTO VolunteerTime VALUES(DEFAULT,?,?,?,?,?,?,?)", volunteerTime.getMes(), volunteerTime.getDia(),
                volunteerTime.getStartTime(), volunteerTime.getEndTime(), volunteerTime.getAvailability(), volunteerTime.getDniVolunteer(), volunteerTime.getDniElderly());

    }

    public VolunteerTime getVolunteerTime(Integer idVolunteerTime) {
        return jdbcTemplate.queryForObject("SELECT * FROM VolunteerTime WHERE idVolunteerTime=?", new VolunteerTimeRowMapper(), idVolunteerTime);
    }

    public void updateVolunteerTime(VolunteerTime volunteerTime) {
        jdbcTemplate.update("UPDATE VolunteerTime SET dniVolunteer=?, dni=?, endTime=?, availability=?, mes=?, dia=?, startTime=? WHERE idVolunteerTime=?", volunteerTime.getDniVolunteer(), volunteerTime.getDniElderly(), volunteerTime.getEndTime(),
                volunteerTime.getAvailability(), volunteerTime.getMes(), volunteerTime.getDia(), volunteerTime.getStartTime(), volunteerTime.getIdVolunteerTime());
    }

    public void deleteVolunteerTime(Integer idVolunteerTime) {
        jdbcTemplate.update("DELETE FROM VolunteerTime WHERE idVolunteerTime=?", idVolunteerTime);
    }

    public List<VolunteerTime> getScheduleList(String dnivolunteer) {
        try {
            return jdbcTemplate.query("Select * From volunteertime Where dniVolunteer = ? AND availability = 'True' ", new VolunteerTimeRowMapper(), dnivolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public List<VolunteerTime> getScheduleListDisponibles(String dnivolunteer) {
        try {
            return jdbcTemplate.query("Select * From volunteertime Where dniVolunteer = ? AND dni=null AND availability = 'True'", new VolunteerTimeRowMapper(), dnivolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<VolunteerTime>();
        }
    }

    public void solicitar(Integer id, String dni) {
        jdbcTemplate.update("UPDATE VolunteerTime SET  dni=? WHERE idVolunteerTime=?", dni, id);
    }

    public int ultimoIdVolunteerTime() {
        List<String> ids = jdbcTemplate.queryForList("select MAX(idVolunteerTime) from VolunteerTime;", String.class);
        int id = Integer.parseInt(ids.get(0));
        return id;
    }
}
