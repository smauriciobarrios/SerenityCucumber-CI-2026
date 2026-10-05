package starter.steps;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static org.hamcrest.Matchers.equalTo;

public class ProjectSteps {

    //{actor} es propio de serenity
    @Given("{actor} is an user on todo.ly")
    public void carlosIsAnUserOnTodoLy(Actor actor) {
        actor.whoCan(CallAnApi.at("https://todo.ly"));
    }

    @When("{actor} create a project with {string}")
    public void heCreateAProjectWith(Actor actor, String projectName) {
        actor.attemptsTo(
                Post
                        .to("/api/projects.json")
                        .with(requestSpecification ->
                                requestSpecification
                                        .given()
                                        .auth()
                                        .preemptive()
                                        .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .body("""
                                        {
                                            "Content": "%s",
                                            "Icon": 5
                                        }
                                        """.formatted(projectName))
                                        .log().all()
                        )
        );
    }

    @Then("{actor} should see the project {string}")
    public void heShouldSeeTheProject(Actor actor, String projectName) {
        SerenityRest.lastResponse()
                .then()
                .log().all()
                .statusCode(200)
                .body("Content", equalTo(projectName));
    }
/*
    @ParameterType(".*")
    public Actor actor(String name) {
        return OnStage.theActorCalled(name);
    }

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }
*/
}