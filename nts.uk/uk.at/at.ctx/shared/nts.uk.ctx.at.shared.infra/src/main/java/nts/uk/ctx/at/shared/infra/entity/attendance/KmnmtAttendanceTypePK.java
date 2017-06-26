package nts.uk.ctx.at.shared.infra.entity.attendance;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KmnmtAttendanceTypePK implements Serializable{
	private static final long serialVersionUID = 1L;
	/*会社ID*/
	@Column(name = "CID")
	public String companyId;
	/*勤怠項目ID*/
	@Column(name = "ATTENDANCEITEM_ID")
	public int attendanceItemId;
	/*勤怠項目名称*/
	@Column(name = "ATTENDANCEITEM_TYPE")
	public int attendanceItemType;
}
