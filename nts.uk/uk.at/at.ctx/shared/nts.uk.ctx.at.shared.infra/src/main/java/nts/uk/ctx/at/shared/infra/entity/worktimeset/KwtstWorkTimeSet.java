package nts.uk.ctx.at.shared.infra.entity.worktimeset;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KWTST_WORK_TIME_SET")
public class KwtstWorkTimeSet extends UkJpaEntity{
	@EmbeddedId
	public KwtspWorkTimeSetPK kwtspWorkTimeSetPK;
	
	@Column(name="RANGE_TIME_DAY")
	public int rangeTimeDay;
	
	@Column(name="ADDITION_SET_ID")
	public String additionSetID;
	
	@Column(name="NIGHT_SHIFT_ATR")
	public int nightShiftAtr;
	
	@OneToMany(targetEntity=KwtdtWorkTimeDay.class)
	@JoinTable(name="KWTDT_WORK_TIME_DAY")
	public List<KwtdtWorkTimeDay> kwtdtWorkTimeDay;
	
	@Column(name="START_DATE_CLOCK")
	public int startDateClock;
	
	@Column(name="PREDETERMINE_ATR")
	public int predetermineAtr;
	
	@Override
	protected Object getKey() {
		return kwtspWorkTimeSetPK;
	}
}
