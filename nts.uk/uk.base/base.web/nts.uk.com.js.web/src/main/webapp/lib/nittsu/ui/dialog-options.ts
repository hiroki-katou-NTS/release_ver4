/// <reference path="../reference.ts"/>

module nts.uk.ui.option {

    export abstract class DialogOption {
        modal: boolean;
        show: boolean = false;
        buttons: DialogButton[];
    }

    // Normal Dialog
    export interface IDialogOption {
        modal?: boolean
    }

    export class ConfirmDialogOption extends DialogOption {
        constructor(option?: IDialogOption) {
            super();
            // Default value
            this.modal = (option && option.modal !== undefined) ? option.modal : true;
            this.buttons = [];
            // Add OK Button
            this.buttons.push({
                text: "OK",
                "class": "yes",
                size: "large",
                color: "proceed",
                click: function(viewmodel, ui): void {
                    viewmodel.okButtonClicked();
                    $(ui).dialog("close");
                }
            });
        }
    }

    export class DelDialogOption extends DialogOption {
        constructor(option?: IDialogOption) {
            super();
            // Default value
            this.modal = (option && option.modal !== undefined) ? option.modal : true;
            this.buttons = [];
            // Add OK Button
            this.buttons.push({
                text: "はい",
                "class": "yes ",
                size: "large",
                color: "danger",
                click: function(viewmodel, ui) {
                    viewmodel.okButtonClicked();
                    ui.dialog("close");
                }
            });
            // Add Cancel Button
            this.buttons.push({
                text: "いいえ",
                "class": "no ",
                size: "large",
                color: "",
                click: function(viewmodel, ui) {
                    viewmodel.cancelButtonClicked();
                    ui.dialog("close");
                }
            });
        }
    }

    export class OKDialogOption extends DialogOption {
        constructor(option?: IDialogOption) {
            super();
            // Default value
            this.modal = (option && option.modal !== undefined) ? option.modal : true;
            this.buttons = [];
            // Add OK Button
            this.buttons.push({
                text: "はい",
                "class": "yes ",
                size: "large",
                color: "proceed",
                click: function(viewmodel, ui) {
                    viewmodel.okButtonClicked();
                    ui.dialog("close");
                }
            });
            // Add Cancel Button
            this.buttons.push({
                text: "いいえ",
                "class": "no ",
                size: "large",
                color: "",
                click: function(viewmodel, ui) {
                    viewmodel.cancelButtonClicked();
                    ui.dialog("close");
                }
            });
        }
    }

    // Error Dialog
    export interface IErrorDialogOption {
        headers?: errors.ErrorHeader[],
        modal?: boolean,
        displayrows?: number,
        //maxrows?: number,
        autoclose?: boolean
    }

    export class ErrorDialogOption extends DialogOption {
        headers: errors.ErrorHeader[];
        displayrows: number;
        //maxrows: number;
        autoclose: boolean;

        constructor(option?: IErrorDialogOption) {
            super();
            // Default value
            this.headers = (option && option.headers) ? option.headers : [
                
                 new nts.uk.ui.errors.ErrorHeader("messageText", "エラー内容", "auto", true),
                new nts.uk.ui.errors.ErrorHeader("message", "エラーコード", 150, true)


            ];
            this.modal = (option && option.modal !== undefined) ? option.modal : false;
            this.displayrows = (option && option.displayrows) ? option.displayrows : 10;
            //this.maxrows = (option && option.maxrows) ? option.maxrows : 1000;
            this.autoclose = (option && option.autoclose !== undefined) ? option.autoclose : true;

            this.buttons = [];
            // Add Close Button
            this.buttons.push({
                text: "閉じる",
                "class": "yes ",
                size: "large",
                color: "",
                click: function(viewmodel, ui) {
                    viewmodel.closeButtonClicked();
                    ui.dialog("close");
                }
            });
        }
    }

    export class ErrorDialogWithTabOption extends ErrorDialogOption {

        constructor(option?: IErrorDialogOption) {
            super();
            // Default value
            this.headers = (option && option.headers) ? option.headers : [
                new errors.ErrorHeader("tab", "タブ", 90, true),
                new errors.ErrorHeader("location", "エラー箇所", 115, true),
                new errors.ErrorHeader("message", "エラー詳細", 250, true)
            ];
            this.modal = (option && option.modal !== undefined) ? option.modal : false;
            this.displayrows = (option && option.displayrows) ? option.displayrows : 10;
            //this.maxrows = (option && option.maxrows) ? option.maxrows : 1000;
            this.autoclose = (option && option.autoclose !== undefined) ? option.autoclose : true;

            this.buttons = [];
            // Add Close Button
            this.buttons.push({
                text: "閉じる",
                "class": "yes ",
                size: "large",
                color: "",
                click: function(viewmodel, ui) {
                    viewmodel.closeButtonClicked();
                    ui.dialog("close");
                }
            });
        }
    }

    export type ButtonSize = "x-large" | "large" | "medium" | "small";
    export type ButtonColor = "" | "danger" | "proceed";

    export class DialogButton {
        text: string;
        "class": string;
        size: ButtonSize;
        color: ButtonColor;
        click(viewmodel, ui): void { };
    }

}