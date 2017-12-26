package nts.uk.ctx.at.request.app.find.application.common;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationMetaDto;
import nts.uk.ctx.at.request.app.find.application.requestofearch.GetDataAppCfDetailFinder;
import nts.uk.ctx.at.request.app.find.application.requestofearch.OutputMessageDeadline;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.CollectApprovalRootPatternService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.StartupErrorCheckService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.output.ApprovalRootPattern;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AppDataDateFinder {
	
	@Inject
	private GetDataAppCfDetailFinder getDataAppCfDetailFinder;
	
	@Inject
	private CollectApprovalRootPatternService approvalRootPatternService;
	
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	
	@Inject
	private StartupErrorCheckService startupErrorCheckService;
	
	private final String DATE_FORMAT = "yyyy/MM/dd";
	
	public AppDateData getAppDataByDate(Integer appTypeValue, String appDate, Boolean isStartUp){
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		GeneralDate appGeneralDate = GeneralDate.fromString(appDate, DATE_FORMAT);
		OutputMessageDeadline outputMessageDeadline = getDataAppCfDetailFinder.getDataConfigDetail(new ApplicationMetaDto("", appTypeValue, appGeneralDate));
		ApprovalRootPattern approvalRootPattern = approvalRootPatternService.getApprovalRootPatternService(
				companyID, 
				employeeID, 
				EmploymentRootAtr.APPLICATION, 
				EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
				appGeneralDate);
		if(isStartUp.equals(Boolean.TRUE)){
			startupErrorCheckService.startupErrorCheck(appGeneralDate, approvalRootPattern.getApprovalRootContentImport());
		}
		RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(employeeID, appGeneralDate);
		return new AppDateData(
				outputMessageDeadline, 
				approvalRootPattern.getApprovalRootContentImport().getApprovalRootState().getListApprovalPhaseState()
					.stream().map(x -> ApprovalPhaseStateDto.fromApprovalPhaseStateImport(x)).collect(Collectors.toList()), 
				new RecordWorkDto_New(
						recordWorkInfoImport.getWorkTypeCode(),
						recordWorkInfoImport.getWorkTimeCode(), 
						recordWorkInfoImport.getAttendanceStampTimeFirst(), 
						recordWorkInfoImport.getLeaveStampTimeFirst(), 
						recordWorkInfoImport.getAttendanceStampTimeSecond(), 
						recordWorkInfoImport.getLeaveStampTimeSecond()),
				approvalRootPattern.getApprovalRootContentImport().getErrorFlag().value);
	}
	
}
@Value
@AllArgsConstructor
class ApprovalPhaseStateDto{
	private Integer phaseOrder;
	
	private String approvalAtr;
	
	private List<ApprovalFrameDto> listApprovalFrame;
	
	public static ApprovalPhaseStateDto fromApprovalPhaseStateImport(ApprovalPhaseStateImport_New approvalPhaseStateImport){
		return new ApprovalPhaseStateDto(
				approvalPhaseStateImport.getPhaseOrder(), 
				approvalPhaseStateImport.getApprovalAtr(), 
				approvalPhaseStateImport.getListApprovalFrame().stream().map(x -> ApprovalFrameDto.fromApprovalFrameImport(x)).collect(Collectors.toList()));
	}
}

@Value
@AllArgsConstructor
class ApprovalFrameDto {
	private Integer phaseOrder;
	
	private Integer frameOrder;
	
	private String approvalAtr;
	
	private List<ApproverStateDto> listApprover;
	
	private String approverID;
	
	private String representerID;
	
	private String approvalReason;
	
	public static ApprovalFrameDto fromApprovalFrameImport(ApprovalFrameImport_New approvalFrameImport){
		return new ApprovalFrameDto(
				approvalFrameImport.getPhaseOrder(), 
				approvalFrameImport.getFrameOrder(), 
				approvalFrameImport.getApprovalAtr(), 
				approvalFrameImport.getListApprover().stream().map(x -> new ApproverStateDto(x.getApprover(), x.getRepresenter())).collect(Collectors.toList()), 
				approvalFrameImport.getApproverID(), 
				approvalFrameImport.getRepresenterID(), 
				approvalFrameImport.getApprovalReason());
	}
}

@Value
@AllArgsConstructor
class ApproverStateDto {
	private String approver;
	
	private String representer;
}

@Value
@AllArgsConstructor
class RecordWorkDto_New {
	// 勤務種類コード
	private String workTypeCode;
	
	// 就業時間帯コード
	private String workTimeCode;
	
	// 開始時刻1
	private int attendanceStampTimeFirst;
	
	// 終了時刻1
	private int leaveStampTimeFirst;
	
	// 開始時刻2
	private int attendanceStampTimeSecond;
	
	// 終了時刻2
	private int leaveStampTimeSecond;
}
