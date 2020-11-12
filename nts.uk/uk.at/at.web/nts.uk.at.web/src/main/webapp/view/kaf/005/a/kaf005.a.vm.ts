module nts.uk.at.view.kaf005.a.viewmodel {
	import Application = nts.uk.at.view.kaf000.shr.viewmodel.Application;
	import AppType = nts.uk.at.view.kaf000.shr.viewmodel.model.AppType;
	import Kaf000AViewModel = nts.uk.at.view.kaf000.a.viewmodel.Kaf000AViewModel;
	
	@bean()
    class Kaf005AViewModel extends Kaf000AViewModel {
		appType: KnockoutObservable<number> = ko.observable(AppType.OVER_TIME_APPLICATION);
		application: KnockoutObservable<Application>;
			
		
		mounted() {
			const self = this;
			self.start();
			
		}
		
		start() {
			const self = this;
			self.application = ko.observable(new Application(self.appType()));
			self.$blockui("show");
			let empLst: Array<string> = [],
				dateLst: Array<string> = [];
			self.loadData(empLst, dateLst, self.appType())
				.then((loadDataFlag: any) => {
					self.application().appDate.subscribe(value => {	
	                    
                	});
	
					if (loadDataFlag) {
						let param1 = {
							
						} as FirstParam;
						let param2 = {
							
						} as SecondParam;
						
						param1.companyId = self.$user.companyId;
						param1.overtimeAppAtr = 1;
						param1.appDispInfoStartupDto = ko.toJS(self.appDispInfoStartupOutput);
						param1.isProxy = true;
						let command = {
							companyId: param1.companyId,
							dateOp: param1.dateOp,
							overtimeAppAtr: param1.overtimeAppAtr,
							appDispInfoStartupDto: param1.appDispInfoStartupDto,
							startTimeSPR: param1.endTimeSPR,
							endTimeSPR: param1.endTimeSPR,
							isProxy: param1.isProxy,
							employeeId: param2.employeeId,
							prePostInitAtr: param2.prePostInitAtr,
							overtimeLeaveAppCommonSet: param2.overtimeLeaveAppCommonSet,
							advanceApplicationTime: param2.advanceApplicationTime,
							achieveApplicationTime: param2.achivementApplicationTime,
							workContent: param2.workContent
						};
						
						return self.$ajax(API.start, command);
					}
				})
				.then((res: any) => {
					
				})
				.fail((res: any) => {
					
				})
				.always(() => self.$blockui('hide'));
		}
		
		
	
	}
	const API = {
		start: 'at/request/application/overtime/start'
	}
	
	interface FirstParam { // start param
		companyId: string; // 会社ID
		appType?: number; // 申請種類
		sids?: Array<string>; // 申請者リスト
		dates?: Array<string>; // 申請対象日リスト
		mode: number; // 新規詳細モード
		dateOp?: string // 申請日
		overtimeAppAtr: number; // 残業申請区分
		appDispInfoStartupDto: any; // 申請表示情報
		startTimeSPR?: number; // SPR連携の開始時刻
		endTimeSPR?: number; // SPR連携の終了時刻
		isProxy: boolean; // 代行申請か
	}
	
	interface SecondParam { // start param
		companyId: string; // 会社ID
		employeeId: string; // 社員ID
		appDate: string; // 申請日
		prePostInitAtr: number; // 事前事後区分
		overtimeLeaveAppCommonSet: OvertimeLeaveAppCommonSet; // 残業休出申請共通設定
		advanceApplicationTime: ApplicationTime; // 事前の申請時間
		achivementApplicationTime: ApplicationTime; // 実績の申請時間
		workContent: WorkContent; // 勤務内容
	}
	interface OvertimeLeaveAppCommonSet {
		preExcessDisplaySetting: number; // 事前超過表示設定
		extratimeExcessAtr: number; // 時間外超過区分
		extratimeDisplayAtr: number; // 時間外表示区分
		performanceExcessAtr: number; // 実績超過区分
		checkOvertimeInstructionRegister: number; // 登録時の指示時間超過チェック
		checkDeviationRegister: number; // 登録時の乖離時間チェック
		overrideSet: number; // 実績超過打刻優先設定
		
	}
	interface ApplicationTime {
		applicationTime: Array<OvertimeApplicationSetting>; //  申請時間
		flexOverTime: number; // フレックス超過時間
		overTimeShiftNight: OverTimeShiftNight; // 就業時間外深夜時間
		anyItem: Array<AnyItemValue>; // 任意項目
		reasonDissociation: Array<any>; // 乖離理由
	}
	interface OvertimeApplicationSetting {
		frameNo: number; 
		attendanceType: number;
		applicationTime: number
	}
	interface OverTimeShiftNight {
		midNightHolidayTimes: Array<any>;
		midNightOutSide: number;
		overTimeMidNight: number;
	}
	interface AnyItemValue {
		itemNo: number;
		times: number;
		amount: number;
		time: number
	}
	interface ReasonDivergence {
		
		reason: DivergenceReason;
		reasonCode: string;
		diviationTime: number;
	}
	interface DivergenceReason {
		
	}
	interface WorkContent {
		workTypeCode: string;
		workTimeCode: string;
		timeZones: Array<TimeZone>;
		breakTimes: Array<BreakTimeSheet>;
	}
	interface TimeZone {
		start: number;
		end: number;
	}
	interface BreakTimeSheet {
		breakFrameNo: number;
		startTime: number;
		endTime: number;
		breakTime: number;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}