module nts.uk.at.view.kmf002.d {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
                findList: "screen/com/systemresource/findList",
                save: "bs/employee/holidaysetting/employment/save",
                find: "bs/employee/holidaysetting/employment/findEmploymentMonthDaySetting",
                remove: "bs/employee/holidaysetting/employment/remove"
            };
        
        /**
         * 
         */
        export function save(year: number, data: model.EmploymentMonthDaySetting, empCd: number): JQueryPromise<any> {
            model.EmploymentMonthDaySetting employmentMonthDaySetting = new model.EmploymentMonthDaySetting(year, empCd, new Array<model.PublicHolidayMonthSettingDto>);
            employmentMonthDaySetting.toDto(data);
            let command = {};
            command.year = year;
            command.publicHolidayMonthSettings = employmentMonthDaySetting.publicHolidayMonthSettings;
            command.empCd = empCd;
            return nts.uk.request.ajax("com", path.save, command);
        }
        
        export function find(year: number, employmentCode: number): JQueryPromise<any> {
            return nts.uk.request.ajax("com", path.find + "/" + year + "/" + employmentCode);
        }
        
        export function remove(year: number, employmentCode: number): JQueryPromise<any> {
            model.EmploymentMonthDaySettingRemoveCommand employmentMonthDaySettingRemoveCommand = new model.EmploymentMonthDaySettingRemoveCommand(year, employmentCode);
            let command = {};
            command.year = year;
            command.empCd = employmentCode;
            return nts.uk.request.ajax("com", path.remove, command);
        }
    }
    
    /**
     * Model define.
     */
    export module model {
        export class EmploymentMonthDaySetting {
            year: number;
            publicHolidayMonthSettingDto: Array<PublicHolidayMonthSettingDto>;
            empCd: number;
            
            constructor(year: number, empCd: number, publicHolidayMonthSettingDto: Array<PublicHolidayMonthSettingDto>){
                let _self = this;
                _self.year = year;
                _self.publicHolidayMonthSettingDto = publicHolidayMonthSettingDto;
                _self.empCd = empCd;
            }
            
            toDto(data: any): void {
                let _self = this;
                _.forEach(data, function(newValue) {
                    _self.publicHolidayMonthSettingDto.push(new PublicHolidayMonthSettingDto(_self.year,newValue.month(),newValue.day(),newValue.day()));
                });
            }
        }
        
        export class EmploymentMonthDaySettingRemoveCommand {
            year: number;
            empCd: number;
            
            constructor(year: number, empCd: number){
                let _self = this;
                _self.year = year;
                _self.empCd = empCd;
            }
        }
        
        export class PublicHolidayMonthSettingDto{
            publicHdManagementYear: number;
            month: number;
            inLegalHoliday: number;
            outLegalHoliday: number;
            
            constructor(publicHdManagementYear: number, month: number, inLegalHoliday: number, outLegalHoliday: number) {
                this.publicHdManagementYear = publicHdManagementYear;
                this.month = month;
                this.inLegalHoliday = inLegalHoliday;
                this.outLegalHoliday = outLegalHoliday;
            }
        }
    }
    
}