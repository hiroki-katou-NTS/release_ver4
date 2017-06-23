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
@Table(name = "KBPST_BP_TIME_ITEM_SET")
public class KbpstBPTimeItemSetting extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KbpstBPTimeItemSettingPK kbpstBPTimeItemSettingPK;
	@Column(name = "HOLIDAY_TIMESHEET_ATR")	
	public BigDecimal holidayCalSettingAtr;
	@Column(name = "OVERTIME_TIMESHEET_ATR")
	public BigDecimal overtimeCalSettingAtr;
	@Column(name = "WORKING_TIMESHEET_ATR")
	public BigDecimal worktimeCalSettingAtr;
//	@Column(name = "TYPE_ATR")
//	private BigDecimal timeItemTypeAtr;
	@Override
	protected Object getKey() {
		return kbpstBPTimeItemSettingPK;
	}


}
