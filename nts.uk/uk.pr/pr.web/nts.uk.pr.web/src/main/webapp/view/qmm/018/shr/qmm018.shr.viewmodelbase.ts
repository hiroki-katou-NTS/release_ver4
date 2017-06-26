module qmm018.shr.viewmodelbase {
    export class ItemModel {
        itemCode: string;
        itemAbName: string;
        constructor(itemCode: string, itemAbName: string) {
            this.itemCode = itemCode;
            this.itemAbName = itemAbName;
        }
    }

    export enum CategoryAtr {
        PAYMENT = 0,
        DEDUCTION = 1,
        PERSONAL_TIME = 2,
        ARTICLES = 3,
        OTHER = 9
    }

    export enum Error {
        ER001 = <any>"が入力されていません。",
        ER007 = <any>"が選択されていません。",
        ER010 = <any>"対象データがありません。",
    }
}