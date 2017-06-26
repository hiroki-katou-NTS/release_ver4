package nts.uk.ctx.pr.core.infra.entity.rule.employment.layout;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QstmtStmtLayoutDetailPK implements Serializable{
	/**
	 * serialVersionUID
	 */
	public static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name ="CCD")
	public String companyCd;

	@Basic(optional = false)
	@Column(name ="STMT_CD")
	public String stmtCd;

	@Basic(optional = false)
	@Column(name ="HIST_ID")
	public String historyId;
	
	@Basic(optional = false)
	@Column(name ="CTG_ATR")
	public int ctgAtr;

	@Basic(optional = false)
	@Column(name ="ITEM_CD")
	public String itemCd;
}
