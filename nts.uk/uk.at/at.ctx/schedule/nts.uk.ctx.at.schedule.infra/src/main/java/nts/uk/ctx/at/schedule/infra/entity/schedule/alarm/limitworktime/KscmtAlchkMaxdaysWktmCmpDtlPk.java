package nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.limitworktime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KscmtAlchkMaxdaysWktmCmpDtlPk {
	
	@Column(name = "CID")
	public String companyId;

	@Column(name = "CD")
	public String code;
	
	@Column(name = "TGT_WKTM_CD")
	public String workTimeCode;
	
}
