package fr.olivier.projet;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class CheckIpResourceTest {

    private final String bodyParam = "Starting Nmap 7.93 ( https://nmap.org ) at 2023-12-28 18:40 CET\n" + //
            "Nmap scan report for livebox.home (192.168.1.1)\n" + //
            "Host is up (0.00027s latency).\n" + //
            "MAC Address: 8C:F8:13:50:DB:D8 (Orange Polska)";

    @Test
    void testHelloEndpoint() {
        given().body(bodyParam)
          .when().post("/checkIp")
          .then()
             .statusCode(200)
             .body(is("size 1"));
    }

}