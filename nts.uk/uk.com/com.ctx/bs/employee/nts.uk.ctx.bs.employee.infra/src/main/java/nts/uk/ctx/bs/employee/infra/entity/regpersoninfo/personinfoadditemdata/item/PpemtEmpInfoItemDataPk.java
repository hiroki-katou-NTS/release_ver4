package nts.uk.ctx.bs.employee.infra.entity.regpersoninfo.personinfoadditemdata.item;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PpemtEmpInfoItemDataPk implements Serializable {

	private static final long serialVersionUID = 1L;

	// 個人情報項目定義ID
	@Basic(optional = false)
	@Column(name = "PER_INFO_DEF_ID")
	public String perInfoDefId;

	// レコードID

	@Basic(optional = false)
	@Column(name = "RECORD_ID")
	public String recordId;

}
