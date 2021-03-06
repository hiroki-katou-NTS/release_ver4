module nts.uk.pr.view.qmm005.share.model {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;

    export enum MonthSelectionSegment {
        JANUARY = 1,
        FEBRUARY = 2,
        MARCH = 3,
        APRIL = 4,
        MAY = 5,
        JUNE = 6,
        JULY = 7,
        AUGUST = 8,
        SEPTEMBER = 9,
        OCTOBER = 10,
        NOVEMBER = 11,
        DECEMBER = 12
    }

    export function getMonthSelectionSegmentItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.MonthSelectionSegment.JANUARY, getText('Enum_MonthSelectionSegment_JANUARY')),
            new model.ItemModel(model.MonthSelectionSegment.FEBRUARY, getText('Enum_MonthSelectionSegment_FEBRUARY')),
            new model.ItemModel(model.MonthSelectionSegment.MARCH, getText('Enum_MonthSelectionSegment_MARCH')),
            new model.ItemModel(model.MonthSelectionSegment.APRIL, getText('Enum_MonthSelectionSegment_APRIL')),
            new model.ItemModel(model.MonthSelectionSegment.MAY, getText('Enum_MonthSelectionSegment_SECOND_MAY')),
            new model.ItemModel(model.MonthSelectionSegment.JUNE, getText('Enum_MonthSelectionSegment_JUNE')),
            new model.ItemModel(model.MonthSelectionSegment.JULY, getText('Enum_MonthSelectionSegment_JULY')),
            new model.ItemModel(model.MonthSelectionSegment.AUGUST, getText('Enum_MonthSelectionSegment_AUGUST')),
            new model.ItemModel(model.MonthSelectionSegment.SEPTEMBER, getText('Enum_MonthSelectionSegment_SEPTEMBER')),
            new model.ItemModel(model.MonthSelectionSegment.OCTOBER, getText('Enum_MonthSelectionSegment_SECOND_OCTOBER')),
            new model.ItemModel(model.MonthSelectionSegment.NOVEMBER, getText('Enum_MonthSelectionSegment_NOVEMBER')),
            new model.ItemModel(model.MonthSelectionSegment.DECEMBER, getText('Enum_MonthSelectionSegment_DECEMBER'))
        ]
    }

    export enum InsuranceStanMonthClassSification {
        LAST_MONTH = 0,
        MONTH = 1,
        JANUARY = 2,
        FEBRUARY = 3,
        MARCH = 4,
        APRIL = 5,
        MAY = 6,
        JUNE = 7,
        JULY = 8,
        AUGUST = 9,
        SEPTEMBER = 10,
        OCTOBER = 11,
        NOVEMBER = 12,
        DECEMBER = 13
    }

    export function getInsuranceStanMonthClassSificationItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.InsuranceStanMonthClassSification.LAST_MONTH, getText('Enum_InsuranceStanMonthClassification_LAST_MONTH')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.MONTH, getText('Enum_InsuranceStanMonthClassification_MONTH')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.JANUARY, getText('Enum_InsuranceStanMonthClassification_JANUARY')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.FEBRUARY, getText('Enum_InsuranceStanMonthClassification_FEBRUARY')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.MARCH, getText('Enum_InsuranceStanMonthClassification_MARCH')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.APRIL, getText('Enum_InsuranceStanMonthClassification_APRIL')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.MAY, getText('Enum_InsuranceStanMonthClassification_MAY')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.JUNE, getText('Enum_InsuranceStanMonthClassification_JUNE')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.JULY, getText('Enum_InsuranceStanMonthClassification_JULY')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.AUGUST, getText('Enum_InsuranceStanMonthClassification_AUGUST')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.SEPTEMBER, getText('Enum_InsuranceStanMonthClassification_SEPTEMBER')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.OCTOBER, getText('Enum_InsuranceStanMonthClassification_OCTOBER')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.NOVEMBER, getText('Enum_InsuranceStanMonthClassification_NOVEMBER')),
            new model.ItemModel(model.InsuranceStanMonthClassSification.DECEMBER, getText('Enum_InsuranceStanMonthClassification_DECEMBER'))
        ]
    }

    export enum YearSelectClassification {
        THIS_YEAR = 1,
        AFTER_YEAR = 2,
        LEAP_YEAR = 3,
        LAST_YEAR = 0
    }

    export function getYearSelectClassificationItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.YearSelectClassification.LAST_YEAR, getText('Enum_YearSelectClassification_PREVIOUS_YEAR')),
            new model.ItemModel(model.YearSelectClassification.THIS_YEAR, getText('Enum_YearSelectClassification_THIS_YEAR')),
            new model.ItemModel(model.YearSelectClassification.AFTER_YEAR, getText('Enum_YearSelectClassification_AFTER_YEAR')),
            new model.ItemModel(model.YearSelectClassification.LEAP_YEAR, getText('Enum_YearSelectClassification_LEAP_YEAR'))
        ]
    }

    export enum TimeCloseDateClassification {
        SAME_DATE = 0,
        APART_FROM_DATE = 1
    }

    export function getTimeCloseDateClassificationItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.TimeCloseDateClassification.SAME_DATE, getText('QMM005_117')),
            new model.ItemModel(model.TimeCloseDateClassification.APART_FROM_DATE, getText('QMM005_118'))
        ]
    }

    export enum PreviousMonthClassification {
        THIS_MONTH = 0,
        LAST_MONTH = 1
    }

    export function getPreviousMonthClassificationItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.PreviousMonthClassification.THIS_MONTH, getText('Enum_PreviousMonthClassification_THIS_MONTH')),
            new model.ItemModel(model.PreviousMonthClassification.LAST_MONTH, getText('Enum_PreviousMonthClassification_LAST_MONTH'))
        ]
    }

    export enum SocialInsuColleMonth {
        BEFORE_MONTH           = 0,
        LAST_MONTH             = 1,
        MONTH                  = 2,
        NEXT_MONTH             = 3,
        SECOND_FOLLOWING_MONTH = 4
    }

    export function getSocialInsuColleMonthItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.SocialInsuColleMonth.BEFORE_MONTH, getText('Enum_SocialInsuColleMonth_BEFORE_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.LAST_MONTH, getText('Enum_SocialInsuColleMonth_LAST_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.MONTH, getText('Enum_SocialInsuColleMonth_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.NEXT_MONTH, getText('Enum_SocialInsuColleMonth_NEXT_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.SECOND_FOLLOWING_MONTH, getText('Enum_SocialInsuColleMonth_SECOND_FOLLOWING_MONTH')),
        ]
    }

    export function getSocialInsuColleMonthItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.SocialInsuColleMonth.BEFORE_MONTH, getText('Enum_SocialInsuColleMonth_BEFORE_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.LAST_MONTH, getText('Enum_SocialInsuColleMonth_LAST_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.MONTH, getText('Enum_SocialInsuColleMonth_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.NEXT_MONTH, getText('Enum_SocialInsuColleMonth_NEXT_MONTH')),
            new model.ItemModel(model.SocialInsuColleMonth.SECOND_FOLLOWING_MONTH, getText('Enum_SocialInsuColleMonth_SECOND_FOLLOWING_MONTH')),
        ]
    }

    export enum DateSelectClassification {
        FIRST = 1,
        SECOND = 2,
        THIRD = 3,
        FOURTH = 4,
        FIFTH = 5,
        SIXTH = 6,
        SEVENTH = 7,
        EIGHTH = 8,
        NINETH = 9,
        TENTH = 10,
        ELEVENTH = 11,
        TWELFTH = 12,
        THIRTEENTH = 13,
        FOURTEENTH = 14,
        FIFTEENTH = 15,
        SIXTEENTH = 16,
        SEVENTEENTH = 17,
        EIGHTEENTH = 18,
        NINETEENTH = 19,
        TWENTIETH = 20,
        TWENTY_FIRST = 21,
        TWENTY_SECOND = 22,
        TWENTY_THIRD = 23,
        TWENTY_FOURTH = 24,
        TWENTY_FIFTH = 25,
        TWENTY_SIXTH = 26,
        TWENTY_SEVENTH = 27,
        TWENTY_EIGHTH = 28,
        TWENTY_NINTH = 29,
        THIRTIETH = 30,
        LASTDAY = 31
    }


    export class ItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export interface ISetDaySupport {
        processCateNo: number,
        closeDateTime: string,
        empInsurdStanDate: string,
        closureDateAccounting: string,
        paymentDate: string,
        empExtraRefeDate: string,
        socialInsurdStanDate: string,
        socialInsurdCollecMonth: number,
        processDate: number,
        incomeTaxDate: string,
        numberWorkDay: number
    }


    export class SetDaySupport {
        processCateNo: number;
        socialInsurCollecMonth: number;
        processDate: number;
        incomeTaxDate: string;
        closeDateTime: string;
        empExtraRefeDate: string;
        closureAccountingDate: string;
        socialInsurStanDate: string;
        empInsurdStanDate: string;
        paymentDate: string;
        numberWorkDay: number;

        constructor(params: ISetDaySupport) {
            this.processCateNo = params.processCateNo;
            this.socialInsurCollecMonth = ko.observable(params.socialInsurdCollecMonth);
            this.processDate = ko.observable(params.processDate);
            this.incomeTaxDate = ko.observable(params.incomeTaxDate);
            this.closeDateTime = ko.observable(params.closeDateTime);
            this.empExtraRefeDate = ko.observable(params.empExtraRefeDate);
            this.closureAccountingDate = ko.observable(params.closureDateAccounting);
            this.socialInsurStanDate = ko.observable(params.socialInsurdStanDate);
            this.empInsurdStanDate = ko.observable(params.empInsurdStanDate);
            this.paymentDate = ko.observable(params.paymentDate);
            this.numberWorkDay = ko.observable(params.numberWorkDay);

        }

    }

    export enum Abolition {
        ABOLITION = 1,
        NOT_ABOLITION = 0
    }

    export function getAbolitionItems(): Array<ItemModel> {
        return [
            new model.ItemModel(model.Abolition.ABOLITION, 'ABOLITION'),
            new model.ItemModel(model.Abolition.NOT_ABOLITION, 'NOT_ABOLITION')
        ]
    }

    export interface IProcessInfomation {
        processCateNo: number,
        processCls: string,
        deprecatCate: number
    }

    export class ProcessInfomation {
        processCateNo: number;
        processCls: KnockoutObservable<string> = ko.observable('');
        deprecatCate: number;

        constructor(params: IProcessInfomation) {
            this.processCateNo = params.processCateNo;
            this.processCls(params.processCls);
            this.deprecatCate = params.deprecatCate;
        }
    }


    export class SpecPrintYmSet {
        processCategoryNO: string;
        processDate: KnockoutObservable<string>;
        printDate: KnockoutObservable<string>;

        constructor(spec: ISpecPrintYmSet) {
            this.processCategoryNO = spec.processCategoryNO;
            this.processDate = ko.observable(spec.printDate);
            this.printDate = ko.observable(spec.printDate);
        }
    }

    export interface ISpecPrintYmSet {
        processCategoryNO: string;
        processDate: string;
        printDate: string;
    };


    export class MonthlyPaymentDate {
        datePayMent: DateSelectClassification;

        constructor(datePayment: DateSelectClassification) {
            this.datePayMent = datePayment;
        }
    }

    export class EmployeeExtractionReferenceDate {
        refeDate: DateSelectClassification;
        refeMonth: PreviousMonthClassification;

        constructor(refeDate: DateSelectClassification, referMonth: PreviousMonthClassification) {
            this.refeDate = refeDate;
            this.refeMonth = referMonth;
        }
    }

    export class AccountingClosureDate {
        disposalDay: DateSelectClassification;
        processMonth: PreviousMonthClassification;

        constructor(disposalDay: DateSelectClassification,
                    processMonth: PreviousMonthClassification) {
            this.disposalDay = disposalDay;
            this.processMonth = processMonth;
        }
    }


    export class DetailPrintingMonth {
        printingMonth: PreviousMonthClassification;

        constructor(printingMonth: PreviousMonthClassification) {
            this.printingMonth = printingMonth;
        }
    }


    export class SalaryInsuranceCollecMonth {
        monthCollected: SocialInsuColleMonth;

        constructor(monthsCollected: SocialInsuColleMonth) {
            this.monthCollected = monthsCollected;
        }
    }

    export class SocialInsuranceStanDate {
        baseYear: YearSelectClassification;
        baseMonth: InsuranceStanMonthClassSification;
        refeDate: DateSelectClassification;

        constructor(baseYear: YearSelectClassification,
                    baseMonth: InsuranceStanMonthClassSification,
                    baseDate: DateSelectClassification) {
            this.refeDate = baseDate;
            this.baseMonth = baseMonth;
            this.baseYear = baseYear
        }
    }

    export class CloseDate {
        timeCloseDate: TimeCloseDateClassification;
        baseYear: YearSelectClassification;
        refeDate: DateSelectClassification;
        baseMonth: SocialInsuColleMonth;

        constructor(timeCloseDate: TimeCloseDateClassification,
                    baseYear: YearSelectClassification,
                    RefeDate: DateSelectClassification,
                    baseMonth: SocialInsuColleMonth) {
            this.timeCloseDate = timeCloseDate;
            this.baseYear = baseYear;
            this.refeDate = RefeDate;
            this.baseMonth = baseMonth;
        }
    }

    export class IncomeTaxBaseYear {
        baseYear: YearSelectClassification;
        refeDate: DateSelectClassification;
        baseMonth: MonthSelectionSegment;

        constructor(baseYear: YearSelectClassification,
                    baseMonth: MonthSelectionSegment,
                    refeDate: DateSelectClassification) {
            this.baseYear = baseYear;
            this.baseMonth = baseMonth;
            this.refeDate = refeDate;
        }
    }


    export interface IBasicSetting {
        monthlyPaymentDate: MonthlyPaymentDate;
        employeeExtractionReferenceDate: EmployeeExtractionReferenceDate;
        accountingClosureDate: AccountingClosureDate;
        workDay: number;
    }

    //??????????????????
    export class BasicSetting {
        monthlyPaymentDate: MonthlyPaymentDate;
        employeeExtractionReferenceDate: EmployeeExtractionReferenceDate;
        accountingClosureDate: AccountingClosureDate;
        workDay: number;

        constructor(param: IBasicSetting) {
            this.monthlyPaymentDate = param.monthlyPaymentDate;
            this.employeeExtractionReferenceDate = param.employeeExtractionReferenceDate;
            this.accountingClosureDate = param.accountingClosureDate;
            this.workDay = param.workDay;
        }
    }

    export class EmploymentInsuranceStanDate {
        refeDate: DateSelectClassification;
        baseMonth: MonthSelectionSegment;

        constructor(refeDate: DateSelectClassification,
                    baseMonth: MonthSelectionSegment) {
            this.refeDate = refeDate;
            this.baseMonth = baseMonth;
        }
    }

    export interface ICurrentProcessDate {
        processCateNo: number,
        giveCurrTreatYear: number
    }

    export class CurrentProcessDate {
        processCateNo: number;
        salaryCurrentProcessingDate: number;

        constructor(param: ICurrentProcessDate) {
            this.processCateNo = param.processCateNo;
            this.salaryCurrentProcessingDate = param.giveCurrTreatYear;
        }

    }

    export interface IEmpTiedProYear {
        processCateNo: number,
        getEmploymentCodes: Array<string>
    }

    export class EmpTiedProYear {
        processCateNo: number;
        employmentCodes: Array<string>;

        constructor(param: IEmpTiedProYear) {
            this.processCateNo = param.processCateNo;
            this.employmentCodes = param.getEmploymentCodes;
        }
    }

    export interface IEmpCdNameImport {
        code: string,
        name: string
    }

    export class EmpCdNameImport {
        code: string;
        name: string;

        constructor(param: IEmpCdNameImport) {
            this.code = param.code;
            this.name = param.name;

        }
    }


    export interface IAdvancedSetting {
        closeDate: CloseDate;
        incomTaxBaseYear: IncomeTaxBaseYear;
        detailPrintingMon: DetailPrintingMonth;
        sociInsuStanDate: SocialInsuranceStanDate;
        salaryInsuColMon: SalaryInsuranceCollecMonth;
        empInsurStanDate: EmploymentInsuranceStanDate;
    }

    //???????????????
    export class AdvancedSetting {
        closeDate: CloseDate;
        incomTaxBaseYear: IncomeTaxBaseYear;
        detailPrintingMon: DetailPrintingMonth;
        sociInsuStanDate: SocialInsuranceStanDate;
        salaryInsuColMon: SalaryInsuranceCollecMonth;
        empInsurStanDate: EmploymentInsuranceStanDate;

        constructor(param: IAdvancedSetting) {
            this.closeDate = param.closeDate;
            this.incomTaxBaseYear = param.incomTaxBaseYear;
            this.detailPrintingMon = param.detailPrintingMon;
            this.sociInsuStanDate = param.sociInsuStanDate;
            this.salaryInsuColMon = param.salaryInsuColMon;
            this.empInsurStanDate = param.empInsurStanDate;
        }
    }


    export interface IValPayDateSet {
        processCateNo: number,
        basicSetting: BasicSetting,
        advancedSetting: AdvancedSetting
    }

    //??????????????????????????????
    export class ValPayDateSet {
        processCateNo: number;
        basicSetting: BasicSetting;
        advancedSetting: AdvancedSetting;

        constructor(param: IValPayDateSet) {
            this.processCateNo = param.processCateNo;
            this.advancedSetting = param.advancedSetting;
            this.basicSetting = param.basicSetting;
        }
    }

    export class SetPaymentDateTransfer {
        processingYear: KnockoutObservable<number>;
        listSettingPayment: KnockoutObservableArray<PaymentDateItem>;

        constructor(processingYear: KnockoutObservable<number>, listSettingPayment: KnockoutObservableArray<PaymentDateItem>) {
            this.processingYear = ko.observable(processingYear);
            this.listSettingPayment = listSettingPayment;
        }
    }

    interface IPaymentDateItem {
        paymentDate: string;
        employeeExtractionReferenceDate: string;
        socialInsuranceCollectionMonth: string;
        specificationPrintDate: string;
        numberOfWorkingDays: number;
        targetMonth: string;
        incomeTaxReferenceDate: string;
        accountingClosureDate: string;
        socialInsuranceStandardDate: string;
        employmentInsuranceStandardDate: string;
        timeClosingDate: string;
    }

    export class PaymentDateItem {
        paymentDate: KnockoutObservable<string>;
        employeeExtractionReferenceDate: KnockoutObservable<string>;
        socialInsuranceCollectionMonth: KnockoutObservable<string>;
        specificationPrintDate: KnockoutObservable<string>;
        numberOfWorkingDays: KnockoutObservable<number>;
        targetMonth: KnockoutObservable<string>;
        incomeTaxReferenceDate: KnockoutObservable<string>;
        accountingClosureDate: KnockoutObservable<string>;
        socialInsuranceStandardDate: KnockoutObservable<string>;
        employmentInsuranceStandardDate: KnockoutObservable<string>;
        timeClosingDate: KnockoutObservable<string>;

        constructor(params: IPaymentDateItem) {
            this.paymentDate = ko.observable(params.paymentDate);
            this.employeeExtractionReferenceDate = ko.observable(params.employeeExtractionReferenceDate);
            this.socialInsuranceCollectionMonth = ko.observable(params.socialInsuranceCollectionMonth);
            this.specificationPrintDate = ko.observable(params.specificationPrintDate);
            this.numberOfWorkingDays = ko.observable(params.numberOfWorkingDays);
            this.targetMonth = ko.observable(params.targetMonth);
            this.incomeTaxReferenceDate = ko.observable(params.incomeTaxReferenceDate);
            this.accountingClosureDate = ko.observable(params.accountingClosureDate);
            this.socialInsuranceStandardDate = ko.observable(params.socialInsuranceStandardDate);
            this.employmentInsuranceStandardDate = ko.observable(params.employmentInsuranceStandardDate);
            this.timeClosingDate = ko.observable(params.timeClosingDate);
        }
    }


}