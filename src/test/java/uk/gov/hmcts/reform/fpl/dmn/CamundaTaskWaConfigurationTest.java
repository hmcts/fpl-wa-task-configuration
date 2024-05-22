package uk.gov.hmcts.reform.fpl.dmn;

import camundajar.impl.scala.math.BigDecimal;
import camundajar.impl.scala.util.Either;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.feel.FeelEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.hmcts.reform.fpl.DmnDecisionTable;
import uk.gov.hmcts.reform.fpl.DmnDecisionTableBaseUnitTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CamundaTaskWaConfigurationTest extends DmnDecisionTableBaseUnitTest {

    @BeforeAll
    public static void initialization() {
        CURRENT_DMN_DECISION_TABLE = DmnDecisionTable.WA_TASK_CONFIGURATION;
    }

    private static LocalDateTime NOW = LocalDateTime.now();

    private static Map<String, Object> CASE_DATA = Map.of(
        "caseName", "Test v Smith",
        "caseManagementLocation", Map.of(
            "region", "1",
            "baseLocation", "111"
        ),
        "court", Map.of("name", "Birmingham"),
        "caseSummaryNextHearingDate", NOW
    );

    @Test
    void shouldHaveBasicRules() {
        VariableMap inputVariables = new VariableMapImpl();
        inputVariables.putValue("taskType", "");
        inputVariables.putValue("caseData", CASE_DATA);

        DmnDecisionTableResult dmnDecisionTableResult = evaluateDmnTable(inputVariables);
        assertTrue(dmnDecisionTableResult.getResultList().containsAll(getBaseValues()));
    }

    @Test
    void shouldHaveCorrectPriorities() {
        VariableMap inputVariables = new VariableMapImpl();
        inputVariables.putValue("taskType", "checkPlacementApplication");
        inputVariables.putValue("caseData", CASE_DATA);

        DmnDecisionTableResult dmnDecisionTableResult = evaluateDmnTable(inputVariables);
        assertTrue(dmnDecisionTableResult.getResultList().containsAll(List.of(
            getRowResult("minorPriority", "500", true),
            getRowResult("majorPriority", "5000", true)
        )));
    }

    @Test
    void shouldHaveCorrectNumberOfRules() {
        // The purpose of this test is to prevent adding new rows without being tested
        DmnDecisionTableImpl logic = (DmnDecisionTableImpl) decision.getDecisionLogic();
        assertThat(logic.getRules().size(), is(63));
    }

    private static List<Map<String, Object>> getBaseValues() {
        return List.of(
            getRowResult("caseName", "Test v Smith", true),
            getRowResult("caseManagementCategory", "PUBLICLAW", false),
            getRowResult("region", "1", true),
            getRowResult("location", "111", true),
            getRowResult("locationName", "Birmingham", true)
        );
    }

    private static Map<String, Object> getRowResult(String name, String value, boolean canReconfigure) {
        return Map.of(
            "canReconfigure", canReconfigure,
            "name", name,
            "value", value
        );
    }

    private static String encloseDoubleQuote(String name) {
        return "\"" + name + "\"";
    }

    private static String getValueFromWaConfiguration(DmnDecisionTableImpl logic, String taskType, String name) {
        return logic.getRules().stream().filter(d
                -> encloseDoubleQuote(taskType).equals(d.getConditions().get(1).getExpression())
                && encloseDoubleQuote(name).equals(d.getConclusions().get(0).getExpression()))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("Unable to locate " + taskType + " and " + name
                + " from the DMN table"))
            .getConclusions().get(1).getExpression();
    }

    private static Map<String, Object> toNullValueMap(String key) {
        Map<String, Object> ret =  new HashMap<>();
        ret.put(key, null);
        return ret;
    }

    private static Stream<Arguments> approveOrdersMajorPriorityScenarios() {
        return Stream.of(
            Arguments.of(1000, Map.of("draftOrderUrgency", Map.of("urgency", List.of("YES")))),
            Arguments.of(4000, Map.of("draftOrderUrgency", Map.of("urgency", List.of("NO")))),
            Arguments.of(4000, Map.of("draftOrderUrgency", toNullValueMap("urgency"))),
            Arguments.of(4000, toNullValueMap("draftOrderUrgency")),
            Arguments.of(4000, null)
        );
    }

    @ParameterizedTest
    @MethodSource("approveOrdersMajorPriorityScenarios")
    void testApproveOrdersMajorPriority(int expected, Map<String, Object> caseData) {
        DmnDecisionTableImpl logic = (DmnDecisionTableImpl) decision.getDecisionLogic();
        String feelExpression = getValueFromWaConfiguration(logic, "approveOrders", "majorPriority");

        FeelEngine feelEngine = new FeelEngine.Builder().build();

        Either<FeelEngine.Failure, Object> result =
            feelEngine.evalExpression(feelExpression, caseData == null ? toNullValueMap("caseData")
                : Map.of("caseData", caseData));
        assertEquals(BigDecimal.decimal(expected), result.toOption().get());
    }
}
