package nts.uk.ctx.bs.employee.pub.temporaryabsence;

import lombok.Getter;

import java.util.List;

@Getter
public class TempAbsenceHistoryExport {

    /**
     * 会社ID
     */
    private String companyId;

    /**
     * 社員ID
     */
    private String employeeId;

    /**
     * 期間
     */
    private List<DateHistoryItemExport> dateHistoryItems;

}
