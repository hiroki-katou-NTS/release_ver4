module nts.uk.at.view.kmk013.g {
    export module viewmodel {
        export class ScreenModel {
            flexSettingOpt: KnockoutObservableArray<ItemModel>;
            aggWorkSetOpt: KnockoutObservableArray<ItemModel>;
            tempWorkOpt: KnockoutObservableArray<ItemModel>;
            multipleWorkOpt: KnockoutObservableArray<ItemModel>;
            
            flexSetting: KnockoutObservable<number>;
            aggWorkSet: KnockoutObservable<number>;
            tempWorkSet: KnockoutObservable<number>;
            multipleWorkSet: KnockoutObservable<number>;
            
            constructor() {
                var self = this;
                self.flexSettingOpt = ko.observableArray<ItemModel>([
                    new ItemModel(0, nts.uk.resource.getText("KMK013_209")),
                    new ItemModel(1, nts.uk.resource.getText("KMK013_210"))
                ]);
                
                self.aggWorkSetOpt = ko.observableArray<ItemModel>([
                    new ItemModel(0, nts.uk.resource.getText("KMK013_209")),
                    new ItemModel(1, nts.uk.resource.getText("KMK013_210"))
                ]);
                self.tempWorkOpt = ko.observableArray<ItemModel>([
                    new ItemModel(0, nts.uk.resource.getText("KMK013_209")),
                    new ItemModel(1, nts.uk.resource.getText("KMK013_210"))
                ]);
                self.multipleWorkOpt = ko.observableArray<ItemModel>([
                    new ItemModel(0, nts.uk.resource.getText("KMK013_209")),
                    new ItemModel(1, nts.uk.resource.getText("KMK013_210"))
                ]);
                
                
                // Set default setting to Not use
                self.flexSetting = ko.observable(1);
                self.aggWorkSet = ko.observable(1);
                self.tempWorkSet = ko.observable(1);
                self.multipleWorkSet = ko.observable(1);
            }
            // Start Page
            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                service.loadAllSetting().done(function(data) {
                    self.flexSetting(data.flexWorkManagement);
                    self.aggWorkSet(data.useAggDeformedSetting);
                    self.tempWorkSet(data.useTempWorkUse);
                    self.multipleWorkSet(data.useWorkManagementMultiple);
                    dfd.resolve();
                })
                .fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                return dfd.promise();
            }
            
            saveData(): void {
                let dataRegAgg = {};
                let dataRegFlexWork = {};
                let dataRegTempWork = {};
                let dataRegWorkMulti = {};
                let self = this;
                
                // Packing data
                dataRegFlexWork.managingFlexWork = self.flexSetting();
                dataRegAgg.useDeformedSetting = self.aggWorkSet();
                dataRegTempWork.useClassification = self.tempWorkSet();
                dataRegWorkMulti.useAtr = self.multipleWorkSet();
                
                // Register to DB
                service.regAgg(dataRegAgg).done(function() {});
                service.regFlexWorkSet(dataRegFlexWork).done(function() {});
                service.regTempWork(dataRegTempWork).done(function() {});
                service.regWorkMulti(dataRegWorkMulti).done(function() {});
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
            }
            
            
        }
        
        // Class ItemModel
        class ItemModel {
            id: number;
            name: string;
            constructor(id: number, name: string) {
                this.id = id;
                this.name = name;
            }
        }
    }
}