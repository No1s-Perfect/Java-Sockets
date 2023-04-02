package org.una.server.model.us;
import java.sql.SQLException;

import org.json.JSONArray;
import org.una.server.data.us.GrupoDBA;
public class GrupoModel {
    private static GrupoModel instance = null;

    private final GrupoDBA dba;

    private GrupoModel() {
        this.dba = GrupoDBA.getInstance();
    }

    public static GrupoModel getInstance() {
        if (instance == null)
            instance = new GrupoModel();
        return instance;
    }

    public JSONArray getAll() throws SQLException {
        return this.dba.getAll();
    }

    public void create(Integer ciclo, String curso, String horario, Integer profesor)
            throws SQLException {
        this.dba.create(ciclo, curso, horario, profesor);
    }

    public void update(Integer numeroGrupo, Integer ciclo, String curso, String horario, Integer profesor)
            throws SQLException {
        this.dba.update(numeroGrupo, ciclo, curso, horario, profesor);
    }

    public void delete(Integer numeroGrupo) throws SQLException {
        this.dba.delete(numeroGrupo);
    }
}
