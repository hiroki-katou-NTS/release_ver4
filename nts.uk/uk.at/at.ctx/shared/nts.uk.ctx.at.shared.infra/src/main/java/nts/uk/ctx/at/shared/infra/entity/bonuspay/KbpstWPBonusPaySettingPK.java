package nts.uk.ctx.at.shared.infra.entity.bonuspay;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.infra.data.query.DBCharPaddingAs;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.WorkplaceId;
@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KbpstWPBonusPaySettingPK implements Serializable{
	private static final long serialVersionUID = 1L;
	@DBCharPaddingAs(WorkplaceId.class)
	@Column(name = "WKPID")
	public String workplaceId;
}
