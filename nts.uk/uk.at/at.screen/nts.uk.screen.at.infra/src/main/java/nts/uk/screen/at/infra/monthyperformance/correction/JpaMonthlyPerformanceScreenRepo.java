package nts.uk.screen.at.infra.monthyperformance.correction;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.infra.entity.monthly.performance.KrcdtEditStateOfMothlyPer;
import nts.uk.ctx.at.record.infra.entity.monthly.performance.KrcdtEditStateOfMothlyPerPK;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.BsymtAffiWorkplaceHistItem;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.monthlyperformance.correction.MonthlyPerformanceScreenRepo;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ClosureDateDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.EditStateOfMonthlyPerformanceDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyAttendanceItemDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceEmployeeDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaMonthlyPerformanceScreenRepo extends JpaRepository implements MonthlyPerformanceScreenRepo {

	private final static String SEL_EMPLOYEE;
	private final static String SEL_PERSON = "SELECT p FROM BpsmtPerson p WHERE p.bpsmtPersonPk.pId IN :lstPersonId";
	private final static String SEL_WORKPLACE;
	private final static String SEL_BUSINESS_TYPE;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT DISTINCT s FROM BsymtEmployeeDataMngInfo s ");
		// builderString.append("JOIN KmnmtAffiliClassificationHist c ");
		// builderString.append("JOIN KmnmtAffiliEmploymentHist e ");
		// builderString.append("JOIN KmnmtAffiliJobTitleHist j ");
		builderString.append("JOIN BsymtAffiWorkplaceHistItem w ");
		builderString.append("WHERE w.workPlaceId IN :lstWkp ");
		// builderString.append("AND e.kmnmtEmploymentHistPK.emptcd IN :lstEmp
		// ");
		// builderString.append("AND j.kmnmtJobTitleHistPK.jobId IN :lstJob ");
		// builderString.append("AND c.kmnmtClassificationHistPK.clscd IN
		// :lstClas ");
		builderString.append("AND s.bsymtEmployeeDataMngInfoPk.sId = w.sid ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// e.kmnmtEmploymentHistPK.empId ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// j.kmnmtJobTitleHistPK.empId ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// c.kmnmtClassificationHistPK.empId ");
		SEL_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT w FROM BsymtAffiWorkplaceHistItem w JOIN ");
		builderString.append("BsymtAffiWorkplaceHist a ");
		builderString.append("WHERE a.sid = :sId ");
		builderString.append("AND a.strDate <= :baseDate ");
		builderString.append("AND a.endDate >= :baseDate ");
		builderString.append("AND w.sid = a.sid ");
		// builderString.append("AND w.bsymtWorkplaceHist.strD <= :baseDate ");
		// builderString.append("AND w.bsymtWorkplaceHist.endD >= :baseDate");
		SEL_WORKPLACE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT DISTINCT b.businessTypeCode");
		builderString.append(" FROM KrcmtBusinessTypeOfEmployee b");
		builderString.append(" JOIN KrcmtBusinessTypeOfHistory h");
		builderString
				.append(" ON b.krcmtBusinessTypeOfEmployeePK.historyId = h.krcmtBusinessTypeOfHistoryPK.historyId");
		builderString.append(" WHERE b.sId IN :lstSID");
		builderString.append(" AND h.startDate <= :endYmd");
		builderString.append(" AND h.endDate >= :startYmd");
		builderString.append(" ORDER BY b.businessTypeCode ASC");
		SEL_BUSINESS_TYPE = builderString.toString();
	}

	@Override
	public Map<String, String> getListWorkplace(String employeeId, DateRange dateRange) {
		Map<String, String> lstWkp = new HashMap<>();
		List<BsymtAffiWorkplaceHistItem> bsymtAffiWorkplaceHistItem = this.queryProxy()
				.query(SEL_WORKPLACE, BsymtAffiWorkplaceHistItem.class).setParameter("sId", employeeId)
				.setParameter("baseDate", dateRange.getEndDate()).getList();
		List<String> workPlaceIds = bsymtAffiWorkplaceHistItem.stream().map(item -> item.getWorkPlaceId())
				.collect(Collectors.toList());

		String query = "SELECT w FROM BsymtWorkplaceInfo w WHERE w.bsymtWorkplaceInfoPK.wkpid IN :wkpId AND w.bsymtWorkplaceHist.strD <= :baseDate AND w.bsymtWorkplaceHist.endD >= :baseDate";
		CollectionUtil.split(workPlaceIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			this.queryProxy().query(query, BsymtWorkplaceInfo.class)
				.setParameter("wkpId", subList)
				.setParameter("baseDate", dateRange.getEndDate()).getList().stream().forEach(w -> {
					lstWkp.put(w.getBsymtWorkplaceInfoPK().getWkpid(), w.getWkpName());
				});
		});
		return lstWkp;
	}

	@Override
	public List<MonthlyPerformanceEmployeeDto> getListEmployee(List<String> lstJobTitle, List<String> lstEmployment,
			Map<String, String> lstWorkplace, List<String> lstClassification) {
		List<BsymtEmployeeDataMngInfo> lstEmployee = new ArrayList<>();
		CollectionUtil.split(lstWorkplace.keySet().stream().collect(Collectors.toList()), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			lstEmployee.addAll(this.queryProxy().query(SEL_EMPLOYEE, BsymtEmployeeDataMngInfo.class)
								.setParameter("lstWkp", subList).getList());
		});
		List<String> ids = lstEmployee.stream().map((employee) -> {
			return employee.bsymtEmployeeDataMngInfoPk.pId.trim();
		}).collect(Collectors.toList());

		List<BpsmtPerson> lstPerson = new ArrayList<>();
		CollectionUtil.split(ids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			lstPerson.addAll(this.queryProxy().query(SEL_PERSON, BpsmtPerson.class).setParameter("lstPersonId", subList)
					.getList());
		});

		return lstEmployee.stream().map((employee) -> {
			for (BpsmtPerson person : lstPerson) {
				if (person.bpsmtPersonPk.pId.equals(employee.bsymtEmployeeDataMngInfoPk.pId)) {
					return new MonthlyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId,
							employee.employeeCode, person.personName, lstWorkplace.values().stream().findFirst().get(),
							lstWorkplace.keySet().stream().findFirst().get(), "", false);
				}
			}
			return new MonthlyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId, employee.employeeCode, "",
					lstWorkplace.values().stream().findFirst().get(), lstWorkplace.keySet().stream().findFirst().get(),
					"", false);
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> getListBusinessType(List<String> lstEmployee, DateRange dateRange) {
		// return this.queryProxy().query(SEL_BUSINESS_TYPE,
		// String.class).setParameter("lstSID", lstEmployee)
		// .setParameter("startYmd",
		// dateRange.getStartDate()).setParameter("endYmd",
		// dateRange.getEndDate())
		// .getList();
		List<String> result = new ArrayList<>();
		CollectionUtil.split(lstEmployee, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			result.addAll(this.queryProxy().query(SEL_BUSINESS_TYPE, String.class).setParameter("lstSID", subList)
					.setParameter("startYmd", dateRange.getStartDate()).setParameter("endYmd", dateRange.getEndDate())
					.getList());
		});
		return result;
	}

	@Override
	public List<MonthlyAttendanceItemDto> findByAttendanceItemId(String companyId, List<Integer> attendanceItemIds) {
		List<MonthlyAttendanceItemDto> data = new ArrayList<>();
		CollectionUtil.split(attendanceItemIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try (PreparedStatement statement = this.connection().prepareStatement(
						"SELECT * from KRCMT_MON_ATTENDANCE_ITEM h"
						+ " WHERE h.CID = ? AND h.M_ATD_ITEM_ID IN (" + subList.stream().map(s -> "?").collect(Collectors.joining(",")) + ")")) {
				statement.setString(1, companyId);
				for (int i = 0; i < subList.size(); i++) {
					statement.setInt(i + 2, subList.get(i));
				}
				data.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
					return new MonthlyAttendanceItemDto(companyId,
							rec.getInt("M_ATD_ITEM_ID"), rec.getString("M_ATD_ITEM_NAME"), rec.getInt("DISP_NO"),
							rec.getInt("IS_ALLOW_CHANGE"), rec.getInt("M_ATD_ITEM_ATR"), rec.getInt("LINE_BREAK_POS_NAME"), 
							rec.getInt("PRIMITIVE_VALUE"));
				}));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		return data;
	}

	@Override
	public List<EditStateOfMonthlyPerformanceDto> findEditStateOfMonthlyPer(YearMonth processingDate,
			List<String> employeeIds, List<Integer> attendanceItemIds) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcdtEditStateOfMothlyPer b");
		builderString.append(" WHERE b.krcdtEditStateOfMothlyPerPK.employeeID IN :employeeIds");
		builderString.append(" AND b.krcdtEditStateOfMothlyPerPK.attendanceItemID IN :attendanceItemIds");
		builderString.append(" AND b.krcdtEditStateOfMothlyPerPK.processDate = :processingDate");

		List<EditStateOfMonthlyPerformanceDto> list = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			CollectionUtil.split(attendanceItemIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (attdItemIds) -> {
				list.addAll(this.queryProxy().query(builderString.toString(), KrcdtEditStateOfMothlyPer.class)
						.setParameter("employeeIds", subList).setParameter("attendanceItemIds", attdItemIds)
						.setParameter("processingDate", processingDate.v()).getList().stream()
						.map(item -> new EditStateOfMonthlyPerformanceDto(item.krcdtEditStateOfMothlyPerPK.employeeID,
								item.krcdtEditStateOfMothlyPerPK.attendanceItemID,
								new DatePeriod(item.startYmd, item.endYmd),
								item.krcdtEditStateOfMothlyPerPK.processDate,
								item.krcdtEditStateOfMothlyPerPK.closureID,
								new ClosureDateDto(
										item.krcdtEditStateOfMothlyPerPK.closeDay,
										item.krcdtEditStateOfMothlyPerPK.isLastDay),
								item.stateOfEdit))
						.collect(Collectors.toList()));

			});
		});
		return list;
	}

	@Override
	public void insertOrUpdateEditStateOfMonthlyPer(EditStateOfMonthlyPerformanceDto editStateOfMonthlyPerformanceDto) {
		Optional<KrcdtEditStateOfMothlyPer> optKrcdtEditStateOfMothlyPer = this.queryProxy()
				.find(new KrcdtEditStateOfMothlyPerPK(editStateOfMonthlyPerformanceDto.getEmployeeId(),
						editStateOfMonthlyPerformanceDto.getProcessDate(),
						editStateOfMonthlyPerformanceDto.getClosureID(),
						editStateOfMonthlyPerformanceDto.getClosureDate().getCloseDay(),
						editStateOfMonthlyPerformanceDto.getClosureDate().getLastDayOfMonth(),
						editStateOfMonthlyPerformanceDto.getAttendanceItemId()), KrcdtEditStateOfMothlyPer.class);
		KrcdtEditStateOfMothlyPer newkrcdtEditStateOfMothlyPer = this.toEntity(editStateOfMonthlyPerformanceDto);
		if (optKrcdtEditStateOfMothlyPer.isPresent()) {
			KrcdtEditStateOfMothlyPer oldkrcdtEditStateOfMothlyPer = optKrcdtEditStateOfMothlyPer.get();
			oldkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.processDate = newkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.processDate;
			oldkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.closureID = newkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.closureID;
			oldkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.closeDay = newkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.closeDay;
			oldkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.isLastDay = newkrcdtEditStateOfMothlyPer.krcdtEditStateOfMothlyPerPK.isLastDay;
			oldkrcdtEditStateOfMothlyPer.stateOfEdit = newkrcdtEditStateOfMothlyPer.stateOfEdit;
			this.commandProxy().update(oldkrcdtEditStateOfMothlyPer);
		} else {
			this.commandProxy().insert(newkrcdtEditStateOfMothlyPer);
		}
	}

	private KrcdtEditStateOfMothlyPer toEntity(EditStateOfMonthlyPerformanceDto editStateOfMonthlyPerformanceDto) {
		return new KrcdtEditStateOfMothlyPer(
				new KrcdtEditStateOfMothlyPerPK(editStateOfMonthlyPerformanceDto.getEmployeeId(),
						editStateOfMonthlyPerformanceDto.getProcessDate(),
						editStateOfMonthlyPerformanceDto.getClosureID(),
						editStateOfMonthlyPerformanceDto.getClosureDate().getCloseDay(),
						editStateOfMonthlyPerformanceDto.getClosureDate().getLastDayOfMonth(),
						editStateOfMonthlyPerformanceDto.getAttendanceItemId()),
				editStateOfMonthlyPerformanceDto.getStateOfEdit(),
				editStateOfMonthlyPerformanceDto.getDatePeriod().start(),
				editStateOfMonthlyPerformanceDto.getDatePeriod().end());
	}

}
