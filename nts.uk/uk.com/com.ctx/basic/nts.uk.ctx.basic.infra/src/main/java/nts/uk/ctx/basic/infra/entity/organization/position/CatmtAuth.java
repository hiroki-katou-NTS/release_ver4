package nts.uk.ctx.basic.infra.entity.organization.position;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CATMT_AUTH")
public class CatmtAuth extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
    public CatmtAuthPK catmtAuthPk;

	@Basic(optional = true)
	@Column(name = "AUTH_NAME")
	public String authName;
	
	@Basic(optional = false)
	@Column(name = "EMP_SCOPE_ATR")
	public int empScopeAtr;
	
	@Basic(optional = false)
	@Column(name = "IN_CHARGE_ATR")
	public int inChargeAtr;
	
	@Basic(optional = true)
	@Column(name = "MEMO")
	public String memo;

	@Override
	protected CatmtAuthPK getKey() {
		return this.catmtAuthPk;
	}
}
