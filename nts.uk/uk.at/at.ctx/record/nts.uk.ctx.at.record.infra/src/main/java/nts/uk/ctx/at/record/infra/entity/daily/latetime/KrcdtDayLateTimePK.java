package nts.uk.ctx.at.record.infra.entity.daily.latetime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KrcdtDayLateTimePK implements Serializable{

	private static final long serialVersionUID = 1L;
	/*社員ID*/
	@Column(name = "SID")
	public String employeeID;
	/*年月日*/
	@Convert(converter = GeneralDateToDBConverter.class)
	@Column(name = "YMD")
	public GeneralDate generalDate;
	/*勤務No*/
	@Column(name = "WORK_NO")
	public int workNo;
}
