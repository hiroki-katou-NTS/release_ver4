package nts.uk.ctx.pr.core.infra.repository.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttendRespository;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemAttend;
import nts.uk.ctx.pr.core.infra.entity.itemmaster.QcamtItemAttendPK;

@Stateless
@Transactional
public class JpaItemAttendRepository extends JpaRepository implements ItemAttendRespository {
	private final String SEL = "SELECT c FROM QcamtItemAttend c";
	private final String SEL_1 = SEL + " WHERE c.qcamtItemAttendPK.ccd = :companyCode";
	private final String SEL_4 = SEL + " WHERE c.qcamtItemAttendPK.ccd = :companyCode AND c.avePayAtr = :avePayAtr ";
	private final String UPD_2 = "UPDATE QcamtItemAttend c SET c.avePayAtr = :avePayAtr WHERE c.qcamtItemAttendPK.ccd = :companyCode AND c.qcamtItemAttendPK.itemCd IN :itemCodeList";


	@Override
	public Optional<ItemAttend> find(String companyCode, String itemCode) {
		return this.queryProxy().find(new QcamtItemAttendPK(companyCode, itemCode), QcamtItemAttend.class)
				.map(x -> toDomain(x));
	}

	@Override
	public List<ItemAttend> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QcamtItemAttend.class).setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<ItemAttend> findAll(String companyCode, AvePayAtr avePayAtr) {
		return this.queryProxy().query(SEL_4, QcamtItemAttend.class)
				.setParameter("companyCode", companyCode)
				.setParameter("avePayAtr", avePayAtr.value)
				.getList(c -> toDomain(c));
	}
	
	@Override
	public void update(String companyCode, ItemAttend item) {
		this.commandProxy().update(toEntity(companyCode, item));
	}

	@Override
	public void updateItems(String companyCode, List<String> itemCodeList, AvePayAtr avePayAtr) {
		this.getEntityManager().createQuery(UPD_2).setParameter("companyCode", companyCode)
				.setParameter("itemCodeList", itemCodeList).setParameter("avePayAtr", avePayAtr.value).executeUpdate();
	}

	@Override
	public void add(String companyCode, ItemAttend itemAttend) {
		this.commandProxy().insert(toEntity(companyCode,itemAttend));

	}

	@Override
	public void delete(String companyCode, String itemCode) {
		this.commandProxy().remove(QcamtItemAttend.class, new QcamtItemAttendPK(companyCode, itemCode));

	}

	/**
	 * Convert entity to domain
	 * 
	 * @param entity
	 *            QcamtItemAttend
	 * @return ItemAttend
	 */
	private ItemAttend toDomain(QcamtItemAttend entity) {
		val domain = ItemAttend.createFromJavaType(entity.qcamtItemAttendPK.itemCd, entity.avePayAtr, entity.itemAtr,
				entity.errRangeLowAtr, entity.errRangeLow, entity.errRangeHighAtr, entity.errRangeHigh,
				entity.alRangeLowAtr, entity.alRangeLow, entity.alRangeHighAtr, entity.alRangeHigh,
				entity.workDaysScopeAtr, entity.memo);
		// TODO Auto-generated method stub
		return domain;
	}

	/**
	 * Convert domain to entity object
	 * 
	 * @param companyCode
	 *            company code
	 * @param domain
	 *            domain object
	 * @return QcamtItemAttend
	 */
	private QcamtItemAttend toEntity(String companyCode, ItemAttend domain) {
		return new QcamtItemAttend(new QcamtItemAttendPK(companyCode, domain.getItemCode().v()),
				domain.getAvePayAtr().value, domain.getItemAtr().value, domain.getErrRangeLowAtr().value,
				domain.getErrRangeLow().v(), domain.getErrRangeHighAtr().value, domain.getErrRangeHigh().v(),
				domain.getAlRangeLowAtr().value, domain.getAlRangeLow().v(), domain.getAlRangeHighAtr().value,
				domain.getAlRangeHigh().v(), domain.getWorkDaysScopeAtr().value, domain.getMemo().v());
	}
}
