package nts.uk.ctx.pr.core.dom.wageprovision.statebindingset;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.history.YearMonthHistoryItem;
import nts.uk.shr.com.history.strategic.ContinuousResidentHistory;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

import java.util.List;

/**
* 明細書紐付け履歴（会社）
*/
@Getter
public class StateCorreHisCom extends AggregateRoot implements ContinuousResidentHistory<YearMonthHistoryItem, YearMonthPeriod,YearMonth> {

    /**
     * 会社ID
     */
    private String cId;

    /**
     * 履歴
     */
    private List<YearMonthHistoryItem> history;
    
    public StateCorreHisCom(String cid, List<YearMonthHistoryItem> history) {
        this.cId = cid;
        this.history =  history;
    }

    @Override
    public List<YearMonthHistoryItem> items() {
        return this.history;
    }

    @Override
    public void exCorrectToRemove(YearMonthHistoryItem latest) {
        latest.changeSpan(latest.span().newSpanWithMaxEnd());
    }
    
}
