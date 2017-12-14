package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat.businesstype;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfEmployee;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.businesstype.KrcmtBusinessTypeOfEmployeePK;

/**
 * implement BusinessTypeOfEmployeeRepository
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaBusinessTypeOfEmployee extends JpaRepository implements BusinessTypeOfEmployeeRepository {
	private static final String FIND_BY_LIST_CODE;
	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT k ");
		stringBuilder.append("FROM KrcmtBusinessTypeOfEmployee ");
		stringBuilder.append("WHERE k.krcmtBusinessTypeOfEmployeePK.businessTypeCode IN :businessTypeCodes");
		FIND_BY_LIST_CODE = stringBuilder.toString();

	}

	@Override
	public List<BusinessTypeOfEmployee> findAllByListCode(List<String> businessTypeCodes) {
		return this.queryProxy().query(FIND_BY_LIST_CODE, KrcmtBusinessTypeOfEmployee.class)
				.getList(entity -> toDomain(entity));
	}

	@Override
	public void insert(BusinessTypeOfEmployee businessTypeOfEmployee) {
		this.commandProxy().insert(toEntity(businessTypeOfEmployee));

	}

	@Override
	public void update(BusinessTypeOfEmployee businessTypeOfEmployee) {
		this.commandProxy().update(toEntity(businessTypeOfEmployee));

	}

	@Override
	public void delete(String historyId) {
		this.commandProxy().remove(KrcmtBusinessTypeOfEmployee.class, new KrcmtBusinessTypeOfEmployeePK(historyId));

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

}
