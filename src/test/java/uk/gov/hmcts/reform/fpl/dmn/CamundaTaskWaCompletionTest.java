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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CamundaTaskWaCompletionTest extends DmnDecisionTableBaseUnitTest {

    @BeforeAll
    public static void initialization() {
        CURRENT_DMN_DECISION_TABLE = DmnDecisionTable.WA_TASK_COMPLETION;
    }

    @ParameterizedTest
    @MethodSource("scenarioProvider")
    void givenInputShouldReturnOutcomeDmn(String eventId,
                                               List<Map<String, ? extends Serializable>> expectedDmnOutcome) {
        VariableMap inputVariables = new VariableMapImpl();
        inputVariables.putValue("eventId", eventId);

        DmnDecisionTableResult dmnDecisionTableResult = evaluateDmnTable(inputVariables);

        assertThat(dmnDecisionTableResult.getResultList(), is(expectedDmnOutcome));
    }

    public static Stream<Arguments> scenarioProvider() {
        return Stream.of(
            Arguments.of("reviewCMO", getAutoCompleteTaskTypes(FPLTask.APPROVE_ORDERS)),
            Arguments.of("replyToMessageJudgeOrLegalAdviser", getAutoCompleteTaskTypes(
                FPLTask.REVIEW_MESSAGE_ALLOCATED_JUDGE, FPLTask.REVIEW_RESPONSE_ALLOCATED_JUDGE,
                FPLTask.REVIEW_MESSAGE_HEARING_JUDGE, FPLTask.REVIEW_RESPONSE_HEARING_JUDGE,
                FPLTask.REVIEW_MESSAGE_HEARING_CENTRE_ADMIN, FPLTask.REVIEW_RESPONSE_HEARING_CENTRE_ADMIN,
                FPLTask.REVIEW_MESSAGE_CTSC, FPLTask.REVIEW_RESPONSE_CTSC, FPLTask.REVIEW_MESSAGE_LEGAL_ADVISOR,
                FPLTask.REVIEW_RESPONSE_LEGAL_ADVISOR
            )),
            Arguments.of("sendToGatekeeper", getAutoCompleteTaskTypes(FPLTask.REVIEW_URGENT_APPLICATION,
                                                                              FPLTask.REVIEW_STANDARD_APPLICATION)),
            Arguments.of("returnApplication", getAutoCompleteTaskTypes(FPLTask.REVIEW_URGENT_APPLICATION,
                                                                              FPLTask.REVIEW_STANDARD_APPLICATION)),
            Arguments.of("sendOrderReminder", getAutoCompleteTaskTypes(FPLTask.CHASE_OUTSTANDING_ORDER))

            );
    }

    @Test
    void shouldHaveCorrectNumberOfRules() {
        // The purpose of this test is to prevent adding new rows without being tested
        DmnDecisionTableImpl logic = (DmnDecisionTableImpl) decision.getDecisionLogic();
        assertThat(logic.getRules().size(), is(17));
    }

    private static Map<String, String> getAutoCompleteTaskType(FPLTask taskType) {
        return Map.of(
            "taskType", taskType.getValue(),
            "completionMode", "Auto"
        );
    }

    private static List<Map<String, String>> getAutoCompleteTaskTypes(FPLTask... taskTypes) {
        return Stream.of(taskTypes).map(el -> getAutoCompleteTaskType(el)).collect(Collectors.toList());
    }
}
