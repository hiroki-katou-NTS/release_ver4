package nts.uk.ctx.at.record.infra.entity.standardtime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KmkmtAgeementTimeClassPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "CID")
	public String companyId;
	
	@Column(name = "BASIC_SETTING_ID")
	public String basicSettingId;
	
	@Column(name = "CLASSIFICATION_CODE")
	public String classificationCode;
}
