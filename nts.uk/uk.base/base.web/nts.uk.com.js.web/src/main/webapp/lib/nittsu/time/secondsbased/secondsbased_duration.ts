/// <reference path="../../reference.ts"/>

module nts.uk.time.secondsBased {
    
    export module duration {
        
        export class ResultParseSecondsBasedDuration extends time.ParseResult {
            minus: boolean;
            hours: number;
            minutes: number;
            seconds: number;
            msg: string;
            
            constructor(success, minus?, hours?, minutes?, seconds?, msg?) {
                super(success);
                this.minus = minus;
                this.hours = hours;
                
                this.minutes = minutes;
                this.seconds = seconds;
                this.msg = msg || "FND_E_TIME";
            }
            
            format() {
                if (!this.success)
                    return "";
                return (this.minus ? '-' : '') + this.hours + ':' + text.padLeft(String(this.minutes), '0', 2)
                                                    + ':' + text.padLeft(String(this.seconds), '0', 2);
            }
            
            toValue() {
                if (!this.success)
                    return NaN;
                return (this.minus ? -1 : 1) * (this.hours * 60 * 60 + this.minutes * 60 + this.seconds);
            }
        
            getMsg() {
                return this.msg;
            }
            
            static succeeded(minus, hours, minutes, seconds) {
                return new ResultParseSecondsBasedDuration(true, minus, hours, minutes, seconds);
            }
    
            static failed() {
                return new ResultParseSecondsBasedDuration(false);
            }
        }
        
        export function parseString(source: string): ResultParseSecondsBasedDuration {
            var isNegative = source.indexOf('-') === 0;
            var hourPart: number;
            var minutePart: number;
            var secondPart: number;
            
            if(isNegative){
                return ResultParseSecondsBasedDuration.failed();        
            }
            
            if (source.indexOf(':') !== -1) {
                let parts = source.split(':');
                if (parts.length !== 3) {
                    return ResultParseSecondsBasedDuration.failed();
                }
                
                hourPart = Math.abs(Number(parts[0]));
                minutePart = Number(parts[1]);
                secondPart = Number(parts[2]);
            
                if(!nts.uk.ntsNumber.isNumber(hourPart, false, undefined, undefined) 
                    || !nts.uk.ntsNumber.isNumber(minutePart, false, undefined, undefined)
                    || !nts.uk.ntsNumber.isNumber(secondPart, false, undefined, undefined)){
                    return ResultParseSecondsBasedDuration.failed();
                }
            } else {
                let integerized = Number(source);
                if (isNaN(integerized)) {
                    return ResultParseSecondsBasedDuration.failed();
                }
                
                let regularized = Math.abs(integerized);
                if(!nts.uk.ntsNumber.isNumber(regularized, false, undefined, undefined)){
                    return ResultParseSecondsBasedDuration.failed();
                }
                hourPart = Math.floor(regularized / 10000);
                minutePart = Math.floor((regularized % 10000) /100);
                secondPart = Math.floor(regularized % 100);
            }
            if (!isFinite(hourPart) || !isFinite(minutePart) || !isFinite(secondPart)) {
                return ResultParseSecondsBasedDuration.failed();
            }
            
            if (minutePart >= 60 || secondPart >= 60) {
                return ResultParseSecondsBasedDuration.failed();
            }
            
            let result = ResultParseSecondsBasedDuration.succeeded(isNegative, hourPart, minutePart, secondPart);
            let values = result.toValue();
            let maxValue = 24 * 60 * 60;
            if(values >= maxValue){
                return ResultParseSecondsBasedDuration.failed();    
            }
            return result;
        }
    

        export type DurationFormatId
            = "Time_Short_HMS"           // 105:03:02
        
        export interface DurationSecondsBasedTime extends SecondsBasedTime<DurationSecondsBasedTime> {
            asHoursDouble: number;
            asHoursInt: number;
            text: string;
            formatById(formatId: DurationFormatId): string;
        }
        
        export function create(timeAsSeconds: number): DurationSecondsBasedTime {
            let duration: any = createBase(timeAsSeconds);
            
            util.accessor.defineInto(duration)
                .get("typeName", () => "DurationSecondsBasedTime")
                .get("asHoursDouble", () => timeAsSeconds / (60 * 60))
                .get("asHoursInt", () => ntsNumber.trunc(duration.asHoursDouble))
                .get("text", () => createText(duration));
            
            duration.formatById = function (formatId: DurationFormatId) {
                switch (formatId) {
                    default: return createText(duration);
                }
            };
            
            return duration;
        }
        
        function createText(duration: DurationSecondsBasedTime): string {
            return (duration.isNegative ? "-" : "")
                + duration.asHoursInt + ":" + duration.minutePartText + ":" + duration.secondPartText;
        }
        
    }
}
