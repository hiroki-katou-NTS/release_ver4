package entity.roles.auth.category;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PPEMT_PERSON_CTG_AUTH")
@Entity
public class PpemtPersonCategoryAuth extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public PpemtPersonCategoryAuthPk ppemtPersonCategoryAuthPk;
	
	@Basic(optional = false)
	@Column(name = "ALLOW_PER_REF_ATR")
	public int allowPersonRef;
	
	@Basic(optional = false)
	@Column(name = "ALLOW_OTHER_REF_ATR")
	public int allowOtherRef;
	
	@Basic(optional = false)
	@Column(name = "ALLOW_OTHER_C_REF_ATR")
	public int allowOtherCompanyRef;
	
	@Basic(optional = false)
	@Column(name = "SELF_PAST_HIS_AUTH_TYPE")
	public int selfPastHisAuth;
	
	@Basic(optional = false)
	@Column(name = "SELF_FUTURE_HIS_AUTH_TYPE")
	public int selfFutureHisAuth;
	
	@Basic(optional = false)
	@Column(name = "SELF_ALLOW_DEL_HIS_ATR")
	public int selfAllowDelHis;
	
	@Basic(optional = false)
	@Column(name = "SELF_ALLOW_ADD_HIS_ATR")
	public int selfAllowAddHis;
	
	@Basic(optional = false)
	@Column(name = "OTHER_PAST_HIS_AUTH_TYPE")
	public int otherPastHisAuth;
	
	@Basic(optional = false)
	@Column(name = "OTHER_FUTURE_HIS_AUTH_TYPE")
	public int otherFutureHisAuth;
	
	@Basic(optional = false)
	@Column(name = "OTHER_ALLOW_DEL_HIS_ATR")
	public int otherAllowDelHis;
	
	
	@Basic(optional = false)
	@Column(name = "OTHER_ALLOW_ADD_HIS_ATR")
	public int otherAllowAddHis;
	
	@Basic(optional = false)
	@Column(name = "SELF_ALLOW_DEL_MULTI_ATR")
	public int selfAllowDelMulti;
	
	
	@Basic(optional = false)
	@Column(name = "SELF_ALLOW_ADD_MULTI_ATR")
	public int selfAllowAddMulti;
	
	@Basic(optional = false)
	@Column(name = "OTHER_ALLOW_DEL_MULTI_ATR")
	public int otherAllowDelMulti;
	
	@Basic(optional = false)
	@Column(name = "OTHER_ALLOW_ADD_MULTI_ATR")
	public int otherAllowAddMulti;
	
	@Override
	protected Object getKey() {
		return this.ppemtPersonCategoryAuthPk;
	}

}
