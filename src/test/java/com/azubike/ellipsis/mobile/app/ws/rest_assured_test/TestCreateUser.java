package com.azubike.ellipsis.mobile.app.ws.rest_assured_test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

class TestCreateUser {
	public final String CONTEXT_PATH = "/mobile-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	void testCreateUser() {

		List<Map<String, Object>> userAddresses = new ArrayList<>();

		Map<String, Object> shippingAddresses = new HashMap<>();
		shippingAddresses.put("city", "Vancover");
		shippingAddresses.put("country", "Canada");
		shippingAddresses.put("streetName", "Dancing ave");
		shippingAddresses.put("postalCode", "12345");
		shippingAddresses.put("type", "shipping");
		userAddresses.add(shippingAddresses);

		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Azubike");
		userDetails.put("lastName", "Enu");
		userDetails.put("email", "enuazubike88@gmail.com");
		userDetails.put("password", "12345");
		userDetails.put("addresses", userAddresses);

		Response response = given().contentType("application/json").accept("application/json").body(userDetails).when()
				.post(CONTEXT_PATH + "/users").then().statusCode(200).contentType("application/json").extract()
				.response();

		assertNotNull(response);
		String userId = response.jsonPath().getString("userId");
		assertNotNull(userId);
	}

}
