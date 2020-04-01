module nts.uk.com.view.cmm011.v2.b.viewmodel {
    // import block = nts.uk.ui.block;
    // import getText = nts.uk.resource.getText;
    // import confirm = nts.uk.ui.dialog.confirm;
    // import alertError = nts.uk.ui.dialog.alertError;
    // import info = nts.uk.ui.dialog.info;
    // import modal = nts.uk.ui.windows.sub.modal;
    // import setShared = nts.uk.ui.windows.setShared;
    // import getShared = nts.uk.ui.windows.getShared;
    // const DEFAULT_END = "9999/12/31";

    export class ScreenModel {
//         initMode: KnockoutObservable<number> = ko.observable(INIT_MODE.WORKPLACE);
//         screenMode: KnockoutObservable<number> = ko.observable(SCREEN_MODE.NEW);
//         lstWpkHistory: KnockoutObservableArray<HistoryItem>;
//         selectedHistoryId: KnockoutObservable<string>;
//         bkHistoryId: string;
//         bkStartDate: string;
//         bkEndDate: string;
//         selectedStartDateInput: KnockoutObservable<string>;
//         selectedStartDateText: KnockoutObservable<string>;
//         selectedEndDate: KnockoutObservable<string>;
//         copyPreviousConfig: KnockoutObservable<boolean>;
//         isLatestHistory: KnockoutObservable<boolean>;
        
//         constructor() {
//             let self = this, params = getShared("CMM011AParams");
//             self.lstWpkHistory = ko.observableArray([]);
//             self.selectedHistoryId = ko.observable(null);
//             self.selectedStartDateInput = ko.observable(null);
//             self.selectedStartDateText = ko.observable(null);
//             self.selectedEndDate = ko.observable(DEFAULT_END);
//             self.copyPreviousConfig = ko.observable(false);
//             if (params) {
//                 self.initMode(params.initMode);
//                 self.selectedHistoryId(params.historyId);
//                 self.bkHistoryId = params.historyId;
//             }
//             if (self.initMode() == INIT_MODE.DEPARTMENT) {
//                 let currentScreen = nts.uk.ui.windows.getSelf();
//                 currentScreen.setTitle(getText("CMM011_302"));
//             }
//             self.selectedHistoryId.subscribe(value => {
//                 if (value) {
//                     let history: HistoryItem = _.find(self.lstWpkHistory(), i => i.historyId == value);
//                     if (history) {
//                         self.selectedStartDateInput(history.startDate);
//                         self.selectedStartDateText(history.startDate);
//                         self.selectedEndDate(history.endDate);
//                     }
//                     self.screenMode(SCREEN_MODE.SELECT);
//                 } else {
// //                    if (self.screenMode() == SCREEN_MODE.NEW || self.screenMode() == SCREEN_MODE.ADD)
// //                        self.selectedEndDate(DEFAULT_END);
//                 }
//             });
//             self.isLatestHistory = ko.computed(() => {
//                 return !_.isEmpty(self.lstWpkHistory()) && self.selectedHistoryId() == self.lstWpkHistory()[0].historyId;
//             }, this);
//         }
        
//         startPage(): JQueryPromise<any> {
//             let self = this, dfd = $.Deferred();
//             block.invisible();
//             service.getAllConfiguration(self.initMode()).done(data => {
//                 if (data) {
//                     self.lstWpkHistory(_.map(data, i => new HistoryItem(i)));
//                     let selectedHist = _.find(data, h => h.historyId == self.bkHistoryId);
//                     if (selectedHist && self.bkStartDate == null && self.bkEndDate == null) {
//                         self.bkStartDate = selectedHist.startDate;
//                         self.bkEndDate = selectedHist.endDate;
//                     }
//                     if (self.selectedHistoryId() != null)
//                         self.selectedHistoryId.valueHasMutated();
//                     else
//                         self.selectedHistoryId(self.lstWpkHistory()[0].historyId);
//                 }
//                 dfd.resolve();
//             }).fail((error) => {
//                 dfd.reject();
//                 alertError(error);
//             }).always(() => {
//                 block.clear()
//             });
//             return dfd.promise();
//         }
        
//         addHistory() {
//             let self = this;
//             self.selectedHistoryId(self.lstWpkHistory()[0].historyId);
//             self.screenMode(SCREEN_MODE.ADD);
//             self.selectedStartDateInput(null)
//             self.selectedEndDate(DEFAULT_END);
//         }
        
//         updateHistory() {
//             let self = this;
//             self.screenMode(SCREEN_MODE.UPDATE);
//         }
        
//         deleteHistory() {
//             let self = this;
//             confirm({ messageId: "Msg_18" }).ifYes(() => {
//                 block.invisible();
//                 let data = { historyId: self.selectedHistoryId(), initMode: self.initMode() };
//                 service.deleteConfiguration(data).done(() => {
//                     self.startPage().done(() => {
//                         self.selectedHistoryId(self.lstWpkHistory()[0].historyId);
//                     });
//                 }).fail(error => {
//                     alertError(error);
//                 }).always(() => {
//                     block.clear();
//                 });
//             }).ifNo(() => {
//             });
//         }
        
//         registerConfig() {
//             let self = this, data = null;
//             $(".nts-input").trigger("validate");
//             if (nts.uk.ui.errors.hasError()) 
//                 return;
//             block.invisible();
//             let startDate = moment.utc(self.selectedStartDateInput(), "YYYY/MM/DD"),
//                 endDate = moment.utc(self.selectedEndDate(), "YYYY/MM/DD");
//             switch (self.screenMode()) {
//                 case SCREEN_MODE.NEW:
//                     data = {
//                         initMode: self.initMode(),
//                         newHistoryId: null,
//                         prevHistoryId: null,
//                         startDate: startDate.toISOString(),
//                         endDate: endDate.toISOString(),
//                         copyPreviousConfig: false
//                     };
//                     service.addConfiguration(data).done((historyId) => {
//                         self.startPage().done(() => {
//                             self.selectedHistoryId(historyId);
//                             self.sendDataToParentScreen();
//                         });
//                     }).fail((error) => {
//                         alertError(error);
//                     }).always(() => {
//                         block.clear();
//                     });
//                     break;
//                 case SCREEN_MODE.ADD:
//                     data = {
//                         initMode: self.initMode(),
//                         newHistoryId: null,
//                         prevHistoryId: self.lstWpkHistory()[0].historyId,
//                         startDate: startDate.toISOString(),
//                         endDate: endDate.toISOString(),
//                         copyPreviousConfig: self.copyPreviousConfig()
//                     };
//                     service.addConfiguration(data).done((historyId) => {
//                         self.startPage().done(() => {
//                             self.selectedHistoryId(historyId);
//                             self.sendDataToParentScreen();
//                         });
//                     }).fail((error) => {
//                         alertError(error);
//                     }).always(() => {
//                         block.clear();
//                     });
//                     break;
//                 case SCREEN_MODE.UPDATE:
//                     data = {
//                         initMode: self.initMode(),
//                         historyId: self.selectedHistoryId(),
//                         startDate: startDate.toISOString(),
//                         endDate: endDate.toISOString()
//                     };
//                     service.updateConfiguration(data).done(() => {
//                         self.startPage().done(() => {
//                             self.selectedHistoryId.valueHasMutated();
//                             self.sendDataToParentScreen();
//                         });
//                     }).fail((error) => {
//                         alertError(error);
//                     }).always(() => {
//                         block.clear();
//                     });
//                     break;
//                 default:
//                     block.clear();
//                     if (self.selectedHistoryId())
//                         self.sendDataToParentScreen(); 
//                     break;
//             }
//         }
        
//         sendDataToParentScreen() {
//             let self = this;
//             let params = { 
//                 historyId: self.selectedHistoryId(), 
//                 startDate: self.selectedStartDateText(), 
//                 endDate: self.selectedEndDate() 
//             };
//             setShared("CMM011BParams", params);
//             nts.uk.ui.windows.close();
//         }
        
//         cancel() {
//             let self = this;
//             let preSelectHist = _.find(self.lstWpkHistory(), h => h.historyId == self.bkHistoryId);
//             if (preSelectHist && (preSelectHist.startDate != self.bkStartDate || preSelectHist.endDate != self.bkEndDate)) {
//                 setShared("CMM011BParams", { 
//                     historyId: preSelectHist.historyId, 
//                     startDate: preSelectHist.startDate, 
//                     endDate: preSelectHist.endDate 
//                 });
//             } else if (preSelectHist == null) {
//                 setShared("CMM011BParams", { 
//                     historyId: self.lstWpkHistory()[0].historyId, 
//                     startDate: self.lstWpkHistory()[0].startDate, 
//                     endDate: self.lstWpkHistory()[0].endDate 
//                 });
//             }
//             nts.uk.ui.windows.close();
//         }
    }
    
    // enum INIT_MODE {
    //     WORKPLACE = 0,
    //     DEPARTMENT = 1
    // }
    
    // enum SCREEN_MODE {
    //     SELECT = 0,
    //     NEW = 1,
    //     ADD = 2,
    //     UPDATE = 3
    // }
    
    // class HistoryItem {
    //     historyId: string;
    //     startDate: string;
    //     endDate: string;
    //     displayText: string;
        
    //     constructor(params) {
    //         if (params) {
    //             this.historyId = params.historyId;
    //             this.startDate = params.startDate;
    //             this.endDate = params.endDate;
    //             this.displayText = params.startDate + " " + getText("CMM011_125") + " " + params.endDate;
    //         }
    //     }
    // }
}