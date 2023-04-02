package org.una.server.data.us;

import oracle.jdbc.OracleTypes;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class AlumnoDBA {
    private static AlumnoDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static AlumnoDBA getInstance() {
        if (instance == null)
            instance = new AlumnoDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {
        var query = connection.prepareCall("{ ? = call listarAlumnos() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("id", rs.getInt("id"));
            object.put("nombre", rs.getString("nombre"));
            object.put("telefono", rs.getString("telefono"));
            object.put("email", rs.getString("email"));
            object.put("fechaNacimiento", rs.getString("fechaNacimiento"));
            object.put("carrera", rs.getString("carrera"));
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

    public void create(Integer id, String nombre, String telefono, String email, String fechaNacimiento, String carrera)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarAlumno( ?, ?, ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setString(2, nombre);
        query.setString(3, telefono);
        query.setString(4, email);
        query.setString(5, fechaNacimiento);
        query.setString(6, carrera);
        query.execute();
        query.close();
    }

    public void update(Integer id, String nombre, String telefono, String email, String fechaNacimiento, String carrera)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarAlumno( ?, ?, ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setString(2, nombre);
        query.setString(3, telefono);
        query.setString(4, email);
        query.setString(5, fechaNacimiento);
        query.setString(6, carrera);
        query.execute();
        query.close();
    }

    public void delete(Integer id) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarAlumno( ? )");
        query.setInt(1, id);
        query.execute();
        query.close();
    }

}
