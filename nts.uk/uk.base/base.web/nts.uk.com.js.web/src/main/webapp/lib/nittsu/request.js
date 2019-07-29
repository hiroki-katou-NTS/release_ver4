var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var request;
        (function (request) {
            var csrf;
            (function (csrf) {
                var STORAGE_KEY_CSRF_TOKEN = "nts.uk.request.csrf.STORAGE_KEY_CSRF_TOKEN";
                uk.cookie.get("nts.arc.CSRF_TOKEN")
                    .ifPresent(function (csrfToken) {
                    uk.sessionStorage.setItem(STORAGE_KEY_CSRF_TOKEN, csrfToken);
                });
                function getToken() {
                    return uk.sessionStorage.getItem(STORAGE_KEY_CSRF_TOKEN).orElse("");
                }
                csrf.getToken = getToken;
            })(csrf || (csrf = {}));
            request.STORAGE_KEY_TRANSFER_DATA = "nts.uk.request.STORAGE_KEY_TRANSFER_DATA";
            request.WEB_APP_NAME = {
                comjs: 'nts.uk.com.js.web',
                com: 'nts.uk.com.web',
                pr: 'nts.uk.pr.web',
                at: 'nts.uk.at.web'
            };
            var QueryString = (function () {
                function QueryString() {
                    this.items = {};
                }
                QueryString.parseUrl = function (url) {
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
                };
                QueryString.build = function (entriesObj) {
                    var instance = new QueryString();
                    for (var key in entriesObj) {
                        instance.set(key, entriesObj[key]);
                    }
                    return instance;
                };
                QueryString.prototype.get = function (key) {
                    return this.items[key];
                };
                QueryString.prototype.set = function (key, value) {
                    if (key === null || key === undefined || key === '') {
                        return;
                    }
                    this.items[key] = value;
                };
                QueryString.prototype.remove = function (key) {
                    delete this.items[key];
                };
                QueryString.prototype.mergeFrom = function (otherObj) {
                    for (var otherKey in otherObj.items) {
                        this.set(otherKey, otherObj.items[otherKey]);
                    }
                };
                QueryString.prototype.count = function () {
                    var count = 0;
                    for (var key in this.items) {
                        count++;
                    }
                    return count;
                };
                QueryString.prototype.hasItems = function () {
                    return this.count() !== 0;
                };
                QueryString.prototype.serialize = function () {
                    var entryStrings = [];
                    for (var key in this.items) {
                        entryStrings.push(key + '=' + this.items[key]);
                    }
                    return entryStrings.join('&');
                };
                return QueryString;
            }());
            request.QueryString = QueryString;
            /**
             * URL and QueryString
             */
            var Locator = (function () {
                function Locator(url) {
                    this.rawUrl = url.split('?')[0];
                    this.queryString = QueryString.parseUrl(url);
                }
                Locator.prototype.serialize = function () {
                    if (this.queryString.hasItems()) {
                        return this.rawUrl + '?' + this.queryString.serialize();
                    }
                    else {
                        return this.rawUrl;
                    }
                };
                Locator.prototype.mergeRelativePath = function (relativePath) {
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
                };
                return Locator;
            }());
            request.Locator = Locator;
            function writeDynamicConstraint(codes) {
                var dfd = $.Deferred();
                ajax("constraint/getlist", codes).done(function (data) {
                    if (nts.uk.util.isNullOrUndefined(__viewContext.primitiveValueConstraints)) {
                        __viewContext.primitiveValueConstraints = {};
                    }
                    _.forEach(data, function (item) {
                        __viewContext.primitiveValueConstraints[item.itemCode] = item;
                    });
                    dfd.resolve(data);
                }).fail(function (error) {
                    dfd.reject(error);
                });
                return dfd.promise();
            }
            request.writeDynamicConstraint = writeDynamicConstraint;
            function ajax(webAppId, path, data, options, restoresSession) {
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
                    .mergeRelativePath(request.WEB_APP_NAME[webAppId] + '/')
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
                            "X-CSRF-TOKEN": csrf.getToken()
                        }
                    }).done(function (res) {
                        if (nts.uk.util.exception.isErrorToReject(res)) {
                            dfd.reject(res);
                        }
                        else if (res !== undefined && res.commandResult === true) {
                            dfd.resolve(res.value);
                        }
                        else {
                            dfd.resolve(res);
                        }
                    }).fail(function (jqXHR, textStatus, errorThrown) {
                        // デッドロックの場合、待機時間を少しずつ増やしながらリトライ（とりあえず10回までとする）
                        if (jqXHR.responseJSON && jqXHR.responseJSON.deadLock === true && countRetryByDeadLock < 10) {
                            countRetryByDeadLock++;
                            setTimeout(ajaxFunc, 300 + countRetryByDeadLock * 100);
                            return;
                        }
                        AjaxErrorHandlers.main(jqXHR, textStatus, errorThrown);
                    });
                }
                if (restoresSession && webAppId != nts.uk.request.location.currentAppId) {
                    doTaskShareingSesion(webAppId, ajaxFunc);
                }
                else {
                    ajaxFunc();
                }
                return dfd.promise();
            }
            request.ajax = ajax;
            function syncAjax(webAppId, path, data, options) {
                if (typeof arguments[1] !== 'string') {
                    return syncAjax.apply(null, _.concat(location.currentAppId, arguments));
                }
                var dfd = $.Deferred();
                options = options || {};
                if (typeof data === 'object') {
                    data = JSON.stringify(data);
                }
                var webserviceLocator = location.siteRoot
                    .mergeRelativePath(request.WEB_APP_NAME[webAppId] + '/')
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
                            "X-CSRF-TOKEN": csrf.getToken()
                        },
                        success: function (res) {
                            if (nts.uk.util.exception.isErrorToReject(res)) {
                                dfd.reject(res);
                            }
                            else if (res !== undefined && res.commandResult === true) {
                                dfd.resolve(res.value);
                            }
                            else {
                                dfd.resolve(res);
                            }
                        },
                        error: function (xhr, status, error) {
                            AjaxErrorHandlers.main(xhr, status, error);
                        }
                    });
                }
                if (webAppId != nts.uk.request.location.currentAppId) {
                    doTaskShareingSesion(webAppId, ajaxFunc);
                }
                else {
                    ajaxFunc();
                }
                return dfd.promise();
            }
            request.syncAjax = syncAjax;
            var AjaxErrorHandlers;
            (function (AjaxErrorHandlers) {
                function main(xhr, status, error) {
                    switch (xhr.status) {
                        case 401:
                            handle401(xhr);
                            break;
                        default:
                            handleUnknownError(xhr, status, error);
                            break;
                    }
                }
                AjaxErrorHandlers.main = main;
                function handle401(xhr) {
                    var res = xhr.responseJSON;
                    // res.sessionTimeout || res.csrfError
                    specials.errorPages.sessionTimeout();
                }
                function handleUnknownError(xhr, status, error) {
                    console.log("request failed");
                    console.log(arguments);
                    specials.errorPages.systemError(xhr.responseJSON);
                }
            })(AjaxErrorHandlers || (AjaxErrorHandlers = {}));
            function doTaskShareingSesion(webAppId, task) {
                login.keepSerializedSession()
                    .then(function () {
                    return login.restoreSessionTo(webAppId);
                })
                    .then(function () {
                    task();
                });
            }
            function uploadFile(data, option) {
                return $.ajax({
                    url: "/nts.uk.com.web/webapi/ntscommons/arc/filegate/upload",
                    type: 'POST',
                    data: data,
                    cache: false,
                    contentType: false,
                    processData: false
                });
            }
            request.uploadFile = uploadFile;
            function exportFile(path, data, options) {
                var dfd = $.Deferred();
                ajax(path, data, options)
                    .then(function (res) {
                    return uk.deferred.repeat(function (conf) { return conf
                        .task(function () { return specials.getAsyncTaskInfo(res.taskId); })
                        .while(function (info) { return info.pending || info.running; })
                        .pause(1000); });
                })
                    .done(function (res) {
                    if (res.failed || res.status == "ABORTED") {
                        dfd.reject(res.error);
                    }
                    else {
                        specials.donwloadFile(res.id);
                        dfd.resolve(res);
                    }
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            request.exportFile = exportFile;
            function downloadFileWithTask(taskId, data, options) {
                var dfd = $.Deferred();
                var checkTask = function () {
                    specials.getAsyncTaskInfo(taskId).done(function (res) {
                        if (res.status == "PENDING" || res.status == "RUNNING") {
                            setTimeout(function () {
                                checkTask();
                            }, 1000);
                        }
                        if (res.failed || res.status == "ABORTED") {
                            dfd.reject(res.error);
                        }
                        else {
                            specials.donwloadFile(res.id);
                            dfd.resolve(res);
                        }
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                };
                checkTask();
                return dfd.promise();
            }
            request.downloadFileWithTask = downloadFileWithTask;
            var asyncTask;
            (function (asyncTask) {
                function getInfo(taskId) {
                    return ajax('/ntscommons/arc/task/async/info/' + taskId);
                }
                asyncTask.getInfo = getInfo;
                function requestToCancel(taskId) {
                    return ajax('/ntscommons/arc/task/async/requesttocancel/' + taskId);
                }
                asyncTask.requestToCancel = requestToCancel;
            })(asyncTask = request.asyncTask || (request.asyncTask = {}));
            var file;
            (function (file) {
                function donwload(fileId) {
                    var dfd = $.Deferred();
                    $.fileDownload(pathToGet(fileId), {
                        successCallback: function (url) {
                            dfd.resolve();
                        },
                        failCallback: function (responseHtml, url) {
                            var responseError = $(responseHtml);
                            var error = JSON.parse(responseError.text());
                            dfd.reject(error);
                        }
                    });
                    return dfd.promise();
                }
                file.donwload = donwload;
                function liveViewUrl(fileId, entryName) {
                    var liveViewPath = "/webapi/shr/infra/file/storage/liveview/";
                    var locator = location.siteRoot
                        .mergeRelativePath(request.WEB_APP_NAME.com + '/')
                        .mergeRelativePath(liveViewPath);
                    if (arguments.length === 1) {
                        return locator.mergeRelativePath(fileId).serialize();
                    }
                    else if (arguments.length === 2) {
                        return locator.mergeRelativePath(fileId + "/").mergeRelativePath(entryName).serialize();
                    }
                }
                file.liveViewUrl = liveViewUrl;
                function remove(fileId) {
                    return ajax("com", "/shr/infra/file/storage/delete/" + fileId);
                }
                file.remove = remove;
                function isExist(fileId) {
                    return ajax("com", "/shr/infra/file/storage/isexist/" + fileId);
                }
                file.isExist = isExist;
                function pathToGet(fileId) {
                    return resolvePath('/webapi/shr/infra/file/storage/get/' + fileId);
                }
                file.pathToGet = pathToGet;
            })(file = request.file || (request.file = {}));
            function liveView(fileId) {
                return file.liveViewUrl(fileId);
            }
            request.liveView = liveView;
            var specials;
            (function (specials) {
                function getAsyncTaskInfo(taskId) {
                    return asyncTask.getInfo(taskId);
                }
                specials.getAsyncTaskInfo = getAsyncTaskInfo;
                function donwloadFile(fileId) {
                    return file.donwload(fileId);
                }
                specials.donwloadFile = donwloadFile;
                function deleteFile(fileId) {
                    return file.remove(fileId);
                }
                specials.deleteFile = deleteFile;
                function isFileExist(fileId) {
                    return file.isExist(fileId);
                }
                specials.isFileExist = isFileExist;
                var errorPages;
                (function (errorPages) {
                    function systemError(error) {
                        if ($(".nts-system-error-dialog").length !== 0) {
                            return;
                        }
                        uk.ui.windows.setShared("errorInfo", error);
                        var sub = uk.ui.windows.sub.modal("com", "/view/common/error/system/index.xhtml", {
                            resizable: true
                        });
                        sub.$dialog.addClass("nts-system-error-dialog");
                    }
                    errorPages.systemError = systemError;
                    function sessionTimeout() {
                        jump('com', '/view/common/error/sessiontimeout/index.xhtml');
                    }
                    errorPages.sessionTimeout = sessionTimeout;
                })(errorPages = specials.errorPages || (specials.errorPages = {}));
            })(specials = request.specials || (request.specials = {}));
            function jumpFromDialogOrFrame(webAppId, path, data) {
                var self;
                if (nts.uk.util.isInFrame()) {
                    self = nts.uk.ui.windows.getSelf();
                }
                else {
                    self = window.parent.nts.uk.ui.windows.getSelf();
                }
                while (!self.isRoot) {
                    self = self.parent;
                }
                self.globalContext.nts.uk.request.jump(webAppId, path, data);
            }
            request.jumpFromDialogOrFrame = jumpFromDialogOrFrame;
            function jump(webAppId, path, data) {
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
                    uk.sessionStorage.removeItem(request.STORAGE_KEY_TRANSFER_DATA);
                }
                else {
                    uk.sessionStorage.setItemAsJson(request.STORAGE_KEY_TRANSFER_DATA, data);
                }
                window.location.href = resolvePath(path);
            }
            request.jump = jump;
            function jumpToOtherWebApp(webAppId, path, data) {
                var resolvedPath = nts.uk.request.location.siteRoot
                    .mergeRelativePath(nts.uk.request.WEB_APP_NAME[webAppId] + '/')
                    .mergeRelativePath(path).serialize();
                if (data === undefined) {
                    uk.sessionStorage.removeItem(request.STORAGE_KEY_TRANSFER_DATA);
                }
                else {
                    uk.sessionStorage.setItemAsJson(request.STORAGE_KEY_TRANSFER_DATA, data);
                }
                login.keepSerializedSession()
                    .then(function () {
                    return login.restoreSessionTo(webAppId);
                })
                    .then(function () {
                    window.location.href = resolvedPath;
                });
            }
            function jumpToMenu(path) {
                var end = path.charAt(0) === '/' ? path.indexOf("/", 1) : path.indexOf("/");
                var appName = path.substring(0, end);
                var appId;
                switch (appName) {
                    case request.WEB_APP_NAME.com:
                    case "/" + request.WEB_APP_NAME.com:
                        appId = "com";
                        break;
                    case request.WEB_APP_NAME.pr:
                    case "/" + request.WEB_APP_NAME.pr:
                        appId = "pr";
                        break;
                    case request.WEB_APP_NAME.at:
                    case "/" + request.WEB_APP_NAME.at:
                        appId = "at";
                        break;
                }
                var d = new Date();
                d.setTime(d.getTime() + (10 * 60 * 1000));
                //        $.cookie('startfrommenu', "true", { expires: d });
                document.cookie = "startfrommenu=true";
                jump(appId, path.substr(end));
            }
            request.jumpToMenu = jumpToMenu;
            var login;
            (function (login) {
                var STORAGE_KEY_USED_LOGIN_PAGE = "nts.uk.request.login.STORAGE_KEY_USED_LOGIN_PAGE";
                var STORAGE_KEY_SERIALIZED_SESSION = "nts.uk.request.login.STORAGE_KEY_SERIALIZED_SESSION";
                function keepUsedLoginPage(url) {
                    if (arguments.length === 2) {
                        var loginLocator = location.siteRoot
                            .mergeRelativePath(request.WEB_APP_NAME[arguments[0]] + '/')
                            .mergeRelativePath(arguments[1]);
                        keepUsedLoginPage.apply(null, [loginLocator.serialize()]);
                        return;
                    }
                    if (url === undefined) {
                        keepUsedLoginPage(location.current.serialize());
                        return;
                    }
                    uk.sessionStorage.setItem(STORAGE_KEY_USED_LOGIN_PAGE, url);
                }
                login.keepUsedLoginPage = keepUsedLoginPage;
                function jumpToUsedLoginPage() {
                    uk.sessionStorage.getItem(STORAGE_KEY_USED_LOGIN_PAGE).ifPresent(function (path) {
                        window.location.href = path;
                    }).ifEmpty(function () {
                        request.jump('com', '/view/ccg/007/d/index.xhtml');
                    });
                }
                login.jumpToUsedLoginPage = jumpToUsedLoginPage;
                function jumpToUsedSSOLoginPage() {
                    uk.sessionStorage.getItem(STORAGE_KEY_USED_LOGIN_PAGE).ifPresent(function (path) {
                        window.location.href = path;
                    }).ifEmpty(function () {
                        request.jump('com', '/view/ccg/007/d/index.xhtml?signon=on');
                    });
                }
                login.jumpToUsedSSOLoginPage = jumpToUsedSSOLoginPage;
                function keepSerializedSession() {
                    var dfd = $.Deferred();
                    dfd.resolve();
                    //            request.ajax("/shr/web/session/serialize").done(res => {
                    //                uk.sessionStorage.setItem(STORAGE_KEY_SERIALIZED_SESSION, res);
                    //                dfd.resolve();
                    //            });
                    //            
                    return dfd.promise();
                }
                login.keepSerializedSession = keepSerializedSession;
                function restoreSessionTo(webAppId) {
                    var dfd = $.Deferred();
                    dfd.resolve();
                    return dfd.promise();
                    //            let serializedTicket = uk.sessionStorage.getItem(STORAGE_KEY_SERIALIZED_SESSION).get();
                    //            return (<any>request).ajax(webAppId, "/shr/web/session/restore", serializedTicket, null, false);
                }
                login.restoreSessionTo = restoreSessionTo;
            })(login = request.login || (request.login = {}));
            function jumpToTopPage() {
                jumpToMenu('nts.uk.com.web/view/ccg/008/a/index.xhtml');
            }
            request.jumpToTopPage = jumpToTopPage;
            function resolvePath(path) {
                var destination;
                if (path.charAt(0) === '/') {
                    destination = location.appRoot.mergeRelativePath(path);
                }
                else {
                    destination = location.current.mergeRelativePath(path);
                }
                return destination.serialize();
            }
            request.resolvePath = resolvePath;
            var location;
            (function (location) {
                location.current = new Locator(window.location.href);
                location.appRoot = location.current.mergeRelativePath(__viewContext.rootPath);
                location.siteRoot = location.appRoot.mergeRelativePath('../');
                location.ajaxRootDir = 'webapi/';
                var currentAppName = _.takeRight(location.appRoot.serialize().split('/'), 2)[0];
                for (var id in request.WEB_APP_NAME) {
                    if (currentAppName === request.WEB_APP_NAME[id]) {
                        location.currentAppId = id;
                        break;
                    }
                }
            })(location = request.location || (request.location = {}));
            ;
        })(request = uk.request || (uk.request = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
