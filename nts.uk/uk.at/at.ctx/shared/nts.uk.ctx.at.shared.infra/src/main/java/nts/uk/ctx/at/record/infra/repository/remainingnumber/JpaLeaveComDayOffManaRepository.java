package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.base.TargetSelectionAtr;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveComDayOffManaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.subhdmana.KrcmtLeaveDayOffMana;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.subhdmana.KrcmtLeaveDayOffManaPK;

@Stateless
public class JpaLeaveComDayOffManaRepository extends JpaRepository implements LeaveComDayOffManaRepository{

	private final String QUERY = "SELECT lc FROM KrcmtLeaveDayOffMana lc";
	private final String QUERY_BY_LEAVEID = String.join(" ", QUERY," WHERE lc.krcmtLeaveDayOffManaPK.leaveID =:leaveID");
	private final String QUERY_BY_COMDAYOFFID = String.join(" ", QUERY," WHERE lc.krcmtLeaveDayOffManaPK.comDayOffID =:comDayOffID");
	private final String QUERY_BY_LIST_LEAVEID = String.join(" ", QUERY," WHERE lc.krcmtLeaveDayOffManaPK.leaveID IN :leaveID");
	
	private static final String GET_LEAVE_COM  = "SELECT c FROM KrcmtLeaveDayOffMana c "
			+ " WHERE c.krcmtLeaveDayOffManaPK.leaveID = :leaveID";
	
	@Override
	public void add(LeaveComDayOffManagement domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(LeaveComDayOffManagement domain) {
		KrcmtLeaveDayOffManaPK key = new KrcmtLeaveDayOffManaPK(domain.getLeaveID(), domain.getComDayOffID());
		Optional<KrcmtLeaveDayOffMana> existed = this.queryProxy().find(key, KrcmtLeaveDayOffMana.class);
		if (existed.isPresent()){
			this.commandProxy().update(toEntity(domain));
		}
	}

	@Override
	public void delete(String leaveID, String comDayOffID) {
		KrcmtLeaveDayOffManaPK key = new KrcmtLeaveDayOffManaPK(leaveID, comDayOffID);
		Optional<KrcmtLeaveDayOffMana> existed = this.queryProxy().find(key, KrcmtLeaveDayOffMana.class);
		if (existed.isPresent()){
			this.commandProxy().remove(KrcmtLeaveDayOffMana.class, key);
		}
		
	}
	
	/** 
	 * Convert from enity to domain
	 * @param entity
	 * @return
	 */
	private LeaveComDayOffManagement toDomain(KrcmtLeaveDayOffMana entity){
		return new LeaveComDayOffManagement(entity.krcmtLeaveDayOffManaPK.leaveID, entity.krcmtLeaveDayOffManaPK.comDayOffID, entity.usedDays, entity.usedHours, entity.targetSelectionAtr);
	}
	
	/**
	 * Convert from domain to entity
	 * @param domain
	 * @return
	 */
	private KrcmtLeaveDayOffMana toEntity(LeaveComDayOffManagement domain){
		KrcmtLeaveDayOffManaPK key = new KrcmtLeaveDayOffManaPK(domain.getLeaveID(), domain.getComDayOffID());
		return new KrcmtLeaveDayOffMana(key, domain.getUsedDays().v(), domain.getUsedHours().v(), domain.getTargetSelectionAtr().value);
	}

	@Override
	public List<LeaveComDayOffManagement> getByLeaveID(String leaveID) {
		List<KrcmtLeaveDayOffMana> listLeaveD = this.queryProxy().query(QUERY_BY_LEAVEID,KrcmtLeaveDayOffMana.class)
				.setParameter("leaveID", leaveID).getList();
		return listLeaveD.stream().map(item->toDomain(item)).collect(Collectors.toList());
	}

	@Override
	public List<LeaveComDayOffManagement> getBycomDayOffID(String comDayOffID) {
		List<KrcmtLeaveDayOffMana> listLeaveD = this.queryProxy().query(QUERY_BY_COMDAYOFFID,KrcmtLeaveDayOffMana.class)
				.setParameter("comDayOffID", comDayOffID).getList();
		return listLeaveD.stream().map(item->toDomain(item)).collect(Collectors.toList());
	}
	
	@Override
	public void insertAll(List<LeaveComDayOffManagement> entitiesLeave) {
		List<KrcmtLeaveDayOffMana> entities = entitiesLeave.stream()
				.map(c -> newEntities(c.getLeaveID(),c.getComDayOffID(),c.getUsedDays().v(),TargetSelectionAtr.MANUAL.value,TargetSelectionAtr.MANUAL.value)).collect(Collectors.toList());
		commandProxy().insertAll(entities);
		this.getEntityManager().flush();
	}
	
	private KrcmtLeaveDayOffMana newEntities(String leaveID, String comDayOffID, BigDecimal usedDays,int usedHours,int targetSelectionAtr) {
		return new KrcmtLeaveDayOffMana(new KrcmtLeaveDayOffManaPK(leaveID, comDayOffID),
				usedDays,usedHours,targetSelectionAtr);
	}

	@Override
	public void deleteByLeaveId(String leaveId) {
		List<KrcmtLeaveDayOffMana> data = this.queryProxy().query(GET_LEAVE_COM,KrcmtLeaveDayOffMana.class)
				.setParameter("leaveID", leaveId)
				.getList();
		this.commandProxy().removeAll(data);
		this.getEntityManager().flush();
	}
	
	public List<LeaveComDayOffManagement> getByListComLeaveID(List<String> listLeaveID) {
		List<KrcmtLeaveDayOffMana> listLeaveD = this.queryProxy().query(QUERY_BY_LIST_LEAVEID,KrcmtLeaveDayOffMana.class)
				.setParameter("leaveID", listLeaveID).getList();
		return listLeaveD.stream().map(item->toDomain(item)).collect(Collectors.toList());
	}

	@Override
	public void deleteByComDayOffID(String comDayOffID) {
		List<KrcmtLeaveDayOffMana> data = this.queryProxy().query(QUERY_BY_COMDAYOFFID,KrcmtLeaveDayOffMana.class)
				.setParameter("comDayOffID", comDayOffID)
				.getList();
		this.commandProxy().removeAll(data);
		this.getEntityManager().flush();
	}

	
	@Override
	public void deleteByComDayOffId(String comDayOffId) {
		Optional<KrcmtLeaveDayOffMana> entity = this.queryProxy()
				.query(QUERY_BY_COMDAYOFFID, KrcmtLeaveDayOffMana.class).setParameter("comDayOffId", comDayOffId)
				.getSingle();
		if(entity.isPresent()){
			this.commandProxy().remove(entity.get());
		}
	}
}
