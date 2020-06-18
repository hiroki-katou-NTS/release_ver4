package nts.uk.ctx.at.record.ac.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.adapter.algorithm.AppTargetPersonStatusAdapter;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.ctx.workflow.pub.service.export.ApproveRootStatusForEmpExport;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AppTargetPersonStatusAcFinder implements AppTargetPersonStatusAdapter {

	@Inject
	private ApprovalRootStatePub approvalRootStatePub;
	@Inject
	private IntermediateDataPub intermediateDataPub;

	@Override
	public List<StateConfirm> appTargetPersonStatus(String employeeID, GeneralDate startDate, GeneralDate endDate,
			int routeType) {
		// rq NO 113 old
		List<ApproveRootStatusForEmpExport> listState = approvalRootStatePub.getApprovalByEmplAndDate(startDate,
				endDate, employeeID, AppContexts.user().companyId(), routeType);

		if (!listState.isEmpty()) {
			return listState.stream().map(c -> convertToStateConfirm(c)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private StateConfirm convertToStateConfirm(ApproveRootStatusForEmpExport export) {
		return new StateConfirm(export.getAppDate(), export.getApprovalStatus().value == 2 ? true : false, export.getEmployeeID());
	}

	@Override
	public List<StateConfirm> appTargetPersonStatus(String employeeID, DatePeriod date) {
		List<AppRootStateStatusSprExport> listState=intermediateDataPub.getDailyAppRootStatus(Arrays.asList(employeeID), date).getAppRootStateStatusLst();
		if(!CollectionUtil.isEmpty(listState)) {
			return listState.stream().map(c->convertToStateConfirms(c)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<StateConfirm> appTargetPersonStatus(List<String> employeeID, DatePeriod date) {
		List<AppRootStateStatusSprExport> listState = intermediateDataPub.getAppRootStatusByEmpsPeriod(employeeID, date);
		if (!CollectionUtil.isEmpty(listState)) {
			return listState.stream().map(c -> convertToStateConfirms(c)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private StateConfirm convertToStateConfirms(AppRootStateStatusSprExport export) {
		return new StateConfirm(export.getDate(), export.getDailyConfirmAtr() == 2 ? true : false, export.getEmployeeID());
	}

}
