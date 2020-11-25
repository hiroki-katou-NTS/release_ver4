package nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import nts.arc.error.BusinessException;
import nts.arc.task.tran.AtomTask;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerSerialNo;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal.EmpInfoTerminalBuilder;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalName;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.FullIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.IPAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MacAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ModelEmpInfoTer;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MonitorIntervalTime;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.PartialIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.TimeRecordReqSetting;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.TimeRecordReqSetting.ReqSettingBuilder;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.ReservationReceptionData;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationCount;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bento.WorkLocationCode;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampTypeDisplay;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * @author ThanhNX
 *
 *         データタイムレコードを予約に変換するTest
 */
@RunWith(JMockit.class)
public class ConvertTimeRecordReservationServiceTest {

	private static EmpInfoTerminalCode empInfoTerCode;

	private static ContractCode contractCode;

	@Injectable
	private ConvertTimeRecordReservationService.Require require;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		empInfoTerCode = new EmpInfoTerminalCode(1);
		contractCode = new ContractCode("1");
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEmpInfoTerNoPresent() {
		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");
		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		assertThat(resultActual).isEqualTo(Optional.empty());

	}

	@Test
	public void testEmpInfoTerNoPresentSetting() {
		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		assertThat(resultActual).isEqualTo(Optional.empty());

	}

	@Test
	public void testExistHistory() {
		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		Optional<TimeRecordReqSetting> timeRecordReqSetting = Optional
				.of(new ReqSettingBuilder(empInfoTerCode, contractCode, null, null, null, null, null).build());

		Optional<StampRecord> stampRecord = 
				Optional.of(new StampRecord(new ContractCode("1"), new StampNumber("1"), GeneralDateTime.now(),
				new StampTypeDisplay("1"), Optional.empty()));

		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

				require.getTimeRecordReqSetting((EmpInfoTerminalCode) any, (ContractCode) any);
				result = timeRecordReqSetting;

				require.getStampRecord(contractCode, (StampNumber) any, (GeneralDateTime) any);
				result = stampRecord;

			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		assertThat(resultActual).isEqualTo(Optional.empty());

	}

	@Test
	public void testRegistDoneData() {
		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		Optional<TimeRecordReqSetting> timeRecordReqSetting = Optional
				.of(new ReqSettingBuilder(empInfoTerCode, contractCode, null, null, null, null, null).build());

		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

				require.getTimeRecordReqSetting((EmpInfoTerminalCode) any, (ContractCode) any);
				result = timeRecordReqSetting;

				require.getStampRecord(contractCode, (StampNumber) any, (GeneralDateTime) any);
				result = Optional.empty();

			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		NtsAssert.atomTask(() -> resultActual.get(), any -> require.insert(any.get()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNoFindStampCard(@Mocked BentoMenu menu) {
		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		Optional<TimeRecordReqSetting> timeRecordReqSetting = Optional
				.of(new ReqSettingBuilder(empInfoTerCode, contractCode, null, null, null, null, null).build());

		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

				require.getTimeRecordReqSetting((EmpInfoTerminalCode) any, (ContractCode) any);
				result = timeRecordReqSetting;

				menu.reserve((ReservationRegisterInfo) any, (ReservationDate) any, (GeneralDateTime) any,
						(Optional<WorkLocationCode>)any,
						((Map<Integer, BentoReservationCount>) any));
				result = new BusinessException("System error");

			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		assertThat(resultActual).isEqualTo(Optional.empty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHasError(@Mocked BentoMenu menu2) {

		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		Optional<TimeRecordReqSetting> timeRecordReqSetting = Optional.of(
				new ReqSettingBuilder(empInfoTerCode, contractCode, new CompanyId("1"), "", null, null, null).build());

		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

				require.getTimeRecordReqSetting((EmpInfoTerminalCode) any, (ContractCode) any);
				result = timeRecordReqSetting;

				menu2.reserve((ReservationRegisterInfo) any, (ReservationDate) any, (GeneralDateTime) any,
						(	Optional<WorkLocationCode>)any,
						((Map<Integer, BentoReservationCount>) any));
				result = new BusinessException("System error");

//				require.getStampRecord(contractCode, (StampNumber) any, (GeneralDateTime) any);
//				result = Optional.empty();

				require.getByCardNoAndContractCode(contractCode, (StampNumber) any);
				result = Optional.of(new StampCard(contractCode, new StampNumber("1"), "1", GeneralDate.today(), "2"));

				require.getListEmpID(anyString, (GeneralDate) any);
				result = Arrays.asList("1");
			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		NtsAssert.atomTask(() -> resultActual.get(), any -> require.insertLogAll(any.get()));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHasErrorEmpSid(@Mocked BentoMenu menu2) {

		ReservationReceptionData receptionData = new ReservationReceptionData("1", "A", "200303", "010101", "2");

		Optional<EmpInfoTerminal> empInfoTer = Optional
				.of(new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
						new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
						new EmpInfoTerminalCode(1), Optional.of(new EmpInfoTerSerialNo("1")),
						new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(null)
								.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1)))
								.build());
		Optional<TimeRecordReqSetting> timeRecordReqSetting = Optional.of(
				new ReqSettingBuilder(empInfoTerCode, contractCode, new CompanyId("1"), "", null, null, null).build());

		new Expectations() {
			{
				require.getEmpInfoTerminal((EmpInfoTerminalCode) any, (ContractCode) any);
				result = empInfoTer;

				require.getTimeRecordReqSetting((EmpInfoTerminalCode) any, (ContractCode) any);
				result = timeRecordReqSetting;

				menu2.reserve((ReservationRegisterInfo) any, (ReservationDate) any, (GeneralDateTime) any,
						(Optional<WorkLocationCode>) any,
						((Map<Integer, BentoReservationCount>) any));
				result = new BusinessException("System error");

//				require.getStampRecord(contractCode, (StampNumber) any, (GeneralDateTime) any);
//				result = Optional.empty();

				require.getByCardNoAndContractCode(contractCode, (StampNumber) any);
				result = Optional.of(new StampCard(contractCode, new StampNumber("1"), "1", GeneralDate.today(), "2"));

				require.getListEmpID(anyString, (GeneralDate) any);
				result = new ArrayList<>();
			}
		};

		Optional<AtomTask> resultActual = ConvertTimeRecordReservationService.convertData(require, empInfoTerCode,
				contractCode, receptionData);
		NtsAssert.atomTask(() -> resultActual.get());

	}
}