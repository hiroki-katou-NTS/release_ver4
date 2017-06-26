module qmm020.i.viewmodel {
    export class ScreenModel {
        paymentDateProcessingList: KnockoutObservableArray<any>;
        selectedPaymentDate: KnockoutObservable<any>;
        //Grid List 
        items: KnockoutObservableArray<ItemModel>;
        columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        count: number = 10;
        constructor() {
            var self = this;
            self.paymentDateProcessingList = ko.observableArray([]);
            self.selectedPaymentDate = ko.observable(null);
            self.items = ko.observableArray([]);

            for (let i = 1; i < 10; i++) {
                this.items.push(new ItemModel('00' + i, '基本給', "description " + i, i % 3 === 0, "other 1 " + i, "other2 " + i));
            }

            this.columns2 = ko.observableArray([
                { headerText: 'コード', key: 'code', width: 100 },
                { headerText: '名称', key: 'name', width: 150 },
                { headerText: '給与明細書', key: 'description', width: 150 },
                { headerText: '賞与明細書', key: 'other1', width: 150 },
                { headerText: '適用されている設定', key: 'other2', width: 150 },
                // { headerText: 'test template', key: 'template', width: 150, columnCssClass: "test"}
            ]);

            //Delegate 
//            $(document).delegate("#single-list", "iggriddatarendered", function(evt, ui) {
//
//                $('.error').parent().css({ 'background-color': 'rgba(253, 159, 159, 0.44)' });
//
//            })
            //close dialog

            this.currentCode = ko.observable();
            this.currentCodeList = ko.observableArray([]);

        }

        startPage(): JQueryPromise<any> {
            var self = this;

            var dfd = $.Deferred();
            service.getPaymentDateProcessingList().done(function(data) {
                self.paymentDateProcessingList(data);
                dfd.resolve();
            }).fail(function(res) {

            });
            return dfd.promise();
        }

    }
}
class ItemModel {
    code: string;
    name: string;
    description: string;
    other1: string;
    other2: string;
    deletable: boolean;
    //  template: string;  
    constructor(code: string, name: string, description: string, deletable: boolean, other1?: string, other2?: string) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.other1 = other1;
        this.other2 = other2 || other1;
        this.deletable = deletable;



        if (deletable) {
            this.other1 = "<div class ='error'>" + other1 + "</div>";
            this.other2 = "<div class ='error'>" + other2 + "</div>";
            this.description = "<div class ='error'>" + description + "</div>";
        } else {
            this.other1 = "<div class ='no-error'>" + other1 + "</div>";
            this.other2 = "<div class ='no-error'>" + other2 + "</div>";
            this.description = "<div class ='no-error'>" + description + "</div>";
        }
    }
}