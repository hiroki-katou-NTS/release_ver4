package nts.uk.ctx.at.record.infra.entity.monthly.agreement;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeBreakdown;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：管理期間の36協定時間
 * @author shuichi_ishida
 */
@Entity
@Table(name = "KRCDT_MON_MNG_AGRE_TIME")
@NoArgsConstructor
public class KrcdtMonMngAgreTime extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcdtMonMngAgreTimePK PK;
	
	/** 36協定時間 */
	@Column(name = "AGREEMENT_TIME")
	public int agreementTime;
	
	/** 限度エラー時間 */
	@Column(name = "LIMIT_ERROR_TIME")
	public int limitErrorTime;
	
	/** 限度アラーム時間 */
	@Column(name = "LIMIT_ALARM_TIME")
	public int limitAlarmTime;
	
	/** 特例限度エラー時間 */
	@Column(name = "EXCEPT_LIMIT_ERR_TIME")
	public Integer exceptionLimitErrorTime;
	
	/** 特例限度アラーム時間 */
	@Column(name = "EXCEPT_LIMIT_ALM_TIME")
	public Integer exceptionLimitAlarmTime;
	
	/** 状態 */
	@Column(name = "STATUS")
	public int status;
	
	/** 所定内割増時間 */
	@Column(name = "PRESCR_PREMIUM_TIME")
	public int withinPresctibedPremiumTime;
	
	/** 残業時間 */
	@Column(name = "OVER_TIME")
	public int overTime;
	
	/** 振替残業時間 */
	@Column(name = "TRANS_OVER_TIME")
	public int transferOverTime;
	
	/** 休出時間 */
	@Column(name = "HOLIDAY_WORK_TIME")
	public int holidayWorkTime;
	
	/** 振替休出時間 */
	@Column(name = "TRANS_HDWK_TIME")
	public int transferHolidayWorkTime;
	
	/** フレックス法定内時間 */
	@Column(name = "FLEX_LEGAL_TIME")
	public int flexLegalTime;
	
	/** フレックス法定外時間 */
	@Column(name = "FLEX_ILLEGAL_TIME")
	public int flexIllegalTime;
	
	/** 週割増時間 */
	@Column(name = "WEEK_PREMIUM_TIME")
	public int weeklyPremiumTime;
	
	/** 月割増時間 */
	@Column(name = "MONTH_PREMIUM_TIME")
	public int monthlyPremiumTime;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {
		return this.PK;
	}
	
	/**
	 * ドメインに変換
	 * @return 管理期間の36協定時間
	 */
	public AgreementTimeOfManagePeriod toDomain(){
		
		// 月別実績の36協定時間
		val agreementTime = AgreementTimeOfMonthly.of(
				new AttendanceTimeMonth(this.agreementTime),
				new LimitOneMonth(this.limitErrorTime),
				new LimitOneMonth(this.limitAlarmTime),
				(this.exceptionLimitErrorTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(this.exceptionLimitErrorTime))),
				(this.exceptionLimitAlarmTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(this.exceptionLimitAlarmTime))),
				EnumAdaptor.valueOf(this.status, AgreementTimeStatusOfMonthly.class));
		
		// 36協定時間内訳
		val breakdown = AgreementTimeBreakdown.of(
				new AttendanceTimeMonth(this.overTime),
				new AttendanceTimeMonth(this.transferOverTime),
				new AttendanceTimeMonth(this.holidayWorkTime),
				new AttendanceTimeMonth(this.transferHolidayWorkTime),
				new AttendanceTimeMonth(this.flexLegalTime),
				new AttendanceTimeMonth(this.flexIllegalTime),
				new AttendanceTimeMonth(this.withinPresctibedPremiumTime),
				new AttendanceTimeMonth(this.weeklyPremiumTime),
				new AttendanceTimeMonth(this.monthlyPremiumTime));
		
		return AgreementTimeOfManagePeriod.of(
				this.PK.employeeId,
				new YearMonth(this.PK.yearMonth),
				new Year(this.PK.yearMonth / 100),
				agreementTime,
				breakdown);
	}
	
	/**
	 * ドメインから変換　（for Insert）
	 * @param domain 管理期間の36協定時間
	 */
	public void fromDomainForPersist(AgreementTimeOfManagePeriod domain){
		
		this.PK = new KrcdtMonMngAgreTimePK(
				domain.getEmployeeId(),
				domain.getYearMonth().v());
		this.fromDomainForUpdate(domain);
	}
	
	/**
	 * ドメインから変換　(for Update)
	 * @param domain 管理期間の36協定時間
	 */
	public void fromDomainForUpdate(AgreementTimeOfManagePeriod domain){
		
		val agreementTime = domain.getAgreementTime();
		val breakdown = domain.getBreakdown();
		
		this.agreementTime = agreementTime.getAgreementTime().v();
		this.limitErrorTime = agreementTime.getLimitErrorTime().v();
		this.limitAlarmTime = agreementTime.getLimitAlarmTime().v();
		this.exceptionLimitErrorTime = (agreementTime.getExceptionLimitErrorTime().isPresent() ?
				agreementTime.getExceptionLimitErrorTime().get().v() : null);
		this.exceptionLimitAlarmTime = (agreementTime.getExceptionLimitAlarmTime().isPresent() ?
				agreementTime.getExceptionLimitAlarmTime().get().v() : null);
		this.status = agreementTime.getStatus().value;
		this.withinPresctibedPremiumTime = breakdown.getWithinPrescribedPremiumTime().v();
		this.overTime = breakdown.getOverTime().v();
		this.transferOverTime = breakdown.getTransferOverTime().v();
		this.holidayWorkTime = breakdown.getHolidayWorkTime().v();
		this.transferHolidayWorkTime = breakdown.getTransferTime().v();
		this.flexLegalTime = breakdown.getFlexLegalTime().v();
		this.flexIllegalTime = breakdown.getFlexIllegalTime().v();
		this.weeklyPremiumTime = breakdown.getWeeklyPremiumTime().v();
		this.monthlyPremiumTime = breakdown.getMonthlyPremiumTime().v();
	}
}
