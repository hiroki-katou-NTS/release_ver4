package nts.uk.ctx.basic.infra.entity.organization.position;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CatmtAuthPK implements Serializable{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)	
	@Column(name = "CCD")
	public String companyCode;
		
	@Basic(optional = false)
	@Column(name = "AUTH_SCOPE_ATR")
	public String authScopeAtr;
	
	@Basic(optional = false)
	@Column(name = "AUTHCD")
	public String authCode;
}
