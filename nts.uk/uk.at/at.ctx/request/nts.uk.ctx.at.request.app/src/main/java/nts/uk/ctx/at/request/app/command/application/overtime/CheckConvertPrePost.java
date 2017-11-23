package nts.uk.ctx.at.request.app.command.application.overtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.app.find.application.overtime.dto.OverTimeDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeInputDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.PreAppOvertimeDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.service.IOvertimePreProcess;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.shared.dom.employmentrule.hourlate.overtime.overtimeframe.OvertimeFrame;
import nts.uk.ctx.at.shared.dom.employmentrule.hourlate.overtime.overtimeframe.OvertimeFrameRepository;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckConvertPrePost {
	final String DATE_FORMAT = "yyyy/MM/dd";
	@Inject
	private OvertimeRestAppCommonSetRepository overtimeRestAppCommonSetRepository;
	@Inject
	private IOvertimePreProcess iOvertimePreProcess;
	@Inject
	private WorkTimeRepository workTimeRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OvertimeFrameRepository overtimeFrameRepository;
	
	public OverTimeDto convertPrePost(int prePostAtr,String appDate){
		
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		PreAppOvertimeDto preAppOvertimeDto = new PreAppOvertimeDto();
		OverTimeDto result = new OverTimeDto();
		Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet = this.overtimeRestAppCommonSetRepository.getOvertimeRestAppCommonSetting(companyID, ApplicationType.OVER_TIME_APPLICATION.value);
		if(prePostAtr == 1){
			if(overtimeRestAppCommonSet.isPresent()){
				if(overtimeRestAppCommonSet.get().getPerformanceDisplayAtr() == UseAtr.USE){
					result.setReferencePanelFlg(true);
					//to do....
				}
				if(overtimeRestAppCommonSet.get().getPreDisplayAtr() == UseAtr.USE ){
					AppOverTime appOverTime = iOvertimePreProcess.getPreApplication(employeeID, overtimeRestAppCommonSet, appDate, prePostAtr);
					if(appOverTime != null){
						convertOverTimeDto(companyID,preAppOvertimeDto,result,appOverTime);
						result.setPreAppPanelFlg(true);
					}else{
						result.setPreAppPanelFlg(false);
					}
				}
			}
		}else if(prePostAtr ==0){
			if(overtimeRestAppCommonSet.isPresent()){
				if(overtimeRestAppCommonSet.get().getPerformanceDisplayAtr() == UseAtr.USE){
					result.setReferencePanelFlg(false);
					//to do....
				}
				if(overtimeRestAppCommonSet.get().getPreDisplayAtr() == UseAtr.USE ){
					result.setPreAppPanelFlg(false);
					//to do....
				}
			}
		}
		return result;
	}
	private void convertOverTimeDto(String companyID,PreAppOvertimeDto preAppOvertimeDto, OverTimeDto result,AppOverTime appOvertime){
		if(appOvertime.getApplication() != null){
			if(appOvertime.getApplication().getApplicationDate() != null){
				preAppOvertimeDto.setAppDatePre(appOvertime.getApplication().getApplicationDate().toString(DATE_FORMAT));
			}
		}
		
		if (appOvertime.getWorkTypeCode() != null) {
			WorkTypeOvertime workTypeOvertime = new WorkTypeOvertime();
			workTypeOvertime.setWorkTypeCode(appOvertime.getWorkTypeCode().toString());
			Optional<WorkType> workType = workTypeRepository.findByPK(companyID,
					appOvertime.getWorkTypeCode().toString());
			if (workType.isPresent()) {
				workTypeOvertime.setWorkTypeName(workType.get().getName().toString());
			}
			preAppOvertimeDto.setWorkTypePre(workTypeOvertime);
		}
		if (appOvertime.getSiftCode() != null) {
			SiftType siftType = new SiftType();

			siftType.setSiftCode(appOvertime.getSiftCode().toString());
			Optional<WorkTime> workTime = workTimeRepository.findByCode(companyID,
					appOvertime.getSiftCode().toString());
			if (workTime.isPresent()) {
				siftType.setSiftName(workTime.get().getWorkTimeDisplayName().toString());
			}
			preAppOvertimeDto.setSiftTypePre(siftType);
		}
		preAppOvertimeDto.setWorkClockFrom1Pre(appOvertime.getWorkClockFrom1());
		preAppOvertimeDto.setWorkClockTo1Pre(appOvertime.getWorkClockTo1());
		preAppOvertimeDto.setWorkClockFrom2Pre(appOvertime.getWorkClockFrom2());
		preAppOvertimeDto.setWorkClockTo2Pre(appOvertime.getWorkClockTo2());
		
		List<OvertimeInputDto> overtimeInputDtos = new ArrayList<>();
		List<OverTimeInput> overtimeInputs = appOvertime.getOverTimeInput();
		if (overtimeInputs != null && !overtimeInputs.isEmpty()) {
			List<Integer> frameNo = new ArrayList<>();
			for (OverTimeInput overTimeInput : overtimeInputs) {
				OvertimeInputDto overtimeInputDto = new OvertimeInputDto();
				overtimeInputDto.setAttendanceID(overTimeInput.getAttendanceID().value);
				overtimeInputDto.setFrameNo(overTimeInput.getFrameNo());
				overtimeInputDto.setStartTime(overTimeInput.getStartTime().v());
				overtimeInputDto.setEndTime(overTimeInput.getEndTime().v());
				overtimeInputDto.setApplicationTime(overTimeInput.getApplicationTime().v());
				overtimeInputDtos.add(overtimeInputDto);
				frameNo.add(overTimeInput.getFrameNo());
			}
			List<OvertimeFrame> overtimeFrames = this.overtimeFrameRepository.getOvertimeFrameByFrameNo(frameNo);
			for (OvertimeInputDto overtimeInputDto : overtimeInputDtos) {
				for (OvertimeFrame overtimeFrame : overtimeFrames) {
					if (overtimeInputDto.getFrameNo() == overtimeFrame.getOtFrameNo()) {
						overtimeInputDto.setFrameName(overtimeFrame.getOvertimeFrameName().toString());
						continue;
					}
				}
			}
			preAppOvertimeDto.setOverTimeInputsPre(overtimeInputDtos);
			preAppOvertimeDto.setOverTimeShiftNightPre(appOvertime.getOverTimeShiftNight());
			preAppOvertimeDto.setFlexExessTimePre(appOvertime.getFlexExessTime());
			result.setPreAppOvertimeDto(preAppOvertimeDto);
		}

	}
	
}
