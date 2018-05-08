package nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.old;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KrcmtDaiPerformanceAutPk {
	
	@Column(name = "CID")
	public String companyId;
	
	@Column(name = "ROLE_ID")
	public String roleId;

	@Column(name = "FUNCTION_NO")
	public BigDecimal functionNo;

	public KrcmtDaiPerformanceAutPk() {
		super();
	}

	public KrcmtDaiPerformanceAutPk(String companyId, String roleId, BigDecimal functionNo) {
		super();
		this.companyId = companyId;
		this.roleId = roleId;
		this.functionNo = functionNo;
	}

}
