package nts.uk.ctx.at.shared.infra.entity.calculation.holiday;
/**
 * @author phongtq
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KshstHolidayAdditionSetPK  implements Serializable{
	private static final long serialVersionUID = 1L;
	/**会社ID*/
	@Column(name = "CID")
	public String companyId;
}
