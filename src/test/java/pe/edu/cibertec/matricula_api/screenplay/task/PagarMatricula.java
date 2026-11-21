package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class PagarMatricula implements Task {

    private final long idMatricula;
    private final String metodo;

    public PagarMatricula(long idMatricula, String metodo) {
        this.idMatricula = idMatricula;
        this.metodo = metodo;
    }

    public static PagarMatricula conMetodo(long idMatricula, String metodo) {
        return new PagarMatricula(idMatricula, metodo);
    }

    @Override
    @Step("{0} paga la matricula #idMatricula con metodo #metodo")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(
                Patch.to("/api/matriculas/{id}/pagar")
                        .with(req -> {
                            req.header("Authorization", "Bearer " + token).pathParam("id", idMatricula);
                            if (metodo != null && !metodo.isBlank() && !metodo.equalsIgnoreCase("SIN_CUERPO")) {
                                req.contentType(ContentType.JSON).body(Map.of("metodo", metodo));
                            }
                            return req;
                        })
        );
    }
}