package nts.uk.ctx.at.shared.ac.dailyperformance;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.AppEmpStatusImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.ApprovalStatusImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.DailyPerformanceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.RouteSituationImport;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppEmpStatusExport;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class DailyPerformanceAdapterImpl implements DailyPerformanceAdapter {

	
	@Inject
	private ApprovalRootStatePub approvalRootStatePub;
	
	@Inject
	private IntermediateDataPub intermediateDataPub;
	
	@Override
	public boolean checkDataApproveed(GeneralDate startDate, GeneralDate endDate, String approverID, Integer rootType,String companyID) {
		return approvalRootStatePub.checkDataApproveed(startDate, endDate, approverID, rootType, companyID);}
	
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType){
		return approvalRootStatePub.getApprovalByListEmplAndListApprovalRecordDate(approvalRecordDates, employeeID, rootType).stream().map(item ->{
			return new ApproveRootStatusForEmpImport(item.getEmployeeID(), item.getAppDate(), item.getApprovalStatus().value);
		}).collect(Collectors.toList());
	}

	@Override
	public boolean isDataExist(String approverID, DatePeriod period, Integer rootType) {
		// TODO Auto-generated method stub
		val x = intermediateDataPub.isDataExist(approverID, period, rootType);
		return x;
	}

	@Override
	public boolean dataMonth(String approverID, DatePeriod period, YearMonth yearMonth) {
		val x = intermediateDataPub.isDataExistMonth(approverID, period, yearMonth);
		return x;
	}

	/**
	 * @author yennth
	 */
	@Override 
	public AppEmpStatusImport appEmpStatusExport(String employeeID, DatePeriod period, Integer rootType) {
		AppEmpStatusExport appEmpStatusExport = intermediateDataPub.getApprovalEmpStatus(employeeID, period, rootType);
		List<RouteSituationImport> routeSituationLst = appEmpStatusExport.getRouteSituationLst().stream().map(item -> {
			return new RouteSituationImport(item.getDate(), item.getEmployeeID(), item.getApproverEmpState(), 
											Optional.of(new ApprovalStatusImport(item.getApprovalStatus().get().getReleaseAtr(), item.getApprovalStatus().get().getApprovalAction())));
		}).collect(Collectors.toList());
		// use RequestList133
		AppEmpStatusImport x = new AppEmpStatusImport(appEmpStatusExport.getEmployeeID(), routeSituationLst);
		return x;
	}

}
