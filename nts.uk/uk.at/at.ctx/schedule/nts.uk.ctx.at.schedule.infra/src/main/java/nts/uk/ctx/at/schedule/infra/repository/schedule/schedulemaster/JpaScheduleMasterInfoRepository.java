package nts.uk.ctx.at.schedule.infra.repository.schedule.schedulemaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfoRepository;
import nts.uk.ctx.at.schedule.infra.entity.schedule.schedulemaster.KscdtScheMasterInfo;
import nts.uk.ctx.at.schedule.infra.entity.schedule.schedulemaster.KscdtScheMasterInfoPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaScheduleMasterInfoRepository extends JpaRepository implements ScheMasterInfoRepository {
	
	private static final String GET_ALL_BY_PERIOD = "SELECT c FROM KscdtScheMasterInfo c "
			+ " WHERE c.kscdtScheMasterInfoPk.sId = :sId"
			+ " AND c.kscdtScheMasterInfoPk.generalDate >= :startDate"
			+ " AND c.kscdtScheMasterInfoPk.generalDate <= :endDate";

	@Override
	public Optional<ScheMasterInfo> getScheMasterInfo(String sId, GeneralDate generalDate) {
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(sId, generalDate);

		return this.queryProxy().find(primaryKey, KscdtScheMasterInfo.class).map(x -> toDomain(x));
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ScheMasterInfo> getScheMasterInfoByPeriod(String sId, DatePeriod period) {
		List<ScheMasterInfo> listScheMasterInfo = new ArrayList<>();
			listScheMasterInfo.addAll(
					this.queryProxy().query(GET_ALL_BY_PERIOD, KscdtScheMasterInfo.class)
						.setParameter("sId", sId)
						.setParameter("startDate", period.start())
						.setParameter("endDate", period.end())
						.getList(x -> toDomain(x)));
		return listScheMasterInfo;
	}

	private static ScheMasterInfo toDomain(KscdtScheMasterInfo entity) {
		if (entity == null) {
			return null;
		}
		
		ScheMasterInfo domain = ScheMasterInfo.createFromJavaType(entity.kscdtScheMasterInfoPk.sId, entity.kscdtScheMasterInfoPk.generalDate,
				entity.employmentCd, entity.classificationCd, entity.businessTypeCd, entity.jobId, entity.workplaceId);
		
		return domain;
	}

	@Override
	public void addScheMasterInfo(ScheMasterInfo scheMasterInfo) {
		this.commandProxy().insert(convertToDbType(scheMasterInfo));
	}

	private KscdtScheMasterInfo convertToDbType(ScheMasterInfo scheMasterInfo) {
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(scheMasterInfo.getSId(), scheMasterInfo.getGeneralDate());
		
		return new KscdtScheMasterInfo(
				primaryKey,
				scheMasterInfo.getEmploymentCd(),
				scheMasterInfo.getClassificationCd(),
				scheMasterInfo.getBusinessTypeCd(),
				scheMasterInfo.getJobId(),
				scheMasterInfo.getWorkplaceId());
	}

	@Override
	public void updateScheMasterInfo(ScheMasterInfo scheMasterInfo) {
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(scheMasterInfo.getSId(), scheMasterInfo.getGeneralDate());		
		KscdtScheMasterInfo kscdtScheMasterInfo = this.queryProxy().find(primaryKey, KscdtScheMasterInfo.class).get();
		
		kscdtScheMasterInfo.employmentCd = scheMasterInfo.getEmploymentCd();
		kscdtScheMasterInfo.classificationCd = scheMasterInfo.getClassificationCd();
		kscdtScheMasterInfo.businessTypeCd = scheMasterInfo.getBusinessTypeCd();
		kscdtScheMasterInfo.jobId = scheMasterInfo.getJobId();
		kscdtScheMasterInfo.workplaceId = scheMasterInfo.getWorkplaceId();
		
		this.commandProxy().update(kscdtScheMasterInfo);
	}

	@Override
	public void deleteScheMasterInfo(String sId, GeneralDate generalDate) {
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(sId, generalDate);		
		this.commandProxy().remove(KscdtScheMasterInfo.class, primaryKey);
	}

}
