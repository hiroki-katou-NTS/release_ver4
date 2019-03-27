package nts.uk.ctx.at.function.ac.processexecution;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.AffWorkplaceHistoryImport;
import nts.uk.ctx.at.function.dom.adapter.AffWorkplaceHistoryItemImport;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceIdAndPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.WorkplaceWorkRecordAdapter;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceHistoryExport;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceHistoryItemExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceHistExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class WorkplaceWorkRecordAcFinder implements WorkplaceWorkRecordAdapter {

	@Inject
	private SyWorkplacePub syWorkplacePub;

	@Override
	public List<WorkPlaceHistImport> getWplByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {
		List<WorkPlaceHistImport> importList =
				this.syWorkplacePub.GetWplByListSidAndPeriod(sids, datePeriod)
					.stream()
						.map(x->convertToImport(x))
							.collect(Collectors.toList());
		return importList;
	}
	
	private WorkPlaceHistImport convertToImport(WorkPlaceHistExport export) {
		List<WorkPlaceIdAndPeriodImport> subListImport =
				export.getLstWkpIdAndPeriod()
					.stream()
						.map(x-> new WorkPlaceIdAndPeriodImport(x.getDatePeriod(), x.getWorkplaceId()))
							.collect(Collectors.toList());
		return new WorkPlaceHistImport(export.getEmployeeId(), subListImport);
	}

	@Override
	public List<String> findListWorkplaceIdByBaseDate(GeneralDate baseDate) {
		List<String> listWorkplaceId = syWorkplacePub.findListWorkplaceIdByBaseDate(baseDate);
		if(listWorkplaceId.isEmpty())
			return Collections.emptyList();
		return listWorkplaceId;
	}

	@Override
	public List<AffWorkplaceHistoryImport> getWorkplaceBySidsAndBaseDate(List<String> sids, GeneralDate baseDate) {
		List<AffWorkplaceHistoryExport> listAffWorkplaceHistoryExport  = syWorkplacePub.getWorkplaceBySidsAndBaseDate(sids,baseDate);
		return listAffWorkplaceHistoryExport
		.stream()
		.map(x -> 
			new AffWorkplaceHistoryImport(
					x.getSid(), 
					x.getHistoryItems(), 
					x.getWorkplaceHistItems()
						.entrySet()
						.stream()
						.collect(Collectors.toMap(Map.Entry::getKey, y -> convertToAffWorkplaceHistoryItemExport(y.getValue())))))
		.collect(Collectors.toList());
	}
//	private AffWorkplaceHistoryImport convertToExport(AffWorkplaceHistoryExport export) {
//		return new AffWorkplaceHistoryImport(export.getSid(),export.getHistoryItems(),);
//	}
	
	private AffWorkplaceHistoryItemImport convertToAffWorkplaceHistoryItemExport(AffWorkplaceHistoryItemExport export) {
		return new AffWorkplaceHistoryItemImport(export.getHistoryId(),export.getWorkplaceId(),export.getNormalWorkplaceId());
	}
}
