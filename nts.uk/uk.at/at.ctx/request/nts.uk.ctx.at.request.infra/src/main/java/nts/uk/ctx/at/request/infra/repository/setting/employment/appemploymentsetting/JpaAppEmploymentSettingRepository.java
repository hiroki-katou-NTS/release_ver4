package nts.uk.ctx.at.request.infra.repository.setting.employment.appemploymentsetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;


import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSettingRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.BreakOrRestTime;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.HolidayType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.WorkTypeObjAppHoliday;
import nts.uk.ctx.at.request.infra.entity.setting.employment.appemploymentsetting.KrqdtAppEmployWorktype;
import nts.uk.ctx.at.request.infra.entity.setting.employment.appemploymentsetting.KrqdtAppEmployWorktypePK;
import nts.uk.ctx.at.request.infra.entity.setting.employment.appemploymentsetting.KrqstAppEmploymentSet;
import nts.uk.ctx.at.request.infra.entity.setting.employment.appemploymentsetting.KrqstAppEmploymentSetPK;

@Stateless
public class JpaAppEmploymentSettingRepository extends JpaRepository implements AppEmploymentSettingRepository {
	private static final String FINDER_ALL ="SELECT e FROM KrqdtAppEmployWorktype e";
	private static final String FINDER_ALL_EMPLOYMENT_SET ="SELECT e FROM KrqstAppEmploymentSet e";
	private static final String FIND_EMPLOYMENT_SET = "SELECT c FROM KrqstAppEmploymentSet c "
			+ "WHERE c.krqstAppEmploymentSetPK.cid = :companyId "
			+ "AND c.krqstAppEmploymentSetPK.employmentCode = :employmentCode AND c.krqstAppEmploymentSetPK.appType = :appType";
			
	private static final String FIND_EMPLOYMENT_BY_COMPANYID = FINDER_ALL_EMPLOYMENT_SET + " WHERE e.krqstAppEmploymentSetPK.cid = :companyId";
	private static final String FIND_EMPLOYMENT_BY_EMPLOYMENTCD = FIND_EMPLOYMENT_BY_COMPANYID + " AND e.krqstAppEmploymentSetPK.employmentCode = :employmentCode";
	
	private static final String FIND;
	static{
		StringBuilder query = new StringBuilder();
		query.append(FINDER_ALL); 
		query.append(" WHERE e.krqdtAppEmployWorktypePK.cid = :companyID");
		query.append(" AND e.krqdtAppEmployWorktypePK.employmentCode = :employmentCode");
		query.append(" AND e.krqdtAppEmployWorktypePK.appType = :appType");
		query.append(" ORDER BY e.krqdtAppEmployWorktypePK.workTypeCode ASC");
		FIND = query.toString();
	}
	
	private static final String DELETE_WORKTYPE_SET = "DELETE FROM KrqdtAppEmployWorktype c "
			+ "WHERE c.krqdtAppEmployWorktypePK.cid =:companyId "
			+ "AND c.krqdtAppEmployWorktypePK.employmentCode =:employmentCode "
			+ "AND c.krqdtAppEmployWorktypePK.appType =:appType "
			+ "AND c.krqdtAppEmployWorktypePK.holidayOrPauseType =:holidayOrPauseType ";
	private static final String DELETE_WORKTYPE_SET_BY_CODE = "DELETE FROM KrqdtAppEmployWorktype c "
			+ "WHERE c.krqdtAppEmployWorktypePK.cid =:companyId "
			+ "AND c.krqdtAppEmployWorktypePK.employmentCode =:employmentCode ";
	private static final String DELETE_EMPLOYMENT_SET = "DELETE FROM KrqstAppEmploymentSet c "
			+ "WHERE c.krqstAppEmploymentSetPK.cid =:companyId "
			+ "AND c.krqstAppEmploymentSetPK.employmentCode =:employmentCode ";
	@Override
	public List<AppEmployWorkType> getEmploymentWorkType(String companyID, String employmentCode, int appType) {
		return this.queryProxy().query(FIND,KrqdtAppEmployWorktype.class)
				.setParameter("companyID", companyID).setParameter("employmentCode", employmentCode).setParameter("appType", appType).getList(c -> convertToDomain(c));
	}

	public Optional<AppEmploymentSetting> getEmploymentSetting(String companyId, String employmentCode, int appType){
		List<KrqstAppEmploymentSet> list = this.queryProxy().query(FIND_EMPLOYMENT_SET, KrqstAppEmploymentSet.class)
				.setParameter("companyId", companyId)
				.setParameter("employmentCode", employmentCode)
				.setParameter("appType", appType)
				.getList();
		List<AppEmploymentSetting> listReturn;
		if(!list.isEmpty()) {			
			listReturn = toDomain(list, companyId);	
			return Optional.ofNullable(listReturn.get(0));
		}else {
			return Optional.ofNullable(null);
		}
				
	}
	public List<AppEmploymentSetting> getEmploymentSetting(String companyId){
		
		List<KrqstAppEmploymentSet> list = this.queryProxy().query(FIND_EMPLOYMENT_BY_COMPANYID, KrqstAppEmploymentSet.class)
				.setParameter("companyId", companyId)
				.getList();
		if(!list.isEmpty()) {			
			List<AppEmploymentSetting> listReturn = toDomain(list, companyId);
			return listReturn;
		}
		return new ArrayList<>();
				
	}

	public Optional<AppEmploymentSetting> getEmploymentSetting(String companyId, String employmentCode){
		List<KrqstAppEmploymentSet> list = this.queryProxy().query(FIND_EMPLOYMENT_BY_EMPLOYMENTCD, KrqstAppEmploymentSet.class)
				.setParameter("companyId", companyId)
				.setParameter("employmentCode", employmentCode)
				.getList();
		List<AppEmploymentSetting> listReturn;
		if(!list.isEmpty()) {			
			listReturn = toDomain(list, companyId);	
			return Optional.ofNullable(listReturn.get(0));
		}else {
			return Optional.ofNullable(null);
		}
		
				
	}
	/**
	 * Insert list employment setting
	 */

	public void insert(AppEmploymentSetting domain){
		List<KrqstAppEmploymentSet> updateLst;
		if(!CollectionUtil.isEmpty(domain.getListWTOAH())) {
			updateLst = domain.getListWTOAH().stream().map( x ->{
				
				return new KrqstAppEmploymentSet(domain.getVersion(), new KrqstAppEmploymentSetPK(domain.getCompanyID(),
						domain.getEmploymentCode(), 
						x.getAppType().value,
						x.getAppType().value == 1 ? x.getHolidayAppType().get().value : x.getAppType().value == 10 ? x.getSwingOutAtr().get().value : 9),
						x.getHolidayTypeUseFlg().get() ? 1 : 0, x.getWorkTypeSetDisplayFlg() ? 1 : 0,
						CollectionUtil.isEmpty(x.getWorkTypeList()) ? null : x.getWorkTypeList().stream().map(y -> new KrqdtAppEmployWorktype(new KrqdtAppEmployWorktypePK(domain.getCompanyID(), domain.getEmploymentCode(),
								 x.getAppType().value, x.getHolidayAppType().isPresent() ? x.getHolidayAppType().get().value : x.getSwingOutAtr().isPresent() ? x.getSwingOutAtr().get().value : 9, y), null)).collect(Collectors.toList())  		
						);
			}).collect(Collectors.toList());
			if(!CollectionUtil.isEmpty(updateLst)) {
				commandProxy().insertAll(updateLst);
			}
		}
			
	}

	public void update(AppEmploymentSetting domain) {
		updateWorkType(domain);
		List<KrqstAppEmploymentSet> updateLst;
		if(!CollectionUtil.isEmpty(domain.getListWTOAH())) {
			updateLst = domain.getListWTOAH().stream().map( x ->{
				Optional<KrqstAppEmploymentSet> updateItemOp = queryProxy().find(new KrqstAppEmploymentSetPK(
						domain.getCompanyID(), 
						domain.getEmploymentCode(), 
						x.getAppType().value, 
						x.getAppType().value == 1 ? x.getHolidayAppType().get().value : x.getAppType().value == 10 ? x.getSwingOutAtr().get().value : 9 
						), KrqstAppEmploymentSet.class);
				if(updateItemOp.isPresent()) {
					KrqstAppEmploymentSet updateItem = updateItemOp.get();
					Integer holidayTypeUseFlg = null;
					if (x.getHolidayTypeUseFlg() != null) {
						holidayTypeUseFlg = x.getHolidayTypeUseFlg().get() ? 1: 0;
					}
					//updateItem.setVersion(item.getVersion());
					updateItem.setHolidayTypeUseFlg(x.getHolidayTypeUseFlg().get() ? 1 : 0);
					updateItem.setDisplayFlag(x.getWorkTypeSetDisplayFlg() ? 1 : 0);
					updateItem.setHolidayTypeUseFlg(holidayTypeUseFlg);						
					return updateItem;
				}
				return new KrqstAppEmploymentSet(domain.getVersion(), new KrqstAppEmploymentSetPK(domain.getCompanyID(),
						domain.getEmploymentCode(), 
						x.getAppType().value,
						x.getAppType().value == 1 ? x.getHolidayAppType().get().value : x.getAppType().value == 10 ? x.getSwingOutAtr().get().value : 9),
						x.getHolidayTypeUseFlg().get() ? 1 : 0, x.getWorkTypeSetDisplayFlg() ? 1 : 0,new ArrayList<>());
			}).collect(Collectors.toList());
			if(!CollectionUtil.isEmpty(updateLst)) {
				commandProxy().updateAll(updateLst);				
			} 
		}
	}
	public void updateWorkType(AppEmploymentSetting domain) {
		List<KrqdtAppEmployWorktype> list = new ArrayList<>();
		if (!CollectionUtil.isEmpty(domain.getListWTOAH())) {
			domain.getListWTOAH().stream().map(x -> {
				deleteWorkType(domain.getCompanyID(), domain.getEmploymentCode(), 
						x.getAppType().value, x.getSwingOutAtr().isPresent() ? x.getSwingOutAtr().get().value : x.getHolidayAppType().isPresent() ? x.getHolidayAppType().get().value : 9);
				if(!CollectionUtil.isEmpty(x.getWorkTypeList())){	
					return x.getWorkTypeList().stream().map( y-> new KrqdtAppEmployWorktype(new KrqdtAppEmployWorktypePK(
							domain.getCompanyID(),
							domain.getEmploymentCode(),
							x.getAppType().value,
							x.getAppType().value == 1 ? x.getHolidayAppType().get().value : x.getAppType().value == 10 ? x.getSwingOutAtr().get().value : 9 ,
							y), null)).collect(Collectors.toList());
				}
				return null;
			}).filter(x -> x != null).forEach(a -> list.addAll(a));
			
		}
		this.commandProxy().insertAll(list);
	}
	public void remove(String companyId, String employmentCode){			
		//Delete Employment
		this.getEntityManager().createQuery(DELETE_EMPLOYMENT_SET).setParameter("companyId", companyId)
		.setParameter("employmentCode", employmentCode)
		.executeUpdate();
		//Delete work type
		this.getEntityManager().createQuery(DELETE_WORKTYPE_SET_BY_CODE).setParameter("companyId", companyId)
		.setParameter("employmentCode", employmentCode)
		.executeUpdate();
		this.getEntityManager().flush();
	}

	private void deleteWorkType(String companyId, String employmentCode, int appType, int holidayOrPauseType){
		this.getEntityManager().createQuery(DELETE_WORKTYPE_SET).setParameter("companyId", companyId)
		.setParameter("employmentCode", employmentCode)
		.setParameter("appType", appType)
		.setParameter("holidayOrPauseType", holidayOrPauseType)
		.executeUpdate();
		this.getEntityManager().flush();
	}
	/**
	 * Convert employment setting domain to entity object
	 * @param domain
	 * @return
	 */

	
	private AppEmployWorkType convertToDomain(KrqdtAppEmployWorktype entity){
		return AppEmployWorkType.createSimpleFromJavaType(entity.getKrqdtAppEmployWorktypePK().getCid(),
				entity.getKrqdtAppEmployWorktypePK().getEmploymentCode(),
				entity.getKrqdtAppEmployWorktypePK().getAppType(),
				entity.getKrqdtAppEmployWorktypePK().getHolidayOrPauseType(),
				entity.getKrqdtAppEmployWorktypePK().getWorkTypeCode());
	}
	
	private List<AppEmploymentSetting> toDomain(List<KrqstAppEmploymentSet> list, String companyId){
		Map<String, Map<String,List<WorkTypeObjAppHoliday>>> maps = new HashMap<>();
		list.stream().forEach(x -> {
			String cid = x.getKrqstAppEmploymentSetPK().getCid();
			String empCode = x.getKrqstAppEmploymentSetPK().getEmploymentCode();
			WorkTypeObjAppHoliday i = new WorkTypeObjAppHoliday();
			i.setAppType(EnumAdaptor.valueOf(x.getKrqstAppEmploymentSetPK().getAppType(), ApplicationType.class));
			if(x.getKrqstAppEmploymentSetPK().getAppType() == 1) {
				i.setHolidayAppType(Optional.of(EnumAdaptor.valueOf(x.getKrqstAppEmploymentSetPK().getHolidayOrPauseType(), HolidayType.class)));				
			}else {
				i.setHolidayAppType(Optional.ofNullable(null));
			}
			i.setHolidayTypeUseFlg(Optional.of((x.getHolidayTypeUseFlg() == null || x.getHolidayTypeUseFlg() == 0) ? false : true));
			if(x.getKrqstAppEmploymentSetPK().getAppType() == 10) {
				i.setSwingOutAtr(Optional.of(EnumAdaptor.valueOf(x.getKrqstAppEmploymentSetPK().getHolidayOrPauseType(), BreakOrRestTime.class)));			
			}else {
				i.setSwingOutAtr(Optional.ofNullable(null));
			}
			i.setWorkTypeSetDisplayFlg(x.getDisplayFlag() == 1);
			if(!x.getKrqdtAppEmployWorktype().isEmpty()) {	
				i.setWorkTypeList(x.getKrqdtAppEmployWorktype().stream().map(y->y.krqdtAppEmployWorktypePK.getWorkTypeCode()).collect(Collectors.toList()));
				
			}
			 
			if(maps.containsKey(cid)) {
				if(maps.get(cid).containsKey(empCode)){
					maps.get(cid).get(empCode).add(i);
				}else {
					List<WorkTypeObjAppHoliday> list1 = new ArrayList<>();
					list1.add(i);
					maps.get(cid).put(empCode, list1);
				}
			}else {
				List<WorkTypeObjAppHoliday> list1 = new ArrayList<>();
				list1.add(i);
				Map<String,List<WorkTypeObjAppHoliday>> map = new HashMap<>();
				map.put(empCode, list1);
				maps.put(cid, map);
			}
		});
		Map<String,List<WorkTypeObjAppHoliday>> map2 = maps.get(companyId);
		List<AppEmploymentSetting> listReturn = new ArrayList<>();
		for (Map.Entry<String, List<WorkTypeObjAppHoliday>> entry : map2.entrySet()) {
		    if(entry != null) {
		    	AppEmploymentSetting item = new AppEmploymentSetting(companyId, entry.getKey(), entry.getValue());
			    listReturn.add(item);
		    }
		}
		return listReturn;
	}
}
