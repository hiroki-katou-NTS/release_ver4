package nts.uk.ctx.at.record.infra.entity.jobmanagement.favoritetask.onedayfavoriteset;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author tutt
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcdtTaskFavDaySetItemPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "FAV_ID")
	public String favId;
	
	@Column(name = "SUP_TASK_NO")
	public int supTaskNo;
	
	@Column(name = "START_CLOCK")
	public int startClock;
}
