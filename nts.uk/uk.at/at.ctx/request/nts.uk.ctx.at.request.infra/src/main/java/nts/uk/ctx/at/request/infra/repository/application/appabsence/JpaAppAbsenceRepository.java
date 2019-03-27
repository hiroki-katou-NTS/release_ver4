package nts.uk.ctx.at.request.infra.repository.application.appabsence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.infra.entity.application.appabsence.KrqdtAppForLeave;
import nts.uk.ctx.at.request.infra.entity.application.appabsence.KrqdtAppForLeavePK;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdpApplicationPK_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdtApplication_New;
//import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertime;
//import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimePK;
@Stateless
public class JpaAppAbsenceRepository extends JpaRepository implements AppAbsenceRepository{

	@Override
	public Optional<AppAbsence> getAbsenceById(String companyID, String appId) {
		Optional<KrqdtAppForLeave> opKrqdtAppForLeave = this.queryProxy().find(new KrqdtAppForLeavePK(companyID, appId), KrqdtAppForLeave.class);
		if(!opKrqdtAppForLeave.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppForLeave krqdtAppAbsence = opKrqdtAppForLeave.get();
		AppAbsence appAbsence = krqdtAppAbsence.toDomain();
		return Optional.of(appAbsence);
	}

	@Override
	@SneakyThrows
	public List<AppAbsence> getAbsenceByIds(String companyID, List<String> appId){
		List<AppAbsence> result = new ArrayList<>();
		CollectionUtil.split(appId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ids -> {
			
			result.addAll(internalQuery(companyID, appId));
		});
		
		return result;
	}

	@SneakyThrows
	private List<AppAbsence> internalQuery(String companyID, List<String> appId) {
		try(val st = this.connection().prepareStatement("SELECT * FROM KRQDT_APP_FOR_LEAVE WHERE CID = ? AND APP_ID IN (" 
				+ appId .stream().map(c -> "?").collect(Collectors.joining(", "))+ ")")){
		st.setString(1, companyID);
		for(int i = 0; i < appId.size(); i++){
			st.setString(2 + i, appId.get(i));
		}
		return new NtsResultSet(st.executeQuery()).getList(c -> {
			return new AppAbsence(companyID, c.getString("APP_ID"), c.getInt("HOLIDAY_APP_TYPE"), 
					c.getString("WORKTYPE_CD"), c.getString("WORKTIME_CD"), c.getBoolean("HALF_DAY_FLG"), 
					c.getBoolean("CHANGE_WORK_HOUR"), c.getInt("ALLDAY_HALFDAY_LEAVE_ATR"), 
					c.getInt("START_TIME1"), c.getInt("END_TIME1"), 
					c.getInt("START_TIME2"), c.getInt("END_TIME2"), null);
		});
}
	}

	@Override
	public void insertAbsence(AppAbsence appAbsence) {
		this.commandProxy().insert(toEntity(appAbsence));
	}
	
	private KrqdtAppForLeave toEntity(AppAbsence domain) {

		return new KrqdtAppForLeave(new KrqdtAppForLeavePK(domain.getCompanyID(), domain.getAppID()),
				domain.getVersion(),
				domain.getHolidayAppType() == null ? null : domain.getHolidayAppType().value,
				domain.getWorkTypeCode() == null? null : domain.getWorkTypeCode().toString(),
				domain.getWorkTimeCode() == null ? null : domain.getWorkTimeCode().toString(),
				domain.getStartTime1() == null ? null : domain.getStartTime1().v(),
				domain.getEndTime1() == null ? null : domain.getEndTime1().v(),
				domain.getStartTime2() == null ? null : domain.getStartTime2().v(),
				domain.getEndTime2() == null ? null : domain.getEndTime2().v(),
				domain.isHalfDayFlg(),
				domain.isChangeWorkHour(),
				domain.getAllDayHalfDayLeaveAtr().value
				);
	}

	@Override
	public Optional<AppAbsence> getAbsenceByAppId(String companyID, String appID) {
		Optional<KrqdtAppForLeave> opKrqdtAppForLeave = this.queryProxy().find(new KrqdtAppForLeavePK(companyID, appID), KrqdtAppForLeave.class);
		Optional<KrqdtApplication_New> opKafdtApplication = this.queryProxy().find(new KrqdpApplicationPK_New(companyID, appID), KrqdtApplication_New.class);
		if(!opKrqdtAppForLeave.isPresent() || !opKafdtApplication.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppForLeave krqdtAppAbsence = opKrqdtAppForLeave.get();
		KrqdtApplication_New kafdtApplication = opKafdtApplication.get();
		AppAbsence appAbsence = krqdtAppAbsence.toDomain();
		appAbsence.setApplication(kafdtApplication.toDomain());
		return Optional.of(appAbsence);
	}

	@Override
	public void updateAbsence(AppAbsence appAbsence) {
		this.commandProxy().update(toEntity(appAbsence));
	}

	@Override
	public void delete(String companyID, String appID) {
		Optional<KrqdtAppForLeave> opKrqdtAppForLeave = this.queryProxy().find(new KrqdtAppForLeavePK(companyID, appID), KrqdtAppForLeave.class);
		if(!opKrqdtAppForLeave.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de xoa");
		}
		//Delete application over time
		this.commandProxy().remove(KrqdtAppForLeave.class, new KrqdtAppForLeavePK(companyID, appID));
		
	}

}
