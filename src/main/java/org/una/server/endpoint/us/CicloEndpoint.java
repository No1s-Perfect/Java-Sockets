package org.una.server.endpoint.us;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.una.server.controller.SessionController;
import org.una.server.controller.us.CicloController;
import org.una.server.controller.us.ProfesorController;
import org.una.server.endpoint.decode.JsonObjectDecoder;
import org.una.server.endpoint.encode.JsonObjectEncoder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
@ServerEndpoint(value = "/ciclo", decoders = {JsonObjectDecoder.class}, encoders = {JsonObjectEncoder.class})
public class CicloEndpoint {
    private static final CicloController controller = CicloController.getInstance();

    private static final SessionController sessionController = SessionController.getInstance();

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws Exception {
    
    }
    public void sendToMany(JSONObject message, Predicate<Session> condition) throws EncodeException, IOException {
        if (message == null) return;
        for (var session : sessions) {
            if (condition.test(session)) {
                session.getBasicRemote().sendObject(controller.processQuery(message, session));
            }
        }
    }

    public void broadcast(JSONObject message) throws EncodeException, IOException {
        if (message == null) return;
        for (var session : sessions) {
            session.getBasicRemote().sendObject(controller.processQuery(message, session));
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(JSONObject message, Session session) throws IOException, EncodeException {
        var response = controller.processQuery(message, session);
        if (response != null) {
            session.getBasicRemote().sendObject(response);
            this.sendToMany(switch (response.optString("action")) {
                case "CREATE", "UPDATE", "DELETE" -> new JSONObject().put("action", "GET_ALL");
                default -> null;
            }, sessionController::isSessionAdmin);
            this.broadcast(switch (response.optString("action")) {
                case "CREATE", "UPDATE", "DELETE" -> new JSONObject().put("action", "VIEW_ALL");
                default -> null;
            });
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.format("Error for session %s: %s%n", session.getId(), throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
    }    
}
