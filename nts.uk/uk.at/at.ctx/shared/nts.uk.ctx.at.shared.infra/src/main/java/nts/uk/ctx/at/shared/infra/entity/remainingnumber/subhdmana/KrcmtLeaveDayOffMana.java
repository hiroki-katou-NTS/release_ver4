package nts.uk.ctx.at.shared.infra.entity.remainingnumber.subhdmana;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_LEAVE_DAYOFF_MANA")
@NoArgsConstructor
@AllArgsConstructor
public class KrcmtLeaveDayOffMana extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcmtLeaveDayOffManaPK krcmtLeaveDayOffManaPK;
	
	// 使用日数
	@Column(name = "USED_DAYS")
	public Double usedDays;
		
	// 使用時間数
	@Column(name = "USED_HOURS")
	public int usedHours;
		
	// 対象選択区分
	@Column(name = "TARGET_SELECTION_ATR")
	public int targetSelectionAtr;

	@Override
	protected Object getKey() {
		return krcmtLeaveDayOffManaPK;
	}
}
