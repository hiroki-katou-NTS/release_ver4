module nts.uk.request {

    export var STORAGE_KEY_TRANSFER_DATA = "nts.uk.request.STORAGE_KEY_TRANSFER_DATA";

    export type WebAppId = 'com' | 'pr' | 'at';
    export const WEB_APP_NAME = {
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
            this.rawUrl = url;
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
            var parts = relativePath.split('/');
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

            queryStringToAdd.mergeFrom(this.queryString);

            var queryStringParts = queryStringToAdd.hasItems()
                ? '?' + queryStringToAdd.serialize()
                : '';

            return new Locator(stack.join('/') + queryStringParts);
        }
    }

    export function ajax(path: string, data?: any, options?: any);
    export function ajax(webAppId: WebAppId, path: string, data?: any, options?: any) {

        if (typeof arguments[1] !== 'string') {
            return ajax.apply(null, _.concat(location.currentAppId, arguments));
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

        $.ajax({
            type: options.method || 'POST',
            contentType: options.contentType || 'application/json',
            url: webserviceLocator.serialize(),
            dataType: options.dataType || 'json',
            data: data,
            headers: {
                'PG-Path': location.current.serialize()
            }
        }).done(function(res) {
            if (res !== undefined && res.businessException) {
                dfd.reject(res);
            } else {
                dfd.resolve(res);
            }
        });

        return dfd.promise();
    }

    export function uploadFile(data: FormData, option?: any): $.Deferred {
        let dfd = $.Deferred();
        $.ajax({
            url: "/nts.uk.com.web/webapi/ntscommons/arc/filegate/upload",
            type: 'POST',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            success: function(data, textStatus, jqXHR) {
                if (option.onSuccess) {
                    option.onSuccess();
                }
                
            },
            error: function(jqXHR, textStatus, errorThrown) {
                if (option.onFail) {
                    option.onFail();
                }
               
            }
        }).done(function(res) {
            if (res !== undefined && res.businessException) {
                dfd.reject(res);
            } else {
                dfd.resolve(res);
            }
        }).fail(function(res){
              dfd.reject(res);
        });
        return dfd.promise();
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
                if (res.failed||res.status == "ABORTED") {
                    dfd.reject(res.error);
                } else {
                    specials.donwloadFile(res.id);
                    dfd.resolve(res);
                }

            })
            .fail(res => {
                dfd.reject(res);
            });

        return dfd.promise();
    }

    export module specials {

        export function getAsyncTaskInfo(taskId: string) {
            return ajax('/ntscommons/arc/task/async/' + taskId);
        }

        export function donwloadFile(fileId: string) {
            $('<iframe/>')
                .attr('id', 'download-frame')
                .appendTo('body')
                .attr('src', resolvePath('/webapi/ntscommons/arc/filegate/get/' + fileId));
        }
    }


    export function jump(path: string, data?: any) {

        uk.sessionStorage.setItemAsJson(STORAGE_KEY_TRANSFER_DATA, data);

        window.location.href = resolvePath(path);
    }

    export function resolvePath(path: string) {
        var destination: Locator;
        if (path.charAt(0) === '/') {
            destination = location.appRoot.mergeRelativePath(path);
        } else {
            destination = location.current.mergeRelativePath(path);
        }

        return destination.rawUrl;
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