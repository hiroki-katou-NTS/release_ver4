package nts.uk.ctx.at.record.dom.adapter.auth.wkpmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.calendar.period.DatePeriod;

@AllArgsConstructor
@Getter
public class WorkplaceManagerImport {
    /**
     * 職場管理者ID
     */
    private String workplaceManagerId;
    /**
     * 社員ID
     */
    private String employeeId;
    /**
     * 職場ID
     */
    private String workplaceId;
    /**
     * 履歴期間
     */
    private DatePeriod historyPeriod;
}