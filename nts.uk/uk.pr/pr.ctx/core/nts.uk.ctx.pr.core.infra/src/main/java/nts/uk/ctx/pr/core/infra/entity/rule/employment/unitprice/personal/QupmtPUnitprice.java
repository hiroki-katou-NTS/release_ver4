package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.personal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author sonnh
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "QUPMT_P_UNITPRICE")
public class QupmtPUnitprice extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QupmtPUnitpricePK qupmtPUnitpricePK;

	@Column(name = "P_UNITPRICE_NAME")
	public String personalUnitPriceName;

	@Column(name = "P_UNITPRICE_AB_NAME")
	public String personalUnitPriceShortName;

	@Column(name = "DISP_SET")
	public int displaySet;

	@Column(name = "UNITE_CD")
	public String uniteCode;

	@Column(name = "FIX_PAY_SET")
	public int paymentSettingType;

	@Column(name = "FIX_PAY_ATR")
	public int fixPaymentAtr;

	@Column(name = "FIX_PAY_ATR_MONTHLY")
	public int fixPaymentMonthly;

	@Column(name = "FIX_PAY_ATR_DAYMONTH")
	public int fixPaymentDayMonth;

	@Column(name = "FIX_PAY_ATR_DAILY")
	public int fixPaymentDaily;

	@Column(name = "FIX_PAY_ATR_HOURLY")
	public int fixPaymentHoursly;

	@Column(name = "UNITPRICE_ATR")
	public int unitPriceAtr;

	@Column(name = "MEMO")
	public String memo;

	@Override
	protected QupmtPUnitpricePK getKey() {
		return this.qupmtPUnitpricePK;
	}

}
