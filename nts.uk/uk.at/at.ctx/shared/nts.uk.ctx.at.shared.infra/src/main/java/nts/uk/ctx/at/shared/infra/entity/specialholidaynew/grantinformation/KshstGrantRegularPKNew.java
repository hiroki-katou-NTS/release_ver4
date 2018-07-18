package nts.uk.ctx.at.shared.infra.entity.specialholidaynew.grantinformation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KshstGrantRegularPKNew implements Serializable {
	private static final long serialVersionUID = 1L;

	/* 会社ID */
	@Column(name = "CID")
	public String companyId;

	/* 特別休暇コード */
	@Column(name = "SPHD_CD")
	public int specialHolidayCode;
}