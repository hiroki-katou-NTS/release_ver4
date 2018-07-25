package nts.uk.ctx.sys.log.infra.repository.pereg;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.sys.log.dom.pereg.IPersonInfoCorrectionLogRepository;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtCtgCorrectionLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtDataHistoryLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtItemInfoLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtPerCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.ItemInfo;
import nts.uk.shr.com.security.audittrail.correction.content.ItemInfo.RawValue;
import nts.uk.shr.com.security.audittrail.correction.content.ItemInfo.Value;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey.CalendarKeyType;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.CategoryCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.InfoOperateAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.PersonInfoProcessAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ReviseInfo;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class PersonInfoCorrectionLogRepositoryImp extends JpaRepository implements IPersonInfoCorrectionLogRepository {

	private static final String SELECT_ALL = String.join(" ",
			"SELECT pcl, ccl, dhl, iil",
			"FROM SrcdtPerCorrectionLog pcl",
			"INNER JOIN SrcdtCtgCorrectionLog ccl",
			"ON pcl.perCorrectionLogID = ccl.perCorrectionLogID",
			"INNER JOIN SrcdtDataHistoryLog dhl",
			"ON ccl.ctgCorrectionLogID = dhl.ctgCorrectionLogID",
			"INNER JOIN SrcdtItemInfoLog iil",
			"ON ccl.ctgCorrectionLogID = iil.ctgCorrectionLogID",
			"WHERE pcl.operationID = :operationID",
			"AND (pcl.employeeID IN :employeeIDs OR :employeeIDs IS NULL)",
			"AND pcl.insDate >= :startDate AND pcl.insDate <= :endDate");

	@Override
	public Optional<PersonInfoCorrectionLog> getPeregLog(String operationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PersonInfoCorrectionLog> findByTargetAndDate(String operationId, List<String> listEmployeeId,
			DatePeriod period) {
		GeneralDateTime start = GeneralDateTime.ymdhms(period.start().year(), period.start().month(),
				period.start().day(), 0, 0, 0);
		GeneralDateTime end = GeneralDateTime.ymdhms(period.end().year(), period.end().month(), period.end().day(), 23,
				59, 59);
		
		List<PersonalInfoCorrectionLogQuery> query = queryProxy().query(SELECT_ALL, Object[].class)
				.setParameter("operationID", operationId)
				.setParameter("employeeIDs", listEmployeeId == null || listEmployeeId.size() == 0 ? null : listEmployeeId)
				.setParameter("startDate", start)
				.setParameter("endDate", end)
				.getList().stream()
				.map(f -> {
					SrcdtPerCorrectionLog perCorrectionLog = (SrcdtPerCorrectionLog) f[0];
					SrcdtCtgCorrectionLog ctgCorrectionLog = (SrcdtCtgCorrectionLog) f[1];
					SrcdtDataHistoryLog dataHistoryLog = (SrcdtDataHistoryLog) f[2];
					SrcdtItemInfoLog itemInfoLog = (SrcdtItemInfoLog) f[3];

					return new PersonalInfoCorrectionLogQuery(perCorrectionLog.getPerCorrectionLogID(),
							perCorrectionLog, ctgCorrectionLog, dataHistoryLog, itemInfoLog);
				}).collect(Collectors.toList());

		return query.stream().map(m -> m.getPerCorrectionLogID()).distinct().map(m -> {
			List<PersonalInfoCorrectionLogQuery> filter = query.stream()
					.filter(f -> f.getPerCorrectionLogID().equals(m)).collect(Collectors.toList());

			if (filter.size() == 0) {
				return null;
			}

			SrcdtPerCorrectionLog perCorrectionLog = filter.get(0).getSrcdtPerCorrectionLog();

			List<CategoryCorrectionLog> ctgs = filter.stream()
					.map(lc -> lc.getSrcdtCtgCorrectionLog().ctgCorrectionLogID).distinct().map(lc -> {
						List<PersonalInfoCorrectionLogQuery> ctgFilter = filter.stream()
								.filter(f -> f.getSrcdtCtgCorrectionLog().ctgCorrectionLogID.equals(lc))
								.collect(Collectors.toList());
						
						if(ctgFilter.size() == 0) {
							return null;
						}

						// get first cat and first dataHistLog
						PersonalInfoCorrectionLogQuery perICLQuery = ctgFilter.get(0);

						SrcdtDataHistoryLog dhLog = perICLQuery.getSrcdtDataHistoryLog();
						SrcdtCtgCorrectionLog ctgcLog = perICLQuery.getSrcdtCtgCorrectionLog();

						// get list itemInfos
						List<ItemInfo> itemInfos = ctgFilter.stream().map(ii -> ii.getSrcdtItemInfoLog()).map(ii -> {
							// filter type of raw value
							return new ItemInfo(ii.itemInfoLogID, ii.itemName,
									new Value(RawValue.asString(""), ii.contentBefore),
									new Value(RawValue.asString(""), ii.contentAfter));
						}).collect(Collectors.toList());

						// create reviseInfo from dataHistLog
						Optional<ReviseInfo> reviseInfo = Optional.ofNullable(dhLog).map(r -> {
							return new ReviseInfo(r.reviseItemName, Optional.ofNullable(r.reviseYMD),
									Optional.ofNullable(new YearMonth(r.reviseYM)), Optional.ofNullable(r.reviseY));
						});

						return new CategoryCorrectionLog(ctgcLog.categoryName,
								EnumAdaptor.valueOf(ctgcLog.infoOperateAttr, InfoOperateAttr.class),
								dhLog.targetKeyYMD != null ? TargetDataKey.of(dhLog.targetKeyYMD, dhLog.stringKey)
										: dhLog.targetKeyYM != null
												? TargetDataKey.of(
														GeneralDate.fromString(dhLog.targetKeyYM.toString(), "yyyyMM"),
														dhLog.stringKey)
												: dhLog.targetKeyY != null
														? TargetDataKey.of(GeneralDate.fromString(
																dhLog.targetKeyYM.toString(), "yyyy"), dhLog.stringKey)
														: TargetDataKey.of(dhLog.stringKey),
								itemInfos, reviseInfo);
					}).filter(f -> f != null).collect(Collectors.toList());

			return new PersonInfoCorrectionLog(m,
					EnumAdaptor.valueOf(perCorrectionLog.processingAttr, PersonInfoProcessAttr.class),
					UserInfo.employee(perCorrectionLog.userID, perCorrectionLog.employeeID, perCorrectionLog.userName),
					ctgs, perCorrectionLog.remark);
		}).filter(f -> f != null && f.getCategoryCorrections() != null).collect(Collectors.toList());
	}
}
