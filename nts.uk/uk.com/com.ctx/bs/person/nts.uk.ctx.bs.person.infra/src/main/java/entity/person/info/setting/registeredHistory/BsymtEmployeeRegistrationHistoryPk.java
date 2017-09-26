package entity.person.info.setting.registeredHistory;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BsymtEmployeeRegistrationHistoryPk implements Serializable {

	
	@Basic(optional = false)
	@Column(name = "REG_SID")
	public String RegisteredEmployeeID;
	
	
	private static final long serialVersionUID = 1L;

}
