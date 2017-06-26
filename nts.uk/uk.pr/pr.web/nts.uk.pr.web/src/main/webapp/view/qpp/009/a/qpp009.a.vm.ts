module qpp009.a.viewmodel {
    export class ScreenModel {
        outputDivisions: KnockoutObservableArray<SelectionModel>;
        selectedDivision: KnockoutObservable<number>;
        detailItemsSetting: KnockoutObservable<DetailItemsSetting>;
        printSetting: KnockoutObservable<PrintSetting>;
        yearMonth: KnockoutObservable<number>;
        japanYearmonth: KnockoutObservable<string>;
        detailLabel: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.outputDivisions = ko.observableArray<SelectionModel>([
                new SelectionModel(0, '通常月'),
                new SelectionModel(1, '予備月')
            ]);
            self.selectedDivision = ko.observable(0);
            self.detailItemsSetting = ko.observable(new DetailItemsSetting());
            self.printSetting = ko.observable(new PrintSetting());
            self.yearMonth = ko.observable(parseInt(nts.uk.time.formatDate(new Date(), 'yyyyMM')));

            self.japanYearmonth = ko.computed(() => {
                return '(' + nts.uk.time.yearmonthInJapanEmpire(self.yearMonth()).toString() + ')';
            })
            self.detailLabel = ko.observable("※ " + nts.uk.time.formatDate(new Date(), "yyyy/MM/dd") 
            + " の部門構成で集計します.");
            

        }

        /**
         * Start srceen.
         */
        public start(): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            dfd.resolve();
            return dfd.promise();
        }

        /**
         * Print report.
         */
        public printData() {
            var self = this;
            self.clearAllError();
            // Validate
            if (self.validate()) {
                    return;
            }
            
            // Print Report
            service.printService(this).done(function() {
            })
                .fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                })
           
        }
        
        private validate(): boolean {
            var self = this;
            var hasError = false;
            // Validate year month
            $('#date-picker').ntsEditor('validate');
            
            if(self.detailItemsSetting().isPrintDepHierarchy() 
            && self.detailItemsSetting().selectedLevels().length < 1) {
                 $('#hierarchy-content').ntsError('set', '1~9階層 が選択されていません。');
                hasError = true;
            }
            
            if(self.detailItemsSetting().isPrintDepHierarchy() 
            && self.detailItemsSetting().selectedLevels().length > 5) {
                $('#hierarchy-content').ntsError('set', '部門階層が正しく指定されていません。');
                hasError = true;
            }
            
            if(!self.detailItemsSetting().isPrintDepHierarchy() && self.printSetting().selectedBreakPageCode() == 4) {
                $('#specify-break-page-select').ntsError('set', '設定が正しくありません。');
                hasError = true;
            }
            if (self.detailItemsSetting().isPrintDepHierarchy() && self.printSetting().selectedBreakPageCode() == 4
            && self.detailItemsSetting().selectedLevels().indexOf(self.printSetting().selectedBreakPageHierarchyCode()) < 0) {
                $('#specify-break-page-hierarchy-select').ntsError('set', '設定が正しくありません。');
                hasError = true;
            }
            // TODO: Validation relate to employee list.
            return hasError || $('.nts-input').ntsError('hasError');
        }
        
        private clearAllError(): void {
            $('#date-picker').ntsError('clear')
            $('#hierarchy-content').ntsError('clear');
            $('#specify-break-page-select').ntsError('clear');
            $('#specify-break-page-hierarchy-select').ntsError('clear');
        }

    }

    /**
     * Class detail items setting.
     */
    export class DetailItemsSetting {
        isPrintDetailItem: KnockoutObservable<boolean>;
        isPrintTotalOfDepartment: KnockoutObservable<boolean>;
        isPrintDepHierarchy: KnockoutObservable<boolean>;
        isCalculateTotal: KnockoutObservable<boolean>;
        AccumulatedLevelList: KnockoutObservableArray<SelectionDto>;
        selectedLevels: KnockoutObservableArray<number>;

        constructor() {
            this.isPrintDetailItem = ko.observable(true);
            this.isPrintTotalOfDepartment = ko.observable(false);
            this.isPrintDepHierarchy = ko.observable(false);
            this.isCalculateTotal = ko.observable(true);
            this.AccumulatedLevelList = ko.observableArray<SelectionDto>([
                new SelectionDto(1, '1階層'), new SelectionDto(2, '2階層'), new SelectionDto(3, '3階層'),
                new SelectionDto(4, '4階層'), new SelectionDto(5, '5階層'), new SelectionDto(6, '6階層'),
                new SelectionDto(7, '7階層'), new SelectionDto(8, '8階層'), new SelectionDto(9, '9階層')
            ]);
            this.selectedLevels = ko.observableArray<number>([]);
        }
    }

    /**
     * Class print setting.
     */
    export class PrintSetting {
        specifyBreakPageList: KnockoutObservableArray<SelectionDto>;
        selectedBreakPageCode: KnockoutObservable<number>;
        specifyBreakPageHierarchyList: KnockoutObservableArray<SelectionDto>;
        selectedBreakPageHierarchyCode: KnockoutObservable<number>;
        use2000yenSelection: KnockoutObservableArray<SelectionDto>;
        selectedUse2000yen: KnockoutObservable<number>;
        isBreakPageByAccumulated: KnockoutObservable<boolean>;

        constructor() {
            this.specifyBreakPageList = ko.observableArray<SelectionDto>([
                new SelectionDto(1, 'なし'),
                new SelectionDto(2, '社員毎'),
                new SelectionDto(3, '部門ごと'),
                new SelectionDto(4, '部門階層'),
            ])
            this.selectedBreakPageCode = ko.observable(1);
            this.specifyBreakPageHierarchyList = ko.observableArray<SelectionDto>([
                new SelectionDto(1, '1'), new SelectionDto(2, '2'), new SelectionDto(3, '3'),
                new SelectionDto(4, '4'), new SelectionDto(5, '5'), new SelectionDto(6, '6'),
                new SelectionDto(7, '7'), new SelectionDto(8, '8'), new SelectionDto(9, '9')
            ]);
            this.selectedBreakPageHierarchyCode = ko.observable(1);
            this.use2000yenSelection = ko.observableArray<SelectionDto>([
                new SelectionDto(1, '含める'),
                new SelectionDto(0, '含めない'),
            ]);
            this.selectedUse2000yen = ko.observable(0);
            var self = this;
            this.isBreakPageByAccumulated = ko.computed(function() {
                return self.selectedBreakPageCode() == 4;
            });
        }
    }

    export class SalaryChartResultViewModel {
        empCode: string;
        empName: string;
        paymentAmount: number;
        empDesignation: string;
        depDesignation: string;
        totalDesignation: string;
        depCode: string;
        depName: string;
        depPath: string;
        constructor(empCode: string, empName: string, paymentAmount: number, empDesignation: string,
            depDesignation: string, totalDesignation: string, depCode: string, depName: string, depPath: string) {
            this.empCode = empCode;
            this.empName = empName;
            this.paymentAmount = paymentAmount;
            this.empDesignation = empDesignation;
            this.depDesignation = depDesignation;
            this.totalDesignation = totalDesignation;
            this.depCode = depCode;
            this.depName = depName;
            this.depPath = depPath;
        }
    }
    /**
 * Class 出力区分.
 */
    export class SelectionModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    export class SelectionDto {
        code: number;
        name: string;
        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }
}