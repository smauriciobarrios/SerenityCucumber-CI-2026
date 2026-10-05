package starter.basic;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class BasicJUnit {

    @Test
    void createProject() {
        given()
                .auth()
                .preemptive()
                .basic("smbr.api.rest@gmail.com", "smbr.api.rest")
                .body("""
                        {
                            "Content": "Serenity",
                            "Icon": 3
                        """)
                .log().all()
                .when()
                .post("https://todo.ly/api/projects.json")
                .then()
                .log().all();
    }

}