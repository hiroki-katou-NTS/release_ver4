package nts.uk.ctx.at.shared.infra.repository.scherec.appreflectprocess.appreflectcondition.cancellation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.cancellation.ApplicationReflectHistory;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.cancellation.ApplicationReflectHistoryRepo;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.cancellation.AttendanceBeforeApplicationReflect;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateSetting;
import nts.uk.ctx.at.shared.infra.entity.scherec.appreflectprocess.appreflectcondition.cancellation.KsrdtReflectAppHist;
import nts.uk.ctx.at.shared.infra.entity.scherec.appreflectprocess.appreflectcondition.cancellation.KsrdtReflectAppHistPK;
import nts.uk.ctx.at.shared.infra.entity.scherec.appreflectprocess.appreflectcondition.cancellation.KsrdtReflectAppHistRestore;
import nts.uk.ctx.at.shared.infra.entity.scherec.appreflectprocess.appreflectcondition.cancellation.KsrdtReflectAppHistRestorePK;

@Stateless
public class JpaApplicationReflectHistoryRepo extends JpaRepository implements ApplicationReflectHistoryRepo {

	private final static String UPDATE_FLAG;
	private final static String FIND;
	private final static String FIND_AFTER_MAX;
	private final static String FIND_APP_NOT_REF;
	private final static String FIND_APP_REF;
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE KsrdtReflectAppHist hist ");
		builder.append("SET hist.deleteAtr = :deleteAtr ");
		builder.append("WHERE hist.pk.sid = :sid ");
		builder.append("AND hist.pk.date = :date ");
		builder.append("AND hist.pk.appId = :appId ");
		builder.append("AND hist.pk.atr = :atr");
		UPDATE_FLAG = builder.toString();

		builder = new StringBuilder();
		builder.append(
				"SELECT hist.SID, hist.YMD, hist.APP_ID, hist.ATR, hist.REFLECT_TIME, hist.DELETE_ATR,  rest.ATTENDANCE_ID,  rest.VALUE, rest.STATUS ");
		builder.append("FROM KSRDT_REFLECT_APP_HIST hist ");
		builder.append("JOIN KSRDT_REFLECT_APP_HIST_RESTORE rest ON  hist.SID =  rest.SID AND  hist.YMD =  rest.YMD ");
		builder.append(
				"and hist.APP_ID = rest.APP_ID and hist.ATR = rest.ATR and hist.REFLECT_TIME = rest.REFLECT_TIME ");
		FIND = builder.toString();

		builder = new StringBuilder();
		builder.append(FIND);
		builder.append(
				"WHERE  hist.SID =  @SID AND hist.YMD =  @YMD  AND hist.ATR =  @ATR AND hist.DELETE_ATR =  @DELETE_ATR AND hist.REFLECT_TIME > @REFLECT_TIME");
		FIND_AFTER_MAX = builder.toString();

		builder = new StringBuilder();
		builder.append(FIND);
		builder.append(
				"WHERE  hist.SID =  @SID AND hist.APP_ID =  @APP_ID AND hist.YMD =  @YMD  AND hist.ATR =  @ATR AND hist.DELETE_ATR =  @DELETE_ATR ");
		builder.append(
				"ORDER BY hist.DELETE_ATR DESC ");
		FIND_APP_NOT_REF = builder.toString();

		builder = new StringBuilder();
		builder.append(FIND);
		builder.append(
				"WHERE  hist.SID =  @SID AND hist.YMD =  @YMD  AND hist.ATR =  @ATR AND hist.DELETE_ATR =  @DELETE_ATR AND hist.REFLECT_TIME = @REFLECT_TIME ");
		builder.append(
				"ORDER BY hist.DELETE_ATR DESC ");
		FIND_APP_REF = builder.toString();

	}

	@Override
	public List<ApplicationReflectHistory> findAppReflectHistAfterMaxTime(String sid, GeneralDate baseDate,
			ScheduleRecordClassifi classification, boolean flgRemove, GeneralDateTime reflectionTime) {
		List<ApplicationReflectHistory> lstResult = new NtsStatement(FIND_AFTER_MAX, this.jdbcProxy())
				.paramString("SID", sid).paramDate("YMD", baseDate).paramInt("ATR", classification.value)
				.paramInt("DELETE_ATR", flgRemove ? 1 : 0).paramString("REFLECT_TIME", reflectionTime.toString())
				.getList(x -> toDomain(x));
		return lstResult.stream().collect(Collectors.toMap(x -> getKey(x), x -> x, (x, y) -> {
			x.getLstAttBeforeAppReflect().addAll(y.getLstAttBeforeAppReflect());
			return x;
		})).values().stream().collect(Collectors.toList());
	}

	@Override
	public void insertAppReflectHist(String cid, ApplicationReflectHistory hist) {
		Optional<KsrdtReflectAppHist> findData = this.queryProxy().find(new KsrdtReflectAppHistPK(hist.getEmployeeId(), hist.getDate(), hist.getApplicationId(),
				hist.getClassification().value, hist.getReflectionTime()), KsrdtReflectAppHist.class);
		if (!findData.isPresent()) {
			this.commandProxy().insert(toEntityHist(cid, hist));
			this.commandProxy().insertAll(toEntityRestore(cid, hist));
		}else {
			List<KsrdtReflectAppHistRestore> lstRestore = toEntityRestore(cid, hist);
			lstRestore.forEach(x -> {
				val data = this.queryProxy().find(x.pk, KsrdtReflectAppHistRestore.class);
				if (!data.isPresent()) {
					this.commandProxy().insert(x);
				} 
			});
		}
		this.getEntityManager().flush();
	}

	@Override
	public void updateAppReflectHist(String sid, String appId, GeneralDate baseDate,
			ScheduleRecordClassifi classification, boolean flagRemove) {
		this.queryProxy().query(UPDATE_FLAG, KsrdtReflectAppHist.class).setParameter("sid", sid)
				.setParameter("appId", appId).setParameter("date", baseDate).setParameter("atr", classification.value);
	}

	@Override
	public List<ApplicationReflectHistory> findAppReflectHist(String sid, String appId, GeneralDate baseDate,
			ScheduleRecordClassifi classification, boolean flgRemove) {
		List<ApplicationReflectHistory> lstResult = new NtsStatement(FIND_APP_NOT_REF, this.jdbcProxy())
				.paramString("SID", sid).paramDate("YMD", baseDate).paramInt("ATR", classification.value)
				.paramInt("DELETE_ATR", flgRemove ? 1 : 0).paramString("APP_ID", appId).getList(x -> toDomain(x));
		return lstResult.stream().collect(Collectors.toMap(x -> getKey(x), x -> x, (x, y) -> {
			x.getLstAttBeforeAppReflect().addAll(y.getLstAttBeforeAppReflect());
			return x;
		})).values().stream().collect(Collectors.toList());
	}

	@Override
	public List<ApplicationReflectHistory> findAppReflectHistDateCond(String sid, GeneralDate baseDate,
			ScheduleRecordClassifi classification, boolean flgRemove, GeneralDateTime reflectionTime) {
		List<ApplicationReflectHistory> lstResult = new NtsStatement(FIND_APP_REF, this.jdbcProxy())
				.paramString("SID", sid).paramDate("YMD", baseDate).paramInt("ATR", classification.value)
				.paramInt("DELETE_ATR", flgRemove ? 1 : 0).paramString("REFLECT_TIME", reflectionTime.toString())
				.getList(x -> toDomain(x));
		return lstResult.stream().collect(Collectors.toMap(x -> getKey(x), x -> x, (x, y) -> {
			x.getLstAttBeforeAppReflect().addAll(y.getLstAttBeforeAppReflect());
			return x;
		})).values().stream().collect(Collectors.toList());
	}

	private KsrdtReflectAppHist toEntityHist(String cid, ApplicationReflectHistory dom) {
		return new KsrdtReflectAppHist(new KsrdtReflectAppHistPK(dom.getEmployeeId(), dom.getDate(),
				dom.getApplicationId(), dom.getClassification().value, dom.getReflectionTime()), cid, 
				dom.isCancellationCate() ? 1 : 0);
	}

	private List<KsrdtReflectAppHistRestore> toEntityRestore(String cid, ApplicationReflectHistory dom) {
		return dom.getLstAttBeforeAppReflect().stream().map(x -> {
			return new KsrdtReflectAppHistRestore(
					new KsrdtReflectAppHistRestorePK(dom.getEmployeeId(), dom.getDate(), dom.getApplicationId(),
							dom.getClassification().value, dom.getReflectionTime(), x.getAttendanceId()), cid, 
					x.getValue().orElse(null), x.getEditState().map(y -> y.getEditStateSetting().value).orElse(null));
		}).collect(Collectors.toList());
	}

	private ApplicationReflectHistory toDomain(NtsResultRecord rec) {
		AttendanceBeforeApplicationReflect restore = new AttendanceBeforeApplicationReflect(rec.getInt("ATTENDANCE_ID"),
				Optional.ofNullable(rec.getString("VALUE")),
				Optional.ofNullable(rec.getInt("STATUS") == null ? null
						: new EditStateOfDailyAttd(rec.getInt("ATTENDANCE_ID"),
								EnumAdaptor.valueOf(rec.getInt("STATUS"), EditStateSetting.class))));
		List<AttendanceBeforeApplicationReflect> lstRestore = new ArrayList<>();
		lstRestore.add(restore);
		return new ApplicationReflectHistory(rec.getString("SID"), rec.getGeneralDate("YMD"), rec.getString("APP_ID"),
				rec.getGeneralDateTime("REFLECT_TIME"),
				EnumAdaptor.valueOf(rec.getInt("ATR"), ScheduleRecordClassifi.class), rec.getInt("DELETE_ATR") == 1,
				lstRestore);

	}

	private String getKey(ApplicationReflectHistory x) {
		return x.getEmployeeId().toString() + "-" + x.getApplicationId() + "-" + x.getClassification().value + "-"
				+ x.getReflectionTime().toString();
	}
}
