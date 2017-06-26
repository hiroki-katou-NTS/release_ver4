package nts.uk.ctx.pr.core.infra.entity.personalinfo.holiday;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PhldtHolidayPaidPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "PID")
	public String pId;
	
	@Column(name = "GRANT_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate grantDate;
}
