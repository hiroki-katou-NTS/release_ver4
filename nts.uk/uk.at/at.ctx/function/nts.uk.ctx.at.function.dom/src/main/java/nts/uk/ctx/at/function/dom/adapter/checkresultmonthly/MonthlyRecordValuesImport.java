package nts.uk.ctx.at.function.dom.adapter.checkresultmonthly;

/*
 * author : thuongtv
 */


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Getter
@Setter
@NoArgsConstructor

public class MonthlyRecordValuesImport {
	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日付 */
	private ClosureDate closureDate;
	/** 項目値リスト */
	private List<ItemValue> itemValues;
	
	public MonthlyRecordValuesImport(YearMonth yearMonth,ClosureId closureId,ClosureDate closureDate){
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.itemValues = new ArrayList<>();
	}

	public static MonthlyRecordValuesImport of(YearMonth yearMonth,ClosureId closureId,ClosureDate closureDate, List<ItemValue> itemValues) {

		MonthlyRecordValuesImport domain = new MonthlyRecordValuesImport(yearMonth,closureId,closureDate);
		domain.itemValues = itemValues;
		return domain;
	}
}
