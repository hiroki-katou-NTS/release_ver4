package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat.businesstype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfHistory;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfHistoryPK;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHis;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHisAdaptor;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * implement BusinessTypeEmpOfHistoryRepository
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaBusinessTypeEmpOfHistory extends JpaRepository
		implements BusinessTypeEmpOfHistoryRepository, BusinessTypeOfEmpHisAdaptor {
	private static final String FIND_BY_BASE_DATE;
	private static final String FIND_BY_EMPLOYEE;
	private static final String FIND_BY_EMPLOYEE_DESC;
//	private static final String SEL_BUSINESS_TYPE;

	private static final String FIND_BY_CID_SID_DATE_PERIOD = "SELECT NEW " + BusinessTypeOfEmpDto.class.getName()
			+ " (a.cID, a.sId, a.krcmtBusinessTypeOfHistoryPK.historyId, a.startDate, a.endDate, b.businessTypeCode)"
			+ " FROM KrcmtBusinessTypeOfHistory a JOIN KrcmtBusinessTypeOfEmployee b"
			+ " ON a.krcmtBusinessTypeOfHistoryPK.historyId = b.krcmtBusinessTypeOfEmployeePK.historyId"
			+ " WHERE a.sId IN :sIds" + " AND a.cID = :cId"
			+ " AND a.startDate <= :endDate and a.endDate >= :startDate";

	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT k ");
		stringBuilder.append("FROM KrcmtBusinessTypeOfHistory k ");
		stringBuilder.append("WHERE k.sId = :sId ");
		stringBuilder.append("AND k.startDate <= :baseDate and k.endDate >= :baseDate");
		FIND_BY_BASE_DATE = stringBuilder.toString();
		
//		StringBuilder builderString = new StringBuilder();
//		builderString.append("SELECT h");
//		builderString.append(" FROM KrcmtBusinessTypeOfEmployee b");
//		builderString.append(" JOIN KrcmtBusinessTypeOfHistory h");
//		builderString
//				.append(" ON b.krcmtBusinessTypeOfEmployeePK.historyId = h.krcmtBusinessTypeOfHistoryPK.historyId");
//		builderString.append(" WHERE b.sId IN :lstSID");
//		builderString.append(" AND h.startDate <= :endYmd");
//		builderString.append(" AND h.endDate >= :startYmd");
//		SEL_BUSINESS_TYPE = stringBuilder.toString();

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
			GeneralDate startDate, GeneralDate endDate, String businessTypeCode) {

		KrcmtBusinessTypeOfHistory entity = new KrcmtBusinessTypeOfHistory();
		KrcmtBusinessTypeOfHistoryPK pk = new KrcmtBusinessTypeOfHistoryPK(historyId);
		entity.krcmtBusinessTypeOfHistoryPK = pk;
		entity.startDate = startDate;
		entity.endDate = endDate;
		entity.cID = companyId;
		entity.sId = employeeId;
		entity.businessTypeCode = businessTypeCode;
		return entity;

	}

	private static BusinessTypeOfEmployeeHistory toDomain(List<KrcmtBusinessTypeOfHistory> entities) {
		String companyId = entities.get(0).cID;
		String employeeId = entities.get(0).sId;
		List<DateHistoryItem> histories = entities.stream().map(entity -> {
			DateHistoryItem history = new DateHistoryItem(entity.krcmtBusinessTypeOfHistoryPK.historyId,
					new DatePeriod(entity.startDate, entity.endDate));
			return history;
		}).collect(Collectors.toList());
		return new BusinessTypeOfEmployeeHistory(companyId, histories, employeeId);
	}

	@Override
	public Optional<BusinessTypeOfEmployeeHistory> findByBaseDate(GeneralDate baseDate, String sId) {
		List<KrcmtBusinessTypeOfHistory> entities = this.queryProxy()
				.query(FIND_BY_BASE_DATE, KrcmtBusinessTypeOfHistory.class).setParameter("sId", sId)
				.setParameter("baseDate", baseDate).getList();
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
	
//	 Merge KRCMT_BUS_TYPE_HIST To KRCMT_BUS_TYPE_SYAIN  because response
//	 new Insert Method ↓
//	         Class      : here
//	         MethodName : addToMerge
//	@Override
//	public void add(String companyId, String employeeId, String historyId, GeneralDate startDate, GeneralDate endDate) {
//		this.commandProxy().insert(toEntity(companyId, employeeId, historyId, startDate, endDate));
//	}
	@Override
	public void addToMerge(String companyId, String employeeId, String historyId, GeneralDate startDate, GeneralDate endDate, String businessTypeCode) {
		this.commandProxy().insert(toEntity(companyId, employeeId, historyId, startDate, endDate, businessTypeCode));
	}

	@Override
	public void update(String companyId, String employeeId, String historyId, GeneralDate startDate,
			GeneralDate endDate) {
		Optional<KrcmtBusinessTypeOfHistory> optional = this.queryProxy()
				.find(new KrcmtBusinessTypeOfHistoryPK(historyId), KrcmtBusinessTypeOfHistory.class);
		if (optional.isPresent()) {
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

	@Override
	public Optional<BusinessTypeOfEmpHis> findByBaseDateAndSid(GeneralDate baseDate, String sId) {
		return this.findByBaseDate(baseDate, sId).map(x -> new BusinessTypeOfEmpHis(x.getCompanyId(), x.getEmployeeId(),
				x.getHistory().get(0).identifier(), x.getHistory().get(0).span()));
	}

	@Override
	public List<BusinessTypeOfEmpDto> findByCidSidBaseDate(String cid, List<String> sIds, DatePeriod datePeriod) {
		
		// ResultList
		List<BusinessTypeOfEmpDto> resultList = new ArrayList<>();
		// Split employeeId List if size of employeeId List is greater than 1000
		CollectionUtil.split(sIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			List<BusinessTypeOfEmpDto> entities = this.queryProxy()
					.query(FIND_BY_CID_SID_DATE_PERIOD, BusinessTypeOfEmpDto.class).setParameter("sIds", subList)
					.setParameter("cId", cid).setParameter("startDate", datePeriod.start())
					.setParameter("endDate", datePeriod.end())
					.getList();
			resultList.addAll(entities);
		});
		
		return resultList;
	}

//	@Override
//	public List<BusinessTypeOfEmployeeHistory> getListBusinessType(List<String> lstEmployee, Period dateRange) {
//		List<BusinessTypeOfEmployeeHistory> data = this.queryProxy().query(SEL_BUSINESS_TYPE,KrcmtBusinessTypeOfHistory.class)
//				.set
//				List<String> businessTypes = new ArrayList<>();
//				CollectionUtil.split(lstEmployee, 1000, subList -> {
//					businessTypes.addAll(this.queryProxy().query(SEL_BUSINESS_TYPE, String.class)
//							.setParameter("lstSID", subList).setParameter("startYmd", dateRange.getStartDate())
//							.setParameter("endYmd", dateRange.getEndDate()).getList());
//				});
//				return businessTypes;
//		return null;
//	}
}
