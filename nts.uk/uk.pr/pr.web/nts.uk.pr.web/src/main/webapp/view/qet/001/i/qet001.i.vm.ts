module qet001.i.viewmodel {
    import NtsGridColumn = nts.uk.ui.NtsGridListColumn;
    import WageLedgerOutputSetting = qet001.a.service.model.WageLedgerOutputSetting;
    import WageledgerCategorySetting = qet001.a.service.model.WageledgerCategorySetting;
    import WageLedgerSettingItem = qet001.a.service.model.WageLedgerSettingItem;

    export class ScreenModel {
        aggregateItemCategories: KnockoutObservableArray<AggregateCategory>;
        masterItems: KnockoutObservableArray<service.Item>;
        selectedTab: KnockoutObservable<number>;
        swapListColumns: KnockoutObservableArray<any>;
        switchs: KnockoutObservableArray<any>;
        itemListColumns: KnockoutObservableArray<any>;

        constructor() {
            this.aggregateItemCategories = ko.observableArray([]);
            this.masterItems = ko.observableArray([]);
            this.selectedTab = ko.observable(0);
            this.switchs = ko.observableArray([
                { code: '0', name: '表示する' },
                { code: '1', name: '表示しない' }
            ]);
            this.swapListColumns = ko.observableArray([
                { headerText: 'コード', key: 'code', width: 50 },
                { headerText: '名称', key: 'name', width: 180 }
            ]);
            this.itemListColumns = ko.observableArray([
                { headerText: 'コード', prop: 'code', width: 50 },
                { headerText: '名称', prop: 'name', width: 180 }]);
            var self = this;
            $("#sidebar-area > div > ul > li").on('click', function() {
                var index = $("#sidebar-area > div > ul > li").index(this);
                $("#sidebar").ntsSideBar("active", index);
                self.selectedTab(index);
            });
            self.selectedTab.subscribe(val => {
                if (!val) {
                    return;
                }
                // reload tab.
                self.aggregateItemCategories()[val].clearError();
                self.aggregateItemCategories()[val].loadAggregateItemByCategory();
            })
        }

        start(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findMasterItems().done(function(res) {
                self.masterItems(res);

                // init aggregate categories.
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.SALARY, Category.PAYMENT, res));
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.SALARY, Category.DEDUCTION, res));
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.SALARY, Category.ATTENDANCE, res));
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.BONUS, Category.PAYMENT, res));
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.BONUS, Category.DEDUCTION, res));
                self.aggregateItemCategories.push(new AggregateCategory(PaymentType.BONUS, Category.ATTENDANCE, res));

                self.aggregateItemCategories()[0].loadAggregateItemByCategory();
                dfd.resolve();
            })
            return dfd.promise();
        }

        /**
         * After rended template.
         */
        public afterRender() {
            // set width when swap list is rended.
            $('.master-table-label').width($('#swap-list-gridArea1').width());
            $('.sub-table-label').width($('#swap-list-gridArea2').width())
        }
    }

    export class AggregateCategory {
        itemList: KnockoutObservableArray<service.Item>;
        category: string;
        paymentType: string;
        fullCategoryName: string;
        aggregateItemSelectedCode: KnockoutObservable<string>;
        temporarySelectedCode: KnockoutObservable<string>;
        aggregateItemDetail: KnockoutObservable<AggregateItemDetail>;
        dirty: nts.uk.ui.DirtyChecker;

        constructor(paymentType: string, categoryName: string, masterItems: service.Item[]) {
            this.itemList = ko.observableArray([]);
            this.category = categoryName;
            this.paymentType = paymentType;
            this.aggregateItemSelectedCode = ko.observable(null);
            this.temporarySelectedCode = ko.observable(null);
            this.fullCategoryName = this.getFullCategoryName(categoryName, paymentType);

            // Filter master item by category and payment type.
            var masterItemInCate = masterItems.filter(item => item.category == categoryName);
            this.aggregateItemDetail = ko.observable(new AggregateItemDetail(paymentType,
                categoryName, masterItemInCate));
            var self = this;
            self.dirty = new nts.uk.ui.DirtyChecker(self.aggregateItemDetail);

            // When selected aggregate item => load detail.
            self.aggregateItemSelectedCode.subscribe((code: string) => {
                // Do nothing if selected same code.
                if (self.temporarySelectedCode() == code) {
                    return;
                }
                self.confirmDirtyAndExecute(function() {
                    self.clearError();
                    // update mode.
                    if (code) {
                        self.temporarySelectedCode(code);
                        self.loadDetailAggregateItem(self.category, self.paymentType, code).done(function(res: service.Item) {
                            self.aggregateItemDetail(new AggregateItemDetail(paymentType,
                                categoryName, masterItemInCate, res));
                            self.dirty.reset();
                            self.setStyle();
                        });
                    }
                    // new mode.
                    else {
                        self.temporarySelectedCode('');
                        self.aggregateItemDetail(new AggregateItemDetail(paymentType,
                            categoryName, masterItemInCate));
                        self.dirty.reset();
                        self.setStyle();
                    }
                }, function() {
                    self.aggregateItemSelectedCode(self.temporarySelectedCode());
                });
            });
        }

        /**
         * Get full category name by category and payment type.
         */
        private getFullCategoryName(category: string, paymentType: string): string {
            var categoryName = '';
            switch (category) {
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

        /**
         * Load Aggregate items by category.
         */
        public loadAggregateItemByCategory(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findAggregateItemsByCategory(self.category, self.paymentType).done(function(res: service.Item[]) {
                self.itemList(res);
                if (!self.aggregateItemSelectedCode()) {
                    // Select first element or enter new mode if there is no element.
                    let selectedCode = res.length > 0 ? res[0].code : null
                    self.aggregateItemSelectedCode(selectedCode);
                }
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Load detail aggregate item.
         */
        public loadDetailAggregateItem(category: string, paymentType: string, code: string): JQueryPromise<service.Item> {
            var dfd = $.Deferred<service.Item>();
            service.findAggregateItemDetail(category, paymentType, code).done((data: service.Item) => {
                dfd.resolve(data);
            }).fail((res) => {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            })
            return dfd.promise();
        }

        /**
         * Switch to create mode.
         */
        public switchToCreateMode(): void {
            var self = this;
            self.aggregateItemSelectedCode(null);
        }

        public save(): void {
            var self = this;
            // clear error.
            self.clearError();
            // Validate.
            $('#code-input').ntsEditor('validate');
            $('#name-input').ntsEditor('validate')
            if (self.aggregateItemDetail().subItems().length == 0) {
                $('#swap-list').ntsError('set', '集約する'+ self.fullCategoryName +'項目が選択されていません。');
            }
            if ($('.nts-input').ntsError('hasError') || $('#swap-list').ntsError('hasError')) {
                return;
            }

            // save.
            service.save(self.aggregateItemDetail()).done(function() {
                service.findAggregateItemsByCategory(self.category, self.paymentType).done(function(res: service.Item[]) {
                    self.itemList(res);
                    self.dirty.reset();
                    self.aggregateItemSelectedCode(self.aggregateItemDetail().code());
                });
            }).fail(function(res) {
               $('#code-input').ntsError('set', res.message); 
            });
        }

        public remove(): void {
            var self = this;
            if (!self.aggregateItemSelectedCode()) {
                return;
            }
            nts.uk.ui.dialog.confirm('出力項目設定からもデータを削除します。\r\nよろしいですか？').ifYes(function() {
                service.remove(self.category, self.paymentType, self.aggregateItemSelectedCode()).done(function() {
                    // Find item selected.
                    var itemSelected = self.itemList().filter(item => item.code == self.aggregateItemSelectedCode())[0];
                    var indexSelected = self.itemList().indexOf(itemSelected);
                    // Remove item selected in list.
                    self.itemList.remove(itemSelected);

                    if (self.itemList() && self.itemList().length > 0) {
                        let currentItem = self.itemList()[indexSelected];
                        // Select same row with item selected.
                        if (currentItem) {
                            self.aggregateItemSelectedCode(currentItem.code);
                        }
                        else {
                            // Select next higher row.
                            self.aggregateItemSelectedCode(self.itemList()[indexSelected - 1].code)
                        }
                    }
                    // If list is empty -> new mode.
                    else {
                        self.aggregateItemSelectedCode(null);
                    }
                });
            });
        }

        /**
         * Close dialog.
         */
        public close(): void {
            // Dirty check.
            var self = this;
            self.confirmDirtyAndExecute(() => nts.uk.ui.windows.close());
        }

        /**
         * Set style when re-rending list.
         */
        public setStyle(): void {
            // set width when swap list is rended.
            $('.master-table-label').attr('style', 'width: ' + $('#swap-list-gridArea1').width() + 'px');
            $('.sub-table-label').attr('style', 'width: ' + $('#swap-list-gridArea2').width() + 'px');
        }
        
        /**
         * Clear Error inputs.
         */
        public clearError(): void {
            if (nts.uk.ui._viewModel) {
                $('#code-input').ntsError('clear');
                $('#name-input').ntsError('clear');
                $('#swap-list').ntsError('clear');
            }
        }

        /**
         * Confirm dirty state and execute function.
         */
        private confirmDirtyAndExecute(functionToExecute: () => void, functionToExecuteIfNo?: () => void): void {
            var self = this;
            if (self.dirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                    functionToExecute();
                }).ifNo(function() {
                    if (functionToExecuteIfNo) {
                        functionToExecuteIfNo();
                    }
                });
            } else {
                functionToExecute();
            }
        }
    }

    /**
     * Aggregate detail model.
     */
    export class AggregateItemDetail {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        paymentType: string;
        category: string;
        showNameZeroValue: KnockoutObservable<boolean>;
        showValueZeroValue: KnockoutObservable<boolean>;
        masterItems: KnockoutObservableArray<service.Item>;
        masterItemsSelected: KnockoutObservableArray<string>;
        subItems: KnockoutObservableArray<service.SubItem>;
        subItemsSelected: KnockoutObservableArray<string>;
        showNameZeroCode: KnockoutObservable<string>;
        showValueZeroCode: KnockoutObservable<string>;
        createMode: KnockoutObservable<boolean>;

        constructor(paymentType: string, category: string,
            masterItems: service.Item[], item?: service.Item) {
            this.code = !item ? ko.observable('') : ko.observable(item.code);
            this.name = !item ? ko.observable('') : ko.observable(item.name);
            this.paymentType = paymentType;
            this.category = category;
            this.showNameZeroValue = !item ? ko.observable(true)
                : ko.observable(item.showNameZeroValue);
            this.showValueZeroValue = !item ? ko.observable(true)
                : ko.observable(item.showValueZeroValue);
            this.subItems = !item ? ko.observableArray([]) : ko.observableArray(item.subItems);
            this.showNameZeroCode = ko.observable(this.showNameZeroValue() ? '0' : '1');
            this.showValueZeroCode = ko.observable(this.showValueZeroValue() ? '0' : '1');
            this.createMode = ko.observable(!item);
            var self = this;

            // Computed show values variable.
            self.showNameZeroValue = ko.computed(function() {
                return self.showNameZeroCode() == '0';
            });
            self.showValueZeroValue = ko.computed(function() {
                return self.showValueZeroCode() == '0';
            })

            // exclude items contain in sub items.
            var subItemCodes = self.subItems().map(item => item.code);
            var masterItemsExcluded = masterItems.filter((item) => subItemCodes.indexOf(item.code) == -1);
            self.masterItems = ko.observableArray(masterItemsExcluded);
        }
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