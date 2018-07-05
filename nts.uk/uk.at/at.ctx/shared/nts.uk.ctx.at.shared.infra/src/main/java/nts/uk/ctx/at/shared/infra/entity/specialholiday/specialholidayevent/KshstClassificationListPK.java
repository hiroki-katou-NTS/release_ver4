package nts.uk.ctx.at.shared.infra.entity.specialholiday.specialholidayevent;

import java.io.Serializable;

import javax.persistence.Column;

public class KshstClassificationListPK implements Serializable {

	private static final long serialVersionUID = 1L;

	/* 会社ID */
	@Column(name = "CID")
	public String companyId;

	/* 特別休暇枠NO */
	@Column(name = "S_HOLIDAY_EVENT_NO")
	public int specialHolidayEventNo;

	/* 分類コード */
	@Column(name = "CLASIFICATION_CD")
	public String classificationCd;

}
