package nts.uk.ctx.at.record.app.find.monthly.root;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.common.TimeUsedNumberDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveAttdRateDaysDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveGrantDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.HalfDayAnnualLeaveDto;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveAttdRateDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveGrant;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveMaxRemainingTime;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.HalfDayAnnualLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.RealAnnualLeave;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
/** 年休月別残数データ */
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_ANNUAL_LEAVING_REMAIN_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class AnnLeaRemNumEachMonthDto extends MonthlyItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
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

	/** 締め期間: 期間 */
	@AttendanceItemLayout(jpPropertyName = PERIOD, layout = LAYOUT_A)
	private DatePeriodDto datePeriod;

	/** 締め処理状態 */
	@AttendanceItemLayout(jpPropertyName = CLOSURE_STATE, layout = LAYOUT_B)
	@AttendanceItemValue(type = ValueType.ATTR)
	private int closureStatus;

	/** 年休 */
	@AttendanceItemLayout(jpPropertyName = ANNUNAL_LEAVE, layout = LAYOUT_C)
	private AnnualLeaveDto annualLeave;

	/** 実年休 */
	@AttendanceItemLayout(jpPropertyName = REAL + ANNUNAL_LEAVE, layout = LAYOUT_D)
	private AnnualLeaveDto realAnnualLeave;

	/** 半日年休 */
	@AttendanceItemLayout(jpPropertyName = HALF_DAY + ANNUNAL_LEAVE, layout = LAYOUT_E)
	private HalfDayAnnualLeaveDto halfDayAnnualLeave;

	/** 実半日年休 */
	@AttendanceItemLayout(jpPropertyName = REAL + HALF_DAY + ANNUNAL_LEAVE, layout = LAYOUT_F)
	private HalfDayAnnualLeaveDto realHalfDayAnnualLeave;

	/** 年休付与情報 */
	@AttendanceItemLayout(jpPropertyName = ANNUNAL_LEAVE + GRANT, layout = LAYOUT_G)
	private AnnualLeaveGrantDto annualLeaveGrant;

	/** 上限残時間 */
	@AttendanceItemLayout(jpPropertyName = UPPER_LIMIT + REMAIN, layout = LAYOUT_H)
	private TimeUsedNumberDto maxRemainingTime;

	/** 実上限残時間 */
	@AttendanceItemLayout(jpPropertyName = REAL + UPPER_LIMIT + REMAIN, layout = LAYOUT_I)
	private TimeUsedNumberDto realMaxRemainingTime;

	/** 年休出勤率日数 */
	@AttendanceItemLayout(jpPropertyName = ATTENDANCE + RATE, layout = LAYOUT_J)
	private AnnualLeaveAttdRateDaysDto attendanceRateDays;

	/** 付与区分 */
	@AttendanceItemLayout(jpPropertyName = GRANT + ATTRIBUTE, layout = LAYOUT_K)
	@AttendanceItemValue(type = ValueType.FLAG)
	private boolean grantAtr;

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	public static AnnLeaRemNumEachMonthDto from(AnnLeaRemNumEachMonth domain) {
		AnnLeaRemNumEachMonthDto dto = new AnnLeaRemNumEachMonthDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYm(domain.getYearMonth());
			dto.setClosureID(domain.getClosureId() == null ? 1 : domain.getClosureId().value);
			dto.setClosureDate(domain.getClosureDate() == null ? null : ClosureDateDto.from(domain.getClosureDate()));
			dto.setDatePeriod(DatePeriodDto.from(domain.getClosurePeriod()));
			dto.setClosureStatus(domain.getClosureStatus().value);
			dto.setAnnualLeave(AnnualLeaveDto.from(domain.getAnnualLeave()));
			dto.setRealAnnualLeave(AnnualLeaveDto.from(domain.getRealAnnualLeave()));
			dto.setHalfDayAnnualLeave(HalfDayAnnualLeaveDto.from(domain.getHalfDayAnnualLeave().orElse(null)));
			dto.setRealHalfDayAnnualLeave(HalfDayAnnualLeaveDto.from(domain.getRealHalfDayAnnualLeave().orElse(null)));
			dto.setAnnualLeaveGrant(AnnualLeaveGrantDto.from(domain.getAnnualLeaveGrant().orElse(null)));
			dto.setMaxRemainingTime(TimeUsedNumberDto.from(domain.getMaxRemainingTime().orElse(null)));
			dto.setRealMaxRemainingTime(TimeUsedNumberDto.from(domain.getRealMaxRemainingTime().orElse(null)));
			dto.setAttendanceRateDays(AnnualLeaveAttdRateDaysDto.from(domain.getAttendanceRateDays()));
			dto.setGrantAtr(domain.isGrantAtr());
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public AnnLeaRemNumEachMonth toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		if(!this.isHaveData()) {
			return null;
		}
		if(employeeId == null){
			employeeId = this.employeeId;
		}
		if(ym == null){
			ym = this.ym;
		}else {
			if(datePeriod == null){
				datePeriod = new DatePeriodDto(GeneralDate.ymd(ym.year(), ym.month(), 1), 
						GeneralDate.ymd(ym.year(), ym.month(), ym.lastDateInMonth()));
			}
		}
		if(closureDate == null){
			closureDate = this.closureDate;
		}
		return AnnLeaRemNumEachMonth.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class),
				closureDate == null ? null : closureDate.toDomain(), datePeriod == null ? null : datePeriod.toDomain(),
				closureStatus == ClosureStatus.PROCESSED.value ? ClosureStatus.PROCESSED : ClosureStatus.UNTREATED,
				annualLeave == null ? new AnnualLeave() : annualLeave.toDomain(), 
				realAnnualLeave == null ? new RealAnnualLeave() : realAnnualLeave.toRealDomain(),
				Optional.of(halfDayAnnualLeave == null ? new HalfDayAnnualLeave() : halfDayAnnualLeave.toDomain()), 
				Optional.of(realHalfDayAnnualLeave == null ? new HalfDayAnnualLeave() : realHalfDayAnnualLeave.toDomain()), 
				Optional.of(annualLeaveGrant == null ? new AnnualLeaveGrant() : annualLeaveGrant.toDomain()),
				Optional.of(maxRemainingTime == null ? new AnnualLeaveMaxRemainingTime()  : maxRemainingTime.toMaxRemainingTimeDomain()), 
				Optional.of(realMaxRemainingTime == null ? new AnnualLeaveMaxRemainingTime() : realMaxRemainingTime.toMaxRemainingTimeDomain()),
				attendanceRateDays == null ? new AnnualLeaveAttdRateDays() : attendanceRateDays.toAttdRateDaysDomain(), grantAtr);
	}

	@Override
	public YearMonth yearMonth() {
		return this.ym;
	}
}
