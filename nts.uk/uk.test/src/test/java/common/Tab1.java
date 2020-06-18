package common;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Tab1 extends CreateMainTest{

    public JScrollPane scrollPane;

    public Tab1() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setPreferredSize(new Dimension(1250, 1000));

        scrollPane = new JScrollPane(p);
        scrollPane.setBounds(0, 0, 1280, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // KAF005
        JLabel lb_32 = new JLabel("KAF005");
        lb_32.setName("lb_32");
        lb_32.setBounds(12, 10, 73, 13);

        JLabel lb_32_1 = new JLabel("SCENARIO1");
        lb_32_1.setName("lb_32_1");
        lb_32_1.setBounds(158, 10, 78, 13);

        JCheckBox cbx_32_1_1 = new JCheckBox("CASE1");
        cbx_32_1_1.setBounds(304, 6, 71, 21);
        cbx_32_1_1.setName("cbx_32_1_1");

        JCheckBox cbx_32_1_2 = new JCheckBox("CASE2");
        cbx_32_1_2.setBounds(440, 6, 71, 21);
        cbx_32_1_2.setName("cbx_32_1_2");

        JTextField tF_32_01_01 = new JTextField("");
        tF_32_01_01.setBounds(384, 7, 48, 19);
        tF_32_01_01.setEditable(false);
        tF_32_01_01.setName("tF_32_01_01");

        JTextField tF_32_01_02 = new JTextField("");
        tF_32_01_02.setBounds(520, 7, 48, 19);
        tF_32_01_02.setEditable(false);
        tF_32_01_02.setName("tF_32_01_02");

        JLabel lb_32_2 = new JLabel("SCENARIO2");
        lb_32_2.setName("lb_32_2");
        lb_32_2.setBounds(158, 33, 78, 13);

        JCheckBox cbx_32_2_1 = new JCheckBox("CASE1");
        cbx_32_2_1.setBounds(304, 29, 71, 21);
        cbx_32_2_1.setName("cbx_32_2_1");

        JCheckBox cbx_32_2_2 = new JCheckBox("CASE2");
        cbx_32_2_2.setBounds(440, 29, 71, 21);
        cbx_32_2_2.setName("cbx_32_2_2");

        JTextField tF_32_02_01 = new JTextField("");
        tF_32_02_01.setBounds(384, 30, 48, 19);
        tF_32_02_01.setEditable(false);
        tF_32_02_01.setName("tF_32_02_01");

        JTextField tF_32_02_02 = new JTextField("");
        tF_32_02_02.setBounds(520, 30, 48, 19);
        tF_32_02_02.setEditable(false);
        tF_32_02_02.setName("tF_32_02_02");

        JLabel lb_32_3 = new JLabel("SCENARIO3");
        lb_32_3.setName("lb_32_3");
        lb_32_3.setBounds(158, 56, 78, 13);

        JCheckBox cbx_32_3_1 = new JCheckBox("CASE1");
        cbx_32_3_1.setBounds(304, 52, 71, 21);
        cbx_32_3_1.setName("cbx_32_3_1");

        JCheckBox cbx_32_3_2 = new JCheckBox("CASE2");
        cbx_32_3_2.setBounds(440, 52, 71, 21);
        cbx_32_3_2.setName("cbx_32_3_2");

        JCheckBox cbx_32_3_3 = new JCheckBox("CASE3");
        cbx_32_3_3.setBounds(576, 52, 71, 21);
        cbx_32_3_3.setName("cbx_32_3_3");

        JTextField tF_32_03_01 = new JTextField("");
        tF_32_03_01.setBounds(384, 53, 48, 19);
        tF_32_03_01.setEditable(false);
        tF_32_03_01.setName("tF_32_03_01");

        JTextField tF_32_03_02 = new JTextField("");
        tF_32_03_02.setBounds(520, 53, 48, 19);
        tF_32_03_02.setEditable(false);
        tF_32_03_02.setName("tF_32_03_02");

        JTextField tF_32_03_03 = new JTextField("");
        tF_32_03_03.setBounds(656, 53, 48, 19);
        tF_32_03_03.setEditable(false);
        tF_32_03_03.setName("tF_32_03_03");

        JCheckBox checkBox_65 = new JCheckBox("全件選択");
        checkBox_65.setName("cbx_32_all");
        checkBox_65.setBounds(712, 6, 88, 21);
        p.add(checkBox_65);
        addActionCheckboxAll(checkBox_65);

        // KTG001
        JLabel lb_3 = new JLabel("KTG001");
        lb_3.setName("lb_3");
        lb_3.setBounds(12, 98, 73, 13);

        JLabel lb_3_1 = new JLabel("SCENARIO1");
        lb_3_1.setName("lb_3_1");
        lb_3_1.setBounds(158, 98, 78, 13);

        JCheckBox cbx_3_1_1 = new JCheckBox("CASE1");
        cbx_3_1_1.setBounds(304, 94, 71, 21);
        cbx_3_1_1.setName("cbx_3_1_1");

        JCheckBox cbx_3_1_2 = new JCheckBox("CASE2");
        cbx_3_1_2.setBounds(440, 94, 71, 21);
        cbx_3_1_2.setName("cbx_3_1_2");

        JTextField tF_03_01_01 = new JTextField("");
        tF_03_01_01.setBounds(384, 95, 48, 19);
        tF_03_01_01.setEditable(false);
        tF_03_01_01.setName("tF_03_01_01");

        JTextField tF_03_01_02 = new JTextField("");
        tF_03_01_02.setBounds(520, 95, 48, 19);
        tF_03_01_02.setEditable(false);
        tF_03_01_02.setName("tF_03_01_02");

        // KTG030
        JLabel lb_6 = new JLabel("KTG030");
        lb_6.setName("lb_6");
        lb_6.setBounds(12, 139, 73, 13);

        JLabel lb_6_1 = new JLabel("SCENARIO1");
        lb_6_1.setName("lb_6_1");
        lb_6_1.setBounds(158, 139, 78, 13);

        JCheckBox cbx_6_1_1 = new JCheckBox("CASE1");
        cbx_6_1_1.setBounds(304, 135, 71, 21);
        cbx_6_1_1.setName("cbx_6_1_1");

        JCheckBox cbx_6_1_2 = new JCheckBox("CASE2");
        cbx_6_1_2.setBounds(440, 135, 71, 21);
        cbx_6_1_2.setName("cbx_6_1_2");

        JTextField tF_06_01_01 = new JTextField("");
        tF_06_01_01.setBounds(384, 136, 48, 19);
        tF_06_01_01.setEditable(false);
        tF_06_01_01.setName("tF_06_01_01");

        JTextField tF_06_01_02 = new JTextField("");
        tF_06_01_02.setBounds(520, 136, 48, 19);
        tF_06_01_02.setEditable(false);
        tF_06_01_02.setName("tF_06_01_02");

        // KWR001
        JLabel lb_24 = new JLabel("KWR001");
        lb_24.setName("lb_24");
        lb_24.setBounds(12, 182, 73, 13);

        JLabel lb_24_1 = new JLabel("SCENARIO1");
        lb_24_1.setName("lb_24_1");
        lb_24_1.setBounds(158, 182, 78, 13);

        JCheckBox cbx_24_1_1 = new JCheckBox("CASE1");
        cbx_24_1_1.setBounds(304, 178, 71, 21);
        cbx_24_1_1.setName("cbx_24_1_1");

        JCheckBox cbx_24_1_2 = new JCheckBox("CASE2");
        cbx_24_1_2.setBounds(440, 178, 71, 21);
        cbx_24_1_2.setName("cbx_24_1_2");

        JTextField tF_24_01_01 = new JTextField("");
        tF_24_01_01.setBounds(384, 179, 48, 19);
        tF_24_01_01.setEditable(false);
        tF_24_01_01.setName("tF_24_01_01");

        JTextField tF_24_01_02 = new JTextField("");
        tF_24_01_02.setBounds(520, 179, 48, 19);
        tF_24_01_02.setEditable(false);
        tF_24_01_02.setName("tF_24_01_02");

        // KWR008
        JLabel lb_25 = new JLabel("KWR008");
        lb_25.setName("lb_25");
        lb_25.setBounds(12, 225, 73, 13);

        JLabel lb_25_1 = new JLabel("SCENARIO1");
        lb_25_1.setName("lb_25_1");
        lb_25_1.setBounds(158, 225, 78, 13);

        JCheckBox cbx_25_1_1 = new JCheckBox("CASE1");
        cbx_25_1_1.setBounds(304, 221, 71, 21);
        cbx_25_1_1.setName("cbx_25_1_1");

        JTextField tF_25_01_01 = new JTextField("");
        tF_25_01_01.setBounds(384, 222, 48, 19);
        tF_25_01_01.setEditable(false);
        tF_25_01_01.setName("tF_25_01_01");

        // KWR006
        JLabel lb_26 = new JLabel("KWR006");
        lb_26.setName("lb_26");
        lb_26.setBounds(12, 268, 73, 13);

        JLabel lb_26_1 = new JLabel("SCENARIO1");
        lb_26_1.setName("lb_26_1");
        lb_26_1.setBounds(158, 268, 78, 13);

        JCheckBox cbx_26_1_1 = new JCheckBox("CASE1");
        cbx_26_1_1.setBounds(304, 264, 71, 21);
        cbx_26_1_1.setName("cbx_26_1_1");

        JTextField tF_26_01_01 = new JTextField("");
        tF_26_01_01.setBounds(384, 265, 48, 19);
        tF_26_01_01.setEditable(false);
        tF_26_01_01.setName("tF_26_01_01");

        // KDR001
        JLabel lb_27 = new JLabel("KDR001");
        lb_27.setName("lb_27");
        lb_27.setBounds(12, 311, 73, 13);

        JLabel lb_27_1 = new JLabel("SCENARIO1");
        lb_27_1.setName("lb_27_1");
        lb_27_1.setBounds(158, 311, 78, 13);

        JCheckBox cbx_27_1_1 = new JCheckBox("CASE1");
        cbx_27_1_1.setBounds(304, 307, 71, 21);
        cbx_27_1_1.setName("cbx_27_1_1");

        JTextField tF_27_01_01 = new JTextField("");
        tF_27_01_01.setBounds(384, 308, 48, 19);
        tF_27_01_01.setEditable(false);
        tF_27_01_01.setName("tF_27_01_01");

        // CMM018
        JLabel lb_28 = new JLabel("CMM018");
        lb_28.setName("lb_28");
        lb_28.setBounds(12, 354, 73, 13);

        JLabel lb_28_1 = new JLabel("SCENARIO1");
        lb_28_1.setName("lb_28_1");
        lb_28_1.setBounds(158, 354, 78, 13);

        JCheckBox cbx_28_1_1 = new JCheckBox("CASE1");
        cbx_28_1_1.setBounds(304, 350, 71, 21);
        cbx_28_1_1.setName("cbx_28_1_1");

        JTextField tF_28_01_01 = new JTextField("");
        tF_28_01_01.setBounds(384, 351, 48, 19);
        tF_28_01_01.setEditable(false);
        tF_28_01_01.setName("tF_28_01_01");

        JCheckBox cbx_28_1_2 = new JCheckBox("CASE2");
        cbx_28_1_2.setBounds(440, 350, 71, 21);
        cbx_28_1_2.setName("cbx_28_1_2");

        JTextField tF_28_01_02 = new JTextField("");
        tF_28_01_02.setBounds(520, 351, 48, 19);
        tF_28_01_02.setEditable(false);
        tF_28_01_02.setName("tF_28_01_02");

        JCheckBox cbx_28_1_3 = new JCheckBox("CASE3");
        cbx_28_1_3.setBounds(576, 350, 71, 21);
        cbx_28_1_3.setName("cbx_28_1_3");

        JTextField tF_28_01_03 = new JTextField("");
        tF_28_01_03.setBounds(656, 351, 48, 19);
        tF_28_01_03.setEditable(false);
        tF_28_01_03.setName("tF_28_01_03");

        // KAF011
        JLabel lb_36 = new JLabel("KAF011");
        lb_36.setName("lb_36");
        lb_36.setBounds(12, 397, 73, 13);

        JLabel lb_36_1 = new JLabel("SCENARIO1");
        lb_36_1.setName("lb_36_1");
        lb_36_1.setBounds(158, 397, 78, 13);

        JCheckBox cbx_36_1_1 = new JCheckBox("CASE1");
        cbx_36_1_1.setBounds(304, 393, 71, 21);
        cbx_36_1_1.setName("cbx_36_1_1");

        JTextField tF_36_01_01 = new JTextField("");
        tF_36_01_01.setBounds(384, 394, 48, 19);
        tF_36_01_01.setEditable(false);
        tF_36_01_01.setName("tF_36_01_01");

        JLabel lb_36_2 = new JLabel("SCENARIO2");
        lb_36_2.setName("lb_36_2");
        lb_36_2.setBounds(158, 420, 78, 13);

        JCheckBox cbx_36_2_1 = new JCheckBox("CASE1");
        cbx_36_2_1.setBounds(304, 416, 71, 21);
        cbx_36_2_1.setName("cbx_36_2_1");

        JTextField tF_36_02_01 = new JTextField("");
        tF_36_02_01.setBounds(384, 417, 48, 19);
        tF_36_02_01.setEditable(false);
        tF_36_02_01.setName("tF_36_02_01");

        JCheckBox cbx_36_2_2 = new JCheckBox("CASE2");
        cbx_36_2_2.setBounds(440, 416, 71, 21);
        cbx_36_2_2.setName("cbx_36_2_2");

        JTextField tF_36_02_02 = new JTextField("");
        tF_36_02_02.setBounds(520, 417, 48, 19);
        tF_36_02_02.setEditable(false);
        tF_36_02_02.setName("tF_36_02_02");

        // KAF010
        JLabel lb_35 = new JLabel("KAF010");
        lb_35.setName("lb_35");
        lb_35.setBounds(12, 468, 73, 13);

        JLabel lb_35_1 = new JLabel("SCENARIO1");
        lb_35_1.setName("lb_35_1");
        lb_35_1.setBounds(158, 468, 78, 13);

        JCheckBox cbx_35_1_1 = new JCheckBox("CASE1");
        cbx_35_1_1.setBounds(304, 464, 71, 21);
        cbx_35_1_1.setName("cbx_35_1_1");

        JTextField tF_35_01_01 = new JTextField("");
        tF_35_01_01.setBounds(384, 465, 48, 19);
        tF_35_01_01.setEditable(false);
        tF_35_01_01.setName("tF_35_01_01");

        JCheckBox cbx_35_1_2 = new JCheckBox("CASE2");
        cbx_35_1_2.setBounds(440, 464, 71, 21);
        cbx_35_1_2.setName("cbx_35_1_2");

        JTextField tF_35_01_02 = new JTextField("");
        tF_35_01_02.setBounds(520, 465, 48, 19);
        tF_35_01_02.setEditable(false);
        tF_35_01_02.setName("tF_35_01_02");

        JLabel lb_35_2 = new JLabel("SCENARIO2");
        lb_35_2.setName("lb_35_2");
        lb_35_2.setBounds(158, 491, 78, 13);

        JCheckBox cbx_35_2_1 = new JCheckBox("CASE1");
        cbx_35_2_1.setBounds(304, 487, 71, 21);
        cbx_35_2_1.setName("cbx_35_2_1");

        JTextField tF_35_02_01 = new JTextField("");
        tF_35_02_01.setBounds(384, 488, 48, 19);
        tF_35_02_01.setEditable(false);
        tF_35_02_01.setName("tF_35_02_01");

        JLabel lb_35_3 = new JLabel("SCENARIO3");
        lb_35_3.setName("lb_35_3");
        lb_35_3.setBounds(158, 514, 78, 13);

        JCheckBox cbx_35_3_1 = new JCheckBox("CASE1");
        cbx_35_3_1.setBounds(304, 510, 71, 21);
        cbx_35_3_1.setName("cbx_35_3_1");

        JTextField tF_35_03_01 = new JTextField("");
        tF_35_03_01.setBounds(384, 511, 48, 19);
        tF_35_03_01.setEditable(false);
        tF_35_03_01.setName("tF_35_03_01");

        JCheckBox cbx_35_3_2 = new JCheckBox("CASE2");
        cbx_35_3_2.setBounds(440, 510, 71, 21);
        cbx_35_3_2.setName("cbx_35_3_2");

        JTextField tF_35_03_02 = new JTextField("");
        tF_35_03_02.setBounds(520, 511, 48, 19);
        tF_35_03_02.setEditable(false);
        tF_35_03_02.setName("tF_35_03_02");

        JCheckBox cbx_35_3_3 = new JCheckBox("CASE3");
        cbx_35_3_3.setBounds(576, 510, 71, 21);
        cbx_35_3_3.setName("cbx_35_3_3");

        JTextField tF_35_03_03 = new JTextField("");
        tF_35_03_03.setBounds(656, 511, 48, 19);
        tF_35_03_03.setEditable(false);
        tF_35_03_03.setName("tF_35_03_03");

        JCheckBox checkBox_66 = new JCheckBox("全件選択");
        checkBox_66.setName("cbx_35_all");
        checkBox_66.setBounds(712, 464, 88, 21);
        p.add(checkBox_66);
        addActionCheckboxAll(checkBox_66);

        // CMM045
        JLabel lb_30 = new JLabel("CMM045");
        lb_30.setName("lb_30");
        lb_30.setBounds(12, 562, 73, 13);

        JLabel lb_30_1 = new JLabel("SCENARIO1");
        lb_30_1.setName("lb_30_1");
        lb_30_1.setBounds(158, 562, 78, 13);

        JCheckBox cbx_30_1_1 = new JCheckBox("CASE1");
        cbx_30_1_1.setBounds(304, 558, 71, 21);
        cbx_30_1_1.setName("cbx_30_1_1");

        JTextField tF_30_01_01 = new JTextField("");
        tF_30_01_01.setBounds(384, 559, 48, 19);
        tF_30_01_01.setEditable(false);
        tF_30_01_01.setName("tF_30_01_01");

        JLabel lb_30_2 = new JLabel("SCENARIO2");
        lb_30_2.setName("lb_30_2");
        lb_30_2.setBounds(158, 585, 78, 13);

        JCheckBox cbx_30_2_1 = new JCheckBox("CASE1");
        cbx_30_2_1.setBounds(304, 581, 71, 21);
        cbx_30_2_1.setName("cbx_30_2_1");

        JTextField tF_30_02_01 = new JTextField("");
        tF_30_02_01.setBounds(384, 582, 48, 19);
        tF_30_02_01.setEditable(false);
        tF_30_02_01.setName("tF_30_02_01");

        JCheckBox cbx_30_2_2 = new JCheckBox("CASE2");
        cbx_30_2_2.setBounds(440, 581, 71, 21);
        cbx_30_2_2.setName("cbx_30_2_2");

        JTextField tF_30_02_02 = new JTextField("");
        tF_30_02_02.setBounds(520, 582, 48, 19);
        tF_30_02_02.setEditable(false);
        tF_30_02_02.setName("tF_30_02_02");

        // KSC001
        JLabel lb_9 = new JLabel("KSC001");
        lb_9.setName("lb_9");
        lb_9.setBounds(12, 632, 73, 13);

        JLabel lb_9_1 = new JLabel("SCENARIO1");
        lb_9_1.setName("lb_9_1");
        lb_9_1.setBounds(158, 632, 78, 13);

        JCheckBox cbx_9_1_1 = new JCheckBox("CASE1");
        cbx_9_1_1.setBounds(304, 628, 71, 21);
        cbx_9_1_1.setName("cbx_9_1_1");

        JTextField tF_09_01_01 = new JTextField("");
        tF_09_01_01.setBounds(384, 629, 48, 19);
        tF_09_01_01.setEditable(false);
        tF_09_01_01.setName("tF_09_01_01");

        // CPS001
        JLabel lb_2 = new JLabel("CPS001");
        lb_2.setName("lb_2");
        lb_2.setBounds(12, 679, 73, 13);

        JLabel lb_2_1 = new JLabel("SCENARIO1");
        lb_2_1.setName("lb_2_1");
        lb_2_1.setBounds(158, 679, 78, 13);

        JLabel lb_2_2 = new JLabel("SCENARIO2");
        lb_2_2.setName("lb_2_2");
        lb_2_2.setBounds(158, 702, 78, 13);

        JCheckBox cbx_2_2_1 = new JCheckBox("CASE1");
        cbx_2_2_1.setName("cbx_2_2_1");
        cbx_2_2_1.setBounds(304, 698, 71, 21);

        JCheckBox cbx_2_1_1 = new JCheckBox("CASE1");
        cbx_2_1_1.setName("cbx_2_1_1");
        cbx_2_1_1.setBounds(304, 675, 71, 21);

        JTextField tF_02_01_01 = new JTextField("");
        tF_02_01_01.setName("tF_02_01_01");
        tF_02_01_01.setEditable(false);
        tF_02_01_01.setBounds(384, 676, 48, 19);

        JTextField tF_02_02_01 = new JTextField("");
        tF_02_02_01.setName("tF_02_02_01");
        tF_02_02_01.setEditable(false);
        tF_02_02_01.setBounds(384, 699, 48, 19);

        JCheckBox cbx_2_2_2 = new JCheckBox("CASE2");
        cbx_2_2_2.setName("cbx_2_2_2");
        cbx_2_2_2.setBounds(440, 698, 71, 21);

        JTextField tF_02_02_02 = new JTextField("");
        tF_02_02_02.setName("tF_02_02_02");
        tF_02_02_02.setEditable(false);
        tF_02_02_02.setBounds(520, 699, 48, 19);

        // KTG029
        JLabel lb_5 = new JLabel("KTG029");
        lb_5.setName("lb_5");
        lb_5.setBounds(12, 754, 73, 13);

        JLabel lb_5_1 = new JLabel("SCENARIO1");
        lb_5_1.setName("lb_5_1");
        lb_5_1.setBounds(158, 754, 78, 13);

        JCheckBox cbx_5_1_1 = new JCheckBox("CASE1");
        cbx_5_1_1.setName("cbx_5_1_1");
        cbx_5_1_1.setBounds(304, 750, 71, 21);

        JTextField tF_05_01_01 = new JTextField("");
        tF_05_01_01.setName("tF_05_01_01");
        tF_05_01_01.setEditable(false);
        tF_05_01_01.setBounds(384, 751, 48, 19);

        JCheckBox cbx_5_1_2 = new JCheckBox("CASE2");
        cbx_5_1_2.setName("cbx_5_1_2");
        cbx_5_1_2.setBounds(440, 750, 71, 21);

        JTextField tF_05_01_02 = new JTextField("");
        tF_05_01_02.setName("tF_05_01_02");
        tF_05_01_02.setEditable(false);
        tF_05_01_02.setBounds(520, 751, 48, 19);

        JCheckBox cbx_5_1_3 = new JCheckBox("CASE3");
        cbx_5_1_3.setName("cbx_5_1_3");
        cbx_5_1_3.setBounds(576, 750, 71, 21);

        JTextField tF_05_01_03 = new JTextField("");
        tF_05_01_03.setName("tF_05_01_03");
        tF_05_01_03.setEditable(false);
        tF_05_01_03.setBounds(656, 751, 48, 19);

        JCheckBox cbx_5_1_4 = new JCheckBox("CASE4");
        cbx_5_1_4.setName("cbx_5_1_4");
        cbx_5_1_4.setBounds(712, 750, 71, 21);

        JTextField tF_05_01_04 = new JTextField("");
        tF_05_01_04.setName("tF_05_01_04");
        tF_05_01_04.setEditable(false);
        tF_05_01_04.setBounds(792, 751, 48, 19);

        JCheckBox cbx_5_1_5 = new JCheckBox("CASE5");
        cbx_5_1_5.setName("cbx_5_1_5");
        cbx_5_1_5.setBounds(852, 750, 71, 21);

        JTextField tF_05_01_05 = new JTextField("");
        tF_05_01_05.setName("tF_05_01_05");
        tF_05_01_05.setEditable(false);
        tF_05_01_05.setBounds(932, 751, 48, 19);

        JCheckBox cbx_5_1_6 = new JCheckBox("CASE6");
        cbx_5_1_6.setName("cbx_5_1_6");
        cbx_5_1_6.setBounds(992, 750, 71, 21);

        JTextField tF_05_01_06 = new JTextField("");
        tF_05_01_06.setName("tF_05_01_06");
        tF_05_01_06.setEditable(false);
        tF_05_01_06.setBounds(1072, 751, 48, 19);

        JCheckBox cbx_5_1_7 = new JCheckBox("CASE7");
        cbx_5_1_7.setName("cbx_5_1_7");
        cbx_5_1_7.setBounds(304, 773, 71, 21);

        JTextField tF_05_01_07 = new JTextField("");
        tF_05_01_07.setName("tF_05_01_07");
        tF_05_01_07.setEditable(false);
        tF_05_01_07.setBounds(384, 774, 48, 19);

        JCheckBox cbx_5_1_8 = new JCheckBox("CASE8");
        cbx_5_1_8.setName("cbx_5_1_8");
        cbx_5_1_8.setBounds(440, 773, 71, 21);

        JTextField tF_05_01_08 = new JTextField("");
        tF_05_01_08.setName("tF_05_01_08");
        tF_05_01_08.setEditable(false);
        tF_05_01_08.setBounds(520, 774, 48, 19);

        JCheckBox cbx_5_1_9 = new JCheckBox("CASE9");
        cbx_5_1_9.setName("cbx_5_1_9");
        cbx_5_1_9.setBounds(576, 773, 71, 21);

        JTextField tF_05_01_09 = new JTextField("");
        tF_05_01_09.setName("tF_05_01_09");
        tF_05_01_09.setEditable(false);
        tF_05_01_09.setBounds(656, 774, 48, 19);

        JCheckBox cbx_5_1_10 = new JCheckBox("CASE10");
        cbx_5_1_10.setName("cbx_5_1_10");
        cbx_5_1_10.setBounds(712, 773, 71, 21);

        JTextField tF_05_01_10 = new JTextField("");
        tF_05_01_10.setName("tF_05_01_10");
        tF_05_01_10.setEditable(false);
        tF_05_01_10.setBounds(792, 774, 48, 19);

        JCheckBox cbx_5_1_11 = new JCheckBox("CASE11");
        cbx_5_1_11.setName("cbx_5_1_11");
        cbx_5_1_11.setBounds(852, 773, 71, 21);

        JTextField tF_05_01_11 = new JTextField("");
        tF_05_01_11.setName("tF_05_01_11");
        tF_05_01_11.setEditable(false);
        tF_05_01_11.setBounds(932, 774, 48, 19);

        JCheckBox cbx_5_1_12 = new JCheckBox("CASE12");
        cbx_5_1_12.setName("cbx_5_1_12");
        cbx_5_1_12.setBounds(992, 773, 71, 21);

        JTextField tF_05_01_12 = new JTextField("");
        tF_05_01_12.setName("tF_05_01_12");
        tF_05_01_12.setEditable(false);
        tF_05_01_12.setBounds(1072, 774, 48, 19);

        JCheckBox cbx_5_1_13 = new JCheckBox("CASE13");
        cbx_5_1_13.setName("cbx_5_1_13");
        cbx_5_1_13.setBounds(304, 796, 71, 21);

        JTextField tF_05_01_13 = new JTextField("");
        tF_05_01_13.setName("tF_05_01_13");
        tF_05_01_13.setEditable(false);
        tF_05_01_13.setBounds(384, 797, 48, 19);

        JCheckBox cbx_5_1_14 = new JCheckBox("CASE14");
        cbx_5_1_14.setName("cbx_5_1_14");
        cbx_5_1_14.setBounds(440, 796, 71, 21);

        JTextField tF_05_01_14 = new JTextField("");
        tF_05_01_14.setName("tF_05_01_14");
        tF_05_01_14.setEditable(false);
        tF_05_01_14.setBounds(520, 797, 48, 19);

        JTextField tF_05_01_15 = new JTextField("");
        tF_05_01_15.setName("tF_05_01_15");
        tF_05_01_15.setEditable(false);
        tF_05_01_15.setBounds(656, 797, 48, 19);

        JCheckBox cbx_5_1_15 = new JCheckBox("CASE15");
        cbx_5_1_15.setName("cbx_5_1_15");
        cbx_5_1_15.setBounds(576, 796, 71, 21);

        JCheckBox cbx_5_1_16 = new JCheckBox("CASE16");
        cbx_5_1_16.setName("cbx_5_1_16");
        cbx_5_1_16.setBounds(712, 796, 71, 21);

        JTextField tF_05_01_16 = new JTextField("");
        tF_05_01_16.setName("tF_05_01_16");
        tF_05_01_16.setEditable(false);
        tF_05_01_16.setBounds(792, 797, 48, 19);

        JCheckBox cbx_5_1_17 = new JCheckBox("CASE17");
        cbx_5_1_17.setName("cbx_5_1_17");
        cbx_5_1_17.setBounds(852, 796, 71, 21);

        JTextField tF_05_01_17 = new JTextField("");
        tF_05_01_17.setName("tF_05_01_17");
        tF_05_01_17.setEditable(false);
        tF_05_01_17.setBounds(932, 797, 48, 19);

        JCheckBox checkBox_67 = new JCheckBox("全件選択");
        checkBox_67.setName("cbx_5_all");
        checkBox_67.setBounds(1128, 750, 88, 21);
        p.add(checkBox_67);
        addActionCheckboxAll(checkBox_67);

        // add item to panel
        p.add(lb_32);
        p.add(lb_32_1);
        p.add(cbx_32_1_1);
        p.add(cbx_32_1_2);
        p.add(tF_32_01_01);
        p.add(tF_32_01_02);
        p.add(lb_32_2);
        p.add(cbx_32_2_1);
        p.add(cbx_32_2_2);
        p.add(tF_32_02_01);
        p.add(tF_32_02_02);
        p.add(lb_32_3);
        p.add(cbx_32_3_1);
        p.add(cbx_32_3_2);
        p.add(cbx_32_3_3);
        p.add(tF_32_03_01);
        p.add(tF_32_03_02);
        p.add(tF_32_03_03);
        p.add(lb_3);
        p.add(lb_3_1);
        p.add(cbx_3_1_1);
        p.add(cbx_3_1_2);
        p.add(tF_03_01_01);
        p.add(tF_03_01_02);
        p.add(lb_6);
        p.add(lb_6_1);
        p.add(cbx_6_1_1);
        p.add(cbx_6_1_2);
        p.add(tF_06_01_01);
        p.add(tF_06_01_02);
        p.add(lb_24);
        p.add(lb_24_1);
        p.add(cbx_24_1_1);
        p.add(cbx_24_1_2);
        p.add(tF_24_01_01);
        p.add(tF_24_01_02);
        p.add(lb_25);
        p.add(lb_25_1);
        p.add(cbx_25_1_1);
        p.add(tF_25_01_01);
        p.add(lb_26);
        p.add(lb_26_1);
        p.add(cbx_26_1_1);
        p.add(tF_26_01_01);
        p.add(lb_27);
        p.add(lb_27_1);
        p.add(cbx_27_1_1);
        p.add(tF_27_01_01);
        p.add(lb_28);
        p.add(lb_28_1);
        p.add(cbx_28_1_1);
        p.add(tF_28_01_01);
        p.add(cbx_28_1_2);
        p.add(tF_28_01_02);
        p.add(cbx_28_1_3);
        p.add(tF_28_01_03);
        p.add(lb_30);
        p.add(lb_30_1);
        p.add(cbx_30_1_1);
        p.add(tF_30_01_01);
        p.add(lb_30_2);
        p.add(cbx_30_2_1);
        p.add(tF_30_02_01);
        p.add(cbx_30_2_2);
        p.add(tF_30_02_02);
        p.add(lb_36);
        p.add(lb_36_1);
        p.add(cbx_36_1_1);
        p.add(tF_36_01_01);
        p.add(lb_36_2);
        p.add(cbx_36_2_1);
        p.add(tF_36_02_01);
        p.add(cbx_36_2_2);
        p.add(tF_36_02_02);
        p.add(lb_35);
        p.add(lb_35_1);
        p.add(cbx_35_1_1);
        p.add(tF_35_01_01);
        p.add(cbx_35_1_2);
        p.add(tF_35_01_02);
        p.add(lb_35_2);
        p.add(cbx_35_2_1);
        p.add(tF_35_02_01);
        p.add(lb_35_3);
        p.add(cbx_35_3_1);
        p.add(tF_35_03_01);
        p.add(cbx_35_3_2);
        p.add(tF_35_03_02);
        p.add(cbx_35_3_3);
        p.add(tF_35_03_03);
        p.add(lb_9);
        p.add(lb_9_1);
        p.add(cbx_9_1_1);
        p.add(tF_09_01_01);
        p.add(lb_2);
        p.add(lb_2_1);
        p.add(cbx_2_1_1);
        p.add(tF_02_01_01);
        p.add(lb_2_2);
        p.add(cbx_2_2_1);
        p.add(tF_02_02_01);
        p.add(cbx_2_2_2);
        p.add(tF_02_02_02);
        p.add(lb_5);
        p.add(lb_5_1);
        p.add(cbx_5_1_1);
        p.add(tF_05_01_01);
        p.add(cbx_5_1_2);
        p.add(tF_05_01_02);
        p.add(cbx_5_1_3);
        p.add(tF_05_01_03);
        p.add(cbx_5_1_4);
        p.add(tF_05_01_04);
        p.add(cbx_5_1_5);
        p.add(tF_05_01_05);
        p.add(cbx_5_1_6);
        p.add(tF_05_01_06);
        p.add(cbx_5_1_7);
        p.add(tF_05_01_07);
        p.add(cbx_5_1_8);
        p.add(tF_05_01_08);
        p.add(cbx_5_1_9);
        p.add(tF_05_01_09);
        p.add(cbx_5_1_10);
        p.add(tF_05_01_10);
        p.add(cbx_5_1_11);
        p.add(tF_05_01_11);
        p.add(cbx_5_1_12);
        p.add(tF_05_01_12);
        p.add(cbx_5_1_13);
        p.add(tF_05_01_13);
        p.add(cbx_5_1_14);
        p.add(tF_05_01_14);
        p.add(cbx_5_1_15);
        p.add(tF_05_01_15);
        p.add(cbx_5_1_16);
        p.add(tF_05_01_16);
        p.add(cbx_5_1_17);
        p.add(tF_05_01_17);

        addActionCheckbox(cbx_32_1_1, "tF_32_01_01", "kaf005.Scenario1Case1");
        addActionCheckbox(cbx_32_1_2, "tF_32_01_02", "kaf005.Scenario1Case2");
        addActionCheckbox(cbx_32_2_1, "tF_32_02_01", "kaf005.Scenario2Case1");
        addActionCheckbox(cbx_32_2_2, "tF_32_02_02", "kaf005.Scenario2Case2");
        addActionCheckbox(cbx_32_3_1, "tF_32_03_01", "kaf005.Scenario3Case1");
        addActionCheckbox(cbx_32_3_1, "tF_32_03_01", "kaf005.Scenario3Case1");
        addActionCheckbox(cbx_32_3_2, "tF_32_03_02", "kaf005.Scenario3Case2");
        addActionCheckbox(cbx_32_3_3, "tF_32_03_03", "kaf005.Scenario3Case3");
        addActionCheckbox(cbx_3_1_1, "tF_03_01_01", "ktg001.Scenario1Case1");
        addActionCheckbox(cbx_3_1_2, "tF_03_01_02", "ktg001.Scenario1Case2");
        addActionCheckbox(cbx_6_1_1, "tF_06_01_01", "ktg030.Scenario1Case1");
        addActionCheckbox(cbx_6_1_2, "tF_06_01_02", "ktg030.Scenario1Case2");
        addActionCheckbox(cbx_24_1_1, "tF_24_01_01", "kwr001.Scenario1Case1");
        addActionCheckbox(cbx_24_1_2, "tF_24_01_02", "kwr001.Scenario1Case2");
        addActionCheckbox(cbx_25_1_1, "tF_25_01_01", "kwr008.Scenario1Case1");
        addActionCheckbox(cbx_26_1_1, "tF_26_01_01", "kwr006.Scenario1Case1");
        addActionCheckbox(cbx_27_1_1, "tF_27_01_01", "kdr001.Scenario1Case1");
        addActionCheckbox(cbx_28_1_1, "tF_28_01_01", "cmm018.Scenario1Case1");
        addActionCheckbox(cbx_28_1_2, "tF_28_01_02", "cmm018.Scenario1Case2");
        addActionCheckbox(cbx_28_1_3, "tF_28_01_03", "cmm018.Scenario1Case3");
        addActionCheckbox(cbx_30_1_1, "tF_30_01_01", "cmm045.Scenario1Case1");
        addActionCheckbox(cbx_30_2_1, "tF_30_02_01", "cmm045.Scenario2Case1");
        addActionCheckbox(cbx_30_2_2, "tF_30_02_02", "cmm045.Scenario2Case2");
        addActionCheckbox(cbx_36_1_1, "tF_36_01_01", "kaf011.Scenario1Case1");
        addActionCheckbox(cbx_36_2_1, "tF_36_02_01", "kaf011.Scenario2Case1");
        addActionCheckbox(cbx_36_2_2, "tF_36_02_02", "kaf011.Scenario2Case2");
        addActionCheckbox(cbx_35_1_1, "tF_35_01_01", "kaf010.Scenario1Case1");
        addActionCheckbox(cbx_35_1_2, "tF_35_01_02", "kaf010.Scenario1Case2");
        addActionCheckbox(cbx_35_2_1, "tF_35_02_01", "kaf010.Scenario2Case1");
        addActionCheckbox(cbx_35_3_1, "tF_35_03_01", "kaf010.Scenario3Case1");
        addActionCheckbox(cbx_35_3_2, "tF_35_03_02", "kaf010.Scenario3Case2");
        addActionCheckbox(cbx_35_3_3, "tF_35_03_03", "kaf010.Scenario3Case3");
        addActionCheckbox(cbx_9_1_1, "tF_09_01_01", "ksc001.Scenario1Case1");
        addActionCheckbox(cbx_2_1_1, "tF_02_01_01", "cps001.Scenario1Case1");
        addActionCheckbox(cbx_2_2_1, "tF_02_02_01", "cps001.Scenario2Case1");
        addActionCheckbox(cbx_2_2_2, "tF_02_02_02", "cps001.Scenario2Case2");
        addActionCheckbox(cbx_5_1_1, "tF_05_01_01", "ktg029.Scenario1Case1");
        addActionCheckbox(cbx_5_1_2, "tF_05_01_02", "ktg029.Scenario1Case2");
        addActionCheckbox(cbx_5_1_3, "tF_05_01_03", "ktg029.Scenario1Case3");
        addActionCheckbox(cbx_5_1_4, "tF_05_01_04", "ktg029.Scenario1Case4");
        addActionCheckbox(cbx_5_1_5, "tF_05_01_05", "ktg029.Scenario1Case5");
        addActionCheckbox(cbx_5_1_6, "tF_05_01_06", "ktg029.Scenario1Case6");
        addActionCheckbox(cbx_5_1_7, "tF_05_01_07", "ktg029.Scenario1Case7");
        addActionCheckbox(cbx_5_1_8, "tF_05_01_08", "ktg029.Scenario1Case8");
        addActionCheckbox(cbx_5_1_9, "tF_05_01_09", "ktg029.Scenario1Case9");
        addActionCheckbox(cbx_5_1_10, "tF_05_01_10", "ktg029.Scenario1Case10");
        addActionCheckbox(cbx_5_1_11, "tF_05_01_11", "ktg029.Scenario1Case11");
        addActionCheckbox(cbx_5_1_12, "tF_05_01_12", "ktg029.Scenario1Case12");
        addActionCheckbox(cbx_5_1_13, "tF_05_01_13", "ktg029.Scenario1Case13");
        addActionCheckbox(cbx_5_1_14, "tF_05_01_14", "ktg029.Scenario1Case14");
        addActionCheckbox(cbx_5_1_15, "tF_05_01_15", "ktg029.Scenario1Case15");
        addActionCheckbox(cbx_5_1_16, "tF_05_01_16", "ktg029.Scenario1Case16");
        addActionCheckbox(cbx_5_1_17, "tF_05_01_17", "ktg029.Scenario1Case17");
    }

}