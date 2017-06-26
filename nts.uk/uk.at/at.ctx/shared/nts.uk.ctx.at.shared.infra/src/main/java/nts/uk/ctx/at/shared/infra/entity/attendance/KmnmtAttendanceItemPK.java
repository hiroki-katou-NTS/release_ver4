package nts.uk.ctx.at.shared.infra.entity.attendance;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KmnmtAttendanceItemPK implements Serializable{
	private static final long serialVersionUID = 1L;
	/*会社ID*/
	@Column(name = "CID")
	public String companyId;
	/*勤怠項目ID*/
	@Column(name = "ID")
	public int 	attendanceItemId;
}
