package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutSideWorkEachBreakdown;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWork;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
/** 時間外超過 */
@NoArgsConstructor
public class ExcessOutsideWorkDto implements ItemConst {

	/** 超過NO: int */
	private int excessNo;

	/** 超過時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = TIME, layout = LAYOUT_A)
	private int breakdown;

	/** 内訳NO: int */
	private int breakdownNo;

	/** FAKE NO */
	private int no;

	public ExcessOutsideWorkDto(int excessNo, int breakdownNo, Integer breakdown) {
		super();
		this.excessNo = excessNo;
		this.breakdown = breakdown;
		this.breakdownNo = breakdownNo;
//		this.no = (breakdownNo-1) * 10 + excessNo;
		this.no = calcFakeNo(excessNo, breakdownNo);
	}

	private int calcFakeNo(int excessNo, int breakdownNo) {
		return (excessNo - 1 ) * 5 + breakdownNo;
	}
	
	private int calcBreakDownNo(int fakeNo) {
		int excessNo = fakeNo % 5;
		return excessNo == 0 ? 5 : excessNo;
	}
	
	private int calcExcessNo(int fakeNo) {
		return ((fakeNo - 1) / 5) + 1;
	}

	public ExcessOutsideWork toDomain() {
//		return ExcessOutsideWork.of((no / 10) + 1, no % 10, new AttendanceTimeMonth(breakdown));
		return ExcessOutsideWork.of(calcBreakDownNo(no), calcExcessNo(no), new AttendanceTimeMonth(breakdown));
	}

	public static ExcessOutsideWorkDto from(ExcessOutsideWork domain) {
		return new ExcessOutsideWorkDto(domain.getExcessNo(), domain.getBreakdownNo(),
				domain.getExcessTime() == null ? 0 : domain.getExcessTime().valueAsMinutes());
	}

	public static List<ExcessOutsideWorkDto> from(Map<Integer, ExcessOutSideWorkEachBreakdown> domain) {
		List<ExcessOutsideWorkDto> result = new ArrayList<>();
		if(domain != null) {
			domain.entrySet().stream().forEach(c -> {
				c.getValue().getBreakdown().entrySet().stream().forEach(b -> {
					result.add(ExcessOutsideWorkDto.from(b.getValue()));
				});
			});
		}
		return result;
	}
}
