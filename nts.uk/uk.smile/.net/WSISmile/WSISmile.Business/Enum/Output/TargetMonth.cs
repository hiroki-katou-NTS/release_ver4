using System;

namespace WSISmile.Business.Enum
{
    /// <summary>
    /// 対象月(連動月の設定)Enum
    /// </summary>
    public enum TargetMonth
    {
        /// <summary>
        /// 当月の情報を連携する
        /// </summary>
        Current = 0,
        /// <summary>
        /// １ヶ月前の情報を連携する
        /// </summary>
        Previous = 1
    }
}
