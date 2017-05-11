package nts.uk.ctx.at.record.infra.repository.divergencetime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.divergencetime.AttendanceType;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItem;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItemName;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItemSet;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceItemSet;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceItemSetPK;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceReason;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceReasonPK;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceTime;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceTimePK;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmnmtAttendanceType;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmnmtDivergenceItem;

@Stateless
public class JpaDivergenceTimeRepository extends JpaRepository implements DivergenceTimeRepository {

	private final String SELECT_FROM_DIVTIME = "SELECT c FROM KmkmtDivergenceTime c";
	private final String SELECT_ALL_DIVTIME = SELECT_FROM_DIVTIME 
			+ " WHERE c.kmkmtDivergenceTimePK.companyId = :companyId";
	private final String SELECT_FROM_DIVREASON = "SELECT c FROM KmkmtDivergenceReason c";
	private final String SELECT_ALL_DIVREASON = SELECT_FROM_DIVREASON
			+ " WHERE c.kmkmtDivergenceReasonPK.companyId = :companyId"
			+ " AND c.kmkmtDivergenceReasonPK.divTimeId = :divTimeId";
	private final String SELECT_FROM_DIVERGENCEITEM = "SELECT c FROM KmkmtDivergenceItemSet c";
	private final String SELECT_ALL_DIVERGENCEITEM = SELECT_FROM_DIVERGENCEITEM
			+ " WHERE c.kmkmtDivergenceItemSetPK.companyId = :companyId"
			+ " AND c.kmkmtDivergenceItemSetPK.divTimeId = :divTimeId";
	private final String SELECT_DIVREASON_BY_CODE = SELECT_ALL_DIVREASON 
			+ " AND c.kmkmtDivergenceReasonPK.divReasonCode = :divReasonCode";
	private final String SELECT_FROM_DIVITEM = "SELECT c FROM KmnmtDivergenceItem c";
	private final String SELECT_ALL_DIVITEM = SELECT_FROM_DIVITEM 
			+ " WHERE c.kmnmtDivergenceItemPK.companyId = :companyId";
	private final String SELECT_ITEMNAME =" SELECT a.kmkmtDivergenceItemSetPK.divergenceItemId, b.divName"
			+ " FROM KmkmtDivergenceItemSet a"+" INNER JOIN KmnmtDivergenceItem b" 
			+ " ON a.kmkmtDivergenceItemSetPK.companyId = b.kmnmtDivergenceItemPK.companyId" 
			+ " AND a.kmkmtDivergenceItemSetPK.divTimeId = :divTimeId"
			+ " AND a.kmkmtDivergenceItemSetPK.companyId = :companyId"
			+ " AND a.kmkmtDivergenceItemSetPK.divergenceItemId = b.kmnmtDivergenceItemPK.attendanceItemId";
	private final String SELECT_FROM_ATTTYPE = " SELECT c FROM KmnmtAttendanceType c";
	private final String SELECT_ALL_ATTENDANCEITEM = SELECT_FROM_ATTTYPE
			+ " WHERE c.kmnmtAttendanceTypePK.companyId = :companyId"
			+ " AND c.kmnmtAttendanceTypePK.attendanceItemType = :attendanceItemType";
	private static DivergenceTime toDomainDivTime(KmkmtDivergenceTime entity){
		val domain = DivergenceTime.createSimpleFromJavaType(
				entity.kmkmtDivergenceTimePK.companyId,
				entity.kmkmtDivergenceTimePK.divTimeId,
				entity.getDivTimeName(),
				entity.divTimeUseSet,
				entity.alarmTime,
				entity.errTime,
				entity.selectUseSet,
				entity.cancelErrSelReason,
				entity.inputUseSet,
				entity.cancelErrInputReason);
		return domain;
	}
	private static DivergenceReason toDomainDivReason(KmkmtDivergenceReason entity){
		val domain = DivergenceReason.createSimpleFromJavaType(
				entity.kmkmtDivergenceReasonPK.companyId,
				entity.kmkmtDivergenceReasonPK.divTimeId,
				entity.kmkmtDivergenceReasonPK.divReasonCode,
				entity.divReason,
				entity.requiredAtr);
		return domain;
	}
	private static DivergenceItemSet toDomainAttendanceItem(KmkmtDivergenceItemSet entity){
		val domain = DivergenceItemSet.createSimpleFromJavaType(
				entity.kmkmtDivergenceItemSetPK.companyId,
				entity.kmkmtDivergenceItemSetPK.divTimeId,
				entity.kmkmtDivergenceItemSetPK.divergenceItemId);
		return domain;
	}
	private static DivergenceItem toDomainDivItem(KmnmtDivergenceItem entity){
		val domain = DivergenceItem.createSimpleFromJavaType(
				entity.kmnmtDivergenceItemPK.companyId,
				entity.kmnmtDivergenceItemPK.attendanceItemId,
				entity.divName,
				entity.displayNumber,
				entity.useAtr,
				entity.attendanceAtr);
		return domain;
	}
	private static AttendanceType toDomainAttType(KmnmtAttendanceType entity){
		val domain = AttendanceType.createSimpleFromJavaType(
				entity.kmnmtAttendanceTypePK.companyId,
				entity.kmnmtAttendanceTypePK.attendanceItemId,
				entity.kmnmtAttendanceTypePK.attendanceItemType);
		return domain;
	}
	private static KmkmtDivergenceTime toEntityDivTime(DivergenceTime domain){
		val entity = new KmkmtDivergenceTime();
		entity.kmkmtDivergenceTimePK = new KmkmtDivergenceTimePK(
												domain.getCompanyId(),
												domain.getDivTimeId());
		entity.divTimeName = domain.getDivTimeName().toString();
		entity.alarmTime = Integer.valueOf(domain.getAlarmTime().toString());
		entity.errTime = Integer.valueOf(domain.getErrTime().toString());
		entity.selectUseSet = Integer.valueOf(domain.getSelectSet().getSelectUseSet().value);
		entity.cancelErrSelReason = Integer.valueOf(domain.getSelectSet().getCancelErrSelReason().value);
		entity.inputUseSet = Integer.valueOf(domain.getInputSet().getSelectUseSet().value);
		entity.cancelErrInputReason = Integer.valueOf(domain.getInputSet().getCancelErrSelReason().value);
		return entity;
	}
	private static KmkmtDivergenceReason toEntityDivReason(DivergenceReason domain){
		val entity = new KmkmtDivergenceReason();
		entity.kmkmtDivergenceReasonPK = new KmkmtDivergenceReasonPK(domain.getCompanyId(),
												domain.getDivTimeId(),
												domain.getDivReasonCode().v());
		entity.divReason = domain.getDivReasonContent().v();
		entity.requiredAtr = domain.getRequiredAtr().value;
		return entity;
	}
	private static KmkmtDivergenceItemSet toEntityItemSet(DivergenceItemSet domain){
		val entity = new KmkmtDivergenceItemSet();
			entity.kmkmtDivergenceItemSetPK =	new KmkmtDivergenceItemSetPK(
												domain.getCompanyId(),
												domain.getDivTimeId(),
												domain.getDivergenceItemId());
		return entity;
	}
	private static KmkmtDivergenceItemSetPK toEntityItemSetPK(DivergenceItemSet domain){
		val entity = new KmkmtDivergenceItemSetPK(
												domain.getCompanyId(),
												domain.getDivTimeId(),
												domain.getDivergenceItemId());
		return entity;
	}
	/**
	 * get all divergence time
	 * @param companyId
	 * @return
	 */
	@Override
	public List<DivergenceTime> getAllDivTime(String companyId) {
		return this.queryProxy().query(SELECT_ALL_DIVTIME, KmkmtDivergenceTime.class)
				.setParameter("companyId", companyId)
				.getList(c->toDomainDivTime(c));
	}
	/**
	 * get all divergence reason
	 * @param companyId
	 * @param divTimeId
	 * @return
	 */
	@Override
	public List<DivergenceReason> getDivReasonByCode(String companyId, int divTimeId) {
		return this.queryProxy().query(SELECT_ALL_DIVREASON, KmkmtDivergenceReason.class)
				.setParameter("companyId", companyId)
				.setParameter("divTimeId", divTimeId)
				.getList(c->toDomainDivReason(c));
	}
	/**
	 * get all attendance item selected
	 * @param companyId
	 * @param divTimeCode
	 * @return
	 */
	@Override
	public List<DivergenceItemSet> getallItembyCode(String companyId, int divTimeId) {
		return this.queryProxy().query(SELECT_ALL_DIVERGENCEITEM, KmkmtDivergenceItemSet.class)
				.setParameter("companyId", companyId)
				.setParameter("divTimeId", divTimeId)
				.getList(c->toDomainAttendanceItem(c));
	}
	/**
	 * update divergence time
	 * @param divTime
	 */
	@Override
	public void updateDivTime(DivergenceTime divTime) {
		this.commandProxy().update(toEntityDivTime(divTime));
	}
	/**
	 * add divergence reason
	 * @param divReason
	 */
	@Override
	public void addDivReason(DivergenceReason divReason) {
		this.commandProxy().insert(toEntityDivReason(divReason));
	}
	/**
	 * update divergence reason
	 * @param divReason
	 */
	@Override
	public void updateDivReason(DivergenceReason divReason) {
		this.commandProxy().update(toEntityDivReason(divReason));
	}
	/**
	 * delete divergence reason
	 * @param companyId
	 * @param divTimeId
	 * @param divReasonCode
	 */
	@Override
	public void deleteDivReason(String companyId, int divTimeId, String divReasonCode) {
		KmkmtDivergenceReasonPK kmkmtDivergenceReasonPK = new KmkmtDivergenceReasonPK(companyId,divTimeId, divReasonCode);
		this.commandProxy().remove(KmkmtDivergenceReason.class, kmkmtDivergenceReasonPK);
	}
	/**
	 * get divergence time
	 * @param companyId
	 * @param divTimeId
	 * @param divReasonCode
	 * @return
	 */
	@Override
	public Optional<DivergenceReason> getDivReason(String companyId, int divTimeId, String divReasonCode) {
		return this.queryProxy().query(SELECT_DIVREASON_BY_CODE, KmkmtDivergenceReason.class)
				.setParameter("companyId", companyId)
				.setParameter("divTimeId", divTimeId)
				.setParameter("divReasonCode", divReasonCode)
				.getSingle(c->toDomainDivReason(c));
	}

	@Override
	public List<DivergenceItem> getallDivItem(String companyId) {
		return this.queryProxy().query(SELECT_ALL_DIVITEM, KmnmtDivergenceItem.class)
				.setParameter("companyId", companyId)
				.getList(c->toDomainDivItem(c));
	}

	/**
	 * get all name attendance item selected
	 * @param companyId
	 * @param divTimeId
	 * @return
	 */
	@Override
	public List<DivergenceItemName> getItemSelected(String companyId, int divTimeId) {
		return this.queryProxy().query(SELECT_ITEMNAME, Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("divTimeId", divTimeId)
				.getList(c->{
					int id = Integer.valueOf(c[0].toString());
					String name = (String)c[1];
					return DivergenceItemName.createSimpleFromJavaType(id, name);
				});
	}
	/**
	 * get all item
	 * @param companyId
	 * @param type
	 * @return
	 */
	@Override
	public List<AttendanceType> getAllItem(String companyId, int attendanceItemType) {
		return this.queryProxy().query(SELECT_ALL_ATTENDANCEITEM, KmnmtAttendanceType.class)
				.setParameter("companyId", companyId)
				.setParameter("attendanceItemType", attendanceItemType)
				.getList(c->toDomainAttType(c));
	}
	@Override
	public void addItemId(List<DivergenceItemSet> lstItemId) {
		List<KmkmtDivergenceItemSet> listAdd = lstItemId.stream()
						.map(c ->toEntityItemSet(c)).collect(Collectors.toList());
		this.commandProxy().insertAll(listAdd);
	}
	@Override
	public void deleteItemId(String companyId, int divTimeId) {
		List<DivergenceItemSet> list = this.getallItembyCode(companyId, divTimeId);
		List<KmkmtDivergenceItemSetPK> listDel = list.stream().map(c->toEntityItemSetPK(c))
												.collect(Collectors.toList());
		this.commandProxy().removeAll(KmkmtDivergenceItemSet.class, listDel);
		this.getEntityManager().flush();
	}
	
}
