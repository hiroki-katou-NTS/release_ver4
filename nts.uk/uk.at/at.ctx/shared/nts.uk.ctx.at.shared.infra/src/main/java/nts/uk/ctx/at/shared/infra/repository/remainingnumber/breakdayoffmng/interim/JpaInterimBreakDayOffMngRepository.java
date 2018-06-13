package nts.uk.ctx.at.shared.infra.repository.remainingnumber.breakdayoffmng.interim;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.SelectedAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseTime;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.breakdayoff.interim.KrcmtInterimBreakDayOff;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.breakdayoff.interim.KrcmtInterimBreakDayOffPK;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.breakdayoff.interim.KrcmtInterimBreakMng;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.breakdayoff.interim.KrcmtInterimDayOffMng;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaInterimBreakDayOffMngRepository extends JpaRepository implements InterimBreakDayOffMngRepository{
	
	private static final String QUERY_BREAK_MNG = "SELECT c FROM KrcmtInterimBreakDayOff c"
			+ " WHERE c.breakDayOffKey.breakMngId = :mngId"
			+ " AND c.breakMngAtr = :mngAtr";
	private static final String QUERY_DAYOFF_MNG = "SELECT c FROM KrcmtInterimBreakDayOff c"
			+ " WHERE c.breakDayOffKey.dayOffMngId = :mngId"
			+ " AND c.dayOffMngAtr = :mngAtr";
	private static final String QUERY_BY_EXPIRATIONDATE = "SELECT c FROM KrcmtInterimBreakMng c"
			+ " WHERE c.breakMngId IN :breakMngIds"
			+ " AND c.unUsedDays > :unUsedDays"
			+ " AND c.expirationDate >= :startDate"
			+ " AND c.expirationDate <= :endDate";
	private static final String QUERY_BREAK_BYID = "SELECT c FROM KrcmtInterimBreakDayOff c"
			+ " WHERE c.breakDayOffKey.breakMngId = :mngId";
	private static final String QUERY_DAYOFF_BY_ID = "SELECT c FROM KrcmtInterimBreakDayOff c"
			+ " WHERE c.breakDayOffKey.dayOffMngId = :mngId";
	private static final String QUERY_BYID_AND_ATR = "SELECT c FROM KrcmtInterimBreakDayOff c"
			+ " WHERE c.breakDayOffKey.breakMngId = :breakId"
			+ " AND c.breakDayOffKey.dayOffMngId = :dayOffId"
			+ " AND c.breakMngAtr = :breakAtr"
			+ " AND c.dayOffMngAtr = :dayOffAtr";
	
	@Override
	public Optional<InterimBreakMng> getBreakManaBybreakMngId(String breakManaId) {
		return this.queryProxy().find(breakManaId, KrcmtInterimBreakMng.class)
				.map(x -> toDomainBreakMng(x));
	}
	private InterimBreakMng toDomainBreakMng(KrcmtInterimBreakMng x) {		
		return new InterimBreakMng(x.breakMngId, 
				new AttendanceTime(x.occurrenceTimes),
				x.expirationDate,
				new OccurrenceTime(x.occurrenceTimes), 
				new OccurrenceDay(x.occurrenceDays),
				new AttendanceTime(x.haftDayEquiTime),
				new UnUsedTime(x.unUsedTimes),
				new UnUsedDay(x.unUsedDays));
	}
	
	@Override
	public Optional<InterimDayOffMng> getDayoffById(String dayOffManaId) {
		return this.queryProxy().find(dayOffManaId, KrcmtInterimDayOffMng.class)
				.map(x -> toDomainDayoffMng(x));
	}
	private InterimDayOffMng toDomainDayoffMng(KrcmtInterimDayOffMng x) {
		return new InterimDayOffMng(x.dayOffMngId, new RequiredTime(x.requiredTimes),
				new RequiredDay(x.requiredDays),
				new UnOffsetTime(x.unOffSetTimes),
				new UnOffsetDay(x.unOffsetDays));
	}
	
	@Override
	public List<InterimBreakDayOffMng> getBreakDayOffMng(String mngId, boolean breakDay, DataManagementAtr mngAtr) {
		return this.queryProxy().query(breakDay ? QUERY_BREAK_MNG : QUERY_DAYOFF_MNG, KrcmtInterimBreakDayOff.class)
				.setParameter("mngId", mngId)
				.setParameter("mngAtr", mngAtr.values)
				.getList(x -> toDomainBreakDayoffMng(x));
				
	}
	private InterimBreakDayOffMng toDomainBreakDayoffMng(KrcmtInterimBreakDayOff x) {		
		return new InterimBreakDayOffMng(x.breakDayOffKey.breakMngId,
				EnumAdaptor.valueOf(x.breakMngAtr, DataManagementAtr.class),
				x.breakDayOffKey.dayOffMngId,
				EnumAdaptor.valueOf(x.dayOffMngAtr, DataManagementAtr.class),
				new UseTime(x.userTimes),
				new UseDay(x.userDays),
				EnumAdaptor.valueOf(x.selectedAtr, SelectedAtr.class));
	}
	
	@Override
	public List<InterimBreakMng> getByPeriod(List<String> mngId, Double unUseDays, DatePeriod dateData) {
		if(mngId.isEmpty()) {
			return Collections.emptyList();
		}
		return this.queryProxy().query(QUERY_BY_EXPIRATIONDATE, KrcmtInterimBreakMng.class)
				.setParameter("breakMngIds", mngId)
				.setParameter("unUsedDays", unUseDays)
				.setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.getList(c -> toDomainBreakMng(c));
	}
	
	@Override
	public void persistAndUpdateInterimBreakMng(InterimBreakMng domain) {

		// キー
		val key = domain.getBreakMngId();
		
		// 登録・更新
		KrcmtInterimBreakMng entity = this.getEntityManager().find(KrcmtInterimBreakMng.class, key);
		if (entity == null){
			entity = new KrcmtInterimBreakMng();
			entity.breakMngId = domain.getBreakMngId();
			entity.oneDayEquivalentTime = domain.getOnedayTime().v();
			entity.expirationDate = domain.getExpirationDate();
			entity.occurrenceTimes = domain.getOccurrenceTimes().v();
			entity.occurrenceDays = domain.getOccurrenceDays().v();
			entity.haftDayEquiTime = domain.getHaftDayTime().v();
			entity.unUsedTimes = domain.getUnUsedTimes().v();
			entity.unUsedDays = domain.getUnUsedDays().v();
			this.getEntityManager().persist(entity);
		}
		else {
			entity.oneDayEquivalentTime = domain.getOnedayTime().v();
			entity.expirationDate = domain.getExpirationDate();
			entity.occurrenceTimes = domain.getOccurrenceTimes().v();
			entity.occurrenceDays = domain.getOccurrenceDays().v();
			entity.haftDayEquiTime = domain.getHaftDayTime().v();
			entity.unUsedTimes = domain.getUnUsedTimes().v();
			entity.unUsedDays = domain.getUnUsedDays().v();
		}
	}

	@Override
	public void deleteInterimBreakMng(String mngId) {
		Optional<KrcmtInterimBreakMng> optData = this.queryProxy().find(mngId, KrcmtInterimBreakMng.class);
		optData.ifPresent(x -> {
			this.commandProxy().remove(KrcmtInterimBreakMng.class, mngId);
		});
	}
	
	@Override
	public void persistAndUpdateInterimDayOffMng(InterimDayOffMng domain) {
		
		// キー
		val key = domain.getDayOffManaId();
		
		// 登録・更新
		KrcmtInterimDayOffMng entity = this.getEntityManager().find(KrcmtInterimDayOffMng.class, key);
		if (entity == null){
			entity = new KrcmtInterimDayOffMng();
			entity.dayOffMngId = domain.getDayOffManaId();
			entity.requiredTimes = domain.getRequiredTime().v();
			entity.requiredDays = domain.getRequiredDay().v();
			entity.unOffSetTimes = domain.getUnOffsetTimes().v();
			entity.unOffsetDays = domain.getUnOffsetDay().v();
			this.getEntityManager().persist(entity);
		}
		else {
			entity.requiredTimes = domain.getRequiredTime().v();
			entity.requiredDays = domain.getRequiredDay().v();
			entity.unOffSetTimes = domain.getUnOffsetTimes().v();
			entity.unOffsetDays = domain.getUnOffsetDay().v();
		}
	}

	@Override
	public void deleteInterimDayOffMng(String mngId) {
		Optional<KrcmtInterimDayOffMng> optData = this.queryProxy().find(mngId, KrcmtInterimDayOffMng.class);
		optData.ifPresent(x -> {
			this.commandProxy().remove(KrcmtInterimDayOffMng.class, mngId);
		});
	}

	@Override
	public void persistAndUpdateInterimBreakDayOffMng(InterimBreakDayOffMng domain) {

		// キー
		val key = new KrcmtInterimBreakDayOffPK(domain.getBreakManaId(), domain.getDayOffManaId());
		
		// 登録・更新
		KrcmtInterimBreakDayOff entity = this.getEntityManager().find(KrcmtInterimBreakDayOff.class, key);
		if (entity == null){
			entity = new KrcmtInterimBreakDayOff();
			entity.breakDayOffKey = new KrcmtInterimBreakDayOffPK();
			entity.breakDayOffKey.breakMngId = domain.getBreakManaId();
			entity.breakDayOffKey.dayOffMngId = domain.getDayOffManaId();
			entity.breakMngAtr = domain.getBreakManaAtr().values;
			entity.dayOffMngAtr = domain.getDayOffManaAtr().values;
			entity.userTimes = domain.getUseTimes().v();
			entity.userDays = domain.getUseDays().v();
			entity.selectedAtr = domain.getSelectedAtr().value;
			this.getEntityManager().persist(entity);
		}
		else {
			entity.breakMngAtr = domain.getBreakManaAtr().values;
			entity.dayOffMngAtr = domain.getDayOffManaAtr().values;
			entity.userTimes = domain.getUseTimes().v();
			entity.userDays = domain.getUseDays().v();
			entity.selectedAtr = domain.getSelectedAtr().value;
		}
	}

	@Override
	public void deleteBreakDayOffById(String mngId, boolean isBreak) {
		List<KrcmtInterimBreakDayOff> lstEntity = this.queryProxy().query(isBreak ? QUERY_BREAK_BYID : QUERY_DAYOFF_BY_ID, KrcmtInterimBreakDayOff.class)
				.setParameter("mngId", mngId)
				.getList();
		lstEntity.stream().forEach(x -> {
			this.commandProxy().remove(x);
		});
	}

	@Override
	public void deleteBreakDayOfByIdAndAtr(String breakId, String dayOffId, DataManagementAtr breakAtr,
			DataManagementAtr dayOffAtr) {
		Optional<KrcmtInterimBreakDayOff>  optEntity = this.queryProxy().query(QUERY_BYID_AND_ATR,KrcmtInterimBreakDayOff.class)
				.setParameter("breakId", breakId)
				.setParameter("dayOffId", dayOffId)
				.setParameter("breakAtr", breakAtr)
				.setParameter("dayOffAtr", dayOffAtr)
				.getSingle();				
		optEntity.ifPresent(x -> {
			this.commandProxy().remove(x);
		});
		
	}

	@Override
	public void deleteBreakDayOfById(String mngId, DataManagementAtr mngAtr, boolean isBreak) {
		List<KrcmtInterimBreakDayOff> lstEntity = this.queryProxy().query(isBreak ? QUERY_BREAK_MNG : QUERY_DAYOFF_MNG, KrcmtInterimBreakDayOff.class)
			.setParameter("mngId", mngId)
			.setParameter("mngAtr", mngAtr.values)
			.getList();
		lstEntity.stream().forEach(x -> {
			this.commandProxy().remove(x);
		});
	}
}
