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
import static uk.gov.hmcts.reform.fpl.dmn.ProcessCategory.CASE_PROGRESSION;

class CamundaTaskWaInitiationTest extends DmnDecisionTableBaseUnitTest {

    @BeforeAll
    public static void initialization() {
        CURRENT_DMN_DECISION_TABLE = DmnDecisionTable.WA_TASK_INITIATION;
    }

    @ParameterizedTest
    @MethodSource("scenarioProvider")
    void givenInputShouldReturnOutcomeDmn(String eventId,
                                          Map<String, String> additionalData,
                                          Map<String, ? extends Serializable> expectedDmnOutcome) {
        VariableMap inputVariables = new VariableMapImpl();
        inputVariables.putValue("eventId", eventId);
        inputVariables.putValue("additionalData", Map.of("Data", additionalData));

        DmnDecisionTableResult dmnDecisionTableResult = evaluateDmnTable(inputVariables);

        assertThat(dmnDecisionTableResult.getResultList(), is(singletonList(expectedDmnOutcome)));
    }

    public static Stream<Arguments> scenarioProvider() {
        return Stream.of(
            Arguments.of(
                "messageJudgeOrLegalAdviser",
                Map.of(
                    "latestRoleSent", "JUDICIARY",
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "reviewMessageAllocatedJudge",
                    "name", "Review Message (Allocated Judge)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "replyToMessageJudgeOrLegalAdviser",
                Map.of(
                    "latestRoleSent", "JUDICIARY",
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "reviewResponseAllocatedJudge",
                    "name", "Review Response (Allocated Judge)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "messageJudgeOrLegalAdviser",
                Map.of(
                    "latestRoleSent", "HEARING_JUDGE",
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "reviewMessageHearingJudge",
                    "name", "Review Message (Hearing Judge)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "messageJudgeOrLegalAdviser",
                Map.of(
                    "latestRoleSent", "LOCAL_COURT_ADMIN",
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "reviewMessageHearingCentreAdmin",
                    "name", "Review Message (Local Court)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "messageJudgeOrLegalAdviser",
                Map.of(
                    "latestRoleSent", "OTHER",
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "reviewMessageOther",
                    "name", "Review Message (Other Judiciary)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "uploadAdditionalApplications",
                Map.of(
                    "latestRoleSent", "OTHER"
                ),
                Map.of(
                    "taskId", "viewAdditionalApplicationsAllocatedLegalAdviser",
                    "name", "View Additional Applications (Allocated Legal Adviser)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "uploadAdditionalApplications",
                Map.of(
                    "latestRoleSent", "JUDICIARY"
                ),
                Map.of(
                    "taskId", "viewAdditionalApplicationsAllocatedJudge",
                    "name", "View Additional Applications (Allocated Judge)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "uploadCMO",
                Map.of(
                    "latestRoleSent", "HEARING_JUDGE",
                    "draftOrderNeedsReviewUploaded", true,
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "approveOrdersHearingJudge",
                    "name", "Approve Orders (Hearing Judge)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "uploadCMO",
                Map.of(
                    "latestRoleSent", "OTHER",
                    "draftOrderNeedsReviewUploaded", true,
                    "court", Map.of("code", "151")
                ),
                Map.of(
                    "taskId", "approveOrdersLegalAdviser",
                    "name", "Approve Orders (Hearing Legal Adviser)",
                    "processCategories", CASE_PROGRESSION.getValue()
                )
            ),
            Arguments.of(
                "requestListingAction",
                Map.of(
                    "lastListingRequestType", "Listing required"
                ),
                Map.of(
                    "taskId", "reviewListingAction",
                    "name", "Review listing request",
                    "processCategories", "actionType_Listing required"
                )
            ),
            Arguments.of(
                "submitApplication",
                Map.of(
                    "hearing", Map.of("hearingUrgencyType", "STANDARD")
                ),
                Map.of(
                    "taskId", "reviewStandardApplication",
                    "name", "Review Standard Application",
                    "processCategories", "case creation"
                )
            ),
            Arguments.of(
                "submitApplication",
                Map.of(
                    "hearing", Map.of("hearingUrgencyType", "SAME_DAY")
                ),
                Map.of(
                    "taskId", "reviewUrgentApplication",
                    "name", "Review Urgent Application",
                    "processCategories", "case creation"
                )
            ),
            Arguments.of(
                "submitApplication",
                Map.of(
                    "hearing", Map.of("hearingUrgencyType", "URGENT")
                ),
                Map.of(
                    "taskId", "reviewUrgentApplication",
                    "name", "Review Urgent Application",
                    "processCategories", "case creation"
                )
            )
        );
    }

    @Test
    void shouldHaveCorrectNumberOfRules() {
        // The purpose of this test is to prevent adding new rows without being tested
        DmnDecisionTableImpl logic = (DmnDecisionTableImpl) decision.getDecisionLogic();
        assertThat(logic.getRules().size(), is(28));
    }
}
