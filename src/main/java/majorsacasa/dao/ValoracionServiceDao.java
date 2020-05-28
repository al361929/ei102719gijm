package majorsacasa.dao;

import majorsacasa.model.UserDetails;
import majorsacasa.model.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ValoracionServiceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ValoracionService> getValoraciones() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM ServiceValoration",
                    new ValoracionServiceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ValoracionService>();
        }
    }

    public ValoracionService getValoracion(String idService, String dniElderly) {
        return jdbcTemplate.queryForObject("SELECT * FROM ServiceValoration WHERE dni=? AND idService=?", new ValoracionServiceRowMapper(), dniElderly, idService);

    }

    //añadir
    public void addValoracion(ValoracionService valoracionService) {
        jdbcTemplate.update("INSERT INTO ServiceValoration VALUES(?,?,?,?,?)", valoracionService.getIdService(), valoracionService.getDni(), valoracionService.getComments(), valoracionService.getValoration(), LocalDate.now());
    }

    //eliminar
    public void deleteValoracion(String idService, String dniElderly) {
        jdbcTemplate.update("DELETE FROM ServiceValoration WHERE dni=? AND idService=?", dniElderly, idService);
    }

    //editar
    public void updateValoracion(ValoracionService valoracionService) {
        jdbcTemplate.update("UPDATE ServiceValoration SET comments=?, valoration=?, dateValoration=? WHERE idService=? AND dni=?", valoracionService.getComments(), valoracionService.getValoration(),
                LocalDate.now(), valoracionService.getIdService(), valoracionService.getDni());
    }

    public List<ValoracionService> getMisValoraciones(String idService) {
        try {
            return jdbcTemplate.query("SELECT * FROM ServiceValoration WHERE idService=?", new ValoracionServiceRowMapper(), idService);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ValoracionService>();
        }
    }

    public List<ValoracionService> getMisValoracionesElderly(String dni) {
        try {
            return jdbcTemplate.query("SELECT * FROM ServiceValoration WHERE dni=?", new ValoracionServiceRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<ValoracionService>();
        }
    }

    public boolean checkValoracion(String dniElderly, String idService) {
        List<String> valoracion = jdbcTemplate.queryForList(" SELECT dni FROM ServiceValoration WHERE dni=? AND idService=?", String.class, dniElderly, idService);

        return valoracion.isEmpty();
    }

    public HashMap<String, Float> getPromedio() {
        List<String> key = jdbcTemplate.queryForList("SELECT idService FROM ServiceValoration GROUP BY idService", String.class);
        List<String> value = jdbcTemplate.queryForList("SELECT avg(valoration) FROM ServiceValoration GROUP BY idService", String.class);
        HashMap<String, Float> promedio = new HashMap<>();
        for (int i = 0; i < key.size(); i++) {
            promedio.put(key.get(i), Float.parseFloat(value.get(i)));

        }
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer","select avg(valoration) from volunteerValoration GROUP BY dniVolunteer");
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer, avg(valoration) from volunteerValoration GROUP BY dniVolunteer");

        return promedio;
    }

    public HashMap<String, String> getUsersInfo() {

        List<UserDetails> usuarios = jdbcTemplate.query("SELECT user_name, password,name, dnivolunteer FROM Volunteer", new UserRowMapper());

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, dni,name FROM ElderlyPeople", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password,dnisocialworker,name FROM SocialWorker ", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, nif,name FROM Company", new UserRowMapper()));

        HashMap<String, String> info = new HashMap<>();
        for (UserDetails u : usuarios) {
            info.put(u.getDni(), u.getName());
        }

        info.put("0", " ");

        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer","select avg(valoration) from volunteerValoration GROUP BY dniVolunteer");
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer, avg(valoration) from volunteerValoration GROUP BY dniVolunteer");

        return info;
    }
}
