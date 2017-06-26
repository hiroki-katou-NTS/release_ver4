module nts.uk.pr.view.qpp007.j {
    import SalaryAggregateItemInDto = service.model.SalaryAggregateItemInDto;
    import SalaryAggregateItemFindDto = service.model.SalaryAggregateItemFindDto;
    import SalaryAggregateItemSaveDto = service.model.SalaryAggregateItemSaveDto;
    import SalaryItemDto = service.model.SalaryItemDto;
    import TaxDivision = nts.uk.pr.view.qpp007.c.viewmodel.TaxDivision;

    const NUMBER_OF_ITEM = 10;
    const AGGREGATE_CODE_LENGTH = 3;

    export module viewmodel {

        export class ScreenModel {
            taxDivisionTab: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            aggregateItemTab: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedDivision: KnockoutObservable<string>;
            selectedAggregateItem: KnockoutObservable<string>;
            salaryAggregateItemModel: KnockoutObservable<SalaryAggregateItemModel>;
            columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;

            constructor() {
                var self = this;
                self.taxDivisionTab = ko.observableArray<nts.uk.ui.NtsTabPanelModel>([
                    { id: TaxDivision.PAYMENT, title: '支給集計', content: '#tab-payment', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: TaxDivision.DEDUCTION, title: '控除集計', content: '#tab-deduction', enable: ko.observable(true), visible: ko.observable(true) },
                ]);
                self.aggregateItemTab = ko.observableArray<nts.uk.ui.NtsTabPanelModel>([]);
                for (let i = 1; i <= NUMBER_OF_ITEM; i++) {
                    let item: nts.uk.ui.NtsTabPanelModel = {
                        id: String('000' + i).slice(-AGGREGATE_CODE_LENGTH),
                        title: '集計項目' + i,
                        content: '#aggregate' + i,
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    };
                    self.aggregateItemTab.push(item);
                };

                self.selectedDivision = ko.observable(TaxDivision.PAYMENT);
                self.selectedAggregateItem = ko.observable('001');
                self.salaryAggregateItemModel = ko.observable(new SalaryAggregateItemModel());
                self.columns = ko.observableArray<nts.uk.ui.NtsGridListColumn>([
                    { headerText: 'コード', key: 'salaryItemCode', width: 50 },
                    { headerText: '名称', key: 'salaryItemName', width: 150 }
                ]);
                self.selectedAggregateItem.subscribe(function(selectedAggregateItem: string) {
                    self.onShowDataChange(self.selectedDivision(), selectedAggregateItem);
                });
                self.selectedDivision.subscribe(function(selectedDivision: string) {
                    self.onShowDataChange(selectedDivision, self.selectedAggregateItem());
                });
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<any> {
                var self = this;
                return self.initData();
            }

            public initData(): JQueryPromise<any> {
                var self = this;
                var salaryAggregateItemInDto: SalaryAggregateItemInDto;
                salaryAggregateItemInDto = new SalaryAggregateItemInDto();
                salaryAggregateItemInDto.taxDivision = 0;
                salaryAggregateItemInDto.aggregateItemCode = self.selectedAggregateItem();
                return self.showDataModel(salaryAggregateItemInDto);
            }

            public onShowDataChange(selectedDivision: string, selectedAggregateItem: string) {
                if (nts.uk.ui._viewModel) {
                    $('#inpDisplayName').ntsError('clear');
                }
                var self = this;
                var salaryAggregateItemInDto: SalaryAggregateItemInDto;
                salaryAggregateItemInDto = new SalaryAggregateItemInDto();
                if (selectedDivision === TaxDivision.PAYMENT) {
                    salaryAggregateItemInDto.taxDivision = 0;
                }
                else {
                    salaryAggregateItemInDto.taxDivision = 1;
                }
                salaryAggregateItemInDto.aggregateItemCode = selectedAggregateItem;
                self.showDataModel(salaryAggregateItemInDto);
            }

            public showDataModel(salaryAggregateItemInDto: SalaryAggregateItemInDto): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                service.findSalaryAggregateItem(salaryAggregateItemInDto).done(data => {
                    self.salaryAggregateItemModel().convertDtoToData(data);

                    service.findAllMasterItem().done(masterData => {
                        var dataMasterModel: SalaryItemDto[];
                        dataMasterModel = [];
                        for (var item of masterData) {
                            if (item.category == self.selectedDivision()) {
                                dataMasterModel.push(item);
                            }
                        }
                        self.salaryAggregateItemModel().setFullItemCode(dataMasterModel);
                        dfd.resolve(self);
                    });
                }).fail(function(error: any) {

                });
                return dfd.promise();
            }

            public closeDialogBtnClicked() {
                nts.uk.ui.windows.close();
            }

            private saveSalaryAggregateItem() {
                $('#inpDisplayName').ntsEditor('validate');
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                var self = this;
                if (self.selectedDivision() === TaxDivision.PAYMENT) {
                    self.convertModelToDto(0);
                }
                else {
                    self.convertModelToDto(1);
                }
                //reload///
            }

            private convertModelToDto(taxDivision: number): SalaryAggregateItemSaveDto {
                var self = this;
                var salaryAggregateItemSaveDto: SalaryAggregateItemSaveDto;
                salaryAggregateItemSaveDto = new SalaryAggregateItemSaveDto();
                salaryAggregateItemSaveDto.salaryAggregateItemCode =
                    self.salaryAggregateItemModel().salaryAggregateItemCode();
                salaryAggregateItemSaveDto.salaryAggregateItemName =
                    self.salaryAggregateItemModel().salaryAggregateItemName();
                salaryAggregateItemSaveDto.subItemCodes = [];
                for (var itemModel of self.salaryAggregateItemModel().subItemCodes()) {
                    salaryAggregateItemSaveDto.subItemCodes.push(itemModel.toDto());
                }
                salaryAggregateItemSaveDto.taxDivision = taxDivision;

                service.saveSalaryAggregateItem(salaryAggregateItemSaveDto).done(function() {
                    //reload
                }).fail(function() {
                    //reload
                });
                return salaryAggregateItemSaveDto;
            }

        }

        export class SalaryItemModel {

            salaryItemCode: string;
            salaryItemName: string;

            //convert dto find => model
            convertDtoToData(salaryItemDto: SalaryItemDto) {
                this.salaryItemCode = salaryItemDto.code;
                this.salaryItemName = salaryItemDto.name;
            }

            toDto(): SalaryItemDto {
                var dto: SalaryItemDto;
                dto = new SalaryItemDto();
                dto.code = this.salaryItemCode;
                dto.name = this.salaryItemName;
                return dto;
            }
        }
        export class SalaryAggregateItemModel {

            salaryAggregateItemCode: KnockoutObservable<string>;
            salaryAggregateItemName: KnockoutObservable<string>;
            subItemCodes: KnockoutObservableArray<SalaryItemModel>;
            fullItemCodes: KnockoutObservableArray<SalaryItemModel>;

            constructor() {
                this.salaryAggregateItemCode = ko.observable('');
                this.salaryAggregateItemName = ko.observable('');
                this.subItemCodes = ko.observableArray([]);
                this.fullItemCodes = ko.observableArray([]);
            }

            convertDtoToData(salaryAggregateItemFindDto: SalaryAggregateItemFindDto) {
                if (this.salaryAggregateItemCode) {
                    this.salaryAggregateItemCode(salaryAggregateItemFindDto.salaryAggregateItemCode);
                } else {
                    this.salaryAggregateItemCode = ko.observable(salaryAggregateItemFindDto.salaryAggregateItemCode);
                }
                if (this.salaryAggregateItemName) {
                    this.salaryAggregateItemName(salaryAggregateItemFindDto.salaryAggregateItemName);
                } else {
                    this.salaryAggregateItemName = ko.observable(salaryAggregateItemFindDto.salaryAggregateItemName);
                }
                if (salaryAggregateItemFindDto.subItemCodes) {
                    this.subItemCodes([]);
                    var subItemCodes: SalaryItemModel[];
                    subItemCodes = [];
                    for (var item of salaryAggregateItemFindDto.subItemCodes) {
                        var salaryItemModel: SalaryItemModel;
                        salaryItemModel = new SalaryItemModel();
                        salaryItemModel.convertDtoToData(item);
                        subItemCodes.push(salaryItemModel);
                    }
                    this.subItemCodes(subItemCodes);
                } else {
                    this.subItemCodes([]);
                }
            }

            setFullItemCode(fullItemCodes: SalaryItemDto[]) {
                if (fullItemCodes && fullItemCodes.length > 0) {
                    this.fullItemCodes([]);
                    var fullItemCodesModel: SalaryItemModel[];
                    fullItemCodesModel = [];
                    for (var item of fullItemCodes) {
                        var check: number;
                        check = 1;
                        for (var itemSub of this.subItemCodes()) {
                            if (itemSub.salaryItemCode == item.code) {
                                check = 0;
                                break;
                            }
                        }
                        if (check == 1) {
                            var salaryItemModel: SalaryItemModel;
                            salaryItemModel = new SalaryItemModel();
                            salaryItemModel.convertDtoToData(item);
                            fullItemCodesModel.push(salaryItemModel);
                        }
                    }
                    this.fullItemCodes(fullItemCodesModel);
                } else {
                    this.fullItemCodes([]);
                }
            }
        }
    }
}