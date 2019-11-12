module nts.uk.request {
    
    module csrf {
        let STORAGE_KEY_CSRF_TOKEN = "nts.uk.request.csrf.STORAGE_KEY_CSRF_TOKEN";
        
        cookie.get("nts.arc.CSRF_TOKEN")
            .ifPresent(csrfToken => {
                uk.sessionStorage.setItem(STORAGE_KEY_CSRF_TOKEN, csrfToken);
            });
        
        export function getToken() {
            return uk.sessionStorage.getItem(STORAGE_KEY_CSRF_TOKEN).orElse("");
        }
    }

    export var STORAGE_KEY_TRANSFER_DATA = "nts.uk.request.STORAGE_KEY_TRANSFER_DATA";

    export type WebAppId = 'comjs' | 'com' | 'pr' | 'at';
    export const WEB_APP_NAME = {
        comjs: 'nts.uk.com.js.web',
        com: 'nts.uk.com.web',
        pr: 'nts.uk.pr.web',
        at: 'nts.uk.at.web'
    };

    export class QueryString {

        items: { [key: string]: any };

        constructor() {
            this.items = {};
        }

        static parseUrl(url: string): QueryString {
            var instance = new QueryString();
            var queryString = url.split('?')[1];
            if (queryString) {
                var queryEntries = queryString.split('&');
                for (var i = 0; i < queryEntries.length; i++) {
                    var entryParts = queryEntries[i].split('=');
                    instance.set(entryParts[0], entryParts[1]);
                }
            }
            return instance;
        }

        static build(entriesObj: { [key: string]: any }) {
            var instance = new QueryString();

            for (var key in entriesObj) {
                instance.set(key, entriesObj[key]);
            }

            return instance;
        }

        get(key: string): any { 
            return this.items[key];
        }

        set(key: string, value: any) {
            if (key === null || key === undefined || key === '') {
                return;
            }
            this.items[key] = value;
        }

        remove(key: string) {
            delete this.items[key];
        }

        mergeFrom(otherObj: QueryString) {
            for (var otherKey in otherObj.items) {
                this.set(otherKey, otherObj.items[otherKey]);
            }
        }

        count() {
            var count = 0;
            for (var key in this.items) {
                count++;
            }
            return count;
        }

        hasItems() {
            return this.count() !== 0;
        }

        serialize() {
            var entryStrings = [];
            for (var key in this.items) {
                entryStrings.push(key + '=' + this.items[key]);
            }

            return entryStrings.join('&');
        }
    }

    /**
     * URL and QueryString
     */
    export class Locator {

        rawUrl: string;
        queryString: QueryString;

        constructor(url: string) {
            this.rawUrl = url.split('?')[0];
            this.queryString = QueryString.parseUrl(url);
        }

        serialize() {
            if (this.queryString.hasItems()) {
                return this.rawUrl + '?' + this.queryString.serialize();
            } else {
                return this.rawUrl;
            }
        }

        mergeRelativePath(relativePath) {
            var stack = this.rawUrl.split('/');
            var parts = relativePath.split('?')[0].split('/');
            var queryStringToAdd = QueryString.parseUrl(relativePath);

            // 最後のファイル名は除外
            // (最後がフォルダ名でしかも / で終わっていない場合は考慮しない)
            stack.pop();

            // relativePathの先頭が '/' の場合、それを取り除く
            if (parts[0] === '') {
                parts.shift();
            }

            for (var i = 0; i < parts.length; i++) {
                if (parts[i] === '.')
                    continue;
                if (parts[i] === '..')
                    stack.pop();
                else
                    stack.push(parts[i]);
            }

            var queryStringParts = queryStringToAdd.hasItems()
                ? '?' + queryStringToAdd.serialize()
                : '';

            return new Locator(stack.join('/') + queryStringParts);
        }
    }
    
    export function writeDynamicConstraint(codes: Array<string>){
        var dfd = $.Deferred();
        ajax("constraint/getlist", codes).done(function(data: Array<any>){
            if(nts.uk.util.isNullOrUndefined(__viewContext.primitiveValueConstraints)){
                __viewContext.primitiveValueConstraints = {};
            }
            _.forEach(data, function(item){
                __viewContext.primitiveValueConstraints[item.itemCode] = item;
            });
            dfd.resolve(data); 
        }).fail(function(error){
            dfd.reject(error);            
        });        
        return dfd.promise();
    }
    
    export module subSession {
        const SubSessionIdKey = "nts.uk.request.subSessionId.";
        const SecondsToKeepSubSession = 30;
        const SecondsIntervalToReportAlive = 3;
        export let currentId;
        if (uk.util.isInFrame()) {
            currentId = parent.window.nts.uk.request.subSession.currentId;
        } else {
            currentId = uk.util.randomId();
        }
         
        // keep alive sub sessions
        function keepAliveSubSessionId() {
            window.localStorage.setItem(SubSessionIdKey + currentId, +new Date());
        }
        keepAliveSubSessionId();
        setInterval(keepAliveSubSessionId, SecondsIntervalToReportAlive * 1000);
        
        export function getAliveIds() {
            let aliveIds = [];
            let deadIds = [];
            for (let i = 0; ; i++) {
                let key = window.localStorage.key(i);
                if (key == null) break;
                if (key.indexOf(SubSessionIdKey) !== 0) continue;
                
                let id = key.slice(SubSessionIdKey.length);
                let lastReportTime = window.localStorage.getItem(SubSessionIdKey + id);
                let duration = +new Date() - lastReportTime;
                if (duration <= SecondsToKeepSubSession * 1000) {
                    aliveIds.push(id);
                } else {
                    deadIds.push(id);
                }
            }
            
            // prune dead IDs
            deadIds.forEach(deadId => {
                window.localStorage.removeItem(SubSessionIdKey + deadId);
            });
            
            return aliveIds;
        }
    }

    export function ajax(path: string, data?: any, options?: any);
    export function ajax(webAppId: WebAppId, path: string, data?: any, options?: any, restoresSession?: boolean) {

        if (typeof arguments[1] !== 'string') {
            return ajax.apply(null, _.concat(location.currentAppId, arguments));
        }

        var dfd = $.Deferred();
        options = options || {};
        
        restoresSession = restoresSession !== false;

        if (typeof data === 'object') {
            data = JSON.stringify(data);
        }

        var webserviceLocator = location.siteRoot
            .mergeRelativePath(WEB_APP_NAME[webAppId] + '/')
            .mergeRelativePath(location.ajaxRootDir)
            .mergeRelativePath(path);
        
        var countRetryByDeadLock = 0;
        
        function ajaxFunc() {
            $.ajax({
                type: options.method || 'POST',
                contentType: options.contentType || 'application/json',
                url: webserviceLocator.serialize(),
                dataType: options.dataType || 'json',
                data: data,
                headers: {
                    'PG-Path': location.current.serialize(),
                    "X-CSRF-TOKEN": csrf.getToken(),
                    "X-SubSessionId": subSession.currentId,
                    "X-AliveSubSessionIds": subSession.getAliveIds()
                }
            }).done(function(res) {
                if (nts.uk.util.exception.isErrorToReject(res)) {
                    dfd.reject(res);
                } else if (res !== undefined && res.commandResult === true) {
                    dfd.resolve(res.value);
                } else {
                    dfd.resolve(res);
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                // デッドロックの場合、待機時間を少しずつ増やしながらリトライ（とりあえず10回までとする）
                if (jqXHR.responseJSON && jqXHR.responseJSON.deadLock === true && countRetryByDeadLock < 10) {
//                    countRetryByDeadLock++;
//                    setTimeout(ajaxFunc, 300 + countRetryByDeadLock * 100);
                    jqXHR.responseJSON.message = "アクセスが集中しています。少し時間を空けてからもう一度登録してください";
                    dfd.reject(jqXHR.responseJSON);
                    return;
                }
                AjaxErrorHandlers.main(jqXHR, textStatus, errorThrown);
            });
        }
        
        if (restoresSession && webAppId != nts.uk.request.location.currentAppId) {
            doTaskShareingSesion(webAppId, ajaxFunc);
        } else {
            ajaxFunc();
        }

        return dfd.promise();
    }
    export function syncAjax(path: string, data?: any, options?: any);
    export function syncAjax(webAppId: WebAppId, path: string, data?: any, options?: any) {

        if (typeof arguments[1] !== 'string') {
            return syncAjax.apply(null, _.concat(location.currentAppId, arguments));
        }

        var dfd = $.Deferred();
        options = options || {};

        if (typeof data === 'object') {
            data = JSON.stringify(data);
        }

        var webserviceLocator = location.siteRoot
            .mergeRelativePath(WEB_APP_NAME[webAppId] + '/')
            .mergeRelativePath(location.ajaxRootDir)
            .mergeRelativePath(path);

        function ajaxFunc() {
            $.ajax({
                type: options.method || 'POST',
                contentType: options.contentType || 'application/json',
                url: webserviceLocator.serialize(),
                dataType: options.dataType || 'json',
                data: data,
                async: false,
                headers: {
                    'PG-Path': location.current.serialize(),
                    "X-CSRF-TOKEN": csrf.getToken(),
                    "X-SubSessionId": subSession.currentId,
                    "X-AliveSubSessionIds": subSession.getAliveIds()
                },
                success: function(res) {
                    if (nts.uk.util.exception.isErrorToReject(res)) {
                        dfd.reject(res);
                    } else if (res !== undefined && res.commandResult === true) {
                        dfd.resolve(res.value);
                    } else {
                        dfd.resolve(res);
                    }
                },
                error: function(xhr,status, error) {
                    AjaxErrorHandlers.main(xhr, status, error);
                }
            });
        }
        
        if (webAppId != nts.uk.request.location.currentAppId) {
            doTaskShareingSesion(webAppId, ajaxFunc);
        } else {
            ajaxFunc();
        }

        return dfd.promise();
    }
    
    module AjaxErrorHandlers {
        export function main(xhr, status, error) {
                
            switch (xhr.status) {
                case 401:
                    handle401(xhr);
                    break;
                default:
                    handleUnknownError(xhr, status, error);
                    break;
            }
        }
        
        function handle401(xhr) {
            let res = xhr.responseJSON;
            
            // res.sessionTimeout || res.csrfError
            specials.errorPages.sessionTimeout();
        }
        
        function handleUnknownError(xhr, status, error) {
            console.log("request failed");
            console.log(arguments);
            specials.errorPages.systemError(xhr.responseJSON);
        }
    }
    
    function doTaskShareingSesion(webAppId: WebAppId, task: () => void) {
         login.keepSerializedSession()
            .then(() => {
                return login.restoreSessionTo(webAppId);
            })
            .then(() => {
                task();
            });
    }
	
    export function uploadFile(data: FormData, option?: any): JQueryPromise<any> {
        return $.ajax({
            url: "/nts.uk.com.web/webapi/ntscommons/arc/filegate/upload",
            type: 'POST',
            data: data,
            cache: false,
            contentType: false,
            processData: false
        });
    }

    export function exportFile(path: string, data?: any, options?: any) {
        let dfd = $.Deferred();

        ajax(path, data, options)
            .then((res: any) => {
                return deferred.repeat(conf => conf
                    .task(() => specials.getAsyncTaskInfo(res.taskId))
                    .while(info => info.pending || info.running)
                    .pause(1000));
            })
            .done((res: any) => {
                if (res.failed || res.status == "ABORTED") {
                    dfd.reject(res.error);
                } else {
                    specials.donwloadFile(res.id);
                    dfd.resolve(res);
                }
            })
            .fail((res: any) => {
                dfd.reject(res);
            });

        return dfd.promise();
    }
    
    export function downloadFileWithTask(taskId: string, data?: any, options?: any) {
        let dfd = $.Deferred();

        var checkTask = function(){
            specials.getAsyncTaskInfo(taskId).done((res: any) => {
                if (res.status == "PENDING" || res.status == "RUNNING") {
                    setTimeout(function(){ 
                     checkTask();       
                    }, 1000)
                } if (res.failed || res.status == "ABORTED") { 
                        dfd.reject(res.error);
                } else {
                    specials.donwloadFile(res.id);
                    dfd.resolve(res);
                }
            }).fail(res => {
                dfd.reject(res);
            });
        };
        
        checkTask();

        return dfd.promise();
    }
    
    export module asyncTask {
        export function getInfo(taskId: string) {
            return ajax('/ntscommons/arc/task/async/info/' + taskId);
        }
        
        export function requestToCancel(taskId: string) {
            return ajax('/ntscommons/arc/task/async/requesttocancel/' + taskId);
        }
    }
    
    export module file {
        
        export function donwload(fileId: string) {
            var dfd = $.Deferred();
            $.fileDownload(pathToGet(fileId), {
                successCallback: function(url) {
                    dfd.resolve();
                },
                failCallback: function(responseHtml, url) {
                    var responseError = $(responseHtml);
                    var error = JSON.parse(responseError.text());
                    dfd.reject(error);
                }
            });
            
            return dfd.promise();
        }
        
        export function liveViewUrl(fileId: string): string;
        export function liveViewUrl(fileId: string, entryName?: string): string {
            let liveViewPath = "/webapi/shr/infra/file/storage/liveview/";
            let locator = location.siteRoot
                .mergeRelativePath(WEB_APP_NAME.com + '/')
                .mergeRelativePath(liveViewPath);
                
            if (arguments.length === 1) {
                return locator.mergeRelativePath(fileId).serialize();
            } else if (arguments.length === 2) {
                return locator.mergeRelativePath(fileId + "/").mergeRelativePath(entryName).serialize();
            }
        }

        export function remove(fileId: string) {
            return ajax("com", "/shr/infra/file/storage/delete/" + fileId);
        }
        
        export function isExist(fileId: string) {
            return ajax("com", "/shr/infra/file/storage/isexist/" + fileId);
        }
        
        export function pathToGet(fileId: string) {
            return resolvePath('/webapi/shr/infra/file/storage/get/' + fileId);
        }
    }

    export function liveView(fileId: string): string {
        return file.liveViewUrl(fileId);
    }
    

    export module specials {

        export function getAsyncTaskInfo(taskId: string) {
            return asyncTask.getInfo(taskId);
        }

        export function donwloadFile(fileId: string) {
            return file.donwload(fileId);
        }
        
        export function deleteFile(fileId: string) {
            return file.remove(fileId);
        }
        
        export function isFileExist(fileId: string) {
            return file.isExist(fileId);
        }
        
        export module errorPages {
            
            export function systemError(error) {
                if ($(".nts-system-error-dialog").length !== 0) {
                    return;
                }
                
                ui.windows.setShared("errorInfo", error);
                let sub = (<any>ui).windows.sub.modal("com", "/view/common/error/system/index.xhtml", {
                    resizable: true
                });
                sub.$dialog.addClass("nts-system-error-dialog");
            }
            
            export function sessionTimeout() {
                jump('com', '/view/common/error/sessiontimeout/index.xhtml');
            }
            
        }
    }
    
    export function jumpFromDialogOrFrame(path: string, data?: any);
    export function jumpFromDialogOrFrame(webAppId: WebAppId, path: string, data?: any) {
        let self: nts.uk.ui.windows.ScreenWindow;
        if(nts.uk.util.isInFrame()){
            self = nts.uk.ui.windows.getSelf();
        } else {
            self = window.parent.nts.uk.ui.windows.getSelf();    
        }
        while(!self.isRoot){
            self = self.parent;
        }
        self.globalContext.nts.uk.request.jump(webAppId, path, data);
    }


    export function jump(path: string, data?: any);
    export function jump(webAppId: WebAppId, path: string, data?: any) {
        
        uk.ui.block.invisible();
        
        // handle overload
        if (typeof arguments[1] !== 'string') {
            jump.apply(null, _.concat(nts.uk.request.location.currentAppId, arguments));
            return;
        }
        
        if (webAppId != nts.uk.request.location.currentAppId) {
            jumpToOtherWebApp.apply(this, arguments);
            return;
        }
        
        if (data === undefined) {
            uk.sessionStorage.removeItem(STORAGE_KEY_TRANSFER_DATA)
        } else {
            uk.sessionStorage.setItemAsJson(STORAGE_KEY_TRANSFER_DATA, data);
        }

        window.location.href = resolvePath(path);
    }
    
    function jumpToOtherWebApp(webAppId: WebAppId, path: string, data?: any) {
        
        let resolvedPath = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME[webAppId] + '/')
                .mergeRelativePath(path).serialize();
        
        if (data === undefined) {
            uk.sessionStorage.removeItem(STORAGE_KEY_TRANSFER_DATA)
        } else {
            uk.sessionStorage.setItemAsJson(STORAGE_KEY_TRANSFER_DATA, data);
        }
        
        login.keepSerializedSession()
            .then(() => {
                return login.restoreSessionTo(webAppId);
            })
            .then(() => {
                window.location.href = resolvedPath;
            });
    }
    
    export function jumpToMenu(path: string) {
        let end = path.charAt(0) === '/' ? path.indexOf("/", 1) : path.indexOf("/");
        let appName = path.substring(0, end);
        let appId;
        switch(appName) {
            case WEB_APP_NAME.com:
            case "/" + WEB_APP_NAME.com:
                appId = "com";
                break;
            case WEB_APP_NAME.pr:
            case "/" + WEB_APP_NAME.pr:
                appId = "pr";
                break;
            case WEB_APP_NAME.at:
            case "/" + WEB_APP_NAME.at:
                appId = "at";
                break;
        }
        
        var d = new Date();
        d.setTime(d.getTime() + (10 * 60 * 1000));
//        $.cookie('startfrommenu', "true", { expires: d });
        document.cookie = "startfrommenu=true";
        
        jump(appId, path.substr(end));
    }
    
    export module login {
        
        let STORAGE_KEY_USED_LOGIN_PAGE = "nts.uk.request.login.STORAGE_KEY_USED_LOGIN_PAGE";
        let STORAGE_KEY_SERIALIZED_SESSION = "nts.uk.request.login.STORAGE_KEY_SERIALIZED_SESSION";
        
        export function keepUsedLoginPage(webAppId: WebAppId, path: string);
        export function keepUsedLoginPage(url?: string) {
            if (arguments.length === 2) {
                let loginLocator = location.siteRoot
                                        .mergeRelativePath(WEB_APP_NAME[arguments[0]] + '/')
                                        .mergeRelativePath(arguments[1]);
                keepUsedLoginPage.apply(null, [ loginLocator.serialize() ]);
                return;
            }
            
            if (url === undefined) {
                keepUsedLoginPage(location.current.serialize());
                return;
            }
            
            uk.sessionStorage.setItem(STORAGE_KEY_USED_LOGIN_PAGE, url);
        }
        
        export function jumpToUsedLoginPage() {
            uk.sessionStorage.getItem(STORAGE_KEY_USED_LOGIN_PAGE).ifPresent(path => {
                window.location.href = path;
            }).ifEmpty(() => {
                request.jump('com', '/view/ccg/007/d/index.xhtml');
            });
        }
        
        export function jumpToUsedSSOLoginPage() {
            uk.sessionStorage.getItem(STORAGE_KEY_USED_LOGIN_PAGE).ifPresent(path => {
                window.location.href = path;
            }).ifEmpty(() => {
                request.jump('com', '/view/ccg/007/d/index.xhtml?signon=on');
            });
        }
        
        export function keepSerializedSession() {
            let dfd = $.Deferred();
            dfd.resolve();
//            request.ajax("/shr/web/session/serialize").done(res => {
//                uk.sessionStorage.setItem(STORAGE_KEY_SERIALIZED_SESSION, res);
//                dfd.resolve();
//            });
//            
            return dfd.promise();
        }
        
        export function restoreSessionTo(webAppId: WebAppId) {
            let dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
            
//            let serializedTicket = uk.sessionStorage.getItem(STORAGE_KEY_SERIALIZED_SESSION).get();
//            return (<any>request).ajax(webAppId, "/shr/web/session/restore", serializedTicket, null, false);
        }
    }
    
    export function jumpToTopPage() {
        jumpToMenu('nts.uk.com.web/view/ccg/008/a/index.xhtml');
    }

    export function resolvePath(path: string) {
        var destination: Locator;
        if (path.charAt(0) === '/') {
            destination = location.appRoot.mergeRelativePath(path);
        } else {
            destination = location.current.mergeRelativePath(path);
        }

        return destination.serialize();
    }
    
    export module location {
        export var current = new Locator(window.location.href);
        export var appRoot = current.mergeRelativePath(__viewContext.rootPath);
        export var siteRoot = appRoot.mergeRelativePath('../');
        export var ajaxRootDir = 'webapi/';

        var currentAppName = _.takeRight(appRoot.serialize().split('/'), 2)[0];
        export var currentAppId: WebAppId;
        for (var id in WEB_APP_NAME) {
            if (currentAppName === WEB_APP_NAME[id]) {
                currentAppId = <WebAppId>id;
                break;
            }
        }
    };

}
