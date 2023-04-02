package org.una.server.model.us;
import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.CicloDBA;
public class CicloModel {
    private static CicloModel instance = null;

    private final CicloDBA dba;

    private CicloModel() {
        this.dba = CicloDBA.getInstance();
    }

    public static CicloModel getInstance() {
        if (instance == null)
            instance = new CicloModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer numero, String fechaInicio, String fechaFin, Integer activo) throws SQLException {
        this.dba.create(numero, fechaInicio, fechaFin, activo);
    }

    public void update(Integer id, Integer numero, String fechaInicio, String fechaFin, Integer activo)
            throws SQLException {
        this.dba.update(id, numero, fechaInicio, fechaFin, activo);
    }

    public void delete(Integer id) throws SQLException {
        this.dba.delete(id);
    }
}
