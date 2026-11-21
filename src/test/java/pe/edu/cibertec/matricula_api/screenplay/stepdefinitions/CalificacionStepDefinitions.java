package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Y;
import net.serenitybdd.rest.SerenityRest;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElHistorial;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarMatriculas;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CalificacionStepDefinitions {

    private long idMatriculaObtenida;

    @Y("obtiene el id de su matricula del curso {string}")
    public void obtieneElIdDeSuMatriculaDelCurso(String codigoCurso) {
        theActorInTheSpotlight().attemptsTo(ListarMatriculas.delAlumno());
        Long id = SerenityRest.lastResponse().jsonPath()
                .getObject("find { it.codigoCurso == '" + codigoCurso + "' }.id", Long.class);

        assertThat("No se encontró matrícula para el curso " + codigoCurso, id, notNullValue());
        this.idMatriculaObtenida = id;
    }

    @Cuando("registra la nota {double} en la matricula obtenida")
    public void registraLaNotaEnLaMatriculaObtenida(double nota) {
        theActorInTheSpotlight().attemptsTo(CalificarMatricula.conNota(idMatriculaObtenida, nota));
    }

    @Cuando("intenta registrar la nota {int} en la matricula obtenida")
    public void intentaRegistrarLaNotaEnLaMatriculaObtenida(int nota) {
        theActorInTheSpotlight().attemptsTo(CalificarMatricula.conNota(idMatriculaObtenida, nota));
    }

    @Y("la condicion de la matricula calificada es {string}")
    public void laCondicionDeLaMatriculaCalificadaEs(String condicionEsperada) {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campo("condicion"), equalTo(condicionEsperada)));
    }

    @Cuando("{word} consulta el historial del alumno {string}")
    public void actorConsultaElHistorialDelAlumno(String nombreActor, String codigoAlumno) {
        theActorCalled(nombreActor).attemptsTo(ConsultarHistorial.delAlumno(codigoAlumno));
    }

    @Y("el historial muestra {int} creditos aprobados y promedio ponderado {double}")
    public void elHistorialMuestraCreditosAprobadosYPromedioPonderado(int creditosEsperados, double promedioEsperado) {
        theActorInTheSpotlight().should(
                seeThat(ElHistorial.creditosAprobados(), equalTo(creditosEsperados)),
                seeThat(ElHistorial.promedioPonderado(), closeTo(promedioEsperado, 0.01))
        );
    }
}