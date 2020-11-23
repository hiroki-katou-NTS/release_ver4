package nts.uk.screen.at.app.monthlyperformance.correction.query;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.arc.time.calendar.period.DatePeriod;

@Data
public class MonthlyModifyResult {
	/** Attendance items */
	private List<ItemValue> items;

	/** 年月: 年月 */
	private Integer yearMonth;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureId;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;
	
		/** 月別実績の勤怠時間 期間: 期間  */
	private DatePeriod workDatePeriod;

	private long version;

	public static MonthlyModifyResult builder(){
		return new MonthlyModifyResult();
	}

	public MonthlyModifyResult employeeId(String employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	public MonthlyModifyResult items(List<ItemValue> items) {
		this.items = items;
		return this;
	}

	public MonthlyModifyResult closureDate(ClosureDateDto closureDate) {
		this.closureDate = closureDate;
		return this;
	}

	public MonthlyModifyResult yearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
		return this;
	}

	public MonthlyModifyResult closureId(Integer closureId) {
		this.closureId = closureId;
		return this;
	}

	public MonthlyModifyResult workDatePeriod(DatePeriod period) {
		this.workDatePeriod = period;
		return this;
	}
	
	public MonthlyModifyResult version(long version){
		this.version = version;
		return this;
	}	
	public MonthlyModifyResult completed(){
		return this;
	}
}
