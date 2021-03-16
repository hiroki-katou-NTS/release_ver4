package nts.uk.ctx.at.record.infra.entity.employmentinfoterminal.infoterminal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcdtTrRqStMonitorPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 契約コード
	 */
	@Column(name = "CONTRACT_CD")
	public String contractCode;
	
	/**
	 * 端末コード
	 */
	@Column(name = "TIMERECORDER_CD")
	public String timeRecordCode;
	
	
	
}
