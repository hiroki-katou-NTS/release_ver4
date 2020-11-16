module nts.uk.at.view.kaf005.a.viewmodel {
	import OvertimeWork = nts.uk.at.view.kaf005.shr.header.viewmodel.OvertimeWork;
	import Application = nts.uk.at.view.kaf000.shr.viewmodel.Application;
	import AppType = nts.uk.at.view.kaf000.shr.viewmodel.model.AppType;
	import Kaf000AViewModel = nts.uk.at.view.kaf000.a.viewmodel.Kaf000AViewModel;
	
	@bean()
    class Kaf005AViewModel extends Kaf000AViewModel {
	
		appType: KnockoutObservable<number> = ko.observable(AppType.OVER_TIME_APPLICATION);
		application: KnockoutObservable<Application>;
		isSendMail: KnockoutObservable<Boolean>;	
		isAgentMode : KnockoutObservable<boolean> = ko.observable(false);
		overTimeWork: KnockoutObservableArray<OvertimeWork> = ko.observableArray([]);
		dataSource: DisplayInfoOverTime;
		created() {
			const self = this;
			self.isSendMail = ko.observable(false);
			self.application = ko.observable(new Application(self.appType()));
		}
		
		mounted() {
			const self = this;
			self.start();
			
		}
		
		start() {
			const self = this;
			
			self.$blockui("show");
			let empLst: Array<string> = [],
				dateLst: Array<string> = [];
			self.loadData(empLst, dateLst, self.appType())
				.then((loadDataFlag: any) => {
					self.application().appDate.subscribe(value => {	
	                    if (value) {
							self.changeDate();
						}
                	});
	
					if (loadDataFlag) {
						let param1 = {
							
						} as FirstParam;
						param1.companyId = self.$user.companyId;
						// param1.dateOp = '2020/11/13';
						param1.overtimeAppAtr = OvertimeAppAtr.EARLY_OVERTIME;
						param1.appDispInfoStartupDto = ko.toJS(self.appDispInfoStartupOutput);
						param1.startTimeSPR = 100;
						param1.endTimeSPR = 200;
						param1.isProxy = true;
						let command = {
							companyId: param1.companyId,
							dateOp: param1.dateOp,
							overtimeAppAtr: param1.overtimeAppAtr,
							appDispInfoStartupDto: param1.appDispInfoStartupDto,
							startTimeSPR: param1.startTimeSPR,
							endTimeSPR: param1.endTimeSPR,
							isProxy: param1.isProxy,
						};
						
						return self.$ajax(API.start, command);
					}
				})
				.then((res: DisplayInfoOverTime) => {
					self.dataSource = res;
					self.bindOverTimeWorks(res);
				})
				.fail((res: any) => {
					
				})
				.always(() => self.$blockui('hide'));
		}
		
		changeDate() {
			console.log('change date');
			const self = this;
			let param1 = {
							
			} as FirstParam;
			param1.companyId = self.$user.companyId;
			param1.dateOp = ko.toJS(self.appDispInfoStartupOutput).appDispInfoWithDateOutput.baseDate;
			param1.overtimeAppAtr = OvertimeAppAtr.EARLY_OVERTIME;
			param1.appDispInfoStartupDto = ko.toJS(self.appDispInfoStartupOutput);
			param1.startTimeSPR = 100;
			param1.endTimeSPR = 200;
			let command = {
				companyId: param1.companyId,
				dateOp: param1.dateOp,
				overtimeAppAtr: param1.overtimeAppAtr,
				appDispInfoStartupDto: param1.appDispInfoStartupDto,
				startTimeSPR: param1.startTimeSPR,
				endTimeSPR: param1.endTimeSPR,
				overTimeAppSet: self.dataSource.infoNoBaseDate.overTimeAppSet,
				worktypes: self.dataSource.infoBaseDateOutput.worktypes
			}
			self.$ajax(API.changeDate, command)
				.done((res: DisplayInfoOverTime) => {
					console.log(res);
					self.dataSource.infoWithDateApplicationOp = res.infoWithDateApplicationOp;
					self.dataSource.calculationResultOp = res.calculationResultOp;
					self.dataSource.workdayoffFrames = res.workdayoffFrames;
				})
				.fail((res: any) => {
					
				})
				.always(() => self.$blockui('hide'));
		}
		
		register() {
			
		}
		
		bindOverTimeWorks(res: DisplayInfoOverTime) {
			const self = this;
			let overTimeWorks = [];
			{
				let item = new OvertimeWork();
				let currentMonth = res.infoNoBaseDate.agreeOverTimeOutput.currentMonth;
				item.yearMonth = ko.observable(currentMonth);
				overTimeWorks.push(item);
			}
			{
				let item = new OvertimeWork(); 
				let nextMonth = res.infoNoBaseDate.agreeOverTimeOutput.nextMonth;
				item.yearMonth = ko.observable(nextMonth);
				overTimeWorks.push(item);
			}
			self.overTimeWork(overTimeWorks);
		}
		
		
	
	}
	const API = {
		start: 'at/request/application/overtime/start',
		changeDate: 'at/request/application/overtime/changeDate'
	}
	
	interface DisplayInfoOverTime {
		infoBaseDateOutput: any;
		infoNoBaseDate: InfoNoBaseDate;
		workdayoffFrames: Array<any>;
		overtimeAppAtr: OvertimeAppAtr;
		appDispInfoStartup: any;
		isProxy: Boolean;
		calculationResultOp?: any;
		infoWithDateApplicationOp?: any;
	}
	interface InfoBaseDateOutput {
		worktypes: Array<any>;
		quotaOutput: any;
	}
	interface InfoNoBaseDate {
		overTimeReflect: any;
		overTimeAppSet: any;
		agreeOverTimeOutput: AgreeOverTimeOutput;
		divergenceReasonInputMethod: Array<any>;
		divergenceTimeRoot: Array<any>;
	}
	interface AgreeOverTimeOutput {
		detailCurrentMonth: AgreementTimeImport;
		detailNextMonth: AgreementTimeImport;
		currentMonth: string;
		nextMonth: string;
	}
	interface AgreementTimeImport {
		employeeId: string;
		confirmed?: AgreeTimeOfMonthExport;
		afterAppReflect?: AgreeTimeOfMonthExport;
		confirmedMax?: AgreMaxTimeOfMonthExport;
		afterAppReflectMax?: AgreMaxTimeOfMonthExport;
		errorMessage?: string;
	}
	interface AgreeTimeOfMonthExport {
		agreementTime: number;
		limitErrorTime: number;
		limitAlarmTime: number
		exceptionLimitErrorTime?: number;
		exceptionLimitAlarmTime?: number;
		status: number
	}
	interface AgreMaxTimeOfMonthExport {
		agreementTime: number;
		maxTime: number;
		status: number;
	}
	enum OvertimeAppAtr {
		
		EARLY_OVERTIME,
		NORMAL_OVERTIME,
		EARLY_NORMAL_OVERTIME
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