package Nationalities;

import _BeforeApiTest.SetupSettings;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class Nationalities_Test extends SetupSettings {

    Faker randomUreteci = new Faker();
    Nationalities newNationalities = new Nationalities();
    String nationalitiesID = "";
    String rndNationalitiesName = "";
    @Test
    public void createNationalities() {

        rndNationalitiesName = randomUreteci.address().country() + randomUreteci.address().countryCode();
        newNationalities.name = rndNationalitiesName;
        newNationalities.translateName = new Object[1];

        nationalitiesID =
                given()
                        .spec(reqSpec)
                        .body(newNationalities)
                        .when()
                        .post("school-service/api/nationality")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
    }

    @Test(dependsOnMethods = "createNationalities")
    public void createNationalitiesNegative() {

        newNationalities.name = rndNationalitiesName;
        newNationalities.translateName = new Object[1];


        given()
                .spec(reqSpec)
                .body(newNationalities)
                .when()
                .post("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createNationalitiesNegative")
    public void updateNationalities(){

        String newCountryName=""+rndNationalitiesName+randomUreteci.number().digits(5);

        rndNationalitiesName = randomUreteci.address().country() + randomUreteci.address().countryCode()+newCountryName;
        newNationalities.name = rndNationalitiesName;
        newNationalities.id=nationalitiesID;
        newNationalities.translateName = new Object[1];

        nationalitiesID =
        given()
                .spec(reqSpec)
                .body(newNationalities)

                .when()
                .put("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", containsString(newCountryName))
                .extract().path("id")
                ;

    }
    @Test(dependsOnMethods = "updateNationalities")
    public void searchNationalities() {


        given()
                .spec(reqSpec)
                .body(newNationalities)
                .when()
                .post("school-service/api/nationality/search")

                .then()
                .log().body()
                .statusCode(200)

        ;
    }
    @Test(dependsOnMethods = "searchNationalities")
    public void deleteNationalities() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/nationality/"+nationalitiesID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteNationalities")
    public void deleteNationalitiesNegative()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/nationality/"+nationalitiesID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Nationality not  found"))
        ;
    }
}

