
    abstract class EditorOptionBase {
        textmode: TextMode;
        enable: boolean = true;
        readonly: boolean = false;
        placeholder: string;
        width: string;
        textalign: string;
        protected class: string;
    }

    // Text Editor Option
    interface ITextEditorOption{
        textmode?: TextMode;
        enable?: boolean;
        readonly?: boolean;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }

    class TextEditorOption extends EditorOptionBase {
        constructor(option?: ITextEditorOption) {
            super();
            // Default value
            this.textmode = (option && option.textmode) ? option.textmode : "text";
            this.placeholder = (option && option.placeholder) ? option.placeholder : "";
            this.width = (option && option.width) ? option.width : "";
            this.textalign = (option && option.textalign) ? option.textalign : "left";
        }
    }

    class TimeEditorOption extends EditorOptionBase {
        constructor(option?: ITextEditorOption) {
            super();
            // Default value
            this.textmode = (option && option.textmode) ? option.textmode : "text";
            this.placeholder = (option && option.placeholder) ? option.placeholder : "";
            this.width = (option && option.width) ? option.width : "";
            this.textalign = (option && option.textalign) ? option.textalign : "left";
        }
    }

    class MaskEditorOption extends EditorOptionBase {
        constructor(option?: ITextEditorOption) {
            super();
            // Default value
            this.textmode = (option && option.textmode) ? option.textmode : "text";
            this.placeholder = (option && option.placeholder) ? option.placeholder : "";
            this.width = (option && option.width) ? option.width : "";
            this.textalign = (option && option.textalign) ? option.textalign : "left";
        }
    }

    
    // Number Editor Option
    interface INumberEditorOption{
        groupseperator?: string,
        grouplength?: number,
        decimalseperator?: string,
        decimallength?: number,
        currencyformat?: Currency,
        currencyposition?: string,
        enable?: boolean;
        readonly?: boolean;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }

    class NumberEditorOption extends EditorOptionBase {
        groupseperator: string;
        grouplength: number;
        decimalseperator: string;
        decimallength: number;
        protected regex: string;
        
        constructor(option?: INumberEditorOption) {
            super();
            // Default value
            this.groupseperator = (option && option.groupseperator) ? option.groupseperator : ",";
            this.grouplength = (option && option.grouplength) ? option.grouplength : 0;
            this.decimalseperator = (option && option.decimalseperator) ? option.decimalseperator : ".";
            this.decimallength = (option && option.decimallength) ? option.decimallength : 0;
            this.placeholder = (option && option.placeholder) ? option.placeholder : "";
            this.width = (option && option.width) ? option.width : "";
            this.textalign = (option && option.textalign) ? option.textalign : "left";
            // Regex: /^-?\d+(\,\d{3})*([.]\d+)?$/
            this.regex = "/^-?\\d+";
            if (this.grouplength > 0)
                this.regex += "(\\" + this.groupseperator + "\\d{" + this.grouplength + "})*";
            if (this.decimallength > 0)
                this.regex += "([" + this.decimalseperator + "]\\d+)?";
            this.regex += "$/";
            console.log(this.regex);
        }
    }

    class CurrencyEditorOption extends NumberEditorOption {
        currencyformat: Currency;
        currencyposition: string;
        
        constructor(option?: INumberEditorOption) {
            super();
            // Default value
            this.groupseperator = (option && option.groupseperator) ? option.groupseperator : ",";
            this.grouplength = (option && option.grouplength) ? option.grouplength : 0;
            this.decimalseperator = (option && option.decimalseperator) ? option.decimalseperator : ".";
            this.decimallength = (option && option.decimallength) ? option.decimallength : 0;
            this.currencyformat = (option && option.currencyformat) ? option.currencyformat : "JPY";
            this.currencyposition = (option && option.currencyposition) ? option.currencyposition : "";
            // TODO: Write ()=> to return string instead of check
            switch(this.currencyformat) {
                case "JPY":
                    this.currencyposition = "left"; break;
                case "USD":
                    this.currencyposition = "right"; break;
            }
            this.placeholder = (option && option.placeholder) ? option.placeholder : "";
            this.width = (option && option.width) ? option.width : "";
            this.textalign = (option && option.textalign) ? option.textalign : "left";
        }
    }

    type TextMode = "text" | "password";
    type Currency = "JPY" | "USD";