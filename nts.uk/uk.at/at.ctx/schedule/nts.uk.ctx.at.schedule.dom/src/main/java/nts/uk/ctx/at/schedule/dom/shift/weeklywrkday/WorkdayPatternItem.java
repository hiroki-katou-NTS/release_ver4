package nts.uk.ctx.at.schedule.dom.shift.weeklywrkday;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkdayDivision;

/**
 * The Class WorkdayPatternItem.
 */
//曜日勤務設定リスト
@Getter
public class WorkdayPatternItem extends DomainObject {

    /** The day of week. */
    //曜日
    private DayOfWeek dayOfWeek;

    /** The workday division. */
    // 稼働日区分
    private WorkdayDivision workdayDivision;

    public WorkdayPatternItem(DayOfWeek dayOfWeek, WorkdayDivision workdayDivision) {
        this.dayOfWeek = dayOfWeek;
        this.workdayDivision = workdayDivision;
    }

}
