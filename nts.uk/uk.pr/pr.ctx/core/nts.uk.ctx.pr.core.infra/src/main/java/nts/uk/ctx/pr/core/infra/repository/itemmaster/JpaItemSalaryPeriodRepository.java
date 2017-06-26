package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriod;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriodRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalaryPeriod;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalaryPeriodPK;

@Stateless
public class JpaItemSalaryPeriodRepository extends JpaRepository implements ItemSalaryPeriodRepository {

	@Override
	public Optional<ItemSalaryPeriod> find(String companyCode, String itemCode) {
		QcamtItemSalaryPeriodPK key = new QcamtItemSalaryPeriodPK(companyCode, itemCode);
		return this.queryProxy().find(key, QcamtItemSalaryPeriod.class).map(x -> toDomain(x));
	}

	private ItemSalaryPeriod toDomain(QcamtItemSalaryPeriod entity) {
		val domain = ItemSalaryPeriod.createFromJavaType(entity.qcamtItemSalaryPeriodPK.itemCd, entity.periodAtr,
				entity.strY, entity.endY, entity.cycleAtr, entity.cycle01Atr, entity.cycle02Atr, entity.cycle03Atr,
				entity.cycle04Atr, entity.cycle05Atr, entity.cycle06Atr, entity.cycle07Atr, entity.cycle08Atr,
				entity.cycle09Atr, entity.cycle10Atr, entity.cycle11Atr, entity.cycle12Atr);
		return domain;

	}

	@Override
	public void add(String companyCode, ItemSalaryPeriod domain) {
		this.commandProxy().insert(toEntity(companyCode, domain));
	}

	@Override
	public void update(String companyCode, ItemSalaryPeriod domain) {
		this.commandProxy().update(toEntity(companyCode, domain));

	}

	@Override
	public void delete(String companyCode, String itemCd) {
		QcamtItemSalaryPeriodPK key = new QcamtItemSalaryPeriodPK(companyCode, itemCd);
		this.commandProxy().remove(QcamtItemSalaryPeriod.class, key);

	}

	private QcamtItemSalaryPeriod toEntity(String companyCode, ItemSalaryPeriod domain) {
		QcamtItemSalaryPeriodPK key = new QcamtItemSalaryPeriodPK(companyCode, domain.getItemCode().v());
		return new QcamtItemSalaryPeriod(key, domain.getPeriodAtr().value, domain.getStrY().v(), domain.getEndY().v(),
				domain.getCycleAtr().value, domain.getCycle01Atr().value, domain.getCycle02Atr().value,
				domain.getCycle03Atr().value, domain.getCycle04Atr().value, domain.getCycle05Atr().value,
				domain.getCycle06Atr().value, domain.getCycle07Atr().value, domain.getCycle08Atr().value,
				domain.getCycle09Atr().value, domain.getCycle10Atr().value, domain.getCycle11Atr().value,
				domain.getCycle12Atr().value);
	}

}
