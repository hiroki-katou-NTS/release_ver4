package nts.uk.ctx.bs.employee.pubimp.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.pub.company.AffComHistItem;
import nts.uk.ctx.bs.employee.pub.company.AffCompanyHistExport;
import nts.uk.ctx.bs.employee.pub.company.StatusOfEmployee;
import nts.uk.ctx.bs.employee.pub.company.SyCompanyPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ComPubImp implements SyCompanyPub {

	@Inject
	private AffCompanyHistRepository affComHistRepo;

	@Override
	public List<AffCompanyHistExport> GetAffCompanyHistByEmployee(List<String> sids, DatePeriod datePeriod) {

		if (sids.isEmpty() || datePeriod.start() == null || datePeriod.end() == null)
			return Collections.emptyList();

		List<AffCompanyHist> his = affComHistRepo.getAffComHisEmpByLstSidAndPeriod(sids, datePeriod);
		return sids.stream().map(sid -> {
			AffCompanyHistExport affComHostEx = new AffCompanyHistExport();
			affComHostEx.setEmployeeId(sid);
			affComHostEx.setLstAffComHistItem(new ArrayList<>());
			
			AffCompanyHistByEmployee affComHistByEmp = his.stream().filter(c -> c.getAffCompanyHistByEmployee(sid) != null)
								.map(c -> c.getAffCompanyHistByEmployee(sid))											
								.findFirst().orElse(null);
			if (affComHistByEmp != null) {
				if (affComHistByEmp.items() != null) {
					affComHostEx.setLstAffComHistItem(affComHistByEmp.items().stream().map(item -> new AffComHistItem(item.getHistoryId(),
											item.isDestinationData(), item.getDatePeriod()))
									.collect(Collectors.toList()));
				}
			}
			return affComHostEx;
		}).filter(c -> c != null).collect(Collectors.toList());
	}

	@Override
	public AffCompanyHistExport GetAffComHisBySidAndBaseDate(String sid, GeneralDate baseDate) {
		
		AffCompanyHist affComHis = affComHistRepo.getAffCompanyHistoryOfEmployeeAndBaseDate(sid, baseDate);
		
		if (affComHis == null){
			return new AffCompanyHistExport(null, Collections.emptyList());
		}
		
		AffCompanyHistByEmployee affComBySid = affComHis.getAffCompanyHistByEmployee(sid);
		
		AffCompanyHistExport affComHostEx = new AffCompanyHistExport();
		affComHostEx.setEmployeeId(sid);

		affComHostEx.setLstAffComHistItem(affComBySid.getLstAffCompanyHistoryItem().stream()
				.map(item -> new AffComHistItem(item.getHistoryId(), item.isDestinationData(), item.getDatePeriod()))
				.collect(Collectors.toList()));

		return affComHostEx;
	}

	@Override
	public AffCompanyHistExport GetAffComHisBySid(String cid, String sid) {
		AffCompanyHist affComHis = affComHistRepo.getAffCompanyHistoryOfEmployee(sid);
		
		if (affComHis == null){
			return new AffCompanyHistExport(null, Collections.emptyList());
		}
		
		AffCompanyHistByEmployee affComBySid = affComHis.getAffCompanyHistByEmployee(sid);
		
		AffCompanyHistExport affComHostEx = new AffCompanyHistExport();
		affComHostEx.setEmployeeId(sid);

		affComHostEx.setLstAffComHistItem(affComBySid.getLstAffCompanyHistoryItem().stream()
				.map(item -> new AffComHistItem(item.getHistoryId(), item.isDestinationData(), item.getDatePeriod()))
				.collect(Collectors.toList()));

		return affComHostEx;
	}

	@Override
	public List<StatusOfEmployee> GetListAffComHistByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {
		
		if (sids.isEmpty() || datePeriod.start() == null || datePeriod.end() == null)
			return Collections.emptyList();
		
		List<AffCompanyHistByEmployee> listAffComHisByEmp = this.affComHistRepo.getAffEmployeeHistory(sids, datePeriod);
		
		if(listAffComHisByEmp.isEmpty())
			return Collections.emptyList();
		
		List<StatusOfEmployee> result = listAffComHisByEmp.stream().map(i ->{
			return new StatusOfEmployee();
		}).collect(Collectors.toList());
		
		for (int i = 0; i < listAffComHisByEmp.size(); i++) {
			List<DatePeriod> lstPeriod = listAffComHisByEmp.get(i).items()
					.stream().map(ent -> ent.span())
					.collect(Collectors.toList());
			StatusOfEmployee statusOfEmployee = new StatusOfEmployee(listAffComHisByEmp.get(i).getSId(), lstPeriod);
			result.add(statusOfEmployee);
		}
		
		return result;
	};

}
