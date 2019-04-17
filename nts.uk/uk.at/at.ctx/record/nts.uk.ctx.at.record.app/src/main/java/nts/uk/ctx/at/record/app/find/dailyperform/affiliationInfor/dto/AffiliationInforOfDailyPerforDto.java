package nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_AFFILIATION_INFO_NAME)
public class AffiliationInforOfDailyPerforDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate baseDate; 
	
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = EMPLOYEMENT)
	@AttendanceItemValue
	private String employmentCode;

	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = JOB_TITLE)
	@AttendanceItemValue
	private String jobId;

	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = WORKPLACE)
	@AttendanceItemValue
	private String workplaceID;

	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = CLASSIFICATION)
	@AttendanceItemValue
	private String classificationCode;

//	@AttendanceItemLayout(layout = "E", jpPropertyName = "加給コード")
//	@AttendanceItemValue
	private String subscriptionCode;
	
	public static AffiliationInforOfDailyPerforDto getDto(AffiliationInforOfDailyPerfor domain){
		AffiliationInforOfDailyPerforDto dto = new AffiliationInforOfDailyPerforDto();
		if(domain != null){
			dto.setClassificationCode(domain.getClsCode() == null ? null : domain.getClsCode().v());
			dto.setEmploymentCode(domain.getEmploymentCode() == null ? null : domain.getEmploymentCode().v());
			dto.setJobId(domain.getJobTitleID());
			dto.setSubscriptionCode(domain.getBonusPaySettingCode() == null ? null 
					: domain.getBonusPaySettingCode().v());
			dto.setWorkplaceID(domain.getWplID());
			dto.setBaseDate(domain.getYmd());
			dto.setEmployeeId(domain.getEmployeeId());
			dto.exsistData();
		}
		return dto;
	}
	
	@Override
	public AffiliationInforOfDailyPerforDto clone(){
		AffiliationInforOfDailyPerforDto dto = new AffiliationInforOfDailyPerforDto();
		dto.setClassificationCode(classificationCode);
		dto.setEmploymentCode(employmentCode);
		dto.setJobId(jobId);
		dto.setSubscriptionCode(subscriptionCode);
		dto.setWorkplaceID(workplaceID);
		dto.setBaseDate(workingDate());
		dto.setEmployeeId(employeeId());
		if(this.isHaveData()){
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.baseDate;
	}

	@Override
	public AffiliationInforOfDailyPerfor toDomain(String employeeId, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new AffiliationInforOfDailyPerfor(new EmploymentCode(this.employmentCode), 
												employeeId, this.jobId, this.workplaceID, date,
												new ClassificationCode(this.classificationCode),
												new BonusPaySettingCode(this.subscriptionCode));
	}
}
