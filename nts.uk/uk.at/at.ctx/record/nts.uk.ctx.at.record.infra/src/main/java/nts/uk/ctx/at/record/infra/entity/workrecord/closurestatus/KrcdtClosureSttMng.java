package nts.uk.ctx.at.record.infra.entity.workrecord.closurestatus;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author HungTT - 締め状態管理
 *
 */

@NoArgsConstructor
@Entity
@Table(name = "KRCDT_CLOSURE_STT_MNG")
public class KrcdtClosureSttMng extends UkJpaEntity {

	@EmbeddedId
	public KrcdtClosureSttMngPk pk;

	// 実行期間
	@Column(name = "START_DATE")
	public GeneralDate start;

	// 実行期間
	@Column(name = "END_DATE")
	public GeneralDate end;

	// @Column(name = "STATUS")
	// public int status;

	public KrcdtClosureSttMng(int yearMonth, String employeeId, int closureId, ClosureDate closureDate,
			DatePeriod period) {
		super();
		this.pk = new KrcdtClosureSttMngPk(yearMonth, employeeId, closureId, closureDate.getClosureDay().v(),
				closureDate.getLastDayOfMonth() ? 1 : 0);
		this.start = period.start();
		this.end = period.end();
		// this.status = status;
	}

	public static KrcdtClosureSttMng fromDomain(ClosureStatusManagement domain) {
		return new KrcdtClosureSttMng(domain.getYearMonth().v(), domain.getEmployeeId(), domain.getClosureId().value,
				domain.getClosureDate(), domain.getPeriod());
	}

	public ClosureStatusManagement toDomain() {
		return new ClosureStatusManagement(new YearMonth(this.pk.yearMonth), this.pk.employeeId, this.pk.closureId,
				new ClosureDate(this.pk.closeDay, this.pk.isLastDay == 1), new DatePeriod(this.start, this.end));
	}
	
	public static ClosureStatusManagement toDomain(KrcdtClosureSttMng entity) {
		return entity.toDomain();
	}

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public KrcdtClosureSttMng(KrcdtClosureSttMngPk pk, GeneralDate start, GeneralDate end) {
		super();
		this.pk = pk;
		this.start = start;
		this.end = end;
	}

	
}
