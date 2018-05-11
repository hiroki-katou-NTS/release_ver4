package nts.uk.ctx.at.record.app.find.monthly.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.AggregateAffiliationInfoDto;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ClosureDateDto;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の所属情報 */
public class AffiliationInfoOfMonthlyDto extends MonthlyItemCommon {
	/** 年月: 年月 */
	private YearMonth yearMonth;

	/** 月初の情報: 集計所属情報 */
	@AttendanceItemLayout(jpPropertyName = "月初の情報", layout = "A")
	private AggregateAffiliationInfoDto startMonthInfo;

	/** 月末の情報: 集計所属情報 */
	@AttendanceItemLayout(jpPropertyName = "月末の情報", layout = "B")
	private AggregateAffiliationInfoDto endMonthInfo;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureId;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public AffiliationInfoOfMonthly toDomain() {
		if (this.isHaveData()) {
			return AffiliationInfoOfMonthly.of(employeeId, yearMonth, 
										ConvertHelper.getEnum(closureId, ClosureId.class), 
										closureDate == null ? null : closureDate.toDomain(), 
										startMonthInfo == null ? null : startMonthInfo.toDomain(), 
										endMonthInfo == null ? null : endMonthInfo.toDomain());
		}
		return null;
	}

	@Override
	public YearMonth yearMonth() {
		return yearMonth;
	}

	public static AffiliationInfoOfMonthlyDto from(AffiliationInfoOfMonthly domain) {
		AffiliationInfoOfMonthlyDto dto = new AffiliationInfoOfMonthlyDto();
		if (domain != null) {
			dto.setClosureDate(ClosureDateDto.from(domain.getClosureDate()));
			dto.setClosureId(domain.getClosureId() == null ? 1 : domain.getClosureId().value);
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setEndMonthInfo(AggregateAffiliationInfoDto.from(domain.getLastInfo()));
			dto.setStartMonthInfo(AggregateAffiliationInfoDto.from(domain.getFirstInfo()));
			dto.setYearMonth(domain.getYearMonth());
			dto.exsistData();
		}
		return dto;
	}

}
