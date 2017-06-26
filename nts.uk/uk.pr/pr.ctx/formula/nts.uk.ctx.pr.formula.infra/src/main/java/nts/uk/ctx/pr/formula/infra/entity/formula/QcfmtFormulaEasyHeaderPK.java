package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class QcfmtFormulaEasyHeaderPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	public String companyCode;
	
	@Column(name = "FORMULA_CD")
	public String formulaCode;
	
	@Column(name = "HIST_ID")
	public String historyId;
	
	@Column(name ="REF_MASTER_NO")
	public BigDecimal refMasterNo;
	
}
