var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var format;
        (function (format) {
            var NoFormatter = (function () {
                function NoFormatter() {
                }
                NoFormatter.prototype.format = function (source) {
                    return source;
                };
                return NoFormatter;
            }());
            format.NoFormatter = NoFormatter;
        })(format = uk.format || (uk.format = {}));
        var text;
        (function (text_1) {
            var regexp = {
                allHalfNumeric: /^\d*$/,
                allHalfAlphabet: /^[a-zA-Z]*$/,
                allHalfAlphanumeric: /^[a-zA-Z0-9]*$/,
                allHalfKatakanaReg: /^[ｱ-ﾝｧ-ｫｬ-ｮｯｦ ﾞﾟ｡.ｰ､･'-]*$/,
                allFullKatakanaReg: /^[ァ-ー　。．ー、・’－ヴヽヾ]*$/,
                allHiragana: /^[ぁ-ん　ー ]*$/,
            };
            function countHalf(text) {
                var count = 0;
                for (var i = 0; i < text.length; i++) {
                    var c = text.charCodeAt(i);
                    if ((0x20 <= c && c <= 0x7e) || (0xff61 <= c && c <= 0xff9f)) {
                        count += 1;
                    }
                    else {
                        count += 2;
                    }
                }
                return count;
            }
            text_1.countHalf = countHalf;
            function allHalfNumeric(text) {
                return regexp.allHalfNumeric.test(text);
            }
            text_1.allHalfNumeric = allHalfNumeric;
            function allHalfAlphabet(text) {
                return regexp.allHalfAlphabet.test(text);
            }
            text_1.allHalfAlphabet = allHalfAlphabet;
            function allHalfAlphanumeric(text) {
                return regexp.allHalfAlphanumeric.test(text);
            }
            text_1.allHalfAlphanumeric = allHalfAlphanumeric;
            function allHalfKatakana(text) {
                return regexp.allHalfKatakanaReg.test(text);
            }
            text_1.allHalfKatakana = allHalfKatakana;
            function allFullKatakana(text) {
                return regexp.allFullKatakanaReg.test(text);
            }
            text_1.allFullKatakana = allFullKatakana;
            function allHalf(text) {
                return text.length === countHalf(text);
            }
            text_1.allHalf = allHalf;
            function allHiragana(text) {
                return regexp.allHiragana.test(text);
            }
            text_1.allHiragana = allHiragana;
            function allKatakana(text) {
                return regexp.allFullKatakanaReg.test(text);
            }
            text_1.allKatakana = allKatakana;
            function htmlEncode(text) {
                var element = document.createElement('pre');
                if (typeof element.textContent !== 'undefined') {
                    element.textContent = text;
                }
                else {
                    element.innerText = text;
                }
                return element.innerHTML;
            }
            text_1.htmlEncode = htmlEncode;
            function toLowerCaseFirst(text) {
                return text.charAt(0).toLowerCase() + text.slice(1);
            }
            text_1.toLowerCaseFirst = toLowerCaseFirst;
            ;
            function toUpperCaseFirst(text) {
                return text.charAt(0).toUpperCase() + text.slice(1);
            }
            text_1.toUpperCaseFirst = toUpperCaseFirst;
            function isNullOrEmpty(text) {
                var result = true;
                if (text !== null && text !== undefined) {
                    var convertValue = String(text);
                    result = convertValue.length === 0;
                }
                return result;
            }
            text_1.isNullOrEmpty = isNullOrEmpty;
            function format(format) {
                var args = [];
                for (var _i = 1; _i < arguments.length; _i++) {
                    args[_i - 1] = arguments[_i];
                }
                var replaceFunction = undefined;
                if (typeof args === 'object') {
                    replaceFunction = function (m, k) { return args[k]; };
                }
                else {
                    var workArgs = arguments;
                    replaceFunction = function (m, k) { return workArgs[Number(k) + 1]; };
                }
                return format.replace(/\{(\w+)\}/g, replaceFunction);
            }
            text_1.format = format;
            function padLeft(text, paddingChar, length) {
                return charPadding(text, paddingChar, true, length);
            }
            text_1.padLeft = padLeft;
            function padRight(text, paddingChar, length) {
                return charPadding(text, paddingChar, false, length);
            }
            text_1.padRight = padRight;
            function charPadding(text, paddingChar, isPadLeft, length) {
                var result;
                if (countHalf(paddingChar) !== 1) {
                    throw new Error('paddingChar "' + paddingChar + '" is not single character');
                }
                var lengthOfSource = countHalf(text);
                var shortage = length - lengthOfSource;
                if (shortage <= 0) {
                    return text;
                }
                var pad = new Array(shortage + 1).join(paddingChar);
                if (isPadLeft) {
                    return pad + text;
                }
                else {
                    return text + pad;
                }
            }
            text_1.charPadding = charPadding;
            function replaceAll(originalString, find, replace) {
                return originalString.split(find).join(replace);
            }
            text_1.replaceAll = replaceAll;
            function removeFromStart(originalString, charSet) {
                if (originalString.length === charSet.length) {
                    return (originalString === charSet) ? "" : originalString;
                }
                var i = findLastContinousIndex(originalString, charSet, 0);
                return originalString.substr(i, originalString.length - i);
            }
            text_1.removeFromStart = removeFromStart;
            function findLastContinousIndex(originalString, charSet, startIndex) {
                if (startIndex >= originalString.length - 1) {
                    return startIndex;
                }
                if (originalString.substr(startIndex, charSet.length) !== charSet) {
                    return startIndex;
                }
                else {
                    return findLastContinousIndex(originalString, charSet, startIndex + charSet.length);
                }
            }
            var CharType = (function () {
                function CharType(viewName, width, validator) {
                    this.viewName = viewName;
                    this.width = width;
                    this.validator = validator;
                }
                CharType.prototype.validate = function (text) {
                    var result = new uk.ui.validation.ValidationResult();
                    if (this.validator(text)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                };
                CharType.prototype.buildConstraintText = function (maxLength) {
                    return this.viewName + this.getViewLength(maxLength) + '文字';
                };
                CharType.prototype.getViewLength = function (length) {
                    return Math.floor(length / (this.width * 2));
                };
                return CharType;
            }());
            text_1.CharType = CharType;
            var charTypes = {
                AnyHalfWidth: new CharType('半角', 0.5, nts.uk.text.allHalf),
                AlphaNumeric: new CharType('半角英数字', 0.5, nts.uk.text.allHalfAlphanumeric),
                Alphabet: new CharType('半角英字', 0.5, nts.uk.text.allHalfAlphabet),
                Numeric: new CharType('半角数字', 0.5, nts.uk.text.allHalfNumeric),
                Any: new CharType('全角', 1, nts.uk.util.alwaysTrue),
            };
            function getCharType(primitiveValueName) {
                var constraint = __viewContext.primitiveValueConstraints[primitiveValueName];
                if (constraint === undefined)
                    return null;
                if (constraint.charType === undefined)
                    constraint.charType = "Any";
                var charType = charTypes[constraint.charType];
                if (charType === undefined) {
                    throw new Error('invalid charTypeName: ' + constraint.charType);
                }
                return charType;
            }
            text_1.getCharType = getCharType;
            function formatEmployeeCode(code, filldirection, fillcharacter, length) {
                if (filldirection === "left")
                    return padLeft(code, fillcharacter, length);
                else
                    return padRight(code, fillcharacter, length);
            }
            text_1.formatEmployeeCode = formatEmployeeCode;
            function splitOrPadRight(originalString, length, char) {
                if (originalString === undefined || length > originalString.length) {
                    originalString = text.padRight(originalString ? originalString : "", char ? char : " ", length);
                }
                else {
                    originalString = originalString.substr(0, length);
                }
                return originalString;
            }
            text_1.splitOrPadRight = splitOrPadRight;
            function addSeperation(amount) {
                var leng = amount.indexOf(".") > -1 ? amount.indexOf(".") : amount.length;
                if (leng < 4)
                    return amount;
                var result = amount.substring(leng);
                var num = parseInt(amount.substring(0, leng));
                var times = Math.floor(leng / 3);
                for (var i = 0; i < times; i++) {
                    var block = num % 1000;
                    if (i > 0)
                        result = padLeft("" + block, "0", 3) + "," + result;
                    else
                        result = padLeft("" + block, "0", 3) + result;
                    num = Math.floor(num / 1000);
                }
                result = num % 1000 + "," + result;
                return result;
            }
            function formatCurrency(amount, locale) {
                var result = addSeperation("" + amount);
                if (locale == 'en' || locale == 'EN')
                    return "￥" + result;
                return result + "円";
            }
            text_1.formatCurrency = formatCurrency;
            function reverseDirection(direction) {
                if (direction === "left")
                    return "right";
                else if (direction === "right")
                    return "left";
                else if (direction === "top")
                    return "bottom";
                else if (direction === "bottom")
                    return "top";
            }
            text_1.reverseDirection = reverseDirection;
            function getISOFormat(format) {
                if (format.toLowerCase() === "iso")
                    return "YYYY-MM-DD[T]HH:mm:ss.SSS[Z]";
                if (format.toLowerCase() === "date")
                    return "YYYY/MM/DD";
                if (format.toLowerCase() === "yearmonth")
                    return "YYYY/MM";
                if (format.toLowerCase() === "time")
                    return "HH:mm";
                if (format.toLowerCase() === "datetime")
                    return "YYYY/MM/DD HH:mm";
                format = format.replace(/y/g, "Y");
                return format;
            }
            text_1.getISOFormat = getISOFormat;
            var StringFormatter = (function () {
                function StringFormatter(args) {
                    this.args = args;
                }
                StringFormatter.prototype.format = function (source) {
                    var constraintName = this.args.constraintName;
                    if (constraintName === "EmployeeCode") {
                        var constraint = this.args.constraint;
                        var filldirection = this.args.editorOption.filldirection;
                        var fillcharacter = this.args.editorOption.fillcharacter;
                        var length = (constraint && constraint.maxLength) ? constraint.maxLength : 10;
                        return formatEmployeeCode(source, filldirection, fillcharacter, length);
                    }
                    return source;
                };
                return StringFormatter;
            }());
            text_1.StringFormatter = StringFormatter;
            var NumberFormatter = (function () {
                function NumberFormatter(option) {
                    this.option = option;
                }
                NumberFormatter.prototype.format = function (source) {
                    return uk.ntsNumber.formatNumber(source, this.option.option);
                };
                return NumberFormatter;
            }());
            text_1.NumberFormatter = NumberFormatter;
            var TimeFormatter = (function () {
                function TimeFormatter(option) {
                    this.option = option;
                }
                TimeFormatter.prototype.format = function (source) {
                    var result;
                    if (this.option.inputFormat === "yearmonth") {
                        result = uk.time.parseYearMonth(source);
                    }
                    else if (this.option.inputFormat === "time") {
                        result = uk.time.parseTime(source, true);
                    }
                    else {
                        result = moment(source, "YYYYMMDD");
                        if (result.isValid()) {
                            var format = getISOFormat(this.option.inputFormat);
                            return result.format(format);
                        }
                        return source;
                    }
                    if (result.success)
                        return result.format();
                    return source;
                };
                return TimeFormatter;
            }());
            text_1.TimeFormatter = TimeFormatter;
        })(text = uk.text || (uk.text = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
