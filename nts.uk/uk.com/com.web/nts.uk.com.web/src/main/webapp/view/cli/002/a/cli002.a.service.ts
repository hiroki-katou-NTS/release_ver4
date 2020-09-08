module nts.uk.com.view.cli002.a.service {
    import ajax = nts.uk.request.ajax;
        var servicePath: any = {
            findBySystem: "sys/portal/pginfomation/findBySystem",
            servicePath: "sys/portal/logsettings/update",
            findLogSettingBySystem: "sys/portal/logsettings/findBySystem"
        }

        export function findBySystem(systemType: number): JQueryPromise<any> {
            return ajax(`${servicePath.findBySystem}/${systemType}`);
        }

        export function updateLogSetting(logSettings: any): JQueryPromise<any> {
            return ajax(servicePath.servicePath, logSettings);
        }

        export function findLogSettingBySystem(systemType: number): JQueryPromise<any> {
            return ajax(`${servicePath.findLogSettingBySystem}/${systemType}`);
        }
  }