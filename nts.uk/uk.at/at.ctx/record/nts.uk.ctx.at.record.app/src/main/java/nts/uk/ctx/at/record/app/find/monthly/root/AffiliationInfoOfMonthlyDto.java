package nts.uk.ctx.at.record.app.find.monthly.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AggregateAffiliationInfoDto;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AggregateAffiliationInfo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の所属情報 */
@AttendanceItemRoot(rootName = ItemConst.MONTHLY_AFFILIATION_INFO_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class AffiliationInfoOfMonthlyDto extends MonthlyItemCommon {
	/** 年月: 年月 */
	private YearMonth yearMonth;

	/** 月初の情報: 集計所属情報 */
	@AttendanceItemLayout(jpPropertyName = START_MONTH, layout = LAYOUT_A)
	private AggregateAffiliationInfoDto startMonthInfo;

	/** 月末の情報: 集計所属情報 */
	@AttendanceItemLayout(jpPropertyName = END_MONTH, layout = LAYOUT_B)
	private AggregateAffiliationInfoDto endMonthInfo;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureID = 1;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public AffiliationInfoOfMonthly toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		if(!this.isHaveData()) {
			return null;
		}
		if(employeeId == null){
			employeeId = this.employeeId;
		}
		if(ym == null){
			ym = this.yearMonth;
		} 
		if(closureDate == null){
			closureDate = this.closureDate;
		}
		return AffiliationInfoOfMonthly.of(employeeId, yearMonth, 
									ConvertHelper.getEnum(closureID, ClosureId.class), 
									closureDate == null ? null : closureDate.toDomain(), 
									startMonthInfo == null ? new AggregateAffiliationInfo()  : startMonthInfo.toDomain(), 
									endMonthInfo == null ? new AggregateAffiliationInfo() : endMonthInfo.toDomain());
		
	}

	@Override
	public YearMonth yearMonth() {
		return yearMonth;
	}

	public static AffiliationInfoOfMonthlyDto from(AffiliationInfoOfMonthly domain) {
		AffiliationInfoOfMonthlyDto dto = new AffiliationInfoOfMonthlyDto();
		if (domain != null) {
			dto.setClosureDate(ClosureDateDto.from(domain.getClosureDate()));
			dto.setClosureID(domain.getClosureId() == null ? 1 : domain.getClosureId().value);
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setEndMonthInfo(AggregateAffiliationInfoDto.from(domain.getLastInfo()));
			dto.setStartMonthInfo(AggregateAffiliationInfoDto.from(domain.getFirstInfo()));
			dto.setYearMonth(domain.getYearMonth());
			dto.exsistData();
		}
		return dto;
	}

}
