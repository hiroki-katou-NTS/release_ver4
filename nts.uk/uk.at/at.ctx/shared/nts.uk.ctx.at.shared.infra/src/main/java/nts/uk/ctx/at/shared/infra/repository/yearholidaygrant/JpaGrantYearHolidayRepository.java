package nts.uk.ctx.at.shared.infra.repository.yearholidaygrant;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.infra.entity.yearholidaygrant.KshstGrantHdTbl;
import nts.uk.ctx.at.shared.infra.entity.yearholidaygrant.KshstGrantHdTblPK;

/**
 * 
 * A - Screen
 * @author TanLV
 *
 */
@Stateless
public class JpaGrantYearHolidayRepository extends JpaRepository implements GrantYearHolidayRepository {
	
	private static final String FIND_BY_CODE = "SELECT a FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.conditionNo = :conditionNo "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ORDER BY a.kshstGrantHdTblPK.grantNum ";
	
	private static final String DELETE_ALL_BY_CONDITION = "DELETE FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.conditionNo = :conditionNo "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	private static final String DELETE_ALL_BY_SPHD = "DELETE FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	private static final String DELETE_ALL_BY_CONDITIONS = "DELETE FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.conditionNo IN :conditionNos "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	private static final String DELETE_BY_GRANTNUMS = "DELETE FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.grantNum IN :grantNums "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<GrantHdTbl> find(String companyId, int conditionNo, 
			String yearHolidayCode, int grantNum) {
		return this.queryProxy().find(new KshstGrantHdTblPK(companyId, grantNum, conditionNo, yearHolidayCode), KshstGrantHdTbl.class)
					.map(x -> convertToDomain(x));
	}
	
	@Override
	public List<GrantHdTbl> findByCode(String companyId, int conditionNo, String yearHolidayCode) {
		return this.queryProxy().query(FIND_BY_CODE, KshstGrantHdTbl.class)
				.setParameter("companyId", companyId)
				.setParameter("conditionNo", conditionNo)
				.setParameter("yearHolidayCode", yearHolidayCode)
				.getList(x -> convertToDomain(x));
	}	

	@Override
	public void add(GrantHdTbl holidayGrant) {
		this.commandProxy().insert(toEntity(holidayGrant, null));
	}

	@Override
	public void update(GrantHdTbl holidayGrant) {
		KshstGrantHdTblPK key = new KshstGrantHdTblPK(holidayGrant.getCompanyId(), holidayGrant.getGrantNum().v(), holidayGrant.getConditionNo(), holidayGrant.getYearHolidayCode().v());
		Optional<KshstGrantHdTbl> entity = this.queryProxy().find(key, KshstGrantHdTbl.class);
		this.commandProxy().update(toEntity(holidayGrant, entity.get()));
	}

	@Override
	public void remove(String companyId, int grantNum, int conditionNo, String yearHolidayCode) {
		this.commandProxy().remove(KshstGrantHdTbl.class, new KshstGrantHdTblPK(companyId, grantNum, conditionNo, yearHolidayCode));
	}
	
	@Override
	public void remove(String companyId, int conditionNo, String yearHolidayCode) {
		this.getEntityManager().createQuery(DELETE_ALL_BY_CONDITION)
			.setParameter("companyId", companyId)
			.setParameter("conditionNo", conditionNo)
			.setParameter("yearHolidayCode", yearHolidayCode)
			.executeUpdate();
	}
	
	@Override
	public void remove(String companyId, String yearHolidayCode) {
		this.getEntityManager().createQuery(DELETE_ALL_BY_SPHD)
			.setParameter("companyId", companyId)
			.setParameter("yearHolidayCode", yearHolidayCode)
			.executeUpdate();
	}
	
	@Override
	public void remove(String companyId, String yearHolidayCode, List<Integer> conditionNos) {
		CollectionUtil.split(conditionNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			this.getEntityManager().createQuery(DELETE_ALL_BY_CONDITIONS)
				.setParameter("companyId", companyId)
				.setParameter("yearHolidayCode", yearHolidayCode)
				.setParameter("conditionNos", subList)
				.executeUpdate();
		});
		this.getEntityManager().flush();
	}
		
	/**
	 * Convert from KshstGrantHdTbl entity to domain
	 * @param KshstGrantHdTbl
	 * @return
	 * @author yennth
	 */
	private GrantHdTbl convertToDomain(KshstGrantHdTbl x) {
		return GrantHdTbl.createFromJavaType(x.kshstGrantHdTblPK.companyId, x.kshstGrantHdTblPK.conditionNo, 
											x.kshstGrantHdTblPK.yearHolidayCode, x.kshstGrantHdTblPK.grantNum, 
											x.grantDay, x.limitTimeHd, x.limitDayYear);
	}
	
	/**
	 * Convert to entity
	 * 
	 * @param holidayGrant
	 * @return
	 */
	private KshstGrantHdTbl toEntity(GrantHdTbl holidayGrant, KshstGrantHdTbl kshstGrantHdTbl) {
		if (kshstGrantHdTbl == null) {
			kshstGrantHdTbl = new KshstGrantHdTbl();
		}
		kshstGrantHdTbl.kshstGrantHdTblPK  =  new KshstGrantHdTblPK(holidayGrant.getCompanyId(), holidayGrant.getGrantNum().v(), holidayGrant.getConditionNo(), holidayGrant.getYearHolidayCode().v());
		kshstGrantHdTbl.grantDay =	holidayGrant.getGrantDays().v();
		
		if(holidayGrant.getLimitTimeHd() != null) {
			if(holidayGrant.getLimitTimeHd().isPresent()) {
				kshstGrantHdTbl.limitTimeHd = holidayGrant.getLimitTimeHd().get().v();
			} else {
				kshstGrantHdTbl.limitTimeHd = 0;
			}			
		} else {
			kshstGrantHdTbl.limitTimeHd = 0;
		}
		
		if(holidayGrant.getLimitDayYear() != null) {
			if(holidayGrant.getLimitDayYear().isPresent()) {
				kshstGrantHdTbl.limitDayYear = holidayGrant.getLimitDayYear().get().v();
			} else {
				kshstGrantHdTbl.limitDayYear = 0;
			}			
		} else {
			kshstGrantHdTbl.limitDayYear = 0;
		}
		
		return kshstGrantHdTbl;
	}

	@Override
	public void removeByGrantNums(String companyId, String yearHolidayCode, List<Integer> grantNums) {
		CollectionUtil.split(grantNums, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			this.getEntityManager().createQuery(DELETE_BY_GRANTNUMS)
				.setParameter("companyId", companyId)
				.setParameter("grantNums", subList)
				.setParameter("yearHolidayCode", yearHolidayCode)			
				.executeUpdate();
		});
		this.getEntityManager().flush();
	}

}
