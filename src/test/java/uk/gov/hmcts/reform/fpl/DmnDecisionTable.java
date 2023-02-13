package uk.gov.hmcts.reform.fpl;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DmnDecisionTable {

    WA_TASK_CANCELLATION("wa-task-cancellation-publiclaw-care_supervision_epo",
                       "wa-task-cancellation-publiclaw-care_supervision_epo.dmn"),
    WA_TASK_COMPLETION("wa-task-completion-publiclaw-care_supervision_epo",
                       "wa-task-completion-publiclaw-care_supervision_epo.dmn"),
    WA_TASK_CONFIGURATION("wa-task-configuration-publiclaw-care_supervision_epo",
                       "wa-task-configuration-publiclaw-care_supervision_epo.dmn"),
    WA_TASK_INITIATION("wa-task-initiation-publiclaw-care_supervision_epo",
                       "wa-task-initiation-publiclaw-care_supervision_epo.dmn"),
    WA_TASK_PERMISSIONS("wa-task-permissions-publiclaw-care_supervision_epo",
                       "wa-task-permissions-publiclaw-care_supervision_epo.dmn"),
    WA_TASK_TYPES("wa-task-types-publiclaw-care_supervision_epo",
                       "wa-task-types-publiclaw-care_supervision_epo.dmn");

    @JsonValue
    private final String key;
    private final String fileName;

    DmnDecisionTable(String key, String fileName) {
        this.key = key;
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return fileName;
    }
}
