package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static org.example.BaseApi.Y_SCOOTER_URL;

public class RestClient {

    protected static RequestSpecification getBaseSpec() {

        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Y_SCOOTER_URL)
                .build();
    }
}
