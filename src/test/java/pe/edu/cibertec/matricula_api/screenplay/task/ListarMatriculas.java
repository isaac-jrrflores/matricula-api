package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ListarMatriculas implements Task {

    public static ListarMatriculas delAlumno() {
        return new ListarMatriculas();
    }

    @Override
    @Step("{0} consulta sus matriculas")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(
                Get.resource("/api/matriculas")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}