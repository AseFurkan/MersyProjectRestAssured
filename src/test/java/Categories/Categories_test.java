package Categories;

import _BeforeApiTest.SetupSettings;
import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class Categories_test extends SetupSettings {
    Faker randomUreteci=new Faker();
    RequestSpecification reqSpec;
    String cateID="";
    String rndCateName="";
    String rndCateCode="";

    @Test
    public void createCategory(){
        rndCateName=randomUreteci.address().country()+randomUreteci.address().countryCode();
        rndCateCode=randomUreteci.address().countryCode();

        Map<String,String> newCategory=new HashMap<>();
        newCategory.put("name",rndCateName);
        newCategory.put("code",rndCateCode);

        cateID=
                given()
                        .spec(reqSpec)
                        .body(newCategory)
                        //.log().all()

                        .when()
                        .post("school-service/api/subject-categories")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;


    }
    @Test(dependsOnMethods = "createCategory")
    public void negativeCategoryTest(){
        Map<String,String> newCategory=new HashMap<>();
        newCategory.put("name",rndCateName);
        newCategory.put("code",rndCateCode);

        given()
                .spec(reqSpec)
                .body(newCategory)
                //.log().all()

                .when()
                .post("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsString("already"))
        ;
    }
    @Test(dependsOnMethods ="negativeCategoryTest")
    public void updateCategory(){
        String newCategoryName="Update Category"+randomUreteci.number().digits(5);
        Map<String,String> updCategory=new HashMap<>();
        updCategory.put("id",cateID);
        updCategory.put("name",newCategoryName);
        updCategory.put("code","12345");

        given()
                .spec(reqSpec)
                .body(updCategory)
                .when()
                .put("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newCategoryName));

    }
    @Test(dependsOnMethods = "updateCategory")
    public void deleteCategory(){
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/"+cateID)

                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(dependsOnMethods = "deleteCategory")
    public void negativeDeleteCategory(){
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/"+cateID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("SubjectCategory not  found"));
    }



}
