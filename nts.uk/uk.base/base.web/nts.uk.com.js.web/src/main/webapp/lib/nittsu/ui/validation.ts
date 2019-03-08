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

    export class DepartmentCodeValidator implements IValidator {
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
            if (util.isNullOrEmpty(inputText)) {
                if (this.required !== undefined && this.required !== false) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } 
                result.success(inputText);
                return result; 
            }
            
            //let validateResult;
            // Check CharType
            result= checkCharType(inputText,this.charType);
            if(!result.isValid) return result;
            // Check Constraint
            if (this.constraint !== undefined && this.constraint !== null) {
                if (this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
                    let maxLength = this.constraint.maxLength;
                    result.fail(nts.uk.resource.getMessage(result.errorMessage,
                                [ this.name, maxLength ]), result.errorCode);
                    return result;
                }
                
                if (!util.isNullOrUndefined(option) && option.isCheckExpression === true){  
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
    function checkCharType(inputText:string, charType: nts.uk.text.CharType):ValidationResult{
        var result = new ValidationResult();
        let validateResult;
        if (!util.isNullOrUndefined(charType)) { 
            inputText = autoConvertText(inputText, charType);
            validateResult = charType.validate(inputText); 
            if (!validateResult.isValid) {
                result.fail(nts.uk.resource.getMessage(validateResult.errorMessage, 
                            [ this.name, !util.isNullOrUndefined(this.constraint) 
                            ? (!util.isNullOrUndefined(this.constraint.maxLength) 
                                ? this.constraint.maxLength : 9999) : 9999 ]), validateResult.errorCode);
                return result;
            }
		return validateResult;
        }
        result.success(inputText);
        return result;
    }
    function autoConvertText(inputText: string, charType:nts.uk.text.CharType):string{
        if (charType.viewName === '半角英数字') {
           inputText = text.toUpperCase(inputText);
        } else if (charType.viewName === 'カタカナ') {
           inputText = text.oneByteKatakanaToTwoByte(inputText);    
        } else if (charType.viewName === 'カナ') {
           inputText = text.hiraganaToKatakana(text.oneByteKatakanaToTwoByte(inputText));
        }
        return inputText;
    }
    export class WorkplaceCodeValidator implements IValidator {
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
            if (util.isNullOrEmpty(inputText)) {
                if (this.required !== undefined && this.required !== false) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } 
                result.success(inputText);
                return result;
            }
            
            // Check CharType
            result= checkCharType(inputText,this.charType);
            if(!result.isValid) return result;
            // Check Constraint
            if (this.constraint !== undefined && this.constraint !== null) {
                if (this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
                    let maxLength = this.constraint.maxLength;
                    result.fail(nts.uk.resource.getMessage(result.errorMessage,
                                [ this.name, maxLength ]), result.errorCode);
                    return result;
                }
                
                if (!util.isNullOrUndefined(option) && option.isCheckExpression === true){  
                    if (!text.isNullOrEmpty(this.constraint.stringExpression) && !this.constraint.stringExpression.test(inputText)) {
                        result.fail('This field is not valid with pattern!', '');
                        return result;
                    }  
                }
            }
            
            return result;
        }
    }
    export class PostCodeValidator implements IValidator {
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
            if (util.isNullOrEmpty(inputText)) {
                if (this.required !== undefined && this.required !== false) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } 
                result.success(inputText);
                return result;
            }
            //let validateResult;
            // Check CharType
            result= checkCharType(inputText,this.charType);
            if(!result.isValid) return result;
            // Check Constraint
            if (this.constraint !== undefined && this.constraint !== null) {
                if (this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
                    let maxLength = this.constraint.maxLength;
                    result.fail(nts.uk.resource.getMessage(result.errorMessage,
                                [ this.name, maxLength ]), result.errorCode);
                    return result;
                }
                
                if (!util.isNullOrUndefined(option) && option.isCheckExpression === true){  
                    if (!text.isNullOrEmpty(this.constraint.stringExpression) && !this.constraint.stringExpression.test(inputText)) {
                        result.fail(nts.uk.resource.getMessage('Msg_1424', [ this.name ]), 'Msg_1424');
                        return result;
                    }  
                }
            }
            
            result.success(inputText);
            return result;
        }
    }
    export class PunchCardNoValidator implements IValidator {
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
            if (util.isNullOrEmpty(inputText)) {
                if (this.required !== undefined && this.required !== false) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } 
                result.success(inputText);
                return result;
            }
            //let validateResult;
            // Check Constraint
            if (this.constraint !== undefined && this.constraint !== null) {
                
                if (!util.isNullOrUndefined(option) && option.isCheckExpression === true){  
                    if (!text.isNullOrEmpty(this.constraint.stringExpression) && !this.constraint.stringExpression.test(inputText)) {
                        result.fail(nts.uk.resource.getMessage('Msg_1285', [ this.name ]), 'Msg_1285');
                        return result;
                    }  
                }
            }
            
            // Check CharType
            result= checkCharType(inputText,this.charType);
            if(!result.isValid) return result;
            
            if (!_.isNil(this.constraint) && this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
                let maxLength = this.constraint.maxLength;
                result.fail(nts.uk.resource.getMessage(result.errorMessage,
                            [ this.name, maxLength ]), result.errorCode);
                return result;
            }
            
            result.success(inputText);
            return result;
        }
    }
    
    export class EmployeeCodeValidator implements IValidator {
        name: string;
        constraint: any;
        charType: text.CharType;
        options: any;
        
        constructor(name: string, options?: any) {
            let self = this;
            this.name = name;
            this.constraint = getConstraint("EmployeeCode"); 
            this.charType = text.getCharTypeByType("EmployeeCode");
            this.options = options;
        }
        
        validate(inputText: string) : ValidationResult {
            let self = this;
            let result = new ValidationResult();
            if (util.isNullOrEmpty(inputText)) {
                if (self.options.required) { 
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT')
                    return result;
                }
                
                result.success(inputText);
                return result;
            }
            
            result = checkCharType.call(self, inputText, self.charType);
            if (!result.isValid) return result;
            
            if (self.constraint && !util.isNullOrUndefined(self.constraint.maxLength)
                && self.constraint.maxLength < text.countHalf(inputText)) {
                result.fail(nts.uk.resource.getMessage(result.errorMessage, 
                    [ self.name, self.constraint.maxLength ]), result.errorCode);
                return result;
            }
            
            result.success(inputText);
            return result;
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
            if(nts.uk.util.isNullOrUndefined(this.constraint)){
                this.constraint = {};
            }
            this.charType = text.getCharType(primitiveValueName);
            this.required = (!nts.uk.util.isNullOrUndefined(option.required) && option.required) || this.constraint.required;
        }

        validate(inputText: string, option?: any): ValidationResult {
            var result = new ValidationResult();
            // Check Required
            if (util.isNullOrEmpty(inputText)) {
                if (this.required !== undefined && this.required !== false) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                } 
                result.success(inputText);
                return result;
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
                                [ this.name, (!util.isNullOrUndefined(this.constraint.maxLength) 
                                    ? this.charType.getViewLength(this.constraint.maxLength) : 9999) ]), validateResult.errorCode);
                    return result;
                }
            } else {
                validateResult = result;    
            }
            
            // Check Constraint
            if (this.constraint.maxLength !== undefined && text.countHalf(inputText) > this.constraint.maxLength) {
            	let maxLength = this.constraint.maxLength;
            	if (this.constraint.charType == "Any" || this.constraint.charType === "Kana")
            		maxLength = nts.uk.text.getCharTypeByType("Any").getViewLength(maxLength);
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
            if(util.isNullOrUndefined(this.constraint)){
                this.constraint = {};
            }
            this.option = option;
        }

        validate(inputText: string): ValidationResult {
            var result = new ValidationResult();
            var isDecimalNumber = false;
            if (this.option !== undefined) {
                if(nts.uk.util.isNullOrUndefined(inputText) || inputText.trim().length <= 0){
                    if((this.option['required'] === true || this.constraint["required"] === true)&& nts.uk.util.isNullOrEmpty(this.option['defaultValue'])){    
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
            inputText = inputText.trim();
            var message: any = {};
            var validateFail = false, max = 99999999, min = 0, mantissaMaxLength;
            if (this.constraint.valueType === "HalfInt") {
                if (!ntsNumber.isHalfInt(inputText, message)) validateFail = true;
            } else if (!ntsNumber.isNumber(inputText, isDecimalNumber, undefined, message)) {
                validateFail = true;
            }
            var value = isDecimalNumber ?
                ntsNumber.getDecimal(inputText, this.option.decimallength) : parseInt(inputText);

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
                let parts = inputText.split(".");
                if (parts[1] !== undefined && parts[1].length > mantissaMaxLength) validateFail = true;
            }
            if(!(/^-?\d*(\.\d+)?$/).test(inputText)){
                validateFail = true;
            }
            
            if (validateFail) {
                result.fail(nts.uk.resource.getMessage(message.id, [ this.name, min, max, mantissaMaxLength ]), message.id);
            } else {  
                let formated = value.toString() === "0" ? "0" : text.removeFromStart(inputText, "0"); 
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
        inputFormat: string;
        required: boolean; 
        valueType: string;
        mode: string;
        acceptJapaneseCalendar: boolean;
        defaultValue: string;
        
        constructor(name: string, primitiveValueName: string, option?: any) {
            this.name = name;
            this.constraint = getConstraint(primitiveValueName);
            if(nts.uk.util.isNullOrUndefined(this.constraint)){
                this.constraint = {}; 
                if (option && option.min && option.max) {
                    this.constraint.min = option.min;
                    this.constraint.max = option.max;
                }                   
            }
            this.outputFormat = (option && option.outputFormat) ? option.outputFormat : "";
            this.inputFormat = (option && option.inputFormat) ? option.inputFormat : "";
            this.required = ((option && option.required) ? option.required : false) || this.constraint.required === true;
            this.valueType = (option && option.valueType) ? option.valueType : "string";
            this.mode = (option && option.mode) ? option.mode : "";
            this.acceptJapaneseCalendar = (option && option.acceptJapaneseCalendar) ? option.acceptJapaneseCalendar : true;
            this.defaultValue = (option && option.defaultValue) ? option.defaultValue : ""; 
        }

        validate(inputText: string): any {
            var result = new ValidationResult();
            // Check required
            if(util.isNullOrEmpty(inputText) && !util.isNullOrEmpty(this.defaultValue)){
                inputText = this.defaultValue;
            } else if (util.isNullOrEmpty(inputText)) {
                if (this.required === true) {
                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ this.name ]), 'FND_E_REQ_INPUT');
                    return result;
                }
                else {
                    result.success(null);
                    return result;
                }
            }
            
            if(this.acceptJapaneseCalendar){
                inputText = time.convertJapaneseDateToGlobal(inputText);            
            }
            
            let maxStr, minStr, max, min;
            // Time duration
            if(this.mode === "time"){
                var timeParse, isSecondBase = this.outputFormat.indexOf("s") >= 0,
                    mesId = isSecondBase ? "FND_E_CLOCK_SECOND" : "FND_E_TIME";
                if(isSecondBase){
                    timeParse = time.secondsBased.duration.parseString(inputText);    
                } else {
                    timeParse = time.minutesBased.duration.parseString(inputText);
                }
                if (timeParse.success) {
                    result.success(timeParse.toValue());
                } else {
                    let msgId = timeParse.getMsg();
                    let msg = nts.uk.resource.getMessage(msgId, [this.name, this.constraint.min, this.constraint.max]);
                    result.fail(msg, msgId); 
                    return result;
                }
                
                if (!util.isNullOrUndefined(this.constraint.max)) {
                    maxStr = this.constraint.max;
                    if(isSecondBase){
                        max = time.secondsBased.duration.parseString(maxStr);    
                    } else {
                        max = time.minutesBased.duration.parseString(maxStr);
                    }
                    if (timeParse.success && (max.toValue() < timeParse.toValue())) {
                        let msg = nts.uk.resource.getMessage(mesId, [this.name, this.constraint.min, this.constraint.max]);
                        result.fail(msg, mesId);
                        return result;
                    }
                }
                
                if (!util.isNullOrUndefined(this.constraint.min)) {
                    minStr = this.constraint.min;
                    if(isSecondBase){
                        min = time.secondsBased.duration.parseString(minStr);    
                    } else {
                        min = time.minutesBased.duration.parseString(minStr);
                    }
                    if (timeParse.success && (min.toValue() > timeParse.toValue())) {
                        let msg = nts.uk.resource.getMessage(mesId, [this.name, this.constraint.min, this.constraint.max]);
                        result.fail(msg, mesId);
                        return result;
                    }
                }
                
                if (!result.isValid && this.constraint.valueType === "Time") {
                    result.fail(nts.uk.resource.getMessage(mesId, [ this.name, minStr, maxStr ]), mesId);
                }
                return result;   
            }
            
            var isMinuteTime = this.outputFormat === "time" ? inputText.charAt(0) === "-" : false;
            if(isMinuteTime){
                inputText = inputText.substring(1, inputText.length);            
            }
            
            var parseResult = time.parseMoment(inputText, this.outputFormat, this.inputFormat);
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
            if(nts.uk.util.isNullOrUndefined(this.constraint)){
                this.constraint = {};
            }
            this.required = (option && option.required) ? option.required : false;
        }

        validate(inputText: string): any {
            var result = new ValidationResult();
            
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
            
            var minValue: any = time.minutesBased.clock.dayattr.MIN_VALUE;
            var maxValue: any = time.minutesBased.clock.dayattr.MAX_VALUE;
            
            if (!util.isNullOrUndefined(this.constraint.min)) { 
                var minS = time.minutesBased.clock.dayattr.parseString(this.constraint.min);
                if(minS.success){
                    minValue = time.minutesBased.clock.dayattr.create(minS.asMinutes);
                }
                minValue = time.minutesBased.clock.dayattr.create(
                    time.minutesBased.clock.dayattr.parseString(this.constraint.min).asMinutes);
            }
            if (!util.isNullOrUndefined(this.constraint.max)) {
                var maxS = time.minutesBased.clock.dayattr.parseString(this.constraint.max);
                if(maxS.success){
                    maxValue = time.minutesBased.clock.dayattr.create(maxS.asMinutes);
                }      
            }
            
            var parsed = time.minutesBased.clock.dayattr.parseString(inputText);
            if (!parsed.success || parsed.asMinutes !== Math.round(parsed.asMinutes) 
                || parsed.asMinutes < minValue || parsed.asMinutes > maxValue) {
                result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [ this.name, minValue.fullText, maxValue.fullText ]), "FND_E_CLOCK");
            } else {
                result.success(parsed.asMinutes);
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
    
    export interface ConstraintDescriptor{
        itemCode: string;
        required?: boolean;
    }
    
    export interface StringConstraintDescriptor extends ConstraintDescriptor{
        maxLength: number;
        charType: string;
        paddingCharacter: string;
        isPaddingLeft: boolean;
        isPadding: boolean;
        stringExpression: string;
    }
    
    export interface NumericConstraintDescriptor extends ConstraintDescriptor{
        min: number;
        max: number;
        valueType: string;
        mantissaMaxLength: number; 
    }
    
    export interface TimeConstraintDescriptor extends ConstraintDescriptor{
        min: string;
        max: string;
        valueType: string;
    }
    
    export function writeConstraint(constraintName: string, constraint: ConstraintDescriptor){
        __viewContext.primitiveValueConstraints[constraintName] = constraint;
    }
    
    export function writeConstraints(constraints: Array<ConstraintDescriptor>){
        _.forEach(constraints, function (constraint: ConstraintDescriptor){
            __viewContext.primitiveValueConstraints[constraint.itemCode] = constraint;        
        });
    }
}
