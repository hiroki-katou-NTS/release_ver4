package common;

import javax.swing.*;
import java.awt.Dimension;

public class Tab4 extends CreateMainTest{

    public JScrollPane scrollPane;

    public Tab4() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setPreferredSize(new Dimension(1250, 1000));
        
        scrollPane = new JScrollPane(p);
        scrollPane.setBounds(0, 0, 1280, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // KDW003
        JLabel lblKdw = new JLabel("KDW003");
        lblKdw.setName("lb_17");
        lblKdw.setBounds(12, 10, 73, 13);
        p.add(lblKdw);
        
        JLabel label_14 = new JLabel("SCENARIO1");
        label_14.setName("lb_17_1");
        label_14.setBounds(158, 10, 78, 13);
        p.add(label_14);
        
        JCheckBox checkBox_23 = new JCheckBox("CASE1");
        checkBox_23.setName("cbx_17_1_1");
        checkBox_23.setBounds(304, 6, 71, 21);
        p.add(checkBox_23);
        addActionCheckbox(checkBox_23, "tF_17_1_1", "kdw003.Scenario1Case1");
        
        JTextField textField_35 = new JTextField("");
        textField_35.setName("tF_17_1_1");
        textField_35.setEditable(false);
        textField_35.setBounds(384, 7, 48, 19);
        p.add(textField_35);
        
        JCheckBox checkBox_24 = new JCheckBox("CASE2");
        checkBox_24.setName("cbx_17_1_2");
        checkBox_24.setBounds(440, 6, 71, 21);
        p.add(checkBox_24);
        addActionCheckbox(checkBox_24, "tF_17_1_2", "kdw003.Scenario1Case2");
        
        JTextField textField_36 = new JTextField("");
        textField_36.setName("tF_17_1_2");
        textField_36.setEditable(false);
        textField_36.setBounds(520, 7, 48, 19);
        p.add(textField_36);
        
        JCheckBox checkBox_25 = new JCheckBox("CASE3");
        checkBox_25.setName("cbx_17_1_3");
        checkBox_25.setBounds(576, 6, 71, 21);
        p.add(checkBox_25);
        addActionCheckbox(checkBox_25, "tF_17_1_3", "kdw003.Scenario1Case3");
        
        JTextField textField_37 = new JTextField("");
        textField_37.setName("tF_17_1_3");
        textField_37.setEditable(false);
        textField_37.setBounds(656, 7, 48, 19);
        p.add(textField_37);
        
        JCheckBox checkBox_26 = new JCheckBox("CASE4");
        checkBox_26.setName("cbx_17_1_4");
        checkBox_26.setBounds(712, 6, 71, 21);
        p.add(checkBox_26);
        addActionCheckbox(checkBox_26, "tF_17_1_4", "kdw003.Scenario1Case4");
        
        JTextField textField_38 = new JTextField("");
        textField_38.setName("tF_17_1_4");
        textField_38.setEditable(false);
        textField_38.setBounds(792, 7, 48, 19);
        p.add(textField_38);
        
        JCheckBox checkBox_27 = new JCheckBox("CASE5");
        checkBox_27.setName("cbx_17_1_5");
        checkBox_27.setBounds(852, 6, 71, 21);
        p.add(checkBox_27);
        addActionCheckbox(checkBox_27, "tF_17_1_5", "kdw003.Scenario1Case5");
        
        JTextField textField_39 = new JTextField("");
        textField_39.setName("tF_17_1_5");
        textField_39.setEditable(false);
        textField_39.setBounds(932, 7, 48, 19);
        p.add(textField_39);
        
        JCheckBox checkBox_28 = new JCheckBox("CASE6");
        checkBox_28.setName("cbx_17_1_6");
        checkBox_28.setBounds(992, 6, 71, 21);
        p.add(checkBox_28);
        addActionCheckbox(checkBox_28, "tF_17_1_6", "kdw003.Scenario1Case6");
        
        JTextField textField_40 = new JTextField("");
        textField_40.setName("tF_17_1_6");
        textField_40.setEditable(false);
        textField_40.setBounds(1072, 7, 48, 19);
        p.add(textField_40);
        
        JLabel lblScenario_1 = new JLabel("SCENARIO2");
        lblScenario_1.setName("lb_17_2");
        lblScenario_1.setBounds(158, 33, 78, 13);
        p.add(lblScenario_1);
        
        JCheckBox checkBox_29 = new JCheckBox("CASE1");
        checkBox_29.setName("cbx_17_2_1");
        checkBox_29.setBounds(304, 29, 71, 21);
        p.add(checkBox_29);
        addActionCheckbox(checkBox_29, "tF_17_2_1", "kdw003.Scenario2Case1");
        
        JTextField textField_41 = new JTextField("");
        textField_41.setName("tF_17_2_1");
        textField_41.setEditable(false);
        textField_41.setBounds(384, 30, 48, 19);
        p.add(textField_41);
        
        JCheckBox checkBox_30 = new JCheckBox("CASE2");
        checkBox_30.setName("cbx_17_2_2");
        checkBox_30.setBounds(440, 29, 71, 21);
        p.add(checkBox_30);
        addActionCheckbox(checkBox_30, "tF_17_2_2", "kdw003.Scenario2Case2");
        
        JTextField textField_42 = new JTextField("");
        textField_42.setName("tF_17_2_2");
        textField_42.setEditable(false);
        textField_42.setBounds(520, 30, 48, 19);
        p.add(textField_42);
        
        JCheckBox checkBox_31 = new JCheckBox("CASE3");
        checkBox_31.setName("cbx_17_2_3");
        checkBox_31.setBounds(576, 29, 71, 21);
        p.add(checkBox_31);
        addActionCheckbox(checkBox_31, "tF_17_2_3", "kdw003.Scenario2Case3");
        
        JTextField textField_43 = new JTextField("");
        textField_43.setName("tF_17_2_3");
        textField_43.setEditable(false);
        textField_43.setBounds(656, 30, 48, 19);
        p.add(textField_43);
        
        JCheckBox checkBox_32 = new JCheckBox("CASE4");
        checkBox_32.setName("cbx_17_2_4");
        checkBox_32.setBounds(712, 29, 71, 21);
        p.add(checkBox_32);
        addActionCheckbox(checkBox_32, "tF_17_2_4", "kdw003.Scenario2Case4");
        
        JTextField textField_44 = new JTextField("");
        textField_44.setName("tF_17_2_4");
        textField_44.setEditable(false);
        textField_44.setBounds(792, 30, 48, 19);
        p.add(textField_44);
        
        JCheckBox checkBox_33 = new JCheckBox("CASE5");
        checkBox_33.setName("cbx_17_2_5");
        checkBox_33.setBounds(852, 29, 71, 21);
        p.add(checkBox_33);
        addActionCheckbox(checkBox_33, "tF_17_2_5", "kdw003.Scenario2Case5");
        
        JTextField textField_45 = new JTextField("");
        textField_45.setName("tF_17_2_5");
        textField_45.setEditable(false);
        textField_45.setBounds(932, 30, 48, 19);
        p.add(textField_45);
        
        JCheckBox checkBox_34 = new JCheckBox("CASE6");
        checkBox_34.setName("cbx_17_2_6");
        checkBox_34.setBounds(992, 29, 71, 21);
        p.add(checkBox_34);
        addActionCheckbox(checkBox_34, "tF_17_2_6", "kdw003.Scenario2Case6");
        
        JTextField textField_46 = new JTextField("");
        textField_46.setName("tF_17_2_6");
        textField_46.setEditable(false);
        textField_46.setBounds(1072, 30, 48, 19);
        p.add(textField_46);
        
        JLabel lblScenario_2 = new JLabel("SCENARIO3");
        lblScenario_2.setName("lb_17_3");
        lblScenario_2.setBounds(158, 56, 78, 13);
        p.add(lblScenario_2);
        
        JCheckBox checkBox_35 = new JCheckBox("CASE1");
        checkBox_35.setName("cbx_17_3_1");
        checkBox_35.setBounds(304, 52, 71, 21);
        p.add(checkBox_35);
        addActionCheckbox(checkBox_35, "tF_17_3_1", "kdw003.Scenario3Case1");
        
        JTextField textField_47 = new JTextField("");
        textField_47.setName("tF_17_3_1");
        textField_47.setEditable(false);
        textField_47.setBounds(384, 53, 48, 19);
        p.add(textField_47);
        
        JCheckBox checkBox_36 = new JCheckBox("CASE2");
        checkBox_36.setName("cbx_17_3_2");
        checkBox_36.setBounds(440, 52, 71, 21);
        p.add(checkBox_36);
        addActionCheckbox(checkBox_36, "tF_17_3_2", "kdw003.Scenario3Case2");
        
        JTextField textField_48 = new JTextField("");
        textField_48.setName("tF_17_3_2");
        textField_48.setEditable(false);
        textField_48.setBounds(520, 53, 48, 19);
        p.add(textField_48);
        
        JCheckBox chckbxCase_6 = new JCheckBox("CASE4");
        chckbxCase_6.setName("cbx_17_3_4");
        chckbxCase_6.setBounds(576, 52, 71, 21);
        p.add(chckbxCase_6);
        addActionCheckbox(chckbxCase_6, "tF_17_3_4", "kdw003.Scenario3Case4");
        
        JTextField textField_49 = new JTextField("");
        textField_49.setName("tF_17_3_4");
        textField_49.setEditable(false);
        textField_49.setBounds(656, 53, 48, 19);
        p.add(textField_49);
        
        JCheckBox chckbxCase_7 = new JCheckBox("CASE5");
        chckbxCase_7.setName("cbx_17_3_5");
        chckbxCase_7.setBounds(712, 52, 71, 21);
        p.add(chckbxCase_7);
        addActionCheckbox(chckbxCase_7, "tF_17_3_5", "kdw003.Scenario3Case5");
        
        JTextField textField_50 = new JTextField("");
        textField_50.setName("tF_17_3_5");
        textField_50.setEditable(false);
        textField_50.setBounds(792, 53, 48, 19);
        p.add(textField_50);
        
        JCheckBox chckbxCase_8 = new JCheckBox("CASE6");
        chckbxCase_8.setName("cbx_17_3_6");
        chckbxCase_8.setBounds(852, 52, 71, 21);
        p.add(chckbxCase_8);
        addActionCheckbox(chckbxCase_8, "tF_17_3_6", "kdw003.Scenario3Case6");
        
        JTextField textField_51 = new JTextField("");
        textField_51.setName("tF_17_3_6");
        textField_51.setEditable(false);
        textField_51.setBounds(932, 53, 48, 19);
        p.add(textField_51);
        
        JCheckBox chckbxCase_9 = new JCheckBox("CASE7");
        chckbxCase_9.setName("cbx_17_3_7");
        chckbxCase_9.setBounds(992, 52, 71, 21);
        p.add(chckbxCase_9);
        addActionCheckbox(chckbxCase_9, "tF_17_3_7", "kdw003.Scenario3Case7");
        
        JTextField textField_52 = new JTextField("");
        textField_52.setName("tF_17_3_7");
        textField_52.setEditable(false);
        textField_52.setBounds(1072, 53, 48, 19);
        p.add(textField_52);
        
        JCheckBox chckbxCase_10 = new JCheckBox("CASE8");
        chckbxCase_10.setName("cbx_17_3_8");
        chckbxCase_10.setBounds(304, 75, 71, 21);
        p.add(chckbxCase_10);
        addActionCheckbox(chckbxCase_10, "tF_17_3_8", "kdw003.Scenario3Case8");
        
        JTextField textField_53 = new JTextField("");
        textField_53.setName("tF_17_3_8");
        textField_53.setEditable(false);
        textField_53.setBounds(384, 76, 48, 19);
        p.add(textField_53);
        
        JCheckBox chckbxCase_11 = new JCheckBox("CASE9");
        chckbxCase_11.setName("cbx_17_3_9");
        chckbxCase_11.setBounds(440, 75, 71, 21);
        p.add(chckbxCase_11);
        addActionCheckbox(chckbxCase_11, "tF_17_3_9", "kdw003.Scenario3Case9");
        
        JTextField textField_54 = new JTextField("");
        textField_54.setName("tF_17_3_9");
        textField_54.setEditable(false);
        textField_54.setBounds(520, 76, 48, 19);
        p.add(textField_54);
        
        JCheckBox chckbxCase_12 = new JCheckBox("CASE12");
        chckbxCase_12.setName("cbx_17_3_12");
        chckbxCase_12.setBounds(576, 75, 71, 21);
        p.add(chckbxCase_12);
        addActionCheckbox(chckbxCase_12, "tF_17_3_12", "kdw003.Scenario3Case12");
        
        JTextField textField_55 = new JTextField("");
        textField_55.setName("tF_17_3_12");
        textField_55.setEditable(false);
        textField_55.setBounds(656, 76, 48, 19);
        p.add(textField_55);
        
        JLabel lblScenario_3 = new JLabel("SCENARIO8");
        lblScenario_3.setName("lb_17_8");
        lblScenario_3.setBounds(158, 194, 78, 13);
        p.add(lblScenario_3);
        
        JCheckBox checkBox_37 = new JCheckBox("CASE1");
        checkBox_37.setName("cbx_17_8_1");
        checkBox_37.setBounds(304, 190, 71, 21);
        p.add(checkBox_37);
        addActionCheckbox(checkBox_37, "tF_17_8_1", "kdw003.Scenario8Case1");
        
        JTextField textField_56 = new JTextField("");
        textField_56.setName("tF_17_8_1");
        textField_56.setEditable(false);
        textField_56.setBounds(384, 191, 48, 19);
        p.add(textField_56);
        
        JCheckBox checkBox_38 = new JCheckBox("CASE2");
        checkBox_38.setName("cbx_17_8_2");
        checkBox_38.setBounds(440, 190, 71, 21);
        p.add(checkBox_38);
        addActionCheckbox(checkBox_38, "tF_17_8_2", "kdw003.Scenario8Case2");
        
        JTextField textField_57 = new JTextField("");
        textField_57.setName("tF_17_8_2");
        textField_57.setEditable(false);
        textField_57.setBounds(520, 191, 48, 19);
        p.add(textField_57);
        
        JCheckBox checkBox_39 = new JCheckBox("CASE3");
        checkBox_39.setName("cbx_17_8_3");
        checkBox_39.setBounds(576, 190, 71, 21);
        p.add(checkBox_39);
        addActionCheckbox(checkBox_39, "tF_17_8_3", "kdw003.Scenario8Case3");
        
        JTextField textField_58 = new JTextField("");
        textField_58.setName("tF_17_8_3");
        textField_58.setEditable(false);
        textField_58.setBounds(656, 191, 48, 19);
        p.add(textField_58);
        
        JLabel lblScenario_4 = new JLabel("SCENARIO9");
        lblScenario_4.setName("lb_17_9");
        lblScenario_4.setBounds(158, 217, 78, 13);
        p.add(lblScenario_4);
        
        JCheckBox checkBox_40 = new JCheckBox("CASE1");
        checkBox_40.setName("cbx_17_9_1");
        checkBox_40.setBounds(304, 213, 71, 21);
        p.add(checkBox_40);
        addActionCheckbox(checkBox_40, "tF_17_9_1", "kdw003.Scenario9Case1");
        
        JTextField textField_59 = new JTextField("");
        textField_59.setName("tF_17_9_1");
        textField_59.setEditable(false);
        textField_59.setBounds(384, 214, 48, 19);
        p.add(textField_59);
        
        JCheckBox checkBox_41 = new JCheckBox("CASE2");
        checkBox_41.setName("cbx_17_9_2");
        checkBox_41.setBounds(440, 213, 71, 21);
        p.add(checkBox_41);
        addActionCheckbox(checkBox_41, "tF_17_9_2", "kdw003.Scenario9Case2");
        
        JTextField textField_60 = new JTextField("");
        textField_60.setName("tF_17_9_2");
        textField_60.setEditable(false);
        textField_60.setBounds(520, 214, 48, 19);
        p.add(textField_60);
        
        JLabel lblScenario_5 = new JLabel("SCENARIO10");
        lblScenario_5.setName("lb_17_10");
        lblScenario_5.setBounds(158, 240, 78, 13);
        p.add(lblScenario_5);
        
        JCheckBox checkBox_42 = new JCheckBox("CASE1");
        checkBox_42.setName("cbx_17_10_1");
        checkBox_42.setBounds(304, 236, 71, 21);
        p.add(checkBox_42);
        addActionCheckbox(checkBox_42, "tF_17_10_1", "kdw003.Scenario10Case1");
        
        JTextField textField_61 = new JTextField("");
        textField_61.setName("tF_17_10_1");
        textField_61.setEditable(false);
        textField_61.setBounds(384, 237, 48, 19);
        p.add(textField_61);
        
        JCheckBox checkBox_43 = new JCheckBox("CASE2");
        checkBox_43.setName("cbx_17_10_2");
        checkBox_43.setBounds(440, 236, 71, 21);
        p.add(checkBox_43);
        addActionCheckbox(checkBox_43, "tF_17_10_2", "kdw003.Scenario10Case2");
        
        JTextField textField_62 = new JTextField("");
        textField_62.setName("tF_17_10_2");
        textField_62.setEditable(false);
        textField_62.setBounds(520, 237, 48, 19);
        p.add(textField_62);
        
        JCheckBox checkBox_44 = new JCheckBox("CASE3");
        checkBox_44.setName("cbx_17_10_3");
        checkBox_44.setBounds(576, 236, 71, 21);
        p.add(checkBox_44);
        addActionCheckbox(checkBox_44, "tF_17_10_3", "kdw003.Scenario10Case3");
        
        JTextField textField_63 = new JTextField("");
        textField_63.setName("tF_17_10_3");
        textField_63.setEditable(false);
        textField_63.setBounds(656, 237, 48, 19);
        p.add(textField_63);
        
        JCheckBox checkBox_45 = new JCheckBox("CASE4");
        checkBox_45.setName("cbx_17_10_4");
        checkBox_45.setBounds(712, 236, 71, 21);
        p.add(checkBox_45);
        addActionCheckbox(checkBox_45, "tF_17_10_4", "kdw003.Scenario10Case4");
        
        JTextField textField_64 = new JTextField("");
        textField_64.setName("tF_17_10_4");
        textField_64.setEditable(false);
        textField_64.setBounds(792, 237, 48, 19);
        p.add(textField_64);
        
        JLabel lblScenario_6 = new JLabel("SCENARIO12");
        lblScenario_6.setName("lb_17_12");
        lblScenario_6.setBounds(158, 355, 78, 13);
        p.add(lblScenario_6);
        
        JCheckBox checkBox_46 = new JCheckBox("CASE1");
        checkBox_46.setName("cbx_17_12_1");
        checkBox_46.setBounds(304, 351, 71, 21);
        p.add(checkBox_46);
        addActionCheckbox(checkBox_46, "tF_17_12_1", "kdw003.Scenario12Case1");
        
        JTextField textField_65 = new JTextField("");
        textField_65.setName("tF_17_12_1");
        textField_65.setEditable(false);
        textField_65.setBounds(384, 352, 48, 19);
        p.add(textField_65);
        
        JCheckBox checkBox_47 = new JCheckBox("CASE2");
        checkBox_47.setName("cbx_17_12_2");
        checkBox_47.setBounds(440, 351, 71, 21);
        p.add(checkBox_47);
        addActionCheckbox(checkBox_47, "tF_17_12_2", "kdw003.Scenario12Case2");
        
        JTextField textField_66 = new JTextField("");
        textField_66.setName("tF_17_12_2");
        textField_66.setEditable(false);
        textField_66.setBounds(520, 352, 48, 19);
        p.add(textField_66);
        
        JCheckBox checkBox_48 = new JCheckBox("CASE4");
        checkBox_48.setName("cbx_17_12_4");
        checkBox_48.setBounds(712, 351, 71, 21);
        p.add(checkBox_48);
        addActionCheckbox(checkBox_48, "tF_17_12_4", "kdw003.Scenario12Case4");
        
        JTextField textField_67 = new JTextField("");
        textField_67.setName("tF_17_12_4");
        textField_67.setEditable(false);
        textField_67.setBounds(792, 352, 48, 19);
        p.add(textField_67);
        
        JCheckBox checkBox_49 = new JCheckBox("CASE5");
        checkBox_49.setName("cbx_17_12_5");
        checkBox_49.setBounds(852, 351, 71, 21);
        p.add(checkBox_49);
        addActionCheckbox(checkBox_49, "tF_17_12_5", "kdw003.Scenario12Case5");
        
        JTextField textField_68 = new JTextField("");
        textField_68.setName("tF_17_12_5");
        textField_68.setEditable(false);
        textField_68.setBounds(932, 352, 48, 19);
        p.add(textField_68);
        
        JCheckBox checkBox_50 = new JCheckBox("CASE3");
        checkBox_50.setName("cbx_17_12_3");
        checkBox_50.setBounds(576, 351, 71, 21);
        p.add(checkBox_50);
        addActionCheckbox(checkBox_50, "tF_17_12_3", "kdw003.Scenario12Case3");
        
        JTextField textField_69 = new JTextField("");
        textField_69.setName("tF_17_12_3");
        textField_69.setEditable(false);
        textField_69.setBounds(656, 352, 48, 19);
        p.add(textField_69);
        
        JCheckBox checkBox_51 = new JCheckBox("CASE1");
        checkBox_51.setName("cbx_17_13_1");
        checkBox_51.setBounds(304, 374, 71, 21);
        p.add(checkBox_51);
        addActionCheckbox(checkBox_51, "tF_17_13_1", "kdw003.Scenario13Case1");
        
        JTextField textField_70 = new JTextField("");
        textField_70.setName("tF_17_13_1");
        textField_70.setEditable(false);
        textField_70.setBounds(384, 375, 48, 19);
        p.add(textField_70);
        
        JCheckBox checkBox_52 = new JCheckBox("CASE2");
        checkBox_52.setName("cbx_17_13_2");
        checkBox_52.setBounds(440, 374, 71, 21);
        p.add(checkBox_52);
        addActionCheckbox(checkBox_52, "tF_17_13_2", "kdw003.Scenario13Case2");
        
        JTextField textField_71 = new JTextField("");
        textField_71.setName("tF_17_13_2");
        textField_71.setEditable(false);
        textField_71.setBounds(520, 375, 48, 19);
        p.add(textField_71);
        
        JCheckBox checkBox_53 = new JCheckBox("CASE4");
        checkBox_53.setName("cbx_17_13_4");
        checkBox_53.setBounds(712, 374, 71, 21);
        p.add(checkBox_53);
        addActionCheckbox(checkBox_53, "tF_17_13_4", "kdw003.Scenario13Case4");
        
        JTextField textField_72 = new JTextField("");
        textField_72.setName("tF_17_13_4");
        textField_72.setEditable(false);
        textField_72.setBounds(792, 375, 48, 19);
        p.add(textField_72);
        
        JCheckBox checkBox_54 = new JCheckBox("CASE3");
        checkBox_54.setName("cbx_17_13_3");
        checkBox_54.setBounds(576, 374, 71, 21);
        p.add(checkBox_54);
        addActionCheckbox(checkBox_54, "tF_17_13_3", "kdw003.Scenario13Case3");
        
        JTextField textField_73 = new JTextField("");
        textField_73.setName("tF_17_13_3");
        textField_73.setEditable(false);
        textField_73.setBounds(656, 375, 48, 19);
        p.add(textField_73);
        
        JLabel lblScenario_7 = new JLabel("SCENARIO13");
        lblScenario_7.setName("lb_17_13");
        lblScenario_7.setBounds(158, 378, 78, 13);
        p.add(lblScenario_7);
        
        JLabel lblScenario_8 = new JLabel("SCENARIO14");
        lblScenario_8.setName("lb_17_14");
        lblScenario_8.setBounds(158, 401, 78, 13);
        p.add(lblScenario_8);
        
        JCheckBox checkBox_55 = new JCheckBox("CASE1");
        checkBox_55.setName("cbx_17_14_1");
        checkBox_55.setBounds(304, 397, 71, 21);
        p.add(checkBox_55);
        addActionCheckbox(checkBox_55, "tF_17_14_1", "kdw003.Scenario14Case1");
        
        JTextField textField_74 = new JTextField("");
        textField_74.setName("tF_17_14_1");
        textField_74.setEditable(false);
        textField_74.setBounds(384, 398, 48, 19);
        p.add(textField_74);
        
        JCheckBox checkBox_56 = new JCheckBox("CASE2");
        checkBox_56.setName("cbx_17_14_2");
        checkBox_56.setBounds(440, 397, 71, 21);
        p.add(checkBox_56);
        addActionCheckbox(checkBox_56, "tF_17_14_2", "kdw003.Scenario14Case2");
        
        JTextField textField_75 = new JTextField("");
        textField_75.setName("tF_17_14_2");
        textField_75.setEditable(false);
        textField_75.setBounds(520, 398, 48, 19);
        p.add(textField_75);
        
        JCheckBox chckbxCase_13 = new JCheckBox("CASE3");
        chckbxCase_13.setName("cbx_17_14_3");
        chckbxCase_13.setBounds(576, 397, 71, 21);
        p.add(chckbxCase_13);
        addActionCheckbox(chckbxCase_13, "tF_17_14_3", "kdw003.Scenario14Case3");
        
        JTextField textField_76 = new JTextField("");
        textField_76.setName("tF_17_14_3");
        textField_76.setEditable(false);
        textField_76.setBounds(656, 398, 48, 19);
        p.add(textField_76);
        
        JCheckBox chckbxCase_14 = new JCheckBox("CASE4");
        chckbxCase_14.setName("cbx_17_14_4");
        chckbxCase_14.setBounds(712, 397, 71, 21);
        p.add(chckbxCase_14);
        addActionCheckbox(chckbxCase_14, "tF_17_14_4", "kdw003.Scenario14Case4");
        
        JTextField textField_77 = new JTextField("");
        textField_77.setName("tF_17_14_4");
        textField_77.setEditable(false);
        textField_77.setBounds(792, 398, 48, 19);
        p.add(textField_77);
        
        JCheckBox chckbxCase_15 = new JCheckBox("CASE5");
        chckbxCase_15.setName("cbx_17_14_5");
        chckbxCase_15.setBounds(852, 397, 71, 21);
        p.add(chckbxCase_15);
        addActionCheckbox(chckbxCase_15, "tF_17_14_5", "kdw003.Scenario14Case5");
        
        JTextField textField_78 = new JTextField("");
        textField_78.setName("tF_17_14_5");
        textField_78.setEditable(false);
        textField_78.setBounds(932, 398, 48, 19);
        p.add(textField_78);
        
        JCheckBox chckbxCase_16 = new JCheckBox("CASE6");
        chckbxCase_16.setName("cbx_17_14_6");
        chckbxCase_16.setBounds(992, 397, 71, 21);
        p.add(chckbxCase_16);
        addActionCheckbox(chckbxCase_16, "tF_17_14_6", "kdw003.Scenario14Case6");
        
        JTextField textField_79 = new JTextField("");
        textField_79.setName("tF_17_14_6");
        textField_79.setEditable(false);
        textField_79.setBounds(1072, 398, 48, 19);
        p.add(textField_79);
        
        JCheckBox chckbxCase_17 = new JCheckBox("CASE7");
        chckbxCase_17.setName("cbx_17_14_7");
        chckbxCase_17.setBounds(304, 420, 71, 21);
        p.add(chckbxCase_17);
        addActionCheckbox(chckbxCase_17, "tF_17_14_7", "kdw003.Scenario14Case7");
        
        JTextField textField_80 = new JTextField("");
        textField_80.setName("tF_17_14_7");
        textField_80.setEditable(false);
        textField_80.setBounds(384, 421, 48, 19);
        p.add(textField_80);
        
        JCheckBox chckbxCase_18 = new JCheckBox("CASE8");
        chckbxCase_18.setName("cbx_17_14_8");
        chckbxCase_18.setBounds(440, 420, 71, 21);
        p.add(chckbxCase_18);
        addActionCheckbox(chckbxCase_18, "tF_17_14_8", "kdw003.Scenario14Case8");
        
        JTextField textField_81 = new JTextField("");
        textField_81.setName("tF_17_14_8");
        textField_81.setEditable(false);
        textField_81.setBounds(520, 421, 48, 19);
        p.add(textField_81);
        
        JCheckBox chckbxCase_19 = new JCheckBox("CASE9");
        chckbxCase_19.setName("cbx_17_14_9");
        chckbxCase_19.setBounds(576, 420, 71, 21);
        p.add(chckbxCase_19);
        addActionCheckbox(chckbxCase_19, "tF_17_14_9", "kdw003.Scenario14Case9");
        
        JTextField textField_82 = new JTextField("");
        textField_82.setName("tF_17_14_9");
        textField_82.setEditable(false);
        textField_82.setBounds(656, 421, 48, 19);
        p.add(textField_82);
        
        JCheckBox chckbxCase_20 = new JCheckBox("CASE10");
        chckbxCase_20.setName("cbx_17_14_10");
        chckbxCase_20.setBounds(712, 420, 71, 21);
        p.add(chckbxCase_20);
        addActionCheckbox(chckbxCase_20, "tF_17_14_10", "kdw003.Scenario14Case10");
        
        JTextField textField_83 = new JTextField("");
        textField_83.setName("tF_17_14_10");
        textField_83.setEditable(false);
        textField_83.setBounds(792, 421, 48, 19);
        p.add(textField_83);
        
        JCheckBox chckbxCase_21 = new JCheckBox("CASE11");
        chckbxCase_21.setName("cbx_17_14_11");
        chckbxCase_21.setBounds(852, 420, 71, 21);
        p.add(chckbxCase_21);
        addActionCheckbox(chckbxCase_21, "tF_17_14_11", "kdw003.Scenario14Case11");
        
        JTextField textField_84 = new JTextField("");
        textField_84.setName("tF_17_14_11");
        textField_84.setEditable(false);
        textField_84.setBounds(932, 421, 48, 19);
        p.add(textField_84);
        
        JCheckBox chckbxCase_22 = new JCheckBox("CASE12");
        chckbxCase_22.setName("cbx_17_14_12");
        chckbxCase_22.setBounds(992, 420, 71, 21);
        p.add(chckbxCase_22);
        addActionCheckbox(chckbxCase_22, "tF_17_14_12", "kdw003.Scenario14Case12");
        
        JTextField textField_85 = new JTextField("");
        textField_85.setName("tF_17_14_12");
        textField_85.setEditable(false);
        textField_85.setBounds(1072, 421, 48, 19);
        p.add(textField_85);
        
        JCheckBox chckbxCase_23 = new JCheckBox("CASE13");
        chckbxCase_23.setName("cbx_17_14_13");
        chckbxCase_23.setBounds(304, 443, 71, 21);
        p.add(chckbxCase_23);
        addActionCheckbox(chckbxCase_23, "tF_17_14_13", "kdw003.Scenario14Case13");
        
        JTextField textField_86 = new JTextField("");
        textField_86.setName("tF_17_14_13");
        textField_86.setEditable(false);
        textField_86.setBounds(384, 444, 48, 19);
        p.add(textField_86);
        
        JCheckBox chckbxCase_24 = new JCheckBox("CASE14");
        chckbxCase_24.setName("cbx_17_14_14");
        chckbxCase_24.setBounds(440, 443, 71, 21);
        p.add(chckbxCase_24);
        addActionCheckbox(chckbxCase_24, "tF_17_14_14", "kdw003.Scenario14Case14");
        
        JTextField textField_87 = new JTextField("");
        textField_87.setName("tF_17_14_14");
        textField_87.setEditable(false);
        textField_87.setBounds(520, 444, 48, 19);
        p.add(textField_87);
        
        JCheckBox chckbxCase_25 = new JCheckBox("CASE15");
        chckbxCase_25.setName("cbx_17_14_15");
        chckbxCase_25.setBounds(576, 443, 71, 21);
        p.add(chckbxCase_25);
        addActionCheckbox(chckbxCase_25, "tF_17_14_15", "kdw003.Scenario14Case15");
        
        JTextField textField_88 = new JTextField("");
        textField_88.setName("tF_17_14_15");
        textField_88.setEditable(false);
        textField_88.setBounds(656, 444, 48, 19);
        p.add(textField_88);
        
        JCheckBox chckbxCase_26 = new JCheckBox("CASE16");
        chckbxCase_26.setName("cbx_17_14_16");
        chckbxCase_26.setBounds(712, 443, 71, 21);
        p.add(chckbxCase_26);
        addActionCheckbox(chckbxCase_26, "tF_17_14_16", "kdw003.Scenario14Case16");
        
        JTextField textField_89 = new JTextField("");
        textField_89.setName("tF_17_14_16");
        textField_89.setEditable(false);
        textField_89.setBounds(792, 444, 48, 19);
        p.add(textField_89);
        
        JCheckBox chckbxCase_27 = new JCheckBox("CASE17");
        chckbxCase_27.setName("cbx_17_14_17");
        chckbxCase_27.setBounds(852, 443, 71, 21);
        p.add(chckbxCase_27);
        addActionCheckbox(chckbxCase_27, "tF_17_14_17", "kdw003.Scenario14Case17");
        
        JTextField textField_90 = new JTextField("");
        textField_90.setName("tF_17_14_17");
        textField_90.setEditable(false);
        textField_90.setBounds(932, 444, 48, 19);
        p.add(textField_90);
        
        JCheckBox chckbxCase_28 = new JCheckBox("CASE18");
        chckbxCase_28.setName("cbx_17_14_18");
        chckbxCase_28.setBounds(992, 443, 71, 21);
        p.add(chckbxCase_28);
        addActionCheckbox(chckbxCase_28, "tF_17_14_18", "kdw003.Scenario14Case18");
        
        JTextField textField_91 = new JTextField("");
        textField_91.setName("tF_17_14_18");
        textField_91.setEditable(false);
        textField_91.setBounds(1072, 444, 48, 19);
        p.add(textField_91);
        
        JLabel lblScenario_9 = new JLabel("SCENARIO16");
        lblScenario_9.setName("lb_17_16");
        lblScenario_9.setBounds(158, 493, 78, 13);
        p.add(lblScenario_9);
        
        JCheckBox checkBox_57 = new JCheckBox("CASE1");
        checkBox_57.setName("cbx_17_16_1");
        checkBox_57.setBounds(304, 489, 71, 21);
        p.add(checkBox_57);
        addActionCheckbox(checkBox_57, "tF_17_16_1", "kdw003.Scenario16Case1");
        
        JTextField textField_92 = new JTextField("");
        textField_92.setName("tF_17_16_1");
        textField_92.setEditable(false);
        textField_92.setBounds(384, 490, 48, 19);
        p.add(textField_92);
        
        JCheckBox checkBox_58 = new JCheckBox("CASE2");
        checkBox_58.setName("cbx_17_16_2");
        checkBox_58.setBounds(440, 489, 71, 21);
        p.add(checkBox_58);
        addActionCheckbox(checkBox_58, "tF_17_16_2", "kdw003.Scenario16Case2");
        
        JTextField textField_93 = new JTextField("");
        textField_93.setName("tF_17_16_2");
        textField_93.setEditable(false);
        textField_93.setBounds(520, 490, 48, 19);
        p.add(textField_93);
        
        JCheckBox checkBox_59 = new JCheckBox("CASE3");
        checkBox_59.setName("cbx_17_16_3");
        checkBox_59.setBounds(576, 489, 71, 21);
        p.add(checkBox_59);
        addActionCheckbox(checkBox_59, "tF_17_16_3", "kdw003.Scenario16Case3");
        
        JTextField textField_94 = new JTextField("");
        textField_94.setName("tF_17_16_3");
        textField_94.setEditable(false);
        textField_94.setBounds(656, 490, 48, 19);
        p.add(textField_94);
        
        JCheckBox checkBox_60 = new JCheckBox("CASE4");
        checkBox_60.setName("cbx_17_16_4");
        checkBox_60.setBounds(712, 489, 71, 21);
        p.add(checkBox_60);
        addActionCheckbox(checkBox_60, "tF_17_16_4", "kdw003.Scenario16Case4");
        
        JTextField textField_95 = new JTextField("");
        textField_95.setName("tF_17_16_4");
        textField_95.setEditable(false);
        textField_95.setBounds(792, 490, 48, 19);
        p.add(textField_95);
        
        JLabel lblScenario_10 = new JLabel("SCENARIO4");
        lblScenario_10.setName("lb_17_4");
        lblScenario_10.setBounds(158, 102, 78, 13);
        p.add(lblScenario_10);
        
        JCheckBox checkBox_78 = new JCheckBox("CASE1");
        checkBox_78.setName("cbx_17_4_1");
        checkBox_78.setBounds(304, 98, 71, 21);
        p.add(checkBox_78);
        addActionCheckbox(checkBox_78, "tF_17_4_1", "kdw003.Scenario4Case1");
        
        JTextField textField_100 = new JTextField("");
        textField_100.setName("tF_17_4_1");
        textField_100.setEditable(false);
        textField_100.setBounds(384, 99, 48, 19);
        p.add(textField_100);
        
        JCheckBox checkBox_79 = new JCheckBox("CASE2");
        checkBox_79.setName("cbx_17_4_2");
        checkBox_79.setBounds(440, 98, 71, 21);
        p.add(checkBox_79);
        addActionCheckbox(checkBox_79, "tF_17_4_2", "kdw003.Scenario4Case2");
        
        JTextField textField_101 = new JTextField("");
        textField_101.setName("tF_17_4_2");
        textField_101.setEditable(false);
        textField_101.setBounds(520, 99, 48, 19);
        p.add(textField_101);
        
        JCheckBox chckbxCase_29 = new JCheckBox("CASE3");
        chckbxCase_29.setName("cbx_17_4_3");
        chckbxCase_29.setBounds(576, 98, 71, 21);
        p.add(chckbxCase_29);
        addActionCheckbox(chckbxCase_29, "tF_17_4_3", "kdw003.Scenario4Case3");
        
        JTextField textField_102 = new JTextField("");
        textField_102.setName("tF_17_4_3");
        textField_102.setEditable(false);
        textField_102.setBounds(656, 99, 48, 19);
        p.add(textField_102);
        
        JCheckBox chckbxCase_30 = new JCheckBox("CASE4");
        chckbxCase_30.setName("cbx_17_4_4");
        chckbxCase_30.setBounds(712, 98, 71, 21);
        p.add(chckbxCase_30);
        addActionCheckbox(chckbxCase_30, "tF_17_4_4", "kdw003.Scenario4Case4");
        
        JTextField textField_103 = new JTextField("");
        textField_103.setName("tF_17_4_4");
        textField_103.setEditable(false);
        textField_103.setBounds(792, 99, 48, 19);
        p.add(textField_103);
        
        JCheckBox chckbxCase_31 = new JCheckBox("CASE5");
        chckbxCase_31.setName("cbx_17_4_5");
        chckbxCase_31.setBounds(852, 98, 71, 21);
        p.add(chckbxCase_31);
        addActionCheckbox(chckbxCase_31, "tF_17_4_5", "kdw003.Scenario4Case5");
        
        JTextField textField_104 = new JTextField("");
        textField_104.setName("tF_17_4_5");
        textField_104.setEditable(false);
        textField_104.setBounds(932, 99, 48, 19);
        p.add(textField_104);
        
        JCheckBox chckbxCase_32 = new JCheckBox("CASE6");
        chckbxCase_32.setName("cbx_17_4_6");
        chckbxCase_32.setBounds(992, 98, 71, 21);
        p.add(chckbxCase_32);
        addActionCheckbox(chckbxCase_32, "tF_17_4_6", "kdw003.Scenario4Case6");
        
        JTextField textField_105 = new JTextField("");
        textField_105.setName("tF_17_4_6");
        textField_105.setEditable(false);
        textField_105.setBounds(1072, 99, 48, 19);
        p.add(textField_105);
        
        JCheckBox chckbxCase_33 = new JCheckBox("CASE7");
        chckbxCase_33.setName("cbx_17_4_7");
        chckbxCase_33.setBounds(304, 121, 71, 21);
        p.add(chckbxCase_33);
        addActionCheckbox(chckbxCase_33, "tF_17_4_7", "kdw003.Scenario4Case7");
        
        JTextField textField_106 = new JTextField("");
        textField_106.setName("tF_17_4_7");
        textField_106.setEditable(false);
        textField_106.setBounds(384, 122, 48, 19);
        p.add(textField_106);
        
        JCheckBox chckbxCase_34 = new JCheckBox("CASE8");
        chckbxCase_34.setName("cbx_17_4_8");
        chckbxCase_34.setBounds(440, 121, 71, 21);
        p.add(chckbxCase_34);
        addActionCheckbox(chckbxCase_34, "tF_17_4_8", "kdw003.Scenario4Case8");
        
        JTextField textField_107 = new JTextField("");
        textField_107.setName("tF_17_4_8");
        textField_107.setEditable(false);
        textField_107.setBounds(520, 122, 48, 19);
        p.add(textField_107);
        
        JCheckBox chckbxCase_35 = new JCheckBox("CASE9");
        chckbxCase_35.setName("cbx_17_4_9");
        chckbxCase_35.setBounds(576, 121, 71, 21);
        p.add(chckbxCase_35);
        addActionCheckbox(chckbxCase_35, "tF_17_4_9", "kdw003.Scenario4Case9");
        
        JTextField textField_108 = new JTextField("");
        textField_108.setName("tF_17_4_9");
        textField_108.setEditable(false);
        textField_108.setBounds(656, 122, 48, 19);
        p.add(textField_108);
        
        JLabel lblScenario_11 = new JLabel("SCENARIO5");
        lblScenario_11.setName("lb_17_5");
        lblScenario_11.setBounds(158, 148, 78, 13);
        p.add(lblScenario_11);
        
        JCheckBox checkBox_80 = new JCheckBox("CASE1");
        checkBox_80.setName("cbx_17_5_1");
        checkBox_80.setBounds(304, 144, 71, 21);
        p.add(checkBox_80);
        addActionCheckbox(checkBox_80, "tF_17_5_1", "kdw003.Scenario5Case1");
        
        JTextField textField_109 = new JTextField("");
        textField_109.setName("tF_17_5_1");
        textField_109.setEditable(false);
        textField_109.setBounds(384, 145, 48, 19);
        p.add(textField_109);
        
        JLabel lblScenario_12 = new JLabel("SCENARIO7");
        lblScenario_12.setName("lb_17_7");
        lblScenario_12.setBounds(158, 171, 78, 13);
        p.add(lblScenario_12);
        
        JCheckBox checkBox_81 = new JCheckBox("CASE1");
        checkBox_81.setName("cbx_17_7_1");
        checkBox_81.setBounds(304, 167, 71, 21);
        p.add(checkBox_81);
        addActionCheckbox(checkBox_81, "tF_17_7_1", "kdw003.Scenario7Case1");
        
        JTextField textField_110 = new JTextField("");
        textField_110.setName("tF_17_7_1");
        textField_110.setEditable(false);
        textField_110.setBounds(384, 168, 48, 19);
        p.add(textField_110);
        
        JCheckBox checkBox_82 = new JCheckBox("CASE2");
        checkBox_82.setName("cbx_17_7_2");
        checkBox_82.setBounds(440, 167, 71, 21);
        p.add(checkBox_82);
        addActionCheckbox(checkBox_82, "tF_17_7_2", "kdw003.Scenario7Case2");
        
        JTextField textField_111 = new JTextField("");
        textField_111.setName("tF_17_7_2");
        textField_111.setEditable(false);
        textField_111.setBounds(520, 168, 48, 19);
        p.add(textField_111);
        
        JLabel lblScenario_13 = new JLabel("SCENARIO15");
        lblScenario_13.setName("lb_17_15");
        lblScenario_13.setBounds(158, 470, 78, 13);
        p.add(lblScenario_13);
        
        JCheckBox checkBox_83 = new JCheckBox("CASE1");
        checkBox_83.setName("cbx_17_15_1");
        checkBox_83.setBounds(304, 466, 71, 21);
        p.add(checkBox_83);
        addActionCheckbox(checkBox_83, "tF_17_15_1", "kdw003.Scenario15Case1");
        
        JTextField textField_112 = new JTextField("");
        textField_112.setName("tF_17_15_1");
        textField_112.setEditable(false);
        textField_112.setBounds(384, 467, 48, 19);
        p.add(textField_112);
        
        JCheckBox checkBox_84 = new JCheckBox("CASE2");
        checkBox_84.setName("cbx_17_15_2");
        checkBox_84.setBounds(440, 466, 71, 21);
        p.add(checkBox_84);
        addActionCheckbox(checkBox_84, "tF_17_15_2", "kdw003.Scenario15Case2");
        
        JTextField textField_113 = new JTextField("");
        textField_113.setName("tF_17_15_2");
        textField_113.setEditable(false);
        textField_113.setBounds(520, 467, 48, 19);
        p.add(textField_113);
        
        JCheckBox checkBox_85 = new JCheckBox("CASE3");
        checkBox_85.setName("cbx_17_15_3");
        checkBox_85.setBounds(576, 466, 71, 21);
        p.add(checkBox_85);
        addActionCheckbox(checkBox_85, "tF_17_15_3", "kdw003.Scenario15Case3");
        
        JTextField textField_114 = new JTextField("");
        textField_114.setName("tF_17_15_3");
        textField_114.setEditable(false);
        textField_114.setBounds(656, 467, 48, 19);
        p.add(textField_114);
        
        JLabel lblScenario_14 = new JLabel("SCENARIO17");
        lblScenario_14.setName("lb_17_17");
        lblScenario_14.setBounds(158, 516, 78, 13);
        p.add(lblScenario_14);
        
        JCheckBox checkBox_86 = new JCheckBox("CASE1");
        checkBox_86.setName("cbx_17_17_1");
        checkBox_86.setBounds(304, 512, 71, 21);
        p.add(checkBox_86);
        addActionCheckbox(checkBox_86, "tF_17_17_1", "kdw003.Scenario17Case1");
        
        JTextField textField_115 = new JTextField("");
        textField_115.setName("tF_17_17_1");
        textField_115.setEditable(false);
        textField_115.setBounds(384, 513, 48, 19);
        p.add(textField_115);

        JCheckBox checkBox_77 = new JCheckBox("全件選択");
        checkBox_77.setName("cbx_17_all");
        checkBox_77.setBounds(8, 29, 88, 21);
        p.add(checkBox_77);
        addActionCheckboxAll(checkBox_77);

        JCheckBox checkBox_87 = new JCheckBox("全件選択");
        checkBox_87.setName("cbx_17_3_all");
        checkBox_87.setBounds(1128, 52, 88, 21);
        p.add(checkBox_87);
        addActionCheckboxAll(checkBox_87);
        
        JCheckBox checkBox_88 = new JCheckBox("全件選択");
        checkBox_88.setName("cbx_17_4_all");
        checkBox_88.setBounds(1128, 98, 88, 21);
        p.add(checkBox_88);
        addActionCheckboxAll(checkBox_88);
        
        JCheckBox checkBox_89 = new JCheckBox("全件選択");
        checkBox_89.setName("cbx_17_14_all");
        checkBox_89.setBounds(1128, 397, 88, 21);
        p.add(checkBox_89);
        addActionCheckboxAll(checkBox_89);
        
        JLabel lblScenario_15 = new JLabel("SCENARIO11");
        lblScenario_15.setName("lb_17_11");
        lblScenario_15.setBounds(158, 263, 78, 13);
        p.add(lblScenario_15);
        
        JCheckBox checkBox_106 = new JCheckBox("CASE1");
        checkBox_106.setName("cbx_17_11_1");
        checkBox_106.setBounds(304, 259, 71, 21);
        p.add(checkBox_106);
        addActionCheckbox(checkBox_106, "tF_17_11_1", "kdw003.Scenario11Case1");
        
        JTextField textField_135 = new JTextField("");
        textField_135.setName("tF_17_11_1");
        textField_135.setEditable(false);
        textField_135.setBounds(384, 260, 48, 19);
        p.add(textField_135);
        
        JCheckBox checkBox_107 = new JCheckBox("CASE2");
        checkBox_107.setName("cbx_17_11_2");
        checkBox_107.setBounds(440, 259, 71, 21);
        p.add(checkBox_107);
        addActionCheckbox(checkBox_107, "tF_17_11_2", "kdw003.Scenario11Case2");
        
        JTextField textField_136 = new JTextField("");
        textField_136.setName("tF_17_11_2");
        textField_136.setEditable(false);
        textField_136.setBounds(520, 260, 48, 19);
        p.add(textField_136);
        
        JCheckBox checkBox_108 = new JCheckBox("CASE3");
        checkBox_108.setName("cbx_17_11_3");
        checkBox_108.setBounds(576, 259, 71, 21);
        p.add(checkBox_108);
        addActionCheckbox(checkBox_108, "tF_17_11_3", "kdw003.Scenario11Case3");
        
        JTextField textField_137 = new JTextField("");
        textField_137.setName("tF_17_11_3");
        textField_137.setEditable(false);
        textField_137.setBounds(656, 260, 48, 19);
        p.add(textField_137);
        
        JCheckBox checkBox_109 = new JCheckBox("CASE4");
        checkBox_109.setName("cbx_17_11_4");
        checkBox_109.setBounds(712, 259, 71, 21);
        p.add(checkBox_109);
        addActionCheckbox(checkBox_109, "tF_17_11_4", "kdw003.Scenario11Case4");
        
        JTextField textField_138 = new JTextField("");
        textField_138.setName("tF_17_11_4");
        textField_138.setEditable(false);
        textField_138.setBounds(792, 260, 48, 19);
        p.add(textField_138);
        
        JCheckBox checkBox_110 = new JCheckBox("CASE5");
        checkBox_110.setName("cbx_17_11_5");
        checkBox_110.setBounds(852, 259, 71, 21);
        p.add(checkBox_110);
        addActionCheckbox(checkBox_110, "tF_17_11_5", "kdw003.Scenario11Case5");
        
        JTextField textField_139 = new JTextField("");
        textField_139.setName("tF_17_11_5");
        textField_139.setEditable(false);
        textField_139.setBounds(932, 260, 48, 19);
        p.add(textField_139);
        
        JCheckBox checkBox_111 = new JCheckBox("CASE6");
        checkBox_111.setName("cbx_17_11_6");
        checkBox_111.setBounds(992, 259, 71, 21);
        p.add(checkBox_111);
        addActionCheckbox(checkBox_111, "tF_17_11_6", "kdw003.Scenario11Case6");
        
        JTextField textField_140 = new JTextField("");
        textField_140.setName("tF_17_11_6");
        textField_140.setEditable(false);
        textField_140.setBounds(1072, 260, 48, 19);
        p.add(textField_140);
        
        JCheckBox checkBox_112 = new JCheckBox("CASE7");
        checkBox_112.setName("cbx_17_11_7");
        checkBox_112.setBounds(304, 282, 71, 21);
        p.add(checkBox_112);
        addActionCheckbox(checkBox_112, "tF_17_11_7", "kdw003.Scenario11Case7");
        
        JTextField textField_141 = new JTextField("");
        textField_141.setName("tF_17_11_7");
        textField_141.setEditable(false);
        textField_141.setBounds(384, 283, 48, 19);
        p.add(textField_141);
        
        JCheckBox checkBox_113 = new JCheckBox("CASE8");
        checkBox_113.setName("cbx_17_11_8");
        checkBox_113.setBounds(440, 282, 71, 21);
        p.add(checkBox_113);
        addActionCheckbox(checkBox_113, "tF_17_11_8", "kdw003.Scenario11Case8");
        
        JTextField textField_142 = new JTextField("");
        textField_142.setName("tF_17_11_8");
        textField_142.setEditable(false);
        textField_142.setBounds(520, 283, 48, 19);
        p.add(textField_142);
        
        JCheckBox checkBox_114 = new JCheckBox("CASE9");
        checkBox_114.setName("cbx_17_11_9");
        checkBox_114.setBounds(576, 282, 71, 21);
        p.add(checkBox_114);
        addActionCheckbox(checkBox_114, "tF_17_11_9", "kdw003.Scenario11Case9");
        
        JTextField textField_143 = new JTextField("");
        textField_143.setName("tF_17_11_9");
        textField_143.setEditable(false);
        textField_143.setBounds(656, 283, 48, 19);
        p.add(textField_143);
        
        JCheckBox checkBox_115 = new JCheckBox("CASE10");
        checkBox_115.setName("cbx_17_11_10");
        checkBox_115.setBounds(712, 282, 71, 21);
        p.add(checkBox_115);
        addActionCheckbox(checkBox_115, "tF_17_11_10", "kdw003.Scenario11Case10");
        
        JTextField textField_144 = new JTextField("");
        textField_144.setName("tF_17_11_10");
        textField_144.setEditable(false);
        textField_144.setBounds(792, 283, 48, 19);
        p.add(textField_144);
        
        JCheckBox checkBox_116 = new JCheckBox("CASE11");
        checkBox_116.setName("cbx_17_11_11");
        checkBox_116.setBounds(852, 282, 71, 21);
        p.add(checkBox_116);
        addActionCheckbox(checkBox_116, "tF_17_11_11", "kdw003.Scenario11Case11");
        
        JTextField textField_145 = new JTextField("");
        textField_145.setName("tF_17_11_11");
        textField_145.setEditable(false);
        textField_145.setBounds(932, 283, 48, 19);
        p.add(textField_145);
        
        JCheckBox chckbxCase_40 = new JCheckBox("CASE14");
        chckbxCase_40.setName("cbx_17_11_14");
        chckbxCase_40.setBounds(992, 282, 71, 21);
        p.add(chckbxCase_40);
        addActionCheckbox(chckbxCase_40, "tF_17_11_14", "kdw003.Scenario11Case14");
        
        JTextField textField_146 = new JTextField("");
        textField_146.setName("tF_17_11_14");
        textField_146.setEditable(false);
        textField_146.setBounds(1072, 283, 48, 19);
        p.add(textField_146);
        
        JCheckBox chckbxCase_41 = new JCheckBox("CASE19");
        chckbxCase_41.setName("cbx_17_11_19");
        chckbxCase_41.setBounds(852, 305, 71, 21);
        p.add(chckbxCase_41);
        addActionCheckbox(chckbxCase_41, "tF_17_11_19", "kdw003.Scenario11Case19");
        
        JTextField textField_147 = new JTextField("");
        textField_147.setName("tF_17_11_19");
        textField_147.setEditable(false);
        textField_147.setBounds(932, 306, 48, 19);
        p.add(textField_147);
        
        JCheckBox chckbxCase_42 = new JCheckBox("CASE20");
        chckbxCase_42.setName("cbx_17_11_20");
        chckbxCase_42.setBounds(992, 305, 71, 21);
        p.add(chckbxCase_42);
        addActionCheckbox(chckbxCase_42, "tF_17_11_20", "kdw003.Scenario11Case20");
        
        JTextField textField_148 = new JTextField("");
        textField_148.setName("tF_17_11_20");
        textField_148.setEditable(false);
        textField_148.setBounds(1072, 306, 48, 19);
        p.add(textField_148);
        
        JCheckBox checkBox_120 = new JCheckBox("CASE15");
        checkBox_120.setName("cbx_17_11_15");
        checkBox_120.setBounds(304, 305, 71, 21);
        p.add(checkBox_120);
        addActionCheckbox(chckbxCase_40, "tF_17_11_15", "kdw003.Scenario11Case15");
        
        JTextField textField_149 = new JTextField("");
        textField_149.setName("tF_17_11_15");
        textField_149.setEditable(false);
        textField_149.setBounds(384, 306, 48, 19);
        p.add(textField_149);
        
        JCheckBox checkBox_121 = new JCheckBox("CASE16");
        checkBox_121.setName("cbx_17_11_16");
        checkBox_121.setBounds(440, 305, 71, 21);
        p.add(checkBox_121);
        addActionCheckbox(checkBox_121, "tF_17_11_16", "kdw003.Scenario11Case16");
        
        JTextField textField_150 = new JTextField("");
        textField_150.setName("tF_17_11_16");
        textField_150.setEditable(false);
        textField_150.setBounds(520, 306, 48, 19);
        p.add(textField_150);
        
        JCheckBox checkBox_122 = new JCheckBox("CASE17");
        checkBox_122.setName("cbx_17_11_17");
        checkBox_122.setBounds(576, 305, 71, 21);
        p.add(checkBox_122);
        addActionCheckbox(checkBox_122, "tF_17_11_17", "kdw003.Scenario11Case17");
        
        JTextField textField_151 = new JTextField("");
        textField_151.setName("tF_17_11_17");
        textField_151.setEditable(false);
        textField_151.setBounds(656, 306, 48, 19);
        p.add(textField_151);
        
        JCheckBox checkBox_123 = new JCheckBox("CASE18");
        checkBox_123.setName("cbx_17_11_18");
        checkBox_123.setBounds(712, 305, 71, 21);
        p.add(checkBox_123);
        addActionCheckbox(checkBox_123, "tF_17_11_18", "kdw003.Scenario11Case18");
        
        JTextField textField_152 = new JTextField("");
        textField_152.setName("tF_17_11_18");
        textField_152.setEditable(false);
        textField_152.setBounds(792, 306, 48, 19);
        p.add(textField_152);
        
        JCheckBox chckbxCase_43 = new JCheckBox("CASE21");
        chckbxCase_43.setName("cbx_17_11_21");
        chckbxCase_43.setBounds(304, 328, 71, 21);
        p.add(chckbxCase_43);
        addActionCheckbox(chckbxCase_43, "tF_17_11_21", "kdw003.Scenario11Case21");
        
        JTextField textField_153 = new JTextField("");
        textField_153.setName("tF_17_11_21");
        textField_153.setEditable(false);
        textField_153.setBounds(384, 329, 48, 19);
        p.add(textField_153);
        
        JCheckBox chckbxCase_44 = new JCheckBox("CASE22");
        chckbxCase_44.setName("cbx_17_11_22");
        chckbxCase_44.setBounds(440, 328, 71, 21);
        p.add(chckbxCase_44);
        addActionCheckbox(chckbxCase_44, "tF_17_11_22", "kdw003.Scenario11Case22");
        
        JTextField textField_154 = new JTextField("");
        textField_154.setName("tF_17_11_22");
        textField_154.setEditable(false);
        textField_154.setBounds(520, 329, 48, 19);
        p.add(textField_154);
        
        JCheckBox chckbxCase_45 = new JCheckBox("CASE23");
        chckbxCase_45.setName("cbx_17_11_23");
        chckbxCase_45.setBounds(576, 328, 71, 21);
        p.add(chckbxCase_45);
        addActionCheckbox(chckbxCase_45, "tF_17_11_23", "kdw003.Scenario11Case23");
        
        JTextField textField_155 = new JTextField("");
        textField_155.setName("tF_17_11_23");
        textField_155.setEditable(false);
        textField_155.setBounds(656, 329, 48, 19);
        p.add(textField_155);
        
        JCheckBox checkBox_117 = new JCheckBox("全件選択");
        checkBox_117.setName("cbx_17_11_all");
        checkBox_117.setBounds(1128, 259, 88, 21);
        p.add(checkBox_117);
        addActionCheckboxAll(checkBox_117);
       
    } 

}