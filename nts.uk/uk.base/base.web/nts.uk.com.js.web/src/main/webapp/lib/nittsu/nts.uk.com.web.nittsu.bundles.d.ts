
declare module nts.uk.ntsNumber {
    function isNumber(value: any, isDecimalValue?: boolean, option?: any): boolean;
    var trunc: (value: number) => number;
    function getDecimal(value: any, scale: number): number;
    function formatNumber(value: any, formatOption: any): any;
}
declare module nts.uk {
    module KeyCodes {
        const Tab = 9;
    }
    module util {
        /**
         * 常にtrueを返す関数が必要になったらこれ
         */
        function alwaysTrue(): boolean;
        /**
         * function find an item index in array
         * if key presented will perform find index of item in array which contain key equal to the 'item' parameter
         */
        function findIndex(arr: any, value: any, key: any): number;
        /**
         * DFS algorithm function to iterate over an object with structre like tree
         */
        function visitDfs(node: any, func: any, childField: any, arr?: any): void;
        /**
         * return flatern array of array of tree-like objects
         */
        function flatArray(arr: any, childField: any): any;
        /**
         * return filtered array
         * @param {Array} array of items
         * @param {String} user input
         * @param {Array} array of fields used to search on
         * @param {String} if not null, search will perform in flatarray of arr
         */
        function searchArray(arr: any, searchTerm: any, fields: any, childField: any): any;
        /**
         * SearchBox helper function to jump next search
         */
        function nextSelectionSearch(selected: any, arr: any, selectedKey: any, isArray: any): any;
        /**
         * Returns true if the target is null or undefined.
         */
        function isNullOrUndefined(target: any): boolean;
        /**
         * Returns true if the target is null or undefined or blank.
         * @param  {any} [target] Target need to check
         * @return {boolean}      True for blank
         */
        function isNullOrEmpty(target: any): boolean;
        /**
         * Generate random identifier string (UUIDv4)
         */
        function randomId(): string;
        /**
         * Returns true if current window is in frame.
         */
        function isInFrame(): boolean;
        /**
         * valueMaybeEmptyがnullまたはundefinedの場合、defaultValueを返す。
         * そうでなければ、valueMaybeEmptyを返す。
         */
        function orDefault(valueMaybeEmpty: any, defaultValue: any): any;
        /**
         * Returns true if expects contains actual.
         */
        function isIn(actual: any, expects: Array<any>): boolean;
        function createTreeFromString(original: string, openChar: string, closeChar: string, seperatorChar: string, operatorChar: Array<string>): Array<TreeObject>[];
        class TreeObject {
            value: string;
            isOperator: boolean;
            children: Array<TreeObject>[];
            index: number;
            constructor(value?: string, children?: Array<TreeObject>[], index?: number, isOperator?: boolean);
        }
        /**
         * Like Java Optional
         */
        module optional {
            function of<V>(value: V): Optional<V>;
            function empty(): Optional<any>;
            class Optional<V> {
                value: V;
                constructor(value: V);
                ifPresent(consumer: (value: V) => {}): this;
                ifEmpty(action: () => {}): this;
                map<M>(mapper: (value: V) => M): Optional<M>;
                isPresent(): boolean;
                get(): V;
                orElse(stead: V): V;
                orElseThrow(errorBuilder: () => Error): void;
            }
        }
        class Range {
            start: number;
            end: number;
            constructor(start: number, end: number);
            contains(value: number): boolean;
            greaterThan(value: number): boolean;
            greaterThanOrEqualTo(value: number): boolean;
            lessThan(value: number): boolean;
            lessThanOrEqualTo(value: number): boolean;
            distanceFrom(value: number): number;
        }
    }
    class WebStorageWrapper {
        nativeStorage: Storage;
        constructor(nativeStorage: Storage);
        setItem(key: string, value: string): void;
        setItemAsJson(key: string, value: any): void;
        containsKey(key: string): boolean;
        getItem(key: string): util.optional.Optional<string>;
        getItemAndRemove(key: string): util.optional.Optional<string>;
        removeItem(key: string): void;
        clear(): void;
    }
    /**
     * Utilities about jquery deferred
     */
    module deferred {
        /**
         * Repeats a task with jQuery Deferred
         */
        function repeat(configurator: (conf: repeater.IConfiguration) => void): JQueryPromise<{}>;
        module repeater {
            function begin(conf: IConfiguration): JQueryPromise<{}>;
            interface IConfiguration {
                /**
                 * Set task returns JQueryPromise.
                 */
                task(taskFunction: () => JQueryPromise<any>): IConfiguration;
                /**
                 * Set condition to repeat task.
                 */
                while(whileCondition: (taskResult: any) => boolean): IConfiguration;
                /**
                 * Set pause time as milliseconds.
                 */
                pause(pauseMilliseconds: number): IConfiguration;
            }
            function createConfiguration(): IConfiguration;
        }
    }
    module resource {
        function getText(code: string): string;
        function getMessage(messageId: string, ...params: any[]): string;
    }
    var sessionStorage: WebStorageWrapper;
}
declare module nts.uk {
    module format {
        interface IFormatter {
            format(source: any): string;
        }
        interface IFormatterOption {
            constraintName: string;
        }
        class NoFormatter implements format.IFormatter {
            format(source: any): string;
        }
    }
    module text {
        /**
         * 文字列の半角文字数を数える（Unicode用）
         * @param text 解析対象の文字列
         */
        function countHalf(text: string): number;
        function toOneByteAlphaNumberic(text: string): string;
        function toTwoByteAlphaNumberic(text: string): string;
        function katakanaToHiragana(text: string): string;
        function hiraganaToKatakana(text: string, opt?: boolean): string;
        /**
         * 半角カタカナを全角カタカナに変換
         *
         * @param {String} str 変換したい文字列
         */
        function oneByteKatakanaToTwoByte(text: string): string;
        /**
         * 文字列が半角数字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHalfNumeric(text: string): boolean;
        /**
         * 文字列が半角英字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHalfAlphabet(text: string): boolean;
        /**
         * 文字列が半角英数字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHalfAlphanumeric(text: string): boolean;
        /**
         * 文字列が半角カナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHalfKatakana(text: string): boolean;
        /**
         * 文字列が全角カナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allFullKatakana(text: string): boolean;
        /**
         * 文字列が半角文字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHalf(text: string): boolean;
        /**
         * 文字列が平仮名のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allHiragana(text: string): boolean;
        /**
         * 文字列がカタカナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        function allKatakana(text: string): boolean;
        /**
         * 文字列中のHTML記号をサニタイズする
         * @param text 変換対象の文字列
         */
        function htmlEncode(text: string): string;
        /**
         * 1文字目のみ小文字に変換する
         * @param text 変換対象の文字列
         */
        function toLowerCaseFirst(text: string): string;
        /**
         * 1文字目のみ大文字に変換する
         * @param text 変換対象の文字列
         */
        function toUpperCaseFirst(text: string): string;
        /**
        * 指定された文字列が、null、undefined、Emptyか判定する
        * @param text 判定対象の文字列
        */
        function isNullOrEmpty(text: any): boolean;
        /**
        * 指定した文字列の各書式項目を、対応するオブジェクトの値と等価のテキストに置換する
        * @param text 書式文字列
        * @param args 置換の文字列（配列可）
        */
        function format(format: string, ...args: any[]): string;
        /**
        * 変換文字列の先頭に、文字数分の指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 指定文字列
        * @param length 文字数
        */
        function padLeft(text: string, paddingChar: string, length: number): string;
        /**
        * 変換文字列の末尾に、文字数分の指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 指定文字列
        * @param length 文字数
        */
        function padRight(text: string, paddingChar: string, length: number): string;
        /**
        * 指定した文字列に、指定した文字列数分、指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 埋める文字列
        * @param isPadLeft 左埋めフラグ（false：右埋め）
        * @param length 文字数
        */
        function charPadding(text: string, paddingChar: string, isPadLeft: boolean, length: number): string;
        function replaceAll(originalString: string, find: string, replace: string): string;
        function removeFromStart(originalString: string, charSet: string): string;
        /**
         * Type of characters
         */
        class CharType {
            viewName: string;
            width: number;
            validator: (text: string) => boolean;
            constructor(viewName: string, width: number, validator: (text: string) => boolean);
            validate(text: string): boolean;
            buildConstraintText(maxLength: number): string;
            getViewLength(length: any): number;
        }
        function getCharType(primitiveValueName: string): CharType;
        /**
         * Format for EmployeeCode
         * @return {String}  EmployeeCode
         */
        function formatEmployeeCode(code: string, filldirection: string, fillcharacter: string, length: number): string;
        function splitOrPadRight(originalString: string, length: number, char?: string): string;
        function formatCurrency(amount: number, locale: string): string;
        function reverseDirection(direction: string): string;
        function getISOFormat(format: string): string;
        class StringFormatter implements format.IFormatter {
            args: any;
            constructor(args: any);
            format(source: any): string;
        }
        class NumberFormatter implements format.IFormatter {
            option: any;
            constructor(option: any);
            format(source: any): string;
        }
        class TimeFormatter implements format.IFormatter {
            option: any;
            constructor(option: any);
            format(source: any): string;
        }
    }
}
declare module nts.uk.time {
    class JapanYearMonth {
        empire: string;
        year: number;
        month: number;
        constructor(empire?: string, year?: number, month?: number);
        getEmpire(): string;
        getYear(): number;
        getMonth(): number;
        toString(): string;
    }
    function yearInJapanEmpire(date: any): JapanYearMonth;
    function yearmonthInJapanEmpire(yearmonth: any): JapanYearMonth;
    class JapanDateMoment {
        moment: moment.Moment;
        empire: string;
        year: number;
        month: number;
        day: number;
        constructor(date: any, outputFormat?: string);
        toString(): string;
    }
    function dateInJapanEmpire(date: any): JapanDateMoment;
    /**
    * Format by pattern
    * @param  {number} [seconds]	  Input seconds
    * @param  {string} [formatOption] Format option
    * @return {string}				Formatted duration
    */
    function formatSeconds(seconds: number, formatOption: string): string;
    /**
    * 日付をフォーマットする
    * @param  {Date}   date	 日付
    * @param  {String} [format] フォーマット
    * @return {String}		  フォーマット済み日付
    */
    function formatDate(date: Date, format: any): any;
    /**
    * Format YearMonth
    * @param  {Number} [yearMonth]	Input Yearmonth
    * @return {String}				Formatted YearMonth
    */
    function formatYearMonth(yearMonth: number): string;
    /**
    * Format by pattern
    * @param  {Date}   [date]		 Input date
    * @param  {String} [inputFormat]  Input format
    * @param  {String} [outputFormat] Output format
    * @return {String}				Formatted date
    */
    function formatPattern(date: any, inputFormat?: string, outputFormat?: string): string;
    abstract class ParseResult {
        success: boolean;
        constructor(success: any);
        abstract format(): any;
        abstract toValue(): any;
        abstract getMsg(): any;
    }
    class ResultParseTime extends ParseResult {
        minus: boolean;
        hours: number;
        minutes: number;
        msg: string;
        constructor(success: any, minus?: any, hours?: any, minutes?: any, msg?: any);
        static succeeded(minus: any, hours: any, minutes: any): ResultParseTime;
        static failed(): ResultParseTime;
        format(): string;
        toValue(): number;
        getMsg(): string;
    }
    function parseTime(time: any, isMinutes?: boolean): ResultParseTime;
    class ResultParseYearMonth extends ParseResult {
        year: number;
        month: number;
        msg: string;
        constructor(success: any, msg?: any, year?: any, month?: any);
        static succeeded(year: any, month: any): ResultParseYearMonth;
        static failed(msg?: any): ResultParseYearMonth;
        format(): string;
        toValue(): number;
        getMsg(): string;
    }
    function parseYearMonth(yearMonth: any): ResultParseYearMonth;
    class ResultParseTimeOfTheDay extends ParseResult {
        hour: number;
        minute: number;
        msg: string;
        constructor(success: any, msg?: any, hour?: any, minute?: any);
        static succeeded(hour: any, minute: any): ResultParseTimeOfTheDay;
        static failed(msg?: any): ResultParseTimeOfTheDay;
        format(): string;
        toValue(): number;
        getMsg(): string;
    }
    function parseTimeOfTheDay(timeOfDay: any): ResultParseTimeOfTheDay;
    class ResultParseYearMonthDate extends ParseResult {
        year: number;
        month: number;
        date: number;
        msg: string;
        constructor(success: any, msg?: any, year?: any, month?: any, date?: any);
        static succeeded(year: any, month: any, date: any): ResultParseYearMonthDate;
        static failed(msg?: any): ResultParseYearMonthDate;
        format(): string;
        toValue(): number;
        getMsg(): string;
    }
    function parseYearMonthDate(yearMonthDate: any): ResultParseYearMonthDate;
    class MomentResult extends ParseResult {
        momentObject: moment.Moment;
        outputFormat: string;
        msg: string;
        constructor(momentObject: moment.Moment, outputFormat?: string);
        succeeded(): void;
        failed(msg?: string): void;
        format(): string;
        toValue(): moment.Moment;
        toNumber(outputFormat?: string): number;
        getMsg(): string;
    }
    function parseMoment(datetime: any, outputFormat?: any, inputFormat?: any): MomentResult;
    function UTCDate(year?: number, month?: number, date?: number, hours?: number, minutes?: number, seconds?: number, milliseconds?: number): Date;
}
declare module nts.uk.request {
    var STORAGE_KEY_TRANSFER_DATA: string;
    type WebAppId = 'com' | 'pr';
    class QueryString {
        items: {
            [key: string]: any;
        };
        constructor();
        static parseUrl(url: string): QueryString;
        static build(entriesObj: {
            [key: string]: any;
        }): QueryString;
        get(key: string): any;
        set(key: string, value: any): void;
        remove(key: string): void;
        mergeFrom(otherObj: QueryString): void;
        count(): number;
        hasItems(): boolean;
        serialize(): string;
    }
    /**
     * URL and QueryString
     */
    class Locator {
        rawUrl: string;
        queryString: QueryString;
        constructor(url: string);
        serialize(): string;
        mergeRelativePath(relativePath: any): Locator;
    }
    function ajax(path: string, data?: any, options?: any): any;
    function exportFile(path: string, data?: any, options?: any): JQueryPromise<{}>;
    module specials {
        function getAsyncTaskInfo(taskId: string): any;
        function donwloadFile(fileId: string): void;
    }
    function jump(path: string, data?: any): void;
    function resolvePath(path: string): string;
    module location {
        var current: Locator;
        var appRoot: Locator;
        var siteRoot: Locator;
        var ajaxRootDir: string;
        var currentAppId: WebAppId;
    }
}
declare module nts.uk.ui {
    var _viewModel: any;
    /** Event to notify document ready to initialize UI. */
    var documentReady: JQueryCallback;
    /** Event to notify ViewModel built to bind. */
    var viewModelBuilt: JQueryCallback;
    class KibanViewModel {
        title: KnockoutObservable<string>;
        errorDialogViewModel: errors.ErrorsViewModel;
        constructor();
    }
}
declare module nts.uk.ui.notify {
}
declare module nts.uk.ui.validation {
    interface IValidator {
        validate(inputText: string, option?: any): ValidationResult;
    }
    class NoValidator {
        validate(inputText: string, option?: any): ValidationResult;
    }
    class ValidationResult {
        isValid: boolean;
        parsedValue: any;
        errorMessage: string;
        fail(errorMessage: any): void;
        success(parsedValue: any): void;
    }
    class StringValidator implements IValidator {
        constraint: any;
        charType: nts.uk.text.CharType;
        required: boolean;
        constructor(primitiveValueName: string, option?: any);
        validate(inputText: string, option?: any): ValidationResult;
    }
    class NumberValidator implements IValidator {
        constraint: any;
        option: any;
        constructor(primitiveValueName: string, option: any);
        validate(inputText: string): ValidationResult;
    }
    class TimeValidator implements IValidator {
        constraint: any;
        outputFormat: string;
        required: boolean;
        valueType: string;
        mode: string;
        constructor(primitiveValueName: string, option?: any);
        validate(inputText: string): any;
    }
    function getConstraint(primitiveValueName: string): any;
}
declare module nts.uk.ui.errors {
    class ErrorsViewModel {
        title: string;
        errors: KnockoutObservableArray<ErrorListItem>;
        option: any;
        occurs: KnockoutComputed<boolean>;
        allResolved: JQueryCallback;
        constructor();
        closeButtonClicked(): void;
        open(): void;
        hide(): void;
        addError(error: ErrorListItem): void;
        hasError(): boolean;
        clearError(): void;
        removeErrorByElement($element: JQuery): void;
    }
    interface ErrorListItem {
        tab?: string;
        location: string;
        message: string;
        $control?: JQuery;
    }
    class ErrorHeader {
        name: string;
        text: string;
        width: number;
        visible: boolean;
        constructor(name: string, text: string, width: number, visible: boolean);
    }
    /**
     *  Public API
    **/
    function errorsViewModel(): ErrorsViewModel;
    function show(): void;
    function hide(): void;
    function add(error: ErrorListItem): void;
    function hasError(): boolean;
    function clearAll(): void;
    function removeByElement($control: JQuery): void;
}
declare module nts.uk.ui {
    module windows {
        /**
         * Main or Sub Window(dialog)
         */
        class ScreenWindow {
            id: string;
            isRoot: boolean;
            parent: ScreenWindow;
            globalContext: any;
            $dialog: JQuery;
            $iframe: JQuery;
            onClosedHandler: () => void;
            constructor(id: string, isRoot: boolean, parent: ScreenWindow);
            static createMainWindow(): ScreenWindow;
            static createSubWindow(parent: ScreenWindow): ScreenWindow;
            setGlobal(globalContext: any): void;
            setTitle(newTitle: string): void;
            setHeight(height: any): void;
            setWidth(width: any): void;
            setSize(height: any, width: any): void;
            setupAsDialog(path: string, options: any): void;
            build$dialog(options: any): void;
            onClosed(callback: () => void): void;
            close(): void;
            dispose(): void;
        }
        /**
         * All ScreenWindows are managed by this container.
         * this instance is singleton in one browser-tab.
         */
        class ScreenWindowContainer {
            windows: {
                [key: string]: ScreenWindow;
            };
            shared: {
                [key: string]: any;
            };
            localShared: {
                [key: string]: any;
            };
            constructor();
            /**
             * All dialog object is in MainWindow.
             */
            createDialog(path: string, options: any, parentId: string): ScreenWindow;
            getShared(key: string): any;
            setShared(key: string, data: any, isRoot: boolean, persist?: boolean): void;
            close(id: string): void;
        }
        var selfId: string;
        var container: ScreenWindowContainer;
        function getShared(key: string): any;
        function setShared(key: string, data: any, persist?: boolean): void;
        function getSelf(): ScreenWindow;
        function close(windowId?: string): void;
        module sub {
            function modal(path: string, options?: any): ScreenWindow;
            function modeless(path: string, options?: any): ScreenWindow;
            function open(path: string, options?: any): ScreenWindow;
        }
    }
    function localize(textId: string): string;
    module dialog {
        /**
         * Show information dialog.
         *
         * @param {String}
         *			text information text
         * @returns handler
         */
        function info(text: any): {
            then: (callback: any) => void;
        };
        /**
         * Show alert dialog.
         *
         * @param {String}
         *			text information text
         * @returns handler
         */
        function alert(text: any): {
            then: (callback: any) => void;
        };
        /**
         * Show confirm dialog.
         *
         * @param {String}
         *			text information text
         * @returns handler
         */
        function confirm(text: any): {
            ifYes: (handler: any) => any;
            ifCancel: (handler: any) => any;
            ifNo: (handler: any) => any;
            then: (handler: any) => any;
        };
    }
    module contextmenu {
        class ContextMenu {
            guid: string;
            selector: string;
            items: Array<ContextMenuItem>;
            enable: boolean;
            private target;
            /**
             * Create an instance of ContextMenu. Auto call init() method
             *
             * @constructor
             * @param {selector} Jquery selector for elements need to show ContextMenu
             * @param {items} List ContextMenuItem for ContextMenu
             * @param {enable} (Optinal) Set enable/disable for ContextMenu
             */
            constructor(selector: string, items: Array<ContextMenuItem>, enable?: boolean);
            /**
             * Create ContextMenu and bind event in DOM
             */
            init(): void;
            /**
             * Remove and unbind ContextMenu event
             */
            destroy(): void;
            /**
             * Re-create ContextMenu. Useful when you change various things in ContextMenu.items
             */
            refresh(): void;
            /**
             * Get a ContextMenuItem instance
             *
             * @param {target} Can be string or number. String type will select item by "key", Number type will select item by index
             * @return {any} Return ContextMenuItem if found or undefiend
             */
            getItem(target: any): ContextMenuItem;
            /**
             * Add an ContextMenuItem instance to ContextMenu
             *
             * @param {item} An ContextMenuItem instance
             */
            addItem(item: ContextMenuItem): void;
            /**
             * Remove item with given "key" or index
             *
             * @param {target} Can be string or number. String type will select item by "key", Number type will select item by index
             */
            removeItem(target: any): void;
            /**
             * Enable/Disable ContextMenu. If disable right-click will have default behavior
             *
             * @param {enable} A boolean value set enable/disable
             */
            setEnable(enable: boolean): void;
            /**
             * Enable/Disable item with given "key" or index
             *
             * @param {enable} A boolean value set enable/disable
             * @param {target} Can be string or number. String type will select item by "key", Number type will select item by index
             */
            setEnableItem(enable: boolean, target: any): void;
            /**
             * Show/Hide item with given "key" or index
             *
             * @param {enable} A boolean value set visible/hidden
             * @param {target} Can be string or number. String type will select item by "key", Number type will select item by index
             */
            setVisibleItem(visible: boolean, target: any): void;
            private createMenuItems(container);
        }
        class ContextMenuItem {
            key: string;
            text: string;
            handler: (ui: any) => void;
            icon: string;
            visible: boolean;
            enable: boolean;
            constructor(key: string, text?: string, handler?: (ui: any) => void, icon?: string, visible?: boolean, enable?: boolean);
        }
    }
    var confirmSave: (dirtyChecker: DirtyChecker) => any;
    function confirmSaveEnableDialog(beforeunloadHandler: any, dialog: any): void;
    function confirmSaveDisableDialog(dialog: any): void;
    function confirmSaveEnable(beforeunloadHandler: any): void;
    function confirmSaveDisable(): void;
    class DirtyChecker {
        targetViewModel: KnockoutObservable<any>;
        initialState: string;
        constructor(targetViewModelObservable: KnockoutObservable<any>);
        getCurrentState(): string;
        reset(): void;
        isDirty(): boolean;
    }
    /**
     * Utilities for IgniteUI
     */
    module ig {
        module grid {
            function getRowIdFrom($anyElementInRow: JQuery): any;
            function getRowIndexFrom($anyElementInRow: JQuery): number;
            module virtual {
                function getDisplayContainer(gridId: String): JQuery;
                function getVisibleRows(gridId: String): JQuery;
                function getFirstVisibleRow(gridId: String): JQuery;
                function getLastVisibleRow(gridId: String): JQuery;
            }
            module header {
                function getCell(gridId: String, columnKey: String): JQuery;
                function getLabel(gridId: String, columnKey: String): JQuery;
            }
        }
    }
}
declare module nts.uk.ui.option {
    abstract class DialogOption {
        modal: boolean;
        show: boolean;
        buttons: DialogButton[];
    }
    interface IDialogOption {
        modal?: boolean;
    }
    class ConfirmDialogOption extends DialogOption {
        constructor(option?: IDialogOption);
    }
    class DelDialogOption extends DialogOption {
        constructor(option?: IDialogOption);
    }
    class OKDialogOption extends DialogOption {
        constructor(option?: IDialogOption);
    }
    interface IErrorDialogOption {
        headers?: errors.ErrorHeader[];
        modal?: boolean;
        displayrows?: number;
        maxrows?: number;
        autoclose?: boolean;
    }
    class ErrorDialogOption extends DialogOption {
        headers: errors.ErrorHeader[];
        displayrows: number;
        maxrows: number;
        autoclose: boolean;
        constructor(option?: IErrorDialogOption);
    }
    class ErrorDialogWithTabOption extends ErrorDialogOption {
        constructor(option?: IErrorDialogOption);
    }
    type ButtonSize = "x-large" | "large" | "medium" | "small";
    type ButtonColor = "" | "danger" | "proceed";
    class DialogButton {
        text: string;
        "class": string;
        size: ButtonSize;
        color: ButtonColor;
        click(viewmodel: any, ui: any): void;
    }
}
declare module nts.uk.ui.option {
    abstract class EditorOptionBase {
        placeholder: string;
        width: string;
        textalign: string;
    }
    interface ITextEditorOption {
        textmode?: TextMode;
        placeholder?: string;
        width?: string;
        textalign?: string;
        filldirection?: FillDirection;
        fillcharacter?: string;
    }
    class TextEditorOption extends EditorOptionBase {
        textmode: TextMode;
        filldirection: FillDirection;
        fillcharacter: string;
        constructor(option?: ITextEditorOption);
    }
    interface ITimeEditorOption {
        inputFormat?: string;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }
    class TimeEditorOption extends EditorOptionBase {
        inputFormat: string;
        constructor(option?: ITimeEditorOption);
    }
    interface INumberEditorOption {
        groupseperator?: string;
        grouplength?: number;
        decimalseperator?: string;
        decimallength?: number;
        currencyformat?: Currency;
        currencyposition?: string;
        placeholder?: string;
        width?: string;
        textalign?: string;
        symbolChar?: string;
        symbolPosition?: string;
    }
    class NumberEditorOption extends EditorOptionBase {
        groupseperator: string;
        grouplength: number;
        decimalseperator: string;
        decimallength: number;
        symbolChar: string;
        symbolPosition: string;
        constructor(option?: INumberEditorOption);
    }
    class CurrencyEditorOption extends NumberEditorOption {
        currencyformat: Currency;
        currencyposition: string;
        constructor(option?: INumberEditorOption);
    }
    interface IMultilineEditorOption {
        resizeable?: boolean;
        placeholder?: string;
        width?: string;
        textalign?: string;
    }
    class MultilineEditorOption extends EditorOptionBase {
        resizeable: boolean;
        constructor(option?: IMultilineEditorOption);
    }
    type TextMode = "text" | "password";
    type FillDirection = "left" | "right";
    type Currency = "JPY" | "USD";
}
interface JQuery {
    ntsPopup(args: any): JQuery;
    ntsError(action: string, param?: any): any;
    ntsListBox(action: string, param?: any): any;
    ntsGridList(action: string, param?: any): any;
    ntsGridListFeature(feature: string, action: string, ...params: any[]): any;
    ntsWizard(action: string, param?: any): any;
    ntsUserGuide(action?: string, param?: any): any;
    ntsSideBar(action?: string, param?: any): any;
    ntsEditor(action?: string, param?: any): any;
    setupSearchScroll(controlType: string, param?: any): any;
}
declare module nts.uk.ui.jqueryExtentions {
    module igGridExt {
    }
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.file {
    class FileDownload {
        servicePath: string;
        data: any;
        isFinishTask: KnockoutObservable<boolean>;
        taskId: any;
        interval: any;
        deferred: any;
        constructor(servicePath: string, data?: any);
        isTaskFinished(file: any): void;
        print(): any;
        download(): void;
    }
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.koExtentions {
}
declare module nts.uk.ui.sharedvm {
    class KibanTimer {
        elapsedSeconds: number;
        formatted: KnockoutObservable<string>;
        targetComponent: string;
        isTimerStart: KnockoutObservable<boolean>;
        oldDated: KnockoutObservable<Date>;
        interval: any;
        constructor(target: string);
        run(timer: any): void;
        start(): void;
        end(): void;
    }
}
