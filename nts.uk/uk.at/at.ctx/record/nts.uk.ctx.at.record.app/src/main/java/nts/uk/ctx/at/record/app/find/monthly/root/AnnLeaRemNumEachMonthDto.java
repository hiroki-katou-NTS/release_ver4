package nts.uk.ctx.at.record.app.find.monthly.root;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveAttdRateDaysDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AnnualLeaveGrantDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.HalfDayAnnualLeaveDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.TimeAnnualLeaveUsedTimeDto;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 年休月別残数データ */
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_ANNUAL_LEAVING_REMAIN_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class AnnLeaRemNumEachMonthDto extends MonthlyItemCommon {
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
	@AttendanceItemValue(type = ValueType.INTEGER)
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
	private TimeAnnualLeaveUsedTimeDto maxRemainingTime;

	/** 実上限残時間 */
	@AttendanceItemLayout(jpPropertyName = REAL + UPPER_LIMIT + REMAIN, layout = LAYOUT_I)
	private TimeAnnualLeaveUsedTimeDto realMaxRemainingTime;

	/** 年休出勤率日数 */
	@AttendanceItemLayout(jpPropertyName = ATTENDANCE + RATE, layout = LAYOUT_J)
	private AnnualLeaveAttdRateDaysDto attendanceRateDays;

	/** 付与区分 */
	@AttendanceItemLayout(jpPropertyName = GRANT + ATTRIBUTE, layout = LAYOUT_K)
	@AttendanceItemValue(type = ValueType.BOOLEAN)
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
			dto.setMaxRemainingTime(TimeAnnualLeaveUsedTimeDto.from(domain.getMaxRemainingTime().orElse(null)));
			dto.setRealMaxRemainingTime(TimeAnnualLeaveUsedTimeDto.from(domain.getRealMaxRemainingTime().orElse(null)));
			dto.setAttendanceRateDays(AnnualLeaveAttdRateDaysDto.from(domain.getAttendanceRateDays()));
			dto.setGrantAtr(domain.isGrantAtr());
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public AnnLeaRemNumEachMonth toDomain() {
		if (!this.isHaveData()) {
			return null;
		}
		return AnnLeaRemNumEachMonth.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class),
				closureDate == null ? null : closureDate.toDomain(), datePeriod == null ? null : datePeriod.toDomain(),
				ConvertHelper.getEnum(closureStatus, ClosureStatus.class), 
				annualLeave == null ? null : annualLeave.toDomain(), realAnnualLeave == null ? null : realAnnualLeave.toRealDomain(),
				Optional.ofNullable(halfDayAnnualLeave == null ? null : halfDayAnnualLeave.toDomain()), 
				Optional.ofNullable(realHalfDayAnnualLeave == null ? null : realHalfDayAnnualLeave.toDomain()), 
				Optional.ofNullable(annualLeaveGrant == null ? null : annualLeaveGrant.toDomain()),
				Optional.ofNullable(maxRemainingTime == null ? null : maxRemainingTime.toMaxRemainingTimeDomain()), 
				Optional.ofNullable(realMaxRemainingTime == null ? null : realMaxRemainingTime.toMaxRemainingTimeDomain()),
				attendanceRateDays == null ? null : attendanceRateDays.toAttdRateDaysDomain(), grantAtr);
	}

	@Override
	public YearMonth yearMonth() {
		return this.ym;
	}
}
