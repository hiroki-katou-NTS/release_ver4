package nts.uk.screen.at.app.query.kdp.kdp001.a;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampDakokuRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.GetStampTypeToSuppressService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.StampToSuppress;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.PortalStampSettings;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.PortalStampSettingsRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SettingsSmartphoneStamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SettingsSmartphoneStampRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSetPerRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSettingPerson;
import nts.uk.shr.com.context.AppContexts;

/**
 * UKDesign.UniversalK.就業.KDP_打刻.KDP001_打刻入力(ポータル).A:打刻入力(ポータル).メニュー別OCD.打刻入力(ポータル)の打刻ボタンを抑制の表示をする
 * 
 * @author sonnlb
 *
 */
@Stateless
public class DisplaySuppressStampButtonInStampInput {
	@Inject
	private GetStampTypeToSuppressService getStampToSuppressService;

	@Inject
	private StampSetPerRepository stampSetPerRepo;

	@Inject
	private SettingsSmartphoneStampRepository settingsSmartphoneStampRepo;

	@Inject
	private PortalStampSettingsRepository portalStampSettingsrepo;

	@Inject
	private StampRecordRepository stampRecordRepo;

	@Inject
	private StampDakokuRepository stampRepo;

	@Inject
	private StampCardRepository stampCardRepo;

	public StampToSuppress getStampToSuppress() {
		GetStampTypeToSuppressServiceRequireImpl require = new GetStampTypeToSuppressServiceRequireImpl(stampSetPerRepo,
				settingsSmartphoneStampRepo, portalStampSettingsrepo, stampRecordRepo, stampRepo, stampCardRepo);

		String employeeId = AppContexts.user().employeeId();
		// 取得する(Require, 社員ID, 打刻手段)
		return this.getStampToSuppressService.get(require, employeeId, StampMeans.PORTAL);
	}

	@AllArgsConstructor
	private class GetStampTypeToSuppressServiceRequireImpl implements GetStampTypeToSuppressService.Require {

		@Inject
		private StampSetPerRepository stampSetPerRepo;

		@Inject
		private SettingsSmartphoneStampRepository settingsSmartphoneStampRepo;

		@Inject
		private PortalStampSettingsRepository portalStampSettingsrepo;

		@Inject
		private StampRecordRepository stampRecordRepo;

		@Inject
		private StampDakokuRepository stampRepo;

		@Inject
		private StampCardRepository stampCardRepo;

		@Override
		public List<StampCard> getListStampCard(String sid) {
			return this.stampCardRepo.getListStampCard(sid);
		}

		@Override
		public List<StampRecord> getStampRecord(List<StampNumber> stampNumbers, GeneralDate date) {
			return this.stampRecordRepo.get(stampNumbers, date);
		}

		@Override
		public List<Stamp> getStamp(List<StampNumber> stampNumbers, GeneralDate date) {
			String contractCode = AppContexts.user().contractCode();
			return this.stampRepo.get(contractCode, stampNumbers, date);
		}

		@Override
		public Optional<StampSettingPerson> getStampSet() {
			String companyId = AppContexts.user().companyId();
			return this.stampSetPerRepo.getStampSet(companyId);
		}

		@Override
		public Optional<SettingsSmartphoneStamp> getSettingsSmartphoneStamp(String comppanyID) {
			return this.settingsSmartphoneStampRepo.get(comppanyID);
		}

		@Override
		public Optional<PortalStampSettings> getPortalStampSettings(String comppanyID) {
			return this.portalStampSettingsrepo.get(comppanyID);
		}

	}
}
