package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonthDayHolder.Difference;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffComHistItemShareImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffCompanyHistSharedImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.SClsHistImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveAppSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.AgeBaseYear;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.SpecialLeaveRestriction;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.ElapseYear;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDate;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTbl;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTblRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantTime;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.TypeTime;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.AvailabilityPeriod;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.SpecialVacationDeadline;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.TimeLimitSpecification;
import nts.uk.shr.com.time.calendar.MonthDay;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class InforSpecialLeaveOfEmployeeSeviceImpl implements InforSpecialLeaveOfEmployeeSevice{
	@Inject
	private SpecialLeaveBasicInfoRepository leaveBasicInfoRepo;
	@Inject
	private SpecialHolidayRepository holidayRepo;
	@Inject
	private EmpEmployeeAdapter sysCompanyAdapter;
	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepository;
	@Inject
	private ShareEmploymentAdapter sysEmploymentHist;
	@Inject
	private GrantDateTblRepository grantTableRepos;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public InforSpecialLeaveOfEmployee getInforSpecialLeaveOfEmployee(String cid, String sid, int specialLeaveCode,
			DatePeriod complileDate,SpecialHoliday specialHoliday) {
		InforSpecialLeaveOfEmployee outputData = new InforSpecialLeaveOfEmployee(InforStatus.NOTUSE, Optional.empty(), new ArrayList<>(), false);
		//ドメインモデル「特別休暇基本情報」を取得する
		Optional<SpecialLeaveBasicInfo> optBasicInfor = leaveBasicInfoRepo.getBySidLeaveCdUser(sid, specialLeaveCode, UseAtr.USE);
		if(!optBasicInfor.isPresent()) {
			return outputData;
		}
		if(specialHoliday == null) {
			//ドメインモデル「特別休暇」を取得する
			Optional<SpecialHoliday> optSpecialHoliday = holidayRepo.findBySingleCD(cid, specialLeaveCode);
			if(!optSpecialHoliday.isPresent()) {
				return outputData;
			}
			specialHoliday = optSpecialHoliday.get();	
		}
			
		SpecialLeaveBasicInfo leaverBasicInfo = optBasicInfor.get();
		//付与日数情報を取得する
		GrantDaysInforByDates grantDayInfors = this.getGrantDays(cid, sid, complileDate, specialHoliday, leaverBasicInfo);
		//「付与日数一覧」の件数をチェックする
		Optional<Integer> upLimiDays = specialHoliday.getGrantPeriodic().getLimitCarryoverDays() == null 
				|| specialHoliday.getGrantPeriodic().getLimitCarryoverDays().v() == null ? Optional.empty() 
				: Optional.of(specialHoliday.getGrantPeriodic().getLimitCarryoverDays().v());
		if(grantDayInfors == null || grantDayInfors.getLstGrantDaysInfor().isEmpty()) {
			//状態：「付与なし」を返す
			return new InforSpecialLeaveOfEmployee(InforStatus.NOTGRANT, upLimiDays,
					new ArrayList<>(), specialHoliday.getGrantRegular().isAllowDisappear());
		} else {
			//期限を取得する
			List<SpecialHolidayInfor> getDeadlineInfo = this.getDeadlineInfo(grantDayInfors, specialHoliday);
			
			return new InforSpecialLeaveOfEmployee(InforStatus.GRANTED, upLimiDays,
					getDeadlineInfo, specialHoliday.getGrantRegular().isAllowDisappear());
		}
	}
	@Override
	public GrantDaysInforByDates getGrantDays(String cid, String employeeId, DatePeriod period, SpecialHoliday speHoliday,
			SpecialLeaveBasicInfo leaveBasicInfo) {
		GeneralDate grantDate = GeneralDate.today();
		//取得しているドメインモデル「特別休暇．付与情報．付与基準日」をチェックする
		if(speHoliday.getGrantRegular().getGrantDate() == GrantDate.EMP_GRANT_DATE) {
			//社員ID（List）と指定期間から所属会社履歴項目を取得
			List<String> lstEmp = new ArrayList<>();
			lstEmp.add(employeeId);
			List<AffCompanyHistSharedImport> getAffCompanyHistByEmployee = sysCompanyAdapter.getAffCompanyHistByEmployee(lstEmp, period);
			for (AffCompanyHistSharedImport affCompanyHistSharedImport : getAffCompanyHistByEmployee) {
				List<AffComHistItemShareImport> lstAffComHistItem = affCompanyHistSharedImport.getLstAffComHistItem();
				for (AffComHistItemShareImport affComHistItemShareImport : lstAffComHistItem) {
					grantDate = affComHistItemShareImport.getDatePeriod().start();
					break;
				}
			}
		} else if (speHoliday.getGrantRegular().getGrantDate() == GrantDate.GRANT_BASE_HOLIDAY){
			//ドメインモデル「年休社員基本情報」を取得する
			Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfo = annLeaEmpBasicInfoRepository.get(employeeId);
			if(!annualLeaveEmpBasicInfo.isPresent()) {
				return new GrantDaysInforByDates(grantDate, Collections.emptyList());
			}
			//所得したドメインモデル「年休社員基本情報．付与ルール．付与基準日」をパラメータ「付与基準日」にセットする
			grantDate = annualLeaveEmpBasicInfo.get().getGrantRule().getGrantStandardDate();
						
		} else {
			//取得している「特別休暇基本情報．付与設定．付与基準日」をパラメータ「付与基準日」にセットする
			grantDate = leaveBasicInfo.getGrantSetting().getGrantDate();
		}
		//取得している「特別休暇．付与情報．付与するタイミングの種類」をチェックする
		if(speHoliday.getGrantRegular().getTypeTime() == TypeTime.GRANT_START_DATE_SPECIFY) {
			//固定の付与日一覧を求める
			return this.askGrantDays(cid, employeeId, period, grantDate, speHoliday, leaveBasicInfo);
		} else {
			//テーブルに基づいた付与日数一覧を求める
			return this.askGrantdaysFromtable(cid, employeeId, period, grantDate, leaveBasicInfo, speHoliday);
		}
	}
	@Override
	public GrantDaysInforByDates askGrantDays(String cid, String sid, DatePeriod period, GeneralDate grantDate, SpecialHoliday speHoliday, 
			SpecialLeaveBasicInfo leaveBasicInfo) {
		List<GrantDaysInfor> lstOutput = new ArrayList<>();
		//パラメータ「付与基準日」をパラメータ「比較年月日」にセットする
		GrantTime grantTime = speHoliday.getGrantRegular().getGrantTime();
		//取得している「特別休暇．付与情報．固定付与日．周期」
		int interval = grantTime.getFixGrantDate().getInterval().v();
		//取得している「特別休暇．付与情報．固定付与日．周期」をチェックする
		if(interval == 0) {
			return null;
		}
		double grantDays = 0;
		//　◆特別休暇基本情報．適用設定≠所定の条件を適用する　の場合
		//　　付与日数　←　特別休暇基本情報．付与設定．付与日数
		if(leaveBasicInfo.getApplicationSet() != SpecialLeaveAppSetting.PRESCRIBED) {
			grantDays = leaveBasicInfo.getGrantSetting().getGrantDays().isPresent() && leaveBasicInfo.getGrantSetting().getGrantDays().get().v() != null 
					? leaveBasicInfo.getGrantSetting().getGrantDays().get().v() : 0;
		}
		//　◆特別休暇基本情報．適用設定＝所定の条件を適用する　の場合
		// 　　付与日数　←　ドメインモデル「特別休暇．付与情報．固定付与日．固定付与日数」
		
		else {
			grantDays = speHoliday.getGrantRegular().getGrantTime().getFixGrantDate().getGrantDays().v();
		}
		GrantDaysInforByDates outputData = new GrantDaysInforByDates(grantDate,new ArrayList<>());
		//パラメータ「期間」に一致する付与日数を生成する
		for(GeneralDate loopDate = grantDate; loopDate.beforeOrEquals(period.end());) {
			//パラメータ「比較年月日」とパラメータ「期間」を比較する
			if(period.start().beforeOrEquals(loopDate)
					&& loopDate.beforeOrEquals(period.end())) {//「期間．開始日」≦「比較年月日」≦「期間．終了日」
				//利用条件をチェックする
				ErrorFlg checkUser = this.checkUse(cid, sid, loopDate, speHoliday);
				if(checkUser.isAgeError()
						|| checkUser.isClassError()
						|| checkUser.isEmploymentError()
						|| checkUser.isGenderError()) {
					//パラメータ「付与日数一覧」を追加する
					GrantDaysInfor outPut = new GrantDaysInfor(loopDate, Optional.of(checkUser), 0);
					lstOutput.add(outPut);
				} else {
					GrantDaysInfor outPut = new GrantDaysInfor(loopDate, Optional.empty(), grantDays);
					lstOutput.add(outPut);
				}
			}
			loopDate = loopDate.addYears(interval);
			outputData.setGrantDate(loopDate);
		}
		outputData.setLstGrantDaysInfor(lstOutput);
		return outputData;
	}
	@Override
	public ErrorFlg checkUse(String cid, String sid, GeneralDate baseDate, SpecialHoliday speHoliday) {
		ErrorFlg outData = new ErrorFlg(false, false, false, false);
		SpecialLeaveRestriction specialLeaveRestric = speHoliday.getSpecialLeaveRestriction();
		//Imported(就業)「社員」を取得する
		EmployeeRecordImport empInfor = sysCompanyAdapter.findByAllInforEmpId(sid);
		//取得しているドメインモデル「定期付与．特別休暇利用条件．性別条件」をチェックする
		if(specialLeaveRestric.getGenderRest() == nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.UseAtr.USE) {
			//性別が一致するかチェックする
			if(empInfor.getGender() != specialLeaveRestric.getGender().value) {
				//パラメータ「エラーフラグ．性別条件に一致しない」にTRUEをセットする
				outData.setGenderError(true);				
			}
		}
		//取得しているドメインモデル「定期付与．特別休暇利用条件．雇用条件」をチェックする
		if(specialLeaveRestric.getRestEmp() == nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.UseAtr.USE) {
			//アルゴリズム「社員所属雇用履歴を取得」を実行する
			Optional<BsEmploymentHistoryImport> findEmploymentHistory = sysEmploymentHist.findEmploymentHistory(cid, sid, baseDate);
			if(!findEmploymentHistory.isPresent()) {
				outData.setEmploymentError(true);
			} else {
				BsEmploymentHistoryImport lstEmployment = findEmploymentHistory.get();
			
				List<String> listEmp = speHoliday.getSpecialLeaveRestriction().getListEmp();
				if(listEmp != null && !listEmp.isEmpty()) {
					boolean isExit = false;
					if(listEmp.contains(lstEmployment.getEmploymentCode())) {
						isExit = true;
					}
					if(!isExit) {
						outData.setEmploymentError(true);
					}	
				}					
			}
		}
		//ドメインモデル「特別休暇利用条件」．分類条件をチェックする
		if(specialLeaveRestric.getRestrictionCls() == nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.UseAtr.USE) {
			//アルゴリズム「社員所属分類履歴を取得」を実行する
			List<String> lstSids = new ArrayList<>();
			lstSids.add(sid);
			List<SClsHistImport> lstClass = sysCompanyAdapter.lstClassByEmployeeId(cid, lstSids, new DatePeriod(baseDate, baseDate));			
			if(lstClass.isEmpty()) {
				outData.setClassError(true);
			}
			//取得した分類コードが取得しているドメインモデル「定期付与．特別休暇利用条件．分類一覧」に存在するかチェックする
			List<String> listClassSpe = speHoliday.getSpecialLeaveRestriction().getListCls();
			if(listClassSpe != null && !listClassSpe.isEmpty()) {
				boolean isExit = false;
				for (SClsHistImport classData : lstClass) {
					if(listClassSpe.contains(classData.getClassificationCode())) {
						isExit = true;
						break;
					}
				}
				if(!isExit) {
					outData.setClassError(true);
				}	
			}
			
		}
		//ドメインモデル「特別休暇利用条件」．年齢条件をチェックする
		if(specialLeaveRestric.getAgeLimit() == nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.UseAtr.USE) {
			GeneralDate ageBase = baseDate;
			//年齢基準日を求める
			MonthDay ageBaseDate = specialLeaveRestric.getAgeStandard().getAgeBaseDate();
			int year = 0;
			if(specialLeaveRestric.getAgeStandard().getAgeCriteriaCls() == AgeBaseYear.THIS_YEAR) {
				//年齢基準日 = パラメータ「基準日．年」 + ドメインモデル「定期付与．特別休暇利用条件．年齢基準．年齢基準日」
				year = ageBaseDate != null ? ageBase.year() : 0;
			} else {
				//年齢基準日 = パラメータ「基準日．年」 の翌年 + ドメインモデル「定期付与．特別休暇利用条件．年齢基準．年齢基準日」
				year = ageBaseDate != null ? ageBase.year() + 1 : 0;
			}
			if(year != 0
					&& ageBaseDate.getMonth() != 0
					&& ageBaseDate.getDay() != 0) {
				ageBase = GeneralDate.ymd(year, ageBaseDate.getMonth(), ageBaseDate.getDay());
			}
			//求めた「年齢基準日」時点の年齢を求める
			Difference difYMD = ageBase.differenceFrom(empInfor.getBirthDay());
			//求めた「年齢」が年齢条件に一致するかチェックする
			if(specialLeaveRestric.getAgeRange().getAgeLowerLimit().v() > difYMD.years()
					|| specialLeaveRestric.getAgeRange().getAgeHigherLimit().v() < difYMD.years()) {
				outData.setAgeError(true);
			}
		}
		
		return outData;
	}
	@Override
	public GrantDaysInforByDates askGrantdaysFromtable(String cid, String sid, DatePeriod period, GeneralDate granDate,
			SpecialLeaveBasicInfo basicInfor, SpecialHoliday speHoliday) {
		List<GrantDaysInfor> lstOutput = new ArrayList<>();
		Optional<GrantDateTbl> optGranDateTbl = Optional.empty();
		GeneralDate outputDate = null;
		List<ElapseYear> elapseYear = new ArrayList<>();
		//◆特別休暇基本情報．適用設定≠所定の条件を適用する　の場合
		//取得している「特別休暇基本情報．付与設定．付与テーブルコード」　
		if(basicInfor.getApplicationSet() != SpecialLeaveAppSetting.PRESCRIBED 
				&& basicInfor.getGrantSetting().getGrantTable().isPresent()) {
			elapseYear = grantTableRepos.findElapseByGrantDateCd(cid, speHoliday.getSpecialHolidayCode().v(),
					basicInfor.getGrantSetting().getGrantTable().get().v());
		}
		//◆特別休暇基本情報．適用設定＝所定の条件を適用する　の場合
		//規定のテーブルとする＝TRUE
		else {
			optGranDateTbl = grantTableRepos.findByCodeAndIsSpecified(cid, basicInfor.getSpecialLeaveCode().v());
			
			if(optGranDateTbl.isPresent()) {				
				elapseYear = grantTableRepos.findElapseByGrantDateCd(cid, basicInfor.getSpecialLeaveCode().v(), optGranDateTbl.get().getGrantDateCode().v());
			}
		}
		//※処理中の「特別休暇付与テーブル．経過年数に対する付与日数．経過年数」を次へ更新
		if(elapseYear.isEmpty()) {
			return new GrantDaysInforByDates(outputDate, lstOutput);
		}
		int lastYear = granDate.year();
		for (ElapseYear yearData : elapseYear) {
			//パラメータ「比較年月日」に取得したドメインモデル「特別休暇付与テーブル．経過年数に対する付与日数．経過年数」を加算する
			GeneralDate granDateTmp = granDate.addYears(yearData.getYears().v());
			granDateTmp = granDateTmp.addMonths(yearData.getMonths().v());
			outputDate = granDateTmp;
			//最後の経過年数 = 処理中の「特別休暇付与テーブル．経過年数．年数
			lastYear = granDateTmp.year();
			//パラメータ「比較年月日」とパラメータ「期間」を比較する
			if(period.start().beforeOrEquals(granDateTmp)
					&& period.end().afterOrEquals(granDateTmp)) {
				//利用条件をチェックする
				ErrorFlg errorFlg = this.checkUse(cid, sid, granDateTmp, speHoliday);
				if(errorFlg.isAgeError()
						|| errorFlg.isClassError()
						|| errorFlg.isEmploymentError()
						|| errorFlg.isGenderError()) {
					GrantDaysInfor output = new GrantDaysInfor(granDateTmp, Optional.of(errorFlg), 0);
					lstOutput.add(output);
				} else {
					GrantDaysInfor output = new GrantDaysInfor(granDateTmp, Optional.empty(), yearData.getGrantedDays().v());
					lstOutput.add(output);
				}
			} else if(period.end().before(granDateTmp)) {
				break;
			}
		}
		//パラメータ「付与日数一覧」の件数= 0 AND
		//ドメインモデル「特別休暇付与テーブル」．テーブル以降の固定付与をおこなうが「チェックある」
		if(lstOutput.isEmpty() 
				&& (optGranDateTbl.isPresent() && optGranDateTbl.get().isSpecified())) {
			GeneralDate grantDateNext = GeneralDate.ymd(lastYear + 1, granDate.month(), granDate.day());
			for(GeneralDate loopDate = grantDateNext; loopDate.beforeOrEquals(period.end());) {
				//「期間．開始日」≦「比較年月日」≦「期間．終了日」
				if(period.start().beforeOrEquals(loopDate)
						&& loopDate.beforeOrEquals(period.end())) {
					//利用条件をチェックする
					ErrorFlg errorFlg = this.checkUse(cid, sid, loopDate, speHoliday);
					if(errorFlg.isAgeError()
							|| errorFlg.isClassError()
							|| errorFlg.isEmploymentError()
							|| errorFlg.isGenderError()) {
						GrantDaysInfor output = new GrantDaysInfor(loopDate, Optional.of(errorFlg), 0);
						lstOutput.add(output);
					} else {
						GrantDaysInfor output = new GrantDaysInfor(loopDate, Optional.empty(), optGranDateTbl.get().getNumberOfDays());
						lstOutput.add(output);
					}
				}
				loopDate = loopDate.addYears(1);
				outputDate = loopDate;
			}
		}

		return new GrantDaysInforByDates(outputDate, lstOutput);
	}
	@Override
	public List<SpecialHolidayInfor> getDeadlineInfo(GrantDaysInforByDates grantDaysInfor, SpecialHoliday specialHoliday) {
		TimeLimitSpecification timeSpecifyMethod = specialHoliday.getGrantPeriodic().getTimeSpecifyMethod();
		List<SpecialHolidayInfor> lstOutput = new ArrayList<>();
		//取得している「特別休暇．期限情報．期限指定方法」をチェックする
		if(timeSpecifyMethod == TimeLimitSpecification.INDEFINITE_PERIOD) {
			//パラメータ「付与日数一覧」を元にパラメータ「特別休暇情報一覧」を生成する
			grantDaysInfor.getLstGrantDaysInfor().stream().forEach(x -> {
				SpecialHolidayInfor output = new SpecialHolidayInfor(x, Optional.empty());
				lstOutput.add(output);
			});			
		} else if (timeSpecifyMethod == TimeLimitSpecification.AVAILABLE_GRANT_DATE_DESIGNATE) {
			//期限日　←　パラメータ「付与日数一覧．年月日」＋取得している「定期付与．付与日（定期）．特別休暇の期限」の「年数」「月数」の年月日
			SpecialVacationDeadline speDeadline = specialHoliday.getGrantPeriodic().getExpirationDate();
			grantDaysInfor.getLstGrantDaysInfor().stream().forEach(x -> {
				GeneralDate dealineDate = x.getYmd().addYears(speDeadline.getYears().v());
				dealineDate = dealineDate.addMonths(speDeadline.getMonths().v());
				dealineDate = dealineDate.addDays(-1);
				SpecialHolidayInfor output = new SpecialHolidayInfor(x, Optional.of(dealineDate));
				lstOutput.add(output);
			});	
		} else if (timeSpecifyMethod == TimeLimitSpecification.AVAILABLE_UNTIL_NEXT_GRANT_DATE) {
			//パラメータ「付与日数一覧．年月日」の次の「付与日数一覧．年月日」
			//　※最後の処理で次の「付与日数一覧．年月日」が存在しない場合 パラメータ「期間外次回付与日」をセット
			//List<GrantDaysInfor> lstGrantDaysInfor
			int i = 1;
			for (GrantDaysInfor daysInfor : grantDaysInfor.getLstGrantDaysInfor()) {
				SpecialHolidayInfor output = new SpecialHolidayInfor();
				if(i == grantDaysInfor.getLstGrantDaysInfor().size()) {
					output = new SpecialHolidayInfor(daysInfor, grantDaysInfor.getGrantDate() != null 
							? Optional.of(grantDaysInfor.getGrantDate().addDays(-1)) : Optional.empty());
				} else {
					GrantDaysInfor nextInfor = grantDaysInfor.getLstGrantDaysInfor().get(i);
					output = new SpecialHolidayInfor(daysInfor, Optional.of(nextInfor.getYmd().addDays(-1)));
				}
				lstOutput.add(output);
				i += 1;
			}
		} else {
			grantDaysInfor.getLstGrantDaysInfor().stream().forEach(x -> {
				//付与日　←　パラメータ「付与日数一覧．年月日」の次の「特別休暇．期限情報．使用可能期間．開始日」
				AvailabilityPeriod period = specialHoliday.getGrantPeriodic().getAvailabilityPeriod();
				GeneralDate ymd = GeneralDate.ymd(x.getYmd().year(), period.getStartDate().getMonth(), period.getStartDate().getDay());
				if(ymd.after(x.getYmd())) {
					x.setYmd(ymd);	
				}				
				//パラメータ「付与日数一覧．年月日」の次の「特別休暇．期限情報．使用可能期間．終了日」
				GeneralDate ymdDealine = GeneralDate.ymd(x.getYmd().year(), period.getEndDate().getMonth(), period.getEndDate().getDay());
				if(x.getYmd().after(ymdDealine)) {
					ymdDealine = ymdDealine.addYears(1);
				}
				SpecialHolidayInfor output = new SpecialHolidayInfor(x, Optional.of(ymdDealine));
				lstOutput.add(output);
			});
		}
		return lstOutput;
	}
}
