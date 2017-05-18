module qmm019.f.deductItem.viewmodel {
    export class ScreenModel {
        itemMasterDto: KnockoutObservable<qmm019.f.service.model.ItemMasterDto> = ko.observable(null);
        itemDtoSelected: KnockoutObservable<qmm019.f.viewmodel.ItemDto> = ko.observable(null);
        checkUseHighError: KnockoutObservable<boolean> = ko.observable(false);
        checkUseLowError: KnockoutObservable<boolean> = ko.observable(false);
        checkUseHighAlam: KnockoutObservable<boolean> = ko.observable(false);
        checkUseLowAlam: KnockoutObservable<boolean> = ko.observable(false);
        wageCode: KnockoutObservable<string> = ko.observable('');
        wageName: KnockoutObservable<string> = ko.observable('');
        comboBoxCalcMethod: KnockoutObservable<qmm019.f.viewmodel.ComboBox>;
        comboBoxSumScopeAtr: KnockoutObservable<qmm019.f.viewmodel.ComboBox>;
        switchButton: KnockoutObservable<qmm019.f.viewmodel.SwitchButton>;
        distributeWayselected: KnockoutObservable<number> = ko.observable(0);
        itemListDistributeWay: KnockoutObservableArray<any>;
        constructor() {
            let self = this;
            var itemListSumScopeAtr = ko.observableArray([
                new qmm019.f.viewmodel.ItemModel(0, '合計対象外'),
                new qmm019.f.viewmodel.ItemModel(1, '合計対象内')
            ]);

            var itemListCalcMethod = ko.observableArray([
                new qmm019.f.viewmodel.ItemModel(0, '手入力'),
                new qmm019.f.viewmodel.ItemModel(1, '個人情報'),
                new qmm019.f.viewmodel.ItemModel(2, '計算式'),
                new qmm019.f.viewmodel.ItemModel(3, '賃金テーブル'),
                new qmm019.f.viewmodel.ItemModel(4, '共通金額'),
                new qmm019.f.viewmodel.ItemModel(5, '支給相殺 '),
                new qmm019.f.viewmodel.ItemModel(9, 'システム計算')
            ]);
            self.itemListDistributeWay = ko.observableArray([
                new qmm019.f.viewmodel.ItemModel(0, '割合で計算'),
                new qmm019.f.viewmodel.ItemModel(1, '日数控除'),
                new qmm019.f.viewmodel.ItemModel(2, '計算式')
            ]);
            //計算方法
            //6 支給相殺は項目区分が「控除項目」の場合のみ表示する。
            self.comboBoxCalcMethod = ko.observable(new qmm019.f.viewmodel.ComboBox(itemListCalcMethod));
            //内訳区分
            self.comboBoxSumScopeAtr = ko.observable(new qmm019.f.viewmodel.ComboBox(itemListSumScopeAtr));
            self.switchButton = ko.observable(new qmm019.f.viewmodel.SwitchButton());
            self.itemMasterDto.subscribe(function(NewItem) {
                self.loadLayoutData(NewItem).done(function(itemDto: qmm019.f.viewmodel.ItemDto) {
                    self.setItemDtoSelected(itemDto);
                });
            });
            self.itemDtoSelected.subscribe(function(NewItem) {
                self.checkUseHighError(NewItem.checkUseHighError());
                self.checkUseLowError(NewItem.checkUseLowError());
                self.checkUseHighAlam(NewItem.checkUseHighAlam());
                self.checkUseLowAlam(NewItem.checkUseLowAlam());
                self.distributeWayselected(NewItem.distributeWay());
                self.switchButton().selectedRuleCode(NewItem.distributeSet())
            });
        }
        loadLayoutData(itemMaster: qmm019.f.service.model.ItemMasterDto): JQueryPromise<qmm019.f.viewmodel.ItemDto> {
            let self = this;
            let dfd = $.Deferred<qmm019.f.viewmodel.ItemDto>();
            service.getDeductItem(itemMaster.itemCode).done(function(itemDeduct: service.model.DeductItemModel) {
                dfd.resolve(self.createNewItemDto(itemMaster, itemDeduct));
            });
            return dfd.promise();
        }
        createNewItemDto(itemMaster: qmm019.f.service.model.ItemMasterDto, itemDeduct: service.model.DeductItemModel): qmm019.f.viewmodel.ItemDto {
            let ItemDto;
            let self = this;
            if (itemDeduct) {
                ItemDto = new qmm019.f.viewmodel.ItemDto(itemMaster.itemCode, itemMaster.categoryAtr, itemMaster.itemAbName, itemDeduct.errRangeHighAtr == 1, itemDeduct.errRangeHigh, itemDeduct.errRangeLowAtr == 1, itemDeduct.errRangeLow,
                    itemDeduct.alRangeHighAtr == 1, itemDeduct.alRangeHigh, itemDeduct.alRangeLowAtr == 1, itemDeduct.alRangeLow, 0, 0, 0, 0, '00', 0, itemDeduct.deductAtr);
            } else {
                ItemDto = self.createDefaltItemDto(itemMaster);
            }
            return ItemDto;
        }
        createDefaltItemDto(itemMaster: qmm019.f.service.model.ItemMasterDto): qmm019.f.viewmodel.ItemDto {
            return new qmm019.f.viewmodel.ItemDto(itemMaster.itemCode, itemMaster.categoryAtr, itemMaster.itemAbName, false, 0, false, 0,
                false, 0, false, 0, 0, 0, 0, 0, '00', 0, 0);
        }
        setItemDtoSelected(itemDto: qmm019.f.viewmodel.ItemDto, itemMasterDto?) {
            let self = this;
            self.itemDtoSelected(itemDto);
        }
        openHDialog() {
            var self = this;
            nts.uk.ui.windows.setShared('categoryAtr', self.itemDtoSelected().categoryAtr);
            nts.uk.ui.windows.sub.modal('/view/qmm/019/h/index.xhtml', { title: '明細レイアウトの作成＞個人金額コードの選択', dialogClass: 'no-close' }).onClosed(() => {
                self.wageCode(nts.uk.ui.windows.getShared('selectedCode'));
                self.wageName(nts.uk.ui.windows.getShared('selectedName'));
                return this;
            });
        }
        checkPersonalInformationReference(): boolean {
            var self = this;
            if (self.comboBoxCalcMethod().selectedCode() == 1) {
                nts.uk.ui.windows.getSelf().setHeight(670);
                return true;
            } else {
                nts.uk.ui.windows.getSelf().setHeight(620);
                return false;
            }
        }
        getItemDto(): qmm019.f.viewmodel.ItemDto {
            let self = this;
            let ItemDto = new qmm019.f.viewmodel.ItemDto(self.itemDtoSelected().itemCode(), self.itemDtoSelected().categoryAtr(), self.itemDtoSelected().itemAbName(), self.checkUseHighError(), self.itemDtoSelected().errRangeHigh(), self.checkUseLowError(), self.itemDtoSelected().errRangeLow(),
                self.checkUseHighAlam(), self.itemDtoSelected().alamRangeHigh(), self.checkUseLowAlam(), self.itemDtoSelected().alamRangeLow(), 0, self.comboBoxCalcMethod().selectedCode(), self.switchButton().selectedRuleCode(), self.distributeWayselected(), '00', self.comboBoxSumScopeAtr().selectedCode(), self.itemDtoSelected().taxAtr());
            return ItemDto;
        }
        setMasterDto(itemMasterDto) {
            let self = this;
            self.itemMasterDto(itemMasterDto);
        }
    }

}