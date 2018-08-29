package nts.uk.ctx.workflow.pubimp.resultrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.DailyConfirmAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.service.AppRootInstanceContent;
import nts.uk.ctx.workflow.dom.resultrecord.service.CreateDailyApprover;
import nts.uk.ctx.workflow.dom.service.ApprovalRootStateStatusService;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;
import nts.uk.ctx.workflow.dom.service.resultrecord.AppRootConfirmService;
import nts.uk.ctx.workflow.dom.service.resultrecord.AppRootInstancePeriod;
import nts.uk.ctx.workflow.dom.service.resultrecord.AppRootInstanceService;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApproverEmployee;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApproverToApprove;
import nts.uk.ctx.workflow.pub.resultrecord.ApproveDoneExport;
import nts.uk.ctx.workflow.pub.resultrecord.ApproverApproveExport;
import nts.uk.ctx.workflow.pub.resultrecord.ApproverEmpExport;
import nts.uk.ctx.workflow.pub.resultrecord.EmployeePerformParam;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppFrameInsExport;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppPhaseInsExport;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppRootInsContentExport;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppRootInsExport;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class IntermediateDataPubImpl implements IntermediateDataPub {
	
	@Inject
	private AppRootInstanceService appRootInstanceService;
	
	@Inject
	private ApprovalRootStateStatusService approvalRootStateStatusService;
	
	@Inject
	private AppRootConfirmService appRootConfirmService;
	
	@Inject
	private CreateDailyApprover createDailyApprover;

	@Override
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpPeriod(String employeeID, DatePeriod period,
			Integer rootType) {
		List<String> employeeIDLst = Arrays.asList(employeeID);
		return appRootInstanceService.getAppRootStatusByEmpsPeriod(employeeIDLst, period, EnumAdaptor.valueOf(rootType, RecordRootType.class))
				.stream().map(x -> convertStatusFromDomain(x)).collect(Collectors.toList());
	}

	@Override
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpsDates(List<String> employeeIDLst,
			List<GeneralDate> dateLst, Integer rootType) {
		List<AppRootStateStatusSprExport> result = new ArrayList<>();
		dateLst.forEach(date -> {
			DatePeriod period = new DatePeriod(date, date);
			result.addAll(appRootInstanceService.getAppRootStatusByEmpsPeriod(employeeIDLst, period, EnumAdaptor.valueOf(rootType, RecordRootType.class))
			.stream().map(x -> convertStatusFromDomain(x)).collect(Collectors.toList()));
		});
		return result;
	}

	@Override
	public List<AppRootStateStatusSprExport> getAppRootStatusByEmpsPeriod(List<String> employeeIDLst, DatePeriod period,
			Integer rootType) {
		return appRootInstanceService.getAppRootStatusByEmpsPeriod(employeeIDLst, period, EnumAdaptor.valueOf(rootType, RecordRootType.class))
			.stream().map(x -> convertStatusFromDomain(x)).collect(Collectors.toList());
	}
	
	private AppRootStateStatusSprExport convertStatusFromDomain(ApprovalRootStateStatus approvalRootStateStatus){
		return new AppRootStateStatusSprExport(
				approvalRootStateStatus.getDate(), 
				approvalRootStateStatus.getEmployeeID(), 
				approvalRootStateStatus.getDailyConfirmAtr().value);
	}

	@Override
	public List<ApproveDoneExport> checkDateApprovedStatus(String employeeID, DatePeriod period, Integer rootType) {
		RecordRootType rootTypeEnum = EnumAdaptor.valueOf(rootType, RecordRootType.class);
		List<String> employeeIDLst = Arrays.asList(employeeID);
		String companyID = AppContexts.user().companyId();
		List<ApproveDoneExport> approveDoneExportLst = new ArrayList<>();
		// 対象者と期間から承認ルート中間データを取得する
		List<AppRootInstancePeriod> appRootInstancePeriodLst = appRootInstanceService.getAppRootInstanceByEmpPeriod(employeeIDLst, period, rootTypeEnum);
		// INPUT．対象者社員IDの先頭から最後へループ
		employeeIDLst.forEach(employeeIDLoop -> {
			// INPUT．期間の開始日から終了日へループ
			for(GeneralDate loopDate = period.start(); loopDate.beforeOrEquals(period.end()); loopDate = loopDate.addDays(1)){
				// 対象日の承認ルート中間データを取得する
				AppRootInstance appRootInstance = appRootInstanceService.getAppRootInstanceByDate(loopDate, 
						appRootInstancePeriodLst.stream().filter(x -> x.getEmployeeID().equals(employeeIDLoop)).findAny().get().getAppRootInstanceLst());
				// 対象日の就業実績確認状態を取得する
				AppRootConfirm appRootConfirm = appRootInstanceService.getAppRootConfirmByDate(companyID, employeeIDLoop, loopDate, rootTypeEnum);
				// 中間データから承認ルートインスタンスに変換する
				ApprovalRootState approvalRootState = appRootInstanceService.convertFromAppRootInstance(appRootInstance, appRootConfirm);
				// 承認ルート状況を取得する
				List<ApprovalRootStateStatus> approvalRootStateStatusLst = approvalRootStateStatusService.getApprovalRootStateStatus(Arrays.asList(approvalRootState));
				// 承認状況が承認済ならtrue、それ以外ならfalseをセットする
				for(ApprovalRootStateStatus approvalRootStateStatus : approvalRootStateStatusLst) {
					boolean isApproved = false;
					if(approvalRootStateStatus.getDailyConfirmAtr().equals(DailyConfirmAtr.ALREADY_APPROVED)){
						isApproved = true;
					}
					approveDoneExportLst.add(new ApproveDoneExport(loopDate, isApproved));
				};
			}
		});
		return approveDoneExportLst;
	}

	@Override
	public void approve(String approverID, List<EmployeePerformParam> employeePerformLst, Integer rootType) {
		String companyID = AppContexts.user().companyId();
		RecordRootType rootTypeEnum = EnumAdaptor.valueOf(rootType, RecordRootType.class);
		employeePerformLst.forEach(employee -> {
			String employeeID = employee.getEmployeeID();
			GeneralDate date = employee.getDate();
			// 対象者と期間から承認ルート中間データを取得する
			List<AppRootInstancePeriod> appRootInstancePeriodLst = appRootInstanceService.getAppRootInstanceByEmpPeriod(
					Arrays.asList(employeeID), 
					new DatePeriod(date, date), 
					rootTypeEnum);
			// ループする社員の「承認ルート中間データ」を取得する
			AppRootInstance appRootInstance = appRootInstanceService.getAppRootInstanceByDate(date, 
					appRootInstancePeriodLst.stream().filter(x -> x.getEmployeeID().equals(employeeID)).findAny().get().getAppRootInstanceLst());
			// 対象日の就業実績確認状態を取得する
			AppRootConfirm appRootConfirm = appRootInstanceService.getAppRootConfirmByDate(companyID, employeeID, date, rootTypeEnum);
			// (中間データ版)承認する
			appRootConfirmService.approve(approverID, employeeID, date, appRootInstance, appRootConfirm);
		});
	}

	@Override
	public boolean cancel(String approverID, List<EmployeePerformParam> employeePerformLst, Integer rootType) {
		boolean result = false;
		String companyID = AppContexts.user().companyId();
		RecordRootType rootTypeEnum = EnumAdaptor.valueOf(rootType, RecordRootType.class);
		for(EmployeePerformParam employee : employeePerformLst){
			String employeeID = employee.getEmployeeID();
			GeneralDate date = employee.getDate();
			// 対象者と期間から承認ルート中間データを取得する
			List<AppRootInstancePeriod> appRootInstancePeriodLst = appRootInstanceService.getAppRootInstanceByEmpPeriod(
					Arrays.asList(employeeID), 
					new DatePeriod(date, date), 
					rootTypeEnum);
			// ループする社員の「承認ルート中間データ」を取得する
			AppRootInstance appRootInstance = appRootInstanceService.getAppRootInstanceByDate(date, 
					appRootInstancePeriodLst.stream().filter(x -> x.getEmployeeID().equals(employeeID)).findAny().get().getAppRootInstanceLst());
			// 対象日の就業実績確認状態を取得する
			AppRootConfirm appRootConfirm = appRootInstanceService.getAppRootConfirmByDate(companyID, employeeID, date, rootTypeEnum);
			// (中間データ版)解除する
			result = appRootConfirmService.cleanStatus(approverID, employeeID, date, appRootInstance, appRootConfirm);
			if(!result){
				break;
			}
		};
		return result;
	}

	@Override
	public List<ApproverApproveExport> getApproverByPeriod(List<String> employeeIDLst, DatePeriod period, Integer rootType) {
		RecordRootType rootTypeEnum = EnumAdaptor.valueOf(rootType, RecordRootType.class);
		return appRootInstanceService.getApproverByPeriod(employeeIDLst, period, rootTypeEnum).stream()
			.map(x -> convertApproverApprove(x)).collect(Collectors.toList());
	}

	@Override
	public List<ApproverApproveExport> getApproverByDateLst(List<String> employeeIDLst, List<GeneralDate> dateLst, Integer rootType) {
		RecordRootType rootTypeEnum = EnumAdaptor.valueOf(rootType, RecordRootType.class);
		List<ApproverApproveExport> approverApproveExportLst = new ArrayList<>();
		dateLst.forEach(date -> {
			approverApproveExportLst.addAll(appRootInstanceService.getApproverByPeriod(employeeIDLst, new DatePeriod(date, date), rootTypeEnum)
					.stream().map(x -> convertApproverApprove(x)).collect(Collectors.toList()));
		});
		return approverApproveExportLst;
	}
	
	private ApproverApproveExport convertApproverApprove(ApproverToApprove approverToApprove){
		return new ApproverApproveExport(
				approverToApprove.getDate(), 
				approverToApprove.getEmployeeID(), 
				approverToApprove.getAuthorList().stream().map(x -> convertApproverEmployee(x)).collect(Collectors.toList()));
	}
	
	private ApproverEmpExport convertApproverEmployee(ApproverEmployee approverEmployee){
		return new ApproverEmpExport(
				approverEmployee.getEmployeeID(), 
				approverEmployee.getEmployeeCD(), 
				approverEmployee.getEmployeeName());
	}

	@Override
	public AppRootInsContentExport createDailyApprover(String employeeID, Integer rootType, GeneralDate recordDate) {
		AppRootInstanceContent appRootInstanceContent = createDailyApprover.createDailyApprover(employeeID, EnumAdaptor.valueOf(rootType, RecordRootType.class), recordDate);
		return new AppRootInsContentExport(
				new AppRootInsExport(
						appRootInstanceContent.getAppRootInstance().getRootID(), 
						appRootInstanceContent.getAppRootInstance().getCompanyID(), 
						employeeID, 
						appRootInstanceContent.getAppRootInstance().getDatePeriod(), 
						rootType, 
						appRootInstanceContent.getAppRootInstance().getListAppPhase()
							.stream().map(x -> new AppPhaseInsExport(
									x.getPhaseOrder(), 
									x.getApprovalForm().value, 
									x.getListAppFrame()
										.stream().map(y -> new AppFrameInsExport(
											y.getFrameOrder(), 
											y.isConfirmAtr(), 
											y.getListApprover()))
										.collect(Collectors.toList())))
							.collect(Collectors.toList())), 
				appRootInstanceContent.getErrorFlag().value, 
				appRootInstanceContent.getErrorMsgID());
	}
	
}
