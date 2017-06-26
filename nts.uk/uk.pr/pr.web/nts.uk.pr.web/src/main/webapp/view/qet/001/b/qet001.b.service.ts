module qet001.b {
    export module service {
        import WageLedgerOutputSetting = qet001.a.service.model.WageLedgerOutputSetting;

        // Service paths.
        var servicePath = {
            findOutputSettingDetail: 'ctx/pr/report/wageledger/outputsetting/find',
            findAggregateItems: 'ctx/pr/report/wageledger/aggregateitem/findAll',
            findMasterItems: 'ctx/pr/report/masteritem/findAll',
            saveOutputSetting: 'ctx/pr/report/wageledger/outputsetting/save',
            removeOutputSetting: 'ctx/pr/report/wageledger/outputsetting/remove',
        }
        
        /**
         * Find output setting detail.
         */
        export function findOutputSettingDetail(settingCode: string): JQueryPromise<WageLedgerOutputSetting> {
            return nts.uk.request.ajax(servicePath.findOutputSettingDetail + '/' + settingCode);
        }
        
        /**
         * Find all Aggregate items.
         */
        export function findAggregateItems() : JQueryPromise<Item[]> {
            return nts.uk.request.ajax(servicePath.findAggregateItems);
        }
        
        /**
         * Save Output setting to DB.
         */
        export function saveOutputSetting(settingDetail: viewmodel.OutputSettingDetail) : JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var categorySettingData = [];
            // Set order number to item list.
            settingDetail.categorySettings().forEach(setting => {
                for (var i = 0; i < setting.outputItems().length; i++) {
                    setting.outputItems()[i].orderNumber = i;
                }
            })
            var data = {
                code: settingDetail.settingCode(),
                name: settingDetail.settingName(),
                onceSheetPerPerson: settingDetail.isPrintOnePageEachPer(),
                categorySettings: ko.toJS(settingDetail.categorySettings()),
                createMode: settingDetail.isCreateMode()
            };
            nts.uk.request.ajax(servicePath.saveOutputSetting, data).done(function(){
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        /**
         * Remove Output setting to DB.
         */
        export function removeOutputSetting(code: string) : JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            nts.uk.request.ajax(servicePath.removeOutputSetting, {code: code}).done(function(){
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise()
        }
        
        /**
         * Find master items.
         */
        export function findMasterItems() : JQueryPromise<Item[]> {
            var dfd = $.Deferred<Item[]>();
            return nts.uk.request.ajax(servicePath.findMasterItems);
        }
        
        /**
         * Aggregate item class.
         */
        export interface Item {
            code: string;
            name: string;
            paymentType?: string;
            category: string;
            orderNumber? : number;
            showNameZeroValue?: boolean;
            showValueZeroValue?: boolean;
        }
    }
}
