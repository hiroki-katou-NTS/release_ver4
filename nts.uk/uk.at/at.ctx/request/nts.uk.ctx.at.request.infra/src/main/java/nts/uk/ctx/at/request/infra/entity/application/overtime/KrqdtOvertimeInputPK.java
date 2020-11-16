package nts.uk.ctx.at.request.infra.entity.application.overtime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrqdtOvertimeInputPK extends ContractUkJpaEntity implements Serializable{

	public static final long serialVersionUID = 1L;
	
	
	@Column(name = "CID")
	private String cid;

	@Column(name = "APP_ID")
	private String appId;

	@Column(name = "ATTENDANCE_TYPE")
	private Integer attendanceType;

	@Column(name = "FRAME_NO")
	private Integer frameNo;

	
	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
