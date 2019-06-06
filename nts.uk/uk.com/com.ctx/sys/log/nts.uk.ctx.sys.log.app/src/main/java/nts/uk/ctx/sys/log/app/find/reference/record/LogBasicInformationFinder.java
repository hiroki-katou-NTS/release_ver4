package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Getter;
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
					List<String> itemDefinitionIds = new ArrayList<>();
					List<PersonInfoCorrectionLog> listDataPersionInforReturn = new ArrayList<>();
					
					List<String> categoryIds = listPersonInfoCorrectionLog == null? new ArrayList<>(): listPersonInfoCorrectionLog.parallelStream().filter(c -> !CollectionUtil.isEmpty(c.getCategoryCorrections()))
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

								LogBasicInfoDto logTemp = getEmpCodeByEmpId(logBasicInfoDto, mapEmployeeCodes.get(personInfoCorrectionLog.getTargetUser().getEmployeeId()),
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
							// tối ưu lấy header
							Map<String, TargetDataCorrect> itemOutHeaders = lstDataCorectLog.stream()
									.filter(distinctByKey(DataCorrectionLog::getTargetDataType))
									.collect(Collectors.toMap(c -> String.valueOf(logParams.getRecordType()) + String.valueOf(c.getTargetDataType().value),
								     c -> new TargetDataCorrect(logParams.getRecordType(), c.getTargetDataType().value)));
							Map<String, List<LogOutputItemDto>>  headerMaps =  getSubHeaderDataCorrectList(itemOutHeaders, logParams.getRecordType());
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
									// set employeeCode của target name
									LogBasicInfoDto logTemp = getEmpCodeByEmpId(logBasicInfoDto, mapEmployeeCodes.get(logDataCorrectRecordRefeDto.getEmployeeIdtaget()),logDataCorrectRecordRefeDto.getUserNameTaget());
									logDataCorrectRecordRefeDto.setParentKey(keyEmploy);
									lstLogDataCorecRecordRefeDto.add(logDataCorrectRecordRefeDto);
									logTemp.setLstLogDataCorrectRecordRefeDto(lstLogDataCorecRecordRefeDto);
									// set header
									logTemp.setLstLogOutputItemDto(headerMaps.get(String.valueOf(logDataCorrectRecordRefeDto.getTargetDataType())+ String.valueOf(logParams.getRecordType())));
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
	
	public LogBasicInfoDto getEmpCodeByEmpId(LogBasicInfoDto logBasicInfoDto, String employeeCode, String userNameTaget) {
		// get employee code taget
		logBasicInfoDto.setEmployeeCodeTaget(employeeCode);
		logBasicInfoDto.setUserNameTaget(userNameTaget);	
		
		return logBasicInfoDto;
	}
	
	public Map<String, List<LogOutputItemDto>> getSubHeaderDataCorrectList(Map<String, TargetDataCorrect> targetDataCorrect, int recordType) {
		List<String> listHeaderDateTimeText = Arrays.asList("22", "26", "27", "30", "31");
		List<String> listHeaderMothText =  Arrays.asList("23", "26", "27", "30", "31");
		List<String> listHeaderYearText = Arrays.asList( "24", "26", "27", "30", "31");
		// tổng những itemNo ở các header trên
		String[] listItemNoAll = {"22","23","24","26","27","30","31"};
		Map<String, List<LogOutputItemDto>> result = new HashMap<>();
		List<LogOutputItemDto> lstHeader = logOuputItemFinder.getLogOutputItemByItemNosAndRecordType(
				Arrays.asList(listItemNoAll), recordType);
		targetDataCorrect.entrySet().forEach(c ->{
			if (c.getValue().getTargetKey() == TargetDataType.SCHEDULE.value
					|| c.getValue().getTargetKey() == TargetDataType.DAILY_RECORD.value) {
				List<LogOutputItemDto> lstHeader1 = lstHeader.parallelStream()
						.filter(h -> listHeaderDateTimeText.contains(String.valueOf(h.getItemNo())))
						.collect(Collectors.toList());
				result.put(String.valueOf(c.getValue().getTargetKey()), lstHeader1);
			}
			
			if (c.getValue().getTargetKey() == TargetDataType.MONTHLY_RECORD.value
					|| c.getValue().getTargetKey() == TargetDataType.ANY_PERIOD_SUMMARY.value
					|| c.getValue().getTargetKey() == TargetDataType.SALARY_DETAIL.value
					|| c.getValue().getTargetKey() == TargetDataType.BONUS_DETAIL.value) {
				List<LogOutputItemDto> lstHeader1 = lstHeader.parallelStream()
						.filter(h -> listHeaderMothText.contains(String.valueOf(h.getItemNo())))
						.collect(Collectors.toList());
				result.put(String.valueOf(c.getValue().getTargetKey()), lstHeader1);
			}
			
			if (c.getValue().getTargetKey() == TargetDataType.YEAR_END_ADJUSTMENT.value
					|| c.getValue().getTargetKey() == TargetDataType.MONTHLY_CALCULATION.value
					|| c.getValue().getTargetKey() == TargetDataType.RISING_SALARY_BACK.value) {
				List<LogOutputItemDto> lstHeader1 = lstHeader.parallelStream()
						.filter(h -> listHeaderYearText.contains(String.valueOf(h.getItemNo())))
						.collect(Collectors.toList());
				result.put(String.valueOf(c.getValue().getTargetKey()), lstHeader1);
			}
		});		
		return result;
	}
	
	public Map<String, String> getEmployeeCodes(RecordTypeEnum recordTypeEnum,Map<String, LogBasicInformation> mapLogBasicInfo,
			List<LoginRecord> loginRecords, List<PersonInfoCorrectionLog> persionLogs, List<DataCorrectionLog> dataCorectLogs,
			List<StartPageLog> startPageLogs){
		List<String> employeeIds = new ArrayList<>();
		switch (recordTypeEnum) {
		case LOGIN:
			employeeIds.addAll(loginRecords.stream()
					.filter(loginRecord -> mapLogBasicInfo.containsKey(loginRecord.getOperationId()))
					.map(loginRecord -> {
						return mapLogBasicInfo.get(loginRecord.getOperationId()).getUserInfo().getEmployeeId();
					}).collect(Collectors.toList()));
			break;
		case START_UP:
			employeeIds.addAll(startPageLogs.stream().filter(startPageLog -> startPageLog.getBasicInfo() != null)
					.map(startPageLog -> startPageLog.getBasicInfo().getUserInfo().getEmployeeId())
					.collect(Collectors.toList()));
			break;
		case UPDATE_PERSION_INFO:
			persionLogs.stream().forEach(persionlog ->{
				employeeIds.add(persionlog.getTargetUser().getEmployeeId()) ;
				if(mapLogBasicInfo.containsKey(persionlog.getOperationId())){
					employeeIds.add(mapLogBasicInfo.get(persionlog.getOperationId()).getUserInfo().getEmployeeId()) ;
				}
			});
			break;
		case DATA_CORRECT:
			dataCorectLogs.stream().forEach(dataCorrectionLog ->{
				employeeIds.add(dataCorrectionLog.getTargetUser().getEmployeeId()) ;
				if(mapLogBasicInfo.containsKey(dataCorrectionLog.getOperationId())){
					employeeIds.add(mapLogBasicInfo.get(dataCorrectionLog.getOperationId()).getUserInfo().getEmployeeId()) ;
				}
			});
			break;
		default:
			break;
		}
		// Get list employee code by list id employeeIds.stream().distinct().collect(Collectors.toList()) - request list 228
		return personEmpBasicInfoAdapter
				.getEmployeeCodesByEmpIds(employeeIds.stream().distinct().collect(Collectors.toList()));
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	@Getter
	public class TargetDataCorrect{
		private int recordType;
		private int targetKey;
		public TargetDataCorrect(int recordType, int targetKey) {
			this.recordType = recordType;
			this.targetKey = targetKey;
		}
	}
}


