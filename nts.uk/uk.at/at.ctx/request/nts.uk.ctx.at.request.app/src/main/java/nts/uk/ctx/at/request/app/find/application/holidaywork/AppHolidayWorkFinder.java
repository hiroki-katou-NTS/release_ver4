package nts.uk.ctx.at.request.app.find.application.holidaywork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AppHdWorkDispInfoDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.AppHolidayWorkParamPC;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.CheckBeforeOutputDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.CheckBeforeOutputMultiDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.HdWorkDetailOutputDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.HolidayWorkCalculationResultDto;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCalculationHolidayWork;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCheckBeforeRegister;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCheckBeforeRegisterMulti;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamCheckBeforeUpdate;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamDeleteHdChange;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamHdWorkDetail;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamHolidayWorkChangeDate;
import nts.uk.ctx.at.request.app.find.application.holidaywork.dto.ParamHolidayWorkChangeWork;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.service.other.PreAppContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementDetail;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ActualContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.commonalgorithm.ICommonAlgorithmHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayService;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.AppHdWorkDispInfoOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CalculatedFlag;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CheckBeforeOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.CheckBeforeOutputMulti;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HdSelectWorkDispInfoOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HdWorkDetailOutput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.HolidayWorkCalculationResult;
import nts.uk.ctx.at.request.dom.application.overtime.CommonAlgorithm.ICommonAlgorithmOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkContent;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.PrePostInitAtr;
import nts.uk.ctx.at.request.dom.workrecord.dailyrecordprocess.dailycreationwork.BreakTimeZoneSetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * Refactor5
 * @author huylq
 *
 */
@Stateless
public class AppHolidayWorkFinder {

	public static final String PATTERN_DATE = "yyyy/MM/dd";

	@Inject
	private HolidayService holidayWorkService;
	
	@Inject
	private ICommonAlgorithmHolidayWork commonHolidayWorkAlgorithm;
	
	@Inject
	private ICommonAlgorithmOverTime commonOverTimeAlgorithm;

	public AppHdWorkDispInfoDto getStartNew(AppHolidayWorkParamPC param) {
		String companyId = AppContexts.user().companyId();

		Optional<List<String>> empList = Optional.empty();
//		if(param.getEmpList() != null && !param.getEmpList().isEmpty()) {
//			empList = Optional.of(param.getEmpList());
//		}
		if (param.getAppDispInfoStartupOutput() != null
				&& param.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput() != null) {
			List<String> empListParam = new ArrayList<String>();
			empListParam.add(param.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput().getEmployeeInfoLst()
					.get(0).getSid());
			empList = Optional.of(empListParam);
		}

		Optional<List<GeneralDate>> dateListOptional = Optional.empty();
		if (param.getDateList() != null && !param.getDateList().isEmpty()) {
			List<GeneralDate> dateList = new ArrayList<GeneralDate>();
			param.getDateList().stream().filter(date -> StringUtils.isNotBlank(date))
					.map(date -> dateList.add(GeneralDate.fromString(date, PATTERN_DATE)));
			if (!dateList.isEmpty()) {
				dateListOptional = Optional.of(dateList);
			}
		}

		// 1.休出申請（新規）起動処理
		AppDispInfoStartupOutput appDispInfoStartupOutput = param.getAppDispInfoStartupOutput().toDomain();
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = holidayWorkService.startA(companyId, empList,
				dateListOptional, appDispInfoStartupOutput);

		// 	計算（従業員）
		List<PreAppContentDisplay> opPreAppContentDisplayLst = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput()
				.getAppDispInfoWithDateOutput().getOpPreAppContentDisplayLst().orElse(new ArrayList<>());
		Optional<AppHolidayWork> appHolidayWork = !opPreAppContentDisplayLst.isEmpty()
				? opPreAppContentDisplayLst.get(0).getAppHolidayWork()
				: Optional.empty();

		WorkContent workContent = commonHolidayWorkAlgorithm.getWorkContent(appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput());

		HolidayWorkCalculationResult calculationResult = holidayWorkService.calculate(companyId,
				appDispInfoStartupOutput.getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getSid(),
				!param.getDateList().isEmpty()
						? Optional.of(GeneralDate.fromString(param.getDateList().get(0), PATTERN_DATE))
						: Optional.empty(),
				appDispInfoStartupOutput.getAppDispInfoWithDateOutput().getPrePostAtr(),
				appHdWorkDispInfoOutput.getHolidayWorkAppSet().getOvertimeLeaveAppCommonSet(),
				appHolidayWork.isPresent() ? appHolidayWork.get().getApplicationTime() : null,
				appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().getActualApplicationTime().orElse(null),
				workContent);
		appHdWorkDispInfoOutput.setCalculationResult(Optional.ofNullable(calculationResult));

		return AppHdWorkDispInfoDto.fromDomain(appHdWorkDispInfoOutput);
	}

	public HolidayWorkCalculationResultDto calculate(ParamCalculationHolidayWork param) {
		Optional<GeneralDate> dateOp = Optional.empty();
		if (StringUtils.isNotBlank(param.getDate())) {
			dateOp = Optional.of(GeneralDate.fromString(param.getDate(), PATTERN_DATE));	
		}
		
		HolidayWorkCalculationResult calculationResult = holidayWorkService.calculate(param.getCompanyId(),
				param.getEmployeeId(),
				dateOp,
				EnumAdaptor.valueOf(param.getPrePostAtr(), PrePostInitAtr.class),
				param.getOvertimeLeaveAppCommonSet() != null ? param.getOvertimeLeaveAppCommonSet().toDomain() : null,
				param.getPreApplicationTime() != null ? param.getPreApplicationTime().toDomain() : null,
				param.getActualApplicationTime() != null ? param.getActualApplicationTime().toDomain() : null,
				param.getWorkContent() != null ? param.getWorkContent().toDomain() : null);

		return HolidayWorkCalculationResultDto.fromDomain(calculationResult);
	}
	
	public AppHdWorkDispInfoDto changeAppDate(ParamHolidayWorkChangeDate param) {
		List<GeneralDate> dateList = new ArrayList<GeneralDate>();
		if(!param.getDateList().isEmpty()) {
			dateList = param.getDateList().stream().map(date -> GeneralDate.fromString(date, PATTERN_DATE)).collect(Collectors.toList());
		}
		
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = holidayWorkService.changeAppDate(param.getCompanyId(), 
				dateList, EnumAdaptor.valueOf(param.getApplicationType(), ApplicationType.class),
				param.getAppHdWorkDispInfoDto().toDomain());
		
		return AppHdWorkDispInfoDto.fromDomain(appHdWorkDispInfoOutput);
	}
	
	public AppHdWorkDispInfoDto changeWorkHours(ParamHolidayWorkChangeWork param) {
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = param.getAppHdWorkDispInfoDto().toDomain();
		
		List<ActualContentDisplay> opActualContentDisplayLst = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput()
				.getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().orElse(Collections.emptyList());
		Optional<AchievementDetail> achievementDetail = !opActualContentDisplayLst.isEmpty() ? 
				opActualContentDisplayLst.get(0).getOpAchievementDetail() : Optional.empty();
		
		BreakTimeZoneSetting breakTimeZoneSettingList = commonOverTimeAlgorithm.selectWorkTypeAndTime(param.getCompanyId(), new WorkTypeCode(param.getWorkTimeCode()), 
				new WorkTimeCode(param.getWorkTimeCode()), Optional.of(new TimeWithDayAttr(param.getStartTime())), 
				Optional.of(new TimeWithDayAttr(param.getEndTime())), achievementDetail);
		appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().setBreakTimeZoneSettingList(Optional.ofNullable(breakTimeZoneSettingList));
		
		if(appHdWorkDispInfoOutput.getCalculationResult().isPresent()) {
			appHdWorkDispInfoOutput.getCalculationResult().get().setCalculatedFlag(CalculatedFlag.UNCALCULATED);
		}
		
		return AppHdWorkDispInfoDto.fromDomain(appHdWorkDispInfoOutput);
	}
	
	public AppHdWorkDispInfoDto selectWork(ParamHolidayWorkChangeWork param) {
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = param.getAppHdWorkDispInfoDto().toDomain();
		
		//	勤務種類・就業時間帯選択時に表示するデータを取得する
		HdSelectWorkDispInfoOutput hdSelectWorkDispInfoOutput = holidayWorkService.selectWork(param.getCompanyId(), param.getDateList().isEmpty() ? GeneralDate.fromString(param.getDateList().get(0), PATTERN_DATE) : null, 
				new WorkTypeCode(param.getWorkTypeCode()), new WorkTimeCode(param.getWorkTimeCode()), 
				appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput().getOpActualContentDisplayLst().orElse(Collections.emptyList()), 
				appHdWorkDispInfoOutput.getAppDispInfoStartupOutput(), appHdWorkDispInfoOutput.getHolidayWorkAppSet());
		
		appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().setWorkHours(hdSelectWorkDispInfoOutput.getWorkHours());
		appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().setActualApplicationTime(Optional.ofNullable(hdSelectWorkDispInfoOutput.getActualApplicationTime()));
		appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().setBreakTimeZoneSettingList(hdSelectWorkDispInfoOutput.getBreakTimeZoneSettingList());
		
		//	計算（従業員）
		WorkContent workContent = commonHolidayWorkAlgorithm.getWorkContent(appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput());
		List<PreAppContentDisplay> preAppContentDisplayList = appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput()
				.getOpPreAppContentDisplayLst().orElse(Collections.emptyList());
		Optional<AppHolidayWork> appHolidayWork = !preAppContentDisplayList.isEmpty() ? preAppContentDisplayList.get(0).getAppHolidayWork() : Optional.empty();
		
		HolidayWorkCalculationResult calculationResult = holidayWorkService.calculate(param.getCompanyId(), 
				appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getSid(), 
				Optional.ofNullable(param.getDateList().isEmpty() ? GeneralDate.fromString(param.getDateList().get(0), PATTERN_DATE) : null), 
				appHdWorkDispInfoOutput.getAppDispInfoStartupOutput().getAppDispInfoWithDateOutput().getPrePostAtr(), 
				appHdWorkDispInfoOutput.getHolidayWorkAppSet().getOvertimeLeaveAppCommonSet(), 
				appHolidayWork.isPresent() ? appHolidayWork.get().getApplicationTime() : null, 
				appHdWorkDispInfoOutput.getHdWorkDispInfoWithDateOutput().getActualApplicationTime().orElse(null), 
				workContent);
		appHdWorkDispInfoOutput.setCalculationResult(Optional.ofNullable(calculationResult));
		
		return AppHdWorkDispInfoDto.fromDomain(appHdWorkDispInfoOutput);
	}

	public CheckBeforeOutputDto checkBeforeRegister(ParamCheckBeforeRegister param) {
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = param.getAppHdWorkDispInfo().toDomain();
		Application application = param.getAppHolidayWork().getApplication().toDomain();
		AppHolidayWork appHolidayWork = param.getAppHolidayWork().toDomain();
		if (appHolidayWork.getAppOvertimeDetail().isPresent()) {
			appHolidayWork.getAppOvertimeDetail().get().setAppId(application.getAppID());
		}
		appHolidayWork.setApplication(application);
		CheckBeforeOutput checkBeforeOutput = holidayWorkService.checkBeforeRegister(param.isRequire(), param.getCompanyId(), appHdWorkDispInfoOutput, 
				appHolidayWork, param.isProxy());
		
		return CheckBeforeOutputDto.fromDomain(checkBeforeOutput);
	}
	
	public CheckBeforeOutputMultiDto checkBeforeRegisterMulti(ParamCheckBeforeRegisterMulti param) {
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = param.getAppHdWorkDispInfo().toDomain();
		Application application = param.getAppHolidayWork().getApplication().toDomain();
		AppHolidayWork appHolidayWork = param.getAppHolidayWork().toDomain();
		if (appHolidayWork.getAppOvertimeDetail().isPresent()) {
			appHolidayWork.getAppOvertimeDetail().get().setAppId(application.getAppID());
		}
		appHolidayWork.setApplication(application);
		
		CheckBeforeOutputMulti checkBeforeOutputMulti = holidayWorkService.checkBeforeRegisterMulti(param.isRequire(), param.getCompanyId(), 
				param.getEmpList(), appHdWorkDispInfoOutput, appHolidayWork);
		
		return CheckBeforeOutputMultiDto.fromDomain(checkBeforeOutputMulti);
	}
	
	public HdWorkDetailOutputDto getDetail(ParamHdWorkDetail param) {
		HdWorkDetailOutput hdWorkDetailOutput = holidayWorkService.getDetail(param.getCompanyId(), param.getApplicationId(), 
				param.getAppDispInfoStartup().toDomain());
		return HdWorkDetailOutputDto.fromDomain(hdWorkDetailOutput);
	}
	
//	public Application createApplication(ApplicationDto application) {
//		
//		return Application.createFromNew(
//				EnumAdaptor.valueOf(application.getPrePostAtr(), PrePostAtr.class),
//				application.getEmployeeID(),
//				ApplicationType.OVER_TIME_APPLICATION,
//				new ApplicationDate(GeneralDate.fromString(application.getAppDate(), PATTERN_DATE)),
//				application.getEnteredPerson(),
//				application.getOpStampRequestMode() == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(application.getOpStampRequestMode(), StampRequestMode.class)),
//				application.getOpReversionReason() == null ? Optional.empty() : Optional.of(new ReasonForReversion(application.getOpReversionReason())),
//				application.getOpAppStartDate() == null ? Optional.empty() : Optional.of(new ApplicationDate(GeneralDate.fromString(application.getOpAppStartDate(), PATTERN_DATE))),
//				application.getOpAppEndDate() == null ? Optional.empty() : Optional.of(new ApplicationDate(GeneralDate.fromString(application.getOpAppEndDate(), PATTERN_DATE))),
//				application.getOpAppReason() == null ? Optional.empty() : Optional.of(new AppReason(application.getOpAppReason())),
//				application.getOpAppStandardReasonCD() == null ? Optional.empty() : Optional.of(new AppStandardReasonCode(application.getOpAppStandardReasonCD())));
//	}

	public CheckBeforeOutputDto checkBeforeUpdate(ParamCheckBeforeUpdate param) {
		AppHdWorkDispInfoOutput appHdWorkDispInfoOutput = param.getAppHdWorkDispInfo().toDomain();
		Application application = param.getAppHolidayWork().getApplication().toDomain(param.getAppHdWorkDispInfo().getAppDispInfoStartupOutput().getAppDetailScreenInfo().getApplication());
		AppHolidayWork appHolidayWork = param.getAppHolidayWork().toDomain();
		if (appHolidayWork.getAppOvertimeDetail().isPresent()) {
			appHolidayWork.getAppOvertimeDetail().get().setAppId(application.getAppID());
		}
		appHolidayWork.setApplication(application);
		
		CheckBeforeOutput checkBeforeOutput = 
				holidayWorkService.checkBeforeUpdate(param.isRequire(), param.getCompanyId(), appHdWorkDispInfoOutput, appHolidayWork, param.isProxy());
		return CheckBeforeOutputDto.fromDomain(checkBeforeOutput);
	}

	public void deleteHdChange(ParamDeleteHdChange param) {
		holidayWorkService.deleteHdChange(param.getApplicationId());
	}
}
