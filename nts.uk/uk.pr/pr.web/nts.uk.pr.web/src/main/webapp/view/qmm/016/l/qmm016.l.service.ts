module nts.uk.pr.view.qmm016.l {
    export module service {

        var paths: any = {
            findAllCertification: "pr/wagetable/certification/findall",
            findAllCertifyGroup: "pr/wagetable/certifygroup/findall",
            findCertifyGroup: "pr/wagetable/certifygroup/find",
            addCertifyGroup: "pr/wagetable/certifygroup/add",
            updateCertifyGroup: "pr/wagetable/certifygroup/update",
            deleteCertifyGroup: "pr/wagetable/certifygroup/delete",
        };

        //Function connection service FindAll Certification
        export function findAllCertification(): JQueryPromise<model.CertificationFindInDto[]> {
            return nts.uk.request.ajax(paths.findAllCertification);
        }

        //Function connection service FindAll CertifyGroup
        export function findAllCertifyGroup(): JQueryPromise<model.CertifyGroupFindOutDto[]> {
            return nts.uk.request.ajax(paths.findAllCertifyGroup);
        }

        //Function connection service FindA CertifyGroup
        export function findCertifyGroup(code: string): JQueryPromise<model.CertifyGroupDto> {
            return nts.uk.request.ajax(paths.findCertifyGroup + "/" + code);
        }

        //Function connection service Add CertifyGroup
        export function addCertifyGroup(certifyGroupDto: model.CertifyGroupDto): JQueryPromise<void> {
            var data = { certifyGroupDto: certifyGroupDto };
            return nts.uk.request.ajax(paths.addCertifyGroup, data);
        }

        //Function connection service Update CertifyGroup
        export function updateCertifyGroup(certifyGroupDto: model.CertifyGroupDto): JQueryPromise<void> {
            var data = { certifyGroupDto: certifyGroupDto };
            return nts.uk.request.ajax(paths.updateCertifyGroup, data);
        }

        //Function connection service Delete CertifyGroup
        export function deleteCertifyGroup(certifyGroupDeleteDto: model.CertifyGroupDeleteDto): JQueryPromise<void> {
            var data = { certifyGroupDeleteDto: certifyGroupDeleteDto };
            return nts.uk.request.ajax(paths.deleteCertifyGroup, data);
        }

        export module model {

            export class CertificationDto {
                code: string;
                name: string;
            }

            export class CertificationFindInDto {
                code: string;
                name: string;
            }

            export class CertifyGroupFindOutDto {
                code: string;
                name: string;
            }

            export class CertifyGroupDto {
                /** The code. */
                code: string;

                /** The name. */
                name: string;

                /** The multi apply set. */
                multiApplySet: number;

                /** The certifies. */
                certifies: CertificationDto[];
            }

            export class CertifyGroupDeleteDto {
                /** The group code. */
                groupCode: string;

                /** The version. */
                version: number;
            }

            export enum MultipleTargetSetting {
                BigestMethod = 0,//BigestMethod
                TotalMethod = 1//TotalMethod
            }

            export class MultipleTargetSettingDto {
                code: number;
                name: string;
                constructor(code: number, name: string) {
                    this.code = code;
                    this.name = name;
                }
            }

            export enum TypeActionCertifyGroup {
                add = 1,
                update = 2
            }
        }
    }
}