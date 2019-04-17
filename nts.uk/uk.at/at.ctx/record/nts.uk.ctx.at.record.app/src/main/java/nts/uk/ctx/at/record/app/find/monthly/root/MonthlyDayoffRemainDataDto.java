package nts.uk.ctx.at.record.app.find.monthly.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DayAndTimeDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

/** 代休月別残数データ */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_OFF_REMAIN_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class MonthlyDayoffRemainDataDto extends MonthlyItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月: 年月 */
	private YearMonth ym;

	/** 締めID: 締めID */
	// @AttendanceItemValue
	// @AttendanceItemLayout(jpPropertyName = "締めID", layout = "A")
	private int closureID = 1;

	/** 締め日: 日付 */
	// @AttendanceItemLayout(jpPropertyName = "締め日", layout = "B")
	private ClosureDateDto closureDate;

	/** 締め期間: 期間 */
	@AttendanceItemLayout(jpPropertyName = PERIOD, layout = LAYOUT_A)
	private DatePeriodDto datePeriod;

	/** 締め処理状態 */
	@AttendanceItemLayout(jpPropertyName = CLOSURE_STATE, layout = LAYOUT_B)
	@AttendanceItemValue(type = ValueType.ATTR)
	private int closureStatus;
	
	/**	発生 */
	@AttendanceItemLayout(jpPropertyName = OCCURRENCE, layout = LAYOUT_C)
	private DayAndTimeDto occurrenceDayTimes;	
	
	/**	使用 */
	@AttendanceItemLayout(jpPropertyName = USAGE, layout = LAYOUT_D)
	private DayAndTimeDto useDayTimes;	
	
	/**	残日数, 残時間 */
	@AttendanceItemLayout(jpPropertyName = REMAIN, layout = LAYOUT_E)
	private DayAndTimeDto remainingDayTimes;
	
	/**	繰越日数, 	繰越時間 */
	@AttendanceItemLayout(jpPropertyName = CARRY_FORWARD, layout = LAYOUT_F)
	private DayAndTimeDto carryForWardDayTimes;	
	
	/**	未消化日数, 未消化時間 */
	@AttendanceItemLayout(jpPropertyName = NOT_DIGESTION, layout = LAYOUT_G)
	private DayAndTimeDto unUsedDayTimes;
	
	@Override
	public String employeeId() {
		return employeeId;
	}
	@Override
	public MonthlyDayoffRemainData toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		return new MonthlyDayoffRemainData(employeeId, ym, closureID, 
				closureDate == null ? 1 : closureDate.getClosureDay(), 
				closureDate == null ? false : closureDate.getLastDayOfMonth(), 
				closureStatus == ClosureStatus.PROCESSED.value ? ClosureStatus.PROCESSED : ClosureStatus.UNTREATED,
				datePeriod == null ? null : datePeriod.getStart(), 
				datePeriod == null ? null : datePeriod.getEnd(), 
				occurrenceDayTimes == null ? new DayAndTimeDto().toOff() : occurrenceDayTimes.toOff(), 
				useDayTimes == null ? new DayAndTimeDto().toOff() : useDayTimes.toOff(),
				remainingDayTimes == null ? new DayAndTimeDto().toOffRemain() : remainingDayTimes.toOffRemain(), 
				carryForWardDayTimes == null ? new DayAndTimeDto().toOffRemain() : carryForWardDayTimes.toOffRemain(),
				unUsedDayTimes == null ? new DayAndTimeDto().toOff() : unUsedDayTimes.toOff());
	}
	@Override
	public YearMonth yearMonth() {
		return ym;
	}
	
	public static MonthlyDayoffRemainDataDto from(MonthlyDayoffRemainData domain){
		MonthlyDayoffRemainDataDto dto = new MonthlyDayoffRemainDataDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getSId());
			dto.setYm(domain.getYm());
			dto.setClosureID(domain.getClosureId());
			dto.setClosureDate(new ClosureDateDto(domain.getClosureDay(), domain.isLastDayis()));
			dto.setDatePeriod(new DatePeriodDto(domain.getStartDate(), domain.getEndDate()));
			dto.setClosureStatus(domain.getClosureStatus().value);
			dto.setOccurrenceDayTimes(DayAndTimeDto.from(domain.getOccurrenceDayTimes()));
			dto.setUseDayTimes(DayAndTimeDto.from(domain.getUseDayTimes()));
			dto.setRemainingDayTimes(DayAndTimeDto.from(domain.getRemainingDayTimes()));
			dto.setCarryForWardDayTimes(DayAndTimeDto.from(domain.getCarryForWardDayTimes()));
			dto.setUnUsedDayTimes(DayAndTimeDto.from(domain.getUnUsedDayTimes()));
			dto.exsistData();
		}
		return dto;
	}
}
