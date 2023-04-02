package org.una.server.data.us;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

public class MatriculaDBA {
    private static MatriculaDBA instance = null;

    private final Connection connection = Servicio.getInstance().getConnection();

    public static MatriculaDBA getInstance() {
        if (instance == null)
            instance = new MatriculaDBA();
        return instance;
    }

    public JSONArray viewAll() throws SQLException {

        var query = connection.prepareCall("{ ? = call listarMatricula() }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();

        while (rs.next()) {
            var object = new JSONObject();

            object.put("id", rs.getInt("id"));
            var curso = GrupoDBA.getInstance().getGrupoByID(rs.getInt("grupo"));
            if (curso != null) {

                object.put("nombreCurso", curso.getJSONObject("curso").getString("nombre"));
            }
            object.put("alumno", rs.getInt("alumno"));
            object.put("grupo", rs.getInt("grupo"));
            object.put("nota", rs.getFloat("nota"));
            object.put("ciclo", this.getCiclo(rs.getInt("id")));
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

    public void create(Integer alumno, Integer grupo, Integer nota) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call insertarMatricula( ?, ?, ? )");
        query.setInt(1, alumno);
        query.setInt(2, grupo);
        query.setInt(3, nota);
        query.execute();
        query.close();
    }

    public void update(Integer id, Integer alumno, Integer grupo, Integer nota) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call modificarMatricula( ?, ?, ?, ? )");
        query.setInt(1, id);
        query.setInt(2, alumno);
        query.setInt(3, grupo);
        query.setInt(4, nota);
        query.execute();
        query.close();
    }

    public void delete(Integer id) throws SQLException {
        connection.setAutoCommit(true);
        var query = connection.prepareStatement("call eliminarMatricula( ? )");
        query.setInt(1, id);
        query.execute();
        query.close();
    }

    public JSONObject getCiclo(Integer id) throws SQLException {
        var json = new JSONObject();
        String query = "select c.numero, c.fechaInicio,c.fechaFin,c.activo,c.id from matricula m, ciclo c, grupo g" +
                " where c.id=g.ciclo and m.grupo=g.numeroGrupo and  m.id = ?";
        var stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        var rs = stmt.executeQuery();
        while (rs.next()) {
            json.put("numero", rs.getInt("numero"));
            json.put("fechaInicio", rs.getString("fechaInicio"));
            json.put("fechaFin", rs.getString("fechaFin"));
            json.put("activo", rs.getInt("activo"));
            json.put("idCiclo", rs.getInt("id"));
        }
        stmt.close();
        return json;
    }

    public JSONArray getMatriculaParticular(Integer groupID) throws SQLException {

        var query = connection.prepareCall("{? = call listarMatriculaNombre ( ? ) }");
        query.registerOutParameter(1, OracleTypes.CURSOR);
        query.setInt(2, groupID);
        query.execute();
        var rs = (ResultSet) query.getObject(1);
        var resultJSON = new JSONArray();

        while (rs.next()) {
            var object = new JSONObject();
            object.put("nota", rs.getInt("nota"));
            object.put("id", rs.getInt("id"));
            object.put("grupo", rs.getInt("grupo"));
            object.put("nombre", rs.getString("nombre"));
            object.put("alumnoId", rs.getInt("alumnoId"));
            resultJSON.put(object);
        }
        rs.close();
        query.close();
        return resultJSON;

    }

}
