package nts.uk.ctx.at.record.infra.entity.daily.dailyimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Table(name = "KRCDT_TEMP_DAI")
@NoArgsConstructor
@AllArgsConstructor
public class KrcdtTempDai extends UkJpaEntity implements Serializable {

	/***/
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtTempDaiPk id;
	
	@Column(name = "WORKTYPE")
	public String workType;

	@Column(name = "WORKTIME")
	public String workTime;

	@Column(name = "STARTTIME")
	public String startTime;
	
	@Column(name = "ENDTIME")
	public String endTime;
	
	@Column(name = "BREAKSTARTTIME1")
	public String breakStart1;

	@Column(name = "BREAKENDTIME1")
	public String breakEnd1;

	@Column(name = "BREAKSTARTTIME2")
	public String breakStart2;
	
	@Column(name = "BREAKENDTIME2")
	public String breakEnd2;
	
	@Override
	protected Object getKey() {
		return this.id;
	}

}
