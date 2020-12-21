package nts.uk.screen.at.app.query.kmk004.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSetCom;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSetRepo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 会社別月単位労働時間を表示する
 * UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.メニュー別OCD（共通）.会社別月単位労働時間を表示する.会社別月単位の労働時間を表示する
 * 
 * @author chungnt
 *
 */

@Stateless
public class DisplayMonthlyWorkingHoursByCompany {

	@Inject
	private CompanyAdapter companyRepository;

	@Inject
	private MonthlyWorkTimeSetRepo monthlyWorkTimeSetRepo;

	public List<DisplayMonthlyWorkingDto> get(DisplayMonthlyWorkingInput param) {
		String cid = AppContexts.user().companyId();
		List<DisplayMonthlyWorkingDto> resutl = new ArrayList<>();
		
		// 1 年度の期間を取得(require, 会社ID, 年度)
		YearMonthPeriod yearMonthPeriod = companyRepository.get(cid, param.year);
		List<MonthlyWorkTimeSetCom> coms = new ArrayList<>();
		
		resutl = yearMonthPeriod.stream().map(m -> {
			LaborTime laborTime = new LaborTime(0,0,0);
			DisplayMonthlyWorkingDto s = new DisplayMonthlyWorkingDto(m.v(), laborTime);
			return s;
		}).collect(Collectors.toList());

		switch (param.workType) {
		case 0:
			coms = monthlyWorkTimeSetRepo.findCompanyByPeriod(cid, LaborWorkTypeAttr.REGULAR_LABOR, yearMonthPeriod);
			break;
		case 1:
			coms = monthlyWorkTimeSetRepo.findCompanyByPeriod(cid, LaborWorkTypeAttr.DEFOR_LABOR, yearMonthPeriod);
			break;
		case 2:
			coms = monthlyWorkTimeSetRepo.findCompanyByPeriod(cid, LaborWorkTypeAttr.FLEX, yearMonthPeriod);
			break;
		}
		
		if (coms.isEmpty()) {
			return resutl;
		}

		resutl = coms.stream().map(m -> {
			
			LaborTime laborTime = new LaborTime();
			laborTime.setLegalLaborTime(m.getLaborTime().getLegalLaborTime().v());
			
			if(m.getLaborTime().getWithinLaborTime().isPresent()){
				laborTime.setWithinLaborTime(m.getLaborTime().getWithinLaborTime().get().v());
			}
			
			if(m.getLaborTime().getWeekAvgTime().isPresent()){
				laborTime.setWeekAvgTime(m.getLaborTime().getWeekAvgTime().get().v());
			}
			
			DisplayMonthlyWorkingDto s = new DisplayMonthlyWorkingDto(m.getYm().v(), laborTime);
			return s;
		}).collect(Collectors.toList());

		return resutl;
	}
}
