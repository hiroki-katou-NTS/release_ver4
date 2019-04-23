import { _ } from '@app/provider';

export class WebStorageWrapper {
    nativeStorage: Storage;

    constructor(storage: Storage) {
        this.nativeStorage = storage;
    }

    setItem(key: string, value: string) {
        if (value === undefined) {
            return;
        }
        this.nativeStorage.setItem(key, value);
    }

    setItemAsJson(key: string, value: any) {
        this.setItem(key, JSON.stringify(value));
    }
    
    containsKey(key: string) {
        return this.getItem(key) !== null;
    };

    getItem(key: string): string {
        var value: string = this.nativeStorage.getItem(key);
        if (value === null || value === undefined || value === 'undefined') {
            return null;
        }
        return value;
    }

    getItemAndRemove(key: string) {
        var item = this.getItem(key);
        this.removeItem(key);
        return item;
    }

    removeItem(key: string) {
        this.nativeStorage.removeItem(key);
    }

    clear() {
        this.nativeStorage.clear();
    }
}

export var sessionStorage = new WebStorageWrapper(window.sessionStorage);
export var localStorage = new WebStorageWrapper(window.localStorage);

export module characteristics {

    /**
     * Now, "characteristic data" is saved in Local Storage.
     * In the future, the data may be saved in DB using Ajax.
     * So these APIs have jQuery Deferred Interface to support asynchronous. 
     */

    let delayToEmulateAjax = 100;
    function convertObjectToArray(key:any):string{
        var result=[];
        for(var p in key){
            result.push([p,key[p]]);
        }
        result.sort(function(a,b){
            return (a > b) ? 1 : (a < b) ? -1 : 0;    
        });
        return result.toString();
    }    
    
    export function saveByObjectKey(key: any, value: any){
        return save(convertObjectToArray(key),value);
    }
    
    export function restoreByObjectKey(key:any): Promise<any>{
        return restore(convertObjectToArray(key));    
    }
    
    export function save(key: string, value: any) {
        let dfd = new Promise((resolve, reject) => {
            setTimeout(() => {
                localStorage.setItemAsJson(createKey(key), value);
                resolve();
            }, delayToEmulateAjax);
        });

        return dfd;
    }

    export function restore(key: string): Promise<any> {
        let dfd = new Promise((resolve, reject) => {
            setTimeout(() => {
                let value = localStorage.getItem(createKey(key));
                if(!_.isNil(value)){
                    resolve(JSON.parse(value));
                }
                resolve(undefined);
            }, delayToEmulateAjax);
        });

        return dfd;
    }

    export function remove(key: string): Promise<any> {
        let dfd = new Promise((resolve, reject) => {
            setTimeout(() => {
                localStorage.removeItem(createKey(key));
                resolve();
            }, delayToEmulateAjax);
        });

        return dfd;
    }

    function createKey(key: string): string {
        return 'nts.uk.characteristics.' + key;
    }
}

export module cookie {
        
    export function get(name: string): string {
        let value = asMap()[name];
        return value;
    }
    
    export function remove(name: string, attr: any) {
        document.cookie = name + "=; path=" + attr.path + "; max-age=0";
    }
    
    export function asMap(): any {
        let map = {};
        document.cookie.split(";")
            .forEach(item => {
                let positionOfDelimiter = item.indexOf("=");
                let name = item.slice(0, positionOfDelimiter).trim();
                map[name] = item.slice(positionOfDelimiter + 1);
            });
        
        return map;
    }
}