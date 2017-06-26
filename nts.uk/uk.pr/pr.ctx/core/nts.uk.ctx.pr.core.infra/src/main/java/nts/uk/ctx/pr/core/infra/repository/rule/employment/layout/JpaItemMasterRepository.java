package nts.uk.ctx.pr.core.infra.repository.rule.employment.layout;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItem;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemPK;

//@Stateless
public class JpaItemMasterRepository extends JpaRepository {

	private final String SELECT_NO_WHERE = "SELECT c FROM QcamtItem c";
	private final String SELECT_ALL_BY_COMPANY = SELECT_NO_WHERE + " WHERE c.qcamtItemPK.ccd = :companyCode";
	private final String SELECT_ALL_BY_CATEGORY = SELECT_ALL_BY_COMPANY + " AND c.qcamtItemPK.ctgAtr = :categoryType";
	private final String SELECT_DETAIL = SELECT_ALL_BY_CATEGORY + " AND c.qcamtItemPK.itemCd = :itemCode";

	//@Override
	public List<ItemMaster> findAll(String companyCode) {
		return this.queryProxy().query(SELECT_ALL_BY_COMPANY, QcamtItem.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	/**
	 * find all item master by company code, category type
	 */
	//@Override
	public List<ItemMaster> findAllByCategory(String companyCode, int categoryType) {
		return this.queryProxy().query(SELECT_ALL_BY_CATEGORY, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("categoryType", categoryType).getList(c -> toDomain(c));
	}

	private static ItemMaster toDomain(QcamtItem entity) {
//		val domain = ItemMaster.createSimpleFromJavaType(entity.qcamtItemPK.ccd, entity.qcamtItemPK.itemCd,
//				entity.qcamtItemPK.ctgAtr, entity.itemName, entity.itemAbName, entity.taxAtr, entity.itemAtr);
//		domain.additionalInfo(entity.limitMny.intValue(), entity.fixPayAtr, entity.laborInsAtr, entity.socialInsAtr, entity.avePayAtr, entity.deductAtr);
//		domain.additionalErrorAlarm(entity.errRangeHighAtr, entity.errRangeHigh, entity.errRangeLowAtr, entity.errRangeLow, entity.alRangeHighAtr, 
//				entity.alRangeHigh, entity.alRangeLowAtr, entity.alRangeLow);
		return null;
	}

	//@Override
	/**
	 * find item by company code, category type, item code
	 */
	public Optional<ItemMaster> getItemMaster(String companyCode, int categoryType, String itemCode) {
		return this.queryProxy().query(SELECT_DETAIL, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("categoryType", categoryType).setParameter("itemCode", itemCode)
				.getSingle(c -> toDomain(c));
	}

	//@Override
	public Optional<ItemMaster> find(String companyCode, int categoryAtr, String itemCode) {
		return null;
	}

}
