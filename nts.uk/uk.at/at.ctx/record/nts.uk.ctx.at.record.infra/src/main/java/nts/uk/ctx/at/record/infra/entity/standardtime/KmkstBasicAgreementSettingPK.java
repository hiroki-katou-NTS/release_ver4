package nts.uk.ctx.at.record.infra.entity.standardtime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KmkstBasicAgreementSettingPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "BASIC_SETTING_ID")
	public String basicSettingId;
}
