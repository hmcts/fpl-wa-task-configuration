package uk.gov.hmcts.reform.fpl.dmn;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AdditionalData {

    private Map<String, String> data;

}
