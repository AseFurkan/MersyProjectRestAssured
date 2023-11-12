package Fields;

import _BeforeApiTest.SetupSettings;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Field_Test extends SetupSettings {

        Faker randomUreteci = new Faker();
        Field newField = new Field();
        String FieldID = "";
        String FieldName = "";
        String FieldCode = "";

    @Test
    public void createField() {

        FieldName = randomUreteci.address().country() + randomUreteci.address().countryCode();
        FieldCode = randomUreteci.address().country() + randomUreteci.address().countryCode();
        newField.name = FieldName;
        newField.translateName = new Object[1];

        FieldID =
                given()
                        .spec(reqSpec)
                        .body(newField)
                        .when()
                        .post("school-service/api/entity-field")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
    }
    @Test(dependsOnMethods = "createField")
    public void createFieldNegative() {

        newField.name = FieldName;
        newField.code = FieldCode;
        newField.translateName = new Object[1];


        given()
                .spec(reqSpec)
                .body(newField)
                .when()
                .post("school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }
    @Test(dependsOnMethods = "createFieldNegative")
    public void updateField(){

        String newFieldName=""+FieldName+randomUreteci.number().digits(5);

        FieldName = randomUreteci.address().country() + randomUreteci.address().countryCode()+newFieldName;
        newField.name = FieldName;
        newField.id=FieldID;
        newField.translateName = new Object[1];

        FieldID =
                given()
                        .spec(reqSpec)
                        .body(newField)

                        .when()
                        .put("school-service/api/entity-field")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name", containsString(newFieldName))
                        .extract().path("id")
        ;

    }
    @Test(dependsOnMethods = "updateField")
    public void searchField() {

        given()
                .spec(reqSpec)
                .body(newField)
                .when()
                .post("school-service/api/entity-field/search")

                .then()
                .log().body()
                .statusCode(200)

        ;
    }
    @Test(dependsOnMethods = "searchField")
    public void deleteField() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/entity-field/"+FieldID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteField")
    public void deleteFieldNegative()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/entity-field/"+FieldID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"))
        ;
    }

}
