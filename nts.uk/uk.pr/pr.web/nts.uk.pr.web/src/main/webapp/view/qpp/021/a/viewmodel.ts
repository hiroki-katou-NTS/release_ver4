module nts.uk.pr.view.qpp021.a.viewmodel {
    export class ItemModel {
        id: any;
        code: any;
        name: any;
        constructor(id, code, name) {
            var self = this;
            this.id = id;
            this.code = code;
            this.name = name;
        }
    }

    export class RadioBox {
        enable: KnockoutObservable<boolean>;
        selectedValue: KnockoutObservable<any>;
        items: KnockoutObservableArray<any>;

        constructor() {
            var self = this;
            self.enable = ko.observable(true);
            self.items = ko.observableArray([
                { value: 1, text: '単票出力' },
                { value: 2, text: '連続帳票印刷' },
                { value: 3, text: '圧着式印刷（Z折り）' },
                { value: 4, text: '圧着式印刷（はがき）' }
            ]);
            self.selectedValue = ko.observable(1);
        }
    }

    export class Listbox {
        itemList: KnockoutObservableArray<any>;
        itemName: KnockoutObservable<any>;
        currentCode: KnockoutObservable<any>;
        selectedCode: KnockoutObservable<any>;
        isEnable: KnockoutObservable<any>;
        selectedCodes: KnockoutObservableArray<any>;
        isEmpty: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.itemList = ko.observableArray([
                new ItemModel('A00000000000000000000000000000000001', 'A00000000001', '日通　社員１'),
                new ItemModel('A00000000000000000000000000000000002', 'A00000000002', '日通　社員2'),
                new ItemModel('A00000000000000000000000000000000003', 'A00000000003', '日通　社員3'),
                new ItemModel('A00000000000000000000000000000000004', 'A00000000004', '日通　社員4'),
                new ItemModel('A00000000000000000000000000000000005', 'A00000000005', '日通　社員5'),
                new ItemModel('A00000000000000000000000000000000006', 'A00000000006', '日通　社員6'),
                new ItemModel('A00000000000000000000000000000000007', 'A00000000007', '日通　社員7'),
                new ItemModel('A00000000000000000000000000000000008', 'A00000000008', '日通　社員8'),
                new ItemModel('A00000000000000000000000000000000009', 'A00000000009', '日通　社員9'),
                new ItemModel('A00000000000000000000000000000000010', 'A000000000010', '日通　社員１0'),
            ]);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.selectedCode = ko.observable(null);
            self.isEnable = ko.observable(true);
            self.selectedCodes = ko.observableArray([]);
            self.isEmpty = ko.observable(self.selectedCodes().length > 0);
            self.selectedCodes.subscribe(function() {
                self.isEmpty(self.selectedCodes().length > 0);
            });
        }
    }

    export class ScreenModel {
        radioBox: RadioBox;
        listBox: Listbox;
        isEnable: KnockoutObservable<any> = ko.observable(true);

        constructor() {
            var self = this;
            self.listBox = new Listbox();
            self.radioBox = new RadioBox();
        }
        print() {
            var self = this;
            var paymentQuery = [];
            for (var i = 0; i < self.listBox.selectedCodes().length; i++) {
                paymentQuery.push({ personId: self.listBox.selectedCodes()[i].id, employeeCode:  self.listBox.selectedCodes()[i].code});
            }
            nts.uk.pr.view.qpp021.a.service.print(paymentQuery).fail(function(res){
                nts.uk.ui.dialog.alert(res.message);    
            });
        }
    }

    /**
          * Model namespace.
       */
    export class PaymentDataResultViewModel {
        paymentHeader: PaymentDataHeaderViewModel;
        categories: Array<LayoutMasterCategoryViewModel>;
        remarks: KnockoutObservable<string>;
        remarkCount: KnockoutComputed<string>;

        constructor(paymentHeader: PaymentDataHeaderViewModel, categories: Array<LayoutMasterCategoryViewModel>, remarks: string) {
            var self = this;

            self.paymentHeader = paymentHeader;
            self.categories = categories;
            self.remarks = ko.observable(remarks);
            self.remarkCount = ko.computed(function() {
                return nts.uk.text.format('入力文字数：{0}文字', self.remarks() == undefined ? 0 : self.remarks().length);
            });
        }
    }

    // header
    export class PaymentDataHeaderViewModel {
        personId: string;
        companyName: String;
        departmentCode: String;
        departmentName: String;
        personName: string;
        processingYM: number;
        dependentNumber: number;
        specificationCode: string;
        specificationName: string;
        makeMethodFlag: number;
        employeeCode: string;
        comment: KnockoutObservable<string>;
        printPositionCategories: Array<PrintPositionCategoryViewModel>;
        isCreated: boolean;

        constructor(personId: string, companyName: string, departmentCode: string, departmentName: string, personName: string, processingYM: number, dependentNumber: number, specificationCode: string, specificationName: string, makeMethodFlag: number, employeeCode: string, comment: string,
            printPositionCategories: Array<PrintPositionCategoryViewModel>, isCreated: boolean) {
            var self = this;

            self.personId = personId;
            self.personName = personName;
            self.companyName = companyName;
            self.departmentCode = departmentCode;
            self.departmentName = departmentName;
            self.processingYM = processingYM;
            self.dependentNumber = dependentNumber;
            self.specificationCode = specificationCode;
            self.makeMethodFlag = makeMethodFlag;
            self.employeeCode = employeeCode;
            self.comment = ko.observable(comment);
            self.printPositionCategories = printPositionCategories;
            self.isCreated = isCreated;
        }
    }

    export class PrintPositionCategoryViewModel {
        categoryAtr: number;
        lines: number;
    }
    // categories
    export class LayoutMasterCategoryViewModel {
        categoryAttribute: number;
        categoryPosition: number;
        lineCounts: number;
        lines: KnockoutObservableArray<LayoutMasterLine>;
    }

    export interface LayoutMasterLine {
        details: KnockoutObservableArray<DetailItemViewModel>;
    }

    // item
    export class DetailItemViewModel {
        categoryAtr: number;
        itemAtr: number;
        itemCode: string;
        itemName: string;
        value: KnockoutObservable<number>;
        calculationMethod: number;
        correctFlag: number;
        columnPosition: number;
        linePosition: number;
        deductAtr: number;
        displayAtr: number;
        taxAtr: number;
        limitAmount: number;
        commuteAllowTaxImpose: number;
        commuteAllowMonth: number;
        commuteAllowFraction: number;
        isCreated: boolean;
        itemType: number;

        constructor(categoryAtr: number, itemAtr: number, itemCode: string, itemName: string, value: number, calculationMethod: number, correctFlag: number,
            columnPosition: number, linePosition: number, deductAtr: number, displayAtr: number, taxAtr: number,
            limitAmount: number, commuteAllowTaxImpose: number, commuteAllowMonth: number, commuteAllowFraction: number,
            isCreated: boolean, itemType: number) {
            var self = this;
            self.categoryAtr = categoryAtr;
            self.itemAtr = itemAtr;
            self.itemCode = itemCode;
            self.itemName = itemName;
            self.value = ko.observable(value);
            self.calculationMethod = calculationMethod;
            self.columnPosition = columnPosition;
            self.linePosition = linePosition;
            self.deductAtr = deductAtr;
            self.displayAtr = displayAtr;
            self.taxAtr = taxAtr;
            self.limitAmount = limitAmount;
            self.commuteAllowTaxImpose = commuteAllowTaxImpose;
            self.commuteAllowMonth = commuteAllowMonth;
            self.commuteAllowFraction = commuteAllowFraction;
            self.isCreated = isCreated;
            self.itemType = itemType;
        }
    }

    export class CategoriesList {
        items: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<any>;
        selectionChangedEvent = $.Callbacks();
        unselecting: KnockoutObservable<boolean>;

        constructor() {
            var self = this;

            self.items = ko.observableArray([]);
            self.selectedCode = ko.observable();
            self.selectedCode.subscribe(function(selectedCode) {
                self.selectionChangedEvent.fire(selectedCode);
            });
        }
    }
}