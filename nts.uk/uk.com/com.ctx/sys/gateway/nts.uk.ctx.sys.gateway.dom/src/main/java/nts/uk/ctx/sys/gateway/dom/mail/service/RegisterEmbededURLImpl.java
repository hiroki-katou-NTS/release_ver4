package nts.uk.ctx.sys.gateway.dom.mail.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserImportNew;
import nts.uk.ctx.sys.gateway.dom.adapter.user.UserInforExImport;
import nts.uk.ctx.sys.gateway.dom.login.service.CollectCompanyList;
import nts.uk.ctx.sys.gateway.dom.mail.UrlExecInfoRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.url.EmbeddedUrlScreenID;
import nts.uk.shr.com.url.RegisterEmbededURL;
import nts.uk.shr.com.url.UrlExecInfo;
import nts.uk.shr.com.url.UrlTaskIncre;
/**
 * @author hiep.ld
 *
 */
@Stateless
public class RegisterEmbededURLImpl implements RegisterEmbededURL {

	@Inject
	private UrlExecInfoRepository urlExcecInfoRepo;
	
	@Inject
	private UserAdapter userAdapter;
	
	@Inject
	private CollectCompanyList collectCompanyList;

	@Override
	public String obtainApplicationEmbeddedUrl(String appId, int appType, int prePostAtr, String employeeId) {
		return this.registerEmbeddedForApp(appId, appType, prePostAtr, "", employeeId);
	}

	@Override
	public String registerEmbeddedForApp(String appId, int appType, int prePostAtr, String loginId, String employeeId) {
		EmbeddedUrlScreenID embeddedUrlScreenID = this.getEmbeddedUrlRequestScreenID(appType, prePostAtr);
		List<UrlTaskIncre> taskInce = new ArrayList<>();
		taskInce.add(UrlTaskIncre.createFromJavaType(null, null, null, appId, appId));
		return this.embeddedUrlInfoRegis(embeddedUrlScreenID.getProgramId(), embeddedUrlScreenID.getDestinationId(), 1, 1, 
				employeeId, "000000000000", loginId, "", 0, taskInce);
	}

	@Override
	public String embeddedUrlInfoRegis(String programId, String screenId, Integer periodCls, Integer numOfPeriod, 
			String employeeId, String contractCD, String loginId, String employeeCD, Integer isCompanyNotLogin, List<UrlTaskIncre> taskIncidental) {
		if (loginId.isEmpty() && employeeId.isEmpty()) {
			return Strings.EMPTY;
		} 
		// Request list 313
		if(Strings.isNotBlank(employeeId)){
			Optional<UserInforExImport> opUserInforEx = userAdapter.getByEmpID(employeeId);
			if(opUserInforEx.isPresent()){
				loginId = opUserInforEx.get().getLoginID();
				employeeCD = opUserInforEx.get().getEmpCD();
			};
		}	
		// 埋込URL期間区分＝空白（指定なし）
		int periodClsReal = 1;
		if(periodCls!=null){
			// 埋込URL期間区分＝月(phân khu period URL nhứng = month)
			periodClsReal = periodCls;
		}
		// 期間数＝空白（指定なし）
		int numOfPeriodReal = 1;
		if(numOfPeriod!=null){
			// 期間数＝1(Số thời gian = 1)
			numOfPeriodReal = numOfPeriod;
		}
		String cid = AppContexts.user().companyId();
		if(isCompanyNotLogin==1){
			// imported（ゲートウェイ）「ユーザ」を取得する
			Optional<UserImportNew> opUserImportNew = userAdapter.findUserByContractAndLoginIdNew(contractCD, loginId);
			if(!opUserImportNew.isPresent()){
				throw new BusinessException("Msg_301");
			}
			// 「切替可能な会社一覧を取得する」
			List<String> companyIDLst = collectCompanyList.getCompanyList(opUserImportNew.get().getUserId());
			if(CollectionUtil.isEmpty(companyIDLst)){
				throw new BusinessException("Msg_1419");
			}
			cid = companyIDLst.get(0);
		}
		GeneralDateTime issueDate = GeneralDateTime.now();
		GeneralDateTime startDate = GeneralDateTime.now();
		GeneralDateTime expiredDate = this.getEmbeddedUrlExpriredDate(startDate, periodClsReal, numOfPeriodReal);
		UrlExecInfo urlInfo = this.updateEmbeddedUrl(cid, contractCD, loginId, employeeCD, employeeId, programId, screenId, issueDate, expiredDate, taskIncidental);
		if (!Objects.isNull(urlInfo)){
			String serverPath = AppContexts.requestedWebApi().getHostApi().replaceFirst("at", "com");
			String apiPath = "view/ccg/033/index.xhtml";
			return (serverPath + apiPath +"?id="+ urlInfo.getEmbeddedId());
		}
		return Strings.EMPTY;
		
	}

	@Override
	public EmbeddedUrlScreenID getEmbeddedUrlRequestScreenID(int appType, int prePostAtr) {
		ApplicationType appTypeEnum = EnumAdaptor.valueOf(appType, ApplicationType.class);
		switch (appTypeEnum) {
			case STAMP_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF002", "C");
			}
			case OVER_TIME_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF005", "B");
			}
			case ABSENCE_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF006", "B");
			}
			case WORK_CHANGE_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF007", "B");
			}
			case GO_RETURN_DIRECTLY_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF009", "B");
			}
			case EARLY_LEAVE_CANCEL_APPLICATION: {
				PrePostInitialAtr prePostAtrEnum = EnumAdaptor.valueOf(prePostAtr, PrePostInitialAtr.class);
				switch (prePostAtrEnum) {
					case PRE: {
						return new EmbeddedUrlScreenID("KAF004", "E");
					}
					case POST: {
						return new EmbeddedUrlScreenID("KAF004", "F");
					}
					case NO_CHOISE: {
						return new EmbeddedUrlScreenID("", "");
					}
				}
			}
			case BREAK_TIME_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF010", "B");
			}
			case COMPLEMENT_LEAVE_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF011", "B");
			}
			case ANNUAL_HOLIDAY_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF012", "B");
			}
			case LONG_BUSINESS_TRIP_APPLICATION: {
				return new EmbeddedUrlScreenID("KAF008", "D");
			}
			case BUSINESS_TRIP_APPLICATION_OFFICE_HELPER: {
				return new EmbeddedUrlScreenID("KAF008", "E");
			}
			default:
				return new EmbeddedUrlScreenID("", "");
		}
	}

	@Override
	public GeneralDateTime getEmbeddedUrlExpriredDate(GeneralDateTime startDate, int periodCls, int numOfPeriod) {
		PeriodClassification periodClsEnum = PeriodClassification.values()[periodCls];
		switch (periodClsEnum) {
			case YEAR: {
				GeneralDateTime expiredDate = startDate.addYears(numOfPeriod);
				expiredDate = expiredDate.addHours(- expiredDate.hours());
				expiredDate = expiredDate.addMinutes(- expiredDate.minutes());
				expiredDate = expiredDate.addSeconds(- expiredDate.seconds() - 1);
				return expiredDate;
			}
			case MONTH: {
				GeneralDateTime expiredDate = startDate.addMonths(numOfPeriod);
				expiredDate = expiredDate.addHours(- expiredDate.hours());
				expiredDate = expiredDate.addMinutes(- expiredDate.minutes());
				expiredDate = expiredDate.addSeconds(- expiredDate.seconds() - 1);
				return expiredDate;
			}
			case DAY: {
				GeneralDateTime expiredDate = startDate.addDays(numOfPeriod);
				expiredDate = expiredDate.addHours(- expiredDate.hours());
				expiredDate = expiredDate.addMinutes(- expiredDate.minutes());
				expiredDate = expiredDate.addSeconds(- expiredDate.seconds() - 1);
				return expiredDate;
			}
			case HOUR: {
				GeneralDateTime expiredDate = startDate.addHours(numOfPeriod);
				expiredDate = expiredDate.addSeconds(- expiredDate.seconds() - 1);
				return expiredDate;
			}
			case MINUTE: {
				GeneralDateTime expiredDate = startDate.addMinutes(numOfPeriod);
				expiredDate = expiredDate.addSeconds(- expiredDate.seconds() - 1);
				return expiredDate;
			}
		}
		return GeneralDateTime.now();
	}

	@Override
	public UrlExecInfo updateEmbeddedUrl(String cid, String contractCd, String loginId, String scd, String sid,
			String programId, String screenId, GeneralDateTime issueDate, GeneralDateTime expiredDate,
			List<UrlTaskIncre> taskIncre) {
		String embeddedId = UUID.randomUUID().toString();
		try{
			taskIncre.forEach(x -> {
				x.setVersion(0);
				x.setCid(cid);
				x.setEmbeddedId(embeddedId);
				x.setTaskIncreId(UUID.randomUUID().toString());
			});
			UrlExecInfo url = UrlExecInfo.createFromJavaType(embeddedId, cid, programId, loginId, contractCd, expiredDate, issueDate, screenId, sid, scd, taskIncre);
			this.urlExcecInfoRepo.add(url);
			return url;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void checkPassLimitExpire(String embeddedURLID) {
		// ドメインモデル「埋込URL実行情報」を取得する(Get domain model 「埋込URL実行情報」)
		Optional<UrlExecInfo> opUrlExecInfo = urlExcecInfoRepo.getUrlExecInfoByUrlID(embeddedURLID);
		if(!opUrlExecInfo.isPresent()){
			throw new BusinessException("Msg_1095");
		}
		UrlExecInfo urlExecInfo = opUrlExecInfo.get();
		// システム日時が「埋込URL実行情報.有効期限」を超えていないことを確認する
		if(urlExecInfo.getExpiredDate().before(GeneralDateTime.now())){
			throw new BusinessException("Msg_1095");
		}
	}
}
