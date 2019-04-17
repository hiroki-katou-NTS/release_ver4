package nts.uk.ctx.at.record.app.find.monthly.root;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AgreementTimeBreakdownDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AgreementTimeOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.MonthlyAggregationErrorInfoDto;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeBreakdown;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.common.Year;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
/** 管理期間の36協定時間 */
@AttendanceItemRoot(rootName = ItemConst.AGREEMENT_TIME_OF_MANAGE_PERIOD_NAME, itemType = AttendanceItemType.MONTHLY_ITEM)
public class AgreementTimeOfManagePeriodDto extends MonthlyItemCommon{

	/***/
	private static final long serialVersionUID = 1L;
	
	/** 社員ID */
	private String employeeId;
	/** 月度 */
	private YearMonth yearMonth;
	/** 年度 */
	private Year year;
	
	/** 36協定時間: 月別実績の36協定時間 */
	@AttendanceItemLayout(jpPropertyName = AGREEMENT, layout = LAYOUT_A)
	private AgreementTimeOfMonthlyDto agreementTime;
	
	/** 内訳 */
	@AttendanceItemLayout(jpPropertyName = BREAK_DOWN, layout = LAYOUT_B)
	private AgreementTimeBreakdownDto breakdown;

	/** エラー情報 */
	private List<MonthlyAggregationErrorInfoDto> errorInfos;

	@Override
	public String employeeId() {
		return employeeId;
	}

	@Override
	public AgreementTimeOfManagePeriod toDomain(String employeeId, YearMonth ym, int closureID, ClosureDateDto closureDate) {
		return AgreementTimeOfManagePeriod.of(employeeId, ym, year, 
											agreementTime == null ? new AgreementTimeOfMonthly() : agreementTime.toDomain(), 
											breakdown == null ? new AgreementTimeBreakdown() : breakdown.toDomain());
	}

	@Override
	public YearMonth yearMonth() {
		return yearMonth;
	}

	@Override
	public int getClosureID() {
		return 1;
	}

	@Override
	public ClosureDateDto getClosureDate() {
		return null;
	}
	
	public static AgreementTimeOfManagePeriodDto from(AgreementTimeOfManagePeriod domain){
		AgreementTimeOfManagePeriodDto dto = new AgreementTimeOfManagePeriodDto();
		if(domain != null){
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setAgreementTime(AgreementTimeOfMonthlyDto.from(domain.getAgreementTime()));
			dto.setBreakdown(AgreementTimeBreakdownDto.from(domain.getBreakdown()));
			dto.setErrorInfos(ConvertHelper.mapTo(domain.getErrorInfos(), c -> new MonthlyAggregationErrorInfoDto(c.getResourceId(), c.getMessage().v())));
			dto.setYear(domain.getYear());
			dto.setYearMonth(domain.getYearMonth());
			dto.exsistData();
		}
		return dto;
	}
}
