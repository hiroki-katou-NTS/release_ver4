package nts.uk.screen.at.app.monthlyperformance.correction.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyModifyQuery {
	/** Attendance items*/
	private List<ItemValue> items;
	
	/** 年月: 年月 */
	private Integer yearMonth;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureId;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;
	
	private long version;

	public MonthlyModifyQuery(List<ItemValue> items, Integer yearMonth, String employeeId, int closureId,
			ClosureDateDto closureDate) {
		super();
		this.items = items;
		this.yearMonth = yearMonth;
		this.employeeId = employeeId;
		this.closureId = closureId;
		this.closureDate = closureDate;
	}
	/** Data */
//	private List<ItemValue> itemValues;
}
