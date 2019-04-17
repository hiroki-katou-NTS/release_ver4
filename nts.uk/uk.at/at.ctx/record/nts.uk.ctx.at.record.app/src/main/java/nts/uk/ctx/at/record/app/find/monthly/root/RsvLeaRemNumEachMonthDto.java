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
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ReserveLeaveDto;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RealReserveLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveGrant;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveGrantDayNumber;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 積立年休月別残数データ */
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_RESERVE_LEAVING_REMAIN_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class RsvLeaRemNumEachMonthDto extends MonthlyItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	/** 会社ID */
	private String companyId;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月: 年月 */
	private YearMonth ym;

	/** 締めID: 締めID */
	private int closureID = 1;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;

	/** 締め期間: 期間 */
	@AttendanceItemLayout(jpPropertyName = PERIOD, layout = LAYOUT_A)
	private DatePeriodDto datePeriod;

	/** 締め処理状態 */
	@AttendanceItemValue(type = ValueType.ATTR)
	@AttendanceItemLayout(jpPropertyName = CLOSURE_STATE, layout = LAYOUT_B)
	private int closureStatus;

	/** 積立年休 */
	@AttendanceItemLayout(jpPropertyName = RETENTION, layout = LAYOUT_C)
	private ReserveLeaveDto reserveLeave;
	
	/** 実積立年休 */
	@AttendanceItemLayout(jpPropertyName = REAL + RETENTION, layout = LAYOUT_D)
	private ReserveLeaveDto realReserveLeave;
	
	/** 積立年休付与情報 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = GRANT + INFO, layout = LAYOUT_E)
	private double reserveLeaveGrant;

	/** 付与区分 */
	@AttendanceItemLayout(jpPropertyName = GRANT + ATTRIBUTE, layout = LAYOUT_F)
	@AttendanceItemValue(type = ValueType.FLAG)
	private boolean grantAtr;

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	public static RsvLeaRemNumEachMonthDto from(RsvLeaRemNumEachMonth domain) {
		RsvLeaRemNumEachMonthDto dto = new RsvLeaRemNumEachMonthDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYm(domain.getYearMonth());
			dto.setClosureID(domain.getClosureId() == null ? 1 : domain.getClosureId().value);
			dto.setClosureDate(domain.getClosureDate() == null ? null : ClosureDateDto.from(domain.getClosureDate()));
			dto.setDatePeriod(DatePeriodDto.from(domain.getClosurePeriod()));
			dto.setClosureStatus(domain.getClosureStatus().value);
			dto.setReserveLeave(ReserveLeaveDto.from(domain.getReserveLeave()));
			dto.setRealReserveLeave(ReserveLeaveDto.from(domain.getRealReserveLeave()));
			domain.getReserveLeaveGrant().ifPresent(g -> {
				dto.setReserveLeaveGrant(g.getGrantDays().v());
			});
			dto.setGrantAtr(domain.isGrantAtr());
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public RsvLeaRemNumEachMonth toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
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
		return RsvLeaRemNumEachMonth.of(employeeId, ym, ConvertHelper.getEnum(closureID, ClosureId.class),
				closureDate == null ? null : closureDate.toDomain(), datePeriod == null ? null : datePeriod.toDomain(),
				closureStatus == ClosureStatus.PROCESSED.value ? ClosureStatus.PROCESSED : ClosureStatus.UNTREATED,
				reserveLeave == null ? new ReserveLeave() : reserveLeave.toDomain(), 
				realReserveLeave == null ? new RealReserveLeave() : realReserveLeave.toRealDomain(),
				Optional.of(ReserveLeaveGrant.of(new ReserveLeaveGrantDayNumber(reserveLeaveGrant))),
				grantAtr);
	}

	@Override
	public YearMonth yearMonth() {
		return this.ym;
	}
}
