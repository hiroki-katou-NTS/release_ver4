package nts.uk.ctx.at.request.infra.repository.application.overtime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertime;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimePK;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInput;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInputPK;

@Stateless
public class OvertimeImpl extends JpaRepository implements OvertimeRepository {
	private static final String FIND_ALL = "SELECT e FROM KrqdtAppOvertime e";

	private static final String FIND_BY_APPID;
	static {
		StringBuilder query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" WHERE e.krqdtAppOvertimePK.cid = :companyID");
		query.append(" AND e.krqdtAppOvertimePK.appId = :appID");
		FIND_BY_APPID = query.toString();
	}

	@Override
	public Optional<AppOverTime> getAppOvertime(String companyID, String appID) {

		return this.queryProxy().query(FIND_BY_APPID, KrqdtAppOvertime.class).setParameter("companyID", companyID)
				.setParameter("appID", appID).getSingle(e -> convertToDomain(e));
	}

	@Override
	public void Add(AppOverTime domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	private KrqdtAppOvertime toEntity(AppOverTime domain) {
		List<KrqdtOvertimeInput> overtimeInputs = domain.getOverTimeInput().stream()
				.map(item -> {
					KrqdtOvertimeInputPK pk =  new KrqdtOvertimeInputPK(item.getCompanyID(), item.getAppID(),
							item.getAttendanceID().value, item.getFrameNo(),item.getTimeItemTypeAtr().value);
					return new KrqdtOvertimeInput(pk, item.getStartTime().v(), item.getEndTime().v(),
							item.getApplicationTime().v());
				})
				.collect(Collectors.toList());

		return new KrqdtAppOvertime(new KrqdtAppOvertimePK(domain.getCompanyID(), domain.getAppID()),
				domain.getOverTimeAtr().value, domain.getWorkTypeCode().v(), domain.getSiftCode().v(),
				domain.getWorkClockFrom1(), domain.getWorkClockTo1(), domain.getWorkClockFrom2(),
				domain.getWorkClockTo2(), domain.getDivergenceReason(), domain.getFlexExessTime(),
				domain.getOverTimeShiftNight(), overtimeInputs);
	}

	private AppOverTime convertToDomain(KrqdtAppOvertime entity) {
		return AppOverTime.createSimpleFromJavaType(entity.getKrqdtAppOvertimePK().getCid(),
				entity.getKrqdtAppOvertimePK().getAppId(), entity.getOvertimeAtr(), entity.getWorkTypeCode(),
				entity.getSiftCode(), entity.getWorkClockFrom1(), entity.getWorkClockTo1(), entity.getWorkClockFrom2(),
				entity.getWorkClockTo2(), entity.getDivergenceReason(), entity.getFlexExcessTime(),
				entity.getOvertimeShiftNight());
	}

}
