package nts.uk.ctx.at.request.app.find.application.common;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.DisabledSegment_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.ReflectionInformation_New;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDto_New {
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
	private static final String DATE_TIME_FORMAT_045 = "yyyy/MM/dd HH:mm:ss";
	private Long version;
	
	// 会社ID
	private String companyID;
	
	// 申請ID
	private String applicationID;
	
	// 事前事後区分
	private Integer prePostAtr; 

	// 入力日
	private String inputDate; 
	
	// 入力者
	private String enteredPersonSID;
	
	// 差戻し理由
	private String reversionReason; 
	
	// 申請日
	private String applicationDate; 
	
	// 申請理由
	private String applicationReason;
	
	// 申請種類
	private Integer applicationType;
	
	// 申請者
	private String  applicantSID;
	
	// 予定反映不可理由
	private Integer reflectPlanScheReason;
    
    // 予定反映日時
	private String reflectPlanTime;
	
	// 予定反映状態
	private Integer reflectPlanState;
	
	// 予定強制反映
	private Integer reflectPlanEnforce;

	// 実績反映不可理由
	private Integer  reflectPerScheReason;
	
	// 実績反映日時
	private String reflectPerTime;
	
	// 予定反映状態=comment line71???
	private Integer reflectPerState;
	
	// 実績強制反映
	private Integer reflectPerEnforce;
	
	private String startDate;
	
	private String endDate;
	
	public static ApplicationDto_New fromDomain(Application_New domain) {
		return new ApplicationDto_New(
				domain.getVersion(),
				domain.getCompanyID(),
				domain.getAppID(),
				domain.getPrePostAtr().value,
				domain.getInputDate() == null ? null :domain.getInputDate().toString(DATE_TIME_FORMAT), 
				domain.getEnteredPersonID(), 
				domain.getReversionReason().v(), 
				domain.getAppDate() == null ? null :domain.getAppDate().toString(DATE_FORMAT), 
				domain.getAppReason().v(),
				domain.getAppType().value, 
				domain.getEmployeeID(), 
				domain.getReflectionInformation().getNotReason().isPresent() ? domain.getReflectionInformation().getNotReason().get().value : null, 
				domain.getReflectionInformation().getDateTimeReflection().isPresent() ? domain.getReflectionInformation().getDateTimeReflection().get().toString(DATE_FORMAT) : null, 
				domain.getReflectionInformation().getStateReflection().value, 
				domain.getReflectionInformation().getForcedReflection().value, 
				domain.getReflectionInformation().getNotReasonReal().isPresent() ? domain.getReflectionInformation().getNotReasonReal().get().value : null, 
				domain.getReflectionInformation().getDateTimeReflectionReal().isPresent() ? domain.getReflectionInformation().getDateTimeReflectionReal().get().toString(DATE_FORMAT) : null, 
				domain.getReflectionInformation().getStateReflectionReal().value, 
				domain.getReflectionInformation().getForcedReflectionReal().value,
				domain.getStartDate().isPresent() ? domain.getStartDate().get().toString(DATE_FORMAT) : null,
				domain.getEndDate().isPresent() ? domain.getEndDate().get().toString(DATE_FORMAT) : null
				);
	}
	public static Application_New toEntity(ApplicationDto_New appDto) {
		Application_New app = new Application_New(
				appDto.getVersion(), 
				appDto.getCompanyID(), 
				appDto.getApplicationID(),
				EnumAdaptor.valueOf(appDto.getPrePostAtr(), PrePostAtr.class), 
				GeneralDateTime.fromString(appDto.getInputDate(), DATE_FORMAT), 
				appDto.getEnteredPersonSID(), 
				new AppReason(appDto.getReversionReason()), 
				GeneralDate.fromString(appDto.getApplicationDate(), DATE_FORMAT),
				new AppReason(appDto.getApplicationReason()),
				EnumAdaptor.valueOf(appDto.getApplicationType(), ApplicationType.class), 
				appDto.getApplicantSID(),
				Optional.ofNullable((GeneralDate.fromString(appDto.getStartDate(), DATE_FORMAT))),
				Optional.ofNullable((GeneralDate.fromString(appDto.getEndDate(), DATE_FORMAT))), 
				ReflectionInformation_New.builder()
						.stateReflection(
								EnumAdaptor.valueOf(appDto.getReflectPlanState(), ReflectedState_New.class))
						.stateReflectionReal(
								EnumAdaptor.valueOf(appDto.getReflectPerState(), ReflectedState_New.class))
						.forcedReflection(
								EnumAdaptor.valueOf(appDto.getReflectPlanEnforce(), DisabledSegment_New.class))
						.forcedReflectionReal(
								EnumAdaptor.valueOf(appDto.getReflectPerEnforce(), DisabledSegment_New.class))
						.notReason(Optional.ofNullable(appDto.getReflectPlanScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflect_New.class)))
						.notReasonReal(Optional.ofNullable(appDto.getReflectPerScheReason())
								.map(x -> EnumAdaptor.valueOf(x, ReasonNotReflectDaily_New.class)))
						.dateTimeReflection(Optional
								.ofNullable(GeneralDateTime.fromString(appDto.getReflectPlanTime(), DATE_FORMAT)))
						.dateTimeReflectionReal(Optional
								.ofNullable(GeneralDateTime.fromString(appDto.getReflectPerTime(), DATE_FORMAT)))
						.build());
		return app;
	}
	public static ApplicationDto_New fromDomainCMM045(Application_New domain) {
		return new ApplicationDto_New(
				domain.getVersion(),
				domain.getCompanyID(),
				domain.getAppID(),
				domain.getPrePostAtr().value,
				domain.getInputDate() == null ? null :domain.getInputDate().toString(DATE_TIME_FORMAT_045), 
				domain.getEnteredPersonID(), 
				domain.getReversionReason().v(), 
				domain.getAppDate() == null ? null :domain.getAppDate().toString(DATE_FORMAT), 
				domain.getAppReason().v(),
				domain.getAppType().value, 
				domain.getEmployeeID(), 
				domain.getReflectionInformation().getNotReason().isPresent() ? domain.getReflectionInformation().getNotReason().get().value : null, 
				domain.getReflectionInformation().getDateTimeReflection().isPresent() ? domain.getReflectionInformation().getDateTimeReflection().get().toString(DATE_FORMAT) : null, 
				domain.getReflectionInformation().getStateReflection().value, 
				domain.getReflectionInformation().getForcedReflection().value, 
				domain.getReflectionInformation().getNotReasonReal().isPresent() ? domain.getReflectionInformation().getNotReasonReal().get().value : null, 
				domain.getReflectionInformation().getDateTimeReflectionReal().isPresent() ? domain.getReflectionInformation().getDateTimeReflectionReal().get().toString(DATE_FORMAT) : null, 
				domain.getReflectionInformation().getStateReflectionReal().value, 
				domain.getReflectionInformation().getForcedReflectionReal().value,
				domain.getStartDate().isPresent() ? domain.getStartDate().get().toString(DATE_FORMAT) : null,
				domain.getEndDate().isPresent() ? domain.getEndDate().get().toString(DATE_FORMAT) : null
		);
	}
	public static ApplicationDto_New convertInputDate(ApplicationDto_New dto) {
		dto.inputDate = GeneralDateTime.fromString(dto.inputDate, DATE_TIME_FORMAT_045).toString(DATE_TIME_FORMAT);
		return dto;
	}
}
