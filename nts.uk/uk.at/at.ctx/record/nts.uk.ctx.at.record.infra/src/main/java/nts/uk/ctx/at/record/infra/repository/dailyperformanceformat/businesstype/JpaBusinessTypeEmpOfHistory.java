package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat.businesstype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfHistory;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfHistoryPK;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * implement BusinessTypeEmpOfHistoryRepository
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaBusinessTypeEmpOfHistory extends JpaRepository implements BusinessTypeEmpOfHistoryRepository {
	
	private static final String FIND_BY_BASE_DATE;
	private static final String FIND_BY_EMPLOYEE;
	private static final String FIND_BY_EMPLOYEE_DESC;
	
	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT k ");
		stringBuilder.append("FROM KrcmtBusinessTypeOfHistory k ");
		stringBuilder.append("WHERE k.sId = :sId ");
		stringBuilder.append("AND k.startDate <= :baseDate1 and k.endDate >= :baseDate2");
		FIND_BY_BASE_DATE = stringBuilder.toString();

		stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT k ");
		stringBuilder.append("FROM KrcmtBusinessTypeOfHistory k ");
		stringBuilder.append("WHERE k.sId = :sId ");
		stringBuilder.append("AND k.cID = :cId ");
		stringBuilder.append("ORDER BY k.startDate");
		FIND_BY_EMPLOYEE = stringBuilder.toString();
		
		stringBuilder.append(" DESC");
		FIND_BY_EMPLOYEE_DESC = stringBuilder.toString();
	}

	private static KrcmtBusinessTypeOfHistory toEntity(String companyId, String employeeId, String historyId,
			GeneralDate startDate, GeneralDate endDate) {

		KrcmtBusinessTypeOfHistory entity = new KrcmtBusinessTypeOfHistory();
		KrcmtBusinessTypeOfHistoryPK pk = new KrcmtBusinessTypeOfHistoryPK(historyId);
		entity.KrcmtBusinessTypeOfHistoryPK = pk;
		entity.startDate = startDate;
		entity.endDate = endDate;
		entity.cID = companyId;
		entity.sId = employeeId;
		return entity;

	}

	private static BusinessTypeOfEmployeeHistory toDomain(List<KrcmtBusinessTypeOfHistory> entities) {
		String companyId = entities.get(0).cID;
		String employeeId = entities.get(0).sId;
		List<DateHistoryItem> histories = entities.stream().map(entity -> {
			DateHistoryItem history = new DateHistoryItem(entity.KrcmtBusinessTypeOfHistoryPK.historyId,
					new DatePeriod(entity.startDate, entity.endDate));
			return history;
		}).collect(Collectors.toList());
		return new BusinessTypeOfEmployeeHistory(companyId, histories, employeeId);
	}

	@Override
	public Optional<BusinessTypeOfEmployeeHistory> findByBaseDate(GeneralDate baseDate, String sId) {
		List<KrcmtBusinessTypeOfHistory> entities = this.queryProxy()
				.query(FIND_BY_BASE_DATE, KrcmtBusinessTypeOfHistory.class).setParameter("sId", sId)
				.setParameter("baseDate1", baseDate).setParameter("baseDate2", baseDate).getList();
		if (entities == null || entities.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(toDomain(entities));
		}

	}

	@Override
	public Optional<BusinessTypeOfEmployeeHistory> findByEmployee(String cid, String sId) {
		List<KrcmtBusinessTypeOfHistory> entities = this.queryProxy()
				.query(FIND_BY_EMPLOYEE, KrcmtBusinessTypeOfHistory.class).setParameter("sId", sId)
				.setParameter("cId", cid).getList();
		if (entities == null || entities.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(toDomain(entities));
		}
	}
	
	@Override
	public Optional<BusinessTypeOfEmployeeHistory> findByEmployeeDesc(String cid, String sId) {
		List<KrcmtBusinessTypeOfHistory> entities = this.queryProxy()
				.query(FIND_BY_EMPLOYEE_DESC, KrcmtBusinessTypeOfHistory.class).setParameter("sId", sId)
				.setParameter("cId", cid).getList();
		if (entities == null || entities.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(toDomain(entities));
		}
	}

	@Override
	public void add(String companyId, String employeeId, String historyId, GeneralDate startDate, GeneralDate endDate) {
		this.commandProxy().insert(toEntity(companyId, employeeId, historyId, startDate, endDate));
	}

	@Override
	public void update(String companyId, String employeeId, String historyId, GeneralDate startDate,
			GeneralDate endDate) {
		Optional<KrcmtBusinessTypeOfHistory> optional = this.queryProxy()
				.find(new KrcmtBusinessTypeOfHistoryPK(historyId), KrcmtBusinessTypeOfHistory.class);
		if(optional.isPresent()){
			KrcmtBusinessTypeOfHistory entity = optional.get();
			entity.startDate = startDate;
			entity.endDate = endDate;
			this.commandProxy().update(entity);
		}
		

	}

	@Override
	public void delete(String historyId) {
		this.commandProxy().remove(KrcmtBusinessTypeOfHistory.class, new KrcmtBusinessTypeOfHistoryPK(historyId));
	}

	@Override
	public Optional<BusinessTypeOfEmployeeHistory> findByHistoryId(String historyId) {
		Optional<KrcmtBusinessTypeOfHistory> entity = this.queryProxy()
				.find(new KrcmtBusinessTypeOfHistoryPK(historyId), KrcmtBusinessTypeOfHistory.class);
		if (entity.isPresent()) {
			return Optional.of(toDomain(new ArrayList<KrcmtBusinessTypeOfHistory>() {
				private static final long serialVersionUID = 1L;

				{
					add(entity.get());
				}
			}));
		}
		return Optional.empty();
	}

}
