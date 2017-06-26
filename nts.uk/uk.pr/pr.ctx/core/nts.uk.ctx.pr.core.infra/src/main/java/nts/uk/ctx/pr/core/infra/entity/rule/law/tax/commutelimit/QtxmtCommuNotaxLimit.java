package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.commutelimit;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "QTXMT_COMMU_NOTAX_LIMIT")
public class QtxmtCommuNotaxLimit extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;


	@EmbeddedId
	public QtxmtCommuNotaxLimitPK qtxmtCommuNotaxLimitPK;
	
	
	@Basic(optional = false)
	@Column(name = "COMMU_NOTAX_LIMIT_NAME")
	public String commuNotaxLimitName;

	@Basic(optional = false)
	@Column(name = "COMMU_NOTAX_LIMIT_VALUE")
	public BigDecimal commuNotaxLimitValue;
	
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	public int exclusVer;

	@Override
	protected Object getKey() {
		return qtxmtCommuNotaxLimitPK;
	}
}
