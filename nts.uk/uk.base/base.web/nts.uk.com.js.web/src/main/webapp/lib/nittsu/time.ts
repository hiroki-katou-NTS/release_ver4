/// <reference path="reference.ts"/>

module nts.uk.time {
    
    const MINUTES_IN_DAY = 24 * 60;

    var defaultInputFormat = [
        "YYYY/M/D",
        "YYYY-M-D",
        "YYYYMMDD",
        "YYYY/M",
        "YYYY-M",
        "YYYYMM",
        "H:mm",
        "H:mm:ss",
        "Hmm",
        "YYYY"];
    
    var dotW = ["日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"];

    function getYearMonthJapan(year, month?) {
        if (month)
            return year + "年 " + month + " 月";
        return year;
    }
    
    export function today (): moment.Moment {
        let todayDate ="";
        nts.uk.request.syncAjax("com", "server/time/today/").done(function(res) {
            todayDate = res;
        }).fail(function() {
        });
        return moment.utc(todayDate, "yyyy/MM/dd");
    }
    
    export function now (): moment.Moment {
        let nowDateTime ="";
        nts.uk.request.syncAjax("com", "server/time/now/").done(function(res) {
            nowDateTime = res;
        }).fail(function() {
        });
        return moment.utc(nowDateTime);
    }

    export class JapanYearMonth {
        empire: string;
        year: number;
        month: number;

        constructor(empire?: string, year?: number, month?: number) {
            this.empire = empire;
            this.year = year;
            this.month = month;
        }

        public getEmpire() {
            return this.empire;
        }

        public getYear() {
            return this.year;
        }

        public getMonth() {
            return this.month;
        }

        public toString() {
            return (this.empire === undefined ? "" : this.empire)
                + (this.year === undefined || this.year === "" ? "" : this.year + "年")
                + (this.month === undefined || this.month === "" ? "" : this.month + "月");
        }
    }

    export function yearInJapanEmpire(date: any): JapanYearMonth {
        return yearmonthInJapanEmpire(date, true);
    }

    export function yearmonthInJapanEmpire(yearmonth: any, onlyYear?: boolean): JapanYearMonth {
        if(!nts.uk.ntsNumber.isNumber(yearmonth) && _.isEmpty(yearmonth)){
            return null;
        }
        let dateString = yearmonth.toString(), 
            seperator = (dateString.match(/\//g) || []).length
        
        if(seperator > 2 || seperator < 0){
            return null;
        }
        let format = _.filter(defaultInputFormat, function(f){ return (f.match(/\//g) || []).length === seperator}),
            formatted = moment.utc(dateString, defaultInputFormat, true),
            formattedYear = formatted.year();
        
        if (onlyYear && dateString.length < 5) {
            formattedYear = Number(dateString);    
        }
        
        for(let i of __viewContext.env.japaneseEras){
            let startEraYear = moment(i.start).year(),
                endEraYear = moment(i.end).year();
            if (startEraYear <= formattedYear && formattedYear <= endEraYear) {
                let diff = formattedYear - startEraYear + 1;
                return new JapanYearMonth(diff === 0 ? i.name + "元年" : i.name, diff, onlyYear === true ? "" : formatted.month() + 1);
            }               
        }
        return null;
    }

    export class JapanDateMoment {
        moment: moment.Moment;
        empire: string;
        year: number;
        month: number;
        day: number;

        constructor(date: any, outputFormat?: string) {
            var MomentResult = parseMoment(date, outputFormat);
            var year = MomentResult.momentObject.year();
            var month = MomentResult.momentObject.month() + 1;
        }

        public toString() {
            return (this.empire === undefined ? "" : this.empire + " ")
                + (this.year === undefined ? "" : this.year + " 年 ")
                + (this.month === undefined ? "" : this.month + " 月")
                + (this.day === undefined ? "" : this.day + " ");
        }
    }

    export function dateInJapanEmpire(date: any): JapanDateMoment {
        return new JapanDateMoment(date);
    }

	/**
	* Format by pattern
	* @param  {number} [seconds]	  Input seconds
	* @param  {string} [formatOption] Format option
	* @return {string}				Formatted duration
	*/
    export function formatSeconds(seconds: number, formatOption: string) {
        seconds = parseInt(String(seconds));

        var ss = text.padLeft(String(seconds % 60), '0', 2);
        var minutes = Math.floor(seconds / 60);
        var mm = text.padLeft(String(minutes % 60), '0', 2);
        var hours = ntsNumber.trunc(seconds / 60 / 60);
        var h = String(hours);

        // TODO: use formatOption
        return "h:mm:ss"
            .replace(/h/g, h)
            .replace(/mm/g, mm)
            .replace(/ss/g, ss);
    }

	/**
	* Format date
	* @param  {Date}   date	 date
	* @param  {String} [format] format
	* @return {String}		  formatted date
	*/
    export function formatDate(date: Date, format: any) {
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

	/**
	* Format YearMonth
	* @param  {Number} [yearMonth]	Input Yearmonth
	* @return {String}				Formatted YearMonth
	*/
    export function formatYearMonth(yearMonth: number) {
        var result: string;
        var num = parseInt(String(yearMonth));
        var year = String(Math.floor(num / 100));
        var month = text.charPadding(String(num % 100), '0', true, 2);
        result = year + '/' + month;
        return result;
    }

    /**
    * Format MonthDay
    * @param  {any} [monthDay]  Input MonthDay
    * @return {string}          Formatted MonthDay
    */
    export function formatMonthDayLocalized(monthDay: any) {
        monthDay = String(monthDay);
        monthDay = text.padLeft(monthDay, '0', 4);
        return moment.utc(monthDay, "MMDD").format("MMMDo");
    }
    
	/**
	* Format by pattern
	* @param  {Date}   [date]		 Input date
	* @param  {String} [inputFormat]  Input format
	* @param  {String} [outputFormat] Output format
	* @return {String}				Formatted date
	*/
    export function formatPattern(date: any, inputFormat?: string, outputFormat?: string) {
        outputFormat = text.getISOFormat(outputFormat);
        var inputFormats = (inputFormat) ? inputFormat : defaultInputFormat;
        return moment(date, inputFormats).format(outputFormat);
    }

    export abstract class ParseResult {
        success: boolean;
        constructor(success) {
            this.success = success;
        }

        abstract format();
        abstract toValue();
        abstract getMsg();
    }

    export class ResultParseTime extends ParseResult {
        minus: boolean;
        hours: number;
        minutes: number;
        msg: string;
        constructor(success, minus?, hours?, minutes?, msg?) {
            super(success);
            this.minus = minus;
            this.hours = hours;
            this.minutes = minutes;
            this.msg = msg || "MsgB_15";
        }

        static succeeded(minus, hours, minutes) {
            return new ResultParseTime(true, minus, hours, minutes);
        }

        static failed() {
            return new ResultParseTime(false);
        }

        format() {
            if (!this.success)
                return "";
            return (this.minus ? '-' : '') + this.hours + ':' + text.padLeft(String(this.minutes), '0', 2);
        }

        toValue() {
            if (!this.success)
                return 0;
            return (this.minus ? -1 : 1) * (this.hours * 60 + this.minutes);
        }

        getMsg() { return this.msg; }
    }

    export function parseTime(time: any, isMinutes?: boolean): ResultParseTime {
        if (time === undefined || time === null) {
            return ResultParseTime.failed();
        }
        if (isMinutes) {
            var hoursX = ntsNumber.trunc(time / 60);
            time = (time < 0 && hoursX == 0 ? "-" : "") + hoursX + text.padLeft((Math.abs(time - hoursX * 60)).toString(), '0', 2);
        }
        if (!(time instanceof String)) {
            time = time.toString();
        }
        if (time.length < 1 || time.split(':').length > 2 || time.split('-').length > 2
            || time.lastIndexOf('-') > 0 || (time.length == 1 && !ntsNumber.isNumber(time.charAt(0)))) {
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
        } else {
            time = ntsNumber.trunc(time);
            time = text.padLeft(time, "0", time.length > 4 ? time.length : 4);
            minutes = time.substr(-2, 2);
            hours = time.substr(0, time.length - 2);
        }

        if (!ntsNumber.isNumber(minutes, false) || parseInt(minutes) > 59 || !ntsNumber.isNumber(hours, false)) {
            return ResultParseTime.failed();
        }

        return ResultParseTime.succeeded(minusNumber, parseInt(hours), parseInt(minutes));
    }
    
    export class ResultParseTimeWithSecond extends ResultParseTime {
        second: number;
        constructor(success, minus?, hours?, minutes?, second?, msg?) {
            super(success, minus, hours, minutes, msg);
            this.second = second;
        }

        static succeeded(minus, hours, minutes, second) {
            return new ResultParseTimeWithSecond(true, minus, hours, minutes, second);
        }

        static failed() {
            return new ResultParseTimeWithSecond(false);
        }

        format() {
            if (!this.success)
                return "";
            return (this.minus ? '-' : '') + this.hours + ':' + text.padLeft(String(this.minutes), '0', 2)
                        + ':' + text.padLeft(String(this.second), '0', 2);
        }

        toValue() {
            if (!this.success)
                return 0;
            return (this.minus ? -1 : 1) * (this.hours * 60 * 60 + this.minutes * 60 + this.second);
        }

        getMsg() { return this.msg; }
    }
    
    export function parseTimeWithSecond(time: any, isMinutes?: boolean): ResultParseTimeWithSecond {
        if (time === undefined || time === null) {
            return ResultParseTimeWithSecond.failed();
        }
        if (isMinutes) {
            var totalMinuteX = ntsNumber.trunc(time / 60);
            var secondX = ntsNumber.trunc(time % 60);
            var minuteX = ntsNumber.trunc(totalMinuteX % 60);
            var hoursX = ntsNumber.trunc(totalMinuteX / 60);
            time = (time < 0 ? "-" : "") + hoursX + ":" + text.padLeft(minuteX.toString(), '0', 2)
                                        + ":" + text.padLeft(secondX.toString(), '0', 2);
        }
        if (!(time instanceof String)) {
            time = time.toString();
        }
        if (time.length < 1 || time.split(':').length > 3 || time.split('-').length > 2
            || time.lastIndexOf('-') > 0 || (time.length == 1 && !ntsNumber.isNumber(time.charAt(0)))) {
            return ResultParseTimeWithSecond.failed();
        }

        var minusNumber = time.charAt(0) === '-';
        if (minusNumber) {
            time = time.split('-')[1];
        }
        var minutes;
        var hours;
        var seconds;
        if (time.indexOf(':') > -1) {
            var times = time.split(':');
            seconds = times[2];
            minutes = times[1];
            hours = times[0];
        } else {
            time = ntsNumber.trunc(time);
            time = text.padLeft(time, "0", time.length > 6 ? time.length : 6);
            var mAS = time.substr(-2, 4);
            seconds = mAS.substr(-2, 2);
            minutes = mAS.substr(0, 2);
            hours = time.substr(0, time.length - 4);
        }

        if (!ntsNumber.isNumber(minutes, false) || parseInt(minutes) > 59 || !ntsNumber.isNumber(hours, false)
             || !ntsNumber.isNumber(seconds, false) || parseInt(seconds) > 59) {
            return ResultParseTimeWithSecond.failed();
        }

        return ResultParseTimeWithSecond.succeeded(minusNumber, parseInt(hours), parseInt(minutes), parseInt(seconds));
    }

    export class ResultParseYearMonth extends ParseResult {
        year: number;
        month: number;
        msg: string;
        constructor(success, msg?, year?, month?) {
            super(success);
            this.year = year;
            this.month = month;
            this.msg = msg || "must yyyymm or yyyy/mm format: year in [1900-9999] and month in [1-12] ";
        }

        static succeeded(year, month) {
            return new ResultParseYearMonth(true, "", year, month);
        }

        static failed(msg?) {
            return new ResultParseYearMonth(false, msg);
        }

        format() {
            if (!this.success) {
                return "";
            }
            return this.year + '/' + text.padLeft(String(this.month), '0', 2);
        }

        toValue() {
            if (!this.success) {
                return 0;
            }
            return (this.year * 100 + this.month);
        }
        getMsg() { return this.msg; }
    }

    export function parseYearMonth(yearMonth: any): ResultParseYearMonth {
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
        if (checkNum.length > 0) return ResultParseYearMonth.failed("yearmonth must contain digits and slashes only!");
        if (yearMonth.length != 6 && yearMonth.length != 5) return ResultParseYearMonth.failed("wrong yearmonth format: must be yyyy/mm or yyyymm");
        var year = parseInt(yearMonth.substring(0, 4));
        var month = parseInt(yearMonth.substring(4));
        if (year < 1900 || year > 9999) return ResultParseYearMonth.failed("wrong year: year must in range 1900-9999");
        if (month < 1 || month > 12) return ResultParseYearMonth.failed("wrong month: month must in range 1-12");
        return ResultParseYearMonth.succeeded(year, month);
    }

    export class ResultParseTimeOfTheDay extends ParseResult {
        hour: number;
        minute: number;
        msg: string;
        constructor(success, msg?, hour?, minute?) {
            super(success);
            this.hour = hour;
            this.minute = minute;
            this.msg = msg || nts.uk.resource.getMessage("FND_E_DATE_YMD");
        }
        static succeeded(hour, minute) {
            return new ResultParseTimeOfTheDay(true, "", hour, minute);
        }
        static failed(msg?) {
            return new ResultParseTimeOfTheDay(false, msg);
        }
        format() {
            if (!this.success) {
                return "";
            }
            return this.hour + ':' + text.padLeft(String(this.minute), '0', 2);
        }
        toValue() {
            if (!this.success) {
                return 0;
            }
            return (this.hour * 100 + this.minute);
        }
        getMsg() { return this.msg; }
    }

    export function parseTimeOfTheDay(timeOfDay: any): ResultParseTimeOfTheDay {
        if (timeOfDay === undefined || timeOfDay === null) {
            return ResultParseTimeOfTheDay.failed("time of the day cannot be empty!");
        }
        if (!(timeOfDay instanceof String)) {
            timeOfDay = timeOfDay.toString();
        }
        timeOfDay = timeOfDay.replace(":", "");
        var checkNum = timeOfDay.replace(/[0-9]/g, "");
        var stringLength = timeOfDay.length;
        if (checkNum.length > 0) return ResultParseTimeOfTheDay.failed(nts.uk.resource.getMessage("FND_E_DATE_YMD"));
        if (stringLength < 3 || stringLength > 4) return ResultParseTimeOfTheDay.failed(nts.uk.resource.getMessage("FND_E_DATE_YMD"));
        var hour = parseInt(timeOfDay.substring(0, stringLength - 2));
        var minute = parseInt(timeOfDay.substring(stringLength - 2));
        //console.log(checkNum.substring(0,stringLength-2));
        if (hour < 0 || hour > 23) return ResultParseTimeOfTheDay.failed(nts.uk.resource.getMessage("FND_E_DATE_YMD"));
        if (minute < 0 || minute > 59) return ResultParseTimeOfTheDay.failed(nts.uk.resource.getMessage("FND_E_DATE_YMD"));
        return ResultParseTimeOfTheDay.succeeded(hour, minute);
    }

    export class ResultParseYearMonthDate extends ParseResult {
        year: number;
        month: number;
        date: number;
        msg: string;
        constructor(success, msg?, year?, month?, date?) {
            super(success);
            this.year = year;
            this.month = month;
            this.date = date;
            this.msg = msg || nts.uk.resource.getMessage("FND_E_DATE_YMD");
        }

        static succeeded(year, month, date) {
            return new ResultParseYearMonthDate(true, "", year, month, date);
        }

        static failed(msg?) {
            return new ResultParseYearMonthDate(false, msg);
        }

        format() {
            if (!this.success) {
                return "";
            }
            return this.year + '/' + text.padLeft(String(this.month), '0', 2) + text.padLeft(String(this.date), '0', 2);
        }

        toValue() {
            if (!this.success) {
                return 0;
            }
            return (this.year * 10000 + this.month * 100 + this.date);
        }
        getMsg() { return this.msg; }
    }

    export function parseYearMonthDate(yearMonthDate: any): ResultParseYearMonthDate {
        if (yearMonthDate === undefined || yearMonthDate === null) {
            return ResultParseYearMonthDate.failed("full date can not empty!");
        }
        if (!(yearMonthDate instanceof String)) {
            yearMonthDate = yearMonthDate.toString();
        }
        yearMonthDate = yearMonthDate.replace("/", "");
        yearMonthDate = yearMonthDate.replace("/", "");
        var checkNum = yearMonthDate.replace(/[0-9]/g, "");
        if (checkNum.length !== 0) return ResultParseYearMonthDate.failed("full date must contain digits and slashes only");
        if (yearMonthDate.length != 8) return ResultParseYearMonthDate.failed("full date format must be yyyy/mm/dd or yyyymmdd");
        var year = parseInt(yearMonthDate.substring(0, 4));
        if (year < 1900 || year > 9999) {
            return ResultParseYearMonthDate.failed("invalid: year must in range 1900-9999");
        }
        var month = parseInt(yearMonthDate.substring(4, 6));
        if (month < 1 || month > 12) return ResultParseYearMonthDate.failed("invalid: month must in range 1-12");
        var date = parseInt(yearMonthDate.substring(6));
        var maxDate = 30;
        switch (month) {
            case 2:
                if (year % 400 == 0) {
                    maxDate = 29;
                } else if (year % 4 == 0 && year % 25 != 0) {
                    maxDate = 29;
                } else {
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
        if (date < 1 || date > maxDate) return ResultParseYearMonthDate.failed("invalid: month = " + month + ", so your date must in range 1-" + maxDate);
        return ResultParseYearMonthDate.succeeded(year, month, date);
    }

    export class MomentResult extends ParseResult {
        momentObject: moment.Moment;
        min: moment.Moment = moment.utc("1900/01/01", "YYYY/MM/DD", true);
        max: moment.Moment = moment.utc("9999/12/31", "YYYY/MM/DD", true);
        outputFormat: string;
        msg: string;
        msgID: string;
        params: Array<string>;
        constructor(momentObject: moment.Moment, outputFormat?: string) {
            super(true);
            this.momentObject = momentObject;
            this.outputFormat = text.getISOFormat(outputFormat);
        }

        succeeded() {
            this.success = true;
        }

        failed(msg?: string) {
            this.msg = (msg) ? msg : "Invalid format";
            this.success = false;
        }

        failedWithMessegeId(msgID?: string, params?: Array<string>) {
            this.msgID = msgID;
            this.params = params;
            this.success = false;
        }

        format() {
            if (!this.success)
                return "";
            return this.momentObject.format(this.outputFormat);
        }

        toValue() {
            if (!this.success)
                return null;
            return this.momentObject;
        }

        systemMin() {
            return this.min;
        }

        systemMax() {
            return this.max;
        }

        toNumber(outputFormat?: string) {
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
            else if (outputFormat === "time") {
                return this.momentObject.hours() * 60 + this.momentObject.minutes();
            } else {
                return parseInt(this.momentObject.format(outputFormat).replace(/[^\d]/g, ""));
            }
        }

        getMsg() { return this.msg; }

        getEmsg(name?: string) {
            if (this.msgID === undefined) {
                return this.msg;
            } else {
                if (name !== undefined) {
                    this.params.unshift(name);
                }
                return nts.uk.resource.getMessage(this.msgID, this.params);
            }
        }

        getMsgID() { return this.msgID === undefined ? "" : this.msgID; }
    }


    export function parseMoment(datetime: any, outputFormat?: any, inputFormat?: any): MomentResult {
        var inputFormats = (inputFormat) ? findSame(inputFormat) : findFormat(outputFormat);
        var momentObject = moment.utc(datetime, inputFormats, true);
        var result = new MomentResult(momentObject, outputFormat);
        if (momentObject.isValid() && (momentObject.isSameOrBefore(result.systemMax()) && momentObject.isSameOrAfter(result.systemMin()))) {
            result.succeeded();
        } else {
            let parsedFormat: any = momentObject.creationData().format;
            let isHasYear = (nts.uk.util.isNullOrEmpty(outputFormat) ? false : outputFormat.indexOf("Y") >= 0) || parsedFormat.indexOf("Y") >= 0;
            let isHasMonth = (nts.uk.util.isNullOrEmpty(outputFormat) ? false : outputFormat.indexOf("M") >= 0) || parsedFormat.indexOf("M") >= 0;
            let isHasDay = (nts.uk.util.isNullOrEmpty(outputFormat) ? false : outputFormat.indexOf("D") >= 0) || parsedFormat.indexOf("D") >= 0;
            if (isHasDay && isHasMonth && isHasYear) {
                result.failedWithMessegeId("FND_E_DATE_YMD", [result.systemMin().format("YYYY/MM/DD"), result.systemMax().format("YYYY/MM/DD")]);
            } else if (isHasMonth && isHasYear) {
                result.failedWithMessegeId("FND_E_DATE_YM", [result.systemMin().format("YYYY/MM"), result.systemMax().format("YYYY/MM")]);
            } else {
                result.failedWithMessegeId("FND_E_DATE_Y", [result.systemMin().format("YYYY"), result.systemMax().format("YYYY")]);
            }
        }
        return result;
    }
    
    function findSame(format: string): Array<string> {
        let result = [], splited = format.split(" "),
            isHasYear = _.indexOf(splited[0], "Y") >= 0, 
            isHasMonth = _.indexOf(splited[0], "M") >= 0, 
            isHasDay = _.indexOf(splited[0], "D") >= 0, 
            separator = splited[0].replace(/Y/g, "").replace(/M/g, "").replace(/D/g, "").charAt(0),
            years = isHasYear ? ["YYYY", ""] : [""],
            months = isHasMonth ? ["M", "MM"] : [""],
            days = isHasDay ? ["D", "DD"] : [""],
            separators = ["", separator],
            isTime = _.size(splited) > 1;
        
        result.push(format);
        _.forEach(years, function(y) {
            _.forEach(months, function(m) {
                _.forEach(days, function(d) {
                    let r = [];
                    if(!_.isEmpty(y)){
                        r.push(y);
                    }
                    
                    if(!_.isEmpty(m)){
                        r.push(m);
                    }
                    
                    if(!_.isEmpty(d)){
                        r.push(d);
                    }
                    if(!_.isEmpty(r)){
                        _.forEach(separators, function(s) {
                            if(isTime){
                                result.push(_.join([_.join(r, s), splited[1]], ' '));
                            } else {
                                result.push(_.join(r, s));  
                            } 
                        });
                    }
                });
            });
        });
//        result.push(format.replace(/\//g, ""));
//        result.push(format.replace("MM", "M"));
//        result.push(format.replace("DD", "D"));
//        result.push(format.replace("MM", "M").replace("DD", "D"));
//        result.push(format.replace(/:/g, ""));
//        result.push(format.replace(/\//g, "").replace(/:/g, ""));
        return result;
    }

    function findFormat(format: string): Array<string> {
        if (nts.uk.util.isNullOrEmpty(format)) {
            return defaultInputFormat;
        }
        if (format === "yearmonth") {
            format = "YM";
            format = "YM";
        }
        let uniqueFormat = _.uniq(format.split(""));
        let formats = _.filter(defaultInputFormat, function(dfFormat: string) {
            return _.find(uniqueFormat, function(opFormat: string) {
                return dfFormat.indexOf(opFormat) >= 0;
            }) !== undefined;
        });
        return nts.uk.util.isNullOrEmpty(formats) ? defaultInputFormat : formats;
    }

    export function UTCDate(year?: number, month?: number, date?: number, hours?: number, minutes?: number, seconds?: number, milliseconds?: number): Date {
        // Return local time in UTC
        if (util.isNullOrUndefined(year)) {
            var currentDate: Date = new Date();
            year = currentDate.getUTCFullYear();
            month = (util.isNullOrUndefined(month)) ? currentDate.getUTCMonth() : month;
            date = (util.isNullOrUndefined(date)) ? currentDate.getUTCDate() : date;
            hours = (util.isNullOrUndefined(hours)) ? currentDate.getUTCHours() : hours;
            minutes = (util.isNullOrUndefined(minutes)) ? currentDate.getUTCMinutes() : minutes;
            seconds = (util.isNullOrUndefined(seconds)) ? currentDate.getUTCSeconds() : seconds;
            milliseconds = (util.isNullOrUndefined(milliseconds)) ? currentDate.getUTCMilliseconds() : milliseconds;
            return new Date(Date.UTC(year, month, date, hours, minutes, seconds, milliseconds));
        }
        // Return input time in UTC
        else {
            month = (util.isNullOrUndefined(month)) ? 0 : month;
            date = (util.isNullOrUndefined(date)) ? 1 : date;
            hours = (util.isNullOrUndefined(hours)) ? 0 : hours;
            minutes = (util.isNullOrUndefined(minutes)) ? 0 : minutes;
            seconds = (util.isNullOrUndefined(seconds)) ? 1 : seconds;
            milliseconds = (util.isNullOrUndefined(milliseconds)) ? 0 : milliseconds;
            return new Date(Date.UTC(year, month, date, hours, minutes, seconds, milliseconds));
        }
    }

    export class DateTimeFormatter {
        shortYmdPattern = /^\d{4}\/\d{1,2}\/\d{1,2}$/;
        shortYmdwPattern = /^\d{4}\/\d{1,2}\/\d{1,2}\(\w+\)$/;
        shortYmPattern = /^\d{4}\/\d{1,2}$/;
        shortMdPattern = /^\d{1,2}\/\d{1,2}$/;
        longYmdPattern = /^\d{4}年\d{1,2}月d{1,2}日$/;
        longYmdwPattern = /^\d{4}年\d{1,2}月d{1,2}日\(\w+\)$/;
        longFPattern = /^\d{4}年度$/;
        longJmdPattern = /^\w{2}\d{1,3}年\d{1,2}月d{1,2}日$/;
        longJmPattern = /^\w{2}\d{1,3}年\d{1,2}月$/;
        fullDateTimeShortPattern = /^\d{4}\/\d{1,2}\/\d{1,2} \d+:\d{2}:\d{2}$/;
        timeShortHmsPattern = /^\d+:\d{2}:\d{2}$/;
        timeShortHmPattern = /^\d+:\d{2}$/;
        days = ['日', '月', '火', '水', '木', '金', '土'];

        shortYmd(date: string) {
            let d = this.dateOf(date);
            if (this.shortYmdPattern.test(d)) return this.format(d);
        }

        shortYmdw(date: string) {
            let d = this.dateOf(date);
            if (this.shortYmdwPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let dayStr = this.days[new Date(d).getDay()];
                return this.format(d) + '(' + dayStr + ')';
            }
        }

        shortYm(date: string) {
            let d = this.format(this.dateOf(date));
            if (this.shortYmPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let end = d.lastIndexOf("/");
                if (end !== -1) return d.substring(0, end);
            }
        }

        shortMd(date: string) {
            let d = this.format(this.dateOf(date));
            if (this.shortMdPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let start = d.indexOf("/");
                if (start !== -1) return d.substring(start + 1);
            }
        }

        longYmd(date: string) {
            let d = this.dateOf(date);
            if (this.longYmdPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let mDate = new Date(d);
                return this.toLongJpDate(mDate);
            }
        }

        longYmdw(date: string) {
            let d = this.dateOf(date);
            if (this.longYmdwPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let mDate = new Date(d);
                return this.toLongJpDate(mDate) + '(' + this.days[mDate.getDay()] + ')';
            }
        }

        toLongJpDate(d: Date) {
            return d.getFullYear() + '年' + (d.getMonth() + 1) + '月' + d.getDate() + '日';
        }

        longF(date: string) {
            let d = this.dateOf(date);
            if (this.longFPattern.test(d)) return d;
            if (this.shortYmdPattern.test(d)) {
                let mDate = new Date(d);
                return this.fiscalYearOf(mDate) + '年度';
            }
        }

        longJmd(date: string) {
            let d = this.dateOf(date);
            if (this.longJmdPattern.test(d)) return d;
            return this.fullJapaneseDateOf(d);
        }

        longJm(date: string) {
            let d = this.dateOf(date);
            if (this.longJmPattern.test(d)) return d;
            let jpDate = this.fullJapaneseDateOf(d);
            let start = jpDate.indexOf("月");
            if (start !== -1) {
                return jpDate.substring(0, start + 1);
            }
        }

        fullJapaneseDateOf(date: string) {
            if (this.shortYmdPattern.test(date)) {
                let d = new Date(date);
                return d.toLocaleDateString("ja-JP-u-ca-japanese", { era: 'short' });
            }
            return date;
        }

        fiscalYearOf(date: Date) {
            if (date < new Date(date.getFullYear(), 3, 1))
                return date.getFullYear() - 1;
            return date.getFullYear();
        }

        dateOf(dateTime: string) {
            if (this.fullDateTimeShortPattern.test(dateTime)) {
                return dateTime.split(" ")[0];
            }
            return dateTime;
        }

        timeOf(dateTime: string) {
            if (this.fullDateTimeShortPattern.test(dateTime)) {
                return dateTime.split(" ")[1];
            }
            return dateTime;
        }

        timeShortHm(time: string) {
            let t = this.timeOf(time);
            if (this.timeShortHmPattern.test(t)) return t;
            if (this.timeShortHmsPattern.test(t)) {
                return t.substring(0, t.lastIndexOf(":"));
            }
        }

        timeShortHms(time: string) {
            let t = this.timeOf(time);
            if (this.timeShortHmsPattern.test(t)) return t;
        }

        clockShortHm(time: string) {
            return this.timeShortHm(time);
        }

        fullDateTimeShort(dateTime: string) {
            if (this.fullDateTimeShortPattern.test(dateTime)) return dateTime;
        }

        format(date: string) {
            return date;//new Date(date).toLocaleDateString("ja-JP");
        }
    }

    export function getFormatter() {
        switch (systemLanguage) {
            case 'ja':
                return new DateTimeFormatter();
            case 'en':
                return null;
        }
    }

    export function applyFormat(format: string, dateTime: string, formatter: DateTimeFormatter) {
        if (formatter === undefined) formatter = getFormatter();
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

    export function isEndOfMonth(value: string, format: string): boolean {
        let currentDate = moment(value, format);
        if (currentDate.isValid()) {
            return currentDate.daysInMonth() === currentDate.date();
        }
        return false;
    }

    export function convertJapaneseDateToGlobal(japaneseDate: string): string{
        let inputDate = _.clone(japaneseDate);
        let endEraSymbolIndex = -1;
        let currentEra;
        let eraAcceptFormats = ["YYMMDD", "YY/MM/DD", "YY/M/DD", "YY/MM/D", "YY/M/D", "Y/MM/DD", "Y/M/DD", "Y/MM/D", "Y/M/D"];
        
        for(let i of __viewContext.env.japaneseEras){
            if (inputDate.indexOf(i.name) >= 0) {
                endEraSymbolIndex = inputDate.indexOf(i.name) + i.name.length;
                currentEra = i;
                break;
            } else if (inputDate.indexOf(i.symbol) >= 0) { 
                endEraSymbolIndex = inputDate.indexOf(i.symbol) + i.symbol.length;
                currentEra = i;
                break;
            }                
        }
        if (endEraSymbolIndex > -1) {
            let startEraDate = moment(currentEra.start, "YYYY-MM-DD");
            let inputEraDate = inputDate.substring(endEraSymbolIndex);
            let tempEra: any = moment.utc(inputEraDate, eraAcceptFormats, true); 
            if (tempEra.isValid()) {
                return startEraDate.add(parseInt(tempEra.format("YY")) - 1, "Y")
                                    .set({'month': tempEra.month(), "date": tempEra.date()})
                                    .format("YYYY/MM/DD");      
            }
        }
        
        return japaneseDate;
    }

}
