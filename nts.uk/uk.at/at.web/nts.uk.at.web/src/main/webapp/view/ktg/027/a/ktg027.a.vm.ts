/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />
module nts.uk.at.view.ktg027.a.viewmodel {
  import block = nts.uk.ui.block;
  import formatById = nts.uk.time.format.byId;
  import getShared = nts.uk.ui.windows.getShared;
  @bean()
  export class ViewModel extends ko.ViewModel {
    width: KnockoutObservable<string> = ko.observable("90px");
    width2: KnockoutObservable<string> = ko.observable("30px");
    year: KnockoutObservable<number> = ko.observable(0);
    currentOrNextMonth: KnockoutObservable<number> = ko.observable(0);
    listEmp: KnockoutObservableArray<PersonEmpBasicInfoImport> = ko.observableArray([]);
    listOvertimeByEmp: KnockoutObservableArray<any> = ko.observableArray([]);
    closureId: KnockoutObservable<number> = ko.observable(0);
    dataRes: any;
    created() {
      const vm = this;
      vm.currentOrNextMonth(parseInt(getShared('cache').currentOrNextMonth));
      service.getDataInit(vm.currentOrNextMonth()).then((response) => {
        if(!response.closingInformationForNextMonth){
          vm.year(response.closingInformationForCurrentMonth.processingYm);
        }else{
          vm.year(response.closingInformationForNextMonth.processingYm);
        }
        vm.listEmp(response.personalInformationOfSubordinateEmployees);
        vm.listOvertimeByEmp(response.overtimeOfSubordinateEmployees);
        vm.closureId(response.closureId);
      })
      
    }

    mounted() {
      const vm = this;
      
        vm.year.subscribe(function (dateChange) {
          if(vm.closureId() != 0){
          vm.onChangeDate(dateChange);
        }
      })
    }
    
    // format int to HM
    public  genTime(data){
      return formatById("Clock_Short_HM", data);
    }

    // show chart
    public genWidthByTime(data){
      return data/60*2 + "" + "px"
    }
    
    // event when change date
    private onChangeDate(dateChange: number){
      const vm = this;
      service.onChangeDate(vm.closureId(), dateChange).then((response) => {
        vm.listEmp(response.personalInformationOfSubordinateEmployees);
        vm.listOvertimeByEmp(response.overtimeOfSubordinateEmployees);
      });
    }
  }

  // export class ScreenModel {
  //     /**YM Picker **/
  //     targetMonth: KnockoutObservable<any>;
  //     cssRangerYM = ko.observable({});
  //     /**ComboBox**/
  //     selectedClosureID: KnockoutObservable<number>;
  //     inforOvertime: KnockoutObservableArray<InforOvertime>;
  //     inforOvertimeFooter: KnockoutObservable<InforOvertime>;
  //     closureResultModel: KnockoutObservableArray<ClosureResultModel> = ko.observableArray([]);
  //     check: KnockoutObservable<boolean>;
  //     //color
  //     backgroundColor: KnockoutObservable<String>;
  //     color: KnockoutObservable<String>;
  //     displayEr: KnockoutObservable<boolean>;
  //     msg: KnockoutObservable<String>;
  //     year: KnockoutObservable<string> = ko.observable('');
  //     width: KnockoutObservable<string> = ko.observable('90px');
  //     width2: KnockoutObservable<string> = ko.observable('30px');
  //     constructor() {
  //         var self = this;
  //         var today = moment();
  //         var month = today.month() + 1;
  //         if (month < 10) {
  //             month = '0' + month;
  //         }
  //         else {
  //             month = month;
  //         }
  //         var year = today.year();
  //         var targetMonth = year + "" + month

  //         self.targetMonth = ko.observable(targetMonth);
  //         self.selectedClosureID = ko.observable(1);
  //         var inforOvertime: Array<InforOvertime> = [];
  //         self.inforOvertimeFooter = ko.observable(new InforOvertime("", null, null, null, null, "", ""));
  //         self.targetMonth.subscribe((newSelect) => {
  //             self.clickExtractionBtn();

  //         });
  //         self.inforOvertime = ko.observableArray([]);
  //         self.backgroundColor = ko.observable('');
  //         self.color = ko.observable('');
  //         self.check = ko.observable(false);
  //         self.displayEr = ko.observable(false);
  //         self.msg = ko.observable('');

  //     }

  //     startPage(): JQueryPromise<any> {
  //         var self = this;
  //         var dfd = $.Deferred();
  //         block.grayout();
  //         service.getListClosure().done((closureResultModel) => {
  //             self.closureResultModel(closureResultModel);
  //         });
  //         service.getOvertimeHours(self.targetMonth()).done((data) => {
  //             self.displayEr(false);
  //             self.selectedClosureID(data.closureID);
  //             var inforOvertime = [];
  //             var inforOvertimeFooter = [];
  //             let total1 = 0;
  //             let total2 = 0;
  //             let total3 = 0;

  //             _.forEach(data.overtimeHours.overtimeLaborInfor, function(e) {
  //                 let timeLimit = e.confirmed.exceptionLimitErrorTime;
  //                 let actualTime = e.confirmed.agreementTime;
  //                 let applicationTime = e.afterAppReflect.agreementTime - actualTime;
  //                 let totalTime = e.afterAppReflect.agreementTime;
  //                 let status = e.afterAppReflect.status;
  //                   var backgroundColor = '';
  //                     var color = '';
  //                     let status = e.confirmed.status;
  //                     if (status == 2 || status == 4) { backgroundColor = '#F6F636'; color = '#ff0000'; }
  //                     else if (status == 1 || status == 3) { backgroundColor = '#FD4D4D'; color = '#ffffff'; }
  //                     else if (status == 6 || status == 7) { backgroundColor = '#eb9152'; }

  //                 //  if (!nts.uk.text.isNullOrEmpty(e.afterAppReflect.exceptionLimitErrorTime) || e.afterAppReflect.exceptionLimitErrorTime == "") {
  //                 //      timeLimit = e.afterAppReflect.exceptionLimitErrorTime
  //                 inforOvertime.push(new InforOvertime(e.employeeCD + " " + e.empName, formatById("Clock_Short_HM", timeLimit), formatById("Clock_Short_HM", actualTime), formatById("Clock_Short_HM", applicationTime), formatById("Clock_Short_HM", totalTime), backgroundColor, color));

  //                 total1 += actualTime;
  //                 total2 += applicationTime;
  //                 total3 += totalTime;

  //             });
  //             self.inforOvertimeFooter(new InforOvertime(nts.uk.resource.getText("KTG027_10"), null, formatById("Clock_Short_HM", total1), formatById("Clock_Short_HM", total2), formatById("Clock_Short_HM", total3), '', ''));
  //             inforOvertime.push(self.inforOvertimeFooter())
  //             self.inforOvertime(inforOvertime);
  //             if (!nts.uk.text.isNullOrEmpty(data.overtimeHours.errorMessage))
  //                 var MsgID = data.overtimeHours.errorMessage;

  //             $.each(data.overtimeHours.overtimeLaborInfor, function(item) {
  //                 if (item.errorMessage != null) {
  //                     self.check(true);
  //                     return false;
  //                 }
  //             });
  //             nts.uk.ui.errors.clearAll();
  //             dfd.resolve();
  //             block.clear();
  //         }).fail(function(msg) {
  //             self.displayEr(true);
  //             self.msg(msg.errorMessage);
  //             nts.uk.ui.errors.clearAll();
  //             dfd.resolve();
  //             block.clear();
  //         });

  //         // Init Fixed Table
  //         $("#fixed-table").ntsFixedTable({ height: 300, width: 600 });
  //         return dfd.promise();
  //     }
  //     clickExtractionBtn() {
  //         $(".inputYM").trigger("validate");
  //         $('#inputYM').focus();
  //         if (!$(".nts-input").ntsError("hasError")) {
  //             var self = this;
  //             var dfd = $.Deferred();
  //             block.grayout();

  //             service.buttonPressingProcess(self.targetMonth(), self.selectedClosureID()).done((data) => {
  //                 self.displayEr(false);
  //                 var inforOvertime = [];
  //                 var inforOvertime = [];
  //                 var inforOvertimeFooter = [];
  //                 let total1 = 0;
  //                 let total2 = 0;
  //                 let total3 = 0;
  //                 _.forEach(data.overtimeLaborInfor, function(e) {
  //                     let timeLimit = e.confirmed.exceptionLimitErrorTime;
  //                     let actualTime = e.confirmed.agreementTime;
  //                     let applicationTime = e.afterAppReflect.agreementTime - actualTime;
  //                     let totalTime = e.afterAppReflect.agreementTime;
  //                     var backgroundColor = '';
  //                     var color = '';
  //                     let status = e.confirmed.status;
  //                    if (status == 2 || status == 4) { backgroundColor = '#F6F636'; color = '#ff0000'; }
  //                     else if (status == 1 || status == 3) { backgroundColor = '#FD4D4D'; color = '#ffffff'; }
  //                     else if (status == 6 || status == 7) { backgroundColor = '#eb9152'; }

  //                     //if (!nts.uk.text.isNullOrEmpty(e.afterAppReflect.exceptionLimitErrorTime) || e.afterAppReflect.exceptionLimitErrorTime == "") {
  //                     //  timeLimit = e.afterAppReflect.exceptionLimitErrorTime;
  //                     //}

  //                     inforOvertime.push(new InforOvertime(e.employeeCD + " " + e.empName, formatById("Clock_Short_HM", timeLimit), formatById("Clock_Short_HM", actualTime), formatById("Clock_Short_HM", applicationTime), formatById("Clock_Short_HM", totalTime), backgroundColor, color));

  //                     total1 += actualTime;
  //                     total2 += applicationTime;
  //                     total3 += totalTime;
  //                 });
  //                 self.inforOvertimeFooter(new InforOvertime(nts.uk.resource.getText("KTG027_10"), null, formatById("Clock_Short_HM", total1), formatById("Clock_Short_HM", total2), formatById("Clock_Short_HM", total3), '', ''));
  //                 inforOvertime.push(self.inforOvertimeFooter())
  //                 self.inforOvertime(inforOvertime);
  //             }).fail(function(msg) {
  //                 self.displayEr(true);
  //                 self.msg(msg.errorMessage);
  //                 nts.uk.ui.errors.clearAll();
  //                 dfd.resolve();
  //                 nts.uk.ui.errors.clearAll();
  //                 block.clear();
  //             }).always(() => {
  //                 block.clear();
  //             });
  //         }
  //     }

  //     printData(): void {
  //         block.invisible();
  //         let self = this;
  //         service.saveAsCsv(self.inforOvertime());
  //         block.clear();
  //     }

  // }
  // export class ClosureResultModel {
  //     closureId: string;
  //     closureName: string;

  //     constructor(closureId: string, closureName: string) {
  //         this.closureId = closureId;
  //         this.closureName = closureName;
  //     }
  // }
  // //define
  // export class InforOvertime {
  //     employee: string;
  //     timeLimit: number;
  //     actualTime: number;
  //     applicationTime: number;
  //     totalTime: number;
  //     backgroundColor: string;
  //     color: string;
  //     constructor(employee: string, timeLimit: number, actualTime: number, applicationTime: number, totalTime: number, backgroundColor: string, color: string) {
  //         this.employee = employee;
  //         this.timeLimit = timeLimit;
  //         this.actualTime = actualTime;
  //         this.applicationTime = applicationTime;
  //         this.totalTime = totalTime;
  //         this.backgroundColor = backgroundColor;
  //         this.color = color;
  //     }
  // }
  // enum Status {
  //     /** 正常 */
  //     NORMAL = 0,
  //     /** 限度エラー時間超過 */
  //     EXCESS_LIMIT_ERROR = 1,
  //     /** 限度アラーム時間超過 */
  //     EXCESS_LIMIT_ALARM = 2,
  //     /** 特例限度エラー時間超過 */
  //     EXCESS_EXCEPTION_LIMIT_ERROR = 3,
  //     /** 特例限度アラーム時間超過 */
  //     EXCESS_EXCEPTION_LIMIT_ALARM = 4,
  //     /** 正常（特例あり） */
  //     NORMAL_SPECIAL = 5,
  //     /** 限度エラー時間超過（特例あり） */
  //     EXCESS_LIMIT_ERROR_SP = 6,
  //     /** 限度アラーム時間超過（特例あり） */
  //     EXCESS_LIMIT_ALARM_SP =7
  // }
  export class CurrentClosingPeriod {
    processingYm: number;
    closureStartDate: number;
    closureEndDate: number;
    constructor(init?: Partial<CurrentClosingPeriod>) {
      $.extend(this, init);
    }
  }

  export class PersonEmpBasicInfoImport {
    personId: string;
    employeeId: string;
    gender: number;
    businessName: string;
    birthday: number;
    employeeCode: string;
    jobEntryDate: number;
    retirementDate: number;
    constructor(init?: Partial<CurrentClosingPeriod>) {
      $.extend(this, init);
    }
  }

  export class AgreementTimeDetail {
    employeeID: string;
    // 状態
    status: any;
    // 法定上限対象時間
    legalUpperTime: any;
    // 36協定対象時間
    agreementTime: any;
    // 年月
    yearMonth: number;
    // 内訳
    breakdown: any;
    constructor(init?: Partial<CurrentClosingPeriod>) {
      $.extend(this, init);
    }
  }

  export class AcquisitionOfOvertimeHoursOfEmployeesDto {
    // 配下社員の個人情報
    personalInformationOfSubordinateEmployees: any;
    // 配下社員の時間外時間
    OvertimeOfSubordinateEmployees: any;
  }

  //上長用の時間外時間表示
  export class OvertimedDisplayForSuperiorsDto {
    // ログイン者の締めID
    closureId: number;
    // 当月の締め情報
    closingInformationForCurrentMonth: CurrentClosingPeriod;
    // 配下社員の個人情報
    personalInformationOfSubordinateEmployees: PersonEmpBasicInfoImport[];
    // 配下社員の時間外時間
    overtimeOfSubordinateEmployees: AgreementTimeDetail[];
    // 翌月の締め情報
    closingInformationForNextMonth: CurrentClosingPeriod;
  }
}
