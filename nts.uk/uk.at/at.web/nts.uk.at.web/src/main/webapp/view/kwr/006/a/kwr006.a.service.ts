module nts.uk.at.view.kwr006.a {
    export module service {
        const SLASH = "/";
        var paths = {
            exportSchedule: "screen/at/monthlyschedule/export",
            getPeriod: "at/function/monthlyworkschedule/get/monthlyPeriod",
            findAllOutputItemMonthlyWorkSchedule: "at/function/monthlyworkschedule/findall",
            getCurrentLoginerRole: "at/function/monthlyworkschedule/getCurrentLoginerRole",
            getFreeSettingAuthority: "at/function/monthlyworkschedule/get/freeSettingAuthority"
        }
        export function saveCharacteristic(data: model.MonthlyWorkScheduleConditionDto): JQueryPromise<void> {
            return nts.uk.characteristics.save("MonthlyWorkScheduleCondition" +
                "_companyId_" + data.companyId +
                "_userId_" + data.userId, data);
        }

        export function restoreCharacteristic(): JQueryPromise<model.MonthlyWorkScheduleConditionDto> {
            return nts.uk.characteristics.restore("MonthlyWorkScheduleCondition" +
                "_companyId_" + __viewContext.user.companyId +
                "_userId_" + __viewContext.user.employeeId);
        }
        
        export function exportSchedule(query: model.MonthlyWorkScheduleQuery): JQueryPromise<any> {
            return nts.uk.request.exportFile(paths.exportSchedule, query);
        }

        export function getPeriod(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getPeriod);
        }

        export function findAllOutputItemMonthlyWorkSchedule(itemSelectionType: number)
                            : JQueryPromise<Array<model.OutputItemMonthlyWorkScheduleDto>> {
            return nts.uk.request.ajax(paths.findAllOutputItemMonthlyWorkSchedule + SLASH + itemSelectionType);
        }

        export function getCurrentLoginerRole(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getCurrentLoginerRole);
        }

        export function getFreeSettingAuthority(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getFreeSettingAuthority);
        }
        
        export module model {

            export interface OutputItemMonthlyWorkScheduleDto {
                itemCode: string;
                itemName: string;
                layoutId : string;
            }

            export interface MonthlyWorkScheduleQuery {
                endYearMonth: number;
                workplaceIds: Array<string>;
                condition: MonthlyWorkScheduleConditionDto;
                fileType: number;
                baseDate: string;
            }

            export interface MonthlyWorkScheduleConditionDto {
                companyId: string;
                userId: string;
                selectedCode: string;
                outputType: number;
                pageBreakIndicator: number;
                totalOutputSetting: WorkScheduleSettingTotalOutputDto;
                itemSettingType: number;
                displayType: number;
                itemDisplaySwitch: number;
                selectedCodeFreeSetting: string;
            }

            export interface WorkScheduleSettingTotalOutputDto {
                details: boolean;
                personalTotal: boolean;
                workplaceTotal: boolean;
                grossTotal: boolean;
                cumulativeWorkplace: boolean;
                workplaceHierarchyTotal: TotalWorkplaceHierachyDto;
            }

            export interface TotalWorkplaceHierachyDto {
                firstLevel: boolean;
                secondLevel: boolean;
                thirdLevel: boolean;
                fourthLevel: boolean;
                fifthLevel: boolean;
                sixthLevel: boolean;
                seventhLevel: boolean;
                eighthLevel: boolean;
                ninthLevel: boolean;
            }
        }

    }

}