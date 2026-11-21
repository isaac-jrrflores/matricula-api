package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import net.serenitybdd.rest.SerenityRest;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElMonto;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.CotizarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarPagos;
import pe.edu.cibertec.matricula_api.screenplay.task.Matricularse;
import pe.edu.cibertec.matricula_api.screenplay.task.PagarMatricula;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

public class MatriculaStepDefinitions {

    @Dado("inicia sesion con usuario {string} y clave {string}")
    public void iniciaSesionConUsuarioYClave(String usuario, String clave) {
        theActorInTheSpotlight().attemptsTo(IniciarSesion.conCredenciales(usuario, clave));
    }

    @Cuando("cotiza para el alumno {string} el curso {string}")
    public void cotizaParaElAlumnoElCurso(String alumno, String curso) {
        theActorInTheSpotlight().attemptsTo(CotizarMatricula.para(alumno, List.of(curso)));
    }

    @Cuando("cotiza para el alumno {string} los cursos {string}")
    public void cotizaParaElAlumnoLosCursos(String alumno, String cursosCsv) {
        List<String> cursos = Arrays.stream(cursosCsv.split(","))
                .map(String::trim)
                .toList();
        theActorInTheSpotlight().attemptsTo(CotizarMatricula.para(alumno, cursos));
    }

    @Y("la cotizacion tiene {int} creditos, subtotal {double}, descuento {int} y total {double}")
    public void laCotizacionTieneCreditosSubtotalDescuentoYTotal(int creditos, double subtotal, int descuento, double total) {
        int creditosActual = SerenityRest.lastResponse().jsonPath().getInt("creditos");
        double subtotalActual = SerenityRest.lastResponse().jsonPath().getDouble("subtotal");
        int descuentoActual = SerenityRest.lastResponse().jsonPath().getInt("descuento");
        double totalActual = SerenityRest.lastResponse().jsonPath().getDouble("total");

        assertThat("Créditos cotizados", creditosActual, equalTo(creditos));
        assertThat("Subtotal cotizado", subtotalActual, closeTo(subtotal, 0.01));
        assertThat("Porcentaje de descuento", descuentoActual, equalTo(descuento));
        assertThat("Total cotizado", totalActual, closeTo(total, 0.01));
    }

    @Cuando("se matricula con el codigo {string} en el curso {string}")
    public void seMatriculaConElCodigoEnElCurso(String alumno, String curso) {
        theActorInTheSpotlight().attemptsTo(Matricularse.en(alumno, curso));
        if (SerenityRest.lastResponse().statusCode() == 201) {
            long idMatricula = SerenityRest.lastResponse().jsonPath().getLong("id");
            theActorInTheSpotlight().remember("idMatricula", idMatricula);
        }
    }

    @Y("el monto de la matricula es {double}")
    public void elMontoDeLaMatriculaEs(double montoEsperado) {
        theActorInTheSpotlight().should(seeThat(ElMonto.deLamaTricula(), closeTo(montoEsperado, 0.01)));
    }

    @Y("el estado de la matricula es {string}")
    public void elEstadoDeLaMatriculaEs(String estadoEsperado) {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campo("estado"), equalTo(estadoEsperado)));
    }

    @Cuando("paga la matricula registrada con el metodo {string}")
    public void pagaLaMatriculaRegistradaConElMetodo(String metodo) {
        long idMatricula = theActorInTheSpotlight().recall("idMatricula");
        theActorInTheSpotlight().attemptsTo(PagarMatricula.conMetodo(idMatricula, metodo));
    }

    @Y("la matricula tiene fecha de pago registrada")
    public void laMatriculaTieneFechaDePagoRegistrada() {
        String fechaPago = SerenityRest.lastResponse().jsonPath().getString("fechaPago");
        assertThat("La fecha de pago debe tener valor", fechaPago, notNullValue());
    }

    @Cuando("consulta los pagos del alumno")
    public void consultaLosPagosDelAlumno() {
        theActorInTheSpotlight().attemptsTo(ListarPagos.delAlumno());
    }

    @Y("existe el pago de la matricula con metodo {string} y formato de operacion valido")
    public void existeElPagoDeLaMatriculaConMetodoYFormatoDeOperacionValido(String metodoEsperado) {
        long idMatricula = theActorInTheSpotlight().recall("idMatricula");

        String metodoRegistrado = SerenityRest.lastResponse().jsonPath()
                .getString("find { it.idMatricula == " + idMatricula + " }.metodo");
        String numeroOperacion = SerenityRest.lastResponse().jsonPath()
                .getString("find { it.idMatricula == " + idMatricula + " }.numeroOperacion");

        assertThat("No se encontró pago para idMatricula=" + idMatricula, metodoRegistrado, notNullValue());
        assertThat("Método de pago registrado", metodoRegistrado, equalTo(metodoEsperado));
        assertThat("Formato de número de operación", numeroOperacion, matchesPattern("^OP-\\d{6}$"));
    }
}