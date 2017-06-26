module nts.uk.pr.view.qmm016.a {
    export module viewmodel {
        import ElementSettingDto = model.ElementSettingDto;

        export var elementTypes: Array<model.ElementTypeDto>;
        export function getElementTypeByValue(val: number): model.ElementTypeDto {
            return _.filter(viewmodel.elementTypes, (el) => {
                return el.value == val;
            })[0];
        }

        export class ScreenModel extends base.simplehistory.viewmodel.ScreenBaseModel<model.WageTable, model.WageTableHistory> {
            // For UI Tab.
            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<any>;

            wageTableList: any;
            monthRange: KnockoutComputed<string>;

            // Head part viewmodel.
            head: KnockoutObservable<HeadViewModel>;

            // History part viewmodel.
            history: KnockoutObservable<HistoryViewModel>;

            // FIXED PART
            generalTableTypes: KnockoutObservableArray<model.DemensionElementCountType>;
            specialTableTypes: KnockoutObservableArray<model.DemensionElementCountType>;

            // Dirty checker
            headDirtyChecker: nts.uk.ui.DirtyChecker;
            settingDirtyChecker: nts.uk.ui.DirtyChecker;
            valuesDirtyChecker: nts.uk.ui.DirtyChecker;
            valueItems: KnockoutObservable<Array<model.CellItemDto>>;

            demensionBullet: Array<string>;

            constructor() {
                super({
                    functionName: '賃金テープル',
                    service: service.instance,
                    removeMasterOnLastHistoryRemove: true
                });
                var self = this;

                // Head view model.
                self.head = ko.observable<HeadViewModel>(new HeadViewModel());
                self.history = ko.observable<HistoryViewModel>(new HistoryViewModel());

                // Tabs.
                self.selectedTab = ko.observable('tab-1');
                self.tabs = ko.observableArray([
                    {
                        id: 'tab-1',
                        title: '基本情報',
                        content: '#tab-content-1',
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    },
                    {
                        id: 'tab-2',
                        title: '賃金テーブルの情報',
                        content: '#tab-content-2',
                        enable: ko.computed(() => {
                            return !self.isNewMode();
                        }),
                        visible: ko.observable(true)
                    }
                ]);

                // General table type init.
                self.generalTableTypes = ko.observableArray(model.normalDemension);
                self.specialTableTypes = ko.observableArray(model.specialDemension);

                // Set dirtyChecker
                self.valueItems = ko.observable<Array<model.CellItemDto>>([]);
                self.headDirtyChecker = new nts.uk.ui.DirtyChecker(self.head);
                self.settingDirtyChecker = new nts.uk.ui.DirtyChecker(self.history().elements);
                self.valuesDirtyChecker = new nts.uk.ui.DirtyChecker(self.valueItems);

                self.demensionBullet = ["①", "②", "③"];
            }

            /**
             * Start load data.
             */
            start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                service.instance.loadElementList().done(res => {
                    elementTypes = res;
                    dfd.resolve();
                })
                return dfd.promise();
            }

            /**
             * Do check dirty later.
             */
            isDirty(): boolean {
                var self = this;
                self.valueItems(self.history().detailViewModel ? self.history().detailViewModel.getCellItem() : []);
                return self.headDirtyChecker.isDirty() ||
                    self.settingDirtyChecker.isDirty() ||
                    self.valuesDirtyChecker.isDirty();
            }

            /**
            * Load wage table detail.
            */
            onSelectHistory(id: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.instance.loadHistoryByUuid(id).done(model => {
                    $.when(self.head().resetBy(model.head), self.history().resetBy(model.head, model.history)).done(function() {
                        self.valueItems(self.history().detailViewModel.getCellItem());
                        self.headDirtyChecker.reset();
                        self.settingDirtyChecker.reset();
                        self.valuesDirtyChecker.reset();
                    })
                }).fail(function(error) {
                    nts.uk.ui.dialog.alert(error.message);
                });
                dfd.resolve();
                return dfd.promise();
            }

            // Validate data
            private validateData() {
                $("#inp_code").ntsEditor("validate");
                $("#inp_name").ntsEditor("validate");
                $("#inp_start_date").ntsEditor("validate");
                if ($('.nts-editor').ntsError("hasError")) {
                    return true;
                }
                return false;
            }

            //function clear message error
            private clearErrorSave() {
                $('.save-error').ntsError('clear');
            }

            /**
             * Create or Update UnitPriceHistory.
             */
            onSave(): JQueryPromise<string> {
                var self = this;
                var dfd = $.Deferred<string>();

                self.clearErrorSave();
                if (self.validateData()) {
                    return dfd.promise();
                }

                // New mode.
                if (self.isNewMode()) {
                    // Reg new.
                    var wagetableDto = self.head().getWageTableDto();
                    service.instance.initWageTable({
                        wageTableHeadDto: wagetableDto,
                        startMonth: self.history().startYearMonth()
                    }).done(res => {
                        dfd.resolve(res.uuid);
                        self.headDirtyChecker.reset();
                        self.settingDirtyChecker.reset();
                        self.valuesDirtyChecker.reset();
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alert(error.message);
                    });
                } else {
                    // Update mode.
                    service.instance.updateHistory({
                        code: self.head().code(),
                        name: self.head().name(),
                        memo: self.head().memo(),
                        wtHistoryDto: self.history().getWageTableHistoryDto()
                    }).done(() => {
                        dfd.resolve(self.history().history.historyId);
                        self.valueItems(self.history().detailViewModel.getCellItem());
                        self.headDirtyChecker.reset();
                        self.settingDirtyChecker.reset();
                        self.valuesDirtyChecker.reset();
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alert(error.message);
                    });
                }
                return dfd.promise();
            }

            /**
             * Clear all input and switch to new mode.
             */
            onRegistNew(): void {
                var self = this;
                self.selectedTab('tab-1');
                self.head().reset();
                self.headDirtyChecker.reset();
                self.settingDirtyChecker.reset();
                self.valuesDirtyChecker.reset();
            }

            /**
             * Show group setting screen.
             */
            btnGroupSettingClick(): void {
                var self = this;
                var ntsDialogOptions = {
                    title: '資格グループの設定',
                    dialogClass: 'no-close'
                };
                nts.uk.ui.windows.sub.modal('/view/qmm/016/l/index.xhtml', ntsDialogOptions);
            }

            /**
             * Download input file.
             */
            btnInputFileDownload(): void {
                var self = this;
                nts.uk.request.exportFile('/screen/pr/qmm016/inputfile', {
                    code: self.head().code(),
                    name: self.head().name(),
                    memo: self.head().memo(),
                    wtHistoryDto: self.history().getWageTableHistoryDto()
                });
            }
        }

        /**
         * Wage table head dto.
         */
        export class HeadViewModel {
            /** The code. */
            code: KnockoutObservable<string>;

            /** The name. */
            name: KnockoutObservable<string>;

            /** The demension set. */
            demensionSet: KnockoutObservable<number>;

            demensionType: KnockoutObservable<model.DemensionElementCountType>;

            lblContent: KnockoutComputed<string>;

            lblSampleImgLink: KnockoutComputed<string>;

            /** The memo. */
            memo: KnockoutObservable<string>;

            /**
             * The demension item list.
             */
            demensionItemList: KnockoutObservableArray<DemensionItemViewModel>;

            // Flag mode.
            isNewMode: KnockoutObservable<boolean>;

            /**
             * Const.
             */
            constructor() {
                var self = this;
                self.code = ko.observable(undefined);
                self.name = ko.observable(undefined);
                self.demensionSet = ko.observable(undefined);
                self.memo = ko.observable(undefined);
                self.demensionType = ko.computed(() => {
                    return model.demensionMap[self.demensionSet()];
                });

                self.lblContent = ko.computed(() => {
                    var contentMap = [
                        '１つの要素でテーブルを作成します。',
                        '２つの要素でテーブルを作成します。',
                        '３つの要素でテーブルを作成します。',
                        '資格手当用のテーブルを作成します。',
                        '精皆勤手当て用のテーブルを作成します。'
                    ];

                    return contentMap[self.demensionSet()];
                });

                self.lblSampleImgLink = ko.computed(() => {
                    var linkMap = [
                        '１.png',
                        '２.png',
                        '３.png',
                        '4.png',
                        '5.png'
                    ];

                    return linkMap[self.demensionSet()];
                });

                self.demensionItemList = ko.observableArray<DemensionItemViewModel>([]);

                self.demensionSet.subscribe(val => {
                    // Not new mode.
                    if (!self.isNewMode()) {
                        return
                    }
                    self.demensionItemList(self.getDemensionItemListByType(val));
                })

                self.isNewMode = ko.observable(true);
                self.reset();
            }

            /**
             * Reset.
             */
            reset(): void {
                var self = this;
                self.isNewMode(true);
                self.code('');
                self.name('');
                self.demensionSet(model.allDemension[0].code);
                self.demensionItemList(self.getDemensionItemListByType(self.demensionSet()));
                self.memo('');
            }

            /**
             * Wage table dto.
             */
            getWageTableDto(): model.WageTableHeadDto {
                var self = this;
                var dto = <model.WageTableHeadDto>{};
                dto.code = self.code();
                dto.name = self.name();
                dto.memo = self.memo();
                dto.mode = self.demensionSet();
                dto.elements = _.map(self.demensionItemList(), (item) => {
                    var elementDto = <model.ElementDto>{};
                    elementDto.demensionName = item.elementName();
                    elementDto.demensionNo = item.demensionNo();
                    elementDto.type = item.elementType();
                    elementDto.referenceCode = item.elementCode();
                    return elementDto;
                });
                return dto;
            }

            /**
             * Get default demension item list by default.
             */
            getDemensionItemListByType(typeCode: number): Array<DemensionItemViewModel> {
                // Regenerate.
                var newDemensionItemList = new Array<DemensionItemViewModel>();
                switch (typeCode) {
                    case 0:
                        newDemensionItemList.push(new DemensionItemViewModel(1));
                        break;
                    case 1:
                        newDemensionItemList.push(new DemensionItemViewModel(1));
                        newDemensionItemList.push(new DemensionItemViewModel(2));
                        break;

                    case 2:
                        newDemensionItemList.push(new DemensionItemViewModel(1));
                        newDemensionItemList.push(new DemensionItemViewModel(2));
                        newDemensionItemList.push(new DemensionItemViewModel(3));
                        break;

                    // Certificate.
                    case 3:
                        {
                            let cert = new DemensionItemViewModel(1);
                            cert.elementType(6);
                            cert.elementName('資格名称');
                            newDemensionItemList.push(cert);
                        }
                        break;

                    // Attendance.
                    case 4:
                        {
                            let workDay = new DemensionItemViewModel(1);
                            workDay.elementType(7);
                            workDay.elementName('欠勤日数');
                            let late = new DemensionItemViewModel(2);
                            late.elementType(8);
                            late.elementName('遅刻・早退回数');
                            let level = new DemensionItemViewModel(3);
                            level.elementType(9);
                            level.elementName('精皆勤レベル');
                            newDemensionItemList.push(workDay);
                            newDemensionItemList.push(late);
                            newDemensionItemList.push(level);
                        }
                        break;
                }

                // Ret.
                return newDemensionItemList;
            }

            /**
             * Reset by wage table.
             */
            resetBy(head: model.WageTableHeadDto): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred();
                self.isNewMode(false);
                self.code(head.code);
                self.name(head.name);
                self.demensionSet(head.mode);
                self.demensionItemList(_.map(head.elements, (item) => {
                    var itemViewModel = new DemensionItemViewModel(item.demensionNo);
                    itemViewModel.resetBy(item);
                    return itemViewModel;
                }));
                self.memo(head.memo);
                dfd.resolve();
                return dfd.promise();
            }

            /**
             * On select demension btn click.
             */
            onSelectDemensionBtnClick(demension: DemensionItemViewModel) {
                var self = this;
                var dlgOptions: k.viewmodel.Option = {
                    selectedDemensionDto: _.map(self.demensionItemList(), (item) => {
                        var dto = <model.DemensionItemDto>{};
                        dto.type = item.elementType();
                        dto.code = item.elementCode();
                        return dto;
                    }),
                    onSelectItem: (data) => {
                        demension.elementType(data.demension.type);
                        demension.elementCode(data.demension.code);
                        demension.elementName(data.demension.name);
                    }
                };
                nts.uk.ui.windows.setShared('options', dlgOptions);
                var ntsDialogOptions = { title: '要素の選択', dialogClass: 'no-close' };
                nts.uk.ui.windows.sub.modal('/view/qmm/016/k/index.xhtml', ntsDialogOptions);
            }
        }

        /**
         * Wage table demension detail dto.
         */
        export class DemensionItemViewModel {
            demensionNo: KnockoutObservable<number>;
            elementType: KnockoutObservable<number>;
            elementCode: KnockoutObservable<string>;
            elementName: KnockoutObservable<string>;

            /**
             * Demension item view model.
             */
            constructor(demensionNo: number) {
                var self = this;
                self.demensionNo = ko.observable(demensionNo);
                self.elementType = ko.observable(0);
                self.elementCode = ko.observable('');
                self.elementName = ko.observable('');
            }

            resetBy(element: model.ElementDto) {
                var self = this;
                self.elementType(element.type);
                self.elementCode(element.referenceCode);
                self.elementName(element.demensionName);
            }
        }

        /**
         * History model.
         */
        export class HistoryViewModel {
            startYearMonth: KnockoutObservable<number>;
            endYearMonth: KnockoutObservable<number>;
            startYearMonthText: KnockoutObservable<string>;
            endYearMonthText: KnockoutObservable<string>;
            startYearMonthJpText: KnockoutObservable<string>;
            elements: KnockoutObservableArray<HistoryElementSettingViewModel>;
            detailViewModel: history.base.BaseHistoryViewModel;
            history: model.WageTableHistoryDto;

            constructor() {
                var self = this;
                self.startYearMonth = ko.observable(parseInt(nts.uk.time.formatDate(new Date(), 'yyyyMM')));
                self.endYearMonth = ko.observable(99999);
                self.startYearMonthText = ko.computed(() => {
                    return nts.uk.time.formatYearMonth(self.startYearMonth());
                })
                self.endYearMonthText = ko.computed(() => {
                    return nts.uk.time.formatYearMonth(self.endYearMonth());
                })
                self.startYearMonthJpText = ko.computed(() => {
                    return nts.uk.text.format('（{0}）', nts.uk.time.yearmonthInJapanEmpire(self.startYearMonth()).toString());
                })

                // Element info.
                self.elements = ko.observableArray<HistoryElementSettingViewModel>([]);
            }

            /**
             * Reset.
             */
            public resetBy(head: model.WageTableHeadDto, history: model.WageTableHistoryDto): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                self.history = history;
                self.startYearMonth(history.startMonth);
                self.endYearMonth(history.endMonth);
                var elementSettingViewModel = _.map(history.elements, (el) => {
                    return new HistoryElementSettingViewModel(head, el);
                })
                self.elements(elementSettingViewModel);

                // Load detail.
                if ($('#detailContainer').children().length > 0) {
                    var element = $('#detailContainer').children().get(0);
                    ko.cleanNode(element);
                    $('#detailContainer').empty();
                }

                self.detailViewModel = this.getDetailViewModelByType(head.mode);
                $('#detailContainer').load(self.detailViewModel.htmlPath, () => {
                    var element = $('#detailContainer').children().get(0);
                    self.detailViewModel.onLoad().done(() => {
                        ko.applyBindings(self.detailViewModel, element);
                        dfd.resolve();
                    });
                })

                return dfd.promise();
            }

            /**
             * Generate item.
             */
            public generateItem(): void {
                var self = this;
                service.instance.genearetItemSetting({
                    historyId: self.history.historyId,
                    settings: self.getElementSettings()
                }).done((res: Array<ElementSettingDto>) => {
                    self.history.elements = res;
                    self.detailViewModel.refreshElementSettings(res);
                }).fail(function(error) {
                    nts.uk.ui.dialog.alert(error.message);
                });
            }

            /**
             * Get element setting dto.
             */
            public getElementSettings(): Array<model.ElementSettingDto> {
                var self = this;
                return _.map(self.elements(), (el) => {
                    var dto = <model.ElementSettingDto>{};
                    dto.type = el.elementType();
                    dto.demensionNo = el.demensionNo();
                    dto.upperLimit = el.upperLimit();
                    dto.lowerLimit = el.lowerLimit();
                    dto.interval = el.interval();
                    return dto;
                })
            }

            /**
             * Get history dto.
             */
            public getWageTableHistoryDto(): model.WageTableHistoryDto {
                var self = this;
                self.history.valueItems = self.detailViewModel.getCellItem();
                return self.history;
            }


            /**
             * Get default demension item list by default.
             */
            private getDetailViewModelByType(typeCode: number): history.base.BaseHistoryViewModel {
                // Regenerate.
                var self = this;
                switch (typeCode) {
                    case 0:
                        return new qmm016.a.history.OneDemensionViewModel(self.history);
                    case 1:
                        return new qmm016.a.history.TwoDemensionViewModel(self.history);
                    case 2:
                        return new qmm016.a.history.ThreeDemensionViewModel(self.history);
                    case 3:
                        return new qmm016.a.history.CertificateViewModel(self.history);
                    case 4:
                        return new qmm016.a.history.ThreeDemensionViewModel(self.history);
                    default:
                        return new qmm016.a.history.OneDemensionViewModel(self.history);
                }
            }

            /**
             * Unapply bindings.
             */
            unapplyBindings($node: any, remove: boolean): void {
                // unbind events
                $node.find("*").each(function() {
                    $(this).unbind();
                });

                // Remove KO subscriptions and references
                if (remove) {
                    ko.removeNode($node[0]);
                } else {
                    ko.cleanNode($node[0]);
                }
            }
        }

        /**
         * Element history setting view model.
         */
        export class HistoryElementSettingViewModel {
            demensionNo: KnockoutObservable<number>;
            elementType: KnockoutObservable<number>;
            elementCode: KnockoutObservable<string>;
            elementName: KnockoutObservable<string>;
            lowerLimit: KnockoutObservable<number>;
            upperLimit: KnockoutObservable<number>;
            interval: KnockoutObservable<number>;
            type: model.ElementTypeDto;

            /**
             * Element setting view model.
             */
            constructor(head: model.WageTableHeadDto, element: model.ElementSettingDto) {
                var self = this;
                self.demensionNo = ko.observable(element.demensionNo);
                self.elementType = ko.observable(0);
                self.elementCode = ko.observable('');
                self.elementName = ko.observable('');
                self.lowerLimit = ko.observable(0);
                self.upperLimit = ko.observable(0);
                self.interval = ko.observable(0);
                self.resetBy(head, element);
            }

            /**
             * Reset by model.
             */
            resetBy(head: model.WageTableHeadDto, element: model.ElementSettingDto) {
                var self = this;
                self.elementType(element.type);

                // Get element from head.
                var elementDto = _.filter(head.elements, (el) => {
                    return el.demensionNo == element.demensionNo;
                })[0];
                self.elementCode(elementDto.referenceCode);
                self.elementName(elementDto.demensionName);

                // Set upper and lower limit.
                self.upperLimit(element.upperLimit);
                self.lowerLimit(element.lowerLimit);
                self.interval(element.interval);

                // Set type.
                self.type = _.filter(elementTypes, (type) => {
                    return type.value == self.elementType();
                })[0];
            }
        }
    }
}