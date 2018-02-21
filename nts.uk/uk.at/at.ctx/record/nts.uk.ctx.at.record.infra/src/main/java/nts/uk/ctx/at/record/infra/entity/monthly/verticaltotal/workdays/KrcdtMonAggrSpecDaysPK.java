package nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * プライマリキー：集計特定日数
 * @author shuichi_ishida
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KrcdtMonAggrSpecDaysPK implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 社員ID */
	@Column(name = "SID")
	public String employeeID;

	/** 年月 */
	@Column(name = "YM")
	public int yearMonth;
	
	/** 締めID */
	@Column(name = "CLOSURE_ID")
	public int closureId;
	
	/** 締め日 */
	@Column(name = "CLOSURE_DAY")
	public int closureDay;
	
	/** 末日とする */
	@Column(name = "IS_LAST_DAY")
	public int isLastDay;

	/** 特定日項目NO */
	@Column(name = "SPCDAY_ITEM_NO")
	public int specificDayItemNo;
}
