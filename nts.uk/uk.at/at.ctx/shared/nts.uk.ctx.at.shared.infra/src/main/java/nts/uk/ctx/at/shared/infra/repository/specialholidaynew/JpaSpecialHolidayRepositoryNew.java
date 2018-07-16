package nts.uk.ctx.at.shared.infra.repository.specialholidaynew;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.specialholidaynew.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholidaynew.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.AgeLimit;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.AgeRange;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.AgeStandard;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.SpecialLeaveRestriction;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.FixGrantDate;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantRegular;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantTime;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantedDays;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantedYears;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.AvailabilityPeriod;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.EndDate;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.GrantPeriodic;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.SpecialVacationDeadline;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.SpecialVacationMonths;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.SpecialVacationYears;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.StartDate;
import nts.uk.ctx.at.shared.infra.entity.specialholidaynew.KshstSpecialHolidayNew;
import nts.uk.ctx.at.shared.infra.entity.specialholidaynew.KshstSpecialHolidayPKNew;

/**
 * Jpa Special Holiday Repository
 * 
 * @author tanlv
 *
 */
@Stateless
public class JpaSpecialHolidayRepositoryNew extends JpaRepository implements SpecialHolidayRepository {
	private final static String SELECT_SPHD_BY_COMPANY_ID_QUERY = "SELECT e.pk.companyId, e.pk.specialHolidayCode, e.specialHolidayName, e.memo FROM KshstSpecialHolidayNew e "
			+ "WHERE e.pk.companyId = :companyId "
			+ "ORDER BY e.pk.specialHolidayCode ASC";
	
	private final static String SELECT_SPHD_BY_CODE_QUERY = "SELECT sphd.pk.companyId, sphd.pk.specialHolidayCode, sphd.specialHolidayName, sphd.memo,"
			+ " gra.typeTime, gra.grantDate, gra.allowDisappear, gra.interval, gra.grantedDays,"
			+ " pe.timeMethod, pe.startDate, pe.endDate, pe.deadlineMonths, pe.deadlineYears, pe.limitCarryoverDays,"
			+ " re.pk.specLeaveCd, re.restrictionCls, re.ageLimit, re.genderRest, re.restEmp, re.ageCriteriaCls, re.ageBaseDate, re.ageLowerLimit, re.ageHigherLimit, re.gender"
			+ " FROM KshstSpecialHolidayNew sphd"
			+ " LEFT JOIN KshstGrantRegularNew gra"
			+ " ON sphd.pk.companyId = gra.pk.companyId AND sphd.pk.specialHolidayCode = gra.pk.specialHolidayCode"
			+ " LEFT JOIN KshstGrantPeriodicNew pe"
			+ " ON sphd.pk.companyId = pe.pk.companyId AND sphd.pk.specialHolidayCode = pe.pk.specialHolidayCode"
			+ " LEFT JOIN KshstSpecialLeaveRestriction re"
			+ " ON sphd.pk.companyId = re.pk.companyId AND sphd.pk.specialHolidayCode = re.pk.specialHolidayCode"
			+ " WHERE sphd.pk.companyId = :companyId AND sphd.pk.specialHolidayCode = :specialHolidayCode"
			+ " ORDER BY sphd.pk.specialHolidayCode";
	
	private SpecialHoliday createDomainFromEntity(Object[] c) {
		String companyId = String.valueOf(c[0]);
		int specialHolidayCode = Integer.parseInt(String.valueOf(c[1]));
		String specialHolidayName = String.valueOf(c[2]);
		String memo = String.valueOf(c[3]);
		int typeTime = Integer.parseInt(String.valueOf(c[4]));
		int grantDate = Integer.parseInt(String.valueOf(c[5]));
		boolean allowDisappear = Integer.parseInt(String.valueOf(c[6])) == 1 ? true : false;
		int interval = Integer.parseInt(String.valueOf(c[7]));
		int grantedDays = c[8] != null ? Integer.parseInt(String.valueOf(c[8])) : 0;
		int timeMethod = Integer.parseInt(String.valueOf(c[9]));
		int startDate = c[10] != null ? Integer.parseInt(String.valueOf(c[10])) : 0;
		int endDate = c[11] != null ? Integer.parseInt(String.valueOf(c[11])) : 0;
		int deadlineMonths = c[12] != null ? Integer.parseInt(String.valueOf(c[12])) : 0;
		int deadlineYears = c[13] != null ? Integer.parseInt(String.valueOf(c[13])) : 0;
		int limitCarryoverDays = c[14] != null ? Integer.parseInt(String.valueOf(c[14])) : 0;
		int specialLeaveCode = c[15] != null ? Integer.parseInt(String.valueOf(c[15])) : 0;
		int restrictionCls = Integer.parseInt(String.valueOf(c[16]));
		int ageLimit = Integer.parseInt(String.valueOf(c[17]));
		int genderRest = Integer.parseInt(String.valueOf(c[18]));
		int restEmp = Integer.parseInt(String.valueOf(c[19]));
		int ageCriteriaCls = c[20] != null ? Integer.parseInt(String.valueOf(c[20])) : 0;
		String ageBaseDate = c[21] != null ? String.valueOf(c[21]) : "";
		int ageLowerLimit = c[22] != null ? Integer.parseInt(String.valueOf(c[22])) : 0;
		int ageHigherLimit = c[23] != null ? Integer.parseInt(String.valueOf(c[23])) : 0;
		int gender = c[24] != null ? Integer.parseInt(String.valueOf(c[24])) : 0;
		
		FixGrantDate fixGrantDate = FixGrantDate.createFromJavaType(new GrantedYears(interval), new GrantedDays(grantedDays));
		GrantTime grantTime = GrantTime.createFromJavaType(fixGrantDate, null);
		GrantRegular grantRegular = GrantRegular.createFromJavaType(companyId, specialHolidayCode, typeTime, grantDate, allowDisappear, grantTime);
		
		AvailabilityPeriod availabilityPeriod = AvailabilityPeriod.createFromJavaType(new StartDate(startDate), new EndDate(endDate));
		SpecialVacationDeadline expirationDate = SpecialVacationDeadline.createFromJavaType(new SpecialVacationMonths(deadlineMonths), new SpecialVacationYears(deadlineYears));
		GrantPeriodic grantPeriodic = GrantPeriodic.createFromJavaType(companyId, specialHolidayCode, timeMethod, availabilityPeriod, expirationDate, limitCarryoverDays);
		
		AgeStandard ageStandard = AgeStandard.createFromJavaType(ageCriteriaCls, ageBaseDate);
		AgeRange ageRange = AgeRange.createFromJavaType(new AgeLimit(ageLowerLimit), new AgeLimit(ageHigherLimit));
		SpecialLeaveRestriction specialLeaveRestriction = SpecialLeaveRestriction.createFromJavaType(companyId, specialHolidayCode, specialLeaveCode, restrictionCls, 
				ageLimit, genderRest, restEmp, ageStandard, ageRange, gender);
		
		return SpecialHoliday.createFromJavaType(companyId, specialHolidayCode, specialHolidayName, grantRegular, grantPeriodic, specialLeaveRestriction, memo);
	}
	
	private SpecialHoliday createSphdDomainFromEntity(Object[] c) {
		String companyId = String.valueOf(c[0]);
		int specialHolidayCode = Integer.parseInt(String.valueOf(c[1]));
		String specialHolidayName = String.valueOf(c[2]);
		String memo = String.valueOf(c[3]);
		
		return SpecialHoliday.createFromJavaType(companyId, specialHolidayCode, specialHolidayName, memo);
	}
	
	private KshstSpecialHolidayNew createSpecialHolidayFromDomain(SpecialHoliday domain) {
		KshstSpecialHolidayPKNew pk = new KshstSpecialHolidayPKNew(domain.getCompanyId(), domain.getSpecialHolidayCode().v());
		return new KshstSpecialHolidayNew(pk, domain.getSpecialHolidayName().v(), domain.getMemo().v());
	}
	
	@Override
	public List<SpecialHoliday> findByCompanyId(String companyId) {
		return this.queryProxy().query(SELECT_SPHD_BY_COMPANY_ID_QUERY, Object[].class)
				.setParameter("companyId", companyId)
				.getList(c -> {
					return createSphdDomainFromEntity(c);
				});
	}
	
	@Override
	public Optional<SpecialHoliday> findByCode(String companyId, int specialHolidayCode) {
		return this.queryProxy().query(SELECT_SPHD_BY_CODE_QUERY, Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("specialHolidayCode", specialHolidayCode)
				.getSingle(c -> {
					return createDomainFromEntity(c);
				});
	}

	@Override
	public void add(SpecialHoliday specialHoliday) {
		this.commandProxy().insert(createSpecialHolidayFromDomain(specialHoliday));
	}

	@Override
	public void update(SpecialHoliday specialHoliday) {
		KshstSpecialHolidayPKNew pk = new KshstSpecialHolidayPKNew(specialHoliday.getCompanyId(), specialHoliday.getSpecialHolidayCode().v());
		KshstSpecialHolidayNew old = this.queryProxy().find(pk, KshstSpecialHolidayNew.class).orElse(null);
		old.specialHolidayName = specialHoliday.getSpecialHolidayName().v();
		old.memo = specialHoliday.getMemo().v();
		this.commandProxy().update(old);
	}

	@Override
	public void delete(String companyId, int specialHolidayCode) {
		KshstSpecialHolidayPKNew pk = new KshstSpecialHolidayPKNew(companyId, specialHolidayCode);
		this.commandProxy().remove(KshstSpecialHolidayNew.class, pk);
	}
}
