package nts.uk.ctx.basic.infra.entity.organization.payclassification;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "QMNMT_PAYCLASS")
public class QmnmtPayClass implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QmnmtPayClassPK qmnmtPayClassPK;
	

	@Basic(optional = false)
	@Column(name = "MEMO")
	public String memo;

	
	@Basic(optional = false)
	@Column(name = "PAYCLASS_NAME")
	public String payClassificationName;
}
