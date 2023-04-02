package org.una.server.model.us;
import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.AlumnoDBA;
public class AlumnoModel {
    private static AlumnoModel instance = null;

    private final AlumnoDBA dba;

    private AlumnoModel() {
        this.dba = AlumnoDBA.getInstance();
    }

    public static AlumnoModel getInstance() {
        if (instance == null)
            instance = new AlumnoModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer id, String nombre, String telefono, String email, String fechaNacimiento, String carrera)
            throws SQLException {
        this.dba.create(id, nombre, telefono, email, fechaNacimiento, carrera);
    }

    public void update(Integer id, String nombre, String telefono, String email, String fechaNacimiento, String carrera)
            throws SQLException {
        this.dba.update(id, nombre, telefono, email, fechaNacimiento, carrera);
    }

    public void delete(Integer id) throws SQLException {
        this.dba.delete(id);
    }
}
