/// <reference path="../reference.ts"/>

module nts.uk.ui.option {
    
    export abstract class EditorOptionBase {
        placeholder: string;
        width: string;
        textalign: string;
    }

    // Text Editor Option
    export interface ITextEditorOption{
        textmode?: TextMode;
        placeholder?: string;
        width?: string;
        textalign?: string;
        autofill?: boolean;
        filldirection?: FillDirection;
        fillcharacter?: string;
    }

    export class TextEditorOption extends EditorOptionBase {
        textmode: TextMode;
        autofill: boolean;
        filldirection: FillDirection;
        fillcharacter: string;
        
        constructor(option?: ITextEditorOption) {
            super();
            // Default value
            this.textmode = (option !== undefined && option.textmode !== undefined) ? option.textmode : "text";
            this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
            this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
            this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "";
            this.autofill = (option !== undefined && option.autofill !== undefined) ? option.autofill : false;
            this.filldirection = (option !== undefined && option.filldirection !== undefined) ? option.filldirection : "right";
            this.fillcharacter = (option !== undefined && option.fillcharacter !== undefined) ? option.fillcharacter : "0";
        }
    }

    // Time Editor Option
    export interface ITimeEditorOption{
        inputFormat?: string;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }
    
    export class TimeEditorOption extends EditorOptionBase {
        inputFormat: string;
        
        constructor(option?: ITimeEditorOption) {
            super();
            // Default value
            this.inputFormat = (option !== undefined && option.inputFormat !== undefined) ? option.inputFormat : "date";
            this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
            this.width = (option !== undefined && option.width !== undefined ) ? option.width : "";
            this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "right";
        }
    }

    // Number Editor Option
    export interface INumberEditorOption{
        groupseperator?: string,
        grouplength?: number,
        decimalseperator?: string,
        decimallength?: number,
        currencyformat?: Currency,
        currencyposition?: string,
        placeholder?: string;
        width?: string;
        textalign?: string;
        symbolChar?: string;
        symbolPosition?: string;
        defaultValue?: string;
    }

    export class NumberEditorOption extends EditorOptionBase {
        groupseperator: string;
        grouplength: number;
        decimalseperator: string;
        decimallength: number;
        symbolChar: string;
        symbolPosition: string;
        defaultValue: string;
        
        constructor(option?: INumberEditorOption) {
            super();
            // Default value
            this.groupseperator = (option !== undefined && option.groupseperator !== undefined) ? option.groupseperator : ",";
            this.grouplength = (option !== undefined && option.grouplength !== undefined) ? option.grouplength : 0;
            this.decimalseperator = (option !== undefined && option.decimalseperator !== undefined) ? option.decimalseperator : ".";
            this.decimallength = (option !== undefined && option.decimallength !== undefined) ? option.decimallength : 0;
            this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
            this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
            this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "right";
            this.symbolChar = (option !== undefined && option.symbolChar !== undefined) ? option.symbolChar : "";
            this.symbolPosition = (option !== undefined && option.symbolPosition !== undefined) ? option.symbolPosition : "right";
            this.defaultValue = (option !== undefined && !nts.uk.util.isNullOrEmpty(option.defaultValue)) ? option.defaultValue : "";
        }
    }

    export class CurrencyEditorOption extends NumberEditorOption {
        currencyformat: Currency;
        currencyposition: string;
        
        constructor(option?: INumberEditorOption) {
            super();
            // Default value
            this.groupseperator = (option !== undefined && option.groupseperator !== undefined) ? option.groupseperator : ",";
            this.grouplength = (option !== undefined && option.grouplength !== undefined) ? option.grouplength : 0;
            this.decimalseperator = (option !== undefined && option.decimalseperator !== undefined) ? option.decimalseperator : ".";
            this.decimallength = (option !== undefined && option.decimallength !== undefined) ? option.decimallength : 0;
            this.currencyformat = (option !== undefined && option.currencyformat !== undefined) ? option.currencyformat : "JPY";
            this.currencyposition = (option !== undefined && option.currencyposition !== undefined)
                ? option.currencyposition : getCurrencyPosition(this.currencyformat);
            this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
            this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
            this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "right";
            this.defaultValue = (option !== undefined && !nts.uk.util.isNullOrEmpty(option.defaultValue)) ? option.defaultValue : "";
        }
    }
    
    function getCurrencyPosition(currencyformat): string{
        return currenryPosition[currencyformat] === null ? "right" : currenryPosition[currencyformat];
    }
        
    // Multiline Editor Option
    export interface IMultilineEditorOption{
        resizeable?: boolean;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }

    export class MultilineEditorOption extends EditorOptionBase {
        resizeable: boolean;
        
        constructor(option?: IMultilineEditorOption) {
            super();
            // Default value
            this.resizeable = (option !== undefined && option.resizeable !== undefined) ? option.resizeable : false;
            this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
            this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
            this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "";
        }
    }
    
    export type TextMode = "text" | "password";
    export type FillDirection = "left" |"right";
    export type Currency = "JPY" | "USD";
    
    var currenryPosition = {
        "JPY" : "left",
        "USD" : "right"
    }
}