package nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * entity 暫定年休管理データ
 * @author do_dt
 *
 */
@Entity
@Table(name = "KRCMT_INTERIM_ANNUAL_MNG")
public class KrcmtInterimAnnualMng extends UkJpaEntity{
	/**
	 * 暫定年休管理データID
	 */
	@Id
	@Column(name = "ANNUAL_MNG_ID")	
	public String annualMngId;
	/**
	 * 勤務種類
	 */
	@Column(name = "WORKTYPE_CODE")
	public String workTypeCode;
	/**
	 * 使用日数
	 */
	@Column(name = "USE_DAYS")
	public double useDays;

	@Override
	protected Object getKey() {
		return annualMngId;
	}

	public static final JpaEntityMapper<KrcmtInterimAnnualMng> MAPPER = new JpaEntityMapper<>(KrcmtInterimAnnualMng.class);
	
	public TmpAnnualHolidayMng toDomain() {
		return new TmpAnnualHolidayMng(this.annualMngId, this.workTypeCode, new UseDay(this.useDays));
	}
}
