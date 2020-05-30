package majorsacasa.dao;

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

    public ValoracionService getValoracion(int idService, String dniElderly) {
        return jdbcTemplate.queryForObject("SELECT * FROM ServiceValoration WHERE dni=? AND idService=?", new ValoracionServiceRowMapper(), dniElderly, idService);

    }

    //añadir
    public void addValoracion(ValoracionService valoracionService) {
        jdbcTemplate.update("INSERT INTO ServiceValoration VALUES(?,?,?,?,?)", valoracionService.getDni(), valoracionService.getIdService(), valoracionService.getComments(), valoracionService.getValoration(), LocalDate.now());
    }

    //eliminar
    public void deleteValoracion(int idService, String dniElderly) {
        jdbcTemplate.update("DELETE FROM ServiceValoration WHERE dni=? AND idService=?", dniElderly, idService);
    }

    //editar
    public void updateValoracion(ValoracionService valoracionService) {
        jdbcTemplate.update("UPDATE ServiceValoration SET comments=?, valoration=?, dateValoration=? WHERE idService=? AND dni=?", valoracionService.getComments(), valoracionService.getValoration(),
                LocalDate.now(), valoracionService.getIdService(), valoracionService.getDni());
    }

    public List<ValoracionService> getMisValoraciones(String dni) {
        try {
            return jdbcTemplate.query("SELECT * FROM ServiceValoration WHERE dni=?", new ValoracionServiceRowMapper(), dni);
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

    public boolean checkValoracion(String dniElderly, int idService) {
        try {

            List<String> valoracion = jdbcTemplate.queryForList(" SELECT dni FROM ServiceValoration WHERE dni=? AND idService=?", String.class, dniElderly, idService);
            return valoracion.isEmpty();
        } catch (EmptyResultDataAccessException e) {

            return true;
        }

    }

    public HashMap<String, Float> getPromedio() {
        List<String> key = jdbcTemplate.queryForList("SELECT idService FROM ServiceValoration GROUP BY idService", String.class);
        List<String> value = jdbcTemplate.queryForList("SELECT avg(valoration) FROM ServiceValoration GROUP BY idService", String.class);
        HashMap<String, Float> promedio = new HashMap<>();
        for (int i = 0; i < key.size(); i++) {
            promedio.put(key.get(i), Float.parseFloat(value.get(i)));

        }

        return promedio;
    }

    public HashMap<Integer, String> getServices() {

        List<String> id = jdbcTemplate.queryForList("SELECT idService FROM Service", String.class);
        List<String> descripcion = jdbcTemplate.queryForList("SELECT description FROM Service", String.class);


        HashMap<Integer, String> info = new HashMap<>();
        for (int i = 0; i < id.size(); i++) {
            int cod = Integer.parseInt(id.get(i));
            info.put(cod, descripcion.get(i));
        }

        return info;
    }
}
