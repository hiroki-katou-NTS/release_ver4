package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalary;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemSalaryPK;

@Stateless
public class JpaItemSalaryRepository extends JpaRepository implements ItemSalaryRespository {
	private final String SEL = "SELECT c FROM QcamtItemSalary c";
	private final String SEL_1 = SEL + " WHERE c.qcamtItemSalaryPK.ccd = :companyCode";
	private final String SEL_3 = SEL + " WHERE c.qcamtItemSalaryPK.ccd = :companyCode AND c.avePayAtr = :avePayAtr";
	private final String UPD_2 = "UPDATE QcamtItemSalary c SET c.avePayAtr = :avePayAtr WHERE  c.qcamtItemSalaryPK.ccd = :companyCode AND c.qcamtItemSalaryPK.itemCd IN :itemCodeList";

	@Override
	public Optional<ItemSalary> find(String companyCode, String itemCode) {
		return this.queryProxy().find(new QcamtItemSalaryPK(companyCode, itemCode), QcamtItemSalary.class)
				.map(x -> toDomain(x));

	}

	@Override
	public List<ItemSalary> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QcamtItemSalary.class).setParameter("companyCode", companyCode)
				.getList(x -> toDomain(x));
	}

	@Override
	public List<ItemSalary> findAll(String companyCode, AvePayAtr avePayAtr) {
		return this.queryProxy().query(SEL_3, QcamtItemSalary.class).setParameter("companyCode", companyCode)
				.setParameter("avePayAtr", avePayAtr.value).getList(c -> toDomain(c));
	}

	@Override
	public void update(String companyCode, ItemSalary item) {
		this.commandProxy().update(toEntity(companyCode, item));
	}

	@Override
	public void updateItems(String companyCode, List<String> itemCodeList, AvePayAtr avePayAtr) {
		this.getEntityManager().createQuery(UPD_2).setParameter("companyCode", companyCode)
				.setParameter("itemCodeList", itemCodeList).setParameter("avePayAtr", avePayAtr.value).executeUpdate();
	}

	@Override
	public void add(String companyCode, ItemSalary item) {
		this.commandProxy().insert(toEntity(companyCode, item));
	}
	
	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return ItemSalary
	 */
	
	private ItemSalary toDomain(QcamtItemSalary entity) {

		val domain = ItemSalary.createFromJavaType(entity.qcamtItemSalaryPK.itemCd, entity.taxAtr, entity.socialInsAtr,
				entity.laborInsAtr, entity.fixPayAtr, entity.applyForAllEmpFlg, entity.applyForMonthlyPayEmp,
				entity.applyForDaymonthlyPayEmp, entity.applyForDaylyPayEmp, entity.applyForHourlyPayEmp,
				entity.avePayAtr, entity.errRangeLowAtr, entity.errRangeLow, entity.errRangeHighAtr,
				entity.errRangeHigh, entity.alRangeLowAtr, entity.alRangeLow, entity.alRangeHighAtr, entity.alRangeHigh,
				entity.memo, entity.limitMnyAtr, entity.limitMnyRefItemCd, entity.limitMny);

		return domain;
	}

	/**
	 * Convert domain object to entity object
	 * 
	 * @param domain
	 *            ItemSalary
	 * @return QcamtItemSalary
	 */
	private QcamtItemSalary toEntity(String companyCode, ItemSalary domain) {
		return new QcamtItemSalary(new QcamtItemSalaryPK(companyCode, domain.getItemCode().v()),
				domain.getTaxAtr().value, domain.getSocialInsAtr().value, domain.getLaborInsAtr().value,
				domain.getFixPayAtr().value, domain.getApplyForAllEmpFlg().value,
				domain.getApplyForMonthlyPayEmp().value, domain.getApplyForDaymonthlyPayEmp().value,
				domain.getApplyForDaylyPayEmp().value, domain.getApplyForHourlyPayEmp().value,
				domain.getAvePayAtr().value, domain.getErrRangeLowAtr().value, domain.getErrRangeLow().v(),
				domain.getErrRangeHighAtr().value, domain.getErrRangeHigh().v(), domain.getAlRangeLowAtr().value,
				domain.getAlRangeLow().v(), domain.getAlRangeHighAtr().value, domain.getAlRangeHigh().v(),
				domain.getMemo().v(), domain.getLimitMnyAtr().value, domain.getLimitMnyRefItemCode().v(),
				domain.getLimitMny().v());
	}

	@Override
	public void delete(String companyCode, String itemCode) {
		this.commandProxy().remove(QcamtItemSalary.class, new QcamtItemSalaryPK(companyCode, itemCode));

	}
}
