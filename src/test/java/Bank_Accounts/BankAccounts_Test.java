package Bank_Accounts;

import Bank_Accounts.BankAccounts;
import _BeforeApiTest.SetupSettings;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class BankAccounts_Test extends SetupSettings {

    String rndID = "";
    String rndName = "";
    String rndIban = "";
    public Faker randomUreteci = new Faker();
    BankAccounts newBankAccounts = new BankAccounts();


    @Test
    public void createBankAccounts() {

        rndName = randomUreteci.address().country() + randomUreteci.address().countryCode();
        rndIban = randomUreteci.number().digits(16);
        newBankAccounts.name = rndName;
        newBankAccounts.iban = rndName;

        rndID =
                given()
                        .spec(reqSpec)
                        .body(newBankAccounts)
                        .when()
                        .post("school-service/api/bank-accounts")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println("rndID = " + rndID);
    }

    @Test(dependsOnMethods = "createBankAccounts")
    public void createBankAccountsNegative() {

        newBankAccounts.name = rndName;
        newBankAccounts.iban = rndName;


        given()
                .spec(reqSpec)
                .body(newBankAccounts)
                .when()
                .post("school-service/api/bank-accounts")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createBankAccountsNegative")
    public void updateBankAccounts() {

        String upBankAccounts = "" + rndName + randomUreteci.number().digits(5);

        rndName = randomUreteci.address().country() + randomUreteci.address().countryCode() + upBankAccounts;
        newBankAccounts.name = rndName;
        newBankAccounts.id = rndID;
        newBankAccounts.iban = rndIban;


        rndID =
                given()
                        .spec(reqSpec)
                        .body(newBankAccounts)

                        .when()
                        .put("school-service/api/bank-accounts")


                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name", containsString(upBankAccounts))
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "updateBankAccounts")
    public void searchBankAccounts() {


        given()
                .spec(reqSpec)
                .body(newBankAccounts)
                .when()
                .post("school-service/api/nationality/search")

                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @Test(dependsOnMethods = "searchBankAccounts")
    public void deleteBankAccounts() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + rndID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteBankAccounts")
    public void deleteBankAccountsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + rndID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Please, bank account must be exist"))
        ;
    }
}