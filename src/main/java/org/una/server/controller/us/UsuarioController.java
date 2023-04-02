package org.una.server.controller.us;

import java.sql.SQLException;
import java.util.stream.IntStream;

import jakarta.websocket.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.una.server.model.us.UsuarioModel;

public class UsuarioController {
    private static final UsuarioModel model = UsuarioModel.getInstance();
    private static UsuarioController instance = null;

    public static UsuarioController getInstance() {
        if (instance == null)
            instance = new UsuarioController();
        return instance;
    }

    public JSONObject processQuery(JSONObject object, Session session) {
        if (object == null)
            return null;
        try {
            return switch (object.getString("action")) {
                case "VIEW_ALL" -> viewAll();
                case "CREATE" -> create(object);
                case "DELETE" -> delete(object);
                case "UPDATE" -> update(object);
                default -> null;
            };
        } catch (JSONException ex) {
            return null;
        }
    }

    public JSONObject update(JSONObject object) {
        var response = new JSONObject();
        try {
            model.update(object.getInt("id"), object.getInt("rol"), object.getString("clave"),
                    object.getString("nombre"));
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
            model.create(object.getInt("id"), object.getInt("rol"), object.getString("clave"),
                    object.getString("nombre"));
            response.put("action", "CREATE");
        } catch (SQLException ex) {
            response.put("action", "ERROR");
            response.put("message", ex.getMessage());
        } catch (JSONException ex) {
            return null;
        }
        return response;
    }

    public JSONObject viewAll() {
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
