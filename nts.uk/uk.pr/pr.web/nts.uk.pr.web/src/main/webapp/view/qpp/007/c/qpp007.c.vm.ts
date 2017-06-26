module nts.uk.pr.view.qpp007.c {
    export module viewmodel {
        export class ScreenModel {
            outputSettings: KnockoutObservableArray<OutputSettingHeader>;
            outputSettingSelectedCode: KnockoutObservable<string>;
            temporarySelectedCode: KnockoutObservable<string>;
            outputSettingColumns: KnockoutObservableArray<any>;
            outputSettingDetailModel: KnockoutObservable<OutputSettingDetailModel>;
            aggregateOutputItems: KnockoutObservableArray<AggregateOutputItem>;
            aggregateOutputItemSelected: KnockoutObservable<string>;
            aggregateOutputItemColumns: KnockoutObservableArray<any>;
            allAggregateItems: KnockoutObservableArray<AggregateItem>;
            allMasterItems: KnockoutObservableArray<MasterItem>;
            isLoading: KnockoutObservable<boolean>;
            isNewMode: KnockoutObservable<boolean>;
            isSomethingChanged: KnockoutObservable<boolean>;
            codeDirtyChecker: nts.uk.ui.DirtyChecker;
            nameDirtyChecker: nts.uk.ui.DirtyChecker;
            outputItemsDirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.isLoading = ko.observable(true);
                self.isNewMode = ko.observable(true);
                self.isSomethingChanged = ko.observable(false);
                self.outputSettings = ko.observableArray<OutputSettingHeader>([]);
                self.outputSettingSelectedCode = ko.observable('');
                self.temporarySelectedCode = ko.observable('');
                self.allAggregateItems = ko.observableArray<AggregateItem>([]);
                self.allMasterItems = ko.observableArray<MasterItem>([]);
                self.outputSettingDetailModel = ko.observable(new OutputSettingDetailModel(
                    self.allMasterItems,
                    self.allAggregateItems));
                self.aggregateOutputItems = ko.observableArray<AggregateOutputItem>([]);
                self.aggregateOutputItemSelected = ko.observable('');
                self.outputItemsDirtyChecker = new nts.uk.ui.DirtyChecker(self.aggregateOutputItems);
                self.codeDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetailModel().settingCode);
                self.nameDirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetailModel().settingName);

                // Define aggregateOutputItems column.
                self.aggregateOutputItemColumns = ko.observableArray<any>([
                    {headerText: 'key', prop: 'itemKey', width: 50, hidden: true},
                    { headerText: '区分', prop: 'categoryNameJPN', width: 40 },
                    {
                        headerText: '集約', prop: 'isAggregate', width: 40,
                        formatter: function(isAggregate: string) {
                            if (isAggregate == 'true') {
                                return '<div class="halign-center"><i class="icon icon-dot"></i></div>';
                            }
                            return '';
                        }
                    },
                    { headerText: 'コード', prop: 'code', width: 50 },
                    { headerText: '名称', prop: 'name', width: 100 },
                ]);

                // Init subscribe
                self.outputSettingDetailModel().categorySettings.subscribe(() => {
                    self.reloadAggregateOutputItems();
                    self.outputSettingDetailModel().reloadAggregateOutputItems = self.reloadAggregateOutputItems.bind(self);
                });

                self.temporarySelectedCode.subscribe(code => {
                    // Do nothing if code == null or undefined.
                    if (!code) {
                        return;
                    }
                    // Function to executed on confirmDirty dialog.
                    var executeIfConfirmed = () => {
                        self.outputSettingSelectedCode(self.temporarySelectedCode());
                        self.onSelectOutputSetting(code);
                    };
                    var executeIfCanceled = () => {
                        self.temporarySelectedCode(self.outputSettingSelectedCode());
                    };

                    // Do nothing if selected same code.
                    if (self.outputSettingSelectedCode() == code) {
                        return;
                    }
                    // Check dirty then execute appropriate function.
                    else {
                        self.confirmDirtyAndExecute(executeIfConfirmed, executeIfCanceled);
                    }
                })
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                var outputSettings: OutputSettingHeader[] = nts.uk.ui.windows.getShared('outputSettings');
                var selectedCode: string = nts.uk.ui.windows.getShared('selectedCode');
                $.when(self.loadMasterItems(), self.loadAggregateItems()).done(() => {
                    self.isLoading(false);
                    // New mode if there is 0 outputSettings. 
                    if (!outputSettings || outputSettings.length == 0) {
                        self.enableNewMode();
                    }
                    // else select selected code from caller screen.
                    else {
                        self.outputSettings(outputSettings);
                        self.temporarySelectedCode(selectedCode);
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
            * Open common setting dialog.
            */
            public onCommonSettingBtnClicked(): void {
                var self = this;
                nts.uk.ui.windows.sub.modal('/view/qpp/007/j/index.xhtml', { title: '集計項目の設定', dialogClass: 'no-close' })
                    .onClosed(function() {
                        // Reload aggregateItems.
                        self.loadAggregateItems().done(() => {
                            let selectedCode = self.outputSettingDetailModel().settingCode();
                            // Update mode.
                            if (selectedCode) {
                                self.loadOutputSettingDetail(selectedCode);
                            } 
                            // New mode.
                            else {
                                self.outputSettingDetailModel().updateData();
                            }
                        });
                    });
            }

            /**
             * Clear errors and enable new mode.
             */
            public onNewModeBtnClicked(): void {
                var self = this;
                self.confirmDirtyAndExecute(function() {
                    self.clearError();
                    self.enableNewMode();
                });
            }

            /**
             * Save outputSetting.
             */
            public onSaveBtnClicked(): void {
                var self = this;
                // Validate.
                $('.nts-input').ntsEditor('validate');
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                var data = self.collectData();
                // Set isCreateMode.
                if (self.isNewMode()) {
                    data.createMode = true;
                } else {
                    data.createMode = false;
                }
                // Save.
                service.save(data).done(() => {
                    self.isNewMode(false);
                    // Used for parent screen.
                    self.isSomethingChanged(true);
                    self.resetDirty();
                    self.loadAllOutputSetting()
                        .done(() => self.temporarySelectedCode(self.outputSettingDetailModel().settingCode()));
                }).fail(res => {
                    // Clear old error messages & set new error messages.
                    self.clearError();
                    switch (res.messageId) {
                        case 'ER011':
                            $('#inpCode').ntsError('set', '入力したコードは既に存在しています。\r\n コードを確認してください。');
                            break;
                        case 'ER026':
                            $('#contents-area').ntsError('set', '更新対象のデータが存在しません。');
                            break;
                        case 'ER027':
                            if (!self.outputSettingDetailModel().settingCode()) {
                                $('#inpCode').ntsError('set', '入力にエラーがあります。');
                            }
                            if (!self.outputSettingDetailModel().settingName()) {
                                $('#inpName').ntsError('set', '入力にエラーがあります。');
                            }
                            break;
                        default:
                            console.log(res);
                    }
                });
            }

            /**
             * Delete selected outputSetting.
             */
            public onRemoveBtnClicked(): void {
                var self = this;
                var selectedCode = self.outputSettingSelectedCode();
                if (selectedCode) {
                    nts.uk.ui.dialog.confirm("データを削除します。\r\n よろしいですか？").ifYes(function() {
                        service.remove(selectedCode).done(() => {
                            // Used for parent screen.
                            self.isSomethingChanged(true);

                            // Find selected outputSetting.
                            var selectedOutputSetting = self.outputSettings().filter(item => item.code == selectedCode)[0];
                            var selectedIndex = self.outputSettings().indexOf(selectedOutputSetting);

                            // Remove selected setting from list.
                            self.outputSettings.remove(selectedOutputSetting);
                            // Reset dirty.
                            self.resetDirty();

                            if (self.outputSettings() && self.outputSettings().length > 0) {
                                var currentSetting = self.outputSettings()[selectedIndex];
                                // Select setting with the same index.
                                if (currentSetting) {
                                    self.temporarySelectedCode(currentSetting.code);
                                }
                                // Select next setting.
                                else {
                                    self.temporarySelectedCode(self.outputSettings()[selectedIndex - 1].code);
                                }
                            }
                            // Set new mode if all setting is removed.
                            else {
                                self.clearError();
                                self.enableNewMode();
                            }

                        });
                    });
                }
            }

            /**
             * Close dialog.
             */
            public onCloseBtnClicked(): void {
                var self = this;
                self.confirmDirtyAndExecute(function() {
                    nts.uk.ui.windows.setShared('isSomethingChanged', self.isSomethingChanged());
                    nts.uk.ui.windows.close();
                });
            }

            /**
              * On select outputSetting
              */
            private onSelectOutputSetting(code: string): void {
                var self = this;
                self.loadOutputSettingDetail(code).done(() => {
                    self.isNewMode(false);
                    self.isLoading(false);
                    self.clearError();
                });
            }

            /**
            * Collect Data
            */
            private collectData(): OutputSettingDto {
                var self = this;
                var model = self.outputSettingDetailModel();
                // Convert model to dto.
                var dto = new OutputSettingDto();
                dto.code = model.settingCode();
                dto.name = model.settingName();

                var categorySettingDto = new Array<CategorySettingDto>();
                model.categorySettings().forEach(setting => {
                    // Set order number.
                    for (var i = 0; i < setting.outputItems().length; i++) {
                        setting.outputItems()[i].orderNumber = i;
                    }
                    categorySettingDto.push(new CategorySettingDto(setting.categoryName, setting.outputItems().map(item => {
                        var mappedItem = item;
                        // map Attendance && ArticleOthers item.
                        if (!item.isAggregateItem) {
                            mappedItem = new OutputItem();
                            mappedItem.code = item.code;
                            mappedItem.name = item.name;
                            mappedItem.orderNumber = item.orderNumber;
                            mappedItem.isAggregateItem = false;
                        }
                        return mappedItem;
                    })));
                });

                // Set categorySettings.
                dto.categorySettings = categorySettingDto;

                // return dto.
                return dto;
            }

            /**
            * Clear all input errors.
            */
            private clearError(): void {
                if (nts.uk.ui._viewModel) {
                    $('#inpCode').ntsError('clear');
                    $('#inpName').ntsError('clear');
                    $('#contents-area').ntsError('clear');
                }
            }

            /**
             * Confirm dirty state and execute function.
             */
            private confirmDirtyAndExecute(functionToExecute: () => void, functionToExecuteIfNo?: () => void) {
                var self = this;
                if (self.isDirty()) {
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

            /**
            * Reset selected code & all inputs.
            */
            private enableNewMode(): void {
                var self = this;
                self.outputSettingDetailModel().updateData();
                self.temporarySelectedCode(null);
                self.resetDirty();
                self.isNewMode(true);
            }

            /**
             * Check is dirty.
             */
            private isDirty(): boolean {
                var self = this;
                if (self.codeDirtyChecker.isDirty()
                    || self.nameDirtyChecker.isDirty()
                    || self.outputItemsDirtyChecker.isDirty()) {
                    return true;
                }
                return false;
            }

            /**
             * Reset dirty checker.
             */
            private resetDirty(): void {
                var self = this;
                self.codeDirtyChecker.reset();
                self.nameDirtyChecker.reset();
                self.outputItemsDirtyChecker.reset();
            }

            /**
            * Reload aggregateOutput items.
            */
            private reloadAggregateOutputItems(): void {
                var self = this;
                var data: Array<CategorySettingModel> = self.outputSettingDetailModel().categorySettings();
                var list: AggregateOutputItem[] = [];
                // Set data to aggregateOutputItem list.
                if (data && data.length > 0) {
                    data.forEach((setting) => {
                        var categoryName: SalaryCategory = setting.categoryName;
                        setting.outputItems().forEach((item) => {
                            list.push(new AggregateOutputItem(item.code, item.name, categoryName, item.isAggregateItem));
                        });
                    });
                }
                self.aggregateOutputItems(list);
            }

            /**
             * Load all output setting.
             */
            private loadAllOutputSetting(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findAllOutputSettings().done(data => {
                    self.outputSettings(data);
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject();
                })
                return dfd.promise();
            }

            /**
             * Load detail output setting.
             */
            private loadOutputSettingDetail(code: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                if (code) {
                    service.findOutputSettingDetail(code).done(function(data: OutputSettingDto) {
                        self.outputSettingDetailModel().updateData(data);
                        // Reset dirty.
                        self.resetDirty();
                        dfd.resolve();
                    }).fail(function(res) {
                        dfd.reject();
                    });
                }
                return dfd.promise();
            }

            /**
            * Load aggregate items
            */
            private loadAggregateItems(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findAllAggregateItems().done(res => {
                    self.allAggregateItems.removeAll();
                    res.forEach(function(item: any) {
                        self.allAggregateItems.push({
                            code: item.salaryAggregateItemCode,
                            name: item.salaryAggregateItemName,
                            taxDivision: item.taxDivision,
                        });
                    });
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
            * Load master items
            */
            private loadMasterItems(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findAllMasterItems().done(res => {
                    self.allMasterItems(res);
                    dfd.resolve();
                });
                return dfd.promise();
            }

        }

        /**
         * OutputSettingHeader model.
         */
        export class OutputSettingHeader {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }

        /**
         * OutputSettingDto model.
         */
        export class OutputSettingDto {
            code: string;
            name: string;
            categorySettings: CategorySettingDto[];
            createMode: boolean;
        }

        /**
         * OutputSettingDetailModel model.
         */
        export class OutputSettingDetailModel {
            settingCode: KnockoutObservable<string>;
            settingName: KnockoutObservable<string>;
            categorySettingTabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedCategory: KnockoutObservable<string>;
            categorySettings: KnockoutObservableArray<CategorySettingModel>;
            aggregateItems: KnockoutObservableArray<AggregateItem>;
            masterItems: KnockoutObservableArray<MasterItem>;
            reloadAggregateOutputItems: () => void;
            constructor(masterItems: KnockoutObservableArray<MasterItem>,
                aggregateItems: KnockoutObservableArray<AggregateItem>,
                outputSetting?: OutputSettingDto) {
                this.settingCode = ko.observable(outputSetting != undefined ? outputSetting.code : '');
                this.settingName = ko.observable(outputSetting != undefined ? outputSetting.name : '');
                this.aggregateItems = aggregateItems;
                this.masterItems = masterItems;

                // construct categorySettings.
                var settings: CategorySettingModel[] = [];
                if (outputSetting == undefined) {
                    settings = this.toModel();
                } else {
                    settings = this.toModel(outputSetting.categorySettings);
                }
                this.categorySettings = ko.observableArray<CategorySettingModel>(settings);

                this.categorySettingTabs = ko.observableArray<nts.uk.ui.NtsTabPanelModel>([
                    { id: SalaryCategory.PAYMENT, title: '支給', content: '#payment', 
                    enable: ko.observable(true), visible: ko.observable(true) },
                    { id: SalaryCategory.DEDUCTION, title: '控除', content: '#deduction', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: SalaryCategory.ATTENDANCE, title: '勤怠', content: '#attendance', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: SalaryCategory.ARTICLE_OTHERS, title: '記事・その他', content: '#article-others', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                this.selectedCategory = ko.observable(SalaryCategory.PAYMENT);
                var self = this;
                self.categorySettings().forEach(setting => {
                    setting.outputItems.subscribe(() => {
                        self.reloadAggregateOutputItems();
                    })
                });
            }

            /**
             * Update masterItems, aggregateItems, categorySettings.
             */
            public updateData(outputSetting?: OutputSettingDto): void {
                var self = this;
                self.settingCode(outputSetting != undefined ? outputSetting.code : '');
                self.settingName(outputSetting != undefined ? outputSetting.name : '');
                // construct categorySettings.
                var settings: CategorySettingModel[] = [];
                if (outputSetting == undefined) {
                    settings = self.toModel();
                } else {
                    settings = self.toModel(outputSetting.categorySettings);
                }
                self.categorySettings(settings);
                // ReSubscribe.
                self.categorySettings().forEach(setting => {
                    setting.outputItems.subscribe(() => {
                        self.reloadAggregateOutputItems();
                    })
                });
            }

            /**
            * Convert category setting data from dto to screen model.
            */
            private toModel(categorySettings?: CategorySettingDto[]): CategorySettingModel[] {
                var settings: CategorySettingModel[] = [];
                var categorySettingDtos: CategorySettingDto[];
                if (categorySettings && categorySettings.length > 0) {
                    categorySettingDtos = categorySettings;
                }

                settings[0] = this.filterSettingByCategory(SalaryCategory.PAYMENT, categorySettingDtos);
                settings[1] = this.filterSettingByCategory(SalaryCategory.DEDUCTION, categorySettingDtos);
                settings[2] = this.filterSettingByCategory(SalaryCategory.ATTENDANCE, categorySettingDtos);
                settings[3] = this.filterSettingByCategory(SalaryCategory.ARTICLE_OTHERS, categorySettingDtos);
                return settings;
            }

            private filterSettingByCategory(category: SalaryCategory, categorySettings?: CategorySettingDto[]): CategorySettingModel {
                var cateTempSetting: CategorySettingDto = { category: category, outputItems: [] };
                if (categorySettings == undefined) {
                    return new CategorySettingModel(category, this.masterItems, this.aggregateItems, cateTempSetting);
                }

                var categorySetting = categorySettings.filter(item => item.category == category)[0];
                if (categorySetting == undefined) {
                    categorySetting = cateTempSetting;
                }
                return new CategorySettingModel(category, this.masterItems, this.aggregateItems, categorySetting);
            }

        }

        /**
         * CategorySettingDto model.
         */
        export class CategorySettingDto {
            category: SalaryCategory;
            outputItems: OutputItem[];
            constructor(category: SalaryCategory,
                outputItems: OutputItem[]) {
                this.category = category;
                this.outputItems = outputItems;
            }
        }

        /**
         * ReportItem model.
         */
        export class AggregateOutputItem {
            code: string;
            name: string;
            categoryNameJPN: string;
            isAggregate: boolean;
            itemKey: string;
            
            constructor(code: string, name: string, categoryName: SalaryCategory, isAggregate: boolean) {
                this.code = code;
                this.name = name;
                this.isAggregate = isAggregate;
                var self = this;
                switch (categoryName) {
                    case SalaryCategory.PAYMENT:
                        self.categoryNameJPN = '支給';
                        break;
                    case SalaryCategory.DEDUCTION:
                        self.categoryNameJPN = '控除';
                        break;
                    case SalaryCategory.ATTENDANCE:
                        self.categoryNameJPN = '勤怠';
                        break;
                    case SalaryCategory.ARTICLE_OTHERS:
                        self.categoryNameJPN = '記事・その他';
                        break;
                    default:
                        self.categoryNameJPN = '';
                }
                self.itemKey = code + isAggregate + categoryName;
            }
        }

        /**
         * CategorySettingModel model.
         */
        export class CategorySettingModel {
            categoryName: SalaryCategory;
            aggregateItems: KnockoutObservableArray<AggregateItem>;
            aggregateItemsSelected: KnockoutObservableArray<string>;
            masterItems: KnockoutObservableArray<MasterItem>;
            masterItemsSelected: KnockoutObservableArray<string>;
            outputItems: KnockoutObservableArray<OutputItem>;
            outputItemSelected: KnockoutObservable<string>;
            outputItemsSelected: KnockoutObservableArray<string>;
            outputItemColumns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            constructor(categoryName: SalaryCategory,
                masterItems: KnockoutObservableArray<MasterItem>,
                aggregateItems: KnockoutObservableArray<AggregateItem>,
                categorySetting?: CategorySettingDto) {
                var self = this;
                self.categoryName = categoryName;
                self.aggregateItems = ko.observableArray<AggregateItem>([]);
                self.aggregateItemsSelected = ko.observableArray<string>([]);
                self.masterItems = ko.observableArray<MasterItem>([]);
                self.masterItemsSelected = ko.observableArray<string>([]);
                self.outputItems = ko.observableArray<OutputItem>(categorySetting != undefined ? categorySetting.outputItems : []);
                self.outputItemSelected = ko.observable(null);
                self.outputItemsSelected = ko.observableArray<string>([]);

                // Filter masterItems & aggregateItems by category.
                switch (categoryName) {
                    case SalaryCategory.PAYMENT:
                        aggregateItems().forEach(item => {
                            if (item.taxDivision == TaxDivision.PAYMENT) {
                                self.aggregateItems.push(item);
                            }
                        });
                        masterItems().forEach(item => {
                            if (item.category == SalaryCategory.PAYMENT) {
                                self.masterItems.push(item);
                            }
                        });
                        break;
                    case SalaryCategory.DEDUCTION:
                        aggregateItems().forEach(item => {
                            if (item.taxDivision == TaxDivision.DEDUCTION) {
                                self.aggregateItems.push(item);
                            }
                        });
                        masterItems().forEach(item => {
                            if (item.category == SalaryCategory.DEDUCTION) {
                                self.masterItems.push(item);
                            }
                        });
                        break;
                    case SalaryCategory.ATTENDANCE:
                        masterItems().forEach(item => {
                            if (item.category == SalaryCategory.ATTENDANCE) {
                                self.masterItems.push(item);
                            }
                        });
                        break;
                    case SalaryCategory.ARTICLE_OTHERS:
                        masterItems().forEach(item => {
                            if (item.category == SalaryCategory.ARTICLE_OTHERS
                                || item.category == 'Other'
                                || item.category == 'Articles') {
                                self.masterItems.push(item);
                            }
                        });
                        break;
                    default: // Do nothing.
                }

                /**
                 * Remove aggregate & master items exist in outputItems.
                 * Replace output item with name.
                 */
                var existCodes: string[] = [];
                if (categorySetting != undefined) {
                    existCodes = categorySetting.outputItems.map(item => {
                        return item.code;
                    });
                }
                existCodes.forEach(code => {
                    let outputItem: OutputItem;
                    let aggrItem = self.aggregateItems().filter(item => code == item.code)[0];
                    let masterItem = self.masterItems().filter(item => code == item.code)[0];
                    if (aggrItem) {
                        self.aggregateItems.remove(aggrItem);
                        outputItem = {
                            code: aggrItem.code,
                            name: aggrItem.name,
                            isAggregateItem: true,
                            orderNumber: 1
                        };
                    } else {
                        self.masterItems.remove(masterItem);
                        outputItem = {
                            code: masterItem.code,
                            name: masterItem.name,
                            isAggregateItem: false,
                            orderNumber: 1
                        };
                    }
                    self.outputItems.replace(self.outputItems().filter(item => code == item.code)[0], outputItem);
                });

                // Define outputItemColumns.
                this.outputItemColumns = ko.observableArray<any>([
                    {
                        headerText: '集約', prop: 'isAggregateItem', width: 40,
                        formatter: function(data: string) {
                            if (data == 'true') {
                                return '<div class="halign-center"><i class="icon icon-dot"></i></div>';
                            }
                            return '';
                        }
                    },
                    { headerText: 'コード', prop: 'code', width: 50 },
                    { headerText: '名称', prop: 'name', width: 120 },
                    {
                        headerText: '削除', prop: 'code', width: 60,
                        formatter: function(code: string) {
                            return '<div class="halign-center"><button class="icon icon-close" id="' + code + '" >'
                                + '</button></div>';
                        }
                    }
                ]);

                // Customs handle.
                (<any>ko.bindingHandlers).delete = {
                    init: function(element: any, valueAccessor: any, allBindings: any, viewModel: any, bindingContext: any) { },
                    update: function(element: any, valueAccessor: any, allBindings: any, viewModel: CategorySettingModel, bindingContext: any) {
                        var code = valueAccessor();
                        viewModel.outputItems().forEach(item => {
                            $('#' + item.code).on('click', function() {
                                code(item.code);
                                viewModel.remove();
                                code(null);
                            })
                        });
                    }
                };
            }

            /**
            * Move master item to outputItems.
            */
            public moveMasterItem() {
                // Check if has selected
                if (this.masterItemsSelected()[0]) {
                    var self = this;
                    // Get selected items from selected code list.
                    var selectedItems: MasterItem[] = [];
                    self.masterItemsSelected().forEach(selectedCode => {
                        selectedItems.push(self.masterItems().filter((item: MasterItem) => {
                            return selectedCode == item.code;
                        })[0]);
                    });

                    // Remove from master list and add to output list
                    selectedItems.forEach(item => {
                        self.masterItems.remove(item);
                        self.outputItems.push({
                            code: item.code,
                            name: item.name,
                            isAggregateItem: false,
                            orderNumber: 1
                        });
                    });
                    self.masterItemsSelected([]);
                }
            }

            /**
            * Move aggregate item to outputItems.
            */
            public moveAggregateItem() {
                // Check if has selected
                if (this.aggregateItemsSelected()[0]) {
                    var self = this;
                    // Get selected items from selected code list.
                    var selectedItems: AggregateItem[] = [];
                    self.aggregateItemsSelected().forEach(selectedCode => {
                        selectedItems.push(self.aggregateItems().filter((item: AggregateItem) => {
                            return selectedCode == item.code;
                        })[0]);
                    });

                    // Remove from aggregate list and add to output list
                    selectedItems.forEach(item => {
                        self.aggregateItems.remove(item);
                        self.outputItems.push({
                            code: item.code,
                            name: item.name,
                            isAggregateItem: true,
                            orderNumber: 1
                        });
                    });
                    self.aggregateItemsSelected([]);
                }
            }

            /**
            * Remove item from outputItems.
            */
            public remove(): void {
                var self = this;
                var selectedItem = self.outputItems().filter((item) => {
                    return item.code == self.outputItemSelected();
                })[0];
                self.outputItems.remove(selectedItem);

                // Return item.
                if (selectedItem.isAggregateItem) {
                    // Return to Aggregate items table.
                    self.aggregateItems.push({
                        code: selectedItem.code,
                        name: selectedItem.name,
                        taxDivision: TaxDivision.PAYMENT
                    });
                    return;
                }
                // Return to master items table.
                self.masterItems.push({
                    code: selectedItem.code,
                    name: selectedItem.name,
                    category: SalaryCategory.PAYMENT
                });
            }

        }
        export class AggregateItem {
            code: string;
            name: string;
            taxDivision: TaxDivision;
        }
        export class MasterItem {
            code: string;
            name: string;
            category: SalaryCategory;
        }
        export class OutputItem {
            code: string;
            name: string;
            isAggregateItem: boolean;
            orderNumber: number;
        }
        export class SalaryCategory {
            static PAYMENT = 'Payment';
            static DEDUCTION = 'Deduction';
            static ATTENDANCE = 'Attendance';
            static ARTICLE_OTHERS = 'ArticleOthers';
        }
        export class TaxDivision {
            static PAYMENT = 'Payment';
            static DEDUCTION = 'Deduction';
        }
        export class SalaryOutputDistinction {
            static HOURLY = 'Hourly';
            static MINUTLY = 'Minutely';
        }
    }
}