package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ListarPagos implements Task {

    public static ListarPagos delAlumno() {
        return new ListarPagos();
    }

    @Override
    @Step("{0} consulta los pagos registrados")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(
                Get.resource("/api/pagos")
                        .with(req -> req.header("Authorization", "Bearer " + token))
        );
    }
}