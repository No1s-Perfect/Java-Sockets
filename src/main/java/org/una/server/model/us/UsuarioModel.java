package org.una.server.model.us;
import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.UsuarioDBA;

public class UsuarioModel {
    private static UsuarioModel instance = null;

    private final UsuarioDBA dba;

    private UsuarioModel() {
        this.dba = UsuarioDBA.getInstance();
    }

    public static UsuarioModel getInstance() {
        if (instance == null)
            instance = new UsuarioModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer id, Integer rol, String clave, String nombre) throws SQLException {
        this.dba.create(id, rol, clave, nombre);
    }

    public void update(Integer id, Integer rol, String clave, String nombre) throws SQLException {
        this.dba.update(id, rol, clave, nombre);
    }

    public void delete(Integer id) throws SQLException {
        this.dba.delete(id);
    }
}
