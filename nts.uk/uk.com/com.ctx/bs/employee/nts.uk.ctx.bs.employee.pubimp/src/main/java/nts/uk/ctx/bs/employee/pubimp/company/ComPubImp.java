package nts.uk.ctx.bs.employee.pubimp.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

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
			if (listAffComHisByEmp.get(i).getSId() != null && !listAffComHisByEmp.get(i).getSId().isEmpty() && !lstPeriod.isEmpty()) {
				StatusOfEmployee statusOfEmployee = new StatusOfEmployee(listAffComHisByEmp.get(i).getSId(), CheckPeriod(lstPeriod, datePeriod));
				result.add(statusOfEmployee);
			}
		}
		return result;
	};
	
	List<DatePeriod> CheckPeriod(List<DatePeriod> lstPeriod, DatePeriod param) {

		List<DatePeriod> result = new ArrayList<>();
		for (int i = 0; i < lstPeriod.size(); i++) {
			GeneralDate start = lstPeriod.get(i).start();
			GeneralDate end = lstPeriod.get(i).end();
			/**
			 *  case1
			 *   period |========================>
			 *   param       |===========>
			 */
			if(param.start().afterOrEquals(start) && param.end().beforeOrEquals(end)){
				result.add(new DatePeriod(param.start(), param.end()));
			}
			/**
			 *  case2
			 *  period           |========================>
			 *  param       |===========>
			 */
			if(param.start().beforeOrEquals(start) && param.end().afterOrEquals(start) && param.end().beforeOrEquals(end)){
				result.add(new DatePeriod(start , param.end()));
			}
			/**
			 *  case3
			 *  period           |========================>
			 *  param       |===================================>
			 */
			if(param.start().beforeOrEquals(start) && param.end().afterOrEquals(end)){
				result.add(new DatePeriod(start , end));
			}
			/**
			 *  case4
			 *  period      |========================>
			 *  param                        |===========>
			 *  
			 */
			if(param.start().afterOrEquals(start) && param.start().beforeOrEquals(end) && param.end().afterOrEquals(end)){
				result.add(new DatePeriod(param.start() , end));
			}
		}
		return result;

	}

}
