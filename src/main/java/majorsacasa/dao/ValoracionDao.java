package majorsacasa.dao;

import majorsacasa.model.Valoracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository

public class ValoracionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Valoracion> getValoraciones() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM VolunteerValoration",
                    new ValoracionRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Valoracion>();
        }
    }

    public Valoracion getValoracion(String dniVolunteer, String dniElderly) {
        return jdbcTemplate.queryForObject("SELECT * FROM VolunteerValoration WHERE dni=? AND dniVolunteer=?", new ValoracionRowMapper(), dniElderly, dniVolunteer);

    }

    //añadir
    public void addValoracion(Valoracion valoracion) {
        jdbcTemplate.update("INSERT INTO VolunteerValoration VALUES(?,?,?,?,?)", valoracion.getDniVolunteer(), valoracion.getDni(), valoracion.getComments(), valoracion.getValoration(), LocalDate.now());
    }

    //eliminar
    public void deleteValoracion(String dniVolunteer, String dniElderly) {
        jdbcTemplate.update("DELETE FROM VolunteerValoration WHERE WHERE dni=? AND dniVolunteer=?", dniElderly, dniVolunteer);
    }

    //editar
    public void updateValoracion(Valoracion valoracion) {
        jdbcTemplate.update("UPDATE VolunteerValoration SET comments=?, valoration=?, dateValoration=? WHERE dniVolunteer=? AND dni=?", valoracion.getComments(), valoracion.getValoration(),
                LocalDate.now(), valoracion.getDniVolunteer(), valoracion.getDni());
    }

}
