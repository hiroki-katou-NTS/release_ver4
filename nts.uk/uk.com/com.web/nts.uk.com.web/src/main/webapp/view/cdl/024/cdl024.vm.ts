module nts.uk.at.view.cdl024.viewmodel {
    export class ScreenModel {
        columns: KnockoutObservableArray<any>;
        items: KnockoutObservableArray<IItemModel> = ko.observableArray([]);
        currentCodeList: KnockoutObservableArray<string> = ko.observableArray([]);
        selectMultiple: KnockoutObservable<boolean> = ko.observable(true);

        constructor() {
            let self = this;
            this.columns = ko.observableArray([
                { headerText: 'コード', prop: 'code', width: 100 },
                { headerText: '名称', prop: 'name', width: 230, formatter: _.escape }
            ]); 
        }

        closeDialog() {
            nts.uk.ui.windows.close();
        }

        sendAttribute() {
            nts.uk.ui.windows.setShared("currentCodeList", this.currentCodeList());
            nts.uk.ui.windows.close();
        }

        startPage(): JQueryPromise<any> {
            let self = this,
                items = self.items,
                dfd = $.Deferred();

            items.removeAll();
            service.getAll().done((data: Array<IItemModel>) => {
                data = _.orderBy(data, ["code"], ['asc']);
                items(data);
                let parameter: InputParam = nts.uk.ui.windows.getShared("CDL024");
                if(parameter != null && parameter.selectMultiple != null && parameter.selectMultiple != undefined){
                    self.selectMultiple(parameter.selectMultiple);
                }
                if (parameter != null && parameter.codeList != null) {
                    self.currentCodeList(parameter.codeList);
                }
                dfd.resolve();
            });


            return dfd.promise();
        }
    }

    export interface IItemModel {
        code: string;
        name: string;
    }
    
    export interface InputParam {
        codeList: Array<string>;
    }
}