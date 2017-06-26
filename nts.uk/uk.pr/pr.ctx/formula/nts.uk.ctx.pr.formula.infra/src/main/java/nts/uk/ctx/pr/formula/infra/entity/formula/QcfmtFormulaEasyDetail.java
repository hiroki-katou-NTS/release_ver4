package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QCFMT_FORMULA_EASY_DETAIL")
public class QcfmtFormulaEasyDetail extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;	
	
	@EmbeddedId
    public QcfmtFormulaEasyDetailPK qcfmtFormulaEasyDetailPK;

	@Column(name = "EASY_FORMULA_NAME")
	public String easyFormulaName;
	
	@Column(name = "EASY_FORMULA_TYPE_ATR")
	public BigDecimal easyFormulaTypeAttribute;
	
	@Column(name = "A_BASE_MNY_ATR")
	public BigDecimal baseAmountDevision;
	
	@Column(name = "A_BASE_MNY")
	public BigDecimal baseFixedAmount;
	
	@Column(name = "B_DIVIDE_VALUE_SET")
	public BigDecimal baseValueDevision;
	
	@Column(name = "B_DIVIDE_VALUE")
	public BigDecimal baseFixedValue;
	
	@Column(name = "C_PREMIUM_RATE")
	public BigDecimal premiumRate;
	
	@Column(name = "D_ROUND_ATR")
	public BigDecimal roundProcessingDevision;
	
	@Column(name = "E_WORK_ITEM_CD")
	public String coefficientDivision;
	
	@Column(name = "E_WORK_VALUE")
	public BigDecimal coefficientFixedValue;
	
	@Column(name = "G_ADJUSTMENT_ATR")
	public BigDecimal adjustmentDevision;
	
	@Column(name = "F_ROUND_ATR")
	public BigDecimal totalRounding;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaEasyDetailPK;
	}

}
