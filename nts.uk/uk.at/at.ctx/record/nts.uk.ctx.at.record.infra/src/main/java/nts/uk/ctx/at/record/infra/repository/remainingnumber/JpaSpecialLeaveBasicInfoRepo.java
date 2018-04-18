package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.spLea.basicInfo.KrcmtSpecialLeaveInfo;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.spLea.basicInfo.KrcmtSpecialLeaveInfoPK;

@Stateless
public class JpaSpecialLeaveBasicInfoRepo extends JpaRepository implements SpecialLeaveBasicInfoRepository{

	private String FIND_QUERY = "SELECT s FROM KrcmtSpecialLeaveInfo s WHERE s.key.employeeId = :employeeId ";
	
	private String FIND_QUERY_BYSIDCD = String.join(" ", FIND_QUERY, "AND s.key.spLeaveCD = :spLeaveCD");
	
	@Override
	public List<SpecialLeaveBasicInfo> listSPLeav(String sid) {
		List<SpecialLeaveBasicInfo> result = this.queryProxy().query(FIND_QUERY,KrcmtSpecialLeaveInfo.class)
		.setParameter("employeeId", sid).getList(item ->{
			return toDomain(item);
		});
		return result;
	}

	@Override
	public void add(SpecialLeaveBasicInfo domain) {
		this.commandProxy().insert(toEntity(domain));
		
	}

	@Override
	public void update(SpecialLeaveBasicInfo domain) {
		KrcmtSpecialLeaveInfoPK key  = new KrcmtSpecialLeaveInfoPK(domain.getSID(), domain.getSpecialLeaveCode().v());
		Optional<KrcmtSpecialLeaveInfo> entity = this.queryProxy().find(key, KrcmtSpecialLeaveInfo.class);
		if (!entity.isPresent()){
			return;
		}
		
		updateEntity(domain, entity.get());
		
		this.commandProxy().update(entity.get());
		
	}

	@Override
	public void delete(String sID, int spLeavCD) {
		KrcmtSpecialLeaveInfoPK key = new KrcmtSpecialLeaveInfoPK(sID,spLeavCD);
		this.commandProxy().remove(KrcmtSpecialLeaveInfo.class, key);
	}
	
	/**
	 * Update to entity
	 * @param domain
	 * @return
	 */
	private void updateEntity(SpecialLeaveBasicInfo domain, KrcmtSpecialLeaveInfo entity){
		entity.appSetting = domain.getApplicationSet().value;
		entity.grantDate = domain.getGrantSetting().getGrantDate();
		if (domain.getGrantSetting().getGrantDays().isPresent()){
			entity.grantNumber = domain.getGrantSetting().getGrantDays().get().v();
		}
		if (domain.getGrantSetting().getGrantTable().isPresent()){
			entity.grantTable = domain.getGrantSetting().getGrantTable().get().v();
		}
	}
	
	/**
	 * Convert to entity
	 * @param domain
	 * @return
	 */
	private KrcmtSpecialLeaveInfo toEntity(SpecialLeaveBasicInfo domain){
		KrcmtSpecialLeaveInfo entity = new KrcmtSpecialLeaveInfo();
		entity.cID= domain.getCID();
		KrcmtSpecialLeaveInfoPK key  = new KrcmtSpecialLeaveInfoPK(domain.getSID(), domain.getSpecialLeaveCode().v());
		entity.key = key;
		entity.appSetting = domain.getApplicationSet().value;
		entity.grantDate = domain.getGrantSetting().getGrantDate();
		if (domain.getGrantSetting().getGrantDays().isPresent()){
			entity.grantNumber = domain.getGrantSetting().getGrantDays().get().v();
		}
		if (domain.getGrantSetting().getGrantTable().isPresent()){
			entity.grantTable = domain.getGrantSetting().getGrantTable().get().v();
		}
		
		return entity;
	}
	
	/**
	 * Convert to domain
	 * @param entity
	 * @return
	 */
	private SpecialLeaveBasicInfo toDomain(KrcmtSpecialLeaveInfo entity) {
		return new SpecialLeaveBasicInfo(entity.cID, entity.key.employeeId, entity.key.spLeaveCD, entity.useCls,
				entity.appSetting, entity.grantDate, entity.grantNumber, entity.grantTable);
	}

	@Override
	public Optional<SpecialLeaveBasicInfo> getBySidLeaveCd(String sid, int spLeaveCD) {
		Optional<SpecialLeaveBasicInfo> result = this.queryProxy().query(FIND_QUERY_BYSIDCD,KrcmtSpecialLeaveInfo.class)
				.setParameter("employeeId", sid)
				.setParameter("spLeaveCD", spLeaveCD).getSingle(item->toDomain(item));
		return result;
	}

}
