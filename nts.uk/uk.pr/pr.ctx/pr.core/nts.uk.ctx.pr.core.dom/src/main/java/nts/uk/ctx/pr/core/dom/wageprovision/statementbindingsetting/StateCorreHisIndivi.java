package nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.history.YearMonthHistoryItem;
import nts.uk.shr.com.history.strategic.UnduplicatableHistory;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
* 明細書紐付け履歴（個人）
*/
@Getter
public class StateCorreHisIndivi extends AggregateRoot implements UnduplicatableHistory<YearMonthHistoryItem, YearMonthPeriod, YearMonth> {
    
    /**
    * 社員ID
    */
    private String empID;
    
    /**
    * 履歴
    */
    private List<YearMonthHistoryItem> history;
    
    public StateCorreHisIndivi(String empId, List<YearMonthHistoryItem> history) {
        this.empID = empId;
        this.history = history;
    }

    @Override
    public List<YearMonthHistoryItem> items() {
        return history;
    }

    
}
