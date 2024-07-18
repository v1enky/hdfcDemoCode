package org.com.hdfc.processor;

import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.com.hdfc.model.UserDetails;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import okhttp3.*;

@ApplicationScoped
public class MobileOTPGenerator {

    private Random random = new Random();

    private final OkHttpClient httpClient = new OkHttpClient();

    @Incoming("mobile-validate-request")
    @Outgoing("mobile-validate-response")
    @Blocking
    public Response process(String Request) throws InterruptedException, IOException {

        UserDetails userDetails = new ObjectMapper().readValue(Request, UserDetails.class);

        // json formatted data
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // json request body
        RequestBody body = RequestBody.create(
                mediaType,
                String.valueOf(userDetails)
        );

        Request request = new Request.Builder()
                .url("https://sendOTPURL/"+userDetails.getMobileNumber()+"/"+ MobileOTPGenerator.generateOTP(4))
                .method("POST",body)
                .addHeader("Authorization", "Basic ")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response;
    }


    public static String generateOTP(int length) {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return new String(otp);
    }
}
