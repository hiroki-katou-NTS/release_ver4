/**
 * 9:59:33 AM Jun 6, 2017
 */
package nts.uk.ctx.at.shared.infra.entity.bonuspay;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "KBPST_BP_UNIT_USE")
public class KbpstBPUnitUseSetting extends UkJpaEntity implements Serializable {
	public static final long serialVersionUID = 1L;
	@EmbeddedId
	public KbpstBPUnitUseSettingPK kbpstBPUnitUseSettingPK;
	@Column(name = "WORKPLACE_USE_ATR")
	public BigDecimal workplaceUseAtr;
	@Column(name = "PERSONAL_USE_ATR")
	public BigDecimal personalUseAtr;
	@Column(name = "WORKING_TIMESHEET_USE_ATR")
	public BigDecimal workingTimesheetUseAtr;

	@Override
	protected Object getKey() {
		return kbpstBPUnitUseSettingPK;
	}
}
