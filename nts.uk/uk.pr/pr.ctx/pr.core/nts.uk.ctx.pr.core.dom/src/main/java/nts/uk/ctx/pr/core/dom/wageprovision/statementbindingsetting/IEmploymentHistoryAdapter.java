package nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting;

import nts.arc.time.GeneralDate;

import java.util.List;
import java.util.Optional;


public interface IEmploymentHistoryAdapter {
    /**
     * RequestList326
     * @param sID
     * @return List<社員の雇用履歴>
     */
    List<EmploymentHisOfEmployee> getEmploymentHisBySid(String sID);

    /**
     * (給与）「社員雇用履歴」を取得する
     * @param cid
     * @param baseDate
     * @return
     */
    List<EmploymentHisExport> getEmploymentHistoryItem(String cid, GeneralDate baseDate);
    /**
     * (給与)「雇用履歴」を取得する (salary)
     * @param historyId
     * @param employmentCode
     * @return
     */
    Optional<EmploymentHisExport> getEmploymentHistory(String historyId, String employmentCode);
}
