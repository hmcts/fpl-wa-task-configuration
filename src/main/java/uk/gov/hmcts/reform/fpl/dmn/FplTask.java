package uk.gov.hmcts.reform.fpl.dmn;

public enum FplTask {

    REVIEW_MESSAGE_ALLOCATED_JUDGE("reviewMessageAllocatedJudge"),
    REVIEW_RESPONSE_ALLOCATED_JUDGE("reviewResponseAllocatedJudge"),
    REVIEW_MESSAGE_HEARING_JUDGE("reviewMessageHearingJudge"),
    REVIEW_RESPONSE_HEARING_JUDGE("reviewResponseHearingJudge"),
    REVIEW_MESSAGE_HEARING_CENTRE_ADMIN("reviewMessageHearingCentreAdmin"),
    REVIEW_RESPONSE_HEARING_CENTRE_ADMIN("reviewResponseHearingCentreAdmin"),
    REVIEW_MESSAGE_CTSC("reviewMessageCTSC"),
    REVIEW_RESPONSE_CTSC("reviewResponseCTSC"),
    REVIEW_MESSAGE_LEGAL_ADVISOR("reviewMessageLegalAdviser"),
    REVIEW_RESPONSE_LEGAL_ADVISOR("reviewResponseLegalAdviser"),
    VIEW_ADDITIONAL_APPLICATIONS("viewAdditionalApplications"),
    APPROVE_ORDERS("approveOrders"),
    REVIEW_URGENT_APPLICATION("reviewUrgentApplication"),
    REVIEW_STANDARD_APPLICATION("reviewStandardApplication"),
    REVIEW_STANDARD_DIRECTION_ORDER("reviewStandardDirectionOrder"),
    REVIEW_STANDARD_DIRECTION_ORDER_HIGH_COURT("reviewStandardDirectionOrderHighCourt"),
    REVIEW_ORDER_CMO("reviewOrderCMO"),
    REVIEW_ORDER_CMO_HIGH_COURT("reviewOrderCMOHighCourt"),
    REVIEW_FAILED_PAYMENT("reviewFailedPayment"),
    REVIEW_FAILED_PAYMENT_HIGH_COURT("reviewFailedPaymentHighCourt"),
    REVIEW_CORRESPONDENCE("reviewCorrespondence"),
    REVIEW_CORRESPONDENCE_HIGH_COURT("reviewCorrespondenceHighCourt"),
    CHECK_PLACEMENT_APPLICATION("checkPlacementApplication"),
    CHECK_PLACEMENT_APPLICATION_HIGH_COURT("checkPlacementApplicationHighCourt"),
    CHASE_OUTSTANDING_ORDER("chaseOutstandingOrder");

    private String value;

    FplTask(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
