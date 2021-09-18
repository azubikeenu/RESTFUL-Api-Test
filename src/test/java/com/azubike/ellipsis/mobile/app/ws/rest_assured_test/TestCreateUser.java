package com.azubike.ellipsis.mobile.app.ws.rest_assured_test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("city", "Vancover");
		shippingAddress.put("country", "Canada");
		shippingAddress.put("streetName", "Dancing ave");
		shippingAddress.put("postalCode", "12345");
		shippingAddress.put("type", "shipping");
		userAddresses.add(shippingAddress);

		Map<String, Object> billingAddress = new HashMap<>();
		billingAddress.put("city", "Vancover");
		billingAddress.put("country", "Canada");
		billingAddress.put("streetName", "Dancing ave");
		billingAddress.put("postalCode", "12345");
		billingAddress.put("type", "billing");

		userAddresses.add(billingAddress);

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

		String bodyString = response.body().asString();
		try {
			JSONObject responseBodyJson = new JSONObject(bodyString);
			JSONArray addresses = responseBodyJson.getJSONArray("addresses");
			assertNotNull(addresses);
			assertTrue(addresses.length() == 2);
			String addressId = addresses.getJSONObject(0).getString("addressId");
			assertNotNull(addressId);
			assertTrue(addressId.length() == 30);
		} catch (JSONException ex) {
			fail(ex.getMessage());
		}

	}

}
