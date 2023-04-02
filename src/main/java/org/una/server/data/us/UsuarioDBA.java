package org.una.server.data.us;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
public class UsuarioDBA {
    private static UsuarioDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static UsuarioDBA getInstance() {
        if (instance == null)
            instance = new UsuarioDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {
        var query = connection.prepareCall("{ ? = call listarusuario() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("id", rs.getInt("id"));
            object.put("rol", rs.getInt("rol"));
            object.put("clave", rs.getString("clave"));
            object.put("nombre", rs.getString("nombre"));
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

    public void create(Integer id, Integer rol, String clave, String nombre) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarusuario( ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setInt(2, rol);
        query.setString(3, clave);
        query.setString(4, nombre);
        query.execute();
        query.close();
    }

    public void update(Integer id, Integer rol, String clave, String nombre) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarusuario( ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setInt(2, rol);
        query.setString(3, clave);
        query.setString(4, nombre);
        query.execute();
        query.close();
    }

    public void delete(Integer id) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarusuario( ? )");
        query.setInt(1, id);
        query.execute();
        query.close();
    }

    public Optional<Object> getUserById(Integer id) throws SQLException {
        var jsonArray = this.viewAll();
        return IntStream.range(0, jsonArray.length())
        .mapToObj(jsonArray::get)
        .filter(obj->{
            var json  = (JSONObject) obj;
            return json.getInt("id") == id;    
        })
        .findFirst();
    }
}
