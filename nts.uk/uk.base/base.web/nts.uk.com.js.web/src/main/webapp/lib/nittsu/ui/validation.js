/// <reference path="../reference.ts"/>
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var validation;
            (function (validation) {
                var NoValidator = (function () {
                    function NoValidator() {
                    }
                    NoValidator.prototype.validate = function (inputText, option) {
                        var result = new ValidationResult();
                        result.isValid = true;
                        result.parsedValue = inputText;
                        return result;
                    };
                    return NoValidator;
                }());
                validation.NoValidator = NoValidator;
                var ValidationResult = (function () {
                    function ValidationResult() {
                        this.errorMessage = 'error message';
                    }
                    ValidationResult.prototype.fail = function (errorMessage) {
                        this.errorMessage = errorMessage;
                        this.isValid = false;
                    };
                    ValidationResult.prototype.success = function (parsedValue) {
                        this.parsedValue = parsedValue;
                        this.isValid = true;
                    };
                    return ValidationResult;
                }());
                validation.ValidationResult = ValidationResult;
                var StringValidator = (function () {
                    function StringValidator(primitiveValueName, option) {
                        this.constraint = getConstraint(primitiveValueName);
                        this.charType = uk.text.getCharType(primitiveValueName);
                        this.required = option.required;
                    }
                    StringValidator.prototype.validate = function (inputText, option) {
                        var result = new ValidationResult();
                        // Check Required
                        if (this.required !== undefined && this.required !== false) {
                            if (uk.util.isNullOrEmpty(inputText)) {
                                result.fail('This field is required');
                                return result;
                            }
                        }
                        // Check CharType
                        if (this.charType !== null && this.charType !== undefined) {
                            if (this.charType.viewName === '半角数字') {
                                inputText = uk.text.toOneByteAlphaNumberic(inputText);
                            }
                            else if (this.charType.viewName === '半角英数字') {
                                inputText = uk.text.toOneByteAlphaNumberic(uk.text.toUpperCase(inputText));
                            }
                            else if (this.charType.viewName === 'カタカナ') {
                                inputText = uk.text.oneByteKatakanaToTwoByte(inputText);
                            }
                            else if (this.charType.viewName === 'カナ') {
                                inputText = uk.text.hiraganaToKatakana(uk.text.oneByteKatakanaToTwoByte(inputText));
                            }
                            if (!this.charType.validate(inputText)) {
                                result.fail('Invalid text');
                                return result;
                            }
                        }
                        // Check Constraint
                        if (this.constraint !== undefined && this.constraint !== null) {
                            if (this.constraint.maxLength !== undefined && uk.text.countHalf(inputText) > this.constraint.maxLength) {
                                result.fail('Max length for this input is ' + this.constraint.maxLength);
                                return result;
                            }
                            if (!uk.util.isNullOrUndefined(option) && option.isCheckExpression === true) {
                                if (!uk.text.isNullOrEmpty(this.constraint.stringExpression) && !this.constraint.stringExpression.test(inputText)) {
                                    result.fail('This field is not valid with pattern!');
                                    return result;
                                }
                            }
                        }
                        result.success(inputText);
                        return result;
                    };
                    return StringValidator;
                }());
                validation.StringValidator = StringValidator;
                var NumberValidator = (function () {
                    function NumberValidator(primitiveValueName, option) {
                        this.constraint = getConstraint(primitiveValueName);
                        this.option = option;
                    }
                    NumberValidator.prototype.validate = function (inputText) {
                        var result = new ValidationResult();
                        var isDecimalNumber = false;
                        if (this.option !== undefined) {
                            if (nts.uk.util.isNullOrUndefined(inputText) || inputText.trim().length <= 0) {
                                if (this.option['required'] === true && nts.uk.util.isNullOrEmpty(this.option['defaultValue'])) {
                                    result.fail('This field is required.');
                                    return result;
                                }
                                else {
                                    result.success(this.option['defaultValue']);
                                    return result;
                                }
                            }
                            isDecimalNumber = (this.option.decimallength > 0);
                            inputText = uk.text.replaceAll(inputText.toString(), this.option.groupseperator, '');
                        }
                        if (!uk.ntsNumber.isNumber(inputText, isDecimalNumber)) {
                            result.fail('invalid number');
                            return result;
                        }
                        var value = isDecimalNumber ?
                            uk.ntsNumber.getDecimal(inputText, this.option.decimallength) : parseInt(inputText);
                        if (this.constraint !== null) {
                            if (this.constraint.max !== undefined && value > this.constraint.max) {
                                result.fail('max value is: ' + this.constraint.max);
                                return result;
                            }
                            if (this.constraint.min !== undefined && value < this.constraint.min) {
                                result.fail('min value is: ' + this.constraint.min);
                                return result;
                            }
                        }
                        result.success(inputText === "0" ? inputText : uk.text.removeFromStart(inputText, "0"));
                        return result;
                    };
                    return NumberValidator;
                }());
                validation.NumberValidator = NumberValidator;
                var TimeValidator = (function () {
                    function TimeValidator(primitiveValueName, option) {
                        this.constraint = getConstraint(primitiveValueName);
                        this.outputFormat = (option && option.outputFormat) ? option.outputFormat : "";
                        this.required = (option && option.required) ? option.required : false;
                        this.valueType = (option && option.valueType) ? option.valueType : "string";
                        this.mode = (option && option.mode) ? option.mode : "";
                    }
                    TimeValidator.prototype.validate = function (inputText) {
                        var result = new ValidationResult();
                        // Check required
                        if (uk.util.isNullOrEmpty(inputText)) {
                            if (this.required === true) {
                                result.fail('This field is required');
                                return result;
                            }
                            else {
                                result.success("");
                                return result;
                            }
                        }
                        // Create Parser
                        if (this.mode === "time") {
                            var timeParse = uk.time.parseTime(inputText, false);
                            if (timeParse.success) {
                                result.success(timeParse.toValue());
                            }
                            else {
                                result.fail(timeParse.getMsg());
                            }
                            return result;
                        }
                        var parseResult = uk.time.parseMoment(inputText, this.outputFormat);
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
                        }
                        else {
                            result.fail(parseResult.getMsg());
                        }
                        return result;
                    };
                    return TimeValidator;
                }());
                validation.TimeValidator = TimeValidator;
                function getConstraint(primitiveValueName) {
                    var constraint = __viewContext.primitiveValueConstraints[primitiveValueName];
                    if (constraint === undefined)
                        return null;
                    else
                        return __viewContext.primitiveValueConstraints[primitiveValueName];
                }
                validation.getConstraint = getConstraint;
            })(validation = ui.validation || (ui.validation = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=validation.js.map