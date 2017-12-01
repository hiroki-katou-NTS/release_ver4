package nts.uk.ctx.bs.employee.infra.repository.department.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistory;
import nts.uk.ctx.bs.employee.dom.department.affiliate.AffDepartmentHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.department.BsymtAffiDepartmentHist;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaAffDepartmentHistoryRepository  extends JpaRepository implements AffDepartmentHistoryRepository{
	private final String QUERY_GET_AFFDEPARTMENT_BYSID = "SELECT ad FROM BsymtAffiDepartmentHist ad "
			+ "WHERE ad.sid = :sid ORDER BY ad.strDate";
	
	private static final String SELECT_BY_EMPID_STANDARDDATE = "SELECT ad FROM BsymtAffiDepartmentHist ad"
			+ " WHERE ad.sid = :employeeId AND ad.strDate <= :standardDate <= ad.endDate";
	
	private static final String SELECT_BY_HISTID = "SELECT ad FROM BsymtAffiDepartmentHist ad"
			+ " WHERE ad.hisId = :historyId";
	
	private AffDepartmentHistory toAffDepartment(String sId, List<BsymtAffiDepartmentHist> listHist){
		AffDepartmentHistory affDepart = new AffDepartmentHistory(sId, new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BsymtAffiDepartmentHist item : listHist){
			dateItem = new DateHistoryItem(item.getHisId(), new DatePeriod(item.getStrDate(), item.getEndDate()));
			affDepart.add(dateItem);
		}
		return affDepart;
	}
	
	@Override
	public Optional<AffDepartmentHistory> getAffDepartmentHistorytByEmployeeId(String employeeId) {
		
		List<BsymtAffiDepartmentHist> listHist = this.queryProxy().query(QUERY_GET_AFFDEPARTMENT_BYSID,BsymtAffiDepartmentHist.class)
				.setParameter("sid", employeeId).getList();
		if (!listHist.isEmpty()){
			return Optional.of(toAffDepartment(employeeId,listHist));
		}
		return Optional.empty();
	}

	@Override
	public void add(String sid, DateHistoryItem domain) {
		this.commandProxy().insert(toEntity(sid, domain));
	}

	@Override
	public void update(DateHistoryItem domain) {
		Optional<BsymtAffiDepartmentHist> itemToBeUpdated = this.queryProxy().find(domain.identifier(), BsymtAffiDepartmentHist.class);
		if (!itemToBeUpdated.isPresent()){
			throw new RuntimeException("Invalid BsymtAffiDepartmentHist");
		}
		updateEntity(domain, itemToBeUpdated.get());
		this.commandProxy().update(itemToBeUpdated.get());
	}

	@Override
	public void delete(String histId) {
		Optional<BsymtAffiDepartmentHist> itemToBeDeleted = this.queryProxy().find(histId, BsymtAffiDepartmentHist.class);
		if (!itemToBeDeleted.isPresent()){
			throw new RuntimeException("Invalid BsymtAffiDepartmentHist");
		}
		this.commandProxy().remove(BsymtAffiDepartmentHist.class, histId);
		
	}
	
	private BsymtAffiDepartmentHist toEntity(String employeeId, DateHistoryItem item){
		return new BsymtAffiDepartmentHist(item.identifier(), employeeId, item.start(), item.end());
	}
	/**
	 * Update entity from domain
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item,BsymtAffiDepartmentHist entity){	
		entity.setStrDate(item.start());
		entity.setEndDate(item.end());
	}

	@Override
	public Optional<AffDepartmentHistory> getAffDeptHistByEmpHistStandDate(String employeeId, GeneralDate standardDate) {
		List<BsymtAffiDepartmentHist> listHist = this.queryProxy().query(SELECT_BY_EMPID_STANDARDDATE,BsymtAffiDepartmentHist.class)
				.setParameter("employeeId", employeeId)
				.setParameter("standardDate", standardDate).getList();
		if (!listHist.isEmpty()){
			return Optional.of(toAffDepartment(employeeId,listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AffDepartmentHistory> getByHistId(String historyId) {
		List<BsymtAffiDepartmentHist> listHist = this.queryProxy().query(SELECT_BY_HISTID,BsymtAffiDepartmentHist.class)
				.setParameter("historyId", historyId).getList();
		if (!listHist.isEmpty()){
			return Optional.of(toAffDepartment(listHist.get(0).getSid(),listHist));
		}
		return Optional.empty();
	}
}
