package nts.uk.ctx.at.request.infra.entity.application.common.approveaccepted;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="KRQDT_APPROVE_ACCEPTED")
@AllArgsConstructor
@NoArgsConstructor
public class KafdtApproveAccepted extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KafdtApproveAcceptedPK kafdtApproveAcceptedPK;
	

	
	@Override
	protected Object getKey() {
		return kafdtApproveAcceptedPK;
	}
	

}
