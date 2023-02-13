package uk.gov.hmcts.reform.fpl.dmn;

public enum ProcessCategory {

    CASE_PROGRESSION("case progression"),
    CASE_CREATION("case creation"),
    MANAGE_OUTCOME("manage outcome");

    private String value;

    ProcessCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
