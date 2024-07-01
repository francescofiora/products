package it.francescofiora.product.itt.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.francescofiora.product.itt.container.StartStopContainers;
import it.francescofiora.product.itt.service.ApplicationService;
import it.francescofiora.product.itt.util.ContainerGenerator;
import it.francescofiora.product.itt.util.DataSourceUtils;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Cucumber Step Definitions.
 */
@Slf4j
public class StepDefinitions extends SpringGlue {

  private static final String DATASOURCE_URL =
      "jdbc:postgresql://product-postgresql:5432/db_product";

  private static final StartStopContainers containers = new StartStopContainers();
  private static final ContainerGenerator containerGenerator = new ContainerGenerator();

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private ApplicationService applicationService;

  /**
   * Start all containers.
   *
   * @throws SQLException if errors occur
   */
  @BeforeAll
  public static void init() throws SQLException {
    postgreContainer = containerGenerator.createPostgreSqlContainer();
    containers.add(postgreContainer);

    try (var ds = DataSourceUtils.createHikariDataSource(postgreContainer)) {
      DataSourceUtils.executeQuery(ds, "CREATE SCHEMA IF NOT EXISTS STORE");
      DataSourceUtils.executeQuery(ds, "CREATE SCHEMA IF NOT EXISTS COMPANY");
    }

    eureka = containerGenerator.createEurekaServerContainer();
    containers.add(eureka);

    var eurekaHttp = "http://" + EUREKA_USER + ":" + EUREKA_PASSWORD + "@"
        + ContainerGenerator.PRODUCT_EUREKA + ":8761/eureka";

    // @formatter:off
    var product = containerGenerator.createContainer("francescofiora-product")
        .withEnv(Map.of(
            "DATASOURCE_URL", DATASOURCE_URL,
            "EUREKA_URI", eurekaHttp,
            "eureka.instance.prefer-ip-address", "true"))
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.NETWORK_PRODUCT_API)
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(product);

    // @formatter:off
    var company = containerGenerator.createContainer("francescofiora-company")
        .withEnv(Map.of(
            "DATASOURCE_URL", DATASOURCE_URL,
            "EUREKA_URI", eurekaHttp,
            "eureka.instance.prefer-ip-address", "true"))
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.NETWORK_COMPANY_API)
        .withExposedPorts(8082);
    // @formatter:on
    containers.add(company);
  }

  /**
   * Given the system up and running.
   */
  @Given("the system up and running")
  public void givenSystemUp() {
    assertTrue(containers.areRunning());
    checkInstance(ContainerGenerator.INSTANCE_PRODUCT_API);
    checkInstance(ContainerGenerator.INSTANCE_COMPANY_API);
  }

  private void checkInstance(String name) {
    while (discoveryClient.getInstances(name).isEmpty()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * When GET the Application Health/Info.
   *
   * @param op Health or Info
   */
  @When("^GET the (\\w+) Application (\\w+)$")
  public void whenGetApplication(final String application, final String op) {
    applicationService.whenGetApplication(application, op);
  }

  /**
   * Check then Status of last operation.
   *
   * @param expected expected result
   */
  @Then("the result should contain {string}")
  public void thenResultContains(final String expected) {
    applicationService.thenResultContains(expected);
  }

  /**
   * Given a new entity.
   *
   * @param entity the entity
   * @param dataTable the example
   */
  @Given("^a new (\\w+)$")
  public void givenNew(final String entity, final DataTable dataTable) {
    var rows = dataTable.transpose().asList(String.class);
    applicationService.givenNew(entity, rows);
  }

  /**
   * When create that entity.
   *
   * @param entity the entity
   */
  @When("^create that (\\w+)$")
  public void whenCreate(final String entity) {
    applicationService.whenCreate(entity);
  }

  /**
   * Then should be able to get that entity.
   *
   * @param entity the entity
   */
  @Then("^should be able to get that (\\w+)$")
  public void thenGetEntity(final String entity) {
    applicationService.thenGetEntity(entity);
  }

  /**
   * Compare the object used from creation with the object from fetching GET.
   *
   * @param entity the entity
   * @param op1 POST or PUT
   * @param op2 GET or GET_ALL
   */
  @Then("^the (\\w+) from (\\w+) should be the same as from (\\w+)$")
  public void thenCompareObject(final String entity, final String op1, final String op2) {
    applicationService.thenCompareObject(entity, op1, op2);
  }

  @Then("that Order should have {int} items")
  public void checkItems(int size) {
    applicationService.checkItems(size);
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   * @param dataTable the example
   */
  @When("^update the (\\w+)$")
  public void whenUpdate(final String entity, final DataTable dataTable) {
    var rows = dataTable.transpose().asList(String.class);
    applicationService.whenUpdate(entity, rows);
  }

  /**
   * Delete entity.
   *
   * @param entity the entity
   */
  @When("^delete the (\\w+)$")
  public void thenDelete(final String entity) {
    applicationService.thenDelete(entity);
  }

  /**
   * Fetch entity from GET.
   *
   * @param entity the entity
   */
  @When("^get all (\\w+)$")
  public void whenGetAll(final String entity) {
    applicationService.whenGetAll(entity);
  }

  /**
   * Check the entity not exists.
   *
   * @param entity the entity
   */
  @Then("^that (\\w+) should be not present$")
  public void thenNotExist(final String entity) {
    applicationService.thenNotExist(entity);
  }

  /**
   * Stop and close all containers.
   */
  @AfterAll
  public static void endAll() {
    containers.stopAndCloseAll();
  }
}
