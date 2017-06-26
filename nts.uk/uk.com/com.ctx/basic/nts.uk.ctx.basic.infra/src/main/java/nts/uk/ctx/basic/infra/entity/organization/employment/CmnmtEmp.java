package nts.uk.ctx.basic.infra.entity.organization.employment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="CMNMT_EMP")
@AllArgsConstructor
@NoArgsConstructor
public class CmnmtEmp extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public CmnmtEmpPK cmnmtEmpPk;
	
	@Column(name = "EMPNAME")
	public String employmentName;
	
	@Column(name = "CLOSE_DATE_NO")
	public int closeDateNo;
	
	@Column(name = "MEMO")
	public String memo;
	
	@Column(name = "PROCESSING_NO")
	public int processingNo;
	
	@Column(name = "STATUTORY_HOLIDAY_ATR")
	public int statutoryHolidayAtr;
	
	@Column(name = "EMP_OUT_CD")
	public String employementOutCd;
	
	@Column(name = "INIT_SELECT_SET")
	public int displayFlg;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return cmnmtEmpPk;
	}
}
