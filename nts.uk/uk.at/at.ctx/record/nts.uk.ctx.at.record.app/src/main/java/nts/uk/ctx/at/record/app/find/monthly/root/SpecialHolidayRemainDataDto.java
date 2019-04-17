package nts.uk.ctx.at.record.app.find.monthly.root;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DatePeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.SpecialLeaveDto;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveGrantUseDay;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 特別休暇残数月別データ */
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_SPECIAL_HOLIDAY_REMAIN_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class SpecialHolidayRemainDataDto extends MonthlyItemCommon {

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
	
	/** 特別休暇コード */
	@AttendanceItemLayout(jpPropertyName = SPECIAL_HOLIDAY + CODE, layout = LAYOUT_C)
	@AttendanceItemValue(type = ValueType.CODE)
	private int no;
	
	/** 実特別休暇 */
	@AttendanceItemLayout(jpPropertyName = REAL + SPECIAL_HOLIDAY, layout = LAYOUT_D)
	private SpecialLeaveDto actualSpecial;
	
	/** 特別休暇*/
	@AttendanceItemLayout(jpPropertyName = SPECIAL_HOLIDAY, layout = LAYOUT_E)
	private SpecialLeaveDto specialLeave;
	
	/** 付与区分*/
	@AttendanceItemLayout(jpPropertyName = GRANT + ATTRIBUTE, layout = LAYOUT_F)
	@AttendanceItemValue(type = ValueType.FLAG)
	private boolean grantAtr;
	
	/** 特別休暇付与情報: 付与日数 */
	@AttendanceItemLayout(jpPropertyName = GRANT + DAYS, layout = LAYOUT_G)
	@AttendanceItemValue(type = ValueType.DAYS)
	private Double grantDays;
	
	@Override
	public String employeeId() {
		return employeeId;
	}
	
	public static SpecialHolidayRemainDataDto from(SpecialHolidayRemainData domain){
		SpecialHolidayRemainDataDto dto = new SpecialHolidayRemainDataDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getSid());
			dto.setYm(domain.getYm());
			dto.setClosureID(domain.getClosureId());
			dto.setClosureDate(domain.getClosureDate() == null ? null : ClosureDateDto.from(domain.getClosureDate()));
			dto.setDatePeriod(DatePeriodDto.from(domain.getClosurePeriod()));
			dto.setClosureStatus(domain.getClosureStatus().value);
			dto.setNo(domain.getSpecialHolidayCd());
			dto.setActualSpecial(SpecialLeaveDto.from(domain.getActualSpecial()));
			dto.setSpecialLeave(SpecialLeaveDto.from(domain.getSpecialLeave()));
			dto.setGrantAtr(domain.isGrantAtr());
			dto.setGrantDays(domain.getGrantDays().isPresent() ? domain.getGrantDays().get().v() : null);
			dto.exsistData();
		}
		return dto;
	}
	
	@Override
	public SpecialHolidayRemainData toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		return new SpecialHolidayRemainData(
				employeeId,
				ym,
				closureID, 
				datePeriod == null ? null : datePeriod.toDomain(), 
				closureStatus == ClosureStatus.PROCESSED.value ? ClosureStatus.PROCESSED : ClosureStatus.UNTREATED,
				closureDate == null ? null : closureDate.toDomain(),
				no, actualSpecial == null ? null : actualSpecial.toActualDomain(), 
				specialLeave == null ? null : specialLeave.toDomain(),
				grantAtr,
				Optional.ofNullable(grantDays == null ? null : new SpecialLeaveGrantUseDay(grantDays)));
	}
	@Override
	public YearMonth yearMonth() {
		return ym;
	}
}
