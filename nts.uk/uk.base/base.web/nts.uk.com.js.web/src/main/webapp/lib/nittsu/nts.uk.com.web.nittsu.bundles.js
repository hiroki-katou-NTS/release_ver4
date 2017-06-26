var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ntsNumber;
        (function (ntsNumber) {
            function isInteger(value, option) {
                if (option !== undefined && option.groupseperator() !== undefined) {
                    value = isInteger(value) ? value : uk.text.replaceAll(value.toString(), option.groupseperator(), '');
                }
                return !isNaN(value) && parseInt(value) == value && !isNaN(parseInt(value, 10));
            }
            function isDecimal(value, option) {
                if (option !== undefined) {
                    var seperator = typeof option.groupseperator === 'function' ? option.groupseperator() : option.groupseperator;
                    value = isDecimal(value) || seperator === undefined ? value : uk.text.replaceAll(value.toString(), seperator, '');
                }
                return !isNaN(value) && parseFloat(value) == value && !isNaN(parseFloat(value));
            }
            function isNumber(value, isDecimalValue, option, message) {
                if (isDecimalValue) {
                    if (message !== undefined)
                        message.id = 'FND_E_REALNUMBER';
                    return isDecimal(value, option);
                }
                else {
                    if (message !== undefined)
                        message.id = 'FND_E_INTEGER';
                    return isInteger(value, option);
                }
            }
            ntsNumber.isNumber = isNumber;
            function isHalfInt(value, message) {
                var val = parseFloat(value);
                if (message !== undefined)
                    message.id = 'FND_E_HALFINT';
                if (val !== NaN && (val * 2) % 1 === 0)
                    return true;
                return false;
            }
            ntsNumber.isHalfInt = isHalfInt;
            ntsNumber.trunc = (typeof Math.trunc === 'function') ? Math.trunc : function (value) { return value > 0 ? Math.floor(value) : Math.ceil(value); };
            function getDecimal(value, scale) {
                var scaleX = Math.pow(10, scale);
                return ntsNumber.trunc(value * scaleX) / scaleX;
            }
            ntsNumber.getDecimal = getDecimal;
            function formatNumber(value, formatOption) {
                if (value === undefined || value === null || value.toString().trim().lenth <= 0) {
                    return value;
                }
                var groupSeperator = formatOption.groupseperator ? formatOption.groupseperator : ',';
                var groupLength = formatOption.grouplength ? formatOption.grouplength : 0;
                var decimalSeperator = formatOption.decimalseperator ? formatOption.decimalseperator : ".";
                var decimalLength = formatOption.decimallength ? formatOption.decimallength : 0;
                var formattedValue = "";
                var stringValue = uk.text.replaceAll(value.toString(), groupSeperator, '');
                var isMinus = stringValue.charAt(0) === '-';
                var values = isMinus ? stringValue.split('-')[1].split(decimalSeperator) : stringValue.split(decimalSeperator);
                if (groupLength > 0) {
                    var x = values[0].split('').reverse().join('');
                    for (var i = 0; i < x.length;) {
                        formattedValue += x.substr(i, groupLength) + (x.length > i + groupLength ? groupSeperator : "");
                        i += groupLength;
                    }
                    formattedValue = formattedValue.split('').reverse().join('');
                }
                else {
                    formattedValue = values[0];
                }
                if (values[1] === undefined || decimalLength > values[1].length) {
                    values[1] = uk.text.padRight(values[1] ? values[1] : "", '0', values[1] ? decimalLength : decimalLength + 1);
                }
                else {
                    values[1] = values[1].substr(0, decimalLength);
                }
                values[1] = uk.text.splitOrPadRight(values[1], decimalLength, '0');
                return (isMinus ? '-' : '') + formattedValue + (decimalLength <= 0 ? '' : decimalSeperator + values[1]);
            }
            ntsNumber.formatNumber = formatNumber;
        })(ntsNumber = uk.ntsNumber || (uk.ntsNumber = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var KeyCodes;
        (function (KeyCodes) {
            KeyCodes.Tab = 9;
        })(KeyCodes = uk.KeyCodes || (uk.KeyCodes = {}));
        var util;
        (function (util) {
            function alwaysTrue() {
                return true;
            }
            util.alwaysTrue = alwaysTrue;
            function findIndex(arr, value, key) {
                for (var i = 0; i < arr.length; i++) {
                    var item = arr[i];
                    if (item[key] === value)
                        return i;
                }
                return -1;
            }
            util.findIndex = findIndex;
            function addToArray(node, arr) {
                arr.push(node);
            }
            function visitDfs(node, func, childField, arr) {
                if (func) {
                    if (arr)
                        func(node, arr);
                    else
                        func(node);
                }
                var childs = node[childField];
                $.each(childs, function (child) {
                    visitDfs(childs[child], func, childField, arr);
                });
            }
            util.visitDfs = visitDfs;
            function flatArray(arr, childField) {
                var flatArr = [];
                if (!childField)
                    return arr;
                for (var i = 0; i < arr.length; i++) {
                    var item = arr[i];
                    visitDfs(item, addToArray, childField, flatArr);
                }
                return flatArr;
            }
            util.flatArray = flatArray;
            function searchArray(arr, searchTerm, fields, childField) {
                if (!arr) {
                    return [];
                }
                if (!(searchTerm instanceof String)) {
                    searchTerm = "" + searchTerm;
                }
                var flatArr = flatArray(arr, childField);
                var filter = searchTerm.toLowerCase();
                if (!filter) {
                    return flatArr;
                }
                var filtered = flatArr.filter(function (item) {
                    var i = fields.length;
                    while (i--) {
                        var prop = fields[i];
                        var strProp = ("" + item[prop]).toLocaleLowerCase();
                        if (strProp.indexOf(filter) !== -1) {
                            return true;
                        }
                        ;
                    }
                    return false;
                });
                return filtered;
            }
            util.searchArray = searchArray;
            function nextSelectionSearch(selected, arr, selectedKey, isArray) {
                var current = null;
                if (isArray) {
                    if (selected.length > 0)
                        current = selected[0];
                }
                else if (selected !== undefined && selected !== '' && selected !== null) {
                    current = selected;
                }
                if (arr.length > 0) {
                    if (current) {
                        for (var i = 0; i < arr.length - 1; i++) {
                            var item = arr[i];
                            if (item[selectedKey] === current)
                                return arr[i + 1][selectedKey];
                        }
                    }
                    if (selectedKey)
                        return arr[0][selectedKey];
                    return arr[0];
                }
                return undefined;
            }
            util.nextSelectionSearch = nextSelectionSearch;
            function isNullOrUndefined(target) {
                return target === null || target === undefined;
            }
            util.isNullOrUndefined = isNullOrUndefined;
            function isNullOrEmpty(target) {
                return (target === undefined || target === null || target.length == 0);
            }
            util.isNullOrEmpty = isNullOrEmpty;
            function randomId() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = Math.random() * 16 | 0;
                    return ((c == 'x') ? r : (r & 0x3 | 0x8)).toString(16);
                });
            }
            util.randomId = randomId;
            function isInFrame() {
                return window.parent != window;
            }
            util.isInFrame = isInFrame;
            function orDefault(valueMaybeEmpty, defaultValue) {
                return isNullOrUndefined(valueMaybeEmpty) ? defaultValue : valueMaybeEmpty;
            }
            util.orDefault = orDefault;
            function isIn(actual, expects) {
                for (var i = 0; i < expects.length; i++) {
                    if (actual === expects[i])
                        return true;
                }
                return false;
            }
            util.isIn = isIn;
            ;
            function createTreeFromString(original, openChar, closeChar, seperatorChar, operatorChar) {
                var result = convertToTree(original, openChar, closeChar, seperatorChar, 1, operatorChar).result;
                return result;
            }
            util.createTreeFromString = createTreeFromString;
            function moveToParentIfEmpty(tree) {
                var result = [];
                _.forEach(tree, function (e) {
                    if (e.children.length > 0) {
                        e.children = moveToParentIfEmpty(e.children);
                        if (uk.text.isNullOrEmpty(e.value)) {
                            result = result.concat(e.children);
                        }
                        else {
                            result.push(e);
                        }
                    }
                    else {
                        result.push(e);
                    }
                });
                return result;
            }
            function convertToTree(original, openChar, closeChar, separatorChar, index, operatorChar) {
                var result = [];
                while (original.trim().length > 0) {
                    var firstOpenIndex = original.indexOf(openChar);
                    if (firstOpenIndex < 0) {
                        var values = original.split(separatorChar);
                        _.forEach(values, function (value) {
                            var data = splitByArray(value, operatorChar.slice());
                            _.each(data, function (v) {
                                var object = new TreeObject();
                                object.value = v;
                                object.children = [];
                                object.isOperator = operatorChar.indexOf(v) >= 0;
                                result.push(object);
                            });
                        });
                        return {
                            "result": result,
                            "index": index
                        };
                    }
                    else {
                        var object = new TreeObject();
                        object.value = original.substring(0, firstOpenIndex).trim();
                        object.index = index;
                        var closeIndex = findIndexOfCloseChar(original, openChar, closeChar, firstOpenIndex);
                        if (closeIndex >= 0) {
                            index++;
                            var res = convertToTree(original.substring(firstOpenIndex + 1, closeIndex).trim(), openChar, closeChar, separatorChar, index, operatorChar);
                            object.children = res.result;
                            index = res.index++;
                            result.push(object);
                            var firstSeperatorIndex = original.indexOf(separatorChar, closeIndex);
                            if (firstSeperatorIndex >= 0) {
                                original = original.substring(firstSeperatorIndex + 1, original.length).trim();
                            }
                            else {
                                return {
                                    "result": result,
                                    "index": index
                                };
                            }
                        }
                        else {
                            return {
                                "result": result,
                                "index": index
                            };
                        }
                    }
                }
                return {
                    "result": result,
                    "index": index
                };
            }
            function splitByArray(original, operatorChar) {
                var temp = [];
                var result = [];
                if (original.trim().length <= 0) {
                    return temp;
                }
                if (operatorChar.length <= 0) {
                    return [original];
                }
                var operator = operatorChar.shift();
                while (original.trim().length > 0) {
                    var index = original.indexOf(operator);
                    if (index >= 0) {
                        temp.push(original.substring(0, index).trim());
                        temp.push(original.substring(index, index + 1).trim());
                        original = original.substring(index + 1, original.length).trim();
                    }
                    else {
                        temp.push(original);
                        break;
                    }
                }
                _.each(temp, function (value) {
                    result = result.concat(splitByArray(value, operatorChar));
                });
                return result;
            }
            function findIndexOfCloseChar(original, openChar, closeChar, firstOpenIndex) {
                var openCount = 0;
                var closeCount = 0;
                for (var i = firstOpenIndex; i < original.length; i++) {
                    if (original.charAt(i) === openChar) {
                        openCount++;
                    }
                    else if (original.charAt(i) === closeChar) {
                        closeCount++;
                    }
                    if (openCount > 0 && openCount === closeCount) {
                        return i;
                    }
                }
                return -1;
            }
            var TreeObject = (function () {
                function TreeObject(value, children, index, isOperator) {
                    var self = this;
                    self.value = value;
                    self.children = children;
                    self.index = index;
                    self.isOperator = isOperator;
                }
                return TreeObject;
            }());
            util.TreeObject = TreeObject;
            var optional;
            (function (optional) {
                function of(value) {
                    return new Optional(value);
                }
                optional.of = of;
                function empty() {
                    return new Optional(null);
                }
                optional.empty = empty;
                var Optional = (function () {
                    function Optional(value) {
                        this.value = orDefault(value, null);
                    }
                    Optional.prototype.ifPresent = function (consumer) {
                        if (this.isPresent()) {
                            consumer(this.value);
                        }
                        return this;
                    };
                    Optional.prototype.ifEmpty = function (action) {
                        if (!this.isPresent()) {
                            action();
                        }
                        return this;
                    };
                    Optional.prototype.map = function (mapper) {
                        return this.isPresent() ? of(mapper(this.value)) : empty();
                    };
                    Optional.prototype.isPresent = function () {
                        return this.value !== null;
                    };
                    Optional.prototype.get = function () {
                        if (!this.isPresent()) {
                            throw new Error('not present');
                        }
                        return this.value;
                    };
                    Optional.prototype.orElse = function (stead) {
                        return this.isPresent() ? this.value : stead;
                    };
                    Optional.prototype.orElseThrow = function (errorBuilder) {
                        if (!this.isPresent()) {
                            throw errorBuilder();
                        }
                    };
                    return Optional;
                }());
                optional.Optional = Optional;
            })(optional = util.optional || (util.optional = {}));
            var Range = (function () {
                function Range(start, end) {
                    if (start > end) {
                        throw new Error('start is larger than end');
                    }
                    this.start = start;
                    this.end = end;
                }
                Range.prototype.contains = function (value) {
                    return this.start <= value && value <= this.end;
                };
                Range.prototype.greaterThan = function (value) {
                    return value < this.start;
                };
                Range.prototype.greaterThanOrEqualTo = function (value) {
                    return value <= this.start;
                };
                Range.prototype.lessThan = function (value) {
                    return this.end < value;
                };
                Range.prototype.lessThanOrEqualTo = function (value) {
                    return this.end <= value;
                };
                Range.prototype.distanceFrom = function (value) {
                    if (this.greaterThan(value)) {
                        return value - this.start;
                    }
                    else if (this.lessThan(value)) {
                        return value - this.end;
                    }
                    else {
                        return 0;
                    }
                };
                return Range;
            }());
            util.Range = Range;
            var value;
            (function (value) {
                function reset($controls, defaultVal, immediateApply) {
                    var resetEvent = new CustomEvent(DefaultValue.RESET_EVT, {
                        detail: {
                            value: defaultVal,
                            immediateApply: immediateApply === undefined ? true : immediateApply
                        }
                    });
                    _.forEach($controls, function (control) {
                        control.dispatchEvent(resetEvent);
                    });
                }
                value.reset = reset;
                var DefaultValue = (function () {
                    function DefaultValue() {
                    }
                    DefaultValue.prototype.onReset = function ($control, koValue) {
                        var self = this;
                        $control.addClass("reset-element");
                        $control.on(DefaultValue.RESET_EVT, function (e) {
                            var param = e.detail;
                            self.asDefault($(this), koValue, param.value, param.immediateApply);
                        });
                        return this;
                    };
                    DefaultValue.prototype.applyReset = function ($control, koValue) {
                        var defaultVal = _.cloneDeep($control.data("default"));
                        var isDirty = defaultVal !== koValue();
                        if ($control.ntsError("hasError"))
                            $control.ntsError("clear");
                        if (defaultVal !== undefined && isDirty)
                            setTimeout(function () { return koValue(defaultVal); }, 0);
                        return { isDirty: isDirty };
                    };
                    DefaultValue.prototype.asDefault = function ($control, koValue, defaultValue, immediateApply) {
                        var defaultVal = defaultValue !== undefined ? defaultValue : koValue();
                        $control.data("default", defaultVal);
                        if (immediateApply)
                            this.applyReset($control, koValue);
                    };
                    DefaultValue.RESET_EVT = "reset";
                    return DefaultValue;
                }());
                value.DefaultValue = DefaultValue;
            })(value = util.value || (util.value = {}));
        })(util = uk.util || (uk.util = {}));
        var WebStorageWrapper = (function () {
            function WebStorageWrapper(nativeStorage) {
                this.nativeStorage = nativeStorage;
            }
            WebStorageWrapper.prototype.setItem = function (key, value) {
                if (value === undefined) {
                    return;
                }
                this.nativeStorage.setItem(key, value);
            };
            WebStorageWrapper.prototype.setItemAsJson = function (key, value) {
                this.setItem(key, JSON.stringify(value));
            };
            WebStorageWrapper.prototype.containsKey = function (key) {
                return this.getItem(key) !== null;
            };
            ;
            WebStorageWrapper.prototype.getItem = function (key) {
                var value = this.nativeStorage.getItem(key);
                if (value === null || value === undefined || value === 'undefined') {
                    return util.optional.empty();
                }
                return util.optional.of(value);
            };
            WebStorageWrapper.prototype.getItemAndRemove = function (key) {
                var item = this.getItem(key);
                this.removeItem(key);
                return item;
            };
            WebStorageWrapper.prototype.removeItem = function (key) {
                this.nativeStorage.removeItem(key);
            };
            WebStorageWrapper.prototype.clear = function () {
                this.nativeStorage.clear();
            };
            return WebStorageWrapper;
        }());
        uk.WebStorageWrapper = WebStorageWrapper;
        var deferred;
        (function (deferred) {
            function repeat(configurator) {
                var conf = repeater.createConfiguration();
                configurator(conf);
                return repeater.begin(conf);
            }
            deferred.repeat = repeat;
            var repeater;
            (function (repeater) {
                function begin(conf) {
                    return conf.run();
                }
                repeater.begin = begin;
                function createConfiguration() {
                    return new Configuration();
                }
                repeater.createConfiguration = createConfiguration;
                var Configuration = (function () {
                    function Configuration() {
                        this.pauseMilliseconds = 0;
                        this.runAfter = 0;
                    }
                    Configuration.prototype.task = function (taskFunction) {
                        this.taskFunction = taskFunction;
                        return this;
                    };
                    Configuration.prototype.while = function (whileCondition) {
                        this.whileCondition = whileCondition;
                        return this;
                    };
                    Configuration.prototype.pause = function (pauseMilliseconds) {
                        this.pauseMilliseconds = pauseMilliseconds;
                        return this;
                    };
                    Configuration.prototype.after = function (runAfterMilliseconds) {
                        this.runAfter = runAfterMilliseconds;
                        return this;
                    };
                    Configuration.prototype.run = function () {
                        var _this = this;
                        var dfd = $.Deferred();
                        if (this.runAfter > 0) {
                            setTimeout(function () { return _this.repeat(dfd); }, this.runAfter);
                        }
                        else {
                            this.repeat(dfd);
                        }
                        return dfd.promise();
                    };
                    Configuration.prototype.repeat = function (dfd) {
                        var _this = this;
                        this.taskFunction().done(function (res) {
                            if (_this.whileCondition(res)) {
                                setTimeout(function () { return _this.repeat(dfd); }, _this.pauseMilliseconds);
                            }
                            else {
                                dfd.resolve(res);
                            }
                        }).fail(function (res) {
                            dfd.reject(res);
                        });
                    };
                    return Configuration;
                }());
            })(repeater = deferred.repeater || (deferred.repeater = {}));
        })(deferred = uk.deferred || (uk.deferred = {}));
        var resource;
        (function (resource) {
            function getText(code, params) {
                var text = names[code];
                if (text) {
                    text = formatCompCustomizeResource(text);
                    text = formatParams(text, params);
                    return text;
                }
                return code;
            }
            resource.getText = getText;
            function getMessage(messageId, params) {
                var message = messages[messageId];
                if (!message) {
                    return messageId;
                }
                message = formatParams(message, params);
                message = formatCompCustomizeResource(message);
                return message;
            }
            resource.getMessage = getMessage;
            function formatCompCustomizeResource(message) {
                var compDependceParamRegex = /{#(\w*)}/;
                var matches;
                while (matches = compDependceParamRegex.exec(message)) {
                    var code = matches[1];
                    var text_1 = getText(code);
                    message = message.replace(compDependceParamRegex, text_1);
                }
                return message;
            }
            function formatParams(message, args) {
                if (args == null || args.length == 0)
                    return message;
                var paramRegex = /{([0-9])+(:\w+)?}/;
                var matches;
                var formatter = uk.time.getFormatter();
                while (matches = paramRegex.exec(message)) {
                    var code = matches[1];
                    var text_2 = args[parseInt(code)];
                    var param = matches[2];
                    if (param !== undefined && formatter !== undefined) {
                        text_2 = uk.time.applyFormat(param.substring(1), text_2, formatter);
                    }
                    message = message.replace(paramRegex, text_2);
                }
                return message;
            }
            function getControlName(name) {
                var hashIdx = name.indexOf("#");
                if (hashIdx !== 0)
                    return name;
                var names = name.substring(hashIdx + 2, name.length - 1).split(",");
                if (names.length > 1) {
                    var params_1 = new Array();
                    _.forEach(names, function (n, idx) {
                        if (idx === 0)
                            return true;
                        params_1.push(getText(n.trim()));
                    });
                    return getText(names[0], params_1);
                }
                return getText(names[0]);
            }
            resource.getControlName = getControlName;
        })(resource = uk.resource || (uk.resource = {}));
        uk.sessionStorage = new WebStorageWrapper(window.sessionStorage);
        uk.localStorage = new WebStorageWrapper(window.localStorage);
        var characteristics;
        (function (characteristics) {
            var delayToEmulateAjax = 100;
            function save(key, value) {
                var dfd = $.Deferred();
                setTimeout(function () {
                    uk.localStorage.setItemAsJson(createKey(key), value);
                    dfd.resolve();
                }, delayToEmulateAjax);
                return dfd.promise();
            }
            characteristics.save = save;
            function restore(key) {
                var dfd = $.Deferred();
                setTimeout(function () {
                    var value = uk.localStorage.getItem(createKey(key))
                        .map(function (v) { return JSON.parse(v); }).orElse(undefined);
                    dfd.resolve(value);
                }, delayToEmulateAjax);
                return dfd.promise();
            }
            characteristics.restore = restore;
            function remove(key) {
                var dfd = $.Deferred();
                setTimeout(function () {
                    uk.localStorage.removeItem(createKey(key));
                    dfd.resolve();
                }, delayToEmulateAjax);
                return dfd.promise();
            }
            characteristics.remove = remove;
            function createKey(key) {
                return 'nts.uk.characteristics.' + key;
            }
        })(characteristics = uk.characteristics || (uk.characteristics = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
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
        (function (text_3) {
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
            text_3.countHalf = countHalf;
            function toOneByteAlphaNumberic(text) {
                return text.replace(/[！-～　]/g, function (s) {
                    if (s === "　") {
                        return String.fromCharCode(s.charCodeAt(0) - 12256);
                    }
                    return String.fromCharCode(s.charCodeAt(0) - 0xFEE0);
                });
            }
            text_3.toOneByteAlphaNumberic = toOneByteAlphaNumberic;
            function toTwoByteAlphaNumberic(text) {
                return text.replace(/[\!-\~ ]/g, function (s) {
                    if (s === " ") {
                        return String.fromCharCode(s.charCodeAt(0) + 12256);
                    }
                    return String.fromCharCode(s.charCodeAt(0) + 0xFEE0);
                });
            }
            text_3.toTwoByteAlphaNumberic = toTwoByteAlphaNumberic;
            function katakanaToHiragana(text) {
                text = text.replace(/[ァ-ヴ]/g, function (s) {
                    return String.fromCharCode(s.charCodeAt(0) - 0x60);
                }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜')
                    .replace(/(う゛)/g, 'ゔ').replace(/ヷ/g, 'わ゛')
                    .replace(/ヸ/g, 'ゐ゛').replace(/ヹ/g, 'ゑ゛')
                    .replace(/ヺ/g, 'を゛').replace(/(ヽ゛)/g, 'ゞ')
                    .replace(/ヽ/g, 'ゝ').replace(/ヾ/g, 'ゞ');
                return text;
            }
            text_3.katakanaToHiragana = katakanaToHiragana;
            function hiraganaToKatakana(text, opt) {
                text = text.replace(/[ぁ-ゔ]/g, function (s) {
                    return String.fromCharCode(s.charCodeAt(0) + 0x60);
                }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜')
                    .replace(/(ウ゛)/g, 'ヴ').replace(/(ワ゛)/g, 'ヷ')
                    .replace(/(ヰ゛)/g, 'ヸ').replace(/(ヱ゛)/g, 'ヹ')
                    .replace(/(ヲ゛)/g, 'ヺ').replace(/(ゝ゛)/g, 'ヾ')
                    .replace(/ゝ/g, 'ヽ').replace(/ゞ/g, 'ヾ');
                if (opt !== false) {
                    text = text.replace(/ゕ/g, 'ヵ').replace(/ゖ/g, 'ヶ');
                }
                return text;
            }
            text_3.hiraganaToKatakana = hiraganaToKatakana;
            function oneByteKatakanaToTwoByte(text) {
                var katakanaMap = {
                    'ｶﾞ': 'ガ', 'ｷﾞ': 'ギ', 'ｸﾞ': 'グ', 'ｹﾞ': 'ゲ', 'ｺﾞ': 'ゴ',
                    'ｻﾞ': 'ザ', 'ｼﾞ': 'ジ', 'ｽﾞ': 'ズ', 'ｾﾞ': 'ゼ', 'ｿﾞ': 'ゾ',
                    'ﾀﾞ': 'ダ', 'ﾁﾞ': 'ヂ', 'ﾂﾞ': 'ヅ', 'ﾃﾞ': 'デ', 'ﾄﾞ': 'ド',
                    'ﾊﾞ': 'バ', 'ﾋﾞ': 'ビ', 'ﾌﾞ': 'ブ', 'ﾍﾞ': 'ベ', 'ﾎﾞ': 'ボ',
                    'ﾊﾟ': 'パ', 'ﾋﾟ': 'ピ', 'ﾌﾟ': 'プ', 'ﾍﾟ': 'ペ', 'ﾎﾟ': 'ポ',
                    'ｳﾞ': 'ヴ', 'ﾜﾞ': 'ヷ', 'ｦﾞ': 'ヺ',
                    'ｱ': 'ア', 'ｲ': 'イ', 'ｳ': 'ウ', 'ｴ': 'エ', 'ｵ': 'オ',
                    'ｶ': 'カ', 'ｷ': 'キ', 'ｸ': 'ク', 'ｹ': 'ケ', 'ｺ': 'コ',
                    'ｻ': 'サ', 'ｼ': 'シ', 'ｽ': 'ス', 'ｾ': 'セ', 'ｿ': 'ソ',
                    'ﾀ': 'タ', 'ﾁ': 'チ', 'ﾂ': 'ツ', 'ﾃ': 'テ', 'ﾄ': 'ト',
                    'ﾅ': 'ナ', 'ﾆ': 'ニ', 'ﾇ': 'ヌ', 'ﾈ': 'ネ', 'ﾉ': 'ノ',
                    'ﾊ': 'ハ', 'ﾋ': 'ヒ', 'ﾌ': 'フ', 'ﾍ': 'ヘ', 'ﾎ': 'ホ',
                    'ﾏ': 'マ', 'ﾐ': 'ミ', 'ﾑ': 'ム', 'ﾒ': 'メ', 'ﾓ': 'モ',
                    'ﾔ': 'ヤ', 'ﾕ': 'ユ', 'ﾖ': 'ヨ',
                    'ﾗ': 'ラ', 'ﾘ': 'リ', 'ﾙ': 'ル', 'ﾚ': 'レ', 'ﾛ': 'ロ',
                    'ﾜ': 'ワ', 'ｦ': 'ヲ', 'ﾝ': 'ン',
                    'ｧ': 'ァ', 'ｨ': 'ィ', 'ｩ': 'ゥ', 'ｪ': 'ェ', 'ｫ': 'ォ',
                    'ｯ': 'ッ', 'ｬ': 'ャ', 'ｭ': 'ュ', 'ｮ': 'ョ',
                    '｡': '。', '､': '、', 'ｰ': 'ー', '｢': '「', '｣': '」', '･': '・'
                };
                var expression = new RegExp('(' + Object.keys(katakanaMap).join('|') + ')', 'g');
                return text.replace(expression, function (match) {
                    return katakanaMap[match];
                }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜');
            }
            text_3.oneByteKatakanaToTwoByte = oneByteKatakanaToTwoByte;
            function anyChar(text) {
                return {
                    probe: true,
                    messageId: 'FND_E_ANY'
                };
            }
            text_3.anyChar = anyChar;
            function allHalfNumeric(text) {
                return {
                    probe: regexp.allHalfNumeric.test(text),
                    messageId: 'FND_E_NUMERIC'
                };
            }
            text_3.allHalfNumeric = allHalfNumeric;
            function allHalfAlphabet(text) {
                return {
                    probe: regexp.allHalfAlphabet.test(text),
                    messageId: 'NO_MESSAGE'
                };
            }
            text_3.allHalfAlphabet = allHalfAlphabet;
            function allHalfAlphanumeric(text) {
                return {
                    probe: regexp.allHalfAlphanumeric.test(text),
                    messageId: 'FND_E_ALPHANUMERIC'
                };
            }
            text_3.allHalfAlphanumeric = allHalfAlphanumeric;
            function allHalfKatakana(text) {
                return {
                    probe: regexp.allHalfKatakanaReg.test(text),
                    messageId: 'NO_MESSAGE'
                };
            }
            text_3.allHalfKatakana = allHalfKatakana;
            function allFullKatakana(text) {
                return {
                    probe: regexp.allFullKatakanaReg.test(text),
                    messageId: 'FND_E_KANA'
                };
            }
            text_3.allFullKatakana = allFullKatakana;
            function allHalf(text) {
                return {
                    probe: text.length === countHalf(text),
                    messageId: 'FND_E_ANYHALFWIDTH'
                };
            }
            text_3.allHalf = allHalf;
            function allHiragana(text) {
                return {
                    probe: regexp.allHiragana.test(text),
                    messageId: 'NO_MESSAGE'
                };
            }
            text_3.allHiragana = allHiragana;
            function allKatakana(text) {
                return {
                    probe: regexp.allFullKatakanaReg.test(text),
                    messageId: 'NO_MESSAGE'
                };
            }
            text_3.allKatakana = allKatakana;
            function halfInt(text) {
                var val = parseFloat(text);
                var probe = false;
                if (val !== NaN && (val * 2) % 1 === 0)
                    probe = true;
                return {
                    probe: probe,
                    messageId: 'FND_E_HALFINT'
                };
            }
            text_3.halfInt = halfInt;
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
            text_3.htmlEncode = htmlEncode;
            function toLowerCaseFirst(text) {
                return text.charAt(0).toLowerCase() + text.slice(1);
            }
            text_3.toLowerCaseFirst = toLowerCaseFirst;
            ;
            function toUpperCaseFirst(text) {
                return text.charAt(0).toUpperCase() + text.slice(1);
            }
            text_3.toUpperCaseFirst = toUpperCaseFirst;
            function toUpperCase(text) {
                return text.replace(/[a-z]/g, function (c) {
                    return String.fromCharCode(c.charCodeAt(0) - 0x20);
                });
            }
            text_3.toUpperCase = toUpperCase;
            function isNullOrEmpty(text) {
                var result = true;
                if (text !== null && text !== undefined) {
                    var convertValue = String(text);
                    result = convertValue.length === 0;
                }
                return result;
            }
            text_3.isNullOrEmpty = isNullOrEmpty;
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
            text_3.format = format;
            function padLeft(text, paddingChar, length) {
                return charPadding(text, paddingChar, true, length);
            }
            text_3.padLeft = padLeft;
            function padRight(text, paddingChar, length) {
                return charPadding(text, paddingChar, false, length);
            }
            text_3.padRight = padRight;
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
            text_3.charPadding = charPadding;
            function replaceAll(originalString, find, replace) {
                return originalString.split(find).join(replace);
            }
            text_3.replaceAll = replaceAll;
            function removeFromStart(originalString, charSet) {
                if (originalString.length === charSet.length) {
                    return (originalString === charSet) ? "" : originalString;
                }
                var i = findLastContinousIndex(originalString, charSet, 0);
                return originalString.substr(i, originalString.length - i);
            }
            text_3.removeFromStart = removeFromStart;
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
                    var validateResult = this.validator(text);
                    if (validateResult === true || validateResult.probe) {
                        result.isValid = true;
                        result.errorMessage = validateResult.messageId;
                    }
                    else {
                        result.fail(validateResult.messageId);
                    }
                    return result;
                };
                CharType.prototype.buildConstraintText = function (maxLength) {
                    return this.viewName + this.getViewLength(maxLength) + '文字';
                };
                CharType.prototype.getViewLength = function (length) {
                    return Math.floor(length / (this.width * 2));
                };
                return CharType;
            }());
            text_3.CharType = CharType;
            var charTypes = {
                AnyHalfWidth: new CharType('半角', 0.5, nts.uk.text.allHalf),
                AlphaNumeric: new CharType('半角英数字', 0.5, nts.uk.text.allHalfAlphanumeric),
                Alphabet: new CharType('半角英字', 0.5, nts.uk.text.allHalfAlphabet),
                Numeric: new CharType('半角数字', 0.5, nts.uk.text.allHalfNumeric),
                Any: new CharType('全角', 1, nts.uk.text.anyChar),
                Kana: new CharType('カナ', 1, nts.uk.text.allFullKatakana),
                HalfInt: new CharType('半整数', 0.5, nts.uk.text.halfInt)
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
            text_3.getCharType = getCharType;
            function formatCode(code, filldirection, fillcharacter, length) {
                if (filldirection === "left")
                    return padLeft(code, fillcharacter, length);
                else
                    return padRight(code, fillcharacter, length);
            }
            text_3.formatCode = formatCode;
            function splitOrPadRight(originalString, length, char) {
                if (originalString === undefined || length > originalString.length) {
                    originalString = text.padRight(originalString ? originalString : "", char ? char : " ", length);
                }
                else {
                    originalString = originalString.substr(0, length);
                }
                return originalString;
            }
            text_3.splitOrPadRight = splitOrPadRight;
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
            text_3.formatCurrency = formatCurrency;
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
            text_3.reverseDirection = reverseDirection;
            function getISOFormat(format) {
                format = uk.util.orDefault(format, "ISO");
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
            text_3.getISOFormat = getISOFormat;
            var StringFormatter = (function () {
                function StringFormatter(args) {
                    this.args = args;
                }
                StringFormatter.prototype.format = function (source) {
                    var constraintName = this.args.constraintName;
                    var autofill = this.args.editorOption.autofill;
                    if (!uk.util.isNullOrEmpty(source)) {
                        if (autofill === true || constraintName === "EmployeeCode") {
                            var constraint = this.args.constraint;
                            var filldirection = this.args.editorOption.filldirection;
                            var fillcharacter = this.args.editorOption.fillcharacter;
                            var length = (constraint && constraint.maxLength) ? constraint.maxLength : 10;
                            return formatCode(source, filldirection, fillcharacter, length);
                        }
                    }
                    return source;
                };
                return StringFormatter;
            }());
            text_3.StringFormatter = StringFormatter;
            var NumberFormatter = (function () {
                function NumberFormatter(option) {
                    this.option = option;
                }
                NumberFormatter.prototype.format = function (source) {
                    return nts.uk.util.isNullOrEmpty(source) ? (!nts.uk.util.isNullOrEmpty(this.option.option.defaultValue)
                        ? this.option.option.defaultValue : source)
                        : uk.ntsNumber.formatNumber(source, this.option.option);
                };
                return NumberFormatter;
            }());
            text_3.NumberFormatter = NumberFormatter;
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
            text_3.TimeFormatter = TimeFormatter;
        })(text = uk.text || (uk.text = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var time;
        (function (time_1) {
            var defaultInputFormat = ["YYYY/MM/DD", "YYYY-MM-DD", "YYYYMMDD", "YYYY/MM", "YYYY-MM", "YYYYMM", "H:mm", "Hmm", "YYYY"];
            var listEmpire = {
                "明治": "1868/01/01",
                "大正": "1912/07/30",
                "昭和": "1926/12/25",
                "平成": "1989/01/08"
            };
            var dotW = ["日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"];
            function getYearMonthJapan(year, month) {
                if (month)
                    return year + "年 " + month + " 月";
                return year;
            }
            var JapanYearMonth = (function () {
                function JapanYearMonth(empire, year, month) {
                    this.empire = empire;
                    this.year = year;
                    this.month = month;
                }
                JapanYearMonth.prototype.getEmpire = function () {
                    return this.empire;
                };
                JapanYearMonth.prototype.getYear = function () {
                    return this.year;
                };
                JapanYearMonth.prototype.getMonth = function () {
                    return this.month;
                };
                JapanYearMonth.prototype.toString = function () {
                    return (this.empire === undefined ? "" : this.empire + " ")
                        + (this.year === undefined ? "" : this.year + " 年 ")
                        + (this.month === undefined ? "" : this.month + " 月");
                };
                return JapanYearMonth;
            }());
            time_1.JapanYearMonth = JapanYearMonth;
            function yearInJapanEmpire(date) {
                var year = moment.utc(date, defaultInputFormat, true).year();
                if (year == 1868) {
                    return new JapanYearMonth("明治元年");
                }
                if (year <= 1912) {
                    var diff = year - 1867;
                    return new JapanYearMonth("明治 ", diff);
                }
                if (year <= 1926) {
                    var diff = year - 1911;
                    return new JapanYearMonth("大正 ", diff);
                }
                if (year < 1989) {
                    var diff = year - 1925;
                    return new JapanYearMonth("昭和 ", diff);
                }
                if (year == 1989) {
                    return new JapanYearMonth("平成元年 ", diff);
                }
                var diff = year - 1988;
                return new JapanYearMonth("平成 ", diff);
            }
            time_1.yearInJapanEmpire = yearInJapanEmpire;
            function yearmonthInJapanEmpire(yearmonth) {
                if (!(yearmonth instanceof String)) {
                    yearmonth = "" + yearmonth;
                }
                var nguyennien = "元年";
                yearmonth = yearmonth.replace("/", "");
                var year = parseInt(yearmonth.substring(0, 4));
                var month = parseInt(yearmonth.substring(4));
                if (year == 1868) {
                    return new JapanYearMonth("明治元年 ", undefined, month);
                }
                if (year < 1912) {
                    var diff = year - 1867;
                    return new JapanYearMonth("明治 ", diff, month);
                }
                if (year == 1912) {
                    if (month < 8)
                        return new JapanYearMonth("明治 ", 45, month);
                    return new JapanYearMonth("大正元年 ", undefined, month);
                }
                if (year < 1926) {
                    var diff = year - 1911;
                    return new JapanYearMonth("大正 ", diff, month);
                }
                if (year == 1926) {
                    if (month < 12)
                        return new JapanYearMonth("大正", 15, month);
                    return new JapanYearMonth("昭和元年 ", undefined, month);
                }
                if (year < 1989) {
                    var diff = year - 1925;
                    return new JapanYearMonth("昭和 ", diff, month);
                }
                if (year == 1989) {
                    return new JapanYearMonth("平成元年 ", undefined, month);
                }
                var diff = year - 1988;
                return new JapanYearMonth("平成 ", diff, month);
            }
            time_1.yearmonthInJapanEmpire = yearmonthInJapanEmpire;
            var JapanDateMoment = (function () {
                function JapanDateMoment(date, outputFormat) {
                    var MomentResult = parseMoment(date, outputFormat);
                    var year = MomentResult.momentObject.year();
                    var month = MomentResult.momentObject.month() + 1;
                }
                JapanDateMoment.prototype.toString = function () {
                    return (this.empire === undefined ? "" : this.empire + " ")
                        + (this.year === undefined ? "" : this.year + " 年 ")
                        + (this.month === undefined ? "" : this.month + " 月")
                        + (this.day === undefined ? "" : this.day + " ");
                };
                return JapanDateMoment;
            }());
            time_1.JapanDateMoment = JapanDateMoment;
            function dateInJapanEmpire(date) {
                return new JapanDateMoment(date);
            }
            time_1.dateInJapanEmpire = dateInJapanEmpire;
            function formatSeconds(seconds, formatOption) {
                seconds = parseInt(String(seconds));
                var ss = uk.text.padLeft(String(seconds % 60), '0', 2);
                var minutes = Math.floor(seconds / 60);
                var mm = uk.text.padLeft(String(minutes % 60), '0', 2);
                var hours = uk.ntsNumber.trunc(seconds / 60 / 60);
                var h = String(hours);
                return "h:mm:ss"
                    .replace(/h/g, h)
                    .replace(/mm/g, mm)
                    .replace(/ss/g, ss);
            }
            time_1.formatSeconds = formatSeconds;
            function formatDate(date, format) {
                if (!format)
                    format = 'yyyy-MM-dd hh:mm:ss.SSS';
                format = format.replace(/yyyy/g, date.getFullYear());
                format = format.replace(/yy/g, ('0' + (date.getFullYear() % 100)).slice(-2));
                format = format.replace(/MM/g, ('0' + (date.getMonth() + 1)).slice(-2));
                format = format.replace(/dd/g, ('0' + date.getDate()).slice(-2));
                if (format.indexOf("DDD") != -1) {
                    var daystr = "(" + dotW[date.getDay()] + ")";
                    format = format.replace("DDD", daystr);
                }
                else if (format.indexOf("D") != -1) {
                    var daystr = "(" + dotW[date.getDay()].substring(0, 1) + ")";
                    format = format.replace("D", daystr);
                }
                format = format.replace(/hh/g, ('0' + date.getHours()).slice(-2));
                format = format.replace(/mm/g, ('0' + date.getMinutes()).slice(-2));
                format = format.replace(/ss/g, ('0' + date.getSeconds()).slice(-2));
                if (format.match(/S/g)) {
                    var milliSeconds = ('00' + date.getMilliseconds()).slice(-3);
                    var length = format.match(/S/g).length;
                    for (var i = 0; i < length; i++)
                        format = format.replace(/S/, milliSeconds.substring(i, i + 1));
                }
                return format;
            }
            time_1.formatDate = formatDate;
            function formatYearMonth(yearMonth) {
                var result;
                var num = parseInt(String(yearMonth));
                var year = String(Math.floor(num / 100));
                var month = uk.text.charPadding(String(num % 100), '0', true, 2);
                result = year + '/' + month;
                return result;
            }
            time_1.formatYearMonth = formatYearMonth;
            function formatPattern(date, inputFormat, outputFormat) {
                outputFormat = uk.text.getISOFormat(outputFormat);
                var inputFormats = (inputFormat) ? inputFormat : defaultInputFormat;
                return moment.utc(date, inputFormats).format(outputFormat);
            }
            time_1.formatPattern = formatPattern;
            var ParseResult = (function () {
                function ParseResult(success) {
                    this.success = success;
                }
                return ParseResult;
            }());
            time_1.ParseResult = ParseResult;
            var ResultParseTime = (function (_super) {
                __extends(ResultParseTime, _super);
                function ResultParseTime(success, minus, hours, minutes, msg) {
                    _super.call(this, success);
                    this.minus = minus;
                    this.hours = hours;
                    this.minutes = minutes;
                    this.msg = msg || "invalid time format";
                }
                ResultParseTime.succeeded = function (minus, hours, minutes) {
                    return new ResultParseTime(true, minus, hours, minutes);
                };
                ResultParseTime.failed = function () {
                    return new ResultParseTime(false);
                };
                ResultParseTime.prototype.format = function () {
                    if (!this.success)
                        return "";
                    return (this.minus ? '-' : '') + this.hours + ':' + uk.text.padLeft(String(this.minutes), '0', 2);
                };
                ResultParseTime.prototype.toValue = function () {
                    if (!this.success)
                        return 0;
                    return (this.minus ? -1 : 1) * (this.hours * 60 + this.minutes);
                };
                ResultParseTime.prototype.getMsg = function () { return this.msg; };
                return ResultParseTime;
            }(ParseResult));
            time_1.ResultParseTime = ResultParseTime;
            function parseTime(time, isMinutes) {
                if (time === undefined || time === null) {
                    return ResultParseTime.failed();
                }
                if (isMinutes) {
                    var hoursX = uk.ntsNumber.trunc(time / 60);
                    time = hoursX + uk.text.padLeft((Math.abs(time - hoursX * 60)).toString(), '0', 2);
                }
                if (!(time instanceof String)) {
                    time = time.toString();
                }
                if (time.length < 1 || time.split(':').length > 2 || time.split('-').length > 2
                    || time.lastIndexOf('-') > 0 || (time.length == 1 && !uk.ntsNumber.isNumber(time.charAt(0)))) {
                    return ResultParseTime.failed();
                }
                var minusNumber = time.charAt(0) === '-';
                if (minusNumber) {
                    time = time.split('-')[1];
                }
                var minutes;
                var hours;
                if (time.indexOf(':') > -1) {
                    var times = time.split(':');
                    minutes = times[1];
                    hours = times[0];
                }
                else {
                    time = uk.ntsNumber.trunc(time);
                    time = uk.text.padLeft(time, "0", time.length > 4 ? time.length : 4);
                    minutes = time.substr(-2, 2);
                    hours = time.substr(0, time.length - 2);
                }
                if (!uk.ntsNumber.isNumber(minutes, false) || parseInt(minutes) > 59 || !uk.ntsNumber.isNumber(hours, false)) {
                    return ResultParseTime.failed();
                }
                return ResultParseTime.succeeded(minusNumber, parseInt(hours), parseInt(minutes));
            }
            time_1.parseTime = parseTime;
            var ResultParseYearMonth = (function (_super) {
                __extends(ResultParseYearMonth, _super);
                function ResultParseYearMonth(success, msg, year, month) {
                    _super.call(this, success);
                    this.year = year;
                    this.month = month;
                    this.msg = msg || "must yyyymm or yyyy/mm format: year in [1900-9999] and month in [1-12] ";
                }
                ResultParseYearMonth.succeeded = function (year, month) {
                    return new ResultParseYearMonth(true, "", year, month);
                };
                ResultParseYearMonth.failed = function (msg) {
                    return new ResultParseYearMonth(false, msg);
                };
                ResultParseYearMonth.prototype.format = function () {
                    if (!this.success) {
                        return "";
                    }
                    return this.year + '/' + uk.text.padLeft(String(this.month), '0', 2);
                };
                ResultParseYearMonth.prototype.toValue = function () {
                    if (!this.success) {
                        return 0;
                    }
                    return (this.year * 100 + this.month);
                };
                ResultParseYearMonth.prototype.getMsg = function () { return this.msg; };
                return ResultParseYearMonth;
            }(ParseResult));
            time_1.ResultParseYearMonth = ResultParseYearMonth;
            function parseYearMonth(yearMonth) {
                if (yearMonth === undefined || yearMonth === null) {
                    return ResultParseYearMonth.failed("yearmonth can not empty!");
                }
                if (!(yearMonth instanceof String)) {
                    yearMonth = yearMonth.toString();
                }
                var stringLengh = yearMonth.length;
                yearMonth = yearMonth.replace("/", "");
                yearMonth = yearMonth.replace("/", "");
                var checkNum = yearMonth.replace(/[0-9]/g, "");
                if (checkNum.length > 0)
                    return ResultParseYearMonth.failed("yearmonth must contain digits and slashes only!");
                if (yearMonth.length != 6 && yearMonth.length != 5)
                    return ResultParseYearMonth.failed("wrong yearmonth format: must be yyyy/mm or yyyymm");
                var year = parseInt(yearMonth.substring(0, 4));
                var month = parseInt(yearMonth.substring(4));
                if (year < 1900 || year > 9999)
                    return ResultParseYearMonth.failed("wrong year: year must in range 1900-9999");
                if (month < 1 || month > 12)
                    return ResultParseYearMonth.failed("wrong month: month must in range 1-12");
                return ResultParseYearMonth.succeeded(year, month);
            }
            time_1.parseYearMonth = parseYearMonth;
            var ResultParseTimeOfTheDay = (function (_super) {
                __extends(ResultParseTimeOfTheDay, _super);
                function ResultParseTimeOfTheDay(success, msg, hour, minute) {
                    _super.call(this, success);
                    this.hour = hour;
                    this.minute = minute;
                    this.msg = msg || "time of the days must in format hh:mm with hour in range 0-23; minute in range 00-59";
                }
                ResultParseTimeOfTheDay.succeeded = function (hour, minute) {
                    return new ResultParseTimeOfTheDay(true, "", hour, minute);
                };
                ResultParseTimeOfTheDay.failed = function (msg) {
                    return new ResultParseTimeOfTheDay(false, msg);
                };
                ResultParseTimeOfTheDay.prototype.format = function () {
                    if (!this.success) {
                        return "";
                    }
                    return this.hour + ':' + uk.text.padLeft(String(this.minute), '0', 2);
                };
                ResultParseTimeOfTheDay.prototype.toValue = function () {
                    if (!this.success) {
                        return 0;
                    }
                    return (this.hour * 100 + this.minute);
                };
                ResultParseTimeOfTheDay.prototype.getMsg = function () { return this.msg; };
                return ResultParseTimeOfTheDay;
            }(ParseResult));
            time_1.ResultParseTimeOfTheDay = ResultParseTimeOfTheDay;
            function parseTimeOfTheDay(timeOfDay) {
                if (timeOfDay === undefined || timeOfDay === null) {
                    return ResultParseTimeOfTheDay.failed("time of the day cannot be empty!");
                }
                if (!(timeOfDay instanceof String)) {
                    timeOfDay = timeOfDay.toString();
                }
                timeOfDay = timeOfDay.replace(":", "");
                var checkNum = timeOfDay.replace(/[0-9]/g, "");
                var stringLength = timeOfDay.length;
                if (checkNum.length > 0)
                    return ResultParseTimeOfTheDay.failed("time of the day accept digits and ':' only");
                if (stringLength < 3 || stringLength > 4)
                    return ResultParseTimeOfTheDay.failed("invalid time of the day format");
                var hour = parseInt(timeOfDay.substring(0, stringLength - 2));
                var minute = parseInt(timeOfDay.substring(stringLength - 2));
                if (hour < 0 || hour > 23)
                    return ResultParseTimeOfTheDay.failed("invalid: hour must in range 0-23");
                if (minute < 0 || minute > 59)
                    return ResultParseTimeOfTheDay.failed("invalid: minute must in range 0-59");
                return ResultParseTimeOfTheDay.succeeded(hour, minute);
            }
            time_1.parseTimeOfTheDay = parseTimeOfTheDay;
            var ResultParseYearMonthDate = (function (_super) {
                __extends(ResultParseYearMonthDate, _super);
                function ResultParseYearMonthDate(success, msg, year, month, date) {
                    _super.call(this, success);
                    this.year = year;
                    this.month = month;
                    this.date = date;
                    this.msg = msg || "must yyyymm or yyyy/mm format: year in [1900-9999] and month in [1-12] ";
                }
                ResultParseYearMonthDate.succeeded = function (year, month, date) {
                    return new ResultParseYearMonthDate(true, "", year, month, date);
                };
                ResultParseYearMonthDate.failed = function (msg) {
                    return new ResultParseYearMonthDate(false, msg);
                };
                ResultParseYearMonthDate.prototype.format = function () {
                    if (!this.success) {
                        return "";
                    }
                    return this.year + '/' + uk.text.padLeft(String(this.month), '0', 2) + uk.text.padLeft(String(this.date), '0', 2);
                };
                ResultParseYearMonthDate.prototype.toValue = function () {
                    if (!this.success) {
                        return 0;
                    }
                    return (this.year * 10000 + this.month * 100 + this.date);
                };
                ResultParseYearMonthDate.prototype.getMsg = function () { return this.msg; };
                return ResultParseYearMonthDate;
            }(ParseResult));
            time_1.ResultParseYearMonthDate = ResultParseYearMonthDate;
            function parseYearMonthDate(yearMonthDate) {
                if (yearMonthDate === undefined || yearMonthDate === null) {
                    return ResultParseYearMonthDate.failed("full date can not empty!");
                }
                if (!(yearMonthDate instanceof String)) {
                    yearMonthDate = yearMonthDate.toString();
                }
                yearMonthDate = yearMonthDate.replace("/", "");
                yearMonthDate = yearMonthDate.replace("/", "");
                var checkNum = yearMonthDate.replace(/[0-9]/g, "");
                if (checkNum.length !== 0)
                    return ResultParseYearMonthDate.failed("full date must contain digits and slashes only");
                if (yearMonthDate.length != 8)
                    return ResultParseYearMonthDate.failed("full date format must be yyyy/mm/dd or yyyymmdd");
                var year = parseInt(yearMonthDate.substring(0, 4));
                if (year < 1900 || year > 9999) {
                    return ResultParseYearMonthDate.failed("invalid: year must in range 1900-9999");
                }
                var month = parseInt(yearMonthDate.substring(4, 6));
                if (month < 1 || month > 12)
                    return ResultParseYearMonthDate.failed("invalid: month must in range 1-12");
                var date = parseInt(yearMonthDate.substring(6));
                var maxDate = 30;
                switch (month) {
                    case 2:
                        if (year % 400 == 0) {
                            maxDate = 29;
                        }
                        else if (year % 4 == 0 && year % 25 != 0) {
                            maxDate = 29;
                        }
                        else {
                            maxDate = 28;
                        }
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        maxDate = 31;
                        break;
                    default:
                        maxDate = 30;
                        break;
                }
                if (date < 1 || date > maxDate)
                    return ResultParseYearMonthDate.failed("invalid: month = " + month + ", so your date must in range 1-" + maxDate);
                return ResultParseYearMonthDate.succeeded(year, month, date);
            }
            time_1.parseYearMonthDate = parseYearMonthDate;
            var MomentResult = (function (_super) {
                __extends(MomentResult, _super);
                function MomentResult(momentObject, outputFormat) {
                    _super.call(this, true);
                    this.momentObject = momentObject;
                    this.outputFormat = uk.text.getISOFormat(outputFormat);
                }
                MomentResult.prototype.succeeded = function () {
                    this.success = true;
                };
                MomentResult.prototype.failed = function (msg) {
                    this.msg = (msg) ? msg : "Invalid format";
                    this.success = false;
                };
                MomentResult.prototype.format = function () {
                    if (!this.success)
                        return "";
                    return this.momentObject.format(this.outputFormat);
                };
                MomentResult.prototype.toValue = function () {
                    if (!this.success)
                        return null;
                    return this.momentObject;
                };
                MomentResult.prototype.toNumber = function (outputFormat) {
                    var dateFormats = ["YYYY/MM/DD", "YYYY-MM-DD", "YYYYMMDD", "date"];
                    var yearMonthFormats = ["YYYY/MM", "YYYY-MM", "YYYYMM", "yearmonth"];
                    if (!this.success)
                        return null;
                    if (dateFormats.indexOf(outputFormat) != -1) {
                        return this.momentObject.year() * 10000 + (this.momentObject.month() + 1) * 100 + this.momentObject.date();
                    }
                    else if (yearMonthFormats.indexOf(outputFormat) != -1) {
                        return this.momentObject.year() * 100 + (this.momentObject.month() + 1);
                    }
                    else {
                        return parseInt(this.momentObject.format(outputFormat).replace(/[^\d]/g, ""));
                    }
                };
                MomentResult.prototype.getMsg = function () { return this.msg; };
                return MomentResult;
            }(ParseResult));
            time_1.MomentResult = MomentResult;
            function parseMoment(datetime, outputFormat, inputFormat) {
                var inputFormats = (inputFormat) ? inputFormat : findFormat(outputFormat);
                var momentObject = moment.utc(datetime, inputFormats, true);
                var result = new MomentResult(momentObject, outputFormat);
                if (momentObject.isValid())
                    result.succeeded();
                else
                    result.failed();
                return result;
            }
            time_1.parseMoment = parseMoment;
            function findFormat(format) {
                if (nts.uk.util.isNullOrEmpty(format)) {
                    return defaultInputFormat;
                }
                var uniqueFormat = _.uniq(format.split(""));
                return _.filter(defaultInputFormat, function (dfFormat) {
                    return _.find(uniqueFormat, function (opFormat) {
                        return dfFormat.indexOf(opFormat) >= 0;
                    }) !== undefined;
                });
            }
            function UTCDate(year, month, date, hours, minutes, seconds, milliseconds) {
                if (uk.util.isNullOrUndefined(year)) {
                    var currentDate = new Date();
                    year = currentDate.getUTCFullYear();
                    month = (uk.util.isNullOrUndefined(month)) ? currentDate.getUTCMonth() : month;
                    date = (uk.util.isNullOrUndefined(date)) ? currentDate.getUTCDate() : date;
                    hours = (uk.util.isNullOrUndefined(hours)) ? currentDate.getUTCHours() : hours;
                    minutes = (uk.util.isNullOrUndefined(minutes)) ? currentDate.getUTCMinutes() : minutes;
                    seconds = (uk.util.isNullOrUndefined(seconds)) ? currentDate.getUTCSeconds() : seconds;
                    milliseconds = (uk.util.isNullOrUndefined(milliseconds)) ? currentDate.getUTCMilliseconds() : milliseconds;
                    return new Date(Date.UTC(year, month, date, hours, minutes, seconds, milliseconds));
                }
                else {
                    month = (uk.util.isNullOrUndefined(month)) ? 0 : month;
                    date = (uk.util.isNullOrUndefined(date)) ? 1 : date;
                    hours = (uk.util.isNullOrUndefined(hours)) ? 0 : hours;
                    minutes = (uk.util.isNullOrUndefined(minutes)) ? 0 : minutes;
                    seconds = (uk.util.isNullOrUndefined(seconds)) ? 1 : seconds;
                    milliseconds = (uk.util.isNullOrUndefined(milliseconds)) ? 0 : milliseconds;
                    return new Date(Date.UTC(year, month, date, hours, minutes, seconds, milliseconds));
                }
            }
            time_1.UTCDate = UTCDate;
            var DateTimeFormatter = (function () {
                function DateTimeFormatter() {
                    this.shortYmdPattern = /^\d{4}\/\d{1,2}\/\d{1,2}$/;
                    this.shortYmdwPattern = /^\d{4}\/\d{1,2}\/\d{1,2}\(\w+\)$/;
                    this.shortYmPattern = /^\d{4}\/\d{1,2}$/;
                    this.shortMdPattern = /^\d{1,2}\/\d{1,2}$/;
                    this.longYmdPattern = /^\d{4}年\d{1,2}月\d{1,2}日$/;
                    this.longYmdwPattern = /^\d{4}年\d{1,2}月\d{1,2}日\(\w+\)$/;
                    this.longFPattern = /^\d{4}年度$/;
                    this.longJmdPattern = /^\w{2}\d{1,3}年\d{1,2}月\d{1,2}日$/;
                    this.longJmPattern = /^\w{2}\d{1,3}年\d{1,2}月$/;
                    this.fullDateTimeShortPattern = /^\d{4}\/\d{1,2}\/\d{1,2} \d+:\d{2}:\d{2}$/;
                    this.timeShortHmsPattern = /^\d+:\d{2}:\d{2}$/;
                    this.timeShortHmPattern = /^\d+:\d{2}$/;
                    this.days = ['日', '月', '火', '水', '木', '金', '土'];
                }
                DateTimeFormatter.prototype.shortYmd = function (date) {
                    var d = this.dateOf(date);
                    if (this.shortYmdPattern.test(d))
                        return this.format(d);
                };
                DateTimeFormatter.prototype.shortYmdw = function (date) {
                    var d = this.dateOf(date);
                    if (this.shortYmdwPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var dayStr = this.days[new Date(d).getDay()];
                        return this.format(d) + '(' + dayStr + ')';
                    }
                };
                DateTimeFormatter.prototype.shortYm = function (date) {
                    var d = this.format(this.dateOf(date));
                    if (this.shortYmPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var end = d.lastIndexOf("/");
                        if (end !== -1)
                            return d.substring(0, end);
                    }
                };
                DateTimeFormatter.prototype.shortMd = function (date) {
                    var d = this.format(this.dateOf(date));
                    if (this.shortMdPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var start = d.indexOf("/");
                        if (start !== -1)
                            return d.substring(start + 1);
                    }
                };
                DateTimeFormatter.prototype.longYmd = function (date) {
                    var d = this.dateOf(date);
                    if (this.longYmdPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var mDate = new Date(d);
                        return this.toLongJpDate(mDate);
                    }
                };
                DateTimeFormatter.prototype.longYmdw = function (date) {
                    var d = this.dateOf(date);
                    if (this.longYmdwPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var mDate = new Date(d);
                        return this.toLongJpDate(mDate) + '(' + this.days[mDate.getDay()] + ')';
                    }
                };
                DateTimeFormatter.prototype.toLongJpDate = function (d) {
                    return d.getFullYear() + '年' + (d.getMonth() + 1) + '月' + d.getDate() + '日';
                };
                DateTimeFormatter.prototype.longF = function (date) {
                    var d = this.dateOf(date);
                    if (this.longFPattern.test(d))
                        return d;
                    if (this.shortYmdPattern.test(d)) {
                        var mDate = new Date(d);
                        return this.fiscalYearOf(mDate) + '年度';
                    }
                };
                DateTimeFormatter.prototype.longJmd = function (date) {
                    var d = this.dateOf(date);
                    if (this.longJmdPattern.test(d))
                        return d;
                    return this.fullJapaneseDateOf(d);
                };
                DateTimeFormatter.prototype.longJm = function (date) {
                    var d = this.dateOf(date);
                    if (this.longJmPattern.test(d))
                        return d;
                    var jpDate = this.fullJapaneseDateOf(d);
                    var start = jpDate.indexOf("月");
                    if (start !== -1) {
                        return jpDate.substring(0, start + 1);
                    }
                };
                DateTimeFormatter.prototype.fullJapaneseDateOf = function (date) {
                    if (this.shortYmdPattern.test(date)) {
                        var d = new Date(date);
                        return d.toLocaleDateString("ja-JP-u-ca-japanese", { era: 'short' });
                    }
                    return date;
                };
                DateTimeFormatter.prototype.fiscalYearOf = function (date) {
                    if (date < new Date(date.getFullYear(), 3, 1))
                        return date.getFullYear() - 1;
                    return date.getFullYear();
                };
                DateTimeFormatter.prototype.dateOf = function (dateTime) {
                    if (this.fullDateTimeShortPattern.test(dateTime)) {
                        return dateTime.split(" ")[0];
                    }
                    return dateTime;
                };
                DateTimeFormatter.prototype.timeOf = function (dateTime) {
                    if (this.fullDateTimeShortPattern.test(dateTime)) {
                        return dateTime.split(" ")[1];
                    }
                    return dateTime;
                };
                DateTimeFormatter.prototype.timeShortHm = function (time) {
                    var t = this.timeOf(time);
                    if (this.timeShortHmPattern.test(t))
                        return t;
                    if (this.timeShortHmsPattern.test(t)) {
                        return t.substring(0, t.lastIndexOf(":"));
                    }
                };
                DateTimeFormatter.prototype.timeShortHms = function (time) {
                    var t = this.timeOf(time);
                    if (this.timeShortHmsPattern.test(t))
                        return t;
                };
                DateTimeFormatter.prototype.clockShortHm = function (time) {
                    return this.timeShortHm(time);
                };
                DateTimeFormatter.prototype.fullDateTimeShort = function (dateTime) {
                    if (this.fullDateTimeShortPattern.test(dateTime))
                        return dateTime;
                };
                DateTimeFormatter.prototype.format = function (date) {
                    return new Date(date).toLocaleDateString("ja-JP");
                };
                return DateTimeFormatter;
            }());
            time_1.DateTimeFormatter = DateTimeFormatter;
            function getFormatter() {
                switch (systemLanguage) {
                    case 'ja':
                        return new DateTimeFormatter();
                    case 'en':
                        return null;
                }
            }
            time_1.getFormatter = getFormatter;
            function applyFormat(format, dateTime, formatter) {
                if (formatter === undefined)
                    formatter = getFormatter();
                switch (format) {
                    case 'Short_YMD':
                        return formatter.shortYmd(dateTime);
                    case 'Short_YMDW':
                        return formatter.shortYmdw(dateTime);
                    case 'Short_YM':
                        return formatter.shortYm(dateTime);
                    case 'Short_MD':
                        return formatter.shortMd(dateTime);
                    case 'Long_YMD':
                        return formatter.longYmd(dateTime);
                    case 'Long_YMDW':
                        return formatter.longYmdw(dateTime);
                    case 'Long_F':
                        return formatter.longF(dateTime);
                    case 'Long_JMD':
                        return formatter.longJmd(dateTime);
                    case 'Long_JM':
                        return formatter.longJm(dateTime);
                    case 'Time_Short_HM':
                        return formatter.timeShortHm(dateTime);
                    case 'Time_Short_HMS':
                        return formatter.timeShortHms(dateTime);
                    case 'Clock_Short_HM':
                        return formatter.clockShortHm(dateTime);
                    case 'DateTime_Short_YMDHMS':
                        return formatter.fullDateTimeShort(dateTime);
                }
            }
            time_1.applyFormat = applyFormat;
            function isEndOfMonth(value, format) {
                var currentDate = moment(value, format);
                if (currentDate.isValid()) {
                    return currentDate.daysInMonth() === currentDate.date();
                }
                return false;
            }
            time_1.isEndOfMonth = isEndOfMonth;
        })(time = uk.time || (uk.time = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var request;
        (function (request) {
            request.STORAGE_KEY_TRANSFER_DATA = "nts.uk.request.STORAGE_KEY_TRANSFER_DATA";
            request.WEB_APP_NAME = {
                com: 'nts.uk.com.web',
                pr: 'nts.uk.pr.web',
                at: 'nts.uk.at.web'
            };
            var QueryString = (function () {
                function QueryString() {
                    this.items = {};
                }
                QueryString.parseUrl = function (url) {
                    var instance = new QueryString();
                    var queryString = url.split('?')[1];
                    if (queryString) {
                        var queryEntries = queryString.split('&');
                        for (var i = 0; i < queryEntries.length; i++) {
                            var entryParts = queryEntries[i].split('=');
                            instance.set(entryParts[0], entryParts[1]);
                        }
                    }
                    return instance;
                };
                QueryString.build = function (entriesObj) {
                    var instance = new QueryString();
                    for (var key in entriesObj) {
                        instance.set(key, entriesObj[key]);
                    }
                    return instance;
                };
                QueryString.prototype.get = function (key) {
                    return this.items[key];
                };
                QueryString.prototype.set = function (key, value) {
                    if (key === null || key === undefined || key === '') {
                        return;
                    }
                    this.items[key] = value;
                };
                QueryString.prototype.remove = function (key) {
                    delete this.items[key];
                };
                QueryString.prototype.mergeFrom = function (otherObj) {
                    for (var otherKey in otherObj.items) {
                        this.set(otherKey, otherObj.items[otherKey]);
                    }
                };
                QueryString.prototype.count = function () {
                    var count = 0;
                    for (var key in this.items) {
                        count++;
                    }
                    return count;
                };
                QueryString.prototype.hasItems = function () {
                    return this.count() !== 0;
                };
                QueryString.prototype.serialize = function () {
                    var entryStrings = [];
                    for (var key in this.items) {
                        entryStrings.push(key + '=' + this.items[key]);
                    }
                    return entryStrings.join('&');
                };
                return QueryString;
            }());
            request.QueryString = QueryString;
            var Locator = (function () {
                function Locator(url) {
                    this.rawUrl = url;
                    this.queryString = QueryString.parseUrl(url);
                }
                Locator.prototype.serialize = function () {
                    if (this.queryString.hasItems()) {
                        return this.rawUrl + '?' + this.queryString.serialize();
                    }
                    else {
                        return this.rawUrl;
                    }
                };
                Locator.prototype.mergeRelativePath = function (relativePath) {
                    var stack = this.rawUrl.split('/');
                    var parts = relativePath.split('/');
                    var queryStringToAdd = QueryString.parseUrl(relativePath);
                    stack.pop();
                    if (parts[0] === '') {
                        parts.shift();
                    }
                    for (var i = 0; i < parts.length; i++) {
                        if (parts[i] === '.')
                            continue;
                        if (parts[i] === '..')
                            stack.pop();
                        else
                            stack.push(parts[i]);
                    }
                    queryStringToAdd.mergeFrom(this.queryString);
                    var queryStringParts = queryStringToAdd.hasItems()
                        ? '?' + queryStringToAdd.serialize()
                        : '';
                    return new Locator(stack.join('/') + queryStringParts);
                };
                return Locator;
            }());
            request.Locator = Locator;
            function ajax(webAppId, path, data, options) {
                if (typeof arguments[1] !== 'string') {
                    return ajax.apply(null, _.concat(location.currentAppId, arguments));
                }
                var dfd = $.Deferred();
                options = options || {};
                if (typeof data === 'object') {
                    data = JSON.stringify(data);
                }
                var webserviceLocator = location.siteRoot
                    .mergeRelativePath(request.WEB_APP_NAME[webAppId] + '/')
                    .mergeRelativePath(location.ajaxRootDir)
                    .mergeRelativePath(path);
                $.ajax({
                    type: options.method || 'POST',
                    contentType: options.contentType || 'application/json',
                    url: webserviceLocator.serialize(),
                    dataType: options.dataType || 'json',
                    data: data,
                    headers: {
                        'PG-Path': location.current.serialize()
                    }
                }).done(function (res) {
                    if (res !== undefined && res.businessException) {
                        dfd.reject(res);
                    }
                    else {
                        dfd.resolve(res);
                    }
                });
                return dfd.promise();
            }
            request.ajax = ajax;
            function uploadFile(data, option) {
                var dfd = $.Deferred();
                $.ajax({
                    url: "/nts.uk.com.web/webapi/ntscommons/arc/filegate/upload",
                    type: 'POST',
                    data: data,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data, textStatus, jqXHR) {
                        if (option.onSuccess) {
                            option.onSuccess();
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        if (option.onFail) {
                            option.onFail();
                        }
                    }
                }).done(function (res) {
                    if (res !== undefined && res.businessException) {
                        dfd.reject(res);
                    }
                    else {
                        dfd.resolve(res);
                    }
                }).fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            request.uploadFile = uploadFile;
            function exportFile(path, data, options) {
                var dfd = $.Deferred();
                ajax(path, data, options)
                    .then(function (res) {
                    return uk.deferred.repeat(function (conf) { return conf
                        .task(function () { return specials.getAsyncTaskInfo(res.taskId); })
                        .while(function (info) { return info.pending || info.running; })
                        .pause(1000); });
                })
                    .done(function (res) {
                    if (res.failed || res.status == "ABORTED") {
                        dfd.reject(res.error);
                    }
                    else {
                        specials.donwloadFile(res.id);
                        dfd.resolve(res);
                    }
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            request.exportFile = exportFile;
            var specials;
            (function (specials) {
                function getAsyncTaskInfo(taskId) {
                    return ajax('/ntscommons/arc/task/async/' + taskId);
                }
                specials.getAsyncTaskInfo = getAsyncTaskInfo;
                function donwloadFile(fileId) {
                    $('<iframe/>')
                        .attr('id', 'download-frame')
                        .appendTo('body')
                        .attr('src', resolvePath('/webapi/ntscommons/arc/filegate/get/' + fileId));
                }
                specials.donwloadFile = donwloadFile;
            })(specials = request.specials || (request.specials = {}));
            function jump(path, data) {
                uk.sessionStorage.setItemAsJson(request.STORAGE_KEY_TRANSFER_DATA, data);
                window.location.href = resolvePath(path);
            }
            request.jump = jump;
            function resolvePath(path) {
                var destination;
                if (path.charAt(0) === '/') {
                    destination = location.appRoot.mergeRelativePath(path);
                }
                else {
                    destination = location.current.mergeRelativePath(path);
                }
                return destination.rawUrl;
            }
            request.resolvePath = resolvePath;
            var location;
            (function (location) {
                location.current = new Locator(window.location.href);
                location.appRoot = location.current.mergeRelativePath(__viewContext.rootPath);
                location.siteRoot = location.appRoot.mergeRelativePath('../');
                location.ajaxRootDir = 'webapi/';
                var currentAppName = _.takeRight(location.appRoot.serialize().split('/'), 2)[0];
                for (var id in request.WEB_APP_NAME) {
                    if (currentAppName === request.WEB_APP_NAME[id]) {
                        location.currentAppId = id;
                        break;
                    }
                }
            })(location = request.location || (request.location = {}));
            ;
        })(request = uk.request || (uk.request = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            ui.documentReady = $.Callbacks();
            ui.viewModelBuilt = $.Callbacks();
            var KibanViewModel = (function () {
                function KibanViewModel() {
                    this.title = ko.observable('');
                    this.errorDialogViewModel = new nts.uk.ui.errors.ErrorsViewModel();
                }
                return KibanViewModel;
            }());
            ui.KibanViewModel = KibanViewModel;
            var init;
            (function (init) {
                var _start;
                __viewContext.ready = function (callback) {
                    _start = callback;
                };
                __viewContext.bind = function (contentViewModel) {
                    var kiban = new KibanViewModel();
                    ui._viewModel = {
                        content: contentViewModel,
                        kiban: kiban,
                        errors: {
                            isEmpty: ko.computed(function () { return !kiban.errorDialogViewModel.occurs(); })
                        }
                    };
                    kiban.title(__viewContext.title || 'THIS IS TITLE');
                    ui.viewModelBuilt.fire(ui._viewModel);
                    ko.applyBindings(ui._viewModel);
                    $(".reset-not-apply").find(".reset-element").off("reset");
                };
                $(function () {
                    ui.documentReady.fire();
                    __viewContext.transferred = uk.sessionStorage.getItemAndRemove(uk.request.STORAGE_KEY_TRANSFER_DATA)
                        .map(function (v) { return JSON.parse(v); });
                    _.defer(function () { return _start.call(__viewContext); });
                });
            })(init || (init = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var notify;
            (function (notify) {
                var error;
                (function (error) {
                    ui.documentReady.add(function () {
                        var $functionsArea = $('#functions-area');
                        var $functionsAreaBottom = $('#functions-area-bottom');
                        if ($functionsArea.length > 0) {
                            _.defer(function () {
                                $('#func-notifier-errors').position({ my: 'left+5 top-5', at: 'left bottom', of: $('#functions-area') });
                            });
                        }
                        else if ($functionsAreaBottom.length > 0) {
                            _.defer(function () {
                                $('#func-notifier-errors').position({ my: 'left+5 top+48', at: 'left top', of: $('#functions-area-bottom') });
                            });
                        }
                        else {
                            return;
                        }
                    });
                })(error || (error = {}));
            })(notify = ui.notify || (ui.notify = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var validation;
            (function (validation) {
                var util = nts.uk.util;
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
                    function StringValidator(name, primitiveValueName, option) {
                        this.name = name;
                        this.constraint = getConstraint(primitiveValueName);
                        this.charType = uk.text.getCharType(primitiveValueName);
                        this.required = option.required;
                    }
                    StringValidator.prototype.validate = function (inputText, option) {
                        var result = new ValidationResult();
                        if (this.required !== undefined && this.required !== false) {
                            if (util.isNullOrEmpty(inputText)) {
                                result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [this.name]));
                                return result;
                            }
                        }
                        var validateResult;
                        if (!util.isNullOrUndefined(this.charType)) {
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
                            validateResult = this.charType.validate(inputText);
                            if (!validateResult.isValid) {
                                result.fail(nts.uk.resource.getMessage(validateResult.errorMessage, [this.name, !util.isNullOrUndefined(this.constraint)
                                        ? (!util.isNullOrUndefined(this.constraint.maxLength)
                                            ? this.constraint.maxLength : 9999) : 9999]));
                                return result;
                            }
                        }
                        if (this.constraint !== undefined && this.constraint !== null) {
                            if (this.constraint.maxLength !== undefined && uk.text.countHalf(inputText) > this.constraint.maxLength) {
                                var maxLength = this.constraint.maxLength;
                                if (this.constraint.charType == "Any")
                                    maxLength = maxLength / 2;
                                result.fail(nts.uk.resource.getMessage(validateResult.errorMessage, [this.name, maxLength]));
                                return result;
                            }
                            if (!util.isNullOrUndefined(option) && option.isCheckExpression === true) {
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
                    function NumberValidator(name, primitiveValueName, option) {
                        this.name = name;
                        this.constraint = getConstraint(primitiveValueName);
                        this.option = option;
                    }
                    NumberValidator.prototype.validate = function (inputText) {
                        var result = new ValidationResult();
                        var isDecimalNumber = false;
                        if (this.option !== undefined) {
                            if (nts.uk.util.isNullOrUndefined(inputText) || inputText.trim().length <= 0) {
                                if (this.option['required'] === true && nts.uk.util.isNullOrEmpty(this.option['defaultValue'])) {
                                    result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [this.name]));
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
                        var message = {};
                        var validateFail = false, max = 99999999, min = 0, mantissaMaxLength;
                        if (!util.isNullOrUndefined(this.constraint) && this.constraint.valueType === "HalfInt") {
                            if (!uk.ntsNumber.isHalfInt(inputText, message))
                                validateFail = true;
                        }
                        else if (!uk.ntsNumber.isNumber(inputText, isDecimalNumber, undefined, message)) {
                            validateFail = true;
                        }
                        var value = isDecimalNumber ?
                            uk.ntsNumber.getDecimal(inputText, this.option.decimallength) : parseInt(inputText);
                        if (!util.isNullOrUndefined(this.constraint)) {
                            if (!util.isNullOrUndefined(this.constraint.max)) {
                                max = this.constraint.max;
                                if (value > this.constraint.max)
                                    validateFail = true;
                            }
                            if (!util.isNullOrUndefined(this.constraint.min)) {
                                min = this.constraint.min;
                                if (value < this.constraint.min)
                                    validateFail = true;
                            }
                            if (!util.isNullOrUndefined(this.constraint.mantissaMaxLength)) {
                                mantissaMaxLength = this.constraint.mantissaMaxLength;
                                var parts = String(value).split(".");
                                if (parts[1] !== undefined && parts[1].length > mantissaMaxLength)
                                    validateFail = true;
                            }
                        }
                        if (validateFail) {
                            result.fail(nts.uk.resource.getMessage(message.id, [this.name, min, max, mantissaMaxLength]));
                        }
                        else {
                            result.success(value.toString() === "0" ? inputText : uk.text.removeFromStart(inputText, "0"));
                        }
                        return result;
                    };
                    return NumberValidator;
                }());
                validation.NumberValidator = NumberValidator;
                var TimeValidator = (function () {
                    function TimeValidator(name, primitiveValueName, option) {
                        this.name = name;
                        this.constraint = getConstraint(primitiveValueName);
                        this.outputFormat = (option && option.outputFormat) ? option.outputFormat : "";
                        this.required = (option && option.required) ? option.required : false;
                        this.valueType = (option && option.valueType) ? option.valueType : "string";
                        this.mode = (option && option.mode) ? option.mode : "";
                    }
                    TimeValidator.prototype.validate = function (inputText) {
                        var result = new ValidationResult();
                        if (util.isNullOrEmpty(inputText)) {
                            if (this.required === true) {
                                result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [this.name]));
                                return result;
                            }
                            else {
                                result.success("");
                                return result;
                            }
                        }
                        var maxStr, minStr;
                        if (this.mode === "time") {
                            var timeParse = uk.time.parseTime(inputText, false);
                            if (timeParse.success) {
                                result.success(timeParse.toValue());
                            }
                            else {
                                result.fail("");
                            }
                            if (!util.isNullOrUndefined(this.constraint)) {
                                if (!util.isNullOrUndefined(this.constraint.max)) {
                                    maxStr = this.constraint.max;
                                    var max = uk.time.parseTime(this.constraint.max);
                                    if (timeParse.success && (max.hours < timeParse.hours
                                        || (max.hours === timeParse.hours && max.minutes < timeParse.minutes))) {
                                        result.fail("");
                                    }
                                }
                                if (!util.isNullOrUndefined(this.constraint.min)) {
                                    minStr = this.constraint.min;
                                    var min = uk.time.parseTime(this.constraint.min);
                                    if (timeParse.success && (min.hours > timeParse.hours
                                        || (min.hours === timeParse.hours && min.minutes > timeParse.minutes))) {
                                        result.fail("");
                                    }
                                }
                                if (!result.isValid && this.constraint.valueType === "Time") {
                                    result.fail(nts.uk.resource.getMessage("FND_E_TIME", [this.name, minStr, maxStr]));
                                }
                            }
                            return result;
                        }
                        var parseResult = uk.time.parseMoment(inputText, this.outputFormat);
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
                        if (this.outputFormat === "time") {
                            if (!util.isNullOrUndefined(this.constraint)) {
                                var inputMoment = parseResult.toValue();
                                if (!util.isNullOrUndefined(this.constraint.max)) {
                                    maxStr = this.constraint.max;
                                    var maxMoment = moment.duration(maxStr);
                                    if (parseResult.success && (maxMoment.hours() < inputMoment.hours()
                                        || (maxMoment.hours() === inputMoment.hours() && maxMoment.minutes() < inputMoment.minutes()))) {
                                        result.fail("");
                                    }
                                }
                                if (!util.isNullOrUndefined(this.constraint.min)) {
                                    minStr = this.constraint.min;
                                    var minMoment = moment.duration(minStr);
                                    if (parseResult.success && (minMoment.hours() > inputMoment.hours()
                                        || (minMoment.hours() === inputMoment.hours() && minMoment.minutes() > inputMoment.minutes()))) {
                                        result.fail("");
                                    }
                                }
                                if (!result.isValid && this.constraint.valueType === "Clock") {
                                    result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [this.name, minStr, maxStr]));
                                }
                            }
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
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var errors;
            (function (errors) {
                var ErrorsViewModel = (function () {
                    function ErrorsViewModel() {
                        var _this = this;
                        this.title = "エラー一覧";
                        this.errors = ko.observableArray([]);
                        this.errors.extend({ rateLimit: 1 });
                        this.option = ko.mapping.fromJS(new ui.option.ErrorDialogOption());
                        this.occurs = ko.computed(function () { return _this.errors().length !== 0; });
                        this.allResolved = $.Callbacks();
                        this.errors.subscribe(function () {
                            if (_this.errors().length === 0) {
                                _this.allResolved.fire();
                            }
                        });
                        this.allResolved.add(function () {
                            _this.hide();
                        });
                    }
                    ErrorsViewModel.prototype.closeButtonClicked = function () {
                    };
                    ErrorsViewModel.prototype.open = function () {
                        this.option.show(true);
                    };
                    ErrorsViewModel.prototype.hide = function () {
                        this.option.show(false);
                    };
                    ErrorsViewModel.prototype.addError = function (error) {
                        var duplicate = _.filter(this.errors(), function (e) { return e.$control.is(error.$control)
                            && (typeof error.message === "string" ? e.messageText === error.message : e.messageText === error.messageText); });
                        if (duplicate.length == 0) {
                            if (typeof error.message === "string") {
                                error.messageText = error.message;
                                error.message = "";
                            }
                            else {
                                if (error.message.message) {
                                    error.messageText = error.message.message;
                                    error.message = error.message.messageId != null && error.message.messageId.length > 0 ? error.message.messageId : "";
                                }
                                else {
                                    if (error.$control.length > 0) {
                                        var controlNameId = error.$control.eq(0).attr("data-name");
                                        if (controlNameId) {
                                            error.messageText = nts.uk.resource.getMessage(error.message.messageId, nts.uk.resource.getText(controlNameId), error.message.messageParams);
                                        }
                                        else {
                                            error.messageText = nts.uk.resource.getMessage(error.message.messageId, error.message.messageParams);
                                        }
                                    }
                                    else {
                                        error.messageText = nts.uk.resource.getMessage(error.message.messageId);
                                    }
                                    error.message = error.message.messageId;
                                }
                            }
                            this.errors.push(error);
                        }
                    };
                    ErrorsViewModel.prototype.hasError = function () {
                        return this.errors().length > 0;
                    };
                    ErrorsViewModel.prototype.clearError = function () {
                        $(".error").children().each(function (index, element) {
                            if ($(element).data("hasError"))
                                $(element).data("hasError", false);
                        });
                        $(".error").removeClass('error');
                        this.errors.removeAll();
                    };
                    ErrorsViewModel.prototype.removeErrorByElement = function ($element) {
                        this.errors.remove(function (error) {
                            return error.$control.is($element);
                        });
                    };
                    ErrorsViewModel.prototype.getErrorByElement = function ($element) {
                        return _.find(this.errors(), function (e) {
                            return e.$control.is($element);
                        });
                    };
                    return ErrorsViewModel;
                }());
                errors.ErrorsViewModel = ErrorsViewModel;
                var ErrorHeader = (function () {
                    function ErrorHeader(name, text, width, visible) {
                        this.name = name;
                        this.text = text;
                        this.width = width;
                        this.visible = visible;
                    }
                    return ErrorHeader;
                }());
                errors.ErrorHeader = ErrorHeader;
                function errorsViewModel() {
                    return nts.uk.ui._viewModel.kiban.errorDialogViewModel;
                }
                errors.errorsViewModel = errorsViewModel;
                function show() {
                    errorsViewModel().open();
                }
                errors.show = show;
                function hide() {
                    errorsViewModel().hide();
                }
                errors.hide = hide;
                function add(error) {
                    errorsViewModel().addError(error);
                }
                errors.add = add;
                function hasError() {
                    return errorsViewModel().hasError();
                }
                errors.hasError = hasError;
                function clearAll() {
                    errorsViewModel().clearError();
                }
                errors.clearAll = clearAll;
                function removeByElement($control) {
                    errorsViewModel().removeErrorByElement($control);
                }
                errors.removeByElement = removeByElement;
                function getErrorByElement($element) {
                    return errorsViewModel().getErrorByElement($element);
                }
                errors.getErrorByElement = getErrorByElement;
            })(errors = ui.errors || (ui.errors = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_1) {
            var windows;
            (function (windows) {
                var MAIN_WINDOW_ID = 'MAIN_WINDOW';
                var DEFAULT_DIALOG_OPTIONS = {
                    autoOpen: false,
                    draggable: true,
                    resizable: false,
                    dialogClass: "no-close",
                    create: function (event) {
                        $(event.target).dialog('widget').css({ position: 'fixed' });
                    }
                };
                var ScreenWindow = (function () {
                    function ScreenWindow(id, isRoot, parent) {
                        this.globalContext = null;
                        this.$dialog = null;
                        this.$iframe = null;
                        this.onClosedHandler = $.noop;
                        this.id = id;
                        this.isRoot = isRoot;
                        this.parent = parent;
                    }
                    ScreenWindow.createMainWindow = function () {
                        return new ScreenWindow(MAIN_WINDOW_ID, true, null);
                    };
                    ScreenWindow.createSubWindow = function (parent) {
                        return new ScreenWindow(uk.util.randomId(), false, parent);
                    };
                    ScreenWindow.prototype.setGlobal = function (globalContext) {
                        this.globalContext = globalContext;
                    };
                    ScreenWindow.prototype.setTitle = function (newTitle) {
                        if (this.isRoot) {
                            this.globalContext.title = newTitle;
                        }
                        else {
                            this.$dialog.dialog('option', { title: newTitle });
                        }
                    };
                    ScreenWindow.prototype.setHeight = function (height) {
                        if (!isNaN(height)) {
                            this.$dialog.dialog('option', {
                                height: height
                            });
                            this.$dialog.resize();
                        }
                    };
                    ScreenWindow.prototype.setWidth = function (width) {
                        if (!isNaN(width)) {
                            this.$dialog.dialog('option', {
                                width: width
                            });
                            this.$dialog.resize();
                        }
                    };
                    ScreenWindow.prototype.setSize = function (height, width) {
                        if (!isNaN(width) && !isNaN(height)) {
                            this.$dialog.dialog('option', {
                                width: width,
                                height: height
                            });
                            this.$dialog.resize();
                        }
                    };
                    ScreenWindow.prototype.setupAsDialog = function (path, options) {
                        var _this = this;
                        options.close = function () {
                            _this.dispose();
                        };
                        this.build$dialog(options);
                        this.$iframe.bind('load', function () {
                            _this.globalContext.nts.uk.ui.windows.selfId = _this.id;
                            options.title = '※ダイアログタイトルは基盤で自動化予定';
                            _this.$dialog.dialog('option', {
                                width: options.width || _this.globalContext.dialogSize.width,
                                height: options.height || _this.globalContext.dialogSize.height,
                                title: options.title || "dialog",
                                resizable: options.resizable,
                                beforeClose: function () {
                                }
                            }).dialog('open');
                        });
                        this.globalContext.location.href = uk.request.resolvePath(path);
                    };
                    ScreenWindow.prototype.build$dialog = function (options) {
                        this.$dialog = $('<div/>')
                            .css({
                            padding: '0px',
                            overflow: 'hidden'
                        })
                            .appendTo($('body'))
                            .dialog(options);
                        this.$iframe = $('<iframe/>').css({
                            width: '100%',
                            height: '100%'
                        })
                            .appendTo(this.$dialog);
                        this.setGlobal(this.$iframe[0].contentWindow);
                    };
                    ScreenWindow.prototype.onClosed = function (callback) {
                        this.onClosedHandler = function () {
                            callback();
                            windows.container.localShared = {};
                        };
                    };
                    ScreenWindow.prototype.close = function () {
                        if (this.isRoot) {
                            window.close();
                        }
                        else {
                            this.$dialog.dialog('close');
                        }
                    };
                    ScreenWindow.prototype.dispose = function () {
                        var _this = this;
                        _.defer(function () { return _this.onClosedHandler(); });
                        setTimeout(function () {
                            _this.$iframe.remove();
                            _this.$dialog.remove();
                            _this.$dialog = null;
                            _this.$iframe = null;
                            _this.globalContext = null;
                            _this.parent = null;
                            _this.onClosedHandler = null;
                        }, 2000);
                    };
                    return ScreenWindow;
                }());
                windows.ScreenWindow = ScreenWindow;
                var ScreenWindowContainer = (function () {
                    function ScreenWindowContainer() {
                        this.windows = {};
                        this.windows[windows.selfId] = ScreenWindow.createMainWindow();
                        this.windows[windows.selfId].setGlobal(window);
                        this.shared = {};
                        this.localShared = {};
                    }
                    ScreenWindowContainer.prototype.createDialog = function (path, options, parentId) {
                        var parentwindow = this.windows[parentId];
                        var subWindow = ScreenWindow.createSubWindow(parentwindow);
                        this.windows[subWindow.id] = subWindow;
                        options = $.extend({}, DEFAULT_DIALOG_OPTIONS, options);
                        subWindow.setupAsDialog(path, options);
                        return subWindow;
                    };
                    ScreenWindowContainer.prototype.getShared = function (key) {
                        return this.localShared[key] !== undefined ? this.localShared[key] : this.shared[key];
                    };
                    ScreenWindowContainer.prototype.setShared = function (key, data, isRoot, persist) {
                        if (persist || isRoot) {
                            this.shared[key] = data;
                        }
                        else {
                            this.localShared[key] = data;
                        }
                    };
                    ScreenWindowContainer.prototype.close = function (id) {
                        var target = this.windows[id];
                        delete this.windows[id];
                        target.close();
                    };
                    return ScreenWindowContainer;
                }());
                windows.ScreenWindowContainer = ScreenWindowContainer;
                if (uk.util.isInFrame()) {
                    var parent = window.parent;
                    windows.container = (parent.nts.uk.ui.windows.container);
                }
                else {
                    windows.selfId = MAIN_WINDOW_ID;
                    windows.container = new ScreenWindowContainer();
                }
                function getShared(key) {
                    return windows.container.getShared(key);
                }
                windows.getShared = getShared;
                function setShared(key, data, persist) {
                    windows.container.setShared(key, data, windows.getSelf().isRoot, persist);
                }
                windows.setShared = setShared;
                function getSelf() {
                    return windows.container.windows[windows.selfId];
                }
                windows.getSelf = getSelf;
                function close(windowId) {
                    windowId = uk.util.orDefault(windowId, windows.selfId);
                    windows.container.close(windowId);
                }
                windows.close = close;
                var sub;
                (function (sub) {
                    function modal(path, options) {
                        options = options || {};
                        options.modal = true;
                        return open(path, options);
                    }
                    sub.modal = modal;
                    function modeless(path, options) {
                        options = options || {};
                        options.modal = false;
                        return open(path, options);
                    }
                    sub.modeless = modeless;
                    function open(path, options) {
                        return windows.container.createDialog(path, options, windows.selfId);
                    }
                    sub.open = open;
                })(sub = windows.sub || (windows.sub = {}));
            })(windows = ui_1.windows || (ui_1.windows = {}));
            function localize(textId) {
                return textId;
            }
            ui_1.localize = localize;
            var dialog;
            (function (dialog) {
                var DialogHeader = (function () {
                    function DialogHeader() {
                    }
                    return DialogHeader;
                }());
                dialog.DialogHeader = DialogHeader;
                var Message = (function () {
                    function Message() {
                    }
                    return Message;
                }());
                dialog.Message = Message;
                function createNoticeDialog(message, buttons, header) {
                    var $control = $('<div/>').addClass('control');
                    var text;
                    if (typeof message === "object") {
                        if (message.message) {
                            text = message.message;
                            if (message.messageId) {
                                $control.append(message.messageId);
                            }
                        }
                        else {
                            text = nts.uk.resource.getMessage(message.messageId, message.messageParams);
                            $control.append(message.messageId);
                        }
                    }
                    else {
                        text = message;
                    }
                    text = text.replace(/\n/g, '<br />');
                    var $this = $('<div/>').addClass('notice-dialog')
                        .append($('<div/>').addClass('text').append(text))
                        .append($control)
                        .appendTo('body')
                        .dialog({
                        dialogClass: "no-close-btn",
                        width: 'auto',
                        modal: true,
                        minWidth: 300,
                        maxWidth: 800,
                        maxHeight: 400,
                        closeOnEscape: false,
                        buttons: buttons,
                        open: function () {
                            $(this).closest('.ui-dialog').css('z-index', 120001);
                            $('.ui-widget-overlay').last().css('z-index', 120000);
                            $(this).parent().find('.ui-dialog-buttonset > button:first-child').focus();
                            $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                            if (header && header.icon) {
                                var $headerContainer = $("<div'></div>").addClass("ui-dialog-titlebar-container");
                                $headerContainer.append($("<img>").attr("src", header.icon).addClass("ui-dialog-titlebar-icon"));
                                $headerContainer.append($(this).parent().find(".ui-dialog-title"));
                                $(this).parent().children(".ui-dialog-titlebar").prepend($headerContainer);
                            }
                        },
                        close: function (event) {
                            $(this).dialog('destroy');
                            $(event.target).remove();
                        }
                    });
                    if (header && header.text) {
                        $this.dialog("option", "title", header.text);
                    }
                    return $this;
                }
                function info(text) {
                    var then = $.noop;
                    var $dialog = $('<div/>').hide();
                    $(function () {
                        $dialog.appendTo('body').dialog({
                            autoOpen: false
                        });
                    });
                    setTimeout(function () {
                        var $this = createNoticeDialog(text, [{
                                text: "はい",
                                "class": "large",
                                click: function () {
                                    $this.dialog('close');
                                    then();
                                }
                            }], { icon: "/nts.uk.com.js.web/lib/nittsu/ui/style/images/infor.png", text: nts.uk.resource.getText("infor") });
                    }, 0);
                    return {
                        then: function (callback) {
                            then = callback;
                        }
                    };
                }
                dialog.info = info;
                ;
                function alertError(message) {
                    var then = $.noop;
                    var $dialog = $('<div/>').hide();
                    $(function () {
                        $dialog.appendTo('body').dialog({
                            autoOpen: false
                        });
                    });
                    setTimeout(function () {
                        var $this = createNoticeDialog(message, [{
                                text: "はい",
                                "class": "large",
                                click: function () {
                                    $this.dialog('close');
                                    then();
                                }
                            }], { icon: "/nts.uk.com.js.web/lib/nittsu/ui/style/images/error.png", text: nts.uk.resource.getText("error") });
                    }, 0);
                    return {
                        then: function (callback) {
                            then = callback;
                        }
                    };
                }
                dialog.alertError = alertError;
                function alert(text) {
                    var then = $.noop;
                    var $dialog = $('<div/>').hide();
                    $(function () {
                        $dialog.appendTo('body').dialog({
                            autoOpen: false
                        });
                    });
                    setTimeout(function () {
                        var $this = createNoticeDialog(text, [{
                                text: "はい",
                                "class": "large",
                                click: function () {
                                    $this.dialog('close');
                                    then();
                                }
                            }]);
                    }, 0);
                    return {
                        then: function (callback) {
                            then = callback;
                        }
                    };
                }
                dialog.alert = alert;
                ;
                function confirm(text) {
                    var handleYes = $.noop;
                    var handleNo = $.noop;
                    var handleCancel = $.noop;
                    var handleThen = $.noop;
                    var hasNoButton = true;
                    var hasCancelButton = false;
                    var handlers = {
                        ifYes: function (handler) {
                            handleYes = handler;
                            return handlers;
                        },
                        ifCancel: function (handler) {
                            hasNoButton = false;
                            hasCancelButton = true;
                            handleCancel = handler;
                            return handlers;
                        },
                        ifNo: function (handler) {
                            hasNoButton = true;
                            handleNo = handler;
                            return handlers;
                        },
                        then: function (handler) {
                            handleThen = handler;
                            return handlers;
                        }
                    };
                    setTimeout(function () {
                        var buttons = [];
                        buttons.push({
                            text: "はい",
                            "class": "yes large danger",
                            click: function () {
                                $this.dialog('close');
                                handleYes();
                                handleThen();
                            }
                        });
                        if (hasNoButton) {
                            buttons.push({
                                text: "いいえ",
                                "class": "no large",
                                click: function () {
                                    $this.dialog('close');
                                    handleNo();
                                    handleThen();
                                }
                            });
                        }
                        if (hasCancelButton) {
                            buttons.push({
                                text: "キャンセル",
                                "class": "cancel large",
                                click: function () {
                                    $this.dialog('close');
                                    handleCancel();
                                    handleThen();
                                }
                            });
                        }
                        var $this = createNoticeDialog(text, buttons);
                    });
                    return handlers;
                }
                dialog.confirm = confirm;
                ;
            })(dialog = ui_1.dialog || (ui_1.dialog = {}));
            var contextmenu;
            (function (contextmenu) {
                var ContextMenu = (function () {
                    function ContextMenu(selector, items, enable) {
                        this.selector = selector;
                        this.items = items;
                        this.enable = (enable !== undefined) ? enable : true;
                        this.init();
                    }
                    ContextMenu.prototype.init = function () {
                        var self = this;
                        $('body .ntsContextMenu').each(function () {
                            if ($(this).data("selector") === self.selector) {
                                $("body").off("contextmenu", self.selector);
                                $(this).remove();
                            }
                        });
                        self.guid = nts.uk.util.randomId();
                        var $contextMenu = $("<ul id='" + self.guid + "' class='ntsContextMenu'></ul>").data("selector", self.selector).hide();
                        self.createMenuItems($contextMenu);
                        $('body').append($contextMenu);
                        $("html").on("contextmenu", self.selector, function (event) {
                            if (self.enable === true) {
                                event.preventDefault();
                                self.target = event.target;
                                $contextMenu.show().position({
                                    my: "left+2 top+2",
                                    of: event,
                                    collision: "fit"
                                });
                            }
                        });
                        $("html").on("mousedown", function (event) {
                            if (!$contextMenu.is(event.target) && $contextMenu.has(event.target).length === 0) {
                                $contextMenu.hide();
                            }
                        });
                    };
                    ContextMenu.prototype.destroy = function () {
                        $("html").off("contextmenu", this.selector);
                        $("#" + this.guid).remove();
                    };
                    ContextMenu.prototype.refresh = function () {
                        this.destroy();
                        this.init();
                    };
                    ContextMenu.prototype.getItem = function (target) {
                        if (typeof target === "number") {
                            return this.items[target];
                        }
                        else if (typeof target === "string") {
                            return _.find(this.items, ["key", target]);
                        }
                        else {
                            return undefined;
                        }
                    };
                    ContextMenu.prototype.addItem = function (item) {
                        this.items.push(item);
                        this.refresh();
                    };
                    ContextMenu.prototype.removeItem = function (target) {
                        var item = this.getItem(target);
                        if (item !== undefined) {
                            _.remove(this.items, item);
                            this.refresh();
                        }
                    };
                    ContextMenu.prototype.setEnable = function (enable) {
                        this.enable = enable;
                    };
                    ContextMenu.prototype.setEnableItem = function (enable, target) {
                        var item = this.getItem(target);
                        item.enable = enable;
                        this.refresh();
                    };
                    ContextMenu.prototype.setVisibleItem = function (visible, target) {
                        var item = this.getItem(target);
                        item.visible = visible;
                        this.refresh();
                    };
                    ContextMenu.prototype.createMenuItems = function (container) {
                        var self = this;
                        _.forEach(self.items, function (item) {
                            if (item.key !== "divider") {
                                var menuClasses = "menu-item ";
                                menuClasses += (item.enable === true) ? "" : "disabled ";
                                menuClasses += (item.visible === true) ? "" : "hidden ";
                                var menuItem = $("<li class='" + menuClasses + "'><span class='menu-icon " + item.icon + "'></span>" + item.text + "</li>")
                                    .data("key", item.key)
                                    .on("click", function () {
                                    if (!$(this).hasClass("disabled")) {
                                        item.handler(self.target);
                                        container.hide();
                                    }
                                }).appendTo(container);
                            }
                            else {
                                var menuItem = $("<li class='menu-item divider'></li>").appendTo(container);
                            }
                        });
                    };
                    return ContextMenu;
                }());
                contextmenu.ContextMenu = ContextMenu;
                var ContextMenuItem = (function () {
                    function ContextMenuItem(key, text, handler, icon, visible, enable) {
                        this.key = key;
                        this.text = text;
                        this.handler = (handler !== undefined) ? handler : $.noop;
                        this.icon = (icon) ? icon : "";
                        this.visible = (visible !== undefined) ? visible : true;
                        this.enable = (enable !== undefined) ? enable : true;
                    }
                    return ContextMenuItem;
                }());
                contextmenu.ContextMenuItem = ContextMenuItem;
            })(contextmenu = ui_1.contextmenu || (ui_1.contextmenu = {}));
            ui_1.confirmSave = function (dirtyChecker) {
                var frame = windows.getSelf();
                if (frame.$dialog === undefined || frame.$dialog === null) {
                    confirmSaveWindow(dirtyChecker);
                }
                else {
                    confirmSaveDialog(dirtyChecker, frame.$dialog);
                }
            };
            function confirmSaveWindow(dirtyChecker) {
                var beforeunloadHandler = function (e) {
                    if (dirtyChecker.isDirty()) {
                        return "ban co muon save hok?";
                    }
                };
                confirmSaveEnable(beforeunloadHandler);
            }
            function confirmSaveDialog(dirtyChecker, dialog) {
                var beforeunloadHandler = function (e) {
                    if (dirtyChecker.isDirty()) {
                        e.preventDefault();
                        nts.uk.ui.dialog.confirm("Are you sure you want to leave the page?")
                            .ifYes(function () {
                            dirtyChecker.reset();
                            dialog.dialog("close");
                        }).ifNo(function () {
                        });
                    }
                };
                confirmSaveEnableDialog(beforeunloadHandler, dialog);
            }
            function confirmSaveEnableDialog(beforeunloadHandler, dialog) {
                dialog.on("dialogbeforeclose", beforeunloadHandler);
            }
            ui_1.confirmSaveEnableDialog = confirmSaveEnableDialog;
            ;
            function confirmSaveDisableDialog(dialog) {
                dialog.on("dialogbeforeclose", function () { });
            }
            ui_1.confirmSaveDisableDialog = confirmSaveDisableDialog;
            ;
            function confirmSaveEnable(beforeunloadHandler) {
                $(window).bind('beforeunload', beforeunloadHandler);
            }
            ui_1.confirmSaveEnable = confirmSaveEnable;
            ;
            function confirmSaveDisable() {
                $(window).unbind('beforeunload');
            }
            ui_1.confirmSaveDisable = confirmSaveDisable;
            ;
            var block;
            (function (block) {
                function invisible() {
                    var rect = calcRect();
                    $.blockUI({
                        message: null,
                        overlayCSS: { opacity: 0 },
                        css: {
                            width: rect.width,
                            left: rect.left
                        }
                    });
                }
                block.invisible = invisible;
                function grayout() {
                    var rect = calcRect();
                    $.blockUI({
                        message: '<div class="block-ui-message">お待ちください</div>',
                        fadeIn: 200,
                        css: {
                            width: rect.width,
                            left: rect.left
                        }
                    });
                }
                block.grayout = grayout;
                function clear() {
                    $.unblockUI({
                        fadeOut: 200
                    });
                }
                block.clear = clear;
                function calcRect() {
                    var width = 220;
                    var left = ($(window).width() - width) / 2;
                    return {
                        width: width,
                        left: left
                    };
                }
            })(block = ui_1.block || (ui_1.block = {}));
            var DirtyChecker = (function () {
                function DirtyChecker(targetViewModelObservable) {
                    this.targetViewModel = targetViewModelObservable;
                    this.initialState = this.getCurrentState();
                }
                DirtyChecker.prototype.getCurrentState = function () {
                    return ko.toJSON(this.targetViewModel());
                };
                DirtyChecker.prototype.reset = function () {
                    this.initialState = this.getCurrentState();
                };
                DirtyChecker.prototype.isDirty = function () {
                    return this.initialState !== this.getCurrentState();
                };
                return DirtyChecker;
            }());
            ui_1.DirtyChecker = DirtyChecker;
            var ig;
            (function (ig) {
                var grid;
                (function (grid) {
                    function getRowIdFrom($anyElementInRow) {
                        return $anyElementInRow.closest('tr').attr('data-id');
                    }
                    grid.getRowIdFrom = getRowIdFrom;
                    function getRowIndexFrom($anyElementInRow) {
                        return parseInt($anyElementInRow.closest('tr').attr('data-row-idx'), 10);
                    }
                    grid.getRowIndexFrom = getRowIndexFrom;
                    var virtual;
                    (function (virtual) {
                        function getDisplayContainer(gridId) {
                            return $('#' + gridId + '_displayContainer');
                        }
                        virtual.getDisplayContainer = getDisplayContainer;
                        function getVisibleRows(gridId) {
                            return $('#' + gridId + ' > tbody > tr:visible');
                        }
                        virtual.getVisibleRows = getVisibleRows;
                        function getFirstVisibleRow(gridId) {
                            var top = getDisplayContainer(gridId).scrollTop();
                            var visibleRows = getVisibleRows(gridId);
                            for (var i = 0; i < visibleRows.length; i++) {
                                var $row = $(visibleRows[i]);
                                if (visibleRows[i].offsetTop + $row.height() > top) {
                                    return $row;
                                }
                            }
                        }
                        virtual.getFirstVisibleRow = getFirstVisibleRow;
                        function getLastVisibleRow(gridId) {
                            var $displayContainer = getDisplayContainer(gridId);
                            var bottom = $displayContainer.scrollTop() + $displayContainer.height();
                            return getVisibleRows(gridId).filter(function () {
                                return this.offsetTop < bottom;
                            }).last();
                        }
                        virtual.getLastVisibleRow = getLastVisibleRow;
                    })(virtual = grid.virtual || (grid.virtual = {}));
                    var header;
                    (function (header) {
                        function getCell(gridId, columnKey) {
                            var $headers = $('#' + gridId).igGrid("headersTable");
                            return $headers.find('#' + gridId + '_' + columnKey);
                        }
                        header.getCell = getCell;
                        function getLabel(gridId, columnKey) {
                            return getCell(gridId, columnKey).find('span');
                        }
                        header.getLabel = getLabel;
                    })(header = grid.header || (grid.header = {}));
                })(grid = ig.grid || (ig.grid = {}));
            })(ig = ui_1.ig || (ui_1.ig = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var option;
            (function (option_1) {
                var EditorOptionBase = (function () {
                    function EditorOptionBase() {
                    }
                    return EditorOptionBase;
                }());
                option_1.EditorOptionBase = EditorOptionBase;
                var TextEditorOption = (function (_super) {
                    __extends(TextEditorOption, _super);
                    function TextEditorOption(option) {
                        _super.call(this);
                        this.textmode = (option !== undefined && option.textmode !== undefined) ? option.textmode : "text";
                        this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
                        this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
                        this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "";
                        this.autofill = (option !== undefined && option.autofill !== undefined) ? option.autofill : false;
                        this.filldirection = (option !== undefined && option.filldirection !== undefined) ? option.filldirection : "right";
                        this.fillcharacter = (option !== undefined && option.fillcharacter !== undefined) ? option.fillcharacter : "0";
                    }
                    return TextEditorOption;
                }(EditorOptionBase));
                option_1.TextEditorOption = TextEditorOption;
                var TimeEditorOption = (function (_super) {
                    __extends(TimeEditorOption, _super);
                    function TimeEditorOption(option) {
                        _super.call(this);
                        this.inputFormat = (option !== undefined && option.inputFormat !== undefined) ? option.inputFormat : "date";
                        this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
                        this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
                        this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "right";
                    }
                    return TimeEditorOption;
                }(EditorOptionBase));
                option_1.TimeEditorOption = TimeEditorOption;
                var NumberEditorOption = (function (_super) {
                    __extends(NumberEditorOption, _super);
                    function NumberEditorOption(option) {
                        _super.call(this);
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
                    return NumberEditorOption;
                }(EditorOptionBase));
                option_1.NumberEditorOption = NumberEditorOption;
                var CurrencyEditorOption = (function (_super) {
                    __extends(CurrencyEditorOption, _super);
                    function CurrencyEditorOption(option) {
                        _super.call(this);
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
                    return CurrencyEditorOption;
                }(NumberEditorOption));
                option_1.CurrencyEditorOption = CurrencyEditorOption;
                function getCurrencyPosition(currencyformat) {
                    return currenryPosition[currencyformat] === null ? "right" : currenryPosition[currencyformat];
                }
                var MultilineEditorOption = (function (_super) {
                    __extends(MultilineEditorOption, _super);
                    function MultilineEditorOption(option) {
                        _super.call(this);
                        this.resizeable = (option !== undefined && option.resizeable !== undefined) ? option.resizeable : false;
                        this.placeholder = (option !== undefined && option.placeholder !== undefined) ? option.placeholder : "";
                        this.width = (option !== undefined && option.width !== undefined) ? option.width : "";
                        this.textalign = (option !== undefined && option.textalign !== undefined) ? option.textalign : "";
                    }
                    return MultilineEditorOption;
                }(EditorOptionBase));
                option_1.MultilineEditorOption = MultilineEditorOption;
                var currenryPosition = {
                    "JPY": "left",
                    "USD": "right"
                };
            })(option = ui.option || (ui.option = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_2) {
            var jqueryExtentions;
            (function (jqueryExtentions) {
                var ntsFileUpload;
                (function (ntsFileUpload) {
                    $.fn.ntsFileUpload = function (option) {
                        var dfd = $.Deferred();
                        var file;
                        if ($(this).find("input[type='file']").length == 0) {
                            file = $(this)[0].files;
                        }
                        else {
                            file = $(this).find("input[type='file']")[0].files;
                        }
                        if (file) {
                            var formData = new FormData();
                            formData.append("stereotype", option.stereoType);
                            formData.append("userfile", file[0]);
                            formData.append("filename", file[0].name);
                            if (file[0]) {
                                return nts.uk.request.uploadFile(formData, option);
                            }
                            else {
                                dfd.reject({ message: "please select file", messageId: "-1" });
                                return dfd.promise();
                            }
                        }
                        else {
                            dfd.reject({ messageId: "0", message: "can not find control" });
                        }
                        return dfd.promise();
                    };
                })(ntsFileUpload || (ntsFileUpload = {}));
                var ntsError;
                (function (ntsError) {
                    var DATA_HAS_ERROR = 'hasError';
                    var DATA_GET_ERROR = 'getError';
                    $.fn.ntsError = function (action, message) {
                        var $control = $(this);
                        if (action === DATA_HAS_ERROR) {
                            return _.some($control, function (c) { return hasError($(c)); });
                        }
                        else if (action === DATA_GET_ERROR) {
                            return getErrorByElement($control.first());
                        }
                        else {
                            $control.each(function (index) {
                                var $item = $(this);
                                $item = processErrorOnItem($item, message, action);
                            });
                            return $control;
                        }
                    };
                    function processErrorOnItem($control, message, action) {
                        switch (action) {
                            case 'set':
                                return setError($control, message);
                            case 'clear':
                                return clearErrors($control);
                        }
                    }
                    function getErrorByElement($control) {
                        return ui.errors.getErrorByElement($control);
                    }
                    function setError($control, message) {
                        $control.data(DATA_HAS_ERROR, true);
                        ui.errors.add({
                            location: $control.data('name') || "",
                            message: message,
                            $control: $control
                        });
                        $control.parent().addClass('error');
                        return $control;
                    }
                    function clearErrors($control) {
                        $control.data(DATA_HAS_ERROR, false);
                        ui.errors.removeByElement($control);
                        $control.parent().removeClass('error');
                        return $control;
                    }
                    function hasError($control) {
                        return $control.data(DATA_HAS_ERROR) === true;
                    }
                })(ntsError || (ntsError = {}));
                var ntsPopup;
                (function (ntsPopup) {
                    var DATA_INSTANCE_NAME = 'nts-popup-panel';
                    $.fn.ntsPopup = function () {
                        if (arguments.length === 1) {
                            var p = arguments[0];
                            if (_.isPlainObject(p)) {
                                return init.apply(this, arguments);
                            }
                        }
                        if (typeof arguments[0] === 'string') {
                            return handleMethod.apply(this, arguments);
                        }
                    };
                    function init(param) {
                        var popup = new NtsPopupPanel($(this), param.position);
                        var dismissible = param.dismissible === false;
                        _.defer(function () {
                            if (!dismissible) {
                                $(window).mousedown(function (e) {
                                    if ($(e.target).closest(popup.$panel).length === 0) {
                                        popup.hide();
                                    }
                                });
                            }
                        });
                        return popup.$panel;
                    }
                    function handleMethod() {
                        var methodName = arguments[0];
                        var popup = $(this).data(DATA_INSTANCE_NAME);
                        switch (methodName) {
                            case 'show':
                                popup.show();
                                break;
                            case 'hide':
                                popup.hide();
                                break;
                            case 'destroy':
                                popup.hide();
                                popup.destroy();
                                break;
                            case 'toggle':
                                popup.toggle();
                                break;
                        }
                    }
                    var NtsPopupPanel = (function () {
                        function NtsPopupPanel($panel, position) {
                            this.position = position;
                            var parent = $panel.parent();
                            this.$panel = $panel
                                .data(DATA_INSTANCE_NAME, this)
                                .addClass('popup-panel')
                                .appendTo(parent);
                            this.$panel.css("z-index", 100);
                        }
                        NtsPopupPanel.prototype.show = function () {
                            this.$panel
                                .css({
                                visibility: 'hidden',
                                display: 'block'
                            })
                                .position(this.position)
                                .css({
                                visibility: 'visible'
                            });
                        };
                        NtsPopupPanel.prototype.hide = function () {
                            this.$panel.css({
                                display: 'none'
                            });
                        };
                        NtsPopupPanel.prototype.destroy = function () {
                            this.$panel = null;
                        };
                        NtsPopupPanel.prototype.toggle = function () {
                            var isDisplaying = this.$panel.css("display");
                            if (isDisplaying === 'none') {
                                this.show();
                            }
                            else {
                                this.hide();
                            }
                        };
                        return NtsPopupPanel;
                    }());
                })(ntsPopup || (ntsPopup = {}));
                var ntsGridList;
                (function (ntsGridList) {
                    var OUTSIDE_AUTO_SCROLL_SPEED = {
                        RATIO: 0.2,
                        MAX: 30
                    };
                    $.fn.ntsGridList = function (action, param) {
                        var $grid = $(this);
                        switch (action) {
                            case 'setupSelecting':
                                return setupSelecting($grid);
                            case 'unsetupSelecting':
                                return unsetupSelecting($grid);
                            case 'getSelected':
                                return getSelected($grid);
                            case 'setSelected':
                                return setSelected($grid, param);
                            case 'deselectAll':
                                return deselectAll($grid);
                            case 'setupDeleteButton':
                                return setupDeleteButton($grid, param);
                        }
                    };
                    $.fn.ntsGridListFeature = function (feature, action) {
                        var params = [];
                        for (var _i = 2; _i < arguments.length; _i++) {
                            params[_i - 2] = arguments[_i];
                        }
                        var $grid = $(this);
                        switch (feature) {
                            case 'switch':
                                switch (action) {
                                    case 'setValue':
                                        return setSwitchValue($grid, params);
                                }
                        }
                    };
                    function setSwitchValue($grid) {
                        var params = [];
                        for (var _i = 1; _i < arguments.length; _i++) {
                            params[_i - 1] = arguments[_i];
                        }
                        var rowId = params[0][0];
                        var columnKey = params[0][1];
                        var selectedValue = params[0][2];
                        var $row = $($grid.igGrid("rowById", rowId));
                        var $parent = $row.find(".ntsControl");
                        var currentSelect = $parent.attr('data-value');
                        if (selectedValue !== currentSelect) {
                            var rowKey = $row.attr("data-id");
                            $parent.find(".nts-switch-button").removeClass("selected");
                            var element = _.find($parent.find(".nts-switch-button"), function (e) {
                                return selectedValue.toString() === $(e).attr('data-value').toString();
                            });
                            if (element !== undefined) {
                                $(element).addClass('selected');
                                $parent.attr('data-value', selectedValue);
                                $grid.igGridUpdating("setCellValue", rowKey, columnKey, selectedValue);
                                $grid.igGrid("commit");
                                if ($grid.igGrid("hasVerticalScrollbar")) {
                                    var current = $grid.ntsGridList("getSelected");
                                    if (current !== undefined) {
                                        $grid.igGrid("virtualScrollTo", (typeof current === 'object' ? current.index : current[0].index) + 1);
                                    }
                                }
                            }
                        }
                    }
                    function getSelected($grid) {
                        if ($grid.igGridSelection('option', 'multipleSelection')) {
                            var selectedRows = $grid.igGridSelection('selectedRows');
                            if (selectedRows)
                                return _.map(selectedRows, convertSelected);
                            return [];
                        }
                        else {
                            var selectedRow = $grid.igGridSelection('selectedRow');
                            if (selectedRow)
                                return convertSelected(selectedRow);
                            return undefined;
                        }
                    }
                    function convertSelected(igGridSelectedRow) {
                        return {
                            id: igGridSelectedRow.id,
                            index: igGridSelectedRow.index
                        };
                    }
                    function setSelected($grid, selectedId) {
                        deselectAll($grid);
                        if ($grid.igGridSelection('option', 'multipleSelection')) {
                            selectedId.forEach(function (id) { return $grid.igGridSelection('selectRowById', id); });
                        }
                        else {
                            $grid.igGridSelection('selectRowById', selectedId);
                        }
                    }
                    function deselectAll($grid) {
                        $grid.igGridSelection('clearSelection');
                    }
                    function setupDeleteButton($grid, param) {
                        var itemDeletedEvent = new CustomEvent("itemDeleted", {
                            detail: {},
                        });
                        var currentColumns = $grid.igGrid("option", "columns");
                        currentColumns.push({
                            dataType: "bool", columnCssClass: "delete-column", headerText: "test", key: param.deleteField,
                            width: 60, formatter: function createButton(deleteField, row) {
                                var primaryKey = $grid.igGrid("option", "primaryKey");
                                var result = $('<button tabindex="-1" class="small delete-button">Delete</button>');
                                result.attr("data-value", row[primaryKey]);
                                if (deleteField === true && primaryKey !== null && !uk.util.isNullOrUndefined(row[primaryKey])) {
                                    return result[0].outerHTML;
                                }
                                else {
                                    return result.attr("disabled", "disabled")[0].outerHTML;
                                }
                            }
                        });
                        $grid.igGrid("option", "columns", currentColumns);
                        $grid.on("click", ".delete-button", function () {
                            var key = $(this).attr("data-value");
                            var primaryKey = $grid.igGrid("option", "primaryKey");
                            var source = _.cloneDeep($grid.igGrid("option", "dataSource"));
                            _.remove(source, function (current) {
                                return _.isEqual(current[primaryKey].toString(), key.toString());
                            });
                            if (!uk.util.isNullOrUndefined(param.sourceTarget) && typeof param.sourceTarget === "function") {
                                param.sourceTarget(source);
                            }
                            else {
                                $grid.igGrid("option", "dataSource", source);
                                $grid.igGrid("dataBind");
                            }
                            itemDeletedEvent.detail["target"] = key;
                            document.getElementById($grid.attr('id')).dispatchEvent(itemDeletedEvent);
                        });
                    }
                    function setupSelecting($grid) {
                        setupDragging($grid);
                        setupSelectingEvents($grid);
                        return $grid;
                    }
                    function unsetupSelecting($grid) {
                        unsetupDragging($grid);
                        unsetupSelectingEvents($grid);
                        return $grid;
                    }
                    function setupDragging($grid) {
                        var dragSelectRange = [];
                        var mousePos = null;
                        $grid.bind('mousedown', function (e) {
                            var $container = $grid.closest('.ui-iggrid-scrolldiv');
                            if ($(e.target).closest('.ui-iggrid-table').length === 0) {
                                return;
                            }
                            var gridVerticalRange = new uk.util.Range($container.offset().top, $container.offset().top + $container.height());
                            mousePos = {
                                x: e.pageX,
                                y: e.pageY,
                                rowIndex: ui_2.ig.grid.getRowIndexFrom($(e.target))
                            };
                            dragSelectRange.push(mousePos.rowIndex);
                            var $scroller = $('#' + $grid.attr('id') + '_scrollContainer');
                            var timerAutoScroll = setInterval(function () {
                                var distance = gridVerticalRange.distanceFrom(mousePos.y);
                                if (distance === 0) {
                                    return;
                                }
                                var delta = Math.min(distance * OUTSIDE_AUTO_SCROLL_SPEED.RATIO, OUTSIDE_AUTO_SCROLL_SPEED.MAX);
                                var currentScrolls = $scroller.scrollTop();
                                $grid.igGrid('virtualScrollTo', (currentScrolls + delta) + 'px');
                            }, 20);
                            $(window).bind('mousemove.NtsGridListDragging', function (e) {
                                var newPointedRowIndex = ui_2.ig.grid.getRowIndexFrom($(e.target));
                                if (mousePos.rowIndex === newPointedRowIndex) {
                                    return;
                                }
                                mousePos = {
                                    x: e.pageX,
                                    y: e.pageY,
                                    rowIndex: newPointedRowIndex
                                };
                                if (dragSelectRange.length === 1 && !e.ctrlKey) {
                                    $grid.igGridSelection('clearSelection');
                                }
                                updateSelections();
                            });
                            $(window).one('mouseup', function (e) {
                                mousePos = null;
                                dragSelectRange = [];
                                $(window).unbind('mousemove.NtsGridListDragging');
                                if ($grid.data("selectUpdated") === true) {
                                    $grid.triggerHandler('selectionchanged');
                                }
                                clearInterval(timerAutoScroll);
                                $grid.data("selectUpdated", false);
                            });
                        });
                        function updateSelections() {
                            if (isNaN(mousePos.rowIndex)) {
                                return;
                            }
                            for (var i = 0, i_len = dragSelectRange.length; i < i_len; i++) {
                                $grid.igGridSelection('deselectRow', dragSelectRange[i]);
                            }
                            var newDragSelectRange = [];
                            if (dragSelectRange[0] <= mousePos.rowIndex) {
                                for (var j = dragSelectRange[0]; j <= mousePos.rowIndex; j++) {
                                    $grid.igGridSelection('selectRow', j);
                                    newDragSelectRange.push(j);
                                }
                            }
                            else if (dragSelectRange[0] > mousePos.rowIndex) {
                                for (var j = dragSelectRange[0]; j >= mousePos.rowIndex; j--) {
                                    $grid.igGridSelection('selectRow', j);
                                    newDragSelectRange.push(j);
                                }
                            }
                            dragSelectRange = newDragSelectRange;
                            $grid.data("selectUpdated", true);
                        }
                    }
                    function setupSelectingEvents($grid) {
                        $grid.bind('iggridselectioncellselectionchanging', function () {
                        });
                        $grid.bind('iggridselectionrowselectionchanged', function () {
                            $grid.triggerHandler('selectionchanged');
                        });
                    }
                    function unsetupDragging($grid) {
                        $grid.unbind('mousedown');
                    }
                    function unsetupSelectingEvents($grid) {
                        $grid.unbind('iggridselectionrowselectionchanged');
                    }
                })(ntsGridList || (ntsGridList = {}));
                var ntsTreeView;
                (function (ntsTreeView) {
                    var OUTSIDE_AUTO_SCROLL_SPEED = {
                        RATIO: 0.2,
                        MAX: 30
                    };
                    $.fn.ntsTreeView = function (action, param) {
                        var $tree = $(this);
                        switch (action) {
                            case 'getSelected':
                                return getSelected($tree);
                            case 'setSelected':
                                return setSelected($tree, param);
                            case 'deselectAll':
                                return deselectAll($tree);
                        }
                    };
                    function getSelected($tree) {
                        if ($tree.igTreeGridSelection('option', 'multipleSelection')) {
                            var selectedRows = $tree.igTreeGridSelection('selectedRows');
                            if (selectedRows)
                                return _.map(selectedRows, convertSelected);
                            return [];
                        }
                        else {
                            var selectedRow = $tree.igTreeGridSelection('selectedRow');
                            if (selectedRow)
                                return convertSelected(selectedRow);
                            return undefined;
                        }
                    }
                    function convertSelected(selectedRow) {
                        return {
                            id: selectedRow.id,
                            index: selectedRow.index
                        };
                    }
                    function setSelected($tree, selectedId) {
                        deselectAll($tree);
                        if ($tree.igTreeGridSelection('option', 'multipleSelection')) {
                            selectedId.forEach(function (id) { return $tree.igTreeGridSelection('selectRowById', id); });
                        }
                        else {
                            $tree.igTreeGridSelection('selectRowById', selectedId);
                        }
                    }
                    function deselectAll($grid) {
                        $grid.igTreeGridSelection('clearSelection');
                    }
                })(ntsTreeView || (ntsTreeView = {}));
                var ntsListBox;
                (function (ntsListBox) {
                    $.fn.ntsListBox = function (action) {
                        var $grid = $(this);
                        switch (action) {
                            case 'deselectAll':
                                deselectAll($grid);
                                break;
                            case 'selectAll':
                                selectAll($grid);
                            default:
                                break;
                        }
                    };
                    function selectAll($list) {
                        var $grid = $list.find(".ntsListBox");
                        var options = $grid.igGrid("option", "dataSource");
                        var primaryKey = $grid.igGrid("option", "primaryKey");
                        _.forEach(options, function (option, idx) {
                            $grid.igGridSelection("selectRowById", option[primaryKey]);
                        });
                        $grid.triggerHandler('selectionchanged');
                    }
                    function deselectAll($list) {
                        var $grid = $list.find(".ntsListBox");
                        $grid.igGridSelection('clearSelection');
                        $grid.triggerHandler('selectionchanged');
                    }
                })(ntsListBox || (ntsListBox = {}));
                var ntsEditor;
                (function (ntsEditor) {
                    $.fn.ntsEditor = function (action) {
                        var $editor = $(this);
                        switch (action) {
                            case 'validate':
                                validate($editor);
                            default:
                                break;
                        }
                    };
                    function validate($editor) {
                        var validateEvent = new CustomEvent("validate", {});
                        $editor.each(function (index) {
                            var $input = $(this);
                            document.getElementById($input.attr('id')).dispatchEvent(validateEvent);
                        });
                    }
                })(ntsEditor || (ntsEditor = {}));
                var ntsWizard;
                (function (ntsWizard) {
                    $.fn.ntsWizard = function (action, index) {
                        var $wizard = $(this);
                        if (action === "begin") {
                            return begin($wizard);
                        }
                        else if (action === "end") {
                            return end($wizard);
                        }
                        else if (action === "goto") {
                            return goto($wizard, index);
                        }
                        else if (action === "prev") {
                            return prev($wizard);
                        }
                        else if (action === "next") {
                            return next($wizard);
                        }
                        else if (action === "getCurrentStep") {
                            return getCurrentStep($wizard);
                        }
                        else {
                            return $wizard;
                        }
                        ;
                    };
                    function begin(wizard) {
                        wizard.setStep(0);
                        return wizard;
                    }
                    function end(wizard) {
                        wizard.setStep(wizard.data("length") - 1);
                        return wizard;
                    }
                    function goto(wizard, index) {
                        wizard.setStep(index);
                        return wizard;
                    }
                    function prev(wizard) {
                        wizard.steps("previous");
                        return wizard;
                    }
                    function next(wizard) {
                        wizard.steps("next");
                        return wizard;
                    }
                    function getCurrentStep(wizard) {
                        return wizard.steps("getCurrentIndex");
                    }
                })(ntsWizard || (ntsWizard = {}));
                var ntsUserGuide;
                (function (ntsUserGuide) {
                    $.fn.ntsUserGuide = function (action) {
                        var $controls = $(this);
                        if (nts.uk.util.isNullOrUndefined(action) || action === "init") {
                            return init($controls);
                        }
                        else if (action === "destroy") {
                            return destroy($controls);
                        }
                        else if (action === "show") {
                            return show($controls);
                        }
                        else if (action === "hide") {
                            return hide($controls);
                        }
                        else if (action === "toggle") {
                            return toggle($controls);
                        }
                        else if (action === "isShow") {
                            return isShow($controls);
                        }
                        else {
                            return $controls;
                        }
                        ;
                    };
                    function init(controls) {
                        controls.each(function () {
                            var $control = $(this);
                            $control.remove();
                            if (!$control.hasClass("ntsUserGuide"))
                                $control.addClass("ntsUserGuide");
                            $($control).appendTo($("body")).show();
                            var target = $control.data('target');
                            var direction = $control.data('direction');
                            $control.children().each(function () {
                                var $box = $(this);
                                var boxDirection = $box.data("direction");
                                $box.addClass("userguide-box caret-" + getReveseDirection(boxDirection) + " caret-overlay");
                            });
                            var $overlay = $("<div class='userguide-overlay'></div>")
                                .addClass("overlay-" + direction)
                                .appendTo($control);
                            $control.hide();
                        });
                        $("html").on("mouseup keypress", { controls: controls }, hideBinding);
                        return controls;
                    }
                    function destroy(controls) {
                        controls.each(function () {
                            $(this).remove();
                        });
                        $("html").off("mouseup keypress", hideBinding);
                        return controls;
                    }
                    function hideBinding(e) {
                        e.data.controls.each(function () {
                            $(this).hide();
                        });
                        return e.data.controls;
                    }
                    function show(controls) {
                        controls.each(function () {
                            var $control = $(this);
                            $control.show();
                            var target = $control.data('target');
                            var direction = $control.data('direction');
                            $control.find(".userguide-overlay").each(function (index, elem) {
                                calcOverlayPosition($(elem), target, direction);
                            });
                            $control.children().each(function () {
                                var $box = $(this);
                                var boxTarget = $box.data("target");
                                var boxDirection = $box.data("direction");
                                var boxMargin = ($box.data("margin")) ? $box.data("margin") : "20";
                                calcBoxPosition($box, boxTarget, boxDirection, boxMargin);
                            });
                        });
                        return controls;
                    }
                    function hide(controls) {
                        controls.each(function () {
                            $(this).hide();
                        });
                        return controls;
                    }
                    function toggle(controls) {
                        if (isShow(controls))
                            hide(controls);
                        else
                            show(controls);
                        return controls;
                    }
                    function isShow(controls) {
                        var result = true;
                        controls.each(function () {
                            if (!$(this).is(":visible"))
                                result = false;
                        });
                        return result;
                    }
                    function calcOverlayPosition(overlay, target, direction) {
                        if (direction === "left")
                            return overlay.css("right", "auto")
                                .css("width", $(target).offset().left);
                        else if (direction === "right")
                            return overlay.css("left", $(target).offset().left + $(target).outerWidth());
                        else if (direction === "top")
                            return overlay.css("position", "absolute")
                                .css("bottom", "auto")
                                .css("height", $(target).offset().top);
                        else if (direction === "bottom")
                            return overlay.css("position", "absolute")
                                .css("top", $(target).offset().top + $(target).outerHeight())
                                .css("height", $("body").height() - $(target).offset().top);
                    }
                    function calcBoxPosition(box, target, direction, margin) {
                        var operation = "+";
                        if (direction === "left" || direction === "top")
                            operation = "-";
                        return box.position({
                            my: getReveseDirection(direction) + operation + margin,
                            at: direction,
                            of: target,
                            collision: "none"
                        });
                    }
                    function getReveseDirection(direction) {
                        if (direction === "left")
                            return "right";
                        else if (direction === "right")
                            return "left";
                        else if (direction === "top")
                            return "bottom";
                        else if (direction === "bottom")
                            return "top";
                    }
                })(ntsUserGuide || (ntsUserGuide = {}));
                var ntsSearchBox;
                (function (ntsSearchBox) {
                    $.fn.setupSearchScroll = function (controlType, virtualization) {
                        var $control = this;
                        if (controlType.toLowerCase() == 'iggrid')
                            return setupIgGridScroll($control, virtualization);
                        if (controlType.toLowerCase() == 'igtreegrid')
                            return setupTreeGridScroll($control, virtualization);
                        if (controlType.toLowerCase() == 'igtree')
                            return setupIgTreeScroll($control);
                        return this;
                    };
                    function setupIgGridScroll($control, virtualization) {
                        var $grid = $control;
                        if (virtualization) {
                            $grid.on("selectChange", function () {
                                var row = null;
                                var selectedRows = $grid.igGrid("selectedRows");
                                if (selectedRows) {
                                    row = selectedRows[0];
                                }
                                else {
                                    row = $grid.igGrid("selectedRow");
                                }
                                if (row) {
                                    var index = $(row.element).attr("data-row-idx");
                                    $grid.igGrid("virtualScrollTo", index === undefined ? getSelectRowIndex($grid, row.id) : parseInt(index));
                                }
                            });
                        }
                        else {
                            $grid.on("selectChange", function () {
                                var row = null;
                                var selectedRows = $grid.igGrid("selectedRows");
                                if (selectedRows) {
                                    row = selectedRows[0];
                                }
                                else {
                                    row = $grid.igGrid("selectedRow");
                                }
                                if (row) {
                                    var index = row.index;
                                    var height = row.element[0].scrollHeight;
                                    var gridId = $grid.attr('id');
                                    $("#" + gridId + "_scrollContainer").scrollTop(index * height);
                                }
                            });
                        }
                        return $grid;
                    }
                    function getSelectRowIndex($grid, selectedValue) {
                        var dataSource = $grid.igGrid("option", "dataSource");
                        var primaryKey = $grid.igGrid("option", "primaryKey");
                        return _.findIndex(dataSource, function (s) { return s[primaryKey].toString() === selectedValue.toString(); });
                    }
                    function setupTreeGridScroll($control, virtualization) {
                        var $treegrid = $control;
                        var id = $treegrid.attr('id');
                        $treegrid.on("selectChange", function () {
                            var row = null;
                            var selectedRows = $treegrid.igTreeGridSelection("selectedRows");
                            if (selectedRows) {
                                row = selectedRows[0];
                            }
                            else {
                                row = $treegrid.igTreeGridSelection("selectedRow");
                            }
                            if (row) {
                                var index = row.index;
                                var height = row.element[0].scrollHeight;
                                $("#" + id + "_scroll").scrollTop(index * height);
                            }
                        });
                        return $treegrid;
                    }
                    function setupIgTreeScroll($control) {
                        return $control;
                    }
                })(ntsSearchBox || (ntsSearchBox = {}));
                var ntsSideBar;
                (function (ntsSideBar) {
                    $.fn.ntsSideBar = function (action, index) {
                        var $control = $(this);
                        if (nts.uk.util.isNullOrUndefined(action) || action === "init") {
                            return init($control);
                        }
                        else if (action === "active") {
                            return active($control, index);
                        }
                        else if (action === "enable") {
                            return enable($control, index);
                        }
                        else if (action === "disable") {
                            return disable($control, index);
                        }
                        else if (action === "show") {
                            return show($control, index);
                        }
                        else if (action === "hide") {
                            return hide($control, index);
                        }
                        else if (action === "getCurrent") {
                            return getCurrent($control);
                        }
                        else {
                            return $control;
                        }
                        ;
                    };
                    function init(control) {
                        $("html").addClass("sidebar-html");
                        control.find("div[role=tabpanel]").hide();
                        control.on("click", "#sidebar-area .navigator a", function (e) {
                            e.preventDefault();
                            if ($(this).attr("disabled") !== "true" &&
                                $(this).attr("disabled") !== "disabled" &&
                                $(this).attr("href") !== undefined) {
                                active(control, $(this).closest("li").index());
                            }
                        });
                        control.find("#sidebar-area .navigator a.active").trigger('click');
                        return control;
                    }
                    function active(control, index) {
                        control.find("#sidebar-area .navigator a").removeClass("active");
                        control.find("#sidebar-area .navigator a").eq(index).addClass("active");
                        control.find("div[role=tabpanel]").hide();
                        $(control.find("#sidebar-area .navigator a").eq(index).attr("href")).show();
                        return control;
                    }
                    function enable(control, index) {
                        control.find("#sidebar-area .navigator a").eq(index).removeAttr("disabled");
                        return control;
                    }
                    function disable(control, index) {
                        control.find("#sidebar-area .navigator a").eq(index).attr("disabled", "disabled");
                        return control;
                    }
                    function show(control, index) {
                        control.find("#sidebar-area .navigator a").eq(index).show();
                        return control;
                    }
                    function hide(control, index) {
                        var current = getCurrent(control);
                        if (current === index) {
                            active(control, 0);
                        }
                        control.find("#sidebar-area .navigator a").eq(index).hide();
                        return control;
                    }
                    function getCurrent(control) {
                        var index = 0;
                        index = control.find("#sidebar-area .navigator a.active").closest("li").index();
                        return index;
                    }
                })(ntsSideBar || (ntsSideBar = {}));
                var ntsGrid;
                (function (ntsGrid) {
                    $.fn.ntsGrid = function (options) {
                        var self = this;
                        if (typeof options === "string") {
                            functions.ntsAction($(self), options, [].slice.call(arguments).slice(1));
                            return;
                        }
                        if (options.ntsControls === undefined) {
                            $(this).igGrid(options);
                            return;
                        }
                        validation.scanValidators($(self), options.columns);
                        var cellFormatter = new color.CellFormatter($(this), options.ntsFeatures);
                        var columnControlTypes = {};
                        var columnSpecialTypes = {};
                        var formatColumn = function (column) {
                            if (!uk.util.isNullOrUndefined(column.group)) {
                                var cols = _.map(column.group, formatColumn);
                                column.group = cols;
                                return column;
                            }
                            specialColumn.ifTrue(columnSpecialTypes, column);
                            if (column.ntsControl === undefined) {
                                columnControlTypes[column.key] = ntsControls.TEXTBOX;
                                return cellFormatter.format(column);
                            }
                            if (column.ntsControl === ntsControls.LABEL) {
                                ntsControls.drawLabel($(self), column);
                                columnControlTypes[column.key] = ntsControls.LABEL;
                                return cellFormatter.format(column);
                            }
                            var controlDef = _.find(options.ntsControls, function (ctl) {
                                return ctl.name === column.ntsControl;
                            });
                            if (!uk.util.isNullOrUndefined(controlDef))
                                columnControlTypes[column.key] = controlDef.controlType;
                            else {
                                columnControlTypes[column.key] = ntsControls.TEXTBOX;
                                return cellFormatter.format(column);
                            }
                            var $self = $(self);
                            column.formatter = function (value, rowObj) {
                                if (uk.util.isNullOrUndefined(rowObj))
                                    return value;
                                var rowId = rowObj[$self.igGrid("option", "primaryKey")];
                                var update = function (val) {
                                    if ($self.data("igGrid") !== null) {
                                        $self.igGridUpdating("setCellValue", rowId, column.key, column.dataType !== 'string' ? val : val.toString());
                                        if (options.autoCommit === undefined || options.autoCommit === false) {
                                            var updatedRow = $self.igGrid("rowById", rowId, false);
                                            $self.igGrid("commit");
                                            if (updatedRow !== undefined)
                                                $self.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                                        }
                                    }
                                };
                                var deleteRow = function () {
                                    if ($self.data("igGrid") !== null)
                                        $self.data("igGridUpdating").deleteRow(rowId);
                                };
                                var ntsControl = ntsControls.getControl(controlDef.controlType);
                                var cell = $self.igGrid("cellById", rowId, column.key);
                                var isEnable = $(cell).find("." + ntsControl.containerClass()).data("enable");
                                isEnable = isEnable !== undefined ? isEnable : controlDef.enable === undefined ? true : controlDef.enable;
                                var data = {
                                    controlDef: controlDef,
                                    update: update,
                                    deleteRow: deleteRow,
                                    initValue: value,
                                    enable: isEnable
                                };
                                var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                                var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", "35px"));
                                var $_self = $self;
                                setTimeout(function () {
                                    var $self = $_self;
                                    var $gridCell = $self.igGrid("cellById", rowObj[$self.igGrid("option", "primaryKey")], column.key);
                                    if ($($gridCell.children()[0]).children().length === 0)
                                        $("." + controlCls).append(ntsControl.draw(data));
                                    ntsControl.$containedGrid = $self;
                                }, 0);
                                return $container.html();
                            };
                            return column;
                        };
                        var columns = _.map(options.columns, formatColumn);
                        options.columns = columns;
                        updating.addFeature(options);
                        options.autoCommit = true;
                        copyPaste.ifOn($(self), options);
                        events.afterRendered(options);
                        columnSize.init($(self), options.columns);
                        $(this).data(internal.CONTROL_TYPES, columnControlTypes);
                        $(this).data(internal.SPECIAL_COL_TYPES, columnSpecialTypes);
                        sheet.load.setup($(self), options);
                        $(this).igGrid(options);
                    };
                    var feature;
                    (function (feature_1) {
                        feature_1.UPDATING = "Updating";
                        feature_1.SELECTION = "Selection";
                        feature_1.COLUMN_FIX = "ColumnFixing";
                        feature_1.COPY_PASTE = "CopyPaste";
                        feature_1.CELL_EDIT = "CellEdit";
                        feature_1.CELL_COLOR = "CellColor";
                        feature_1.HIDING = "Hiding";
                        feature_1.SHEET = "Sheet";
                        function replaceBy(options, featureName, newFeature) {
                            var replaceId;
                            _.forEach(options.features, function (feature, id) {
                                if (feature.name === featureName) {
                                    replaceId = id;
                                    return false;
                                }
                            });
                            options.features.splice(replaceId, 1, newFeature);
                        }
                        feature_1.replaceBy = replaceBy;
                        function isEnable(features, name) {
                            return _.find(features, function (feature) {
                                return feature.name === name;
                            }) !== undefined;
                        }
                        feature_1.isEnable = isEnable;
                        function find(features, name) {
                            return _.find(features, function (feature) {
                                return feature.name === name;
                            });
                        }
                        feature_1.find = find;
                    })(feature || (feature = {}));
                    var updating;
                    (function (updating) {
                        function addFeature(options) {
                            var updateFeature = createUpdateOptions(options);
                            if (!feature.isEnable(options.features, feature.UPDATING)) {
                                options.features.push(updateFeature);
                            }
                            else {
                                feature.replaceBy(options, feature.UPDATING, createUpdateOptions(options));
                            }
                        }
                        updating.addFeature = addFeature;
                        function createUpdateOptions(options) {
                            var updateFeature = { name: feature.UPDATING, enableAddRow: false, enableDeleteRow: false, editMode: 'none' };
                            if (feature.isEnable(options.ntsFeatures, feature.CELL_EDIT)) {
                                updateFeature.editMode = "cell";
                                updateFeature.editCellStarting = startEditCell;
                                updateFeature.editCellEnding = beforeFinishEditCell;
                            }
                            return updateFeature;
                        }
                        function containsNtsControl($target) {
                            var td = $target;
                            if (!$target.prev().is("td"))
                                td = $target.closest("td");
                            return td.find("div[class*='nts-grid-control']").length > 0;
                        }
                        updating.containsNtsControl = containsNtsControl;
                        function startEditCell(evt, ui) {
                            if (containsNtsControl($(evt.currentTarget)) || utils.isEnterKey(evt) || utils.isTabKey(evt)) {
                                var selectedCell = selection.getSelectedCell($(evt.target));
                                if (uk.util.isNullOrUndefined(selectedCell) || !utils.selectable($(evt.target)))
                                    return;
                                $(evt.target).igGridSelection("selectCell", selectedCell.rowIndex, selectedCell.index, utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($(evt.target))));
                                return false;
                            }
                            return true;
                        }
                        function onEditCell(evt, cell) {
                            var $grid = fixedColumns.realGridOf($(evt.currentTarget));
                            if (!utils.isEditMode($grid))
                                return;
                            var validators = $grid.data(validation.VALIDATORS);
                            var fieldValidator = validators[cell.columnKey];
                            if (uk.util.isNullOrUndefined(fieldValidator))
                                return;
                            var cellValue = $(cell.element).find("input:first").val();
                            var result = fieldValidator.probe(cellValue);
                            var $cellContainer = $(cell.element).find("div:first");
                            $cellContainer.ntsError("clear");
                            $cellContainer.css("border-color", "inherit");
                            if (!result.isValid) {
                                $cellContainer.ntsError("set", result.errorMessage);
                                $cellContainer.css("border-color", "#ff6666");
                            }
                        }
                        updating.onEditCell = onEditCell;
                        function triggerCellUpdate(evt, cell) {
                            var grid = evt.currentTarget;
                            var $targetGrid = fixedColumns.realGridOf($(grid));
                            if (utils.isEditMode($targetGrid))
                                return;
                            if (utils.isAlphaNumeric(evt)) {
                                startEdit(evt, cell);
                            }
                            if (utils.isDeleteKey(evt)) {
                                $targetGrid.one(events.Handler.GRID_EDIT_CELL_STARTED, function (evt, ui) {
                                    $(ui.editor).find("input").val("");
                                });
                                startEdit(evt, cell);
                            }
                        }
                        updating.triggerCellUpdate = triggerCellUpdate;
                        function startEdit(evt, cell) {
                            var $targetGrid = fixedColumns.realGridOf($(evt.currentTarget));
                            if (!utils.updatable($targetGrid))
                                return;
                            utils.startEdit($targetGrid, cell);
                            if (!utils.isDeleteKey(evt)) {
                                setTimeout(function () {
                                    var $editor = $targetGrid.igGridUpdating("editorForCell", $(cell.element));
                                    var newText = $editor.igTextEditor("value");
                                    newText = newText.substr(newText.length - 1);
                                    $editor.igTextEditor("value", newText.trim());
                                }, 100);
                            }
                            evt.stopImmediatePropagation();
                        }
                        function beforeFinishEditCell(evt, ui) {
                            var $grid = $(evt.target);
                            var selectedCell = selection.getSelectedCell($grid);
                            if (utils.isEditMode($grid)
                                && $(selectedCell.element).find("div:first").ntsError("hasError")) {
                                return false;
                            }
                            specialColumn.tryDo($grid, selectedCell, ui.value);
                            return true;
                        }
                        function updateRow($grid, rowId, visibleColumnsMap, updatedRowData) {
                            if (uk.util.isNullOrUndefined(updatedRowData) || Object.keys(updatedRowData).length === 0)
                                return;
                            $grid.igGridUpdating("updateRow", utils.parseIntIfNumber(rowId, $grid, visibleColumnsMap), updatedRowData);
                        }
                        updating.updateRow = updateRow;
                    })(updating || (updating = {}));
                    var selection;
                    (function (selection_1) {
                        function addFeature(options) {
                            var selection = { name: feature.SELECTION, mode: "cell", multipleSelection: true, wrapAround: false, cellSelectionChanged: selectCellChange };
                            if (!feature.isEnable(options.features, feature.SELECTION)) {
                                options.features.push(selection);
                            }
                            else {
                                feature.replaceBy(options, feature.SELECTION, selection);
                            }
                        }
                        selection_1.addFeature = addFeature;
                        function selectPrev($grid) {
                            var selectedCell = getSelectedCell($grid);
                            if (uk.util.isNullOrUndefined(selectedCell))
                                return;
                            clearSelection($grid);
                            var visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                            var isFixed = utils.isFixedColumnCell(selectedCell, visibleColumnsMap);
                            if (selectedCell.index > 0) {
                                selectCell($grid, selectedCell.rowIndex, selectedCell.index - 1, isFixed);
                            }
                            else if (selectedCell.index === 0) {
                                var columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                                if (uk.util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0)
                                    return;
                                var fixedColumns_1 = utils.getFixedColumns(visibleColumnsMap);
                                var unfixedColumns = utils.getUnfixedColumns(visibleColumnsMap);
                                if ((isFixed || !utils.fixable($grid)) && selectedCell.rowIndex > 0) {
                                    selectCell($grid, selectedCell.rowIndex - 1, unfixedColumns.length - 1);
                                }
                                else if (utils.fixable($grid) && !isFixed) {
                                    selectCell($grid, selectedCell.rowIndex, fixedColumns_1.length - 1, true);
                                }
                            }
                        }
                        selection_1.selectPrev = selectPrev;
                        function selectFollow($grid, enterDirection) {
                            var enter = enterDirection || "right";
                            if (enter === "right")
                                selectNext($grid);
                            else
                                selectBelow($grid);
                        }
                        selection_1.selectFollow = selectFollow;
                        function selectNext($grid) {
                            var selectedCell = getSelectedCell($grid);
                            if (uk.util.isNullOrUndefined(selectedCell))
                                return;
                            clearSelection($grid);
                            var visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                            var dataSource = $grid.igGrid("option", "dataSource");
                            var columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                            if (uk.util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0)
                                return;
                            if (selectedCell.index < columnsGroup.length - 1) {
                                selectCell($grid, selectedCell.rowIndex, selectedCell.index + 1, columnsGroup[0].fixed);
                            }
                            else if (selectedCell.index === columnsGroup.length - 1) {
                                if (columnsGroup[0].fixed) {
                                    selectCell($grid, selectedCell.rowIndex, 0);
                                }
                                else if (selectedCell.rowIndex < dataSource.length - 1) {
                                    selectCell($grid, selectedCell.rowIndex + 1, 0, true);
                                }
                            }
                        }
                        function selectBelow($grid) {
                            var selectedCell = getSelectedCell($grid);
                            if (uk.util.isNullOrUndefined(selectedCell))
                                return;
                            clearSelection($grid);
                            var isFixed = utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($grid));
                            var dataSource = $grid.igGrid("option", "dataSource");
                            if (selectedCell.rowIndex < dataSource.length - 1) {
                                selectCell($grid, selectedCell.rowIndex + 1, selectedCell.index, isFixed);
                            }
                        }
                        function getSelectedCell($grid) {
                            if (!utils.selectable($grid)) {
                                var $targetGrid = fixedColumns.realGridOf($grid);
                                if (!uk.util.isNullOrUndefined($targetGrid)) {
                                    return $targetGrid.igGridSelection("selectedCells")[0] || $targetGrid.data(internal.SELECTED_CELL);
                                }
                            }
                            return $grid.igGridSelection("selectedCells")[0] || $grid.data(internal.SELECTED_CELL);
                        }
                        selection_1.getSelectedCell = getSelectedCell;
                        function getSelectedCells($grid) {
                            return utils.selectable($grid) ? $grid.igGridSelection("selectedCells") : undefined;
                        }
                        selection_1.getSelectedCells = getSelectedCells;
                        function selectCell($grid, rowIndex, columnIndex, isFixed) {
                            if (!utils.selectable($grid))
                                return;
                            $grid.igGridSelection("selectCell", rowIndex, columnIndex, !uk.util.isNullOrUndefined($grid.data("igGridColumnFixing")) ? isFixed : undefined);
                            var ui = { owner: $grid.data("igGridSelection"),
                                selectedCells: $grid.igGridSelection("selectedCells") };
                            var selectedCells = $grid.igGridSelection("selectedCells");
                            if (selectedCells.length > 0)
                                ui.cell = selectedCells[0];
                            selectCellChange({ target: $grid[0] }, ui);
                            var selectedCell = getSelectedCell($grid);
                            var ntsCombo = $(selectedCell.element).find(".nts-combo-container");
                            if (ntsCombo.length > 0) {
                                ntsCombo.find("input").select();
                            }
                        }
                        selection_1.selectCell = selectCell;
                        function selectCellById($grid, rowId, columnKey) {
                            return;
                        }
                        selection_1.selectCellById = selectCellById;
                        function selectCellChange(evt, ui) {
                            if (uk.util.isNullOrUndefined(ui.cell))
                                return;
                            $(evt.target).data(internal.SELECTED_CELL, ui.cell);
                        }
                        function onCellNavigate(evt, enterDirection) {
                            var grid = evt.currentTarget;
                            var $targetGrid = fixedColumns.realGridOf($(grid));
                            if (utils.isTabKey(evt)) {
                                if (utils.isEditMode($targetGrid))
                                    $targetGrid.igGridUpdating("endEdit");
                                if (evt.shiftKey) {
                                    selection.selectPrev($targetGrid);
                                }
                                else {
                                    selection.selectFollow($targetGrid);
                                }
                                evt.preventDefault();
                                return;
                            }
                            if (utils.isEnterKey(evt)) {
                                if (evt.shiftKey) {
                                    selection.selectPrev($targetGrid);
                                }
                                else {
                                    selection.selectFollow($targetGrid, enterDirection);
                                }
                                evt.stopImmediatePropagation();
                                return;
                            }
                        }
                        selection_1.onCellNavigate = onCellNavigate;
                        function clearSelection($grid) {
                            if (utils.selectable($grid)) {
                                $grid.igGridSelection("clearSelection");
                                return;
                            }
                            var $targetGrid = fixedColumns.realGridOf($grid);
                            if (!uk.util.isNullOrUndefined($targetGrid) && utils.selectable($targetGrid))
                                $targetGrid.igGridSelection("clearSelection");
                        }
                        var Direction = (function () {
                            function Direction() {
                            }
                            Direction.prototype.bind = function (evt) {
                                onCellNavigate(evt, this.to);
                            };
                            return Direction;
                        }());
                        selection_1.Direction = Direction;
                    })(selection || (selection = {}));
                    var columnSize;
                    (function (columnSize) {
                        function init($grid, columns) {
                            if (initValueExists($grid))
                                return;
                            var columnWidths = {};
                            _.forEach(columns, function (col, index) {
                                columnWidths[col.key] = parseInt(col.width);
                            });
                            saveAll($grid, columnWidths);
                        }
                        columnSize.init = init;
                        function load($grid) {
                            var storeKey = getStorageKey($grid);
                            uk.localStorage.getItem(storeKey).ifPresent(function (columns) {
                                var widthColumns = JSON.parse(columns);
                                setWidths($grid, widthColumns);
                                return null;
                            });
                        }
                        columnSize.load = load;
                        function save($grid, columnKey, columnWidth) {
                            var storeKey = getStorageKey($grid);
                            var columnsWidth = uk.localStorage.getItem(storeKey);
                            var widths = {};
                            if (columnsWidth.isPresent()) {
                                widths = JSON.parse(columnsWidth.get());
                                widths[columnKey] = columnWidth;
                            }
                            else {
                                widths[columnKey] = columnWidth;
                            }
                            uk.localStorage.setItemAsJson(storeKey, widths);
                        }
                        columnSize.save = save;
                        function saveAll($grid, widths) {
                            var storeKey = getStorageKey($grid);
                            var columnWidths = uk.localStorage.getItem(storeKey);
                            if (!columnWidths.isPresent()) {
                                uk.localStorage.setItemAsJson(storeKey, widths);
                            }
                        }
                        function initValueExists($grid) {
                            var storeKey = getStorageKey($grid);
                            var columnWidths = uk.localStorage.getItem(storeKey);
                            return columnWidths.isPresent();
                        }
                        function getStorageKey($grid) {
                            return uk.request.location.current.rawUrl + "/" + $grid.attr("id");
                        }
                        function loadOne($grid, columnKey) {
                            var storeKey = getStorageKey($grid);
                            uk.localStorage.getItem(storeKey).ifPresent(function (columns) {
                                var widthColumns = JSON.parse(columns);
                                setWidth($grid, columnKey, widthColumns[columnKey]);
                                return null;
                            });
                        }
                        columnSize.loadOne = loadOne;
                        function loadFixedColumns($grid) {
                            var storeKey = getStorageKey($grid);
                            uk.localStorage.getItem(storeKey).ifPresent(function (columns) {
                                var fixedColumns = utils.getVisibleFixedColumns($grid);
                                if (uk.util.isNullOrUndefined(fixedColumns) || fixedColumns.length === 0)
                                    return;
                                var widthColumns = JSON.parse(columns);
                                _.forEach(fixedColumns, function (fixedCol) {
                                    setWidth($grid, fixedCol.key, widthColumns[fixedCol.key]);
                                });
                                return null;
                            });
                        }
                        columnSize.loadFixedColumns = loadFixedColumns;
                        function setWidth($grid, columnKey, width, noCheck) {
                            if (noCheck !== true && uk.util.isNullOrUndefined($grid.data("igGridResizing")))
                                return;
                            try {
                                $grid.igGridResizing("resize", columnKey, width);
                            }
                            catch (e) { }
                        }
                        function setWidths($grid, columns) {
                            if (uk.util.isNullOrUndefined($grid.data("igGridResizing"))
                                || uk.util.isNullOrUndefined(columns))
                                return;
                            var columnKeys = Object.keys(columns);
                            _.forEach(columnKeys, function (key, index) {
                                setWidth($grid, key, columns[key], true);
                            });
                        }
                    })(columnSize || (columnSize = {}));
                    var functions;
                    (function (functions) {
                        function ntsAction($grid, method, params) {
                            switch (method) {
                                case "updateRow":
                                    var autoCommit = $grid.data("igGrid") !== null && $grid.igGrid("option", "autoCommit") ? true : false;
                                    updateRow($grid, params[0], params[1], autoCommit);
                                    break;
                                case "enableNtsControlAt":
                                    enableNtsControlAt($grid, params[0], params[1], params[2]);
                                    break;
                                case "disableNtsControlAt":
                                    disableNtsControlAt($grid, params[0], params[1], params[2]);
                                    break;
                                case "directEnter":
                                    var direction = $grid.data("enter");
                                    direction.to = params[0];
                                    if (utils.fixable($grid)) {
                                        var fixedTable = fixedColumns.getFixedTable($grid);
                                        if (!uk.util.isNullOrUndefined(fixedTable)) {
                                            fixedTable.data("enter").to = params[0];
                                        }
                                    }
                                    break;
                            }
                        }
                        functions.ntsAction = ntsAction;
                        function updateRow($grid, rowId, object, autoCommit) {
                            $grid.data("igGridUpdating").updateRow(rowId, object);
                            if (!autoCommit) {
                                var updatedRow = $grid.igGrid("rowById", rowId, false);
                                $grid.igGrid("commit");
                                if (updatedRow !== undefined)
                                    $grid.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                            }
                        }
                        function disableNtsControlAt($grid, rowId, columnKey, controlType) {
                            var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                            var control = ntsControls.getControl(controlType);
                            control.disable($(cellContainer));
                        }
                        function enableNtsControlAt($grid, rowId, columnKey, controlType) {
                            var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                            var control = ntsControls.getControl(controlType);
                            control.enable($(cellContainer));
                        }
                    })(functions || (functions = {}));
                    var ntsControls;
                    (function (ntsControls) {
                        ntsControls.LABEL = 'Label';
                        ntsControls.LINK_LABEL = 'LinkLabel';
                        ntsControls.CHECKBOX = 'CheckBox';
                        ntsControls.SWITCH_BUTTONS = 'SwitchButtons';
                        ntsControls.COMBOBOX = 'ComboBox';
                        ntsControls.BUTTON = 'Button';
                        ntsControls.DELETE_BUTTON = 'DeleteButton';
                        ntsControls.TEXTBOX = 'TextBox';
                        function getControl(name) {
                            switch (name) {
                                case ntsControls.CHECKBOX:
                                    return new CheckBox();
                                case ntsControls.SWITCH_BUTTONS:
                                    return new SwitchButtons();
                                case ntsControls.COMBOBOX:
                                    return new ComboBox();
                                case ntsControls.BUTTON:
                                    return new Button();
                                case ntsControls.DELETE_BUTTON:
                                    return new DeleteButton();
                                case ntsControls.LINK_LABEL:
                                    return new LinkLabel();
                            }
                        }
                        ntsControls.getControl = getControl;
                        function drawLabel($grid, column) {
                            column.formatter = function (value, rowObj) {
                                if (uk.util.isNullOrUndefined(rowObj))
                                    return value;
                                var $self = this;
                                var rowId = rowObj[$grid.igGrid("option", "primaryKey")];
                                var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                                var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", "35px"));
                                setTimeout(function () {
                                    var $gridCell = $grid.igGrid("cellById", rowObj[$grid.igGrid("option", "primaryKey")], column.key);
                                    if ($($gridCell.children()[0]).children().length === 0)
                                        $("." + controlCls).append(new Label().draw({ text: value }));
                                }, 0);
                                return $container.html();
                            };
                        }
                        ntsControls.drawLabel = drawLabel;
                        var NtsControlBase = (function () {
                            function NtsControlBase() {
                                this.readOnly = false;
                            }
                            return NtsControlBase;
                        }());
                        var CheckBox = (function (_super) {
                            __extends(CheckBox, _super);
                            function CheckBox() {
                                _super.apply(this, arguments);
                            }
                            CheckBox.prototype.containerClass = function () {
                                return "nts-checkbox-container";
                            };
                            CheckBox.prototype.draw = function (data) {
                                var checkBoxText;
                                var setChecked = data.update;
                                var initValue = data.initValue;
                                var $wrapper = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                                $wrapper.addClass("ntsControl").on("click", function (e) {
                                    if ($wrapper.data("readonly") === true)
                                        e.preventDefault();
                                });
                                var text = data.controlDef.options[data.controlDef.optionsText];
                                if (text) {
                                    checkBoxText = text;
                                }
                                else {
                                    checkBoxText = $wrapper.text();
                                    $wrapper.text('');
                                }
                                var $checkBoxLabel = $("<label class='ntsCheckBox'></label>");
                                var $checkBox = $('<input type="checkbox">').on("change", function () {
                                    setChecked($(this).is(":checked"));
                                }).appendTo($checkBoxLabel);
                                var $box = $("<span class='box'></span>").appendTo($checkBoxLabel);
                                if (checkBoxText && checkBoxText.length > 0)
                                    var label = $("<span class='label'></span>").text(checkBoxText).appendTo($checkBoxLabel);
                                $checkBoxLabel.appendTo($wrapper);
                                var checked = initValue !== undefined ? initValue : true;
                                $wrapper.data("readonly", this.readOnly);
                                var $checkBox = $wrapper.find("input[type='checkbox']");
                                if (checked === true)
                                    $checkBox.attr("checked", "checked");
                                else
                                    $checkBox.removeAttr("checked");
                                if (data.enable === true)
                                    $checkBox.removeAttr("disabled");
                                else
                                    $checkBox.attr("disabled", "disabled");
                                return $wrapper;
                            };
                            CheckBox.prototype.disable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                                $wrapper.find("input[type='checkbox']").attr("disabled", "disabled");
                            };
                            CheckBox.prototype.enable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                                $wrapper.find("input[type='checkbox']").removeAttr("disabled");
                            };
                            return CheckBox;
                        }(NtsControlBase));
                        var SwitchButtons = (function (_super) {
                            __extends(SwitchButtons, _super);
                            function SwitchButtons() {
                                _super.apply(this, arguments);
                            }
                            SwitchButtons.prototype.containerClass = function () {
                                return "nts-switch-container";
                            };
                            SwitchButtons.prototype.draw = function (data) {
                                var selectedCssClass = 'selected';
                                var options = data.controlDef.options;
                                var optionsValue = data.controlDef.optionsValue;
                                var optionsText = data.controlDef.optionsText;
                                var selectedValue = data.initValue;
                                var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                                _.forEach(options, function (opt) {
                                    var value = opt[optionsValue];
                                    var text = opt[optionsText];
                                    var btn = $('<button>').text(text)
                                        .addClass('nts-switch-button')
                                        .attr('data-swbtn', value)
                                        .on('click', function () {
                                        var selectedValue = $(this).data('swbtn');
                                        $('button', container).removeClass(selectedCssClass);
                                        $(this).addClass(selectedCssClass);
                                        data.update(selectedValue);
                                    });
                                    if (value === selectedValue) {
                                        btn.addClass(selectedCssClass);
                                    }
                                    container.append(btn);
                                });
                                (data.enable === true) ? $('button', container).prop("disabled", false)
                                    : $('button', container).prop("disabled", true);
                                return container;
                            };
                            SwitchButtons.prototype.enable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                                $('button', $wrapper).prop("disabled", false);
                            };
                            SwitchButtons.prototype.disable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                                $('button', $wrapper).prop("disabled", true);
                            };
                            return SwitchButtons;
                        }(NtsControlBase));
                        var ComboBox = (function (_super) {
                            __extends(ComboBox, _super);
                            function ComboBox() {
                                _super.apply(this, arguments);
                            }
                            ComboBox.prototype.containerClass = function () {
                                return "nts-combo-container";
                            };
                            ComboBox.prototype.draw = function (data) {
                                var self = this;
                                var distanceColumns = '     ';
                                var fillCharacter = ' ';
                                var maxWidthCharacter = 15;
                                var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                                var columns = data.controlDef.columns;
                                var itemTemplate = undefined;
                                var haveColumn = columns && columns.length > 0;
                                if (haveColumn) {
                                    itemTemplate = '<div class="nts-combo-item">';
                                    _.forEach(columns, function (item, i) {
                                        itemTemplate += '<div class="nts-column nts-combo-column-' + i + '">${' + item.prop + '}</div>';
                                    });
                                    itemTemplate += '</div>';
                                }
                                if (data.controlDef.displayMode === "codeName") {
                                    data.controlDef.options = data.controlDef.options.map(function (option) {
                                        var newOptionText = '';
                                        if (haveColumn) {
                                            _.forEach(columns, function (item, i) {
                                                var prop = option[item.prop];
                                                var length = item.length;
                                                if (i === columns.length - 1) {
                                                    newOptionText += prop;
                                                }
                                                else {
                                                    newOptionText += uk.text.padRight(prop, fillCharacter, length) + distanceColumns;
                                                }
                                            });
                                        }
                                        else {
                                            newOptionText = option[data.controlDef.optionsText];
                                        }
                                        option['nts-combo-label'] = newOptionText;
                                        return option;
                                    });
                                }
                                var comboMode = data.controlDef.editable ? 'editable' : 'dropdown';
                                container.igCombo({
                                    dataSource: data.controlDef.options,
                                    valueKey: data.controlDef.optionsValue,
                                    textKey: data.controlDef.displayMode === 'codeName'
                                        ? 'nts-combo-label' : data.controlDef.optionsText,
                                    mode: comboMode,
                                    disabled: !data.enable,
                                    placeHolder: '',
                                    enableClearButton: false,
                                    initialSelectedItems: [
                                        { value: data.initValue }
                                    ],
                                    itemTemplate: itemTemplate,
                                    selectionChanging: function (evt, ui) {
                                        var __self = self;
                                        var $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
                                        if (uk.util.isNullOrUndefined($gridControl))
                                            return;
                                        var cls = $gridControl.attr("class");
                                        var classNameParts = cls.split("-");
                                        var rowId = classNameParts.pop();
                                        var columnKey = classNameParts.pop();
                                        var targetCell = __self.$containedGrid.igGrid("cellById", rowId, columnKey);
                                        var $comboContainer = $(targetCell).find("." + __self.containerClass());
                                        var comboInput = $($comboContainer.find("input")[1]);
                                        comboInput.ntsError("clear");
                                        nts.uk.ui.errors.removeByElement(comboInput);
                                        comboInput.parent().removeClass("error");
                                    },
                                    selectionChanged: function (evt, ui) {
                                        var _self = self;
                                        if (ui.items.length > 0) {
                                            var selectedValue_1 = ui.items[0].data[data.controlDef.optionsValue];
                                            data.update(selectedValue_1);
                                            setTimeout(function () {
                                                var __self = _self;
                                                var $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
                                                if (uk.util.isNullOrUndefined($gridControl))
                                                    return;
                                                var cls = $gridControl.attr("class");
                                                var classNameParts = cls.split("-");
                                                var rowId = classNameParts.pop();
                                                var columnKey = classNameParts.pop();
                                                var targetCell = __self.$containedGrid.igGrid("cellById", rowId, columnKey);
                                                var $comboContainer = $(targetCell).find("." + __self.containerClass());
                                                $comboContainer.data(internal.COMBO_SELECTED, selectedValue_1);
                                            }, 0);
                                        }
                                    }
                                });
                                container.data(internal.COMBO_SELECTED, data.initValue);
                                if (haveColumn) {
                                    var totalWidth = 0;
                                    var $dropDownOptions = $(container.igCombo("dropDown"));
                                    _.forEach(columns, function (item, i) {
                                        var charLength = item.length;
                                        var width = charLength * maxWidthCharacter + 10;
                                        $dropDownOptions.find('.nts-combo-column-' + i).width(width);
                                        if (i !== columns.length - 1) {
                                            $dropDownOptions.find('.nts-combo-column-' + i).css({ 'float': 'left' });
                                        }
                                        totalWidth += width + 10;
                                    });
                                    $dropDownOptions.find('.nts-combo-item').css({ 'min-width': totalWidth });
                                    container.css({ 'min-width': totalWidth });
                                }
                                container.data("columns", columns);
                                container.data("comboMode", comboMode);
                                return container;
                            };
                            ComboBox.prototype.enable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass());
                                $wrapper.data("enable", true);
                                $wrapper.igCombo("option", "disabled", false);
                            };
                            ComboBox.prototype.disable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass());
                                $wrapper.data("enable", false);
                                $wrapper.igCombo("option", "disabled", true);
                            };
                            return ComboBox;
                        }(NtsControlBase));
                        var Button = (function (_super) {
                            __extends(Button, _super);
                            function Button() {
                                _super.apply(this, arguments);
                            }
                            Button.prototype.containerClass = function () {
                                return "nts-button-container";
                            };
                            Button.prototype.draw = function (data) {
                                var $container = $("<div/>").addClass(this.containerClass());
                                var $button = $("<button/>").addClass("ntsButton").appendTo($container).text(data.controlDef.text || data.initValue)
                                    .data("enable", data.enable).on("click", data.controlDef.click);
                                $button.prop("disabled", !data.enable);
                                return $container;
                            };
                            Button.prototype.enable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                                $wrapper.find(".ntsButton").prop("disabled", false);
                            };
                            Button.prototype.disable = function ($container) {
                                var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                                $wrapper.find(".ntsButton").prop("disabled", true);
                            };
                            return Button;
                        }(NtsControlBase));
                        var DeleteButton = (function (_super) {
                            __extends(DeleteButton, _super);
                            function DeleteButton() {
                                _super.apply(this, arguments);
                            }
                            DeleteButton.prototype.draw = function (data) {
                                var btnContainer = _super.prototype.draw.call(this, data);
                                var btn = btnContainer.find("button");
                                btn.off("click", data.controlDef.click);
                                btn.on("click", data.deleteRow);
                                return btn;
                            };
                            return DeleteButton;
                        }(Button));
                        var Label = (function (_super) {
                            __extends(Label, _super);
                            function Label() {
                                _super.apply(this, arguments);
                            }
                            Label.prototype.containerClass = function () {
                                return "nts-label-container";
                            };
                            Label.prototype.draw = function (data) {
                                var $container = $("<div/>").addClass(this.containerClass());
                                $("<label/>").addClass("ntsLabel").css("padding-left", "0px").text(data.text).appendTo($container);
                                return $container;
                            };
                            Label.prototype.enable = function ($container) {
                                return;
                            };
                            Label.prototype.disable = function ($container) {
                                return;
                            };
                            return Label;
                        }(NtsControlBase));
                        var LinkLabel = (function (_super) {
                            __extends(LinkLabel, _super);
                            function LinkLabel() {
                                _super.apply(this, arguments);
                            }
                            LinkLabel.prototype.containerClass = function () {
                                return "nts-link-container";
                            };
                            LinkLabel.prototype.draw = function (data) {
                                return $('<div/>').addClass(this.containerClass()).append($("<a/>")
                                    .addClass("link-button").css({ backgroundColor: "inherit", color: "deepskyblue" })
                                    .text(data.initValue).on("click", data.controlDef.click));
                            };
                            LinkLabel.prototype.enable = function ($container) {
                                return;
                            };
                            LinkLabel.prototype.disable = function ($container) {
                                return;
                            };
                            return LinkLabel;
                        }(NtsControlBase));
                        var comboBox;
                        (function (comboBox) {
                            function getCopiedValue(cell, copiedText) {
                                var copiedValue;
                                var $comboBox = utils.comboBoxOfCell(cell);
                                if ($comboBox.length > 0) {
                                    var items = $comboBox.igCombo("items");
                                    var textKey_1 = $comboBox.igCombo("option", "textKey");
                                    var valueKey_1 = $comboBox.igCombo("option", "valueKey");
                                    _.forEach(items, function (item) {
                                        if (item.data[textKey_1] === copiedText.trim()) {
                                            copiedValue = item.data[valueKey_1];
                                            return false;
                                        }
                                    });
                                }
                                return copiedValue;
                            }
                            comboBox.getCopiedValue = getCopiedValue;
                        })(comboBox = ntsControls.comboBox || (ntsControls.comboBox = {}));
                    })(ntsControls || (ntsControls = {}));
                    var specialColumn;
                    (function (specialColumn_1) {
                        specialColumn_1.CODE = "code";
                        function ifTrue(columnSpecialTypes, column) {
                            if (uk.util.isNullOrUndefined(column.ntsType))
                                return;
                            if (column.ntsType === specialColumn_1.CODE) {
                                columnSpecialTypes[column.key] = { type: column.ntsType,
                                    onChange: column.onChange };
                            }
                        }
                        specialColumn_1.ifTrue = ifTrue;
                        function tryDo($grid, cell, pastedText, visibleColumnsMap) {
                            var columnTypes = $grid.data(internal.SPECIAL_COL_TYPES);
                            var specialColumn;
                            var columnKey = cell.columnKey;
                            for (var key in columnTypes) {
                                if (key === columnKey) {
                                    specialColumn = columnTypes[key];
                                    break;
                                }
                            }
                            if (uk.util.isNullOrUndefined(specialColumn))
                                return;
                            visibleColumnsMap = !uk.util.isNullOrUndefined(visibleColumnsMap) ? visibleColumnsMap : utils.getVisibleColumnsMap($grid);
                            var isFixedColumn = utils.isFixedColumn(columnKey, visibleColumnsMap);
                            var nextColumn = utils.nextColumnByKey(visibleColumnsMap, columnKey, isFixedColumn);
                            if (uk.util.isNullOrUndefined(nextColumn) || nextColumn.index === 0)
                                return;
                            specialColumn.onChange(pastedText).done(function (res) {
                                var updatedRow = {};
                                var $gridRow = utils.rowAt(cell);
                                updatedRow[nextColumn.options.key] = res;
                                updating.updateRow($grid, $gridRow.data("id"), visibleColumnsMap, updatedRow);
                            }).fail(function (res) {
                            });
                            return true;
                        }
                        specialColumn_1.tryDo = tryDo;
                        function onChange($grid, rowId, updatedRow) {
                            var columnTypes = $grid.data(internal.SPECIAL_COL_TYPES);
                            var columnKeys = Object.keys(updatedRow);
                            _.find(columnKeys, function (key) {
                                return _.find(columnTypes, function (column) {
                                    return column.type === key;
                                }) !== undefined;
                            });
                        }
                        specialColumn_1.onChange = onChange;
                    })(specialColumn || (specialColumn = {}));
                    var copyPaste;
                    (function (copyPaste) {
                        var CopyMode;
                        (function (CopyMode) {
                            CopyMode[CopyMode["SINGLE"] = 0] = "SINGLE";
                            CopyMode[CopyMode["MULTIPLE"] = 1] = "MULTIPLE";
                        })(CopyMode || (CopyMode = {}));
                        var PasteMode;
                        (function (PasteMode) {
                            PasteMode[PasteMode["NEW"] = 0] = "NEW";
                            PasteMode[PasteMode["UPDATE"] = 1] = "UPDATE";
                        })(PasteMode || (PasteMode = {}));
                        var Processor = (function () {
                            function Processor(options) {
                                this.pasteInMode = PasteMode.UPDATE;
                                this.options = options;
                            }
                            Processor.addFeatures = function (options) {
                                selection.addFeature(options);
                                return new Processor(options);
                            };
                            Processor.prototype.chainEvents = function ($grid, $target) {
                                var self = this;
                                self.$grid = $grid;
                                var target = !uk.util.isNullOrUndefined($target) ? $target : $grid;
                                events.Handler.pull(target).focusInWith(self).ctrlCxpWith(self);
                            };
                            Processor.prototype.copyHandler = function (cut) {
                                var selectedCells = selection.getSelectedCells(this.$grid);
                                var copiedData;
                                var checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                                if (selectedCells.length === 1) {
                                    this.copyMode = CopyMode.SINGLE;
                                    if (!checker(this.$grid, selectedCells[0].columnKey))
                                        return;
                                    if (utils.isComboBox(this.$grid, selectedCells[0].columnKey)) {
                                        var $comboBox = utils.comboBoxOfCell(selectedCells[0]);
                                        if ($comboBox.length > 0) {
                                            copiedData = $comboBox.igCombo("text");
                                        }
                                    }
                                    else {
                                        copiedData = selectedCells[0].element.text();
                                    }
                                }
                                else {
                                    this.copyMode = CopyMode.MULTIPLE;
                                    copiedData = this.converseStructure(selectedCells, cut);
                                }
                                $("#copyHelper").val(copiedData).select();
                                document.execCommand("copy");
                                return selectedCells;
                            };
                            Processor.prototype.converseStructure = function (cells, cut) {
                                var self = this;
                                var maxRow = 0;
                                var minRow = 0;
                                var maxColumn = 0;
                                var minColumn = 0;
                                var structure = [];
                                var structData = "";
                                var checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                                _.forEach(cells, function (cell, index) {
                                    var rowIndex = cell.rowIndex;
                                    var columnIndex = utils.getDisplayColumnIndex(self.$grid, cell);
                                    if (index === 0) {
                                        minRow = maxRow = rowIndex;
                                        minColumn = maxColumn = columnIndex;
                                    }
                                    if (rowIndex < minRow)
                                        minRow = rowIndex;
                                    if (rowIndex > maxRow)
                                        maxRow = rowIndex;
                                    if (columnIndex < minColumn)
                                        minColumn = columnIndex;
                                    if (columnIndex > maxColumn)
                                        maxColumn = columnIndex;
                                    if (uk.util.isNullOrUndefined(structure[rowIndex])) {
                                        structure[rowIndex] = {};
                                    }
                                    if (!checker(self.$grid, cell.columnKey))
                                        return;
                                    if (utils.isComboBox(self.$grid, cell.columnKey)) {
                                        var $comboBox = utils.comboBoxOfCell(cell);
                                        if ($comboBox.length > 0) {
                                            structure[rowIndex][columnIndex] = $comboBox.igCombo("text");
                                        }
                                    }
                                    else {
                                        structure[rowIndex][columnIndex] = cell.element.text();
                                    }
                                });
                                for (var i = minRow; i <= maxRow; i++) {
                                    for (var j = minColumn; j <= maxColumn; j++) {
                                        if (uk.util.isNullOrUndefined(structure[i]) || uk.util.isNullOrUndefined(structure[i][j])) {
                                            structData += "null";
                                        }
                                        else {
                                            structData += structure[i][j];
                                        }
                                        if (j === maxColumn)
                                            structData += "\n";
                                        else
                                            structData += "\t";
                                    }
                                }
                                return structData;
                            };
                            Processor.prototype.cutHandler = function () {
                                var self = this;
                                var selectedCells = this.copyHandler(true);
                                var cellsGroup = _.groupBy(selectedCells, "rowIndex");
                                _.forEach(Object.keys(cellsGroup), function (rowIdx) {
                                    var $row = utils.rowAt(cellsGroup[rowIdx][0]);
                                    var updatedRowData = {};
                                    _.forEach(cellsGroup[rowIdx], function (cell) {
                                        if (!utils.isCuttableControls(self.$grid, cell.columnKey))
                                            return;
                                        updatedRowData[cell.columnKey] = "";
                                    });
                                    self.$grid.igGridUpdating("updateRow", parseInt($row.data("id")), updatedRowData);
                                });
                            };
                            Processor.prototype.pasteHandler = function (evt) {
                                if (this.copyMode === CopyMode.SINGLE) {
                                    this.pasteSingleCellHandler(evt);
                                }
                                else {
                                    this.pasteRangeHandler(evt);
                                }
                            };
                            Processor.prototype.pasteSingleCellHandler = function (evt) {
                                var self = this;
                                var cbData = this.getClipboardContent(evt);
                                var selectedCells = selection.getSelectedCells(this.$grid);
                                var visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                                _.forEach(selectedCells, function (cell, index) {
                                    if (!utils.isPastableControls(self.$grid, cell.columnKey))
                                        return;
                                    var rowIndex = cell.rowIndex;
                                    var columnIndex = cell.index;
                                    var $gridRow = utils.rowAt(cell);
                                    var updatedRow = {};
                                    var columnsGroup = utils.columnsGroupOfCell(cell, visibleColumnsMap);
                                    var columnKey = columnsGroup[columnIndex].key;
                                    if (utils.isComboBox(self.$grid, cell.columnKey)) {
                                        var copiedValue = ntsControls.comboBox.getCopiedValue(cell, cbData);
                                        if (!uk.util.isNullOrUndefined(copiedValue)) {
                                            updatedRow[columnKey] = columnsGroup[columnIndex].dataType === "number"
                                                ? parseInt(copiedValue) : copiedValue;
                                        }
                                        else {
                                            var $combo = cell.element.find(".nts-combo-container");
                                            var $comboInput = $($combo.find("input")[1]);
                                            $comboInput.ntsError("set", "Pasted text not valid");
                                            $combo.igCombo("text", "");
                                            return;
                                        }
                                    }
                                    else {
                                        specialColumn.tryDo(self.$grid, cell, cbData, visibleColumnsMap);
                                        if (columnsGroup[columnIndex].dataType === "number") {
                                            updatedRow[columnKey] = parseInt(cbData);
                                        }
                                        else {
                                            updatedRow[columnKey] = cbData;
                                        }
                                    }
                                    updating.updateRow(self.$grid, $gridRow.data("id"), visibleColumnsMap, updatedRow);
                                });
                            };
                            Processor.prototype.pasteRangeHandler = function (evt) {
                                var cbData = this.getClipboardContent(evt);
                                if (utils.isEditMode(this.$grid)) {
                                    cbData = this.processInEditMode(cbData);
                                    this.updateInEditMode(cbData);
                                }
                                else {
                                    cbData = this.process(cbData);
                                    this.pasteInMode === PasteMode.UPDATE ? this.updateWith(cbData) : this.addNew(cbData);
                                }
                            };
                            Processor.prototype.getClipboardContent = function (evt) {
                                if (window.clipboardData) {
                                    window.event.returnValue = false;
                                    return window.clipboardData.getData("text");
                                }
                                else {
                                    return evt.originalEvent.clipboardData.getData("text/plain");
                                }
                            };
                            Processor.prototype.processInEditMode = function (data) {
                                if (uk.util.isNullOrUndefined(data))
                                    return;
                                return data.split("\n")[0];
                            };
                            Processor.prototype.updateInEditMode = function (data) {
                                var selectedCell = selection.getSelectedCell(this.$grid);
                                var rowIndex = selectedCell.rowIndex;
                                var columnIndex = selectedCell.index;
                                var visibleColumnsMap = utils.getVisibleColumnsMap(this.$grid);
                                var updateRow = {};
                                var columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                                var columnKey = columnsGroup[columnIndex].key;
                                updateRow[columnKey] = data;
                                var $gridRow = utils.rowAt(selectedCell);
                                this.$grid.igGridUpdating("updateRow", utils.parseIntIfNumber($gridRow.data("id"), this.$grid, visibleColumnsMap), updateRow);
                            };
                            Processor.prototype.process = function (data) {
                                var dataRows = _.map(data.split("\n"), function (row) {
                                    return row.split("\t");
                                });
                                var rowsCount = dataRows.length;
                                if ((dataRows[rowsCount - 1].length === 1 && dataRows[rowsCount - 1][0] === "")
                                    || dataRows.length === 1 && dataRows[0].length === 1
                                        && (dataRows[0][0] === "" || dataRows[0][0] === "\r")) {
                                    dataRows.pop();
                                }
                                return dataRows;
                            };
                            Processor.prototype.updateWith = function (data) {
                                var self = this;
                                if (!utils.selectable(this.$grid) || !utils.updatable(this.$grid))
                                    return;
                                var selectedCell = selection.getSelectedCell(this.$grid);
                                if (selectedCell === undefined)
                                    return;
                                selectedCell.element.focus();
                                var visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                                var visibleColumns = utils.visibleColumnsFromMap(visibleColumnsMap);
                                var columnIndex = selectedCell.index;
                                var rowIndex = selectedCell.rowIndex;
                                var targetCol = _.find(visibleColumns, function (column) {
                                    return column.key === selectedCell.columnKey;
                                });
                                if (uk.util.isNullOrUndefined(targetCol))
                                    return;
                                _.forEach(data, function (row, idx) {
                                    var $gridRow;
                                    if (idx === 0)
                                        $gridRow = utils.rowAt(selectedCell);
                                    else
                                        $gridRow = utils.nextNRow(selectedCell, idx);
                                    if (uk.util.isNullOrUndefined($gridRow))
                                        return;
                                    var rowData = {};
                                    var targetIndex = columnIndex;
                                    var targetCell = selectedCell;
                                    var targetColumn = targetCol;
                                    var comboErrors = [];
                                    for (var i = 0; i < row.length; i++) {
                                        var nextColumn = void 0;
                                        var columnKey = targetColumn.key;
                                        if ((!uk.util.isNullOrUndefined(row[i]) && row[i].trim() === "null")
                                            || !utils.isPastableControls(self.$grid, columnKey)) {
                                            nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                            targetColumn = nextColumn.options;
                                            targetIndex = nextColumn.index;
                                            continue;
                                        }
                                        var columnsGroup = utils.columnsGroupOfColumn(targetColumn, visibleColumnsMap);
                                        if (targetIndex > columnsGroup.length - 1)
                                            break;
                                        var cellElement = self.$grid.igGrid("cellById", $gridRow.data("id"), columnKey);
                                        if (utils.isComboBox(self.$grid, columnKey)) {
                                            var cellContent = row[i].trim();
                                            var copiedValue = ntsControls.comboBox.getCopiedValue({ element: cellElement[0] }, cellContent);
                                            if (!uk.util.isNullOrUndefined(copiedValue)) {
                                                rowData[columnKey] = targetColumn.dataType === "number" ? parseInt(copiedValue) : copiedValue;
                                            }
                                            else {
                                                comboErrors.push({ cell: cellElement, content: cellContent });
                                                nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                                targetColumn = nextColumn.options;
                                                targetIndex = nextColumn.index;
                                                continue;
                                            }
                                        }
                                        else {
                                            var cell = {};
                                            cell.columnKey = columnKey;
                                            cell.element = cellElement;
                                            cell.id = $gridRow.data("id");
                                            cell.index = targetIndex;
                                            cell.row = $gridRow;
                                            cell.rowIndex = $gridRow.data("rowIdx");
                                            specialColumn.tryDo(self.$grid, cell, row[i].trim(), visibleColumnsMap);
                                            if (targetColumn.dataType === "number") {
                                                rowData[columnKey] = parseInt(row[i]);
                                            }
                                            else {
                                                rowData[columnKey] = row[i];
                                            }
                                        }
                                        nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                        targetColumn = nextColumn.options;
                                        targetIndex = nextColumn.index;
                                    }
                                    updating.updateRow(self.$grid, $gridRow.data("id"), visibleColumnsMap, rowData);
                                    _.forEach(comboErrors, function (combo) {
                                        setTimeout(function () {
                                            var $container = combo.cell.find(".nts-combo-container");
                                            var $comboInput = $($container.find("input")[1]);
                                            $comboInput.ntsError("set", "Pasted text not valid");
                                            $container.igCombo("text", combo.content);
                                        }, 0);
                                    });
                                });
                            };
                            Processor.prototype.addNew = function (data) {
                                var self = this;
                                var visibleColumns = null;
                                if (!this.pasteable(data[0].length - visibleColumns.length))
                                    return;
                                _.forEach(data, function (row, idx) {
                                    var rowData = {};
                                    for (var i = 0; i < visibleColumns.length; i++) {
                                        var columnKey = visibleColumns[i].key;
                                        if (visibleColumns[i].dataType === "number") {
                                            rowData[columnKey] = parseInt(row[i]);
                                        }
                                        else {
                                            rowData[columnKey] = row[i];
                                        }
                                    }
                                    self.$grid.igGridUpdating("addRow", rowData);
                                });
                            };
                            Processor.prototype.pasteable = function (excessColumns) {
                                if (excessColumns > 0) {
                                    nts.uk.ui.dialog.alert("Copied table structure doesn't match.");
                                    return false;
                                }
                                return true;
                            };
                            return Processor;
                        }());
                        copyPaste.Processor = Processor;
                        function ifOn($grid, options) {
                            if (options.ntsFeatures === undefined)
                                return;
                            _.forEach(options.ntsFeatures, function (f) {
                                if (f.name === feature.COPY_PASTE) {
                                    Processor.addFeatures(options).chainEvents($grid);
                                    return false;
                                }
                            });
                        }
                        copyPaste.ifOn = ifOn;
                    })(copyPaste || (copyPaste = {}));
                    var events;
                    (function (events) {
                        var Handler = (function () {
                            function Handler($grid) {
                                this.$grid = $grid;
                            }
                            Handler.pull = function ($grid) {
                                return new Handler($grid);
                            };
                            Handler.prototype.turnOn = function ($mainGrid) {
                                this.filter($mainGrid).onCellUpdate().onCellUpdateKeyUp().onDirectEnter().onSpacePress().onColumnResizing();
                            };
                            Handler.prototype.onDirectEnter = function () {
                                var direction = new selection.Direction();
                                this.$grid.on(Handler.KEY_DOWN, $.proxy(direction.bind, direction));
                                this.$grid.data("enter", direction);
                                return this;
                            };
                            Handler.prototype.onCellUpdate = function () {
                                var self = this;
                                this.$grid.on(Handler.KEY_DOWN, function (evt) {
                                    if (evt.ctrlKey)
                                        return;
                                    var selectedCell = selection.getSelectedCell(self.$grid);
                                    updating.triggerCellUpdate(evt, selectedCell);
                                });
                                return this;
                            };
                            Handler.prototype.onCellUpdateKeyUp = function () {
                                var self = this;
                                this.$grid.on(Handler.KEY_UP, function (evt) {
                                    if (evt.ctrlKey)
                                        return;
                                    var selectedCell = selection.getSelectedCell(self.$grid);
                                    updating.onEditCell(evt, selectedCell);
                                });
                                return this;
                            };
                            Handler.prototype.onSpacePress = function () {
                                var self = this;
                                self.$grid.on(Handler.KEY_DOWN, function (evt) {
                                    if (!utils.isSpaceKey(evt))
                                        return;
                                    var selectedCell = selection.getSelectedCell(self.$grid);
                                    if (uk.util.isNullOrUndefined(selectedCell))
                                        return;
                                    var checkbox = $(selectedCell.element).find(".nts-checkbox-container");
                                    if (checkbox.length > 0) {
                                        checkbox.find("input[type='checkbox']").click();
                                    }
                                });
                                return this;
                            };
                            Handler.prototype.focusInWith = function (processor) {
                                this.$grid.on(Handler.FOCUS_IN, function (evt) {
                                    if ($("#pasteHelper").length > 0 && $("#copyHelper").length > 0)
                                        return;
                                    var pasteArea = $("<textarea id='pasteHelper'/>").css({ "opacity": 0, "overflow": "hidden" })
                                        .on("paste", $.proxy(processor.pasteHandler, processor));
                                    var copyArea = $("<textarea id='copyHelper'/>").css({ "opacity": 0, "overflow": "hidden" });
                                    $("<div/>").css({ "position": "fixed", "top": -10000, "left": -10000 })
                                        .appendTo($(document.body)).append(pasteArea).append(copyArea);
                                });
                                return this;
                            };
                            Handler.prototype.ctrlCxpWith = function (processor) {
                                this.$grid.on(Handler.KEY_DOWN, function (evt) {
                                    if (evt.ctrlKey && utils.isPasteKey(evt)) {
                                        $("#pasteHelper").focus();
                                    }
                                    else if (evt.ctrlKey && utils.isCopyKey(evt)) {
                                        processor.copyHandler();
                                    }
                                    else if (evt.ctrlKey && utils.isCutKey(evt)) {
                                        processor.cutHandler();
                                    }
                                });
                                return this;
                            };
                            Handler.prototype.filter = function ($target) {
                                var self = this;
                                var $mainGrid = !uk.util.isNullOrUndefined($target) ? $target : self.$grid;
                                self.$grid.on(Handler.KEY_DOWN, function (evt) {
                                    if (utils.isAlphaNumeric(evt) || utils.isDeleteKey(evt)) {
                                        var cell = selection.getSelectedCell($mainGrid);
                                        if (cell === undefined || updating.containsNtsControl($(evt.target)))
                                            evt.stopImmediatePropagation();
                                        return;
                                    }
                                    if (utils.isTabKey(evt) && utils.isErrorStatus($mainGrid)) {
                                        evt.preventDefault();
                                        evt.stopImmediatePropagation();
                                    }
                                });
                                self.$grid[0].addEventListener(Handler.MOUSE_DOWN, function (evt) {
                                    if (utils.isNotErrorCell($mainGrid, evt)) {
                                        evt.preventDefault();
                                        evt.stopImmediatePropagation();
                                    }
                                }, true);
                                self.$grid[0].addEventListener(Handler.CLICK, function (evt) {
                                    if (utils.isNotErrorCell($mainGrid, evt)) {
                                        evt.preventDefault();
                                        evt.stopImmediatePropagation();
                                    }
                                }, true);
                                return this;
                            };
                            Handler.prototype.onColumnResizing = function () {
                                var self = this;
                                self.$grid.on(Handler.COLUMN_RESIZING, function (evt, args) {
                                    columnSize.save(self.$grid, args.columnKey, args.desiredWidth);
                                });
                                return this;
                            };
                            Handler.KEY_DOWN = "keydown";
                            Handler.KEY_UP = "keyup";
                            Handler.FOCUS_IN = "focusin";
                            Handler.CLICK = "click";
                            Handler.MOUSE_DOWN = "mousedown";
                            Handler.GRID_EDIT_CELL_STARTED = "iggridupdatingeditcellstarted";
                            Handler.COLUMN_RESIZING = "iggridresizingcolumnresizing";
                            return Handler;
                        }());
                        events.Handler = Handler;
                        function afterRendered(options) {
                            options.rendered = function (evt, ui) {
                                var $grid = $(evt.target);
                                events.Handler.pull($grid).turnOn();
                                var $fixedTbl = fixedColumns.getFixedTable($grid);
                                if ($fixedTbl.length === 0)
                                    return;
                                new copyPaste.Processor().chainEvents($grid, $fixedTbl);
                                events.Handler.pull($fixedTbl).turnOn($grid);
                                columnSize.load($grid);
                                var selectedCell = $grid.data(internal.SELECTED_CELL);
                                if (!uk.util.isNullOrUndefined(selectedCell)) {
                                    var fixedColumns_2 = utils.getVisibleFixedColumns($grid);
                                    if (_.find(fixedColumns_2, function (col) {
                                        return col.key === selectedCell.columnKey;
                                    }) !== undefined) {
                                        $grid.igGrid("virtualScrollTo", selectedCell.rowIndex);
                                        setTimeout(function () {
                                            selection.selectCell($grid, selectedCell.rowIndex, selectedCell.index, true);
                                        }, 1);
                                    }
                                }
                            };
                        }
                        events.afterRendered = afterRendered;
                    })(events || (events = {}));
                    var validation;
                    (function (validation) {
                        validation.VALIDATORS = "ntsValidators";
                        var ColumnFieldValidator = (function () {
                            function ColumnFieldValidator(name, primitiveValue, options) {
                                this.name = name;
                                this.primitiveValue = primitiveValue;
                                this.options = options;
                            }
                            ColumnFieldValidator.prototype.probe = function (value) {
                                var constraint = nts.uk.ui.validation.getConstraint(this.primitiveValue);
                                switch (constraint.valueType) {
                                    case "String":
                                        return new nts.uk.ui.validation.StringValidator(this.name, this.primitiveValue, this.options)
                                            .validate(value, this.options);
                                    case "Integer":
                                    case "Decimal":
                                    case "HalfInt":
                                        return new nts.uk.ui.validation.NumberValidator(this.name, this.primitiveValue, this.options)
                                            .validate(value);
                                    case "Time":
                                        this.options.mode = "time";
                                        return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                            .validate(value);
                                    case "Clock":
                                        this.options.outputFormat = "time";
                                        return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                            .validate(value);
                                }
                            };
                            return ColumnFieldValidator;
                        }());
                        validation.ColumnFieldValidator = ColumnFieldValidator;
                        function getValidators(columnsDef) {
                            var validators = {};
                            _.forEach(columnsDef, function (def) {
                                if (def.constraint === undefined)
                                    return;
                                validators[def.key] = new ColumnFieldValidator(def.headerText, def.constraint.primitiveValue, def.constraint);
                            });
                            return validators;
                        }
                        function scanValidators($grid, columnsDef) {
                            $grid.data(validation.VALIDATORS, getValidators(utils.analyzeColumns(columnsDef)));
                        }
                        validation.scanValidators = scanValidators;
                    })(validation || (validation = {}));
                    var color;
                    (function (color) {
                        var CellFormatter = (function () {
                            function CellFormatter($grid, features) {
                                this.$grid = $grid;
                                this.colorFeatureDef = this.getColorFeatureDef(features);
                            }
                            CellFormatter.prototype.getColorFeatureDef = function (features) {
                                var colorFeature = _.find(features, function (f) {
                                    return f.name === feature.CELL_COLOR;
                                });
                                if (colorFeature !== undefined)
                                    return colorFeature.columns;
                            };
                            CellFormatter.prototype.format = function (column) {
                                var self = this;
                                if (uk.util.isNullOrUndefined(this.colorFeatureDef)
                                    || column.formatter !== undefined)
                                    return column;
                                column.formatter = function (value, rowObj) {
                                    if (uk.util.isNullOrUndefined(rowObj))
                                        return value;
                                    var _self = self;
                                    setTimeout(function () {
                                        var $gridCell = self.$grid.igGrid("cellById", rowObj[self.$grid.igGrid("option", "primaryKey")], column.key);
                                        var aColumn = _.find(_self.colorFeatureDef, function (col) {
                                            return col.key === column.key;
                                        });
                                        if (uk.util.isNullOrUndefined(aColumn))
                                            return;
                                        var cellColor = aColumn.map(aColumn.parse(value));
                                        $gridCell.css("background-color", cellColor);
                                    }, 0);
                                    return value;
                                };
                                return column;
                            };
                            return CellFormatter;
                        }());
                        color.CellFormatter = CellFormatter;
                    })(color || (color = {}));
                    var fixedColumns;
                    (function (fixedColumns) {
                        function getFixedTable($grid) {
                            return $("#" + $grid.attr("id") + "_fixed");
                        }
                        fixedColumns.getFixedTable = getFixedTable;
                        function realGridOf($grid) {
                            if (utils.isIgGrid($grid))
                                return $grid;
                            var gridId = $grid.attr("id");
                            if (uk.util.isNullOrUndefined(gridId))
                                return;
                            var endIdx = gridId.indexOf("_fixed");
                            if (endIdx !== -1) {
                                var referGrid = $("#" + gridId.substring(0, endIdx));
                                if (!uk.util.isNullOrUndefined(referGrid) && utils.fixable(referGrid))
                                    return referGrid;
                            }
                        }
                        fixedColumns.realGridOf = realGridOf;
                    })(fixedColumns || (fixedColumns = {}));
                    var sheet;
                    (function (sheet_1) {
                        sheet_1.SHEETS = "ntsGridSheets";
                        var normalStyles = { backgroundColor: '', color: '' };
                        var selectedStyles = { backgroundColor: '#00B050', color: '#fff' };
                        var Configurator = (function () {
                            function Configurator(currentSheet, sheets) {
                                this.currentSheet = currentSheet;
                                this.sheets = sheets;
                            }
                            Configurator.load = function ($grid, sheetFeature) {
                                var config = new Configurator(sheetFeature.initialDisplay, sheetFeature.sheets);
                                $grid.data(sheet_1.SHEETS, config);
                            };
                            return Configurator;
                        }());
                        sheet_1.Configurator = Configurator;
                        function setup($grid, options) {
                            var sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                            if (uk.util.isNullOrUndefined(sheetFeature))
                                return;
                            var hidingFeature = { name: 'Hiding' };
                            if (feature.isEnable(options.features, feature.HIDING)) {
                                feature.replaceBy(options, feature.HIDING, hidingFeature);
                            }
                            else {
                                options.features.push(hidingFeature);
                            }
                            Configurator.load($grid, sheetFeature);
                            configButtons($grid, sheetFeature.sheets);
                        }
                        sheet_1.setup = setup;
                        function configButtons($grid, sheets) {
                            var gridWrapper = $("<div class='nts-grid-wrapper'/>");
                            $grid.wrap($("<div class='nts-grid-container'/>")).wrap(gridWrapper);
                            var gridContainer = $grid.closest(".nts-grid-container");
                            var sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                            var sheetMng = $grid.data(sheet_1.SHEETS);
                            _.forEach(sheets, function (sheet) {
                                var btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                                if (sheetMng.currentSheet === sheet.name)
                                    btn.css(selectedStyles);
                                btn.on("click", function (evt) {
                                    if (!utils.hidable($grid) || utils.isErrorStatus($grid))
                                        return;
                                    updateCurrentSheet($grid, sheet.name);
                                    utils.showColumns($grid, sheet.columns);
                                    hideOthers($grid);
                                    sheetButtonsWrapper.find("button").css(normalStyles);
                                    $(this).css(selectedStyles);
                                });
                            });
                        }
                        function hideOthers($grid) {
                            var sheetMng = $grid.data(sheet_1.SHEETS);
                            if (uk.util.isNullOrUndefined(sheetMng))
                                return;
                            var displayColumns;
                            _.forEach(sheetMng.sheets, function (sheet) {
                                if (sheet.name !== sheetMng.currentSheet) {
                                    utils.hideColumns($grid, sheet.columns);
                                }
                                else {
                                    displayColumns = sheet.columns;
                                }
                            });
                            setTimeout(function () {
                                _.forEach(displayColumns, function (column) {
                                    columnSize.loadOne($grid, column);
                                });
                            }, 0);
                        }
                        sheet_1.hideOthers = hideOthers;
                        function updateCurrentSheet($grid, name) {
                            var sheetMng = $grid.data(sheet_1.SHEETS);
                            if (uk.util.isNullOrUndefined(sheetMng))
                                return;
                            sheetMng.currentSheet = name;
                            $grid.data(sheet_1.SHEETS, sheetMng);
                        }
                        var load;
                        (function (load) {
                            function setup($grid, options) {
                                var sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                                Configurator.load($grid, sheetFeature);
                                configButtons($grid, sheetFeature.sheets);
                                if (!uk.util.isNullOrUndefined($grid.data(internal.GRID_OPTIONS)))
                                    return;
                                $grid.data(internal.GRID_OPTIONS, _.cloneDeep(options));
                                var sheetMng = $grid.data(sheet_1.SHEETS);
                                var sheet = _.filter(sheetMng.sheets, function (sheet) {
                                    return sheet.name === sheetMng.currentSheet;
                                });
                                var columns = getSheetColumns(options.columns, sheet[0], options.features);
                                options.columns = columns;
                            }
                            load.setup = setup;
                            function configButtons($grid, sheets) {
                                if ($grid.closest(".nts-grid-container").length > 0)
                                    return;
                                var gridWrapper = $("<div class='nts-grid-wrapper'/>");
                                $grid.wrap($("<div class='nts-grid-container'/>")).wrap(gridWrapper);
                                var gridContainer = $grid.closest(".nts-grid-container");
                                var sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                                var sheetMng = $grid.data(sheet_1.SHEETS);
                                _.forEach(sheets, function (sheet) {
                                    var btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                                    if (sheetMng.currentSheet === sheet.name)
                                        btn.css(selectedStyles);
                                    btn.on("click", function (evt) {
                                        if (utils.isErrorStatus($grid))
                                            return;
                                        updateCurrentSheet($grid, sheet.name);
                                        var options = $grid.data(internal.GRID_OPTIONS);
                                        var columns = getSheetColumns(options.columns, sheet, options.features);
                                        var clonedOpts = _.cloneDeep(options);
                                        clonedOpts.columns = columns;
                                        clonedOpts.dataSource = $grid.igGrid("option", "dataSource");
                                        $grid.igGrid("destroy");
                                        $grid.off();
                                        $grid.ntsGrid(clonedOpts);
                                        sheetButtonsWrapper.find("button").css(normalStyles);
                                        $(this).css(selectedStyles);
                                    });
                                });
                            }
                            function getSheetColumns(allColumns, displaySheet, features) {
                                return _.filter(allColumns, function (column) {
                                    if (column.group !== undefined && _.find(displaySheet.columns, function (col) {
                                        return col === column.group[0].key;
                                    }) !== undefined)
                                        return true;
                                    var belongToSheet = _.find(displaySheet.columns, function (col) {
                                        return col === column.key;
                                    }) !== undefined;
                                    var columnFixFeature = feature.find(features, feature.COLUMN_FIX);
                                    if (!uk.util.isNullOrUndefined(columnFixFeature)) {
                                        return _.find(columnFixFeature.columnSettings, function (s) {
                                            return s.columnKey === column.key;
                                        }) !== undefined || belongToSheet;
                                    }
                                    return belongToSheet;
                                });
                            }
                        })(load = sheet_1.load || (sheet_1.load = {}));
                    })(sheet || (sheet = {}));
                    var internal;
                    (function (internal) {
                        internal.CONTROL_TYPES = "ntsControlTypesGroup";
                        internal.COMBO_SELECTED = "ntsComboSelection";
                        internal.GRID_OPTIONS = "ntsGridOptions";
                        internal.SELECTED_CELL = "ntsSelectedCell";
                        internal.SPECIAL_COL_TYPES = "ntsSpecialColumnTypes";
                    })(internal || (internal = {}));
                    var utils;
                    (function (utils) {
                        function isArrowKey(evt) {
                            return evt.keyCode >= 37 && evt.keyCode <= 40;
                        }
                        utils.isArrowKey = isArrowKey;
                        function isAlphaNumeric(evt) {
                            return evt.keyCode >= 48 && evt.keyCode <= 90;
                        }
                        utils.isAlphaNumeric = isAlphaNumeric;
                        function isTabKey(evt) {
                            return evt.keyCode === 9;
                        }
                        utils.isTabKey = isTabKey;
                        function isEnterKey(evt) {
                            return evt.keyCode === 13;
                        }
                        utils.isEnterKey = isEnterKey;
                        function isSpaceKey(evt) {
                            return evt.keyCode === 32;
                        }
                        utils.isSpaceKey = isSpaceKey;
                        function isDeleteKey(evt) {
                            return evt.keyCode === 46;
                        }
                        utils.isDeleteKey = isDeleteKey;
                        function isPasteKey(evt) {
                            return evt.keyCode === 86;
                        }
                        utils.isPasteKey = isPasteKey;
                        function isCopyKey(evt) {
                            return evt.keyCode === 67;
                        }
                        utils.isCopyKey = isCopyKey;
                        function isCutKey(evt) {
                            return evt.keyCode === 88;
                        }
                        utils.isCutKey = isCutKey;
                        function isErrorStatus($grid) {
                            var cell = selection.getSelectedCell($grid);
                            return (isEditMode($grid)
                                && $(cell.element).find("div:first").ntsError("hasError"));
                        }
                        utils.isErrorStatus = isErrorStatus;
                        function isNotErrorCell($grid, evt) {
                            var cell = selection.getSelectedCell($grid);
                            var $target = $(evt.target);
                            var td = $target;
                            if (!$target.prev().is("td"))
                                td = $target.closest("td");
                            return (isEditMode($grid) && td.length > 0 && td[0] !== cell.element[0]
                                && $(cell.element).find("div:first").ntsError("hasError"));
                        }
                        utils.isNotErrorCell = isNotErrorCell;
                        function isEditMode($grid) {
                            return (updatable($grid) && $grid.igGridUpdating("isEditing"));
                        }
                        utils.isEditMode = isEditMode;
                        function isIgGrid($grid) {
                            return !uk.util.isNullOrUndefined($grid.data("igGrid"));
                        }
                        utils.isIgGrid = isIgGrid;
                        function selectable($grid) {
                            return !uk.util.isNullOrUndefined($grid.data("igGridSelection"));
                        }
                        utils.selectable = selectable;
                        function updatable($grid) {
                            return !uk.util.isNullOrUndefined($grid.data("igGridUpdating"));
                        }
                        utils.updatable = updatable;
                        function fixable($grid) {
                            return !uk.util.isNullOrUndefined($grid.data("igGridColumnFixing"));
                        }
                        utils.fixable = fixable;
                        function hidable($grid) {
                            return !uk.util.isNullOrUndefined($grid.data("igGridHiding"));
                        }
                        utils.hidable = hidable;
                        function dataTypeOfPrimaryKey($grid, visibleColumnsMap) {
                            var visibleColumns = _.concat(visibleColumnsMap["true"], visibleColumnsMap["undefined"]);
                            var primaryKey = $grid.igGrid("option", "primaryKey");
                            var keyColumn = _.filter(visibleColumns, function (column) {
                                return column.key === primaryKey;
                            });
                            if (!uk.util.isNullOrUndefined(keyColumn))
                                return keyColumn[0].dataType;
                            return;
                        }
                        utils.dataTypeOfPrimaryKey = dataTypeOfPrimaryKey;
                        function parseIntIfNumber(value, $grid, visibleColumnsMap) {
                            if (dataTypeOfPrimaryKey($grid, visibleColumnsMap) === "number") {
                                return parseInt(value);
                            }
                            return value;
                        }
                        utils.parseIntIfNumber = parseIntIfNumber;
                        function isCopiableControls($grid, columnKey) {
                            var columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                            switch (columnControlTypes[columnKey]) {
                                case ntsControls.LINK_LABEL:
                                case ntsControls.TEXTBOX:
                                case ntsControls.LABEL:
                                case ntsControls.COMBOBOX:
                                    return true;
                            }
                            return false;
                        }
                        utils.isCopiableControls = isCopiableControls;
                        function isCuttableControls($grid, columnKey) {
                            var columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                            switch (columnControlTypes[columnKey]) {
                                case ntsControls.TEXTBOX:
                                    return true;
                            }
                            return false;
                        }
                        utils.isCuttableControls = isCuttableControls;
                        function isPastableControls($grid, columnKey) {
                            var columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                            switch (columnControlTypes[columnKey]) {
                                case ntsControls.LABEL:
                                case ntsControls.CHECKBOX:
                                case ntsControls.LINK_LABEL:
                                    return false;
                            }
                            return true;
                        }
                        utils.isPastableControls = isPastableControls;
                        function isComboBox($grid, columnKey) {
                            var columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                            if (columnControlTypes[columnKey] === ntsControls.COMBOBOX)
                                return true;
                            return false;
                        }
                        utils.isComboBox = isComboBox;
                        function comboBoxOfCell(cell) {
                            return $(cell.element).find(".nts-combo-container");
                        }
                        utils.comboBoxOfCell = comboBoxOfCell;
                        function getColumns($grid) {
                            if (isIgGrid($grid)) {
                                return $grid.igGrid("option", "columns");
                            }
                            var referGrid = fixedColumns.realGridOf($grid);
                            if (!uk.util.isNullOrUndefined(referGrid))
                                return referGrid.igGrid("option", "columns");
                        }
                        utils.getColumns = getColumns;
                        function getVisibleColumns($grid) {
                            return _.filter(getColumns($grid), function (column) {
                                return column.hidden !== true;
                            });
                        }
                        utils.getVisibleColumns = getVisibleColumns;
                        function getVisibleColumnsMap($grid) {
                            var visibleColumns = getVisibleColumns($grid);
                            return _.groupBy(visibleColumns, "fixed");
                        }
                        utils.getVisibleColumnsMap = getVisibleColumnsMap;
                        function getVisibleFixedColumns($grid) {
                            return _.filter(getColumns($grid), function (column) {
                                return column.hidden !== true && column.fixed === true;
                            });
                        }
                        utils.getVisibleFixedColumns = getVisibleFixedColumns;
                        function isFixedColumn(columnKey, visibleColumnsMap) {
                            return _.find(visibleColumnsMap["true"], function (column) {
                                return column.key === columnKey;
                            }) !== undefined;
                        }
                        utils.isFixedColumn = isFixedColumn;
                        function isFixedColumnCell(cell, visibleColumnsMap) {
                            return _.find(visibleColumnsMap["true"], function (column) {
                                return column.key === cell.columnKey;
                            }) !== undefined;
                        }
                        utils.isFixedColumnCell = isFixedColumnCell;
                        function columnsGroupOfColumn(column, visibleColumnsMap) {
                            return visibleColumnsMap[column.fixed ? "true" : "undefined"];
                        }
                        utils.columnsGroupOfColumn = columnsGroupOfColumn;
                        function columnsGroupOfCell(cell, visibleColumnsMap) {
                            if (isFixedColumnCell(cell, visibleColumnsMap))
                                return visibleColumnsMap["true"];
                            return visibleColumnsMap["undefined"];
                        }
                        utils.columnsGroupOfCell = columnsGroupOfCell;
                        function visibleColumnsFromMap(visibleColumnsMap) {
                            return _.concat(visibleColumnsMap["true"], visibleColumnsMap["undefined"]);
                        }
                        utils.visibleColumnsFromMap = visibleColumnsFromMap;
                        function noOfVisibleColumns(visibleColumnsMap) {
                            return visibleColumnsMap["true"].length + visibleColumnsMap["undefined"].length;
                        }
                        utils.noOfVisibleColumns = noOfVisibleColumns;
                        function getFixedColumns(visibleColumnsMap) {
                            return visibleColumnsMap["true"];
                        }
                        utils.getFixedColumns = getFixedColumns;
                        function getUnfixedColumns(visibleColumnsMap) {
                            return visibleColumnsMap["undefined"];
                        }
                        utils.getUnfixedColumns = getUnfixedColumns;
                        function nextColumn(visibleColumnsMap, columnIndex, isFixed) {
                            if (uk.util.isNullOrUndefined(visibleColumnsMap))
                                return;
                            var nextCol = {};
                            var mapKeyName = isFixed ? "true" : "undefined";
                            var reverseKeyName = isFixed ? "undefined" : "true";
                            if (columnIndex < visibleColumnsMap[mapKeyName].length - 1) {
                                return {
                                    options: visibleColumnsMap[mapKeyName][columnIndex + 1],
                                    index: columnIndex + 1
                                };
                            }
                            else if (columnIndex === visibleColumnsMap[mapKeyName].length - 1) {
                                return {
                                    options: visibleColumnsMap[reverseKeyName][0],
                                    index: 0
                                };
                            }
                        }
                        utils.nextColumn = nextColumn;
                        function nextColumnByKey(visibleColumnsMap, columnKey, isFixed) {
                            if (uk.util.isNullOrUndefined(visibleColumnsMap))
                                return;
                            var currentColumnIndex;
                            var currentColumn;
                            var fixedColumns = visibleColumnsMap["true"];
                            var unfixedColumns = visibleColumnsMap["undefined"];
                            if (isFixed && fixedColumns.length > 0) {
                                _.forEach(fixedColumns, function (col, index) {
                                    if (col.key === columnKey) {
                                        currentColumnIndex = index;
                                        currentColumn = col;
                                        return false;
                                    }
                                });
                                if (uk.util.isNullOrUndefined(currentColumn) || uk.util.isNullOrUndefined(currentColumnIndex))
                                    return;
                                if (currentColumnIndex === fixedColumns.length - 1) {
                                    return {
                                        options: unfixedColumns[0],
                                        index: 0
                                    };
                                }
                                return {
                                    options: fixedColumns[currentColumnIndex + 1],
                                    index: currentColumnIndex + 1
                                };
                            }
                            if (!isFixed && unfixedColumns.length > 0) {
                                _.forEach(unfixedColumns, function (col, index) {
                                    if (col.key === columnKey) {
                                        currentColumnIndex = index;
                                        currentColumn = col;
                                        return false;
                                    }
                                });
                                if (uk.util.isNullOrUndefined(currentColumn) || uk.util.isNullOrUndefined(currentColumnIndex))
                                    return;
                                if (currentColumnIndex === unfixedColumns.length - 1) {
                                    return {
                                        options: fixedColumns.length > 0 ? fixedColumns[0] : unfixedColumns[0],
                                        index: 0
                                    };
                                }
                                return {
                                    options: unfixedColumns[currentColumnIndex + 1],
                                    index: currentColumnIndex + 1
                                };
                            }
                        }
                        utils.nextColumnByKey = nextColumnByKey;
                        function rowAt(cell) {
                            if (uk.util.isNullOrUndefined(cell))
                                return;
                            return $(cell.element).closest("tr");
                        }
                        utils.rowAt = rowAt;
                        function nextNRow(cell, noOfNext) {
                            return $(cell.element).closest("tr").nextAll("tr:eq(" + (noOfNext - 1) + ")");
                        }
                        utils.nextNRow = nextNRow;
                        function getDisplayColumnIndex($grid, cell) {
                            var columns = $grid.igGrid("option", "columns");
                            for (var i = 0; i < columns.length; i++) {
                                if (columns[i].key === cell.columnKey)
                                    return i;
                            }
                            return -1;
                        }
                        utils.getDisplayColumnIndex = getDisplayColumnIndex;
                        function startEdit($grid, cell) {
                            var visibleColumns = getVisibleColumns($grid);
                            for (var i = 0; i < visibleColumns.length; i++) {
                                if (visibleColumns[i].key === cell.columnKey) {
                                    $grid.igGridUpdating("startEdit", cell.id, i);
                                    break;
                                }
                            }
                        }
                        utils.startEdit = startEdit;
                        function hideColumns($grid, columns) {
                            $grid.igGridHiding("hideMultiColumns", columns);
                        }
                        utils.hideColumns = hideColumns;
                        function showColumns($grid, columns) {
                            $grid.igGridHiding("showMultiColumns", columns);
                        }
                        utils.showColumns = showColumns;
                        function analyzeColumns(columns) {
                            var flatCols = [];
                            flatColumns(columns, flatCols);
                            return flatCols;
                        }
                        utils.analyzeColumns = analyzeColumns;
                        function flatColumns(columns, flatCols) {
                            _.forEach(columns, function (column) {
                                if (uk.util.isNullOrUndefined(column.group)) {
                                    flatCols.push(column);
                                    return;
                                }
                                flatColumns(column.group, flatCols);
                            });
                        }
                    })(utils || (utils = {}));
                })(ntsGrid = jqueryExtentions.ntsGrid || (jqueryExtentions.ntsGrid = {}));
            })(jqueryExtentions = ui_2.jqueryExtentions || (ui_2.jqueryExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_3) {
            var koExtentions;
            (function (koExtentions) {
                var NtsCheckboxBindingHandler = (function () {
                    function NtsCheckboxBindingHandler() {
                    }
                    NtsCheckboxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var setChecked = data.checked;
                        var textId = data.text;
                        var checkBoxText;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        if (nts.uk.util.isNullOrUndefined(container.attr("tabindex")))
                            container.attr("tabindex", "0");
                        container.addClass("ntsControl ntsCheckBox").on("click", function (e) {
                            if (container.data("readonly") === true)
                                e.preventDefault();
                        });
                        if (textId) {
                            checkBoxText = textId;
                        }
                        else {
                            checkBoxText = container.text();
                            container.text('');
                        }
                        container.data("enable", enable);
                        var $checkBoxLabel = $("<label class='ntsCheckBox-label'></label>");
                        var $checkBox = $('<input type="checkbox">').on("change", function () {
                            if (typeof setChecked === "function")
                                setChecked($(this).is(":checked"));
                        }).appendTo($checkBoxLabel);
                        var $box = $("<span class='box'></span>").appendTo($checkBoxLabel);
                        if (checkBoxText && checkBoxText.length > 0)
                            var label = $("<span class='label'></span>").text(checkBoxText).appendTo($checkBoxLabel);
                        $checkBoxLabel.appendTo(container);
                        container.keypress(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (code === 32) {
                                if (container.data("enable") !== false) {
                                    var checkbox = container.find("input[type='checkbox']:first");
                                    if (checkbox.is(":checked")) {
                                        checkbox.prop("checked", false);
                                        setChecked(false);
                                    }
                                    else {
                                        checkbox.prop("checked", true);
                                        setChecked(true);
                                    }
                                }
                                evt.preventDefault();
                            }
                        });
                    };
                    NtsCheckboxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var checked = ko.unwrap(data.checked);
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.data("enable", enable);
                        container.data("readonly", readonly);
                        var $checkBox = $(element).find("input[type='checkbox']");
                        $checkBox.prop("checked", checked);
                        (enable === true) ? $checkBox.removeAttr("disabled") : $checkBox.attr("disabled", "disabled");
                    };
                    return NtsCheckboxBindingHandler;
                }());
                var NtsMultiCheckBoxBindingHandler = (function () {
                    function NtsMultiCheckBoxBindingHandler() {
                    }
                    NtsMultiCheckBoxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var container = $(element);
                        container.addClass("ntsControl").on("click", function (e) {
                            if (container.data("readonly") === true)
                                e.preventDefault();
                        });
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        container.data("enable", _.clone(enable));
                        container.data("init", true);
                        container.data("tabindex", container.attr("tabindex"));
                        container.removeAttr("tabindex");
                        new nts.uk.util.value.DefaultValue().onReset(container, data.value);
                    };
                    NtsMultiCheckBoxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = data.options === undefined ? [] : JSON.parse(ko.toJSON(data.options));
                        var optionValue = ko.unwrap(data.optionsValue);
                        var optionText = ko.unwrap(data.optionsText);
                        var selectedValue = data.value;
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.data("readonly", readonly);
                        var getOptionValue = function (item) {
                            return (optionValue === undefined) ? item : item[optionValue];
                        };
                        var selectedValues = JSON.parse(ko.toJSON(data.value));
                        if (!_.isEqual(container.data("options"), options)) {
                            container.empty();
                            _.forEach(options, function (option) {
                                var checkBoxLabel = $("<label class='ntsCheckBox'></label>");
                                var checkBox = $('<input type="checkbox">').data("option", option).data("value", getOptionValue(option)).on("change", function () {
                                    var self = $(this);
                                    if (self.is(":checked"))
                                        selectedValue.push(self.data("value"));
                                    else
                                        selectedValue.remove(_.find(selectedValue(), function (value) {
                                            return _.isEqual(JSON.parse(ko.toJSON(value)), self.data("value"));
                                        }));
                                });
                                var disableOption = option["enable"];
                                if (nts.uk.util.isNullOrUndefined(container.data("tabindex")))
                                    checkBoxLabel.attr("tabindex", "0");
                                else {
                                    checkBoxLabel.attr("tabindex", container.data("tabindex"));
                                }
                                checkBoxLabel.keypress(function (evt, ui) {
                                    var code = evt.which || evt.keyCode;
                                    if (code === 32) {
                                        if (container.data("enable") !== false && disableOption !== false) {
                                            var cb = checkBoxLabel.find("input[type='checkbox']:first");
                                            if (cb.is(":checked")) {
                                                cb.prop("checked", false);
                                                selectedValue.remove(_.find(selectedValue(), function (value) {
                                                    return _.isEqual(JSON.parse(ko.toJSON(value)), checkBox.data("value"));
                                                }));
                                            }
                                            else {
                                                if (!cb.is(":checked")) {
                                                    cb.prop("checked", true);
                                                    selectedValue.push(checkBox.data("value"));
                                                }
                                            }
                                        }
                                        evt.preventDefault();
                                    }
                                });
                                if (!nts.uk.util.isNullOrUndefined(disableOption) && (disableOption === false)) {
                                    checkBox.attr("disabled", "disabled");
                                }
                                checkBox.appendTo(checkBoxLabel);
                                var box = $("<span class='box'></span>").appendTo(checkBoxLabel);
                                if (option[optionText] && option[optionText].length > 0)
                                    var label = $("<span class='label'></span>").text(option[optionText]).appendTo(checkBoxLabel);
                                checkBoxLabel.appendTo(container);
                            });
                            container.data("options", _.cloneDeep(options));
                        }
                        container.find("input[type='checkbox']").prop("checked", function () {
                            var _this = this;
                            return (_.find(selectedValue(), function (value) {
                                return _.isEqual(JSON.parse(ko.toJSON(value)), $(_this).data("value"));
                            }) !== undefined);
                        });
                        container.data("enable", _.clone(enable));
                        if (enable === true) {
                            _.forEach(container.find("input[type='checkbox']"), function (checkbox) {
                                var dataOpion = $(checkbox).data("option");
                                if (dataOpion["enable"] === true) {
                                    $(checkbox).removeAttr("disabled");
                                }
                            });
                        }
                        else if (enable === false) {
                            container.find("input[type='checkbox']").attr("disabled", "disabled");
                            new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
                        }
                    };
                    return NtsMultiCheckBoxBindingHandler;
                }());
                ko.bindingHandlers['ntsCheckBox'] = new NtsCheckboxBindingHandler();
                ko.bindingHandlers['ntsMultiCheckBox'] = new NtsMultiCheckBoxBindingHandler();
            })(koExtentions = ui_3.koExtentions || (ui_3.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_4) {
            var koExtentions;
            (function (koExtentions) {
                var ComboBoxBindingHandler = (function () {
                    function ComboBoxBindingHandler() {
                    }
                    ComboBoxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var container = $(element);
                        container.keypress(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (code === 32) {
                                container.igCombo("openDropDown");
                                evt.preventDefault();
                            }
                        });
                    };
                    ComboBoxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var self = this;
                        var options = ko.unwrap(data.options);
                        var optionValue = ko.unwrap(data.optionsValue);
                        var optionText = ko.unwrap(data.optionsText);
                        var selectedValue = ko.unwrap(data.value);
                        var editable = ko.unwrap(data.editable);
                        var enable = ko.unwrap(data.enable);
                        var columns = ko.unwrap(data.columns);
                        var container = $(element);
                        var comboMode = editable ? 'editable' : 'dropdown';
                        var distanceColumns = '     ';
                        var fillCharacter = ' ';
                        var maxWidthCharacter = 15;
                        var defVal = new nts.uk.util.value.DefaultValue().onReset(container, data.value);
                        if (_.find(options, function (item) { return item[optionValue] === selectedValue; }) === undefined && !editable) {
                            selectedValue = options.length > 0 ? options[0][optionValue] : '';
                            data.value(selectedValue);
                        }
                        var haveColumn = columns && columns.length > 0;
                        var isChangeOptions = !_.isEqual(container.data("options"), options);
                        if (isChangeOptions) {
                            container.data("options", options.slice());
                            options = options.map(function (option) {
                                var newOptionText = '';
                                if (haveColumn) {
                                    _.forEach(columns, function (item, i) {
                                        var prop = option[item.prop];
                                        var length = item.length;
                                        if (i === columns.length - 1) {
                                            newOptionText += prop;
                                        }
                                        else {
                                            newOptionText += uk.text.padRight(prop, fillCharacter, length) + distanceColumns;
                                        }
                                    });
                                }
                                else {
                                    newOptionText = option[optionText];
                                }
                                option['nts-combo-label'] = newOptionText;
                                return option;
                            });
                        }
                        var currentColumnSetting = container.data("columns");
                        var currentComboMode = container.data("comboMode");
                        var isInitCombo = !_.isEqual(currentColumnSetting, columns) || !_.isEqual(currentComboMode, comboMode);
                        if (isInitCombo) {
                            if (container.data("igCombo") != null) {
                                container.igCombo('destroy');
                                container.removeClass('ui-state-disabled');
                            }
                            var itemTemplate = undefined;
                            if (haveColumn) {
                                itemTemplate = '<div class="nts-combo-item">';
                                _.forEach(columns, function (item, i) {
                                    itemTemplate += '<div class="nts-column nts-combo-column-' + i + '">${' + item.prop + '}</div>';
                                });
                                itemTemplate += '</div>';
                            }
                            container.igCombo({
                                dataSource: options,
                                valueKey: data.optionsValue,
                                textKey: 'nts-combo-label',
                                mode: comboMode,
                                disabled: !enable,
                                placeHolder: '',
                                enableClearButton: false,
                                initialSelectedItems: [
                                    { value: selectedValue }
                                ],
                                itemTemplate: itemTemplate,
                                selectionChanged: function (evt, ui) {
                                    if (ui.items.length > 0) {
                                        data.value(ui.items[0].data[optionValue]);
                                    }
                                }
                            });
                        }
                        else {
                            container.igCombo("option", "disabled", !enable);
                        }
                        if (!enable)
                            defVal.applyReset(container, data.value);
                        if (isChangeOptions && !isInitCombo) {
                            container.igCombo("option", "dataSource", options);
                            container.igCombo("dataBind");
                        }
                        if (selectedValue !== undefined && selectedValue !== null) {
                            container.igCombo("value", selectedValue);
                        }
                        if (haveColumn && (isChangeOptions || isInitCombo)) {
                            var totalWidth = 0;
                            var $dropDownOptions = $(container.igCombo("dropDown"));
                            _.forEach(columns, function (item, i) {
                                var charLength = item.length;
                                var width = charLength * maxWidthCharacter + 10;
                                $dropDownOptions.find('.nts-combo-column-' + i).width(width);
                                if (i != columns.length - 1) {
                                    $dropDownOptions.find('.nts-combo-column-' + i).css({ 'float': 'left' });
                                }
                                totalWidth += width + 10;
                            });
                            $dropDownOptions.find('.nts-combo-item').css({ 'min-width': totalWidth });
                            container.css({ 'min-width': totalWidth });
                        }
                        container.data("columns", columns);
                        container.data("comboMode", comboMode);
                    };
                    return ComboBoxBindingHandler;
                }());
                ko.bindingHandlers['ntsComboBox'] = new ComboBoxBindingHandler();
            })(koExtentions = ui_4.koExtentions || (ui_4.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var DatePickerBindingHandler = (function () {
                    function DatePickerBindingHandler() {
                    }
                    DatePickerBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var value = data.value;
                        var name = data.name !== undefined ? ko.unwrap(data.name) : "";
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var dateFormat = (data.dateFormat !== undefined) ? ko.unwrap(data.dateFormat) : "YYYY/MM/DD";
                        var ISOFormat = uk.text.getISOFormat(dateFormat);
                        var hasDayofWeek = (ISOFormat.indexOf("ddd") !== -1);
                        var dayofWeekFormat = ISOFormat.replace(/[^d]/g, "");
                        ISOFormat = ISOFormat.replace(/d/g, "").trim();
                        var valueFormat = (data.valueFormat !== undefined) ? ko.unwrap(data.valueFormat) : "";
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        var button = (data.button !== undefined) ? ko.unwrap(data.button) : false;
                        var startDate = (data.startDate !== undefined) ? ko.unwrap(data.startDate) : null;
                        var endDate = (data.endDate !== undefined) ? ko.unwrap(data.endDate) : null;
                        var autoHide = (data.autoHide !== undefined) ? ko.unwrap(data.autoHide) : true;
                        var valueType = typeof value();
                        if (valueType === "string") {
                            valueFormat = (valueFormat) ? valueFormat : uk.text.getISOFormat("ISO");
                        }
                        else if (valueType === "number") {
                            valueFormat = (valueFormat) ? valueFormat : ISOFormat;
                        }
                        else if (valueType === "object") {
                            if (moment.isDate(value())) {
                                valueType = "date";
                            }
                            else if (moment.isMoment(value())) {
                                valueType = "moment";
                            }
                        }
                        var container = $(element);
                        var idString;
                        if (!container.attr("id")) {
                            idString = nts.uk.util.randomId();
                        }
                        else {
                            idString = container.attr("id");
                            container.removeAttr("id");
                        }
                        var containerClass = container.attr('class');
                        container.removeClass(containerClass);
                        container.addClass("ntsControl nts-datepicker-wrapper").data("init", true);
                        var inputClass = (ISOFormat.length < 10) ? "yearmonth-picker" : "";
                        var $input = $("<input id='" + container.attr("id") + "' class='ntsDatepicker nts-input reset-element' />").addClass(inputClass);
                        $input.addClass(containerClass).attr("id", idString).attr("data-name", container.data("name"));
                        container.append($input);
                        if (hasDayofWeek) {
                            var lengthClass = (dayofWeekFormat.length > 3) ? "long-day" : "short-day";
                            var $label = $("<label id='" + idString + "-label' for='" + idString + "' class='dayofweek-label' />");
                            $input.addClass(lengthClass);
                            container.append($label);
                        }
                        $input.datepicker({
                            language: 'ja-JP',
                            format: ISOFormat,
                            startDate: startDate,
                            endDate: endDate,
                            autoHide: autoHide,
                        });
                        DatePickerNormalizer.getInstance($input).setCssRanger(data.cssRanger)
                            .fiscalMonthsMode(data.fiscalMonthsMode)
                            .setDefaultCss(data.defaultClass || "");
                        name = nts.uk.resource.getControlName(name);
                        var validator = new ui.validation.TimeValidator(name, constraintName, { required: required,
                            outputFormat: nts.uk.util.isNullOrEmpty(valueFormat) ? ISOFormat : valueFormat, valueType: valueType });
                        $input.on("change", function (e) {
                            var newText = $input.val();
                            var result = validator.validate(newText);
                            $input.ntsError('clear');
                            if (result.isValid) {
                                if (hasDayofWeek) {
                                    if (uk.util.isNullOrEmpty(result.parsedValue))
                                        $label.text("");
                                    else
                                        $label.text("(" + uk.time.formatPattern(newText, "", dayofWeekFormat) + ")");
                                }
                                value(result.parsedValue);
                            }
                            else {
                                $input.ntsError('set', result.errorMessage);
                                value(newText);
                            }
                        });
                        $input.on("blur", function () {
                            var newText = $input.val();
                            var result = validator.validate(newText);
                            if (!result.isValid) {
                                $input.ntsError('set', result.errorMessage);
                            }
                        });
                        $input.on('validate', (function (e) {
                            var newText = $input.val();
                            var result = validator.validate(newText);
                            $input.ntsError('clear');
                            if (!result.isValid) {
                                $input.ntsError('set', result.errorMessage);
                            }
                        }));
                        new nts.uk.util.value.DefaultValue().onReset($input, data.value);
                    };
                    DatePickerBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var value = data.value;
                        var dateFormat = (data.dateFormat !== undefined) ? ko.unwrap(data.dateFormat) : "YYYY/MM/DD";
                        var ISOFormat = uk.text.getISOFormat(dateFormat);
                        var hasDayofWeek = (ISOFormat.indexOf("ddd") !== -1);
                        var dayofWeekFormat = ISOFormat.replace(/[^d]/g, "");
                        ISOFormat = ISOFormat.replace(/d/g, "").trim();
                        var valueFormat = (data.valueFormat !== undefined) ? ko.unwrap(data.valueFormat) : ISOFormat;
                        var disabled = (data.disabled !== undefined) ? ko.unwrap(data.disabled) : false;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : undefined;
                        var startDate = (data.startDate !== undefined) ? ko.unwrap(data.startDate) : null;
                        var endDate = (data.endDate !== undefined) ? ko.unwrap(data.endDate) : null;
                        var container = $(element);
                        var init = container.data("init");
                        var $input = container.find(".nts-input");
                        var $label = container.find(".dayofweek-label");
                        var dateFormatValue = (value() !== "") ? uk.time.formatPattern(value(), valueFormat, ISOFormat) : "";
                        if (dateFormatValue !== "" && dateFormatValue !== "Invalid date") {
                            $input.datepicker('setDate', dateFormatValue);
                            $label.text("(" + uk.time.formatPattern(value(), valueFormat, dayofWeekFormat) + ")");
                        }
                        else {
                            $input.val("");
                            $label.text("");
                        }
                        container.data("init", false);
                        $input.datepicker('setStartDate', startDate);
                        $input.datepicker('setEndDate', endDate);
                        if (enable !== undefined)
                            $input.prop("disabled", !enable);
                        else
                            $input.prop("disabled", disabled);
                        if ($input.prop("disabled") === true) {
                            new nts.uk.util.value.DefaultValue().applyReset($input, value);
                        }
                        if (data.button)
                            container.find('.datepicker-btn').prop("disabled", disabled);
                    };
                    return DatePickerBindingHandler;
                }());
                ko.bindingHandlers['ntsDatePicker'] = new DatePickerBindingHandler();
                var ViewLocation;
                (function (ViewLocation) {
                    ViewLocation[ViewLocation["PREV"] = 0] = "PREV";
                    ViewLocation[ViewLocation["CURRENT"] = 1] = "CURRENT";
                    ViewLocation[ViewLocation["NEXT"] = 2] = "NEXT";
                })(ViewLocation || (ViewLocation = {}));
                var DatePickerNormalizer = (function () {
                    function DatePickerNormalizer() {
                        this.fiscalMonth = 1;
                        this.NAMESPACE = "datepicker";
                        this.YEARS = "years";
                        this.MONTHS = "months";
                        this.DAYS = "days";
                        this.WEEK = "week";
                        this.PICKER = " picker";
                        this.YEAR = "year";
                        this.MONTH = "month";
                        this.DAY = "day";
                        this.YEAR_TEXT = "年";
                        this.MONTH_TEXT = "月";
                        this.PERIOD_TEXT = "度";
                        this.structure = { 0: this.YEARS, 1: this.MONTHS, 2: this.DAYS };
                        this.EVENT_SHOW = "show." + this.NAMESPACE;
                        this.EVENT_KEYUP = "keyup." + this.NAMESPACE;
                        this.EVENT_PICK = "pick." + this.NAMESPACE;
                        this.YM_FORMAT = "YYYY/MM";
                        this.YMD_FORMAT = "YYYY/MM/DD";
                        this.DATE_SPLITTER = "/";
                    }
                    DatePickerNormalizer.getInstance = function ($input) {
                        var instance = new DatePickerNormalizer();
                        instance.$input = $input;
                        return instance.onShow().onKeyup().onPick();
                    };
                    DatePickerNormalizer.prototype.setCssRanger = function (range) {
                        this.cssRanger = range;
                        return this;
                    };
                    DatePickerNormalizer.prototype.setFiscalMonth = function (month) {
                        this.fiscalMonth = month;
                        return this;
                    };
                    DatePickerNormalizer.prototype.setDefaultCss = function (clazz) {
                        this.defaultCss = clazz;
                        return this;
                    };
                    DatePickerNormalizer.prototype.fiscalMonthsMode = function (value) {
                        if (value === true)
                            this.setFiscalMonth(4);
                        return this;
                    };
                    DatePickerNormalizer.prototype.getPicker = function () {
                        return this.$input.data(this.NAMESPACE).$picker;
                    };
                    DatePickerNormalizer.prototype.getYearsPicker = function () {
                        return this.$input.data(this.NAMESPACE).$yearsPicker;
                    };
                    DatePickerNormalizer.prototype.getMonthsPicker = function () {
                        return this.$input.data(this.NAMESPACE).$monthsPicker;
                    };
                    DatePickerNormalizer.prototype.getYearsBoard = function () {
                        return this.$input.data(this.NAMESPACE).$years;
                    };
                    DatePickerNormalizer.prototype.getMonthsBoard = function () {
                        return this.$input.data(this.NAMESPACE).$months;
                    };
                    DatePickerNormalizer.prototype.getCurrentYear = function () {
                        return this.$input.data(this.NAMESPACE).$yearCurrent;
                    };
                    DatePickerNormalizer.prototype.getView = function (view, isCurrentView) {
                        var pickerView, viewPart, currentViewPart;
                        var viewName = this.structure[view];
                        switch (viewName) {
                            case this.YEARS:
                                pickerView = this.YEARS + this.PICKER;
                                viewPart = this.YEARS;
                                currentViewPart = "current year";
                                break;
                            case this.MONTHS:
                                pickerView = this.MONTHS + this.PICKER;
                                viewPart = this.MONTHS;
                                currentViewPart = "current month";
                                break;
                            case this.DAYS:
                                pickerView = this.DAYS + this.PICKER;
                                viewPart = this.DAYS;
                                break;
                            case this.WEEK:
                                pickerView = this.DAYS + this.PICKER;
                                viewPart = this.WEEK;
                                break;
                        }
                        return $(this.getPicker()[0]).children().filter(function (idx, elm) {
                            return $(elm).data("view") === pickerView;
                        }).find("ul").filter(function (idx, elm) {
                            if (isCurrentView === true)
                                return idx === 0;
                            else
                                return $(elm).data("view") === viewPart;
                        });
                    };
                    DatePickerNormalizer.prototype.getMutedClass = function () {
                        return this.options !== undefined ? this.options.mutedClass : "";
                    };
                    DatePickerNormalizer.prototype.getPickedClass = function () {
                        return this.options !== undefined ? this.options.pickedClass : "";
                    };
                    DatePickerNormalizer.prototype.setColorLevel = function () {
                        if (this.options.format === this.YM_FORMAT)
                            this.colorLevel = this.MONTHS;
                        else if (this.options.format === this.YMD_FORMAT)
                            this.colorLevel = this.DAYS;
                        if (this.selectedView === undefined)
                            this.selectedView = this.colorLevel;
                    };
                    DatePickerNormalizer.prototype.color = function () {
                        if (this.cssRanger === undefined)
                            return;
                        this.colorNode(this.cssRanger, ViewLocation.CURRENT, 0);
                        this.colorNode(this.cssRanger, ViewLocation.NEXT, 0);
                        this.colorNode(this.cssRanger, ViewLocation.PREV, 0);
                    };
                    DatePickerNormalizer.prototype.colorNode = function (holders, location, currentLayer) {
                        var _this = this;
                        var holder;
                        var handledYear = location === ViewLocation.CURRENT ? this.viewYear : this.viewYear + 1;
                        if (this.colorLevel === this.structure[currentLayer + 1]) {
                            switch (currentLayer) {
                                case 0:
                                    holder = handledYear;
                                    break;
                                case 1:
                                    if (location === ViewLocation.CURRENT)
                                        holder = this.viewMonth;
                                    else if (location === ViewLocation.PREV)
                                        holder = this.viewMonth - 1;
                                    else
                                        holder = this.viewMonth + 1;
                                    break;
                                case 2:
                                    holder = this.date;
                                    break;
                                default:
                                    holder = handledYear;
                                    currentLayer = 0;
                                    break;
                            }
                        }
                        else {
                            switch (currentLayer) {
                                case 0:
                                    holder = this.viewYear;
                                    break;
                                case 1:
                                    holder = this.viewMonth;
                                    break;
                                case 2:
                                    holder = this.date;
                                    break;
                                default:
                                    holder = this.viewYear;
                                    currentLayer = 0;
                                    break;
                            }
                        }
                        if (holders.hasOwnProperty(holder)) {
                            if (holders[holder].constructor === Array) {
                                _.each(holders[holder], function (cell) { return _this.colorCell(cell, location, currentLayer); });
                                return;
                            }
                            currentLayer++;
                            this.colorNode(holders[holder], location, currentLayer);
                        }
                    };
                    DatePickerNormalizer.prototype.colorCell = function (cell, location, layer) {
                        var self = this;
                        var data = typeof cell === "object" ? Object.keys(cell)[0] : cell;
                        var $target = this.$view.children().filter(function (idx, elm) {
                            if (self.structure[layer] === self.YEARS) {
                                return $(elm).text() === self.defaultMonths[data - 1]
                                    && ((location === ViewLocation.PREV && $(elm).data("view").indexOf("prev") !== -1)
                                        || (location === ViewLocation.NEXT && $(elm).data("view").indexOf("next") !== -1)
                                        || location === ViewLocation.CURRENT && $(elm).data("view").indexOf("prev") === -1
                                            && $(elm).data("view").indexOf("next") === -1);
                            }
                            else if (self.structure[layer] === self.MONTHS) {
                                return $(elm).text() === data.toString()
                                    && ((location === ViewLocation.PREV && $(elm).data("view").indexOf("prev") !== -1)
                                        || (location === ViewLocation.NEXT && $(elm).data("view").indexOf("next") !== -1)
                                        || location === ViewLocation.CURRENT && $(elm).data("view").indexOf("prev") === -1
                                            && $(elm).data("view").indexOf("next") === -1);
                            }
                        });
                        if ($target.length > 0) {
                            $target.addClass((typeof cell === "object" && cell[data] !== undefined) ? cell[data] : this.defaultCss);
                        }
                    };
                    DatePickerNormalizer.prototype.fillFiscalMonthsInYear = function () {
                        var self = this;
                        if (this.fiscalMonth === 1)
                            return;
                        var nextYearMonths = this.defaultMonths.slice(0, this.fiscalMonth - 1);
                        var currentYearMonths = this.defaultMonths.slice(this.fiscalMonth - 1);
                        var newMonths = $.merge(currentYearMonths, nextYearMonths);
                        var nextYearMark = 12 - this.fiscalMonth;
                        this.getMonthsBoard().children().each(function (idx, elm) {
                            $(elm).text(newMonths[idx]);
                            if (idx > nextYearMark)
                                $(elm).addClass(self.getMutedClass()).attr("data-view", "fiscalMonth next")
                                    .data("view", "fiscalMonth next").css("font-size", "inherit");
                        });
                        if (this.viewMonth < this.fiscalMonth) {
                            var self = this;
                            var $currentYear = this.getCurrentYear();
                            if ($currentYear.length > 0)
                                $currentYear.text(this.viewYear + this.yearText());
                        }
                    };
                    DatePickerNormalizer.prototype.allowPickMonth = function () {
                        return (this.viewMonth < this.fiscalMonth && this.viewYear === this.year - 1)
                            || (this.viewMonth >= this.fiscalMonth && this.viewYear === this.year);
                    };
                    DatePickerNormalizer.prototype.allowPickDate = function () {
                        return this.viewYear === this.year && this.viewMonth === this.month;
                    };
                    DatePickerNormalizer.prototype.pickMonth = function () {
                        var self = this;
                        if (self.fiscalMonth === 1)
                            return;
                        var month = self.month + self.MONTH_TEXT;
                        this.getMonthsBoard().children().each(function (idx, elm) {
                            var view;
                            if ($(elm).text() === month.toString()) {
                                view = "month picked";
                                $(elm).addClass(self.getPickedClass()).attr("data-view", view).data("view", view);
                            }
                            else if ($(elm).hasClass(self.getPickedClass())) {
                                view = $(elm).data("view").split(" ")[0];
                                $(elm).removeClass(self.getPickedClass()).attr("data-view", view).data("view", view);
                            }
                        });
                    };
                    DatePickerNormalizer.prototype.pickDate = function () {
                        var self = this;
                        if (self.colorLevel !== self.DAYS || self.fiscalMonth === 1)
                            return;
                        var date = self.date;
                        this.$view.children().each(function (idx, elm) {
                            if ($(elm).text() === date.toString() && $(elm).data("view").indexOf("prev") === -1
                                && $(elm).data("view").indexOf("next") === -1) {
                                $(elm).addClass(self.getPickedClass()).attr("data-view", "day picked").data("view", "day picked");
                            }
                            else if ($(elm).hasClass(self.getPickedClass())) {
                                $(elm).removeClass(self.getPickedClass()).attr("data-view", "day").data("view", "day");
                            }
                        });
                    };
                    DatePickerNormalizer.prototype.clearPicked = function () {
                        var self = this;
                        var view = self.colorLevel === self.MONTHS ? "month" : "day";
                        var $selectedBoard;
                        if (this.selectedView === this.MONTHS) {
                            $selectedBoard = this.getMonthsBoard();
                        }
                        else if (this.selectedView === this.DAYS) {
                            $selectedBoard = this.getYearsBoard();
                        }
                        if ($selectedBoard === undefined)
                            return;
                        $selectedBoard.children().filter(function (idx, elm) {
                            return $(elm).data("view").indexOf("picked") !== -1;
                        }).removeClass(self.getPickedClass()).attr("data-view", view).data("view", view);
                    };
                    DatePickerNormalizer.prototype.yearText = function () {
                        return this.fiscalMonth !== 1 ? this.YEAR_TEXT + this.PERIOD_TEXT : this.YEAR_TEXT;
                    };
                    DatePickerNormalizer.prototype.onClick = function () {
                        var self = this;
                        var picker = this.getPicker();
                        picker.off("click", this._click);
                        picker.on("click", $.proxy(this._click, this));
                    };
                    DatePickerNormalizer.prototype._click = function (evt) {
                        var $target = $(evt.target);
                        var view = $target.data("view");
                        switch (view) {
                            case "year prev":
                                this.viewYear--;
                                this.updateMonthsView();
                                break;
                            case "year next":
                                this.viewYear++;
                                this.updateMonthsView();
                                break;
                            case "month prev":
                                if (this.viewMonth == 1) {
                                    this.viewMonth = 12;
                                    this.viewYear--;
                                }
                                else
                                    this.viewMonth--;
                                this.updateDaysView();
                                break;
                            case "month next":
                                if (this.viewMonth == 12) {
                                    this.viewMonth = 1;
                                    this.viewYear++;
                                }
                                else
                                    this.viewMonth++;
                                this.updateDaysView();
                                break;
                            case "day prev":
                                this.updateDaysView();
                                break;
                            case "day next":
                                this.updateDaysView();
                                break;
                            case "fiscalMonth next":
                                if ($target.hasClass(this.getPickedClass()))
                                    return;
                                var pickedMonth = this.defaultMonths.indexOf($target.text());
                                this._clickFiscalNextMonth(pickedMonth);
                                this.$input.datepicker("hide");
                                if (this.colorLevel === this.DAYS) {
                                    this.$input.datepicker("show");
                                }
                                break;
                            case "year current":
                                this.selectedView = this.YEARS;
                                break;
                            case "month current":
                                this.selectedView = this.MONTHS;
                                if (this.viewMonth < this.fiscalMonth)
                                    this.viewYear--;
                                this.updateMonthsView();
                                break;
                        }
                    };
                    DatePickerNormalizer.prototype.updateMonthsView = function () {
                        if (this.fiscalMonth !== 1) {
                            this.fillFiscalMonthsInYear();
                        }
                        if (this.colorLevel === this.MONTHS) {
                            this.color();
                        }
                        if (this.allowPickMonth())
                            this.pickMonth();
                        if (this.viewMonth < this.fiscalMonth && this.viewYear === this.year)
                            this.clearPicked();
                    };
                    DatePickerNormalizer.prototype.updateDaysView = function () {
                        if (this.colorLevel === this.DAYS) {
                            this.color();
                        }
                        if (this.allowPickDate())
                            this.pickDate();
                    };
                    DatePickerNormalizer.prototype._beforeShow = function () {
                        this.options = this.$input.data(this.NAMESPACE).options;
                        this.setColorLevel();
                        var initValue = this.$input.datepicker("getDate", true);
                        var viewTime = this.$input.data(this.NAMESPACE).viewDate;
                        this.viewYear = viewTime.getFullYear();
                        this.viewMonth = viewTime.getMonth() + 1;
                        this.defaultMonths = this.options.months;
                        var parsedTime;
                        if (this.options.format === this.YMD_FORMAT)
                            parsedTime = this.parseDate(initValue);
                        else if (this.options.format === this.YM_FORMAT)
                            parsedTime = this.parseDate(initValue);
                        if (parsedTime !== undefined) {
                            this.year = parsedTime.year;
                            this.month = parsedTime.month;
                            this.date = parsedTime.date;
                        }
                        else
                            return;
                        var colorLevel = this.colorLevel;
                        var layer = colorLevel === this.MONTHS ? 1 : 2;
                        this.$view = this.getView(layer);
                        this.$currentView = this.getView(layer, true);
                        if (this.selectedView === this.MONTHS) {
                            if (this.viewMonth < this.fiscalMonth)
                                this.viewYear--;
                            this.fillFiscalMonthsInYear();
                        }
                        this.color();
                        if (this.selectedView === this.MONTHS && this.allowPickMonth()) {
                            if (this.viewMonth < this.fiscalMonth && this.viewYear === this.year)
                                this.clearPicked();
                            this.pickMonth();
                        }
                        else if (this.selectedView === this.DAYS && this.allowPickDate()) {
                            this.pickDate();
                        }
                    };
                    DatePickerNormalizer.prototype.parseDate = function (date) {
                        var exp = new RegExp(/\d+\/\d+(\/\d+)?/);
                        if (exp.test(date) === false)
                            return;
                        var dateParts = date.split(this.DATE_SPLITTER);
                        return {
                            year: parseInt(dateParts[0]),
                            month: parseInt(dateParts[1]),
                            date: dateParts[2] !== undefined ? parseInt(dateParts[2]) : undefined
                        };
                    };
                    DatePickerNormalizer.prototype.onShow = function () {
                        var self = this;
                        this.$input.on(this.EVENT_SHOW, function (evt) {
                            var _self = self;
                            setTimeout(function () {
                                _self._beforeShow.call(_self);
                                _self.onClick.call(_self);
                            }, 0);
                        });
                        return self;
                    };
                    DatePickerNormalizer.prototype.onKeyup = function () {
                        this.$input.off(this.EVENT_KEYUP, this._beforeShow);
                        this.$input.on(this.EVENT_KEYUP, $.proxy(this._beforeShow, this));
                        return this;
                    };
                    DatePickerNormalizer.prototype.onPick = function () {
                        var self = this;
                        this.$input.on(this.EVENT_PICK, function (evt) {
                            var view = evt.view;
                            if (view === self.DAY) {
                                self.date = evt.date.getDate();
                                self.month = evt.date.getMonth() + 1;
                                self.viewMonth = self.month;
                                self.year = evt.date.getFullYear();
                                self.viewYear = self.year;
                            }
                            else if (view === self.MONTH) {
                                self._clickFiscalNextMonth.call(self, evt.date.getMonth());
                            }
                            else if (view === self.YEAR) {
                                var _self = self;
                                setTimeout(function () {
                                    _self.year = evt.date.getFullYear();
                                    _self.viewYear = _self.year;
                                    _self.month = _self.viewMonth;
                                    if (_self.viewMonth < _self.fiscalMonth)
                                        _self.viewYear--;
                                    _self.updateMonthsView.call(_self);
                                }, 0);
                            }
                        });
                        return self;
                    };
                    DatePickerNormalizer.prototype._clickFiscalNextMonth = function (pickedMonth) {
                        var self = this;
                        self.month = pickedMonth + 1;
                        self.viewMonth = self.month;
                        if (self.fiscalMonth !== 1) {
                            self.year = self.month >= self.fiscalMonth ? self.viewYear : (self.viewYear + 1);
                            self.viewYear = self.year;
                            self.$input.datepicker("setDate", new Date(self.year, self.month - 1, self.date || 1));
                        }
                    };
                    return DatePickerNormalizer;
                }());
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsDialogBindingHandler = (function () {
                    function NtsDialogBindingHandler() {
                    }
                    NtsDialogBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                    };
                    NtsDialogBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var option = ko.unwrap(data.option);
                        var title = ko.unwrap(data.title);
                        var message = ko.unwrap(data.message);
                        var modal = ko.unwrap(option.modal);
                        var show = ko.unwrap(option.show);
                        var buttons = ko.unwrap(option.buttons);
                        var $dialog = $("<div id='ntsDialog'></div>");
                        if (show == true) {
                            $('body').append($dialog);
                            var dialogbuttons = [];
                            var _loop_1 = function(button) {
                                dialogbuttons.push({
                                    text: ko.unwrap(button.text),
                                    "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                                    click: function () { button.click(bindingContext.$data, $dialog); }
                                });
                            };
                            for (var _i = 0, buttons_1 = buttons; _i < buttons_1.length; _i++) {
                                var button = buttons_1[_i];
                                _loop_1(button);
                            }
                            $dialog.dialog({
                                title: title,
                                modal: modal,
                                closeOnEscape: false,
                                buttons: dialogbuttons,
                                dialogClass: "no-close",
                                open: function () {
                                    $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                                    $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                                    $('.ui-widget-overlay').last().css('z-index', 120000);
                                },
                                close: function (event) {
                                    bindingContext.$data.option.show(false);
                                }
                            }).text(message);
                        }
                        else {
                            if ($('#ntsDialog').dialog("instance") != null)
                                $('#ntsDialog').dialog("destroy");
                            $('#ntsDialog').remove();
                        }
                    };
                    return NtsDialogBindingHandler;
                }());
                var NtsErrorDialogBindingHandler = (function () {
                    function NtsErrorDialogBindingHandler() {
                    }
                    NtsErrorDialogBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var option = ko.unwrap(data.option);
                        var title = ko.unwrap(data.title);
                        var headers = ko.unwrap(option.headers);
                        var modal = ko.unwrap(option.modal);
                        var show = ko.unwrap(option.show);
                        var buttons = ko.unwrap(option.buttons);
                        var $dialog = $("<div id='ntsErrorDialog'></div>");
                        $('body').append($dialog);
                        var dialogbuttons = [];
                        var _loop_2 = function(button) {
                            dialogbuttons.push({
                                text: ko.unwrap(button.text),
                                "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                                click: function () { button.click(bindingContext.$data, $dialog); }
                            });
                        };
                        for (var _i = 0, buttons_2 = buttons; _i < buttons_2.length; _i++) {
                            var button = buttons_2[_i];
                            _loop_2(button);
                        }
                        var dialogWidth = 40 + 35 + 17;
                        headers.forEach(function (header, index) {
                            if (ko.unwrap(header.visible)) {
                                if (typeof ko.unwrap(header.width) === "number") {
                                    dialogWidth += ko.unwrap(header.width);
                                }
                                else {
                                    dialogWidth += 200;
                                }
                            }
                        });
                        $dialog.dialog({
                            title: title,
                            modal: modal,
                            closeOnEscape: false,
                            width: dialogWidth,
                            maxHeight: 500,
                            buttons: dialogbuttons,
                            dialogClass: "no-close",
                            open: function () {
                                $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                                $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                                $('.ui-widget-overlay').last().css('z-index', 120000);
                            },
                            close: function (event) {
                                bindingContext.$data.option.show(false);
                            }
                        });
                    };
                    NtsErrorDialogBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var option = ko.unwrap(data.option);
                        var title = ko.unwrap(data.title);
                        var errors = ko.unwrap(data.errors);
                        var headers = ko.unwrap(option.headers);
                        var displayrows = ko.unwrap(option.displayrows);
                        var autoclose = ko.unwrap(option.autoclose);
                        var show = ko.unwrap(option.show);
                        var $dialog = $("#ntsErrorDialog");
                        if (show == true) {
                            var $errorboard = $("<div id='error-board'></div>");
                            var $errortable = $("<table></table>");
                            var $header = $("<thead></thead>");
                            var $headerRow_1 = $("<tr></tr>");
                            $headerRow_1.append("<th style='display:none;'></th>");
                            headers.forEach(function (header, index) {
                                if (ko.unwrap(header.visible)) {
                                    var $headerElement = $("<th>" + ko.unwrap(header.text) + "</th>").width(ko.unwrap(header.width));
                                    $headerRow_1.append($headerElement);
                                }
                            });
                            $header.append($headerRow_1);
                            $errortable.append($header);
                            var $body = $("<tbody></tbody>");
                            errors.forEach(function (error, index) {
                                var $row = $("<tr></tr>");
                                $row.click(function () {
                                    error.$control[0].focus();
                                });
                                $row.append("<td style='display:none;'>" + (index + 1) + "</td>");
                                headers.forEach(function (header) {
                                    if (ko.unwrap(header.visible))
                                        if (error.hasOwnProperty(ko.unwrap(header.name))) {
                                            var $column = $("<td>" + error[ko.unwrap(header.name)] + "</td>");
                                            $row.append($column);
                                        }
                                });
                                $body.append($row);
                            });
                            $errortable.append($body);
                            $errorboard.append($errortable);
                            var $message = $("<div></div>");
                            $dialog.html("");
                            $dialog.append($errorboard).append($message);
                            $dialog.on("dialogopen", function () {
                                var maxrowsHeight = 0;
                                var index = 0;
                                $(this).find("table tbody tr").each(function () {
                                    if (index < displayrows) {
                                        index++;
                                        maxrowsHeight += $(this).height();
                                    }
                                });
                                maxrowsHeight = maxrowsHeight + 33 + 20 + 20 + 55 + 4 + $(this).find("table thead").height();
                                if (maxrowsHeight > $dialog.dialog("option", "maxHeight")) {
                                    maxrowsHeight = $dialog.dialog("option", "maxHeight");
                                }
                                $dialog.dialog("option", "height", maxrowsHeight);
                            });
                            $dialog.dialog("open");
                        }
                        else {
                            $dialog.dialog("close");
                        }
                    };
                    return NtsErrorDialogBindingHandler;
                }());
                ko.bindingHandlers['ntsDialog'] = new NtsDialogBindingHandler();
                ko.bindingHandlers['ntsErrorDialog'] = new NtsErrorDialogBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var validation = nts.uk.ui.validation;
                var EditorProcessor = (function () {
                    function EditorProcessor() {
                    }
                    EditorProcessor.prototype.init = function ($input, data) {
                        var _this = this;
                        var self = this;
                        var value = data.value;
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var constraint = validation.getConstraint(constraintName);
                        var immediate = ko.unwrap(data.immediate !== undefined ? data.immediate : 'false');
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
                        var valueUpdate = (immediate === true) ? 'input' : 'change';
                        var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : {};
                        this.editorOption = $.extend(this.getDefaultOption(), option);
                        var characterWidth = 9;
                        if (constraint && constraint.maxLength && !$input.is("textarea")) {
                            var autoWidth = constraint.maxLength * characterWidth;
                            $input.width(autoWidth);
                        }
                        $input.addClass('nts-editor nts-input');
                        $input.wrap("<span class= 'nts-editor-wrapped ntsControl'/>");
                        $input.on(valueUpdate, function (e) {
                            var newText = $input.val();
                            var validator = _this.getValidator(data);
                            var result = validator.validate(newText);
                            if (result.isValid) {
                                $input.ntsError('clear');
                                value(result.parsedValue);
                            }
                            else {
                                var error = $input.ntsError('getError');
                                if (nts.uk.util.isNullOrUndefined(error) || error.messageText !== result.errorMessage) {
                                    $input.ntsError('clear');
                                    $input.ntsError('set', result.errorMessage);
                                }
                                value(newText);
                            }
                        });
                        $input.blur(function () {
                            if (!$input.attr('readonly')) {
                                var formatter = self.getFormatter(data);
                                var newText = $input.val();
                                var validator = self.getValidator(data);
                                var result = validator.validate(newText);
                                if (result.isValid) {
                                    $input.ntsError('clear');
                                    $input.val(formatter.format(result.parsedValue));
                                }
                                else {
                                    var error = $input.ntsError('getError');
                                    if (nts.uk.util.isNullOrUndefined(error) || error.messageText !== result.errorMessage) {
                                        $input.ntsError('clear');
                                        $input.ntsError('set', result.errorMessage);
                                    }
                                    value(newText);
                                }
                            }
                        });
                        $input.on('validate', (function (e) {
                            var newText = $input.val();
                            var validator = self.getValidator(data);
                            var result = validator.validate(newText);
                            $input.ntsError('clear');
                            if (!result.isValid) {
                                $input.ntsError('set', result.errorMessage);
                            }
                        }));
                        new nts.uk.util.value.DefaultValue().onReset($input, data.value);
                    };
                    EditorProcessor.prototype.update = function ($input, data) {
                        var value = data.value;
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
                        var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : {};
                        this.editorOption = $.extend(this.getDefaultOption(), option);
                        var placeholder = this.editorOption.placeholder;
                        var textalign = this.editorOption.textalign;
                        var width = this.editorOption.width;
                        if (enable !== false) {
                            $input.removeAttr('disabled');
                        }
                        else {
                            $input.attr('disabled', 'disabled');
                            new nts.uk.util.value.DefaultValue().applyReset($input, value);
                        }
                        (readonly === false) ? $input.removeAttr('readonly') : $input.attr('readonly', 'readonly');
                        $input.attr('placeholder', placeholder);
                        $input.css('text-align', textalign);
                        if (width.trim() != "")
                            $input.width(width);
                        var formatted = $input.ntsError('hasError') ? value() : this.getFormatter(data).format(value());
                        $input.val(formatted);
                    };
                    EditorProcessor.prototype.getDefaultOption = function () {
                        return {};
                    };
                    EditorProcessor.prototype.getFormatter = function (data) {
                        return new uk.format.NoFormatter();
                    };
                    EditorProcessor.prototype.getValidator = function (data) {
                        return new validation.NoValidator();
                    };
                    return EditorProcessor;
                }());
                var TextEditorProcessor = (function (_super) {
                    __extends(TextEditorProcessor, _super);
                    function TextEditorProcessor() {
                        _super.apply(this, arguments);
                    }
                    TextEditorProcessor.prototype.init = function ($input, data) {
                        var value = data.value;
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var constraint = validation.getConstraint(constraintName);
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : false;
                        var characterWidth = 9;
                        if (constraint && constraint.maxLength && !$input.is("textarea")) {
                            var autoWidth = constraint.maxLength * characterWidth;
                            $input.width(autoWidth);
                        }
                        $input.addClass('nts-editor nts-input');
                        $input.wrap("<span class= 'nts-editor-wrapped ntsControl'/>");
                        var validator = this.getValidator(data);
                        $input.on("keyup", function (e) {
                            var code = e.keyCode || e.which;
                            if (!readonly && code.toString() !== '9') {
                                var newText = $input.val();
                                var result = validator.validate(newText);
                                $input.ntsError('clear');
                                if (!result.isValid) {
                                    $input.ntsError('set', result.errorMessage);
                                }
                            }
                        });
                        $input.on("change", function (e) {
                            if (!$input.attr('readonly')) {
                                var newText = $input.val();
                                var result = validator.validate(newText, { isCheckExpression: true });
                                $input.ntsError('clear');
                                if (result.isValid) {
                                    if (value() === result.parsedValue) {
                                        $input.val(result.parsedValue);
                                    }
                                    else {
                                        value(result.parsedValue);
                                    }
                                }
                                else {
                                    $input.ntsError('set', result.errorMessage);
                                    value(newText);
                                }
                            }
                        });
                        $input.on('validate', (function (e) {
                            var newText = $input.val();
                            var result = validator.validate(newText);
                            $input.ntsError('clear');
                            if (!result.isValid) {
                                $input.ntsError('set', result.errorMessage);
                            }
                        }));
                        new nts.uk.util.value.DefaultValue().onReset($input, data.value);
                    };
                    TextEditorProcessor.prototype.update = function ($input, data) {
                        _super.prototype.update.call(this, $input, data);
                        var textmode = this.editorOption.textmode;
                        $input.attr('type', textmode);
                    };
                    TextEditorProcessor.prototype.getDefaultOption = function () {
                        return new nts.uk.ui.option.TextEditorOption();
                    };
                    TextEditorProcessor.prototype.getFormatter = function (data) {
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var constraint = validation.getConstraint(constraintName);
                        return new uk.text.StringFormatter({ constraintName: constraintName, constraint: constraint, editorOption: this.editorOption });
                    };
                    TextEditorProcessor.prototype.getValidator = function (data) {
                        var name = data.name !== undefined ? ko.unwrap(data.name) : "";
                        name = nts.uk.resource.getControlName(name);
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        return new validation.StringValidator(name, constraintName, { required: required });
                    };
                    return TextEditorProcessor;
                }(EditorProcessor));
                var MultilineEditorProcessor = (function (_super) {
                    __extends(MultilineEditorProcessor, _super);
                    function MultilineEditorProcessor() {
                        _super.apply(this, arguments);
                    }
                    MultilineEditorProcessor.prototype.update = function ($input, data) {
                        _super.prototype.update.call(this, $input, data);
                        var resizeable = this.editorOption.resizeable;
                        $input.css('resize', (resizeable) ? "both" : "none");
                    };
                    MultilineEditorProcessor.prototype.getDefaultOption = function () {
                        return new ui.option.MultilineEditorOption();
                    };
                    MultilineEditorProcessor.prototype.getFormatter = function (data) {
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var constraint = validation.getConstraint(constraintName);
                        return new uk.text.StringFormatter({ constraintName: constraintName, constraint: constraint, editorOption: this.editorOption });
                    };
                    MultilineEditorProcessor.prototype.getValidator = function (data) {
                        var name = data.name !== undefined ? ko.unwrap(data.name) : "";
                        name = nts.uk.resource.getControlName(name);
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        return new validation.StringValidator(name, constraintName, { required: required });
                    };
                    return MultilineEditorProcessor;
                }(EditorProcessor));
                var NumberEditorProcessor = (function (_super) {
                    __extends(NumberEditorProcessor, _super);
                    function NumberEditorProcessor() {
                        _super.apply(this, arguments);
                    }
                    NumberEditorProcessor.prototype.init = function ($input, data) {
                        _super.prototype.init.call(this, $input, data);
                        $input.focus(function () {
                            var selectionType = document.getSelection().type;
                            $input.val(data.value());
                            if (selectionType === 'Range') {
                                $input.select();
                            }
                        });
                    };
                    NumberEditorProcessor.prototype.update = function ($input, data) {
                        _super.prototype.update.call(this, $input, data);
                        var $parent = $input.parent();
                        var width = this.editorOption.width;
                        var parentTag = $parent.parent().prop("tagName").toLowerCase();
                        if (parentTag === "td" || parentTag === "th" || parentTag === "a" || width === "100%") {
                            $parent.css({ 'width': '100%' });
                        }
                        $input.css("box-sizing", "border-box");
                        if (width.trim() != "")
                            $input.width(width);
                        if (this.editorOption.currencyformat !== undefined && this.editorOption.currencyformat !== null) {
                            $parent.addClass("symbol").addClass(this.editorOption.currencyposition === 'left' ? 'symbol-left' : 'symbol-right');
                            var format = this.editorOption.currencyformat === "JPY" ? "\u00A5" : '$';
                            $parent.attr("data-content", format);
                        }
                        else if (this.editorOption.symbolChar !== undefined && this.editorOption.symbolChar !== "" && this.editorOption.symbolPosition !== undefined) {
                            $parent.addClass("symbol").addClass(this.editorOption.symbolPosition === 'right' ? 'symbol-right' : 'symbol-left');
                            $parent.attr("data-content", this.editorOption.symbolChar);
                        }
                        if (!nts.uk.util.isNullOrEmpty(this.editorOption.defaultValue)
                            && nts.uk.util.isNullOrEmpty(data.value())) {
                            data.value(this.editorOption.defaultValue);
                        }
                    };
                    NumberEditorProcessor.prototype.getDefaultOption = function () {
                        return new nts.uk.ui.option.NumberEditorOption();
                    };
                    NumberEditorProcessor.prototype.getFormatter = function (data) {
                        return new uk.text.NumberFormatter({ option: this.editorOption });
                    };
                    NumberEditorProcessor.prototype.getValidator = function (data) {
                        var name = data.name !== undefined ? ko.unwrap(data.name) : "";
                        name = nts.uk.resource.getControlName(name);
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        this.editorOption['required'] = required;
                        return new validation.NumberValidator(name, constraintName, this.editorOption);
                    };
                    return NumberEditorProcessor;
                }(EditorProcessor));
                var TimeEditorProcessor = (function (_super) {
                    __extends(TimeEditorProcessor, _super);
                    function TimeEditorProcessor() {
                        _super.apply(this, arguments);
                    }
                    TimeEditorProcessor.prototype.update = function ($input, data) {
                        _super.prototype.update.call(this, $input, data);
                        var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
                        var width = option.width;
                        var parent = $input.parent();
                        var parentTag = parent.parent().prop("tagName").toLowerCase();
                        if (parentTag === "td" || parentTag === "th" || parentTag === "a" || width === "100%") {
                            parent.css({ 'width': '100%' });
                        }
                    };
                    TimeEditorProcessor.prototype.getDefaultOption = function () {
                        return new nts.uk.ui.option.TimeEditorOption();
                    };
                    TimeEditorProcessor.prototype.getFormatter = function (data) {
                        var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
                        var inputFormat = (data.inputFormat !== undefined) ? ko.unwrap(data.inputFormat) : option.inputFormat;
                        return new uk.text.TimeFormatter({ inputFormat: inputFormat });
                    };
                    TimeEditorProcessor.prototype.getValidator = function (data) {
                        var name = data.name !== undefined ? ko.unwrap(data.name) : "";
                        name = nts.uk.resource.getControlName(name);
                        var constraintName = (data.constraint !== undefined) ? ko.unwrap(data.constraint) : "";
                        var option = (data.option !== undefined) ? ko.mapping.toJS(data.option) : this.getDefaultOption();
                        var required = (data.required !== undefined) ? ko.unwrap(data.required) : false;
                        var inputFormat = (data.inputFormat !== undefined) ? ko.unwrap(data.inputFormat) : option.inputFormat;
                        var mode = (data.mode !== undefined) ? ko.unwrap(data.mode) : "";
                        return new validation.TimeValidator(name, constraintName, { required: required, outputFormat: inputFormat, mode: mode });
                    };
                    return TimeEditorProcessor;
                }(EditorProcessor));
                var NtsEditorBindingHandler = (function () {
                    function NtsEditorBindingHandler() {
                    }
                    NtsEditorBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new EditorProcessor().init($(element), valueAccessor());
                    };
                    NtsEditorBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new EditorProcessor().update($(element), valueAccessor());
                    };
                    return NtsEditorBindingHandler;
                }());
                var NtsTextEditorBindingHandler = (function (_super) {
                    __extends(NtsTextEditorBindingHandler, _super);
                    function NtsTextEditorBindingHandler() {
                        _super.apply(this, arguments);
                    }
                    NtsTextEditorBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new TextEditorProcessor().init($(element), valueAccessor());
                    };
                    NtsTextEditorBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new TextEditorProcessor().update($(element), valueAccessor());
                    };
                    return NtsTextEditorBindingHandler;
                }(NtsEditorBindingHandler));
                var NtsNumberEditorBindingHandler = (function () {
                    function NtsNumberEditorBindingHandler() {
                    }
                    NtsNumberEditorBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new NumberEditorProcessor().init($(element), valueAccessor());
                    };
                    NtsNumberEditorBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new NumberEditorProcessor().update($(element), valueAccessor());
                    };
                    return NtsNumberEditorBindingHandler;
                }());
                var NtsTimeEditorBindingHandler = (function () {
                    function NtsTimeEditorBindingHandler() {
                    }
                    NtsTimeEditorBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new TimeEditorProcessor().init($(element), valueAccessor());
                    };
                    NtsTimeEditorBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new TimeEditorProcessor().update($(element), valueAccessor());
                    };
                    return NtsTimeEditorBindingHandler;
                }());
                var NtsMultilineEditorBindingHandler = (function (_super) {
                    __extends(NtsMultilineEditorBindingHandler, _super);
                    function NtsMultilineEditorBindingHandler() {
                        _super.apply(this, arguments);
                    }
                    NtsMultilineEditorBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new MultilineEditorProcessor().init($(element), valueAccessor());
                    };
                    NtsMultilineEditorBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        new MultilineEditorProcessor().update($(element), valueAccessor());
                    };
                    return NtsMultilineEditorBindingHandler;
                }(NtsEditorBindingHandler));
                ko.bindingHandlers['ntsTextEditor'] = new NtsTextEditorBindingHandler();
                ko.bindingHandlers['ntsNumberEditor'] = new NtsNumberEditorBindingHandler();
                ko.bindingHandlers['ntsTimeEditor'] = new NtsTimeEditorBindingHandler();
                ko.bindingHandlers['ntsMultilineEditor'] = new NtsMultilineEditorBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsFormLabelBindingHandler = (function () {
                    function NtsFormLabelBindingHandler() {
                    }
                    NtsFormLabelBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var primitiveValue = ko.unwrap(data.constraint);
                        var isRequired = ko.unwrap(data.required) === true;
                        var isInline = ko.unwrap(data.inline) === true;
                        var isEnable = ko.unwrap(data.enable) !== false;
                        var $formLabel = $(element).addClass('form-label');
                        $('<label/>').html($formLabel.html()).appendTo($formLabel.empty());
                        if (!isEnable) {
                            $formLabel.addClass('disabled');
                        }
                        else {
                            $formLabel.removeClass('disabled');
                        }
                        if (isRequired) {
                            $formLabel.addClass('required');
                        }
                        if (primitiveValue !== undefined) {
                            $formLabel.addClass(isInline ? 'inline' : 'broken');
                            var constraintText = NtsFormLabelBindingHandler.buildConstraintText(primitiveValue);
                            $('<i/>').text(constraintText).appendTo($formLabel);
                        }
                    };
                    NtsFormLabelBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                    };
                    NtsFormLabelBindingHandler.buildConstraintText = function (primitiveValues) {
                        if (!Array.isArray(primitiveValues))
                            primitiveValues = [primitiveValues];
                        var constraintText = "";
                        _.forEach(primitiveValues, function (primitiveValue) {
                            var constraint = __viewContext.primitiveValueConstraints[primitiveValue];
                            switch (constraint.valueType) {
                                case 'String':
                                    constraintText += (constraintText.length > 0) ? "/" : "";
                                    constraintText += uk.text.getCharType(primitiveValue).buildConstraintText(constraint.maxLength);
                                    break;
                                case 'Decimal':
                                    constraintText += (constraintText.length > 0) ? "/" : "";
                                    constraintText += constraint.min + "～" + constraint.max;
                                    break;
                                case 'Integer':
                                    constraintText += (constraintText.length > 0) ? "/" : "";
                                    constraintText += constraint.min + "～" + constraint.max;
                                    break;
                                default:
                                    constraintText += 'ERROR';
                                    break;
                            }
                        });
                        return constraintText;
                    };
                    return NtsFormLabelBindingHandler;
                }());
                ko.bindingHandlers['ntsFormLabel'] = new NtsFormLabelBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_5) {
            var koExtentions;
            (function (koExtentions) {
                var NtsGridListBindingHandler = (function () {
                    function NtsGridListBindingHandler() {
                    }
                    NtsGridListBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var HEADER_HEIGHT = 27;
                        var $grid = $(element);
                        var gridId = $grid.attr('id');
                        if (nts.uk.util.isNullOrUndefined(gridId)) {
                            throw new Error('the element NtsGridList must have id attribute.');
                        }
                        var data = valueAccessor();
                        var optionsValue = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
                        var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
                        var deleteOptions = ko.unwrap(data.deleteOptions);
                        var observableColumns = ko.unwrap(data.columns);
                        var showNumbering = ko.unwrap(data.showNumbering) === true ? true : false;
                        var enable = ko.unwrap(data.enable);
                        var value = ko.unwrap(data.value);
                        $grid.data("init", true);
                        var features = [];
                        features.push({ name: 'Selection', multipleSelection: data.multiple });
                        features.push({ name: 'Sorting', type: 'local' });
                        if (data.multiple || showNumbering) {
                            features.push({ name: 'RowSelectors', enableCheckBoxes: data.multiple, enableRowNumbering: showNumbering });
                        }
                        var gridFeatures = ko.unwrap(data.features);
                        var iggridColumns = _.map(observableColumns, function (c) {
                            c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                            c["dataType"] = 'string';
                            if (c["controlType"] === "switch") {
                                var switchF = _.find(gridFeatures, function (s) {
                                    return s["name"] === "Switch";
                                });
                                if (!uk.util.isNullOrUndefined(switchF)) {
                                    features.push({ name: 'Updating', enableAddRow: false, enableDeleteRow: false, editMode: 'none' });
                                    var switchOptions_1 = ko.unwrap(switchF['options']);
                                    var switchValue_1 = switchF['optionsValue'];
                                    var switchText_1 = switchF['optionsText'];
                                    c["formatter"] = function createButton(val, row) {
                                        var result = $('<div class="ntsControl"/>');
                                        result.attr("data-value", val);
                                        _.forEach(switchOptions_1, function (opt) {
                                            var value = opt[switchValue_1];
                                            var text = opt[switchText_1];
                                            var btn = $('<button class="nts-switch-button" tabindex="-1"/>').text(text);
                                            btn.attr('data-value', value);
                                            if (val == value) {
                                                btn.addClass('selected');
                                            }
                                            btn.appendTo(result);
                                        });
                                        return result[0].outerHTML;
                                    };
                                    $grid.on("click", ".nts-switch-button", function (evt, ui) {
                                        var $element = $(this);
                                        var selectedValue = $element.attr('data-value');
                                        var $tr = $element.closest("tr");
                                        $grid.ntsGridListFeature('switch', 'setValue', $tr.attr("data-id"), c["key"], selectedValue);
                                    });
                                }
                            }
                            return c;
                        });
                        $grid.igGrid({
                            width: data.width,
                            height: (data.height) + "px",
                            primaryKey: optionsValue,
                            columns: iggridColumns,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            features: features,
                            tabIndex: -1
                        });
                        if (!uk.util.isNullOrUndefined(deleteOptions) && !uk.util.isNullOrUndefined(deleteOptions.deleteField)
                            && deleteOptions.visible === true) {
                            var sources = (data.dataSource !== undefined ? data.dataSource : data.options);
                            $grid.ntsGridList("setupDeleteButton", {
                                deleteField: deleteOptions.deleteField,
                                sourceTarget: sources
                            });
                        }
                        $grid.ntsGridList('setupSelecting');
                        if (data.multiple) {
                            $grid.bind('iggridrowselectorscheckboxstatechanging', function (evt, uiX) {
                                if ($grid.data("enable") === false) {
                                    return false;
                                }
                            });
                        }
                        $grid.bind('iggridselectionrowselectionchanging', function (evt, uiX) {
                            if ($grid.data("enable") === false) {
                                return false;
                            }
                        });
                        $grid.bind('selectionchanged', function () {
                            $grid.data("ui-changed", true);
                            if (data.multiple) {
                                var selected = $grid.ntsGridList('getSelected');
                                if (selected) {
                                    data.value(_.map(selected, function (s) { return s.id; }));
                                }
                                else {
                                    data.value([]);
                                }
                            }
                            else {
                                var selected = $grid.ntsGridList('getSelected');
                                if (selected) {
                                    data.value(selected.id);
                                }
                                else {
                                    data.value('');
                                }
                            }
                        });
                        $grid.setupSearchScroll("igGrid", true);
                    };
                    NtsGridListBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var $grid = $(element);
                        var data = valueAccessor();
                        var enable = ko.unwrap(data.enable);
                        var optionsValue = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
                        var currentSource = $grid.igGrid('option', 'dataSource');
                        var sources = (data.dataSource !== undefined ? data.dataSource() : data.options());
                        if ($grid.data("enable") !== enable) {
                            if (!enable) {
                                $grid.ntsGridList('unsetupSelecting');
                                $grid.addClass("disabled");
                            }
                            else {
                                $grid.ntsGridList('setupSelecting');
                                $grid.removeClass("disabled");
                            }
                        }
                        $grid.data("enable", enable);
                        if (!($grid.attr("filtered") === true || $grid.attr("filtered") === "true") && $grid.data("ui-changed") !== true) {
                            var currentSources = sources.slice();
                            var observableColumns = _.filter(ko.unwrap(data.columns), function (c) {
                                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                                return c["isDateColumn"] !== undefined && c["isDateColumn"] !== null && c["isDateColumn"] === true;
                            });
                            if (!nts.uk.util.isNullOrEmpty(observableColumns)) {
                                _.forEach(currentSources, function (s) {
                                    _.forEach(observableColumns, function (c) {
                                        var key = c["key"] === undefined ? c["prop"] : c["key"];
                                        s[key] = moment(s[key]).format(c["format"]);
                                    });
                                });
                            }
                            $grid.igGrid('option', 'dataSource', currentSources);
                            $grid.igGrid("dataBind");
                        }
                        var currentSelectedItems = $grid.ntsGridList('getSelected');
                        var isEqual = _.isEqualWith(currentSelectedItems, data.value(), function (current, newVal) {
                            if ((current === undefined && newVal === undefined) || (current !== undefined && current.id === newVal)) {
                                return true;
                            }
                        });
                        if (!isEqual) {
                            $grid.ntsGridList('setSelected', data.value());
                        }
                        $grid.data("ui-changed", false);
                        $grid.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height).attr("tabindex", "0");
                    };
                    return NtsGridListBindingHandler;
                }());
                ko.bindingHandlers['ntsGridList'] = new NtsGridListBindingHandler();
            })(koExtentions = ui_5.koExtentions || (ui_5.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var ListBoxBindingHandler = (function () {
                    function ListBoxBindingHandler() {
                    }
                    ListBoxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = ko.unwrap(data.options);
                        var optionValue = ko.unwrap(data.primaryKey === undefined ? data.optionsValue : data.primaryKey);
                        var optionText = ko.unwrap(data.primaryText === undefined ? data.optionsText : data.primaryText);
                        var selectedValue = ko.unwrap(data.value);
                        var isMultiSelect = ko.unwrap(data.multiple);
                        var enable = ko.unwrap(data.enable);
                        var columns = data.columns;
                        var $element = $(element);
                        var elementId = $element.addClass("listbox-wrapper").attr("id");
                        $element.attr("tabindex", "0");
                        var gridId = elementId;
                        if (nts.uk.util.isNullOrUndefined(gridId)) {
                            gridId = nts.uk.util.randomId();
                        }
                        else {
                            gridId += "_grid";
                        }
                        $element.append("<table id='" + gridId + "' class='ntsListBox ntsControl'/>");
                        var container = $element.find("#" + gridId);
                        container.data("options", options.slice());
                        container.data("init", true);
                        container.data("enable", enable);
                        var changeEvent = new CustomEvent("selectionChange", {
                            detail: {},
                        });
                        container.data("selectionChange", changeEvent);
                        var features = [];
                        features.push({ name: 'Selection', multipleSelection: isMultiSelect });
                        var maxWidthCharacter = 15;
                        var gridFeatures = ko.unwrap(data.features);
                        var width = 0;
                        var iggridColumns = [];
                        if (nts.uk.util.isNullOrUndefined(columns)) {
                            iggridColumns.push({ "key": optionValue, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column', 'hidden': true });
                            iggridColumns.push({ "key": optionText, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column' });
                            width += 10 * maxWidthCharacter + 20;
                            container.data("fullValue", true);
                        }
                        else {
                            var isHaveKey_1 = false;
                            iggridColumns = _.map(columns, function (c) {
                                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                                c["width"] = c["length"] * maxWidthCharacter + 20;
                                c["headerText"] = '';
                                c["columnCssClass"] = 'nts-column';
                                width += c["length"] * maxWidthCharacter + 20;
                                if (optionValue === c["key"]) {
                                    isHaveKey_1 = true;
                                }
                                return c;
                            });
                            if (!isHaveKey_1) {
                                iggridColumns.push({ "key": optionValue, "width": 10 * maxWidthCharacter + 20, "headerText": '', "columnCssClass": 'nts-column', 'hidden': true });
                            }
                        }
                        var gridHeaderHeight = 24;
                        container.igGrid({
                            width: width + "px",
                            height: (data.rows * 28 + gridHeaderHeight) + "px",
                            primaryKey: optionValue,
                            columns: iggridColumns,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            features: features,
                            tabIndex: -1
                        });
                        container.ntsGridList('setupSelecting');
                        container.bind('iggridselectionrowselectionchanging', function (evt, uiX) {
                            if (container.data("enable") === false) {
                                return false;
                            }
                            var itemSelected = uiX.row.id;
                            var dataSource = container.igGrid('option', "dataSource");
                            if (container.data("fullValue")) {
                                itemSelected = _.find(dataSource, function (d) {
                                    return d[optionValue].toString() === itemSelected.toString();
                                });
                            }
                            var changingEvent = new CustomEvent("selectionChanging", {
                                detail: itemSelected,
                                bubbles: true,
                                cancelable: false,
                            });
                            container.data("chaninged", true);
                            document.getElementById(elementId).dispatchEvent(changingEvent);
                        });
                        container.bind('selectionchanged', function () {
                            var itemSelected;
                            if (container.igGridSelection('option', 'multipleSelection')) {
                                var selected = container.ntsGridList('getSelected');
                                if (selected) {
                                    itemSelected = _.map(selected, function (s) { return s.id; });
                                }
                                else {
                                    itemSelected = [];
                                }
                            }
                            else {
                                var selected = container.ntsGridList('getSelected');
                                if (selected) {
                                    itemSelected = selected.id;
                                }
                                else {
                                    itemSelected = ('');
                                }
                            }
                            container.data("selected", itemSelected);
                            var isMultiOld = container.igGridSelection('option', 'multipleSelection');
                            if (container.data("fullValue")) {
                                var dataSource = container.igGrid('option', "dataSource");
                                if (isMultiOld) {
                                    itemSelected = _.filter(dataSource, function (d) {
                                        itemSelected.indexOf(d[optionValue].toString()) >= 0;
                                    });
                                }
                                else {
                                    itemSelected = _.find(dataSource, function (d) {
                                        return d[optionValue].toString() === itemSelected.toString();
                                    });
                                }
                            }
                            if (container.data("chaninged") !== true) {
                                var changingEvent = new CustomEvent("selectionChanging", {
                                    detail: itemSelected,
                                    bubbles: true,
                                    cancelable: false,
                                });
                                document.getElementById(container.attr('id')).dispatchEvent(changingEvent);
                            }
                            container.data("chaninged", false);
                            container.data("ui-changed", true);
                            if (!_.isEqual(itemSelected, data.value())) {
                                data.value(itemSelected);
                            }
                        });
                        container.setupSearchScroll("igGrid", true);
                        container.data("multiple", isMultiSelect);
                        $("#" + gridId + "_container").find("#" + gridId + "_headers").closest("tr").hide();
                        $("#" + gridId + "_container").height($("#" + gridId + "_container").height() - gridHeaderHeight);
                    };
                    ListBoxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = ko.unwrap(data.options);
                        var optionValue = ko.unwrap(data.primaryKey === undefined ? data.optionsValue : data.primaryKey);
                        var optionText = ko.unwrap(data.primaryText === undefined ? data.optionsText : data.primaryText);
                        var selectedValue = ko.unwrap(data.value);
                        var isMultiSelect = ko.unwrap(data.multiple);
                        var enable = ko.unwrap(data.enable);
                        var columns = data.columns;
                        var rows = data.rows;
                        var container = $(element).find(".ntsListBox");
                        if (container.data("enable") !== enable) {
                            if (!enable) {
                                container.ntsGridList('unsetupSelecting');
                                container.addClass("disabled");
                            }
                            else {
                                container.ntsGridList('setupSelecting');
                                container.removeClass("disabled");
                            }
                        }
                        container.data("enable", enable);
                        if (!((container.attr("filtered") === true || container.attr("filtered") === "true") || container.data("ui-changed") === true)) {
                            var currentSources = options.slice();
                            var observableColumns = _.filter(ko.unwrap(data.columns), function (c) {
                                c["key"] = c["key"] === undefined ? c["prop"] : c["key"];
                                return c["isDateColumn"] !== undefined && c["isDateColumn"] !== null && c["isDateColumn"] === true;
                            });
                            _.forEach(currentSources, function (s) {
                                _.forEach(observableColumns, function (c) {
                                    var key = c["key"] === undefined ? c["prop"] : c["key"];
                                    s[key] = moment(s[key]).format(c["format"]);
                                });
                            });
                            container.igGrid('option', 'dataSource', currentSources);
                            container.igGrid("dataBind");
                        }
                        var isMultiOld = container.igGridSelection('option', 'multipleSelection');
                        if (isMultiOld !== isMultiSelect) {
                            container.igGridSelection('option', 'multipleSelection', isMultiSelect);
                            if (isMultiOld && !nts.uk.util.isNullOrUndefined(data.value()) && data.value().length > 0) {
                                data.value(data.value()[0]);
                            }
                            else if (!isMultiOld && !nts.uk.util.isNullOrUndefined(data.value())) {
                                data.value([data.value()]);
                            }
                            var dataValue = data.value();
                            if (container.data("fullValue")) {
                                if (isMultiOld) {
                                    dataValue = _.map(dataValue, optionValue);
                                }
                                else {
                                    dataValue = dataValue[optionValue];
                                }
                            }
                            container.ntsGridList('setSelected', dataValue);
                        }
                        else {
                            var dataValue = data.value();
                            if (container.data("fullValue")) {
                                if (isMultiOld) {
                                    dataValue = _.map(dataValue, optionValue);
                                }
                                else {
                                    dataValue = dataValue[optionValue];
                                }
                            }
                            var currentSelectedItems = container.ntsGridList('getSelected');
                            if (isMultiOld) {
                                if (currentSelectedItems) {
                                    currentSelectedItems = _.map(currentSelectedItems, function (s) { return s["id"]; });
                                }
                                else {
                                    currentSelectedItems = [];
                                }
                                if (dataValue) {
                                    dataValue = _.map(dataValue, function (s) { return s.toString(); });
                                }
                            }
                            else {
                                if (currentSelectedItems) {
                                    currentSelectedItems = currentSelectedItems.id;
                                }
                                else {
                                    currentSelectedItems = ('');
                                }
                                if (dataValue) {
                                    dataValue = dataValue.toString();
                                }
                            }
                            var isEqual = _.isEqual(currentSelectedItems, dataValue);
                            if (!isEqual) {
                                container.ntsGridList('setSelected', dataValue);
                            }
                        }
                        container.data("ui-changed", false);
                        container.closest('.ui-iggrid').addClass('nts-gridlist').height(data.height);
                    };
                    return ListBoxBindingHandler;
                }());
                ko.bindingHandlers['ntsListBox'] = new ListBoxBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_6) {
            var koExtentions;
            (function (koExtentions) {
                var NtsRadioBoxBindingHandler = (function () {
                    function NtsRadioBoxBindingHandler() {
                    }
                    NtsRadioBoxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var optionValue = ko.unwrap(data.optionValue);
                        var optionText = ko.unwrap(data.optionText);
                        var dataName = ko.unwrap(data.name);
                        var option = ko.unwrap(data.option);
                        var group = ko.unwrap(data.group);
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var selectedValue = ko.unwrap(data.checked);
                        var container = $(element);
                        container.addClass("ntsControl radio-wrapper");
                        container.data("enable", enable);
                        if (nts.uk.util.isNullOrUndefined(container.attr("tabindex"))) {
                            container.attr("tabindex", "0");
                        }
                        container.keydown(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (code === 32) {
                                evt.preventDefault();
                            }
                        });
                        container.keyup(function (evt, ui) {
                            if (container.data("enable") !== false) {
                                var code = evt.which || evt.keyCode;
                                if (code === 32) {
                                    var checkitem = container.find("input[type='radio']");
                                    if (!container.find("input[type='radio']").is(":checked")) {
                                        checkitem.prop("checked", true);
                                        data.checked(true);
                                    }
                                    else {
                                        checkitem.prop("checked", false);
                                        data.checked(false);
                                    }
                                    container.focus();
                                }
                            }
                        });
                        var radioBoxLabel = drawRadio(data.checked, option, dataName, optionValue, enable, optionText, true);
                        radioBoxLabel.appendTo(container);
                        var radio = container.find("input[type='radio']");
                        radio.attr("name", group).bind('selectionchanged', function () {
                            data.checked(radio.is(":checked"));
                        });
                        new nts.uk.util.value.DefaultValue().onReset(container, data.value);
                    };
                    NtsRadioBoxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var option = data.option === undefined ? [] : ko.unwrap(data.option);
                        var optionValue = ko.unwrap(data.optionValue);
                        var optionText = ko.unwrap(data.optionText);
                        var selectedValue = data.checked;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.data("enable", enable);
                        container.find(".label").text(nts.uk.util.isNullOrUndefined(option) ? optionText : option[optionText]);
                        if (selectedValue() === true) {
                            container.find("input[type='radio']").prop("checked", true);
                        }
                        else {
                            container.find("input[type='radio']").prop("checked", false);
                        }
                        if (enable === true) {
                            container.find("input[type='radio']").removeAttr("disabled");
                        }
                        else if (enable === false) {
                            container.find("input[type='radio']").attr("disabled", "disabled");
                            new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
                        }
                    };
                    return NtsRadioBoxBindingHandler;
                }());
                var NtsRadioBoxGroupBindingHandler = (function () {
                    function NtsRadioBoxGroupBindingHandler() {
                    }
                    NtsRadioBoxGroupBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var optionValue = ko.unwrap(data.optionsValue);
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.addClass("ntsControl radio-wrapper");
                        container.data("enable", enable);
                        if (nts.uk.util.isNullOrUndefined(container.attr("tabindex"))) {
                            container.attr("tabindex", "0");
                        }
                        container.keydown(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (code === 32) {
                                evt.preventDefault();
                            }
                        });
                        container.keyup(function (evt, ui) {
                            if (container.data("enable") !== false) {
                                var code = evt.which || evt.keyCode;
                                var checkitem = void 0;
                                if (code === 32) {
                                    checkitem = $(_.find(container.find("input[type='radio']"), function (radio, idx) {
                                        return $(radio).attr("disabled") !== "disabled";
                                    }));
                                }
                                else if (code === 37 || code === 38) {
                                    var inputList = _.filter(container.find("input[type='radio']"), function (radio, idx) {
                                        return $(radio).attr("disabled") !== "disabled";
                                    });
                                    var currentSelect = _.findIndex(inputList, function (item) {
                                        return $(item).is(":checked");
                                    });
                                    checkitem = $(inputList[currentSelect - 1]);
                                }
                                else if (code === 39 || code === 40) {
                                    var inputList = _.filter(container.find("input[type='radio']"), function (radio, idx) {
                                        return $(radio).attr("disabled") !== "disabled";
                                    });
                                    var currentSelect = _.findIndex(inputList, function (item) {
                                        return $(item).is(":checked");
                                    });
                                    checkitem = $(inputList[currentSelect + 1]);
                                }
                                if (checkitem !== undefined && checkitem.length > 0) {
                                    checkitem.prop("checked", true);
                                    data.value(optionValue === undefined ? checkitem.data("option") : checkitem.data("option")[optionValue]);
                                }
                                container.focus();
                            }
                        });
                        new nts.uk.util.value.DefaultValue().onReset(container, data.value);
                    };
                    NtsRadioBoxGroupBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = data.options === undefined ? [] : JSON.parse(ko.toJSON(data.options));
                        var optionValue = ko.unwrap(data.optionsValue);
                        var optionText = ko.unwrap(data.optionsText);
                        var selectedValue = data.value;
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.data("enable", enable);
                        if (!_.isEqual(container.data("options"), options)) {
                            var radioName = uk.util.randomId();
                            container.empty();
                            _.forEach(options, function (option) {
                                var radioBoxLabel = drawRadio(selectedValue, option, radioName, optionValue, option["enable"], optionText, false);
                                radioBoxLabel.appendTo(container);
                            });
                            container.data("options", _.cloneDeep(options));
                        }
                        var checkedRadio = _.find(container.find("input[type='radio']"), function (item) {
                            return _.isEqual(JSON.parse(ko.toJSON(selectedValue())), $(item).data("value"));
                        });
                        if (checkedRadio !== undefined)
                            $(checkedRadio).prop("checked", true);
                        if (enable === true) {
                            _.forEach(container.find("input[type='radio']"), function (radio) {
                                var dataOpion = $(radio).data("option");
                                if (dataOpion["enable"] !== false) {
                                    $(radio).removeAttr("disabled");
                                }
                            });
                        }
                        else if (enable === false) {
                            container.find("input[type='radio']").attr("disabled", "disabled");
                            new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
                        }
                    };
                    return NtsRadioBoxGroupBindingHandler;
                }());
                function getOptionValue(item, optionValue) {
                    if (nts.uk.util.isNullOrUndefined(item)) {
                        return optionValue;
                    }
                    return (optionValue === undefined) ? item : item[optionValue];
                }
                ;
                function drawRadio(selectedValue, option, radioName, optionValue, disableOption, optionText, booleanValue) {
                    var radioBoxLabel = $("<label class='ntsRadioBox'></label>");
                    var radioBox = $('<input type="radio">').data("option", option).attr("name", radioName).data("value", getOptionValue(option, optionValue)).on("change", function () {
                        var self = $(this);
                        if (self.is(":checked") && !booleanValue) {
                            selectedValue(self.data("value"));
                        }
                        else if (booleanValue) {
                            var name_1 = self.attr("name");
                            if (nts.uk.util.isNullOrUndefined(name_1)) {
                                selectedValue(self.is(":checked"));
                            }
                            else {
                                var selector = 'input[name=' + name_1 + ']';
                                $(selector).each(function (idx, e) {
                                    $(e).triggerHandler('selectionchanged');
                                });
                            }
                        }
                    });
                    if (!nts.uk.util.isNullOrUndefined(disableOption) && (disableOption === false)) {
                        radioBox.attr("disabled", "disabled");
                    }
                    radioBox.appendTo(radioBoxLabel);
                    var box = $("<span class='box'></span>").appendTo(radioBoxLabel);
                    var label = $("<span class='label'></span>").text(nts.uk.util.isNullOrUndefined(option) ? optionText : option[optionText]).appendTo(radioBoxLabel);
                    return radioBoxLabel;
                }
                ko.bindingHandlers['ntsRadioButton'] = new NtsRadioBoxBindingHandler();
                ko.bindingHandlers['ntsRadioBoxGroup'] = new NtsRadioBoxGroupBindingHandler();
            })(koExtentions = ui_6.koExtentions || (ui_6.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_7) {
            var koExtentions;
            (function (koExtentions) {
                var SearchBox = (function () {
                    function SearchBox(source, searchField, childField) {
                        this.childField = childField;
                        this.source = nts.uk.util.isNullOrEmpty(source) ? [] : this.cloneDeep(source);
                        this.searchField = searchField;
                    }
                    SearchBox.prototype.search = function (searchKey) {
                        var self = this;
                        if (nts.uk.util.isNullOrEmpty(this.source)) {
                            return [];
                        }
                        var flatArr = nts.uk.util.flatArray(this.source, this.childField);
                        var filtered = _.filter(flatArr, function (item) {
                            return _.find(self.searchField, function (x) {
                                if (x !== undefined && x !== null) {
                                    var val = item[x].toString();
                                    return val.indexOf(searchKey.toString()) >= 0;
                                }
                                return false;
                            }) !== undefined;
                        });
                        return filtered;
                    };
                    SearchBox.prototype.setDataSource = function (source) {
                        this.source = nts.uk.util.isNullOrEmpty(source) ? [] : this.cloneDeep(source);
                    };
                    SearchBox.prototype.getDataSource = function () {
                        return this.cloneDeep(this.source);
                    };
                    SearchBox.prototype.cloneDeep = function (source) {
                        var self = this;
                        return self.cloneDeepX(source);
                    };
                    SearchBox.prototype.cloneDeepX = function (source) {
                        var self = this;
                        return _.cloneDeep(source);
                    };
                    return SearchBox;
                }());
                var SearchResult = (function () {
                    function SearchResult() {
                        this.options = [];
                        this.selectItems = [];
                    }
                    return SearchResult;
                }());
                var SearchPub = (function () {
                    function SearchPub(key, mode, source, searchField, childField) {
                        this.seachBox = new SearchBox(source, searchField, childField);
                        ;
                        this.mode = nts.uk.util.isNullOrEmpty(mode) ? "highlight" : mode;
                        this.key = key;
                    }
                    SearchPub.prototype.search = function (searchKey, selectedItems) {
                        var result = new SearchResult();
                        var filted = this.seachBox.search(searchKey);
                        if (!nts.uk.util.isNullOrEmpty(filted)) {
                            var key_1 = this.key;
                            if (this.mode === "highlight") {
                                result.options = this.seachBox.getDataSource();
                                var index = 0;
                                if (!nts.uk.util.isNullOrEmpty(selectedItems)) {
                                    var firstItemValue_1 = $.isArray(selectedItems)
                                        ? selectedItems[0]["id"].toString() : selectedItems["id"].toString();
                                    index = _.findIndex(filted, function (item) {
                                        return item[key_1].toString() === firstItemValue_1;
                                    });
                                    if (!nts.uk.util.isNullOrUndefined(index)) {
                                        index++;
                                    }
                                }
                                if (index >= 0) {
                                    result.selectItems = [filted[index >= filted.length ? 0 : index]];
                                }
                            }
                            else if (this.mode === "filter") {
                                result.options = filted;
                                var selectItem = _.filter(filted, function (itemFilterd) {
                                    return _.find(selectedItems, function (item) {
                                        var itemVal = itemFilterd[key_1];
                                        return itemVal === item["id"];
                                    }) !== undefined;
                                });
                                result.selectItems = selectItem;
                            }
                        }
                        return result;
                    };
                    SearchPub.prototype.setDataSource = function (source) {
                        this.seachBox.setDataSource(source);
                    };
                    SearchPub.prototype.getDataSource = function () {
                        return this.seachBox.getDataSource();
                    };
                    return SearchPub;
                }());
                koExtentions.SearchPub = SearchPub;
                var NtsSearchBoxBindingHandler = (function () {
                    function NtsSearchBoxBindingHandler() {
                    }
                    NtsSearchBoxBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var searchBox = $(element);
                        var data = ko.unwrap(valueAccessor());
                        var fields = ko.unwrap(data.fields);
                        var searchText = (data.searchText !== undefined) ? ko.unwrap(data.searchText) : "検索";
                        var placeHolder = (data.placeHolder !== undefined) ? ko.unwrap(data.placeHolder) : "コード・名称で検索・・・";
                        var selected = data.selected;
                        var searchMode = (data.searchMode !== undefined) ? ko.unwrap(data.searchMode) : "highlight";
                        var selectedKey = null;
                        if (data.selectedKey) {
                            selectedKey = ko.unwrap(data.selectedKey);
                        }
                        var dataSource = ko.unwrap(data.items);
                        var childField = null;
                        if (data.childField) {
                            childField = ko.unwrap(data.childField);
                        }
                        var component;
                        var targetMode = data.mode;
                        if (targetMode === "listbox") {
                            component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");
                            targetMode = "igGrid";
                        }
                        else {
                            component = $("#" + ko.unwrap(data.comId));
                        }
                        var $container = $(element);
                        $container.append("<span class='nts-editor-wrapped ntsControl'><input class='ntsSearchBox nts-editor' type='text' /></span>");
                        $container.append("<button class='search-btn caret-bottom'>" + searchText + "</button>");
                        var $button = $container.find("button.search-btn");
                        var $input = $container.find("input.ntsSearchBox");
                        var buttonWidth = $button.outerWidth(true);
                        if (searchMode === "filter") {
                            $container.append("<button class='clear-btn'>解除</button>");
                            var $clearButton = $container.find("button.clear-btn");
                            buttonWidth += $clearButton.outerWidth(true);
                            $clearButton.click(function (evt, ui) {
                                if (component.length === 0) {
                                    component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");
                                }
                                var srh = $container.data("searchObject");
                                component.igGrid("option", "dataSource", srh.seachBox.getDataSource());
                                component.igGrid("dataBind");
                                $container.data("searchKey", null);
                                component.attr("filtered", false);
                                _.defer(function () {
                                    component.trigger("selectChange");
                                });
                            });
                        }
                        $input.attr("placeholder", placeHolder);
                        $input.attr("data-name", "検索テキストボックス");
                        $input.outerWidth($container.outerWidth(true) - buttonWidth);
                        var primaryKey = ko.unwrap(data.targetKey);
                        var searchObject = new SearchPub(primaryKey, searchMode, dataSource, fields, childField);
                        $container.data("searchObject", searchObject);
                        var search = function (searchKey) {
                            if (targetMode) {
                                var selectedItems = void 0;
                                if (targetMode == 'igGrid') {
                                    if (component.length === 0) {
                                        component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");
                                    }
                                    selectedItems = component.ntsGridList("getSelected");
                                }
                                else if (targetMode == 'igTree') {
                                    selectedItems = component.ntsTreeView("getSelected");
                                }
                                var srh = $container.data("searchObject");
                                var result = srh.search(searchKey, selectedItems);
                                if (nts.uk.util.isNullOrEmpty(result.options) && searchMode === "highlight") {
                                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOHIT"));
                                    return;
                                }
                                var isMulti = targetMode === 'igGrid' ? component.igGridSelection('option', 'multipleSelection')
                                    : component.igTreeGridSelection('option', 'multipleSelection');
                                var selectedProperties = _.map(result.selectItems, primaryKey);
                                var selectedValue = void 0;
                                if (selectedKey !== null) {
                                    selectedValue = isMulti ? _.map(result.selectItems, selectedKey) :
                                        result.selectItems.length > 0 ? result.selectItems[0][selectedKey] : undefined;
                                }
                                else {
                                    selectedValue = isMulti ? [result.selectItems] :
                                        result.selectItems.length > 0 ? result.selectItems[0] : undefined;
                                }
                                if (targetMode === 'igGrid') {
                                    if (searchMode === "filter") {
                                        $container.data("filteredSrouce", result.options);
                                        component.attr("filtered", true);
                                        selected(selectedValue);
                                        selected.valueHasMutated();
                                    }
                                    else {
                                        selected(selectedValue);
                                    }
                                    component.ntsGridList("setSelected", selectedProperties);
                                }
                                else if (targetMode == 'igTree') {
                                    component.ntsTreeView("setSelected", selectedProperties);
                                    selected(selectedValue);
                                }
                                _.defer(function () {
                                    component.trigger("selectChange");
                                });
                                $container.data("searchKey", searchKey);
                            }
                        };
                        var nextSearch = function () {
                            var searchKey = $input.val();
                            if (nts.uk.util.isNullOrEmpty(searchKey)) {
                                nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                                return;
                            }
                            search(searchKey);
                        };
                        $input.keydown(function (event) {
                            if (event.which == 13) {
                                event.preventDefault();
                                nextSearch();
                                _.defer(function () {
                                    $input.focus();
                                });
                            }
                        });
                        $button.click(function () {
                            nextSearch();
                        });
                    };
                    NtsSearchBoxBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var $searchBox = $(element);
                        var data = valueAccessor();
                        var arr = ko.unwrap(data.items);
                        var searchMode = ko.unwrap(data.searchMode);
                        var primaryKey = ko.unwrap(data.targetKey);
                        var selectedValue = ko.unwrap(data.selected);
                        var targetMode = data.mode;
                        var component;
                        if (targetMode === "listbox") {
                            component = $("#" + ko.unwrap(data.comId)).find(".ntsListBox");
                            targetMode = "igGrid";
                        }
                        else {
                            component = $("#" + ko.unwrap(data.comId));
                        }
                        var srhX = $searchBox.data("searchObject");
                        if (searchMode === "filter" && (component.attr("filtered") === true || component.attr("filtered") === "true")) {
                            var filteds_1 = $searchBox.data("filteredSrouce");
                            if (!nts.uk.util.isNullOrUndefined(filteds_1)) {
                                var source = _.filter(arr, function (item) {
                                    return _.find(filteds_1, function (itemFilterd) {
                                        return itemFilterd[primaryKey] === item[primaryKey];
                                    }) !== undefined || _.find(srhX.getDataSource(), function (oldItem) {
                                        return oldItem[primaryKey] === item[primaryKey];
                                    }) === undefined;
                                });
                                component.igGrid("option", "dataSource", source);
                                component.igGrid("dataBind");
                            }
                        }
                        srhX.setDataSource(arr);
                    };
                    return NtsSearchBoxBindingHandler;
                }());
                ko.bindingHandlers['ntsSearchBox'] = new NtsSearchBoxBindingHandler();
            })(koExtentions = ui_7.koExtentions || (ui_7.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_8) {
            var koExtentions;
            (function (koExtentions) {
                var NtsSwapListBindingHandler = (function () {
                    function NtsSwapListBindingHandler() {
                    }
                    NtsSwapListBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var HEADER_HEIGHT = 27;
                        var CHECKBOX_WIDTH = 70;
                        var SEARCH_AREA_HEIGHT = 45;
                        var BUTTON_SEARCH_WIDTH = 60;
                        var INPUT_SEARCH_PADDING = 65;
                        var $swap = $(element);
                        var elementId = $swap.attr('id');
                        if (nts.uk.util.isNullOrUndefined(elementId)) {
                            throw new Error('the element NtsSwapList must have id attribute.');
                        }
                        var data = valueAccessor();
                        var originalSource = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
                        var totalWidth = ko.unwrap(data.width);
                        var height = ko.unwrap(data.height);
                        var showSearchBox = ko.unwrap(data.showSearchBox);
                        var primaryKey = data.primaryKey !== undefined ? data.primaryKey : data.optionsValue;
                        var columns = data.columns;
                        $swap.wrap("<div class= 'ntsComponent ntsSwapList' id='" + elementId + "_container'/>");
                        if (totalWidth !== undefined) {
                            $swap.parent().width(totalWidth);
                        }
                        $swap.parent().height(height);
                        $swap.addClass("ntsSwapList-container");
                        var gridWidth = _.sumBy(columns(), function (c) {
                            return c.width;
                        });
                        var iggridColumns = _.map(columns(), function (c) {
                            c["key"] = c.key === undefined ? c.prop : c.key;
                            c["dataType"] = 'string';
                            return c;
                        });
                        var gridHeight = (height - 20);
                        var grid1Id = "#" + elementId + "-grid1";
                        var grid2Id = "#" + elementId + "-grid2";
                        if (!uk.util.isNullOrUndefined(showSearchBox) && (showSearchBox.showLeft || showSearchBox.showEright)) {
                            var initSearchArea = function ($SearchArea, targetId, searchMode) {
                                $SearchArea.append("<div class='ntsSearchTextContainer'/>")
                                    .append("<div class='ntsSearchButtonContainer'/>");
                                if (searchMode === "filter") {
                                    $SearchArea.append("<div class='ntsClearButtonContainer'/>");
                                    $SearchArea.find(".ntsClearButtonContainer")
                                        .append("<button id = " + searchAreaId + "-clear-btn" + " class='ntsSearchButton clear-btn'/>");
                                    $SearchArea.find(".clear-btn").text("検索");
                                }
                                $SearchArea.find(".ntsSearchTextContainer")
                                    .append("<input id = " + searchAreaId + "-input" + " class = 'ntsSearchInput ntsSearchBox'/>");
                                $SearchArea.find(".ntsSearchButtonContainer")
                                    .append("<button id = " + searchAreaId + "-btn" + " class='ntsSearchButton search-btn caret-bottom'/>");
                                $SearchArea.find(".ntsSearchInput").attr("placeholder", "コード・名称で検索・・・");
                                $SearchArea.find(".search-btn").text("検索");
                            };
                            var searchAreaId = elementId + "-search-area";
                            $swap.append("<div class = 'ntsSearchArea' id = " + searchAreaId + "/>");
                            var $searchArea = $swap.find(".ntsSearchArea");
                            $searchArea.append("<div class='ntsSwapSearchLeft'/>")
                                .append("<div class='ntsSwapSearchRight'/>");
                            $searchArea.css({ position: "relative" });
                            var searchAreaWidth = gridWidth + CHECKBOX_WIDTH;
                            if (showSearchBox.showLeft) {
                                var $searchLeftContainer = $swap.find(".ntsSwapSearchLeft");
                                $searchLeftContainer.width(searchAreaWidth).css({ position: "absolute", left: 0 });
                                initSearchArea($searchLeftContainer, grid1Id, data.searchMode);
                            }
                            if (showSearchBox.showRight) {
                                var $searchRightContainer = $swap.find(".ntsSwapSearchRight");
                                $searchRightContainer.width(gridWidth + CHECKBOX_WIDTH).css({ position: "absolute", right: 0 });
                                initSearchArea($searchRightContainer, grid2Id, data.searchMode);
                            }
                            $searchArea.find(".ntsSearchBox").width(searchAreaWidth - BUTTON_SEARCH_WIDTH - INPUT_SEARCH_PADDING - (data.searchMode === "filter" ? BUTTON_SEARCH_WIDTH : 0));
                            $searchArea.height(SEARCH_AREA_HEIGHT);
                            gridHeight -= SEARCH_AREA_HEIGHT;
                        }
                        $swap.append("<div class= 'ntsSwapArea ntsGridArea'/>");
                        $swap.find(".ntsGridArea").append("<div class = 'ntsSwapGridArea ntsSwapComponent' id = " + elementId + "-gridArea1" + "/>")
                            .append("<div class = 'ntsMoveDataArea ntsSwapComponent' id = " + elementId + "-move-data" + "/>")
                            .append("<div class = 'ntsSwapGridArea ntsSwapComponent' id = " + elementId + "-gridArea2" + "/>");
                        $swap.find("#" + elementId + "-gridArea1").append("<table class = 'ntsSwapGrid' id = " + elementId + "-grid1" + "/>");
                        $swap.find("#" + elementId + "-gridArea2").append("<table class = 'ntsSwapGrid' id = " + elementId + "-grid2" + "/>");
                        var $grid1 = $swap.find(grid1Id);
                        var $grid2 = $swap.find(grid2Id);
                        var features = [{ name: 'Selection', multipleSelection: true },
                            { name: 'Sorting', type: 'local' },
                            { name: 'RowSelectors', enableCheckBoxes: true, enableRowNumbering: true }];
                        $swap.find(".nstSwapGridArea").width(gridWidth + CHECKBOX_WIDTH);
                        var criterion = _.map(columns(), function (c) { return c.key === undefined ? c.prop : c.key; });
                        var swapParts = new Array();
                        swapParts.push(new GridSwapPart().listControl($grid1)
                            .searchControl($swap.find(".ntsSwapSearchLeft").find(".search-btn"))
                            .clearControl($swap.find(".ntsSwapSearchLeft").find(".clear-btn"))
                            .searchBox($swap.find(".ntsSwapSearchLeft").find(".ntsSearchBox"))
                            .setDataSource(originalSource)
                            .setSearchCriterion(data.searchCriterion || criterion)
                            .setSearchMode(data.searchMode || "highlight")
                            .setColumns(columns())
                            .setPrimaryKey(primaryKey)
                            .setInnerDrop((data.innerDrag && data.innerDrag.left !== undefined) ? data.innerDrag.left : true)
                            .setOuterDrop((data.outerDrag && data.outerDrag.left !== undefined) ? data.outerDrag.left : true)
                            .build());
                        swapParts.push(new GridSwapPart().listControl($grid2)
                            .searchControl($swap.find(".ntsSwapSearchRight").find(".search-btn"))
                            .clearControl($swap.find(".ntsSwapSearchRight").find(".clear-btn"))
                            .searchBox($swap.find(".ntsSwapSearchRight").find(".ntsSearchBox"))
                            .setDataSource(data.value())
                            .setSearchCriterion(data.searchCriterion || criterion)
                            .setSearchMode(data.searchMode || "highlight")
                            .setColumns(columns())
                            .setPrimaryKey(primaryKey)
                            .setInnerDrop((data.innerDrag && data.innerDrag.right !== undefined) ? data.innerDrag.right : true)
                            .setOuterDrop((data.outerDrag && data.outerDrag.right !== undefined) ? data.outerDrag.right : true)
                            .build());
                        this.swapper = new SwapHandler().setModel(new GridSwapList($swap, swapParts));
                        $grid1.igGrid({
                            width: gridWidth + CHECKBOX_WIDTH,
                            height: (gridHeight) + "px",
                            primaryKey: primaryKey,
                            columns: iggridColumns,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            features: features
                        });
                        $grid1.closest('.ui-iggrid')
                            .addClass('nts-gridlist')
                            .height(gridHeight);
                        $grid1.ntsGridList('setupSelecting');
                        $grid2.igGrid({
                            width: gridWidth + CHECKBOX_WIDTH,
                            height: (gridHeight) + "px",
                            primaryKey: primaryKey,
                            columns: iggridColumns,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            features: features
                        });
                        if (data.draggable === true) {
                            this.swapper.enableDragDrop(data.value);
                            if (data.multipleDrag && data.multipleDrag.left === true) {
                                this.swapper.Model.swapParts[0].$listControl.addClass("multiple-drag");
                            }
                            if (data.multipleDrag && data.multipleDrag.right === true) {
                                this.swapper.Model.swapParts[1].$listControl.addClass("multiple-drag");
                            }
                        }
                        $grid2.closest('.ui-iggrid')
                            .addClass('nts-gridlist')
                            .height(gridHeight);
                        $grid2.ntsGridList('setupSelecting');
                        var $moveArea = $swap.find("#" + elementId + "-move-data")
                            .append("<button class = 'move-button move-forward'><i class='icon icon-button-arrow-right'></i></button>")
                            .append("<button class = 'move-button move-back'><i class='icon icon-button-arrow-left'></i></button>");
                        var $moveForward = $moveArea.find(".move-forward");
                        var $moveBack = $moveArea.find(".move-back");
                        var swapper = this.swapper;
                        $moveForward.click(function () {
                            swapper.Model.move(true, data.value);
                        });
                        $moveBack.click(function () {
                            swapper.Model.move(false, data.value);
                        });
                    };
                    NtsSwapListBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var elementId = this.swapper.Model.$container.attr('id');
                        var primaryKey = this.swapper.Model.swapParts[0].primaryKey;
                        if (nts.uk.util.isNullOrUndefined(elementId)) {
                            throw new Error('the element NtsSwapList must have id attribute.');
                        }
                        var $grid1 = this.swapper.Model.swapParts[0].$listControl;
                        var $grid2 = this.swapper.Model.swapParts[1].$listControl;
                        var currentSource = $grid1.igGrid('option', 'dataSource');
                        var currentSelectedList = $grid2.igGrid('option', 'dataSource');
                        var newSources = (data.dataSource !== undefined ? data.dataSource() : data.options());
                        var newSelectedList = data.value();
                        _.remove(newSources, function (item) {
                            return _.find(newSelectedList, function (selected) {
                                return selected[primaryKey] === item[primaryKey];
                            }) !== undefined;
                        });
                        if (!_.isEqual(currentSource, newSources)) {
                            this.swapper.Model.swapParts[0].bindData(newSources.slice());
                            this.swapper.Model.transportBuilder.setFirst(newSources);
                        }
                        if (!_.isEqual(currentSelectedList, newSelectedList)) {
                            this.swapper.Model.swapParts[1].bindData(newSelectedList.slice());
                            this.swapper.Model.transportBuilder.setSecond(newSelectedList);
                        }
                    };
                    NtsSwapListBindingHandler.prototype.makeBindings = function () {
                        var handler = this;
                        return {
                            init: function (element, valueAccessor, allBindings, viewModel, bindingContext) {
                                var newHandler = Object.create(handler);
                                if (handler.init) {
                                    handler.init.call(newHandler, element, valueAccessor, allBindings, viewModel, bindingContext);
                                }
                                if (handler.update) {
                                    ko.computed({
                                        read: handler.update.bind(newHandler, element, valueAccessor, allBindings, viewModel, bindingContext),
                                        disposeWhenNodeIsRemoved: element
                                    });
                                }
                            }
                        };
                    };
                    return NtsSwapListBindingHandler;
                }());
                ko.bindingHandlers['ntsSwapList'] = new NtsSwapListBindingHandler().makeBindings();
                var SwapHandler = (function () {
                    function SwapHandler() {
                    }
                    SwapHandler.prototype.setModel = function (model) {
                        this.model = model;
                        return this;
                    };
                    Object.defineProperty(SwapHandler.prototype, "Model", {
                        get: function () {
                            return this.model;
                        },
                        enumerable: true,
                        configurable: true
                    });
                    SwapHandler.prototype.handle = function (parts, value) {
                        var self = this;
                        var model = this.model;
                        for (var id in parts) {
                            var options = {
                                items: "tbody > tr",
                                containment: this.model.$container,
                                cursor: "move",
                                connectWith: ".ntsSwapGrid",
                                placeholder: "ui-state-highlight",
                                helper: this._createHelper,
                                appendTo: this.model.$container,
                                start: function (evt, ui) {
                                    model.transportBuilder.at(model.sender(ui));
                                },
                                beforeStop: function (evt, ui) {
                                    self._beforeStop.call(this, model, evt, ui);
                                },
                                update: function (evt, ui) {
                                    self._update.call(this, model, evt, ui, value);
                                }
                            };
                            this.model.swapParts[parts[id]].initDraggable(options);
                        }
                    };
                    SwapHandler.prototype._createHelper = function (evt, ui) {
                        var selectedRowElms = $(evt.currentTarget).igGrid("selectedRows");
                        selectedRowElms.sort(function (one, two) {
                            return one.index - two.index;
                        });
                        var $helper;
                        if ($(evt.currentTarget).hasClass("multiple-drag") && selectedRowElms.length > 1) {
                            $helper = $("<div><table><tbody></tbody></table></div>").addClass("select-drag");
                            var rowId = ui.data("row-idx");
                            var selectedItems = selectedRowElms.map(function (elm) { return elm.element; });
                            var height = 0;
                            $.each(selectedItems, function () {
                                $helper.find("tbody").append($(this).clone());
                                height += $(this).outerHeight();
                                if (rowId !== this.data("row-idx"))
                                    $(this).hide();
                            });
                            $helper.height(height);
                            $helper.find("tr").first().children().each(function (idx) {
                                $(this).width(ui.children().eq(idx).width());
                            });
                        }
                        else {
                            $helper = ui.clone();
                            $helper.children().each(function (idx) {
                                $(this).width(ui.children().eq(idx).width());
                            });
                        }
                        return $helper[0];
                    };
                    SwapHandler.prototype._beforeStop = function (model, evt, ui) {
                        var partId = model.transportBuilder.startAt === "first" ? 0 : 1;
                        var destPartId = model.receiver(ui) === "first" ? 0 : 1;
                        model.transportBuilder.toAdjacent(model.neighbor(ui)).target(model.target(ui));
                        if (ui.helper.hasClass("select-drag") === true) {
                            var rowsInHelper = ui.helper.find("tr");
                            var rows = rowsInHelper.toArray();
                            if (model.transportBuilder.startAt === model.receiver(ui)
                                || (model.swapParts[partId].outerDrop === false
                                    && model.transportBuilder.startAt !== model.receiver(ui))) {
                                $(this).sortable("cancel");
                                for (var idx in rows) {
                                    model.swapParts[partId].$listControl.find("tbody").children()
                                        .eq($(rows[idx]).data("row-idx")).show();
                                }
                                return;
                            }
                            else {
                                var standardIdx = ui.placeholder.index();
                                var targetIdx = ui.item.data("row-idx");
                                var rowsBefore = new Array();
                                var rowsAfter = new Array();
                                for (var id in rows) {
                                    if ($(rows[id]).data("row-idx") < targetIdx)
                                        rowsBefore.push(rows[id]);
                                    else if ($(rows[id]).data("row-idx") > targetIdx)
                                        rowsAfter.push(rows[id]);
                                }
                                model.swapParts[destPartId].$listControl.find("tbody").children()
                                    .eq(standardIdx - 1).before(rowsBefore).after(rowsAfter);
                                var sourceRows = model.swapParts[partId].$listControl.find("tbody").children();
                                for (var index = rowsAfter.length - 1; index >= 0; index--) {
                                    if ($(rows[index]).data("row-idx") === targetIdx)
                                        continue;
                                    sourceRows.eq($(rowsAfter[index]).data("row-idx") - 1).remove();
                                }
                                for (var val in rowsBefore) {
                                    sourceRows.eq($(rowsBefore[val]).data("row-idx")).remove();
                                }
                            }
                        }
                        else if ((model.swapParts[partId].innerDrop === false
                            && model.transportBuilder.startAt === model.receiver(ui))
                            || (model.swapParts[partId].outerDrop === false
                                && model.transportBuilder.startAt !== model.receiver(ui))) {
                            $(this).sortable("cancel");
                        }
                    };
                    SwapHandler.prototype._update = function (model, evt, ui, value) {
                        if (ui.item.closest("table").length === 0)
                            return;
                        model.transportBuilder.directTo(model.receiver(ui)).update();
                        if (model.transportBuilder.startAt === model.transportBuilder.direction) {
                            model.transportBuilder.startAt === "first"
                                ? model.swapParts[0].bindData(model.transportBuilder.getFirst())
                                : model.swapParts[1].bindData(model.transportBuilder.getSecond());
                        }
                        else {
                            model.swapParts[0].bindData(model.transportBuilder.getFirst());
                            model.swapParts[1].bindData(model.transportBuilder.getSecond());
                        }
                        value(model.transportBuilder.getSecond());
                        setTimeout(function () { model.dropDone(); }, 0);
                    };
                    SwapHandler.prototype.enableDragDrop = function (value, parts) {
                        parts = parts || [0, 1];
                        this.model.enableDrag(this, value, parts, this.handle);
                    };
                    return SwapHandler;
                }());
                var SwapModel = (function () {
                    function SwapModel($container, swapParts) {
                        this.$container = $container;
                        this.swapParts = swapParts;
                        this.transportBuilder = new ListItemTransporter(this.swapParts[0].dataSource, this.swapParts[1].dataSource)
                            .primary(this.swapParts[0].primaryKey);
                    }
                    return SwapModel;
                }());
                var SearchResult = (function () {
                    function SearchResult(results, indices) {
                        this.data = results;
                        this.indices = indices;
                    }
                    return SearchResult;
                }());
                var SwapPart = (function () {
                    function SwapPart() {
                        this.searchMode = "highlight";
                        this.innerDrop = true;
                        this.outerDrop = true;
                    }
                    SwapPart.prototype.listControl = function ($listControl) {
                        this.$listControl = $listControl;
                        return this;
                    };
                    SwapPart.prototype.searchControl = function ($searchControl) {
                        this.$searchControl = $searchControl;
                        return this;
                    };
                    SwapPart.prototype.clearControl = function ($clearControl) {
                        this.$clearControl = $clearControl;
                        return this;
                    };
                    SwapPart.prototype.searchBox = function ($searchBox) {
                        this.$searchBox = $searchBox;
                        return this;
                    };
                    SwapPart.prototype.setSearchMode = function (searchMode) {
                        this.searchMode = searchMode;
                        return this;
                    };
                    SwapPart.prototype.setSearchCriterion = function (searchCriterion) {
                        this.searchCriterion = searchCriterion;
                        return this;
                    };
                    SwapPart.prototype.setDataSource = function (dataSource) {
                        this.dataSource = dataSource;
                        return this;
                    };
                    SwapPart.prototype.setColumns = function (columns) {
                        this.columns = columns;
                        return this;
                    };
                    SwapPart.prototype.setPrimaryKey = function (primaryKey) {
                        this.primaryKey = primaryKey;
                        return this;
                    };
                    SwapPart.prototype.setInnerDrop = function (innerDrop) {
                        this.innerDrop = innerDrop;
                        return this;
                    };
                    SwapPart.prototype.setOuterDrop = function (outerDrop) {
                        this.outerDrop = outerDrop;
                        return this;
                    };
                    SwapPart.prototype.initDraggable = function (opts) {
                        this.$listControl.sortable(opts).disableSelection();
                    };
                    SwapPart.prototype.resetOriginalDataSource = function () {
                        this.originalDataSource = this.dataSource;
                    };
                    SwapPart.prototype.search = function () {
                        var searchContents = this.$searchBox.val();
                        var orders = new Array();
                        if (nts.uk.util.isNullOrEmpty(searchContents)) {
                            nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                            return null;
                        }
                        var searchCriterion = this.searchCriterion;
                        if (this.originalDataSource === undefined)
                            this.resetOriginalDataSource();
                        var results = _.filter(this.originalDataSource, function (value, index) {
                            var found = false;
                            _.forEach(searchCriterion, function (criteria) {
                                if (value[criteria].toString().indexOf(searchContents) !== -1) {
                                    found = true;
                                    return false;
                                }
                                else
                                    return true;
                            });
                            orders.push(index);
                            return found;
                        });
                        return new SearchResult(results, orders);
                    };
                    SwapPart.prototype.bindData = function (src) {
                        this.bindIn(src);
                        this.dataSource = src;
                    };
                    SwapPart.prototype.bindSearchEvent = function () {
                        var self = this;
                        var proceedSearch = this.proceedSearch;
                        var clearFilter = this.clearFilter;
                        this.$searchControl.click(function (evt, ui) {
                            proceedSearch.apply(self);
                        });
                        this.$clearControl.click(function (evt, ui) {
                            clearFilter.apply(self);
                            ;
                        });
                        this.$searchBox.keydown(function (evt, ui) {
                            if (evt.which === 13) {
                                proceedSearch.apply(self);
                                _.defer(function () {
                                    $input.focus();
                                });
                            }
                        });
                    };
                    SwapPart.prototype.proceedSearch = function () {
                        if (this.searchMode === "filter") {
                            var results = this.search();
                            if (results === null)
                                return;
                            this.bindData(results.data);
                        }
                        else {
                            this.highlightSearch();
                        }
                    };
                    SwapPart.prototype.clearFilter = function () {
                        if (this.searchMode === "filter") {
                            this.bindData(this.originalDataSource);
                        }
                    };
                    SwapPart.prototype.build = function () {
                        this.bindSearchEvent();
                        return this;
                    };
                    return SwapPart;
                }());
                var GridSwapPart = (function (_super) {
                    __extends(GridSwapPart, _super);
                    function GridSwapPart() {
                        _super.apply(this, arguments);
                    }
                    GridSwapPart.prototype.search = function () {
                        return _super.prototype.search.call(this);
                    };
                    GridSwapPart.prototype.highlightSearch = function () {
                        var value = this.$searchBox.val();
                        if (nts.uk.util.isNullOrEmpty(value)) {
                            nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOWORD"));
                            return;
                        }
                        var source = this.dataSource.slice();
                        var selected = this.$listControl.ntsGridList("getSelected");
                        if (selected.length > 0) {
                            var gotoEnd = source.splice(0, selected[0].index + 1);
                            source = source.concat(gotoEnd);
                        }
                        var iggridColumns = _.map(this.columns, function (c) {
                            c["key"] = c.key === undefined ? c.prop : c.key;
                            c["dataType"] = 'string';
                            return c;
                        });
                        var searchedValues = _.find(source, function (val) {
                            return _.find(iggridColumns, function (x) {
                                return x !== undefined && x !== null && val[x["key"]].toString().indexOf(value) >= 0;
                            }) !== undefined;
                        });
                        if (searchedValues === undefined) {
                            nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("FND_E_SEARCH_NOHIT"));
                            return;
                        }
                        this.$listControl.ntsGridList('setSelected', searchedValues !== undefined ? [searchedValues[this.primaryKey]] : []);
                        if (searchedValues !== undefined && (selected.length === 0 ||
                            selected[0].id !== searchedValues[this.primaryKey])) {
                            var current = this.$listControl.igGrid("selectedRows");
                            if (current.length > 0 && this.$listControl.igGrid("hasVerticalScrollbar")) {
                                this.$listControl.igGrid("virtualScrollTo", current[0].index === source.length - 1
                                    ? current[0].index : current[0].index + 1);
                            }
                        }
                    };
                    GridSwapPart.prototype.bindIn = function (src) {
                        this.$listControl.igGrid("option", "dataSource", src);
                        this.$listControl.igGrid("dataBind");
                    };
                    return GridSwapPart;
                }(SwapPart));
                var GridSwapList = (function (_super) {
                    __extends(GridSwapList, _super);
                    function GridSwapList() {
                        _super.apply(this, arguments);
                    }
                    GridSwapList.prototype.sender = function (opts) {
                        return opts.item.closest("table")[0].id == this.swapParts[0].$listControl.attr("id")
                            ? "first" : "second";
                    };
                    GridSwapList.prototype.receiver = function (opts) {
                        return opts.item.closest("table")[0].id == this.swapParts[1].$listControl.attr("id")
                            ? "second" : "first";
                    };
                    GridSwapList.prototype.target = function (opts) {
                        if (opts.helper !== undefined && opts.helper.hasClass("select-drag")) {
                            return opts.helper.find("tr").map(function () {
                                return $(this).data("id");
                            });
                        }
                        return [opts.item.data("id")];
                    };
                    GridSwapList.prototype.neighbor = function (opts) {
                        return opts.item.prev().length === 0 ? "ceil" : opts.item.prev().data("id");
                    };
                    GridSwapList.prototype.dropDone = function () {
                        var self = this;
                        self.swapParts[0].$listControl.igGridSelection("clearSelection");
                        self.swapParts[1].$listControl.igGridSelection("clearSelection");
                        setTimeout(function () {
                            (self.transportBuilder.direction === "first"
                                ? self.swapParts[0].$listControl
                                : self.swapParts[1].$listControl).igGrid("virtualScrollTo", self.transportBuilder.incomeIndex);
                            (self.transportBuilder.startAt === "first"
                                ? self.swapParts[0].$listControl
                                : self.swapParts[1].$listControl).igGrid("virtualScrollTo", self.transportBuilder.outcomeIndex + 1);
                        }, 0);
                    };
                    GridSwapList.prototype.enableDrag = function (ctx, value, parts, cb) {
                        var self = this;
                        for (var idx in parts) {
                            this.swapParts[parts[idx]].$listControl.on("iggridrowsrendered", function (evt, ui) {
                                cb.call(ctx, parts, value);
                            });
                        }
                    };
                    GridSwapList.prototype.move = function (forward, value) {
                        var $source = forward === true ? this.swapParts[0].$listControl : this.swapParts[1].$listControl;
                        var sourceList = forward === true ? this.swapParts[0].dataSource : this.swapParts[1].dataSource;
                        var $dest = forward === true ? this.swapParts[1].$listControl : this.swapParts[0].$listControl;
                        var destList = forward === true ? this.swapParts[1].dataSource : this.swapParts[0].dataSource;
                        var selectedRows = $source.igGrid("selectedRows");
                        selectedRows.sort(function (one, two) {
                            return one.index - two.index;
                        });
                        var selectedIds = selectedRows.map(function (row) { return row.id; });
                        this.transportBuilder.at(forward ? "first" : "second").directTo(forward ? "second" : "first")
                            .target(selectedIds).toAdjacent(destList.length > 0 ? destList[destList.length - 1][this.swapParts[0].primaryKey] : null).update();
                        this.swapParts[0].bindData(this.transportBuilder.getFirst());
                        this.swapParts[1].bindData(this.transportBuilder.getSecond());
                        value(this.transportBuilder.getSecond());
                        $source.igGridSelection("clearSelection");
                        $dest.igGridSelection("clearSelection");
                        setTimeout(function () {
                            $source.igGrid("virtualScrollTo", sourceList.length - 1 === selectedRows[0].index
                                ? selectedRows[0].index - 1 : selectedRows[0].index + 1);
                            $dest.igGrid("virtualScrollTo", destList.length - 1);
                        }, 10);
                    };
                    return GridSwapList;
                }(SwapModel));
                var ListItemTransporter = (function () {
                    function ListItemTransporter(firstList, secondList) {
                        this.firstList = firstList;
                        this.secondList = secondList;
                    }
                    ListItemTransporter.prototype.first = function (firstList) {
                        this.firstList = firstList;
                        return this;
                    };
                    ListItemTransporter.prototype.second = function (secondList) {
                        this.secondList = secondList;
                        return this;
                    };
                    ListItemTransporter.prototype.at = function (startAt) {
                        this.startAt = startAt;
                        return this;
                    };
                    ListItemTransporter.prototype.directTo = function (direction) {
                        this.direction = direction;
                        return this;
                    };
                    ListItemTransporter.prototype.out = function (index) {
                        this.outcomeIndex = index;
                        return this;
                    };
                    ListItemTransporter.prototype.into = function (index) {
                        this.incomeIndex = index;
                        return this;
                    };
                    ListItemTransporter.prototype.primary = function (primaryKey) {
                        this.primaryKey = primaryKey;
                        return this;
                    };
                    ListItemTransporter.prototype.target = function (targetIds) {
                        this.targetIds = targetIds;
                        return this;
                    };
                    ListItemTransporter.prototype.toAdjacent = function (adjId) {
                        if (adjId === null)
                            adjId = "ceil";
                        this.adjacentIncomeId = adjId;
                        return this;
                    };
                    ListItemTransporter.prototype.indexOf = function (list, targetId) {
                        var _this = this;
                        return _.findIndex(list, function (elm) { return elm[_this.primaryKey].toString() === targetId.toString(); });
                    };
                    ListItemTransporter.prototype.move = function (src, dest) {
                        for (var i = 0; i < this.targetIds.length; i++) {
                            this.outcomeIndex = this.indexOf(src, this.targetIds[i]);
                            if (this.outcomeIndex === -1)
                                return;
                            var target = src.splice(this.outcomeIndex, 1);
                            this.incomeIndex = this.indexOf(dest, this.adjacentIncomeId) + 1;
                            if (this.incomeIndex === 0) {
                                if (this.adjacentIncomeId === "ceil")
                                    this.incomeIndex = 0;
                                else if (target !== undefined) {
                                    src.splice(this.outcomeIndex, 0, target[0]);
                                    return;
                                }
                            }
                            dest.splice(this.incomeIndex + i, 0, target[0]);
                        }
                    };
                    ListItemTransporter.prototype.determineDirection = function () {
                        if (this.startAt.toLowerCase() !== this.direction.toLowerCase()
                            && this.direction.toLowerCase() === "second") {
                            return "firstToSecond";
                        }
                        else if (this.startAt.toLowerCase() !== this.direction.toLowerCase()
                            && this.direction.toLowerCase() === "first") {
                            return "secondToFirst";
                        }
                        else if (this.startAt.toLowerCase() === this.direction.toLowerCase()
                            && this.direction.toLowerCase() === "first") {
                            return "insideFirst";
                        }
                        else
                            return "insideSecond";
                    };
                    ListItemTransporter.prototype.update = function () {
                        switch (this.determineDirection()) {
                            case "firstToSecond":
                                this.move(this.firstList, this.secondList);
                                break;
                            case "secondToFirst":
                                this.move(this.secondList, this.firstList);
                                break;
                            case "insideFirst":
                                this.move(this.firstList, this.firstList);
                                break;
                            case "insideSecond":
                                this.move(this.secondList, this.secondList);
                                break;
                        }
                    };
                    ListItemTransporter.prototype.getFirst = function () {
                        return this.firstList;
                    };
                    ListItemTransporter.prototype.getSecond = function () {
                        return this.secondList;
                    };
                    ListItemTransporter.prototype.setFirst = function (first) {
                        this.firstList = first;
                    };
                    ListItemTransporter.prototype.setSecond = function (second) {
                        this.secondList = second;
                    };
                    return ListItemTransporter;
                }());
            })(koExtentions = ui_8.koExtentions || (ui_8.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_9) {
            var koExtentions;
            (function (koExtentions) {
                var NtsSwitchButtonBindingHandler = (function () {
                    function NtsSwitchButtonBindingHandler() {
                    }
                    NtsSwitchButtonBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var container = $(element);
                        container.keydown(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (code === 32) {
                                evt.preventDefault();
                            }
                        });
                        container.keyup(function (evt, ui) {
                            var code = evt.which || evt.keyCode;
                            if (container.data("enable") !== false) {
                                if (code === 32) {
                                    var selectedCode = container.find(".nts-switch-button:first").data('swbtn');
                                    data.value(selectedCode);
                                }
                                else if (code === 37 || code === 38) {
                                    var inputList = container.find(".nts-switch-button");
                                    var currentSelect = _.findIndex(inputList, function (item) {
                                        return $(item).data('swbtn') === data.value();
                                    });
                                    var selectedCode = $(inputList[currentSelect - 1]).data('swbtn');
                                    if (!nts.uk.util.isNullOrUndefined(selectedCode)) {
                                        data.value(selectedCode);
                                    }
                                }
                                else if (code === 39 || code === 40) {
                                    var inputList = container.find(".nts-switch-button");
                                    var currentSelect = _.findIndex(inputList, function (item) {
                                        return $(item).data('swbtn') === data.value();
                                    });
                                    var selectedCode = $(inputList[currentSelect + 1]).data('swbtn');
                                    if (!nts.uk.util.isNullOrUndefined(selectedCode)) {
                                        data.value(selectedCode);
                                    }
                                }
                            }
                        });
                        var defVal = new nts.uk.util.value.DefaultValue().onReset(container, data.value);
                    };
                    NtsSwitchButtonBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var selectedCssClass = 'selected';
                        var options = ko.unwrap(data.options);
                        var optionValue = ko.unwrap(data.optionsValue);
                        var optionText = ko.unwrap(data.optionsText);
                        var selectedValue = ko.unwrap(data.value);
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var container = $(element);
                        container.data("enable", enable);
                        container.addClass("ntsControl switchButton-wrapper");
                        if (nts.uk.util.isNullOrUndefined(container.attr("tabindex")))
                            container.attr("tabindex", "0");
                        $('button', container).each(function (index, btn) {
                            var $btn = $(btn);
                            var btnValue = $(btn).data('swbtn');
                            var foundFlag = _.findIndex(options, function (opt) {
                                return opt[optionValue] == btnValue;
                            }) != -1;
                            if (!foundFlag) {
                                $btn.remove();
                                return;
                            }
                        });
                        _.forEach(options, function (opt) {
                            var value = opt[optionValue];
                            var text = opt[optionText];
                            var targetBtn = NtsSwitchButtonBindingHandler.setSelectedClass(container, selectedCssClass, selectedValue, value);
                            if (targetBtn) {
                            }
                            else {
                                var btn = $('<button>').text(text)
                                    .addClass('nts-switch-button')
                                    .data('swbtn', value)
                                    .attr('tabindex', "-1")
                                    .on('click', function () {
                                    var selectedValue = $(this).data('swbtn');
                                    data.value(selectedValue);
                                    $('button', container).removeClass(selectedCssClass);
                                    $(this).addClass(selectedCssClass);
                                });
                                if (selectedValue == value) {
                                    btn.addClass(selectedCssClass);
                                }
                                container.append(btn);
                            }
                        });
                        if (enable === true) {
                            $('button', container).prop("disabled", false);
                        }
                        else {
                            $('button', container).prop("disabled", true);
                            new nts.uk.util.value.DefaultValue().applyReset(container, data.value);
                        }
                    };
                    NtsSwitchButtonBindingHandler.setSelectedClass = function ($container, selectedCssClass, selectedValue, optValue) {
                        var targetBtn;
                        $('button', $container).each(function (index, btn) {
                            var btnValue = $(btn).data('swbtn');
                            if (btnValue == optValue) {
                                targetBtn = $(btn);
                            }
                            if (btnValue == selectedValue) {
                                $(btn).addClass(selectedCssClass);
                            }
                            else {
                                $(btn).removeClass(selectedCssClass);
                            }
                        });
                        return targetBtn;
                    };
                    return NtsSwitchButtonBindingHandler;
                }());
                ko.bindingHandlers['ntsSwitchButton'] = new NtsSwitchButtonBindingHandler();
            })(koExtentions = ui_9.koExtentions || (ui_9.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_10) {
            var koExtentions;
            (function (koExtentions) {
                var TabPanelBindingHandler = (function () {
                    function TabPanelBindingHandler() {
                    }
                    TabPanelBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var tabs = ko.unwrap(data.dataSource);
                        var direction = ko.unwrap(data.direction || "horizontal");
                        var container = $(element);
                        container.prepend('<ul></ul>');
                        var titleContainer = container.children('ul');
                        for (var i = 0; i < tabs.length; i++) {
                            var id = tabs[i].id;
                            var title = tabs[i].title;
                            titleContainer.append('<li><a href="#' + id + '">' + title + '</a></li>');
                            var content = tabs[i].content;
                            container.children(content).wrap('<div id="' + id + '"></div>');
                        }
                        container.tabs({
                            activate: function (evt, ui) {
                                data.active(ui.newPanel[0].id);
                                container.children('ul').children('.ui-tabs-active').addClass('active');
                                container.children('ul').children('li').not('.ui-tabs-active').removeClass('active');
                                container.children('ul').children('.ui-state-disabled').addClass('disabled');
                                container.children('ul').children('li').not('.ui-state-disabled').removeClass('disabled');
                            }
                        }).addClass(direction);
                    };
                    TabPanelBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var tabs = ko.unwrap(data.dataSource);
                        var container = $(element);
                        var activeTab = tabs.filter(function (tab) { return tab.id == data.active(); })[0];
                        var indexActive = tabs.indexOf(activeTab);
                        container.tabs("option", "active", indexActive);
                        tabs.forEach(function (tab) {
                            if (tab.enable()) {
                                container.tabs("enable", '#' + tab.id);
                                container.children('#' + tab.id).children('div').show();
                                container.children('ul').children('li[aria-controls="' + tab.id + '"]').removeClass('disabled');
                            }
                            else {
                                container.tabs("disable", '#' + tab.id);
                                container.children('#' + tab.id).children('div').hide();
                                container.children('ul').children('li[aria-controls="' + tab.id + '"]').addClass('disabled');
                            }
                            if (!tab.visible()) {
                                container.children('ul').children('li[aria-controls="' + tab.id + '"]').hide();
                            }
                            else {
                                container.children('ul').children('li[aria-controls="' + tab.id + '"]').show();
                            }
                        });
                    };
                    return TabPanelBindingHandler;
                }());
                ko.bindingHandlers['ntsTabPanel'] = new TabPanelBindingHandler();
            })(koExtentions = ui_10.koExtentions || (ui_10.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsPanelBindingHandler = (function () {
                    function NtsPanelBindingHandler() {
                    }
                    NtsPanelBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var width = (data.width !== undefined) ? ko.unwrap(data.width) : null;
                        var height = (data.height !== undefined) ? ko.unwrap(data.height) : null;
                        var direction = (data.direction !== undefined) ? ko.unwrap(data.direction) : "right";
                        var showIcon = (data.showIcon !== undefined) ? ko.unwrap(data.showIcon) : false;
                        var visible = (data.visible !== undefined) ? ko.unwrap(data.visible) : true;
                        var container = $(element);
                        container.addClass("panel ntsPanel caret-background");
                        var caretClass = "caret-" + direction;
                        container.addClass(caretClass);
                        if (showIcon === true) {
                            container.append("<i class='icon icon-searchbox'></i>");
                        }
                    };
                    NtsPanelBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var width = (data.width !== undefined) ? ko.unwrap(data.width) : null;
                        var height = (data.height !== undefined) ? ko.unwrap(data.height) : null;
                        var direction = (data.direction !== undefined) ? ko.unwrap(data.direction) : "right";
                        var showIcon = (data.showIcon !== undefined) ? ko.unwrap(data.showIcon) : false;
                        var visible = (data.visible !== undefined) ? ko.unwrap(data.visible) : null;
                        var container = $(element);
                        if (!nts.uk.util.isNullOrEmpty(width))
                            container.width(width);
                        if (!nts.uk.util.isNullOrEmpty(height))
                            container.height(height);
                        if (!nts.uk.util.isNullOrEmpty(visible)) {
                            if (visible === true)
                                container.show();
                            else
                                container.hide();
                        }
                    };
                    return NtsPanelBindingHandler;
                }());
                ko.bindingHandlers['ntsPanel'] = new NtsPanelBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_11) {
            var koExtentions;
            (function (koExtentions) {
                var NtsTreeGridViewBindingHandler = (function () {
                    function NtsTreeGridViewBindingHandler() {
                    }
                    NtsTreeGridViewBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
                        var optionsValue = ko.unwrap(data.primaryKey !== undefined ? data.primaryKey : data.optionsValue);
                        var optionsText = ko.unwrap(data.primaryText !== undefined ? data.primaryText : data.optionsText);
                        var optionsChild = ko.unwrap(data.childDataKey !== undefined ? data.childDataKey : data.optionsChild);
                        var extColumns = ko.unwrap(data.columns !== undefined ? data.columns : data.extColumns);
                        var selectedValues = ko.unwrap(data.selectedValues);
                        var singleValue = ko.unwrap(data.value);
                        var showCheckBox = data.showCheckBox !== undefined ? ko.unwrap(data.showCheckBox) : true;
                        var enable = data.enable !== undefined ? ko.unwrap(data.enable) : true;
                        var height = ko.unwrap(data.height !== undefined ? data.height : '100%');
                        var width = ko.unwrap(data.width !== undefined ? data.width : '100%');
                        if (extColumns !== undefined && extColumns !== null) {
                            var displayColumns = extColumns;
                        }
                        else {
                            var displayColumns = [
                                { headerText: "コード", key: optionsValue, dataType: "string", hidden: true },
                                { headerText: "コード／名称", key: optionsText, dataType: "string" }
                            ];
                        }
                        var $treegrid = $(element);
                        $(element).igTreeGrid({
                            width: width,
                            height: height,
                            dataSource: _.cloneDeep(options),
                            primaryKey: optionsValue,
                            columns: displayColumns,
                            childDataKey: optionsChild,
                            initialExpandDepth: 10,
                            tabIndex: -1,
                            features: [
                                {
                                    name: "Selection",
                                    multipleSelection: true,
                                    activation: true,
                                    rowSelectionChanged: function (evt, ui) {
                                        var selectedRows = ui.selectedRows;
                                        if (ko.unwrap(data.multiple)) {
                                            if (ko.isObservable(data.selectedValues)) {
                                                data.selectedValues(_.map(selectedRows, function (row) {
                                                    return row.id;
                                                }));
                                            }
                                        }
                                        else {
                                            if (ko.isObservable(data.value)) {
                                                data.value(selectedRows.length <= 0 ? undefined : selectedRows[0].id);
                                            }
                                        }
                                    }
                                },
                                {
                                    name: "RowSelectors",
                                    enableCheckBoxes: showCheckBox,
                                    checkBoxMode: "biState"
                                }]
                        });
                        var treeGridId = $treegrid.attr('id');
                        $(element).closest('.ui-igtreegrid').addClass('nts-treegridview').attr("tabindex", "0");
                        $treegrid.setupSearchScroll("igTreeGrid");
                    };
                    NtsTreeGridViewBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = ko.unwrap(data.dataSource !== undefined ? data.dataSource : data.options);
                        var selectedValues = ko.unwrap(data.selectedValues);
                        var singleValue = ko.unwrap(data.value);
                        var originalSource = $(element).igTreeGrid('option', 'dataSource');
                        if (!_.isEqual(originalSource, options)) {
                            $(element).igTreeGrid("option", "dataSource", _.cloneDeep(options));
                            $(element).igTreeGrid("dataBind");
                        }
                        var multiple = data.multiple != undefined ? ko.unwrap(data.multiple) : true;
                        if ($(element).igTreeGridSelection("option", "multipleSelection") !== multiple) {
                            $(element).igTreeGridSelection("option", "multipleSelection", multiple);
                        }
                        var showCheckBox = ko.unwrap(data.showCheckBox != undefined ? data.showCheckBox : true);
                        if ($(element).igTreeGridRowSelectors("option", "enableCheckBoxes") !== showCheckBox) {
                            $(element).igTreeGridRowSelectors("option", "enableCheckBoxes", showCheckBox);
                        }
                        if ((selectedValues === null || selectedValues === undefined) && (singleValue === null || singleValue === undefined)) {
                            $(element).igTreeGridSelection("clearSelection");
                        }
                        else {
                            var olds = _.map($(element).igTreeGridSelection("selectedRow"), function (row) {
                                return row.id;
                            });
                            if (multiple) {
                                if (_.isEqual(selectedValues.sort(), olds.sort())) {
                                    return;
                                }
                                $(element).igTreeGridSelection("clearSelection");
                                selectedValues.forEach(function (val) {
                                    $(element).igTreeGridSelection("selectRowById", val);
                                });
                            }
                            else {
                                if (olds.length > 1 && olds[0] === singleValue) {
                                    return;
                                }
                                $(element).igTreeGridSelection("clearSelection");
                                $(element).igTreeGridSelection("selectRowById", singleValue);
                            }
                        }
                    };
                    return NtsTreeGridViewBindingHandler;
                }());
                ko.bindingHandlers['ntsTreeGridView'] = new NtsTreeGridViewBindingHandler();
            })(koExtentions = ui_11.koExtentions || (ui_11.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_12) {
            var koExtentions;
            (function (koExtentions) {
                var NtsUpDownBindingHandler = (function () {
                    function NtsUpDownBindingHandler() {
                    }
                    NtsUpDownBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var $upDown = $(element);
                        if ($upDown.prop("tagName").toLowerCase() !== "div") {
                            throw new Error('The element must be a div');
                        }
                        var data = valueAccessor();
                        var elementId = $upDown.attr('id');
                        var comId = ko.unwrap(data.comId);
                        var childField = ko.unwrap(data.childDataKey);
                        var primaryKey = ko.unwrap(data.primaryKey);
                        var height = ko.unwrap(data.height);
                        var targetType = ko.unwrap(data.type);
                        var swapTarget = ko.unwrap(data.swapTarget);
                        if (nts.uk.util.isNullOrUndefined(elementId)) {
                            throw new Error('The element NtsSwapList must have id attribute.');
                        }
                        if (nts.uk.util.isNullOrUndefined(comId)) {
                            throw new Error('The target element of NtsUpDown is required.');
                        }
                        $upDown.addClass("ntsComponent ntsUpDown").append("<div class='upDown-container'/>");
                        $upDown.find(".upDown-container")
                            .append("<button class = 'ntsUpButton ntsButton ntsUpDownButton auto-height' id= '" + elementId + "-up'/>")
                            .append("<button class = 'ntsDownButton ntsButton ntsUpDownButton auto-height' id= '" + elementId + "-down'/>");
                        var $target = $(comId);
                        if (height !== undefined) {
                            $upDown.height(height);
                            $upDown.find(".upDown-container").height(height);
                        }
                        else {
                            var targetHeight = $(comId + "_container").height();
                            if (targetHeight === undefined) {
                                var h = _.find($(comId).attr("data-bind").split(","), function (attr) {
                                    return attr.indexOf("height") >= 0;
                                });
                                if (h !== undefined) {
                                    targetHeight = parseFloat(h.split(":")[1]);
                                }
                            }
                            $upDown.height(targetHeight);
                            $upDown.find(".upDown-container").height(targetHeight);
                        }
                        var $up = $upDown.find(".ntsUpButton");
                        var $down = $upDown.find(".ntsDownButton");
                        $up.append("<i class='icon icon-button-arrow-top'/>");
                        $down.append("<i class='icon icon-button-arrow-bottom'/>");
                        var move = function (upDown, $targetElement) {
                            var multySelectedRaw = $targetElement.igGrid("selectedRows");
                            var singleSelectedRaw = $targetElement.igGrid("selectedRow");
                            var selected = [];
                            if (multySelectedRaw !== null) {
                                selected = _.filter(multySelectedRaw, function (item) {
                                    return item["index"] >= 0;
                                });
                            }
                            else if (singleSelectedRaw !== null) {
                                selected.push(singleSelectedRaw);
                            }
                            else {
                                return;
                            }
                            var source = _.cloneDeep($targetElement.igGrid("option", "dataSource"));
                            var group = 1;
                            var grouped = { "group1": [] };
                            if (selected.length > 0) {
                                var size = selected.length;
                                selected = _.sortBy(selected, "index");
                                _.forEach(selected, function (item, idx) {
                                    grouped["group" + group].push(item);
                                    if (idx !== size - 1 && item["index"] + 1 !== selected[idx + 1]["index"]) {
                                        group++;
                                        grouped["group" + group] = [];
                                    }
                                });
                                var moved = false;
                                _.forEach(_.valuesIn(grouped), function (items) {
                                    var firstIndex = items[0].index;
                                    var lastIndex = items[items.length - 1].index;
                                    if (upDown < 0) {
                                        var canMove = firstIndex > 0;
                                    }
                                    else {
                                        var canMove = lastIndex < source.length - 1;
                                    }
                                    if (canMove) {
                                        var removed = source.splice(firstIndex, items.length);
                                        _.forEach(removed, function (item, idx) {
                                            source.splice(firstIndex + upDown + idx, 0, item);
                                        });
                                        moved = true;
                                    }
                                });
                                if (moved) {
                                    $targetElement.igGrid("virtualScrollTo", 0);
                                    data.targetSource(source);
                                    var index = upDown + grouped["group1"][0].index;
                                    $targetElement.igGrid("virtualScrollTo", index);
                                }
                            }
                        };
                        var moveTree = function (upDown, $targetElement) {
                            var multiSelectedRaw = $targetElement.igTreeGrid("selectedRows");
                            var singleSelectedRaw = $targetElement.igTreeGrid("selectedRow");
                            var selected;
                            if (multiSelectedRaw !== null) {
                                if (multiSelectedRaw.length !== 1) {
                                    return;
                                }
                                selected = multiSelectedRaw[0];
                            }
                            else if (singleSelectedRaw !== null) {
                                selected.push(singleSelectedRaw);
                            }
                            else {
                                return;
                            }
                            if (selected["index"] < 0) {
                                return;
                            }
                            var source = _.cloneDeep($targetElement.igTreeGrid("option", "dataSource"));
                            var result = findChild(upDown, selected["id"], source, false, false);
                            var moved = result.moved;
                            var changed = result.changed;
                            source = result.source;
                            if (moved && changed) {
                                data.targetSource(source);
                                var index = $targetElement.igTreeGrid("selectedRows")[0].index;
                                if (index !== selected["index"]) {
                                    var scrollTo = _.sumBy(_.filter($target.igTreeGrid("allRows"), function (row) {
                                        return $(row).attr("data-row-idx") < index;
                                    }), function (row) {
                                        return $(row).height();
                                    });
                                    $targetElement.igTreeGrid("scrollContainer").scrollTop(scrollTo);
                                }
                            }
                        };
                        var findChild = function (upDown, key, children, moved, changed) {
                            var index = -1;
                            if (children !== undefined && children !== null && children.length > 0 && !moved && !changed) {
                                _.forEach(children, function (child, idx) {
                                    if (!moved) {
                                        if (child[primaryKey] === key) {
                                            index = idx;
                                            return false;
                                        }
                                        else {
                                            var result = findChild(upDown, key, child[childField], moved, changed);
                                            child[childField] = result.source;
                                            moved = result.moved;
                                            changed = result.changed;
                                        }
                                    }
                                    else {
                                        return false;
                                    }
                                });
                                if (index >= 0) {
                                    if (upDown < 0) {
                                        var canMove = index > 0;
                                    }
                                    else {
                                        var canMove = index < children.length - 1;
                                    }
                                    if (canMove) {
                                        var removed = children.splice(index, 1);
                                        children.splice(index + upDown, 0, removed[0]);
                                        changed = true;
                                    }
                                    moved = true;
                                }
                                return {
                                    source: children,
                                    moved: moved,
                                    changed: changed
                                };
                            }
                            return {
                                source: children,
                                moved: moved,
                                changed: changed
                            };
                        };
                        $up.click(function (event, ui) {
                            if (targetType === "tree") {
                                moveTree(-1, $target);
                            }
                            else if (targetType === "grid") {
                                move(-1, $target);
                            }
                            else if (targetType === "swap") {
                                var swapTargetGrid = swapTarget.toLocaleLowerCase() === "left" ? "-grid1" : "-grid2";
                                move(-1, $(comId + swapTargetGrid));
                            }
                        });
                        $down.click(function (event, ui) {
                            if (targetType === "tree") {
                                moveTree(1, $target);
                            }
                            else if (targetType === "grid") {
                                move(1, $target);
                            }
                            else if (targetType === "swap") {
                                var swapTargetGrid = swapTarget.toLocaleLowerCase() === "left" ? "-grid1" : "-grid2";
                                move(1, $(comId + swapTargetGrid));
                            }
                        });
                    };
                    NtsUpDownBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var $upDown = $(element);
                        var elementId = $upDown.attr('id');
                        if (nts.uk.util.isNullOrUndefined(elementId)) {
                            throw new Error('the element NtsSwapList must have id attribute.');
                        }
                        var data = valueAccessor();
                        var enable = ko.unwrap(data.enable);
                        if (enable === false) {
                            $upDown.find(".ntsUpDownButton").prop('disabled', true);
                        }
                        else {
                            $upDown.find(".ntsUpDownButton").prop('disabled', false);
                        }
                    };
                    return NtsUpDownBindingHandler;
                }());
                ko.bindingHandlers['ntsUpDown'] = new NtsUpDownBindingHandler();
            })(koExtentions = ui_12.koExtentions || (ui_12.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var WizardBindingHandler = (function () {
                    function WizardBindingHandler() {
                    }
                    WizardBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var options = ko.unwrap(data.steps);
                        var theme = ko.unwrap(data.theme);
                        var cssClass = "nts-wizard " + "theme-" + theme;
                        var container = $(element);
                        for (var i = 0; i < options.length; i++) {
                            var contentClass = ko.unwrap(options[i].content);
                            var htmlStep = container.children('.steps').children(contentClass).html();
                            var htmlContent = container.children('.contents').children(contentClass).html();
                            container.append('<h1 class="' + contentClass + '">' + htmlStep + '</h1>');
                            container.append('<div>' + htmlContent + '</div>');
                        }
                        var icon = container.find('.header .image').data('icon');
                        var header = container.children('.header');
                        container.children('.header').remove();
                        container.children('.steps').remove();
                        container.children('.contents').remove();
                        container.steps({
                            headerTag: "h1",
                            bodyTag: "div",
                            transitionEffect: "slideLeft",
                            stepsOrientation: "vertical",
                            titleTemplate: '<div>#title#</div>',
                            enablePagination: false,
                            enableFinishButton: false,
                            autoFocus: false,
                            onStepChanged: function () {
                                container.children('.steps').children('ul').children('li').removeClass('step-current');
                                container.children('.steps').children('ul').children('li').removeClass('step-prev');
                                container.children('.steps').children('ul').children('li').removeClass('step-next');
                                container.children('.steps').children('ul').children('.done').addClass('disabled');
                                container.children('.steps').children('ul').children('.current').addClass('step-current');
                                container.children('.steps').children('ul').children('.done').addClass('step-prev');
                                container.children('.steps').children('ul').children('.step-current').nextAll('li').not('.done').addClass('step-next');
                                return true;
                            }
                        }).data("length", options.length);
                        container.addClass(cssClass);
                        container.children('.steps').children('ul').children('li').children('a').before('<div class="nts-steps"></div>');
                        container.children('.steps').children('ul').children('li').children('a').addClass('nts-step-contents');
                        container.children('.steps').children('ul').children('.last').addClass('end');
                        container.children('.steps').children('ul').children('li').not('.begin').not('.end').children('.nts-steps').addClass('nts-steps-middle');
                        container.find('.nts-steps-middle').append('<div class="nts-vertical-line"></div><div class="nts-bridge"><div class="nts-point"></div><div class="nts-horizontal-line"></div></div>');
                        container.children('.steps').children('ul').children('li').removeClass('step-current');
                        container.children('.steps').children('ul').children('li').removeClass('step-prev');
                        container.children('.steps').children('ul').children('li').removeClass('step-next');
                        container.children('.steps').children('ul').children('.current').addClass('step-current');
                        container.children('.steps').children('ul').children('.done').addClass('step-prev');
                        container.children('.steps').children('ul').children('.step-current').nextAll('li').not('.done').addClass('step-next');
                        container.find('.actions').hide();
                        container.children('.steps').prepend(header);
                        container.find('.header .image').attr('style', 'background-image: url("' + icon + '")');
                    };
                    WizardBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                    };
                    return WizardBindingHandler;
                }());
                ko.bindingHandlers['ntsWizard'] = new WizardBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_13) {
            var option;
            (function (option_2) {
                var DialogOption = (function () {
                    function DialogOption() {
                        this.show = false;
                    }
                    return DialogOption;
                }());
                option_2.DialogOption = DialogOption;
                var ConfirmDialogOption = (function (_super) {
                    __extends(ConfirmDialogOption, _super);
                    function ConfirmDialogOption(option) {
                        _super.call(this);
                        this.modal = (option && option.modal !== undefined) ? option.modal : true;
                        this.buttons = [];
                        this.buttons.push({
                            text: "OK",
                            "class": "yes",
                            size: "large",
                            color: "proceed",
                            click: function (viewmodel, ui) {
                                viewmodel.okButtonClicked();
                                $(ui).dialog("close");
                            }
                        });
                    }
                    return ConfirmDialogOption;
                }(DialogOption));
                option_2.ConfirmDialogOption = ConfirmDialogOption;
                var DelDialogOption = (function (_super) {
                    __extends(DelDialogOption, _super);
                    function DelDialogOption(option) {
                        _super.call(this);
                        this.modal = (option && option.modal !== undefined) ? option.modal : true;
                        this.buttons = [];
                        this.buttons.push({
                            text: "はい",
                            "class": "yes ",
                            size: "large",
                            color: "danger",
                            click: function (viewmodel, ui) {
                                viewmodel.okButtonClicked();
                                ui.dialog("close");
                            }
                        });
                        this.buttons.push({
                            text: "いいえ",
                            "class": "no ",
                            size: "large",
                            color: "",
                            click: function (viewmodel, ui) {
                                viewmodel.cancelButtonClicked();
                                ui.dialog("close");
                            }
                        });
                    }
                    return DelDialogOption;
                }(DialogOption));
                option_2.DelDialogOption = DelDialogOption;
                var OKDialogOption = (function (_super) {
                    __extends(OKDialogOption, _super);
                    function OKDialogOption(option) {
                        _super.call(this);
                        this.modal = (option && option.modal !== undefined) ? option.modal : true;
                        this.buttons = [];
                        this.buttons.push({
                            text: "はい",
                            "class": "yes ",
                            size: "large",
                            color: "proceed",
                            click: function (viewmodel, ui) {
                                viewmodel.okButtonClicked();
                                ui.dialog("close");
                            }
                        });
                        this.buttons.push({
                            text: "いいえ",
                            "class": "no ",
                            size: "large",
                            color: "",
                            click: function (viewmodel, ui) {
                                viewmodel.cancelButtonClicked();
                                ui.dialog("close");
                            }
                        });
                    }
                    return OKDialogOption;
                }(DialogOption));
                option_2.OKDialogOption = OKDialogOption;
                var ErrorDialogOption = (function (_super) {
                    __extends(ErrorDialogOption, _super);
                    function ErrorDialogOption(option) {
                        _super.call(this);
                        this.headers = (option && option.headers) ? option.headers : [
                            new nts.uk.ui.errors.ErrorHeader("messageText", "エラー内容", "auto", true),
                            new nts.uk.ui.errors.ErrorHeader("message", "エラーコード", 150, true)
                        ];
                        this.modal = (option && option.modal !== undefined) ? option.modal : false;
                        this.displayrows = (option && option.displayrows) ? option.displayrows : 10;
                        this.autoclose = (option && option.autoclose !== undefined) ? option.autoclose : true;
                        this.buttons = [];
                        this.buttons.push({
                            text: "閉じる",
                            "class": "yes ",
                            size: "large",
                            color: "",
                            click: function (viewmodel, ui) {
                                viewmodel.closeButtonClicked();
                                ui.dialog("close");
                            }
                        });
                    }
                    return ErrorDialogOption;
                }(DialogOption));
                option_2.ErrorDialogOption = ErrorDialogOption;
                var ErrorDialogWithTabOption = (function (_super) {
                    __extends(ErrorDialogWithTabOption, _super);
                    function ErrorDialogWithTabOption(option) {
                        _super.call(this);
                        this.headers = (option && option.headers) ? option.headers : [
                            new ui_13.errors.ErrorHeader("tab", "タブ", 90, true),
                            new ui_13.errors.ErrorHeader("location", "エラー箇所", 115, true),
                            new ui_13.errors.ErrorHeader("message", "エラー詳細", 250, true)
                        ];
                        this.modal = (option && option.modal !== undefined) ? option.modal : false;
                        this.displayrows = (option && option.displayrows) ? option.displayrows : 10;
                        this.autoclose = (option && option.autoclose !== undefined) ? option.autoclose : true;
                        this.buttons = [];
                        this.buttons.push({
                            text: "閉じる",
                            "class": "yes ",
                            size: "large",
                            color: "",
                            click: function (viewmodel, ui) {
                                viewmodel.closeButtonClicked();
                                ui.dialog("close");
                            }
                        });
                    }
                    return ErrorDialogWithTabOption;
                }(ErrorDialogOption));
                option_2.ErrorDialogWithTabOption = ErrorDialogWithTabOption;
                var DialogButton = (function () {
                    function DialogButton() {
                    }
                    DialogButton.prototype.click = function (viewmodel, ui) { };
                    ;
                    return DialogButton;
                }());
                option_2.DialogButton = DialogButton;
            })(option = ui_13.option || (ui_13.option = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var file;
            (function (file_1) {
                var FileDownload = (function () {
                    function FileDownload(servicePath, data) {
                        var self = this;
                        self.servicePath = servicePath;
                        self.data = data;
                        self.isFinishTask = ko.observable(false);
                        self.isFinishTask.subscribe(function (value) {
                            if (value) {
                                clearInterval(self.interval);
                                self.isFinishTask(false);
                                self.download();
                            }
                        });
                    }
                    FileDownload.prototype.isTaskFinished = function (file) {
                        var options = {
                            dataType: 'text',
                            contentType: 'text/plain'
                        };
                        uk.request.ajax("file/file/isfinished", file.taskId).done(function (res) {
                            file.isFinishTask(res);
                        }).fail(function (error) {
                            file.reject(error);
                        });
                    };
                    FileDownload.prototype.print = function () {
                        var self = this;
                        self.deferred = $.Deferred();
                        var options = {
                            dataType: 'text',
                            contentType: 'application/json'
                        };
                        uk.request.ajax(self.servicePath, self.data, options).done(function (res) {
                            self.taskId = res;
                            self.interval = setInterval(self.isTaskFinished, 1000, self);
                        }).fail(function (error) {
                            self.deferred.reject(error);
                        });
                        return self.deferred.promise();
                    };
                    FileDownload.prototype.download = function () {
                        var self = this;
                        window.location.href = ("http://localhost:8080/nts.uk.pr.web/webapi/file/file/dl/" + self.taskId);
                        if (self.deferred) {
                            self.deferred.resolve();
                        }
                    };
                    return FileDownload;
                }());
                file_1.FileDownload = FileDownload;
            })(file = ui.file || (ui.file = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_14) {
            var koExtentions;
            (function (koExtentions) {
                var NtsMonthDaysBindingHandler = (function () {
                    function NtsMonthDaysBindingHandler() {
                    }
                    NtsMonthDaysBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var $container = $(element);
                        var self = this;
                        var value = ko.unwrap(data.value);
                        var dataName = ko.unwrap(data.name);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        $container.addClass("ntsControl ntsMonthDays_Container");
                        $container.append("<div class='ntsMonthDays'/>");
                        var $control = $container.find(".ntsMonthDays");
                        $control.append("<div class='ntsMonthPicker ntsComboBox ntsMonthDays_Component'/><div class='ntsMonthLabel ntsLabel ntsMonthDays_Component'/><div class='ntsDayPicker ntsComboBox ntsMonthDays_Component'/><div class='ntsDayLabel ntsLabel ntsMonthDays_Component'/>");
                        var $monthPicker = $control.find(".ntsMonthPicker");
                        var $dayPicker = $control.find(".ntsDayPicker");
                        var $monthLabel = $control.find(".ntsMonthLabel");
                        var $dayLabel = $control.find(".ntsDayLabel");
                        $monthLabel.append("<label>月</label>");
                        $dayLabel.append("<label>日</label>");
                        $monthPicker.igCombo({
                            dataSource: NtsMonthDaysBindingHandler.getMonths(),
                            textKey: "text",
                            valueKey: "value",
                            width: "60px",
                            height: "30px",
                            mode: "dropdown",
                            selectionChanged: function (evt, ui) {
                                var currentMonth = ui.items[0].data.value;
                                var currentDay = $dayPicker.igCombo("selectedItems");
                                var days = NtsMonthDaysBindingHandler.getDaysInMonth(currentMonth);
                                var value = currentDay[0].data.value > days.length ? days.length : currentDay[0].data.value;
                                $dayPicker.igCombo("option", "dataSource", days);
                                data.value(currentMonth * 100 + value);
                            }
                        });
                        $dayPicker.igCombo({
                            dataSource: NtsMonthDaysBindingHandler.getDaysInMonth(1),
                            textKey: "text",
                            valueKey: "value",
                            width: "60px",
                            height: "30px",
                            mode: "dropdown",
                            selectionChanged: function (evt, ui) {
                                var currentDay = ui.items[0].data.value;
                                var currentMonth = $monthPicker.igCombo("selectedItems")[0].data.value;
                                data.value(currentMonth * 100 + currentDay);
                            }
                        });
                    };
                    NtsMonthDaysBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var $container = $(element);
                        var value = ko.unwrap(data.value);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        var $monthPicker = $container.find(".ntsMonthPicker");
                        var $dayPicker = $container.find(".ntsDayPicker");
                        if (enable !== false) {
                            $monthPicker.igCombo('option', 'disabled', false);
                            $dayPicker.igCombo('option', 'disabled', false);
                        }
                        else {
                            $monthPicker.igCombo('option', 'disabled', true);
                            $dayPicker.igCombo('option', 'disabled', true);
                        }
                        if (!nts.uk.util.isNullOrUndefined(value) && nts.uk.ntsNumber.isNumber(value)) {
                            var month = nts.uk.ntsNumber.trunc(parseInt(value) / 100);
                            var day = parseInt(value) % 100;
                            $monthPicker.igCombo("value", month);
                            $dayPicker.igCombo("value", day);
                        }
                        var currentDay = $dayPicker.igCombo("selectedItems")[0].data.value;
                        var currentMonth = $monthPicker.igCombo("selectedItems")[0].data.value;
                        data.value(currentMonth * 100 + currentDay);
                    };
                    NtsMonthDaysBindingHandler.getMonths = function () {
                        var monthSource = [];
                        while (monthSource.length < 12) {
                            monthSource.push({ text: monthSource.length + 1, value: monthSource.length + 1 });
                        }
                        return monthSource;
                    };
                    NtsMonthDaysBindingHandler.getDaysInMonth = function (month) {
                        var daysInMonth = moment(month, "MM").daysInMonth();
                        if (daysInMonth !== NaN) {
                            if (month === 2) {
                                daysInMonth++;
                            }
                            var days = [];
                            while (days.length < daysInMonth) {
                                days.push({ text: days.length + 1, value: days.length + 1 });
                            }
                            return days;
                        }
                        return [];
                    };
                    return NtsMonthDaysBindingHandler;
                }());
                ko.bindingHandlers['ntsMonthDays'] = new NtsMonthDaysBindingHandler();
            })(koExtentions = ui_14.koExtentions || (ui_14.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_15) {
            var koExtentions;
            (function (koExtentions) {
                var NtsDateRangePickerBindingHandler = (function () {
                    function NtsDateRangePickerBindingHandler() {
                    }
                    NtsDateRangePickerBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var $container = $(element);
                        var dateType = ko.unwrap(data.type);
                        var maxRange = ko.unwrap(data.maxRange);
                        var value = data.value;
                        var dataName = ko.unwrap(data.name);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        var showNextPrevious = data.showNextPrevious === undefined ? false : ko.unwrap(data.showNextPrevious);
                        var required = ko.unwrap(data.required);
                        var id = nts.uk.util.randomId();
                        $container.append("<div class='ntsDateRange_Container' id='" + id + "' />");
                        var $datePickerArea = $container.find(".ntsDateRange_Container");
                        $datePickerArea.append("<div class='ntsDateRangeComponent ntsControl ntsDateRange'>" +
                            "<div class='ntsDateRangeComponent ntsStartDate ntsControl nts-datepicker-wrapper'/><div class='ntsDateRangeComponent ntsRangeLabel'><label>～</label></div>" +
                            "<div class='ntsDateRangeComponent ntsEndDate ntsControl nts-datepicker-wrapper' /></div>");
                        $datePickerArea.data("required", required);
                        var dateFormat = (dateType !== 'yearmonth') ? "YYYY/MM/DD" : 'YYYY/MM';
                        var ISOFormat = uk.text.getISOFormat(dateFormat);
                        ISOFormat = ISOFormat.replace(/d/g, "").trim();
                        if (showNextPrevious === true) {
                            $datePickerArea.append("<div class= 'ntsDateRangeComponent ntsDateNextButton_Container ntsRangeButton_Container'><button class = 'ntsDateNextButton ntsButton ntsDateRangeButton auto-height'/></div>");
                            $datePickerArea.prepend("<div class='ntsDateRangeComponent ntsDatePreviousButton_Container ntsRangeButton_Container'><button class = 'ntsDatePrevButton ntsButton ntsDateRangeButton auto-height'/></div>");
                            var $nextButton = $container.find(".ntsDateNextButton");
                            var $prevButton = $container.find(".ntsDatePrevButton");
                            $nextButton.text("next").click(function (evt, ui) {
                                var $startDate = $container.find(".ntsStartDatePicker");
                                var $endDate = $container.find(".ntsEndDatePicker");
                                var oldValue = value();
                                var currentStart = $startDate.val();
                                var currentEnd = $endDate.val();
                                if (!nts.uk.util.isNullOrEmpty(currentStart)) {
                                    var startDate = moment(currentStart, dateFormat);
                                    if (startDate.isValid()) {
                                        var isEndOfMonth = startDate.daysInMonth() === startDate.date();
                                        startDate.month(startDate.month() + 1);
                                        if (isEndOfMonth) {
                                            startDate.endOf("month");
                                        }
                                        oldValue.startDate = startDate.format(dateFormat);
                                    }
                                }
                                if (!nts.uk.util.isNullOrEmpty(currentEnd)) {
                                    var endDate = moment(currentEnd, dateFormat);
                                    if (endDate.isValid()) {
                                        var isEndOfMonth = endDate.daysInMonth() === endDate.date();
                                        endDate.month(endDate.month() + 1);
                                        if (isEndOfMonth) {
                                            endDate.endOf("month");
                                        }
                                        oldValue.endDate = endDate.format(dateFormat);
                                    }
                                }
                                value(oldValue);
                            });
                            $prevButton.text("prev").click(function (evt, ui) {
                                var $startDate = $container.find(".ntsStartDatePicker");
                                var $endDate = $container.find(".ntsEndDatePicker");
                                var oldValue = value();
                                var currentStart = $startDate.val();
                                var currentEnd = $endDate.val();
                                if (!nts.uk.util.isNullOrEmpty(currentStart)) {
                                    var startDate = moment(currentStart, dateFormat);
                                    if (startDate.isValid()) {
                                        var isEndOfMonth = startDate.daysInMonth() === startDate.date();
                                        startDate.month(startDate.month() - 1);
                                        if (isEndOfMonth) {
                                            startDate.endOf("month");
                                        }
                                        oldValue.startDate = startDate.format(dateFormat);
                                    }
                                }
                                if (!nts.uk.util.isNullOrEmpty(currentEnd)) {
                                    var endDate = moment(currentEnd, dateFormat);
                                    if (endDate.isValid()) {
                                        var isEndOfMonth = endDate.daysInMonth() === endDate.date();
                                        endDate.month(endDate.month() - 1);
                                        if (isEndOfMonth) {
                                            endDate.endOf("month");
                                        }
                                        oldValue.endDate = endDate.format(dateFormat);
                                    }
                                }
                                value(oldValue);
                            });
                        }
                        var $startDateArea = $datePickerArea.find(".ntsStartDate");
                        var $endDateArea = $datePickerArea.find(".ntsEndDate");
                        $startDateArea.append("<input id='" + id + "-startInput'  class='ntsDatepicker nts-input ntsStartDatePicker' />");
                        $endDateArea.append("<input id='" + id + "-endInput' class='ntsDatepicker nts-input ntsEndDatePicker' />");
                        var $input = $container.find(".ntsDatepicker");
                        $input.datepicker({
                            language: 'ja-JP',
                            format: ISOFormat,
                            autoHide: true,
                        });
                        dataName = nts.uk.util.isNullOrUndefined(dataName) ? "月日入力フォーム" : nts.uk.resource.getControlName(dataName);
                        var validator = new ui_15.validation.TimeValidator(dataName, "", { required: false, outputFormat: dateFormat, valueType: "string" });
                        var $ntsDateRange = $container.find(".ntsRangeLabel");
                        $input.on("change", function (e) {
                            var $target = $(e.target);
                            var newText = $target.val();
                            $target.ntsError('clear');
                            $ntsDateRange.ntsError("clear");
                            var result = validator.validate(newText);
                            var oldValue = value();
                            if ($target.hasClass("ntsStartDatePicker")) {
                                oldValue.startDate = result.isValid ? result.parsedValue : newText;
                            }
                            else {
                                oldValue.endDate = result.isValid ? result.parsedValue : newText;
                            }
                            if (nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true) {
                                $target.ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [dataName]));
                            }
                            else if (!result.isValid) {
                                $target.ntsError('set', result.errorMessage);
                            }
                            else if (!nts.uk.util.isNullOrEmpty(newText)) {
                                var startDate = moment(oldValue.startDate, dateFormat);
                                var endDate = moment(oldValue.endDate, dateFormat);
                                if (endDate.isBefore(startDate)) {
                                    $ntsDateRange.ntsError('set', "期間誤り");
                                }
                                else if (dateFormat === "YYYY/MM/DD" && maxRange === "oneMonth") {
                                    var start = parseInt(startDate.format("YYYYMMDD"));
                                    var end = parseInt(endDate.format("YYYYMMDD"));
                                    if (end - start > 31 || end - start < 0) {
                                        $ntsDateRange.ntsError('set', "最長期間違反");
                                    }
                                }
                            }
                            value(oldValue);
                        });
                        $input.on("blur", function (e) {
                            var newText = $(e.target).val();
                            if (nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true) {
                                $(e.target).ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [dataName]));
                            }
                            else {
                                var result = validator.validate(newText);
                                if (!result.isValid) {
                                    $(e.target).ntsError('set', result.errorMessage);
                                }
                            }
                        });
                        $input.on('validate', (function (e) {
                            var $target = $(e.target);
                            var newText = $target.val();
                            var result = validator.validate(newText);
                            $target.ntsError('clear');
                            $ntsDateRange.ntsError("clear");
                            if (nts.uk.util.isNullOrEmpty(newText) && $datePickerArea.data("required") === true) {
                                $target.ntsError('set', nts.uk.resource.getMessage('FND_E_REQ_INPUT', [dataName]));
                            }
                            else if (!result.isValid) {
                                $target.ntsError('set', result.errorMessage);
                            }
                            else if (!nts.uk.util.isNullOrEmpty(newText)) {
                                $ntsDateRange.ntsError("clear");
                                var startDate = moment(oldValue.startDate, dateFormat);
                                var endDate = moment(oldValue.endDate, dateFormat);
                                if (endDate.isBefore(startDate)) {
                                    $ntsDateRange.ntsError('set', "期間誤り");
                                }
                                else if (dateFormat === "YYYY/MM/DD" && maxRange === "oneMonth") {
                                    var start = parseInt(startDate.format("YYYYMMDD"));
                                    var end = parseInt(endDate.format("YYYYMMDD"));
                                    if (end - start > 31 || end - start < 0) {
                                        $ntsDateRange.ntsError('set', "最長期間違反");
                                    }
                                }
                            }
                        }));
                    };
                    NtsDateRangePickerBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var $container = $(element);
                        var dateType = ko.unwrap(data.type);
                        var maxRange = ko.unwrap(data.maxRange);
                        var dataName = ko.unwrap(data.name);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        var required = ko.unwrap(data.required);
                        var dateFormat = (dateType !== 'yearmonth') ? "YYYY/MM/DD" : 'YYYY/MM';
                        var ISOFormat = uk.text.getISOFormat(dateFormat);
                        ISOFormat = ISOFormat.replace(/d/g, "").trim();
                        var $input = $container.find(".ntsDatepicker");
                        var $startDate = $container.find(".ntsStartDatePicker");
                        var $endDate = $container.find(".ntsEndDatePicker");
                        if (!nts.uk.util.isNullOrUndefined(data.value())) {
                            var startDate = (data.value().startDate !== "") ? uk.time.formatPattern(data.value().startDate, dateFormat, ISOFormat) : "";
                            var oldStart = $startDate.val();
                            if (startDate !== oldStart) {
                                if (startDate !== "" && startDate !== "Invalid date") {
                                    $startDate.datepicker('setDate', startDate);
                                }
                                else {
                                    $startDate.val("");
                                }
                            }
                            var endDate = (data.value().endDate !== "") ? uk.time.formatPattern(data.value().endDate, dateFormat, ISOFormat) : "";
                            var oldEnd = $endDate.val();
                            if (endDate !== oldEnd) {
                                if (endDate !== "" && endDate !== "Invalid date") {
                                    $endDate.datepicker('setDate', endDate);
                                }
                                else {
                                    $endDate.val("");
                                }
                            }
                        }
                        $input.prop("disabled", !enable);
                        $container.find(".ntsDateRangeButton").prop("disabled", !enable);
                        var $datePickerArea = $container.find(".ntsDateRange_Container");
                        $datePickerArea.data("required", required);
                    };
                    return NtsDateRangePickerBindingHandler;
                }());
                ko.bindingHandlers['ntsDateRangePicker'] = new NtsDateRangePickerBindingHandler();
            })(koExtentions = ui_15.koExtentions || (ui_15.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsLinkButtonBindingHandler = (function () {
                    function NtsLinkButtonBindingHandler() {
                    }
                    NtsLinkButtonBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var jump = ko.unwrap(data.jump);
                        var action = data.action;
                        var linkText = $(element).text();
                        var $linkButton = $(element).wrap('<div class="ntsControl"/>')
                            .addClass('link-button')
                            .click(function () {
                            event.preventDefault();
                            if (!nts.uk.util.isNullOrUndefined(action))
                                action.call(viewModel);
                            else if (!nts.uk.util.isNullOrUndefined(jump))
                                nts.uk.request.jump(jump);
                        });
                    };
                    NtsLinkButtonBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                    };
                    return NtsLinkButtonBindingHandler;
                }());
                ko.bindingHandlers['ntsLinkButton'] = new NtsLinkButtonBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsColorPickerBindingHandler = (function () {
                    function NtsColorPickerBindingHandler() {
                    }
                    NtsColorPickerBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var $container = $(element);
                        var width = ko.unwrap(data.width);
                        var color = ko.unwrap(data.value);
                        var dataName = ko.unwrap(data.name);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        var tag = $container.prop("tagName").toLowerCase();
                        var $picker;
                        if (tag === "input") {
                            $picker = $container;
                            $picker.appendTo("<div class='ntsControl ntsColorPicker_Container'/>");
                        }
                        else if (tag === 'div') {
                            $container.addClass("ntsControl ntsColorPicker_Container");
                            $container.append("<input class='ntsColorPicker'/>");
                            $picker = $container.find(".ntsColorPicker");
                        }
                        else {
                            $container.appendTo("<div class='ntsControl ntsColorPicker_Container'/>");
                            $container.hide();
                            $container = $container.parent();
                            $container.append("<input class='ntsColorPicker'/");
                            $picker = $container.find(".ntsColorPicker");
                        }
                        $picker.addClass("ntsColorPicker").attr("data-name", dataName);
                        $picker.spectrum({
                            preferredFormat: "name",
                            showPaletteOnly: true,
                            togglePaletteOnly: true,
                            togglePaletteMoreText: '強化',
                            togglePaletteLessText: '略す',
                            color: color,
                            disabled: !enable,
                            showInput: true,
                            showSelectionPalette: true,
                            showInitial: true,
                            chooseText: "確定",
                            cancelText: "キャンセル",
                            allowEmpty: true,
                            showAlpha: true,
                            palette: [
                                ["#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff"],
                                ["#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f"],
                                ["#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                                ["#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                                ["#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                                ["#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79"],
                                ["#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47"],
                                ["#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130"]
                            ],
                            change: function (color) {
                                if (!nts.uk.util.isNullOrUndefined(color) && !nts.uk.util.isNullOrUndefined(data.value)) {
                                    data.value(color.toHexString());
                                }
                            }
                        });
                        if (!nts.uk.util.isNullOrUndefined(width) && nts.uk.ntsNumber.isNumber(width)) {
                            $container.width(width);
                            $container.find(".sp-replacer").width(width - 10);
                            $container.find(".sp-preview").width(width - 30);
                        }
                    };
                    NtsColorPickerBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var tag = $(element).prop("tagName").toLowerCase();
                        var $picker;
                        if (tag === "input") {
                            $picker = $(element);
                        }
                        else if (tag === 'div') {
                            $picker = $(element).find(".ntsColorPicker");
                        }
                        else {
                            $picker = $(element).parent().find(".ntsColorPicker");
                            $(element).hide();
                        }
                        var colorCode = ko.unwrap(data.value);
                        var enable = data.enable === undefined ? true : ko.unwrap(data.enable);
                        $picker.spectrum("set", colorCode);
                        if (enable !== false) {
                            $picker.spectrum("enable");
                        }
                        else {
                            $picker.spectrum("disable");
                        }
                    };
                    return NtsColorPickerBindingHandler;
                }());
                ko.bindingHandlers['ntsColorPicker'] = new NtsColorPickerBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsFileUploadBindingHandler = (function () {
                    function NtsFileUploadBindingHandler() {
                    }
                    NtsFileUploadBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var fileName = data.filename;
                        var suportedExtension = ko.unwrap(data.accept);
                        var textId = ko.unwrap(data.text);
                        var control = $(element);
                        var onchange = data.onchange;
                        var onfilenameclick = data.onfilenameclick;
                        var fileuploadContainer = $("<div class='nts-fileupload-container'></div>");
                        var fileBrowserButton = $("<button class='browser-button' ></button>");
                        var browserButtonText;
                        if (textId) {
                            browserButtonText = nts.uk.resource.getText(textId);
                        }
                        else {
                            browserButtonText = "ファイルアップロード";
                        }
                        fileBrowserButton.text(browserButtonText);
                        var fileNameLable = $("<span class='filename'></span> ");
                        var fileInput = $("<input style ='display:none' type='file' class='fileinput'/>");
                        if (suportedExtension) {
                            fileInput.attr("accept", suportedExtension.toString());
                        }
                        fileuploadContainer.append(fileBrowserButton);
                        fileuploadContainer.append(fileNameLable);
                        fileuploadContainer.append(fileInput);
                        fileuploadContainer.appendTo(control);
                        fileInput.change(function () {
                            if (fileName != undefined) {
                                data.filename($(this).val());
                            }
                            else {
                                fileNameLable.text($(this).val());
                            }
                            if (typeof onchange == 'function') {
                                onchange($(this).val());
                            }
                        });
                        fileBrowserButton.click(function () {
                            fileInput.click();
                        });
                        if (onfilenameclick) {
                            fileNameLable.click(function () {
                                onfilenameclick($(this).text());
                            });
                        }
                    };
                    NtsFileUploadBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var fileName = ko.unwrap(data.filename);
                        var control = $(element);
                        var fileNameLable = control.parent().find(".filename");
                        fileNameLable.text(fileName);
                    };
                    return NtsFileUploadBindingHandler;
                }());
                ko.bindingHandlers['ntsFileUpload'] = new NtsFileUploadBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var koExtentions;
            (function (koExtentions) {
                var NtsHelpButtonBindingHandler = (function () {
                    function NtsHelpButtonBindingHandler() {
                    }
                    NtsHelpButtonBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var image = ko.unwrap(data.image);
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var position = ko.unwrap(data.position);
                        var myPositions = position.replace(/[^a-zA-Z ]/gmi, "").split(" ");
                        var atPositions = position.split(" ");
                        var operator = 1;
                        var marginDirection = "";
                        var caretDirection = "";
                        var caretPosition = "";
                        if (myPositions[0].search(/(top|left)/i) !== -1) {
                            operator = -1;
                        }
                        if (myPositions[0].search(/(left|right)/i) === -1) {
                            atPositions[0] = atPositions.splice(1, 1, atPositions[0])[0];
                            myPositions[0] = myPositions.splice(1, 1, myPositions[0])[0];
                            caretDirection = myPositions[1] = uk.text.reverseDirection(myPositions[1]);
                            caretPosition = "left";
                            marginDirection = "margin-top";
                        }
                        else {
                            caretDirection = myPositions[0] = uk.text.reverseDirection(myPositions[0]);
                            caretPosition = "top";
                            marginDirection = "margin-left";
                        }
                        $(element).on("click", function () {
                            if ($popup.is(":visible")) {
                                $popup.hide();
                            }
                            else {
                                var CARET_WIDTH = parseFloat($caret.css("font-size")) * 2;
                                $popup.show()
                                    .css(marginDirection, 0)
                                    .position({
                                    my: myPositions[0] + " " + myPositions[1],
                                    at: atPositions[0] + " " + atPositions[1],
                                    of: $(element),
                                    collision: "none"
                                })
                                    .css(marginDirection, CARET_WIDTH * operator);
                                $caret.css(caretPosition, parseFloat($popup.css(caretPosition)) * -1);
                            }
                        }).wrap($("<div class='ntsControl ntsHelpButton'></div>"));
                        var $container = $(element).closest(".ntsHelpButton");
                        var $caret = $("<span class='caret-helpbutton caret-" + caretDirection + "'></span>");
                        var $popup = $("<div class='nts-help-button-image'></div>")
                            .append($caret)
                            .append($("<img src='" + uk.request.resolvePath(image) + "' />"))
                            .appendTo($container).hide();
                        $("html").on("click", function (event) {
                            if (!$container.is(event.target) && $container.has(event.target).length === 0) {
                                $container.find(".nts-help-button-image").hide();
                            }
                        });
                    };
                    NtsHelpButtonBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        (enable === true) ? $(element).removeAttr("disabled") : $(element).attr("disabled", "disabled");
                    };
                    return NtsHelpButtonBindingHandler;
                }());
                ko.bindingHandlers['ntsHelpButton'] = new NtsHelpButtonBindingHandler();
            })(koExtentions = ui.koExtentions || (ui.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_16) {
            var koExtentions;
            (function (koExtentions) {
                var NtsFunctionPanelBindingHandler = (function () {
                    function NtsFunctionPanelBindingHandler() {
                    }
                    NtsFunctionPanelBindingHandler.prototype.init = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var width = (data.width !== undefined) ? ko.unwrap(data.width) : 100;
                        var headerText = (data.headerText !== undefined) ? ko.unwrap(data.headerText) : "";
                        var items = (data.dataSource !== undefined) ? ko.unwrap(data.dataSource) : [];
                        var container = $(element);
                        if (nts.uk.util.isNullOrEmpty(container.attr("id"))) {
                            container.attr("id", nts.uk.util.randomId());
                        }
                        container.width(width);
                        container.addClass("ntsControl ntsFunctionPanel").on("click", function (e) {
                            if (container.data("readonly") === true)
                                e.preventDefault();
                        });
                        container.append("<div class='function-header' /><div class='function-items'/>");
                        var header = container.find('.function-header');
                        header.append("<div class='function-icon'/><div class='function-link'><a class='header-link function-item'>" + headerText + "</a></div>");
                        var itemAreas = container.find('.function-items');
                        header.find(".function-item").click(function (evt, ui) {
                            var current = $(this);
                            if ($(this).data("dbClick") === false) {
                                itemAreas.find(".function-item-container").hide("fast", function () {
                                    current.data("dbClick", true);
                                });
                            }
                            else {
                                itemAreas.find(".function-item-container").show("fast", "linear", function () {
                                    current.data("dbClick", false);
                                });
                            }
                        });
                        container.mouseleave(function (evt, ui) {
                            var current = header.find(".function-item");
                            itemAreas.find(".function-item-container").hide("fast", function () {
                                current.data("dbClick", true);
                            });
                        });
                    };
                    NtsFunctionPanelBindingHandler.prototype.update = function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
                        var data = valueAccessor();
                        var enable = (data.enable !== undefined) ? ko.unwrap(data.enable) : true;
                        var readonly = (data.readonly !== undefined) ? ko.unwrap(data.readonly) : true;
                        var width = (data.width !== undefined) ? ko.unwrap(data.width) : 100;
                        var headerText = (data.headerText !== undefined) ? ko.unwrap(data.headerText) : "";
                        var items = (data.dataSource !== undefined) ? ko.unwrap(data.dataSource) : [];
                        var container = $(element);
                        var itemAreas = container.find('.function-items');
                        var headerLink = container.find('.header-link');
                        var containerId = container.attr("id");
                        headerLink.text(headerText);
                        itemAreas.empty();
                        _.forEach(items, function (item, idx) {
                            var div = $("<div class='function-item-container' />");
                            div.attr("data-idx", idx);
                            div.width(width);
                            div.append("<div class='function-icon'/><div class='function-link'/>");
                            var itemLink = $("<a id='" + (containerId + '-' + idx) + "' class='function-item'>" + item["text"] + "</a>");
                            itemLink.click(item["action"]);
                            itemLink.appendTo(div.find(".function-link"));
                            var icon = $("<img class='ft-icon' src='" + item["icon"] + "'/>");
                            icon.appendTo(div.find(".function-icon"));
                            div.appendTo(itemAreas);
                        });
                        container.find(".function-item-container").hide();
                    };
                    return NtsFunctionPanelBindingHandler;
                }());
                ko.bindingHandlers['ntsFunctionPanel'] = new NtsFunctionPanelBindingHandler();
            })(koExtentions = ui_16.koExtentions || (ui_16.koExtentions = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui) {
            var sharedvm;
            (function (sharedvm) {
                var KibanTimer = (function () {
                    function KibanTimer(target) {
                        var self = this;
                        self.elapsedSeconds = 0;
                        self.formatted = ko.observable(uk.time.formatSeconds(this.elapsedSeconds, 'hh:mm:ss'));
                        self.targetComponent = target;
                        self.isTimerStart = ko.observable(false);
                        self.oldDated = ko.observable(undefined);
                        document.getElementById(self.targetComponent).innerHTML = self.formatted();
                    }
                    KibanTimer.prototype.run = function (timer) {
                        var x = new Date().getTime() - timer.oldDated().getTime();
                        x = Math.floor(x / 1000);
                        timer.elapsedSeconds = x;
                        document.getElementById(timer.targetComponent).innerHTML
                            = uk.time.formatSeconds(x, 'hh:mm:ss');
                    };
                    KibanTimer.prototype.start = function () {
                        var self = this;
                        if (!self.isTimerStart()) {
                            self.oldDated(new Date());
                            self.isTimerStart(true);
                            self.interval = setInterval(self.run, 1000, self);
                        }
                    };
                    KibanTimer.prototype.end = function () {
                        var self = this;
                        if (self.isTimerStart()) {
                            self.oldDated(undefined);
                            self.isTimerStart(false);
                            clearInterval(self.interval);
                        }
                    };
                    return KibanTimer;
                }());
                sharedvm.KibanTimer = KibanTimer;
            })(sharedvm = ui.sharedvm || (ui.sharedvm = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=nts.uk.com.web.nittsu.bundles.js.map