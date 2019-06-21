/**
 * 
 */
package nts.uk.ctx.at.auth.infra.entity.initswitchsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author hieult
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KacmtDispPeriodSwitchPK implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "CID")
	public String companyID;
	
	@Column(name = "ROLE_ID")
	public String roleID;

}
