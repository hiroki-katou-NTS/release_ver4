module nts.uk.at.view.cdl024.viewmodel {
    export class ScreenModel {
        columns: Array<Object>;
        items: Array<IItemModel>;
        currentCodeList: Array<String>;
        selectMultiple: KnockoutObservable<boolean> = ko.observable(true);

        constructor() {
            let self = this;
            this.columns = [
                { headerText: 'コード', prop: 'code', width: 100 },
                { headerText: '名称', prop: 'name', width: 258, formatter: _.escape }
            ]; 
        }

        closeDialog() {
            nts.uk.ui.windows.close();
        }

        sendAttribute() {
            let selectedItems = $("#multi-list").ntsGridList("getSelected");
            if (this.selectMultiple()) {
                this.currentCodeList = _.map(selectedItems, 'id');
            }else{
                this.currentCodeList = selectedItems.id;
            }
            nts.uk.ui.windows.setShared("currentCodeList", this.currentCodeList);
            nts.uk.ui.windows.close();
        }

        startPage(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();

            service.getAll().done((data: Array<IItemModel>) => {
                data = _.orderBy(data, ["code"], ['asc']);
                self.items = data;
                let parameter: InputParam = nts.uk.ui.windows.getShared("CDL024");
                if(parameter != null && parameter.selectMultiple != null && parameter.selectMultiple != undefined){
                    self.selectMultiple(parameter.selectMultiple);
                }
                if (parameter != null && parameter.codeList != null) {
                    self.currentCodeList = parameter.codeList;
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