/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.export.monthlyschedule;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 月別実績データ値 (copied from nts.uk.ctx.at.record.pub.monthly.MonthlyRecordValuesExport)
 * @author shuichu_ishida
 */
@Getter
public class MonthlyRecordValuesExport {

	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日付 */
	private ClosureDate closureDate;
	/** 項目値リスト */
	private List<ItemValue> itemValues;
	/** 社員ID - Additional field for exporting */
	public String employeeId;

	/**
	 * Instantiates a new monthly record values export.
	 *
	 * @param yearMonth the year month
	 * @param closureId the closure id
	 * @param closureDate the closure date
	 * @param itemValues the item values
	 * @param employeeId the employee id
	 */
	public MonthlyRecordValuesExport(YearMonth yearMonth, int closureId, ClosureDate closureDate,
			List<ItemValue> itemValues, String employeeId) {
		this.yearMonth = yearMonth;
		this.closureId = ClosureId.valueOf(closureId);
		this.closureDate = closureDate;
		this.itemValues = itemValues;
		this.employeeId = employeeId;
	}
	
	/**
	 * コンストラクタ
	 */
	public MonthlyRecordValuesExport(YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate){
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.itemValues = new ArrayList<>();
	}
	
	/**
	 * ファクトリー
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param itemValues 項目値リスト
	 * @return 月別実績データ値
	 */
	public static MonthlyRecordValuesExport of(
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			List<ItemValue> itemValues){
	
		MonthlyRecordValuesExport domain = new MonthlyRecordValuesExport(yearMonth, closureId, closureDate);
		domain.itemValues = itemValues;
		return domain;
	}
}
