package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice;

import java.util.Optional;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDateTime;
import nts.gul.location.GeoCoordinate;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.AuthcMethod;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.RefectActualResult;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Relieve;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampLocationInfor;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonSettings;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SettingsSmartphoneStamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampButton;

/**
 * @author sonnlb
 *
 *         スマホから打刻を入力する
 *         UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.勤務実績.打刻管理.打刻.スマホから打刻を入力する.スマホから打刻を入力する
 */
public class EnterStampFromSmartPhoneService {
	/**
	 * [1] 作成する
	 * 
	 * @param require
	 * 
	 * @param 契約コード
	 *            contractCode
	 * @param 社員ID
	 *            employeeID
	 * @param 打刻日時
	 *            stampDatetime
	 * @param 打刻ボタン
	 *            stampButton
	 * @param 地理座標
	 *            positionInfor
	 * @param 実績への反映内容
	 *            refActualResults
	 * 
	 *            ページNOとボタン位置NOから作成する打刻種類を判断する 社員の打刻データを作成する
	 */

	public static TimeStampInputResult create(Require require, ContractCode contractCode, String employeeID,
			GeneralDateTime stampDatetime, StampButton stampButton, Optional<GeoCoordinate> positionInfor,
			RefectActualResult refActualResults) {
		// $打刻場所情報 = empty
		StampLocationInfor stampLocalInfo = null;

		if (positionInfor.isPresent()) {
			stampLocalInfo = new StampLocationInfor(positionInfor.get(), false);
		}

		// $スマホ打刻の打刻設定 = require.スマホ打刻の打刻設定を取得する()
		Optional<SettingsSmartphoneStamp> settingSmartPhoneStampOpt = require
				.getSmartphoneStampSetting();

		if (!settingSmartPhoneStampOpt.isPresent()) {

			throw new BusinessException("Msg_1632");
		}

		// $ボタン詳細設定 = $スマホ打刻の打刻設定.ボタン詳細設定を取得する(打刻ボタン)
		Optional<ButtonSettings> buttonSettingOpt = settingSmartPhoneStampOpt.get()
				.getDetailButtonSettings(stampButton);

		if (!buttonSettingOpt.isPresent()) {
			throw new BusinessException("Msg_1632");
		}

		// $打刻する方法 = 打刻する方法#打刻する方法(ID認証, スマホ打刻)

		Relieve relieve = new Relieve(AuthcMethod.ID_AUTHC, StampMeans.SMART_PHONE);

		return CreateStampDataForEmployeesService.create(require, contractCode, employeeID, Optional.ofNullable(null),
				stampDatetime, relieve, buttonSettingOpt.get().getButtonType(), refActualResults,
				Optional.ofNullable(stampLocalInfo));

	}

	public static interface Require extends CreateStampDataForEmployeesService.Require {

		// [R-1] スマホ打刻の打刻設定を取得する
		Optional<SettingsSmartphoneStamp> getSmartphoneStampSetting();
	}

}