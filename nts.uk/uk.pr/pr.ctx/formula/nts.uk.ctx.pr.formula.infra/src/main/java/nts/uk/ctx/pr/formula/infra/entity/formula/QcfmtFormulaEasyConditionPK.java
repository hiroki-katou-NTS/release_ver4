package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class QcfmtFormulaEasyConditionPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	public String companyCode;
	
	@Column(name = "FORMULA_CD")
	public String formulaCode;
	
	@Column(name = "HIST_ID")
	public String historyId;
	
	@Column(name = "REF_MASTER_CD")
	public String refMasterCd;
}
