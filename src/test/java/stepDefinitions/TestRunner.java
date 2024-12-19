package stepDefinitions;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = {"stepDefinitions"},
monochrome = true,
plugin = {"pretty", "junit:target/JUnitReports/report.xml"})

public class TestRunner {
}
