package nts.uk.ctx.at.request.infra.repository.setting.company.request;

import java.sql.PreparedStatement;
import java.util.ArrayList;
//import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.setting.company.request.AuthorizationSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.applimitset.AppLimitSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.AfterhandRestriction;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.AppTypeSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.BeforehandRestriction;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.ReceptionRestrictionSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.deadlinesetting.AppDeadlineSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.displaysetting.AppDisplaySetting;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.AppReflectionSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstAppTypeDiscrete;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstAppTypeDiscretePK;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstApplicationSetting;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class JpaRequestSettingRepository extends JpaRepository implements RequestSettingRepository{
	private static final String SELECT_NO_WHERE = "SELECT c FROM KrqstApplicationSetting c ";
	private static final String SELECT_BY_COM = SELECT_NO_WHERE + "WHERE c.krqstApplicationSettingPK.companyID = :companyID";
	/**
	 * ドメインモデル「承認一覧表示設定」を取得する
	 */
	@Override
	public Optional<RequestSetting> findByCompany(String companyID) {
		return this.queryProxy().query(SELECT_BY_COM, KrqstApplicationSetting.class)
								.setParameter("companyID", companyID)
								.getSingle(c->toDomain(c));
	}
	
	private  RequestSetting toDomain(KrqstApplicationSetting entity){
		return RequestSetting.toDomain(
				entity.krqstApplicationSettingPK.companyID, 
				ApplicationSetting.toDomain(
						entity.baseDateFlg, 
						AppDisplaySetting.toDomain(
								entity.displayPrePostFlg, 
								entity.displaySearchTimeFlg, 
								entity.manualSendMailAtr), 
						entity.krqstAppTypeDiscretes.stream()
							.map(x -> ReceptionRestrictionSetting.toDomain(
									x.krqstAppTypeDiscretePK.appType, 
									BeforehandRestriction.toDomain(
											x.retrictPreMethodFlg, 
											x.retrictPreUseFlg, 
											x.retrictPreDay, 
											x.retrictPreTimeDay,
											x.preOtTime,
											x.normalOtTime,
											x.otRestrictPreDay,
											x.otToUse), 
									AfterhandRestriction.toDomain(x.retrictPostAllowFutureFlg)))
							.collect(Collectors.toList()), 
						entity.krqstAppTypeDiscretes.stream()
							.map(x -> AppTypeSetting.toDomain(
									x.prePostInitAtr, 
									x.prePostCanChangeFlg, 
									x.typicalReasonDisplayFlg, 
									x.sendMailWhenApprovalFlg, 
									x.sendMailWhenRegisterFlg, 
									x.displayReasonFlg, 
									x.krqstAppTypeDiscretePK.appType))
							.collect(Collectors.toList()), 
						AppLimitSetting.toDomain(
								entity.appActLockFlg, 
								entity.appEndWorkFlg, 
								entity.appActConfirmFlg, 
								entity.appOvertimeNightFlg, 
								entity.appActMonthConfirmFlg, 
								entity.requireAppReasonFlg), 
						entity.krqstAppDeadlines.stream()
							.map(x -> AppDeadlineSetting.createSimpleFromJavaType(
									x.krqstAppDeadlinePK.companyId, 
									x.krqstAppDeadlinePK.closureId, 
									x.useAtr, 
									x.deadline, 
									x.deadlineCriteria))
							.collect(Collectors.toList())
				),  
				AppReflectionSetting.toDomain(
						entity.scheReflectFlg, 
						entity.priorityTimeReflectFlg, 
						entity.attendentTimeReflectFlg,
						entity.classScheAchi,
						entity.reflecTimeofSche), 
				ApprovalListDisplaySetting.toDomain(
						entity.advanceExcessMessDispAtr, 
						entity.hwAdvanceDispAtr, 
						entity.hwActualDispAtr, 
						entity.actualExcessMessDispAtr, 
						entity.otAdvanceDispAtr, 
						entity.otActualDispAtr, 
						entity.warningDateDispAtr, 
						entity.appReasonDispAtr), 
				AuthorizationSetting.toDomain(
						entity.appContentChangeFlg), 
				AppReflectAfterConfirm.toDomain(
						entity.scheduleConfirmedAtr,
						entity.achievementConfirmedAtr));
	}
	
	/**
	 * convert from list AppDeadlineSetting to list entity
	 * @param list
	 * @return
	 * @author yennth
	 */
//	private static List<KrqstAppDeadline> toEntityAppTypeDead(List<AppDeadlineSetting> list){
//		List<KrqstAppDeadline> listAppDead = new ArrayList<>();
//		String companyId = AppContexts.user().companyId();
//		for(AppDeadlineSetting item : list){
//			val entity = new KrqstAppDeadline();
//			entity.krqstAppDeadlinePK = new KrqstAppDeadlinePK(companyId, item.getClosureId());
//			entity.useAtr = item.getUserAtr().value; 
//			entity.deadline = item.getDeadline().v(); 
//			entity.deadlineCriteria = item.getDeadlineCriteria().value;
//			listAppDead.add(entity);
//		}
//		return listAppDead;
//	}
	/**
	 * convert from domain to entity (objiect)
	 * @param recep
	 * @param App
	 * @return
	 * @author yennth
	 */
	private static KrqstAppTypeDiscrete toEntityDiscrete(ReceptionRestrictionSetting recep, AppTypeSetting App){
		String companyId = AppContexts.user().companyId();
			val entity = new KrqstAppTypeDiscrete();
			entity.krqstAppTypeDiscretePK = new KrqstAppTypeDiscretePK(companyId, recep.getAppType().value);
			entity.retrictPreMethodFlg = recep.getBeforehandRestriction().getMethodCheck().value; 
			entity.retrictPreUseFlg  = recep.getBeforehandRestriction().getToUse() == true ? 1 : 0; 
			entity.retrictPreDay = recep.getBeforehandRestriction().getDateBeforehandRestriction().value; 
			entity.retrictPreTimeDay = recep.getBeforehandRestriction().getTimeBeforehandRestriction().v();
			entity.retrictPostAllowFutureFlg = recep.getAfterhandRestriction().getAllowFutureDay() == true ? 1 : 0;
			entity.prePostInitAtr = App.getDisplayInitialSegment().value; 
			entity.prePostCanChangeFlg = App.getCanClassificationChange() == true ? 1 : 0; 
			entity.typicalReasonDisplayFlg = App.getDisplayFixedReason().value; 
			entity.sendMailWhenApprovalFlg = App.getSendMailWhenApproval() == true ? 1 : 0; 
			entity.sendMailWhenRegisterFlg = App.getSendMailWhenRegister() == true ? 1 : 0; 
			entity.displayReasonFlg = App.getDisplayAppReason().value; 
		return entity;
	}
	
	
	/**
	 * convert from list ReceptionRestrictionSetting to list entity
	 * @param list
	 * @return
	 * @author yennth
	 */
//	private static List<KrqstAppTypeDiscrete> toEntityAppTypeDiscrete(List<ReceptionRestrictionSetting> list, List<AppTypeSetting> listApp){
//		List<KrqstAppTypeDiscrete> listAppDisc = new ArrayList<>();
//		String companyId = AppContexts.user().companyId();
//		list.sort(Comparator.comparing(ReceptionRestrictionSetting::getAppType));
//		listApp.sort(Comparator.comparing(AppTypeSetting::getAppType));
//		for(int i = 0; i< list.size(); i++ ){
//			ReceptionRestrictionSetting item1 = list.get(i);
//			AppTypeSetting item2 = listApp.get(i);
//			val entity = new KrqstAppTypeDiscrete();
//			entity.krqstAppTypeDiscretePK = new KrqstAppTypeDiscretePK(companyId, item1.getAppType().value);
//			entity.retrictPreMethodFlg = item1.getBeforehandRestriction().getMethodCheck().value; 
//			entity.retrictPreUseFlg  = item1.getBeforehandRestriction().getToUse() == true ? 1 : 0; 
//			entity.retrictPreDay = item1.getBeforehandRestriction().getDateBeforehandRestriction().value; 
//			entity.retrictPreTimeDay = item1.getBeforehandRestriction().getTimeBeforehandRestriction().v();
//			entity.krqstAppTypeDiscretePK = new KrqstAppTypeDiscretePK(companyId, item2.getAppType().value);
//			entity.prePostInitAtr = item2.getDisplayInitialSegment().value; 
//			entity.prePostCanChangeFlg = item2.getCanClassificationChange() == true ? 1 : 0; 
//			entity.typicalReasonDisplayFlg = item2.getDisplayFixedReason().value; 
//			entity.sendMailWhenApprovalFlg = item2.getSendMailWhenApproval() == true ? 1 : 0; 
//			entity.sendMailWhenRegisterFlg = item2.getSendMailWhenRegister() == true ? 1 : 0; 
//			entity.displayReasonFlg = item2.getDisplayAppReason().value; 
//			entity.retrictPostAllowFutureFlg = item1.getAfterhandRestriction().getAllowFutureDay() == true ? 1 : 0;
//			listAppDisc.add(entity);
//		}
//		return listAppDisc;
//	}
	
	/**
	 * convert from Request Setting domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
//	private static KrqstApplicationSetting toEntity(RequestSetting domain){
//		val entity = new KrqstApplicationSetting();
//		entity.krqstApplicationSettingPK = new KrqstApplicationSettingPK(domain.getCompanyID());
//		entity.baseDateFlg = domain.getApplicationSetting().getRecordDate().value;
//		entity.displayPrePostFlg = domain.getApplicationSetting().getAppDisplaySetting().getPrePostAtr().value; 
//		entity.displaySearchTimeFlg = domain.getApplicationSetting().getAppDisplaySetting().getSearchWorkingHours().value;
//		entity.manualSendMailAtr = domain.getApplicationSetting().getAppDisplaySetting().getManualSendMailAtr().value;
//		entity.scheReflectFlg = domain.getAppReflectionSetting().getScheReflectFlg() == true ? 1 : 0; 
//		entity.priorityTimeReflectFlg = domain.getAppReflectionSetting().getPriorityTimeReflectFlag().value; 
//		entity.attendentTimeReflectFlg = domain.getAppReflectionSetting().getAttendentTimeReflectFlg() == true ? 1 : 0;
//		entity.advanceExcessMessDispAtr = domain.getApprovalListDisplaySetting().getAdvanceExcessMessDisAtr().value; 
//		entity.hwAdvanceDispAtr = domain.getApprovalListDisplaySetting().getHwAdvanceDisAtr().value;
//		entity.hwActualDispAtr = domain.getApprovalListDisplaySetting().getHwActualDisAtr().value; 
//		entity.actualExcessMessDispAtr = domain.getApprovalListDisplaySetting().getActualExcessMessDisAtr().value; 
//		entity.otAdvanceDispAtr = domain.getApprovalListDisplaySetting().getOtAdvanceDisAtr().value; 
//		entity.otActualDispAtr = domain.getApprovalListDisplaySetting().getOtActualDisAtr().value; 
//		entity.warningDateDispAtr = domain.getApprovalListDisplaySetting().getWarningDateDisAtr().v(); 
//		entity.appReasonDispAtr = domain.getApprovalListDisplaySetting().getAppReasonDisAtr().value;
//		entity.appContentChangeFlg = domain.getAuthorizationSetting().getAppContentChangeFlg() == true ? 1:0;
//		entity.scheduleConfirmedAtr = domain.getAppReflectAfterConfirm().getScheduleConfirmedAtr().value;
//		entity.achievementConfirmedAtr = domain.getAppReflectAfterConfirm().getAchievementConfirmedAtr().value;
//		entity.krqstAppTypeDiscretes = toEntityAppTypeDiscrete(domain.getApplicationSetting().getListReceptionRestrictionSetting(), 
//																domain.getApplicationSetting().getListAppTypeSetting());
//		entity.appActLockFlg = domain.getApplicationSetting().getAppLimitSetting().getCanAppAchievementLock() == true ? 1 : 0; 
//		entity.appEndWorkFlg = domain.getApplicationSetting().getAppLimitSetting().getCanAppFinishWork() == true ? 1 : 0; 
//		entity.appActConfirmFlg = domain.getApplicationSetting().getAppLimitSetting().getCanAppAchievementConfirm() == true ? 1 : 0; 
//		entity.appOvertimeNightFlg = domain.getApplicationSetting().getAppLimitSetting().getCanAppOTNight() == true ? 1 : 0; 
//		entity.appActMonthConfirmFlg = domain.getApplicationSetting().getAppLimitSetting().getCanAppAchievementMonthConfirm() == true ? 1 : 0;
//		entity.requireAppReasonFlg = domain.getApplicationSetting().getAppLimitSetting().getRequiredAppReason() == true ? 1 : 0;
//		entity.krqstAppDeadlines = toEntityAppTypeDead(domain.getApplicationSetting().getListAppDeadlineSetting());
//		return entity;
//	}
	
	/**
	 * update after and Before hand Restriction
	 * @author yennth
	 */
	@Override
	public void update(List<ReceptionRestrictionSetting> receiption, List<AppTypeSetting> appType) {
		String companyId = AppContexts.user().companyId();
		List<ApplicationType> listInsert = new ArrayList<>();
		List<AppTypeSetting> listFilter = new ArrayList<>();
		// update before and after 
		for(ReceptionRestrictionSetting item: receiption){ 
			Optional<KrqstAppTypeDiscrete> oldEntity1 = this.queryProxy().find(new KrqstAppTypeDiscretePK(companyId, item.getAppType().value), KrqstAppTypeDiscrete.class);
			// if don't exist => insert 
			if(!oldEntity1.isPresent()){
				listInsert.add(item.getAppType());
				Optional<AppTypeSetting> temp = appType.stream().filter(c -> c.getAppType().equals(item.getAppType())).findFirst();
				KrqstAppTypeDiscrete entity = toEntityDiscrete(item, temp.get());
				this.commandProxy().insert(entity);
			}
			// if exist => update
			else{
				KrqstAppTypeDiscrete temp = oldEntity1.get();
				//チェック方法 - retrictPreMethodFlg - RETRICT_PRE_METHOD_CHECK_FLG
				temp.retrictPreMethodFlg = item.getBeforehandRestriction().getMethodCheck().value;
				
				// 利用する - retrictPreUseFlg - RETRICT_PRE_USE_FLG
				temp.retrictPreUseFlg = item.getBeforehandRestriction().getToUse() ? 1 : 0;
				
				// 日数 - retrictPreDay - RETRICT_PRE_DAY
				temp.retrictPreDay = item.getBeforehandRestriction().getDateBeforehandRestriction().value;
				
				// 時刻 - retrictPreTimeDay - RETRICT_PRE_TIMEDAY
				temp.retrictPreTimeDay = item.getBeforehandRestriction().getTimeBeforehandRestriction() == null ? null : item.getBeforehandRestriction().getTimeBeforehandRestriction().v();
				
				// 時刻（早出残業）
				temp.preOtTime = item.getBeforehandRestriction().getPreOtTime() == null ? null : item.getBeforehandRestriction().getPreOtTime().v();
				
				// 時刻（通常残業）
				temp.normalOtTime = item.getBeforehandRestriction().getNormalOtTime() == null ? null : item.getBeforehandRestriction().getNormalOtTime().v();
				
				// 日数 - 残業申請事前の受付制限
				temp.otRestrictPreDay = item.getBeforehandRestriction().getOtRestrictPreDay().value;
				
				// 利用する - 残業申請事前の受付制限
				temp.otToUse = item.getBeforehandRestriction().getOtToUse() ? 1 : 0;
				
				// 未来日許可しない - retrictPostAllowFutureFlg - RETRICT_POST_ALLOW_FUTURE_FLG
				temp.retrictPostAllowFutureFlg = item.getAfterhandRestriction().getAllowFutureDay() ? 1: 0;
				this.commandProxy().update(temp);
			}
		}
		// filter list app type setting need update (if list insert don't need update)
		if(listInsert.isEmpty()){
			listFilter = appType;
		}else{
			for(ApplicationType i : listInsert){
				listFilter = appType.stream().filter(c -> !c.getAppType().equals(i))
																	.collect(Collectors.toList());
			}
		}
		for(AppTypeSetting app : listFilter){
			KrqstAppTypeDiscrete oldEntity2 = this.queryProxy().find(new KrqstAppTypeDiscretePK(companyId, app.getAppType().value), KrqstAppTypeDiscrete.class).get();
			// 定型理由の表示  - displayFixedReason - TYPICAL_REASON_DISPLAY_FLG (map domain AppTypeDiscreteSetting)
			oldEntity2.typicalReasonDisplayFlg = app.getDisplayFixedReason().value;
			// 申請理由の表示  - displayAppReason - DISPLAY_REASON_FLG
			oldEntity2.displayReasonFlg = app.getDisplayAppReason().value;
			// 新規登録時に自動でメールを送信する - sendMailWhenRegisterFlg - SEND_MAIL_WHEN_REGISTER_FLG
			oldEntity2.sendMailWhenRegisterFlg = app.getSendMailWhenRegister() ? 1 : 0;
			// 承認処理時に自動でメールを送信する -  - SEND_MAIL_WHEN_APPROVAL_FLG
			oldEntity2.sendMailWhenApprovalFlg = app.getSendMailWhenApproval() ? 1 : 0;
			// 事前事後区分の初期表示 - prePostInitAtr - PRE_POST_INIT_ATR
			oldEntity2.prePostInitAtr = app.getDisplayInitialSegment().value;
			// 事前事後区分を変更できる - prePostCanChangeFlg - PRE_POST_CAN_CHANGE_FLG
			oldEntity2.prePostCanChangeFlg = app.getCanClassificationChange() ? 1 : 0;
			this.commandProxy().update(oldEntity2);
		}
	}

	@Override
	@SneakyThrows
	public Optional<AppReflectionSetting> getAppReflectionSetting(String cid) {
		String sql = "SELECT SCHE_REFLECT_FLG,PRIORITY_TIME_REFLECT_FLG,ATTENDENT_TIME_REFLECT_FLG,CLASSIFY_SCHE_ACHIEVE_SAMEATR,REFLEC_TIMEOF_SCHEDULED"
				+ " FROM KRQST_APPLICATION_SETTING"
				+ " WHERE CID = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)){
			stmt.setString(1, cid);
			return new NtsResultSet(stmt.executeQuery())
					.getSingle(x ->{
						return AppReflectionSetting.toDomain(x.getInt("SCHE_REFLECT_FLG"),
								x.getInt("PRIORITY_TIME_REFLECT_FLG"),
								x.getInt("ATTENDENT_TIME_REFLECT_FLG"), 
								x.getInt("CLASSIFY_SCHE_ACHIEVE_SAMEATR"),
								x.getInt("REFLEC_TIMEOF_SCHEDULED"));
					});
		}
	}
}
