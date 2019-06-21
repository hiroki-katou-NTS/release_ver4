package nts.uk.ctx.at.record.dom.workrecord.export;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.EmpAffInfoExport;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

public interface WorkRecordExport {

	/**
	 * RequestList589
	 * Alg: 社員の指定期間中の所属期間を取得する（年月）
	 * @param listSid
	 * @param period
	 * @param baseDate
	 */
	EmpAffInfoExport getAffiliationPeriod(List<String> listSid , YearMonthPeriod period , GeneralDate baseDate);
}
