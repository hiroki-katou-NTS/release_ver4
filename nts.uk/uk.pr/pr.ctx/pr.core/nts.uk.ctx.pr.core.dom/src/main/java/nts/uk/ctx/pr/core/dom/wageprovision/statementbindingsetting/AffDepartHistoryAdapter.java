package nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting;

import nts.arc.time.GeneralDate;

import java.util.Optional;


public interface AffDepartHistoryAdapter {
    Optional<AffDepartHistory> getDepartmentByBaseDate(String employeeId, GeneralDate baseDate);
}
