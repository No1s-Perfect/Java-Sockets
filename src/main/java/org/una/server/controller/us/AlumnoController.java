package org.una.server.controller.us;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.una.server.model.us.AlumnoModel;
import org.una.server.model.us.UsuarioModel;

import jakarta.websocket.Session;

public class AlumnoController {
    private static final AlumnoModel model = AlumnoModel.getInstance();
    private static final UsuarioModel modelU = UsuarioModel.getInstance();
    private static AlumnoController instance = null;

    public static AlumnoController getInstance() {
        if (instance == null)
            instance = new AlumnoController();
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
            modelU.update(object.getInt("id"),1, object.getString("clave"),object.getString("nombre"));
            model.update(object.getInt("id"), object.getString("nombre"), object.getString("telefono"),
                    object.getString("email"),
                    object.getString("fechaNacimiento"), object.getString("carrera"));
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
            modelU.delete(object.getInt("id"));
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
            modelU.create(object.getInt("id"), 1, object.getString("clave"), object.getString("nombre"));
            model.create(object.getInt("id"), object.getString("nombre"), object.getString("telefono"),
                    object.getString("email"),
                    object.getString("fechaNacimiento"), object.getString("carrera"));
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
