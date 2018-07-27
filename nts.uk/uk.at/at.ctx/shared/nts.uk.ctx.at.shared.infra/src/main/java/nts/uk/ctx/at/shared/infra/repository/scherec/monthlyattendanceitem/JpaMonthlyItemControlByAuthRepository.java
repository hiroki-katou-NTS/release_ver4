package nts.uk.ctx.at.shared.infra.repository.scherec.monthlyattendanceitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.DisplayAndInputMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.MonthlyItemControlByAuthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.MonthlyItemControlByAuthority;
import nts.uk.ctx.at.shared.infra.entity.scherec.monthlyattendanceitem.KrcstDisplayAndInputMonthly;

@Stateless
public class JpaMonthlyItemControlByAuthRepository  extends JpaRepository implements MonthlyItemControlByAuthRepository {

	private static final String SELECT_BY_AUTHORITY_MONTHLY_ID = "SELECT c FROM KrcstDisplayAndInputMonthly c"
			+ " WHERE c.krcstDisplayAndInputMonthlyPK.companyID = :companyID"
			+ " AND c.krcstDisplayAndInputMonthlyPK.authorityMonthlyID = :authorityMonthlyID"
			+ " ORDER BY c.krcstDisplayAndInputMonthlyPK.itemMonthlyID";
//	
//	private final String SELECT_BY_AUTHORITY_MONTHLY_ID_AND_TO_USE = "SELECT c FROM KrcstDisplayAndInputMonthly c"
//			+ " WHERE c.krcstDisplayAndInputMonthlyPK.companyID = :companyID"
//			+ " AND c.krcstDisplayAndInputMonthlyPK.authorityMonthlyID = :authorityMonthlyID"
//			+ " AND c.krcstDisplayAndInputMonthlyPK.itemMonthlyID  = :itemMonthlyID "
//			+ " AND c.toUse = :toUse "
//			+ " ORDER BY c.krcstDisplayAndInputMonthlyPK.itemMonthlyID";
	
	@Override
	public List<MonthlyItemControlByAuthority> getListMonthlyAttendanceItemAuthority(String companyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<MonthlyItemControlByAuthority> getMonthlyAttdItem(String companyID, String authorityMonthlyId) {
		List<DisplayAndInputMonthly> data = this.queryProxy().query(SELECT_BY_AUTHORITY_MONTHLY_ID,KrcstDisplayAndInputMonthly.class)
				.setParameter("companyID", companyID)
				.setParameter("authorityMonthlyID", authorityMonthlyId)
				.getList(c->c.toDomain());
		if(data.isEmpty())
			return Optional.empty();
		MonthlyItemControlByAuthority monthlyItemControlByAuthority = new MonthlyItemControlByAuthority(
				companyID,authorityMonthlyId,data
				);
		return Optional.of(monthlyItemControlByAuthority);
	}

	@Override
	public void updateMonthlyAttdItemAuth(MonthlyItemControlByAuthority monthlyItemControlByAuthority) {
		List<KrcstDisplayAndInputMonthly> newEntity = monthlyItemControlByAuthority.getListDisplayAndInputMonthly().stream()
				.map(c->KrcstDisplayAndInputMonthly.toEntity(
						monthlyItemControlByAuthority.getCompanyId(),
						monthlyItemControlByAuthority.getAuthorityMonthlyId(), c))
				.collect(Collectors.toList());
		List<KrcstDisplayAndInputMonthly> updateEntity = this.queryProxy().query(SELECT_BY_AUTHORITY_MONTHLY_ID,KrcstDisplayAndInputMonthly.class)
				.setParameter("companyID", monthlyItemControlByAuthority.getCompanyId())
				.setParameter("authorityMonthlyID", monthlyItemControlByAuthority.getAuthorityMonthlyId())
				.getList();
		for(int i=0;i<newEntity.size();i++) {
			for(int j =0;j<updateEntity.size();j++) {
				if(newEntity.get(i).krcstDisplayAndInputMonthlyPK.itemMonthlyID ==updateEntity.get(j).krcstDisplayAndInputMonthlyPK.itemMonthlyID) {
					updateEntity.get(j).toUse = newEntity.get(i).toUse;
					if(newEntity.get(i).toUse == 1) {
						updateEntity.get(j).canBeChangedByOthers = newEntity.get(i).canBeChangedByOthers;
						updateEntity.get(j).youCanChangeIt = newEntity.get(i).youCanChangeIt;
					}
					this.commandProxy().update(updateEntity.get(j));
					break;
				}
			}
		}
		
	}

	@Override
	public void addMonthlyAttdItemAuth(MonthlyItemControlByAuthority monthlyItemControlByAuthority) {
		List<KrcstDisplayAndInputMonthly> newEntity = monthlyItemControlByAuthority.getListDisplayAndInputMonthly().stream()
				.map(c->KrcstDisplayAndInputMonthly.toEntity(
						monthlyItemControlByAuthority.getCompanyId(),
						monthlyItemControlByAuthority.getAuthorityMonthlyId(), c))
				.collect(Collectors.toList());
		for(KrcstDisplayAndInputMonthly krcstDisplayAndInputMonthly:newEntity) {
			this.commandProxy().insert(krcstDisplayAndInputMonthly);
		} 
	}

	private final String SELECT_BY_AUTHORITY_MONTHLY_LIST_ID = "SELECT c FROM KrcstDisplayAndInputMonthly c"
			+ " WHERE c.krcstDisplayAndInputMonthlyPK.companyID = :companyID"
			+ " AND c.krcstDisplayAndInputMonthlyPK.authorityMonthlyID = :authorityMonthlyID"
			+ " AND c.krcstDisplayAndInputMonthlyPK.itemMonthlyID  IN  :itemMonthlyIDs"
			+ " AND c.toUse = :toUse "
			+ " ORDER BY c.krcstDisplayAndInputMonthlyPK.itemMonthlyID";
	
	@Override
	public Optional<MonthlyItemControlByAuthority> getMonthlyAttdItemByUse(String companyID, String authorityMonthlyId,
			List<Integer> itemMonthlyIDs, int toUse) {
		List<DisplayAndInputMonthly> data = new  ArrayList<>();
		CollectionUtil.split(itemMonthlyIDs, 1000, subIdList -> {
			data.addAll(
					this.queryProxy().query(SELECT_BY_AUTHORITY_MONTHLY_LIST_ID,KrcstDisplayAndInputMonthly.class)
					.setParameter("companyID", companyID)
					.setParameter("authorityMonthlyID", authorityMonthlyId)
					.setParameter("itemMonthlyIDs", itemMonthlyIDs)
					.setParameter("toUse", toUse)
					.getList(c->c.toDomain()));
			
		});
		if(data.isEmpty())
			return Optional.empty();
		MonthlyItemControlByAuthority monthlyItemControlByAuthority = new MonthlyItemControlByAuthority(
				companyID,authorityMonthlyId,data
				);
		return Optional.of(monthlyItemControlByAuthority);
	}

}
