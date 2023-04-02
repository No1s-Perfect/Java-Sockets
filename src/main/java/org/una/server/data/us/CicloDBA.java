package org.una.server.data.us;

import oracle.jdbc.OracleTypes;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;
public class CicloDBA {
    private static CicloDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static CicloDBA getInstance() {
        if (instance == null)
            instance = new CicloDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {
        var query = connection.prepareCall("{ ? = call listarciclos() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("id", rs.getInt("id"));
            object.put("numero", rs.getInt("numero"));
            object.put("fechaInicio", rs.getString("fechaInicio"));
            object.put("fechaFin", rs.getString("fechaFin"));
            object.put("activo", rs.getInt("activo"));
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

    public void create(Integer numero, String fechaInicio, String fechaFin, Integer activo) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarciclo(?, ?, ?, ? )");
        query.setInt(1, numero);
        query.setString(2, fechaInicio);
        query.setString(3, fechaFin);
        query.setInt(4, activo);
        query.execute();
        query.close();
    }

    public void update(Integer id, Integer numero, String fechaInicio, String fechaFin, Integer activo)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarciclo( ?, ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setInt(2, numero);
        query.setString(3, fechaInicio);
        query.setString(4, fechaFin);
        query.setInt(5, activo);
        query.execute();
        query.close();
    }

    public void delete(Integer id) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarciclo( ? )");
        query.setInt(1, id);
        query.execute();
        query.close();
    }

    public JSONObject getCursoSingle(Integer id) throws SQLException {
        var query = connection.prepareCall("{ ? = call buscarciclo( ? ) }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.setInt(2, id);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var object = new JSONObject();
        while (rs.next()) {
            
            object.put("id", rs.getInt("id"));
            object.put("numero", rs.getInt("numero"));
            object.put("fechaInicio", rs.getString("fechaInicio"));
            object.put("fechaFin", rs.getString("fechaFin"));
            object.put("activo", rs.getInt("activo"));
            // get active
        }
        rs.close();
        query.close();
        return object;
    }
}
