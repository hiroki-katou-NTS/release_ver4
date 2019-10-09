package nts.uk.ctx.at.schedule.infra.repository.shift.businesscalendar.specificdate;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.item.SpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.repository.SpecificDateItemRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.specificdate.KsmstSpecificDateItem;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.specificdate.KsmstSpecificDateItemPK;

@Stateless
public class JpaSpecificDateItemRepositoryImp extends JpaRepository implements SpecificDateItemRepository {

	private static final String SELECT_NO_WHERE = "SELECT s FROM KsmstSpecificDateItem s";
	private static final String GET_ALL = SELECT_NO_WHERE
			+ " WHERE s.ksmstSpecificDateItemPK.companyId = :companyId";
	private static final String GET_BY_USE = SELECT_NO_WHERE
			+" WHERE s.ksmstSpecificDateItemPK.companyId = :companyId"
			+" AND s.useAtr = :useAtr";
	private static final String GET_BY_LIST_CODE = GET_ALL 
			+" AND s.ksmstSpecificDateItemPK.itemNo IN :lstSpecificDateItem";
	/**
	 * Entity to Domain
	 * 
	 * @param ksmstSpecificDateItem
	 * @return
	 */
	private SpecificDateItem toBonusPaySettingDomain(KsmstSpecificDateItem ksmstSpecificDateItem) {
		return SpecificDateItem.createFromJavaType(ksmstSpecificDateItem.ksmstSpecificDateItemPK.companyId,
				ksmstSpecificDateItem.useAtr,
				ksmstSpecificDateItem.ksmstSpecificDateItemPK.itemNo, ksmstSpecificDateItem.name);
	}

	/**
	 * convert domain SpecificDateItem to entity KsmstSpecificDateItem
	 * @param domain
	 * @return
	 */
	private static KsmstSpecificDateItem toEntity(SpecificDateItem domain) {
		val entity = new KsmstSpecificDateItem();
		entity.ksmstSpecificDateItemPK = new KsmstSpecificDateItemPK(domain.getCompanyId(), domain.getSpecificDateItemNo().v());
		entity.name = domain.getSpecificName().v();
		entity.useAtr = domain.getUseAtr().value;
		return entity;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<SpecificDateItem> getAll(String companyId) {
		return this.queryProxy().query(GET_ALL, KsmstSpecificDateItem.class).setParameter("companyId", companyId)
				.getList(x -> this.toBonusPaySettingDomain(x));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<SpecificDateItem> getSpecifiDateByUse(String companyId, int useAtr) {
		return this.queryProxy().query(GET_BY_USE, KsmstSpecificDateItem.class).setParameter("companyId", companyId)
				.setParameter("useAtr", useAtr).getList(x -> this.toBonusPaySettingDomain(x));
	}

	/**
	 * update list Specific Date Item
	 * @param lstSpecificDateItem
	 */
	@Override
	public void updateSpecificDateItem(List<SpecificDateItem> lstSpecificDateItem) {
		List<KsmstSpecificDateItem> lstEntity = new ArrayList<>();
		for (SpecificDateItem specificDateItem : lstSpecificDateItem) {
			KsmstSpecificDateItem b = toEntity(specificDateItem);
			KsmstSpecificDateItem x = this.queryProxy().find(b.ksmstSpecificDateItemPK, KsmstSpecificDateItem.class).get();
			x.setUseAtr(b.useAtr);
			x.setName(b.name);
			lstEntity.add(x);
		}
		this.commandProxy().updateAll(lstEntity);
	}
	/**
	 * add list Specific Date Item
	 * @param lstSpecificDateItem
	 */
	@Override
	public void addSpecificDateItem(List<SpecificDateItem> lstSpecificDateItem) {
		List<KsmstSpecificDateItem> lstEntity = new ArrayList<>();
		for (SpecificDateItem specificDateItem : lstSpecificDateItem) {
			lstEntity.add(toEntity(specificDateItem));
		}
		this.commandProxy().insertAll(lstEntity);
	}
	/**
	 * get list Specifi Date By List Code
	 * @param companyId
	 * @param lstSpecificDateItem
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<SpecificDateItem> getSpecifiDateByListCode(String companyId, List<Integer> lstSpecificDateItem) {
		List<SpecificDateItem> resultList = new ArrayList<>();
		CollectionUtil.split(lstSpecificDateItem, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(GET_BY_LIST_CODE, KsmstSpecificDateItem.class)
								  .setParameter("companyId", companyId)
								  .setParameter("lstSpecificDateItem", subList)
								  .getList(c->toBonusPaySettingDomain(c)));
		});
		return resultList;
	}

}
