package com.azubike.ellipsis.mobile.app.ws.rest_assured_test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(OrderAnnotation.class)
class UserWebServiceEndpointTest {
	public final String CONTEXT_PATH = "/mobile-app-ws";
	private final String EMAIL_ADDRESS = "enuazubike88@gmail.com";
	private final String JSON = "application/json";
	private static String authHeader;
	private static String userId;
	private static List<Map<String, String>> addresses;

	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	@Order(1)
	void testUserLogin() {
		Map<String, Object> loginDetails = new HashMap<>();
		loginDetails.put("email", EMAIL_ADDRESS);
		loginDetails.put("password", "12345");

		Response response = given().contentType(JSON).accept(JSON).and().body(loginDetails).when()
				.post(CONTEXT_PATH + "/users/login").then().statusCode(200).extract().response();

		authHeader = response.getHeader("Authorization");
		userId = response.getHeader("userId");

		assertNotNull(authHeader);
		assertNotNull(userId);
	}

	@Test
	@Order(2)
	void testGetUserDetails() {
		Response response = given().pathParam("id", userId).header("Authorization", authHeader).accept(JSON).and()
				.get(CONTEXT_PATH + "/users/{id}").then().statusCode(200).contentType(JSON).extract().response();

		String userPublicId = response.jsonPath().getString("userId");
		String email = response.jsonPath().getString("email");
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		addresses = response.jsonPath().getList("addresses");
		String addressId = addresses.get(0).get("addressId");
		assertNotNull(userPublicId);
		assertNotNull(email);
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals(EMAIL_ADDRESS, email);
		assertTrue(addresses.size() == 2);
		assertTrue(addressId.length() == 30);

	}

	@Test
	@Order(3)
	void testUpdateUserDetails() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Patrick");
		userDetails.put("lastName", "Johnson");
		Response response = given().contentType(JSON).accept(JSON).header("Authorization", authHeader)
				.pathParam("id", userId).body(userDetails).when().put(CONTEXT_PATH + "/users/{id}").then()
				.statusCode(200).contentType(JSON).extract().response();

		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		List<Map<String, String>> returnedAddresses = response.jsonPath().getList("addresses");

		assertEquals(userDetails.get("firstName"), firstName);
		assertEquals(userDetails.get("lastName"), lastName);
		assertNotNull(returnedAddresses);
		assertTrue(addresses.size() == returnedAddresses.size());
		assertEquals(addresses.get(0).get("streetName"), returnedAddresses.get(0).get("streetName"));

	}

	@Test
	@Order(4)
	void testDeleteUserDetails() {
		Response response = given().header("Authorization", authHeader).accept(JSON).pathParam("id", userId).when()
				.delete(CONTEXT_PATH + "/users/{id}").then().statusCode(200).contentType(JSON).extract().response();

		String operationResult = response.jsonPath().getString("operationResult");
		String operationName = response.jsonPath().getString("operationName");
		assertNotNull(response);
		assertEquals("SUCCESS", operationResult);
		assertEquals("DELETE", operationName);
	}
}
