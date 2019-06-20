/**
 * 
 */
package nts.uk.ctx.at.auth.pubimp.initswitchsetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSet;
import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSetRepo;
import nts.uk.ctx.at.auth.pub.initswitchsetting.DateProcessed;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.auth.pub.initswitchsetting.InitDisplayPeriodSwitchSetPub;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hieult
 *
 */
@Stateless
public class InitDisplayPeriodSwitchSetPubImpl implements InitDisplayPeriodSwitchSetPub{
 
	@Inject
	private InitDisplayPeriodSwitchSetRepo repo;
	
	@Inject
	private ClosureService closureService;
	@Override
	public InitDisplayPeriodSwitchSetDto targetDateFromLogin() {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		String attendanceID = AppContexts.user().roles().forAttendance();
		InitDisplayPeriodSwitchSetDto data = new InitDisplayPeriodSwitchSetDto(1, new ArrayList<>());
		//全締めの当月と期間を取得する
	//	InitDisplayPeriodSwitchSetDto data = new InitDisplayPeriodSwitchSetDto();
		List<ClosureInfo> listClosureInfo= closureService.getAllClosureInfo();
		List<DateProcessed> listDate = new ArrayList<>();
		List<DateProcessed> listDateProcessed = listClosureInfo.stream().map(i -> {
			return new DateProcessed(i.getClosureId().value, i.getCurrentMonth(), i.getPeriod());
		}).collect(Collectors.toList());
		//ドメインモデル「初期表示期間切替設定」を取得する
		/** 条件：
			会社ID←ログイン会社ID
			ロールID←ログインユーザコンテキスト．就業ロールID**/
		Optional<InitDisplayPeriodSwitchSet> optDisSwitchSet = repo.findByKey(companyID, attendanceID);
		
		if(!optDisSwitchSet.isPresent()){
			listDateProcessed = listClosureInfo.stream().map(i -> {
				return new DateProcessed(i.getClosureId().value, i.getCurrentMonth(), i.getPeriod());
			}).collect(Collectors.toList());
			/** Enum
			 * 当月 = 1
			 * 翌月 = 2  **/
			  data = new InitDisplayPeriodSwitchSetDto(1, listDateProcessed);
			 return data;
		}else{
			//当月・翌月を判断する
			for(ClosureInfo item : listClosureInfo){
				int endDate = item.getPeriod().end().day();
				int switchDate = optDisSwitchSet.get().getDay();
				if(endDate + switchDate <= GeneralDate.today().day() ){
					/** 取得した締め情報をループする **/
					/** Chỗ này EA đang lấy Closure nhưng có vẻ ko đúng
					 * Đang lấy theo Closure Info - Hieu LT
					 */
					/**DatePeriod getClosurePeriod(int closureId, YearMonth processingYm); */
					DatePeriod datePeriod = closureService.getClosurePeriod(item.getClosureId().value, item.getCurrentMonth().addMonths(1));
					DateProcessed endDateNextMonth = new DateProcessed(item.getClosureId().value, item.getCurrentMonth().addMonths(1), datePeriod);
					listDate.add(endDateNextMonth);
					 data = new InitDisplayPeriodSwitchSetDto(2, listDate);
				}
				else{
					data = new InitDisplayPeriodSwitchSetDto(1, listDateProcessed);
				}
			}
			return data;
		}
	};
}
