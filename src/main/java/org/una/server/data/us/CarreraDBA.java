package org.una.server.data.us;
import oracle.jdbc.OracleTypes;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;
public class CarreraDBA {
    
    private static CarreraDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static CarreraDBA getInstance() {
        if (instance == null)
            instance = new CarreraDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {
        var query = connection.prepareCall("{ ? = call listarCarreras() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("codigo", rs.getString("codigo"));
            object.put("nombre", rs.getString("nombre"));
            object.put("titulo", rs.getString("titulo"));
            resultJSON.put(object);
        }
        rs.close();
        query.close();
        return resultJSON;
    }

    public JSONArray getAll() throws SQLException {
        connection.setAutoCommit(false);
        return this.viewAll();
    }

    public void create(String codigo, String nombre, String titulo) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarCarrera( ?, ?, ? )");
        query.setString(1, codigo);
        query.setString(2, nombre);
        query.setString(3, titulo);
        query.execute();
        query.close();
    }

    public void update(String codigo, String nombre, String titulo) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarCarrera( ?, ?, ? )");
        query.setString(1, codigo);
        query.setString(2, nombre);
        query.setString(3, titulo);
        query.execute();
        query.close();
    }

    public void delete(String codigo) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarCarrera( ? )");
        query.setString(1, codigo);
        query.execute();
        query.close();
    }

}
