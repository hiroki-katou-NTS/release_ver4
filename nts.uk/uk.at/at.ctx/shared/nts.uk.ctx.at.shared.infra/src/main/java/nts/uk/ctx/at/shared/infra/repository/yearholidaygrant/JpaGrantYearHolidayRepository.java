package nts.uk.ctx.at.shared.infra.repository.yearholidaygrant;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
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
	
	private final String findByCode = "SELECT a FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.conditionNo = :conditionNo "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	private final String DELETE_ALL = "DELETE FROM KshstGrantHdTbl a "
			+ "WHERE a.kshstGrantHdTblPK.companyId = :companyId "
			+ "AND a.kshstGrantHdTblPK.conditionNo = :conditionNo "
			+ "AND a.kshstGrantHdTblPK.yearHolidayCode = :yearHolidayCode ";
	
	@Override
	public Optional<GrantHdTbl> find(String companyId, int conditionNo, String yearHolidayCode,
			int grantYearHolidayNo) {
		return this.queryProxy().find(new KshstGrantHdTblPK(companyId, grantYearHolidayNo, conditionNo, yearHolidayCode), KshstGrantHdTbl.class)
					.map(x -> convertToDomain(x));
	}
	
	@Override
	public List<GrantHdTbl> findByCode(String companyId, int conditionNo, String yearHolidayCode) {
		return this.queryProxy().query(findByCode, KshstGrantHdTbl.class)
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
		KshstGrantHdTblPK key = new KshstGrantHdTblPK(holidayGrant.getCompanyId(), holidayGrant.getGrantYearHolidayNo(), holidayGrant.getConditionNo(), holidayGrant.getYearHolidayCode().v());
		Optional<KshstGrantHdTbl> entity = this.queryProxy().find(key, KshstGrantHdTbl.class);
		this.commandProxy().update(toEntity(holidayGrant, entity.get()));
	}

	@Override
	public void remove(String companyId, int grantYearHolidayNo, int conditionNo, String yearHolidayCode) {
		this.commandProxy().remove(KshstGrantHdTbl.class, new KshstGrantHdTblPK(companyId, grantYearHolidayNo, conditionNo, yearHolidayCode));
	}
	
	@Override
	public void remove(String companyId, int conditionNo, String yearHolidayCode) {
		this.getEntityManager().createQuery(DELETE_ALL)
			.setParameter("companyId", companyId)
			.setParameter("conditionNo", conditionNo)
			.setParameter("yearHolidayCode", yearHolidayCode)
			.executeUpdate();
	}
		
	/**
	 * Convert to domain
	 * 
	 * @param KshstGrantHdTbl
	 * @return
	 */
	private GrantHdTbl convertToDomain(KshstGrantHdTbl x) {
		return GrantHdTbl.createFromJavaType(x.kshstGrantHdTblPK.companyId, 
				x.kshstGrantHdTblPK.grantYearHolidayNo, 
				x.kshstGrantHdTblPK.conditionNo, 
				x.kshstGrantHdTblPK.yearHolidayCode, 
				x.grantDays, 
				x.limitedTimeHdDays, 
				x.limitedHalfHdCnt, 
				x.lengthOfServiceMonths, 
				x.lengthOfServiceYears, 
				x.grantReferenceDate, 
				x.grantSimultaneity);
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
		kshstGrantHdTbl.kshstGrantHdTblPK  =  new KshstGrantHdTblPK(holidayGrant.getCompanyId(), holidayGrant.getGrantYearHolidayNo(), holidayGrant.getConditionNo(), holidayGrant.getYearHolidayCode().v());
		kshstGrantHdTbl.grantDays =	holidayGrant.getGrantDays().v();
		kshstGrantHdTbl.limitedTimeHdDays = holidayGrant.getLimitedTimeHdDays() != null ? holidayGrant.getLimitedTimeHdDays().v() : 0;
		kshstGrantHdTbl.limitedHalfHdCnt = holidayGrant.getLimitedHalfHdCnt() != null ? holidayGrant.getLimitedHalfHdCnt().v() : 0;
		kshstGrantHdTbl.lengthOfServiceMonths = holidayGrant.getLengthOfServiceMonths() != null ? holidayGrant.getLengthOfServiceMonths().v() : 0;
		kshstGrantHdTbl.lengthOfServiceYears = holidayGrant.getLengthOfServiceYears() != null ? holidayGrant.getLengthOfServiceYears().v() : 0;
		kshstGrantHdTbl.grantReferenceDate = holidayGrant.getGrantReferenceDate().value;
		kshstGrantHdTbl.grantSimultaneity = holidayGrant.getGrantSimultaneity().value;
		return kshstGrantHdTbl;
	}
}
