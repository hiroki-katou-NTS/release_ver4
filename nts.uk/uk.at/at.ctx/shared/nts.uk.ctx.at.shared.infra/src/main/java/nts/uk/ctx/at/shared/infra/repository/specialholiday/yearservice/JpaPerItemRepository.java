package nts.uk.ctx.at.shared.infra.repository.specialholiday.yearservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.yearserviceper.YearServicePer;
import nts.uk.ctx.at.shared.dom.specialholiday.yearserviceper.YearServicePerSet;
import nts.uk.ctx.at.shared.dom.specialholiday.yearserviceper.primitives.YearServiceCode;
import nts.uk.ctx.at.shared.dom.specialholiday.yearserviceper.repository.YearServicePerRepository;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearserviceper.KshstYearServicePer;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearserviceper.KshstYearServicePerPK;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearserviceper.KshstYearServicePerSet;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearserviceper.KshstYearServicePerSetPK;
@Stateless
public class JpaPerItemRepository extends JpaRepository implements YearServicePerRepository{
	private final String SELECT_NO_WHERE = "SELECT c FROM KshstYearServicePerSet c ";
	
	private final String SELECT_ITEM = SELECT_NO_WHERE + "WHERE c.kshstYearServicePerSetPK.companyId = :companyId ";
	
	private final String SELECT_YEAR = SELECT_ITEM + " AND c.year = :year";
	
	private final String SELECT_CODE = SELECT_ITEM + " AND c.kshstYearServicePerSetPK.specialHolidayCode = :specialHolidayCode";
	
	private final String SELECT_CODE_SET = SELECT_CODE + " AND c.kshstYearServicePerSetPK.yearServiceCode = :yearServiceCode";
	
	private final String SELECT_NO_WHERE_PER = "SELECT c FROM KshstYearServicePer c ";
	
	private final String SELECT_ITEM_PER = SELECT_NO_WHERE_PER + "WHERE c.kshstYearServicePerPK.companyId = :companyId";
	/**
	 * change entity to domain
	 * @param entity
	 * @return
	 * author: Hoang Yen
	 */
	private static YearServicePerSet toDomainPerSet(KshstYearServicePerSet entity){
		YearServicePerSet domain  = YearServicePerSet.createFromJavaType(entity.kshstYearServicePerSetPK.companyId,
				entity.kshstYearServicePerSetPK.specialHolidayCode,
				entity.kshstYearServicePerSetPK.yearServiceCode,
				entity.kshstYearServicePerSetPK.yearServiceNo,
				entity.year,
				entity.month, 
				entity.date);
		return domain;
	}
	/**
	 * change from domain to entity
	 * @param domain
	 * @return
	 */
	private static KshstYearServicePerSet toEntityPerSet(YearServicePerSet domain){
		val entity = new KshstYearServicePerSet();
		entity.kshstYearServicePerSetPK = new KshstYearServicePerSetPK(domain.getCompanyId(), 
																		domain.getSpecialHolidayCode(), 
																		domain.getYearServiceCode(),
																		domain.getYearServiceNo());
		entity.year = domain.getYear();
		entity.month = domain.getMonth();
		entity.date = domain.getDate();
		return entity;
	}
	private static YearServicePer toDomainPer(KshstYearServicePer entity){
		List<KshstYearServicePerSet> ls = entity.listYearServicePerSet;
		List<YearServicePerSet> domls = new ArrayList<>();
		for(KshstYearServicePerSet item : ls){
			domls.add(toDomainPerSet(item));
		}
		YearServicePer domain = YearServicePer.createFromJavaType(entity.kshstYearServicePerPK.companyId,	
																	entity.kshstYearServicePerPK.specialHolidayCode,
																	entity.kshstYearServicePerPK.yearServiceCode.v(),
																	entity.yearServiceName,
																	entity.yearServiceCls, 
																	domls);
		return domain;
	}
	private static KshstYearServicePer toEntityPer(YearServicePer domain){
		val entity = new KshstYearServicePer();
		entity.kshstYearServicePerPK = new KshstYearServicePerPK(domain.getCompanyId(), 
																domain.getSpecialHolidayCode(), 
																domain.getYearServiceCode());
		entity.yearServiceName = domain.getYearServiceName().v();
		entity.yearServiceCls = domain.getYearServiceCls().value;
		if(domain.getYearServicePerSets() != null){
			entity.listYearServicePerSet = domain.getYearServicePerSets().stream()
										.map(x->toEntityPerSet(x))
										.collect(Collectors.toList());
		}
		return entity;
	}
	@Override
	public List<YearServicePerSet> findAllPerSet(String companyId) {
		return this.queryProxy().query(SELECT_ITEM, KshstYearServicePerSet.class).setParameter("companyId", companyId).getList(c->toDomainPerSet(c));
	}
	@Override
	public void updatePerSet(List<YearServicePerSet> yearServicePerSetLs) {
		for(YearServicePerSet item : yearServicePerSetLs){
			KshstYearServicePerSet entity = toEntityPerSet(item);
			KshstYearServicePerSet oldEntity = this.queryProxy().find(entity.kshstYearServicePerSetPK, KshstYearServicePerSet.class).get();
			oldEntity.year = entity.year;
			oldEntity.month = entity.month;
			oldEntity.date = entity.date;
			this.commandProxy().update(oldEntity);
		}
	}
	@Override
	public void insertPerSet(List<YearServicePerSet> yearServicePerSetLs) {
		for(YearServicePerSet item : yearServicePerSetLs){
			KshstYearServicePerSet entity = toEntityPerSet(item);
			this.commandProxy().insert(entity);
		}
	}
	@Override
	public List<YearServicePerSet> findPerSet(String companyId, String specialHolidayCode, String yearServiceCode) {
		return this.queryProxy().query(SELECT_CODE_SET, KshstYearServicePerSet.class)
				.setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode)
				.setParameter("yearServiceCode", yearServiceCode)
				.getList(c -> toDomainPerSet(c));
	}
	@Override
	public List<YearServicePerSet> findYearPerSet(String companyId, String yearServiceCode, Integer year) {
		return this.queryProxy().query(SELECT_YEAR, KshstYearServicePerSet.class)
				.setParameter("companyId", companyId)
				.setParameter("yearServiceCode", yearServiceCode)
				.setParameter("year", year)
				.getList(c->toDomainPerSet(c));
	}
	@Override
	public List<YearServicePer> findAllPer(String companyId) {
		return this.queryProxy().query(SELECT_ITEM_PER, KshstYearServicePer.class).setParameter("companyId", companyId).getList(c->toDomainPer(c));
	}
	@Override
	public void updatePer(YearServicePer yearServicePer) {
			KshstYearServicePer entity = toEntityPer(yearServicePer);
			KshstYearServicePer oldEntity = this.queryProxy().find(entity.kshstYearServicePerPK, KshstYearServicePer.class).get();
			oldEntity.yearServiceName = entity.yearServiceName;
			oldEntity.yearServiceCls = entity.yearServiceCls;
			if(yearServicePer.getYearServicePerSets() != null){
				oldEntity.listYearServicePerSet =  yearServicePer.getYearServicePerSets().stream()
						.map(x -> toEntityPerSet(x))
						.collect(Collectors.toList());
				
			}
			this.commandProxy().update(oldEntity);
}
	
	@Override
	public void insertPer(YearServicePer yearServicePer) {
			KshstYearServicePer entity = toEntityPer(yearServicePer);
			this.commandProxy().insert(entity);
	}
	@Override
	public Optional<YearServicePer> findPer(String companyId, String specialHolidayCode, String yearServiceCode) {
		return this.queryProxy().find(new KshstYearServicePerPK(companyId, specialHolidayCode, new YearServiceCode(yearServiceCode)), KshstYearServicePer.class)
				.map(c -> toDomainPer(c));
	}
	@Override
	public void delete(String companyId, String specialHolidayCode, String yearServiceCode) {
		KshstYearServicePerPK kshstYearServicePerPK = new KshstYearServicePerPK(companyId, specialHolidayCode, new YearServiceCode(yearServiceCode));
		this.commandProxy().remove(KshstYearServicePer.class, kshstYearServicePerPK);
	}
}
