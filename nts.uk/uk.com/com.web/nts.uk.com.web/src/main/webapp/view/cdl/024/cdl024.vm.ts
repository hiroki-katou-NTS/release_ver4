module nts.uk.at.view.cdl024.viewmodel {
    export class ScreenModel {
        columns: Array<Object>;
        items: Array<IItemModel>;
        currentCodeList: Array<String>;

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
            this.currentCodeList = _.map(selectedItems, 'id');
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