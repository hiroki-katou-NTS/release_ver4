package nts.uk.ctx.at.function.dom.scheduletable;

import nts.arc.enums.EnumAdaptor;

/**
 * @author dan_pv
 * Do NOT use this. 
 * please use nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.PersonalCounterCategory
 * この列挙を使わないでください
 * 本物のnts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.PersonalCounterCategoryを使ってください。
 */
public enum PersonalCounterCategory {
	
    /** 月間想定給与額   */
    MONTHLY_EXPECTED_SALARY(0),

    /** 累計想定給与額  */
    CUMULATIVE_ESTIMATED_SALARY(1),

    /** 基準労働時間比較 */
    STANDARD_WORKING_HOURS_COMPARISON(2),

    /** 労働時間 */
    WORKING_HOURS(3),

    /** 夜勤時間 */
    NIGHT_SHIFT_HOURS(4),

    /** 週間休日日数 */
    WEEKS_HOLIDAY_DAYS(5),
 
    /** 出勤・休日日数 */
    ATTENDANCE_HOLIDAY_DAYS(6), 

    /** 回数集計１ */
    TIMES_COUNTING_1(7),
    
    /** 回数集計２ */
    TIMES_COUNTING_2(8),
    
    /** 回数集計３ */
    TIMES_COUNTING_3(9);
	
    public int value;

    private PersonalCounterCategory(int value) {
        this.value = value;
    }
    
    public static PersonalCounterCategory of(int value) {
		return EnumAdaptor.valueOf(value, PersonalCounterCategory.class);
	}

}
