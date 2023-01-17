package org.folio.hello;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.folio.spring.FolioModuleMetadata;
import org.folio.spring.integration.XOkapiHeaders;
import org.folio.tenant.domain.dto.TenantAttributes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Base abstract class for testing APIs.  Contains centralized APIs for Rest Assured, wiremock, etc
 */
@Log4j2
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase(refresh = RefreshMode.NEVER)
@ContextConfiguration(initializers = { WireMockInitializer.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

  public static final String TENANT_ID = "test";

  @Getter
  @Setter
  protected static boolean initialized = false;

  @Autowired
  protected WireMockServer wireMockServer;

  @Autowired
  protected FolioModuleMetadata metadata;

  @Value("${x-okapi-url}")
  protected String okapiUrl = null;

  @LocalServerPort
  protected Integer port;

  protected final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(
    "openapi/api.yaml"
  );

  @BeforeEach
  public void createDatabase() {
    if (!isInitialized()) {
      tenantInstall(new TenantAttributes().moduleTo("mod-hello-spring"));

      setInitialized(true);
    }
  }

  public void tenantInstall(TenantAttributes tenantAttributes) {
    // "/_/tenant" is not in Swagger schema, therefore, validation must be disabled
    // the v2.0 API of /_/tenant requires a non-empty moduleTo; without this, the module will not be initialized properly or enabled
    // the string we use does not matter (as there will be no modules in the database)
    log.info(
      String.format(
        "Initializing database by posting to /_/tenant %s",
        tenantAttributes
      )
    );
    // not part of openapi spec, so don't validate
    ra(false)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .body(tenantAttributes)
      .post(getRequestUrl("/_/tenant"))
      .then()
      .statusCode(both(greaterThanOrEqualTo(200)).and(lessThanOrEqualTo(299)));
  }

  @AfterEach
  void resetWiremock() {
    this.wireMockServer.resetAll();
  }

  /**
   * Create a RestAssured object with the proper headers for Okapi testing
   *
   * @param validate If the response should be validated against the openapi schema
   * @return a @link {RequestSpecification} ready for .get/.post and other
   *         RestAssured library methods
   */
  public RequestSpecification ra(boolean validate) {
    RequestSpecification ra = RestAssured.given();
    if (validate) {
      ra = ra.filter(validationFilter);
    }
    return ra
      .header(new Header(XOkapiHeaders.URL, okapiUrl))
      .header(new Header(XOkapiHeaders.TENANT, TENANT_ID));
  }

  /**
   * Create a RestAssured object with the proper headers for Okapi testing and
   * builtin schema validation
   *
   * @return a {@link RequestSpecification} ready for .get/.post and other
   *         RestAssured library methods
   */
  public RequestSpecification ra() {
    return ra(true);
  }

  /**
   * Fully qualify a URL for testing. For example, if the path is "/test", this
   * method may return something like "http://localhost:8103/hello".
   *
   * @param path The API route's path
   * @return fully qualified URL
   */
  public String getRequestUrl(String path) {
    return String.format("http://localhost:%d%s", port, path);
  }
}
