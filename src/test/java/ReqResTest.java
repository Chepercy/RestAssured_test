import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqResTest {

    @BeforeEach
    public void setup(){
        //RestAssured.baseURI = "https://reqres.in";
        //RestAssured.basePath = "/api";
        RestAssured.filters( new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.requestSpecification=new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    @Test
    public void loginTest(){

        given()
                //.contentType(ContentType.JSON)
                .body("{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}")
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue() );
    }

    @Test
    public void getSingleTest(){

        given()
                //.contentType(ContentType.JSON)
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id",equalTo(2));
    }

    @Test
    public void deleteUserTest(){

        given()
                //.contentType(ContentType.JSON)
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
    @Test
    public void PatchUserTest(){

        String nameUpdated = given()
                //.contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}")
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("name");
        assertThat(nameUpdated,equalTo("morpheus"));
    }

    @Test
    public void PutUserTest(){

        String jobUpdated = given()
                //.contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("job");
        assertThat(jobUpdated,equalTo("zion resident"));
    }

    @Test
    public void getAllUsersTest(){
        Response response = given()
        .get("https://reqres.in/api/users?page=2");

        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.contentType();

        assertThat(statusCode,equalTo(HttpStatus.SC_OK));
        System.out.println("body: "+body);
        System.out.println("content type: "+contentType);
        System.out.println("Headers: "+headers.toString());
        System.out.println("************");
        System.out.println(headers.get("Content-Type"));
        System.out.println(headers.get("Transfer-Encoding"));
    }


}
