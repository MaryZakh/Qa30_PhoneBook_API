package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIDTestOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyZ29AZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3NTk2NzIwNDIsImlhdCI6MTc1OTA3MjA0Mn0.ygTxbSuGZP9pwhInFlzsbbLXO5P85_8qC5oiCLGz9oY";
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    String id;

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        int i = new Random().nextInt(1000) + 1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya" + i + "@gmail.com")
                .phone("12365984" + i)
                .address("NY")
                .description("MayaDow")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDTO), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        String message = messageDTO.getMessage();// "Contact was added! ID: a72596d5-4c61-40ce-a687-67ecffa2765d"
        System.out.println(message);
        String[] all = message.split(": ");
        id = all[1];
        System.out.println(id);

        //get id from "message": "Contact was added! ID: a72596d5-4c61-40ce-a687-67ecffa2765d"
    }


    @Test
    public void DeleteContactByIDSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");
        System.out.println(dto.getMessage());
    }

    @Test
    public void DeleteContactByIDWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/4b40514f-bd91-4623-91f9-f9401ccaa704")
                .delete()
                .addHeader("Authorization", "hskfvjbdf")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Unauthorized");
    }


    @Test
    public void DeleteContactByIDNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + 123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(),"Contact with id: 123 not found in your contacts!");

    }
}


//4b40514f-bd91-4623-91f9-f9401ccaa704
//anna@anna
//================================
//a72596d5-4c61-40ce-a687-67ecffa2765d
//dana@dana
//================================
//517f3b2b-efb2-4d93-bb40-fd80c0194677
//old1980@mail.com
//================================
//b6a99bb5-4413-4947-97a1-4f5c1af5ce77
//old1814@mail.com
//================================
//b91f8d04-90f4-415a-ac2c-4e2b6a64b376
//old1168@mail.com
//================================
//1773e042-3bb5-4e87-9b3b-f7295d66f63f
//old1200@mail.com
//================================
//a7e61772-f732-4a66-8e60-fe9cf3db962d
//old1893@mail.com
//================================