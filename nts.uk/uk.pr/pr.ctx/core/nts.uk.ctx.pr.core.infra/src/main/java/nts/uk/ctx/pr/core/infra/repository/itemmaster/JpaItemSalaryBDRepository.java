package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBD;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBDRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalaryBd;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalaryBdPK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaItemSalaryBDRepository extends JpaRepository implements ItemSalaryBDRepository {

	private final String SEL = "SELECT c FROM QcamtItemSalaryBd c";
	private final String SEL_1 = SEL
			+ " WHERE c.qcamtItemSalaryBdPK.ccd = :companyCode AND c.qcamtItemSalaryBdPK.itemCd = :itemCode";

	@Override
	public List<ItemSalaryBD> findAll(String companyCode, String itemCode) {
		return this.queryProxy().query(SEL_1, QcamtItemSalaryBd.class).setParameter("companyCode", companyCode)
				.setParameter("itemCode", itemCode).getList(c -> toDomain(c));

	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return ItemPeriod
	 */
	private ItemSalaryBD toDomain(QcamtItemSalaryBd entity) {

		val domain = ItemSalaryBD.createFromJavaType(entity.qcamtItemSalaryBdPK.itemCd,
				entity.qcamtItemSalaryBdPK.itemBreakdownCd, entity.itemBreakdownName, entity.itemBreakdownAbName,
				entity.uniteCd, entity.zeroDispSet, entity.itemDispAtr, entity.errRangeLowAtr, entity.errRangeLow,
				entity.errRangeHighAtr, entity.errRangeHigh, entity.alRangeLowAtr, entity.alRangeLow,
				entity.alRangeHighAtr, entity.alRangeHigh);
		return domain;

	}

	@Override
	public void add(String companyCode, ItemSalaryBD itemSalaryBD) {
		this.commandProxy().insert(toEntity(companyCode, itemSalaryBD));

	}

	/**
	 * Convert to entity
	 * 
	 * @param domain
	 * @return QcamtItemSalaryBd
	 */
	private QcamtItemSalaryBd toEntity(String companyCode, ItemSalaryBD domain) {
		String campanyCode = AppContexts.user().companyCode();
		QcamtItemSalaryBdPK pk = new QcamtItemSalaryBdPK(campanyCode, domain.getItemCode().v(),
				domain.getItemBreakdownCode().v());
		return new QcamtItemSalaryBd(pk, domain.getItemBreakdownName().v(), domain.getItemBreakdownAbName().v(),
				domain.getUniteCode().v(), domain.getZeroDispSet().value, domain.getItemDispAtr().value,
				domain.getErrRangeLowAtr().value, domain.getErrRangeLow().v(), domain.getErrRangeHighAtr().value,
				domain.getErrRangeHigh().v(), domain.getAlRangeLowAtr().value, domain.getAlRangeLow().v(),
				domain.getAlRangeHighAtr().value, domain.getAlRangeHigh().v());
	}

	@Override
	public void delete(String companyCode, String itemCode, String itemBreakdownCode) {
		QcamtItemSalaryBdPK pk = new QcamtItemSalaryBdPK(companyCode, itemCode, itemBreakdownCode);
		this.commandProxy().remove(QcamtItemSalaryBd.class, pk);

	}

	@Override
	public Optional<ItemSalaryBD> find(String companyCode, String itemCode, String itemBreakdownCode) {
		QcamtItemSalaryBdPK pk = new QcamtItemSalaryBdPK(companyCode, itemCode, itemBreakdownCode);
		return this.queryProxy().find(pk, QcamtItemSalaryBd.class).map(x -> toDomain(x));
	}

	@Override
	public void update(String companyCode, ItemSalaryBD domain) {
		this.commandProxy().update(toEntity(companyCode, domain));

	}

}
