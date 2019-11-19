package nts.uk.ctx.hr.develop.infra.entity.interviewrecord;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class JflmtInterviewAnalysisPK implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CID")
	public String companyId;
	
	@NotNull
	@Column(name = "INTERVIEW_CONTENT_ID")
	public String interviewContentId;
}
