package nts.uk.ctx.at.schedule.infra.repository.shift.specificdayset.company;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.specificdayset.company.KsmmtComSpecDateSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.specificdayset.company.KsmmtComSpecDateSetPK;

@Stateless
public class JpaCompanySpecificDateRepository extends JpaRepository implements CompanySpecificDateRepository {
	
	/**
	 * select a KsmmtComSpecDateSet ALL
	 */
	private static final String SELECT_NO_WHERE = "SELECT s FROM KsmmtComSpecDateSet s";
	
	/**
	 * select KsmmtComSpecDateSet by date
	 */
	private static final String GET_BY_DATE = SELECT_NO_WHERE 
			+ " WHERE s.ksmmtComSpecDateSetPK.companyId = :companyId"
			+ " AND s.ksmmtComSpecDateSetPK.specificDate = :specificDate";

	/**
	 * get List With Name of Specific
	 */
	private static final String GET_BY_USE_WITH_NAME = "SELECT p.name,p.useAtr, s FROM KsmmtComSpecDateSet s"
			+ " INNER JOIN KsmstSpecificDateItem p ON p.ksmstSpecificDateItemPK.itemNo = s.ksmmtComSpecDateSetPK.specificDateItemNo"
			+ " WHERE s.ksmmtComSpecDateSetPK.companyId = :companyId"
			+ " AND s.ksmmtComSpecDateSetPK.specificDate >= :startYm"
			+ " AND s.ksmmtComSpecDateSetPK.specificDate <= :endYm";
	
	/**
	 *Delete by Month 
	 */
	private static final String DELETE_BY_YEAR_MONTH = "DELETE from KsmmtComSpecDateSet c "
			+ " WHERE c.ksmmtComSpecDateSetPK.companyId = :companyId"
			+ " AND c.ksmmtComSpecDateSetPK.specificDate >= :startYm"
			+ " AND c.ksmmtComSpecDateSetPK.specificDate <= :endYm";
	
	private static final String DELETE_BY_DATE = "DELETE FROM KsmmtComSpecDateSet c"
			+ " WHERE c.ksmmtComSpecDateSetPK.companyId = :companyId"
			+ " AND c.ksmmtComSpecDateSetPK.specificDate = :specificDate";
	

	/**
	 * Get list Company Specific Date NO with name
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<CompanySpecificDateItem> getComSpecByDate(String companyId, GeneralDate specificDate) {
		return this.queryProxy().query(GET_BY_DATE, KsmmtComSpecDateSet.class)
				.setParameter("companyId", companyId)
				.setParameter("specificDate", specificDate).getList(x -> toDomain(x));
	}

	/**
	 * Get list Company Specific Date  WITH name
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<CompanySpecificDateItem> getComSpecByDateWithName(String companyId, GeneralDate startDate, GeneralDate endDate) {
		return this.queryProxy().query(GET_BY_USE_WITH_NAME, Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("startYm", startDate)
				.setParameter("endYm", endDate)
				.getList(x -> toDomainWithName(x));
	}

	/**
	 * convert entity to domain NO with Name
	 * @param entity
	 * @return
	 */
	private static CompanySpecificDateItem toDomain(KsmmtComSpecDateSet entity) {
		CompanySpecificDateItem domain = CompanySpecificDateItem.createFromJavaType(
				entity.ksmmtComSpecDateSetPK.companyId, entity.ksmmtComSpecDateSetPK.specificDate,
				entity.ksmmtComSpecDateSetPK.specificDateItemNo, "");
		return domain;
	}

	/**
	 * convert entity to domain with Name
	 * @param object
	 * @return
	 */
	private static CompanySpecificDateItem toDomainWithName(Object[] object) {
		String specificDateItemName = (String) object[0];
		KsmmtComSpecDateSet entity = (KsmmtComSpecDateSet) object[2];
		CompanySpecificDateItem domain = CompanySpecificDateItem.createFromJavaType(
				entity.ksmmtComSpecDateSetPK.companyId, entity.ksmmtComSpecDateSetPK.specificDate,
				entity.ksmmtComSpecDateSetPK.specificDateItemNo, specificDateItemName);
		return domain;
	}
	
	/**
	 * convert domain to entity NO with name
	 * @param domain
	 * @return
	 */
	private static KsmmtComSpecDateSet toEntity(CompanySpecificDateItem domain){
		val entity = new KsmmtComSpecDateSet();
		entity.ksmmtComSpecDateSetPK = new KsmmtComSpecDateSetPK(
				domain.getCompanyId(),
				domain.getSpecificDate(),
				domain.getSpecificDateItemNo().v());
		return entity;
	}
	/**
	 * INSERT process
	 */
	@Override
	public void InsertComSpecDate(List<CompanySpecificDateItem> lstComSpecDateItem) {
		List<KsmmtComSpecDateSet> lstEntity = new ArrayList<>();
		for(CompanySpecificDateItem comSpecDateItem : lstComSpecDateItem){
			lstEntity.add(toEntity(comSpecDateItem));
		}
		this.commandProxy().insertAll(lstEntity);
	}
	/**
	 * DELETE process
	 */
	@Override
	public void DeleteComSpecDate(String companyId, GeneralDate startDate, GeneralDate endDate) {
		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
			.setParameter("companyId", companyId)
			.setParameter("startYm", startDate)
			.setParameter("endYm", endDate)
			.executeUpdate();
	}
	/**
	 * add List ComSpecDate
	 * @param lstComSpecDateItem
	 */
	@Override
	public void addListComSpecDate(List<CompanySpecificDateItem> lstComSpecDateItem) {
		List<KsmmtComSpecDateSet> lstEntity = new ArrayList<>();
		for (CompanySpecificDateItem specificDateItem : lstComSpecDateItem) {
			lstEntity.add(toEntity(specificDateItem));
		}
		this.commandProxy().insertAll(lstEntity);
	}
	/**
	 * delete ComSpecByDate
	 * @param companyId
	 * @param specificDate
	 */
	@Override
	public void deleteComSpecByDate(String companyId, GeneralDate specificDate) {
		this.getEntityManager().createQuery(DELETE_BY_DATE)
		.setParameter("companyId", companyId)
		.setParameter("specificDate", specificDate)
		.executeUpdate();
	}
}
