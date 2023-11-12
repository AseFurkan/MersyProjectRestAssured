package Positions;

import _BeforeApiTest.SetupSettings;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import com.github.javafaker.Faker;

public class Positions_Test extends SetupSettings {
    Faker randomUreteci = new Faker();
    String nameID = "";
    String rndPositionName = "";
    String rndPositionShortName = "";

    @Test
    public void createPosition() {
        rndPositionName = randomUreteci.name().firstName();
        rndPositionShortName = randomUreteci.name().lastName();

        Map<String, String> newPosition = new HashMap<>();
        newPosition.put("name", rndPositionName);
        newPosition.put("shortName", rndPositionShortName);
        newPosition.put("tenantId", "646cb816433c0f46e7d44cb0");
        newPosition.put("active", "true");

        nameID =
                given()
                        .spec(reqSpec)
                        .body(newPosition)
                        //.log().all()
                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");;    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative() {
        Map<String, String> newPosition = new HashMap<>();
        newPosition.put("name", rndPositionName);
        newPosition.put("shortName", rndPositionShortName);
        newPosition.put("tenantId", "646cb816433c0f46e7d44cb0");
        newPosition.put("active", "true");
        given()
                .spec(reqSpec)
                .body(newPosition)

                .when()
                .post("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createPositionNegative")
    public void updatePosition() {
        String newPositionName = "Updated Position" + randomUreteci.number().digits(5);
        Map<String, String> updPosition = new HashMap<>();
        updPosition.put("id", nameID);
        updPosition.put("name", newPositionName);
        updPosition.put("shortName", "12345");
        updPosition.put("tenantId", "646cb816433c0f46e7d44cb0");
        updPosition.put("active", "true");

        given()
                .spec(reqSpec)
                .body(updPosition)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newPositionName))
        ;
    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/employee-position/" + nameID)

                .then()
                .statusCode(204)
        ;
    }
}