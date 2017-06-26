package nts.uk.ctx.pr.core.infra.repository.layout;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1Repository;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QcamtItem_v1;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QcamtItemPK_v1;

@Stateless
public class JpaItemMasterV1Repository extends JpaRepository implements ItemMasterV1Repository {

	private final String SELECT_NO_WHERE = "SELECT c FROM QcamtItem c";
	private final String SELECT_ALL_BY_COMPANY = SELECT_NO_WHERE + " WHERE c.qcamtItemPK.ccd = :companyCode";
	private final String SELECT_ALL_BY_CATEGORY = SELECT_ALL_BY_COMPANY + " AND c.qcamtItemPK.ctgAtr = :categoryType";
	private final String SELECT_DETAIL = SELECT_ALL_BY_CATEGORY + " AND c.qcamtItemPK.itemCd = :itemCode";
	private final String SEL_3 = SELECT_NO_WHERE + " WHERE c.qcamtItemPK.ccd = :companyCode AND c.avePayAtr = :avePayAtr";

	@Override
	public List<ItemMasterV1> findAll(String companyCode) {
		return this.queryProxy().query(SELECT_ALL_BY_COMPANY, QcamtItem_v1.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	/**
	 * find all item master by company code, category type
	 */
	@Override
	public List<ItemMasterV1> findAllByCategory(String companyCode, int categoryType) {
		return this.queryProxy().query(SELECT_ALL_BY_CATEGORY, QcamtItem_v1.class).setParameter("companyCode", companyCode)
				.setParameter("categoryType", categoryType).getList(c -> toDomain(c));
	}

	private static ItemMasterV1 toDomain(QcamtItem_v1 entity) {
		val domain = ItemMasterV1.createSimpleFromJavaType(entity.qcamtItemPK.ccd, entity.qcamtItemPK.itemCd,
				entity.qcamtItemPK.ctgAtr, entity.itemName, entity.itemAbName, entity.taxAtr, entity.itemAtr);
		domain.additionalInfo(entity.limitMny.intValue(), entity.fixPayAtr, entity.laborInsAtr, entity.socialInsAtr, entity.avePayAtr, entity.deductAtr);
		//domain.additionalErrorAlarm(entity.errRangeHighAtr, entity.errRangeHigh.intValue(), entity.errRangeLowAtr, entity.errRangeLow.intValue(), entity.alRangeHighAtr, entity.alRangeHigh.intValue(), entity.alRangeLowAtr, entity.alRangeLow.intValue());
		return domain;
	}

	@Override
	/**
	 * find item by company code, category type, item code
	 */
	public Optional<ItemMasterV1> getItemMaster(String companyCode, int categoryType, String itemCode) {
		return this.queryProxy().query(SELECT_DETAIL, QcamtItem_v1.class).setParameter("companyCode", companyCode)
				.setParameter("categoryType", categoryType).setParameter("itemCode", itemCode)
				.getSingle(c -> toDomain(c));
	}

	@Override
	public Optional<ItemMasterV1> find(String companyCode, int categoryAtr, String itemCode) {
		return this.queryProxy().find(new QcamtItemPK_v1(companyCode, itemCode, categoryAtr), QcamtItem_v1.class).
				map(entity -> toDomain(entity));
	}

	@Override
	public List<ItemMasterV1> findAll(String companyCode, int avePayAtr) {
		return this.queryProxy().query(SEL_3, QcamtItem_v1.class)
				.setParameter("companyCode", companyCode)
				.setParameter("avePayAtr", avePayAtr)
				.getList(c -> toDomain(c));
	}

	@Override
	public void add(ItemMasterV1 domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ItemMasterV1 domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ItemMasterV1> findAll(String companyCode, int categoryAtr, String itemCode, int fixAtr) {
		// TODO Auto-generated method stub
		return null;
	}
}
