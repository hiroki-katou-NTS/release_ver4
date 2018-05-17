package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AggregateAffiliationInfo;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.JobTitleId;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.primitive.WorkplaceCode;

@Data
/** 集計所属情報 */
@NoArgsConstructor
@AllArgsConstructor
public class AggregateAffiliationInfoDto {

	/** 分類コード: 分類コード */
	@AttendanceItemValue
	@AttendanceItemLayout(jpPropertyName = "分類コード", layout = "A")
	private String classificationCode;

	/** 勤務種別コード: 勤務種別コード */
	@AttendanceItemValue
	@AttendanceItemLayout(jpPropertyName = "勤務種別コード", layout = "B")
	private String businessTypeCode;

	/** 職位ID: 職位ID */
	@AttendanceItemValue
	@AttendanceItemLayout(jpPropertyName = "職位ID", layout = "C")
	private String jobTitle;

	/** 職場ID: 職場ID (work place ID) */
	@AttendanceItemValue
	@AttendanceItemLayout(jpPropertyName = "職場ID", layout = "D")
	private String workPlaceCode;

	/** 雇用コード: 雇用コード */
	@AttendanceItemValue
	@AttendanceItemLayout(jpPropertyName = "雇用コード", layout = "E")
	private String employmentCode;

	public static AggregateAffiliationInfoDto from(AggregateAffiliationInfo domain) {
		AggregateAffiliationInfoDto dto = new AggregateAffiliationInfoDto();
		if (domain != null) {
			dto.setClassificationCode(domain.getClassCd() == null ? null : domain.getClassCd().v());
			dto.setEmploymentCode(domain.getEmploymentCd() == null ? null : domain.getEmploymentCd().v());
			dto.setJobTitle(domain.getJobTitleId() == null ? null : domain.getJobTitleId().v());
			dto.setWorkPlaceCode(domain.getWorkplaceId() == null ? null : domain.getWorkplaceId().v());
			dto.setBusinessTypeCode(domain.getBusinessTypeCd() == null ? null : domain.getBusinessTypeCd().v());
		}
		return dto;
	}

	public AggregateAffiliationInfo toDomain() {
		return AggregateAffiliationInfo.of(employmentCode == null ? null : new EmploymentCode(employmentCode),
				workPlaceCode == null ? null : new WorkplaceId(workPlaceCode),
				jobTitle == null ? null : new JobTitleId(jobTitle),
				classificationCode == null ? null : new ClassificationCode(classificationCode),
				businessTypeCode == null ? null : new BusinessTypeCode(businessTypeCode));
	}
}
