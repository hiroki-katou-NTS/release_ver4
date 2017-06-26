package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.commutelimit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QtxmtCommuNotaxLimitPK implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	@NotNull
	public String companyCode;
	
	@Column(name = "COMMU_NOTAX_LIMIT_CD")
	@NotNull
	public String commuNotaxLimitCd;
}
