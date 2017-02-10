// TreeGrid Node
module qpp014.h {
    export class ScreenModel {
        //DatePicker
        date_H_INP_001: KnockoutObservable<Date>;
        //GridView
        //gridview
        items_H_LST_001: KnockoutObservableArray<ItemModel_H_LST_001>;
        columns_H_LST_001: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode_H_LST_001: KnockoutObservable<any>;
        constructor() {
            let self = this;
            //DatePicker
            self.date_H_INP_001 = ko.observable(new Date('2016/12/01'));
            //gridview
            self.items_H_LST_001 = ko.observableArray([]);
            for (let i = 1; i < 100; i++) {
                self.items_H_LST_001.push(new ItemModel_H_LST_001('00' + i, '基本給', "description " + i, "other" + i));
            }
            self.columns_H_LST_001 = ko.observableArray([
                { headerText: '�R�[�h', prop: 'code', width: 100 },
                { headerText: '����', prop: 'name', width: 150 },
                { headerText: '����', prop: 'description', width: 200 }
            ]);
            self.currentCode_H_LST_001 = ko.observable(); 
        }
    }
        export class ItemModel_H_LST_001 {
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
