package nts.uk.ctx.at.record.infra.entity.divergencetime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KmkmtDivergenceReasonPK implements Serializable{

	private static final long serialVersionUID = 1L;
	/*会社ID*/
	@Column(name = "CID")
	public String companyId;
	/*乖離時間ID*/
	@Column(name = "DIVERGENCETIME_ID")
	public int divTimeId;
	/*乖離理由コード*/
	@Column(name = "DIVERGENCE_REASON_CD")
	public String divReasonCode;
	
}
