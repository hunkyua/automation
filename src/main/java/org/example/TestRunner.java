package org.example;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @Test(dataProvider="scenarios")
    public void process(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws IOException {
        io.cucumber.testng.Pickle pickle = pickleWrapper.getPickle();
        int index = pickle.getUri().getPath().indexOf("src");
        String featureFilePath = pickle.getUri().getPath().substring(index);
        System.out.println(featureFilePath);
        Path featureContent = Paths.get(featureFilePath);

        System.out.println(featureContent);
        GherkinParser parser = GherkinParser.builder()
                .includeSource(true)
                .includePickles(true)
                .includeGherkinDocument(true)
                .build();

        Stream<Envelope> envelopes = parser.parse(featureContent);
        System.out.println(envelopes);

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
    }
}
