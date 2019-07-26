module nts.uk {

    export module KeyCodes {
        export const Tab = 9;
        export const Enter = 13;
        export const Ctrl = 17;
        export const NotValueKeys = [9, 16, 17, 18, 19, 20, 27, 33, 34, 35, 36, 37, 38, 39,
                    40, 91, 92, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 144, 145];        
    }

    export module util {
        export function compare(obj1:any,obj2:any){
            for(var p in obj1){
                if(obj1.hasOwnProperty(p)!== obj2.hasOwnProperty(p)){return false;}
                switch(typeof(obj1[p])){
                    case "object":
                        if(!compare(obj1[p],obj2[p])) return false;
                        break;
                    case "function":
                        
                        break;
                    default:
                        if(obj1[p]!==obj2[p]){
                            return false;    
                        }
                }   
            }
            for(var p in obj2){
                if(typeof(obj1[p])=='undefined') return false;    
            }
            
            return true;
        }
        /**
         * 常にtrueを返す関数が必要になったらこれ
         */
        export function alwaysTrue() {
            return true;
        }
        /**
         * function find an item index in array
         * if key presented will perform find index of item in array which contain key equal to the 'item' parameter
         */
        export function findIndex(arr, value, key) {
            for (var i = 0; i < arr.length; i++) {
                var item = arr[i];
                if (item[key] === value) return i;
            }
            return -1;
        }
        /** function add item to array, this function is used in combine with visitDfs function
         * visitDfs(node, addToArray, childField, arr) will return flatArray by DFS order, start by node and following by each child belong to it.
         */
        function addToArray(node, arr) {
            arr.push(node)
        }
        /**
         * DFS algorithm function to iterate over an object with structre like tree
         */
        export function visitDfs(node, func, childField, arr?) {
            if (func) {
                if (arr)
                    func(node, arr);
                else func(node);
            }
            var childs = node[childField];
            $.each(childs, function(child) {
                visitDfs(childs[child], func, childField, arr);
            });
        }
        /**
         * return flatern array of array of tree-like objects
         */
        export function flatArray(arr, childField) {
            var flatArr = [];
            if (!childField) return arr;
            for (var i = 0; i < arr.length; i++) {
                var item = arr[i];
                visitDfs(item, addToArray, childField, flatArr);
            }
            return flatArr;
        }
        /**
         * return filtered array
         * @param {Array} array of items
         * @param {String} user input
         * @param {Array} array of fields used to search on
         * @param {String} if not null, search will perform in flatarray of arr
         */
        export function searchArray(arr, searchTerm, fields, childField) {
            //if items is empty return empty array
            if (!arr) {
                return [];
            }
            if (!(searchTerm instanceof String)) {
                searchTerm = "" + searchTerm;
            }
            var flatArr = flatArray(arr, childField);
            var filter = searchTerm.toLowerCase();
            //if filter is empty return all the items
            if (!filter) {
                return flatArr;
            }
            //filter data
            var filtered = flatArr.filter(function(item) {
                var i = fields.length;
                while (i--) {
                    var prop = fields[i];
                    var strProp = ("" + item[prop]).toLocaleLowerCase();
                    if (strProp.indexOf(filter) !== -1) {
                        return true;
                    };
                }
                return false;
            });
            return filtered;
        }
        /**
         * SearchBox helper function to jump next search
         */
        export function nextSelectionSearch(selected, arr, selectedKey, isArray) {
            var current = null;
            if (isArray) {
                if (selected.length > 0) current = selected[0];
            } else if (selected !== undefined && selected !== '' && selected !== null) {
                current = selected;
            }
            if (arr.length > 0) {
                if (current) {
                    for (var i = 0; i < arr.length - 1; i++) {
                        var item = arr[i];
                        if (item[selectedKey] === current) return arr[i + 1][selectedKey];
                    }
                }
                if (selectedKey) return arr[0][selectedKey];
                return arr[0];
            }
            return undefined;
        }

        /**
         * Returns true if the target is null or undefined.
         */
        export function isNullOrUndefined(target: any): boolean {
            return target === null || target === undefined;
        }

        /**
         * Returns true if the target is null or undefined or blank.
         * @param  {any} [target] Target need to check
         * @return {boolean}      True for blank
         */
        export function isNullOrEmpty(target: any): boolean {
            return (target === undefined || target === null || target.length == 0);
        }

        /**
         * Generate random identifier string (UUIDv4)
         */
        export function randomId() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random() * 16 | 0;
                return ((c == 'x') ? r : (r & 0x3 | 0x8)).toString(16);
            });
        }

        /**
         * Returns true if current window is in frame.
         */
        export function isInFrame() {
            return window.parent != window;
        }
        
        /**
         * valueMaybeEmptyがnullまたはundefinedの場合、defaultValueを返す。
         * そうでなければ、valueMaybeEmptyを返す。
         */
        export function orDefault(valueMaybeEmpty: any, defaultValue: any) {
            return isNullOrUndefined(valueMaybeEmpty) ? defaultValue : valueMaybeEmpty;
        }
        
        export function getConstraintMes(primitiveValues: any) {
            if(isNullOrEmpty(primitiveValues)) {
                return "";        
            }
            if (!Array.isArray(primitiveValues))
                primitiveValues = [primitiveValues];
            let constraintText: string = "";
            _.forEach(primitiveValues, function(primitiveValue) {
                let constraint = __viewContext.primitiveValueConstraints[primitiveValue];
                switch (constraint.valueType) {
                    case 'String':
                        constraintText += (constraintText.length > 0) ? "/" : "";
                        constraintText += uk.text.getCharType(primitiveValue).buildConstraintText(constraint.maxLength);
                        break;
                    case 'Decimal':
                    case 'Integer':
                    case 'Date':
                    case 'Time':
                    case 'Clock':
                    case 'Duration': // ValidatorScriptではない。DynamicConstraintで使う？
                    case 'TimePoint': // ValidatorScriptではない。DynamicConstraintで使う？
                        constraintText += (constraintText.length > 0) ? "/" : "";
                        constraintText += constraint.min + "～" + constraint.max; 
                        break;
                    default:
                        constraintText += 'ERROR';
                        break;
                }
            });
            return constraintText;
        }
        
        export function getConstraintLabel(primitiveValues: any){
            let constraintText = getConstraintMes(primitiveValues);    
            
            return "<span class='constraint-label'>(" + constraintText + ")</span>";
        } 

        /**
         * Returns true if expects contains actual.
         */
        export function isIn(actual: any, expects: Array<any>) {
            for (var i = 0; i < expects.length; i++) {
                if (actual === expects[i]) return true;
            }
            return false;
        };

        export function createTreeFromString(original: string, openChar: string, closeChar: string,
            seperatorChar: string, operatorChar: Array<string>): Array<TreeObject>[] {
            let result = convertToTree(original, openChar, closeChar, seperatorChar, 1, operatorChar).result;
            //            result = moveToParentIfEmpty(result);
            return result;
        }

        function moveToParentIfEmpty(tree: Array<TreeObject>[]): Array<TreeObject>[] {
            let result = [];
            _.forEach(tree, function(e: TreeObject) {
                if (e.children.length > 0) {
                    e.children = moveToParentIfEmpty(e.children);
                    if (text.isNullOrEmpty(e.value)) {
                        result = result.concat(e.children);
                    } else {
                        result.push(e);
                    }
                } else {
                    result.push(e);
                }
            })
            return result;
        }

        function convertToTree(original: string, openChar: string, closeChar: string, separatorChar: string, index: number, operatorChar: Array<string>)
            : { "result": Array<TreeObject>[], "index": number } {
            let result = [];
            while (original.trim().length > 0) {
                let firstOpenIndex = original.indexOf(openChar);
                if (firstOpenIndex < 0) {
                    let values = original.split(separatorChar);
                    _.forEach(values, function(value) {
                        let data = splitByArray(value, operatorChar.slice());
                        _.each(data, function(v) {
                            let object = new TreeObject();
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
                } else {
                    let object = new TreeObject();
                    object.value = original.substring(0, firstOpenIndex).trim();
                    object.index = index;
                    let closeIndex = findIndexOfCloseChar(original, openChar, closeChar, firstOpenIndex);
                    if (closeIndex >= 0) {
                        index++;
                        let res = convertToTree(original.substring(firstOpenIndex + 1, closeIndex).trim(), openChar, closeChar, separatorChar, index, operatorChar);
                        object.children = res.result;
                        index = res.index++;
                        result.push(object);
                        let firstSeperatorIndex = original.indexOf(separatorChar, closeIndex);
                        if (firstSeperatorIndex >= 0) {
                            original = original.substring(firstSeperatorIndex + 1, original.length).trim();
                        } else {
                            return {
                                "result": result,
                                "index": index
                            };
                        }
                    } else {
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

        function splitByArray(original: string, operatorChar: Array<string>) {
            let temp = [];
            let result = [];
            if (original.trim().length <= 0) {
                return temp;
            }
            if (operatorChar.length <= 0) {
                return [original];
            }
            let operator: string = operatorChar.shift();
            while (original.trim().length > 0) {
                let index = original.indexOf(operator);
                if (index >= 0) {
                    temp.push(original.substring(0, index).trim());
                    temp.push(original.substring(index, index + 1).trim());
                    original = original.substring(index + 1, original.length).trim()
                } else {
                    temp.push(original);
                    break;
                }
            }
            _.each(temp, function(value) {
                result = result.concat(splitByArray(value, operatorChar));
            });
            return result;
        }

        function findIndexOfCloseChar(original: string, openChar: string, closeChar: string, firstOpenIndex: number): number {
            let openCount = 0;
            let closeCount = 0;
            for (var i = firstOpenIndex; i < original.length; i++) {
                if (original.charAt(i) === openChar) {
                    openCount++;
                } else if (original.charAt(i) === closeChar) {
                    closeCount++;
                }
                if (openCount > 0 && openCount === closeCount) {
                    return i;
                }
            }

            return -1;
        }

        export class TreeObject {
            value: string;
            isOperator: boolean;
            children: Array<TreeObject>[];
            index: number;

            constructor(value?: string, children?: Array<TreeObject>[], index?: number, isOperator?: boolean) {
                var self = this;

                self.value = value;
                self.children = children;
                self.index = index;
                self.isOperator = isOperator;
            }
        }

        /**
         * Like Java Optional
         */
        export module optional {
            export function of<V>(value: V) {
                return new Optional(value);
            }

            export function empty() {
                return new Optional(null);
            }

            export class Optional<V> {
                value: V;

                constructor(value: V) {
                    this.value = orDefault(value, null);
                }

                ifPresent(consumer: (value: V) => void) {
                    if (this.isPresent()) {
                        consumer(this.value);
                    }
                    return this;
                }

                ifEmpty(action: () => void) {
                    if (!this.isPresent()) {
                        action();
                    }
                    return this;
                }

                map<M>(mapper: (value: V) => M): Optional<M> {
                    return this.isPresent() ? of(mapper(this.value)) : empty();
                }

                isPresent(): boolean {
                    return this.value !== null;
                }

                get(): V {
                    if (!this.isPresent()) {
                        throw new Error('not present');
                    }
                    return this.value;
                }

                orElse(stead: V): V {
                    return this.isPresent() ? this.value : stead;
                }

                orElseThrow(errorBuilder: () => Error) {
                    if (!this.isPresent()) {
                        throw errorBuilder();
                    }
                }
            }
        }

        export class Range {
            start: number;
            end: number;

            constructor(start: number, end: number) {
                if (start > end) {
                    throw new Error('start is larger than end');
                }

                this.start = start;
                this.end = end;
            }

            contains(value: number) {
                return this.start <= value && value <= this.end;
            }

            greaterThan(value: number) {
                return value < this.start;
            }

            greaterThanOrEqualTo(value: number) {
                return value <= this.start;
            }

            lessThan(value: number) {
                return this.end < value;
            }

            lessThanOrEqualTo(value: number) {
                return this.end <= value;
            }

            distanceFrom(value: number) {
                if (this.greaterThan(value)) {
                    return value - this.start;
                } else if (this.lessThan(value)) {
                    return value - this.end;
                } else {
                    return 0;
                }
            }
        }

        export module value {

            export function reset($controls: JQuery, defaultVal?: any, immediateApply?: boolean) {
                var resetEvent = new CustomEvent(DefaultValue.RESET_EVT, {
                    detail: {
                        value: defaultVal,
                        immediateApply: immediateApply === undefined ? true : immediateApply
                    }
                });
                _.forEach($controls, function(control) {
                    control.dispatchEvent(resetEvent);
                });
            }

            export class DefaultValue {
                static RESET_EVT: string = "reset";
                onReset($control: JQuery, koValue: (data?: any) => any) {
                    var self = this;
                    $control.addClass("reset-element");
                    $control.on(DefaultValue.RESET_EVT, function(e: any) {
                        var param = e.detail;
                        self.asDefault($(this), koValue, param.value, param.immediateApply);
                    });
                    return this;
                }

                applyReset($control: JQuery, koValue: (data?: any) => any): any {
                    var defaultVal = _.cloneDeep($control.data("default"));
                    var isDirty = defaultVal !== koValue();
                    if ($control.ntsError("hasError")) $control.ntsError("clear");
                    if (defaultVal !== undefined && isDirty) setTimeout(() => koValue(defaultVal), 0);
                    return { isDirty: isDirty };
                }

                asDefault($control: JQuery, koValue: (data?: any) => any, defaultValue: any, immediateApply: boolean) {
                    var defaultVal = defaultValue !== undefined ? defaultValue : koValue();
                    $control.data("default", defaultVal);
                    if (immediateApply) this.applyReset($control, koValue);
                }
            }
        }
        
        export module accessor {
            export function defineInto(obj: any): AccessorDefine {
                return new AccessorDefine(obj);
            }
            
            export class AccessorDefine {
                obj: any;
                constructor(obj: any) {
                    this.obj = obj;
                }
                
                get(name: string, func: () => any) {
                    Object.defineProperty(this.obj, name, { get: func, configurable: true });
                    return this;
                }
            }
        }
        
        export module exception {
            export function isBundledBusinessErrors(exception: any): boolean {
                return !isNullOrUndefined(exception) && ($.isArray(exception["errors"]) 
                                            && exception["businessException"]);
            }    
            
            export function isErrorToReject(res: any) : boolean{
                return !isNullOrUndefined(res) && (res.businessException || res.optimisticLock);
            }
            
            export function isBusinessError(res: any) : boolean{
                return !isNullOrUndefined(res) && (res.businessException);
            }
        }
    }

    export class WebStorageWrapper {

        nativeStorage: Storage;

        constructor(nativeStorage: Storage) {
            this.nativeStorage = nativeStorage;
        }

        setItem(key: string, value: string) {
            if (value === undefined) {
                return;
            }
            this.nativeStorage.setItem(key, value);
        }

        setItemAsJson(key: string, value: any) {
            this.setItem(key, JSON.stringify(value));
        }
        
        containsKey(key: string) {
            return this.getItem(key).isPresent();
        };

        getItem(key: string): util.optional.Optional<string> {
            var value: string = this.nativeStorage.getItem(key);
            if (value === null || value === undefined || value === 'undefined') {
                return util.optional.empty();
            }
            return util.optional.of(value);
        }

        getItemAndRemove(key: string) {
            var item = this.getItem(key);
            this.removeItem(key);
            return item;
        }

        removeItem(key: string) {
            this.nativeStorage.removeItem(key);
        }

        clear() {
            this.nativeStorage.clear();
        }
    }


    /**
     * Utilities about jquery deferred
     */
    export module deferred {

        /**
         * Repeats a task with jQuery Deferred
         */
        export function repeat(configurator: (conf: repeater.IConfiguration) => void) {
            var conf = repeater.createConfiguration();
            configurator(conf);
            return repeater.begin(conf);
        }

        export module repeater {
            export function begin(conf: IConfiguration) {
                return (<Configuration>conf).run();
            }

            export interface IConfiguration {
                /**
                 * Set task returns JQueryPromise.
                 */
                task(taskFunction: () => JQueryPromise<any>): IConfiguration;

                /**
                 * Set condition to repeat task.
                 */
                while(whileCondition: (taskResult: any) => boolean): IConfiguration;

                after(runAfterMilliseconds: number): IConfiguration;

                /**
                 * Set pause time as milliseconds.
                 */
                pause(pauseMilliseconds: number): IConfiguration;
            }

            export function createConfiguration(): IConfiguration {
                return new Configuration();
            }

            class Configuration implements IConfiguration {
                taskFunction: () => JQueryPromise<any>;
                whileCondition: (taskResult: any) => boolean;
                pauseMilliseconds = 0;
                runAfter = 0;

                task(taskFunction: () => JQueryDeferred<any>) {
                    this.taskFunction = taskFunction;
                    return this;
                }

                while(whileCondition: (taskResult: any) => boolean) {
                    this.whileCondition = whileCondition;
                    return this;
                }

                pause(pauseMilliseconds: number) {
                    this.pauseMilliseconds = pauseMilliseconds;
                    return this;
                }

                after(runAfterMilliseconds: number) {
                    this.runAfter = runAfterMilliseconds;
                    return this;
                }

                run() {
                    let dfd = $.Deferred();
                    if (this.runAfter > 0) {
                        setTimeout(() => this.repeat(dfd), this.runAfter);
                    } else {
                        this.repeat(dfd);
                    }
                    return dfd.promise();
                }

                repeat(dfd: JQueryDeferred<any>) {
                    this.taskFunction().done(res => {
                        if (this.whileCondition(res)) {
                            setTimeout(() => this.repeat(dfd), this.pauseMilliseconds);
                        } else {
                            dfd.resolve(res);
                        }
                    }).fail(res => {
                        dfd.reject(res);
                    });
                }
            }
        }
    }
     
    
    export module resource {
        
        var names = window['names'] || {};
        var messages = window['messages'] || {};

        export function getText(code: string, params?: any[]): string {
            let text = names[code];
            if (!_.isNil(text)) {
                text = formatParams(text, params);
                text = formatCompCustomizeResource(text);
                return text.replace(/\\r\\n/g, '\r\n');
            }
            return code;
        }

        export function getMessage(messageId: string, params?: any[]): string {
            let message = messages[messageId];
            if (!message) {
                let responseText="";
                nts.uk.request.syncAjax("com", "i18n/resources/rawcontent/" + messageId).done(function(res) {
                    responseText=res;
                }).fail(function() {
                });
                message = responseText;
                messages[messageId] = message;
            }
            message = formatParams(message, params);
            message = formatCompCustomizeResource(message);
			
            return message.replace(/\\r\\n/g, '\r\n');
        }
        function formatCompCustomizeResource(message: string) {
            let compDependceParamRegex = /{#(\w*)}/;
            let matches: string[];
            while (matches = compDependceParamRegex.exec(message)) {
                let code = matches[1];
                let text = getText(code);
                message = message.replace(compDependceParamRegex, text);
            }
            return message;
        }
        function formatParams(message: string, args: string[]) {
            if (args == null || args.length == 0) return message;
            let paramRegex = /{([0-9])+(:\w+)?}/;
            let matches: string[];
            let formatter = time.getFormatter();
            while (matches = paramRegex.exec(message)) {
                let code = matches[1];
                let text = args[parseInt(code)];
                //                if(text!=undefined && text.indexOf("#")==0){
                //                    text = getText(text.substring(1))
                //                }
                let param = matches[2];
                if (param !== undefined && formatter !== undefined) {
                    text = time.applyFormat(param.substring(1), text, formatter);
                }
                message = message.replace(paramRegex, text);
            }
            return message;
        }

        export function getControlName(name: string): string {
            var hashIdx = name.indexOf("#");
            if (hashIdx !== 0) return name;
            var names = name.substring(hashIdx + 2, name.length - 1).split(",");
            if (names.length > 1) {
                let params: Array<string> = new Array<string>();
                _.forEach(names, function(n: string, idx: number) {
                    if (idx === 0) return true;
                    params.push(getText(n.trim()));
                });
                return getText(names[0], params);
            }
            return getText(names[0]);
        }

    }

    export var sessionStorage = new WebStorageWrapper(window.sessionStorage);
    export var localStorage = new WebStorageWrapper(window.localStorage);

    export module characteristics {

        /**
         * Now, "characteristic data" is saved in Local Storage.
         * In the future, the data may be saved in DB using Ajax.
         * So these APIs have jQuery Deferred Interface to support asynchronous. 
         */

        let delayToEmulateAjax = 100;
        function convertObjectToArray(key:any):string{
            var result=[];
            for(var p in key){
                result.push([p,key[p]]);
            }
            result.sort(function(a,b){
                return (a > b) ? 1 : (a < b) ? -1 : 0;    
            });
            return result.toString();
        }    
        
        export function saveByObjectKey(key: any, value: any){
            return save(convertObjectToArray(key),value);
        }
        
        export function restoreByObjectKey(key:any): JQueryPromise<any>{
            return restore(convertObjectToArray(key));    
        }
        
        export function save(key: string, value: any) {
            let dfd = $.Deferred();

            setTimeout(() => {
                localStorage.setItemAsJson(createKey(key), value);
                dfd.resolve();
            }, delayToEmulateAjax);

            return dfd.promise();
        }

        export function restore(key: string): JQueryPromise<any> {
            let dfd = $.Deferred();

            setTimeout(() => {
                let value = localStorage.getItem(createKey(key))
                    .map(v => JSON.parse(v)).orElse(undefined);
                dfd.resolve(value);
            }, delayToEmulateAjax);

            return dfd.promise();
        }

        export function remove(key: string) {
            let dfd = $.Deferred();

            setTimeout(() => {
                localStorage.removeItem(createKey(key));
                dfd.resolve();
            }, delayToEmulateAjax);

            return dfd.promise();
        }

        function createKey(key: string): string {
            return 'nts.uk.characteristics.' + key;
        }
    }
     
    export module types {
        
        export function matchArguments(values: any[], types: string[]) {
            if (values.length !== types.length) {
                return false;
            }
            
            for (var i = 0; i < values.length; i++) {
                if (typeof values[i] !== types[i]) return false;
            }
            
            return true;
        }
    }
     
    export module cookie {
        
        export function get(name: string) {
            let value = asMap()[name];
            return util.optional.of(value);
        }
        
        export function remove(name: string, attr: any) {
            document.cookie = name + "=; path=" + attr.path + "; max-age=0";
        }
        
        export function asMap(): any {
            let map = {};
            document.cookie.split(";")
                .forEach(item => {
                    let positionOfDelimiter = item.indexOf("=");
                    let name = item.slice(0, positionOfDelimiter).trim();
                    map[name] = item.slice(positionOfDelimiter + 1);
                });
            
            return map;
        }
    }
}

