package majorsacasa.dao;

import majorsacasa.model.UserDetails;
import majorsacasa.model.Valoracion;
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

public class ValoracionDao extends GeneralDao{
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
        jdbcTemplate.update("DELETE FROM VolunteerValoration  WHERE dni=? AND dniVolunteer=?", dniElderly, dniVolunteer);
    }

    //editar
    public void updateValoracion(Valoracion valoracion) {
        jdbcTemplate.update("UPDATE VolunteerValoration SET comments=?, valoration=?, dateValoration=? WHERE dniVolunteer=? AND dni=?", valoracion.getComments(), valoracion.getValoration(),
                LocalDate.now(), valoracion.getDniVolunteer(), valoracion.getDni());
    }

    public List<Valoracion> getMisValoraciones(String dniVolunteer) {
        try {
            return jdbcTemplate.query("SELECT * FROM volunteerValoration WHERE dnivolunteer=?", new ValoracionRowMapper(), dniVolunteer);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Valoracion>();
        }
    }
    public List<Valoracion> getMisValoracionesElderly(String dni) {
        try {
            return jdbcTemplate.query("SELECT * FROM volunteerValoration WHERE dni=?", new ValoracionRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Valoracion>();
        }
    }
    public boolean checkValoracion(String dniElderly,String dniVolunter){
        List<String> valoracion = jdbcTemplate.queryForList(" select dni from volunteerValoration where dni=? and dnivolunteer=?", String.class,dniElderly,dniVolunter);

        return valoracion.isEmpty();
    }
    public HashMap<String,Float> getPromedio(){
        List<String> key = jdbcTemplate.queryForList("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer", String.class);
        List<String> value = jdbcTemplate.queryForList("select avg(valoration) from volunteerValoration GROUP BY dniVolunteer", String.class);
        HashMap <String,Float> promedio=new HashMap<>();
        for (int i=0; i<key.size();i++){
            promedio.put(key.get(i),Float.parseFloat(value.get(i)));

        }
       //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer","select avg(valoration) from volunteerValoration GROUP BY dniVolunteer");
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer, avg(valoration) from volunteerValoration GROUP BY dniVolunteer");

        return  promedio;
    }
    public HashMap<String,String> getUsersInfo(){

        List<UserDetails> usuarios = jdbcTemplate.query("SELECT user_name, password,name, dnivolunteer FROM Volunteer", new UserRowMapper());

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, dni,name FROM ElderlyPeople", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password,dnisocialworker,name FROM SocialWorker ", new UserRowMapper()));

        usuarios.addAll(jdbcTemplate.query("SELECT user_name, password, nif,name FROM Company", new UserRowMapper()));

        HashMap <String,String> info=new HashMap<>();
        for (UserDetails u: usuarios){
            info.put(u.getDni(),u.getName());

        }

        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer from volunteerValoration GROUP BY dniVolunteer","select avg(valoration) from volunteerValoration GROUP BY dniVolunteer");
        //HashMap<String, Object> promedio = (HashMap<String, Object>) jdbcTemplate.queryForMap("select dniVolunteer, avg(valoration) from volunteerValoration GROUP BY dniVolunteer");

        return  info;
    }
}
