module qet001.b.viewmodel {
    import NtsGridColumn = nts.uk.ui.NtsGridListColumn;
    import NtsTabModel = nts.uk.ui.NtsTabPanelModel;
    import WageLedgerOutputSetting = qet001.a.service.model.WageLedgerOutputSetting;
    import WageledgerCategorySetting = qet001.a.service.model.WageledgerCategorySetting;
    import WageLedgerSettingItem = qet001.a.service.model.WageLedgerSettingItem;
    import OuputSettingsService = qet001.a.service;
    
    export class ScreenModel {
        outputSettings: KnockoutObservable<OutputSettings>;
        outputSettingDetail: KnockoutObservable<OutputSettingDetail>;
        reportItems: KnockoutObservableArray<ReportItem>;
        reportItemColumns: KnockoutObservableArray<any>;
        reportItemSelected: KnockoutObservable<string>;
        aggregateItemsList: service.Item[];
        masterItemList: service.Item[];
        isLoading: KnockoutObservable<boolean>;
        hasUpdate: KnockoutObservable<boolean>;
        codeDirtyChecker: nts.uk.ui.DirtyChecker;
        nameDirtyChecker: nts.uk.ui.DirtyChecker;
        reportItemsDirtyChecker: nts.uk.ui.DirtyChecker;
        outputItemColumns: KnockoutObservableArray<any>;
        
        constructor() {
            this.outputSettings = ko.observable(new OutputSettings());
            this.outputSettingDetail= ko.observable(new OutputSettingDetail([], []));
            this.reportItems = ko.observableArray([]);
            this.isLoading = ko.observable(true);
            this.outputItemColumns = ko.observableArray([
                    {headerText: 'key', prop: 'itemKey', width: 50, hidden: true},
                    {headerText: '集約', prop: 'isAggregateItem', width: 40,
                        formatter: function(data: string) {
                            if (data == 'true') {
                                return '<div class="center"><i class="icon icon-dot"></i></div>';
                            }
                            return '';
                        }
                    },
                    {headerText: 'コード', prop: 'code', width: 50},
                    {headerText: '名称', prop: 'name', width: 100},
                    {headerText: '削除', prop: 'code', width: 50,
                        formatter: function(data: string) {
                            return '<button class="delete-button icon icon-close" id="' + data + '" >'
                                + '</button>';
                        }
                    },
                ]);
            this.reportItemColumns = ko.observableArray([
                    {headerText: 'key', prop: 'itemKey', width: 50, hidden: true},
                    {headerText: '区分', prop: 'categoryNameJa', width: 50},
                    {headerText: '集約', prop: 'isAggregate', width: 40,
                        formatter: function(data: string): string {
                            if (data == 'true') {
                                return '<div class="center"><i class="icon icon-dot"></i></div>';
                            }
                            return '';
                        }
                    },
                    {headerText: 'コード', prop: 'itemCode', width: 50},
                    {headerText: '名称', prop: 'itemName', width: 100},
                ]);
            this.reportItemSelected = ko.observable(null);
            this.aggregateItemsList = [];
            this.masterItemList = [];
            this.hasUpdate = ko.observable(false);
            
            var self = this;
            self.codeDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetail().settingCode);
            self.nameDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetail().settingName);
            self.reportItemsDirtyChecker = new nts.uk.ui.DirtyChecker(self.reportItems);
            self.outputSettings().outputSettingSelectedCode.subscribe((code: string) => {
                self.isLoading(true);
                // Do nothing if selected same code.
                if (self.outputSettings().temporarySelectedCode() == code) {
                    return;
                }
                else if (code) {
                    if (self.isDirty()) {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                            self.outputSettings().temporarySelectedCode(code);
                            self.loadOutputSettingDetail(code);
                        }).ifNo(function() {
                            self.outputSettings().outputSettingSelectedCode(self.outputSettings().temporarySelectedCode());
                        });
                    } else {
                        self.outputSettings().temporarySelectedCode(code);
                        self.loadOutputSettingDetail(code);
                    }
                    self.isLoading(false);

                } else {
                    if (self.isDirty()) {
                        nts.uk.ui.dialog.confirm('変更された内容が登録されていません。\r\nよろしいですか。').ifYes(function() {
                            self.clearError();
                            self.outputSettings().temporarySelectedCode('');
                            self.outputSettingDetail(new OutputSettingDetail(self.aggregateItemsList, self.masterItemList));
                            self.isLoading(false);
                            self.resetDirty();
                            return;
                        }).ifNo(function() {
                            self.outputSettings().outputSettingSelectedCode(self.outputSettings().temporarySelectedCode());
                        });
                    } else {
                        self.clearError();
                        self.outputSettings().temporarySelectedCode('');
                        self.outputSettingDetail(new OutputSettingDetail(self.aggregateItemsList, self.masterItemList));
                        self.isLoading(false);
                        self.resetDirty();
                        return;
                    }
                }
            });

            self.outputSettingDetail.subscribe((data: OutputSettingDetail) => {
                self.reloadReportItem();
                data.reloadReportItems = self.reloadReportItem.bind(self);
            });
        }

        private resetDirty(): void {
            var self = this;
            self.codeDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetail().settingCode);
            self.nameDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetail().settingName);
            self.reportItemsDirtyChecker.reset();
        }

        private isDirty(): boolean {
                var self = this;
                if (self.codeDirtyChecker.isDirty()
                    || self.nameDirtyChecker.isDirty()
                    || self.reportItemsDirtyChecker.isDirty()) {
                    return true;
                }
                return false;
            }

        private clearError(): void {
            if (nts.uk.ui._viewModel) {
                $('#register-button').ntsError('clear');
                $('#code-input').ntsError('clear');
                $('#name-input').ntsError('clear');
            }
        }

        private validate(): void {
            $('#register-button').ntsEditor('validate');
            $('#code-input').ntsEditor('validate');
            $('#name-input').ntsEditor('validate');
        }

        private hasError(): boolean {
            if ($('#register-button').ntsError('hasError')
                || $('#code-input').ntsError('hasError')
                || $('#name-input').ntsError('hasError')) {
                return true;
            }
            return false;
        }

        /**
         * Reload report items.
         */
        public reloadReportItem() {
            var self = this;
            var data = self.outputSettingDetail();
            if (!data || data.categorySettings().length == 0) {
                self.reportItems([]);
                return;
            }
            // Set data to report item list.
            var reportItemList: ReportItem[] = [];
            data.categorySettings().forEach((setting) => {
                var categoryName: string = setting.category;
                setting.outputItems().forEach((item) => {
                    reportItemList.push(new ReportItem(categoryName, item.isAggregateItem, item.code, item.name));
                });
            });
            self.reportItems(reportItemList);
        }
        
        /**
         * Start load data for this screen.
         */
        public start(): JQueryPromise<void>{
            var dfd = $.Deferred<void>();
            var self = this;
            var outputSettings: WageLedgerOutputSetting[] = nts.uk.ui.windows.getShared('outputSettings');
            var selectedSettingCode: string = nts.uk.ui.windows.getShared('selectedCode');
            
            // Load master items and aggregate items.
            $.when(self.loadAggregateItems(), self.loadMasterItems()).done(() => {
                self.outputSettingDetail(new OutputSettingDetail(self.aggregateItemsList, self.masterItemList));
                // Check output setting is empty.
                var isHasData = outputSettings && outputSettings.length > 0;
                if (!isHasData) {
                    self.outputSettings().outputSettingSelectedCode('');
                    dfd.resolve();
                    return;
                }
                self.outputSettings().outputSettingList(outputSettings);
                self.outputSettings().outputSettingSelectedCode(selectedSettingCode);
                self.outputSettingDetail().isCreateMode(!isHasData);
                dfd.resolve();
            });
            return dfd.promise();
        }
        
         /**
         * Load all output setting.
         */
        public loadAllOutputSetting(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            OuputSettingsService.findOutputSettings().done(function(data: WageLedgerOutputSetting[]) {
                self.outputSettings().outputSettingList(data);
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            })
            return dfd.promise();
        }
        
        /**
         * Close dialog.
         */
        public close() {
            // Dirty check.
            var self = this;
            if (self.isDirty()) {
                nts.uk.ui.dialog.confirm('変更された内容が登録されていません。\r\nよろしいですか。').ifYes(function() {
                    nts.uk.ui.windows.close();
                });
            } else {
                nts.uk.ui.windows.close();
            }
        }
        
        /**
         * Save output setting.
         */
        public save() {
            var self = this;
            self.clearError();
            // Validate.
            self.validate();
            if (self.hasError()) {
                return;
            }
            service.saveOutputSetting(self.outputSettingDetail()).done(function() {
                nts.uk.ui.windows.setShared('isHasUpdate', true, false);
                self.loadAllOutputSetting().done(() => {
                    self.resetDirty();
                    self.outputSettings().outputSettingSelectedCode(self.outputSettingDetail().settingCode());
                });
            }).fail(function(res) {
                $('#code-input').ntsError('set', res.message);
            });
        }
        
        /**
         * Remove Output setting.
         */
        public remove() {
            var self = this;
            // Check selected output setting.
            var selectedCode = self.outputSettings().temporarySelectedCode();
            if (!selectedCode || selectedCode == '') {
                return;
            }
            nts.uk.ui.dialog.confirm('出力項目設定からもデータを削除します。\r\nよろしいですか？').ifYes(function() {
                service.removeOutputSetting(selectedCode).done(function() {
                    nts.uk.ui.windows.setShared('isHasUpdate', true, false);
                    // Find item selected.
                    var itemSelected = self.outputSettings().outputSettingList().filter(item => item.code == selectedCode)[0];
                    var indexSelected = self.outputSettings().outputSettingList().indexOf(itemSelected);
                    // Remove item selected in list.
                    self.outputSettings().outputSettingList.remove(itemSelected);

                    // If list is empty -> new mode.
                    if (self.outputSettings().outputSettingList().length == 0) {
                        self.outputSettings().outputSettingSelectedCode(null);
                        return;
                    }

                    // Select same row with item selected.
                    if (self.outputSettings().outputSettingList()[indexSelected]) {
                        self.outputSettings().outputSettingSelectedCode(
                            self.outputSettings().outputSettingList()[indexSelected].code);
                        return;
                    }

                    // Select next higher row.
                    self.outputSettings().outputSettingSelectedCode(
                        self.outputSettings().outputSettingList()[indexSelected - 1].code)
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
            });
            
        }
        
        /**
         * Load detail output setting.
         */
        public loadOutputSettingDetail(selectedCode: string): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findOutputSettingDetail(selectedCode).done(function(data: WageLedgerOutputSetting){
                self.clearError();
                self.outputSettingDetail(new OutputSettingDetail(self.aggregateItemsList, self.masterItemList, data));
                self.resetDirty();
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            })
            return dfd.promise();
        }
        
        /**
         * Load Aggregate items.
         */
        public loadAggregateItems(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findAggregateItems().done((item: service.Item[]) => {
                self.aggregateItemsList = item;
                dfd.resolve();
            }).fail((res) => {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });
            return dfd.promise();
        }
        
        /**
         * Load master item.
         */
        public loadMasterItems(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findMasterItems().done((item: service.Item[]) => {
                self.masterItemList = item;
                dfd.resolve();
            }).fail((res) => {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });
            return dfd.promise();
        }
        
        /**
         * Switch to create mode.
         */
        public switchToCreateMode() {
            var self = this;
            self.outputSettings().outputSettingSelectedCode('');
        }
    }
    
    /**
     * 登録済みの出力項目設定
     */
    export class OutputSettings {
        searchText: KnockoutObservable<string>;
        outputSettingList: KnockoutObservableArray<WageLedgerOutputSetting>;
        temporarySelectedCode: KnockoutObservable<string>;
        outputSettingSelectedCode: KnockoutObservable<string>;
        outputSettingColumns: KnockoutObservableArray<any>;
        
        constructor() {
            this.searchText = ko.observable(null);
            this.outputSettingList = ko.observableArray([]);
            this.temporarySelectedCode = ko.observable(null);
            this.outputSettingSelectedCode = ko.observable(null);
            this.outputSettingColumns = ko.observableArray([
                {headerText: 'コード', prop: 'code', width: 50}, 
                {headerText: '名称', prop: 'name',  width: 100}]);
        }
    }
    
    /**
     * Output setting detail.
     */
    export class OutputSettingDetail {
        settingCode: KnockoutObservable<string>;
        settingName: KnockoutObservable<string>;
        isPrintOnePageEachPer: KnockoutObservable<boolean>;
        categorySettingTabs: KnockoutObservableArray<NtsTabModel>;
        selectedCategory: KnockoutObservable<string>;
        isCreateMode: KnockoutObservable<boolean>;
        categorySettings: KnockoutObservableArray<CategorySetting>;
        reloadReportItems: () => void;
        
        constructor (aggregateItems: service.Item[], masterItem: service.Item[], outputSetting?: WageLedgerOutputSetting) {
            this.settingCode = ko.observable(outputSetting ? outputSetting.code : '');
            this.settingName = ko.observable(outputSetting ? outputSetting.name : '');
            this.isPrintOnePageEachPer = ko.observable(outputSetting != undefined ? outputSetting.isOnceSheetPerPerson : false);
            this.categorySettingTabs = ko.observableArray([
                { id: 'tab-salary-payment', title: '給与支給', content: '#salary-payment', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-salary-deduction', title: '給与控除', content: '#salary-deduction', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-salary-attendance', title: '給与勤怠', content: '#salary-attendance', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-bonus-payment', title: '賞与支給', content: '#bonus-payment', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-bonus-deduction', title: '賞与控除', content: '#bonus-deduction', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-bonus-attendance', title: '賞与勤怠', content: '#bonus-attendance', 
                    enable: ko.observable(true), visible: ko.observable(true) },
            ]);
            this.selectedCategory = ko.observable('tab-salary-payment');
            this.isCreateMode = ko.observable(!outputSetting);
            var categorySetting : CategorySetting[];
            if (!outputSetting) {
                categorySetting = this.convertCategorySettings(aggregateItems, masterItem);
            } else {
                categorySetting = this.convertCategorySettings(aggregateItems, masterItem, outputSetting.categorySettings);
            }
            this.categorySettings = ko.observableArray(categorySetting);
            var self = this;
            self.categorySettings().forEach((setting) => {
                setting.outputItems.subscribe((newValue) => {
                    self.reloadReportItems();
                });
            });
        }
        
        /**
         * Convert category setting data to screen model.
         */
        private convertCategorySettings(aggregateItems: service.Item[], masterItem: service.Item[],
                categorySettings?: WageledgerCategorySetting[]) : CategorySetting[] {
            var settings: CategorySetting[] = [];
            
            settings[0] = this.createCategorySetting(Category.PAYMENT, PaymentType.SALARY,
                aggregateItems, masterItem, categorySettings);
            settings[1] = this.createCategorySetting(Category.DEDUCTION, PaymentType.SALARY, 
                aggregateItems, masterItem, categorySettings);
            settings[2] = this.createCategorySetting(Category.ATTENDANCE, PaymentType.SALARY, 
                aggregateItems, masterItem, categorySettings);
            settings[3] = this.createCategorySetting(Category.PAYMENT, PaymentType.BONUS, 
                aggregateItems, masterItem, categorySettings);
            settings[4] = this.createCategorySetting(Category.DEDUCTION, PaymentType.BONUS, 
                aggregateItems, masterItem, categorySettings);
            settings[5] = this.createCategorySetting(Category.ATTENDANCE, PaymentType.BONUS, 
                aggregateItems, masterItem, categorySettings);
            
            return settings;
        }
        
        private createCategorySetting(category: string, paymentType: string,
                aggregateItems: service.Item[], masterItem: service.Item[], 
                categorySettings?: WageledgerCategorySetting[]): CategorySetting {
            var aggregateItemsInCategory = aggregateItems.filter((item) => item.category == category && item.paymentType == paymentType);
            var masterItemsInCategory = masterItem.filter((item) => item.category == category);
            var cateTempSetting: WageledgerCategorySetting = {category: category, paymentType: paymentType, outputItems: []};
            if (!categorySettings) {
                return new CategorySetting(aggregateItemsInCategory, masterItemsInCategory, cateTempSetting);
            }
            
            var categorySetting = categorySettings.filter((item) => item.category == category 
                && item.paymentType == paymentType)[0];
            if (!categorySetting) {
                categorySetting = cateTempSetting;
            }
            return new CategorySetting(aggregateItemsInCategory, masterItemsInCategory, categorySetting);
        }
    }
    
    /**
     * Category setting class.
     */
    export class CategorySetting {
        category: string;
        paymentType: string;
        outputItems: KnockoutObservableArray<WageLedgerSettingItem>;
        aggregateItemsList: KnockoutObservableArray<service.Item>;
        masterItemList: KnockoutObservableArray<service.Item>;
        aggregateItemSelected: KnockoutObservable<string>;
        masterItemSelected: KnockoutObservable<string>;
        outputItemsSelected: KnockoutObservable<string>;
        outputItemCache: WageLedgerSettingItem[];
        fullCategoryName: string;

        constructor(aggregateItems: service.Item[], masterItems: service.Item[],
                categorySetting?: WageledgerCategorySetting) {
            this.category = categorySetting.category;
            this.paymentType = categorySetting.paymentType;
            this.fullCategoryName = this.getFullCategoryName(this.category, this.paymentType);

            // exclude item contain in setting.
            var masterSettingItemCode: string[] = [];
            var aggregateSettingItemCode : string[] = [];
            if (categorySetting) {
                masterSettingItemCode = categorySetting.outputItems
                    .filter(item => !item.isAggregateItem)
                    .map((item) => {
                        return item.code;
                    });
                aggregateSettingItemCode = categorySetting.outputItems
                    .filter(item => item.isAggregateItem)
                    .map((item) => {
                        return item.code;
                    });
                categorySetting.outputItems.forEach(item => {
                    item.itemKey = item.code + (item.isAggregateItem ? '_A' : '_NA');
                })
            }
            this.outputItems = ko.observableArray(categorySetting ? categorySetting.outputItems : []);
            var aggregateItemsExcluded = aggregateItems.filter((item) => aggregateSettingItemCode.indexOf(item.code) == -1);
            var masterItemsExcluded = masterItems.filter((item) => masterSettingItemCode.indexOf(item.code) == -1);
            this.aggregateItemsList = ko.observableArray(aggregateItemsExcluded);
            this.masterItemList = ko.observableArray(masterItemsExcluded);
            this.outputItemsSelected = ko.observable(null);
            this.aggregateItemSelected = ko.observable(null);
            this.masterItemSelected = ko.observable(null);
            var self = this;
            self.outputItemCache = categorySetting ? categorySetting.outputItems : [];
            self.outputItems.subscribe(function(items) {
                self.outputItemCache = items;
            });
            // Create Customs handle For event rened nts grid.
            (<any>ko.bindingHandlers).rended = {
                update: function(element: any,
                    valueAccessor: () => any,
                    allBindings: KnockoutAllBindingsAccessor,
                    viewModel: CategorySetting,
                    bindingContext: KnockoutBindingContext) {
                    var code = valueAccessor();
                    viewModel.outputItems().forEach(item => {
                        $('#' + item.code).on('click', function() {
                            code(item.itemKey);
                            viewModel.remove();
                            code(null);
                        })
                    });
                }
            };
        }
        
        public remove() {
            var self = this;
            var seletecedAttr = self.outputItemsSelected().split('_');
            var selectedItem = self.outputItems().filter((item) => {
                var isAggregate = seletecedAttr[1] == 'A';
                return item.code == seletecedAttr[0] && item.isAggregateItem == isAggregate;
            })[0];
            self.outputItems.remove(selectedItem);
            
            // Return item.
            if (selectedItem.isAggregateItem) {
                // Return to Aggregate items table.
                self.aggregateItemsList.push({
                    code: selectedItem.code,
                    name: selectedItem.name,
                    paymentType: self.paymentType,
                    category: self.category,
                });
                return;
            }
            // Return to master items table.
            self.masterItemList.push({
                code: selectedItem.code,
                name: selectedItem.name,
                paymentType: self.paymentType,
                category: self.category,
            });
        }
        
        /**
         * Move master item to outputItems.
         */
        public masterItemToDisplay() {
            // If master item is unselected => return.
            if (!this.masterItemSelected()) {
                return;
            }
            var self = this;
            var selectedItem: service.Item = self.masterItemList().filter((item: service.Item) => {
                return item.code == self.masterItemSelected();
            })[0];
            
            // Remove form master list.
            self.masterItemList.remove(selectedItem);
            
            // Add to outputItems.
            self.outputItems.push({
                code: selectedItem.code,
                name: selectedItem.name,
                isAggregateItem: false,
                itemKey: selectedItem.code + '_NA'
            });
            self.masterItemSelected(null);
        }
        
        public aggregateItemToDisplay() {
            // If master item is unselected => return.
            if (!this.aggregateItemSelected()) {
                return;
            }
            var self = this;
            var selectedItem: service.Item = self.aggregateItemsList().filter((item: service.Item) => {
                return item.code == self.aggregateItemSelected();
            })[0];
            
            // Remove form master list.
            self.aggregateItemsList.remove(selectedItem);
            
            // Add to outputItems.
            self.outputItems.push({
                code: selectedItem.code,
                name: selectedItem.name,
                isAggregateItem: true,
                itemKey: selectedItem.code + '_A'
            });
            self.aggregateItemSelected(null);
        }
        
        /**
         * Get full category name by category and payment type.
         */
        private getFullCategoryName(category: string, paymentType: string) : string {
            var categoryName;
            switch(category) {
                case Category.PAYMENT:
                    categoryName = '支給';
                    break;
                case Category.DEDUCTION: 
                    categoryName = '控除';
                    break;
                case Category.ATTENDANCE:
                    categoryName = '勤怠';
                    break;
                default:
                    categoryName = '';
            }
            var paymentTypeName = paymentType == PaymentType.SALARY ? '給与' : '賞与';
            return paymentTypeName + categoryName;
        }
    }
    
    /**
     * Report Item class.
     */
    export class ReportItem {
        categoryName: string;
        isAggregate: boolean;
        itemCode: string;
        itemName: string;
        categoryNameJa: string;
        itemKey: string;
        
        constructor(categoryName: string, isAggregate: boolean, itemCode: string, itemName: string) {
            this.categoryName = categoryName;
            this.isAggregate = isAggregate;
            this.itemCode = itemCode;
            this.itemName = itemName;
            var self = this;
            // Convert category name to japanese.
            switch(categoryName) {
                case Category.PAYMENT:
                    self.categoryNameJa = '支給';
                    break;
                case Category.DEDUCTION: 
                    self.categoryNameJa = '控除';
                    break;
                case Category.ATTENDANCE:
                    self.categoryNameJa = '勤怠';
                    break;
                default:
                    self.categoryNameJa = '';
            }
            self.itemKey = itemCode + isAggregate + categoryName;
        }
    }
    
    /**
     * 出力するレイアウト.
     */
    export class LayoutOutput {
        /**
         * 賃金台帳（A4横1ページ）を出力する
         */
        static WAGE_LEDGER = 0;
        
        /**
         * 賃金一覧表を出力する
         */
        static WAGE_LIST = 1;
    }
    
    /**
     * 出力する項目の選択
     */
    export class OutputType {
        /**
         * 明細書項目を出力する
         */
        static DETAIL_ITEM = 0;
        /**
         * 明細書の集約項目を出力する
         */
        static SUMMARY_DETAIL_ITEMS = 1;
    }
    
    /**
     * Wage ledger category.
     */
    export class Category {
        /**
         * 支給
         */
        static PAYMENT = 'Payment';
        
        /**
         * 控除
         */
        static DEDUCTION = 'Deduction';
        
        /**
         * 勤怠
         */
        static ATTENDANCE = 'Attendance';
    }
    
    /**
     * Wage ledger payment type.
     */
    export class PaymentType {
        /**
         * Salary.
         */
        static SALARY = 'Salary';
        
        /**
         * Bonus.
         */
        static BONUS = 'Bonus'
    }
}