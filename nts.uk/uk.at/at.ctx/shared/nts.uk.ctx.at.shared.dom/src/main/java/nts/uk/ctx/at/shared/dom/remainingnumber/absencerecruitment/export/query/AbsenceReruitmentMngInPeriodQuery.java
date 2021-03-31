package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.NumberCompensatoryLeavePeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.AbsRecMngInPeriodRefactParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.CompenLeaveAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.CompensatoryDayoffDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AbsenceTenProcess;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.LeaveSetOutput;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AbsenceReruitmentMngInPeriodQuery {
	
	/**
	 * 1.未相殺の振休(確定)を取得する
	 * @param sid
	 * @return
	 */
	public static List<AbsRecDetailPara> getAbsOfUnOffset(RequireM1 require, String cid, String sid, GeneralDate ymd) {
		List<AbsRecDetailPara> lstOutput = new ArrayList<>();
		//アルゴリズム「確定振休から未相殺の振休を取得する」を実行する
		List<SubstitutionOfHDManagementData> lstUnOffsetDays = require.substitutionOfHDManagementData(cid, sid, ymd, 0);
		if(lstUnOffsetDays.isEmpty()) {
			return lstOutput;
		}
		//未相殺のドメインモデル「振休管理データ」(Output)の件数をチェックする
		for (SubstitutionOfHDManagementData x : lstUnOffsetDays) {
			//アルゴリズム「暫定振出と紐付けをしない確定振休を取得する」を実行する
			AbsRecDetailPara output = getInterimAndConfirmAbs(sid, x);
			if(output != null) {
				lstOutput.add(output);	
			}			
		}		
		return lstOutput;
	}
	
	/**
	 * 1-1.確定振休から未相殺の振休を取得する
	 * @param sid
	 * @return
	 */
	//List<SubstitutionOfHDManagementData> getAbsOfUnOffsetFromConfirm(String cid, String sid, GeneralDate ymd);
	/**
	 * 1-3.暫定振出と紐付けをしない確定振休を取得する
	 * @param confirmAbsData
	 * @return
	 */
	public static AbsRecDetailPara getInterimAndConfirmAbs(String sid, SubstitutionOfHDManagementData confirmAbsData) {
		AbsRecDetailPara outData = new AbsRecDetailPara();
		//ドメインモデル「暫定振出振休紐付け管理」を取得する　 REPONSE 対応
		//List<InterimRecAbsMng> lstInterim = recAbsRepo.getBySidMng(DataManagementAtr.INTERIM, DataManagementAtr.CONFIRM, confirmAbsData.getSubOfHDID());
		double unUseDays = confirmAbsData.getRemainDays().v();
		/*for (InterimRecAbsMng interimRecAbsMng : lstInterim) {
			unUseDays -= interimRecAbsMng.getUseDays().v();
		}*/
		if(unUseDays <= 0) {
			return null;
		}
		//未相殺日数：INPUT.未相殺日数－合計(暫定振出振休紐付け管理.使用日数)
		UnOffsetOfAbs unOffsetData = new UnOffsetOfAbs(confirmAbsData.getSubOfHDID(), confirmAbsData.getRequiredDays().v(), unUseDays);
		//INPUT．振休管理データを「振出振休明細」に追加する
		outData = new AbsRecDetailPara(sid, 
				MngDataStatus.CONFIRMED,
				confirmAbsData.getHolidayDate(), 
				OccurrenceDigClass.DIGESTION,
				Optional.of(unOffsetData),
				Optional.empty());
		return outData;
		
	}

	/**
	 * 2.未使用の振出(確定)を取得する
	 * @param sid
	 * @return
	 */
	public static List<AbsRecDetailPara> getUnUseDaysConfirmRec(RequireM9 require, String cid, String sid, List<AbsRecDetailPara> lstDataDetail, GeneralDate ymd) {
		//2-1.確定振出から未使用の振出を取得する
		List<PayoutManagementData> lstConfirmRec = require.payoutManagementData(cid, sid, ymd, 0, DigestionAtr.UNUSED);
		if(lstConfirmRec.isEmpty()) {
			return lstDataDetail;
		}
		for (PayoutManagementData confirmRecData : lstConfirmRec) {
			if(confirmRecData.getPayoutDate().getDayoffDate().isPresent()
					&& confirmRecData.getPayoutDate().getDayoffDate().get().afterOrEquals(ymd)) {
				continue;
			}
			//アルゴリズム「暫定振休と紐付けをしない確定振出を取得する」を実行する
			//ドメインモデル「暫定振出振休紐付け管理」を取得する REPONSE 対応			
			//List<InterimRecAbsMng> lstInterim = recAbsRepo.getRecBySidMngAtr(DataManagementAtr.CONFIRM, DataManagementAtr.INTERIM, confirmRecData.getPayoutId());
			double unUseDays = confirmRecData.getUnUsedDays().v();
			/*for (InterimRecAbsMng interimData : lstInterim) {
				unUseDays -= interimData.getUseDays().v();
			}*/
			//未使用日数をチェックする
			if(unUseDays > 0) {
				UnUseOfRec unUseDayOfRec = new UnUseOfRec(confirmRecData.getExpiredDate(), 
						confirmRecData.getPayoutId(), 
						confirmRecData.getOccurredDays().v(), 
						EnumAdaptor.valueOf(confirmRecData.getLawAtr().value, StatutoryAtr.class), 
						unUseDays,
						DigestionAtr.USED,
						Optional.empty(),
						Optional.of(ymd));
				//Khong ton tai 1 ngay vua co nghi bu va di lam bu
				AbsRecDetailPara dataDetail = new AbsRecDetailPara(sid, 
						MngDataStatus.CONFIRMED,
						confirmRecData.getPayoutDate(),
						OccurrenceDigClass.OCCURRENCE,
						Optional.empty(),
						Optional.of(unUseDayOfRec));
				lstDataDetail.add(dataDetail);	
			}			
		}		
		return lstDataDetail;
	}

	/**
	 * 繰越数を計算する
	 * @param startDate 集計開始日
	 * @param lstDataDetail 振出振休明細
	 * @return
	 */
	public static ResultAndError calcCarryForwardDays(GeneralDate startDate, List<AbsRecDetailPara> lstDataDetail, boolean mode) {
		// アルゴリズム「6.残数と未消化を集計する」を実行
		AbsDaysRemain outPut = getRemainUnDigestedDays(lstDataDetail, startDate, mode);
		return new ResultAndError(outPut.getRemainDays(), outPut.isErrors());
	}

	/**
	 * 6.残数と未消化を集計する
	 * @param lstDataDetail 「振出振休明細」
	 * @param baseDate 基準日
	 * @return
	 */
	public static AbsDaysRemain getRemainUnDigestedDays(List<AbsRecDetailPara> lstDataDetail, GeneralDate baseDate, boolean mode) {
		AbsDaysRemain outData = new AbsDaysRemain(0, 0, false);
		double unOffSetDays = 0;
		//「振出振休明細」をループする
		for (AbsRecDetailPara detailData : lstDataDetail) {
			//「振出振休明細」．発生消化区分をチェックする
			if(detailData.getOccurrentClass() == OccurrenceDigClass.DIGESTION) {
				//「相殺できないエラー」を「振休の集計結果」．エラー情報に追加する
				//ループ中の「振休の未相殺」．未相殺日数 >0 AND
				if(detailData.getUnOffsetOfAb().get().getUnOffSetDays() > 0) {
					outData.setErrors(true);	
				}
				//残日数 -= ループ中の「振休の未相殺」．未相殺日数				
				unOffSetDays += detailData.getUnOffsetOfAb().get().getUnOffSetDays();
			} else if (detailData.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE) {
				UnUseOfRec recData = detailData.getUnUseOfRec().get();
				//期限切れかをチェックする
				if((mode && recData.getExpirationDate().beforeOrEquals(baseDate))
						|| (!mode && recData.getExpirationDate().before(baseDate))) {
					//未消化日数 += ループ中の「振出の未使用」．未使用日数
					outData.setUnDigestedDays(outData.getUnDigestedDays() + recData.getUnUseDays());
				} else {
					//残日数 += ループ中の「振出の未使用」．未使用日数
					outData.setRemainDays(outData.getRemainDays() + recData.getUnUseDays());
				}
			}
			
		}
		if(unOffSetDays != 0) {
			outData.setRemainDays(outData.getRemainDays() - unOffSetDays);
		}
		return outData;
	}

	/**
	 * 3.未相殺の振休(暫定)を取得する
	 * @return
	 */
	public static List<AbsRecDetailPara> getUnOffsetDaysAbsInterim(RequireM8 require, AbsRecMngInPeriodParamInput paramInput) {
		List<InterimAbsMng> lstAbsMng = new ArrayList<>();
		List<InterimRemain> lstInterimMng = new ArrayList<>();
		List<AbsRecDetailPara> lstOutput = new ArrayList<>();
		//INPUT．モードをチェックする
		if(paramInput.isMode()) {
			//暫定残数管理データを作成する
			Map<GeneralDate, DailyInterimRemainMngData> interimData = require.monthInterimRemainData(paramInput.getCid(),
					paramInput.getSid(), paramInput.getDateData());
			//メモリ上からドメインモデル「暫定振休管理データ」を取得する
			if(!interimData.isEmpty()) {				
				List<DailyInterimRemainMngData> lstRemainMngData = interimData.values().stream().collect(Collectors.toList());
				for (DailyInterimRemainMngData x : lstRemainMngData) {
					Optional<InterimAbsMng> optAbsMng = x.getInterimAbsData();
					optAbsMng.ifPresent(y -> {
						lstAbsMng.add(y);
					});
					List<InterimRemain> lstInterimCreate = x.getRecAbsData();
					if(!lstInterimCreate.isEmpty()) {
						lstInterimMng.addAll(lstInterimCreate);
					}
				}				
			}
		} else {
			//ドメインモデル「暫定振休管理データ」を取得する
			lstInterimMng =  require.interimRemains(paramInput.getSid(), paramInput.getDateData(), RemainType.PAUSE);
			
			lstInterimMng.stream().forEach(x -> {
				Optional<InterimAbsMng> optAbsMng = require.interimAbsMng(x.getRemainManaID());
				optAbsMng.ifPresent(z -> lstAbsMng.add(z));
			});
		}
		//INPUT．上書きフラグをチェックする
		if(paramInput.isOverwriteFlg()
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.getUseAbsMng().isEmpty()) {
			List<InterimAbsMng> lstAbsMngTmp = new ArrayList<>(lstAbsMng);
			List<InterimRemain> lstInterimMngTmp = new ArrayList<>(lstInterimMng);
			for (InterimAbsMng absMng : paramInput.getUseAbsMng()) {
				//INPUT．上書き用の暫定管理データをドメインモデル「暫定振休管理データ」に追加する
				List<InterimRemain> lstInputData = paramInput.getInterimMng().stream().
						filter(x -> x.getRemainManaID() == absMng.getRemainManaID())
						.collect(Collectors.toList());			
				if(!lstInputData.isEmpty()) {
					InterimRemain inputData = lstInputData.get(0);
					List<InterimRemain> lstMngTmp = lstInterimMngTmp.stream()
							.filter(x -> x.getYmd().equals(inputData.getYmd()))
							.collect(Collectors.toList());
					if(!lstMngTmp.isEmpty()) {
						InterimRemain interimMngTmp = lstMngTmp.get(0);					
						List<InterimAbsMng> lstAbsTmp = lstAbsMngTmp.stream().filter(x -> x.getRemainManaID().equals(interimMngTmp.getRemainManaID()))
								.collect(Collectors.toList());
						if(!lstAbsTmp.isEmpty()) {
							InterimAbsMng absMngTmp = lstAbsTmp.get(0);
							lstAbsMng.remove(absMngTmp);
							lstAbsMng.add(absMng);
						}
					}
					lstInterimMng.add(inputData);
				}
			}
			
		}
		for (InterimAbsMng interimAbsMng : lstAbsMng) {
			InterimRemain remainData = lstInterimMng.stream().filter(x -> x.getRemainManaID().equals(interimAbsMng.getRemainManaID()))
					.collect(Collectors.toList()).get(0);
			//アルゴリズム「振出と紐付けをしない振休を取得する」を実行する
			AbsRecDetailPara outputData = getNotTypeRec(require, interimAbsMng, remainData);
			lstOutput.add(outputData);
		}
		return lstOutput;
	}
	
	/**
	 * 3-1.振出と紐付けをしない振休を取得する
	 * @param lstAbsMng
	 * @return
	 */
	public static AbsRecDetailPara getNotTypeRec(RequireM7 require, InterimAbsMng absMng, InterimRemain remainData) {
		//ドメインモデル「暫定振出振休紐付け管理」を取得する
		List<InterimRecAbsMng> lstInterimMng = require.interimRecAbsMng(absMng.getRemainManaID(), false, DataManagementAtr.INTERIM);
		double unOffsetDays = absMng.getRequeiredDays().v();
		if(!lstInterimMng.isEmpty()) {
			for (InterimRecAbsMng recAbsData : lstInterimMng) {
				unOffsetDays -= recAbsData.getUseDays().v();
			}			
		}
		UnOffsetOfAbs unOffset = new UnOffsetOfAbs(absMng.getRemainManaID(),
				absMng.getRequeiredDays().v(),
				unOffsetDays);
		MngDataStatus dataAtr = MngDataStatus.NOTREFLECTAPP;
		if(remainData.getCreatorAtr() == CreateAtr.SCHEDULE) {
			dataAtr = MngDataStatus.SCHEDULE;
		} else if (remainData.getCreatorAtr() == CreateAtr.RECORD){
			dataAtr = MngDataStatus.RECORD;
		}
		CompensatoryDayoffDate date = new CompensatoryDayoffDate(false, Optional.of(remainData.getYmd()));
		AbsRecDetailPara outData = new AbsRecDetailPara(remainData.getSID(), 
				dataAtr,
				date,
				OccurrenceDigClass.DIGESTION,
				Optional.of(unOffset),
				Optional.empty());
		return outData;
	}

	/**
	 * 4-1.振休と紐付けをしない振出を取得する
	 * @param interimRecMng
	 * @param remainData
	 * @return
	 */
	public static AbsRecDetailPara getUnUseDayOfRecInterim(RequireM6 require, CacheCarrier cacheCarrier,
			InterimRecMng interimRecMng, InterimRemain remainData,LeaveSetOutput getSetForLeave,
			GeneralDate startDate, GeneralDate baseDate, String cid, String sid) {
		//ドメインモデル「暫定振出振休紐付け管理」を取得する
		List<InterimRecAbsMng> lstInterimMng = require.interimRecAbsMng(interimRecMng.getRemainManaID(),
				true, DataManagementAtr.INTERIM);
		
		//未使用日数←SELF.発生日数
		double unUseDays = interimRecMng.getOccurrenceDays().v();
		if(!lstInterimMng.isEmpty()) {
			for (InterimRecAbsMng interimMng : lstInterimMng) {
				//未使用日数：INPUT.暫定振出管理データ.発生日数－合計(暫定振出振休紐付け管理.使用日数)
				unUseDays -= interimMng.getUseDays().v();
			}
		}
		UnUseOfRec unUseInfo = new UnUseOfRec(interimRecMng.getExpirationDate(),
				interimRecMng.getRemainManaID(),
				interimRecMng.getOccurrenceDays().v(),
				StatutoryAtr.NONSTATURORY,
				//interimRecMng.getStatutoryAtr(),
				unUseDays,
				DigestionAtr.USED,
				Optional.empty(),
				Optional.empty());
		//INPUT．振休使用期限をチェックする //ExpirationTime 0: "当月", 2: "年度末クリア"
		if(getSetForLeave.getExpirationOfLeave() == 0 ||getSetForLeave.getExpirationOfLeave() == 2) {
			//社員に対応する処理締めを取得する
			Closure period = ClosureService.getClosureDataByEmployee(require, cacheCarrier, sid, baseDate);
			if(period == null) {
				return null;
			}
			//指定した年月日時点の締め期間を取得する
			Optional<ClosurePeriod> optClosurePeriod = period.getClosurePeriodByYmd(remainData.getYmd());			
			if(getSetForLeave.getExpirationOfLeave() == 0) {
				unUseInfo.setStartDate(optClosurePeriod.isPresent() ? Optional.ofNullable(optClosurePeriod.get().getPeriod().start()) : Optional.empty());
			} else {
				//会社の期首月を取得する
				CompanyDto compInfor = require.firstMonth(cacheCarrier, cid);
				
				//「締め期間」．年月の月と期首月を比較する
				if(optClosurePeriod.isPresent() && optClosurePeriod.get().getYearMonth().v() >= compInfor.getStartMonth()) {
					//使用開始日=「締め期間」．期間．開始日.AddMonths(「締め期間」．年月の月 - 期首月)
					int months = optClosurePeriod.get().getYearMonth().v() - compInfor.getStartMonth();
					unUseInfo.setStartDate(Optional.of(optClosurePeriod.get().getPeriod().start().addMonths(months)));
				} else {
					//使用開始日=「締め期間」．期間．開始日.AddMonths(12 + 「締め期間」．年月の月 - 期首月)
					int months = optClosurePeriod.get().getYearMonth().v() - compInfor.getStartMonth();
					unUseInfo.setStartDate(Optional.of(optClosurePeriod.get().getPeriod().start().addMonths(12 + months)));
				}
			}
		} else {
			unUseInfo.setStartDate(Optional.empty());
		}
		CompensatoryDayoffDate date = new CompensatoryDayoffDate(false, Optional.of(remainData.getYmd()));
		MngDataStatus dataAtr = MngDataStatus.NOTREFLECTAPP;
		if(remainData.getCreatorAtr() == CreateAtr.SCHEDULE) {
			dataAtr = MngDataStatus.SCHEDULE;
		} else if (remainData.getCreatorAtr() == CreateAtr.RECORD){
			dataAtr = MngDataStatus.RECORD;
		}
		AbsRecDetailPara outputData = new AbsRecDetailPara(remainData.getSID(),
				dataAtr,
				date,
				OccurrenceDigClass.OCCURRENCE,
				Optional.empty(),
				Optional.of(unUseInfo));
		return outputData;
	}

	/**
	 * 5.時系列順で相殺する
	 * @param lstDetailData
	 * @return
	 */
	public static List<AbsRecDetailPara> offsetSortTimes(List<AbsRecDetailPara> lstDetailData) {
		//INPUT．「振出振休明細」．発生消化区分により、振出と振休を別ける
		//振休
		List<AbsRecDetailPara> lstAbsDetailData = lstDetailData.stream().filter(x -> x.getOccurrentClass() == OccurrenceDigClass.DIGESTION)
				.collect(Collectors.toList());
		//振出
		List<AbsRecDetailPara> lstRecDetailData = lstDetailData.stream().filter(x -> x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE)
				.collect(Collectors.toList());
		List<AbsRecDetailPara> lstAbsTmp = new ArrayList<>(lstAbsDetailData);
		List<AbsRecDetailPara> lstRecTmp = new ArrayList<>(lstRecDetailData);
		//「振出振休明細」(振休)をループする
		for (AbsRecDetailPara absDetailPara : lstAbsDetailData) {
			UnOffsetOfAbs absData = absDetailPara.getUnOffsetOfAb().get();
			//ループ中の「振休の未相殺」．未相殺日数をチェックする
			if(absData.getUnOffSetDays() <= 0) {
				continue;
			}
			//「振出振休明細」(振出)をループする
			for (AbsRecDetailPara recDetailData : lstRecDetailData) {
				UnUseOfRec recData = recDetailData.getUnUseOfRec().get();
				
				//日付不明の場合、false：期限切れてない、相殺可
				GeneralDate chkDate = absDetailPara.getYmdData().getDayoffDate().isPresent() ? absDetailPara.getYmdData().getDayoffDate().get() : GeneralDate.min();
				
				
				if(recData.getExpirationDate().before(chkDate) //期限切れかをチェックする
						|| (recData.getStartDate().isPresent() && !recData.getStartDate().get().beforeOrEquals(absDetailPara.getYmdData().getDayoffDate().get()))//使用可能かチェックする
						|| recData.getUnUseDays() < 0 //ループ中の「振出未使用」．未使用日数をチェックする
						) {
					continue;
				}
				//振出振休相殺できる日数の大小チェックする
				if(absData.getUnOffSetDays() > recData.getUnUseDays()) {
					//ループ中の「振休の未相殺」．未相殺日数 -= ループ中の「振出の未使用」．未使用日数
					lstAbsTmp.remove(absDetailPara);
					absDetailPara.getUnOffsetOfAb().get().setUnOffSetDays(absData.getUnOffSetDays() - recData.getUnUseDays());					
					lstAbsTmp.add(absDetailPara);
					//ループ中の「振出の未使用」．未使用日数 = 0
					lstRecTmp.remove(recDetailData);
					recDetailData.getUnUseOfRec().get().setUnUseDays(0);
					lstRecTmp.add(recDetailData);
				} else {
					//ループ中の「振出の未使用」．未使用日数 -= ループ中の「振休の未相殺」．未相殺日数
					lstRecTmp.remove(recDetailData);
					recDetailData.getUnUseOfRec().get().setUnUseDays(recData.getUnUseDays() - absData.getUnOffSetDays());
					lstRecTmp.add(recDetailData);
					//ループ中の「振休の未相殺」．未相殺日数 = 0
					lstAbsTmp.remove(absDetailPara);
					absDetailPara.getUnOffsetOfAb().get().setUnOffSetDays(0);					
					lstAbsTmp.add(absDetailPara);
										
					break;
				}
				
			}
			//相殺できる振出があるかチェックする
			//全ての「振出振休明細」(振出)．「振出の未使用」．未使用日数が0の場合、相殺できる振出がないとする。
			List<AbsRecDetailPara> lstRecTmpChk = lstRecTmp.stream().filter(x -> x.getUnUseOfRec().get().getUnUseDays() == 0)
					.collect(Collectors.toList());
			if(!lstRecTmpChk.isEmpty() && lstRecTmpChk.size() == lstRecTmp.size()) {
				break;
			}
		}
		if(!lstRecTmp.isEmpty()) {
			lstAbsTmp.addAll(lstRecTmp);	
		}
		return lstAbsTmp;
	}

	/**
	 * 7.発生数・使用数を計算する
	 * @param lstDetailData
	 * @param dateData
	 * @return
	 */
	public static AbsDaysRemain getOccurrenceUseDays(List<AbsRecDetailPara> lstDetailData, DatePeriod dateData) {
		AbsDaysRemain outputData = new AbsDaysRemain(0, 0, false);
		
		//パラメータ「List<振休振出明細>」を取得
		for (AbsRecDetailPara detailData : lstDetailData) {
			if(!detailData.getYmdData().isUnknownDate()
					&& detailData.getYmdData().getDayoffDate().isPresent()
					&& detailData.getYmdData().getDayoffDate().get().beforeOrEquals(dateData.end())
					&& detailData.getYmdData().getDayoffDate().get().afterOrEquals(dateData.start())) {
				//処理中の振出振休明細．発生消化区分をチェック
				if(detailData.getOccurrentClass() == OccurrenceDigClass.DIGESTION) {
					//振休使用日数 += 振休の未相殺．必要日数
					outputData.setUnDigestedDays(outputData.getUnDigestedDays() + detailData.getUnOffsetOfAb().get().getRequestDays());
				} else {
					//振出発生日数 += 振出の未使用．発生日数
					outputData.setRemainDays(outputData.getRemainDays() + detailData.getUnUseOfRec().get().getOccurrenceDays());
				}				
			}
		}
		
		return outputData;
	}

	/**
	 * 消化区分と消滅日を計算する
	 * @param lstDetailData
	 * @param baseDate
	 * @return
	 */
	public static List<AbsRecDetailPara> calDigestionAtr(List<AbsRecDetailPara> lstDetailData, GeneralDate baseDate) {
		lstDetailData.stream().forEach(x -> {
			//ループ中の「振出振休明細．発生消化区分」をチェック
			if(x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE) {
				UnUseOfRec recData = x.getUnUseOfRec().get();
				if(recData.getUnUseDays() <= 0) {
					//振出の未使用．振休消化区分　←　"消化済"
					x.getUnUseOfRec().get().setDigestionAtr(DigestionAtr.USED);
				} else {
					if(recData.getExpirationDate().after(baseDate)) {
						//振出の未使用．振休消化区分　←　"未消化"
						x.getUnUseOfRec().get().setDigestionAtr(DigestionAtr.UNUSED);
					} else {
						//振出の未使用．振休消化区分　←　"消滅"
						x.getUnUseOfRec().get().setDigestionAtr(DigestionAtr.EXPIRED);
						//振出の未使用．消滅日　←　振出の未使用．使用期限日
						x.getUnUseOfRec().get().setDisappearanceDate(Optional.of(recData.getExpirationDate()));
					}
				}
			}
		});
		return lstDetailData;
	}

	/**
	 * アルゴリズム「未相殺の振休(暫定)を取得する」を実行する
	 * アルゴリズム「未使用の振出(暫定)を取得する」を実行する
	 * @param paramInput
	 * @return
	 */
	public static List<AbsRecDetailPara> lstInterimInfor(RequireM5 require, CacheCarrier cacheCarrier,
			AbsRecMngInPeriodParamInput paramInput, List<AbsRecDetailPara> lstAbsRec) {
		
		List<InterimAbsMng> lstAbsMng = new ArrayList<>();
		List<InterimRemain> lstInterimMngOfAbs = new ArrayList<>();
		List<InterimRemain> lstInterimMngOfRec = new ArrayList<>();
		List<InterimRecMng> lstRecMng = new ArrayList<>();
		
		//INPUT．モードをチェックする
		if(paramInput.isMode()) {
			//暫定残数管理データを作成する
			//Map<GeneralDate, DailyInterimRemainMngData> interimData = createDataService.monthInterimRemainData(paramInput.getCid(), paramInput.getSid(), paramInput.getDateData());
			
			//if(!interimData.isEmpty()) {				
				//List<DailyInterimRemainMngData> lstRemainMngData = interimData.values().stream().collect(Collectors.toList());
				//for (DailyInterimRemainMngData x : lstRemainMngData) {
					//INPUT．上書き用の暫定管理データを受け取る
					lstInterimMngOfAbs = paramInput.getInterimMng().stream()
							.filter(x -> x.getYmd().afterOrEquals(paramInput.getDateData().start())
									&& x.getYmd().beforeOrEquals(paramInput.getDateData().end())
									&& x.getRemainType() == RemainType.PAUSE)
							.collect(Collectors.toList());
					
					lstInterimMngOfAbs.stream().forEach(x -> {
						List<InterimAbsMng> temp = paramInput.getUseAbsMng()
								.stream()
								.filter(y -> y.getRemainManaID().equals(x.getRemainManaID()))
								.collect(Collectors.toList());
						lstAbsMng.addAll(temp);
						
					});
					
					//メモリ上からドメインモデル「暫定振出管理データ」を取得する
					lstInterimMngOfRec =  paramInput.getInterimMng().stream()
							.filter(x -> x.getYmd().afterOrEquals(paramInput.getDateData().start())
									&& x.getYmd().beforeOrEquals(paramInput.getDateData().end())
									&& x.getRemainType() == RemainType.PICKINGUP)
							.collect(Collectors.toList());
					lstInterimMngOfRec.stream().forEach(a -> {
						List<InterimRecMng> temp = paramInput.getUseRecMng()
								.stream()
								.filter(b -> b.getRemainManaID().equals(a.getRemainManaID()))
								.collect(Collectors.toList());
						lstRecMng.addAll(temp);
					});
					
				//}				
			//}
		} else {
			//ドメインモデル「暫定振休管理データ」を取得する
			lstInterimMngOfAbs =  require.interimRemains(paramInput.getSid(), paramInput.getDateData(), RemainType.PAUSE);
			for (InterimRemain x : lstInterimMngOfAbs) {
				Optional<InterimAbsMng> optAbsMng = require.interimAbsMng(x.getRemainManaID());
				if(optAbsMng.isPresent()) {
					lstAbsMng.add(optAbsMng.get());
				}
			}
			//ドメインモデル「暫定振出管理データ」を取得する
			lstInterimMngOfRec =  require.interimRemains(paramInput.getSid(), paramInput.getDateData(), RemainType.PICKINGUP);
			for (InterimRemain x : lstInterimMngOfRec) {
				Optional<InterimRecMng> optRecMng = require.interimRecMng(x.getRemainManaID());
				if(optRecMng.isPresent()) {
					lstRecMng.add(optRecMng.get());
				}
			}	
		}
		//20181003 DuDT fix bug 101491 ↓
		List<InterimRemain> lstTmpAbs = new ArrayList<>(lstInterimMngOfAbs);
		List<InterimAbsMng> lstAbsUsen = new ArrayList<>(lstAbsMng);
		List<InterimRemain> lstTmpRec = new ArrayList<>(lstInterimMngOfRec);
		List<InterimRecMng> lstRecMngUsen = new ArrayList<>(lstRecMng);
		if(paramInput.isOverwriteFlg() 
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.isMode()) {
			for (InterimRemain interimRemain : paramInput.getInterimMng()) {
				List<InterimRemain> lstInterimAbsUsen = lstTmpAbs.stream()
						.filter(a -> a.getYmd().equals(interimRemain.getYmd())).collect(Collectors.toList());
				if(!lstInterimAbsUsen.isEmpty()) {
					InterimRemain temp = lstInterimAbsUsen.get(0);
					lstInterimMngOfAbs.remove(temp);
					List<InterimAbsMng> tmpAbsUsen = lstAbsUsen.stream().filter(b -> b.getRemainManaID().equals(temp.getRemainManaID()))
							.collect(Collectors.toList());
					if(!tmpAbsUsen.isEmpty()) {
						lstAbsMng.remove(tmpAbsUsen.get(0));
					}
				}
				List<InterimRemain> lstRecUsen = lstTmpRec.stream()
						.filter(b -> b.getYmd().equals(interimRemain.getYmd())).collect(Collectors.toList());
				if(!lstRecUsen.isEmpty()) {
					InterimRemain temp = lstRecUsen.get(0);
					lstInterimMngOfRec.remove(temp);
					List<InterimRecMng> tempLstRec = lstRecMngUsen.stream().filter(b -> b.getRemainManaID().equals(temp.getRemainManaID()))
							.collect(Collectors.toList());
					if(!tempLstRec.isEmpty()) {
						lstRecMng.remove(tempLstRec.get(0));
					}
				}
			}
		}
		//20181003 DuDT fix bug 101491 ↑
		if(paramInput.isOverwriteFlg()
				&& paramInput.getCreatorAtr().isPresent()
				&& paramInput.getProcessDate().isPresent()) {
			List<InterimRemain> lstOfAbsremover = lstInterimMngOfAbs.stream().filter(x -> x.getCreatorAtr() == paramInput.getCreatorAtr().get()
					&& x.getYmd().afterOrEquals(paramInput.getProcessDate().get().start())
					&& x.getYmd().beforeOrEquals(paramInput.getProcessDate().get().end())).collect(Collectors.toList());
			lstOfAbsremover.stream().forEach(x -> {
				List<InterimAbsMng> lstAbs = lstAbsMng.stream().filter(a -> a.getRemainManaID().equals(x.getRemainManaID())).collect(Collectors.toList());
				lstAbsMng.removeAll(lstAbs);
			});
			lstInterimMngOfAbs.removeAll(lstOfAbsremover);
			List<InterimRemain> lstOfRecRemove = lstInterimMngOfRec.stream().filter(x -> x.getCreatorAtr() == paramInput.getCreatorAtr().get()
					&& x.getYmd().afterOrEquals(paramInput.getProcessDate().get().start())
					&& x.getYmd().beforeOrEquals(paramInput.getProcessDate().get().end())).collect(Collectors.toList());
			lstOfRecRemove.stream().forEach(x -> {
				List<InterimRecMng> lstRec = lstRecMng.stream().filter(a -> a.getRemainManaID().equals(x.getRemainManaID())).collect(Collectors.toList());
				lstRecMng.removeAll(lstRec);
			});
			 
			lstInterimMngOfRec.removeAll(lstOfRecRemove);
		}
		List<AbsRecDetailPara> lstOutputOfAbs = lstOutputOfAbs(require, lstAbsMng, lstInterimMngOfAbs, paramInput);
		List<AbsRecDetailPara> lstOutputOfRec = lstOutputOfRec(require, cacheCarrier, lstRecMng, lstInterimMngOfRec, paramInput);
		lstAbsRec.addAll(lstOutputOfAbs);
		lstAbsRec.addAll(lstOutputOfRec);
		return lstAbsRec;
	}
	
	/**
	 * [No.506]振休残数を取得する
	 * @param employeeID
	 * @param date
	 * @return
	 */
	public static CompenLeaveAggrResult getAbsRecMngRemain(RequireM2 require, CacheCarrier cacheCarrier,
			String employeeID, GeneralDate date) {
		String companyID = AppContexts.user().companyId();
		//社員に対応する締め期間を取得する
		DatePeriod period = ClosureService.findClosurePeriod(require, cacheCarrier, employeeID, date);
		val inputParam = new AbsRecMngInPeriodRefactParamInput(companyID, //・ログイン会社ID
				employeeID, //・INPUT．社員ID
				new DatePeriod(period.start(), period.start().addYears(1).addDays(-1)), //・集計開始日＝締め期間．開始年月日 - ・集計終了日＝締め期間．開始年月日＋１年－１日
				date, //・基準日＝INPUT．基準日
				false, //・モード＝その他モード
				false, //・上書きフラグ=false
				Collections.emptyList(), //上書き用の暫定管理データ：なし
				Collections.emptyList(), 
				Collections.emptyList(),
				Optional.empty(),Optional.empty(),Optional.empty(), new FixedManagementDataMonth());
		return NumberCompensatoryLeavePeriodQuery.process(require, inputParam);
	}


	
	/**
	 * 暫定振休管理データ: 上書きフラグをチェックする
	 * @param lstAbsMng
	 * @param lstInterimMngOfAbs
	 * @param paramInput
	 * @return
	 */
	private static List<AbsRecDetailPara> lstOutputOfAbs(RequireM7 require, List<InterimAbsMng> lstAbsMng, 
			List<InterimRemain> lstInterimMngOfAbs, AbsRecMngInPeriodParamInput paramInput) {
		if(paramInput.isOverwriteFlg()
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.getUseAbsMng().isEmpty()) {
			List<InterimAbsMng> lstAbsMngTmp = new ArrayList<>(lstAbsMng);
			List<InterimRemain> lstInterimMngTmp = new ArrayList<>(lstInterimMngOfAbs);
			for (InterimAbsMng absMng : paramInput.getUseAbsMng()) {
				//INPUT．上書き用の暫定管理データをドメインモデル「暫定振休管理データ」に追加する
				List<InterimRemain> lstInputData = paramInput.getInterimMng().stream().
						filter(x -> x.getRemainManaID() == absMng.getRemainManaID())
						.collect(Collectors.toList());			
				if(!lstInputData.isEmpty()) {
					InterimRemain inputData = lstInputData.get(0);
					List<InterimRemain> lstMngTmp = lstInterimMngTmp.stream()
							.filter(x -> x.getYmd().equals(inputData.getYmd()))
							.collect(Collectors.toList());					
					if(!lstMngTmp.isEmpty()) {
						InterimRemain interimMngTmp = lstMngTmp.get(0);
						lstInterimMngOfAbs.remove(interimMngTmp);
						List<InterimAbsMng> lstAbsTmp = lstAbsMngTmp.stream().filter(x -> x.getRemainManaID().equals(interimMngTmp.getRemainManaID()))
								.collect(Collectors.toList());
						if(!lstAbsTmp.isEmpty()) {
							InterimAbsMng absMngTmp = lstAbsTmp.get(0);
							lstAbsMng.remove(absMngTmp);
						}
					}
					lstAbsMng.add(absMng);
					lstInterimMngOfAbs.add(inputData);
				}
			}
		}
		List<AbsRecDetailPara> lstOutputOfAbs = new ArrayList<>();
		for (InterimAbsMng interimAbsMng : lstAbsMng) {
			List<InterimRemain> lstRemainData = lstInterimMngOfAbs.stream().filter(x -> x.getRemainManaID().equals(interimAbsMng.getRemainManaID()))
					.collect(Collectors.toList());
			if(lstRemainData.isEmpty()) {
				continue;
			}
			InterimRemain remainData = lstRemainData.get(0);
			//アルゴリズム「振出と紐付けをしない振休を取得する」を実行する
			AbsRecDetailPara outputData = getNotTypeRec(require, interimAbsMng, remainData);
			lstOutputOfAbs.add(outputData);
		}
		return lstOutputOfAbs;
	}
	
	private static List<AbsRecDetailPara> lstOutputOfRec(RequireM3 require, CacheCarrier cacheCarrier, 
			List<InterimRecMng> lstRecMng, List<InterimRemain> lstInterimMngOfRec, AbsRecMngInPeriodParamInput paramInput) {
		if(paramInput.isOverwriteFlg()
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.getUseRecMng().isEmpty()) {
			List<InterimRecMng> lstRecMngTmp = new ArrayList<>(lstRecMng);
			List<InterimRemain> lstInterimMngTmp = new ArrayList<>(lstInterimMngOfRec);
			for (InterimRecMng recMng : paramInput.getUseRecMng()) {
				List<InterimRemain> lstInputData = paramInput.getInterimMng().stream()
						.filter(x -> x.getRemainManaID() == recMng.getRemainManaID())
						.collect(Collectors.toList());
				if(!lstInputData.isEmpty()) {
					InterimRemain inputRemainData = lstInputData.get(0);
					//INPUT．上書き用の暫定管理データをドメインモデル「暫定振出管理データ」に追加する
					List<InterimRemain> lstRemainTmp = lstInterimMngTmp.stream()
						.filter(x -> x.getYmd() == inputRemainData.getYmd())
						.collect(Collectors.toList());				
					if(!lstRemainTmp.isEmpty()) {
						InterimRemain remainTmp = lstRemainTmp.get(0);
						lstInterimMngOfRec.remove(remainTmp);
						List<InterimRecMng> lstRecTmp = lstRecMngTmp.stream().filter(y -> y.getRemainManaID() == remainTmp.getRemainManaID())
								.collect(Collectors.toList());
						if(!lstRecTmp.isEmpty()) {
							InterimRecMng recMngTmp = lstRecTmp.get(0);
							lstRecMng.remove(recMngTmp);
						}
					}
					lstRecMng.add(recMng);
					lstInterimMngOfRec.add(inputRemainData);
				}
			}
		}
		List<AbsRecDetailPara> lstOutputOfRec = new ArrayList<>();
		LeaveSetOutput getSetForLeave = AbsenceTenProcess.getSetForLeave(require, cacheCarrier,
				paramInput.getCid(), paramInput.getSid(), paramInput.getBaseDate());
		//「暫定振出管理データ」
		for (InterimRecMng interimRecMng : lstRecMng) {
			InterimRemain remainData = lstInterimMngOfRec.stream().filter(x -> x.getRemainManaID().equals(interimRecMng.getRemainManaID()))
					.collect(Collectors.toList()).get(0);
			AbsRecDetailPara outputData = getUnUseDayOfRecInterim(require, cacheCarrier, interimRecMng, remainData, getSetForLeave, paramInput.getDateData().start(),
					paramInput.getBaseDate(), paramInput.getCid(), paramInput.getSid());
			if(outputData != null) {
				lstOutputOfRec.add(outputData);	
			}			
		}
		return lstOutputOfRec;
	}

	public static interface RequireM10 extends RequireM9, RequireM1, RequireM5 {
		
	}
	
	public static interface RequireM8 extends RequireM0, RequireM7 {

		Map<GeneralDate, DailyInterimRemainMngData> monthInterimRemainData(String cid, String sid, DatePeriod dateData);
	}
	
	public static interface RequireM6 extends ClosureService.RequireM3, RequireM7 {
		
		CompanyDto firstMonth(CacheCarrier cacheCarrier,String companyId);
	}
	
	public static interface RequireM7 {

		List<InterimRecAbsMng> interimRecAbsMng(String interimId, boolean isRec, DataManagementAtr mngAtr);
	}
	
	public static interface RequireM5 extends RequireM7, RequireM3, RequireM0 {

		Optional<InterimRecMng> interimRecMng(String recId);
	}
	
	public static interface RequireM0  {

		List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType);

		Optional<InterimAbsMng> interimAbsMng(String absId);
	}
	
	public static interface RequireM3 extends AbsenceTenProcess.RequireM3, RequireM6 {
		
	}
	
	public static interface RequireM2 extends ClosureService.RequireM3, NumberCompensatoryLeavePeriodQuery.Require  {
		
	}
	
	public static interface RequireM9 {
		
		List<PayoutManagementData> payoutManagementData(String cid, String sid, GeneralDate ymd, double unUse, DigestionAtr state);
	}
	
	public static interface RequireM1 {

		List<SubstitutionOfHDManagementData> substitutionOfHDManagementData(String cid, String sid, GeneralDate ymd, double unOffseDays);
	}
	
	public static RequireM10 createRequireM10(InterimRemainRepository interimRemainRepo,
			InterimRecAbasMngRepository interimRecAbasMngRepo, ClosureRepository closureRepo,
			ClosureEmploymentRepository closureEmploymentRepo, CompanyAdapter companyAdapter,
			ShareEmploymentAdapter shareEmploymentAdapter, EmpSubstVacationRepository empSubstVacationRepo,
			ComSubstVacationRepository comSubstVacationRepo, SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepo,
			PayoutManagementDataRepository payoutManagementDataRepo) {
		
		return new RequireM10() {
			
			@Override
			public List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType) {
				return interimRemainRepo.getRemainBySidPriod(employeeId, dateData, remainType);
			}
			
			@Override
			public Optional<InterimAbsMng> interimAbsMng(String absId) {
				return interimRecAbasMngRepo.getAbsById(absId);
			}
			
			@Override
			public Optional<Closure> closure(String companyId, int closureId) {
				return closureRepo.findById(companyId, closureId);
			}
			
			@Override
			public Optional<ClosureEmployment> employmentClosure(String companyID, String employmentCD) {
				return closureEmploymentRepo.findByEmploymentCD(companyID, employmentCD);
			}
			
			@Override
			public CompanyDto firstMonth(CacheCarrier cacheCarrier, String companyId) {
				return companyAdapter.getFirstMonthRequire(cacheCarrier, companyId);
			}
			
			@Override
			public Optional<BsEmploymentHistoryImport> employmentHistory(CacheCarrier cacheCarrier, String companyId,
					String employeeId, GeneralDate baseDate) {
				return shareEmploymentAdapter.findEmploymentHistoryRequire(cacheCarrier, companyId, employeeId, baseDate);
			}
			
			@Override
			public Optional<EmpSubstVacation> empSubstVacation(String companyId, String contractTypeCode) {
				return empSubstVacationRepo.findById(companyId, contractTypeCode);
			}
			
			@Override
			public Optional<ComSubstVacation> comSubstVacation(String companyId) {
				return comSubstVacationRepo.findById(companyId);
			}
			
			@Override
			public List<InterimRecAbsMng> interimRecAbsMng(String interimId, boolean isRec, DataManagementAtr mngAtr) {
				return interimRecAbasMngRepo.getRecOrAbsMng(interimId, isRec, mngAtr);
			}
			
			@Override
			public Optional<InterimRecMng> interimRecMng(String recId) {
				return interimRecAbasMngRepo.getReruitmentById(recId);
			}
			
			@Override
			public List<SubstitutionOfHDManagementData> substitutionOfHDManagementData(String cid, String sid, GeneralDate ymd,
					double unOffseDays) {
				return substitutionOfHDManaDataRepo.getByYmdUnOffset(cid, sid, ymd, unOffseDays);
			}
			
			@Override
			public List<PayoutManagementData> payoutManagementData(String cid, String sid, GeneralDate ymd, double unUse,
					DigestionAtr state) {
				return payoutManagementDataRepo.getByUnUseState(cid, sid, ymd, unUse, state);
			}
		};
	}
}