module nts.uk.at.view.kdw006.i.viewmodel {
    let __viewContext: any = window["__viewContext"] || {};

    export class ScreenModelI extends ko.ViewModel {

        referenceRange: KnockoutObservableArray<any>;
        referenceRangeValue: KnockoutObservable<any>;

        elapsedMonths: KnockoutObservableArray<any>;
        elapsedMonthsValue: KnockoutObservable<any>;

        useAtr: KnockoutObservable<number>;
        time: KnockoutObservable<number>;
        displayMessage: KnockoutObservable<string>;
        messageColor: KnockoutObservable<string>;
        boldAtr: KnockoutObservable<boolean>;

        oldValues: any;

        constructor() {
            super();
            let self = this;

            let referenceRangeTemp: any[] = [];
            _.forEach(__viewContext.enums.ReferenceRange, (item) => {
                referenceRangeTemp.push({value: item.value, name: item.name});
            });
            self.referenceRange = ko.observableArray(referenceRangeTemp);
            self.referenceRangeValue = ko.observable(0);

            let elapsedMonthsTemp: any[] = [];
            _.forEach(__viewContext.enums.ElapsedMonths, (item) => {
                elapsedMonthsTemp.push({value: item.value, name: item.name});
            });
            self.elapsedMonths = ko.observableArray(elapsedMonthsTemp);
            self.elapsedMonthsValue = ko.observable(0);

            self.useAtr = ko.observable(0);
            self.time = ko.observable(0);
            self.displayMessage = ko.observable("test");
            self.messageColor = ko.observable("");
            self.boldAtr = ko.observable(true);

            self.oldValues = {};
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            self.$blockui("grayout");

            $.when(self.init()).done(() => {
                dfd.resolve();
            }).always(() => {
                nts.uk.ui.errors.clearAll();
                self.$blockui("hide");
            });

            return dfd.promise();
        }

        init(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.start().done(function(res: ManHourRecordUseSettingDto) {
                if (res) {
                    let manHourRecordReferenceSetting: ManHourRecordReferenceSettingDto = res.manHourRecordReferenceSetting;
                    let errorAlarmWorkRecord: ErrorAlarmWorkRecordDto = res.errorAlarmWorkRecord;

                    self.referenceRangeValue(manHourRecordReferenceSetting.referenceRange);
                    self.elapsedMonthsValue(manHourRecordReferenceSetting.elapsedMonths);

                    self.useAtr(errorAlarmWorkRecord.useAtr);
                    self.time(errorAlarmWorkRecord.erAlAtdItemConditionGroup1[0].compareEndValue);
                    self.displayMessage(errorAlarmWorkRecord.displayMessage);
                    self.messageColor(errorAlarmWorkRecord.messageColor);
                    self.boldAtr(errorAlarmWorkRecord.boldAtr == 1);

                    self.oldValues.time = self.time();
                    self.oldValues.displayMessage = self.displayMessage();
                    self.oldValues.messageColor = self.messageColor();
                    self.oldValues.boldAtr = self.boldAtr();
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }

        register() {
            let self = this;

            let command: ManHourRecordUseSettingDto = self.toRegisterCommand();
            self.$blockui("grayout");
            self.$validate().then((valid: boolean) => {
                if (valid) {
                    service.register(command).done(function() {
                        self.$blockui("show");
                        self.$dialog.info({ messageId: "Msg_15" }).then(() => {
                            location.reload();
                        });
                        self.$blockui("hide");
                    }).always(() => {
                        self.$blockui("hide");
                    });
                }
            }).always(() => {
                self.$blockui("hide");
            });
        }

        toRegisterCommand(): ManHourRecordUseSettingDto {
            let self = this;

            let manHourRecordReferenceSetting: ManHourRecordReferenceSettingDto = {
                elapsedMonths: self.elapsedMonthsValue(),
                referenceRange: self.referenceRangeValue(),

            };

            let errorAlarmWorkRecord: ErrorAlarmWorkRecordDto = {
                useAtr: self.useAtr(),
                erAlAtdItemConditionGroup1: [{
                    compareStartValue: self.useAtr() == 1 ? -self.time() : -self.oldValues.time,
                    compareEndValue: self.useAtr() == 1 ? self.time() : self.oldValues.time,
                }],
                displayMessage: self.useAtr() == 1 ? self.displayMessage() : self.oldValues.displayMessage,
                boldAtr: self.useAtr() == 1 ? (self.boldAtr() ? 1 : 0) : (self.oldValues.boldAtr ? 1 : 0),
                messageColor: self.useAtr() == 1 ? self.messageColor() : self.oldValues.messageColor,
            };

            return {manHourRecordReferenceSetting, errorAlarmWorkRecord};
        }

        jumpTo() {
            nts.uk.request.jump("/view/kdw/006/a/index.xhtml");
        }  
    }

    export interface ManHourRecordUseSettingDto {
        errorAlarmWorkRecord: ErrorAlarmWorkRecordDto;
        manHourRecordReferenceSetting: ManHourRecordReferenceSettingDto;
    }

    export interface ManHourRecordReferenceSettingDto { 
        elapsedMonths: number;
        referenceRange: number;
    }

    export interface ErrorAlarmWorkRecordDto { 
        useAtr: number;
        erAlAtdItemConditionGroup1: Array<ErAlAtdItemConditionDto>;
        displayMessage: string;
        boldAtr: number;
        messageColor: string;
    }

    export interface ErAlAtdItemConditionDto {
        compareStartValue: number;
        compareEndValue: number
    }
}