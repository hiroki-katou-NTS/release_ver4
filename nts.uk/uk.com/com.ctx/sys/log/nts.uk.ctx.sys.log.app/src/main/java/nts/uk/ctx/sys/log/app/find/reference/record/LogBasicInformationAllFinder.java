package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.app.find.reference.LogDisplaySettingDto;
import nts.uk.ctx.sys.log.app.find.reference.LogDisplaySettingFinder;
import nts.uk.ctx.sys.log.app.find.reference.LogOuputItemFinder;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.ctx.sys.log.app.find.reference.LogSetItemDetailDto;
import nts.uk.ctx.sys.log.app.find.reference.LogSetOutputItemDto;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.dom.logbasicinfo.LogBasicInfoRepository;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecord;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository;
import nts.uk.ctx.sys.log.dom.pereg.IPersonInfoCorrectionLogRepository;
import nts.uk.ctx.sys.log.dom.reference.ItemNoEnum;
import nts.uk.ctx.sys.log.dom.reference.PersonEmpBasicInfoAdapter;
import nts.uk.ctx.sys.log.dom.reference.RecordTypeEnum;
import nts.uk.ctx.sys.log.dom.reference.RoleExportAdapter;
import nts.uk.ctx.sys.log.dom.reference.WebMenuAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.context.loginuser.role.LoginUserRoles;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataType;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.CategoryCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.InfoOperateAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ItemInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoProcessAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ReviseInfo;
import nts.uk.shr.com.security.audittrail.start.StartPageLog;
import nts.uk.shr.com.security.audittrail.start.StartPageLogRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/*
 * author : huannv
 */

@Stateless
public class LogBasicInformationAllFinder {

	@Inject
	private LogBasicInfoRepository logBasicInfoRepository;

	@Inject
	private DataCorrectionLogRepository dataCorrectionLogRepository;

	@Inject
	private StartPageLogRepository startPageLogRepository;

	@Inject
	private LoginRecordRepository loginRecordRepository;

	@Inject
	private IPersonInfoCorrectionLogRepository iPersonInfoCorrectionLogRepository;

	/** The PersonEmpBasicInfoPub. */
	@Inject
	private PersonEmpBasicInfoAdapter personEmpBasicInfoAdapter;

	@Inject
	RoleExportAdapter roleExportAdapter;

	@Inject
	private WebMenuAdapter webMenuAdapter;
	
	@Inject
	private LogDisplaySettingFinder logDisplaySettingFinder;
	
	@Inject
	private LogOuputItemFinder logOuputItemFinder;
	
	public List<LogBasicInfoAllDto> findByOperatorsAndDate(LogParams logParams) {
		List<LogBasicInfoAllDto> lstLogBacsicInfo = new ArrayList<>();
		// get login info
		LoginUserContext loginUserContext = AppContexts.user();
		// get company id
		String cid = loginUserContext.companyId();
		Map<String, String> mapProgramNames = webMenuAdapter.getWebMenuByCId(cid);	
		DatePeriod datePeriodTaget = new DatePeriod(logParams.getStartDateTaget(), logParams.getEndDateTaget());
		RecordTypeEnum recordTypeEnum = RecordTypeEnum.valueOf(logParams.getRecordType());
		List<LogBasicInformation> lstLogBasicInformation=new ArrayList<>();
		if(recordTypeEnum.code !=RecordTypeEnum.START_UP.code){
		 lstLogBasicInformation = this.logBasicInfoRepository.findByOperatorsAndDate(cid,
					logParams.getListOperatorEmployeeId(), logParams.getStartDateOperator(),
					logParams.getEndDateOperator());
		}
		
		TargetDataType targetDataType = null;
		if (!Objects.isNull(logParams.getTargetDataType())) {
			targetDataType = TargetDataType.of(logParams.getTargetDataType());
		}
		if (!CollectionUtil.isEmpty(lstLogBasicInformation)) {			
			List<String> listOperationId= new ArrayList<>();
			Map<String, LogBasicInformation> map=new HashMap<>();
			List<String> employeeIds = new ArrayList<>();
			for(LogBasicInformation logBasicInformation : lstLogBasicInformation){
				listOperationId.add(logBasicInformation.getOperationId());
				map.put(logBasicInformation.getOperationId(), logBasicInformation);
				if(logBasicInformation.getUserInfo() !=null){
					employeeIds.add(logBasicInformation.getUserInfo().getEmployeeId());
					
				}
			}
			// Get list employee code - request list 228
			Map<String, String> mapEmployeeCodes = personEmpBasicInfoAdapter
					.getEmployeeCodesByEmpIds(employeeIds.stream().distinct().collect(Collectors.toList()));
			switch (recordTypeEnum) {
			case LOGIN:
			List<LoginRecord> rsLoginRecord	=this.loginRecordRepository.logRecordInfor(listOperationId);
				if(!CollectionUtil.isEmpty(rsLoginRecord)){
					Map<String, String> roleNameByRoleIds = getRoleNameByRoleId(cid, new ArrayList<>(), rsLoginRecord ,  map, new ArrayList<>(), new ArrayList<>());
					rsLoginRecord.stream().forEach(loginRecord ->{
						LogBasicInformation	logBasicInformation=map.get(loginRecord.getOperationId());
						LogBasicInfoAllDto logBasicInfoDto = LogBasicInfoAllDto.fromDomain(logBasicInformation);					
						// itemNo 3
						UserInfo userDto = logBasicInformation.getUserInfo();
						if (userDto != null) {
							logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
							// itemNo 1
							logBasicInfoDto.setUserIdLogin(userDto.getUserId());
							// itemNo 2
							logBasicInfoDto.setUserNameLogin(userDto.getUserName());
						}					
					

						// itemNo 4
						if (logBasicInformation.getLoginInformation().getIpAddress().isPresent()) {
							logBasicInfoDto
									.setIpAddress(logBasicInformation.getLoginInformation().getIpAddress().get());
						} else {
							logBasicInfoDto.setIpAddress("");
						}
						// itemNo 5
						logBasicInfoDto.setPcName(logBasicInformation.getLoginInformation().getPcName().isPresent()
								? logBasicInformation.getLoginInformation().getPcName().get() : "");
						// itemNo 6
						logBasicInfoDto.setAccount(logBasicInformation.getLoginInformation().getAccount().isPresent()
								? logBasicInformation.getLoginInformation().getAccount().get() : "");

						// itemNo 7
						// logBasicInfoDto.setModifyDateTime(logBasicInformation.getModifiedDateTime().toString());
						// itemNo 8 return nname
						LoginUserRoles loginUserRoles = logBasicInformation.getAuthorityInformation();		
						String role = loginUserRoles.forAttendance();
						logBasicInfoDto.setEmploymentAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");								
						// itemNo 9
						role = loginUserRoles.forPayroll();
						logBasicInfoDto.setSalarytAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 10
						role = loginUserRoles.forPersonnel();
						logBasicInfoDto.setPersonelAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 11
						role = loginUserRoles.forOfficeHelper();
						logBasicInfoDto.setOfficeHelperAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
					/*	// itemNo 12
						
						  logBasicInfoDto.setAccountAuthorityName(
						  roleExportAdapter
						  .getNameByRoleId(logBasicInformation.
						  getAuthorityInformation().forSystemAdmin()));
						 
						// itemNo 13
						
						  logBasicInfoDto.setMyNumberAuthorityName(
						  roleExportAdapter
						  .getNameByRoleId(logBasicInformation.
						 getAuthorityInformation().forPersonalInfo()));*/
						 
						// itemNo 14
						role = loginUserRoles.forGroupCompaniesAdmin();
						logBasicInfoDto.setGroupCompanyAddminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 15
						role = loginUserRoles.forCompanyAdmin();
						logBasicInfoDto.setCompanyAddminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 16
						role = loginUserRoles.forSystemAdmin();
						logBasicInfoDto.setSystemAdminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 17
						role = loginUserRoles.forPersonalInfo();
						logBasicInfoDto.setPersonalInfoAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 18

						String key = logBasicInformation.getTargetProgram().getProgramId()
								+ logBasicInformation.getTargetProgram().getScreenId()
								+ logBasicInformation.getTargetProgram().getQueryString();
						logBasicInfoDto.setMenuName(mapProgramNames.get(key));
						// itemNo 19					;
						logBasicInfoDto.setLoginStatus(loginRecord.getLoginStatus().description);
						// itemNo 20
						logBasicInfoDto.setLoginMethod(loginRecord.getLoginMethod().description);
						// itemNo 21
						logBasicInfoDto.setAccessResourceUrl(
								loginRecord.getUrl().isPresent() ? loginRecord.getUrl().get() : "");
						// itemNo 22
						logBasicInfoDto
								.setNote(loginRecord.getRemarks().isPresent() ? loginRecord.getRemarks().get() : "");
						lstLogBacsicInfo.add(logBasicInfoDto);
					});
				}
			
				break;
			case START_UP:
		
				break;
			case UPDATE_PERSION_INFO:
				
				List<PersonInfoCorrectionLog> listPersonInfoCorrectionLog = this.iPersonInfoCorrectionLogRepository
				.findByTargetAndDate(listOperationId, logParams.getListTagetEmployeeId());
				if (!CollectionUtil.isEmpty(listPersonInfoCorrectionLog)) {
					// Get list employee code
					List<String> employeePerSonIds = new ArrayList<>();
					for (PersonInfoCorrectionLog personInfoCorrectionLog : listPersonInfoCorrectionLog) {
						if(personInfoCorrectionLog.getTargetUser() !=null){
							employeePerSonIds.add(personInfoCorrectionLog.getTargetUser().getEmployeeId());
						}					
					}
					Map<String, String> mapEmployeeCodePersons = personEmpBasicInfoAdapter.getEmployeeCodesByEmpIds(employeePerSonIds);
					Map<String, String> roleNameByRoleIds = getRoleNameByRoleId(cid, new ArrayList<>(), new ArrayList<>() ,  map, listPersonInfoCorrectionLog, new ArrayList<>());
					listPersonInfoCorrectionLog.stream().forEach(personInfoCorrectionLog ->{
						
						List<CategoryCorrectionLog> rsListCategoryCorrectionLog = personInfoCorrectionLog
								.getCategoryCorrections();
						if (!CollectionUtil.isEmpty(rsListCategoryCorrectionLog)) {
							rsListCategoryCorrectionLog.stream().forEach(categoryCorrectionLog ->{
								List<ItemInfo> rsItemInfo = categoryCorrectionLog.getItemInfos();
								
								if (!CollectionUtil.isEmpty(rsItemInfo)) {
									rsItemInfo.stream().forEach(itemInfo ->{
										//ADDNEW
										LogBasicInformation	logBasicInformation = map.get(personInfoCorrectionLog.getOperationId());
										
										// convert log basic info to DTO
										LogBasicInfoAllDto logBasicInfoDto = LogBasicInfoAllDto.fromDomain(logBasicInformation);
										Optional<ReviseInfo> rsReviseInfo=categoryCorrectionLog.getReviseInfo();
										if(rsReviseInfo.isPresent()){
											// item 34
											logBasicInfoDto.setItemEditAddition(rsReviseInfo.get().getItemName());
											// item 35
											if(rsReviseInfo.get().getDate().isPresent()){									
												logBasicInfoDto.setTarGetYmdEditAddition(rsReviseInfo.get().getDate().get().toString());
											}									
										
										}
										// Set user login name
										UserInfo userDto = logBasicInformation.getUserInfo();
										if (userDto != null) {
											logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
											// itemNo 1
											logBasicInfoDto.setUserIdLogin(userDto.getUserId());
											// itemNo 2
											logBasicInfoDto.setUserNameLogin(userDto.getUserName());
										}						
										// itemNo 4
										if (logBasicInformation.getLoginInformation().getIpAddress().isPresent()) {
											logBasicInfoDto.setIpAddress(logBasicInformation.getLoginInformation().getIpAddress().get());
										} else {
											logBasicInfoDto.setIpAddress("");
										}
										// itemNo 5
										logBasicInfoDto.setPcName(logBasicInformation.getLoginInformation().getPcName().isPresent()
												? logBasicInformation.getLoginInformation().getPcName().get() : "");
										// itemNo 6
										logBasicInfoDto.setAccount(logBasicInformation.getLoginInformation().getAccount().isPresent()
												? logBasicInformation.getLoginInformation().getAccount().get() : "");
										// itemNo 8 return nname
										LoginUserRoles loginUserRoles = logBasicInformation.getAuthorityInformation();
										String role = loginUserRoles.forAttendance();
										logBasicInfoDto.setEmploymentAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 9
										role = loginUserRoles.forPayroll();
										logBasicInfoDto.setSalarytAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 10
										role = loginUserRoles.forPersonnel();
										logBasicInfoDto.setPersonelAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 11
										role = loginUserRoles.forOfficeHelper();
										logBasicInfoDto.setOfficeHelperAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 12
										/*
										 * logBasicInfoDto.setAccountAuthorityName(
										 * roleExportAdapter
										 * .getNameByRoleId(logBasicInformation.
										 * getAuthorityInformation().forSystemAdmin()));
										 */
										// itemNo 13
										/*
										 * logBasicInfoDto.setMyNumberAuthorityName(
										 * roleExportAdapter
										 * .getNameByRoleId(logBasicInformation.
										 * getAuthorityInformation().forAttendance()));
										 */
										// itemNo 14
										role = loginUserRoles.forGroupCompaniesAdmin();
										logBasicInfoDto.setGroupCompanyAddminAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 15
										role = loginUserRoles.forCompanyAdmin();
										logBasicInfoDto.setCompanyAddminAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 16
										role = loginUserRoles.forSystemAdmin();
										logBasicInfoDto.setSystemAdminAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 17
										role = loginUserRoles.forPersonalInfo();
										logBasicInfoDto.setPersonalInfoAuthorityName(
												role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
										// itemNo 18
										String key = logBasicInformation.getTargetProgram().getProgramId()
												+ logBasicInformation.getTargetProgram().getScreenId()
												+ logBasicInformation.getTargetProgram().getQueryString();
										logBasicInfoDto.setMenuName(mapProgramNames.get(key));
										UserInfo userInfo	=personInfoCorrectionLog.getTargetUser();
										// itemNO 19
										logBasicInfoDto.setUserIdTaget(userInfo.getUserId());
										// itemNO 20
										logBasicInfoDto.setUserNameTaget(userInfo.getUserName());
										// itemNo 21
//									
										logBasicInfoDto.setEmployeeCodeTaget(mapEmployeeCodePersons.get(userInfo.getEmployeeId()));
										// itemNo 22
										logBasicInfoDto.setCategoryProcess(
												getPersonInfoProcessAttr(personInfoCorrectionLog.getProcessAttr().value));
										// item 36
										logBasicInfoDto.setNote(personInfoCorrectionLog.getRemark());	
										// END NEW									
										// item 23
										logBasicInfoDto.setCategoryName(categoryCorrectionLog.getCategoryName());
										// item 24
										logBasicInfoDto.setMethodCorrection(this.getinfoOperateAttr(
												categoryCorrectionLog.getInfoOperateAttr().value));
										
										if (!Objects.isNull(categoryCorrectionLog.getTargetKey()) && 
											categoryCorrectionLog.getTargetKey().getDateKey().isPresent()) {
											
											Optional<GeneralDate> datekey = categoryCorrectionLog.getTargetKey()
														.getDateKey();
												// item 25
												logBasicInfoDto.setTarGetYmd(datekey.get().toString());
												// item 26
												logBasicInfoDto
														.setTarGetYm(String.valueOf(datekey.get().yearMonth()));
												// item 27
												logBasicInfoDto.setTarGetY(String.valueOf(datekey.get().year()));
												// item 28
												logBasicInfoDto.setKeyString(categoryCorrectionLog.getTargetKey()
														.getStringKey().isPresent()
																? categoryCorrectionLog.getTargetKey()
																		.getStringKey().get()
																: "");
											

										}

										// item 29
										logBasicInfoDto.setItemName(itemInfo.getName());
										// item 30
										if(!Objects.isNull(itemInfo.getValueBefore().getRawValue())){
											logBasicInfoDto.setItemvalueBefor(itemInfo.getValueBefore().getRawValue().getValue().toString());
										
										}
										// item 31
										logBasicInfoDto
												.setItemContentValueBefor(itemInfo.getValueBefore().getViewValue());
										// item 32
										if(!Objects.isNull(itemInfo.getValueAfter().getRawValue())){
											logBasicInfoDto.setItemvalueAppter(itemInfo.getValueAfter().getRawValue().getValue().toString());
										}
										
										// item 33
										logBasicInfoDto
												.setItemContentValueAppter(itemInfo.getValueAfter().getViewValue());

										lstLogBacsicInfo.add(logBasicInfoDto);									
									});
								} 								
							});

						}
					});
				}
				break;
			case DATA_CORRECT:
				
				List<DataCorrectionLog> rsDataCorectLog =	this.dataCorrectionLogRepository.findByTargetAndDate(
						listOperationId, logParams.getListTagetEmployeeId(), datePeriodTaget, targetDataType);
				if(!CollectionUtil.isEmpty(rsDataCorectLog)){					
					// convert list data corect log to DTO
					List<String> employeePerSonIds = new ArrayList<>();
					List<LogDataCorrectRecordAllDto> lstLogDataCorecRecordRefeDto = new ArrayList<>();
					for (DataCorrectionLog dataCorrectionLog : rsDataCorectLog) {
						LogDataCorrectRecordAllDto logDataCorrectRecordRefeDto = LogDataCorrectRecordAllDto
								.fromDomain(dataCorrectionLog);
						lstLogDataCorecRecordRefeDto.add(logDataCorrectRecordRefeDto);
						if(dataCorrectionLog.getTargetUser() !=null){
							employeePerSonIds.add(dataCorrectionLog.getTargetUser().getEmployeeId());
						}
					}
					Map<String, String> mapEmployeeCodePersons = personEmpBasicInfoAdapter.getEmployeeCodesByEmpIds(employeePerSonIds);
					//
					if (!CollectionUtil.isEmpty(lstLogDataCorecRecordRefeDto)) {
						Map<String, String> roleNameByRoleIds = getRoleNameByRoleId(cid, new ArrayList<>(), new ArrayList<>() ,  map, new ArrayList<>(), new ArrayList<>());			
						lstLogDataCorecRecordRefeDto.stream().forEach(logDataCorrectRecordRefeDto ->{
							// convert log basic info to DTO
							LogBasicInformation logBasicInformation =map.get(logDataCorrectRecordRefeDto.getOperationId());
							LogBasicInfoAllDto logBasicInfoDto = LogBasicInfoAllDto.fromDomain(logBasicInformation);
							UserInfo userDto = logBasicInformation.getUserInfo();
							if (userDto != null) {
								logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
								// itemNo 1
								logBasicInfoDto.setUserIdLogin(userDto.getUserId());
								// itemNo 2
								logBasicInfoDto.setUserNameLogin(userDto.getUserName());
							}	
							// itemNo 4
							if (logBasicInformation.getLoginInformation().getIpAddress().isPresent()) {
								logBasicInfoDto.setIpAddress(
										logBasicInformation.getLoginInformation().getIpAddress().get());
							} else {
								logBasicInfoDto.setIpAddress("");
							}
							// itemNo 5
							logBasicInfoDto
									.setPcName(logBasicInformation.getLoginInformation().getPcName().isPresent()
											? logBasicInformation.getLoginInformation().getPcName().get() : "");
							// itemNo 6
							logBasicInfoDto
									.setAccount(logBasicInformation.getLoginInformation().getAccount().isPresent()
											? logBasicInformation.getLoginInformation().getAccount().get() : "");

							// itemNo 7
							// logBasicInfoDto.setModifyDateTime(logBasicInformation.getModifiedDateTime().toString());
							// itemNo 8 return nname
							LoginUserRoles loginUserRoles = logBasicInformation.getAuthorityInformation();
							String role  = loginUserRoles.forAttendance();
							logBasicInfoDto.setEmploymentAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 9
							role  = loginUserRoles.forPayroll();
							logBasicInfoDto.setSalarytAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							;
							// itemNo 10
							role  = loginUserRoles.forPersonnel();
							logBasicInfoDto.setPersonelAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 11
							role  = loginUserRoles.forOfficeHelper();
							logBasicInfoDto.setOfficeHelperAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 12
							/*
							 * logBasicInfoDto.setAccountAuthorityName(
							 * roleExportAdapter
							 * .getNameByRoleId(logBasicInformation.
							 * getAuthorityInformation().forSystemAdmin()));
							 */
							// itemNo 13
							/*
							 * logBasicInfoDto.setMyNumberAuthorityName(
							 * roleExportAdapter
							 * .getNameByRoleId(logBasicInformation.
							 * getAuthorityInformation().forAttendance()));
							 */
							// itemNo 14
							role  = loginUserRoles.forGroupCompaniesAdmin();
							logBasicInfoDto.setGroupCompanyAddminAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 15
							role  = loginUserRoles.forCompanyAdmin();
							logBasicInfoDto.setCompanyAddminAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 16
							role  = loginUserRoles.forSystemAdmin();
							logBasicInfoDto.setSystemAdminAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 17
							role  = loginUserRoles.forPersonalInfo();
							logBasicInfoDto.setPersonalInfoAuthorityName(
									role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
							// itemNo 18
							if (!Objects.isNull(logBasicInformation.getTargetProgram())) {
								String key = logBasicInformation.getTargetProgram().getProgramId()
										+ logBasicInformation.getTargetProgram().getScreenId()
										+ logBasicInformation.getTargetProgram().getQueryString();

								logBasicInfoDto.setMenuName(mapProgramNames.get(key));
							}
							// set dataCorrect
							// itemNo 19
							logBasicInfoDto.setUserIdTaget(logDataCorrectRecordRefeDto.getUserIdtaget());
							// itemNo 20
							logBasicInfoDto.setUserNameTaget(logDataCorrectRecordRefeDto.getUserNameTaget());
							// itemNo 21
//							persionInfor = null;
//							persionInfor = personEmpBasicInfoAdapter
//									.getPersonEmpBasicInfoByEmpId(logDataCorrectRecordRefeDto.getEmployeeIdtaget());
//							if (persionInfor != null) {
//								logBasicInfoDto.setEmployeeCodeTaget(persionInfor.getEmployeeCode());
//							} else {
//								logBasicInfoDto.setEmployeeCodeTaget("");
//							}
							// itemNo 21
							logBasicInfoDto.setEmployeeCodeTaget(mapEmployeeCodePersons.get(logDataCorrectRecordRefeDto.getEmployeeIdtaget()));
							// itemNo 22
							logBasicInfoDto.setTarGetYmd(logDataCorrectRecordRefeDto.getTarGetYmd());
							// itemNo 23
							logBasicInfoDto.setTarGetYm(logDataCorrectRecordRefeDto.getTarGetYm());
							// itemNo 24
							logBasicInfoDto.setTarGetY(logDataCorrectRecordRefeDto.getTarGetY());
							// itemNo 25
							logBasicInfoDto.setKeyString(logDataCorrectRecordRefeDto.getKeyString());
							// itemNo 26
							logBasicInfoDto
									.setCatagoryCorection(logDataCorrectRecordRefeDto.getCatagoryCorection());
							// itemNo 27
							logBasicInfoDto.setItemName(logDataCorrectRecordRefeDto.getItemName());
							// itemNo 28
							logBasicInfoDto
									.setItemvalueBefor(logDataCorrectRecordRefeDto.getValueBefore());
							// itemNo 29
							logBasicInfoDto
									.setItemvalueAppter(logDataCorrectRecordRefeDto.getValueAfter());
							// itemNo 30
							logBasicInfoDto.setItemContentValueBefor(
									logDataCorrectRecordRefeDto.getItemContentValueBefor());
							// itemNo 31
							logBasicInfoDto.setItemContentValueAppter(
									logDataCorrectRecordRefeDto.getItemContentValueAppter());
							// itemNo 32
							logBasicInfoDto.setNote(logDataCorrectRecordRefeDto.getRemarks());

							// add to list
							lstLogBacsicInfo.add(logBasicInfoDto);
						});
					}
					
				}
						
				break;
			default:
				break;
			}
		}else{
			if(recordTypeEnum.code ==RecordTypeEnum.START_UP.code){
				
			List<StartPageLog> listStartPageLog=this.startPageLogRepository.findBy(cid,
						logParams.getListOperatorEmployeeId(), logParams.getStartDateOperator(),
						logParams.getEndDateOperator());
				
			
				if(!CollectionUtil.isEmpty(listStartPageLog)){
					  List<String> employeeIds = new ArrayList<>();
						for(StartPageLog startPageLog : listStartPageLog){
							LogBasicInformation	lgBasicInformation=startPageLog.getBasicInfo();
							if(!Objects.isNull(lgBasicInformation) && !Objects.isNull(lgBasicInformation.getUserInfo()) &&
									!Objects.isNull(lgBasicInformation.getUserInfo().getEmployeeId())  ){
								employeeIds.add(lgBasicInformation.getUserInfo().getEmployeeId());
								
							}
						}
						// Get list employee code
					Map<String, String> mapEmployeeCodes = personEmpBasicInfoAdapter.getEmployeeCodesByEmpIds(employeeIds);
					
					Map<String, String> roleNameByRoleIds = getRoleNameByRoleId(cid, listStartPageLog, new ArrayList<>(),  new HashMap<>(), new ArrayList<>(), new ArrayList<>());
					
					listStartPageLog.stream().forEach(oPStartPageLog ->{
						//	LogBasicInformation	logBasicInformation=map.get(oPStartPageLog.getBasicInfo().getOperationId());
						LogBasicInformation	logBasicInformation=oPStartPageLog.getBasicInfo();
						LogBasicInfoAllDto logBasicInfoDto = LogBasicInfoAllDto.fromDomain(logBasicInformation);
						// Set user login name
						// itemNo 3
						UserInfo userDto = logBasicInformation.getUserInfo();
						if (userDto != null) {
							logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
							// itemNo 1
							logBasicInfoDto.setUserIdLogin(userDto.getUserId());
							// itemNo 2
							logBasicInfoDto.setUserNameLogin(userDto.getUserName());
						}	
					
						// itemNo 4
						if (logBasicInformation.getLoginInformation().getIpAddress().isPresent()) {
							logBasicInfoDto
									.setIpAddress(logBasicInformation.getLoginInformation().getIpAddress().get());
						} else {
							logBasicInfoDto.setIpAddress("");
						}
						// itemNo 5
						logBasicInfoDto.setPcName(logBasicInformation.getLoginInformation().getPcName().isPresent()
								? logBasicInformation.getLoginInformation().getPcName().get() : "");
						// itemNo 6
						logBasicInfoDto.setAccount(logBasicInformation.getLoginInformation().getAccount().isPresent()
								? logBasicInformation.getLoginInformation().getAccount().get() : "");

						// itemNo 7
						// logBasicInfoDto.setModifyDateTime(logBasicInformation.getModifiedDateTime().toString());

						// itemNo 8 return nname
						LoginUserRoles loginUserRoles = logBasicInformation.getAuthorityInformation();
						String role = loginUserRoles.forAttendance();
						logBasicInfoDto.setEmploymentAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role): "" ):"");
						// itemNo 9
						role = loginUserRoles.forPayroll();
						logBasicInfoDto.setSalarytAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						;
						// itemNo 10
						role = loginUserRoles.forPersonnel();
						logBasicInfoDto.setPersonelAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 11
						role = loginUserRoles.forOfficeHelper();
						logBasicInfoDto.setOfficeHelperAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 12
						/*
						 * logBasicInfoDto.setAccountAuthorityName(
						 * roleExportAdapter
						 * .getNameByRoleId(logBasicInformation.
						 * getAuthorityInformation().forSystemAdmin()));
						 */
						// itemNo 13
						/*
						 * logBasicInfoDto.setMyNumberAuthorityName(
						 * roleExportAdapter
						 * .getNameByRoleId(logBasicInformation.
						 * getAuthorityInformation().forAttendance()));
						 */
						// itemNo 14
						role = loginUserRoles.forGroupCompaniesAdmin();
						logBasicInfoDto.setGroupCompanyAddminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 15
						role = loginUserRoles.forCompanyAdmin();
						logBasicInfoDto.setCompanyAddminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 16
						role = loginUserRoles.forSystemAdmin();
						logBasicInfoDto.setSystemAdminAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 17
						role = loginUserRoles.forPersonalInfo();
						logBasicInfoDto.setPersonalInfoAuthorityName(
								role!=null?(roleNameByRoleIds.get(role) != null? roleNameByRoleIds.get(role):""):"");
						// itemNo 18
						logBasicInfoDto.setNote(
								logBasicInformation.getNote().isPresent() ? logBasicInformation.getNote().get() : "");
						// itemNo 19
						String key = logBasicInformation.getTargetProgram().getProgramId()
								+ logBasicInformation.getTargetProgram().getScreenId()
								+ logBasicInformation.getTargetProgram().getQueryString();
						logBasicInfoDto.setMenuName(mapProgramNames.get(key));
						// itemNo 20
						//StartPageLog startPageLog = oPStartPageLog.get();
						if (oPStartPageLog.getStartPageBeforeInfo().isPresent()) {
							String keyResource = oPStartPageLog.getStartPageBeforeInfo().get().getProgramId()
									+ oPStartPageLog.getStartPageBeforeInfo().get().getScreenId()
									+ oPStartPageLog.getStartPageBeforeInfo().get().getQueryString();
									
							logBasicInfoDto.setMenuNameReSource(mapProgramNames.get(keyResource));
						}

						// add to list
						lstLogBacsicInfo.add(logBasicInfoDto);
					});
				}
				
			}
		}
		return lstLogBacsicInfo;
	}

	public String getPersonInfoProcessAttr(int attr) {
		PersonInfoProcessAttr personInfoProcessAttr = PersonInfoProcessAttr.valueOf(attr);
		switch (personInfoProcessAttr) {
		case ADD:
			return TextResource.localize("Enum_PersonInfoProcess_ADD");
		case UPDATE:
			return TextResource.localize("Enum_PersonInfoProcess_UPDATE");
		case LOGICAL_DELETE:
			return TextResource.localize("Enum_PersonInfoProcess_LOGICAL_DELETE");
		case COMPLETE_DELETE:
			return TextResource.localize("Enum_PersonInfoProcess_COMPLETE_DELETE");
		case RESTORE_LOGICAL_DELETE:
			return TextResource.localize("Enum_PersonInfoProcess_RESTORE_LOGICAL_DELETE");
		case TRANSFER:
			return TextResource.localize("Enum_PersonInfoProcess_TRANSFER");
		case RETURN:
			return TextResource.localize("Enum_PersonInfoProcess_RETURN");
		default:
			return "";
		}
	}

	public String getinfoOperateAttr(int attr) {
		InfoOperateAttr infoOperateAttr = InfoOperateAttr.valueOf(attr);
		switch (infoOperateAttr) {
		case ADD:
			return TextResource.localize("Enum_InfoOperate_ADD");
		case UPDATE:
			return TextResource.localize("Enum_InfoOperate_UPDATE");
		case DELETE:
			return TextResource.localize("Enum_InfoOperate_DELETE");
		case ADD_HISTORY:
			return TextResource.localize("Enum_InfoOperate_ADD_HISTORY");
		case DELETE_HISTORY:
			return TextResource.localize("Enum_InfoOperate_DELETE_HISTORY");
		default:
			return "";
		}
	}

	public List<Map<String, Object>> getDataLog(LogParams params) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		dataSource = getDataSource(params, params.getLstHeaderDto());
		return dataSource;

	}

	private List<Map<String, Object>> getDataSource(LogParams params, List<LogOutputItemDto> headers) {
		List<Map<String, Object>> dataSource = new ArrayList<>();
		List<LogBasicInfoAllDto> data = params.getListLogBasicInfoAllDto();
		
		data.stream().forEach(d ->{
			Map<String, Object> row = checkHeader(d, headers, params.getRecordType());
			// filter log
			List<LogSetItemDetailDto> listLogSetItemDetailDto = params.getListLogSetItemDetailDto();
			// boolean check = false;
			if (!CollectionUtil.isEmpty(listLogSetItemDetailDto)) {
				// boolean check = false;
				
				boolean check = true;
				if (!row.isEmpty()) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {

						for (LogOutputItemDto logOutputItemDto : headers) {

							for (LogSetItemDetailDto logSetItemDetailDto : listLogSetItemDetailDto) {
								if (logSetItemDetailDto.getItemNo() == logOutputItemDto.getItemNo()
										&& logOutputItemDto.getItemName().equals(entry.getKey())) {
									// 0 like,1 bang,2 khac  
									if (logSetItemDetailDto.getSybol().intValue() == 0) {
										
										if (!Objects.isNull(entry.getValue()) && entry.getValue().toString().contains(logSetItemDetailDto.getCondition())) {
											check = true;
											break;
										}else{
											check = false;
										}
									}
									if (logSetItemDetailDto.getSybol().intValue() == 1) {
										if (logSetItemDetailDto.getCondition().equals(entry.getValue())) {
											check = true;
											break;
										}else{
											check = false;
										}

									}
									if (logSetItemDetailDto.getSybol().intValue() == 2) {
										if (!logSetItemDetailDto.getCondition().equals(entry.getValue())) {
											check = true;
											break;
										}else{
											check = false;
										}

									}

								}

							}
							if (check == false) {
								break;
							}

						}

					}
					if (check) {
						dataSource.add(row);
					}

				}

			} else {
				dataSource.add(row);
			}
		});

		return dataSource;
	}

	private Map<String, Object> checkHeader(LogBasicInfoAllDto logBaseDto, List<LogOutputItemDto> headers,
			int recordType) {
		Map<String, Object> dataReturn = new HashMap<>();
		RecordTypeEnum recordTypeEnum = RecordTypeEnum.valueOf(recordType);
		for (LogOutputItemDto a : headers) {
			// int itno=a.getItemNo();
			ItemNoEnum itemNoEnum = ItemNoEnum.valueOf(a.getItemNo());
			switch (itemNoEnum) {
			case ITEM_NO_1:
				dataReturn.put(a.getItemName(), logBaseDto.getUserIdLogin());
				break;
			case ITEM_NO_2:
				dataReturn.put(a.getItemName(), logBaseDto.getUserNameLogin());
				break;
			case ITEM_NO_3:
				dataReturn.put(a.getItemName(), logBaseDto.getEmployeeCodeLogin());
				break;
			case ITEM_NO_4:
				dataReturn.put(a.getItemName(), logBaseDto.getIpAddress());
				break;
			case ITEM_NO_5:
				dataReturn.put(a.getItemName(), logBaseDto.getPcName());
				break;
			case ITEM_NO_6:
				dataReturn.put(a.getItemName(), logBaseDto.getAccount());
				break;
			case ITEM_NO_7:
				dataReturn.put(a.getItemName(), logBaseDto.getModifyDateTime());
				break;
			case ITEM_NO_8:
				dataReturn.put(a.getItemName(), logBaseDto.getEmploymentAuthorityName());
				break;
			case ITEM_NO_9:
				dataReturn.put(a.getItemName(), logBaseDto.getSalarytAuthorityName());
				break;
			case ITEM_NO_10:
				dataReturn.put(a.getItemName(), logBaseDto.getPersonelAuthorityName());
				break;
			case ITEM_NO_11:
				dataReturn.put(a.getItemName(), logBaseDto.getOfficeHelperAuthorityName());
				break;
			case ITEM_NO_12:
				dataReturn.put(a.getItemName(), logBaseDto.getAccountAuthorityName());
				break;
			case ITEM_NO_13:
				dataReturn.put(a.getItemName(), logBaseDto.getMyNumberAuthorityName());
				break;
			case ITEM_NO_14:
				dataReturn.put(a.getItemName(), logBaseDto.getGroupCompanyAddminAuthorityName());
				break;
			case ITEM_NO_15:
				dataReturn.put(a.getItemName(), logBaseDto.getCompanyAddminAuthorityName());
				break;
			case ITEM_NO_16:
				dataReturn.put(a.getItemName(), logBaseDto.getSystemAdminAuthorityName());
				break;
			case ITEM_NO_17:
				dataReturn.put(a.getItemName(), logBaseDto.getPersonalInfoAuthorityName());
				break;
			default:
				break;
			}
			switch (recordTypeEnum) {
			case LOGIN:
				switch (itemNoEnum) {
				case ITEM_NO_18:
					dataReturn.put(a.getItemName(), logBaseDto.getMenuName());
					break;
				case ITEM_NO_19:
					dataReturn.put(a.getItemName(), logBaseDto.getLoginStatus());
					break;
				case ITEM_NO_20:
					dataReturn.put(a.getItemName(), logBaseDto.getLoginMethod());
					break;
				case ITEM_NO_21:
					dataReturn.put(a.getItemName(), logBaseDto.getAccessResourceUrl());
					break;
				case ITEM_NO_22:
					dataReturn.put(a.getItemName(), logBaseDto.getNote());
					break;
				default:
					break;
				}

				break;
			case START_UP:
				switch (itemNoEnum) {
				case ITEM_NO_18:
					dataReturn.put(a.getItemName(), logBaseDto.getNote());
					break;
				case ITEM_NO_19:
					dataReturn.put(a.getItemName(), logBaseDto.getMenuName());
					break;
				case ITEM_NO_20:
					dataReturn.put(a.getItemName(), logBaseDto.getMenuNameReSource());
					break;
				default:
					break;

				}
				break;
			case UPDATE_PERSION_INFO:
				switch (itemNoEnum) {
				case ITEM_NO_18:
					dataReturn.put(a.getItemName(), logBaseDto.getMenuName());
					break;
				case ITEM_NO_19:
					dataReturn.put(a.getItemName(), logBaseDto.getUserIdTaget());
					break;
				case ITEM_NO_20:
					dataReturn.put(a.getItemName(), logBaseDto.getUserNameTaget());
					break;
				case ITEM_NO_21:
					dataReturn.put(a.getItemName(), logBaseDto.getEmployeeCodeTaget());
					break;
				case ITEM_NO_22:
					dataReturn.put(a.getItemName(), logBaseDto.getCategoryProcess());
					break;
				case ITEM_NO_23:
					dataReturn.put(a.getItemName(), logBaseDto.getCategoryName());
					break;
				case ITEM_NO_24:
					dataReturn.put(a.getItemName(), logBaseDto.getMethodCorrection());
					break;
				case ITEM_NO_25:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetYmd());
					break;
				case ITEM_NO_26:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetYm());
					break;
				case ITEM_NO_27:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetY());
					break;
				case ITEM_NO_28:
					dataReturn.put(a.getItemName(), logBaseDto.getKeyString());
					break;
				case ITEM_NO_29:
					dataReturn.put(a.getItemName(), logBaseDto.getItemName());
					break;
				case ITEM_NO_30:
					dataReturn.put(a.getItemName(), logBaseDto.getItemvalueBefor());
					break;
				case ITEM_NO_31:
					dataReturn.put(a.getItemName(), logBaseDto.getItemContentValueBefor());
					break;
				case ITEM_NO_32:
					dataReturn.put(a.getItemName(), logBaseDto.getItemvalueAppter());
					break;
				case ITEM_NO_33:
					dataReturn.put(a.getItemName(), logBaseDto.getItemContentValueAppter());
					break;
				case ITEM_NO_34:
					dataReturn.put(a.getItemName(), logBaseDto.getItemEditAddition());
					break;
				case ITEM_NO_35:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetYmdEditAddition());
					break;
				case ITEM_NO_36:
					dataReturn.put(a.getItemName(), logBaseDto.getNote());
					break;
				default:
					break;

				}
				break;
			case DATA_CORRECT:
				switch (itemNoEnum) {
				case ITEM_NO_18:
					dataReturn.put(a.getItemName(), logBaseDto.getMenuName());
					break;
				case ITEM_NO_19:
					dataReturn.put(a.getItemName(), logBaseDto.getUserIdTaget());
					break;
				case ITEM_NO_20:
					dataReturn.put(a.getItemName(), logBaseDto.getUserNameTaget());
					break;
				case ITEM_NO_21:
					dataReturn.put(a.getItemName(), logBaseDto.getEmployeeCodeTaget());
					break;
				case ITEM_NO_22:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetYmd());
					break;
				case ITEM_NO_23:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetYm());
					break;
				case ITEM_NO_24:
					dataReturn.put(a.getItemName(), logBaseDto.getTarGetY());
					break;
				case ITEM_NO_25:
					dataReturn.put(a.getItemName(), logBaseDto.getKeyString());
					break;
				case ITEM_NO_26:
					dataReturn.put(a.getItemName(), logBaseDto.getCatagoryCorection());
					break;
				case ITEM_NO_27:
					dataReturn.put(a.getItemName(), logBaseDto.getItemName());
					break;
				case ITEM_NO_28:
					dataReturn.put(a.getItemName(), logBaseDto.getItemvalueBefor());
					break;
				case ITEM_NO_29:
					dataReturn.put(a.getItemName(), logBaseDto.getItemvalueAppter());
					break;
				case ITEM_NO_30:
					dataReturn.put(a.getItemName(), logBaseDto.getItemContentValueBefor());
					break;
				case ITEM_NO_31:
					dataReturn.put(a.getItemName(), logBaseDto.getItemContentValueAppter());
					break;
				case ITEM_NO_32:
					dataReturn.put(a.getItemName(), logBaseDto.getNote());
					break;
				default:
					break;

				}
				break;

			default:
				break;
			}
		}
		return dataReturn;
	}
	
	private Map<String, String> getRoleNameByRoleId(String cid, List<StartPageLog> listStartPageLog, List<LoginRecord> rsLoginRecord, Map<String, LogBasicInformation> map,
			List<PersonInfoCorrectionLog> listPersonInfoCorrectionLog,
			List<LogDataCorrectRecordAllDto> lstLogDataCorecRecordRefeDto){
		Map<String, String> result = new HashMap<>();
		List<LoginUserRoles> loginUserRoleLst = new ArrayList<>();
		if(!listStartPageLog.isEmpty()) {
			loginUserRoleLst.addAll(listStartPageLog.stream()
					.map(c -> c.getBasicInfo().getAuthorityInformation()).collect(Collectors.toList()));
		}

		if(!rsLoginRecord.isEmpty()) {
			rsLoginRecord.stream().forEach(loginRecord ->{
				LogBasicInformation	logBasicInformation = map.get(loginRecord.getOperationId());
				if(logBasicInformation!= null) {
					loginUserRoleLst.add(logBasicInformation.getAuthorityInformation());
				}
			});
		}
		
		if(!listPersonInfoCorrectionLog.isEmpty()) {
			listPersonInfoCorrectionLog.stream().forEach(personInfoCorrectionLog ->{
				List<CategoryCorrectionLog> rsListCategoryCorrectionLog = personInfoCorrectionLog
						.getCategoryCorrections();
				if (!CollectionUtil.isEmpty(rsListCategoryCorrectionLog)) {
					rsListCategoryCorrectionLog.stream().forEach(categoryCorrectionLog ->{
						List<ItemInfo> rsItemInfo = categoryCorrectionLog.getItemInfos();
						if (!CollectionUtil.isEmpty(rsItemInfo)) {
							rsItemInfo.stream().forEach(itemInfo ->{
								LogBasicInformation	logBasicInformation = map.get(personInfoCorrectionLog.getOperationId());
								if(logBasicInformation!= null) {
									loginUserRoleLst.add(logBasicInformation.getAuthorityInformation());
								}
							});
						}
					});
				}
			});
		}
		
		if (!CollectionUtil.isEmpty(lstLogDataCorecRecordRefeDto)) {
			lstLogDataCorecRecordRefeDto.stream().forEach(logDataCorrectRecordRefeDto ->{
				// convert log basic info to DTO
				LogBasicInformation logBasicInformation =map.get(logDataCorrectRecordRefeDto.getOperationId());
				if(logBasicInformation!= null) {
					loginUserRoleLst.add(logBasicInformation.getAuthorityInformation());
				}
			});
			
		}
		List<String> forAttendances = new ArrayList<>();
		List<String> forPayrolls = new ArrayList<>();
		List<String> forPersonnels = new ArrayList<>();
		List<String> forOfficeHelpers = new ArrayList<>();
		List<String> forGroupCompaniesAdmins = new ArrayList<>();
		List<String> forCompanyAdmins = new ArrayList<>();
		List<String> forSystemAdmins = new ArrayList<>();
		List<String> forPersonalInfos = new ArrayList<>();
		
		loginUserRoleLst.stream().forEach(c ->{
			if(c.forAttendance()!= null) {
				forAttendances.add(c.forAttendance());
			}
			
			if(c.forPayroll()!= null) {
				forPayrolls.add(c.forPayroll());
			}
			
			if(c.forPersonnel()!= null) {
				forPersonnels.add(c.forPersonnel());
			}
			
			if(c.forOfficeHelper()!= null) {
				forOfficeHelpers.add(c.forOfficeHelper());
			}
			
			if(c.forGroupCompaniesAdmin()!= null) {
				forGroupCompaniesAdmins.add(c.forGroupCompaniesAdmin());
			}
			
			if(c.forCompanyAdmin()!= null) {
				forCompanyAdmins.add(c.forCompanyAdmin());
			}
			
			if(c.forSystemAdmin()!= null) {
				forSystemAdmins.add(c.forSystemAdmin());
			}
			
			if(c.forPersonalInfo()!= null) {
				forPersonalInfos.add(c.forPersonalInfo());
			}
			
		});
		
		List<String> roleIdInters = new ArrayList<>();
		
		if(!forAttendances.isEmpty()) {
			roleIdInters.addAll(forAttendances);
		}
		if(!forPayrolls.isEmpty()) {
			roleIdInters.addAll(forPayrolls);
		}
		
		if(!forPersonnels.isEmpty()) {
			roleIdInters.addAll(forPersonnels);
		}
		
		if(!forOfficeHelpers.isEmpty()) {
			roleIdInters.addAll(forOfficeHelpers);
		}
		
		if(!forGroupCompaniesAdmins.isEmpty()) {
			roleIdInters.addAll(forGroupCompaniesAdmins);
		}
		
		if(!forCompanyAdmins.isEmpty()) {
			roleIdInters.addAll(forCompanyAdmins);
		}
		
		if(!forSystemAdmins.isEmpty()) {
			roleIdInters.addAll(forSystemAdmins);
		}
		
		if(!forPersonalInfos.isEmpty()) {
			roleIdInters.addAll(forPersonalInfos);
		}
		
		List<String> roleIds = roleIdInters.stream().distinct().collect(Collectors.toList());
		if(!roleIds.isEmpty()) {
			return  roleExportAdapter.getNameLstByRoleIds(cid, roleIds);
		}
		return result;
	}
	
	// chuẩn bị dữ liệu cho việc in
	public void prepareData(LogParamsVer1 logParams) {
		List<String> listItemNo = new ArrayList<>();
		List<LogOutputItemDto> listHeaderSort = new ArrayList<>();
		List<LogSetOutputItemDto> dataOutPutItem = new ArrayList<>();
		List<LogSetItemDetailDto> listLogSetItemDetailDto = new ArrayList<>();
		LogDisplaySettingDto dataLogDisplaySetting = this.logDisplaySettingFinder
				.getLogDisplaySettingByCodeAndFlag(logParams.getLogDisplaySettingCode());
		if (dataLogDisplaySetting != null) {
			dataOutPutItem.addAll(dataLogDisplaySetting.getLogSetOutputItems());
			logParams.setTargetDataType(
					dataLogDisplaySetting.getDataType() == null ? 0 : dataLogDisplaySetting.getDataType());
			// function get logoutputItem by recordType and itemNo
			if (dataOutPutItem.size() > 0) {
				dataOutPutItem.stream().forEach(dataItemNo -> {
					List<LogSetItemDetailDto> logSetItemDetails = dataItemNo.getLogSetItemDetails();
					if (!CollectionUtil.isEmpty(logSetItemDetails)) {
						logSetItemDetails.stream().forEach(datatemDetail -> {
							if (datatemDetail.getIsUseCondFlg() == 1) {
								listLogSetItemDetailDto.add(datatemDetail);
							}
						});

					}
					listItemNo.add(String.valueOf(dataItemNo.getItemNo()));
				});

			}

			ParamOutputItem paramOutputItem = new ParamOutputItem(logParams.getRecordType(), listItemNo);
			if (listItemNo.size() > 0) {
				List<LogOutputItemDto> dataOutputItems = this.logOuputItemFinder
						.getLogOutputItemByItemNosAndRecordTypeAll(paramOutputItem.getItemNos(),
								paramOutputItem.getRecordType());
				if (dataOutputItems != null && dataOutputItems.size() > 0) {
					List<LogBasicInfoAllDto> data = findByOperatorsAndDate(logParams);
					dataOutPutItem.stream().forEach(dataItemNoOrder -> {
						dataOutputItems.stream().forEach(listdataName -> {
							if (dataItemNoOrder.getItemNo() == listdataName.getItemNo()) {
								listHeaderSort.add(listdataName);
							}

						});

					});
					if (!listHeaderSort.isEmpty()) {
						logParams.setLstHeaderDto(listHeaderSort);
					}
					if (data != null && !data.isEmpty()) {
						logParams.setListLogBasicInfoAllDto(data);
						List<Map<String, Object>> lstData = getDataLog(logParams);
						logParams.setListDataExport(lstData);
					} else {
						throw new BusinessException("Msg_1220");

					}
				} else {
					throw new BusinessException("Msg_1221");
				}
			} else {
				throw new BusinessException("Msg_1221");
			}
		} else {
			throw new BusinessException("Msg_1215");
		}
	}
}
