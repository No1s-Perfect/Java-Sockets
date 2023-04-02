package org.una.server.controller.us;

import org.una.server.model.us.MatriculaModel;
import java.sql.SQLException;
import java.util.Optional;

import jakarta.websocket.Session;

import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;

public class MatriculaController {
    private static final MatriculaModel model = MatriculaModel.getInstance();
    private static MatriculaController instance = null;

    public static MatriculaController getInstance() {
        if (instance == null)
            instance = new MatriculaController();
        return instance;
    }

    public JSONObject processQuery(JSONObject object, Session session) {
        if (object == null)
            return null;
        try {
            return switch (object.getString("action")) {
                case "VIEW_ALL" -> viewAll(object);
                case "CREATE" -> create(object);
                case "DELETE" -> delete(object);
                case "UPDATE" -> update(object);
                default -> null;
            };
        } catch (JSONException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        var a = MatriculaController.getInstance();
        var b = new JSONObject();
        b.put("action","UPDATE");
        b.put("id", 3);
        b.put("alumno", 1);
        b.put("grupo", 4);
        b.put("nota", 100);
        a.processQuery(b, null);
    }

    public JSONObject update(JSONObject object) {
        var response = new JSONObject();
        try {
            model.update(object.getInt("id"), object.getInt("alumno"), object.getInt("grupo"),
                    object.getInt("nota"));
            response.put("action", "UPDATE");
        } catch (SQLException ex) {
            response.put("action", "ERROR");
            response.put("message", ex.getMessage());
        } catch (JSONException ex) {
            return null;
        }
        return response;
    }

    public JSONObject delete(JSONObject object) {
        var response = new JSONObject();
        try {
            model.delete(object.getInt("id"));
            response.put("action", "DELETE");
        } catch (SQLException ex) {
            response.put("action", "ERROR");
            response.put("message", ex.getMessage());
        } catch (JSONException ex) {
            return null;
        }
        return response;
    }

    public JSONObject create(JSONObject object) {
        var response = new JSONObject();
        try {
            model.create(object.getInt("alumno"), object.getInt("grupo"),
                    object.getInt("nota"));
            response.put("action", "CREATE");
        } catch (SQLException ex) {
            response.put("action", "ERROR");
            response.put("message", ex.getMessage());
        } catch (JSONException ex) {
            return null;
        }
        return response;
    }

    public JSONObject viewAll(JSONObject object) {

        try {
            var response = new JSONObject();
            response.put("action", "VIEW_ALL");
            response.put("view", model.getAll());
            return response;
        } catch (SQLException ex) {
            System.err.format("SQLException: %s%n", ex.getMessage());
            return null;
        } 
    }
}
