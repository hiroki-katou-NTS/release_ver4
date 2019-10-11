package nts.uk.ctx.at.shared.infra.repository.calculation.holiday;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.InsufficientFlexHolidayMnt;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.InsufficientFlexHolidayMntRepository;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstInsuffFlexMnt;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstInsuffFlexMntPK;

/**
 * Implementation of insufficient flex holiday time management repository
 * @author HoangNDH
 *
 */
@Stateless
public class JpaInsufficientFlexHolidayMntRepository extends JpaRepository implements InsufficientFlexHolidayMntRepository {
	private static final String SELECT_BY_CID;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstInsuffFlexMnt e");
		builderString.append(" WHERE e.kshstInsuffFlexMntPK.companyId = :companyId");
		SELECT_BY_CID = builderString.toString();
	}
	
	/**
	 * Convert from db type to domain type
	 * @param kshstInsuffFlexMnt
	 * @return
	 */
	private InsufficientFlexHolidayMnt convertToDomain(KshstInsuffFlexMnt kshstInsuffFlexMnt) {
		InsufficientFlexHolidayMnt insufficientFlexHolidayMnt = 
				InsufficientFlexHolidayMnt.createFromJavaType(kshstInsuffFlexMnt.kshstInsuffFlexMntPK.companyId, kshstInsuffFlexMnt.getSupplementableDays());
		return insufficientFlexHolidayMnt;
	}
	
	/**
	 * Convert from domain type to db type
	 * @param insufficientFlexHolidayMnt
	 * @return
	 */
	private KshstInsuffFlexMnt convertToDBType(InsufficientFlexHolidayMnt insufficientFlexHolidayMnt) {
		KshstInsuffFlexMnt kshstInsuffFlexMnt = new KshstInsuffFlexMnt();
		KshstInsuffFlexMntPK kshstInsuffFlexMntPK = new KshstInsuffFlexMntPK(insufficientFlexHolidayMnt.getCompanyId());
		kshstInsuffFlexMnt.setKshstInsuffFlexMntPK(kshstInsuffFlexMntPK);
		kshstInsuffFlexMnt.setSupplementableDays(insufficientFlexHolidayMnt.getSupplementableDays().v());
		return kshstInsuffFlexMnt;
	}
	
	/**
	 * Add Insufficient Flex Holiday
	 * @param refreshInsuffFlex 
	 */
	@Override
	public void add(InsufficientFlexHolidayMnt insufficientFlexHolidayMnt) {
		this.commandProxy().insert(convertToDBType(insufficientFlexHolidayMnt));
	}

	/**
	 * Update Insufficient Flex Holiday
	 * @param refreshInsuffFlex
	 */
	@Override
	public void update(InsufficientFlexHolidayMnt refreshInsuffFlex) {
		KshstInsuffFlexMnt entity = new KshstInsuffFlexMnt();
		KshstInsuffFlexMntPK primaryKey = new KshstInsuffFlexMntPK(refreshInsuffFlex.getCompanyId());
		entity.setKshstInsuffFlexMntPK(primaryKey);
		entity.setSupplementableDays(refreshInsuffFlex.getSupplementableDays().v());
		this.commandProxy().update(entity);
	}

	/**
	 * Find by CID
	 * @param companyId
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<InsufficientFlexHolidayMnt> findByCId(String cid) {
		return this.queryProxy().find(new KshstInsuffFlexMntPK(cid),KshstInsuffFlexMnt.class)
				.map(c->convertToDomain(c));
	}

	/**
	 * Find by company ID
	 * @param companyId
	 * @return
	 */
	@Override
	public List<InsufficientFlexHolidayMnt> findByCompanyId(String companyId) {
		return this.queryProxy().query(SELECT_BY_CID, KshstInsuffFlexMnt.class).setParameter("companyId", companyId)
				.getList(c -> convertToDomain(c));
	}

}
