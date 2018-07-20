package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffComHistItemShareImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffCompanyHistSharedImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.SClsHistImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SEmpHistoryImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SysEmploymentHisAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.specialholidaynew.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholidaynew.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.AgeBaseYear;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.SpecialLeaveRestriction;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.ElapseYear;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantDate;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantDateTbl;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantDateTblRepository;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.GrantTime;
import nts.uk.ctx.at.shared.dom.specialholidaynew.grantinformation.TypeTime;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.SpecialVacationDeadline;
import nts.uk.ctx.at.shared.dom.specialholidaynew.periodinformation.TimeLimitSpecification;
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
	public InforSpecialLeaveOfEmployee getInforSpecialLeaveOfEmployee(String cid, String sid, int specialLeaveCode,
			DatePeriod complileDate) {
		InforSpecialLeaveOfEmployee outputData = new InforSpecialLeaveOfEmployee(InforStatus.NOTUSE, Optional.empty(), new ArrayList<>(), false);
		//ドメインモデル「特別休暇基本情報」を取得する
		Optional<SpecialLeaveBasicInfo> optBasicInfor = leaveBasicInfoRepo.getBySidLeaveCdUser(sid, specialLeaveCode, UseAtr.USE);
		if(!optBasicInfor.isPresent()) {
			return outputData;
		}
		//ドメインモデル「特別休暇」を取得する
		Optional<SpecialHoliday> optSpecialHoliday = holidayRepo.findByCode(cid, specialLeaveCode);
		if(!optSpecialHoliday.isPresent()) {
			return outputData;
		}
		SpecialHoliday specialHoliday = optSpecialHoliday.get();
		SpecialLeaveBasicInfo leaverBasicInfo = optBasicInfor.get();
		//付与日数情報を取得する
		GrantDaysInforByDates grantDayInfors = this.getGrantDays(cid, sid, complileDate, specialHoliday, leaverBasicInfo);
		//「付与日数一覧」の件数をチェックする
		if(grantDayInfors == null || grantDayInfors.getLstGrantDaysInfor().isEmpty()) {
			//状態：「付与なし」を返す
			return new InforSpecialLeaveOfEmployee(InforStatus.NOTGRANT, Optional.of(specialHoliday.getGrantPeriodic().getLimitCarryoverDays().v()),
					new ArrayList<>(), specialHoliday.getGrantRegular().isAllowDisappear());
		} else {
			//期限を取得する
			return new InforSpecialLeaveOfEmployee(InforStatus.GRANTED, Optional.of(specialHoliday.getGrantPeriodic().getLimitCarryoverDays().v()),
					this.getDeadlineInfo(grantDayInfors, specialHoliday), specialHoliday.getGrantRegular().isAllowDisappear());
		}
	}
	@Override
	public GrantDaysInforByDates getGrantDays(String cid, String employeeId, DatePeriod period, SpecialHoliday speHoliday, SpecialLeaveBasicInfo leaveBasicInfo) {
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
			if(annualLeaveEmpBasicInfo.isPresent()) {
				//所得したドメインモデル「年休社員基本情報．付与ルール．付与基準日」をパラメータ「付与基準日」にセットする
				grantDate = annualLeaveEmpBasicInfo.get().getGrantRule().getGrantStandardDate();
			}			
		} else {
			//取得している「特別休暇基本情報．付与設定．付与基準日」をパラメータ「付与基準日」にセットする
			grantDate = leaveBasicInfo.getGrantSetting().getGrantDate();
		}
		//取得している「特別休暇．付与情報．付与するタイミングの種類」をチェックする
		if(speHoliday.getGrantRegular().getTypeTime() == TypeTime.GRANT_START_DATE_SPECIFY) {
			//固定の付与日一覧を求める
			return this.askGrantDays(cid, employeeId, period, grantDate, speHoliday);
		} else {
			//テーブルに基づいた付与日数一覧を求める
			return this.askGrantdaysFromtable(cid, employeeId, period, grantDate, leaveBasicInfo, speHoliday);
		}
	}
	@Override
	public GrantDaysInforByDates askGrantDays(String cid, String sid, DatePeriod period, GeneralDate grantDate, SpecialHoliday speHoliday) {
		List<GrantDaysInfor> lstOutput = new ArrayList<>();
		//TODO パラメータ「付与基準日」をパラメータ「比較年月日」にセットする
		GeneralDate hikakuYmd = grantDate;
		GrantTime grantTime = speHoliday.getGrantRegular().getGrantTime();
		int interval = grantTime.getFixGrantDate().getInterval().v();
		GeneralDate hikakuYmdTmp = grantDate.addDays(interval);
		GeneralDate nextTime = grantDate;
		//パラメータ「期間」に一致する付与日数を生成する
		for(int i = 0; hikakuYmd.daysTo(hikakuYmdTmp) - i >= 0; i++){			
			GeneralDate loopDate = period.start().addDays(i);
			//パラメータ「比較年月日」とパラメータ「期間」を比較する
			if(period.start().beforeOrEquals(loopDate)
					&& loopDate.beforeOrEquals(period.end())) {//「期間．開始日」≦「比較年月日」≦「期間．終了日」
				//利用条件をチェックする
				ErrorFlg checkUser = this.checkUse(cid, sid, period, speHoliday);
				if(checkUser.isAgeError()
						|| checkUser.isClassError()
						|| checkUser.isEmploymentError()
						|| checkUser.isGenderError()) {
					//パラメータ「付与日数一覧」を追加する
					GrantDaysInfor outPut = new GrantDaysInfor(loopDate, Optional.of(checkUser), 0);
					lstOutput.add(outPut);
				} else {
					GrantDaysInfor outPut = new GrantDaysInfor(loopDate, Optional.empty(), speHoliday.getGrantRegular().getGrantTime().getFixGrantDate().getGrantDays().v());
					lstOutput.add(outPut);
				}
			} else if (period.end().before(loopDate)) {
				nextTime = loopDate;
				break;
			}
		}
		return new GrantDaysInforByDates(nextTime, lstOutput);
	}
	@Override
	public ErrorFlg checkUse(String cid, String sid, DatePeriod period, SpecialHoliday speHoliday) {
		ErrorFlg outData = new ErrorFlg(false, false, false, false);
		SpecialLeaveRestriction specialLeaveRestric = speHoliday.getSpecialLeaveRestriction();
		//Imported(就業)「社員」を取得する
		EmployeeRecordImport empInfor = sysCompanyAdapter.findByAllInforEmpId(sid);
		//取得しているドメインモデル「定期付与．特別休暇利用条件．性別条件」をチェックする
		if(specialLeaveRestric.getGenderRest() == nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.UseAtr.USE) {
			//性別が一致するかチェックする
			if(empInfor.getGender() != specialLeaveRestric.getGender().value) {
				//パラメータ「エラーフラグ．性別条件に一致しない」にTRUEをセットする
				outData.setGenderError(true);				
			}
		}
		//取得しているドメインモデル「定期付与．特別休暇利用条件．雇用条件」をチェックする
		if(specialLeaveRestric.getRestEmp() == nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.UseAtr.USE) {
			List<String> sids = new ArrayList<>();
			sids.add(sid);
			//アルゴリズム「社員所属雇用履歴を取得」を実行する
			List<SharedSidPeriodDateEmploymentImport> employmentInfor = sysEmploymentHist.getEmpHistBySidAndPeriod(sids, period);
			if(employmentInfor.isEmpty()) {
				outData.setEmploymentError(true);
			} else {
				List<AffPeriodEmpCodeImport> lstEmployment = employmentInfor.get(0).getAffPeriodEmpCodeExports();
				if(lstEmployment.isEmpty()) {
					outData.setEmploymentError(true);
				} else {
					List<String> listEmp = speHoliday.getSpecialLeaveRestriction().getListEmp();
					boolean isExit = false;
					for (AffPeriodEmpCodeImport x : lstEmployment) {
						if(listEmp.contains(x.getEmploymentCode())) {
							isExit = true;
							break;
						}
					}
					if(!isExit) {
						outData.setEmploymentError(true);
					}
				}
				
			}
		}
		//ドメインモデル「特別休暇利用条件」．分類条件をチェックする
		if(specialLeaveRestric.getRestrictionCls() == nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.UseAtr.USE) {
			//アルゴリズム「社員所属分類履歴を取得」を実行する
			List<String> lstSids = new ArrayList<>();
			lstSids.add(sid);
			List<SClsHistImport> lstClass = sysCompanyAdapter.lstClassByEmployeeId(cid, lstSids, period);			
			if(lstClass.isEmpty()) {
				outData.setClassError(true);
			}
			//取得した分類コードが取得しているドメインモデル「定期付与．特別休暇利用条件．分類一覧」に存在するかチェックする
			List<String> listClassSpe = speHoliday.getSpecialLeaveRestriction().getListCls();
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
		//ドメインモデル「特別休暇利用条件」．年齢条件をチェックする
		if(specialLeaveRestric.getAgeLimit() == nts.uk.ctx.at.shared.dom.specialholidaynew.grantcondition.UseAtr.USE) {
			GeneralDate ageBase = GeneralDate.today();
			//年齢基準日を求める
			if(specialLeaveRestric.getAgeStandard().getAgeCriteriaCls() == AgeBaseYear.THIS_YEAR) {
				MonthDay ageBaseDate = specialLeaveRestric.getAgeStandard().getAgeBaseDate();				
				ageBase = GeneralDate.ymd(ageBase.year(), ageBaseDate.getMonth(), ageBaseDate.getDay());
			} else {
				MonthDay ageBaseDate = specialLeaveRestric.getAgeStandard().getAgeBaseDate();				
				ageBase = GeneralDate.ymd(ageBase.year() + 1, ageBaseDate.getMonth(), ageBaseDate.getDay());
			}
			//求めた「年齢基準日」時点の年齢を求める
			int yearBase = ageBase.addYears(-empInfor.getBirthDay().year()).year();
			//求めた「年齢」が年齢条件に一致するかチェックする
			if(specialLeaveRestric.getAgeRange().getAgeLowerLimit().v() >= yearBase
					|| specialLeaveRestric.getAgeRange().getAgeHigherLimit().v() <= yearBase) {
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
		if( basicInfor.getGrantSetting().getGrantTable().isPresent()) {
			optGranDateTbl = grantTableRepos.findByCode(cid, basicInfor.getSpecialLeaveCode().v(), basicInfor.getGrantSetting().getGrantTable().get().v());
		} else {
			optGranDateTbl = grantTableRepos.findByCodeAndIsSpecified(cid, basicInfor.getSpecialLeaveCode().v());
		}
		
		//取得できなかった場合 - Không get được
		if(!optGranDateTbl.isPresent()) {
			return null;
		}				
		GrantDateTbl grantDateTbl = optGranDateTbl.get();
		//※処理中の「特別休暇付与テーブル．経過年数に対する付与日数．経過年数」を次へ更新
		List<ElapseYear> elapseYear = grantTableRepos.findElapseByGrantDateCd(cid, grantDateTbl.getGrantDateCode().v());
		for (ElapseYear yearData : elapseYear) {//TODO xem lai
			//パラメータ「比較年月日」に取得したドメインモデル「特別休暇付与テーブル．経過年数に対する付与日数．経過年数」を加算する
			GeneralDate granDateTmp = granDate.addYears(yearData.getYears().v());
			granDateTmp = granDateTmp.addMonths(yearData.getMonths().v());
			//パラメータ「比較年月日」とパラメータ「期間」を比較する
			if(period.start().beforeOrEquals(granDateTmp)
					&& period.end().afterOrEquals(granDateTmp)) {
				//利用条件をチェックする
				ErrorFlg errorFlg = this.checkUse(cid, sid, period, speHoliday);
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
				outputDate = granDateTmp;
				break;
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
				dealineDate.addMonths(speDeadline.getMonths().v());
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
					output = new SpecialHolidayInfor(daysInfor, grantDaysInfor.getGrantDate() != null ? Optional.of(grantDaysInfor.getGrantDate()) : Optional.empty());
				} else {
					GrantDaysInfor nextInfor = grantDaysInfor.getLstGrantDaysInfor().get(i);
					output = new SpecialHolidayInfor(daysInfor, Optional.of(nextInfor.getYmd()));
				}
				lstOutput.add(output);
				i += 1;
			}
		} else {
			grantDaysInfor.getLstGrantDaysInfor().stream().forEach(x -> {
				//付与日　←　パラメータ「付与日数一覧．年月日」の次の「特別休暇．期限情報．使用可能期間．開始日」
				DatePeriod period = specialHoliday.getGrantPeriodic().getAvailabilityPeriod();
				GeneralDate ymd = GeneralDate.ymd(x.getYmd().year(), period.start().month(), period.start().day());
				x.setYmd(ymd);
				//パラメータ「付与日数一覧．年月日」の次の「特別休暇．期限情報．使用可能期間．終了日」
				GeneralDate ymdDealine = GeneralDate.ymd(x.getYmd().year(), period.end().month(), period.end().day());
				SpecialHolidayInfor output = new SpecialHolidayInfor(x, Optional.of(ymdDealine));
				lstOutput.add(output);
			});
		}
		return lstOutput;
	}
}
