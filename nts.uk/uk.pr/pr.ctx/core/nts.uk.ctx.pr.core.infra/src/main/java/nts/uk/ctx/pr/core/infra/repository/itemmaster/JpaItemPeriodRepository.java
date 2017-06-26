package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.ItemPeriod;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.ItemPeriodRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemPeriod;

@Stateless
public class JpaItemPeriodRepository extends JpaRepository implements ItemPeriodRepository {
	private final String SEL = "SELECT c FROM QcamtItemPeriod c";
	private final String DEL_1 = SEL
			+ " WHERE c.qcamtItemPeriodPK.ccd = :companyCode AND c.qcamtItemPeriodPK.ctgAtr = :ctgAtr AND c.qcamtItemPeriodPK.itemCd = :itemCd";

	@Override
	public Optional<ItemPeriod> find(String companyCode, int ctgAtr, String itemCd) {
		return this.queryProxy().query(DEL_1, QcamtItemPeriod.class).setParameter("companyCode", companyCode)
				.setParameter("ctgAtr", ctgAtr).setParameter("itemCd", itemCd).getSingle(c -> toDomain(c));
	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return ItemPeriod
	 */
	private ItemPeriod toDomain(QcamtItemPeriod entity) {
		val domain = ItemPeriod.createFromJavaType(entity.qcamtItemPeriodPK.ccd, entity.qcamtItemPeriodPK.ctgAtr,
				entity.qcamtItemPeriodPK.itemCd, entity.periodAtr, entity.strY, entity.endY, entity.cycleAtr,
				entity.cycle01Atr, entity.cycle02Atr, entity.cycle03Atr, entity.cycle04Atr, entity.cycle05Atr,
				entity.cycle06Atr, entity.cycle07Atr, entity.cycle08Atr, entity.cycle09Atr, entity.cycle10Atr,
				entity.cycle11Atr, entity.cycle12Atr);

		return domain;
	}
}
