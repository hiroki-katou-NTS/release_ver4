package nts.uk.ctx.exio.infra.entity.input.revise.type.codeconvert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class XimmtCodeConvertDetailPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CID")
	private String companyId;
	
	@Column(name = "CONVERT_CODE")
	private String convertCode;
	
	@Column(name = "TARGET_CODE")
	private String targetCode;
}
