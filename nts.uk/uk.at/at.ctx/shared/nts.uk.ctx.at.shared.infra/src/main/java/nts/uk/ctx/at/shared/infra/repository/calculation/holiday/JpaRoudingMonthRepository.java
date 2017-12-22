package nts.uk.ctx.at.shared.infra.repository.calculation.holiday;
/**
 * @author phongtq
 */
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.roundingmonth.RoundingMonth;
import nts.uk.ctx.at.shared.dom.calculation.holiday.roundingmonth.RoundingMonthRepository;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstRoundingMonthItem;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstRoundingMonthItemPK;

@Stateless
public class JpaRoudingMonthRepository extends JpaRepository implements RoundingMonthRepository{
	private static final String SELECT_BY_CID;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstRoundingMonthItem e");
		builderString.append(" WHERE e.kshstRoundingMonthSetPK.companyId = :companyId");
		SELECT_BY_CID = builderString.toString();
	}

	/**
	 * convert To Domain Rounding Month
	 * @param monthSet
	 * @return
	 */
	private RoundingMonth convertToDomain(KshstRoundingMonthItem monthSet) {
		RoundingMonth month = RoundingMonth.createFromJavaType(monthSet.kshstRoundingMonthSetPK.companyId, monthSet.kshstRoundingMonthSetPK.timeItemId, monthSet.unit, monthSet.rounding);
		
		return month;
	}
	
	/**
	 * convert To Db Type KshstRoundingMonthItem
	 * @param month
	 * @return
	 */
	private KshstRoundingMonthItem convertToDbType(RoundingMonth month) {
		KshstRoundingMonthItem monthSet = new KshstRoundingMonthItem();
		KshstRoundingMonthItemPK monthSetPK = new KshstRoundingMonthItemPK(month.getCompanyId(),month.getTimeItemId().v());
				monthSet.unit = month.getUnit().value;
				monthSet.rounding = month.getRounding().value;
				monthSet.kshstRoundingMonthSetPK = monthSetPK;
		return monthSet;
	}
	
	/**
	 * find By CID
	 */
	@Override
	public List<RoundingMonth> findByCompanyId(String companyId, String itemTimeId) {
		return this.queryProxy().query(SELECT_BY_CID, KshstRoundingMonthItem.class).setParameter("companyId", companyId)
				.getList(c -> convertToDomain(c));
	}
	
	/**
	 * Add Rounding Month
	 */
	@Override
	public void add(RoundingMonth month) {
		this.commandProxy().insert(convertToDbType(month));
	}
	
	/**
	 * Update Rounding Month
	 */
	@Override
	public void update(RoundingMonth month) {
		KshstRoundingMonthItemPK primaryKey = new KshstRoundingMonthItemPK(month.getCompanyId(), month.getTimeItemId().v());
		KshstRoundingMonthItem entity = this.queryProxy().find(primaryKey, KshstRoundingMonthItem.class).get();
				entity.unit = month.getUnit().value;
				entity.rounding = month.getRounding().value;
				
				entity.kshstRoundingMonthSetPK = primaryKey;
		this.commandProxy().update(entity);
	}
	
	@Override
	public Optional<RoundingMonth> findByCId(String companyId, String timeItemId) {
		return this.queryProxy().find(new KshstRoundingMonthItemPK(companyId,timeItemId),KshstRoundingMonthItem.class)
				.map(c->convertToDomain(c));
	}

}
