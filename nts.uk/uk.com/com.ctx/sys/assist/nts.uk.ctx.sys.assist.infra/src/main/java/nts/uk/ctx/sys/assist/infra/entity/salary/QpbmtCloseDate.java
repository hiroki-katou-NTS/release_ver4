package nts.uk.ctx.sys.assist.infra.entity.salary;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.sys.assist.dom.salary.CloseDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 勤怠締め年月日
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_CLOSE_DATE")
public class QpbmtCloseDate extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public QpbmtCloseDatePk closeDatePk;

	/**
	 * 勤怠締め日
	 */
	@Basic(optional = false)
	@Column(name = "TIME_CLOSE_DATE")
	public int timeCloseDate;

	/**
	 * 基準月
	 */
	@Basic(optional = true)
	@Column(name = "BASE_MONTH")
	public int baseMonth;

	/**
	 * 基準年
	 */
	@Basic(optional = true)
	@Column(name = "BASE_YEAR")
	public int baseYear;

	/**
	 * 基準日
	 */
	@Basic(optional = true)
	@Column(name = "REFE_DATE")
	public int refeDate;

	@Override
	protected Object getKey() {
		return closeDatePk;
	}

	public CloseDate toDomain() {
		return new CloseDate(this.closeDatePk.processCateNo, this.closeDatePk.cid, this.timeCloseDate, this.baseMonth,
				this.baseYear, this.refeDate);
	}

	public static QpbmtCloseDate toEntity(CloseDate domain) {

		return new QpbmtCloseDate(new QpbmtCloseDatePk(domain.getProcessCateNo(), domain.getCid()),
				domain.getTimeCloseDate(),
				Objects.isNull(domain.getBaseMonth()) ? domain.getBaseMonth().get().value : null,
				Objects.isNull(domain.getBaseYear()) ? domain.getBaseYear().get().value : null,
				Objects.isNull(domain.getRefeDate()) ? domain.getRefeDate().get().value : null);
	}

}
