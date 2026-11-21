package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {

    @Before
    public void beforeScenario() {
        String fechaHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        System.out.println("============================================================");
        System.out.println("Estudiante : Isaac Junior Flores Esquivel");
        System.out.println("Código     : I202508621 (D = 1)");
        System.out.println("Ejecución  : " + fechaHora);
        System.out.println("============================================================");

        OnStage.setTheStage(new OnlineCast());
    }
}