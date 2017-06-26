package nts.uk.ctx.basic.infra.entity.organization.classification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CMNMT_CLASS")
@AllArgsConstructor
@NoArgsConstructor
public class CmnmtClass implements Serializable {

	private static final long serialVersionUID = 4161327404158928689L;

	@EmbeddedId
	private CmnmtClassPK cmnmtClassPK;

	@Column(name = "CLSNAME")
	private String name;

	@Column(name = "CLS_OUT_CD")
	private String outCode;

	@Column(name = "MEMO")
	private String memo;
}
