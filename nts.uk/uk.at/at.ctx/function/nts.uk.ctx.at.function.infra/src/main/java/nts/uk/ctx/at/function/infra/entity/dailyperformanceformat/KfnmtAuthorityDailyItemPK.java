package nts.uk.ctx.at.function.infra.entity.dailyperformanceformat;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtAuthorityDailyItemPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CID")
	public String companyId;
	
	@Column(name = "DAILY_PERFORMANCE_FORMAT_CD")
	public String dailyPerformanceFormatCode;
	
	@Column(name = "ATTENDANCE_ITEM_ID")
	public int attendanceItemId;
	
	@Column(name = "SHEET_NO")
	public BigDecimal sheetNo;
}
