module nts.uk.at.view.kbt002.c {
    export module viewmodel {
        import alert = nts.uk.ui.dialog.alert;
        import modal = nts.uk.ui.windows.sub.modal;
        import setShared = nts.uk.ui.windows.setShared;
        import getShared = nts.uk.ui.windows.getShared;
        import block = nts.uk.ui.block;
        import dialog = nts.uk.ui.dialog;
        import isNullOrEmpty = nts.uk.text.isNullOrEmpty
        
        export class ScreenModel {
            repeatChoice: KnockoutObservableArray<any>;
            execItemCd : KnockoutObservable<string> = ko.observable('');
            execItemName : KnockoutObservable<string> = ko.observable('');
            curExecSetting : KnockoutObservable<ExecutionSetting> = ko.observable(new ExecutionSetting(null));
            monthDays : KnockoutObservable<string> = ko.observable('');
            contentList : KnockoutObservableArray<any> = ko.observableArray([]);
            currentDate : string;
            currentTime : number;
//            monthDaysList : KnockoutObservableArray<any> = ko.observableArray([]);
//            repeatContentItemList : KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);
//            
            settingChoice: KnockoutObservableArray<any>;
            
            // Screen mode
            isNewMode: KnockoutObservable<boolean> = ko.observable(false);
            constructor() {
                var self = this;
                self.repeatChoice = ko.observableArray([
                    { id: 1, name: nts.uk.resource.getText('KBT002_61') },
                    { id: 0, name: nts.uk.resource.getText('KBT002_62') }
                ]);

                self.settingChoice = ko.observableArray([
                    { id: 1, name: nts.uk.resource.getText('KBT002_138') },
                    { id: 0, name: nts.uk.resource.getText('KBT002_139') }
                ]);
                
                
            }
            
            // Start page
            start() {
                let self = this;
                let dfd = $.Deferred<void>();
                // init current date and time 
                var today = moment();
                let targetDateStr = '今日' + today.format("YYYY/MM/DD") + '実行すると、作成期間は';
                self.currentDate = today.format("YYYY/MM/DD");
                self.currentTime = today.hour() * 60 + today.minute();
                
                var sharedData = nts.uk.ui.windows.getShared('inputDialogC');
                if (sharedData) {
                    self.execItemCd(sharedData.execItemCd);
                    self.execItemName(sharedData.execItemName);
//                    self.contentList(sharedData.repeatContent);
                }
                service.getEnumDataList().done(function(setting) {
                    self.contentList(setting.repeatContentItems);
                    $.when(self.getExecSetting(self.execItemCd())).done(()=>{
//                        self.curExecSetting().repeatContent.subscribe(contentId => {
//                            self.displayDetailSetting(contentId);
//                        });
                        dfd.resolve();
                    });
                });
                return dfd.promise();
            }
            
            private getExecSetting(savedExecSettingCd) {
                let self = this;
                let dfd = $.Deferred<void>();
                service.getExecSetting(savedExecSettingCd).done(function(execSetting) {
                    if (execSetting && execSetting != null) {
                        self.isNewMode(false);
                        self.curExecSetting(new ExecutionSetting(execSetting, self.currentDate, self.currentTime));
                        self.displayDetailSetting(self.curExecSetting().repeatContent());
//                        self.curExecSetting().repeatContent();
                        self.monthDays(self.buildMonthDaysStr());
                    } else {
                        self.isNewMode(true);
                        self.curExecSetting(new ExecutionSetting(null, self.currentDate, self.currentTime));
                        self.displayDetailSetting(0);
                        self.curExecSetting().execItemCd(self.execItemCd());
                    }
                    self.curExecSetting().repeatContent.subscribe(contentId => {
                        self.displayDetailSetting(contentId);
                    });
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            displayDetailSetting(contentId) {
                $('.detailSettingDiv').hide();
                if (contentId == 0) {
                    $('#daily').show();
                } else if (contentId == 1) {
                    $('#weekly').show();
                } else if (contentId == 2) {
                    $('#monthly').show();
                }
            }
            
            decide() {
                let self = this;
                var dfd = $.Deferred();
                
                // validate
                if (self.validate()) {
                    return;
                }
                nts.uk.ui.block.grayout();
                
                if (!self.isNewMode()) {
                    // 登録されている
                    
                    // 情報メッセージ「#Msg_855」を表示する
                    nts.uk.ui.dialog.info({ messageId: "Msg_855" }).then(() => {
                        self.saveExecSetting();
                    });
                } else {
                    self.saveExecSetting();
                }
                // insert or update process execution setting
                
                
                dfd.resolve(); 
                return dfd.promise();
            }
            
            private saveExecSetting() {
                let self = this;
                var dfd = $.Deferred();
                
                let command: any = self.toJsonObject();
                service.saveExecSetting(command).done(function(savedExecSettingCd) {
                    nts.uk.ui.block.clear();
                    
                    // notice success
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                        $.when(self.getExecSetting(savedExecSettingCd)).done(()=>{
                            dfd.resolve();
                        });
                    });
                }).fail((res: any) => {
                    nts.uk.ui.block.clear();
                    self.showMessageError(res);
                });
                
                dfd.resolve(); 
                return dfd.promise();
            }
            
            closeDialog() {
                nts.uk.ui.windows.close();
            }
            
            openDialogD() {
                let self = this;
                block.grayout();
                setShared('inputDialogD', 
                          { repeatMonthDateList: self.curExecSetting().repeatMonthDateList()
                          });
                modal("/view/kbt/002/d/index.xhtml").onClosed(function(){
                    var sharedData = getShared('outputDialogD');
                    self.curExecSetting().repeatMonthDateList(sharedData.selectedDays);
                    
                    self.monthDays(self.buildMonthDaysStr());
                    block.clear();
                });
            }
            
            private buildMonthDaysStr() {
                let self = this;
                var monthDaysText = '';
                var listSize = self.curExecSetting().repeatMonthDateList().length;
                _.each(self.curExecSetting().repeatMonthDateList(), (value, index) => {
                    if (value == 32) {
                        monthDaysText += '最終日';
                    } else {
                        monthDaysText += value;
                    }
                    monthDaysText += '目';
                    if (index < listSize - 1) {
                        monthDaysText += " + "
                    }
                });
                return monthDaysText;
            }
            
            /**
             * toJsonObject
             */
            private toJsonObject(): any {
                let self = this;

                // to JsObject
                let command: any = {};
                command.newMode = self.isNewMode();
    
                command.companyId = self.curExecSetting().companyId();
                command.execItemCd = self.curExecSetting().execItemCd();
                command.startDate = self.curExecSetting().startDate();
                command.startTime = self.curExecSetting().startTime();
                command.endTimeCls = self.curExecSetting().endTimeCls();
                command.endTime = self.curExecSetting().endTimeCls() == 1 ? self.curExecSetting().endTime() : null;
                command.oneDayRepCls = self.curExecSetting().oneDayRepCls();
                command.oneDayRepInterval = self.curExecSetting().oneDayRepCls() == 1 ? self.curExecSetting().oneDayRepInterval() : null;
                command.repeatCls = self.curExecSetting().repeatCls();
                command.repeatContent = self.curExecSetting().repeatContent();
                command.endDateCls = self.curExecSetting().endDateCls();
                command.endDate = self.curExecSetting().endDateCls() == 1 ? self.curExecSetting().endDate() : null;
                command.enabledSetting = self.curExecSetting().enabledSetting();
                command.repIntervalDay = self.curExecSetting().repIntervalDay();
                command.repIntervalWeek = self.curExecSetting().repIntervalWeek();
                command.monday = self.curExecSetting().monday();
                command.tuesday = self.curExecSetting().tuesday();
                command.wednesday = self.curExecSetting().wednesday();
                command.thursday = self.curExecSetting().thursday();
                command.friday = self.curExecSetting().friday();
                command.saturday = self.curExecSetting().saturday();
                command.sunday = self.curExecSetting().sunday();
                command.january = self.curExecSetting().january();
                command.february = self.curExecSetting().february();
                command.march = self.curExecSetting().march();
                command.april = self.curExecSetting().april();
                command.may = self.curExecSetting().may();
                command.june = self.curExecSetting().june();
                command.july = self.curExecSetting().july();
                command.august = self.curExecSetting().august();
                command.september = self.curExecSetting().september();
                command.october = self.curExecSetting().october();
                command.november = self.curExecSetting().november();
                command.december = self.curExecSetting().december();
                command.repeatMonthDateList = self.curExecSetting().repeatMonthDateList();

                return command;
            }
            
            private validate(): boolean {
                let self = this;
                
                // clear error
//                self.clearError();
                // validate
//                $(".nts-input ").ntsEditor('validate');
                $(".ntsDatepicker ").ntsEditor('validate');
                $("#startTime").ntsEditor('validate');
                if (self.curExecSetting().endTimeCls()) {
                    $("#endTime").ntsEditor('validate');
                }                
                if (self.curExecSetting().oneDayRepCls()) {
                    $("#oneDayTime").ntsEditor('validate');
                }
                
                if (self.curExecSetting().repeatCls() == true && self.curExecSetting().repeatContent() == 0) {
                    $("#repIntervalDay").ntsEditor('validate');
                }
                
                if (self.curExecSetting().repeatCls() == true && self.curExecSetting().repeatContent() == 1) {
                    $("#repIntervalWeek").ntsEditor('validate');
                }
                return $('.nts-input').ntsError('hasError');
            }
            /**
             * showMessageError
             */
            private showMessageError(res: any) {
                if (res.businessException) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                }
            }
        }
        
        export interface IExecutionSetting {
            companyId:           string;
            execItemCd:          string;
            startDate:           string;
            startTime:           number;
            endTimeCls:          number;
            endTime:             number;
            oneDayRepCls:        number;
            oneDayRepInterval:   number;
            repeatCls:           boolean;
            repeatContent:       number;
            endDateCls:          number;
            endDate:             string;
            enabledSetting:      boolean;
//            nextExecDateTime:    string;
            repIntervalDay:      number;
            repIntervalWeek:     number;
            monday:              boolean;
            tuesday:             boolean;
            wednesday:           boolean;
            thursday:            boolean;
            friday:              boolean;
            saturday:            boolean;
            sunday:              boolean;
            january:             boolean;
            february:            boolean;
            march:               boolean;
            april:               boolean;
            may:                 boolean;
            june:                boolean;
            july:                boolean;
            august:              boolean;
            september:           boolean;
            october:             boolean;
            november:            boolean;
            december:            boolean;
            repeatMonthDateList: Array<number>;
        }
        
        export class ExecutionSetting {
            companyId:           KnockoutObservable<string> = ko.observable('');
            execItemCd:          KnockoutObservable<string> = ko.observable('');
            startDate:           KnockoutObservable<string> = ko.observable('');
            startTime:           KnockoutObservable<number> = ko.observable(null);
            endTimeCls:          KnockoutObservable<number> = ko.observable(null);
            endTime:             KnockoutObservable<number> = ko.observable(null);
            oneDayRepCls:        KnockoutObservable<number> = ko.observable(null);
            oneDayRepInterval:   KnockoutObservable<number> = ko.observable(null);
            repeatCls:           KnockoutObservable<boolean> = ko.observable(false);
            repeatContent:       KnockoutObservable<number> = ko.observable(null);
            endDateCls:          KnockoutObservable<number> = ko.observable(null);
            endDate:             KnockoutObservable<string> = ko.observable('');
            enabledSetting:      KnockoutObservable<boolean> = ko.observable(false);
            repIntervalDay:      KnockoutObservable<number> = ko.observable(null);;
            repIntervalWeek:     KnockoutObservable<number> = ko.observable(null);;
            monday:              KnockoutObservable<boolean> = ko.observable(false);
            tuesday:             KnockoutObservable<boolean> = ko.observable(false);
            wednesday:           KnockoutObservable<boolean> = ko.observable(false);
            thursday:            KnockoutObservable<boolean> = ko.observable(false);
            friday:              KnockoutObservable<boolean> = ko.observable(false);
            saturday:            KnockoutObservable<boolean> = ko.observable(false);
            sunday:              KnockoutObservable<boolean> = ko.observable(false);
            january:             KnockoutObservable<boolean> = ko.observable(false);
            february:            KnockoutObservable<boolean> = ko.observable(false);
            march:               KnockoutObservable<boolean> = ko.observable(false);
            april:               KnockoutObservable<boolean> = ko.observable(false);
            may:                 KnockoutObservable<boolean> = ko.observable(false);
            june:                KnockoutObservable<boolean> = ko.observable(false);
            july:                KnockoutObservable<boolean> = ko.observable(false);
            august:              KnockoutObservable<boolean> = ko.observable(false);
            september:           KnockoutObservable<boolean> = ko.observable(false);
            october:             KnockoutObservable<boolean> = ko.observable(false);
            november:            KnockoutObservable<boolean> = ko.observable(false);
            december:            KnockoutObservable<boolean> = ko.observable(false);
            repeatMonthDateList: KnockoutObservableArray<number> = ko.observableArray([]);
            constructor(param: IExecutionSetting, curDate?, curTime?) {
                let self = this;
                if (param && param != null) {
                    self.companyId(param.companyId || '');
                    self.execItemCd(param.execItemCd||'');
                    self.startDate(param.startDate || curDate);
                   if (param.startTime == 0) {
                        self.startTime(param.startTime);
                    } else {
                        self.startTime(param.startTime
                            || curTime);
                    }
                    self.endTimeCls(param.endTimeCls);
                    if (param.endTime == 0) {
                        self.endTime(param.endTime);
                    } else {
                        self.endTime(param.endTime
                            || curTime);
                    } 
                    self.oneDayRepCls(param.oneDayRepCls);
                    self.oneDayRepInterval(param.oneDayRepInterval || 60);
                    self.repeatCls(param.repeatCls || false);
                    self.repeatContent(param.repeatContent);
                    self.endDateCls(param.endDateCls);
                    self.endDate(param.endDate || curDate);
                    self.enabledSetting(param.enabledSetting || false);
                    self.repIntervalDay(param.repIntervalDay);
                    self.repIntervalWeek(param.repIntervalWeek);
                    self.monday(param.monday || false);
                    self.tuesday(param.tuesday || false);
                    self.wednesday(param.wednesday || false);
                    self.thursday(param.thursday || false);
                    self.friday(param.friday || false);
                    self.saturday(param.saturday || false);
                    self.sunday(param.sunday || false);
                    self.january(param.january || false);
                    self.february(param.february || false);
                    self.march(param.march || false);
                    self.april(param.april || false);
                    self.may(param.may || false);
                    self.june(param.june || false);
                    self.july(param.july || false);
                    self.august(param.august || false);
                    self.september(param.september || false);
                    self.october(param.october || false);
                    self.november(param.november || false);
                    self.december(param.december || false);
                    self.repeatMonthDateList(param.repeatMonthDateList || []);
                } else {
                    self.companyId('');
                    self.execItemCd('');
                    self.startDate(curDate);
                    self.startTime(curTime);
                    self.endTimeCls(0);
                    self.endTime(curTime);
                    self.oneDayRepCls(0);
                    self.oneDayRepInterval(60);
                    self.repeatCls(false);
                    self.repeatContent(0);
                    self.endDateCls(0);
                    self.endDate(curDate);
                    self.enabledSetting(false);
                    self.repIntervalDay(null);
                    self.repIntervalWeek(null);
                    self.monday(false);
                    self.tuesday(false);
                    self.wednesday(false);
                    self.thursday(false);
                    self.friday(false);
                    self.saturday(false);
                    self.sunday(false);
                    self.january(false);
                    self.february(false);
                    self.march(false);
                    self.april(false);
                    self.may(false);
                    self.june(false);
                    self.july(false);
                    self.august(false);
                    self.september(false);
                    self.october(false);
                    self.november(false);
                    self.december(false);
                    self.repeatMonthDateList([]);
                }
            }
        }
    }
}