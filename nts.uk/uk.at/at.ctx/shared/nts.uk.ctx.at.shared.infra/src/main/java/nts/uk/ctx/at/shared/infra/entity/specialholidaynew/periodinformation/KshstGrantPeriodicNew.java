package nts.uk.ctx.at.shared.infra.entity.specialholidaynew.periodinformation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.GrantPeriodic;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 付与日数定期
 * 
 * @author tanlv
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KSHST_GRANT_PERIODIC")
public class KshstGrantPeriodicNew extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KshstGrantPeriodicPKNew pk;

	/* 期限指定方法 */
	@Column(name = "TIME_CSL_METHOD")
	public int timeMethod;
	
	/* 使用可能期間.開始日 */
	@Column(name = "START_DATE")
	public GeneralDate startDate;

	/* 使用可能期間.終了日 */
	@Column(name = "END_DATE")
	public GeneralDate endDate;

	/* 特別休暇の有効期限.月数 */
	@Column(name = "DEADLINE_MONTHS")
	public Integer deadlineMonths;
	
	/* 特別休暇の有効期限.年数 */
	@Column(name = "DEADLINE_YEARS")
	public Integer deadlineYears;

	/* 繰越上限日数 */
	@Column(name = "LIMIT_CARRYOVER_DAYS")
	public Integer limitCarryoverDays;
	
	@Override
	protected Object getKey() {
		return pk;
	}

	/**
	 * To Entity
	 * 
	 * @param domain
	 * @return
	 */
	public static KshstGrantPeriodicNew toEntity(GrantPeriodic domain) {
		return new KshstGrantPeriodicNew(
				new KshstGrantPeriodicPKNew(domain.getCompanyId(), domain.getSpecialHolidayCode().v()),
				domain.getTimeSpecifyMethod().value, 
				domain.getAvailabilityPeriod() != null ? domain.getAvailabilityPeriod().getStartDate() : null,
				domain.getAvailabilityPeriod() != null ? domain.getAvailabilityPeriod().getEndDate() : null,
				domain.getExpirationDate() != null ? domain.getExpirationDate().getMonths().v() : null,
				domain.getExpirationDate() != null ? domain.getExpirationDate().getYears().v() : null,
				domain.getLimitCarryoverDays().v());
	}

	public KshstGrantPeriodicNew(KshstGrantPeriodicPKNew pk, int timeMethod, GeneralDate startDate,
			GeneralDate endDate, Integer deadlineMonths, Integer deadlineYears, Integer limitCarryoverDays) {
		
		this.pk = pk;
		this.timeMethod = timeMethod;
		this.startDate = startDate;
		this.endDate = endDate;
		this.deadlineMonths = deadlineMonths;
		this.deadlineYears = deadlineYears;
		this.limitCarryoverDays = limitCarryoverDays;
	}
}