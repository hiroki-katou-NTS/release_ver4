package nts.uk.ctx.at.shared.infra.entity.bonuspay;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.infra.data.query.DBCharPaddingAs;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KbpmtBPTimesheetPK implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "CID")
	public String companyId;
	@Column(name = "BONUS_PAY_TIMESHEET_NO")
	public int timeSheetNO;
	@DBCharPaddingAs(BonusPaySettingCode.class)
	@Column(name = "BONUS_PAY_SET_CD")
	public String bonusPaySettingCode;
}
