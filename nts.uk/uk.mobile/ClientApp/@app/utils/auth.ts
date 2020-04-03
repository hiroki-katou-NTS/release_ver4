import { storage } from '@app/utils';

export const auth = {
    get valid(): boolean {
        return !!auth.token;
    },
    get token(): string | null {
        return storage.session.getItem('csrf') as string;
    },
    get remember(): {
        companyCode: string,
        employeeCode: string
    } {
        let remb: any = storage.local.getItem('remember');

        return remb && {
            companyCode: remb.companyCode,
            employeeCode: remb.employeeCode
        };
    },
    get user(): null | {
        employee: boolean;
        companyId: string;
        employeeId: string;
        companyCode: string;
        employeeCode: string;
        constractCode: string;
        role: {
            payroll: string;
            personnel: string;
            attendance: string;
            systemAdmin: string;
            companyAdmin: string;
            personalInfo: string;
            officeHelper: string;
            groupCompanyAdmin: string;
        }
    } {
        let user: any = storage.session.getItem('user'),
            role: any = (user || { role: {} }).role || {};

        return user && {
            employee: user.employee as boolean,
            companyId: user.companyId as string,
            employeeId: user.employeeId as string,
            companyCode: user.companyCode as string,
            employeeCode: user.employeeCode as string,
            constractCode: user.constractCode as string,
            role: {
                payroll: role.payroll,
                personnel: role.personnel,
                attendance: role.attendance,
                systemAdmin: role.systemAdmin,
                companyAdmin: role.companyAdmin,
                personalInfo: role.personalInfo,
                officeHelper: role.officeHelper,
                groupCompanyAdmin: role.groupCompanyAdmin
            }
        };
    },
    get contract(): { code: string, password: string } {
        let contract: any = storage.local.getItem('contract');

        return contract && {
            code: contract.code,
            password: contract.password
        };
    }
};