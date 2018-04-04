package nts.uk.ctx.at.shared.infra.entity.calculation.holiday;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * @author phongtq
 * フレックス勤務の加算設定
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_WORK_FLEX_SET")
public class KshstWorkFlexSet  extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 主キー */
	@EmbeddedId
	public KshstWorkFlexSetPK kshstFlexWorkSetPK;
	
	/** 実働のみで計算する */
	@Column(name = "PRE_CALC_ACTUAL_OPERATION")
	public int calcActualOperation1;
	
	/** インターバル免除時間を含めて計算する */
	@Column(name = "PRE_EXEMPT_TAX_TIME")
	public int exemptTaxTime1;
	
	/** 育児・介護時間を含めて計算する */
	@Column(name = "PRE_INC_CHILD_NURSE_CARE")
	public int incChildNursingCare1;
	
	/** フレックスの所定超過時 */
	@Column(name = "PRE_PREDETERMIN_OT")
	public int predeterminedOvertime1;
	
	/** 加算する */
	@Column(name = "PRE_ADDITION_TIME")
	public int additionTime1;
	
	/** 遅刻・早退を控除しない */
	@Column(name = "PRE_NOT_DEDUCT_LATELEAVE")
	public int notDeductLateleave1;
	
	/** インターバル免除時間を含めて計算する */
	@Column(name = "WKT_EXEMPT_TAX_TIME")
	public int exemptTaxTime2;
	
	/** 欠勤時間をマイナスする*/
	@Column(name = "WKT_MINUS_ABSENCE_TIME")
	public int minusAbsenceTime2;
	
	/** 実働のみで計算する */
	@Column(name = "WKT_CALC_ACTUAL_OPERATION")
	public int calcActualOperation2;
	
	/** 育児・介護時間を含めて計算する */
	@Column(name = "WKT_INC_CHILD_NURSE_CARE")
	public int incChildNursingCare2;
	
	/** 遅刻・早退を控除しない */
	@Column(name = "WKT_NOT_DEDUCT_LATELEAVE")
	public int notDeductLateleave2;
	
	/** フレックスの所定不足時 */
	@Column(name = "WKT_PREDETERMIN_DEF")
	public int predeterminDeficiency2;
	
	/** 加算する */
	@Column(name = "WKT_ADDITION_TIME")
	public int additionTime2;
	
	/*就業時間帯毎の設定を可能とする*/
	@Column(name = "ENABLE_SET_PER_WORK_HOUR1")
	public int enableSetPerWorkHour1;
	
	/*就業時間帯毎の設定を可能とする*/
	@Column(name = "ENABLE_SET_PER_WORK_HOUR2")
	public int enableSetPerWorkHour2;
	
	/*月次法定内のみ加算*/
	@Column(name = "ADDITION_WITHIN_MONTHLY_STATUTORY")
	public int additionWithinMonthlyStatutory;
	
	@OneToOne(optional = false)
		@JoinColumn(name = "CID", referencedColumnName="CID", insertable = false, updatable = false)
	public KshstHolidayAdditionSet holidayAddtimeSet;
	
	@Override
	protected Object getKey() {
		return kshstFlexWorkSetPK;
	}
}
