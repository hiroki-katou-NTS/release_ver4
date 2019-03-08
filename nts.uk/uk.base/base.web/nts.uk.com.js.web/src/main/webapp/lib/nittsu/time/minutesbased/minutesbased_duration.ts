/// <reference path="../../reference.ts"/>

module nts.uk.time.minutesBased {
    
    export module duration {
        
        export class ResultParseMiuntesBasedDuration extends time.ParseResult {
            
            minus: boolean;
            hours: number;
            minutes: number;
            msg: string;
            
            constructor(success, minus?, hours?, minutes?, msg?) {
                super(success);
                this.minus = minus;
                this.hours = hours;
                
                this.minutes = minutes;
                this.msg = msg || "FND_E_TIME";
            }
            
            format() {
                if (!this.success)
                    return "";
                return (this.minus ? '-' : '') + this.hours + ':' + text.padLeft(String(this.minutes), '0', 2);
            }
            
            toValue() {
                if (!this.success)
                    return NaN;
                return (this.minus ? -1 : 1) * (this.hours * 60 + this.minutes);
            }
        
            getMsg() {
                return this.msg;
            }
            
            static succeeded(minus, hours, minutes) {
                return new ResultParseMiuntesBasedDuration(true, minus, hours, minutes);
            }
    
            static failed() {
                return new ResultParseMiuntesBasedDuration(false);
            }
        }
        
        export function parseString(source: string): ResultParseMiuntesBasedDuration {
            var isNegative = source.indexOf('-') === 0;
            var hourPart: number;
            var minutePart: number;
            
            if (source.indexOf(':') !== -1) {
                let parts = source.split(':');
                if (parts.length !== 2) {
                    return ResultParseMiuntesBasedDuration.failed();
                }
                
                hourPart = Math.abs(Number(parts[0]));
                minutePart = Number(parts[1]);
            
                if(!nts.uk.ntsNumber.isNumber(hourPart, false, undefined, undefined) 
                    || !nts.uk.ntsNumber.isNumber(minutePart, false, undefined, undefined)){
                    return ResultParseMiuntesBasedDuration.failed();
                }
            } else {
                let integerized = Number(source);
                if (isNaN(integerized)) {
                    return ResultParseMiuntesBasedDuration.failed();
                }
                
                let regularized = Math.abs(integerized);
                if(!nts.uk.ntsNumber.isNumber(regularized, false, undefined, undefined)){
                    return ResultParseMiuntesBasedDuration.failed();
                }
                hourPart = Math.floor(regularized / 100);
                minutePart = regularized % 100;
            }
            if (!isFinite(hourPart) || !isFinite(minutePart)) {
                return ResultParseMiuntesBasedDuration.failed();
            }
            
            if (minutePart >= 60) {
                return ResultParseMiuntesBasedDuration.failed();
            }
            
            return ResultParseMiuntesBasedDuration.succeeded(isNegative, hourPart, minutePart);
        }
    

        export type DurationFormatId
            = "Time_Short_HM"           // 105:03
        
        export interface DurationMinutesBasedTime extends MinutesBasedTime<DurationMinutesBasedTime> {
            asHoursDouble: number;
            asHoursInt: number;
            text: string;
            formatById(formatId: DurationFormatId): string;
        }
        
        export function create(timeAsMinutes: number): DurationMinutesBasedTime {
            let duration: any = createBase(timeAsMinutes);
            
            util.accessor.defineInto(duration)
                .get("typeName", () => "DurationMinutesBasedTime")
                .get("asHoursDouble", () => timeAsMinutes / 60)
                .get("asHoursInt", () => ntsNumber.trunc(duration.asHoursDouble))
                .get("text", () => createText(duration));
            
            duration.formatById = function (formatId: DurationFormatId) {
                switch (formatId) {
                    default: return createText(duration);
                }
            };
            
            return duration;
        }
        
        function createText(duration: DurationMinutesBasedTime): string {
            return (duration.isNegative ? "-" : "")
                + duration.asHoursInt + ":" + duration.minutePartText;
        }
        
    }
}