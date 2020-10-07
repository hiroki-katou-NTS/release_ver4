module nts.uk.at.view.kbt002.c {


  @bean()
  export class KBT002CViewModel extends ko.ViewModel {
    execItemCd: KnockoutObservable<string> = ko.observable('');
    execItemName: KnockoutObservable<string> = ko.observable('');
    curExecSetting: KnockoutObservable<ExecutionSettingModel> = ko.observable(new ExecutionSettingModel(null));
    monthDays: KnockoutObservable<string> = ko.observable('');
    executionTypes: KnockoutObservableArray<any>;
    currentDate: string;
    currentTime: number;
    selectExec: KnockoutObservable<number> = ko.observable(1);
    isSelectExec: KnockoutObservable<boolean> = ko.observable(true);
    timeRepeats: KnockoutObservableArray<any>;
    selectTimeRepeat: KnockoutObservable<number> = ko.observable(2);
    isSelectTimeRepeat: KnockoutObservable<boolean> = ko.observable(true);
    endTimes: KnockoutObservableArray<any>;
    selectEndTime: KnockoutObservable<number> = ko.observable(2);
    isSelectEndTime: KnockoutObservable<boolean> = ko.observable(true);
    endDates: KnockoutObservableArray<any>;
    selectEndDate: KnockoutObservable<number> = ko.observable(2);
    isSelectEndDate: KnockoutObservable<boolean> = ko.observable(true);
    isHiddenTime : KnockoutObservable<boolean> = ko.observable(false);
    isHiddenWeek : KnockoutObservable<boolean> = ko.observable(false);
    isHiddenMonth : KnockoutObservable<boolean> = ko.observable(false);
    isHiddenTimeRepeat : KnockoutObservable<boolean> = ko.observable(false);
    lstOneDayRepInterval: KnockoutObservableArray<OneDayRepIntervalModel>;
  
    created(){
      const vm = this;
      let dfd = $.Deferred<void>();
      let today = moment();
      vm.currentDate = today.format("YYYY/MM/DD");
      vm.currentTime = today.hour() * 60 + today.minute();

      vm.lstOneDayRepInterval = ko.observableArray([
        {code: 0, name: "1分"},
        {code: 1, name: "5分"},
        {code: 2, name: "10分"},
        {code: 3, name: "15分"},
        {code: 4, name: "20分"},
        {code: 5, name: "30分"},
        {code: 6, name: "60分"}
      ]);

      vm.executionTypes = ko.observableArray([
        { code: 1, name: vm.$i18n('KBT002_252') },
        { code: 2, name: vm.$i18n('KBT002_253') },
        { code: 3, name: vm.$i18n('KBT002_254') },
        { code: 4, name: vm.$i18n('KBT002_255') }
      ]);

      vm.timeRepeats = ko.observableArray([
        { code: 1, name: vm.$i18n('KBT002_67') },
        { code: 2, name: vm.$i18n('KBT002_68') }
      ]);

      vm.endTimes = ko.observableArray([
        { code: 1, name: vm.$i18n('KBT002_141') },
        { code: 2, name: vm.$i18n('KBT002_142') }
      ]);

      vm.endDates = ko.observableArray([
        { code: 1, name: vm.$i18n('KBT002_71') },
        { code: 2, name: vm.$i18n('KBT002_72') }
      ]);

      const dataStored = vm.$window.storage('inputDialogC');
      if (dataStored){
        dataStored.done((item: dataStoredDto) => {
          vm.execItemCd(item.execItemCd);
          vm.execItemName(item.execItemName);
        } )
        vm.getExecSetting();
      }
      dfd.resolve();

      return dfd.promise();
    }

    getExecSetting(){
      const vm = this;
      vm.$blockui('grayout');
      service.getExecSetting('01').then((item: ExecutionSettingDto) => {
        vm.$blockui('clear');
        if (item){
          vm.curExecSetting(new ExecutionSettingModel(item, vm.currentDate, vm.currentTime));
          vm.monthDays(vm.buildMonthDaysStr());
        }
      })
      .always(() => {
        vm.$blockui("clear");
      });
      console.log(vm.curExecSetting());
    }

    decide() {
      const vm = this;
      if (vm.$validate()) {
        return;
      }

    }

    optionTimeRepeat() {
      const vm = this;
      if (vm.selectTimeRepeat() == 1) {
        vm.curExecSetting().oneDayRepCls(0);
        vm.isSelectEndTime(false);
        vm.curExecSetting().endTimeCls(0);
      } else {
        vm.curExecSetting().oneDayRepCls(1);
        vm.isSelectEndTime(true);
      }
    }

    optionEndDate(){
      const vm = this;
      if(vm.selectEndDate() == 1) {
        vm.curExecSetting().endDateCls(0);
        vm.curExecSetting().repeatCls(false);
      } else {
        vm.curExecSetting().endDateCls(1);
        vm.curExecSetting().repeatCls(true);
      }
    }

    optionEndTime() {
      const vm = this;;
      if (vm.selectEndTime() == 1 ) {
        vm.curExecSetting().endTimeCls(0);
      } else {
        vm.curExecSetting().endTimeCls(1);
      }
    }

    openDialogD() {
      const vm = this;
      vm.$blockui('grayout');
      const openDialogD = {repeatMonthDateList: vm.curExecSetting().repeatMonthDateList};
      vm.$window.modal('/view/kbt/002/d/index.xhtml',openDialogD).then((result:any) => {
        vm.$blockui('clear');
        vm.curExecSetting().repeatMonthDateList =  result.selectedDays;
      })
      .always(() => {
        vm.$blockui('clear');
      });
    }

    closeDialog() {
      const vm = this;
      vm.$window.close();
    }

    private buildMonthDaysStr() {
      const vm = this;
      let monthDaysText = '';
      const listSize = vm.curExecSetting().repeatMonthDateList().length;
      _.each(vm.curExecSetting().repeatMonthDateList(), (data, index) => {
        if (data == 32) {
          monthDaysText += '最終';
        } else { 
          monthDaysText += data;
        }
        monthDaysText += '日';
        if (index > listSize - 1) {
          monthDaysText += ',';
        }
      });
      return monthDaysText;
    }

    getEnumData() {
      // const vm = this;
      // vm.$blockui('grayout');
      // service.getEnumDataList().then(() => {
      //   vm.$blockui("clear");

      // })
      // .always(() => {
      //   vm.$blockui("clear");
      // })
    }


    optionDialog() {
      const vm = this;
      const indexSelectExec = vm.selectExec();
      if (indexSelectExec === 1 ) {
        vm.isHiddenTime(false)
        vm.isHiddenWeek(false)
        vm.isHiddenMonth(false)
      };
      if (indexSelectExec === 2 ) {
        vm.isHiddenTime(true)
        vm.isHiddenWeek(false)
        vm.isHiddenMonth(false)
      }; 
      if(indexSelectExec === 3 ) {
        vm.isHiddenTime(true)
        vm.isHiddenWeek(true)
        vm.isHiddenMonth(false)
      };
      if(indexSelectExec === 4 ) {
        vm.isHiddenTime(true)
        vm.isHiddenWeek(false)
        vm.isHiddenMonth(true)
      };
    }

  }
  export class OneDayRepIntervalModel {
    code: number;
    name: string;

    constructor (init?: Partial<OneDayRepIntervalModel>) {
      $.extend(this, init)
    } 
  }
  
  export interface ExecutionSettingDto {
    companyId: string;
    execItemCd: string;
    startDate: string;
    startTime: number;
    endTimeCls: number;
    endTime: number;
    oneDayRepClassification: number;
    oneDayRepInterval: number;
    repeatCls: boolean;
    repeatContent: number;
    endDateCls: number;
    endDate: string;
    enabledSetting: boolean;
    monday: boolean;
    tuesday: boolean;
    wednesday: boolean;
    thursday: boolean;
    friday: boolean;
    saturday: boolean;
    sunday: boolean;
    january: boolean;
    february: boolean;
    march: boolean;
    april: boolean;
    may: boolean;
    june: boolean;
    july: boolean;
    august: boolean;
    september: boolean;
    october: boolean;
    november: boolean;
    december: boolean;
    repeatMonthDateList: number[];
  }

  export class ExecutionSettingModel {
    companyId: KnockoutObservable<string> = ko.observable('');
    execItemCd: KnockoutObservable<string> = ko.observable('');
    startDate: KnockoutObservable<string> = ko.observable('');
    startTime: KnockoutObservable<number> = ko.observable(null);
    endTimeCls: KnockoutObservable<number> = ko.observable(null);
    endTime: KnockoutObservable<number> = ko.observable(null);
    oneDayRepCls: KnockoutObservable<number> = ko.observable(null);
    oneDayRepInterval: KnockoutObservable<number> = ko.observable(null);
    repeatCls: KnockoutObservable<boolean> = ko.observable(false);
    repeatContent: KnockoutObservable<number> = ko.observable(0);
    endDateCls: KnockoutObservable<number> = ko.observable(null);
    endDate: KnockoutObservable<string> = ko.observable('');
    enabledSetting: KnockoutObservable<boolean> = ko.observable(false);
    monday: KnockoutObservable<boolean> = ko.observable(false);
    tuesday: KnockoutObservable<boolean> = ko.observable(false);
    wednesday: KnockoutObservable<boolean> = ko.observable(false);
    thursday: KnockoutObservable<boolean> = ko.observable(false);
    friday: KnockoutObservable<boolean> = ko.observable(false);
    saturday: KnockoutObservable<boolean> = ko.observable(false);
    sunday: KnockoutObservable<boolean> = ko.observable(false);
    january: KnockoutObservable<boolean> = ko.observable(false);
    february: KnockoutObservable<boolean> = ko.observable(false);
    march: KnockoutObservable<boolean> = ko.observable(false);
    april: KnockoutObservable<boolean> = ko.observable(false);
    may: KnockoutObservable<boolean> = ko.observable(false);
    june: KnockoutObservable<boolean> = ko.observable(false);
    july: KnockoutObservable<boolean> = ko.observable(false);
    august: KnockoutObservable<boolean> = ko.observable(false);
    september: KnockoutObservable<boolean> = ko.observable(false);
    october: KnockoutObservable<boolean> = ko.observable(false);
    november: KnockoutObservable<boolean> = ko.observable(false);
    december: KnockoutObservable<boolean> = ko.observable(false);
    repeatMonthDateList: KnockoutObservableArray<number> = ko.observableArray([]);
    constructor(param: ExecutionSettingDto, curDate?: any, curTime?: any) {
      let vm = this;
      if (param && param != null) {
        vm.companyId(param.companyId || '');
        vm.execItemCd(param.execItemCd || '');
        vm.startDate(param.startDate || curDate);
        param.startTime == 0 ? vm.startTime(param.startTime) : vm.startTime(param.startTime || curTime);
        vm.endTimeCls(param.endTimeCls);
        param.endTime == 0 ? vm.endTime(param.endTime) : vm.endTime(param.endTime || curTime);
        vm.oneDayRepCls(param.oneDayRepClassification);
        vm.oneDayRepInterval(param.oneDayRepInterval || 0);
        vm.repeatCls(param.repeatCls || false);
        vm.repeatContent(param.repeatContent || 0);
        vm.endDateCls(param.endDateCls);
        vm.endDate(param.endDate || curDate);
        vm.enabledSetting(param.enabledSetting || false);
        vm.monday(param.monday || false);
        vm.tuesday(param.tuesday || false);
        vm.wednesday(param.wednesday || false);
        vm.thursday(param.thursday || false);
        vm.friday(param.friday || false);
        vm.saturday(param.saturday || false);
        vm.sunday(param.sunday || false);
        vm.january(param.january || false);
        vm.february(param.february || false);
        vm.march(param.march || false);
        vm.april(param.april || false);
        vm.may(param.may || false);
        vm.june(param.june || false);
        vm.july(param.july || false);
        vm.august(param.august || false);
        vm.september(param.september || false);
        vm.october(param.october || false);
        vm.november(param.november || false);
        vm.december(param.december || false);
        vm.repeatMonthDateList(param.repeatMonthDateList || []);
      } else {
        vm.companyId('');
        vm.execItemCd('');
        vm.startDate(curDate);
        vm.startTime(curTime);
        vm.endTimeCls(0);
        vm.endTime(curTime);
        vm.oneDayRepCls(0);
        vm.oneDayRepInterval(0);
        vm.repeatCls(false);
        vm.repeatContent(0);
        vm.endDateCls(0);
        vm.endDate(curDate);
        vm.enabledSetting(false);
        vm.monday(false);
        vm.tuesday(false);
        vm.wednesday(false);
        vm.thursday(false);
        vm.friday(false);
        vm.saturday(false);
        vm.sunday(false);
        vm.january(false);
        vm.february(false);
        vm.march(false);
        vm.april(false);
        vm.may(false);
        vm.june(false);
        vm.july(false);
        vm.august(false);
        vm.september(false);
        vm.october(false);
        vm.november(false);
        vm.december(false);
        vm.repeatMonthDateList([]);
      }
    }
  }

  export interface dataStoredDto {
    execItemCd: string;
    execItemName: string;
  }
}

// module nts.uk.at.view.kbt002.c {
//   export module viewmodel {
//     import alert = nts.uk.ui.dialog.alert;
//     import modal = nts.uk.ui.windows.sub.modal;
//     import setShared = nts.uk.ui.windows.setShared;
//     import getShared = nts.uk.ui.windows.getShared;
//     import block = nts.uk.ui.block;
//     import dialog = nts.uk.ui.dialog;
//     import isNullOrEmpty = nts.uk.text.isNullOrEmpty;
//     const  vm.$i18n = nts.uk.resource.getText;

//     export class ScreenModel {
//       repeatChoice: KnockoutObservableArray<any>;
//       execItemCd: KnockoutObservable<string> = ko.observable('');
//       execItemName: KnockoutObservable<string> = ko.observable('');
//       curExecSetting: KnockoutObservable<ExecutionSetting> = ko.observable(new ExecutionSetting(null));
//       monthDays: KnockoutObservable<string> = ko.observable('');
//       contentList: KnockoutObservableArray<any> = ko.observableArray([]);
//       currentDate: string;
//       currentTime: number;
//       executionTypes: KnockoutObservableArray<any>;
//       selectExec: KnockoutObservable<number> = ko.observable(1);
//       isSelectExec: KnockoutObservable<boolean> = ko.observable(true);
//       timeRepeats: KnockoutObservableArray<any>;
//       selectTimeRepeat: KnockoutObservable<number> = ko.observable(2);
//       isSelectTimeRepeat: KnockoutObservable<boolean> = ko.observable(true);
//       endTimes: KnockoutObservableArray<any>;
//       selectEndTime: KnockoutObservable<number> = ko.observable(2);
//       isSelectEndTime: KnockoutObservable<boolean> = ko.observable(true);
//       endDates: KnockoutObservableArray<any>;
//       selectEndDate: KnockoutObservable<number> = ko.observable(2);
//       isSelectEndDate: KnockoutObservable<boolean> = ko.observable(true);
//       isHiddenTime : KnockoutObservable<boolean> = ko.observable(false);
//       isHiddenWeek : KnockoutObservable<boolean> = ko.observable(false);
//       isHiddenMonth : KnockoutObservable<boolean> = ko.observable(false);
//       isHiddenTimeRepeat : KnockoutObservable<boolean> = ko.observable(false);


//       //            monthDaysList : KnockoutObservableArray<any> = ko.observableArray([]);
//       //            repeatContentItemList : KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
//       //            
//       settingChoice: KnockoutObservableArray<any>;
//       lstOneDayRepInterval: KnockoutObservableArray<OneDayRepIntervalModel>;

//       // Screen mode
//       isNewMode: KnockoutObservable<boolean> = ko.observable(false);
//       constructor() {
//         var vm = this;
//         vm.repeatChoice = ko.observableArray([
//           { id: 1, name: nts.uk.resource.getText('KBT002_61') },
//           { id: 0, name: nts.uk.resource.getText('KBT002_62') }
//         ]);

//         vm.settingChoice = ko.observableArray([
//           { id: 1, name: nts.uk.resource.getText('KBT002_138') },
//           { id: 0, name: nts.uk.resource.getText('KBT002_139') }
//         ]);

//         vm.lstOneDayRepInterval = ko.observableArray([
//           new OneDayRepIntervalModel(0, "1分"),
//           new OneDayRepIntervalModel(1, "5分"),
//           new OneDayRepIntervalModel(2, "10分"),
//           new OneDayRepIntervalModel(3, "15分"),
//           new OneDayRepIntervalModel(4, "20分"),
//           new OneDayRepIntervalModel(5, "30分"),
//           new OneDayRepIntervalModel(6, "60分")
//         ]);


//         vm.executionTypes = ko.observableArray([
//           { code: 1, name:  vm.$i18n('KBT002_252') },
//           { code: 2, name:  vm.$i18n('KBT002_253') },
//           { code: 3, name:  vm.$i18n('KBT002_254') },
//           { code: 4, name:  vm.$i18n('KBT002_255') }
//         ]);
//         vm.timeRepeats = ko.observableArray([
//           { code: 1, name:  vm.$i18n('KBT002_67') },
//           { code: 2, name:  vm.$i18n('KBT002_68') }
//         ]);

//         vm.endTimes = ko.observableArray([
//           { code: 1, name:  vm.$i18n('KBT002_141') },
//           { code: 2, name:  vm.$i18n('KBT002_142') }
//         ]);

//         vm.endDates = ko.observableArray([
//           { code: 1, name:  vm.$i18n('KBT002_71') },
//           { code: 2, name:  vm.$i18n('KBT002_72') }
//         ])
//       }

//       // Start page
//       start() {
//         const vm = this;
//         let dfd = $.Deferred<void>();
//         // init current date and time 
//         var today = moment();
//         let targetDateStr = '今日' + today.format("YYYY/MM/DD") + '実行すると、作成期間は';
//         vm.currentDate = today.format("YYYY/MM/DD");
//         vm.currentTime = today.hour() * 60 + today.minute();

//         var sharedData = nts.uk.ui.windows.getShared('inputDialogC');
//         if (sharedData) {
//           // vm.execItemCd(sharedData.execItemCd);
//           // vm.execItemName(sharedData.execItemName);
//           //                    vm.contentList(sharedData.repeatContent);
//         }
//         vm.execItemCd('01');
//         vm.execItemName('KBT002C');
//         service.getEnumDataList().done(function (setting) {

//           vm.contentList(setting.repeatContentItems);
//           $.when(vm.getExecSetting(vm.execItemCd())).done((data: any) => {
//             //                        vm.curExecSetting().repeatContent.subscribe(contentId => {
//             //                            vm.displayDetailSetting(contentId);
//             //                        });
//             dfd.resolve();
//           });
//         });
//         return dfd.promise();
//       }

//       private getExecSetting(savedExecSettingCd: string) {
//         const vm = this;
//         let dfd = $.Deferred<void>();
//         service.getExecSetting(savedExecSettingCd).done(function (execSetting) {
//           if (execSetting && execSetting != null) {
//             vm.isNewMode(false);
//             vm.curExecSetting(new ExecutionSetting(execSetting, vm.currentDate, vm.currentTime));
//             vm.displayDetailSetting(vm.curExecSetting().repeatContent());
//             //                        vm.curExecSetting().repeatContent();
//             vm.monthDays(vm.buildMonthDaysStr());
//           } else {
//             vm.isNewMode(true);
//             vm.curExecSetting(new ExecutionSetting(null, vm.currentDate, vm.currentTime));
//             vm.displayDetailSetting(0);
//             vm.curExecSetting().execItemCd(vm.execItemCd());
//           }
//           vm.curExecSetting().repeatContent.subscribe(contentId => {
//             vm.displayDetailSetting(contentId);
//           });
//           dfd.resolve();
//         });
//         return dfd.promise();
//       }

//       displayDetailSetting(contentId) {
//         $('.detailSettingDiv').hide();
//         if (contentId == 0) {
//           $('#daily').show();
//         } else if (contentId == 1) {
//           $('#weekly').show();
//         } else if (contentId == 2) {
//           $('#monthly').show();
//         }
//       }

//       decide() {
//         const vm = this;
//         var dfd = $.Deferred();

//         // validate
//         if (vm.validate()) {
//           return;
//         }
//         if (vm.curExecSetting().repeatContent() == 2 && (vm.monthDays() == '' || nts.uk.util.isNullOrUndefined(vm.monthDays()))) {
//           nts.uk.ui.dialog.alert({ messageId: "Msg_846" });
//           return;
//         }
//         nts.uk.ui.block.grayout();

//         if (!vm.isNewMode()) {
//           // 登録されている

//           // 情報メッセージ「#Msg_855」を表示する
//           nts.uk.ui.dialog.info({ messageId: "Msg_855" }).then(() => {
//             vm.saveExecSetting();
//           });
//         } else {
//           vm.saveExecSetting();
//         }
//         // insert or update process execution setting


//         dfd.resolve();
//         return dfd.promise();
//       }

//       private saveExecSetting() {
//         const vm = this;
//         var dfd = $.Deferred();

//         let command: any = vm.toJsonObject();
//         service.saveExecSetting(command).done(function (savedExecSettingCd: any) {
//           nts.uk.ui.block.clear();

//           // notice success
//           nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
//             $.when(vm.getExecSetting(savedExecSettingCd)).done(() => {
//               dfd.resolve();
//             });
//           });
//         }).fail((res: any) => {
//           nts.uk.ui.block.clear();
//           vm.showMessageError(res);
//         });

//         dfd.resolve();
//         return dfd.promise();
//       }

//       closeDialog() {
//         nts.uk.ui.windows.close();
//       }

//       openDialogD() {
//         const vm = this;
//         block.grayout();
//         setShared('inputDialogD',
//           {
//             repeatMonthDateList: vm.curExecSetting().repeatMonthDateList()
//           });
//         modal("/view/kbt/002/d/index.xhtml").onClosed(function () {
//           var sharedDataD = getShared('outputDialogD');
//           vm.curExecSetting().repeatMonthDateList(sharedDataD.selectedDays);

//           vm.monthDays(vm.buildMonthDaysStr());
//           block.clear();
//         });
//       }

//       optionDialog(){
//         const vm = this;
//         const indexSelectExec = vm.selectExec();
//         if(indexSelectExec === 1 ) {
//           vm.isHiddenTime(false)
//           vm.isHiddenWeek(false)
//           vm.isHiddenMonth(false)
//         }
//         if(indexSelectExec === 2 ){
//           vm.isHiddenTime(true)
//           vm.isHiddenWeek(false)
//           vm.isHiddenMonth(false)
//         }if(indexSelectExec === 3 ){
//           vm.isHiddenTime(true)
//           vm.isHiddenWeek(true)
//           vm.isHiddenMonth(false)
//         }
//         if(indexSelectExec === 4 ){
//           vm.isHiddenTime(true)
//           vm.isHiddenWeek(false)
//           vm.isHiddenMonth(true)
//         }
//       }

//       optionTimeRepeat(){
        
//       }

//       private buildMonthDaysStr() {
//         const vm = this;
//         var monthDaysText = '';
//         var listSize = vm.curExecSetting().repeatMonthDateList().length;
//         _.each(vm.curExecSetting().repeatMonthDateList(), (value, index) => {
//           if (value == 32) {
//             monthDaysText += '最終';
//           } else {
//             monthDaysText += value;
//           }
//           monthDaysText += '日';
//           if (index < listSize - 1) {
//             monthDaysText += " + "
//           }
//         });
//         return monthDaysText;
//       }

//       /**
//        * toJsonObject
//        */
//       private toJsonObject(): any {
//         const vm = this;

//         // to JsObject
//         let command: any = {};
//         command.newMode = vm.isNewMode();

//         command.companyId = vm.curExecSetting().companyId();
//         command.execItemCd = vm.curExecSetting().execItemCd();
//         command.startDate = vm.curExecSetting().startDate();
//         command.startTime = vm.curExecSetting().startTime();
//         command.endTimeCls = vm.curExecSetting().endTimeCls();
//         command.endTime = vm.curExecSetting().endTimeCls() == 1 ? vm.curExecSetting().endTime() : null;
//         command.oneDayRepCls = vm.curExecSetting().oneDayRepCls();
//         command.oneDayRepInterval = vm.curExecSetting().oneDayRepCls() == 1 ? vm.curExecSetting().oneDayRepInterval() : null;
//         command.repeatCls = vm.curExecSetting().repeatCls();
//         command.repeatContent = vm.curExecSetting().repeatContent();
//         command.endDateCls = vm.curExecSetting().endDateCls();
//         command.endDate = vm.curExecSetting().endDateCls() == 1 ? vm.curExecSetting().endDate() : null;
//         command.enabledSetting = vm.curExecSetting().enabledSetting();
//         command.monday = vm.curExecSetting().monday();
//         command.tuesday = vm.curExecSetting().tuesday();
//         command.wednesday = vm.curExecSetting().wednesday();
//         command.thursday = vm.curExecSetting().thursday();
//         command.friday = vm.curExecSetting().friday();
//         command.saturday = vm.curExecSetting().saturday();
//         command.sunday = vm.curExecSetting().sunday();
//         command.january = vm.curExecSetting().january();
//         command.february = vm.curExecSetting().february();
//         command.march = vm.curExecSetting().march();
//         command.april = vm.curExecSetting().april();
//         command.may = vm.curExecSetting().may();
//         command.june = vm.curExecSetting().june();
//         command.july = vm.curExecSetting().july();
//         command.august = vm.curExecSetting().august();
//         command.september = vm.curExecSetting().september();
//         command.october = vm.curExecSetting().october();
//         command.november = vm.curExecSetting().november();
//         command.december = vm.curExecSetting().december();
//         command.repeatMonthDateList = vm.curExecSetting().repeatMonthDateList();

//         return command;
//       }

//       private validate(): boolean {
//         const vm = this;

//         // clear error
//         //                vm.clearError();
//         // validate
//         //                $(".nts-input ").ntsEditor('validate');
//         $(".ntsDatepicker ").ntsEditor('validate');
//         $("#startTime").ntsEditor('validate');
//         if (vm.curExecSetting().endTimeCls()) {
//           $("#endTime").ntsEditor('validate');
//         }
//         if (vm.curExecSetting().oneDayRepCls()) {
//           $("#oneDayTime").ntsEditor('validate');
//         }


//         return $('.nts-input').ntsError('hasError');
//       }
//       /**
//        * showMessageError
//        */
//       private showMessageError(res: any) {
//         if (res.businessException) {
//           nts.uk.ui.dialog.alert({ messageId: res.messageId, messageParams: res.parameterIds });
//         }
//       }
//     }

//     export interface IExecutionSetting {
//       companyId: string;
//       execItemCd: string;
//       startDate: string;
//       startTime: number;
//       endTimeCls: number;
//       endTime: number;
//       oneDayRepCls: number;
//       oneDayRepInterval: number;
//       repeatCls: boolean;
//       repeatContent: number;
//       endDateCls: number;
//       endDate: string;
//       enabledSetting: boolean;
//       //            nextExecDateTime:    string;
//       monday: boolean;
//       tuesday: boolean;
//       wednesday: boolean;
//       thursday: boolean;
//       friday: boolean;
//       saturday: boolean;
//       sunday: boolean;
//       january: boolean;
//       february: boolean;
//       march: boolean;
//       april: boolean;
//       may: boolean;
//       june: boolean;
//       july: boolean;
//       august: boolean;
//       september: boolean;
//       october: boolean;
//       november: boolean;
//       december: boolean;
//       repeatMonthDateList: Array<number>;
//     }

//     export class ExecutionSetting {
//       companyId: KnockoutObservable<string> = ko.observable('');
//       execItemCd: KnockoutObservable<string> = ko.observable('');
//       startDate: KnockoutObservable<string> = ko.observable('');
//       startTime: KnockoutObservable<number> = ko.observable(null);
//       endTimeCls: KnockoutObservable<number> = ko.observable(null);
//       endTime: KnockoutObservable<number> = ko.observable(null);
//       oneDayRepCls: KnockoutObservable<number> = ko.observable(null);
//       oneDayRepInterval: KnockoutObservable<number> = ko.observable(null);
//       repeatCls: KnockoutObservable<boolean> = ko.observable(false);
//       repeatContent: KnockoutObservable<number> = ko.observable(0);
//       endDateCls: KnockoutObservable<number> = ko.observable(null);
//       endDate: KnockoutObservable<string> = ko.observable('');
//       enabledSetting: KnockoutObservable<boolean> = ko.observable(false);
//       monday: KnockoutObservable<boolean> = ko.observable(false);
//       tuesday: KnockoutObservable<boolean> = ko.observable(false);
//       wednesday: KnockoutObservable<boolean> = ko.observable(false);
//       thursday: KnockoutObservable<boolean> = ko.observable(false);
//       friday: KnockoutObservable<boolean> = ko.observable(false);
//       saturday: KnockoutObservable<boolean> = ko.observable(false);
//       sunday: KnockoutObservable<boolean> = ko.observable(false);
//       january: KnockoutObservable<boolean> = ko.observable(false);
//       february: KnockoutObservable<boolean> = ko.observable(false);
//       march: KnockoutObservable<boolean> = ko.observable(false);
//       april: KnockoutObservable<boolean> = ko.observable(false);
//       may: KnockoutObservable<boolean> = ko.observable(false);
//       june: KnockoutObservable<boolean> = ko.observable(false);
//       july: KnockoutObservable<boolean> = ko.observable(false);
//       august: KnockoutObservable<boolean> = ko.observable(false);
//       september: KnockoutObservable<boolean> = ko.observable(false);
//       october: KnockoutObservable<boolean> = ko.observable(false);
//       november: KnockoutObservable<boolean> = ko.observable(false);
//       december: KnockoutObservable<boolean> = ko.observable(false);
//       repeatMonthDateList: KnockoutObservableArray<number> = ko.observableArray([]);
//       constructor(param: IExecutionSetting, curDate?, curTime?) {
//         let vm = this;
//         if (param && param != null) {
//           vm.companyId(param.companyId || '');
//           vm.execItemCd(param.execItemCd || '');
//           vm.startDate(param.startDate || curDate);
//           if (param.startTime == 0) {
//             vm.startTime(param.startTime);
//           } else {
//             vm.startTime(param.startTime
//               || curTime);
//           }
//           vm.endTimeCls(param.endTimeCls);
//           if (param.endTime == 0) {
//             vm.endTime(param.endTime);
//           } else {
//             vm.endTime(param.endTime
//               || curTime);
//           }
//           vm.oneDayRepCls(param.oneDayRepCls);
//           vm.oneDayRepInterval(param.oneDayRepInterval || 0);
//           vm.repeatCls(param.repeatCls || false);
//           vm.repeatContent(param.repeatContent || 0);
//           vm.endDateCls(param.endDateCls);
//           vm.endDate(param.endDate || curDate);
//           vm.enabledSetting(param.enabledSetting || false);
//           vm.monday(param.monday || false);
//           vm.tuesday(param.tuesday || false);
//           vm.wednesday(param.wednesday || false);
//           vm.thursday(param.thursday || false);
//           vm.friday(param.friday || false);
//           vm.saturday(param.saturday || false);
//           vm.sunday(param.sunday || false);
//           vm.january(param.january || false);
//           vm.february(param.february || false);
//           vm.march(param.march || false);
//           vm.april(param.april || false);
//           vm.may(param.may || false);
//           vm.june(param.june || false);
//           vm.july(param.july || false);
//           vm.august(param.august || false);
//           vm.september(param.september || false);
//           vm.october(param.october || false);
//           vm.november(param.november || false);
//           vm.december(param.december || false);
//           vm.repeatMonthDateList(param.repeatMonthDateList || []);
//         } else {
//           vm.companyId('');
//           vm.execItemCd('');
//           vm.startDate(curDate);
//           vm.startTime(curTime);
//           vm.endTimeCls(0);
//           vm.endTime(curTime);
//           vm.oneDayRepCls(0);
//           vm.oneDayRepInterval(0);
//           vm.repeatCls(false);
//           vm.repeatContent(0);
//           vm.endDateCls(0);
//           vm.endDate(curDate);
//           vm.enabledSetting(false);
//           vm.monday(false);
//           vm.tuesday(false);
//           vm.wednesday(false);
//           vm.thursday(false);
//           vm.friday(false);
//           vm.saturday(false);
//           vm.sunday(false);
//           vm.january(false);
//           vm.february(false);
//           vm.march(false);
//           vm.april(false);
//           vm.may(false);
//           vm.june(false);
//           vm.july(false);
//           vm.august(false);
//           vm.september(false);
//           vm.october(false);
//           vm.november(false);
//           vm.december(false);
//           vm.repeatMonthDateList([]);
//         }
//         //fixed release 14/6
//         //            vm.endTimeCls(0);
//         //            vm.endDateCls(0);
//         //            vm.oneDayRepInterval(0);
//         //            vm.oneDayRepCls(0);
//       }
//     }
//     export class OneDayRepIntervalModel {
//       code: number;
//       name: string;

//       constructor(code: number, name: string) {
//         this.code = code;
//         this.name = name;
//       }
//     }

//   }
// }