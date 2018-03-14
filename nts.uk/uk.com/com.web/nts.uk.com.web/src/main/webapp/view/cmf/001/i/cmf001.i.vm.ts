module nts.uk.com.view.cmf001.i.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import model = cmf001.share.model;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        inputMode: boolean = true;
        lineNumber: number = -1;

        setting: KnockoutObservable<model.DateDataFormatSetting> = ko.observable(null);

        itemsFormatSelection: KnockoutObservableArray<model.ItemModel> = ko.observableArray([]);

        itemsFixedValue: KnockoutObservableArray<model.ItemModel> = ko.observableArray([]);

        atrUse: number = model.NOT_USE_ATR.USE;
        atrNotUse: number = model.NOT_USE_ATR.NOT_USE;

        constructor() {
            let self = this;

            self.itemsFormatSelection([
                new model.ItemModel(0, getText('CMF001_303')),
                new model.ItemModel(1, getText('CMF001_304')),
                new model.ItemModel(2, getText('CMF001_305')),
                new model.ItemModel(3, getText('CMF001_306')),
                new model.ItemModel(4, getText('CMF001_307')),
                new model.ItemModel(5, getText('CMF001_308'))
            ]);

            self.itemsFixedValue([
                new model.ItemModel(self.atrUse, getText('CMF001_322')),
                new model.ItemModel(self.atrNotUse, getText('CMF001_323'))
            ]);

            let params = getShared("CMF001iParams");
            self.inputMode = params.inputMode;
            self.lineNumber = params.lineNumber;
            if (params.formatSetting) {
                self.setting(new model.DateDataFormatSetting(
                    params.formatSetting.formatSelection,
                    params.formatSetting.fixedValue,
                    params.formatSetting.valueOfFixedValue));
            }
            else {
                self.setting(new model.DateDataFormatSetting(1, 0, ""));
            }
        }

        private checkActive2(): boolean {
            let self = this;
            if (self.setting().fixedValue() == self.atrUse) {
                return true;
            }
            return false;
        }

        private checkActive3(): boolean {
            let self = this;
            if (self.setting().fixedValue() == self.atrUse) {
                return false;
            }
            return true;
        }

        private checkRequired1(): boolean {
            let self = this;
            if (self.setting().fixedValue() == self.atrUse) {
                return true;
            }
            return false;
        }

        private saveSetting(): void {
            let self = this;
            if (self.inputMode) {
                setShared("CMF001FormatOutput", { lineNumber: self.lineNumber, formatSetting: ko.toJS(self.setting) });
            }
            nts.uk.ui.windows.close();
        }
        private cancelSetting(): void {
            nts.uk.ui.windows.close();
        }
    }
}