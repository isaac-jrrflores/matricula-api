package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class ElHistorial {

    public static Question<Integer> creditosAprobados() {
        return Question.about("los creditos aprobados en el historial")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getInt("creditosAprobados"));
    }

    public static Question<Double> promedioPonderado() {
        return Question.about("el promedio ponderado en el historial")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getDouble("promedioPonderado"));
    }
}