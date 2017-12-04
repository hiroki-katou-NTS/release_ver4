package nts.uk.ctx.bs.employee.infra.entity.employee.mngdata;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateTimeToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BSYMT_EMP_DTA_MNG_INFO")
public class BsymtEmployeeDataMngInfo extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public BsymtEmployeeDataMngInfoPk bsymtEmployeeDataMngInfoPk;

	@Column(name = "CID")
	public String companyId;

	@Column(name = "SCD")
	public String employeeCode;

	@Column(name = "DEL_STATUS_ATR")
	public int delStatus;

	@Column(name = "DEL_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	public GeneralDate delDateTmp;

	@Column(name = "REMV_REASON")
	public String removeReason;

	@Column(name = "EXT_CD")
	public String extCode;

	@Override
	protected Object getKey() {
		return bsymtEmployeeDataMngInfoPk;
	}
}