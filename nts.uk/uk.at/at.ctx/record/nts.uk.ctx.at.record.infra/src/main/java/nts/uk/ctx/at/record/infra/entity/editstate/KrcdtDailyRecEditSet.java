package nts.uk.ctx.at.record.infra.entity.editstate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 * 日別実績の編集状態
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAILY_REC_EDIT_SET")
public class KrcdtDailyRecEditSet extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtDailyRecEditSetPK krcdtDailyRecEditSetPK;
	
	@Column(name = "EDIT_STATE")
	public int editState;
	
	@Override
	protected Object getKey() {
		return this.krcdtDailyRecEditSetPK;
	}
	
	public static final JpaEntityMapper<KrcdtDailyRecEditSet> MAPPER = new JpaEntityMapper<>(KrcdtDailyRecEditSet.class);
	
	public EditStateOfDailyPerformance toDomain() {
		return new EditStateOfDailyPerformance(
				this.krcdtDailyRecEditSetPK.employeeId,
				this.krcdtDailyRecEditSetPK.attendanceItemId,
				this.krcdtDailyRecEditSetPK.processingYmd,
				EnumAdaptor.valueOf(this.editState, EditStateSetting.class));
	}
}
