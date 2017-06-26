package nts.uk.ctx.basic.infra.entity.system.bank;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author sonnh
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CBKMT_BANK")
public class CbkmtBank extends UkJpaEntity implements Serializable {
	
	static final long serialVersionUID = 42L;
	
	@EmbeddedId
	public CbkmtBankPK cbkmtBankPK;
	
	@Column(name = "BANK_NAME")
	public String bankName;

	@Column(name = "BANK_KN_NAME")
	public String bankKnName;

	@Column(name = "MEMO")
	public String memo;

	@Override
	protected Object getKey() {
		return cbkmtBankPK;
	}

}
