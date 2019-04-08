package nts.uk.ctx.at.record.app.find.monthly.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ExcessOutsideWorkOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.MonthlyCalculationDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.TotalCountByPeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.VerticalTotalOfMonthlyDto;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.totalcount.TotalCountByPeriod;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の勤怠時間 */
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_ATTENDANCE_TIME_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
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
	@AttendanceItemLayout(jpPropertyName = PERIOD, layout = LAYOUT_C)
	private DatePeriodDto datePeriod;

	/** 月の計算: 月別実績の月の計算 */
	@AttendanceItemLayout(jpPropertyName = CALC, layout = LAYOUT_D)
	private MonthlyCalculationDto monthlyCalculation;

	/** 時間外超過: 月別実績の時間外超過 */
	@AttendanceItemLayout(jpPropertyName = EXCESS, layout = LAYOUT_E)
	private ExcessOutsideWorkOfMonthlyDto excessOutsideWork;

	/** 集計日数: 勤怠月間日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = AGGREGATE + DAYS, layout = LAYOUT_F)
	private double aggregateDays;

	/** 縦計: 期間別の縦計 */
	@AttendanceItemLayout(jpPropertyName = VERTICAL_TOTAL, layout = LAYOUT_G)
	private VerticalTotalOfMonthlyDto verticalTotal;

	/** 回数集計: 期間別の回数集計 */
	@AttendanceItemLayout(jpPropertyName = COUNT + AGGREGATE, layout = LAYOUT_H)
	private TotalCountByPeriodDto totalCount;

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
			dto.setClosureDate(domain.getClosureDate() == null ? null : ClosureDateDto.from(domain.getClosureDate()));
			dto.setDatePeriod(domain.getDatePeriod() == null ? null 
					: new DatePeriodDto(domain.getDatePeriod().start(), domain.getDatePeriod().end()));
//			dto.setAggregateTimes(aggregateTimes);
			dto.setMonthlyCalculation(MonthlyCalculationDto.from(domain.getMonthlyCalculation()));
			dto.setExcessOutsideWork(ExcessOutsideWorkOfMonthlyDto.from(domain.getExcessOutsideWork()));
			dto.setAggregateDays(domain.getAggregateDays() == null ? 0 : domain.getAggregateDays().v());
			dto.setVerticalTotal(VerticalTotalOfMonthlyDto.from(domain.getVerticalTotal()));
			dto.setTotalCount(TotalCountByPeriodDto.from(domain.getTotalCount()));
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public AttendanceTimeOfMonthly toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		if(!this.isHaveData()) {
			return null;
		}
		if(employeeId == null){
			employeeId = this.employeeId;
		} 
		if(ym == null){
			ym = this.ym;
		} else {
			if(datePeriod == null){
				datePeriod = new DatePeriodDto(GeneralDate.ymd(ym.year(), ym.month(), 1), 
						GeneralDate.ymd(ym.year(), ym.month(), ym.lastDateInMonth()));
			}
		}
		if(closureDate == null){
			closureDate = this.closureDate;
		}
		return AttendanceTimeOfMonthly.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class), 
				closureDate == null ? null : closureDate.toDomain(), 
				datePeriod == null ? null : new DatePeriod(datePeriod.getStart(), datePeriod.getEnd()), 
				monthlyCalculation == null ? new MonthlyCalculation() : monthlyCalculation.toDomain(), 
				excessOutsideWork == null ? new ExcessOutsideWorkOfMonthly() : excessOutsideWork.toDomain(), 
				verticalTotal == null ? new VerticalTotalOfMonthly() : verticalTotal.toDomain(),
				this.totalCount == null ? new TotalCountByPeriod() : this.totalCount.toDomain(),
				new AttendanceDaysMonth(aggregateDays));
	}

	@Override
	public YearMonth yearMonth() {
		return this.ym;
	}
}
