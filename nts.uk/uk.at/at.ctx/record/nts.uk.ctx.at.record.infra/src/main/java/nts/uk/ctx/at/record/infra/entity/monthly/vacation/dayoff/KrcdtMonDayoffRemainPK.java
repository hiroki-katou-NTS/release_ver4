package nts.uk.ctx.at.record.infra.entity.monthly.vacation.dayoff;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * プライマリキー：代休月別残数データ
 * @author do_dt
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class KrcdtMonDayoffRemainPK implements Serializable{
	/**	社員ID */
	@Column(name = "SID")
	private String sid;
	/**	年月 */
	@Column(name = "YM")
	private int ym;
	/**	締めID */
	@Column(name = "CLOSURE_ID")
	private int closureId;
	/**	締め日 */
	@Column(name = "CLOSURE_DAY")
	private int closureDay;
	/**	末日とする */
	@Column(name = "IS_LAST_DAY")
	private int isLastDay;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
