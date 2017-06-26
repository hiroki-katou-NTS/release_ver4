module qet001.a.viewmodel {
    export class ScreenModel {
        targetYear: KnockoutObservable<number>;
        isAggreatePreliminaryMonth: KnockoutObservable<boolean>;
        layoutSelected: KnockoutObservable<LayoutOutput>;
        isPageBreakIndicator: KnockoutObservable<boolean>;
        outputTypeSelected : KnockoutObservable<OutputType>;
        outputSettings: KnockoutObservableArray<service.model.WageLedgerOutputSetting>;
        outputSettingSelectedCode: KnockoutObservable<string>;
        japanTargetYear: KnockoutComputed<string>;
        
        constructor() {
            this.targetYear = ko.observable(2016);
            this.isAggreatePreliminaryMonth = ko.observable(false);
            this.layoutSelected = ko.observable(LayoutOutput.ONE_PAGE);
            this.isPageBreakIndicator = ko.observable(false);
            this.outputTypeSelected = ko.observable(OutputType.MASTER_ITEMS);
            this.outputSettings = ko.observableArray([]);
            this.outputSettingSelectedCode = ko.observable('');
            var self = this;
            self.japanTargetYear = ko.computed(function() {
                return nts.uk.time.yearInJapanEmpire(self.targetYear().toString()).toString();
            })
        }
        
        public start(): JQueryPromise<any>{
            var dfd = $.Deferred<any>();
            var self = this;
            $.when(self.loadAllOutputSetting()).done(function() {
                dfd.resolve();
            })
            return dfd.promise();
        }
        
        /**
         * Load all output setting.
         */
        public loadAllOutputSetting(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            service.findOutputSettings().done(function(data: service.model.WageLedgerOutputSetting[]) {
                self.outputSettings(data);
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            })
            return dfd.promise();
        }
        
        /**
         * Go To Output setting dialog.
         */
        public goToOutputSetting() {
            nts.uk.ui.windows.setShared('selectedCode', this.outputSettingSelectedCode(), true);
            nts.uk.ui.windows.setShared('outputSettings', this.outputSettings(), true);
            var self = this;
            nts.uk.ui.windows.sub.modal("/view/qet/001/b/index.xhtml", { title: "出カ項目の設定",  dialogClass: 'no-close' }).onClosed(function() {
                if (nts.uk.ui.windows.getShared('isHasUpdate')) {
                    // Reload output setting list.
                    self.loadAllOutputSetting();
                }
            });
        }
        
        /**
         * Open Aggregate item dialog.
         */
        public goToAggregateItemPage() {
            nts.uk.ui.windows.sub.modal("/view/qet/001/i/index.xhtml", { title: "明細書項目の集約設定", dialogClass: 'no-close' })
        }
        
        /**
         * Print report.
         */
        public print() {
            // Clear error.
            var self = this;
            $('#target-year-input').ntsError('clear');
            $('#output-settings-selection').ntsError('clear');
            $('#target-year-input').ntsEditor('validate');
            if (self.outputTypeSelected() == OutputType.OUTPUT_SETTING_ITEMS
                && (!self.outputSettingSelectedCode() || self.outputSettingSelectedCode() == '')) {
                $('#output-settings-selection').ntsError('set', '集約項目を出力するが入力されていません。')
            }
            // TODO: Check employee list.
            if ($('.nts-input').ntsError('hasError') || $('#output-settings-selection').ntsError('hasError')) {
                return;
            }
            // Print.
            service.printReport(self).done(function() {}).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
            })
        }
    }
    
    /**
     * 登録済みの出力項目設定
     */
    export class OutputSetting {
        code: string;
        name: string;
        
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    
    /**
     * 出力するレイアウト.
     */
    export class LayoutOutput {
        /**
         * 賃金台帳（A4横1ページ）を出力する
         */
        static ONE_PAGE = 'OnePage';
        
        /**
         * 賃金一覧表を出力する => print with new layout.
         */
        static NEW_LAYOUT = 'NewLayout';
    }
    
    /**
     * 出力する項目の選択
     */
    export class OutputType {
        /**
         * 明細書項目を出力する
         */
        static MASTER_ITEMS = 'MasterItems';
        /**
         * 明細書の集約項目を出力する
         */
        static OUTPUT_SETTING_ITEMS = 'OutputSettingItems';
    }
}