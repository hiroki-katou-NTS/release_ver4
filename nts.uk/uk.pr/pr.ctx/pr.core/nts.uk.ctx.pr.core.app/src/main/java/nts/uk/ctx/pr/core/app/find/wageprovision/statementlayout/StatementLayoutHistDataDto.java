package nts.uk.ctx.pr.core.app.find.wageprovision.statementlayout;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class StatementLayoutHistDataDto {
    private String statementCode;
    private String statementName;
    private String historyId;
    private Integer startMonth;
    private Integer endMonth;
    private StatementLayoutSetDto statementLayoutSet;
}
