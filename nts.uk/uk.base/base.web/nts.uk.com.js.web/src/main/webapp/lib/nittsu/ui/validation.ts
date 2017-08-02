/// <reference path="../reference.ts"/>

module nts.uk.ui.validation {
	import util = nts.uk.util;
	
    export interface IValidator {
        validate(inputText: string, option?: any): ValidationResult;
    }

    export class NoValidator {
        validate(inputText: string, option?: any): ValidationResult {
            var result = new ValidationResult();
            result.isValid = true;
            result.parsedValue = inputText;
            return result;
        }
    }

    export class ValidationResult {
        isValid: boolean;
        parsedValue: any;
        errorMessage = 'error message';
        errorCode: string;

        fail(errorMessage: any, errorCode: string) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.isValid = false;
        }

        success(parsedValue: any) {
            this.parsedValue = parsedValue;
            this.isValid = true;
        }
    }

    export class StringValidator implements IValidator {
        name: string;
        constraint: any;
        charType: nts.uk.text.CharType;
        required: boolean;

        constructor(name: string, primitiveValueName: string, option?: any) {
            this.name = name;
            this.constraint = getConstraint(primitiveValueName);
            this.charType = text.getCharType(primitiveValueName);
            this.required = option.required;
        }

        validate(inputText: string, option?: any): ValidationResult {
            var result = new ValidationResult();
            // Check Required
            if (this.required !== undefined && this.required !== false) {
                if (util.isNullOrEmpty(inputText)) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                }
            }
            let validateResult;
            // Check CharType
            if (!util.isNullOrUndefined(this.charType)) { 
                if (this.charType.viewName === '半角英数字') {
                    inputText = text.toUpperCase(inputText);
                } else if (this.charType.viewName === 'カタカナ') {
                    inputText = text.oneByteKatakanaToTwoByte(inputText);    
                } else if (this.charType.viewName === 'カナ') {
                    inputText = text.hiraganaToKatakana(text.oneByteKatakanaToTwoByte(inputText));
                }
                validateResult = this.charType.validate(inputText); 
                if (!validateResult.isValid) {
                    result.fail(nts.uk.resource.getMessage(validateResult.errorMessage, 
                                [ this.name, !util.isNullOrUndefined(this.constraint) 
                                ? (!util.isNullOrUndefined(this.constraint.maxLength) 
                                    ? this.constraint.maxLength : 9999) : 9999 ]), validateResult.errorCode);
                    return result;
                }
            }
            // Check Constraint
            if (this.constraint !== undefined && this.constraint !== null) {
                if (this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
                	let maxLength = this.constraint.maxLength;
                	if (this.constraint.charType == "Any")
                		maxLength = maxLength/2;
                    result.fail(nts.uk.resource.getMessage(validateResult.errorMessage,
                                [ this.name, maxLength ]), validateResult.errorCode);
                    return result;
                }
                
                if(!util.isNullOrUndefined(option) && option.isCheckExpression === true){  
                    if (!text.isNullOrEmpty(this.constraint.stringExpression) && !this.constraint.stringExpression.test(inputText)) {
                        result.fail('This field is not valid with pattern!', '');
                        return result;
                    }  
                }
            }
            
            result.success(inputText);
            return result;
        }
    }

    export class NumberValidator implements IValidator {
        name: string;
        constraint: any;
        option: any;

        constructor(name: string, primitiveValueName: string, option: any) {
            this.name = name;
            this.constraint = getConstraint(primitiveValueName);
            this.option = option;
        }

        validate(inputText: string): ValidationResult {
            var result = new ValidationResult();
            var isDecimalNumber = false;
            if (this.option !== undefined) {
                if(nts.uk.util.isNullOrUndefined(inputText) || inputText.trim().length <= 0){
                    if(this.option['required'] === true && nts.uk.util.isNullOrEmpty(this.option['defaultValue'])){    
                        result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                        return result;
                    } else {
                        result.success(this.option['defaultValue']);
                        return result;
                    }    
                }
                isDecimalNumber = (this.option.decimallength > 0)
                inputText = text.replaceAll(inputText.toString(), this.option.groupseperator, '');
            }

            var message: any = {};
            var validateFail = false, max = 99999999, min = 0, mantissaMaxLength;
            if (!util.isNullOrUndefined(this.constraint) && this.constraint.valueType === "HalfInt") {
                if (!ntsNumber.isHalfInt(inputText, message)) validateFail = true;
            } else if (!ntsNumber.isNumber(inputText, isDecimalNumber, undefined, message)) {
                validateFail = true;
            }
            var value = isDecimalNumber ?
                ntsNumber.getDecimal(inputText, this.option.decimallength) : parseInt(inputText);

            if (!util.isNullOrUndefined(this.constraint)) {
                if (!util.isNullOrUndefined(this.constraint.max)) {
                    max = this.constraint.max
                    if (value > this.constraint.max) validateFail = true;
                }
                if (!util.isNullOrUndefined(this.constraint.min)) {
                    min = this.constraint.min;
                    if (value < this.constraint.min) validateFail = true;
                }
                if (!util.isNullOrUndefined(this.constraint.mantissaMaxLength)) {
                    mantissaMaxLength = this.constraint.mantissaMaxLength;
                    let parts = String(value).split(".");
                    if (parts[1] !== undefined && parts[1].length > mantissaMaxLength) validateFail = true;
                }
            }
            if (validateFail) {
                result.fail(nts.uk.resource.getMessage(message.id, [ this.name, min, max, mantissaMaxLength ]), message.id);
            } else { 
                let formated = value.toString() === "0" ? inputText : text.removeFromStart(inputText, "0"); 
                if (formated.indexOf(".") >= 0) {
                    formated = text.removeFromEnd(formated, "0");    
                }
                if(formated.charAt(0) === "."){
                    formated = "0" + formated;           
                }
                if (formated.charAt(formated.length - 1) === ".") {
                    formated = formated.substr(0, formated.length - 1);            
                }
                result.success(formated);
            }
            return result; 
        } 
    }

    export class TimeValidator implements IValidator {
        name: string;
        constraint: any;
        outputFormat: string;
        required: boolean; 
        valueType: string;
        mode: string;
        constructor(name: string, primitiveValueName: string, option?: any) {
            this.name = name;
            this.constraint = getConstraint(primitiveValueName);
            this.outputFormat = (option && option.outputFormat) ? option.outputFormat : "";
            this.required = (option && option.required) ? option.required : false;
            this.valueType = (option && option.valueType) ? option.valueType : "string";
            this.mode = (option && option.mode) ? option.mode : "";
        }

        validate(inputText: string): any {
            var result = new ValidationResult();
            // Check required
            if (util.isNullOrEmpty(inputText)) {
                if (this.required === true) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                }
                else {
                    result.success("");
                    return result;
                }
            }
            
            let maxStr, minStr;
            // Time duration
            if(this.mode === "time"){
                var timeParse = time.parseTime(inputText, false) 
                if (timeParse.success) {
                    result.success(timeParse.toValue());
                } else {
                    let msgId = timeParse.getMsg();
                    let msg = nts.uk.resource.getMessage(msgId, [this.name, this.constraint.min, this.constraint.max]);
                    result.fail(msg, msgId); 
                    return result;
                }
                
                if (!util.isNullOrUndefined(this.constraint)) {
                    if (!util.isNullOrUndefined(this.constraint.max)) {
                        maxStr = this.constraint.max;
                        let max = time.parseTime(this.constraint.max);
                        if (timeParse.success && (max.toValue() < timeParse.toValue())) {
                            let msg = nts.uk.resource.getMessage("FND_E_TIME", [this.name, this.constraint.min, this.constraint.max]);
                            result.fail(msg, "FND_E_TIME");
                            return result;
                        }
                    }
                    
                    if (!util.isNullOrUndefined(this.constraint.min)) {
                        minStr = this.constraint.min;
                        let min = time.parseTime(this.constraint.min);
                        if (timeParse.success && (min.toValue() > timeParse.toValue())) {
                            let msg = nts.uk.resource.getMessage("FND_E_TIME", [this.name, this.constraint.min, this.constraint.max]);
                            result.fail(msg, "FND_E_TIME");
                            return result;
                        }
                    }
                    
                    if (!result.isValid && this.constraint.valueType === "Time") {
                        result.fail(nts.uk.resource.getMessage("FND_E_TIME", [ this.name, minStr, maxStr ]), "FND_E_TIME");
                    }
                }
                return result;   
            }
            
            var isMinuteTime = this.outputFormat === "time" ? inputText.charAt(0) === "-" : false;
            if(isMinuteTime){
                inputText = inputText.substring(1, inputText.length);            
            }
            
            var parseResult = time.parseMoment(inputText, this.outputFormat);
            // Parse
            if (parseResult.success) {
                if (this.valueType === "string")
                    result.success(parseResult.format());
                else if (this.valueType === "number") {
                    result.success(parseResult.toNumber(this.outputFormat));
                }
                else if (this.valueType === "date") {
                    result.success(parseResult.toValue().toDate());
                }
                else if (this.valueType === "moment") {
                    result.success(parseResult.toValue());
                }
                else {
                    result.success(parseResult.format());
                }
            } else {
                result.fail(parseResult.getEmsg(this.name), parseResult.getMsgID());
                return result;
            }
            
            // Time clock
            if (this.outputFormat === "time") {
                if (!util.isNullOrUndefined(this.constraint)) {
                    let inputMoment = parseResult.toNumber(this.outputFormat)* (isMinuteTime ? -1 : 1);
                    if (!util.isNullOrUndefined(this.constraint.max)) { 
                        maxStr = this.constraint.max;
                        let maxMoment = moment.duration(maxStr);
                        if (parseResult.success && (maxMoment.hours()*60 + maxMoment.minutes()) < inputMoment) {
                            result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [ this.name, minStr, maxStr ]), "FND_E_CLOCK");
                            return result;
                        } 
                    } 
                    if (!util.isNullOrUndefined(this.constraint.min)) {
                        minStr = this.constraint.min;
                        let minMoment = moment.duration(minStr);
                        if (parseResult.success && (minMoment.hours()*60 + minMoment.minutes()) > inputMoment) {
                            result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [ this.name, minStr, maxStr ]), "FND_E_CLOCK");
                            return result;
                        }
                    }
                    
                    if (!result.isValid && this.constraint.valueType === "Clock") {
                        result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [this.name, minStr, maxStr]), "FND_E_CLOCK");
                    }
                }
                
            }
            return result;
        }
    }
    
    export class TimeWithDayValidator implements IValidator {
        name: string;
        constraint: any;
        required: boolean; 
        constructor(name: string, primitiveValueName: string, option?: any) {
            this.name = name;
            this.constraint = getConstraint(primitiveValueName);
            this.required = (option && option.required) ? option.required : false;
        }

        validate(inputText: string): any {
            var result = new ValidationResult();
            inputText = time.TimeWithDayAttr.cutDayDivision(inputText);
            // Check required
            if (util.isNullOrEmpty(inputText)) {
                if (this.required === true) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } else {
                    result.success("");
                    return result;
                }
            }
            let minStr, maxStr;
            if(!util.isNullOrUndefined(this.constraint)){
                minStr = time.parseTime(this.constraint.min, true).format();
                maxStr = time.parseTime(this.constraint.max, true).format();            
            }
            
            let parseValue = time.parseTime(inputText);
            
            if (!parseValue.success || (parseValue.toValue() < this.constraint.min || parseValue.toValue() > this.constraint.max)) {
                result.fail(nts.uk.resource.getMessage("FND_E_TIME", [ this.name, minStr, maxStr ]), "FND_E_TIME");     
            } else {
                result.success(parseValue.toValue());    
            }
            
            return result;
        }
    }

    export function getConstraint(primitiveValueName: string) {
        var constraint = __viewContext.primitiveValueConstraints[primitiveValueName];
        if (constraint === undefined)
            return null;
        else
            return __viewContext.primitiveValueConstraints[primitiveValueName];
    }
}