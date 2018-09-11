/**
 * 5:07:22 PM Mar 9, 2018
 */
package nts.uk.ctx.at.record.ac.workflow.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootStateStatusImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalStatus;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ReleasedProprietyDivision;
import nts.uk.ctx.workflow.pub.resultrecord.EmployeePerformParam;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.resultrecord.export.AppEmpStatusExport;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootOfEmployeeExport;
import nts.uk.ctx.workflow.pub.spr.SprAppRootStatePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
@Stateless
public class ApprovalStatusAdapterImpl implements ApprovalStatusAdapter {

	@Inject
	private ApprovalRootStatePub approvalRootStatePub;
	
	@Inject
	private IntermediateDataPub intermediateDataPub;
	@Inject
	private SprAppRootStatePub sprPub;
	@Override
	public List<ApproveRootStatusForEmpImport> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate,
			String employeeID, String companyID, Integer rootType) {
		return intermediateDataPub.getAppRootStatusByEmpPeriod(employeeID, new DatePeriod(startDate, endDate), rootType)
				.stream()
				.map((pub) -> new ApproveRootStatusForEmpImport(pub.getEmployeeID(), pub.getDate(),
						EnumAdaptor.valueOf(pub.getDailyConfirmAtr(), ApprovalStatusForEmployee.class)))
				.collect(Collectors.toList());
	}

	@Override
	public ApprovalRootOfEmployeeImport getApprovalRootOfEmloyee(GeneralDate startDate, GeneralDate endDate,
			String approverID, String companyID, Integer rootType) {
		ApprovalRootOfEmployeeExport export = approvalRootStatePub.getApprovalRootOfEmloyee(startDate, endDate,
				approverID, companyID, rootType);
		return convertFromExport(export);
	}

	private ApprovalRootOfEmployeeImport convertFromExport(ApprovalRootOfEmployeeExport export) {
		if(export.getApprovalRootSituations() != null && export.getEmployeeStandard() != null){
		return new ApprovalRootOfEmployeeImport(export.getEmployeeStandard(),
				export.getApprovalRootSituations().stream().map(situation -> {
					return new ApprovalRootSituation(situation.getAppRootID(),
							EnumAdaptor.valueOf(situation.getApprovalAtr().value, ApproverEmployeeState.class),
							situation
									.getAppDate(),
							situation.getTargetID(),
							new ApprovalStatus(
									EnumAdaptor.valueOf(situation.getApprovalStatus().getApprovalActionByEmpl().value,
											ApprovalActionByEmpl.class),
									EnumAdaptor.valueOf(situation.getApprovalStatus().getReleaseDivision().value,
											ReleasedProprietyDivision.class)));
				}).collect(Collectors.toList()));
		} else {
			return null;
		}
	}

	@Override
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(
			GeneralDate startDate, GeneralDate endDate,
			List<String> employeeIDs, String companyID, Integer rootType) {
		return approvalRootStatePub
				.getApprovalByListEmplAndDate(startDate, endDate, employeeIDs, companyID, rootType).stream()
				.map((pub) -> new ApproveRootStatusForEmpImport(pub.getEmployeeID(), pub.getAppDate(),
						EnumAdaptor.valueOf(pub.getApprovalStatus().value, ApprovalStatusForEmployee.class)))
				.collect(Collectors.toList());
	}

	@Override
	public boolean releaseApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,
			Integer rootType, String companyID) {
		return approvalRootStatePub.releaseApproval(approverID, approvalRecordDates, employeeID, rootType, companyID);
	}

	@Override
	public void registerApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,
			Integer rootType, String companyID) {
		approvalRootStatePub.registerApproval(approverID, approvalRecordDates, employeeID, rootType, companyID);
	}

	@Override
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(
			List<GeneralDate> approvalRecordDates, List<String> employeeID, Integer rootType) {
		return approvalRootStatePub.getApprovalByListEmplAndListApprovalRecordDate(approvalRecordDates, employeeID, rootType).stream()
				.map((pub) -> new ApproveRootStatusForEmpImport(pub.getEmployeeID(), pub.getAppDate(),
						EnumAdaptor.valueOf(pub.getApprovalStatus().value, ApprovalStatusForEmployee.class)))
				.collect(Collectors.toList());
	}

	@Override
	public void cleanApprovalRootState(String rootStateID, Integer rootType) {
		approvalRootStatePub.cleanApprovalRootState(rootStateID, rootType);
	}

	@Override
	public List<ApprovalRootStateStatusImport> getStatusByEmpAndDate(String employeeID, DatePeriod datePeriod,
			Integer rootType) {
		List<ApprovalRootStateStatusImport> lstOutput = sprPub.getStatusByEmpAndDate(employeeID, datePeriod.start(), datePeriod.end(), rootType)
				.stream().map(x -> new ApprovalRootStateStatusImport(x.getDate(), x.getEmployeeID(), x.getDailyConfirmAtr()))
				.collect(Collectors.toList());
		return lstOutput;
	}
	
	@Override
	public ApprovalRootOfEmployeeImport getApprovalRootOfEmloyeeNew(GeneralDate startDate, GeneralDate endDate,
			String approverID, String companyID, Integer rootType) {
		//ApprovalRootOfEmployeeExport 
		AppEmpStatusExport export = intermediateDataPub.getApprovalEmpStatus(
				approverID, new DatePeriod(startDate, endDate), rootType);
		return convertFromExportNew(export);
	}
	
	private ApprovalRootOfEmployeeImport convertFromExportNew(AppEmpStatusExport export) {
		//ApprovalRootOfEmployeeExport
		if(export.getRouteSituationLst() != null && export.getEmployeeID() != null){
		return new ApprovalRootOfEmployeeImport(export.getEmployeeID(),
				export.getRouteSituationLst().stream().map(situation -> {
					return new ApprovalRootSituation("",
							EnumAdaptor.valueOf(situation.getApproverEmpState(), ApproverEmployeeState.class),
							situation.getDate(),
							situation.getEmployeeID(),
							new ApprovalStatus(
									EnumAdaptor.valueOf(situation.getApprovalStatus().map(x -> x.getApprovalAction()).orElse(null),
											ApprovalActionByEmpl.class),
									EnumAdaptor.valueOf(situation.getApprovalStatus().map(x -> x.getReleaseAtr()).orElse(null),
											ReleasedProprietyDivision.class)));
				}).collect(Collectors.toList()));
		} else {
			return null;
		}
	}
	
	@Override
	public List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDateNew(
			List<GeneralDate> approvalRecordDates, List<String> employeeID, Integer rootType) {
		return intermediateDataPub.getAppRootStatusByEmpsDates(employeeID, approvalRecordDates, rootType).stream()
				.map((pub) -> new ApproveRootStatusForEmpImport(pub.getEmployeeID(), pub.getDate(),
						EnumAdaptor.valueOf(pub.getDailyConfirmAtr(), ApprovalStatusForEmployee.class)))
				.collect(Collectors.toList());
	}
}
