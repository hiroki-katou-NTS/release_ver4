module nts.uk.com.view.qmm011.share.model {
    import getText = nts.uk.resource.getText;
	
    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

}