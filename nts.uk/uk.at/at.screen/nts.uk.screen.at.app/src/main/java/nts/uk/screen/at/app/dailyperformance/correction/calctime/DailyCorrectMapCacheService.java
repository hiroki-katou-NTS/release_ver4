package nts.uk.screen.at.app.dailyperformance.correction.calctime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;

/**
 * @author thanhnx
 * 実績変更時処理											
 */
@Stateless
public class DailyCorrectMapCacheService {

	public List<DailyRecordDto> updateDomainCache(List<DailyRecordDto> dailyEdits, DPItemValue itemEdit) {
		DailyRecordDto dtoEdit = dailyEdits.stream()
				.filter(x -> equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEdit)).findFirst().orElse(null);
		ItemValue itemValue = new ItemValue(itemEdit.getValue(),
				itemEdit.getValueType() == null ? ValueType.UNKNOWN : ValueType.valueOf(itemEdit.getValueType()),
				itemEdit.getLayoutCode(), itemEdit.getItemId());

		AttendanceItemUtil.fromItemValues(dtoEdit, Arrays.asList(itemValue));
		val dailyEditsResult = dailyEdits.stream().map(x -> {
			if (equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEdit)) {
				dtoEdit.getWorkInfo().setVersion(x.getWorkInfo().getVersion());
				return dtoEdit;
			} else {
				return x;
			}
		}).collect(Collectors.toList());

		return dailyEditsResult;
	}

	private boolean equalEmpAndDate(String employee, GeneralDate date, DPItemValue itemEdit) {
		return employee.equals(itemEdit.getEmployeeId()) && date.equals(itemEdit.getDate());
	}
}
