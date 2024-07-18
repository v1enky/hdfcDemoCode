package org.com.hdfc.producer;

import io.vertx.mutiny.pgclient.PgPool;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.com.hdfc.model.UserDetails;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.io.IOException;
import java.net.URI;
import okhttp3.*;


@Path("/pan")
public class PanValidationResource {

    @Channel("mobile-validate-request")
    Emitter<String> mobileValidatorEmitter;

    private final PgPool client;

    public PanValidationResource(PgPool client) {
        this.client = client;
    }

    private final OkHttpClient httpClient = new OkHttpClient();

    @POST
    @Path("/validate")
    //@Produces(MediaType.APPLICATION_JSON)
    public void createRequest(UserDetails userDetails) throws IOException {
        userDetails.save(client)
                .onItem().transform(id -> URI.create("/pan/validate" + id));

        // json formatted data
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // json request body
        RequestBody body = RequestBody.create(
                mediaType,
                String.valueOf(userDetails)
                );

        Request request = new Request.Builder()
                .url("https://panvalidationurl/post")
                .method("POST",body)
                .addHeader("Authorization", "Basic ")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();

        mobileValidatorEmitter.send(response.body().string());


    }

}
