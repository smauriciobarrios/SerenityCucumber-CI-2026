package starter.basic;

import io.restassured.response.Response;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class BasicSerenity {

    @Test
    void createProject() {

        Actor actor = Actor
                .named("Juan Perez")
                .whoCan(CallAnApi.at("https://todo.ly"));

        actor.attemptsTo(
                Post
                        .to("/api/projects.json")
                        .with(requestSpecification ->
                                requestSpecification
                                        .given()
                                        .auth().preemptive().basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                                        .body("""
                                    {
                                        "Content": "Serenity ScreePlay",
                                        "Icon": 5
                                    }
                                    """)
                                        .log().all()
                        )
        );

        SerenityRest.lastResponse().then().log().all();

//        Response response = SerenityRest.lastResponse()
//                .then().log().all()
//                .extract().response();
//
//        System.out.println("***projectId: " + response.path("projectId"));

    }

}