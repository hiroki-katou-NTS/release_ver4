package nts.uk.cnv.infra.entity.uktabledesign;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ScvmtUkColumnDesignPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TABLE_ID")
	public String tableId;

	@Column(name = "BRANCH")
	private String branch;

	@Column(name = "DATE")
	private GeneralDate date;

	@Column(name = "ID")
	public int id;

}
