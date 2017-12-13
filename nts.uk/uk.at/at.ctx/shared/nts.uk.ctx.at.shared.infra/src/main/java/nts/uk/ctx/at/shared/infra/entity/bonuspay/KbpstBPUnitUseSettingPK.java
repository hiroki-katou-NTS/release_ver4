package nts.uk.ctx.at.record.infra.entity.bonuspay;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KbpstBPUnitUseSettingPK implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "CID")
	public String companyId;
}
