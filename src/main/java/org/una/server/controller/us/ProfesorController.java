package org.una.server.controller.us;

import java.sql.SQLException;
import jakarta.websocket.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.una.server.model.us.ProfesorModel;
import org.una.server.model.us.UsuarioModel;

public class ProfesorController {
    private static final ProfesorModel model = ProfesorModel.getInstance();
    private static ProfesorController instance = null;

    public static ProfesorController getInstance() {
        if (instance == null)
            instance = new ProfesorController();
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
            UsuarioModel.getInstance().update(object.getInt("id"),2, object.getString("clave"),object.getString("name"));
            model.update(object.getInt("id"), object.getString("name"), object.getInt("telefono"),
                    object.getString("email"));
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
            UsuarioModel.getInstance().delete(object.getInt("id"));
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
            UsuarioModel.getInstance().create(object.getInt("id"), 2, object.getString("clave"),
                    object.getString("name"));
            model.create(object.getInt("id"), object.getString("name"), object.getInt("telefono"),
                    object.getString("email"));
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
