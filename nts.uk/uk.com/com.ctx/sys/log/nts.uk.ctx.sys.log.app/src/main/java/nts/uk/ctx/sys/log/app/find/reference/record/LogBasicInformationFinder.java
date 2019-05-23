package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.app.find.reference.LogOuputItemFinder;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.dom.logbasicinfo.LogBasicInfoRepository;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecord;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository;
import nts.uk.ctx.sys.log.dom.pereg.IPersonInfoCorrectionLogRepository;
import nts.uk.ctx.sys.log.dom.reference.IPerInfoCtgOrderByComAdapter;
import nts.uk.ctx.sys.log.dom.reference.ItemNoEnum;
import nts.uk.ctx.sys.log.dom.reference.PersonEmpBasicInfoAdapter;
import nts.uk.ctx.sys.log.dom.reference.RecordTypeEnum;
import nts.uk.ctx.sys.log.dom.reference.WebMenuAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataType;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.CategoryCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoCorrectionLog;
import nts.uk.shr.com.security.audittrail.start.StartPageLog;
import nts.uk.shr.com.security.audittrail.start.StartPageLogRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/*
 * author : thuong.tv
 */

@Stateless
public class LogBasicInformationFinder {

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

	@Inject
	private LogOuputItemFinder logOuputItemFinder;
	
	@Inject
	private WebMenuAdapter webMenuAdapter;
	
	/** The PersonEmpBasicInfoPub. */
	@Inject
	private PersonEmpBasicInfoAdapter personEmpBasicInfoAdapter;
	
	@Inject
	private IPerInfoCtgOrderByComAdapter iPerInfoCtgOrderByComAdapter;

	public List<LogBasicInfoDto> findByOperatorsAndDate(LogParams logParams) {
		List<LogBasicInfoDto> lstLogBacsicInfo = new ArrayList<>();
		Map<String, String> mapEmployeeCodes;
		// get login info
		LoginUserContext loginUserContext = AppContexts.user();
		RecordTypeEnum recordTypeEnum = RecordTypeEnum.valueOf(logParams.getRecordType());
		// get company id
		String cid = loginUserContext.companyId();
		/* DatePeriod datePeriodOperator = new DatePeriod(logParams.getStartDateOperator(),
		logParams.getEndDateOperator());*/
		DatePeriod datePeriodTaget = new DatePeriod(logParams.getStartDateTaget(), logParams.getEndDateTaget());
		List<LogBasicInformation> lstLogBasicInformation = new ArrayList<>();
		if(recordTypeEnum.code != RecordTypeEnum.START_UP.code){
		 lstLogBasicInformation = this.logBasicInfoRepository.findByOperatorsAndDate(cid,
					logParams.getListOperatorEmployeeId(), logParams.getStartDateOperator(),
					logParams.getEndDateOperator());
		}
		
		if (!CollectionUtil.isEmpty(lstLogBasicInformation)) {
			// Get list OperationId
			Map<String, LogBasicInformation> mapLogBasicInfo = new HashMap<>();
			List<String> employeeIds = new ArrayList<>();
			List<String> operationIds = lstLogBasicInformation.stream().map(x -> {
				mapLogBasicInfo.put(x.getOperationId(), x);
				if (x.getUserInfo() != null) {
					employeeIds.add(x.getUserInfo().getEmployeeId());
				}
				return x.getOperationId();
			}).collect(Collectors.toList());

			switch (recordTypeEnum) {
			case LOGIN:
					// Set data of login record
					List<LoginRecord> loginRecords = this.loginRecordRepository.logRecordInfor(operationIds);
					if(!CollectionUtil.isEmpty(loginRecords)){
						// Get list employeeCode operator by list information operator
						mapEmployeeCodes = getEmployeeCodes(recordTypeEnum,mapLogBasicInfo,loginRecords,null,null,null);
						List<LogBasicInfoDto> logBasicLst = loginRecords.parallelStream().map(loginRecord ->{
							// Convert log basic info to DTO
							LogBasicInformation logBasicInformation = mapLogBasicInfo.get(loginRecord.getOperationId());
							LogBasicInfoDto logBasicInfoDto = LogBasicInfoDto.fromDomain(logBasicInformation);
							UserInfo userDto = logBasicInformation.getUserInfo();
							if (userDto != null) {
								logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
							}
							// Set user login name
							logBasicInfoDto.setUserNameLogin(userDto.getUserName());
							logBasicInfoDto.setMethodName(loginRecord.getLoginMethod().description);
							logBasicInfoDto.setLoginStatus(loginRecord.getLoginStatus().description);
							logBasicInfoDto
									.setNote(loginRecord.getRemarks().isPresent() ? loginRecord.getRemarks().get() : "");
							return logBasicInfoDto;
							
						}).collect(Collectors.toList());
						lstLogBacsicInfo.addAll(logBasicLst);
					}
				break;
			case START_UP:
				break;
			case UPDATE_PERSION_INFO:
				String[] listSubHeaderText = { "23", "24", "29", "31", "33" };
				// Get persion info log
				List<PersonInfoCorrectionLog> listPersonInfoCorrectionLog = this.iPersonInfoCorrectionLogRepository
						.findByTargetAndDate(operationIds,logParams.getListTagetEmployeeId());
				
				// Get list employeeCode operator by list information operator
				mapEmployeeCodes = getEmployeeCodes(recordTypeEnum,mapLogBasicInfo, null, listPersonInfoCorrectionLog, null, null);
				
				// Get Map Order list
				List<String> categoryIds = new ArrayList<>();
				List<String> itemDefinitionIds = new ArrayList<>();
				List<PersonInfoCorrectionLog> listDataPersionInforReturn = new ArrayList<>();
				
				List<String> ctgIds = listPersonInfoCorrectionLog == null? new ArrayList<>(): listPersonInfoCorrectionLog.parallelStream().filter(c -> !CollectionUtil.isEmpty(c.getCategoryCorrections()))
						.map(perCorrectLog ->{
							listDataPersionInforReturn.add(perCorrectLog);
							List<CategoryCorrectionLog> lstCate = perCorrectLog.getCategoryCorrections();
								  return  lstCate.parallelStream().filter(c -> !CollectionUtil.isEmpty(c.getItemInfos())).map(c -> {
									List<String> itemIds = c.getItemInfos().parallelStream().map(item -> item.getItemId()).collect(Collectors.toList());
									itemDefinitionIds.addAll(itemIds);
									return c.getCategoryId();
								}).collect(Collectors.toList());
								
						})
						.flatMap(c -> c.stream())
						.distinct()
						.collect(Collectors.toList());
				
				if(!CollectionUtil.isEmpty(ctgIds)) {
					categoryIds.addAll(ctgIds);
				}
				
				HashMap<Integer, HashMap<String, Integer>> mapCheckOrder = iPerInfoCtgOrderByComAdapter.getOrderList(categoryIds, itemDefinitionIds);
				
					Map<String, LogBasicInfoDto> mapCheck = new HashMap<>();
					// Get list subHeader
					List<LogOutputItemDto> lstHeaderTemp = new ArrayList<>();
					List<LogOutputItemDto> lstHeader = logOuputItemFinder.getLogOutputItemByItemNosAndRecordType(Arrays.asList(listSubHeaderText),logParams.getRecordType());
					listDataPersionInforReturn.parallelStream().forEach(personInfoCorrectionLog ->{
						// Convert log basic info to DTO
						LogBasicInformation logBasicInformation = mapLogBasicInfo.get(personInfoCorrectionLog.getOperationId());
						LogBasicInfoDto logBasicInfoDto = LogBasicInfoDto.fromDomain(logBasicInformation);
						String keyCheck = personInfoCorrectionLog.getOperationId() + personInfoCorrectionLog.getTargetUser().getEmployeeId();
						logBasicInfoDto.setParentKey(keyCheck);
						List<LogPerCateCorrectRecordDto> logPerCateCorrectRecordDtos = LogPerCateCorrectRecordDto
								.fromDomain(personInfoCorrectionLog, logBasicInfoDto.getParentKey(),mapCheckOrder);
						if (mapCheck.containsKey(keyCheck)) {
							LogBasicInfoDto logBasicCheck = mapCheck.get(keyCheck);
							List<LogPerCateCorrectRecordDto> dataChildrent = logBasicCheck.getLstLogPerCateCorrectRecordDto();
							if (!CollectionUtil.isEmpty(logPerCateCorrectRecordDtos)) {
								for (LogPerCateCorrectRecordDto logPerCateCorrectRecordDto : logPerCateCorrectRecordDtos) {
									dataChildrent.add(logPerCateCorrectRecordDto);
								}
							}
							logBasicCheck.setLstLogPerCateCorrectRecordDto(dataChildrent);
							mapCheck.replace(keyCheck, logBasicCheck);
						} else {

							LogBasicInfoDto logTemp = getEmpCodeByEmpId(logBasicInfoDto,personInfoCorrectionLog.getTargetUser().getEmployeeId(),
									personInfoCorrectionLog.getTargetUser().getUserName());
							// Setting infor logBasicInfoDto
							UserInfo userDto = logBasicInformation.getUserInfo();
							// get employee code login
							if (userDto != null) {
								logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
							}
							// get user login name
							logBasicInfoDto.setUserNameLogin(userDto.getUserName());

							logTemp.setLstLogPerCateCorrectRecordDto(logPerCateCorrectRecordDtos);
							lstHeader.stream().forEach(logOutputItemDto ->{
								if (logOutputItemDto.getItemNo() == ItemNoEnum.ITEM_NO_23.code) {
									lstHeaderTemp.add(logOutputItemDto);
									lstHeaderTemp.add(new LogOutputItemDto(99, TextResource.localize("CLI003_61"),logParams.getRecordType()));
								} else {
									lstHeaderTemp.add(logOutputItemDto);
								}
							});
							logTemp.setLstLogOutputItemDto(lstHeaderTemp);
							logTemp.setNote(personInfoCorrectionLog.getRemark());
							logTemp.setProcessAttr(LogPerCateCorrectRecordDto.getPersonInfoProcessAttr(personInfoCorrectionLog.getProcessAttr().value));
							mapCheck.put(keyCheck, logTemp);
						}						
					});
					// Convert data to list
					lstLogBacsicInfo = new ArrayList<LogBasicInfoDto>(mapCheck.values());
				break;
			case DATA_CORRECT:
				TargetDataType targetDataType= TargetDataType.of(logParams.getTargetDataType()) ;
				Map<String,LogBasicInfoDto> mapCheckLogBasic = new HashMap<>();
					// get data correct log
					List<DataCorrectionLog> lstDataCorectLog = this.dataCorrectionLogRepository.findByTargetAndDate(
							operationIds, logParams.getListTagetEmployeeId(), datePeriodTaget,targetDataType);
					if (!CollectionUtil.isEmpty(lstDataCorectLog)) {
						// Get list employeeCode operator by list information operator
						mapEmployeeCodes = getEmployeeCodes(recordTypeEnum,mapLogBasicInfo,null,null,lstDataCorectLog,null);
						lstDataCorectLog.stream().forEach(dataCorrectionLog ->{
							// convert log basic info to DTO
							LogBasicInformation logBasicInformation = mapLogBasicInfo.get(dataCorrectionLog.getOperationId());
							LogBasicInfoDto logBasicInfoDto = LogBasicInfoDto.fromDomain(logBasicInformation);
							LogDataCorrectRecordRefeDto logDataCorrectRecordRefeDto = LogDataCorrectRecordRefeDto.fromDomain(dataCorrectionLog);
							String keyEmploy = dataCorrectionLog.getOperationId() + logDataCorrectRecordRefeDto.getEmployeeIdtaget();
							logBasicInfoDto.setParentKey(keyEmploy);
							// group employId
							if(mapCheckLogBasic.containsKey(keyEmploy)){
								LogBasicInfoDto logBasicCheck = mapCheckLogBasic.get(keyEmploy);
								List<LogDataCorrectRecordRefeDto> dataChildrent = logBasicCheck.getLstLogDataCorrectRecordRefeDto();
								dataChildrent.add(logDataCorrectRecordRefeDto);
								logBasicCheck.setLstLogDataCorrectRecordRefeDto(dataChildrent);
								mapCheckLogBasic.replace(keyEmploy, logBasicCheck);	
								
							}else{
								UserInfo userDto = logBasicInformation.getUserInfo();
								// get employee code login
								if (userDto != null) {
									logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
								}
								// get user login name
								logBasicInfoDto.setUserNameLogin(userDto.getUserName());
								
								// convert list data corect log to DTO
								List<LogDataCorrectRecordRefeDto> lstLogDataCorecRecordRefeDto = new ArrayList<>();
								LogBasicInfoDto logTemp = getEmpCodeByEmpId(logBasicInfoDto,logDataCorrectRecordRefeDto.getEmployeeIdtaget(),logDataCorrectRecordRefeDto.getUserNameTaget());
								logDataCorrectRecordRefeDto.setParentKey(keyEmploy);
								lstLogDataCorecRecordRefeDto.add(logDataCorrectRecordRefeDto);
								logTemp.setLstLogDataCorrectRecordRefeDto(lstLogDataCorecRecordRefeDto);
								// set header
								List<LogOutputItemDto> listSubHeader = getSubHeaderDataCorrectList(logDataCorrectRecordRefeDto.getTargetDataType(),logParams.getRecordType());
								logTemp.setLstLogOutputItemDto(listSubHeader);
								mapCheckLogBasic.put(keyEmploy, logTemp);
							}
							
						});
					}
				// xử lý input map to lists
				lstLogBacsicInfo = new ArrayList<LogBasicInfoDto>(mapCheckLogBasic.values());
				break;
			default:
				break;
			}
		}else{
			if(recordTypeEnum.code == RecordTypeEnum.START_UP.code){
				Map<String, String> mapProgramNames = webMenuAdapter.getWebMenuByCId(cid);
				// get start page log
				List<StartPageLog> startPageLogs = this.startPageLogRepository.findBy(cid,
						logParams.getListOperatorEmployeeId(), logParams.getStartDateOperator(),
						logParams.getEndDateOperator());
				if (!CollectionUtil.isEmpty(startPageLogs)) {
					// Get list employee code
					mapEmployeeCodes = getEmployeeCodes(recordTypeEnum,null,null,null,null,startPageLogs);
					List<LogBasicInfoDto> logBasicInfoDtoInter = startPageLogs.stream().map(startPageLog -> {
						// Convert log basic info to DTO
						LogBasicInformation logBasicInformation = startPageLog.getBasicInfo();
						LogBasicInfoDto logBasicInfoDto = LogBasicInfoDto.fromDomain(logBasicInformation);
						// Set user login name
						UserInfo userDto = logBasicInformation.getUserInfo();

						String key = logBasicInformation.getTargetProgram().getProgramId()
								+ logBasicInformation.getTargetProgram().getScreenId()
								+ logBasicInformation.getTargetProgram().getQueryString();
						logBasicInfoDto.setMenuName(mapProgramNames.get(key));
						// Get employee code user login
						if (userDto != null) {
							logBasicInfoDto.setEmployeeCodeLogin(mapEmployeeCodes.get(userDto.getEmployeeId()));
						}
						// get user login name
						logBasicInfoDto.setUserNameLogin(userDto.getUserName());
						// logBasicInfoDto.setMenuName(programName);
						logBasicInfoDto.setNote(
								logBasicInformation.getNote().isPresent() ? logBasicInformation.getNote().get() : "");
						return logBasicInfoDto;
					}).collect(Collectors.toList());
					lstLogBacsicInfo.addAll(logBasicInfoDtoInter);
				}
			}
		}
		return lstLogBacsicInfo;
	}
	
	public LogBasicInfoDto getEmpCodeByEmpId(LogBasicInfoDto logBasicInfoDto,String employeeIdTaget,String userNameTaget) {
		
		// get employee code taget
		logBasicInfoDto.setEmployeeCodeTaget(personEmpBasicInfoAdapter.getEmployeeCodeByEmpId(employeeIdTaget));
		logBasicInfoDto.setUserNameTaget(userNameTaget);	
		
		return logBasicInfoDto;
	}
	
	public List<LogOutputItemDto> getSubHeaderDataCorrectList(int tagetDataKey,int recordType) {
		String[] listHeaderDateTimeText = { "22", "26", "27", "30", "31" };
		String[] listHeaderMothText = { "23", "26", "27", "30", "31" };
		String[] listHeaderYearText = { "24", "26", "27", "30", "31" };
		// get header
		List<LogOutputItemDto> lstHeader = new ArrayList<>();
		if (tagetDataKey == TargetDataType.SCHEDULE.value
				|| tagetDataKey == TargetDataType.DAILY_RECORD.value) {
			lstHeader = logOuputItemFinder.getLogOutputItemByItemNosAndRecordType(
					Arrays.asList(listHeaderDateTimeText), recordType);
		}
		if (tagetDataKey == TargetDataType.MONTHLY_RECORD.value
				|| tagetDataKey == TargetDataType.ANY_PERIOD_SUMMARY.value
				|| tagetDataKey == TargetDataType.SALARY_DETAIL.value
				|| tagetDataKey == TargetDataType.BONUS_DETAIL.value) {
			lstHeader = logOuputItemFinder.getLogOutputItemByItemNosAndRecordType(
					Arrays.asList(listHeaderMothText), recordType);
		}
		if (tagetDataKey == TargetDataType.YEAR_END_ADJUSTMENT.value
				|| tagetDataKey == TargetDataType.MONTHLY_CALCULATION.value
				|| tagetDataKey == TargetDataType.RISING_SALARY_BACK.value) {
			lstHeader = logOuputItemFinder.getLogOutputItemByItemNosAndRecordType(
					Arrays.asList(listHeaderYearText), recordType);
		}
		
		return lstHeader;
	}
	
	public Map<String, String> getEmployeeCodes(RecordTypeEnum recordTypeEnum,Map<String, LogBasicInformation> mapLogBasicInfo,
			List<LoginRecord> loginRecords, List<PersonInfoCorrectionLog> persionLogs, List<DataCorrectionLog> dataCorectLogs,
			List<StartPageLog> startPageLogs){
		List<String> employeeIds = new ArrayList<>();
		switch (recordTypeEnum) {
		case LOGIN:
			for (LoginRecord loginRecord : loginRecords) {
				if(mapLogBasicInfo.containsKey(loginRecord.getOperationId())){
					employeeIds.add(mapLogBasicInfo.get(loginRecord.getOperationId()).getUserInfo().getEmployeeId()) ;
				}
			}
			break;
		case START_UP:
			for (StartPageLog startPageLog : startPageLogs) {
				if (startPageLog.getBasicInfo() != null) {
					employeeIds.add(startPageLog.getBasicInfo().getUserInfo().getEmployeeId());
				}
			}
			break;
		case UPDATE_PERSION_INFO:
			for (PersonInfoCorrectionLog persionlog : persionLogs) {
				if(mapLogBasicInfo.containsKey(persionlog.getOperationId())){
					employeeIds.add(mapLogBasicInfo.get(persionlog.getOperationId()).getUserInfo().getEmployeeId()) ;
				}
			}
			break;
		case DATA_CORRECT:
			for (DataCorrectionLog dataCorrectionLog : dataCorectLogs) {
				if(mapLogBasicInfo.containsKey(dataCorrectionLog.getOperationId())){
					employeeIds.add(mapLogBasicInfo.get(dataCorrectionLog.getOperationId()).getUserInfo().getEmployeeId()) ;
				}
			}
			break;
			
		default:
			break;
		}
		
		// Get list employee code by list id
		return personEmpBasicInfoAdapter.getEmployeeCodesByEmpIds(employeeIds);
	}

}