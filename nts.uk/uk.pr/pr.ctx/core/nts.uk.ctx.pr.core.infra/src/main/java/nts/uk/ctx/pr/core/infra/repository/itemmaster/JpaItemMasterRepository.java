package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItem;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemPK;

@Stateless
public class JpaItemMasterRepository extends JpaRepository implements ItemMasterRepository {
	private final String SEL = "SELECT c FROM QcamtItem c";
	private final String SEL_1 = SEL + " WHERE c.qcamtItemPK.ccd = :companyCode ";
	private final String SEL_1_1 = SEL
			+ " WHERE c.qcamtItemPK.ccd = :companyCode AND c.dispSet =:dispSet AND c.qcamtItemPK.ctgAtr = :ctgAtr";
	private final String SEL_1_2 = SEL + " WHERE c.qcamtItemPK.ccd = :companyCode AND c.dispSet =:dispSet ";
	private final String SEL_3 = SEL + " WHERE c.qcamtItemPK.ccd = :companyCode AND c.avePayAtr = :avePayAtr";
	private final String SEL_3_1 = SEL + " WHERE c.qcamtItemPK.ccd = :companyCode AND c.qcamtItemPK.ctgAtr = :ctgAtr";
	private final String SEL_10 = SEL
			+ " WHERE c.qcamtItemPK.ccd = :companyCode AND c.qcamtItemPK.ctgAtr = :categoryAtr AND c.fixAtr = :fixAtr AND  c.qcamtItemPK.itemCd IN :itemCodeList ";
	private final String SEL_11 = SEL
			+ " WHERE c.qcamtItemPK.ccd = :companyCode AND c.qcamtItemPK.ctgAtr = :categoryAtr AND  c.qcamtItemPK.itemCd IN :itemCodeList ";
	private final String SEL_11_1 = SEL
			+ " WHERE c.qcamtItemPK.ccd = :companyCode AND  c.qcamtItemPK.itemCd IN :itemCodeList ";

	@Override
	public Optional<ItemMaster> find(String companyCode, int categoryAtr, String itemCode) {
		// SEL_2
		return this.queryProxy().find(new QcamtItemPK(companyCode, categoryAtr, itemCode), QcamtItem.class)
				.map(x -> toDomain(x));
	}

	@Override
	public List<ItemMaster> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QcamtItem.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAll(String companyCode, int categoryAtr, List<String> itemCode) {
		return this.queryProxy().query(SEL_11, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("categoryAtr", categoryAtr).setParameter("itemCodeList", itemCode)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAll(String companyCode, List<String> itemCode) {
		return this.queryProxy().query(SEL_11_1, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("itemCodeList", itemCode).getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAll(String companyCode, int avePayAtr) {
		return this.queryProxy().query(SEL_3, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("avePayAtr", avePayAtr).getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAllByDispSetAndCtgAtr(String companyCode, int ctgAtr, int dispSet) {
		return this.queryProxy().query(SEL_1_1, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("ctgAtr", ctgAtr).setParameter("dispSet", dispSet).getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAllByDispSet(String companyCode, int dispSet) {
		return this.queryProxy().query(SEL_1_2, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("dispSet", dispSet).getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAllByCategory(String companyCode, int categoryAtr) {
		return this.queryProxy().query(SEL_3_1, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("ctgAtr", categoryAtr).getList(c -> toDomain(c));
	}

	@Override
	public List<ItemMaster> findAll(String companyCode, int categoryAtr, List<String> itemCode, int fixAtr) {
		return this.queryProxy().query(SEL_10, QcamtItem.class).setParameter("companyCode", companyCode)
				.setParameter("categoryAtr", categoryAtr).setParameter("fixAtr", fixAtr)
				.setParameter("itemCodeList", itemCode).getList(c -> toDomain(c));
	}

	@Override
	public void remove(String companyCode, int categoryAtr, String itemCode) {
		QcamtItemPK key = new QcamtItemPK(companyCode, categoryAtr, itemCode);
		this.commandProxy().remove(QcamtItem.class, key);
	}

	@Override
	public void add(ItemMaster itemMaster) {
		this.commandProxy().insert(toEntity(itemMaster));
	}

	/**
	 * Convert to Entity
	 * 
	 * @param domain
	 * @return QcamtItem
	 */
	private QcamtItem toEntity(ItemMaster domain) {
		return new QcamtItem(
				new QcamtItemPK(domain.getCompanyCode().v(), domain.getCategoryAtr().value, domain.getItemCode().v()),
				domain.getFixAtr().value, domain.getItemName().v(), domain.getItemAbName().v(),
				domain.getItemAbNameE().v(), domain.getItemAbNameO().v(), domain.getDisplaySet().value,
				domain.getUniteCode().v(), domain.getZeroDisplaySet().value, domain.getItemDisplayAtr().value);

	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return ItemMaster
	 */
	private ItemMaster toDomain(QcamtItem entity) {
		val domain = ItemMaster.createFromJavaType(entity.qcamtItemPK.ccd, entity.qcamtItemPK.itemCd, entity.itemName,
				entity.itemAbName, entity.itemAbNameE, entity.itemAbNameO, entity.qcamtItemPK.ctgAtr, entity.fixAtr,
				entity.dispSet, entity.uniteCd, entity.zeroDispSet, entity.itemDispAtr);

		return domain;
	}

	@Override
	public void update(ItemMaster itemMaster) {
		this.commandProxy().update(toEntity(itemMaster));
	}

}
