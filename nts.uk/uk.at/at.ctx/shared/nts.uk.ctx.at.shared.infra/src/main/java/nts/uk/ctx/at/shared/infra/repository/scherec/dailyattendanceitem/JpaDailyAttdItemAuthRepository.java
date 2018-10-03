package nts.uk.ctx.at.shared.infra.repository.scherec.dailyattendanceitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.DailyAttendanceItemAuthority;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.DisplayAndInputControl;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.repository.DailyAttdItemAuthRepository;
import nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem.KshstDailyServiceTypeControl;
import nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem.KshstDailyServiceTypeControlPK;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class JpaDailyAttdItemAuthRepository extends JpaRepository implements DailyAttdItemAuthRepository {

	private static final String SELECT_BY_AUTHORITY_DAILY_ID = "SELECT c FROM KshstDailyServiceTypeControl c"
			+ " WHERE c.kshstDailyServiceTypeControlPK.companyID = :companyID"
			+ " AND c.kshstDailyServiceTypeControlPK.authorityDailyID = :authorityDailyID"
			+ " ORDER BY c.kshstDailyServiceTypeControlPK.itemDailyID";

	private static final String SELECT_BY_KEY = "SELECT c FROM KshstDailyServiceTypeControl c"
			+ " WHERE c.kshstDailyServiceTypeControlPK.companyID = :companyID"
			+ " AND c.kshstDailyServiceTypeControlPK.authorityDailyID = :authorityDailyID"
			+ " AND c.toUse = :toUse";

	private static final String SELECT_BY_KEY_ATT_ITEM_ID = SELECT_BY_KEY
			+ " AND c.kshstDailyServiceTypeControlPK.itemDailyID IN :itemDailyIDs";

	@Override
	public Optional<DailyAttendanceItemAuthority> getDailyAttdItem(String companyID, String authorityDailyId) {
		List<DisplayAndInputControl> data = this.queryProxy()
				.query(SELECT_BY_AUTHORITY_DAILY_ID, KshstDailyServiceTypeControl.class)
				.setParameter("companyID", companyID).setParameter("authorityDailyID", authorityDailyId)
				.getList(c -> c.toDomain());
		if (data.isEmpty())
			return Optional.empty();
		DailyAttendanceItemAuthority dailyAttendanceItemAuthority = new DailyAttendanceItemAuthority(companyID,
				authorityDailyId, data);
		return Optional.of(dailyAttendanceItemAuthority);
	}

	@Override
	public void updateDailyAttdItemAuth(DailyAttendanceItemAuthority dailyAttendanceItemAuthority) {
		List<KshstDailyServiceTypeControl> newEntity = dailyAttendanceItemAuthority.getListDisplayAndInputControl()
				.stream().map(c -> KshstDailyServiceTypeControl.toEntity(dailyAttendanceItemAuthority.getCompanyID(),
						dailyAttendanceItemAuthority.getAuthorityDailyId(), c))
				.collect(Collectors.toList());
		List<KshstDailyServiceTypeControl> updateEntity = this.queryProxy()
				.query(SELECT_BY_AUTHORITY_DAILY_ID, KshstDailyServiceTypeControl.class)
				.setParameter("companyID", dailyAttendanceItemAuthority.getCompanyID())
				.setParameter("authorityDailyID", dailyAttendanceItemAuthority.getAuthorityDailyId()).getList();
		 //int minCount = Math.min(updateEntity.size(), newEntity.size());
		
		//update item có và xóa item k có
		for (int i = 0; i < updateEntity.size(); i++) {
			boolean checkExist = false;
			for(int j = 0; j < newEntity.size(); j++) {
				if(updateEntity.get(i).kshstDailyServiceTypeControlPK.itemDailyID == newEntity.get(j).kshstDailyServiceTypeControlPK.itemDailyID ) {
					updateEntity.get(i).toUse = newEntity.get(j).toUse;
					if (newEntity.get(j).toUse == 1) {
						updateEntity.get(i).canBeChangedByOthers = newEntity.get(j).canBeChangedByOthers;
						updateEntity.get(i).youCanChangeIt = newEntity.get(j).youCanChangeIt;
					}
					this.commandProxy().update(updateEntity.get(i));
					checkExist = true;
					break;
				}
			}
			if(!checkExist) {
				this.commandProxy().remove(KshstDailyServiceTypeControl.class,updateEntity.get(i).kshstDailyServiceTypeControlPK);
				
			}
			
		}
		
		//add item có
		for (int i = 0; i < newEntity.size(); i++) {
			boolean checkExist = false;
			for(int j = 0; j < updateEntity.size(); j++) {
				if(newEntity.get(i).kshstDailyServiceTypeControlPK.itemDailyID == updateEntity.get(j).kshstDailyServiceTypeControlPK.itemDailyID ) {
					checkExist = true;
					break;
				}
			}
			if(!checkExist) {
				this.commandProxy().insert(newEntity.get(i));
			}
			
		}
	}

	@Override
	public void addDailyAttdItemAuth(DailyAttendanceItemAuthority dailyAttendanceItemAuthority) {
		List<KshstDailyServiceTypeControl> newEntity = dailyAttendanceItemAuthority.getListDisplayAndInputControl()
				.stream().map(c -> KshstDailyServiceTypeControl.toEntity(dailyAttendanceItemAuthority.getCompanyID(),
						dailyAttendanceItemAuthority.getAuthorityDailyId(), c))
				.collect(Collectors.toList());
		for (KshstDailyServiceTypeControl dailyServiceTypeControl : newEntity) {
			this.commandProxy().insert(dailyServiceTypeControl);
		}

	}
	//HoiDD
	private final String SELECT_ALL_BY_AUTHORITY_DAILY_LIST_ID = "SELECT c FROM KshstDailyServiceTypeControl c"
			+ " WHERE c.kshstDailyServiceTypeControlPK.companyID = :companyID"
			+ " AND c.kshstDailyServiceTypeControlPK.authorityDailyID = :authorityDailyID"
			+ " AND c.toUse = :toUse "
			+ " ORDER BY c.kshstDailyServiceTypeControlPK.itemDailyID";
	
	private final String SELECT_BY_AUTHORITY_DAILY_LIST_ID = "SELECT c FROM KshstDailyServiceTypeControl c"
			+ " WHERE c.kshstDailyServiceTypeControlPK.companyID = :companyID"
			+ " AND c.kshstDailyServiceTypeControlPK.authorityDailyID = :authorityDailyID"
			+ " AND c.kshstDailyServiceTypeControlPK.itemDailyID  IN  :itemDailyIDs"
			+ " AND c.toUse = :toUse "
			+ " ORDER BY c.kshstDailyServiceTypeControlPK.itemDailyID";
	
	@Override
	public Optional<DailyAttendanceItemAuthority> getDailyAttdItemByUse(String companyId,
			String roleId,List<Integer> attendanceItemIds,int toUse) {
		List<DisplayAndInputControl> data = new  ArrayList<>();
		CollectionUtil.split(attendanceItemIds, 1000, subIdList -> {
			data.addAll(
					this.queryProxy().query(SELECT_BY_AUTHORITY_DAILY_LIST_ID,KshstDailyServiceTypeControl.class)
					.setParameter("companyID", companyId)
					.setParameter("authorityDailyID", roleId)
					.setParameter("itemDailyIDs", attendanceItemIds)
					.setParameter("toUse", toUse)
					.getList(c->c.toDomain()));
			
		});
		if(CollectionUtil.isEmpty(data))
			return Optional.empty();
		DailyAttendanceItemAuthority dailyItemControlByAuthority = new DailyAttendanceItemAuthority(
				companyId,roleId,data
				);
		return Optional.of(dailyItemControlByAuthority);
	}

	@Override
	public Optional<DailyAttendanceItemAuthority> getAllDailyAttdItemByUse(String companyId, String roleId, int toUse) {
		List<DisplayAndInputControl> data = this.queryProxy()
				.query(SELECT_ALL_BY_AUTHORITY_DAILY_LIST_ID, KshstDailyServiceTypeControl.class)
				.setParameter("companyID", companyId).setParameter("authorityDailyID", roleId)
				.setParameter("toUse", toUse).getList(c -> c.toDomain());

		if (CollectionUtil.isEmpty(data))
			return Optional.empty();
		DailyAttendanceItemAuthority monthlyItemControlByAuthority = new DailyAttendanceItemAuthority(companyId, roleId,
				data);
		return Optional.of(monthlyItemControlByAuthority);
	}

	@Override
	public Optional<DailyAttendanceItemAuthority> getDailyAttdItemByAttItemId(String companyID, String authorityDailyId,
			List<Integer> attendanceItemIds) {
		List<DisplayAndInputControl> data = new ArrayList<>();
		if (attendanceItemIds == null || attendanceItemIds.isEmpty()) {
			data = this.queryProxy().query(SELECT_BY_KEY, KshstDailyServiceTypeControl.class)
					.setParameter("companyID", companyID).setParameter("authorityDailyID", authorityDailyId)
					.setParameter("toUse", NotUseAtr.USE.value)
					.getList(c -> c.toDomain());
		} else {
			data = this.queryProxy().query(SELECT_BY_KEY_ATT_ITEM_ID, KshstDailyServiceTypeControl.class)
					.setParameter("companyID", companyID).setParameter("authorityDailyID", authorityDailyId)
					.setParameter("toUse", NotUseAtr.USE.value).setParameter("itemDailyIDs", attendanceItemIds)
					.getList(c -> c.toDomain());
		}
		if (data.isEmpty())
			return Optional.empty();
		DailyAttendanceItemAuthority dailyAttendanceItemAuthority = new DailyAttendanceItemAuthority(companyID,
				authorityDailyId, data);
		return Optional.of(dailyAttendanceItemAuthority);
	}

}
