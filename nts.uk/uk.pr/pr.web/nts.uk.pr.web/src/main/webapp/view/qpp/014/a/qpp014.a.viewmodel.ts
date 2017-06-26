// TreeGrid Node
module qpp014.a.viewmodel {
    export class ScreenModel {
        a_SEL_001_items: KnockoutObservableArray<PayDayProcessing>;
        a_SEL_001_itemSelected: KnockoutObservable<number>;

        constructor() {
            var self = this;
            self.a_SEL_001_items = ko.observableArray([]);
            self.a_SEL_001_itemSelected = ko.observable(0);
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.findAll().done(function() {
                dfd.resolve();
            });
            return dfd.promise();
        }
   
        /**
         * get data from table PAYDAY_PROCESSING
         */
        findAll(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            //get data with DISP_SET = 1
            qpp014.a.service.findAll()
                .done(function(data) {
                    if (data.length > 0) {
                        _.forEach(data, function(x) {
                            self.a_SEL_001_items().push(new PayDayProcessing(x.processingNo, x.processingName, x.dispSet, x.currentProcessingYm,
                                x.bonusAtr, x.bcurrentProcessingYm));
                        });
                        self.a_SEL_001_items(_.sortBy(self.a_SEL_001_items(), 'currentProcessingYm'));
                        self.a_SEL_001_itemSelected(self.a_SEL_001_items()[0].processingNo);
                    } else {
                        nts.uk.ui.dialog.alert("対象データがありません。");//ER010
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        /**
         * transfer data to screen D, go to next screen
         */
        nextScreen(): void {
            var self = this;
            var data = _.find(self.a_SEL_001_items(), function(x) {
                return x.processingNo === self.a_SEL_001_itemSelected();
            });
            nts.uk.request.jump("/view/qpp/014/b/index.xhtml", data);
        }
    }

    export class PayDayProcessing {
        processingNo: number;
        processingName: string;
        displaySet: number;
        currentProcessingYm: number;
        bonusAtr: number;
        bcurrentProcessingYm: number;
        label: string;

        constructor(processingNo: number, processingName: string,
            displaySet: number, currentProcessingYm: number, bonusAtr: number, bcurrentProcessingYm: number) {
            this.processingNo = processingNo;
            this.processingName = processingName;
            this.displaySet = displaySet;
            this.currentProcessingYm = currentProcessingYm;
            this.bonusAtr = bonusAtr;
            this.bcurrentProcessingYm = bcurrentProcessingYm;
            this.label = this.processingNo + ' : ' + this.processingName;
        }
    }
};
