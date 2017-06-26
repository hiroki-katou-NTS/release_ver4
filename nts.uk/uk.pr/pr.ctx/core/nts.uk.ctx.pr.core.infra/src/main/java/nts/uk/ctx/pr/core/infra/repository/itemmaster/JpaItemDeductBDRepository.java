package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBD;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd.ItemDeductBDRepository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemDeductBd;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemDeductBdPK;

@Stateless
public class JpaItemDeductBDRepository extends JpaRepository implements ItemDeductBDRepository {
	private final String SEL = "SELECT c FROM QcamtItemDeductBd c";
	private final String SEL_1 = SEL
			+ " WHERE c.qcamtItemDeductBdPK.ccd = :companyCode AND c.qcamtItemDeductBdPK.itemCd = :itemCode";

	@Override
	public List<ItemDeductBD> findAll(String companyCode, String itemCode) {
		return this.queryProxy().query(SEL_1, QcamtItemDeductBd.class).setParameter("companyCode", companyCode)
				.setParameter("itemCode", itemCode).getList(c -> toDomain(c));

	}
	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return ItemDeductBD
	 */
	private ItemDeductBD toDomain(QcamtItemDeductBd entity) {
		val domain = ItemDeductBD.createFromJavaType(entity.qcamtItemDeductBdPK.itemCd,
				entity.qcamtItemDeductBdPK.itemBreakdownCd, entity.itemBreakdownName, entity.itemBreakdownAbName,
				entity.uniteCd, entity.zeroDispSet, entity.itemDispAtr, entity.errRangeLowAtr, entity.errRangeLow,
				entity.errRangeHighAtr, entity.errRangeHigh, entity.alRangeLowAtr, entity.alRangeLow,
				entity.alRangeHighAtr, entity.alRangeHigh);
		return domain;
	}

	@Override
	public Optional<ItemDeductBD> find(String companyCode, String itemCode, String itemBreakdownCode) {
		QcamtItemDeductBdPK pk = new QcamtItemDeductBdPK(companyCode, itemCode, itemBreakdownCode);
		return this.queryProxy().find(pk, QcamtItemDeductBd.class).map(x -> toDomain(x));
	}

	@Override
	public void add(String companyCode, ItemDeductBD itemDeductBD) {
		this.commandProxy().insert(toEntity(companyCode,itemDeductBD));
	}
	/**
	 * Convert to Entity
	 * 
	 * @param domain
	 * @return QcamtItemDeductBd
	 */
	private QcamtItemDeductBd toEntity(String companyCode, ItemDeductBD domain) {
		QcamtItemDeductBdPK pk = new QcamtItemDeductBdPK(companyCode, domain.getItemCode().v(),
				domain.getItemBreakdownCode().v());
		return new QcamtItemDeductBd(pk, domain.getItemBreakdownName().v(), domain.getItemBreakdownAbName().v(),
				domain.getUniteCode().v(), domain.getZeroDispSet().value, domain.getItemDispAtr().value,
				domain.getErrRangeLowAtr().value, domain.getErrRangeLow().v(), domain.getErrRangeHighAtr().value,
				domain.getErrRangeHigh().v(), domain.getAlRangeLowAtr().value, domain.getAlRangeLow().v(),
				domain.getAlRangeHighAtr().value, domain.getAlRangeHigh().v());
	}

	@Override
	public void delete(String companyCode, String itemCode, String itemBreakdownCode) {
		QcamtItemDeductBdPK pk = new QcamtItemDeductBdPK(companyCode, itemCode, itemBreakdownCode);
		this.commandProxy().remove(QcamtItemDeductBd.class, pk);

	}

	@Override
	public void update(String companyCode, ItemDeductBD itemDeductBD) {
		this.commandProxy().update(toEntity(companyCode, itemDeductBD));

	}

}
