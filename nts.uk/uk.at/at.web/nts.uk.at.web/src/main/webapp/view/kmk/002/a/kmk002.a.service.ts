module nts.uk.at.view.kmk002.a {
    export module service {

        /**
         *  Service paths
         */
        let servicePath: any = {
            findOptionalItemDetail: 'ctx/at/record/optionalitem/find',
            findOptionalItemHeaders: 'ctx/at/record/optionalitem/findall',
            saveOptionalItem: 'ctx/at/record/optionalitem/save',
            getOptItemEnum: 'ctx/at/record/optionalitem/getenum'
        };

        export function getOptItemEnum(): JQueryPromise<model.OptItemEnumDto> {
            return nts.uk.request.ajax(servicePath.getOptItemEnum);
        }

        export function saveOptionalItem(command: model.OptionalItemDto): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.saveOptionalItem, command);
        }

        export function findOptionalItemDetail(itemNo: string): JQueryPromise<model.OptionalItemDto> {
            return nts.uk.request.ajax(servicePath.findOptionalItemDetail + '/' + itemNo);
        }

        export function findOptionalItemHeaders(): JQueryPromise<Array<model.OptionalItemHeader>> {
            return nts.uk.request.ajax(servicePath.findOptionalItemHeaders);
        }

        /**
         * Data Model
         */
        export module model {
            export interface OptionalItemHeader {
                itemNo: string;
                itemName: string;
                usageAtr: number;
                performanceAtr: number;
            }

            /**
             * OptionalItemDto
             */
            export interface OptionalItemDto {
                optionalItemNo: string;
                optionalItemName: string;
                optionalItemAtr: number;
                usageAtr: number;
                empConditionAtr: number;
                performanceAtr: number;
                calcResultRange: CalcResultRangeDto;
                formulas: Array<FormulaDto>;
            }
            /**
             * CalcResultRangeDto
             */
            export interface CalcResultRangeDto {
                upperCheck: boolean;
                lowerCheck: boolean;
                numberUpper: number;
                numberLower: number;
                amountUpper: number;
                amountLower: number;
                timeUpper: number;
                timeLower: number;
            }
            /**
             * CalcFormulaDto
             */
            export interface FormulaDto {
                formulaId: string;
                formulaName: string;
                optionalItemNo: string;
                orderNo: number;
                symbolValue: string;
                formulaAtr: number;
                calcAtr: number;
                formulaSetting: FormulaSettingDto;
                itemSelection: ItemSelectionDto;
                monthlyRounding: RoundingDto;
                dailyRounding: RoundingDto;
            }
            /**
             * RoundingDto
             */
            export interface RoundingDto {
                numberRounding: number;
                numberUnit: number;
                timeRounding: number;
                timeUnit: number;
                amountRounding: number;
                amountUnit: number;
            }
            /**
             * FormulaSettingDto
             */
            export interface FormulaSettingDto {
                minusSegment: number;
                operator: number;
                leftItem: SettingItemDto;
                rightItem: SettingItemDto;
            }
            /**
             * SettingItemDto
             */
            export interface SettingItemDto {
                settingMethod: number;
                dispOrder: number;
                inputValue: number;
                formulaItemId: string;
            }
            /**
             * ItemSelectionDto
             */
            export interface ItemSelectionDto {
                minusSegment: number;
                attendanceItems: Array<AttendanceItemDto>;
            }
            /**
             * SelectedAttendanceItemDto
             */
            export interface AttendanceItemDto {
                attendanceItemId: number;
                operator: number;
                operatorText?: string;
                attendanceItemName?: string;
            }
            export interface RoundingDto {
                unit: EnumConstantDto[];
                rounding: EnumConstantDto[];
            }
            export interface OptItemEnumDto {
                itemAtr: EnumConstantDto[];
                useAtr: EnumConstantDto[];
                empConditionAtr: EnumConstantDto[];
                performanceAtr: EnumConstantDto[];
                formulaAtr: EnumConstantDto[];
                calcAtr: EnumConstantDto[];
                minusSegment: EnumConstantDto[];
                operatorAtr: EnumConstantDto[];
                settingMethod: EnumConstantDto[];
                dispOrder: EnumConstantDto[];
                addSubAtr: EnumConstantDto[];
                amountRounding: RoundingDto;
                timeRounding: RoundingDto;
                numberRounding: RoundingDto;
            }
            export interface EnumConstantDto {
                value: number;
                fieldName: string;
                localizedName: string;
            }

            export class EnumAdaptor {

                /**
                 * Get number value of fieldName.
                 * @param: list enum
                 */
                public static valueOf(fieldName: string, enumConstant: EnumConstantDto[]): number {
                    let result;
                    let found = _.find(enumConstant, item => item.fieldName == fieldName);
                    if (found) {
                        result = found.value;
                    }
                    return result;
                }

                /**
                 * Get localizedName of number value.
                 * @param: list enum
                 */
                public static localizedNameOf(value: number, enumConstant: EnumConstantDto[]): string {
                    let result;
                    let found = _.find(enumConstant, item => item.value == value);
                    if (found) {
                        result = found.localizedName;
                    }
                    return result;
                }
            }
        }

    }
}
