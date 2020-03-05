package majorsacasa.dao;

import majorsacasa.model.Elderly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository

public class ElderlyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<Elderly> getElderlys() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM ElderlyPeople",
                    new ElderlyRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Elderly>();
        }
    }

    public void addElderly(Elderly elderly) {
        jdbcTemplate.update("INSERT INTO ElderlyPeople VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getDni(),
                elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(), elderly.getContraseña(), elderly.getReleaseDate(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria());
    }

    public Elderly getElderly(String elderly) {
        return jdbcTemplate.queryForObject("SELECT * FROM ElderlyPeople WHERE dni=?", new ElderlyRowMapper(), elderly);
    }

    public void updateElderly(Elderly elderly) {
        jdbcTemplate.update("UPDATE ElderlyPeople SET name=?, surname=?, address=?, allergies=?, phonenumber=?, user_name=?, password=?, releasedate=?, datedown=?, birthday=?, bankaccount=? WHERE dni=?",
                elderly.getNombre(), elderly.getApellidos(), elderly.getDireccion(), elderly.getAlergias(), elderly.getTelefono(), elderly.getUsuario(), elderly.getContraseña(),
                elderly.getReleaseDate(), elderly.getDateDown(), elderly.getBirthday(), elderly.getCuentaBancaria(), elderly.getDni());
    }

    public void deleteElderly(String elderly) {
        jdbcTemplate.update("DELETE FROM ElderlyPeople WHERE dni=?", elderly);
    }
}