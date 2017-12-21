package nts.uk.ctx.at.record.infra.repository.bonuspay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.infra.entity.bonuspay.KbpstBonusPayTimeItem;
import nts.uk.ctx.at.record.infra.entity.bonuspay.KbpstBonusPayTimeItemPK;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;

@Stateless
public class JpaBPTimeItemRepository extends JpaRepository implements BPTimeItemRepository {
	private final String CHECK_INIT = "SELECT count(c) " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId " + " AND  c.useAtr = 1 ";
	private final String SELECT_BPTIMEITEM_BY_COMPANYID = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 0 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo  ASC ";
	private final String SELECT_BPTIMEITEM_INUSE_BY_COMPANYID = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 0 " + " AND c.useAtr = 1 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo  ASC ";
	private final String SELECT_SPEC_BPTIMEITEM_BY_COMPANYID = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 1 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo  ASC ";
	private final String SELECT_SPEC_BPTIMEITEM_INUSE_BY_COMPANYID = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 1 " + " AND c.useAtr = 1 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo ASC ";
	private final String SELECT_BPTIMEITEM_BY_KEY = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemNo = :timeItemNo "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 0 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo ASC ";
	private final String SELECT_SPEC_BPTIMEITEM_BY_KEY = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemNo = :timeItemNo "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 1 "
			+ " ORDER BY c.kbpstBonusPayTimeItemPK.timeItemNo ASC ";
	private final String SELECT_BP_TIME_ITEM_NAME = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId"
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemNo IN :timeItemNos "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 0 ";
	private final String SELECT_SPEC_BP_TIME_ITEM_NAME = "SELECT c " + " FROM KbpstBonusPayTimeItem c "
			+ " WHERE c.kbpstBonusPayTimeItemPK.companyId = :companyId"
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemNo IN :timeItemNos "
			+ " AND c.kbpstBonusPayTimeItemPK.timeItemTypeAtr = 1 ";

	@Override
	public List<BonusPayTimeItem> getListBonusPayTimeItem(String companyId) {
		return this.queryProxy().query(SELECT_BPTIMEITEM_BY_COMPANYID, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).getList(x -> this.toBonusPayTimeItemDomain(x));
	}

	@Override
	public List<BonusPayTimeItem> getListSpecialBonusPayTimeItem(String companyId) {
		return this.queryProxy().query(SELECT_SPEC_BPTIMEITEM_BY_COMPANYID, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).getList(x -> this.toBonusPayTimeItemDomain(x));
	}

	@Override
	public void addListBonusPayTimeItem(List<BonusPayTimeItem> lstTimeItem) {
		List<KbpstBonusPayTimeItem> lstKbpstBonusPayTimeItem = lstTimeItem.stream()
				.map(c -> toBonusPayTimeItemEntity(c)).collect(Collectors.toList());
		this.commandProxy().insertAll(lstKbpstBonusPayTimeItem);
	}

	@Override
	public void updateListBonusPayTimeItem(List<BonusPayTimeItem> lstTimeItem) {
		lstTimeItem.forEach(c -> {
			Optional<KbpstBonusPayTimeItem> kbpstBonusPayTimeItemOptional = this.queryProxy()
					.find(new KbpstBonusPayTimeItemPK(c.getCompanyId().toString(), new BigDecimal(c.getId()),
							new BigDecimal(c.getTimeItemTypeAtr().value)), KbpstBonusPayTimeItem.class);
			if (kbpstBonusPayTimeItemOptional.isPresent()) {
				KbpstBonusPayTimeItem kbpstBonusPayTimeItem = kbpstBonusPayTimeItemOptional.get();
				kbpstBonusPayTimeItem.timeItemName = c.getTimeItemName().v();
				kbpstBonusPayTimeItem.useAtr = new BigDecimal(c.getUseAtr().value);
				this.commandProxy().update(kbpstBonusPayTimeItem);
			}
		});

	}

	private BonusPayTimeItem toBonusPayTimeItemDomain(KbpstBonusPayTimeItem kbpstBonusPayTimeItem) {

		return BonusPayTimeItem.createFromJavaType(kbpstBonusPayTimeItem.kbpstBonusPayTimeItemPK.getCompanyId(),
				kbpstBonusPayTimeItem.useAtr.intValue(), kbpstBonusPayTimeItem.timeItemName,
				kbpstBonusPayTimeItem.kbpstBonusPayTimeItemPK.timeItemNo.intValue(),
				kbpstBonusPayTimeItem.kbpstBonusPayTimeItemPK.timeItemTypeAtr.intValue());
	}

	private KbpstBonusPayTimeItem toBonusPayTimeItemEntity(BonusPayTimeItem bonusPayTimeItem) {
		return new KbpstBonusPayTimeItem(new KbpstBonusPayTimeItemPK(bonusPayTimeItem.getCompanyId().toString(),
				new BigDecimal(bonusPayTimeItem.getId()), new BigDecimal(bonusPayTimeItem.getTimeItemTypeAtr().value)),
				new BigDecimal(bonusPayTimeItem.getUseAtr().value), bonusPayTimeItem.getTimeItemName().toString());
	}

	@Override
	public Optional<BonusPayTimeItem> getBonusPayTimeItem(String companyId, BigDecimal timeItemNo) {

		Optional<KbpstBonusPayTimeItem> kbpstBonusPayTimeItem = this.queryProxy()
				.query(SELECT_BPTIMEITEM_BY_KEY, KbpstBonusPayTimeItem.class).setParameter("companyId", companyId)
				.setParameter("timeItemNo", timeItemNo).getSingle();
		if (kbpstBonusPayTimeItem.isPresent()) {
			return Optional.ofNullable(this.toBonusPayTimeItemDomain(this.queryProxy()
					.query(SELECT_BPTIMEITEM_BY_KEY, KbpstBonusPayTimeItem.class).setParameter("companyId", companyId)
					.setParameter("timeItemNo", timeItemNo).getSingle().get()));
		}
		return Optional.empty();

	}

	@Override
	public Optional<BonusPayTimeItem> getSpecialBonusPayTimeItem(String companyId, BigDecimal timeItemNo) {

		Optional<KbpstBonusPayTimeItem> KbpstBonusPayTimeItem = this.queryProxy()
				.query(SELECT_SPEC_BPTIMEITEM_BY_KEY, KbpstBonusPayTimeItem.class).setParameter("companyId", companyId)
				.setParameter("timeItemNo", timeItemNo).getSingle();
		if (KbpstBonusPayTimeItem.isPresent()) {
			return Optional.ofNullable(this.toBonusPayTimeItemDomain(this.queryProxy()
					.query(SELECT_SPEC_BPTIMEITEM_BY_KEY, KbpstBonusPayTimeItem.class)
					.setParameter("companyId", companyId).setParameter("timeItemNo", timeItemNo).getSingle().get()));
		}
		return Optional.empty();

	}

	@Override
	public int checkInit(String companyId) {
		return this.queryProxy().query(CHECK_INIT, Long.class).setParameter("companyId", companyId).getSingle().get()
				.intValue();
	}

	@Override
	public List<BonusPayTimeItem> getListBonusPayTimeItemInUse(String companyId) {
		return this.queryProxy().query(SELECT_BPTIMEITEM_INUSE_BY_COMPANYID, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).getList(x -> this.toBonusPayTimeItemDomain(x));
	}

	@Override
	public List<BonusPayTimeItem> getListSpecialBonusPayTimeItemInUse(String companyId) {
		return this.queryProxy().query(SELECT_SPEC_BPTIMEITEM_INUSE_BY_COMPANYID, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).getList(x -> this.toBonusPayTimeItemDomain(x));
	}

	@Override
	public List<BonusPayTimeItem> getListBonusPayTimeItemName(String companyId, List<Integer> timeItemNos) {
		return this.queryProxy().query(SELECT_BP_TIME_ITEM_NAME, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).setParameter("timeItemNos", timeItemNos)
				.getList(f -> toBonusPayTimeItemDomain(f));
	}

	@Override
	public List<BonusPayTimeItem> getListSpecialBonusPayTimeItemName(String companyId, List<Integer> timeItemNos) {
		return this.queryProxy().query(SELECT_SPEC_BP_TIME_ITEM_NAME, KbpstBonusPayTimeItem.class)
				.setParameter("companyId", companyId).setParameter("timeItemNos", timeItemNos)
				.getList(f -> toBonusPayTimeItemDomain(f));
	}

}
