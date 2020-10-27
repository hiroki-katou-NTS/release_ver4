package nts.uk.ctx.at.function.infra.entity.attendancetype;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;
@Setter
@Getter
@Entity
@Table(name="KMNMT_ATTENDANCE_TYPE")
@AllArgsConstructor
@NoArgsConstructor
public class KmnmtAttendanceType extends ContractUkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/*主キー*/
	@EmbeddedId
    public KmnmtAttendanceTypePK kmnmtAttendanceTypePK;
	
	@Override
	protected Object getKey() {
		return null;
	}
}
