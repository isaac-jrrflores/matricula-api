package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;

public class CotizarMatricula implements Task {

    private final String codigoAlumno;
    private final List<String> cursos;

    public CotizarMatricula(String codigoAlumno, List<String> cursos) {
        this.codigoAlumno = codigoAlumno;
        this.cursos = cursos;
    }

    public static CotizarMatricula para(String codigoAlumno, List<String> cursos) {
        return new CotizarMatricula(codigoAlumno, cursos);
    }

    @Override
    @Step("{0} cotiza los cursos #cursos para el alumno #codigoAlumno")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(
                Post.to("/api/matriculas/cotizacion")
                        .with(req -> req.header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(Map.of("codigoAlumno", codigoAlumno, "cursos", cursos)))
        );
    }
}