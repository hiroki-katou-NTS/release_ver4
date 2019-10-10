package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessType;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypesRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessType;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypePK;

@Stateless
public class JpaBusinessTypesRepository extends JpaRepository implements BusinessTypesRepository {

	private static final String FIND;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessType a ");
		builderString.append("WHERE a.krcmtBusinessTypePK.companyId = :companyId ORDER BY a.krcmtBusinessTypePK.businessTypeCode ASC ");
		FIND = builderString.toString();
	} 
	/**
	 * author: HoangYen
	 * change from domain to entity
	 * @param domain
	 * @return
	 */
	private static KrcmtBusinessType toEntity(BusinessType domain){
		val entity = new KrcmtBusinessType();
		entity.krcmtBusinessTypePK = new KrcmtBusinessTypePK(domain.getCompanyId(), domain.getBusinessTypeCode().v());
		entity.businessTypeName = domain.getBusinessTypeName().v();
		return entity;
	}
	

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<BusinessType> findAll(String companyId) {
		return this.queryProxy().query(FIND, KrcmtBusinessType.class).setParameter("companyId", companyId)
				.getList(f -> toDomain(f));
	}

	private static BusinessType toDomain(KrcmtBusinessType krcmtBusinessType) {
		BusinessType workType = BusinessType.createFromJavaType(
				krcmtBusinessType.krcmtBusinessTypePK.companyId,
				krcmtBusinessType.krcmtBusinessTypePK.businessTypeCode,
				krcmtBusinessType.businessTypeName);
		return workType;
	}
	/**
	 * author: HoangYen
	 * update business type name by companyId and business type code 
	 */
	@Override
	public void updateBusinessTypeName(BusinessType businessType) {
		KrcmtBusinessType a = toEntity(businessType);
		KrcmtBusinessType x = this.queryProxy().find(a.krcmtBusinessTypePK, KrcmtBusinessType.class).get();
		x.setBusinessTypeName(a.businessTypeName);
		this.commandProxy().update(x);
	}
	/**
	 * author: HoangYen
	 * insert business type 
	 */
	@Override
	public void insertBusinessType(BusinessType businessType) {
		this.commandProxy().insert(toEntity(businessType));
	}
	/**
	 * author: HoangYen
	 * find business type by companyId and work type code
	 */
	@Override
	public Optional<BusinessType> findByCode(String companyId, String businessTypeCode) {
		return this.queryProxy().find(new KrcmtBusinessTypePK(companyId, businessTypeCode), KrcmtBusinessType.class).map(c-> toDomain(c));
	}
	/**
	 * author: HoangYen
	 * delete business type by companyId and work type code 
	 */
	@Override
	public void deleteBusinessType(String companyId, String businessTypeCode) {
		KrcmtBusinessTypePK krcmtBusinessTypePK = new KrcmtBusinessTypePK(companyId, businessTypeCode);
		this.commandProxy().remove(KrcmtBusinessType.class, krcmtBusinessTypePK);
	}

}
