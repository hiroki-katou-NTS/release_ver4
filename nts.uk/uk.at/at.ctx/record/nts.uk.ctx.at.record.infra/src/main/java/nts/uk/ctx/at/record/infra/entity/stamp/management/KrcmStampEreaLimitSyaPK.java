package nts.uk.ctx.at.record.infra.entity.stamp.management;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author nws_vandv
 *
 */
public class KrcmStampEreaLimitSyaPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 会社ID */
	@NotNull
	@Column(name = "CID")
	private String cId;
}
