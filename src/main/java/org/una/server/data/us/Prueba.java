package org.una.server.data.us;

import java.sql.SQLException;
import java.util.Optional;

import org.json.JSONObject;
import org.una.server.controller.us.GrupoController;
import org.una.server.controller.us.MatriculaController;



public class Prueba {
    public static void main(String... args) throws SQLException {
        MatriculaDBA.getInstance().viewAll().forEach(System.out::println);
     
    }
}
