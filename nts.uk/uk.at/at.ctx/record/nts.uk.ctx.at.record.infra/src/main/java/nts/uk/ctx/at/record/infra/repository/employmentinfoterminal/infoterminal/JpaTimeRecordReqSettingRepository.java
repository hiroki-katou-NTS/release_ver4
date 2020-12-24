package nts.uk.ctx.at.record.infra.repository.employmentinfoterminal.infoterminal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.TimeRecordReqSetting;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.repo.TimeRecordReqSettingRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.infra.entity.employmentinfoterminal.infoterminal.KrcmtTrRequest;
import nts.uk.ctx.at.record.infra.entity.employmentinfoterminal.infoterminal.KrcmtTrRequestPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaTimeRecordReqSettingRepository extends JpaRepository implements TimeRecordReqSettingRepository {

	private static final String GET_BY_KEY;

	private static final String GET_CONTRACTCD_LISTCODE = "SELECT m FROM KrcmtTrRequest m WHERE m.pk.contractCode = :contractCode AND m.pk.timeRecordCode IN :listCode";

	static {

		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a.CONTRACT_CD, a.CID, a.COMPANY_CD, a.TIMERECORDER_CD, a.SEND_OVERTIME_NAME,");
		builderString.append(
				"a.SEND_SID, a.SEND_RESERVATION, a.SEND_WORKTYPE, SEND_WORKTIME, a.REMOTE_SETTING, a.REBOOT, ");
		builderString.append(
				"a.SEND_REASON_APP, a.SEND_SERVERTIME, a.RECV_ALL_STAMP, a.RECV_ALL_RESERVATION, a.RECV_ALL_APPLICATION,");
		builderString.append("b.WORKTYPE_CD, c.WORKTIME_CD, d.RESERVE_FRAME_NO, e.SID as EMPLOYEE");
		builderString.append(" FROM KRCMT_TR_REQUEST a");
		builderString.append(
				" LEFT JOIN KRCMT_TR_SEND_WORKTYPE b ON a.CONTRACT_CD = b.CONTRACT_CD AND a.TIMERECORDER_CD = b.TIMERECORDER_CD ");

		builderString.append(
				" LEFT JOIN KRCMT_TR_SEND_WORKTIME c ON a.CONTRACT_CD = c.CONTRACT_CD AND a.TIMERECORDER_CD = c.TIMERECORDER_CD ");

		builderString.append(
				" LEFT JOIN KRCMT_TR_SEND_RESERVATION d ON a.CONTRACT_CD = d.CONTRACT_CD AND a.TIMERECORDER_CD = d.TIMERECORDER_CD ");

		builderString.append(
				" LEFT JOIN KRCMT_TR_SEND_EMPLOYEE e ON a.CONTRACT_CD = e.CONTRACT_CD AND a.TIMERECORDER_CD = e.TIMERECORDER_CD ");

		builderString.append(" WHERE a.CONTRACT_CD = ? AND a.TIMERECORDER_CD = ?");
		GET_BY_KEY = builderString.toString();

	}

	@Override
	public Optional<TimeRecordReqSetting> getTimeRecordReqSetting(EmpInfoTerminalCode empInfoTerCode,
			ContractCode contractCode) {
		try (PreparedStatement statement = this.connection().prepareStatement(GET_BY_KEY)) {
			statement.setString(1, contractCode.v());
			statement.setString(2, empInfoTerCode.v());
			List<TimeRecordReqSetting> listFullData = createTimeReqSetting(statement.executeQuery());
			if (listFullData.isEmpty())
				return Optional.empty();
			return getOneByList(listFullData);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@SneakyThrows
	private List<TimeRecordReqSetting> createTimeReqSetting(ResultSet rs) {
		List<TimeRecordReqSetting> listFullData = new ArrayList<>();
		while (rs.next()) {
			TimeRecordReqSetting req = new TimeRecordReqSetting.ReqSettingBuilder(
					new EmpInfoTerminalCode(rs.getString("TIMERECORDER_CD")),
					new ContractCode(rs.getString("CONTRACT_CD")), new CompanyId(rs.getString("CID")),
					String.valueOf(rs.getString("COMPANY_CD")),
					rs.getString("EMPLOYEE") == null ? Collections.emptyList()
							: Arrays.asList(new EmployeeId(rs.getString("EMPLOYEE"))),
					rs.getString("RESERVE_FRAME_NO") == null ? Collections.emptyList()
							: Arrays.asList(Integer.parseInt(rs.getString("RESERVE_FRAME_NO"))),
					rs.getString("WORKTYPE_CD") == null ? Collections.emptyList()
							: Arrays.asList(new WorkTypeCode(rs.getString("WORKTYPE_CD"))))
									.workTime(rs.getString("WORKTIME_CD") == null ? Collections.emptyList()
											: Arrays.asList(new WorkTimeCode(rs.getString("WORKTIME_CD"))))
									.overTimeHoliday(rs.getInt("SEND_OVERTIME_NAME") == 1)
									.applicationReason(rs.getInt("SEND_REASON_APP") == 1)
									.stampReceive(rs.getInt("RECV_ALL_STAMP") == 1)
									.reservationReceive(rs.getInt("RECV_ALL_RESERVATION") == 1)
									.applicationReceive(rs.getInt("RECV_ALL_APPLICATION") == 1)
									.timeSetting(rs.getInt("SEND_SERVERTIME") == 1)
									.sendEmployeeId(rs.getInt("SEND_SID") == 1)
									.sendBentoMenu(rs.getInt("SEND_RESERVATION") == 1)
									.sendWorkType(rs.getInt("SEND_WORKTYPE") == 1)
									.sendWorkTime(rs.getInt("SEND_WORKTIME") == 1)
									.remoteSetting(rs.getInt("REMOTE_SETTING") == 1).reboot(rs.getInt("REBOOT") == 1)
									.build();
			listFullData.add(req);
		}

		if (listFullData.isEmpty()) {
			return Collections.emptyList();
		}
		return listFullData;
	}

	private Optional<TimeRecordReqSetting> getOneByList(List<TimeRecordReqSetting> listFullData) {
		if(listFullData.isEmpty()) {
			return Optional.empty();
		}
		List<WorkTimeCode> wTimeCode = listFullData.stream().flatMap(x -> x.getWorkTimeCodes().stream()).distinct()
				.collect(Collectors.toList());
		List<WorkTypeCode> wTypeCode = listFullData.stream().flatMap(x -> x.getWorkTypeCodes().stream()).distinct()
				.collect(Collectors.toList());
		List<EmployeeId> employeeIds = listFullData.stream().flatMap(x -> x.getEmployeeIds().stream()).distinct()
				.collect(Collectors.toList());
		List<Integer> reservation = listFullData.stream().flatMap(x -> x.getBentoMenuFrameNumbers().stream()).distinct()
				.collect(Collectors.toList());

		TimeRecordReqSetting reqTemp = listFullData.get(0);
		return Optional
				.of(new TimeRecordReqSetting.ReqSettingBuilder(reqTemp.getTerminalCode(), reqTemp.getContractCode(),
						reqTemp.getCompanyId(), reqTemp.getCompanyCode(), employeeIds, reservation, wTypeCode)
								.workTime(wTimeCode).overTimeHoliday(reqTemp.isOverTimeHoliday())
								.applicationReason(reqTemp.isApplicationReason()).stampReceive(reqTemp.isStampReceive())
								.reservationReceive(reqTemp.isReservationReceive())
								.applicationReceive(reqTemp.isApplicationReceive()).timeSetting(reqTemp.isTimeSetting())
								.build());
	}

	@Override
	public void updateSetting(TimeRecordReqSetting setting) {
		this.commandProxy().update(toEntity(setting));
	}

	private KrcmtTrRequest toEntity(TimeRecordReqSetting setting) {

		return new KrcmtTrRequest(new KrcmtTrRequestPK(setting.getContractCode().v(), setting.getTerminalCode().v()),
				setting.getCompanyId().v(), setting.getCompanyCode(), setting.isOverTimeHoliday() ? 1 : 0,
				setting.isApplicationReason() ? 1 : 0, setting.isTimeSetting() ? 1 : 0,
				setting.isSendEmployeeId() ? 1 : 0, setting.isSendBentoMenu() ? 1 : 0, setting.isSendWorkType() ? 1 : 0,
				setting.isSendWorkTime() ? 1 : 0, setting.isStampReceive() ? 1 : 0,
				setting.isReservationReceive() ? 1 : 0, setting.isApplicationReceive() ? 1 : 0,
				setting.isRemoteSetting() ? 1 : 0, setting.isReboot() ? 1 : 0);
	}

	@Override
	public List<TimeRecordReqSetting> get(ContractCode contractCode, List<EmpInfoTerminalCode> listCode) {
		List<TimeRecordReqSetting> results = new ArrayList<>();
		List<KrcmtTrRequest> listEntity = this.queryProxy().query(GET_CONTRACTCD_LISTCODE, KrcmtTrRequest.class)
											.setParameter("contractCode", contractCode.v())
											.setParameter("listCode", listCode)
											.getList();
		results = listEntity.stream().map(e -> new TimeRecordReqSetting.ReqSettingBuilder(
												new EmpInfoTerminalCode(e.pk.timeRecordCode),
												new ContractCode(e.pk.contractCode), new CompanyId(e.cid),
												e.companyCode,
												Collections.emptyList(),
												Collections.emptyList(),
												Collections.emptyList())
													.workTime(Collections.emptyList())
													.overTimeHoliday(e.sendOverTime == 1)
													.applicationReason(e.sendReasonApp == 1)
													.stampReceive(e.recvStamp == 1)
													.reservationReceive(e.recvReservation == 1)
													.applicationReceive(e.recbApplication == 1)
													.timeSetting(e.sendServerTime == 1)
													.sendEmployeeId(e.sendSid == 1)
													.sendBentoMenu(e.sendReservation == 1)
													.sendWorkType(e.sendWorkType == 1)
													.sendWorkTime(e.sendWorkTime == 1)
													.remoteSetting(e.remoteSetting == 1).reboot(e.reboot == 1)
													.build())
									.collect(Collectors.toList());
		
		
		return results;
	}
//	CollectionUtil.split(listCode, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
//	results.addAll(getSubList(contractCode, subList));
//});	
//	private List<TimeRecordReqSetting> getSubList(ContractCode contractCode, List<EmpInfoTerminalCode> codes) {
//		String listCodes = codes.stream().map(e -> "?").collect(Collectors.joining(","));
//		GET_CONTRACTCD_LISTCODE += listCodes + ")" ;
//		System.out.println("----- " + JpaTimeRecordReqSettingRepository.GET_CONTRACTCD_LISTCODE);
//		
//		try (PreparedStatement statement = this.connection().prepareStatement(GET_CONTRACTCD_LISTCODE.toString())) {
//			statement.setString(1, contractCode.v());
//			for (int i = 0; i < codes.size(); i++) {
//				statement.setString(i + 2, codes.get(i).v());
//			}
//			return createTimeReqSetting(statement.executeQuery());
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}

	private Optional<TimeRecordReqSetting> getByContractCodeAndCode(ContractCode contractCode, EmpInfoTerminalCode code) {
		try (PreparedStatement statement = this.connection().prepareStatement(GET_BY_KEY)) {
			statement.setString(1, contractCode.v());
			statement.setString(2, code.v());
			List<TimeRecordReqSetting> listFullData = createTimeReqSetting(statement.executeQuery());
			Optional<TimeRecordReqSetting> timeRecordReqSetting = getOneByList(listFullData);
			if (!timeRecordReqSetting.isPresent()) {
				return Optional.empty();
			}
			return timeRecordReqSetting;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<EmployeeId> getEmployeeIdList(ContractCode contractCode, EmpInfoTerminalCode code) {
		Optional<TimeRecordReqSetting> timeRecordReqSetting = getByContractCodeAndCode(contractCode, code);
		if (!timeRecordReqSetting.isPresent()) {
			return Collections.emptyList();
		}
		return timeRecordReqSetting.get().getEmployeeIds();
	}

	@Override
	public List<WorkTypeCode> getWorkTypeCodeList(ContractCode contractCode, EmpInfoTerminalCode code) {
		Optional<TimeRecordReqSetting> timeRecordReqSetting = getByContractCodeAndCode(contractCode, code);
		if (!timeRecordReqSetting.isPresent()) {
			return Collections.emptyList();
		}
		return timeRecordReqSetting.get().getWorkTypeCodes();
	}

	@Override
	public List<WorkTimeCode> getWorkTimeCodeList(ContractCode contractCode, EmpInfoTerminalCode code) {
		Optional<TimeRecordReqSetting> timeRecordReqSetting = getByContractCodeAndCode(contractCode, code);
		if (!timeRecordReqSetting.isPresent()) {
			return Collections.emptyList();
		}
		return timeRecordReqSetting.get().getWorkTimeCodes();
	}

	@Override
	public List<Integer> getbentoMenuFrameNumbers(ContractCode contractCode, EmpInfoTerminalCode code) {
		Optional<TimeRecordReqSetting> timeRecordReqSetting = getByContractCodeAndCode(contractCode, code);
		if (!timeRecordReqSetting.isPresent()) {
			return Collections.emptyList();
		}
		return timeRecordReqSetting.get().getBentoMenuFrameNumbers();
	}
	
	@Override
	public void insert(TimeRecordReqSetting reqSetting) {
		this.commandProxy().insert(toEntity(reqSetting));
	}

}
