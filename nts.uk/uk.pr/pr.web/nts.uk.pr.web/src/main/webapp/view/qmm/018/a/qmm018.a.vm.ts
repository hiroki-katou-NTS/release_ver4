module nts.uk.pr.view.qmm018.a.viewmodel {
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import alertError = nts.uk.ui.dialog.alertError;

    export class ScreenModel {

        //Checkboxs
        checkWage: KnockoutObservable<boolean> = ko.observable(false);
        exceptionFormula: KnockoutObservable<string> = ko.observable('');
        targetWageItem: KnockoutObservable<string> = ko.observable('');
        lstTargetWageItem: KnockoutObservableArray<IStatement> =  ko.observableArray(null);
        targetWorkingDaysItem: KnockoutObservable<string> = ko.observable('');
        lstTargetWorkingDaysItem: KnockoutObservableArray<IStatement> =  ko.observableArray(null);

        categoryAtr: KnockoutObservable<number> = ko.observable(0);
        salaryItemId: KnockoutObservable<string> = ko.observable('');
        displayData: KnockoutObservable<DisplayData> = ko.observable(new DisplayData(null));

        fractionProcessingAtr: KnockoutObservableArray<ItemModel> =  ko.observable(new ItemModel(null));;
        selectedFractionProcessingAtr:  KnockoutObservableArray<number> = ko.observable(0);

        //Fix swapbutton
        attendanceDays: KnockoutObservableArray<number> = ko.observable(0);
        selectedAttendanceDays: KnockoutObservableArray<number> = ko.observable(0);

        constructor() {
            let self = this;
            self.fractionProcessingAtr(getDaysFractionProcessing());
            self.selectedFractionProcessingAtr.subscribe(x => {
                if (x == 0) {
                    self.displayData().averageWageCalculationSet().daysFractionProcessing(DaysFractionProcessing.AFTER);
                } else {
                    self.displayData().averageWageCalculationSet().daysFractionProcessing(DaysFractionProcessing.BEFORE);
                }
            });

            self.attendanceDays(getAttendanceDays());
            self.selectedAttendanceDays.subscribe(x => {
                if (x == 0) {
                    self.displayData().averageWageCalculationSet().obtainAttendanceDays(AttendanceDays.FROM_STATEMENT_ITEM);
                } else {
                    self.displayData().averageWageCalculationSet().obtainAttendanceDays(AttendanceDays.FROM_EMPLOYMENT);
                }
            });

            self.checkWage.subscribe(x => {
                if (x) {
                    self.displayData().averageWageCalculationSet().decimalPointCutoffSegment(1);
                } else {
                    self.displayData().averageWageCalculationSet().decimalPointCutoffSegment(0);
                }
            });

            service.getStatemetItemData().done(function (data: <IDisplayData>) {
                if (data) {
                    self.displayData(new DisplayData(data));
                    self.exceptionFormula(self.displayData().averageWageCalculationSet().exceptionFormula());
                    self.selectedAttendanceDays(self.displayData().averageWageCalculationSet().obtainAttendanceDays());
                    self.selectedFractionProcessingAtr(self.displayData().averageWageCalculationSet().daysFractionProcessing());

                    //lstPayment item
                    self.lstTargetWorkingDaysItem(self.displayData().lstStatemetPaymentItem());
                    let lstname = self.lstTargetWorkingDaysItem().map(value => value.name);
                    var stringTargetWageItem = lstname.toString();
                    var newStringTargetWageItem = stringTargetWageItem.replace(/,/g, "+");
                    self.targetWageItem(newStringTargetWageItem);

                    //lstAttendanceItem
                    self.lstTargetWageItem(self.displayData().lstStatemetAttendanceItem());
                    let lstname = self.lstTargetWageItem().map(value => value.name);
                    var stringTargetWageItem = lstname.toString();
                    var newStringTargetWageItem = stringTargetWageItem.replace(/,/g, "+");
                    self.targetWorkingDaysItem(newStringTargetWageItem);
                }
                else {
                    self.displayData(null);
                }
            }).fail(function (error) {
                alertError(error);
            }).always(() => {
                block.clear();
            });
        }

        registration() {
        };

        correctionLog() {
        };

        wageItemSet() {
        };

        daysFractionProcessingAtr() {
        };

        workingDaysItemSet() {
        };
    }

    export interface IAverageWageCalculationSet {
        exceptionFormula: number;
        obtainAttendanceDays: number;
        daysFractionProcessing: number;
        decimalPointCutoffSegment: number;
    }

    export class AverageWageCalculationSet {
        exceptionFormula: KnockoutObservable<number> = ko.observable(null);
        obtainAttendanceDays: KnockoutObservable<number> = ko.observable(null);
        daysFractionProcessing: KnockoutObservable<number> = ko.observable(null);
        decimalPointCutoffSegment: KnockoutObservable<number> = ko.observable(null);


        constructor(params: IAverageWageCalculationSet) {
            let self = this;
            this.exceptionFormula(params ? params.exceptionFormula : 0);
            this.obtainAttendanceDays(params ? params.obtainAttendanceDays : 0);
            this.daysFractionProcessing(params ? params.daysFractionProcessing : 0);
            this.decimalPointCutoffSegment(params ? params.decimalPointCutoffSegment : 0);
        }
    }

    export interface IStatement {
        salaryItemId: string;
        categoryAtr: number;
        itemNameCd: string;
        name: string;
    }

    export class Statement {
        salaryItemId: KnockoutObservable<string> = ko.observable(null);
        categoryAtr: KnockoutObservable<number> = ko.observable(null);
        itemNameCd: KnockoutObservable<string> = ko.observable(null);
        name: KnockoutObservable<string> = ko.observable(null);

        constructor(params: IStatement) {
            this.salaryItemId(params ? params.salaryItemId : null);
            this.categoryAtr(params ? params.categoryAtr : null);
            this.itemNameCd(params ? params.itemNameCd : null);
            this.name(params ? params.name : null);
        }
    }

    export interface IDisplayData {
        averageWageCalculationSet: IAverageWageCalculationSet;
        lstStatemetPaymentItem: IStatement;
        lstStatemetAttendanceItem: IStatement;
    }

    export class DisplayData {
        averageWageCalculationSet: KnockoutObservable<AverageWageCalculationSet>;
        lstStatemetPaymentItem: KnockoutObservableArray<Statement>;
        lstStatemetAttendanceItem: KnockoutObservableArray<Statement>;

        constructor(params: IDisplayData) {
            let self = this;
            if (params) {
                self.averageWageCalculationSet = ko.observable(new AverageWageCalculationSet(params.averageWageCalculationSet));
                self.lstStatemetPaymentItem = ko.observableArray(params.lstStatemetPaymentItem);
                self.lstStatemetAttendanceItem = ko.observableArray(params.lstStatemetAttendanceItem);
            }
            else {
                self.averageWageCalculationSet = ko.observable(new AverageWageCalculationSet(null));
                self.lstStatemetPaymentItem = ko.observableArray([]);
                self.lstStatemetAttendanceItem = ko.observableArray([]);
            }

        }
    }

    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export enum AttendanceDays {

        FROM_STATEMENT_ITEM = 0,

        FROM_EMPLOYMENT = 1
    }
    export function getAttendanceDays(): Array<ItemModel> {
        return [
            new ItemModel(AttendanceDays.FROM_STATEMENT_ITEM.toString(), getText('FROM_STATEMENT_ITEM')),
            new ItemModel(AttendanceDays.FROM_EMPLOYMENT.toString(), getText('FROM_EMPLOYMENT'))
        ];
    }

    export enum DaysFractionProcessing {

        AFTER = 0,

        BEFORE = 1
    }

    export function getDaysFractionProcessing(): Array<ItemModel> {
        return [
            new ItemModel(DaysFractionProcessing.AFTER.toString(), getText('DAYSFRACTIONPROCESSING_AFTER')),
            new ItemModel(DaysFractionProcessing.BEFORE.toString(), getText('DAYSFRACTIONPROCESSING_BEFORE'))
        ];
    }

}