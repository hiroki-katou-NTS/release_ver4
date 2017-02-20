package nts.uk.ctx.basic.infra.entity.organization.position;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.LocalDateToDBConverter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CMNMT_JOB_TITLE_REF")
public class CmnmtJobTitleReference {
	
	private static final long serialVersionUID = 1L;
	
	@Convert(converter = LocalDateToDBConverter.class)
	@Basic(optional = false)
	@Column(name ="REF_SET")
	public String referenceSettings;

	public String getReferenceSettings() {
		return referenceSettings;
	}

	public void setAuthorizationCode(String referenceSettings) {
		this.referenceSettings = referenceSettings;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
