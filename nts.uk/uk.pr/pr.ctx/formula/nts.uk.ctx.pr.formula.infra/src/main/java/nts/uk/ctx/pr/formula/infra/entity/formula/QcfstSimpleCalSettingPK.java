package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class QcfstSimpleCalSettingPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ITEM_CODE")
	public String itemCode;

	@Column(name = "CTG_ATR")
	public BigDecimal ctgAtr;
}
