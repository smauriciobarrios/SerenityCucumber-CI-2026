package starter.steps;

import net.serenitybdd.core.Serenity;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static org.hamcrest.Matchers.equalTo;

public class ItemsSteps {

    @ParameterType(".*")
    public Actor actor(String name) {  return OnStage.theActorCalled(name);  }

    @Before
    public void setTheStage() {  OnStage.setTheStage(new OnlineCast());  }

   public static String currentItemName;

    @Given("{actor} tiene acceso a Todo.ly")
    public void sergioTieneAccesoATodoLy(Actor actor) {
        Serenity.recordReportData()
                .withTitle("STEP")
                .andContents(actor.getName() + " tiene acceso a Todo.ly");

        actor.whoCan(CallAnApi.at("https://todo.ly"));

        Serenity.recordReportData()
                .withTitle("INFO")
                .andContents("API configurada correctamente");
    }

    @When("{actor} registra un item con nombre {string}")
    public void sergioRegistraUnItemConNombre(Actor actor, String itemName) {
        Serenity.recordReportData()
                .withTitle("CREATE ITEM")
                .andContents("Creando Item: " + itemName);

        currentItemName = itemName;

        actor.attemptsTo(
                Post.to("/api/items.json")
                        .with(req ->
                                req.given()
                                        .auth()
                                        .preemptive()
                                        .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .body("""
                                          {
                                            "Content":"%s"
                                          }
                                          """.formatted(itemName))
                                        .log().all()
                        )
        );

        Serenity.recordReportData()
                .withTitle("CREATE ITEM RESPONSE")
                .andContents(
                        SerenityRest.lastResponse().asString()
                );
    }

    @Then("{actor} revisa que el item queda registrado {string}")
    public void sergioRevisaQueElItemQuedaRegistrado(Actor actor, String itemName) {
        Serenity.recordReportData()
                .withTitle("VALIDANDO ITEM")
                .andContents("Contenido esperado: " + itemName);

        SerenityRest.lastResponse()
                .then()
                .statusCode(200)
                .body("Content", equalTo(itemName));

        Integer itemId =
                SerenityRest.lastResponse()
                        .jsonPath()
                        .getInt("Id");

        actor.remember("ITEM_ID", itemId);

        Serenity.recordReportData()
                .withTitle("ITEM CREADO")
                .andContents("Id generado: " + itemId);
    }

    @When("{actor} actualiza el nombre del item a {string}")
    public void sergioActualizaElNombreDelItemA(Actor actor, String itemName) {
        Serenity.recordReportData()
                .withTitle("UPDATE ITEM")
                .andContents("Nuevo contenido: " + itemName);

        currentItemName = itemName;

        actor.attemptsTo(
                Put.to("/api/items/" + actor.recall("ITEM_ID") + ".json")
                        .with(req ->
                                req.given()
                                        .auth()
                                        .preemptive()
                                        .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .body("""
                                           {
                                             "Content":"%s"
                                           }
                                           """.formatted(itemName))
                                        .log().all()
                        )
        );

        Serenity.recordReportData()
                .withTitle("UPDATE RESPONSE")
                .andContents(SerenityRest.lastResponse().asString());

    }

    @When("{actor} consulta el item")
    public void sergioConsultaElItem(Actor actor) {
        Serenity.recordReportData()
                .withTitle("GET ITEM")
                .andContents("Consultando Item ID: " + actor.recall("ITEM_ID"));

        actor.attemptsTo(
                Get.resource("/api/items/" + actor.recall("ITEM_ID") + ".json")
                        .with(req ->
                                req.given()
                                        .auth()
                                        .preemptive()
                                        .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .log().all()
                        )
        );

        Serenity.recordReportData()
                .withTitle("GET RESPONSE")
                .andContents(SerenityRest.lastResponse().asString());
    }

    @Then("{actor} visualiza la informacion del item")
    public void sergioVisualizaLaInformacionDelItem(Actor actor) {
        Serenity.recordReportData()
                .withTitle("VALIDANDO ITEM")
                .andContents("ID: " + actor.recall("ITEM_ID"));

        SerenityRest.lastResponse()
                .then()
                .statusCode(200)
                .body("Id", equalTo(actor.recall("ITEM_ID")))
                .body("Content", equalTo(currentItemName))
                .body("Deleted", equalTo(false));

        Serenity.recordReportData()
                .withTitle("ITEM CONSULTADO")
                .andContents(SerenityRest.lastResponse().asString());
    }

    @When("{actor} elimina el item")
    public void sergioEliminaElItem(Actor actor) {
        Serenity.recordReportData()
                .withTitle("DELETE ITEM")
                .andContents("Eliminando Item ID: " + actor.recall("ITEM_ID"));

        actor.attemptsTo(
                Delete.from("/api/items/" + actor.recall("ITEM_ID") + ".json")
                        .with(req ->
                                req.given()
                                        .auth()
                                        .preemptive()
                                        .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .log().all()
                        )
        );

        Serenity.recordReportData()
                .withTitle("DELETE RESPONSE")
                .andContents(SerenityRest.lastResponse().asString());
    }

    @Then("{actor} revisa que el item ya no se encuentra disponible")
    public void sergioRevisaQueElItemYaNoSeEncuentraDisponible(Actor actor) {
        Serenity.recordReportData()
                .withTitle("VALIDANDO DELETE")
                .andContents("Validando Item eliminado");

        SerenityRest.lastResponse()
                .then()
                .statusCode(200)
                .body("Id", equalTo(actor.recall("ITEM_ID")))
                .body("Deleted", equalTo(true));

        Serenity.recordReportData()
                .withTitle("ITEM ELIMINADO")
                .andContents(SerenityRest.lastResponse().asString());
    }
}