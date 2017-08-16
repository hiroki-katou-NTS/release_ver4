package nts.uk.ctx.at.request.infra.entity.setting.applicationformreason;

import java.io.Serializable;

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
@Table(name = "KRQST_APP_REASON")
public class KrqstAppReason  extends UkJpaEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*主キー*/
	@EmbeddedId
	public KrqstAppReasonPK krqstAppReasonPK; 
	
	@Column(name ="DEFAULT_FLG")
	public int defaultOrder;
	
	@Override
	protected Object getKey() {
		return krqstAppReasonPK;
	}

}
