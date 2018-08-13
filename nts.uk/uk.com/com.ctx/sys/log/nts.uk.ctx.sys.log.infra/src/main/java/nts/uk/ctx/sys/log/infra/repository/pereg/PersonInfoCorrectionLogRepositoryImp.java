package nts.uk.ctx.sys.log.infra.repository.pereg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.sys.log.dom.pereg.IPersonInfoCorrectionLogRepository;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtCtgCorrectionLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtDataHistoryLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtItemInfoLog;
import nts.uk.ctx.sys.log.infra.entity.pereg.SrcdtPerCorrectionLog;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataKey;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.CategoryCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.InfoOperateAttr;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ItemInfo;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ItemInfo.RawValue;
import nts.uk.shr.com.security.audittrail.correction.content.pereg.ItemInfo.Value;
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
			"AND (:empIdNULL = 'ISNULL' OR pcl.employeeID IN :employeeIDs)",
			"AND pcl.insDate >= :startDate AND pcl.insDate <= :endDate");

	@Override
	public List<PersonInfoCorrectionLog> findByTargetAndDate(String operationId, List<String> listEmployeeId,
			DatePeriod period) {
		GeneralDateTime start = GeneralDateTime.ymdhms(period.start().year(), period.start().month(),
				period.start().day(), 0, 0, 0);
		GeneralDateTime end = GeneralDateTime.ymdhms(period.end().year(), period.end().month(), period.end().day(), 23,
				59, 59);
		
		List<PersonalInfoCorrectionLogQuery> query = queryProxy().query(SELECT_ALL, Object[].class)
				.setParameter("operationID", operationId)
				.setParameter("empIdNULL", listEmployeeId == null || listEmployeeId.size() == 0 ? "ISNULL" : "ISNOTNULL")
				.setParameter("employeeIDs",
						listEmployeeId == null || listEmployeeId.size() == 0 ? new ArrayList<String>() {
							private static final long serialVersionUID = 1L;
							{
								add("");
							}
						} : listEmployeeId)
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
						
						if (ctgFilter.size() == 0) {
							return null;
						}

						// get first cat and first dataHistLog
						PersonalInfoCorrectionLogQuery perICLQuery = ctgFilter.get(0);

						SrcdtDataHistoryLog dhLog = perICLQuery.getSrcdtDataHistoryLog();
						SrcdtCtgCorrectionLog ctgcLog = perICLQuery.getSrcdtCtgCorrectionLog();

						// get list itemInfos
						List<ItemInfo> itemInfos = ctgFilter.stream().map(ii -> ii.getSrcdtItemInfoLog()).map(ii -> {
							// filter type of raw value
							RawValue rvb = null, rva = null;
							/*
							 * STRING(1), INTEGER(2), DOUBLE(3), DECIMAL(4), DATE(5),
							 */
							switch (ii.dataValueAttr) {
							case 1:
								rvb = RawValue.asString(ii.valueBefore);
								rva = RawValue.asString(ii.valueAfter);
								break;
							case 2:
								rvb = RawValue.asInteger(Integer.parseInt(ii.valueBefore));
								rva = RawValue.asInteger(Integer.parseInt(ii.valueAfter));
								break;
							case 3:
								rvb = RawValue.asDouble(Double.parseDouble(ii.valueBefore));
								rva = RawValue.asDouble(Double.parseDouble(ii.valueAfter));
								break;
							case 4:
								rvb = RawValue.asDecimal(BigDecimal.valueOf(Double.parseDouble(ii.valueBefore)));
								rva = RawValue.asDecimal(BigDecimal.valueOf(Double.parseDouble(ii.valueAfter)));
								break;
							case 5:
								rvb = RawValue.asDate(GeneralDate.fromString(ii.valueBefore, "yyyy/MM/dd"));
								rva = RawValue.asDate(GeneralDate.fromString(ii.valueAfter, "yyyy/MM/dd"));
								break;
							}
							return new ItemInfo(ii.itemInfoLogID, ii.itemID, ii.itemName, new Value(rvb, ii.contentBefore),
									new Value(rva, ii.contentAfter));
						}).collect(Collectors.toList());

						// create reviseInfo from dataHistLog
						Optional<ReviseInfo> reviseInfo = Optional.ofNullable(dhLog).map(r -> {
							return new ReviseInfo(r.reviseItemName, Optional.ofNullable(r.reviseYMD),
									Optional.ofNullable(new YearMonth(r.reviseYM)), Optional.ofNullable(r.reviseY));
						});

						return new CategoryCorrectionLog("UPDATE_ID", ctgcLog.categoryName,
								EnumAdaptor.valueOf(ctgcLog.infoOperateAttr, InfoOperateAttr.class),
								dhLog.targetKeyYMD != null ? TargetDataKey.of(dhLog.targetKeyYMD, dhLog.stringKey)
										: dhLog.targetKeyYM != null
												? TargetDataKey.of(
														YearMonth.of(dhLog.targetKeyYM),
														dhLog.stringKey)
												: dhLog.targetKeyY != null
														? TargetDataKey.of(dhLog.targetKeyY, dhLog.stringKey)
														: TargetDataKey.of(dhLog.stringKey),
								itemInfos, reviseInfo);
					}).filter(f -> f != null).collect(Collectors.toList());

			return new PersonInfoCorrectionLog(perCorrectionLog.operationID,
					EnumAdaptor.valueOf(perCorrectionLog.processingAttr, PersonInfoProcessAttr.class),
					UserInfo.employee(perCorrectionLog.userID, perCorrectionLog.employeeID, perCorrectionLog.userName),
					ctgs, perCorrectionLog.remark);
		}).filter(f -> f != null && f.getCategoryCorrections() != null).collect(Collectors.toList());
	}

	@Override
	public void save(List<PersonInfoCorrectionLog> correctionLogs) {
		correctionLogs.forEach(cor -> {
			SrcdtPerCorrectionLog pcl = toPCLEntity(cor);

			cor.getCategoryCorrections().forEach(ccl -> {
				SrcdtCtgCorrectionLog cce = toCCLEntity(ccl, pcl.perCorrectionLogID);
				SrcdtDataHistoryLog dhl = toDHLEntity(ccl, cce.ctgCorrectionLogID);

				ccl.getItemInfos().forEach(ii -> {
					SrcdtItemInfoLog iil = toIILEntity(ii, cce.ctgCorrectionLogID);

					commandProxy().insert(iil);
				});

				commandProxy().insert(cce);
				commandProxy().insert(dhl);
			});

			commandProxy().insert(pcl);
		});
	}

	private SrcdtPerCorrectionLog toPCLEntity(PersonInfoCorrectionLog domain) {
		SrcdtPerCorrectionLog pcl = new SrcdtPerCorrectionLog();

		// fix or random value
		pcl.companyId = AppContexts.user().companyId();
		pcl.perCorrectionLogID = IdentifierUtil.randomUniqueId();

		pcl.operationID = domain.getOperationId();
		pcl.processingAttr = domain.getProcessAttr().value;

		UserInfo uif = domain.getTargetUser();

		if (uif != null) {
			pcl.userID = uif.getUserId();
			pcl.userName = uif.getUserName();
			pcl.employeeID = uif.getEmployeeId();
		} else {
			// throw error ?
		}

		pcl.remark = domain.getRemark();

		return pcl;
	}

	private SrcdtDataHistoryLog toDHLEntity(CategoryCorrectionLog domain, String ctgCorrectionLogID) {
		SrcdtDataHistoryLog dhl = new SrcdtDataHistoryLog();
		dhl.companyId = AppContexts.user().companyId();
		dhl.ctgCorrectionLogID = ctgCorrectionLogID;
		dhl.dataHistoryLogID = IdentifierUtil.randomUniqueId();

		domain.getReviseInfo().ifPresent(ri -> {
			dhl.reviseItemName = ri.getItemName();

			ri.getYear().ifPresent(y -> {
				dhl.reviseY = y.intValue();
			});

			ri.getYearMonth().ifPresent(ym -> {
				dhl.reviseYM = ym.v().intValue();
			});

			ri.getDate().ifPresent(ymd -> {
				dhl.reviseYMD = ymd;
			});
		});

		TargetDataKey tdKey = domain.getTargetKey();

		tdKey.getStringKey().ifPresent(k -> {
			dhl.stringKey = k;
		});

		tdKey.getDateKey().ifPresent(d -> {
			switch (tdKey.getCalendarKeyType()) {
			case DATE:
				dhl.targetKeyYMD = d;
				break;
			case YEARMONTH:
				dhl.targetKeyYM = d.yearMonth().v().intValue();
				break;
			case YEAR:
				dhl.targetKeyY = d.year();
				break;
			case NONE:
				break;
			}
		});

		return dhl;
	}

	private SrcdtCtgCorrectionLog toCCLEntity(CategoryCorrectionLog domain, String perCorrectionLogID) {
		SrcdtCtgCorrectionLog ccl = new SrcdtCtgCorrectionLog();

		ccl.companyId = AppContexts.user().companyId();
		ccl.ctgCorrectionLogID = IdentifierUtil.randomUniqueId();

		ccl.perCorrectionLogID = perCorrectionLogID;

		ccl.categoryID = domain.getCategoryId();
		ccl.categoryName = domain.getCategoryName();
		ccl.infoOperateAttr = domain.getInfoOperateAttr().value;

		return ccl;
	}
	
	private SrcdtItemInfoLog toIILEntity(ItemInfo domain, String ctgCorrectionLogID) {
		SrcdtItemInfoLog iil = new SrcdtItemInfoLog();
		iil.companyId = AppContexts.user().companyId();
		iil.itemInfoLogID = IdentifierUtil.randomUniqueId();

		iil.itemID = domain.getItemId();
		iil.ctgCorrectionLogID = ctgCorrectionLogID;
		iil.itemName = domain.getName();

		Value vb = domain.getValueBefore(), va = domain.getValueAfter();

		/*
		 * STRING(1), INTEGER(2), DOUBLE(3), DECIMAL(4), DATE(5),
		 */
		switch (vb.getRawValue().getType()) {
		case STRING:
			iil.dataValueAttr = 1;
			break;
		case INTEGER:
			iil.dataValueAttr = 2;
			break;
		case DOUBLE:
			iil.dataValueAttr = 3;
			break;
		case DECIMAL:
			iil.dataValueAttr = 4;
			break;
		case DATE:
			iil.dataValueAttr = 5;
			break;
		}

		iil.contentBefore = vb.getViewValue();
		iil.contentAfter = va.getViewValue();

		iil.valueBefore = vb.getRawValue().toString();
		iil.valueAfter = va.getRawValue().toString();

		return iil;
	}
}
