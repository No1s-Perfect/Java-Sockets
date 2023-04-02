package org.una.server.model.us;
import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.CarreraDBA;

public class CarreraModel {
    private static CarreraModel instance = null;

    private final CarreraDBA dba;

    private CarreraModel() {
        this.dba = CarreraDBA.getInstance();
    }

    public static CarreraModel getInstance() {
        if (instance == null)
            instance = new CarreraModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(String codigo, String nombre, String titulo) throws SQLException {
        this.dba.create(codigo, nombre, titulo);
    }

    public void update(String codigo, String nombre, String titulo) throws SQLException {
        this.dba.update(codigo, nombre, titulo);
    }

    public void delete(String codigo) throws SQLException {
        this.dba.delete(codigo);
    }
}
