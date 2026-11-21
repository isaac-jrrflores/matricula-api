package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarToken implements Task {

    private final String claveMemoria;

    public RenovarToken(String claveMemoria) {
        this.claveMemoria = claveMemoria;
    }

    public static RenovarToken conElActual() {
        return new RenovarToken("refreshToken");
    }

    public static RenovarToken conElOriginal() {
        return new RenovarToken("refreshTokenOriginal");
    }

    @Override
    @Step("{0} renueva el token usando #claveMemoria")
    public <T extends Actor> void performAs(T actor) {
        String refreshToken = actor.recall(claveMemoria);
        actor.attemptsTo(
                Post.to("/api/auth/refresh")
                        .with(req -> req.contentType(ContentType.JSON)
                                .body(Map.of("refreshToken", refreshToken)))
        );

        if (SerenityRest.lastResponse().statusCode() == 200) {
            String nuevoAccess = SerenityRest.lastResponse().jsonPath().getString("accessToken");
            String nuevoRefresh = SerenityRest.lastResponse().jsonPath().getString("refreshToken");
            actor.remember("accessToken", nuevoAccess);
            actor.remember("refreshToken", nuevoRefresh);
        }
    }
}