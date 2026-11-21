package pe.edu.cibertec.matricula_api.screenplay.questions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Y;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElCodigoDeError;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPerfil;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarAlumnos;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarToken;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class SeguridadStepDefinitions {

    @Cuando("renueva el token usando el refresh token actual")
    public void renuevaElTokenUsandoElRefreshTokenActual() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElActual());
    }

    @Cuando("intenta renovar el token reutilizando el refresh token original")
    public void intentaRenovarElTokenReutilizandoElRefreshTokenOriginal() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElOriginal());
    }

    @Cuando("consulta su perfil con el access token nuevo")
    public void consultaSuPerfilConElAccessTokenNuevo() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.delUsuario());
    }

    @Cuando("consulta su perfil con el access token actual")
    public void consultaSuPerfilConElAccessTokenActual() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.delUsuario());
    }

    @Cuando("cierra su sesion actual")
    public void cierraSuSesionActual() {
        theActorInTheSpotlight().attemptsTo(CerrarSesion.actual());
    }

    @Cuando("intenta iniciar sesion con usuario {string} y los siguientes intentos:")
    public void intentaIniciarSesionConUsuarioYLosSiguientesIntentos(String usuario, DataTable tabla) {
        for (Map<String, String> fila : tabla.asMaps()) {
            String clave = fila.get("clave");
            int statusEsperado = Integer.parseInt(fila.get("status"));
            String codigoEsperado = fila.get("codigo");

            theActorInTheSpotlight().attemptsTo(IniciarSesion.conCredenciales(usuario, clave));
            theActorInTheSpotlight().should(
                    seeThat(LaRespuesta.codigoDeEstado(), equalTo(statusEsperado)),
                    seeThat(ElCodigoDeError.deLaRespuesta(), equalTo(codigoEsperado))
            );
            theActorInTheSpotlight().should(
                    seeThatResponse("cumple schemas/error.json",
                            res -> res.body(matchesJsonSchemaInClasspath("schemas/error.json")))
            );
        }
    }

    @Y("intenta iniciar sesion con usuario {string} y clave {string}")
    public void intentaIniciarSesionConUsuarioYClave(String usuario, String clave) {
        theActorInTheSpotlight().attemptsTo(IniciarSesion.conCredenciales(usuario, clave));
    }

    @Cuando("intenta listar todos los alumnos")
    public void intentaListarTodosLosAlumnos() {
        theActorInTheSpotlight().attemptsTo(ListarAlumnos.todos());
    }

    @Y("el codigo de error de seguridad es {string}")
    public void elCodigoDeErrorDeSeguridadEs(String codigo) {
        theActorInTheSpotlight().should(seeThat(ElCodigoDeError.deLaRespuesta(), equalTo(codigo)));
    }
}