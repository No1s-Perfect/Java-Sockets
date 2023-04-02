package org.una.server.data.us;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class CursoDBA {
    private static CursoDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static CursoDBA getInstance() {
        if (instance == null)
            instance = new CursoDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {

        var query = connection.prepareCall("{ ? = call listarCursos() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("id", rs.getString("id"));
            object.put("nombre", rs.getString("nombre"));
            object.put("creditos", rs.getInt("creditos"));
            object.put("horas", rs.getInt("horas"));
            object.put("carrera", rs.getString("carrera"));
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

    public void create(String identifier, String name, Integer creditos, Integer horas, String carrera)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarCurso( ?, ?, ?, ?, ? )");
        query.setString(1, identifier);
        query.setString(2, name);
        query.setInt(3, creditos);
        query.setInt(4, horas);
        query.setString(5, carrera);
        query.execute();
        query.close();
    }

    public void update(String identifier, String name, Integer creditos, Integer horas) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarCurso( ?, ?, ?, ? )");
        query.setString(1, identifier);
        query.setString(2, name);
        query.setInt(3, creditos);
        query.setInt(4, horas);
        query.execute();
        query.close();
    }

    public void delete(String identifier) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarCurso( ? )");
        query.setString(1, identifier);
        query.execute();
        query.close();
    }

    public String getCourseNameById(String id) throws SQLException {
        return this.getCourseById(id).getString("nombre");

    }

    public JSONObject getCourseById(String id) throws SQLException {
        var query = connection.prepareCall("{ ? = call buscarCurso( ? ) }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.setString(2, id);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var object = new JSONObject();
        while (rs.next()) {

            object.put("id", rs.getString("id"));
            object.put("nombre", rs.getString("nombre"));
            object.put("carrera", rs.getString("carrera"));

        }
        rs.close();
        query.close();
        return object;

    }

}
