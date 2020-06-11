package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat.businesstype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfEmployee;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfEmployeePK;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmp;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpAdaptor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * implement BusinessTypeOfEmployeeRepository
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaBusinessTypeOfEmployee extends JpaRepository
		implements BusinessTypeOfEmployeeRepository, BusinessTypeOfEmpAdaptor {
	private static final String FIND_BY_LIST_CODE;
	private static final String FIND_BY_SID_HISTID;
	private static final String SEL_BUSINESS_TYPE;
	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT k ");
		stringBuilder.append("FROM KrcmtBusinessTypeOfEmployee k ");
		stringBuilder.append("WHERE k.businessTypeCode IN :businessTypeCodes");
		FIND_BY_LIST_CODE = stringBuilder.toString();

		StringBuilder stringBuild = new StringBuilder();
		stringBuild.append("SELECT k ");
		stringBuild.append("FROM KrcmtBusinessTypeOfEmployee k ");
		stringBuild.append("WHERE k.sId = :employeeId ");
		stringBuild.append("AND k.krcmtBusinessTypeOfEmployeePK.historyId = :historyId");
		FIND_BY_SID_HISTID = stringBuild.toString();
		
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtBusinessTypeOfEmployee b");
		builderString.append(" JOIN KrcmtBusinessTypeOfHistory h");
		builderString
				.append(" ON b.krcmtBusinessTypeOfEmployeePK.historyId = h.krcmtBusinessTypeOfHistoryPK.historyId");
		builderString.append(" WHERE b.sId IN :lstSid");
		builderString.append(" AND h.startDate <= :endYmd");
		builderString.append(" AND h.endDate >= :startYmd");
		SEL_BUSINESS_TYPE = builderString.toString();
	}

	@Override
	public List<BusinessTypeOfEmployee> findAllByListCode(List<String> businessTypeCodes) {
		List<BusinessTypeOfEmployee> resultList = new ArrayList<>();
		CollectionUtil.split(businessTypeCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(FIND_BY_LIST_CODE, KrcmtBusinessTypeOfEmployee.class)
				.setParameter("businessTypeCodes", subList)
				.getList(entity -> toDomain(entity)));
		});
		return resultList;
	}
	
//	 Merge KRCMT_BUS_TYPE_HIST To KRCMT_BUS_TYPE_SYAIN  because response
//	 new Insert Method ↓
//	         ClassName  : JpaBusinessTypeEmpOfHistory
//	         MethodName : addToMerge
//	@Override
//	public void insert(BusinessTypeOfEmployee businessTypeOfEmployee) {
//		this.commandProxy().insert(toEntity(businessTypeOfEmployee));
//	}

	@Override
	public void update(BusinessTypeOfEmployee businessTypeOfEmployee) {
		// Hop.NT update
		KrcmtBusinessTypeOfEmployeePK pk = new KrcmtBusinessTypeOfEmployeePK(businessTypeOfEmployee.getHistoryId());
		Optional<KrcmtBusinessTypeOfEmployee> bTypeOfEmp = this.queryProxy().find(pk,
				KrcmtBusinessTypeOfEmployee.class);

		if (bTypeOfEmp.isPresent()) {
			updateEntities(businessTypeOfEmployee, bTypeOfEmp.get());
			this.commandProxy().update(bTypeOfEmp.get());
		}
	}

	@Override
	public void delete(String historyId) {
		this.commandProxy().remove(KrcmtBusinessTypeOfEmployee.class, new KrcmtBusinessTypeOfEmployeePK(historyId));

	}

	/**
	 * Update entity
	 * 
	 * @param domain
	 * @param entity
	 * @author hop.nt
	 */
	private void updateEntities(BusinessTypeOfEmployee domain, KrcmtBusinessTypeOfEmployee entity) {
		entity.businessTypeCode = domain.getBusinessTypeCode().v();

	}

	private static KrcmtBusinessTypeOfEmployee toEntity(BusinessTypeOfEmployee domain) {
		KrcmtBusinessTypeOfEmployeePK pk = new KrcmtBusinessTypeOfEmployeePK(domain.getHistoryId());
		return new KrcmtBusinessTypeOfEmployee(pk, domain.getSId(), domain.getBusinessTypeCode().v());
	}

	private static BusinessTypeOfEmployee toDomain(KrcmtBusinessTypeOfEmployee entity) {
		return BusinessTypeOfEmployee.createFromJavaType(entity.businessTypeCode,
				entity.krcmtBusinessTypeOfEmployeePK.historyId, entity.sId);
	}

	@Override
	public Optional<BusinessTypeOfEmployee> findByHistoryId(String historyId) {
		Optional<KrcmtBusinessTypeOfEmployee> entity = this.queryProxy()
				.find(new KrcmtBusinessTypeOfEmployeePK(historyId), KrcmtBusinessTypeOfEmployee.class);
		if (entity.isPresent()) {
			return Optional.of(toDomain(entity.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<BusinessTypeOfEmp> getBySidAndHistId(String employeeId, String histId) {
		return this.queryProxy().query(FIND_BY_SID_HISTID, KrcmtBusinessTypeOfEmployee.class)
				.setParameter("employeeId", employeeId).setParameter("historyId", histId)
				.getSingle(x -> new BusinessTypeOfEmp(x.businessTypeCode, x.krcmtBusinessTypeOfEmployeePK.historyId,
						x.sId));
	}

	@Override
	public List<BusinessTypeOfEmployee> findAllByEmpAndDate(List<String> employeeIds, DatePeriod date) {
		List<BusinessTypeOfEmployee> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SEL_BUSINESS_TYPE, KrcmtBusinessTypeOfEmployee.class)
					.setParameter("lstSid", subList)
					.setParameter("endYmd", date.end())
					.setParameter("startYmd", date.start())
					.getList(entity -> toDomain(entity)));
		});
		return resultList;
	}

}
