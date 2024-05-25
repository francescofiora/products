package it.francescofiora.product.itt.cucumber;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Cucumber Integration Test.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@SelectPackages("it.francescofiora.product.itt.cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "usage")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "it.francescofiora.product.itt.cucumber")
public class CucumberIntegrationTest {

}
