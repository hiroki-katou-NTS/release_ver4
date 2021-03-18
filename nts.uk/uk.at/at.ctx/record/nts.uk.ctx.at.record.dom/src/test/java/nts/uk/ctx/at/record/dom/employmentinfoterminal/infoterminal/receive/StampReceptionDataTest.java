package nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ConvertEmbossCategory;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.CreateStampInfo;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerSerialNo;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal.EmpInfoTerminalBuilder;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalName;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.FullIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MacAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ModelEmpInfoTer;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MonitorIntervalTime;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.OutPlaceConvert;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.PartialIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.StampReceptionData.StampDataBuilder;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.AuthcMethod;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeCalArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SetPreClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampType;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkLocationCD;
import nts.uk.ctx.at.shared.dom.workrule.goingout.GoingOutReason;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author ThanhNX
 *
 *         打刻受信データTest
 */
public class StampReceptionDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getOverTimeHours() {
		StampReceptionData dataNR = new StampDataBuilder("1", "A", "1", "O", "200303", "01").time("0101")
				.overTimeHours(" ").midnightTime("1201").build();

		assertEquals(" ", dataNR.getOverTimeHours());
	}

	@Test
	public void getMidnightTime() {
		StampReceptionData dataNR = new StampDataBuilder("1", "A", "1", "O", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();

		assertEquals(" ", dataNR.getMidnightTime());
	}

	@Test
	public void convertAuthcMethod() {
		StampReceptionData dataNR = new StampDataBuilder("1", "B", "1", "O", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		assertEquals(AuthcMethod.EXTERNAL_AUTHC, dataNR.convertAuthcMethod());

		StampReceptionData dataNRC = new StampDataBuilder("1", "C", "1", "O", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		assertEquals(AuthcMethod.IC_CARD_AUTHC, dataNRC.convertAuthcMethod());

		StampReceptionData dataNRD = new StampDataBuilder("1", "D", "1", "O", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		assertEquals(AuthcMethod.VEIN_AUTHC, dataNRD.convertAuthcMethod());

	}

	@Test
	public void convertChangeCalArt() {

		StampReceptionData dataNRA = new StampDataBuilder("1", "B", "1", "D", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		assertEquals(ChangeCalArt.FIX, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "L", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.FIX, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "5", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.FIX, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "J", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.OVER_TIME, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "S", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.EARLY_APPEARANCE, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "8", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.EARLY_APPEARANCE, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "U", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.BRARK, dataNRA.convertChangeCalArt());

		dataNRA = new StampDataBuilder("1", "B", "1", "6", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeCalArt.BRARK, dataNRA.convertChangeCalArt());

	}

	@Test
	public void convertChangeClockArt() {

		EmpInfoTerminal empInfoTerminal = createEmpInfoTer(NotUseAtr.NOT_USE);
		StampReceptionData dataNRA = new StampDataBuilder("1", "B", "1", "A", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		assertEquals(ChangeClockArt.GOING_TO_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "B", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.GOING_TO_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "D", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.OVER_TIME, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "S", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.GOING_TO_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "U", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.GOING_TO_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "G", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.OVER_TIME, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "H", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.OVER_TIME, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "J", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.OVER_TIME, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.NOT_USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "L", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.WORKING_OUT, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "O", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.START_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.NOT_USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "O", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.GO_OUT, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "Q", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.END_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		empInfoTerminal = createEmpInfoTer(NotUseAtr.NOT_USE);
		dataNRA = new StampDataBuilder("1", "B", "1", "Q", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.RETURN, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "0", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.TEMPORARY_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "1", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.START_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "2", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.END_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "3", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "4", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "5", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "6", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.START_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "7", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.TEMPORARY_SUPPORT_WORK, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "8", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.START_OF_SUPPORT, dataNRA.convertChangeClockArt(empInfoTerminal));

		dataNRA = new StampDataBuilder("1", "B", "1", "9", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		assertEquals(ChangeClockArt.TEMPORARY_LEAVING, dataNRA.convertChangeClockArt(empInfoTerminal));

	}

	@Test
	public void createStampType() {

		StampReceptionData dataNRA = new StampDataBuilder("1", "B", "1", "B", "200303", "01").time("0101")
				.overTimeHours("1101").midnightTime(" ").build();
		EmpInfoTerminal ter = new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
				new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))),
				new MacAddress("AABBCCDD"), new EmpInfoTerminalCode("1"), Optional.of(new EmpInfoTerSerialNo("1")),
				new EmpInfoTerminalName(""), new ContractCode("1")).createStampInfo(
						new CreateStampInfo(new OutPlaceConvert(NotUseAtr.USE, Optional.of(GoingOutReason.UNION)),
								new ConvertEmbossCategory(NotUseAtr.NOT_USE, NotUseAtr.NOT_USE),
								Optional.of(new WorkLocationCD("A")), Optional.of("WorkplaceId")))
						.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1))).build();

		assertThatStamp(new StampType(true, GoingOutReason.UNION, SetPreClockArt.NONE,
				dataNRA.convertChangeClockArt(ter), dataNRA.convertChangeCalArt()), dataNRA.createStampType(ter));

		dataNRA = new StampDataBuilder("1", "B", "1", "O", "200303", "01").time("0101").overTimeHours("1101")
				.midnightTime(" ").build();
		ter = new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
				new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
				new EmpInfoTerminalCode("1"), Optional.of(new EmpInfoTerSerialNo("1")), new EmpInfoTerminalName(""),
				new ContractCode("1")).createStampInfo(
						new CreateStampInfo(new OutPlaceConvert(NotUseAtr.USE, Optional.of(GoingOutReason.UNION)),
								new ConvertEmbossCategory(NotUseAtr.NOT_USE, NotUseAtr.NOT_USE), Optional.empty(), Optional.empty()))
						.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1))).build();

		assertThatStamp(new StampType(true, GoingOutReason.PUBLIC, SetPreClockArt.NONE,
				dataNRA.convertChangeClockArt(ter), dataNRA.convertChangeCalArt()), dataNRA.createStampType(ter));

	}

	private EmpInfoTerminal createEmpInfoTer(NotUseAtr entranceExitOrGout) {
		return new EmpInfoTerminalBuilder(Optional.of(new FullIpAddress(
				new PartialIpAddress(192), new PartialIpAddress(168), new PartialIpAddress(1), new PartialIpAddress(1))), new MacAddress("AABBCCDD"),
				new EmpInfoTerminalCode("1"), Optional.of(new EmpInfoTerSerialNo("1")), new EmpInfoTerminalName(""),
				new ContractCode("1"))
						.createStampInfo(new CreateStampInfo(new OutPlaceConvert(NotUseAtr.NOT_USE, Optional.empty()),
								new ConvertEmbossCategory(entranceExitOrGout, entranceExitOrGout), Optional.empty(), Optional.empty()))
						.modelEmpInfoTer(ModelEmpInfoTer.NRL_1).intervalTime((new MonitorIntervalTime(1))).build();
	}

	private void assertThatStamp(StampType resultActual, StampType recordExpect) {
		assertThat(resultActual.getChangeCalArt()).isEqualTo(recordExpect.getChangeCalArt());
		assertThat(resultActual.getChangeClockArt()).isEqualTo(recordExpect.getChangeClockArt());
		assertThat(resultActual.getGoOutArt()).isEqualTo(recordExpect.getGoOutArt());
		assertThat(resultActual.getSetPreClockArt()).isEqualTo(recordExpect.getSetPreClockArt());
	}

}
