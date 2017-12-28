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
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * @author phongtq
 * 通常勤務の加算設定
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_WORK_REGULAR_SET")
public class KshstWorkRegularSet  extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 主キー */
	@EmbeddedId
	public KshstWorkRegularSetPK kshstRegularWorkSetPK;
	
	/** 実働のみで計算する */
	@Column(name = "PRE_CALC_ACTUAL_OPERATION")
	public int calcActualOperation1;
	
	/** インターバル免除時間を含めて計算する */
	@Column(name = "PRE_EXEMPT_TAX_TIME")
	public int exemptTaxTime1;
	
	/** 育児・介護時間を含めて計算する */
	@Column(name = "PRE_INC_CHILD_NURSE_CARE")
	public int incChildNursingCare1;
	
	/** 加算する */
	@Column(name = "PRE_ADDITION_TIME")
	public int additionTime1;
	
	/** 遅刻・早退を控除しない */
	@Column(name = "PRE_NOT_DEDUCT_LATELEAVE")
	public int notDeductLateleave1;
	
	/** 通常、変形の所定超過時 */
	@Column(name = "PRE_DEFORMAT_EXC_VALUE")
	public int deformatExcValue1;
	
	/** インターバル免除時間を含めて計算する */
	@Column(name = "WKT_EXEMPT_TAX_TIME")
	public int exemptTaxTime2;
	
	/** 実働のみで計算する */
	@Column(name = "WKT_CALC_ACTUAL_OPERATION")
	public int calcActualOperation2;
	
	/** 育児・介護時間を含めて計算する */
	@Column(name = "WKT_INC_CHILD_NURSE_CARE")
	public int incChildNursingCare2;
	
	/** 遅刻・早退を控除しない */
	@Column(name = "WKT_NOT_DEDUCT_LATELEAVE")
	public int notDeductLateleave2;
	
	/** 加算する */
	@Column(name = "WKT_ADDITION_TIME")
	public int additionTime2;
	
	@OneToOne(optional = false)
		@JoinColumn(name = "CID", referencedColumnName="CID", insertable = false, updatable = false)
	public KshstHolidayAdditionSet holidayAddtimeSet;
	
	@Override
	protected Object getKey() {
		return kshstRegularWorkSetPK;
	}
}
