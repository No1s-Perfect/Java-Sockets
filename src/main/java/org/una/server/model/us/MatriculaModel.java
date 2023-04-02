package org.una.server.model.us;
import java.sql.SQLException;
import java.util.Optional;

import org.json.JSONArray;
import org.una.server.data.us.MatriculaDBA;
public class MatriculaModel {
    private static MatriculaModel instance = null;

    private final MatriculaDBA dba;

    private MatriculaModel() {
        this.dba = MatriculaDBA.getInstance();
    }

    public static MatriculaModel getInstance() {
        if (instance == null)
            instance = new MatriculaModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer alumno, Integer grupo, Integer nota)
            throws SQLException {
        this.dba.create(alumno, grupo, nota);
    }

    public void update(Integer id, Integer alumno, Integer grupo, Integer nota)
            throws SQLException {
        this.dba.update(id, alumno, grupo, nota);
    }

    public void delete(Integer id) throws SQLException {
        this.dba.delete(id);
    }
}
