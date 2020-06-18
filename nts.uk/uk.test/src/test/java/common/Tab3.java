package common;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Tab3 extends CreateMainTest{

    public JScrollPane scrollPane;

    public Tab3() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setPreferredSize(new Dimension(1250, 1000));

        scrollPane = new JScrollPane(p);
        scrollPane.setBounds(0, 0, 1280, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // KAF018
        JLabel lb_38 = new JLabel("KAF018");
        lb_38.setName("lb_38");
        lb_38.setBounds(12, 10, 73, 13);
        p.add(lb_38);

        JLabel lb_38_1 = new JLabel("SCENARIO1");
        lb_38_1.setName("lb_38_1");
        lb_38_1.setBounds(158, 10, 78, 13);
        p.add(lb_38_1);

        JCheckBox cbx_38_1_1 = new JCheckBox("CASE1");
        cbx_38_1_1.setName("cbx_38_1_1");
        cbx_38_1_1.setBounds(304, 6, 71, 21);
        p.add(cbx_38_1_1);
        addActionCheckbox(cbx_38_1_1, "tF_38_01_01", "kaf018.Scenario1Case1");

        JTextField tF_38_01_01 = new JTextField("");
        tF_38_01_01.setName("tF_38_01_01");
        tF_38_01_01.setEditable(false);
        tF_38_01_01.setBounds(384, 7, 48, 19);
        p.add(tF_38_01_01);

        JTextField tF_38_02_01 = new JTextField("");
        tF_38_02_01.setName("tF_38_02_01");
        tF_38_02_01.setEditable(false);
        tF_38_02_01.setBounds(384, 30, 48, 19);
        p.add(tF_38_02_01);

        JCheckBox cbx_38_2_1 = new JCheckBox("CASE1");
        cbx_38_2_1.setName("cbx_38_2_1");
        cbx_38_2_1.setBounds(304, 29, 71, 21);
        p.add(cbx_38_2_1);
        addActionCheckbox(cbx_38_2_1, "tF_38_02_01", "kaf018.Scenario2Case1");

        JLabel lb_38_2 = new JLabel("SCENARIO2");
        lb_38_2.setName("lb_38_2");
        lb_38_2.setBounds(158, 32, 78, 13);
        p.add(lb_38_2);

        // KAL001
        JLabel lb_39 = new JLabel("KAL001");
        lb_39.setName("lb_39");
        lb_39.setBounds(12, 70, 73, 13);
        p.add(lb_39);

        JTextField tF_39_02_01 = new JTextField("");
        tF_39_02_01.setName("tF_39_02_01");
        tF_39_02_01.setEditable(false);
        tF_39_02_01.setBounds(384, 90, 48, 19);
        p.add(tF_39_02_01);

        JTextField tF_39_01_01 = new JTextField("");
        tF_39_01_01.setName("tF_39_01_01");
        tF_39_01_01.setEditable(false);
        tF_39_01_01.setBounds(384, 67, 48, 19);
        p.add(tF_39_01_01);

        JCheckBox cbx_39_1_1 = new JCheckBox("CASE1");
        cbx_39_1_1.setName("cbx_39_1_1");
        cbx_39_1_1.setBounds(304, 66, 71, 21);
        p.add(cbx_39_1_1);
        addActionCheckbox(cbx_39_1_1, "tF_39_01_01", "kal001.Scenario1Case1");

        JCheckBox cbx_39_2_1 = new JCheckBox("CASE1");
        cbx_39_2_1.setName("cbx_39_2_1");
        cbx_39_2_1.setBounds(304, 89, 71, 21);
        p.add(cbx_39_2_1);
        addActionCheckbox(cbx_39_2_1, "tF_39_02_01", "kal001.Scenario2Case1");

        JLabel lb_39_2 = new JLabel("SCENARIO2");
        lb_39_2.setName("lb_39_2");
        lb_39_2.setBounds(158, 92, 78, 13);
        p.add(lb_39_2);

        JLabel lb_39_1 = new JLabel("SCENARIO1");
        lb_39_1.setName("lb_39_1");
        lb_39_1.setBounds(158, 70, 78, 13);
        p.add(lb_39_1);

        // CMF003/CMF004
        JLabel lb_42 = new JLabel("CMF003/CMF004");
        lb_42.setName("lb_42");
        lb_42.setBounds(12, 133, 96, 13);
        p.add(lb_42);

        JLabel lb_42_1 = new JLabel("SCENARIO1");
        lb_42_1.setName("lb_42_1");
        lb_42_1.setBounds(158, 133, 78, 13);
        p.add(lb_42_1);

        JCheckBox cbx_42_1_1 = new JCheckBox("CASE1");
        cbx_42_1_1.setName("cbx_42_1_1");
        cbx_42_1_1.setBounds(304, 129, 71, 21);
        p.add(cbx_42_1_1);
        addActionCheckbox(cbx_42_1_1, "tF_42_01_01", "cmf003.Scenario1Case1");

        JTextField tF_42_01_01 = new JTextField("");
        tF_42_01_01.setName("tF_42_01_01");
        tF_42_01_01.setEditable(false);
        tF_42_01_01.setBounds(384, 130, 48, 19);
        p.add(tF_42_01_01);

        JCheckBox cbx_42_1_2 = new JCheckBox("CASE2");
        cbx_42_1_2.setName("cbx_42_1_2");
        cbx_42_1_2.setBounds(440, 129, 71, 21);
        p.add(cbx_42_1_2);
        addActionCheckbox(cbx_42_1_2, "tF_42_01_02", "cmf003.Scenario1Case2");

        JTextField tF_42_01_02 = new JTextField("");
        tF_42_01_02.setName("tF_42_01_02");
        tF_42_01_02.setEditable(false);
        tF_42_01_02.setBounds(519, 130, 48, 19);
        p.add(tF_42_01_02);

        // CCG001
        JLabel lb_44 = new JLabel("CCG001");
        lb_44.setName("lb_44");
        lb_44.setBounds(12, 171, 73, 13);
        p.add(lb_44);

        JLabel lb_44_1 = new JLabel("SCENARIO1");
        lb_44_1.setName("lb_44_1");
        lb_44_1.setBounds(158, 171, 78, 13);
        p.add(lb_44_1);

        JCheckBox cbx_44_1_1 = new JCheckBox("CASE1");
        cbx_44_1_1.setName("cbx_44_1_1");
        cbx_44_1_1.setBounds(304, 167, 71, 21);
        p.add(cbx_44_1_1);
        addActionCheckbox(cbx_44_1_1, "tF_44_01_01", "ccg001.Scenario1Case1");

        JTextField tF_44_01_01 = new JTextField("");
        tF_44_01_01.setName("tF_44_01_01");
        tF_44_01_01.setEditable(false);
        tF_44_01_01.setBounds(384, 168, 48, 19);
        p.add(tF_44_01_01);

        JCheckBox cbx_44_1_2 = new JCheckBox("CASE2");
        cbx_44_1_2.setName("cbx_44_1_2");
        cbx_44_1_2.setBounds(440, 167, 71, 21);
        p.add(cbx_44_1_2);
        addActionCheckbox(cbx_44_1_2, "tF_44_01_02", "ccg001.Scenario1Case2");

        JTextField tF_44_01_02 = new JTextField("");
        tF_44_01_02.setName("tF_44_01_02");
        tF_44_01_02.setEditable(false);
        tF_44_01_02.setBounds(520, 168, 48, 19);
        p.add(tF_44_01_02);

        JCheckBox cbx_44_1_3 = new JCheckBox("CASE3");
        cbx_44_1_3.setName("cbx_44_1_3");
        cbx_44_1_3.setBounds(576, 167, 71, 21);
        p.add(cbx_44_1_3);
        addActionCheckbox(cbx_44_1_3, "tF_44_01_03", "ccg001.Scenario1Case3");

        JTextField tF_44_01_03 = new JTextField("");
        tF_44_01_03.setName("tF_44_01_03");
        tF_44_01_03.setEditable(false);
        tF_44_01_03.setBounds(656, 168, 48, 19);
        p.add(tF_44_01_03);

        JCheckBox cbx_44_1_4 = new JCheckBox("CASE4");
        cbx_44_1_4.setName("cbx_44_1_4");
        cbx_44_1_4.setBounds(712, 167, 71, 21);
        p.add(cbx_44_1_4);
        addActionCheckbox(cbx_44_1_4, "tF_44_01_04", "ccg001.Scenario1Case4");

        JTextField tF_44_01_04 = new JTextField("");
        tF_44_01_04.setName("tF_44_01_04");
        tF_44_01_04.setEditable(false);
        tF_44_01_04.setBounds(792, 168, 48, 19);
        p.add(tF_44_01_04);

        JCheckBox cbx_44_1_5 = new JCheckBox("CASE5");
        cbx_44_1_5.setName("cbx_44_1_5");
        cbx_44_1_5.setBounds(852, 167, 71, 21);
        p.add(cbx_44_1_5);
        addActionCheckbox(cbx_44_1_5, "tF_44_01_05", "ccg001.Scenario1Case5");

        JTextField tF_44_01_05 = new JTextField("");
        tF_44_01_05.setName("tF_44_01_05");
        tF_44_01_05.setEditable(false);
        tF_44_01_05.setBounds(932, 168, 48, 19);
        p.add(tF_44_01_05);

        JCheckBox cbx_44_1_6 = new JCheckBox("CASE6");
        cbx_44_1_6.setName("cbx_44_1_6");
        cbx_44_1_6.setBounds(992, 167, 71, 21);
        p.add(cbx_44_1_6);
        addActionCheckbox(cbx_44_1_6, "tF_44_01_06", "ccg001.Scenario1Case6");

        JTextField tF_44_01_06 = new JTextField("");
        tF_44_01_06.setName("tF_44_01_06");
        tF_44_01_06.setEditable(false);
        tF_44_01_06.setBounds(1072, 168, 48, 19);
        p.add(tF_44_01_06);

        JCheckBox cbx_44_2_1 = new JCheckBox("CASE1");
        cbx_44_2_1.setName("cbx_44_2_1");
        cbx_44_2_1.setBounds(304, 190, 71, 21);
        p.add(cbx_44_2_1);
        addActionCheckbox(cbx_44_2_1, "tF_44_02_01", "ccg001.Scenario2Case1");

        JTextField tF_44_02_01 = new JTextField("");
        tF_44_02_01.setName("tF_44_02_01");
        tF_44_02_01.setEditable(false);
        tF_44_02_01.setBounds(384, 191, 48, 19);
        p.add(tF_44_02_01);

        JCheckBox cbx_44_2_2 = new JCheckBox("CASE2");
        cbx_44_2_2.setName("cbx_44_2_2");
        cbx_44_2_2.setBounds(440, 190, 71, 21);
        p.add(cbx_44_2_2);
        addActionCheckbox(cbx_44_2_2, "tF_44_02_02", "ccg001.Scenario2Case2");

        JTextField textField_19 = new JTextField("");
        textField_19.setName("tF_44_02_02");
        textField_19.setEditable(false);
        textField_19.setBounds(520, 191, 48, 19);
        p.add(textField_19);

        JCheckBox chckbxCase_2 = new JCheckBox("CASE3");
        chckbxCase_2.setName("cbx_44_2_3");
        chckbxCase_2.setBounds(576, 190, 71, 21);
        p.add(chckbxCase_2);
        addActionCheckbox(chckbxCase_2, "tF_44_02_03", "ccg001.Scenario2Case3");

        JTextField textField_20 = new JTextField("");
        textField_20.setName("tF_44_02_03");
        textField_20.setEditable(false);
        textField_20.setBounds(656, 191, 48, 19);
        p.add(textField_20);

        JCheckBox chckbxCase_3 = new JCheckBox("CASE4");
        chckbxCase_3.setName("cbx_44_2_4");
        chckbxCase_3.setBounds(712, 190, 71, 21);
        p.add(chckbxCase_3);
        addActionCheckbox(chckbxCase_3, "tF_44_02_04", "ccg001.Scenario2Case4");

        JTextField textField_21 = new JTextField("");
        textField_21.setName("tF_44_02_04");
        textField_21.setEditable(false);
        textField_21.setBounds(792, 191, 48, 19);
        p.add(textField_21);

        JCheckBox chckbxCase_4 = new JCheckBox("CASE5");
        chckbxCase_4.setName("cbx_44_2_5");
        chckbxCase_4.setBounds(852, 190, 71, 21);
        p.add(chckbxCase_4);
        addActionCheckbox(chckbxCase_4, "tF_44_02_05", "ccg001.Scenario2Case5");

        JTextField textField_22 = new JTextField("");
        textField_22.setName("tF_44_02_05");
        textField_22.setEditable(false);
        textField_22.setBounds(932, 191, 48, 19);
        p.add(textField_22);

        JCheckBox chckbxCase_5 = new JCheckBox("CASE6");
        chckbxCase_5.setName("cbx_44_2_6");
        chckbxCase_5.setBounds(992, 190, 71, 21);
        p.add(chckbxCase_5);
        addActionCheckbox(chckbxCase_5, "tF_44_02_06", "ccg001.Scenario2Case6");

        JTextField textField_23 = new JTextField("");
        textField_23.setName("tF_44_02_06");
        textField_23.setEditable(false);
        textField_23.setBounds(1072, 191, 48, 19);
        p.add(textField_23);

        JLabel label_7 = new JLabel("SCENARIO2");
        label_7.setName("lb_44_2");
        label_7.setBounds(158, 194, 78, 13);
        p.add(label_7);

        JCheckBox checkBox_18 = new JCheckBox("CASE7");
        checkBox_18.setName("cbx_44_2_7");
        checkBox_18.setBounds(304, 213, 71, 21);
        p.add(checkBox_18);
        addActionCheckbox(checkBox_18, "tF_44_02_07", "ccg001.Scenario2Case7");

        JTextField textField_24 = new JTextField("");
        textField_24.setName("tF_44_02_07");
        textField_24.setEditable(false);
        textField_24.setBounds(384, 214, 48, 19);
        p.add(textField_24);

        JCheckBox checkBox_19 = new JCheckBox("CASE8");
        checkBox_19.setName("cbx_44_2_8");
        checkBox_19.setBounds(440, 213, 71, 21);
        p.add(checkBox_19);
        addActionCheckbox(checkBox_19, "tF_44_02_08", "ccg001.Scenario2Case8");

        JTextField textField_25 = new JTextField("");
        textField_25.setName("tF_44_02_08");
        textField_25.setEditable(false);
        textField_25.setBounds(520, 214, 48, 19);
        p.add(textField_25);

        JCheckBox checkBox_20 = new JCheckBox("CASE9");
        checkBox_20.setName("cbx_44_2_9");
        checkBox_20.setBounds(576, 213, 71, 21);
        p.add(checkBox_20);
        addActionCheckbox(checkBox_20, "tF_44_02_09", "ccg001.Scenario2Case9");

        JTextField textField_26 = new JTextField("");
        textField_26.setName("tF_44_02_09");
        textField_26.setEditable(false);
        textField_26.setBounds(656, 214, 48, 19);
        p.add(textField_26);

        JCheckBox checkBox_21 = new JCheckBox("CASE10");
        checkBox_21.setName("cbx_44_2_10");
        checkBox_21.setBounds(712, 213, 71, 21);
        p.add(checkBox_21);
        addActionCheckbox(checkBox_21, "tF_44_02_10", "ccg001.Scenario2Case10");

        JTextField textField_27 = new JTextField("");
        textField_27.setName("tF_44_02_10");
        textField_27.setEditable(false);
        textField_27.setBounds(792, 214, 48, 19);
        p.add(textField_27);

        JCheckBox checkBox_75 = new JCheckBox("全件選択");
        checkBox_75.setName("cbx_44_all");
        checkBox_75.setBounds(1128, 167, 88, 21);
        p.add(checkBox_75);
        addActionCheckboxAll(checkBox_75);

        // CLI003
        JLabel lblCli = new JLabel("CLI003");
        lblCli.setName("lb_45");
        lblCli.setBounds(12, 257, 73, 13);
        p.add(lblCli);

        JLabel label_8 = new JLabel("SCENARIO1");
        label_8.setName("lb_45_1");
        label_8.setBounds(158, 257, 78, 13);
        p.add(label_8);

        JCheckBox checkBox_12 = new JCheckBox("CASE1");
        checkBox_12.setName("cbx_45_1_1");
        checkBox_12.setBounds(304, 253, 71, 21);
        p.add(checkBox_12);
        addActionCheckbox(checkBox_12, "tF_45_01_01", "cli003.Scenario1Case1");

        JTextField textField_28 = new JTextField("");
        textField_28.setName("tF_45_01_01");
        textField_28.setEditable(false);
        textField_28.setBounds(384, 254, 48, 19);
        p.add(textField_28);

        JLabel label_9 = new JLabel("SCENARIO2");
        label_9.setName("lb_45_2");
        label_9.setBounds(158, 280, 78, 13);
        p.add(label_9);

        JCheckBox checkBox_13 = new JCheckBox("CASE1");
        checkBox_13.setName("cbx_45_2_1");
        checkBox_13.setBounds(304, 276, 71, 21);
        p.add(checkBox_13);
        addActionCheckbox(checkBox_13, "tF_45_02_01", "cli003.Scenario2Case1");

        JTextField textField_29 = new JTextField("");
        textField_29.setName("tF_45_02_01");
        textField_29.setEditable(false);
        textField_29.setBounds(384, 277, 48, 19);
        p.add(textField_29);

        JLabel label_10 = new JLabel("SCENARIO3");
        label_10.setName("lb_45_3");
        label_10.setBounds(158, 303, 78, 13);
        p.add(label_10);

        JCheckBox checkBox_14 = new JCheckBox("CASE1");
        checkBox_14.setName("cbx_45_3_1");
        checkBox_14.setBounds(304, 299, 71, 21);
        p.add(checkBox_14);
        addActionCheckbox(checkBox_14, "tF_45_03_01", "cli003.Scenario3Case1");

        JTextField textField_30 = new JTextField("");
        textField_30.setName("tF_45_03_01");
        textField_30.setEditable(false);
        textField_30.setBounds(384, 300, 48, 19);
        p.add(textField_30);

        JLabel label_11 = new JLabel("SCENARIO4");
        label_11.setName("lb_45_4");
        label_11.setBounds(158, 326, 78, 13);
        p.add(label_11);

        JCheckBox checkBox_15 = new JCheckBox("CASE1");
        checkBox_15.setName("cbx_45_4_1");
        checkBox_15.setBounds(304, 322, 71, 21);
        p.add(checkBox_15);
        addActionCheckbox(checkBox_15, "tF_45_04_01", "cli003.Scenario4Case1");

        JTextField textField_31 = new JTextField("");
        textField_31.setName("tF_45_04_01");
        textField_31.setEditable(false);
        textField_31.setBounds(384, 323, 48, 19);
        p.add(textField_31);

        JLabel label_12 = new JLabel("SCENARIO5");
        label_12.setName("lb_45_5");
        label_12.setBounds(158, 349, 78, 13);
        p.add(label_12);

        JCheckBox checkBox_16 = new JCheckBox("CASE1");
        checkBox_16.setName("cbx_45_5_1");
        checkBox_16.setBounds(304, 345, 71, 21);
        p.add(checkBox_16);
        addActionCheckbox(checkBox_16, "tF_45_05_01", "cli003.Scenario5Case1");

        JTextField textField_32 = new JTextField("");
        textField_32.setName("tF_45_05_01");
        textField_32.setEditable(false);
        textField_32.setBounds(384, 346, 48, 19);
        p.add(textField_32);

        JLabel label_13 = new JLabel("SCENARIO6");
        label_13.setName("lb_45_6");
        label_13.setBounds(158, 372, 78, 13);
        p.add(label_13);

        JCheckBox checkBox_17 = new JCheckBox("CASE1");
        checkBox_17.setName("cbx_45_6_1");
        checkBox_17.setBounds(304, 368, 71, 21);
        p.add(checkBox_17);
        addActionCheckbox(checkBox_17, "tF_45_06_01", "cli003.Scenario6Case1");

        JTextField textField_33 = new JTextField("");
        textField_33.setName("tF_45_06_01");
        textField_33.setEditable(false);
        textField_33.setBounds(384, 369, 48, 19);
        p.add(textField_33);

        JLabel lblScenario = new JLabel("SCENARIO7");
        lblScenario.setName("lb_45_7");
        lblScenario.setBounds(158, 395, 78, 13);
        p.add(lblScenario);

        JCheckBox checkBox_22 = new JCheckBox("CASE1");
        checkBox_22.setName("cbx_45_7_1");
        checkBox_22.setBounds(304, 391, 71, 21);
        p.add(checkBox_22);
        addActionCheckbox(checkBox_22, "tF_45_07_01", "cli003.Scenario7Case1");

        JTextField textField_34 = new JTextField("");
        textField_34.setName("tF_45_07_01");
        textField_34.setEditable(false);
        textField_34.setBounds(384, 392, 48, 19);
        p.add(textField_34);

        JCheckBox checkBox_76 = new JCheckBox("全件選択");
        checkBox_76.setName("cbx_45_all");
        checkBox_76.setBounds(440, 253, 88, 21);
        p.add(checkBox_76);
        addActionCheckboxAll(checkBox_76);

        //KIF013
        JLabel lblKif = new JLabel("KIF013");
        lblKif.setName("lb_16");
        lblKif.setBounds(12, 432, 96, 13);
        p.add(lblKif);

        JLabel label_15 = new JLabel("SCENARIO1");
        label_15.setName("lb_16_1");
        label_15.setBounds(158, 432, 78, 13);
        p.add(label_15);

        JCheckBox checkBox_61 = new JCheckBox("CASE1");
        checkBox_61.setName("cbx_16_1_1");
        checkBox_61.setBounds(304, 428, 71, 21);
        p.add(checkBox_61);
        addActionCheckbox(checkBox_61, "tF_16_01_01", "kif013.Scenario1Case1");

        JTextField textField_96 = new JTextField("");
        textField_96.setName("tF_16_01_01");
        textField_96.setEditable(false);
        textField_96.setBounds(384, 429, 48, 19);
        p.add(textField_96);

        JCheckBox checkBox_62 = new JCheckBox("CASE2");
        checkBox_62.setName("cbx_16_1_2");
        checkBox_62.setBounds(440, 428, 71, 21);
        p.add(checkBox_62);
        addActionCheckbox(checkBox_62, "tF_16_01_02", "kif013.Scenario1Case2");

        JTextField textField_97 = new JTextField("");
        textField_97.setName("tF_16_01_02");
        textField_97.setEditable(false);
        textField_97.setBounds(519, 429, 48, 19);
        p.add(textField_97);

        //KIF002
        JLabel lblKif_1 = new JLabel("KIF002");
        lblKif_1.setName("lb_20");
        lblKif_1.setBounds(12, 455, 96, 13);
        p.add(lblKif_1);

        JLabel label_16 = new JLabel("SCENARIO1");
        label_16.setName("lb_20_1");
        label_16.setBounds(158, 455, 78, 13);
        p.add(label_16);

        JCheckBox checkBox_63 = new JCheckBox("CASE1");
        checkBox_63.setName("cbx_20_1_1");
        checkBox_63.setBounds(304, 451, 71, 21);
        p.add(checkBox_63);
        addActionCheckbox(checkBox_63, "tF_20_01_01", "kif002.Scenario1Case1");

        JTextField textField_98 = new JTextField("");
        textField_98.setName("tF_20_01_01");
        textField_98.setEditable(false);
        textField_98.setBounds(384, 452, 48, 19);
        p.add(textField_98);

        JCheckBox checkBox_64 = new JCheckBox("CASE2");
        checkBox_64.setName("cbx_20_1_2");
        checkBox_64.setBounds(440, 451, 71, 21);
        p.add(checkBox_64);
        addActionCheckbox(checkBox_64, "tF_20_01_02", "kif002.Scenario1Case2");

        JTextField textField_99 = new JTextField("");
        textField_99.setName("tF_20_01_02");
        textField_99.setEditable(false);
        textField_99.setBounds(519, 452, 48, 19);
        p.add(textField_99);

        //KDW007
        JCheckBox checkBox_90 = new JCheckBox("CASE1");
        checkBox_90.setName("cbx_12_1_1");
        checkBox_90.setBounds(304, 491, 71, 21);
        p.add(checkBox_90);
        addActionCheckbox(checkBox_90, "tF_12_01_01", "kdw007.Scenario1Case1");

        JCheckBox checkBox_91 = new JCheckBox("CASE1");
        checkBox_91.setName("cbx_12_2_1");
        checkBox_91.setBounds(304, 514, 71, 21);
        p.add(checkBox_91);
        addActionCheckbox(checkBox_91, "tF_12_02_01", "kdw007.Scenario2Case1");

        JLabel label_4 = new JLabel("SCENARIO2");
        label_4.setName("lb_12_2");
        label_4.setBounds(158, 517, 78, 13);
        p.add(label_4);

        JLabel label_17 = new JLabel("SCENARIO1");
        label_17.setName("lb_12_1");
        label_17.setBounds(158, 495, 78, 13);
        p.add(label_17);

        JLabel lblKdw_1 = new JLabel("KDW007");
        lblKdw_1.setName("lb_12");
        lblKdw_1.setBounds(12, 495, 73, 13);
        p.add(lblKdw_1);

        //KDW001
        JLabel lblKdw_2 = new JLabel("KDW001");
        lblKdw_2.setName("lb_14");
        lblKdw_2.setBounds(12, 557, 73, 13);
        p.add(lblKdw_2);

        JLabel label_19 = new JLabel("SCENARIO1");
        label_19.setName("lb_14_1");
        label_19.setBounds(158, 557, 78, 13);
        p.add(label_19);

        JCheckBox checkBox_92 = new JCheckBox("CASE1");
        checkBox_92.setName("cbx_14_1_1");
        checkBox_92.setBounds(304, 553, 71, 21);
        p.add(checkBox_92);
        addActionCheckbox(checkBox_92, "tF_14_01_01", "kdw001.Scenario1Case1");

        //KDW004
        JLabel lblKdw_3 = new JLabel("KDW004");
        lblKdw_3.setName("lb_18");
        lblKdw_3.setBounds(12, 599, 73, 13);
        p.add(lblKdw_3);

        JLabel label_20 = new JLabel("SCENARIO1");
        label_20.setName("lb_18_1");
        label_20.setBounds(158, 599, 78, 13);
        p.add(label_20);

        JCheckBox checkBox_93 = new JCheckBox("CASE1");
        checkBox_93.setName("cbx_18_1_1");
        checkBox_93.setBounds(304, 595, 71, 21);
        p.add(checkBox_93);
        addActionCheckbox(checkBox_93, "tF_18_01_01", "kdw004.Scenario1Case1");

        JTextField textField_119 = new JTextField("");
        textField_119.setName("tF_18_01_01");
        textField_119.setEditable(false);
        textField_119.setBounds(384, 596, 48, 19);
        p.add(textField_119);

        JLabel label_21 = new JLabel("SCENARIO2");
        label_21.setName("lb_18_2");
        label_21.setBounds(158, 622, 78, 13);
        p.add(label_21);

        JCheckBox checkBox_94 = new JCheckBox("CASE1");
        checkBox_94.setName("cbx_18_2_1");
        checkBox_94.setBounds(304, 618, 71, 21);
        p.add(checkBox_94);
        addActionCheckbox(checkBox_94, "tF_18_02_01", "kdw004.Scenario2Case1");

        JTextField textField_120 = new JTextField("");
        textField_120.setName("tF_18_02_01");
        textField_120.setEditable(false);
        textField_120.setBounds(384, 619, 48, 19);
        p.add(textField_120);

        JLabel label_22 = new JLabel("SCENARIO3");
        label_22.setName("lb_18_3");
        label_22.setBounds(158, 645, 78, 13);
        p.add(label_22);

        JCheckBox checkBox_95 = new JCheckBox("CASE1");
        checkBox_95.setName("cbx_18_3_1");
        checkBox_95.setBounds(304, 641, 71, 21);
        p.add(checkBox_95);
        addActionCheckbox(checkBox_95, "tF_18_03_01", "kdw004.Scenario3Case1");

        JTextField textField_121 = new JTextField("");
        textField_121.setName("tF_18_03_01");
        textField_121.setEditable(false);
        textField_121.setBounds(384, 642, 48, 19);
        p.add(textField_121);

        JLabel label_23 = new JLabel("SCENARIO4");
        label_23.setName("lb_18_4");
        label_23.setBounds(158, 668, 78, 13);
        p.add(label_23);

        JCheckBox checkBox_96 = new JCheckBox("CASE1");
        checkBox_96.setName("cbx_18_4_1");
        checkBox_96.setBounds(304, 664, 71, 21);
        p.add(checkBox_96);
        addActionCheckbox(checkBox_96, "tF_18_04_01", "kdw004.Scenario4Case1");

        JTextField textField_122 = new JTextField("");
        textField_122.setName("tF_18_04_01");
        textField_122.setEditable(false);
        textField_122.setBounds(384, 665, 48, 19);
        p.add(textField_122);

        JCheckBox chckbxCase_36 = new JCheckBox("CASE2");
        chckbxCase_36.setName("cbx_18_1_2");
        chckbxCase_36.setBounds(439, 595, 71, 21);
        p.add(chckbxCase_36);
        addActionCheckbox(chckbxCase_36, "tF_18_01_02", "kdw004.Scenario1Case2");

        JTextField textField_123 = new JTextField("");
        textField_123.setName("tF_18_01_02");
        textField_123.setEditable(false);
        textField_123.setBounds(519, 596, 48, 19);
        p.add(textField_123);

        JCheckBox chckbxCase_37 = new JCheckBox("CASE3");
        chckbxCase_37.setName("cbx_18_1_3");
        chckbxCase_37.setBounds(576, 595, 71, 21);
        p.add(chckbxCase_37);
        addActionCheckbox(chckbxCase_37, "tF_18_01_03", "kdw004.Scenario1Case3");

        JTextField textField_124 = new JTextField("");
        textField_124.setName("tF_18_01_03");
        textField_124.setEditable(false);
        textField_124.setBounds(656, 596, 48, 19);
        p.add(textField_124);

        JCheckBox checkBox_97 = new JCheckBox("CASE2");
        checkBox_97.setName("cbx_18_2_2");
        checkBox_97.setBounds(439, 618, 71, 21);
        p.add(checkBox_97);
        addActionCheckbox(checkBox_97, "tF_18_02_02", "kdw004.Scenario2Case2");

        JTextField textField_125 = new JTextField("");
        textField_125.setName("tF_18_02_02");
        textField_125.setEditable(false);
        textField_125.setBounds(519, 619, 48, 19);
        p.add(textField_125);

        JCheckBox checkBox_98 = new JCheckBox("CASE3");
        checkBox_98.setName("cbx_18_2_3");
        checkBox_98.setBounds(576, 618, 71, 21);
        p.add(checkBox_98);
        addActionCheckbox(checkBox_98, "tF_18_02_03", "kdw004.Scenario2Case3");

        JTextField textField_126 = new JTextField("");
        textField_126.setName("tF_18_02_03");
        textField_126.setEditable(false);
        textField_126.setBounds(656, 619, 48, 19);
        p.add(textField_126);

        JCheckBox checkBox_99 = new JCheckBox("CASE2");
        checkBox_99.setName("cbx_18_3_2");
        checkBox_99.setBounds(439, 641, 71, 21);
        p.add(checkBox_99);
        addActionCheckbox(checkBox_99, "tF_18_03_02", "kdw004.Scenario3Case2");

        JTextField textField_127 = new JTextField("");
        textField_127.setName("tF_18_03_02");
        textField_127.setEditable(false);
        textField_127.setBounds(519, 642, 48, 19);
        p.add(textField_127);

        JCheckBox checkBox_100 = new JCheckBox("CASE3");
        checkBox_100.setName("cbx_18_3_3");
        checkBox_100.setBounds(576, 641, 71, 21);
        p.add(checkBox_100);
        addActionCheckbox(checkBox_100, "tF_18_03_03", "kdw004.Scenario3Case3");

        JTextField textField_128 = new JTextField("");
        textField_128.setName("tF_18_03_03");
        textField_128.setEditable(false);
        textField_128.setBounds(656, 642, 48, 19);
        p.add(textField_128);

        JCheckBox checkBox_101 = new JCheckBox("CASE2");
        checkBox_101.setName("cbx_18_4_2");
        checkBox_101.setBounds(440, 664, 71, 21);
        p.add(checkBox_101);
        addActionCheckbox(checkBox_101, "tF_18_04_02", "kdw004.Scenario4Case2");

        JTextField textField_129 = new JTextField("");
        textField_129.setName("tF_18_04_02");
        textField_129.setEditable(false);
        textField_129.setBounds(520, 665, 48, 19);
        p.add(textField_129);

        JCheckBox checkBox_102 = new JCheckBox("CASE3");
        checkBox_102.setName("cbx_18_4_3");
        checkBox_102.setBounds(576, 664, 71, 21);
        p.add(checkBox_102);
        addActionCheckbox(checkBox_102, "tF_18_04_03", "kdw004.Scenario4Case3");

        JTextField textField_130 = new JTextField("");
        textField_130.setName("tF_18_04_03");
        textField_130.setEditable(false);
        textField_130.setBounds(656, 665, 48, 19);
        p.add(textField_130);

        JCheckBox chckbxCase_38 = new JCheckBox("CASE4");
        chckbxCase_38.setName("cbx_18_3_4");
        chckbxCase_38.setBounds(712, 641, 71, 21);
        p.add(chckbxCase_38);
        addActionCheckbox(chckbxCase_38, "tF_18_03_04", "kdw004.Scenario3Case4");

        JTextField textField_131 = new JTextField("");
        textField_131.setName("tF_18_03_04");
        textField_131.setEditable(false);
        textField_131.setBounds(792, 642, 48, 19);
        p.add(textField_131);

        JCheckBox chckbxCase_39 = new JCheckBox("CASE4");
        chckbxCase_39.setName("cbx_18_4_4");
        chckbxCase_39.setBounds(712, 664, 71, 21);
        p.add(chckbxCase_39);
        addActionCheckbox(chckbxCase_39, "tF_18_04_04", "kdw004.Scenario4Case4");

        JTextField textField_132 = new JTextField("");
        textField_132.setName("tF_18_04_04");
        textField_132.setEditable(false);
        textField_132.setBounds(792, 665, 48, 19);
        p.add(textField_132);

        JCheckBox checkBox_105 = new JCheckBox("全件選択");
        checkBox_105.setName("cbx_18_all");
        checkBox_105.setBounds(852, 595, 88, 21);
        p.add(checkBox_105);
        addActionCheckboxAll(checkBox_105);

        //KDW005
        JLabel lblKdw_4 = new JLabel("KDW005");
        lblKdw_4.setName("lb_19");
        lblKdw_4.setBounds(12, 710, 96, 13);
        p.add(lblKdw_4);

        JLabel label_24 = new JLabel("SCENARIO1");
        label_24.setName("lb_19_1");
        label_24.setBounds(158, 710, 78, 13);
        p.add(label_24);

        JCheckBox checkBox_103 = new JCheckBox("CASE1");
        checkBox_103.setName("cbx_19_1_1");
        checkBox_103.setBounds(304, 706, 71, 21);
        p.add(checkBox_103);
        addActionCheckbox(checkBox_103, "tF_19_01_01", "kdw005.Scenario1Case1");

        JTextField textField_133 = new JTextField("");
        textField_133.setName("tF_19_01_01");
        textField_133.setEditable(false);
        textField_133.setBounds(384, 707, 48, 19);
        p.add(textField_133);

        JCheckBox checkBox_104 = new JCheckBox("CASE2");
        checkBox_104.setName("cbx_19_1_2");
        checkBox_104.setBounds(440, 706, 71, 21);
        p.add(checkBox_104);
        addActionCheckbox(checkBox_104, "tF_19_01_02", "kdw005.Scenario1Case2");

        JTextField textField_134 = new JTextField("");
        textField_134.setName("tF_19_01_02");
        textField_134.setEditable(false);
        textField_134.setBounds(519, 707, 48, 19);
        p.add(textField_134);

    }

}