package nts.uk.ctx.at.shared.dom.alarmList.extractionResult;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.alarmList.AlarmCategory;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCondValueDto {
	/**カテゴリ	 */
	private AlarmCategory category;
	/**アラームチェック条件コード	 */
	private String condCode;
	/**	、アラームチェック条件のチェックNO、アラームチェック条件のチェック種類 */
	private List<AlarmListCheckInfor> mapCondCdCheckNoType;

}
