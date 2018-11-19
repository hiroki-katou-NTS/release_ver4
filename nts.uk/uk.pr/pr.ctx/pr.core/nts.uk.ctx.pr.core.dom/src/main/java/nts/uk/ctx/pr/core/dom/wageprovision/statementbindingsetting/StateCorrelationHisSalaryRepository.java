package nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting;

import nts.uk.shr.com.history.YearMonthHistoryItem;

import java.util.Optional;

/**
* 明細書紐付け履歴（給与分類）
*/
public interface StateCorrelationHisSalaryRepository {

    Optional<StateCorrelationHisSalary> getStateCorrelationHisSalaryByCid(String cid);

    Optional<StateCorrelationHisSalary> getStateCorrelationHisSalaryByKey(String cid,String hisId);

    void add(String cid, YearMonthHistoryItem history);

    void update(String cid, YearMonthHistoryItem history);

    void remove(String cid, String hisId);

}
