package org.example;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Scenario;
import io.cucumber.testng.*;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;
public class TestRunner {
    TestNGCucumberRunner testNGCucumberRunner;
    public TestRunner() {
        super();
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        XmlTest currentXmlTest = context.getCurrentXmlTest();
        Objects.requireNonNull(currentXmlTest);
        CucumberPropertiesProvider properties = currentXmlTest::getParameter;
        this.testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);
    }

    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @Test(dataProvider="scenarios")
    public void process(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws IOException {
        io.cucumber.testng.Pickle pickle = pickleWrapper.getPickle();
        int index = pickle.getUri().getPath().indexOf("src");
        String featureFilePath = pickle.getUri().getPath().substring(index);
        System.out.println(featureFilePath);
        Path featureContent = Paths.get(featureFilePath);

        System.out.println("FeatureContent: " + featureContent);
        GherkinParser parser = GherkinParser.builder()
                .includeSource(true)
                .includePickles(true)
                .includeGherkinDocument(true)
                .build();

        Stream<Envelope> envelopes = parser.parse(featureContent);
        System.out.println("Envelopes: " + envelopes);

        // Process the envelopes
        envelopes.forEach(envelope -> {
            if (envelope.getGherkinDocument().isPresent()) {
                GherkinDocument gherkinDocument = envelope.getGherkinDocument().get();

                if (gherkinDocument.getFeature().isPresent()) {
                    Feature feature = gherkinDocument.getFeature().get();
                    System.out.println("Feature Title: " + feature.getName()); // Access Feature Name
                    feature.getChildren().forEach(child -> {
                        if (child != null) {
                            Scenario scenario = child.getScenario().get();
                            System.out.println("Scenario ID:" + scenario.getId());
                            System.out.println("Scenario Title: " + scenario.getName());
                            scenario.getSteps().forEach(step ->
                                    System.out.println("Step: " + step.getText())
                            );
                        } else {
                            System.out.println("Unsupported child type: " + child.getClass().getName());
                        }
                    });
                }
            }
        });
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }
}
