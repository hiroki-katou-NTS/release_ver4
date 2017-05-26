module nts.uk.pr.view.kmf001.d {
    export module service {
        /**
         *  Service paths
         */
        var paths: any = {
            findRetentionYearlyByCompany: 'ctx/at/shared/vacation/setting/retentionyearly/find',
            saveRetentionYearly: 'ctx/at/shared/vacation/setting/retentionyearly/save',
            findByEmployment: 'ctx/at/shared/vacation/setting/employmentsetting/find',
            saveByEmployment: 'ctx/at/shared/vacation/setting/employmentsetting/save'
        };

        /**
         * Normal service.
         */
//        export class Service {
//            constructor() {
//            }
//        }
        
        export function findRetentionYearly(): JQueryPromise<model.RetentionYearlyFindDto> {
            return nts.uk.request.ajax(paths.findRetentionYearlyByCompany);
        }
        
        export function saveRetentionYearly(retentionYearly: model.RetentionYearlyDto):  JQueryPromise<void> {
            var data = {retentionYearly: retentionYearly};
            return nts.uk.request.ajax(paths.saveRetentionYearly, data);
        }
        
        export function findByEmployment(empCode: string): JQueryPromise<model.EmploymentSettingFindDto> {
            return nts.uk.request.ajax(paths.findByEmployment + "/" + empCode);
        }
        
        export function saveByEmployment(employmentSetting: model.EmploymentSettingDto):  JQueryPromise<void> {
            var data = {employmentSetting: employmentSetting};
            return nts.uk.request.ajax(paths.saveByEmployment, data);
        }

        /**
        * Model namespace.
        */
        export module model {
            
            export class UpperLimitSettingFindDto{
                retentionYearsAmount: number;
                maxDaysCumulation: number; 
            }
            
            export class RetentionYearlyFindDto {
                upperLimitSetting: UpperLimitSettingFindDto;
            }
            
            export class UpperLimitSettingDto {
                retentionYearsAmount: number;
                maxDaysCumulation: number;
            }
            
            export class RetentionYearlyDto {
                upperLimitSettingDto: UpperLimitSettingDto;
            }
            
            export class EmploymentSettingDto {
                upperLimitSetting: UpperLimitSettingDto;
                employmentCode: string;
                managementCategory: number;
            }
            
            export class EmploymentSettingFindDto {
                upperLimitSetting: UpperLimitSettingDto;
                employmentCode: string;
                managementCategory: number;
            }
        }
    }
}
