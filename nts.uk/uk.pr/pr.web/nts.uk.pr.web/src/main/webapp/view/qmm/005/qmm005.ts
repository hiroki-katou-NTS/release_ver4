class CheckBoxWithHelpBindingHandler implements KnockoutBindingHandler {
    constructor() {
    }

    init(element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        ko.bindingHandlers['ntsCheckBox'].init(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext);
        if (valueAccessor().helper) {
            var span = document.createElement('span');
            if (typeof valueAccessor().helper == 'function') {
                span.innerHTML = '(' + valueAccessor().helper() + ')';
            }
            else if (typeof valueAccessor().helper == 'string') {
                span.innerHTML = '(' + valueAccessor().helper + ')';
            } else if (typeof valueAccessor().helper == 'object') {
                span.id = valueAccessor().helper.id;
                if (typeof valueAccessor().helper.text == 'function') {
                    span.innerHTML = '(' + valueAccessor().helper.text() + ')';
                }
                else if (typeof valueAccessor().helper.text == 'string') {
                    span.innerHTML = '(' + valueAccessor().helper.text + ')';
                }
            }
            span.setAttribute('class', 'label helper');
            element.getElementsByTagName('label')[0].appendChild(span);
        }
    }

    update(element: HTMLElement, valueAccessor: any, allBindingsAccessor: any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        ko.bindingHandlers['ntsCheckBox'].update(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext);
        if (valueAccessor().helper) {
            var span = document.createElement('span');
            if (typeof valueAccessor().helper == 'function') {
                span.innerHTML = '(' + valueAccessor().helper() + ')';
            }
            else if (typeof valueAccessor().helper == 'string') {
                span.innerHTML = '(' + valueAccessor().helper + ')';
            } else if (typeof valueAccessor().helper == 'object') {
                span.id = valueAccessor().helper.id;

                //remove child helper
                $(".helper", element).remove();

                if (typeof valueAccessor().helper.text == 'function') {
                    span.innerHTML = '(' + valueAccessor().helper.text() + ')';
                }
                else if (typeof valueAccessor().helper.text == 'string') {
                    span.innerHTML = '(' + valueAccessor().helper.text + ')';
                }
            }
            span.setAttribute('class', 'label helper');
            element.getElementsByTagName('label')[0].appendChild(span);
        }
    }
}

ko.bindingHandlers['ntsCheckBox2'] = new CheckBoxWithHelpBindingHandler();

Date.prototype["getWorkDays"] = function() {
    let workDays = 0, lastDate = moment(this).daysInMonth();
    for (let day = 1; day <= lastDate; day++) {
        let date = new Date(this.getFullYear(), this.getMonth(), day);
        if (date.getDay() != 0 && date.getDay() != 6) {
            workDays++;
        }
    }
    return workDays;
}

Date.prototype["getDayJP"] = function() {
    return ['日', '月', '火', '水', '木', '金', '土'][this.getDay()];
}

Number.prototype["formatYearMonth"] = function(format) {
    return new String(this)["formatYearMonth"](format);
}

Date.prototype["formatYearMonth"] = function(format) {
    return (this.getFullYear() + '' + (this.getMonth() + 1))["formatYearMonth"](format);
}

String.prototype["formatYearMonth"] = function(format) {
    var match = this.match(/\d{4}|\d{2}|\d{1}/g);
    if (match.length < 2) {
        console.error('Input string is not correct!');
        return undefined;
    }
    var month = parseInt(match[1]);
    return match[0] + (format || '') + (month < 10 ? '0' + month : month);
}

String.prototype["toDate"] = function() {
    return parseInt(this)["toDate"]();
}

Number.prototype["toDate"] = function() {
    if (new String(this).length > 6) {
        console.error('Input string accept only year and month of date.');
        return undefined;
    }
    return new Date(this["formatYearMonth"]("-"));
}

Number.prototype["getYearInYm"] = function() {
    return new String(this)["getYearInYm"]();
}

String.prototype["getYearInYm"] = function() {
    var match = this.toString().match(/\d{4}|\d{2}|\d{1}/g);
    if (match.length < 2) {
        console.error('Input string is not correct!');
    }
    return parseInt(match[0]);
}

String.prototype["yearInJapanEmpire"] = function() {
    return parseInt(this)["yearInJapanEmpire"]();
}

Number.prototype["yearInJapanEmpire"] = function() {
    return nts.uk.time.yearInJapanEmpire(this).toString().trim();
}

module qmm005.common {
    export function webapi(): any {
        return {
            'qmm005a': {
                'update': 'pr/core/qmm005a/update',
                'getdata': 'pr/core/qmm005a/getdata'
            },
            'qmm005b': {
                'update': 'pr/core/qmm005b/update',
                'delete': 'pr/core/qmm005b/delete',
                'getdata': 'pr/core/qmm005b/getdata'
            },
            'qmm005c': {
                'insert': 'pr/core/qmm005c/insert'
            },
            'qmm005d': {
                'update': 'pr/core/qmm005d/update',
                'getdata': 'pr/core/qmm005d/getdata'
            },
            'qmm005e': {
                'update': 'pr/core/qmm005e/update',
                'getdata': 'pr/core/qmm005e/getdata'
            }
        }
    }

    export function formatNumber(num: number, lenght: number): string {
        let len = num.toString().length;
        if (len >= lenght) {
            return num.toString();
        }

        let str: string = '';
        for (let i = 1; i <= lenght - len; i++) {
            str += '0';
        }

        return str + num;
    }

    interface ISelectItem {
        index: any;
        label: string;
        value?: any;
        selected?: boolean;
    }

    export class SelectItem {
        index: any;
        label: string;
        value: any;
        selected: boolean;
        constructor(param: ISelectItem) {
            let self = this;
            self.index = param.index;
            self.label = param.label;
            self.value = param.value || undefined;
            self.selected = param.selected || false;
        }
    }

    interface ICheckBoxItem {
        text: string;
        helper?: any;
        enable?: boolean;
        checked?: boolean;
    }

    export class CheckBoxItem {
        text: string;
        helper: any;
        enable: KnockoutObservable<boolean>;
        checked: KnockoutObservable<boolean>;
        constructor(param: ICheckBoxItem) {
            let self = this;
            self.text = param.text;
            self.helper = param.helper;
            self.enable = ko.observable(param.enable || true);
            self.checked = ko.observable(param.checked || false);
        }
    }
}

// detect size of window
(window.onresize = () => { window["large"] = window.outerWidth >= 1920; })();

// 4dev
var _ref = (window.location.href.indexOf('localhost') == -1) && 'v1.0.0' || new Date().getTime();

var route = window.location.href
    .slice(0, window.location.href.lastIndexOf('/'))
    .slice(window.location.href.indexOf('/view/') + 6)
    .replace('/', '')
    .replace(/\//g, '.');

document.writeln("<link rel='stylesheet' type='text/css' href='" + route + ".style.css?ref=" + _ref + "' />");
document.writeln("<script type='text/javascript' src='" + route + ".service.js?ref=" + _ref + "'></script>");
document.writeln("<script type='text/javascript' src='" + route + ".viewmodel.js?ref=" + _ref + "'></script>");
document.writeln("<script type='text/javascript' src='" + route + ".start.js?ref=" + _ref + "'></script>");