// TreeGrid Node
module qpp014.i {
    export class ScreenModel {
        //E_LST_003
        items_I_LST_003: KnockoutObservableArray<ItemModel_I_LST_003>;
        columns_I_LST_003: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode_I_LST_003: KnockoutObservable<any>;
        timer: nts.uk.ui.sharedvm.KibanTimer;
        transferBank: any;
        processingDateInJapanEmprire: any;
        constructor() {
            var self = this;
            $('#successful').css('display', 'none');
            //$('#stop').css('display', 'none');
            $('#error').css('display', 'none');
            //nts.uk.ui.windows.getSelf().setHeight(570);
            self.timer = new nts.uk.ui.sharedvm.KibanTimer('timer');
            self.items_I_LST_003 = ko.observableArray([]);
            for (let i = 1; i < 14; i++) {
                self.items_I_LST_003.push(new ItemModel_I_LST_003('00' + i, '基本給', "description " + i));
            }
            self.currentCode_I_LST_003 = ko.observable();
            self.processingDateInJapanEmprire = ko.observable(nts.uk.ui.windows.getShared("processingDateInJapanEmprire"));
            self.transferBank = ko.observable(nts.uk.ui.windows.getShared("label"));
        }
  
        /**
         * close dialog
         */
        closeDialog(): void {
            nts.uk.ui.windows.close();
        }

        /**
         * stop Processing
         */
        stopProcessing(): void {
            var self = this;
            self.timer.end();
            nts.uk.ui.windows.setShared("closeDialog", false, true);
            $('#successful').css('display', 'none');
            $('#stop').css('display', 'none');
            $('#error').css('display', '');
            nts.uk.ui.windows.getSelf().setHeight(570);
        }
    }
    export class ItemModel_I_LST_003 {
        code: string;
        name: string;
        description: string;

        constructor(code: string, name: string, description: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }

};
