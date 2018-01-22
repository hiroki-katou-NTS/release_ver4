module nts.uk.at.view.kmk003.a {
    export module service {

        /**
        *  Service paths
        */
        let servicePath: any = {
            findWithCondition: "at/shared/worktimesetting/findwithcondition",
            findAllWorktime: "at/shared/worktimeset/findAll",
            findWorktimeSetingInfoByCode: "at/shared/worktimesetting/findInfo",
            getEnumWorktimeSeting: "at/shared/worktimeset/getenum",
            getPredByWorkTimeCode: "at/shared/pred/findByWorkTimeCode",
            savePred: "at/shared/pred/save",
            saveFlexWorkSetting: "at/shared/worktimesetting/flexset/save",
            saveFixedWorkSetting: "at/shared/worktimesetting/fixedset/save",
            saveFlowWorkSetting: "at/shared/worktimesetting/flowset/save",
            saveDiffTimeWorkSetting: "at/shared/worktimesetting/difftimeset/save",
            removeWorkTimeByCode: "at/shared/worktimesetting/remove"
        };

        /**
         * function find all work time set
         */
        export function findAllWorkTimeSet(): JQueryPromise<model.worktimeset.SimpleWorkTimeSettingDto[]> {
            return nts.uk.request.ajax(servicePath.findAllWorktime);
        }

        /**
         * Find worktimeset with condition
         */
        export function findWithCondition(condition: model.worktimeset.WorkTimeSettingCondition):
            JQueryPromise<model.worktimeset.SimpleWorkTimeSettingDto[]> {
            return nts.uk.request.ajax(servicePath.findWithCondition, condition);
        }
        
         /**
         * function find work time set by code
         */
        export function findWorktimeSetingInfoByCode(workTimeCode: string): JQueryPromise<model.common.WorkTimeSettingInfoDto> {
            return nts.uk.request.ajax(servicePath.findWorktimeSetingInfoByCode + '/' + workTimeCode);
        }
         /**
         * function get all enum by setting work time
         */
        export function getEnumWorktimeSeting(): JQueryPromise<model.worktimeset.WorkTimeSettingEnumDto> {
            return nts.uk.request.ajax(servicePath.getEnumWorktimeSeting);
        }

        export function getPredByWorkTimeCode(workTimeCode: string): JQueryPromise<model.worktimeset.WorkTimeSettingDto> {
            return nts.uk.request.ajax(servicePath.getPredByWorkTimeCode + "/" + workTimeCode);
        }
        
        export function savePred(data: any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.savePred, data);
        }
        /**
         * function find by work time code flex work setting data by call service
         */
        export function findByCodeFlexWorkSetting(worktimeCode: string): JQueryPromise<model.flexset.FlexWorkSettingDto> {
            return nts.uk.request.ajax(servicePath.findByCodeFlexWorkSetting + '/' + worktimeCode);
        } 
        
        /**
         * function save flex work setting by call service
         */
        export function saveFlexWorkSetting(command: model.command.FlexWorkSettingSaveCommand): JQueryPromise<void> {
            return nts.uk.request.ajax(servicePath.saveFlexWorkSetting, command);
        }

        /**
         * function save flex work setting by call service
         */
        export function saveFixedWorkSetting(command: model.command.FixedWorkSettingSaveCommand): JQueryPromise<void> {
            return nts.uk.request.ajax(servicePath.saveFixedWorkSetting, command);
        }
        
        /**
         * function save flow work setting by call service
         */
        export function saveFlowWorkSetting(command: model.command.FlowWorkSettingSaveCommand): JQueryPromise<void> {
            return nts.uk.request.ajax(servicePath.saveFlowWorkSetting, command);
        export function saveDiffTimeWorkSetting(command: model.command.DiffTimeWorkSettingSaveCommand): JQueryPromise<void> {
            return nts.uk.request.ajax(servicePath.saveDiffTimeWorkSetting, command);
        }
        
        /**
         * function remove work time by work time code
         */
        export function removeWorkTime(workTimeCode: string): JQueryPromise<void> {
            return nts.uk.request.ajax(servicePath.removeWorkTimeByCode, { workTimeCode: workTimeCode });
        }
    }
}
