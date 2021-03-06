package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.cache.CacheCarrier;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.PerServiceLengthTableCD;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.GrantNumber;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayCode;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.LimitCarryoverDays;

@Stateless
public class NotDepentSpecialLeaveOfEmployeeImpl implements NotDepentSpecialLeaveOfEmployee{
	@Inject
	private SpecialHolidayRepository speHolidayRepos;
	
	@Inject
	private RemainNumberTempRequireService RemainNumberTempRequireService;
	
	@Override
	public InforSpecialLeaveOfEmployee getNotDepentInfoSpecialLeave(NotDepentSpecialLeaveOfEmployeeInputExtend param) {

		
		//ドメインモデル「特別休暇」を取得する
		Optional<SpecialHoliday> optSpeHolidayInfor = speHolidayRepos.findBySingleCD(param.getCid(), param.getSpecialLeaveCode());
		if(!optSpeHolidayInfor.isPresent()) {
			return new InforSpecialLeaveOfEmployee(InforStatus.NOTUSE, Optional.empty(), new ArrayList<>());
		}
		
		RemainNumberTempRequireService.Require require = RemainNumberTempRequireService.createRequire();
		CacheCarrier cacheCarrier =new CacheCarrier();
		
		//特別休暇情報を取得する
		return getInforSpecialLeaveOfEmployee(optSpeHolidayInfor.get(), param, require, cacheCarrier);
	}
	
	
	@Override
	public Map<String, InforSpecialLeaveOfEmployee> getNotDepentInfoSpecialLeave(
			List<NotDepentSpecialLeaveOfEmployeeInputExtend> paramList) {
		
		return	paramList.stream().filter(x->x.getSid().isPresent())
				.collect(Collectors.toMap(x -> x.getSid().get(), x -> getNotDepentInfoSpecialLeave(x)));

	}
	
	/**
	 * 社員の特別休暇情報を取得する
	 * @param specialHoliday
	 * @param param
	 * @param require
	 * @param cacheCarrier
	 * @return
	 */
	private InforSpecialLeaveOfEmployee getInforSpecialLeaveOfEmployee(SpecialHoliday specialHoliday,
			NotDepentSpecialLeaveOfEmployeeInputExtend param, RemainNumberTempRequireService.Require require,
			CacheCarrier cacheCarrier) {
	
			List<SpecialHolidayInfor> specialHolidayInfor = specialHoliday.createNotDepentInfoGrantInfo(
					param.getCid(), param.getDatePeriod(), new SpecialHolidayCode(param.getSpecialLeaveCode()),
					param.getSpeGrantDate(), param.getAnnGrantDate(), param.getInputDate(), param.getSpecialSetting(),
					param.getGrantDays().map(x->new GrantNumber(x.intValue())),
					param.getGrantTableCd().map(x->new PerServiceLengthTableCD(x)),
							require, cacheCarrier);
			
			Optional<LimitCarryoverDays> limitCarryoverDays = specialHoliday.getGrantRegular()
					.getLimitCarryoverDays();

			
			if (!specialHolidayInfor.isEmpty()) {
				return  new InforSpecialLeaveOfEmployee(InforStatus.GRANTED,
						limitCarryoverDays.map(x->x.v()),
						specialHolidayInfor);

			} else {
				return  new InforSpecialLeaveOfEmployee(InforStatus.NOTGRANT,
						limitCarryoverDays.map(x->x.v()),
						new ArrayList<>());
			}
	}

}
