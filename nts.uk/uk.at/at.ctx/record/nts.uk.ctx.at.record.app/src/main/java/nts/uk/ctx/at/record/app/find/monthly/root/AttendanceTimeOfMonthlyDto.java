package nts.uk.ctx.at.record.app.find.monthly.root;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AggregateTimesDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ExcessOutsideWorkOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.MonthlyCalculationDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.VerticalTotalOfMonthlyDto;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の勤怠時間 */
@AttendanceItemRoot(rootName = "月別実績の勤怠時間", itemType = AttendanceItemType.MONTHLY_ITEM)
public class AttendanceTimeOfMonthlyDto extends MonthlyItemCommon {
	/** 会社ID */
	private String companyId;

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

	/** 期間: 期間 */
	@AttendanceItemLayout(jpPropertyName = "期間", layout = "C")
	private DatePeriodDto datePeriod;

	/** 回数集計: 期間別の回数集計 */
	@AttendanceItemLayout(jpPropertyName = "回数集計", layout = "D", listMaxLength = 30, indexField = "no")
	private List<AggregateTimesDto> aggregateTimes;

	/** 月の計算: 月別実績の月の計算 */
	@AttendanceItemLayout(jpPropertyName = "月の計算", layout = "E")
	private MonthlyCalculationDto monthlyCalculation;

	/** 時間外超過: 月別実績の時間外超過 */
	@AttendanceItemLayout(jpPropertyName = "時間外超過", layout = "F")
	private ExcessOutsideWorkOfMonthlyDto excessOutsideWork;

	/** 集計日数: 勤怠月間日数 */
	@AttendanceItemValue(type = ValueType.DOUBLE)
	@AttendanceItemLayout(jpPropertyName = "集計日数", layout = "G")
	private Double aggregateDays;

	/** 縦計: 期間別の縦計 */
	@AttendanceItemLayout(jpPropertyName = "縦計", layout = "H")
	private VerticalTotalOfMonthlyDto verticalTotal;

	@Override
	public String employeeId() {
		return this.employeeId;
	}
	
	public static AttendanceTimeOfMonthlyDto from(AttendanceTimeOfMonthly domain) {
		AttendanceTimeOfMonthlyDto dto = new AttendanceTimeOfMonthlyDto();
		if(domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYm(domain.getYearMonth());
			dto.setClosureID(domain.getClosureId() == null ? 1 : domain.getClosureId().value);
			dto.setClosureDate(domain.getClosureDate() == null ? null 
					: new ClosureDateDto(domain.getClosureDate().getClosureDay().v(), domain.getClosureDate().getLastDayOfMonth()));
			dto.setDatePeriod(domain.getDatePeriod() == null ? null 
					: new DatePeriodDto(domain.getDatePeriod().start(), domain.getDatePeriod().end()));
//			dto.setAggregateTimes(aggregateTimes);
			dto.setMonthlyCalculation(MonthlyCalculationDto.from(domain.getMonthlyCalculation()));
			dto.setExcessOutsideWork(ExcessOutsideWorkOfMonthlyDto.from(domain.getExcessOutsideWork()));
			dto.setAggregateDays(domain.getAggregateDays() == null ? null : domain.getAggregateDays().v());
			dto.setVerticalTotal(VerticalTotalOfMonthlyDto.from(domain.getVerticalTotal()));
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public AttendanceTimeOfMonthly toDomain() {
		if(!this.isHaveData()) {
			return null;
		}
		return AttendanceTimeOfMonthly.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class), 
										new ClosureDate(closureDate.getClosureDay(), closureDate.getLastDayOfMonth()), 
										new DatePeriod(datePeriod.getStart(), datePeriod.getEnd()), 
										monthlyCalculation == null ? null : monthlyCalculation.toDomain(), 
										excessOutsideWork == null ? null : excessOutsideWork.toDomain(), 
										verticalTotal == null ? null : verticalTotal.toDomain(), 
										aggregateDays == null ? null : new AttendanceDaysMonth(aggregateDays));

//		// TODO Auto-generated method stub
//		return AttendanceTimeOfMonthly.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class), 
//				new ClosureDate(closureDate.getClosureDay(), closureDate.getLastDayOfMonth()), 
//				new DatePeriod(datePeriod.getStart(), datePeriod.getEnd()), 
//				monthlyCalculation, excessOutsideWork, verticalTotal, 
//				aggregateDays == null ? null : new AttendanceDaysMonth(aggregateDays));
	}

	@Override
	public YearMonth yearMonth() {
		return this.ym;
	}
}
