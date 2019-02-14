package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.CompensatoryDayoffDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
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
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AbsenceReruitmentMngInPeriodQueryImpl implements AbsenceReruitmentMngInPeriodQuery{
	@Inject
	private SubstitutionOfHDManaDataRepository confirmAbsMngRepo;
	@Inject
	private InterimRecAbasMngRepository recAbsRepo;
	@Inject
	private PayoutManagementDataRepository confirmRecRepo;
	@Inject
	private InterimRemainRepository interimRepo;
	@Inject
	private InterimRemainOffMonthProcess createDataService;
	@Inject
	private ClosureService closureService;
	@Inject
	private AbsenceTenProcess absenceTenProcess;
	@Inject
	private CompanyAdapter companyAdapter;
	@Override
	public AbsRecRemainMngOfInPeriod getAbsRecMngInPeriod(AbsRecMngInPeriodParamInput paramInput) {
		//アルゴリズム「未相殺の振休(確定)を取得する」を実行する
		List<AbsRecDetailPara> lstAbsRec = this.getAbsOfUnOffset(paramInput.getCid(), paramInput.getSid(), paramInput.getDateData().start());		
		//アルゴリズム「未使用の振出(確定)を取得する」を実行する
		lstAbsRec = this.getUnUseDaysConfirmRec(paramInput.getCid(), paramInput.getSid(), lstAbsRec, paramInput.getDateData().start());
		//繰越数を計算する
		ResultAndError carryForwardDays = this.calcCarryForwardDays(paramInput.getDateData().start(), lstAbsRec);		
		//アルゴリズム「未相殺の振休(暫定)を取得する」を実行する
		//アルゴリズム「未使用の振出(暫定)を取得する」を実行する
		lstAbsRec = this.lstInterimInfor(paramInput, lstAbsRec);
		//「振出振休明細」をソートする 
		lstAbsRec = lstAbsRec.stream().sorted((a, b) -> a.getYmdData().getDayoffDate().isPresent() ? 
				a.getYmdData().getDayoffDate().get().compareTo(b.getYmdData().getDayoffDate().isPresent() ? b.getYmdData().getDayoffDate().get() 
						: GeneralDate.max())
				: GeneralDate.max().compareTo(GeneralDate.max())).collect(Collectors.toList());
		//アルゴリズム「時系列順で相殺する」を実行する
		lstAbsRec = this.offsetSortTimes(lstAbsRec);		
		//消化区分と消滅日を計算する
		lstAbsRec = this.calDigestionAtr(lstAbsRec, paramInput.getBaseDate());
		//残数と未消化を集計する
		AbsDaysRemain remainUnDigestedDays = this.getRemainUnDigestedDays(lstAbsRec, paramInput.getBaseDate());
		//発生数・使用数を計算する
		AbsDaysRemain occurrenceUseDays= this.getOccurrenceUseDays(lstAbsRec, paramInput.getDateData());
		List<PauseError> lstError = new ArrayList<>();
		if(remainUnDigestedDays.getRemainDays() < 0) {
			lstError.add(PauseError.PAUSEREMAINNUMBER);
		}
		if(remainUnDigestedDays.isErrors()) {
			lstError.add(PauseError.OFFSETNUMBER);
		}
		AbsRecRemainMngOfInPeriod outputData = new AbsRecRemainMngOfInPeriod(lstAbsRec,
				remainUnDigestedDays.getRemainDays(), 
				remainUnDigestedDays.getUnDigestedDays(),
				occurrenceUseDays.getRemainDays(),
				occurrenceUseDays.getUnDigestedDays(),
				carryForwardDays.getRerultDays(),
				lstError);
		return outputData;
	}

	@Override
	public List<AbsRecDetailPara> getAbsOfUnOffset(String cid, String sid, GeneralDate ymd) {
		List<AbsRecDetailPara> lstOutput = new ArrayList<>();
		//アルゴリズム「確定振休から未相殺の振休を取得する」を実行する
		List<SubstitutionOfHDManagementData> lstUnOffsetDays = confirmAbsMngRepo.getByYmdUnOffset(cid, sid, ymd, 0);
		if(lstUnOffsetDays.isEmpty()) {
			return lstOutput;
		}
		//未相殺のドメインモデル「振休管理データ」(Output)の件数をチェックする
		for (SubstitutionOfHDManagementData x : lstUnOffsetDays) {
			//アルゴリズム「暫定振出と紐付けをしない確定振休を取得する」を実行する
			AbsRecDetailPara output = this.getInterimAndConfirmAbs(sid, x);
			if(output != null) {
				lstOutput.add(output);	
			}			
		}		
		return lstOutput;
	}
	/*@Override
	public List<SubstitutionOfHDManagementData> getAbsOfUnOffsetFromConfirm(String cid, String sid, GeneralDate ymd) {
		// ドメインモデル「振休管理データ」
		List<SubstitutionOfHDManagementData> lstAbsConfirmData = confirmAbsMngRepo.getBysiD(cid, sid);
		List<SubstitutionOfHDManagementData> lstOutput = new ArrayList<>();
		for (SubstitutionOfHDManagementData x : lstAbsConfirmData) {
			if(x.getHolidayDate().getDayoffDate().isPresent() && x.getHolidayDate().getDayoffDate().get().afterOrEquals(ymd)) {
				continue;
			}
			lstOutput.add(x);
		}
		return lstOutput;
	}	*/

	@Override
	public AbsRecDetailPara getInterimAndConfirmAbs(String sid, SubstitutionOfHDManagementData confirmAbsData) {
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

	@Override
	public List<AbsRecDetailPara> getUnUseDaysConfirmRec(String cid, String sid, List<AbsRecDetailPara> lstDataDetail, GeneralDate ymd) {
		//2-1.確定振出から未使用の振出を取得する
		List<PayoutManagementData> lstConfirmRec = confirmRecRepo.getByUnUseState(cid, sid, ymd, 0, DigestionAtr.UNUSED);
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

	@Override
	public ResultAndError calcCarryForwardDays(GeneralDate startDate, List<AbsRecDetailPara> lstDataDetail) {
		// アルゴリズム「6.残数と未消化を集計する」を実行
		AbsDaysRemain outPut = this.getRemainUnDigestedDays(lstDataDetail, startDate);
		return new ResultAndError(outPut.getRemainDays(), outPut.isErrors());
	}

	@Override
	public AbsDaysRemain getRemainUnDigestedDays(List<AbsRecDetailPara> lstDataDetail, GeneralDate baseDate) {
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
				if(recData.getExpirationDate().before(baseDate)) {
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

	@Override
	public List<AbsRecDetailPara> getUnOffsetDaysAbsInterim(AbsRecMngInPeriodParamInput paramInput) {
		List<InterimAbsMng> lstAbsMng = new ArrayList<>();
		List<InterimRemain> lstInterimMng = new ArrayList<>();
		List<AbsRecDetailPara> lstOutput = new ArrayList<>();
		//INPUT．モードをチェックする
		if(paramInput.isMode()) {
			//暫定残数管理データを作成する
			Map<GeneralDate, DailyInterimRemainMngData> interimData = createDataService.monthInterimRemainData(paramInput.getCid(), paramInput.getSid(), paramInput.getDateData());
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
			lstInterimMng =  interimRepo.getRemainBySidPriod(paramInput.getSid(), paramInput.getDateData(), RemainType.PAUSE);
			
			lstInterimMng.stream().forEach(x -> {
				Optional<InterimAbsMng> optAbsMng = recAbsRepo.getAbsById(x.getRemainManaID());
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
						filter(x -> x.getRemainManaID() == absMng.getAbsenceMngId())
						.collect(Collectors.toList());			
				if(!lstInputData.isEmpty()) {
					InterimRemain inputData = lstInputData.get(0);
					List<InterimRemain> lstMngTmp = lstInterimMngTmp.stream()
							.filter(x -> x.getYmd().equals(inputData.getYmd()))
							.collect(Collectors.toList());
					if(!lstMngTmp.isEmpty()) {
						InterimRemain interimMngTmp = lstMngTmp.get(0);					
						List<InterimAbsMng> lstAbsTmp = lstAbsMngTmp.stream().filter(x -> x.getAbsenceMngId().equals(interimMngTmp.getRemainManaID()))
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
			InterimRemain remainData = lstInterimMng.stream().filter(x -> x.getRemainManaID().equals(interimAbsMng.getAbsenceMngId()))
					.collect(Collectors.toList()).get(0);
			//アルゴリズム「振出と紐付けをしない振休を取得する」を実行する
			AbsRecDetailPara outputData = this.getNotTypeRec(interimAbsMng, remainData);
			lstOutput.add(outputData);
		}
		return lstOutput;
	}

	@Override
	public AbsRecDetailPara getNotTypeRec(InterimAbsMng absMng, InterimRemain remainData) {
		//ドメインモデル「暫定振出振休紐付け管理」を取得する
		List<InterimRecAbsMng> lstInterimMng = recAbsRepo.getRecOrAbsMng(absMng.getAbsenceMngId(), false, DataManagementAtr.INTERIM);
		double unOffsetDays = absMng.getRequeiredDays().v();
		if(!lstInterimMng.isEmpty()) {
			for (InterimRecAbsMng recAbsData : lstInterimMng) {
				unOffsetDays -= recAbsData.getUseDays().v();
			}			
		}
		UnOffsetOfAbs unOffset = new UnOffsetOfAbs(absMng.getAbsenceMngId(),
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

	@Override
	public AbsRecDetailPara getUnUseDayOfRecInterim(InterimRecMng interimRecMng, InterimRemain remainData,LeaveSetOutput getSetForLeave,
			GeneralDate startDate, GeneralDate baseDate, String cid, String sid) {
		//ドメインモデル「暫定振出振休紐付け管理」を取得する
		List<InterimRecAbsMng> lstInterimMng = recAbsRepo.getRecOrAbsMng(interimRecMng.getRecruitmentMngId(), true, DataManagementAtr.INTERIM);
		//未使用日数←SELF.発生日数
		double unUseDays = interimRecMng.getOccurrenceDays().v();
		if(!lstInterimMng.isEmpty()) {
			for (InterimRecAbsMng interimMng : lstInterimMng) {
				//未使用日数：INPUT.暫定振出管理データ.発生日数－合計(暫定振出振休紐付け管理.使用日数)
				unUseDays -= interimMng.getUseDays().v();
			}
		}
		UnUseOfRec unUseInfo = new UnUseOfRec(interimRecMng.getExpirationDate(),
				interimRecMng.getRecruitmentMngId(),
				interimRecMng.getOccurrenceDays().v(),
				interimRecMng.getStatutoryAtr(),
				unUseDays,
				DigestionAtr.USED,
				Optional.empty(),
				Optional.empty());
		//INPUT．振休使用期限をチェックする //ExpirationTime 0: "当月", 2: "年度末クリア"
		if(getSetForLeave.getExpirationOfLeave() == 0 ||getSetForLeave.getExpirationOfLeave() == 2) {
			//社員に対応する処理締めを取得する
			Closure period = closureService.getClosureDataByEmployee(sid, baseDate);
			if(period == null) {
				return null;
			}
			//指定した年月日時点の締め期間を取得する
			Optional<ClosurePeriod> optClosurePeriod = period.getClosurePeriodByYmd(remainData.getYmd());			
			if(getSetForLeave.getExpirationOfLeave() == 0) {
				unUseInfo.setStartDate(optClosurePeriod.isPresent() ? Optional.ofNullable(optClosurePeriod.get().getPeriod().start()) : Optional.empty());
			} else {
				//会社の期首月を取得する
				CompanyDto compInfor = companyAdapter.getFirstMonth(cid);
				
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

	@Override
	public List<AbsRecDetailPara> offsetSortTimes(List<AbsRecDetailPara> lstDetailData) {
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

	@Override
	public AbsDaysRemain getOccurrenceUseDays(List<AbsRecDetailPara> lstDetailData, DatePeriod dateData) {
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

	@Override
	public List<AbsRecDetailPara> calDigestionAtr(List<AbsRecDetailPara> lstDetailData, GeneralDate baseDate) {
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

	@Override
	public List<AbsRecDetailPara> lstInterimInfor(AbsRecMngInPeriodParamInput paramInput, List<AbsRecDetailPara> lstAbsRec) {
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
								.filter(y -> y.getAbsenceMngId().equals(x.getRemainManaID()))
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
								.filter(b -> b.getRecruitmentMngId().equals(a.getRemainManaID()))
								.collect(Collectors.toList());
						lstRecMng.addAll(temp);
					});
					
				//}				
			//}
		} else {
			//ドメインモデル「暫定振休管理データ」を取得する
			lstInterimMngOfAbs =  interimRepo.getRemainBySidPriod(paramInput.getSid(), paramInput.getDateData(), RemainType.PAUSE);
			for (InterimRemain x : lstInterimMngOfAbs) {
				Optional<InterimAbsMng> optAbsMng = recAbsRepo.getAbsById(x.getRemainManaID());
				if(optAbsMng.isPresent()) {
					lstAbsMng.add(optAbsMng.get());
				}
			}
			//ドメインモデル「暫定振出管理データ」を取得する
			lstInterimMngOfRec =  interimRepo.getRemainBySidPriod(paramInput.getSid(), paramInput.getDateData(), RemainType.PICKINGUP);
			for (InterimRemain x : lstInterimMngOfRec) {
				Optional<InterimRecMng> optRecMng = recAbsRepo.getReruitmentById(x.getRemainManaID());
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
					List<InterimAbsMng> tmpAbsUsen = lstAbsUsen.stream().filter(b -> b.getAbsenceMngId().equals(temp.getRemainManaID()))
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
					List<InterimRecMng> tempLstRec = lstRecMngUsen.stream().filter(b -> b.getRecruitmentMngId().equals(temp.getRemainManaID()))
							.collect(Collectors.toList());
					if(!tempLstRec.isEmpty()) {
						lstRecMng.remove(tempLstRec.get(0));
					}
				}
			}
		}
		//20181003 DuDT fix bug 101491 ↑
		List<AbsRecDetailPara> lstOutputOfAbs = this.lstOutputOfAbs(lstAbsMng, lstInterimMngOfAbs, paramInput);
		List<AbsRecDetailPara> lstOutputOfRec = this.lstOutputOfRec(lstRecMng, lstInterimMngOfRec, paramInput);
		lstAbsRec.addAll(lstOutputOfAbs);
		lstAbsRec.addAll(lstOutputOfRec);
		return lstAbsRec;
	}
	/**
	 * 暫定振休管理データ: 上書きフラグをチェックする
	 * @param lstAbsMng
	 * @param lstInterimMngOfAbs
	 * @param paramInput
	 * @return
	 */
	private List<AbsRecDetailPara> lstOutputOfAbs(List<InterimAbsMng> lstAbsMng, List<InterimRemain> lstInterimMngOfAbs, AbsRecMngInPeriodParamInput paramInput) {
		if(paramInput.isOverwriteFlg()
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.getUseAbsMng().isEmpty()) {
			List<InterimAbsMng> lstAbsMngTmp = new ArrayList<>(lstAbsMng);
			List<InterimRemain> lstInterimMngTmp = new ArrayList<>(lstInterimMngOfAbs);
			for (InterimAbsMng absMng : paramInput.getUseAbsMng()) {
				//INPUT．上書き用の暫定管理データをドメインモデル「暫定振休管理データ」に追加する
				List<InterimRemain> lstInputData = paramInput.getInterimMng().stream().
						filter(x -> x.getRemainManaID() == absMng.getAbsenceMngId())
						.collect(Collectors.toList());			
				if(!lstInputData.isEmpty()) {
					InterimRemain inputData = lstInputData.get(0);
					List<InterimRemain> lstMngTmp = lstInterimMngTmp.stream()
							.filter(x -> x.getYmd().equals(inputData.getYmd()))
							.collect(Collectors.toList());					
					if(!lstMngTmp.isEmpty()) {
						InterimRemain interimMngTmp = lstMngTmp.get(0);
						lstInterimMngOfAbs.remove(interimMngTmp);
						List<InterimAbsMng> lstAbsTmp = lstAbsMngTmp.stream().filter(x -> x.getAbsenceMngId().equals(interimMngTmp.getRemainManaID()))
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
			List<InterimRemain> lstRemainData = lstInterimMngOfAbs.stream().filter(x -> x.getRemainManaID().equals(interimAbsMng.getAbsenceMngId()))
					.collect(Collectors.toList());
			if(lstRemainData.isEmpty()) {
				continue;
			}
			InterimRemain remainData = lstRemainData.get(0);
			//アルゴリズム「振出と紐付けをしない振休を取得する」を実行する
			AbsRecDetailPara outputData = this.getNotTypeRec(interimAbsMng, remainData);
			lstOutputOfAbs.add(outputData);
		}
		return lstOutputOfAbs;
	}
	/**
	 * 暫定振出管理データ: 上書きフラグをチェックする
	 * @param lstRecMng
	 * @param lstInterimMngOfRec
	 * @param paramInput
	 * @return
	 */
	private List<AbsRecDetailPara> lstOutputOfRec(List<InterimRecMng> lstRecMng, List<InterimRemain> lstInterimMngOfRec, AbsRecMngInPeriodParamInput paramInput) {
		if(paramInput.isOverwriteFlg()
				&& !paramInput.getInterimMng().isEmpty()
				&& !paramInput.getUseRecMng().isEmpty()) {
			List<InterimRecMng> lstRecMngTmp = new ArrayList<>(lstRecMng);
			List<InterimRemain> lstInterimMngTmp = new ArrayList<>(lstInterimMngOfRec);
			for (InterimRecMng recMng : paramInput.getUseRecMng()) {
				List<InterimRemain> lstInputData = paramInput.getInterimMng().stream()
						.filter(x -> x.getRemainManaID() == recMng.getRecruitmentMngId())
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
						List<InterimRecMng> lstRecTmp = lstRecMngTmp.stream().filter(y -> y.getRecruitmentMngId() == remainTmp.getRemainManaID())
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
		LeaveSetOutput getSetForLeave = absenceTenProcess.getSetForLeave(paramInput.getCid(), paramInput.getSid(), paramInput.getBaseDate());
		//「暫定振出管理データ」
		for (InterimRecMng interimRecMng : lstRecMng) {
			InterimRemain remainData = lstInterimMngOfRec.stream().filter(x -> x.getRemainManaID().equals(interimRecMng.getRecruitmentMngId()))
					.collect(Collectors.toList()).get(0);
			AbsRecDetailPara outputData = this.getUnUseDayOfRecInterim(interimRecMng, remainData, getSetForLeave, paramInput.getDateData().start(),
					paramInput.getBaseDate(), paramInput.getCid(), paramInput.getSid());
			if(outputData != null) {
				lstOutputOfRec.add(outputData);	
			}			
		}
		return lstOutputOfRec;
	}

	@Override
	public double getAbsRecMngRemain(String employeeID, GeneralDate date) {
		String companyID = AppContexts.user().companyId();
		//社員に対応する締め期間を取得する
		DatePeriod period = closureService.findClosurePeriod(employeeID, date);
		AbsRecMngInPeriodParamInput paramInput = new AbsRecMngInPeriodParamInput(
				companyID, //・ログイン会社ID
				employeeID, //・INPUT．社員ID
				new DatePeriod(period.start(), period.start().addYears(1).addDays(-1)), //・集計開始日＝締め期間．開始年月日 - ・集計終了日＝締め期間．開始年月日＋１年－１日
				date, //・基準日＝INPUT．基準日
				false, //・モード＝その他モード
				false, //・上書きフラグ=false
				Collections.emptyList(), //上書き用の暫定管理データ：なし
				Collections.emptyList(), 
				Collections.emptyList());
		return this.getAbsRecMngInPeriod(paramInput).getRemainDays();
	}
	
}
