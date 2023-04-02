package org.una.server.model.us;

import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.ProfesorDBA;

public class ProfesorModel {
    private static ProfesorModel instance = null;

    private final ProfesorDBA dba;

    private ProfesorModel() {
        this.dba = ProfesorDBA.getInstance();
    }

    public static ProfesorModel getInstance() {
        if (instance == null) instance = new ProfesorModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer identifier, String name, Integer telefono, String email) throws SQLException {
        this.dba.create(identifier,name,telefono,email);
    }

    public void update(Integer identifier, String name, Integer telefono, String email) throws SQLException {
        this.dba.update(identifier, name, telefono, email);
    }

    public void delete(Integer id) throws SQLException {
        this.dba.delete(id);
    }
    
}
