/**
 * 5:11:24 PM Jul 24, 2017
 */
package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KwrmtErAlWorkRecord;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KwrmtErAlWorkRecordPK;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author hungnm
 *
 */
@Stateless
public class JpaErrorAlarmWorkRecordRepository extends JpaRepository implements ErrorAlarmWorkRecordRepository {

	private final String FIND_BY_COMPANY = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId ";
	private final String FIND_BY_ERROR_ALARM_CHECK_ID = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId AND a.eralCheckId = :eralCheckId ";
	private final String FIND_LIST_CODE = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId "
			+ " AND a.kwrmtErAlWorkRecordPK.errorAlarmCode = :errorAlarmCode ";
	private final String FIND_ALL_ER_AL_COMPANY = "SELECT a FROM KwrmtErAlWorkRecord a WHERE a.kwrmtErAlWorkRecordPK.companyId = :companyId "
			+ " AND a.useAtr = 1 AND  a.fixedAtr = 1 AND a.typeAtr IN (0,1)";
	

	@Override
	public Optional<ErrorAlarmWorkRecord> findByCode(String code) {
		Optional<KwrmtErAlWorkRecord> entity = this.queryProxy()
				.find(new KwrmtErAlWorkRecordPK(AppContexts.user().companyId(), code), KwrmtErAlWorkRecord.class);
		return Optional.ofNullable(entity.isPresent() ? KwrmtErAlWorkRecord.toDomain(entity.get()) : null);
	}

	@Override
	public List<ErrorAlarmWorkRecord> getListErrorAlarmWorkRecord(String companyId) {
		List<KwrmtErAlWorkRecord> lstData = this.queryProxy().query(FIND_BY_COMPANY, KwrmtErAlWorkRecord.class)
				.setParameter("companyId", companyId).getList();
		return lstData.stream().map(entity -> KwrmtErAlWorkRecord.toDomain(entity)).collect(Collectors.toList());
	}

	@Override
	public void addErrorAlarmWorkRecord(ErrorAlarmWorkRecord domain, ErrorAlarmCondition conditionDomain) {
		this.commandProxy().insert(KwrmtErAlWorkRecord.fromDomain(domain, conditionDomain));
	}

	@Override
	public void updateErrorAlarmWorkRecord(ErrorAlarmWorkRecord domain, ErrorAlarmCondition conditionDomain) {
		KwrmtErAlWorkRecord targetEntity = this.queryProxy()
				.find(new KwrmtErAlWorkRecordPK(domain.getCompanyId(), domain.getCode().v()), KwrmtErAlWorkRecord.class)
				.get();
		domain.setCheckId(targetEntity.eralCheckId);
		if (!domain.getFixedAtr()) {
			conditionDomain.setGroupId1(targetEntity.krcmtErAlCondition.atdItemConditionGroup1);
			conditionDomain.setGroupId2(targetEntity.krcmtErAlCondition.atdItemConditionGroup2);
		}
		KwrmtErAlWorkRecord domainAfterConvert = KwrmtErAlWorkRecord.fromDomain(domain, conditionDomain);
		targetEntity.eralCheckId = domainAfterConvert.eralCheckId;
		targetEntity.boldAtr = domainAfterConvert.boldAtr;
		targetEntity.cancelableAtr = domainAfterConvert.cancelableAtr;
		targetEntity.cancelRoleId = domainAfterConvert.cancelRoleId;
		targetEntity.errorAlarmName = domainAfterConvert.errorAlarmName;
		targetEntity.errorDisplayItem = domainAfterConvert.errorDisplayItem;
		targetEntity.fixedAtr = domainAfterConvert.fixedAtr;
		targetEntity.krcmtErAlCondition = domainAfterConvert.krcmtErAlCondition;
		targetEntity.krcstErAlApplication = domainAfterConvert.krcstErAlApplication;
		targetEntity.kwrmtErAlWorkRecordPK = domainAfterConvert.kwrmtErAlWorkRecordPK;
		targetEntity.messageColor = domainAfterConvert.messageColor;
		targetEntity.typeAtr = domainAfterConvert.typeAtr;
		targetEntity.useAtr = domainAfterConvert.useAtr;
		this.commandProxy().update(targetEntity);
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

	private final String SELECT_ERAL_BY_LIST_CODE = "SELECT s FROM KwrmtErAlWorkRecord s WHERE s.kwrmtErAlWorkRecordPK.errorAlarmCode IN :listCode";
	@Override
	public List<ErrorAlarmWorkRecord> getListErAlByListCode(String companyId, List<String> listCode) {
		List<ErrorAlarmWorkRecord> datas = new  ArrayList<>();
		CollectionUtil.split(listCode,1000,subIdList->{
			datas.addAll(this.queryProxy().query(SELECT_ERAL_BY_LIST_CODE,KwrmtErAlWorkRecord.class).setParameter("listCode", listCode).getList(c->KwrmtErAlWorkRecord.toDomain(c)));
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

}
