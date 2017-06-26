package nts.uk.ctx.basic.infra.entity.organization.payclassification;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter

public class QmnmtPayClassPK implements Serializable{

	/**
	 * serialVersionUID
	 */
	public static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@Column(name ="CCD")
	public String companyCode;
	
	@Basic(optional = false)
	@Column(name ="PAYCLASS_CD")
	public String payClassificationCode;


}
