package org.una.server.model.us;

import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.CursoDBA;

public class CursoModel {
    private static CursoModel instance = null;

    private final CursoDBA dba;

    private CursoModel() {
        this.dba = CursoDBA.getInstance();
    }

    public static CursoModel getInstance() {
        if (instance == null) instance = new CursoModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }


    public void create(String identifier, String name, Integer creditos, Integer horas,String carrera) throws SQLException {
        this.dba.create(identifier, name, creditos, horas, carrera);
    }

    public void update(String identifier, String name, Integer creditos, Integer horas) throws SQLException {
        this.dba.update(identifier, name, creditos, horas);
    }

    public void delete(String id) throws SQLException {
        this.dba.delete(id);
    }
    
}
