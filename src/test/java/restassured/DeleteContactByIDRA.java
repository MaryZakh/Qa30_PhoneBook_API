package restassured;

import dto.ContactDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIDRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NTk2NzgwMDgsImlhdCI6MTc1OTA3ODAwOH0.a9-IkReISj5dFQaZpT8xh-6sBdCeWAhjuDlXm1bDF20";
    String id;


    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";


        int i = new Random().nextInt(1000) + 1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Donna")
                .lastName("Soww")
                .email("donna"+ i +"@gmail.com")
                .phone("12345962"+i)
                .address("Haifa")
                .description("DonnaSoww")
                .build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                //"message": "Contact was added! ID: a72596d5-4c61-40ce-a687-67ecffa2765d"
                .extract()
                .path("message");
        String[] all = message.split(": ");
        id = all[1];
    }

    @Test
    public void deleteContactByIDSuccess() {
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIDWrongToken() {
        given()
                .header("Authorization", "ghjdshf")
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(401);

    }
}
