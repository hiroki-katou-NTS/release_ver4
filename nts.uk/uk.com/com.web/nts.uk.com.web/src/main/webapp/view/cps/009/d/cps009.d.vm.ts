module nts.uk.com.view.cps009.d.viewmodel {
    import error = nts.uk.ui.errors;
    import text = nts.uk.resource.getText;
    import close = nts.uk.ui.windows.close;
    import dialog = nts.uk.ui.dialog;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import block = nts.uk.ui.block;

    export class ViewModel {

        currentInitVal: KnockoutObservable<ItemInitValue> = ko.observable(new ItemInitValue(
            {
                itemCode: "",
                itemName: ""
            }));



        constructor() {


        }

        newInitValue() {

            let self = this,
                copyObj = {
                    itemCode: self.currentInitVal().itemCode(),
                    itemName: self.currentInitVal().itemName()
                };
            service.add(copyObj).done(function(data) {
                dialog.info({ messageId: "Msg_20" }).then(function() {
                    close();
                });
            });
        }

        cancelNewInitValue() {

            close();
        }




    }

    export interface IItemInitValue {

        itemCode: string;

        itemName: string;

    }

    export class ItemInitValue {

        itemCode: KnockoutObservable<string>;

        itemName: KnockoutObservable<string>;

        constructor(params: IItemInitValue) {

            let self = this;

            self.itemCode = ko.observable(params.itemCode || "");

            self.itemName = ko.observable(params.itemName || "");

        }

        setData(params: any) {

            let self = this;

            self.itemCode(params.itemCode || "");

            self.itemName(params.itemName || "");
        }

    }
}