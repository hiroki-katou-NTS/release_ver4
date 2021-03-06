using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;

using WSISmile.Business.Common;
using WSISmile.Business.Link;
using WSISmile.Business.Log;
using WSISmile.Business.Task;

namespace WSISmile.Business.Category.Accept
{
    /// <summary>
    /// Oσό-gDξρJeS
    /// </summary>
    public class WorkplaceInfo : AcceptCategoryBase
    {
        public WorkplaceInfo() { }

        #region CONST
        /// <summary>
        /// KwxΕεl:10
        /// </summary>
        public const int MAX_LEVEL = 10;

        /// <summary>
        /// X^[gx (001)
        /// </summary>
        private const int LEVEL_START = 1;
        #endregion CONST

        /// <summary>
        /// SmilegDξρ-KwΔ\z
        /// </summary>
        /// <param name="dsSmile">SmilegDξρ</param>
        /// <param name="TI">^XNξρ</param>
        /// <remarks>SmilegDξρΜKw²«πβ³·ι(KwxΔέθ)</remarks>
        private void RebuildSmileOrganization(DataSet dsSmile, TaskInfo TI)
        {
            #region SmilegDξρ-KwΔ\z : RebuildSmileOrganization
            Dictionary<int, int> levelSorter = new Dictionary<int, int>();
            // Kwx[1]
            levelSorter.Add(1, 0);

            try
            {
                dsSmile.Tables[0].Columns.Add(SmileOrganizationItem.SORT, typeof(Int32));

                #region Sortβ³O
                dsSmile.Tables[0].DefaultView.Sort = SmileOrganizationItem.COMPANY_CD + ", " +
                                                     SmileOrganizationItem.START_DAY + ", " +
                                                     SmileOrganizationItem.LEVEL + ", " +
                                                     SmileOrganizationItem.HIGHER_ORGANIZT_CD + ", " +
                                                     SmileOrganizationItem.ORGANIZATION_CD;
                #endregion Sortβ³O

                DataTable dtSmile = dsSmile.Tables[0].DefaultView.ToTable();

                foreach (DataRow drChild in dtSmile.Rows)
                {
                    if (drChild[SmileOrganizationItem.LEVEL] != null)
                    {
                        if (drChild[SmileOrganizationItem.LEVEL].ToString() != "1")
                        {
                            #region Kwx[1]ΘO
                            foreach (DataRow drParent in dtSmile.Rows)
                            {
                                bool found = false;

                                // qΜγΚgDR[heΜgDR[h@Λ@eΜKwxΌΊ(+1)πqΜKwxΖ·ι
                                if (drChild[SmileOrganizationItem.START_DAY] == drParent[SmileOrganizationItem.START_DAY] &&
                                    drChild[SmileOrganizationItem.HIGHER_ORGANIZT_CD] == drParent[SmileOrganizationItem.ORGANIZATION_CD])
                                {
                                    drChild[SmileOrganizationItem.LEVEL] = int.Parse(drParent[SmileOrganizationItem.LEVEL].ToString()) + 1;
                                    found = true;
                                    break;
                                }
                                // ©Β©ηΘ©Α½κAKwx[1]
                                if (!found)
                                {
                                    drChild[SmileOrganizationItem.LEVEL] = 1;
                                }

                                int level = int.Parse(drChild[SmileOrganizationItem.LEVEL].ToString());
                                int sort = 0;
                                if (levelSorter.ContainsKey(level))
                                {
                                    sort = levelSorter[level];
                                }
                                else
                                {
                                    levelSorter.Add(level, sort);
                                }

                                drChild[SmileOrganizationItem.SORT] = sort;

                                levelSorter[level] = levelSorter[level] + 1;
                            }
                            #endregion
                        }
                        else
                        {
                            #region Kwx[1]
                            drChild[SmileOrganizationItem.SORT] = levelSorter[1];
                            levelSorter[1] = levelSorter[1] + 1;
                            #endregion
                        }
                    }
                }

                #region Sortβ³γ
                dtSmile.DefaultView.Sort = SmileOrganizationItem.COMPANY_CD + ", " + 
                                           SmileOrganizationItem.START_DAY + ", " + 
                                           SmileOrganizationItem.LEVEL + ", " + 
                                           SmileOrganizationItem.HIGHER_ORGANIZT_CD + ", " + 
                                           SmileOrganizationItem.SORT;
                #endregion Sortβ³γ
            }
            catch (Exception ex)
            {
                TI.ErrorMsgList.Add("SmilegDξρ-KwΔ\zΙαOͺ­Ά΅ά΅½B" + Environment.NewLine + ex.Message);
            }
            #endregion
        }

        /// <summary>
        /// SmilegDξρπ KwΔ\z  ?W  List.ΦΟ·
        /// </summary>
        /// <param name="dsSmile">SmilegDξρ</param>
        /// <param name="TI">^XNξρ</param>
        /// <returns>SmilegDξρList.</returns>
        public List<SmileOrganization> SmileDataToList(DataSet dsSmile, TaskInfo TI)
        {
            #region SmilegDξρπ KwΔ\z  ?W  List.ΦΟ· : SmileDataToList
            // SmilegDξρ-KwΔ\z
            this.RebuildSmileOrganization(dsSmile, TI);

            List<SmileOrganization> list = new List<SmileOrganization>();

            foreach (DataRow drSmile in dsSmile.Tables[0].Rows)
            {
                SmileOrganization smileOrganization = new SmileOrganization();

                // οΠR[h
                smileOrganization.CompanyCd = TI.CompCode;

                // gDR[h
                smileOrganization.OrganizationCd = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.ORGANIZATION_CD]);

                // ­ίNϊ
                smileOrganization.StartDay = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.START_DAY]);

                // IΉNϊ
                if (drSmile[SmileOrganizationItem.END_DAY].ToString() == "99999999")
                {
                    smileOrganization.EndDay = new DateTime(9999, 12, 31).ToString(Format.Date);
                }
                else
                {
                    smileOrganization.EndDay = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.END_DAY]);
                }

                // γΚgDR[h
                smileOrganization.HigherOrganiztCd = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.HIGHER_ORGANIZT_CD]);

                if (!Toolbox.IsNumber(drSmile[SmileOrganizationItem.LEVEL].ToString()))
                {
                    TI.ErrorMsgList.Add(this.GetErrorMessage(smileOrganization, "KwxΙΕΝΘ’lͺάάκΔ’ά·B"));
                    continue;
                }

                // Kwx
                smileOrganization.Level = int.Parse(Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.LEVEL]));

                // ³?gDΌ
                smileOrganization.OrganiztNameOfficial = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.ORGANIZT_NAME_OFFICIAL]);

                // gDΌ
                smileOrganization.OrganiztName = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.ORGANIZT_NAME]);

                // gDͺΜ
                smileOrganization.OrganiztNameSimple = Toolbox.RemoveInvalidSymbol(drSmile[SmileOrganizationItem.ORGANIZT_NAME_SIMPLE]);

                list.Add(smileOrganization);
            }

            return list;
            #endregion
        }

        /// <summary>
        /// EκξρΜΰKwCD[1`10]πέθ·ι
        /// </summary>
        /// <param name="listSmileOrganization">SmilegDξρList.</param>
        /// <param name="TI"></param>
        public void SetWorkplaceInLevelCd(List<SmileOrganization> listSmileOrganization, TaskInfo TI)
        {
            #region EκξρΜΰKwCD[1`10]πέθ·ι : SetWorkplaceInLevelCd
            // Oρξρ
            SmileOrganization previous = new SmileOrganization();

            try
            {
                foreach (SmileOrganization smileOrganization in listSmileOrganization)
                {
                    // TODO ΞYΜχίJnϊζθΰOΜ­ίNϊ("yyyy/MM/dd")πAg³κάΉρΕ΅½B
                    if (smileOrganization.Level > MAX_LEVEL)
                    {
                        // 10KwΘγΜκΙAΰKwCD[1]πNA
                        smileOrganization.InlevelCds[0] = "";
                        continue;
                    }

                    if (smileOrganization.StartDay != previous.StartDay)
                    {
                        // ­ίNϊͺΩΘικAOρξρπNA
                        previous = new SmileOrganization();
                    }

                    if (smileOrganization.HigherOrganiztCd == "")
                    {
                        #region Kwx[1] (γΚgDR[hͺΆέ΅Θ’)
                        if (smileOrganization.Level == previous.Level)
                        {
                            // Kwx[1]ΜκΙAJEgAbv
                            smileOrganization.InlevelCds[0] = WorkplaceInfo.IntegerToInlevelCd((int.Parse(previous.InlevelCds[0]) + 1));
                        }
                        else
                        {
                            // First.
                            smileOrganization.InlevelCds[0] = WorkplaceInfo.IntegerToInlevelCd(LEVEL_START);
                        }
                        #endregion
                    }
                    else
                    {
                        #region Kwx[1]Θ~
                        if (!SearchHigherOrganization(smileOrganization, listSmileOrganization))
                        {
                            // ΰKwCD[1]πNA
                            smileOrganization.InlevelCds[0] = "";
                            TI.ErrorMsgList.Add(this.GetErrorMessage(smileOrganization, "γΚgDR[hΜf[^ͺΆέ΅άΉρB"));
                            continue;
                        }

                        int index = smileOrganization.Level - 1;

                        if (smileOrganization.HigherOrganiztCd == previous.HigherOrganiztCd)
                        {
                            // γΚgDͺ―ΆΜκΙAJEgAbv
                            smileOrganization.InlevelCds[index] = WorkplaceInfo.IntegerToInlevelCd((int.Parse(previous.InlevelCds[index]) + 1));
                        }
                        else
                        {
                            // γΚgDͺΩΘικΙAZbg
                            smileOrganization.InlevelCds[index] = WorkplaceInfo.IntegerToInlevelCd(LEVEL_START);
                        }
                        #endregion
                    }

                    // OρξρπΫΆ
                    previous = smileOrganization.Clone();

                    // ΰKwR[hπέθ
                    for (int i = 0; i < MAX_LEVEL; i++)
                    {
                        smileOrganization.InlevelCd += smileOrganization.InlevelCds[i];
                    }
                }

            }
            catch (Exception ex)
            {
                TI.ErrorMsgList.Add("EκξρΜΰKwCD[1`10]έθΙαOͺ­Ά΅ά΅½B" + Environment.NewLine + ex.Message);
            }
            #endregion
        }

        /// <summary>
        /// wθgDΜγΚgDπυ΅AΰKwCD[1`10]πRs[
        /// </summary>
        /// <param name="searchOrganization">υΞΫgD</param>
        /// <param name="listSmileOrganization">SgD</param>
        /// <returns>true:©Β©Α½^false:©Β©ΑΔ’Θ’</returns>
        private bool SearchHigherOrganization(SmileOrganization searchOrganization, List<SmileOrganization> listSmileOrganization)
        {
            #region wθgDΜγΚgDπυ΅AΰKwCD[1`10]πRs[ : SearchHigherOrganization
            if (searchOrganization.Level == 1)
            {
                // TOPKwxΜκ
                return true;
            }

            foreach (SmileOrganization smileOrganization in listSmileOrganization)
            {
                /*
                 * ΊLππ½·κΙAγΚgDΖ©Θ΅γΚgDΜΰKwCD[1]`ΰKwCD[10]πυΞΫgDΦRs[·ι
                 *  1) ­ίNϊͺ―Ά
                 *  2) υgDΜγΚgDR[h  YgDΜgDR[h
                 *  3) υgDΜKwxκΒΘγ  YgDΜKwx
                 *  4) YgDΜΰKwCD[1]ͺσΕΝΘ’
                */

                if (smileOrganization.StartDay == searchOrganization.StartDay &&
                    smileOrganization.OrganizationCd == searchOrganization.HigherOrganiztCd &&
                    smileOrganization.Level == searchOrganization.Level - 1 &&
                    smileOrganization.InlevelCds[0] != "")
                {
                    searchOrganization.InlevelCds = new List<string>(smileOrganization.InlevelCds);
                    return true;
                }
            }

            return false;
            #endregion
        }

        /// <summary>
        /// SmilegDξρList.πAgDataTableΦΟ·
        /// </summary>
        /// <param name="listSmile"></param>
        /// <returns></returns>
        public DataTable ListToSmileLinkTable(List<SmileOrganization> listSmile)
        {
            #region SmilegDξρList.πAgDataTableΦΟ· : ListToSmileLinkTable
            // Smile€AgDataTable
            DataTable dtSmile = SmileOrganization.DefineSmileDataTable();

            foreach (SmileOrganization smileOrganization in listSmile)
            {
                DataRow drSmile = dtSmile.NewRow();

                // Jnϊ
                drSmile[SmileOrganizationItem.START_DAY] = smileOrganization.StartDay;

                // IΉϊ
                drSmile[SmileOrganizationItem.END_DAY] = smileOrganization.EndDay;

                // EκR[h
                drSmile[SmileOrganizationItem.ORGANIZATION_CD] = smileOrganization.OrganizationCd;

                // EκΌΜ
                drSmile[SmileOrganizationItem.ORGANIZT_NAME] = smileOrganization.OrganiztName;

                // EκͺΜ
                drSmile[SmileOrganizationItem.ORGANIZT_NAME_SIMPLE] = smileOrganization.OrganiztNameSimple;

                // EκΜ
                drSmile[SmileOrganizationItem.ORGANIZT_NAME_OFFICIAL] = smileOrganization.OrganiztNameOfficial;

                // EκKwR[h
                drSmile[SmileOrganizationItem.INLEVEL_CD] = smileOrganization.InlevelCd;

                #region EκKwR[h 1 ` EκKwR[h 10
                // EκKwR[h 1
                drSmile[SmileOrganizationItem.INLEVEL_CD_1] = smileOrganization.InlevelCds[0];

                // EκKwR[h 2
                drSmile[SmileOrganizationItem.INLEVEL_CD_2] = smileOrganization.InlevelCds[1];

                // EκKwR[h 3
                drSmile[SmileOrganizationItem.INLEVEL_CD_3] = smileOrganization.InlevelCds[2];

                // EκKwR[h 4
                drSmile[SmileOrganizationItem.INLEVEL_CD_4] = smileOrganization.InlevelCds[3];

                // EκKwR[h 5
                drSmile[SmileOrganizationItem.INLEVEL_CD_5] = smileOrganization.InlevelCds[4];

                // EκKwR[h 6
                drSmile[SmileOrganizationItem.INLEVEL_CD_6] = smileOrganization.InlevelCds[5];

                // EκKwR[h 7
                drSmile[SmileOrganizationItem.INLEVEL_CD_7] = smileOrganization.InlevelCds[6];

                // EκKwR[h 8
                drSmile[SmileOrganizationItem.INLEVEL_CD_8] = smileOrganization.InlevelCds[7];

                // EκKwR[h 9
                drSmile[SmileOrganizationItem.INLEVEL_CD_9] = smileOrganization.InlevelCds[8];

                // EκKwR[h 10
                drSmile[SmileOrganizationItem.INLEVEL_CD_10] = smileOrganization.InlevelCds[9];
                #endregion EκKwR[h 1 ` EκKwR[h 10

                dtSmile.Rows.Add(drSmile);
            }

            return dtSmile;
            #endregion
        }

        /// <summary>
        /// EκKwR[htH[}bg
        /// </summary>
        /// <param name="inlevelCd">EκKwR[h</param>
        /// <returns></returns>
        private static string IntegerToInlevelCd(int inlevelCd)
        {
            #region EκKwR[htH[}bg : IntegerToInlevelCd
            return inlevelCd.ToString().PadLeft(3, '0');
            #endregion
        }

        /// <summary>
        /// EκG[bZ[WΜμ¬
        /// </summary>
        /// <returns>G[bZ[W</returns>
        private string GetErrorMessage(SmileOrganization smileOrganization, string appendMsg)
        {
            #region EκG[bZ[WΜμ¬ : GetErrorMessage
            return 
                "gDξρFοΠR[h=" + smileOrganization.CompanyCd + 
                " gDR[h=" + smileOrganization.OrganizationCd + 
                " gDΌ=" + smileOrganization.OrganiztName + 
                " Λ " + appendMsg;
            #endregion
        }
    }
}
