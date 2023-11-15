package uk.gov.hmcts.reform.fpl.dmn;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.hmcts.reform.fpl.DmnDecisionTable;
import uk.gov.hmcts.reform.fpl.DmnDecisionTableBaseUnitTest;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CamundaTaskWaCancellationTest extends DmnDecisionTableBaseUnitTest {

    @BeforeAll
    public static void initialization() {
        CURRENT_DMN_DECISION_TABLE = DmnDecisionTable.WA_TASK_CANCELLATION;
    }

    @ParameterizedTest
    @MethodSource("scenarioProvider")
    void givenInputShouldReturnOutcomeDmn(String eventId,
                                               Map<String, ? extends Serializable> expectedDmnOutcome) {
        VariableMap inputVariables = new VariableMapImpl();
        inputVariables.putValue("event", eventId);
        inputVariables.putValue("fromState", "");
        inputVariables.putValue("state", "");

        DmnDecisionTableResult dmnDecisionTableResult = evaluateDmnTable(inputVariables);

        assertThat(dmnDecisionTableResult.getResultList(), is(singletonList(expectedDmnOutcome)));
    }

    public static Stream<Arguments> scenarioProvider() {
        return Stream.of(
            Arguments.of("manageHearings", Map.of("action", "Reconfigure")),
            Arguments.of("changeCaseName", Map.of("action", "Reconfigure")),
            Arguments.of("changeCaseName-superuser", Map.of("action", "Reconfigure")),
            Arguments.of("manageLocalAuthorities", Map.of("action", "Reconfigure")),
            Arguments.of("internal-update-case-summary", Map.of("action", "Reconfigure"))
        );
    }

    @Test
    void shouldHaveCorrectNumberOfRules() {
        // The purpose of this test is to prevent adding new rows without being tested
        DmnDecisionTableImpl logic = (DmnDecisionTableImpl) decision.getDecisionLogic();
        assertThat(logic.getRules().size(), is(6));
    }
}
