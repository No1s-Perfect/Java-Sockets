package org.una.server.data.us;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
public class ProfesorDBA {
    private static ProfesorDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static ProfesorDBA getInstance() {
        if (instance == null) instance = new ProfesorDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException{
        var query = connection.prepareCall("{ ? = call listarProfesors() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("id", rs.getInt("id"));
            object.put("nombre", rs.getString("nombre"));
            object.put("telefono", rs.getInt("telefono"));
            object.put("email", rs.getString("email"));
            UsuarioDBA.getInstance().getUserById(rs.getInt("id")).ifPresent(obj -> {
                var json = (JSONObject) obj;
                object.put("clave", json.getString("clave"));
            });
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

    public void create(Integer identifier, String name, Integer telefono, String email) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarProfesor( ?, ?, ?, ? )");
        query.setInt(1, identifier);
        query.setString(2, name);
        query.setInt(3, telefono);
        query.setString(4,email);
        query.execute();
        query.close();
    }


    public void update(Integer identifier, String name, Integer telefono, String email) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarProfesor( ?, ?, ?, ? )");
        query.setInt(1, identifier);
        query.setString(2, name);
        query.setInt(3, telefono);
        query.setString(4, email);
        query.execute();
        query.close();
    }

    public void delete(Integer identifier) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarProfesor( ? )");
        query.setInt(1, identifier);
        query.execute();
        query.close();
    }
}
