/**
 * 5:11:24 PM Jul 24, 2017
 */
package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlConditionsAttendanceItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValueDay;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KwrmtErAlWorkRecord;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KwrmtErAlWorkRecordPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcmtErAlCondition;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlApplication;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * @author hungnm
 *
 */
@Stateless
public class JpaErrorAlarmWorkRecordRepository extends JpaRepository implements ErrorAlarmWorkRecordRepository {

	private static final String FIND_BY_COMPANY = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId ";
	private static final String FIND_BY_ERROR_ALARM_CHECK_ID = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId AND a.eralCheckId = :eralCheckId ";
	private static final String FIND_ALL_ER_AL_COMPANY = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId "
			+ " AND a.useAtr = 1 AND a.typeAtr IN (0,1)";
	private static final String SELECT_ERAL_BY_LIST_CODE_ERROR = "SELECT s FROM KwrmtErAlWorkRecord s WHERE s.kwrmtErAlWorkRecordPK.errorAlarmCode IN :listCode AND s.kwrmtErAlWorkRecordPK.companyId = :companyId AND s.typeAtr = 0";
	private static final String FIND_BY_COMPANY_AND_USEATR = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId AND a.useAtr = :useAtr ";
	private static final String SELECT_ERAL_BY_LIST_CODE = "SELECT s FROM KwrmtErAlWorkRecord s WHERE s.kwrmtErAlWorkRecordPK.errorAlarmCode IN :listCode AND s.kwrmtErAlWorkRecordPK.companyId = :companyId";
	
	private static final String SELECT_ERAL_BY_LIST_CODE_REMARK = "SELECT s FROM KwrmtErAlWorkRecord s WHERE s.kwrmtErAlWorkRecordPK.errorAlarmCode IN :listCode AND s.kwrmtErAlWorkRecordPK.companyId = :companyId AND s.remarkCancelErrorInput = 1";

	@Override
	public Optional<ErrorAlarmWorkRecord> findByCode(String code) {
		Optional<KwrmtErAlWorkRecord> entity = this.queryProxy()
				.find(new KwrmtErAlWorkRecordPK(AppContexts.user().companyId(), code), KwrmtErAlWorkRecord.class);
		return Optional.ofNullable(entity.isPresent() ? KwrmtErAlWorkRecord.toDomain(entity.get()) : null);
	}

	@Override
	public List<ErrorAlarmWorkRecord> getListErrorAlarmWorkRecord(String companyId) {
		List<KwrmtErAlWorkRecord> lstData = this.queryProxy().query(FIND_BY_COMPANY_AND_USEATR, KwrmtErAlWorkRecord.class)
				.setParameter("companyId", companyId)
				.setParameter("useAtr", 1).getList();
		return lstData.stream().map(entity -> KwrmtErAlWorkRecord.toDomain(entity)).collect(Collectors.toList());
	}

	@Override
	public void addErrorAlarmWorkRecord(ErrorAlarmWorkRecord domain, ErrorAlarmCondition conditionDomain) {
		domain.setCompanyId(AppContexts.user().companyId());
		KwrmtErAlWorkRecord entity = KwrmtErAlWorkRecord.fromDomain(domain, conditionDomain);
		entity.krcmtErAlCondition.setCompanyId(AppContexts.user().companyId());
		this.commandProxy().insert(entity);
	}

	@Override
	public void updateErrorAlarmWorkRecord(ErrorAlarmWorkRecord domain, ErrorAlarmCondition conditionDomain) {
		KwrmtErAlWorkRecord targetEntity = this.queryProxy()
				.find(new KwrmtErAlWorkRecordPK(domain.getCompanyId(), domain.getCode().v()), KwrmtErAlWorkRecord.class)
				.get();
		domain.setCheckId(targetEntity.eralCheckId);
		if (!domain.getFixedAtr()) {
			conditionDomain.setGroupId1(targetEntity.getGroup1Id());
			conditionDomain.setGroupId2(targetEntity.getGroup2Id());
		}
		KwrmtErAlWorkRecord domainAfterConvert = KwrmtErAlWorkRecord.fromDomain(domain, conditionDomain);
//		targetEntity.eralCheckId = domainAfterConvert.eralCheckId;
//		targetEntity.boldAtr = domainAfterConvert.boldAtr;
//		targetEntity.cancelableAtr = domainAfterConvert.cancelableAtr;
//		targetEntity.cancelRoleId = domainAfterConvert.cancelRoleId;
//		targetEntity.errorAlarmName = domainAfterConvert.errorAlarmName;
//		targetEntity.errorDisplayItem = domainAfterConvert.errorDisplayItem;
//		targetEntity.fixedAtr = domainAfterConvert.fixedAtr;
//		targetEntity.krcmtErAlCondition = domainAfterConvert.krcmtErAlCondition;
//		targetEntity.krcstErAlApplication = domainAfterConvert.krcstErAlApplication;
//		targetEntity.kwrmtErAlWorkRecordPK = domainAfterConvert.kwrmtErAlWorkRecordPK;
//		targetEntity.messageColor = domainAfterConvert.messageColor;
//		targetEntity.typeAtr = domainAfterConvert.typeAtr;
//		targetEntity.useAtr = domainAfterConvert.useAtr;
//		targetEntity.remarkCancelErrorInput = domainAfterConvert.remarkCancelErrorInput;
//		targetEntity.remarkColumnNo = domainAfterConvert.remarkColumnNo;
		this.commandProxy().update(domainAfterConvert);
	}

	@Override
	public void removeErrorAlarmWorkRecord(String code) {
		this.commandProxy().remove(KwrmtErAlWorkRecord.class,
				new KwrmtErAlWorkRecordPK(AppContexts.user().companyId(), code));
	}

    @Override
    public Optional<ErrorAlarmWorkRecord> findByErrorAlamCheckId(String eralCheckId) {
        Optional<KwrmtErAlWorkRecord> entity = this.queryProxy().query(FIND_BY_ERROR_ALARM_CHECK_ID, KwrmtErAlWorkRecord.class)
                .setParameter("companyId", AppContexts.user().companyId())
                .setParameter("eralCheckId", eralCheckId).getSingle();
                return Optional.ofNullable(entity.isPresent() ? KwrmtErAlWorkRecord.toDomain(entity.get()) : null);
    }

	@Override
	public List<ErrorAlarmWorkRecord> findByListErrorAlamCheckId(List<String> listEralCheckId) {
		List<ErrorAlarmWorkRecord> data = new ArrayList<>();
		for(String eralCheckId : listEralCheckId ) {
			Optional<ErrorAlarmWorkRecord> errorAlarmWorkRecord = this.findByErrorAlamCheckId(eralCheckId);
			if(errorAlarmWorkRecord.isPresent()) {
				data.add(errorAlarmWorkRecord.get());
			}
		}
		return data;
	}
	
	@Override
    public Optional<ErrorAlarmCondition> findConditionByErrorAlamCheckId(String eralCheckId) {
        Optional<KwrmtErAlWorkRecord> entity = this.queryProxy().query(FIND_BY_ERROR_ALARM_CHECK_ID, KwrmtErAlWorkRecord.class)
                .setParameter("companyId", AppContexts.user().companyId())
                .setParameter("eralCheckId", eralCheckId).getSingle();
                return Optional.ofNullable(entity.isPresent() ? KwrmtErAlWorkRecord.toConditionDomain(entity.get()) : null);
    }

	@Override
	public List<ErrorAlarmCondition> findConditionByListErrorAlamCheckId(List<String> listEralCheckId) {
		List<ErrorAlarmCondition> data = new ArrayList<>();
		for(String eralCheckId : listEralCheckId ) {
			Optional<ErrorAlarmCondition> errorAlarmWorkRecord = this.findConditionByErrorAlamCheckId(eralCheckId);
			if(errorAlarmWorkRecord.isPresent()) {
				data.add(errorAlarmWorkRecord.get());
			}
		}
		return data;
	}
	
	@Override
	public List<ErrorAlarmCondition> getListErrorAlarmCondition(String companyId) {
		List<KwrmtErAlWorkRecord> lstData = this.queryProxy().query(FIND_BY_COMPANY, KwrmtErAlWorkRecord.class)
				.setParameter("companyId", companyId).getList();
		return lstData.stream().map(entity -> KwrmtErAlWorkRecord.toConditionDomain(entity)).collect(Collectors.toList());
	}

	@Override
	public List<ErrorAlarmWorkRecord> getListErAlByListCode(String companyId, List<String> listCode) {
		List<ErrorAlarmWorkRecord> datas = new ArrayList<>();
		if (listCode.isEmpty())
			return new ArrayList<ErrorAlarmWorkRecord>();
		
		CollectionUtil.split(listCode, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			datas.addAll(this.queryProxy().query(SELECT_ERAL_BY_LIST_CODE, KwrmtErAlWorkRecord.class)
					.setParameter("listCode", subIdList).setParameter("companyId", companyId)
					.getList(c -> KwrmtErAlWorkRecord.toDomain(c)));
		});
		return datas;
	}

	@Override
	public List<ErrorAlarmWorkRecord> getAllErAlCompany(String companyId) {
		return this.queryProxy().query(FIND_ALL_ER_AL_COMPANY, KwrmtErAlWorkRecord.class)
				.setParameter("companyId", companyId).getList(c -> {
					ErrorAlarmWorkRecord record = KwrmtErAlWorkRecord.toDomain(c);
					record.setErrorAlarmCondition(KwrmtErAlWorkRecord.toConditionDomain(c));
					return record;
				}
				);
	}

	@Override
	public List<ErrorAlarmWorkRecord> getListErAlByListCodeError(String companyId, List<String> listCode) {
		List<ErrorAlarmWorkRecord> datas = new ArrayList<>();
		if (listCode.isEmpty())
			return new ArrayList<ErrorAlarmWorkRecord>();
		
		CollectionUtil.split(listCode, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			datas.addAll(this.queryProxy().query(SELECT_ERAL_BY_LIST_CODE_ERROR, KwrmtErAlWorkRecord.class)
					.setParameter("listCode", subIdList).setParameter("companyId", companyId)
					.getList(c -> KwrmtErAlWorkRecord.toDomain(c)));
		});
		return datas;
	}

	@Override
	@SneakyThrows
	public List<ErrorAlarmWorkRecord> getAllErAlCompanyAndUseAtr(String companyId, boolean useAtr) {		
		//get data to call KwrmtErAlWorkRecord.toDomain(c)
		
		Map<String, String> conG1 = new HashMap<>(), conG2 = new HashMap<>();
		
		Map<String, ErrorAlarmWorkRecord> eRecord = new HashMap<>();
		try (PreparedStatement statement = this.connection().prepareStatement(buildJDBCSelectErAl())) {
			statement.setString(1, companyId);
			statement.setBoolean(2, useAtr);
			new NtsResultSet(statement.executeQuery()).getList(rs -> {
				String eralID = rs.getString("ERAL_CHECK_ID"), group1Id = rs.getString("ATD_ITEM_CONDITION_GROUP1"),
						group2ID = rs.getString("ATD_ITEM_CONDITION_GROUP2"),
						businessTypeCode = rs.getString("BUSINESS_TYPE_CD"), jobId = rs.getString("JOB_ID"),
						employmentCode = rs.getString("EMPTCD"), classificationCode = rs.getString("CLSCD"),
						workTypePlan = rs.getString("wtPlanCD"), workTypeActual = rs.getString("wtActualCD"),
						workTimePlan = rs.getString("whPlanWtCD"), workTimeActual = rs.getString("whActualWtCD");
				
				Integer fixedAtr = rs.getInt("FIXED_ATR"), appTypeCode = rs.getInt("APP_TYPE_CD"); 
				
				if(group1Id != null) {
					conG1.put(group1Id, eralID);
				}
				if(group2ID != null) {
					conG2.put(group2ID, eralID);
				}
				ErrorAlarmWorkRecord eral = eRecord.get(eralID);
				if(eral == null){
					String erAlCode = rs.getString("ERROR_ALARM_CD"), erAlName = rs.getString("ERROR_ALARM_NAME"),
							mesColor = rs.getString("MESSAGE_COLOR"), mesDisplay = rs.getString("MESSAGE_DISPLAY");
					
					Integer remarkCanRemove = rs.getInt("REMARK_CANCEL_ERR_INP"),
							remarkNo = rs.getInt("REMARK_COLUMN_NO"), erAlAtr = rs.getInt("ERAL_ATR"), boldAtr = rs.getInt("BOLD_ATR"),
							cancelableAtr =  rs.getInt("CANCELABLE_ATR"), displayItem = rs.getInt("ERROR_DISPLAY_ITEM"),
							continuousAtr = rs.getInt("CONTINUOUS_PERIOD"), filterByBusinessType = rs.getInt("FILTER_BY_BUSINESS_TYPE"),
							filterByJob = rs.getInt("FILTER_BY_JOB_TITLE"), filterByEmployment = rs.getInt("FILTER_BY_EMPLOYMENT"),
							filterByClassification = rs.getInt("FILTER_BY_CLASSIFICATION"), wtComAtr = rs.getInt("WT_COMPARE_ATR"),
							useGroup2 = rs.getInt("GROUP2_USE_ATR"), whComAtr = rs.getInt("WH_COMPARE_ATR"), useWorkType = rs.getInt("WORKTYPE_USE_ATR"),
							useWorkTypePlan = rs.getInt("WT_PLAN_FILTER_ATR"), useWorkTypeActual = rs.getInt("WT_ACTUAL_FILTER_ATR"),
							workTypeOperator = rs.getInt("WT_PLAN_ACTUAL_OPERATOR"), useWorkTime = rs.getInt("WORKING_HOURS_USE_ATR"),
							useWorkTimePlan = rs.getInt("WH_PLAN_FILTER_ATR"), useWorkTimeActual = rs.getInt("WH_ACTUAL_FILTER_ATR"),
							workTimeOperator = rs.getInt("WH_PLAN_ACTUAL_OPERATOR"), groupOperator = rs.getInt("OPERATOR_BETWEEN_GROUPS");
					
					eral = ErrorAlarmWorkRecord.createFromJavaType(companyId, erAlCode, erAlName, fixedAtr == 1, useAtr, remarkCanRemove, 
										remarkNo, erAlAtr, boldAtr == 1, mesColor, cancelableAtr == 1, displayItem, new ArrayList<>(), eralID);
					
					ErrorAlarmCondition condition = new ErrorAlarmCondition(eralID, mesDisplay);
					condition.setContinuousPeriod(continuousAtr != null ? continuousAtr : 0);
					
					if(fixedAtr != 1){
						// Set AlCheckTargetCondition
						condition.createAlCheckTargetCondition(filterByBusinessType == 1, filterByJob == 1, filterByEmployment == 1, 
																filterByClassification == 1, new ArrayList<>(), 
																new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
						
						// Set WorkTypeCondition
						condition.createWorkTypeCondition(useWorkType == 1, wtComAtr == null ? 0 : wtComAtr);
						condition.setWorkType(useWorkTypePlan != null && useWorkTypePlan == 1, useWorkTypeActual != null && useWorkTypeActual == 1);
						condition.chooseWorkTypeOperator(workTypeOperator);
						
						// Set WorkTimeCondtion
						condition.createWorkTimeCondition(useWorkTime == 1, whComAtr == null ? 0 : whComAtr);
						condition.setWorkTime(useWorkTimePlan != null && useWorkTimePlan == 1, useWorkTimeActual != null && useWorkTimeActual == 1);
						condition.chooseWorkTimeOperator(workTimeOperator);
						
						condition.createAttendanceItemCondition(groupOperator, useGroup2 == 1 ? true : false);

					}					
					eral.setErrorAlarmCondition(condition);
					eRecord.put(eralID, eral);
				} 
				
				if(fixedAtr != 1){
					if(appTypeCode != null){
						eral.getLstApplication().add(appTypeCode);
					}
					
					if(businessTypeCode != null){
						eral.getErrorAlarmCondition().getCheckTargetCondtion().getLstBusinessTypeCode().add(new BusinessTypeCode(businessTypeCode));
					}
					
					if(jobId != null){
						eral.getErrorAlarmCondition().getCheckTargetCondtion().getLstJobTitleId().add(jobId);
					}
					
					if(employmentCode != null){
						eral.getErrorAlarmCondition().getCheckTargetCondtion().getLstEmploymentCode().add(new EmploymentCode(employmentCode));
					}
					if(classificationCode != null){
						eral.getErrorAlarmCondition().getCheckTargetCondtion().getLstClassificationCode().add(new ClassificationCode(classificationCode));
					}

					eral.getErrorAlarmCondition().getWorkTypeCondition().addWorkType(workTypePlan == null ? null : new WorkTypeCode(workTypePlan), 
																					workTypeActual == null ? null : new WorkTypeCode(workTypeActual));
					
					eral.getErrorAlarmCondition().getWorkTimeCondition().addWorkTime(workTimePlan == null ? null : new WorkTimeCode(workTimePlan), 
							workTimeActual == null ? null : new WorkTimeCode(workTimeActual));
				}
				
				return null;
			});
		}
		
		getConditionGroupV2(conG1, conG2, eRecord, companyId);

		return eRecord.entrySet().stream().map(c -> {
			return c.getValue().cleanData();
		}).collect(Collectors.toList());
	}

	private String buildJDBCSelectErAl() {
		StringBuilder builder = new StringBuilder("SELECT es.ERAL_CHECK_ID, es.CID, es.ERROR_ALARM_CD, es.ERROR_ALARM_NAME, es.FIXED_ATR, es.USE_ATR, ");
		builder.append(" es.REMARK_CANCEL_ERR_INP, es.REMARK_COLUMN_NO, es.ERAL_ATR, ");
		builder.append(" es.BOLD_ATR, es.MESSAGE_COLOR, es.CANCELABLE_ATR, es.ERROR_DISPLAY_ITEM, ap.APP_TYPE_CD, ");
		builder.append(" ec.MESSAGE_DISPLAY, ec.CONTINUOUS_PERIOD, ec.FILTER_BY_BUSINESS_TYPE, ");
		builder.append(" ec.FILTER_BY_JOB_TITLE, ec.FILTER_BY_EMPLOYMENT, ec.FILTER_BY_CLASSIFICATION, ec.ATD_ITEM_CONDITION_GROUP1,  ");
		builder.append(" ec.WT_ACTUAL_FILTER_ATR, ec.WT_PLAN_ACTUAL_OPERATOR, ec.WH_PLAN_ACTUAL_OPERATOR, ");
		builder.append(" ec.ATD_ITEM_CONDITION_GROUP2, b.BUSINESS_TYPE_CD, jt.JOB_ID, em.EMPTCD, cl.CLSCD, ");
		builder.append(" ec.WORKTYPE_USE_ATR, ec.WT_COMPARE_ATR, ec.WT_PLAN_FILTER_ATR, ");
		builder.append(" wtp.WORKTYPE_CD as wtPlanCD, wtc.WORKTYPE_CD as wtActualCD, ec.WORKING_HOURS_USE_ATR, ec.WH_COMPARE_ATR, ");
		builder.append(" ec.WH_PLAN_FILTER_ATR, whp.WORK_TIME_CD as whPlanWtCD, ec.WH_ACTUAL_FILTER_ATR, ");
		builder.append(" whc.WORK_TIME_CD as whActualWtCD, ec.OPERATOR_BETWEEN_GROUPS, ec.GROUP2_USE_ATR ");
		builder.append(" FROM KRCMT_ERAL_SET es ");
		builder.append(" LEFT JOIN KRCST_ER_AL_APPLICATION ap ON ap.CID = es.CID AND es.ERROR_ALARM_CD = ap.ERROR_CD");
		builder.append(" LEFT JOIN KRCMT_ERAL_CONDITION ec ON ec.ERAL_CHECK_ID = es.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_BUSINESS_TYPE b ON es.ERAL_CHECK_ID = b.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_JOB_TITLE jt ON es.ERAL_CHECK_ID = jt.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_EMPLOYMENT em  ON es.ERAL_CHECK_ID = em.ERAL_CHECK_ID  ");
		builder.append(" LEFT JOIN KRCST_ER_AL_CLASS cl  ON es.ERAL_CHECK_ID = cl.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_WT_PLAN wtp  ON es.ERAL_CHECK_ID = wtp.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_WT_ACTUAL wtc  ON es.ERAL_CHECK_ID = wtc.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_WH_PLAN whp  ON es.ERAL_CHECK_ID = whp.ERAL_CHECK_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_WH_ACTUAL whc  ON es.ERAL_CHECK_ID = whc.ERAL_CHECK_ID ");
		builder.append(" WHERE es.CID = ? AND es.USE_ATR = ? ");
		return builder.toString();
	}
	
	@SneakyThrows
	@SuppressWarnings("unchecked")
	private <V> void getConditionGroupV2(Map<String, String> group1Id, Map<String, String> group2Id, 
			Map<String, ErrorAlarmWorkRecord> tarCon, String comID) {
		List<String> allId = new ArrayList<>(group1Id.keySet());
		allId.addAll(group2Id.keySet());
		CollectionUtil.split(allId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			try (PreparedStatement statement = this.connection().prepareStatement(buildJDBCSelectConditionGroup(subList))) {
				for (int i = 0; i < subList.size(); i++) {
					statement.setString(1 + i, subList.get(i));
				}
				new NtsResultSet(statement.executeQuery()).getList(rs -> {
					String groupId = rs.getString("CONDITION_GROUP_ID");
					
					boolean isGroup1 = group1Id.get(groupId) != null;
					
					Integer attendanceItemID = rs.getInt("ATTENDANCE_ITEM_ID"), conditionAtr = rs.getInt("CONDITION_ATR"), conditionOperator = rs.getInt("CONDITION_OPERATOR"),
							targetAtr = rs.getInt("TARGET_ATR"), conditionType = rs.getInt("CONDITION_TYPE"), useAtr = rs.getInt("USE_ATR"),
							compareSingleType = rs.getInt("CompareSingleType"), singleId = rs.getInt("erSingID"), atdItemConNo = rs.getInt("ATD_ITEM_CON_NO"),
							compareAtr = rs.getInt("COMPARE_ATR"), compareSingleAtr = rs.getInt("eraComSingleAtr"), inputCheck = rs.getInt("INPUT_CHECK");
					
		        	Double startV = rs.getDouble("START_VALUE"), endV = rs.getDouble("END_VALUE"), fixedV = rs.getDouble("FIXED_VALUE");
		        	V startValue = null, endValue = null, singleValue = null;
		        	
					ErAlConditionsAttendanceItem atc = isGroup1 ? tarCon.get(group1Id.get(groupId)).getErrorAlarmCondition().getAtdItemCondition().getGroup1() 
																: tarCon.get(group2Id.get(groupId)).getErrorAlarmCondition().getAtdItemCondition().getGroup2();
					if(atc == null){
						atc = ErAlConditionsAttendanceItem.init(groupId, conditionOperator);
						if(isGroup1){
							tarCon.get(group1Id.get(groupId)).getErrorAlarmCondition().getAtdItemCondition().setGroup1(atc);
						} else {
							tarCon.get(group2Id.get(groupId)).getErrorAlarmCondition().getAtdItemCondition().setGroup2(atc);
						}
					}
					
					if(attendanceItemID == null){
						return null;
					}
					
					ErAlAttendanceItemCondition<V> atdItemConDomain = new ErAlAttendanceItemCondition<V>(comID, "", atdItemConNo, conditionAtr, useAtr == 1 ? true : false, conditionType);
					atc.getLstErAlAtdItemCon().add(atdItemConDomain);
			        // Set Target
			        if (conditionAtr == ConditionAtr.TIME_WITH_DAY.value && targetAtr == 2) {
			            atdItemConDomain.setUncountableTarget(attendanceItemID);
			        } else if (conditionAtr != ConditionAtr.TIME_WITH_DAY.value) {
			        	if(atdItemConDomain.getCountableTarget() == null){
			        		atdItemConDomain.setCountableTarget(new ArrayList<>(), new ArrayList<>());
			        	}
			        	if(targetAtr == 0){
			        		atdItemConDomain.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems().add(attendanceItemID);
			        	} else if(targetAtr == 1){
			        		atdItemConDomain.getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems().add(attendanceItemID);
			        	}
			        }
			        // Set Compare
			        if (compareAtr != null) {
			            if (conditionAtr == ConditionAtr.AMOUNT_VALUE.value) {
			            	startValue = (V) new CheckedAmountValue(startV.intValue());
			            	endValue = (V) new CheckedAmountValue(endV.intValue());
			            } else if (conditionAtr == ConditionAtr.TIME_DURATION.value) {
			            	startValue = (V) new CheckedTimeDuration(startV.intValue());
			            	endValue = (V) new CheckedTimeDuration(endV.intValue());
			            } else if (conditionAtr == ConditionAtr.TIME_WITH_DAY.value) {
			            	startValue = (V) new TimeWithDayAttr(startV.intValue());
			            	endValue = (V) new TimeWithDayAttr(endV.intValue());
			            } else if (conditionAtr == ConditionAtr.TIMES.value) {
			            	startValue = (V) new CheckedTimesValue(startV.intValue());
			            	endValue = (V) new CheckedTimesValue(endV.intValue());
			            } else if (conditionAtr == ConditionAtr.DAYS.value) {
			            	startValue = (V) new CheckedTimesValueDay(startV);
			            	endValue = (V) new CheckedTimesValueDay(endV);
			            }
		            	atdItemConDomain.setCompareRange(compareAtr, startValue, endValue);
			        } else if (compareSingleAtr != null) {
			            if (compareSingleType == ConditionType.FIXED_VALUE.value) {
			                if (conditionAtr == ConditionAtr.AMOUNT_VALUE.value) {
			                    singleValue = (V) new CheckedAmountValue(fixedV.intValue());
			                } else if (conditionAtr == ConditionAtr.TIME_DURATION.value) {
			                    singleValue = (V) new CheckedTimeDuration(fixedV.intValue());
			                } else if (conditionAtr == ConditionAtr.TIME_WITH_DAY.value) {
			                    singleValue = (V) new TimeWithDayAttr(fixedV.intValue());
			                } else if (conditionAtr == ConditionAtr.TIMES.value) {
			                    singleValue = (V) new CheckedTimesValue(fixedV.intValue());
			                } else if (conditionAtr == ConditionAtr.DAYS.value) {
			                	singleValue = (V) new CheckedTimesValueDay(fixedV);
			                }
			            } else {
			                singleValue = (V) new AttendanceItemId(singleId);
			            }
			            atdItemConDomain.setCompareSingleValue(compareSingleAtr, compareSingleType, singleValue);
			        } else if (inputCheck != null) {
			        	atdItemConDomain.setInputCheck(inputCheck);
			        }
					
					return null;
				});
				
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	private String buildJDBCSelectConditionGroup(List<String> subList) {
		StringBuilder builder = new StringBuilder("SELECT cg.CONDITION_GROUP_ID, cg.CONDITION_OPERATOR, ic.CONDITION_TYPE, ic.ATD_ITEM_CON_NO, ic.CONDITION_ATR, ic.USE_ATR, ");
		builder.append(" adt.TARGET_ATR, adt.ATTENDANCE_ITEM_ID, cr.CONDITION_GROUP_ID as compareRanID, cr.COMPARE_ATR, cr.START_VALUE, cr.END_VALUE, ");
		builder.append(" cs.COMPARE_ATR as eraComSingleAtr, cs.CONDITION_TYPE as CompareSingleType, sf.FIXED_VALUE, sa.ATTENDANCE_ITEM_ID as erSingID, inc.INPUT_CHECK ");
		builder.append(" FROM KRCST_ER_AL_CON_GROUP cg LEFT JOIN KRCMT_ER_AL_ATD_ITEM_CON ic ON cg.CONDITION_GROUP_ID = ic.CONDITION_GROUP_ID ");
		builder.append(" LEFT JOIN KRCST_ER_AL_ATD_TARGET adt ON ic.CONDITION_GROUP_ID = adt.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = adt.ATD_ITEM_CON_NO ");
		builder.append(" LEFT JOIN KRCST_ERAL_COMPARE_RANGE cr ON ic.CONDITION_GROUP_ID = cr.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = cr.ATD_ITEM_CON_NO");
		builder.append(" LEFT JOIN KRCST_ERAL_COMPARE_SINGLE cs ON ic.CONDITION_GROUP_ID = cs.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = cs.ATD_ITEM_CON_NO");
		builder.append(" LEFT JOIN KRCST_ERAL_SINGLE_FIXED sf  ON ic.CONDITION_GROUP_ID = sf.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = sf.ATD_ITEM_CON_NO ");
		builder.append(" LEFT JOIN KRCST_ERAL_SINGLE_ATD sa ON ic.CONDITION_GROUP_ID = sa.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = sa.ATD_ITEM_CON_NO");
		builder.append(" LEFT JOIN KRCST_ERAL_INPUT_CHECK inc ON ic.CONDITION_GROUP_ID = inc.CONDITION_GROUP_ID AND ic.ATD_ITEM_CON_NO = inc.ATD_ITEM_CON_NO ");
		builder.append(" WHERE cg.CONDITION_GROUP_ID IN ( " + subList.stream().map(s -> "?").collect(Collectors.joining(",")) + ")");
		return builder.toString();
	}
	
	@Override
	public List<ErrorAlarmWorkRecord> getAllErAlCompanyAndUseAtrV2(String companyId, boolean useAtr) {
		StringBuilder builder = new StringBuilder("SELECT a, eac, eaa FROM KwrmtErAlWorkRecord a");
		builder.append(" LEFT JOIN a.krcmtErAlCondition eac ");
		builder.append(" LEFT JOIN a.krcstErAlApplication eaa ");
		builder.append(" WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId AND a.useAtr = :useAtr ");
		return this.queryProxy().query(builder.toString(), Object[].class)
				.setParameter("companyId", companyId)
				.setParameter("useAtr", useAtr ? 1 : 0).getList()
				.stream().collect(Collectors.groupingBy(c -> c[0], Collectors.toList()))
				.entrySet().stream().map(e -> {
					KwrmtErAlWorkRecord eralRecord = (KwrmtErAlWorkRecord) e.getKey();
					List<KrcstErAlApplication> eralApp = e.getValue().stream().filter(al -> al[2] != null)
							.map(al -> (KrcstErAlApplication) al[2]).collect(Collectors.toList());
					KrcmtErAlCondition eralCon = e.getValue().stream().filter(al -> al[1] != null).findFirst().map(al -> (KrcmtErAlCondition) al[1]).orElse(null);
					ErrorAlarmWorkRecord record = KwrmtErAlWorkRecord.toDomain(eralRecord, eralApp);
					record.setErrorAlarmCondition(KwrmtErAlWorkRecord.toConditionDomain(eralRecord, eralCon));
					return record;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Map<String, Object>> getErAlByComID(String companyId) {
		StringBuilder builder = new StringBuilder("SELECT a.errorDisplayItem, a.kwrmtErAlWorkRecordPK.errorAlarmCode, a.krcmtErAlCondition");
		builder.append(" FROM KwrmtErAlWorkRecord a");
		builder.append(" WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId ");
		builder.append(" AND a.useAtr = 1 AND a.typeAtr IN (0,1)");
		builder.append(" AND a.errorDisplayItem IS NOT NULL");
		builder.append(" AND a.krcmtErAlCondition IS NOT NULL");
		return this.queryProxy().query(builder.toString(), Object[].class)
				.setParameter("companyId", companyId).getList(c -> {
					ErrorAlarmCondition codition = KrcmtErAlCondition.toDomain((KrcmtErAlCondition) c[2], companyId, c[1].toString());
					Map<String, Object> mapped = new HashMap<>();
					mapped.put("Code", c[1].toString());
					mapped.put("ErrorDisplayItem", (int) c[0]);
					mapped.put("ErrorAlarmCondition", codition);
					return mapped;
				});
	}
	
	@Override
	public List<ErrorAlarmWorkRecord> getListErrorAlarmWorkRecord(String companyId, int fixed) {
		List<KwrmtErAlWorkRecord> lstData = this.queryProxy()
				.query(FIND_BY_COMPANY + " AND a.fixedAtr = :fixedAtr ", KwrmtErAlWorkRecord.class)
				.setParameter("companyId", companyId).setParameter("fixedAtr", fixed).getList();
		return lstData.stream().map(entity -> {
			ErrorAlarmWorkRecord record = KwrmtErAlWorkRecord.toDomain(entity);
			record.setErrorAlarmCondition(KwrmtErAlWorkRecord.toConditionDomain(entity));
			return record;
		}).collect(Collectors.toList());
	}

	@Override
	public List<ErrorAlarmWorkRecord> getListErAlByListCodeRemark(String companyId, Collection<String> listCode) {
		// TODO Auto-generated method stub
		List<ErrorAlarmWorkRecord> datas = new ArrayList<>();
		if (listCode.isEmpty())
			return new ArrayList<ErrorAlarmWorkRecord>();
		
		CollectionUtil.split(listCode.stream().collect(Collectors.toList()), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			datas.addAll(this.queryProxy().query(SELECT_ERAL_BY_LIST_CODE_REMARK, KwrmtErAlWorkRecord.class)
					.setParameter("listCode", subIdList).setParameter("companyId", companyId)
					.getList(c -> KwrmtErAlWorkRecord.toDomain(c)));
		});
		return datas;
	}

}
