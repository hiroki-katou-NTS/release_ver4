package nts.uk.ctx.at.record.infra.entity.daily.attendanceleavinggate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnNo;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The persistent class for the KRCDT_DAY_PC_LOGON_INFO database table.
 * 
 */
@Entity
@Table(name = "KRCDT_DAY_PC_LOGON_INFO")
// @NamedQuery(name="KrcdtDayPcLogonInfo.findAll", query="SELECT k FROM
// KrcdtDayPcLogonInfo k")
public class KrcdtDayPcLogonInfo extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtDayPcLogonInfoPK id;

	@Column(name = "LOGOFF_TIME")
	public Integer logoffTime;

	@Column(name = "LOGON_TIME")
	public Integer logonTime;

	public KrcdtDayPcLogonInfo() {
	}

	public KrcdtDayPcLogonInfo(KrcdtDayPcLogonInfoPK id) {
		super();
		this.id = id;
	}

	@Override
	protected Object getKey() {
		return this.id;
	}

	public static List<KrcdtDayPcLogonInfo> from(PCLogOnInfoOfDaily domain) {
		return domain.getLogOnInfo().stream().map(c -> 
	 							from(domain.getEmployeeId(), domain.getYmd(), c)
		).collect(Collectors.toList());
	}
	
	public static KrcdtDayPcLogonInfo from(String eId, GeneralDate ymd, LogOnInfo domain) {
		KrcdtDayPcLogonInfo entity = new KrcdtDayPcLogonInfo(new KrcdtDayPcLogonInfoPK(eId, ymd, domain.getWorkNo().v()));
		entity.setData(domain);
		return entity;
	}
	
	public LogOnInfo toDomain() {
		return new LogOnInfo(new PCLogOnNo(id.pcLogNo), toTimeWithDay(logoffTime), toTimeWithDay(logonTime));
	}
	
	public void setData(LogOnInfo c) {
		this.logonTime = c.getLogOn().isPresent() ? c.getLogOn().get().valueAsMinutes() : null;
		this.logoffTime = c.getLogOff().isPresent() ? c.getLogOff().get().valueAsMinutes() : null;
//		c.getLogOn().ifPresent(lo -> {
//			this.logonTime = lo.valueAsMinutes();
//		});
//		c.getLogOff().ifPresent(lo -> {
//			this.logoffTime = lo.valueAsMinutes();
//		});
	}

	private TimeWithDayAttr toTimeWithDay(Integer time) {
		return time == null ? null : new TimeWithDayAttr(time);
	}
	
	public static final JpaEntityMapper<KrcdtDayPcLogonInfo> MAPPER = new JpaEntityMapper<>(KrcdtDayPcLogonInfo.class);
}
