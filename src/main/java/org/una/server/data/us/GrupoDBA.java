package org.una.server.data.us;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class GrupoDBA {
    private static GrupoDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static GrupoDBA getInstance() {
        if (instance == null)
            instance = new GrupoDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {
        var query = connection.prepareCall("{ ? = call listarGrupos() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();
        while (rs.next()) {
            var object = new JSONObject();
            object.put("numeroGrupo", rs.getInt("numeroGrupo"));
            object.put("ciclo", CicloDBA.getInstance().getCursoSingle(rs.getInt("ciclo")));
            object.put("curso", CursoDBA.getInstance().getCourseById(rs.getString("curso")));
            object.put("cicloSimple", rs.getInt("ciclo"));
            object.put("cursoSimple", rs.getString("curso"));
            object.put("horario", rs.getString("horario"));
            object.put("profesor", rs.getInt("profesor"));
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

    public void create(Integer ciclo, String curso, String horario, Integer profesor)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarGrupo( ?, ?, ?, ? )");
        query.setInt(1, ciclo);
        query.setString(2, curso);
        query.setString(3, horario);
        query.setInt(4, profesor);
        query.execute();
        query.close();
    }

    public void update(Integer numeroGrupo, Integer ciclo, String curso, String horario, Integer profesor)
            throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarGrupo( ?, ?, ?, ?, ? )");
        query.setInt(1, numeroGrupo);
        query.setInt(2, ciclo);
        query.setString(3, curso);
        query.setString(4, horario);
        query.setInt(5, profesor);
        query.execute();
        query.close();
    }

    public void delete(Integer numeroGrupo) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarGrupo( ? )");
        query.setInt(1, numeroGrupo);
        query.execute();
        query.close();
    }

    public JSONObject getGrupoByID(Integer id) throws SQLException {
        var jsonArray = this.viewAll();
        return (JSONObject) IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::get)
                .filter(obj -> {
                    var json = (JSONObject) obj;
                    return json.getInt("numeroGrupo") == id;
                })
                .findFirst().get();

    }

}
