package restassured;

import dto.ContactDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateExistContactRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NTk2NzgwMDgsImlhdCI6MTc1OTA3ODAwOH0.a9-IkReISj5dFQaZpT8xh-6sBdCeWAhjuDlXm1bDF20";
    String endpoint = "contacts";
    String id;

    ContactDTO contact = ContactDTO.builder()
            .name("Donna")
            .lastName("Soww")
            .email("donna@gmail.com")
            .phone("123459626666")
            .address("Haifa")
            .description("DonnaSoww")
            .build();

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";


        String message = given()
                .body(contact)
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
    public void updateExistContactSuccess(){
        String name = contact.getName();
        contact.setId(id);
        contact.setName("wwwwwww");

        given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .put(endpoint)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));

    }
}
