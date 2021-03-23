package nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Getter;
import nts.arc.layer.dom.objecttype.DomainAggregate;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.LeaveCategory;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.ReservationReceptionData;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.receive.StampReceptionData;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.service.ConvertTimeRecordReservationService;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationCount;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReserveService;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTimeFrame;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.RefectActualResult;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Relieve;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampTypeDisplay;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.WorkInformationStamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.support.SupportCardNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonType;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ReservationArt;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.OvertimeDeclaration;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * @author ThanhNX
 * 就業情報端末
 */
public class EmpInfoTerminal implements DomainAggregate {

	/**
	 * IPアドレス
	 */
	@Getter
	private Optional<FullIpAddress> ipAddress;

	/**
	 * MACアドレス
	 */
	@Getter
	private MacAddress macAddress;

	/**
	 * コード
	 */
	@Getter
	private final EmpInfoTerminalCode empInfoTerCode;

	/**
	 * シリアルNO
	 */
	@Getter
	private Optional<EmpInfoTerSerialNo> terSerialNo;

	/**
	 * 名称
	 */
	@Getter
	private EmpInfoTerminalName empInfoTerName;

	/**
	 * 契約コード
	 */
	@Getter
	private final ContractCode contractCode;

	/**
	 * 打刻情報の作成
	 */
	@Getter
	private CreateStampInfo createStampInfo;

	/**
	 * 機種
	 */
	@Getter
	private ModelEmpInfoTer modelEmpInfoTer;

	/**
	 * 監視間隔時間
	 */
	@Getter
	private MonitorIntervalTime intervalTime;

	// 就業情報端末からの電文解読 not use

	/**
	 * 就業情報端末のメモ
	 */
	@Getter
	private Optional<EmpInfoTerMemo> empInfoTerMemo;

	public EmpInfoTerminal(EmpInfoTerminalBuilder builder) {
		super();
		this.ipAddress = builder.ipAddress;
		this.macAddress = builder.macAddress;
		this.empInfoTerCode = builder.empInfoTerCode;
		this.terSerialNo = builder.terSerialNo;
		this.empInfoTerName = builder.empInfoTerName;
		this.contractCode = builder.contractCode;
		this.createStampInfo = builder.createStampInfo;
		this.modelEmpInfoTer = builder.modelEmpInfoTer;
		this.intervalTime = builder.intervalTime;
		this.empInfoTerMemo = builder.empInfoTerMemo;
	}

	// [1] 打刻
	public Pair<Stamp, StampRecord> createStamp(StampReceptionData recept) {
		// 実績への反映内容
		
		WorkInformationStamp workInformationStamp = new WorkInformationStamp(Optional.empty(), Optional.empty(),
				createStampInfo.getWorkLocationCd().isPresent() ? Optional.of(createStampInfo.getWorkLocationCd().get()) : Optional.empty(), 
				recept.getSupportCode().isEmpty() ? Optional.empty() : Optional.of(new SupportCardNumber(Integer.valueOf(recept.getSupportCode()))));	
		
		RefectActualResult refActualResults = new RefectActualResult(
				workInformationStamp,
				(recept.getLeavingCategory().equals(LeaveCategory.GO_OUT.value)
						|| recept.getLeavingCategory().equals(LeaveCategory.RETURN.value)
						|| recept.getShift().isEmpty()) ? null : new WorkTimeCode(recept.getShift()),
				(recept.getOverTimeHours().isEmpty() || recept.getMidnightTime().isEmpty()) ? null
						: new OvertimeDeclaration(new AttendanceTime(Integer.parseInt(recept.getOverTimeHours())),
								new AttendanceTime(Integer.parseInt(recept.getMidnightTime()))));
		// 打刻する方法
		Relieve relieve = new Relieve(recept.convertAuthcMethod(), StampMeans.TIME_CLOCK);

		// 打刻種類
		Stamp stamp = new Stamp(contractCode, new StampNumber(recept.getIdNumber()), recept.getDateTime(), relieve,
				recept.createStampType(this), refActualResults, Optional.empty());

		StampRecord stampRecord = createStampRecord(recept, stamp);
		return Pair.of(stamp, stampRecord);
	}

	// [２] 予約
	public Pair<StampRecord, AtomTask> createReservRecord(ConvertTimeRecordReservationService.Require require,
			ReservationReceptionData reservReceptData) {
		StampRecord stampRecord = createStampRecord(reservReceptData);
		AtomTask createReserv = createReserv(require, reservReceptData);
		return Pair.of(stampRecord, createReserv);
	}

	// [pvt-1] 打刻の打刻記録を作成
	private StampRecord createStampRecord(StampReceptionData recept, Stamp stamp) {
		// TODO: contractCode
		ButtonType bt = new ButtonType(ReservationArt.NONE, Optional.of(stamp.getType()));
		return new StampRecord(new ContractCode(""), new StampNumber(recept.getIdNumber()), recept.getDateTime(),
				new StampTypeDisplay(bt.getStampTypeDisplay()));
	}

	// [pvt-2] 予約の打刻記録を作成
	private StampRecord createStampRecord(ReservationReceptionData reservReceptData) {
		// TODO: contractCode
		ButtonType bt = new ButtonType(ReservationArt.RESERVATION, Optional.empty());
		return new StampRecord(new ContractCode(""), new StampNumber(reservReceptData.getIdNumber()),
				reservReceptData.getDateTime(), new StampTypeDisplay(bt.getStampTypeDisplay()));
	}

	// [pvt-3] 弁当予約を作成
	private AtomTask createReserv(ConvertTimeRecordReservationService.Require require,
			ReservationReceptionData reserv) {
		Map<Integer, BentoReservationCount> bentoDetails = new HashMap<>();
		bentoDetails.put(reserv.getBentoFrame(), new BentoReservationCount(Integer.parseInt(reserv.getQuantity())));
		return BentoReserveService.reserve(require, new ReservationRegisterInfo(reserv.getIdNumber()),
				new ReservationDate(reserv.getDateTime().toDate(), ReservationClosingTimeFrame.FRAME1),
				reserv.getDateTime(), bentoDetails, Optional.empty());
	}

	public static class EmpInfoTerminalBuilder {
		/**
		 * IPアドレス
		 */
		private Optional<FullIpAddress> ipAddress;

		/**
		 * MACアドレス
		 */
		private MacAddress macAddress;

		/**
		 * コード
		 */
		private EmpInfoTerminalCode empInfoTerCode;

		/**
		 * シリアルNO
		 */
		private Optional<EmpInfoTerSerialNo> terSerialNo;

		/**
		 * 名称
		 */
		private EmpInfoTerminalName empInfoTerName;

		/**
		 * 契約コード
		 */
		private ContractCode contractCode;

		/**
		 * 打刻情報の作成
		 */
		private CreateStampInfo createStampInfo;

		/**
		 * 機種
		 */
		private ModelEmpInfoTer modelEmpInfoTer;

		/**
		 * 監視間隔時間
		 */
		private MonitorIntervalTime intervalTime;

		/**
		 * 就業情報端末のメモ
		 */
		private Optional<EmpInfoTerMemo> empInfoTerMemo;

		public EmpInfoTerminalBuilder(Optional<FullIpAddress> ipAddress, MacAddress macAddress,
				EmpInfoTerminalCode empInfoTerCode, Optional<EmpInfoTerSerialNo> terSerialNo,
				EmpInfoTerminalName empInfoTerName, ContractCode contractCode) {
			this.ipAddress = ipAddress;
			this.macAddress = macAddress;
			this.empInfoTerCode = empInfoTerCode;
			this.terSerialNo = terSerialNo;
			this.empInfoTerName = empInfoTerName;
			this.contractCode = contractCode;
		}

		public EmpInfoTerminalBuilder createStampInfo(CreateStampInfo createStampInfo) {
			this.createStampInfo = createStampInfo;
			return this;
		}

		public EmpInfoTerminalBuilder modelEmpInfoTer(ModelEmpInfoTer modelEmpInfoTer) {
			this.modelEmpInfoTer = modelEmpInfoTer;
			return this;
		}

		public EmpInfoTerminalBuilder intervalTime(MonitorIntervalTime intervalTime) {
			this.intervalTime = intervalTime;
			return this;
		}

		public EmpInfoTerminalBuilder empInfoTerMemo(Optional<EmpInfoTerMemo> empInfoTerMemo) {
			this.empInfoTerMemo = empInfoTerMemo;
			return this;
		}

		public EmpInfoTerminal build() {
			return new EmpInfoTerminal(this);
		}
	}
}
