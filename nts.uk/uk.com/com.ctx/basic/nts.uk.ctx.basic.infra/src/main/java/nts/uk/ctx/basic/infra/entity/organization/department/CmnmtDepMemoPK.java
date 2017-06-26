package nts.uk.ctx.basic.infra.entity.organization.department;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CmnmtDepMemoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	@NotNull
	private String companyCode;

	@Column(name = "HIST_ID")
	@NotNull
	private String historyId;

}
