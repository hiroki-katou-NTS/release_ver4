package common;

import javax.swing.*;
import java.awt.Dimension;

public class Tab2 extends CreateMainTest{

    public JScrollPane scrollPane;

    public Tab2() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setPreferredSize(new Dimension(1250, 1000));
        
        scrollPane = new JScrollPane(p);
        scrollPane.setBounds(0, 0, 1280, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // KSU001
        JLabel lb_10 = new JLabel("KSU001");
        lb_10.setName("lb_10");
        lb_10.setBounds(12, 10, 73, 13);
        
        JLabel lb_10_1 = new JLabel("SCENARIO1");
        lb_10_1.setName("lb_10_1");
        lb_10_1.setBounds(158, 10, 78, 13);
        
        JCheckBox cbx_10_1_1 = new JCheckBox("CASE1");
        cbx_10_1_1.setName("cbx_10_1_1");
        cbx_10_1_1.setBounds(304, 6, 71, 21);
        
        JTextField tF_10_1_1 = new JTextField("");
        tF_10_1_1.setName("tF_10_1_1");
        tF_10_1_1.setEditable(false);
        tF_10_1_1.setBounds(384, 7, 48, 19);

        // KSU007
        JLabel lb_11 = new JLabel("KSU007");
        lb_11.setName("lb_11");
        lb_11.setBounds(12, 54, 73, 13);
        
        JLabel lb_11_1 = new JLabel("SCENARIO1");
        lb_11_1.setName("lb_11_1");
        lb_11_1.setBounds(158, 54, 78, 13);
        
        JCheckBox cbx_11_1_1 = new JCheckBox("CASE1");
        cbx_11_1_1.setName("cbx_11_1_1");
        cbx_11_1_1.setBounds(304, 50, 71, 21);
        
        JTextField tF_11_1_1 = new JTextField("");
        tF_11_1_1.setName("tF_11_1_1");
        tF_11_1_1.setEditable(false);
        tF_11_1_1.setBounds(384, 51, 48, 19);

        JCheckBox cbx_11_1_2 = new JCheckBox("CASE2");
        cbx_11_1_2.setName("cbx_11_1_2");
        cbx_11_1_2.setBounds(440, 50, 71, 21);

        JTextField tF_11_1_2 = new JTextField("");
        tF_11_1_2.setName("tF_11_1_2");
        tF_11_1_2.setEditable(false);
        tF_11_1_2.setBounds(520, 54, 48, 19);

        // KIF001
        JLabel lb_15 = new JLabel("KIF001");
        lb_15.setName("lb_15");
        lb_15.setBounds(12, 98, 73, 13);
        
        JLabel lb_15_1 = new JLabel("SCENARIO1");
        lb_15_1.setName("lb_15_1");
        lb_15_1.setBounds(158, 98, 78, 13);
        
        JCheckBox cbx_15_1_1 = new JCheckBox("CASE1");
        cbx_15_1_1.setName("cbx_15_1_1");
        cbx_15_1_1.setBounds(304, 94, 71, 21);
        
        JTextField tF_15_1_1 = new JTextField("");
        tF_15_1_1.setName("tF_15_1_1");
        tF_15_1_1.setEditable(false);
        tF_15_1_1.setBounds(384, 95, 48, 19);
        
        JCheckBox cbx_15_1_2 = new JCheckBox("CASE2");
        cbx_15_1_2.setName("cbx_15_1_2");
        cbx_15_1_2.setBounds(440, 94, 71, 21);
        
        JTextField tF_15_1_2 = new JTextField("");
        tF_15_1_2.setName("tF_15_1_2");
        tF_15_1_2.setEditable(false);
        tF_15_1_2.setBounds(520, 95, 48, 19);
        
        JCheckBox cbx_15_1_3 = new JCheckBox("CASE3");
        cbx_15_1_3.setName("cbx_15_1_3");
        cbx_15_1_3.setBounds(576, 94, 71, 21);
        
        JTextField tF_15_1_3 = new JTextField("");
        tF_15_1_3.setName("tF_15_1_3");
        tF_15_1_3.setEditable(false);
        tF_15_1_3.setBounds(656, 95, 48, 19);
        
        JCheckBox cbx_15_1_4 = new JCheckBox("CASE4");
        cbx_15_1_4.setName("cbx_15_1_4");
        cbx_15_1_4.setBounds(712, 94, 71, 21);
        
        JTextField tF_15_1_4 = new JTextField("");
        tF_15_1_4.setName("tF_15_1_4");
        tF_15_1_4.setEditable(false);
        tF_15_1_4.setBounds(792, 95, 48, 19);

        // KMW003
        JLabel lb_21 = new JLabel("KMW003");
        lb_21.setName("lb_21");
        lb_21.setBounds(12, 142, 73, 13);
        
        JLabel lb_21_1 = new JLabel("SCENARIO1");
        lb_21_1.setName("lb_21_1");
        lb_21_1.setBounds(158, 142, 78, 13);
        
        JCheckBox cbx_21_1_1 = new JCheckBox("CASE1");
        cbx_21_1_1.setName("cbx_21_1_1");
        cbx_21_1_1.setBounds(304, 138, 71, 21);
        
        JTextField tF_21_1_1 = new JTextField("");
        tF_21_1_1.setName("tF_21_1_1");
        tF_21_1_1.setEditable(false);
        tF_21_1_1.setBounds(384, 139, 48, 19);
        
        JCheckBox cbx_21_1_2 = new JCheckBox("CASE2");
        cbx_21_1_2.setName("cbx_21_1_2");
        cbx_21_1_2.setBounds(440, 138, 71, 21);
        
        JTextField tF_21_1_2 = new JTextField("");
        tF_21_1_2.setName("tF_21_1_2");
        tF_21_1_2.setEditable(false);
        tF_21_1_2.setBounds(520, 139, 48, 19);
        
        JCheckBox cbx_21_1_3 = new JCheckBox("CASE3");
        cbx_21_1_3.setName("cbx_21_1_3");
        cbx_21_1_3.setBounds(576, 138, 71, 21);
        
        JTextField tF_21_1_3 = new JTextField("");
        tF_21_1_3.setName("tF_21_1_3");
        tF_21_1_3.setEditable(false);
        tF_21_1_3.setBounds(656, 139, 48, 19);
        
        JCheckBox cbx_21_1_4 = new JCheckBox("CASE4");
        cbx_21_1_4.setName("cbx_21_1_4");
        cbx_21_1_4.setBounds(712, 138, 71, 21);
        
        JTextField tF_21_1_4 = new JTextField("");
        tF_21_1_4.setName("tF_21_1_4");
        tF_21_1_4.setEditable(false);
        tF_21_1_4.setBounds(792, 139, 48, 19);
        
        JCheckBox cbx_21_1_5 = new JCheckBox("CASE5");
        cbx_21_1_5.setName("cbx_21_1_5");
        cbx_21_1_5.setBounds(852, 138, 71, 21);
        
        JTextField tF_21_1_5 = new JTextField("");
        tF_21_1_5.setName("tF_21_1_5");
        tF_21_1_5.setEditable(false);
        tF_21_1_5.setBounds(932, 139, 48, 19);
        
        JCheckBox cbx_21_1_6 = new JCheckBox("CASE6");
        cbx_21_1_6.setName("cbx_21_1_6");
        cbx_21_1_6.setBounds(992, 138, 71, 21);
        
        JTextField tF_21_1_6 = new JTextField("");
        tF_21_1_6.setName("tF_21_1_6");
        tF_21_1_6.setEditable(false);
        tF_21_1_6.setBounds(1072, 139, 48, 19);
        
        JLabel lb_21_2 = new JLabel("SCENARIO2");
        lb_21_2.setName("lb_21_2");
        lb_21_2.setBounds(158, 164, 78, 13);
        
        JCheckBox cbx_21_2_1 = new JCheckBox("CASE1");
        cbx_21_2_1.setName("cbx_21_2_1");
        cbx_21_2_1.setBounds(304, 161, 71, 21);
        
        JTextField tF_21_2_1 = new JTextField("");
        tF_21_2_1.setName("tF_21_2_1");
        tF_21_2_1.setEditable(false);
        tF_21_2_1.setBounds(384, 162, 48, 19);
        
        JCheckBox cbx_21_2_2 = new JCheckBox("CASE2");
        cbx_21_2_2.setName("cbx_21_2_2");
        cbx_21_2_2.setBounds(440, 161, 71, 21);
        
        JTextField tF_21_2_2 = new JTextField("");
        tF_21_2_2.setName("tF_21_2_2");
        tF_21_2_2.setEditable(false);
        tF_21_2_2.setBounds(520, 162, 48, 19);
        
        JCheckBox cbx_21_2_3 = new JCheckBox("CASE3");
        cbx_21_2_3.setName("cbx_21_2_3");
        cbx_21_2_3.setBounds(576, 161, 71, 21);
        
        JTextField tF_21_2_3 = new JTextField("");
        tF_21_2_3.setName("tF_21_2_3");
        tF_21_2_3.setEditable(false);
        tF_21_2_3.setBounds(656, 162, 48, 19);
        
        JCheckBox cbx_21_2_4 = new JCheckBox("CASE4");
        cbx_21_2_4.setName("cbx_21_2_4");
        cbx_21_2_4.setBounds(712, 161, 71, 21);
        
        JTextField tF_21_2_4 = new JTextField("");
        tF_21_2_4.setName("tF_21_2_4");
        tF_21_2_4.setEditable(false);
        tF_21_2_4.setBounds(792, 162, 48, 19);
        
        JCheckBox cbx_21_2_5 = new JCheckBox("CASE5");
        cbx_21_2_5.setName("cbx_21_2_5");
        cbx_21_2_5.setBounds(852, 161, 71, 21);
        
        JTextField tF_21_2_5 = new JTextField("");
        tF_21_2_5.setName("tF_21_2_5");
        tF_21_2_5.setEditable(false);
        tF_21_2_5.setBounds(932, 162, 48, 19);
        
        JCheckBox cbx_21_2_6 = new JCheckBox("CASE6");
        cbx_21_2_6.setName("cbx_21_2_6");
        cbx_21_2_6.setBounds(992, 161, 71, 21);
        
        JTextField tF_21_2_6 = new JTextField("");
        tF_21_2_6.setName("tF_21_2_6");
        tF_21_2_6.setEditable(false);
        tF_21_2_6.setBounds(1072, 162, 48, 19);

        JCheckBox checkBox_68 = new JCheckBox("全件選択");
        checkBox_68.setName("cbx_21_all");
        checkBox_68.setBounds(1128, 138, 88, 21);
        p.add(checkBox_68);
        addActionCheckboxAll(checkBox_68);

        // KMW005
        JLabel lb_22 = new JLabel("KMW005");
        lb_22.setName("lb_22");
        lb_22.setBounds(12, 209, 73, 13);
        
        JLabel lb_22_1 = new JLabel("SCENARIO1");
        lb_22_1.setName("lb_22_1");
        lb_22_1.setBounds(158, 209, 78, 13);
        
        JCheckBox cbx_22_1_1 = new JCheckBox("CASE1");
        cbx_22_1_1.setName("cbx_22_1_1");
        cbx_22_1_1.setBounds(304, 205, 71, 21);
        
        JTextField tF_22_1_1 = new JTextField("");
        tF_22_1_1.setName("tF_22_1_1");
        tF_22_1_1.setEditable(false);
        tF_22_1_1.setBounds(384, 206, 48, 19);
        
        JCheckBox cbx_22_1_2 = new JCheckBox("CASE2");
        cbx_22_1_2.setName("cbx_22_1_2");
        cbx_22_1_2.setBounds(440, 205, 71, 21);
        
        JTextField tF_22_1_2 = new JTextField("");
        tF_22_1_2.setName("tF_22_1_2");
        tF_22_1_2.setEditable(false);
        tF_22_1_2.setBounds(520, 206, 48, 19);
        
        JCheckBox cbx_22_1_3 = new JCheckBox("CASE3");
        cbx_22_1_3.setName("cbx_22_1_3");
        cbx_22_1_3.setBounds(576, 205, 71, 21);
        
        JTextField tF_22_1_3 = new JTextField("");
        tF_22_1_3.setName("tF_22_1_3");
        tF_22_1_3.setEditable(false);
        tF_22_1_3.setBounds(656, 206, 48, 19);
        
        JCheckBox cbx_22_1_4 = new JCheckBox("CASE4");
        cbx_22_1_4.setName("cbx_22_1_4");
        cbx_22_1_4.setBounds(712, 205, 71, 21);
        
        JTextField tF_22_1_4 = new JTextField("");
        tF_22_1_4.setName("tF_22_1_4");
        tF_22_1_4.setEditable(false);
        tF_22_1_4.setBounds(792, 206, 48, 19);
        
        JCheckBox cbx_22_1_5 = new JCheckBox("CASE5");
        cbx_22_1_5.setName("cbx_22_1_5");
        cbx_22_1_5.setBounds(852, 205, 71, 21);
        
        JTextField tF_22_1_5 = new JTextField("");
        tF_22_1_5.setName("tF_22_1_5");
        tF_22_1_5.setEditable(false);
        tF_22_1_5.setBounds(932, 206, 48, 19);
        
        JCheckBox cbx_22_1_6 = new JCheckBox("CASE6");
        cbx_22_1_6.setName("cbx_22_1_6");
        cbx_22_1_6.setBounds(992, 205, 71, 21);
        
        JTextField tF_22_1_6 = new JTextField("");
        tF_22_1_6.setName("tF_22_1_6");
        tF_22_1_6.setEditable(false);
        tF_22_1_6.setBounds(1072, 206, 48, 19);
        
        JLabel lb_22_2 = new JLabel("SCENARIO2");
        lb_22_2.setName("lb_22_2");
        lb_22_2.setBounds(158, 232, 78, 13);
        
        JCheckBox cbx_22_2_1 = new JCheckBox("CASE1");
        cbx_22_2_1.setName("cbx_22_2_1");
        cbx_22_2_1.setBounds(304, 228, 71, 21);
        
        JTextField tF_22_2_1 = new JTextField("");
        tF_22_2_1.setName("tF_22_2_1");
        tF_22_2_1.setEditable(false);
        tF_22_2_1.setBounds(384, 229, 48, 19);
        
        JCheckBox cbx_22_2_3 = new JCheckBox("CASE3");
        cbx_22_2_3.setName("cbx_22_2_3");
        cbx_22_2_3.setBounds(440, 228, 71, 21);
        
        JTextField tF_22_2_3 = new JTextField("");
        tF_22_2_3.setName("tF_22_2_3");
        tF_22_2_3.setEditable(false);
        tF_22_2_3.setBounds(520, 229, 48, 19);

        JCheckBox checkBox_69 = new JCheckBox("全件選択");
        checkBox_69.setName("cbx_22_all");
        checkBox_69.setBounds(1128, 205, 88, 21);
        p.add(checkBox_69);
        addActionCheckboxAll(checkBox_69);

        // KMW006
        JLabel lb_23 = new JLabel("KMW006");
        lb_23.setName("lb_23");
        lb_23.setBounds(12, 268, 73, 13);
        
        JLabel lb_23_1 = new JLabel("SCENARIO1");
        lb_23_1.setName("lb_23_1");
        lb_23_1.setBounds(158, 268, 78, 13);
        
        JLabel lb_23_2 = new JLabel("SCENARIO2");
        lb_23_2.setName("lb_23_2");
        lb_23_2.setBounds(158, 291, 78, 13);
        
        JLabel lb_23_3 = new JLabel("SCENARIO3");
        lb_23_3.setName("lb_23_3");
        lb_23_3.setBounds(158, 314, 78, 13);
        
        JLabel lb_23_4 = new JLabel("SCENARIO4");
        lb_23_4.setName("lb_23_4");
        lb_23_4.setBounds(158, 337, 78, 13);
        
        JCheckBox cbx_23_4_1 = new JCheckBox("CASE1");
        cbx_23_4_1.setName("cbx_23_4_1");
        cbx_23_4_1.setBounds(304, 333, 71, 21);
        
        JCheckBox cbx_23_3_1 = new JCheckBox("CASE1");
        cbx_23_3_1.setName("cbx_23_3_1");
        cbx_23_3_1.setBounds(304, 310, 71, 21);
        
        JCheckBox cbx_23_2_1 = new JCheckBox("CASE1");
        cbx_23_2_1.setName("cbx_23_2_1");
        cbx_23_2_1.setBounds(304, 287, 71, 21);
        
        JCheckBox cbx_23_1_1 = new JCheckBox("CASE1");
        cbx_23_1_1.setName("cbx_23_1_1");
        cbx_23_1_1.setBounds(304, 264, 71, 21);
        
        JTextField tF_23_1_1 = new JTextField("");
        tF_23_1_1.setName("tF_23_1_1");
        tF_23_1_1.setEditable(false);
        tF_23_1_1.setBounds(384, 265, 48, 19);
        
        JTextField tF_23_2_1 = new JTextField("");
        tF_23_2_1.setName("tF_23_2_1");
        tF_23_2_1.setEditable(false);
        tF_23_2_1.setBounds(384, 288, 48, 19);
        
        JTextField tF_23_3_1 = new JTextField("");
        tF_23_3_1.setName("tF_23_3_1");
        tF_23_3_1.setEditable(false);
        tF_23_3_1.setBounds(384, 311, 48, 19);
        
        JTextField tF_23_4_1 = new JTextField("");
        tF_23_4_1.setName("tF_23_4_1");
        tF_23_4_1.setEditable(false);
        tF_23_4_1.setBounds(384, 334, 48, 19);
        
        JCheckBox cbx_23_4_2 = new JCheckBox("CASE2");
        cbx_23_4_2.setName("cbx_23_4_2");
        cbx_23_4_2.setBounds(440, 333, 71, 21);
        
        JCheckBox cbx_23_3_2 = new JCheckBox("CASE2");
        cbx_23_3_2.setName("cbx_23_3_2");
        cbx_23_3_2.setBounds(440, 310, 71, 21);
        
        JCheckBox cbx_23_2_2 = new JCheckBox("CASE2");
        cbx_23_2_2.setName("cbx_23_2_2");
        cbx_23_2_2.setBounds(440, 287, 71, 21);
        
        JTextField tF_23_2_2 = new JTextField("");
        tF_23_2_2.setName("tF_23_2_2");
        tF_23_2_2.setEditable(false);
        tF_23_2_2.setBounds(520, 288, 48, 19);
        
        JTextField tF_23_3_2 = new JTextField("");
        tF_23_3_2.setName("tF_23_3_2");
        tF_23_3_2.setEditable(false);
        tF_23_3_2.setBounds(520, 311, 48, 19);
        
        JTextField tF_23_4_2 = new JTextField("");
        tF_23_4_2.setName("tF_23_4_2");
        tF_23_4_2.setEditable(false);
        tF_23_4_2.setBounds(520, 334, 48, 19);
        
        JCheckBox cbx_23_3_3 = new JCheckBox("CASE3");
        cbx_23_3_3.setName("cbx_23_3_3");
        cbx_23_3_3.setBounds(576, 310, 71, 21);
        
        JCheckBox cbx_23_2_3 = new JCheckBox("CASE3");
        cbx_23_2_3.setName("cbx_23_2_3");
        cbx_23_2_3.setBounds(576, 287, 71, 21);
        
        JTextField tF_23_2_3 = new JTextField("");
        tF_23_2_3.setName("tF_23_2_3");
        tF_23_2_3.setEditable(false);
        tF_23_2_3.setBounds(656, 288, 48, 19);
        
        JTextField tF_23_3_3 = new JTextField("");
        tF_23_3_3.setName("tF_23_3_3");
        tF_23_3_3.setEditable(false);
        tF_23_3_3.setBounds(656, 311, 48, 19);

        JCheckBox checkBox_70 = new JCheckBox("全件選択");
        checkBox_70.setName("cbx_23_all");
        checkBox_70.setBounds(712, 264, 88, 21);
        p.add(checkBox_70);
        addActionCheckboxAll(checkBox_70);

        // CMM044
        JLabel lb_29 = new JLabel("CMM044");
        lb_29.setName("lb_29");
        lb_29.setBounds(12, 378, 73, 13);
        
        JLabel lb_29_1 = new JLabel("SCENARIO1");
        lb_29_1.setName("lb_29_1");
        lb_29_1.setBounds(158, 378, 78, 13);
        
        JCheckBox cbx_29_1_1 = new JCheckBox("CASE1");
        cbx_29_1_1.setName("cbx_29_1_1");
        cbx_29_1_1.setBounds(304, 374, 71, 21);
        
        JTextField tF_29_1_1 = new JTextField("");
        tF_29_1_1.setName("tF_29_1_1");
        tF_29_1_1.setEditable(false);
        tF_29_1_1.setBounds(384, 375, 48, 19);
        
        JCheckBox cbx_29_1_2 = new JCheckBox("CASE2");
        cbx_29_1_2.setName("cbx_29_1_2");
        cbx_29_1_2.setBounds(440, 374, 71, 21);
        
        JTextField tF_29_1_2 = new JTextField("");
        tF_29_1_2.setName("tF_29_1_2");
        tF_29_1_2.setEditable(false);
        tF_29_1_2.setBounds(520, 375, 48, 19);
        
        JCheckBox cbx_29_1_3 = new JCheckBox("CASE3");
        cbx_29_1_3.setName("cbx_29_1_3");
        cbx_29_1_3.setBounds(576, 374, 71, 21);
        
        JTextField tF_29_1_3 = new JTextField("");
        tF_29_1_3.setName("tF_29_1_3");
        tF_29_1_3.setEditable(false);
        tF_29_1_3.setBounds(656, 375, 48, 19);
        
        JCheckBox cbx_29_1_4 = new JCheckBox("CASE4");
        cbx_29_1_4.setName("cbx_29_1_4");
        cbx_29_1_4.setBounds(712, 374, 71, 21);
        
        JTextField tF_29_1_4 = new JTextField("");
        tF_29_1_4.setName("tF_29_1_4");
        tF_29_1_4.setEditable(false);
        tF_29_1_4.setBounds(792, 375, 48, 19);
        
        JCheckBox cbx_29_1_5 = new JCheckBox("CASE5");
        cbx_29_1_5.setName("cbx_29_1_5");
        cbx_29_1_5.setBounds(852, 374, 71, 21);
        
        JTextField tF_29_1_5 = new JTextField("");
        tF_29_1_5.setName("tF_29_1_5");
        tF_29_1_5.setEditable(false);
        tF_29_1_5.setBounds(932, 375, 48, 19);
        
        JCheckBox cbx_29_1_6 = new JCheckBox("CASE6");
        cbx_29_1_6.setName("cbx_29_1_6");
        cbx_29_1_6.setBounds(992, 374, 71, 21);
        
        JTextField tF_29_1_6 = new JTextField("");
        tF_29_1_6.setName("tF_29_1_6");
        tF_29_1_6.setEditable(false);
        tF_29_1_6.setBounds(1072, 375, 48, 19);
        
        JCheckBox cbx_29_1_7 = new JCheckBox("CASE7");
        cbx_29_1_7.setName("cbx_29_1_7");
        cbx_29_1_7.setBounds(304, 397, 71, 21);
        
        JTextField tF_29_1_7 = new JTextField("");
        tF_29_1_7.setName("tF_29_1_7");
        tF_29_1_7.setEditable(false);
        tF_29_1_7.setBounds(384, 398, 48, 19);
        
        JCheckBox cbx_29_1_8 = new JCheckBox("CASE8");
        cbx_29_1_8.setName("cbx_29_1_8");
        cbx_29_1_8.setBounds(440, 397, 71, 21);
        
        JTextField tF_29_1_8 = new JTextField("");
        tF_29_1_8.setName("tF_29_1_8");
        tF_29_1_8.setEditable(false);
        tF_29_1_8.setBounds(520, 398, 48, 19);
        
        JCheckBox cbx_29_1_9 = new JCheckBox("CASE9");
        cbx_29_1_9.setName("cbx_29_1_9");
        cbx_29_1_9.setBounds(576, 397, 71, 21);
        
        JTextField tF_29_1_9 = new JTextField("");
        tF_29_1_9.setName("tF_29_1_9");
        tF_29_1_9.setEditable(false);
        tF_29_1_9.setBounds(656, 398, 48, 19);

        JCheckBox checkBox_71 = new JCheckBox("全件選択");
        checkBox_71.setName("cbx_29_all");
        checkBox_71.setBounds(1128, 374, 88, 21);
        p.add(checkBox_71);
        addActionCheckboxAll(checkBox_71);

        // CMM053
        JLabel lb_31 = new JLabel("CMM053");
        lb_31.setName("lb_31");
        lb_31.setBounds(12, 443, 73, 13);
        
        JLabel lb_31_1 = new JLabel("SCENARIO1");
        lb_31_1.setName("lb_31_1");
        lb_31_1.setBounds(158, 443, 78, 13);
        
        JLabel lb_31_2 = new JLabel("SCENARIO2");
        lb_31_2.setName("lb_31_2");
        lb_31_2.setBounds(158, 466, 78, 13);
        
        JLabel lb_31_3 = new JLabel("SCENARIO3");
        lb_31_3.setName("lb_31_3");
        lb_31_3.setBounds(158, 489, 78, 13);
        
        JLabel lb_31_4 = new JLabel("SCENARIO4");
        lb_31_4.setName("lb_31_4");
        lb_31_4.setBounds(158, 512, 78, 13);
        
        JCheckBox cbx_31_4_1 = new JCheckBox("CASE1");
        cbx_31_4_1.setName("cbx_31_4_1");
        cbx_31_4_1.setBounds(304, 508, 71, 21);
        
        JCheckBox cbx_31_3_1 = new JCheckBox("CASE1");
        cbx_31_3_1.setName("cbx_31_3_1");
        cbx_31_3_1.setBounds(304, 485, 71, 21);
        
        JCheckBox cbx_31_2_1 = new JCheckBox("CASE1");
        cbx_31_2_1.setName("cbx_31_2_1");
        cbx_31_2_1.setBounds(304, 462, 71, 21);
        
        JCheckBox cbx_31_1_1 = new JCheckBox("CASE1");
        cbx_31_1_1.setName("cbx_31_1_1");
        cbx_31_1_1.setBounds(304, 439, 71, 21);
        
        JTextField tF_31_1_1 = new JTextField("");
        tF_31_1_1.setName("tF_31_1_1");
        tF_31_1_1.setEditable(false);
        tF_31_1_1.setBounds(384, 440, 48, 19);
        
        JTextField tF_31_2_1 = new JTextField("");
        tF_31_2_1.setName("tF_31_2_1");
        tF_31_2_1.setEditable(false);
        tF_31_2_1.setBounds(384, 463, 48, 19);
        
        JTextField tF_31_3_1 = new JTextField("");
        tF_31_3_1.setName("tF_31_3_1");
        tF_31_3_1.setEditable(false);
        tF_31_3_1.setBounds(384, 486, 48, 19);
        
        JTextField tF_31_4_1 = new JTextField("");
        tF_31_4_1.setName("tF_31_4_1");
        tF_31_4_1.setEditable(false);
        tF_31_4_1.setBounds(384, 509, 48, 19);

        JLabel lb_31_5 = new JLabel("SCENARIO5");
        lb_31_5.setName("lb_31_5");
        lb_31_5.setBounds(158, 535, 78, 13);
        
        JCheckBox cbx_31_5_1 = new JCheckBox("CASE1");
        cbx_31_5_1.setName("cbx_31_5_1");
        cbx_31_5_1.setBounds(304, 531, 71, 21);
        
        JTextField tF_31_5_1 = new JTextField("");
        tF_31_5_1.setName("tF_31_5_1");
        tF_31_5_1.setEditable(false);
        tF_31_5_1.setBounds(384, 532, 48, 19);
        
        JLabel lb_31_6 = new JLabel("SCENARIO6");
        lb_31_6.setName("lb_31_6");
        lb_31_6.setBounds(158, 558, 78, 13);
        
        JCheckBox cbx_31_6_1 = new JCheckBox("CASE1");
        cbx_31_6_1.setName("cbx_31_6_1");
        cbx_31_6_1.setBounds(304, 554, 71, 21);
        
        JTextField tF_31_6_1 = new JTextField("");
        tF_31_6_1.setName("tF_31_6_1");
        tF_31_6_1.setEditable(false);
        tF_31_6_1.setBounds(384, 555, 48, 19);

        JCheckBox checkBox_72 = new JCheckBox("全件選択");
        checkBox_72.setName("cbx_31_all");
        checkBox_72.setBounds(440, 439, 88, 21);
        p.add(checkBox_72);
        addActionCheckboxAll(checkBox_72);

        // KAF006
        JLabel lb_33 = new JLabel("KAF006");
        lb_33.setName("lb_33");
        lb_33.setBounds(12, 597, 73, 13);
        
        JLabel lb_33_1 = new JLabel("SCENARIO1");
        lb_33_1.setName("lb_33_1");
        lb_33_1.setBounds(158, 597, 78, 13);
        
        JLabel lb_33_2 = new JLabel("SCENARIO2");
        lb_33_2.setName("lb_33_2");
        lb_33_2.setBounds(158, 620, 78, 13);
        
        JLabel lb_33_3 = new JLabel("SCENARIO3");
        lb_33_3.setName("lb_33_3");
        lb_33_3.setBounds(158, 643, 78, 13);
        
        JLabel lb_33_4 = new JLabel("SCENARIO4");
        lb_33_4.setName("lb_33_4");
        lb_33_4.setBounds(158, 666, 78, 13);
        
        JCheckBox cbx_33_4_1 = new JCheckBox("CASE1");
        cbx_33_4_1.setName("cbx_33_4_1");
        cbx_33_4_1.setBounds(304, 662, 71, 21);
        
        JCheckBox cbx_33_3_1 = new JCheckBox("CASE1");
        cbx_33_3_1.setName("cbx_33_3_1");
        cbx_33_3_1.setBounds(304, 639, 71, 21);
        
        JCheckBox cbx_33_2_1 = new JCheckBox("CASE1");
        cbx_33_2_1.setName("cbx_33_2_1");
        cbx_33_2_1.setBounds(304, 616, 71, 21);
        
        JCheckBox cbx_33_1_1 = new JCheckBox("CASE1");
        cbx_33_1_1.setName("cbx_33_1_1");
        cbx_33_1_1.setBounds(304, 593, 71, 21);
        
        JTextField tF_33_1_1 = new JTextField("");
        tF_33_1_1.setName("tF_33_1_1");
        tF_33_1_1.setEditable(false);
        tF_33_1_1.setBounds(384, 594, 48, 19);
        
        JTextField tF_33_2_1 = new JTextField("");
        tF_33_2_1.setName("tF_33_2_1");
        tF_33_2_1.setEditable(false);
        tF_33_2_1.setBounds(384, 617, 48, 19);
        
        JTextField tF_33_3_1 = new JTextField("");
        tF_33_3_1.setName("tF_33_3_1");
        tF_33_3_1.setEditable(false);
        tF_33_3_1.setBounds(384, 640, 48, 19);
        
        JTextField tF_33_4_1 = new JTextField("");
        tF_33_4_1.setName("tF_33_4_1");
        tF_33_4_1.setEditable(false);
        tF_33_4_1.setBounds(384, 663, 48, 19);
        
        JCheckBox cbx_33_4_2 = new JCheckBox("CASE2");
        cbx_33_4_2.setName("cbx_33_4_2");
        cbx_33_4_2.setBounds(440, 333, 71, 21);
        
        JCheckBox cbx_33_3_2 = new JCheckBox("CASE2");
        cbx_33_3_2.setName("cbx_33_3_2");
        cbx_33_3_2.setBounds(440, 639, 71, 21);
        
        JCheckBox cbx_33_2_2 = new JCheckBox("CASE2");
        cbx_33_2_2.setName("cbx_33_2_2");
        cbx_33_2_2.setBounds(440, 616, 71, 21);
        
        JTextField tF_33_2_2 = new JTextField("");
        tF_33_2_2.setName("tF_33_2_2");
        tF_33_2_2.setEditable(false);
        tF_33_2_2.setBounds(520, 617, 48, 19);
        
        JTextField tF_33_3_2 = new JTextField("");
        tF_33_3_2.setName("tF_33_3_2");
        tF_33_3_2.setEditable(false);
        tF_33_3_2.setBounds(520, 640, 48, 19);
        
        JTextField tF_33_4_2 = new JTextField("");
        tF_33_4_2.setName("tF_33_4_2");
        tF_33_4_2.setEditable(false);
        tF_33_4_2.setBounds(520, 334, 48, 19);
        
        JCheckBox cbx_33_3_3 = new JCheckBox("CASE3");
        cbx_33_3_3.setName("cbx_33_3_3");
        cbx_33_3_3.setBounds(576, 639, 71, 21);
        
        JCheckBox cbx_33_2_3 = new JCheckBox("CASE3");
        cbx_33_2_3.setName("cbx_33_2_3");
        cbx_33_2_3.setBounds(576, 616, 71, 21);
        
        JTextField tF_33_2_3 = new JTextField("");
        tF_33_2_3.setName("tF_33_2_3");
        tF_33_2_3.setEditable(false);
        tF_33_2_3.setBounds(656, 617, 48, 19);
        
        JTextField tF_33_3_3 = new JTextField("");
        tF_33_3_3.setName("tF_33_3_3");
        tF_33_3_3.setEditable(false);
        tF_33_3_3.setBounds(656, 640, 48, 19);

        JCheckBox cbx_33_1_2 = new JCheckBox("CASE2");
        cbx_33_1_2.setName("cbx_33_1_2");
        cbx_33_1_2.setBounds(440, 593, 71, 21);
        
        JTextField tF_33_1_2 = new JTextField("");
        tF_33_1_2.setName("tF_33_1_2");
        tF_33_1_2.setEditable(false);
        tF_33_1_2.setBounds(520, 594, 48, 19);
        
        JCheckBox cbx_33_1_3 = new JCheckBox("CASE3");
        cbx_33_1_3.setName("cbx_33_1_3");
        cbx_33_1_3.setBounds(576, 593, 71, 21);
        
        JTextField tF_33_1_3 = new JTextField("");
        tF_33_1_3.setName("tF_33_1_3");
        tF_33_1_3.setEditable(false);
        tF_33_1_3.setBounds(656, 594, 48, 19);
        
        JLabel lb_33_5 = new JLabel("SCENARIO5");
        lb_33_5.setName("lb_33_5");
        lb_33_5.setBounds(158, 689, 78, 13);
        
        JCheckBox cbx_33_5_1 = new JCheckBox("CASE1");
        cbx_33_5_1.setName("cbx_33_5_1");
        cbx_33_5_1.setBounds(304, 685, 71, 21);
        
        JTextField tF_33_5_1 = new JTextField("");
        tF_33_5_1.setName("tF_33_5_1");
        tF_33_5_1.setEditable(false);
        tF_33_5_1.setBounds(384, 686, 48, 19);
        
        JCheckBox cbx_33_5_2 = new JCheckBox("CASE2");
        cbx_33_5_2.setName("cbx_33_5_2");
        cbx_33_5_2.setBounds(440, 685, 71, 21);
        
        JTextField tF_33_5_2 = new JTextField("");
        tF_33_5_2.setName("tF_33_5_2");
        tF_33_5_2.setEditable(false);
        tF_33_5_2.setBounds(520, 686, 48, 19);

        JCheckBox checkBox_73 = new JCheckBox("全件選択");
        checkBox_73.setName("cbx_33_all");
        checkBox_73.setBounds(712, 593, 88, 21);
        p.add(checkBox_73);
        addActionCheckboxAll(checkBox_73);

        // KAF007
        JLabel lb_34 = new JLabel("KAF007");
        lb_34.setName("lb_34");
        lb_34.setBounds(12, 731, 73, 13);
        
        JLabel lb_34_1 = new JLabel("SCENARIO1");
        lb_34_1.setName("lb_34_1");
        lb_34_1.setBounds(158, 731, 78, 13);
        
        JLabel lb_34_2 = new JLabel("SCENARIO2");
        lb_34_2.setName("lb_34_2");
        lb_34_2.setBounds(158, 754, 78, 13);
        
        JLabel lb_34_3 = new JLabel("SCENARIO3");
        lb_34_3.setName("lb_34_3");
        lb_34_3.setBounds(158, 777, 78, 13);
        
        JLabel lb_34_4 = new JLabel("SCENARIO4");
        lb_34_4.setName("lb_34_4");
        lb_34_4.setBounds(158, 800, 78, 13);
        
        JCheckBox cbx_34_4_1 = new JCheckBox("CASE1");
        cbx_34_4_1.setName("cbx_34_4_1");
        cbx_34_4_1.setBounds(304, 796, 71, 21);
        
        JCheckBox cbx_34_3_1 = new JCheckBox("CASE1");
        cbx_34_3_1.setName("cbx_34_3_1");
        cbx_34_3_1.setBounds(304, 773, 71, 21);
        
        JCheckBox cbx_34_2_1 = new JCheckBox("CASE1");
        cbx_34_2_1.setName("cbx_34_2_1");
        cbx_34_2_1.setBounds(304, 750, 71, 21);
        
        JCheckBox cbx_34_1_1 = new JCheckBox("CASE1");
        cbx_34_1_1.setName("cbx_34_1_1");
        cbx_34_1_1.setBounds(304, 727, 71, 21);
        
        JTextField tF_34_1_1 = new JTextField("");
        tF_34_1_1.setName("tF_34_1_1");
        tF_34_1_1.setEditable(false);
        tF_34_1_1.setBounds(384, 728, 48, 19);
        
        JTextField tF_34_2_1 = new JTextField("");
        tF_34_2_1.setName("tF_34_2_1");
        tF_34_2_1.setEditable(false);
        tF_34_2_1.setBounds(384, 751, 48, 19);
        
        JTextField tF_34_3_1 = new JTextField("");
        tF_34_3_1.setName("tF_34_3_1");
        tF_34_3_1.setEditable(false);
        tF_34_3_1.setBounds(384, 774, 48, 19);
        
        JTextField tF_34_4_1 = new JTextField("");
        tF_34_4_1.setName("tF_34_4_1");
        tF_34_4_1.setEditable(false);
        tF_34_4_1.setBounds(384, 797, 48, 19);
        
        JCheckBox cbx_34_1_2 = new JCheckBox("CASE2");
        cbx_34_1_2.setName("cbx_34_1_2");
        cbx_34_1_2.setBounds(440, 727, 71, 21);

        JTextField tF_34_1_2 = new JTextField("");
        tF_34_1_2.setName("tF_34_1_2");
        tF_34_1_2.setEditable(false);
        tF_34_1_2.setBounds(520, 728, 48, 19);

        JCheckBox checkBox_74 = new JCheckBox("全件選択");
        checkBox_74.setName("cbx_34_all");
        checkBox_74.setBounds(576, 727, 88, 21);
        p.add(checkBox_74);
        addActionCheckboxAll(checkBox_74);

        p.add(lb_10);
        p.add(lb_10_1);
        p.add(cbx_10_1_1);
        p.add(tF_10_1_1);
        p.add(lb_11);
        p.add(lb_11_1);
        p.add(cbx_11_1_1);
        p.add(tF_11_1_1);
        p.add(cbx_11_1_2);
        p.add(tF_11_1_2);
        p.add(lb_15);
        p.add(lb_15_1);
        p.add(cbx_15_1_1);
        p.add(tF_15_1_1);
        p.add(cbx_15_1_2);
        p.add(tF_15_1_2);
        p.add(cbx_15_1_3);
        p.add(tF_15_1_3);
        p.add(cbx_15_1_4);
        p.add(tF_15_1_4);
        p.add(lb_21);
        p.add(lb_21_1);
        p.add(cbx_21_1_1);
        p.add(tF_21_1_1);
        p.add(cbx_21_1_2);
        p.add(tF_21_1_2);
        p.add(cbx_21_1_3);
        p.add(tF_21_1_3);
        p.add(cbx_21_1_4);
        p.add(tF_21_1_4);
        p.add(cbx_21_1_5);
        p.add(tF_21_1_5);
        p.add(cbx_21_1_6);
        p.add(tF_21_1_6);
        p.add(lb_21_2);
        p.add(cbx_21_2_1);
        p.add(tF_21_2_1);
        p.add(cbx_21_2_2);
        p.add(tF_21_2_2);
        p.add(cbx_21_2_3);
        p.add(tF_21_2_3);
        p.add(cbx_21_2_4);
        p.add(tF_21_2_4);
        p.add(cbx_21_2_5);
        p.add(tF_21_2_5);
        p.add(cbx_21_2_6);
        p.add(tF_21_2_6);
        p.add(lb_22);
        p.add(lb_22_1);
        p.add(cbx_22_1_1);
        p.add(tF_22_1_1);
        p.add(cbx_22_1_2);
        p.add(tF_22_1_2);
        p.add(cbx_22_1_3);
        p.add(tF_22_1_3);
        p.add(cbx_22_1_4);
        p.add(tF_22_1_4);
        p.add(cbx_22_1_5);
        p.add(tF_22_1_5);
        p.add(cbx_22_1_6);
        p.add(tF_22_1_6);
        p.add(lb_22_2);
        p.add(cbx_22_2_1);
        p.add(tF_22_2_1);
        p.add(cbx_22_2_3);
        p.add(tF_22_2_3);
        p.add(lb_23);
        p.add(lb_23_1);
        p.add(cbx_23_1_1);
        p.add(tF_23_1_1);
        p.add(lb_23_2);
        p.add(cbx_23_2_1);
        p.add(tF_23_2_1);
        p.add(cbx_23_2_2);
        p.add(tF_23_2_2);
        p.add(cbx_23_2_3);
        p.add(tF_23_2_3);
        p.add(lb_23_3);
        p.add(cbx_23_3_1);
        p.add(tF_23_3_1);
        p.add(cbx_23_3_2);
        p.add(tF_23_3_2);
        p.add(cbx_23_3_3);
        p.add(tF_23_3_3);
        p.add(lb_23_4);
        p.add(cbx_23_4_1);
        p.add(tF_23_4_1);
        p.add(cbx_23_4_2);
        p.add(tF_23_4_2);
        p.add(lb_29);
        p.add(lb_29_1);
        p.add(cbx_29_1_1);
        p.add(tF_29_1_1);
        p.add(cbx_29_1_2);
        p.add(tF_29_1_2);
        p.add(cbx_29_1_3);
        p.add(tF_29_1_3);
        p.add(cbx_29_1_4);
        p.add(tF_29_1_4);
        p.add(cbx_29_1_5);
        p.add(tF_29_1_5);
        p.add(cbx_29_1_6);
        p.add(tF_29_1_6);
        p.add(cbx_29_1_7);
        p.add(tF_29_1_7);
        p.add(cbx_29_1_8);
        p.add(tF_29_1_8);
        p.add(cbx_29_1_9);
        p.add(tF_29_1_9);
        p.add(lb_31);
        p.add(lb_31_1);
        p.add(cbx_31_1_1);
        p.add(tF_31_1_1);
        p.add(lb_31_2);
        p.add(cbx_31_2_1);
        p.add(tF_31_2_1);
        p.add(lb_31_3);
        p.add(cbx_31_3_1);
        p.add(tF_31_3_1);
        p.add(lb_31_4);
        p.add(cbx_31_4_1);
        p.add(tF_31_4_1);
        p.add(lb_31_5);
        p.add(cbx_31_5_1);
        p.add(tF_31_5_1);
        p.add(lb_31_6);
        p.add(cbx_31_6_1);
        p.add(tF_31_6_1);
        p.add(lb_33);
        p.add(lb_33_1);
        p.add(cbx_33_1_1);
        p.add(tF_33_1_1);
        p.add(cbx_33_1_2);
        p.add(tF_33_1_2);
        p.add(cbx_33_1_3);
        p.add(tF_33_1_3);
        p.add(lb_33_2);
        p.add(cbx_33_2_1);
        p.add(tF_33_2_1);
        p.add(cbx_33_2_2);
        p.add(tF_33_2_2);
        p.add(cbx_33_2_3);
        p.add(tF_33_2_3);
        p.add(lb_33_3);
        p.add(cbx_33_3_1);
        p.add(tF_33_3_1);
        p.add(cbx_33_3_2);
        p.add(tF_33_3_2);
        p.add(cbx_33_3_3);
        p.add(tF_33_3_3);
        p.add(lb_33_4);
        p.add(cbx_33_4_1);
        p.add(tF_33_4_1);
        p.add(lb_33_5);
        p.add(cbx_33_5_1);
        p.add(tF_33_5_1);
        p.add(cbx_33_5_2);
        p.add(tF_33_5_2);
        p.add(lb_34);
        p.add(lb_34_1);
        p.add(cbx_34_1_1);
        p.add(tF_34_1_1);
        p.add(cbx_34_1_2);
        p.add(tF_34_1_2);
        p.add(lb_34_2);
        p.add(cbx_34_2_1);
        p.add(tF_34_2_1);
        p.add(lb_34_3);
        p.add(cbx_34_3_1);
        p.add(tF_34_3_1);
        p.add(lb_34_4);
        p.add(cbx_34_4_1);
        p.add(tF_34_4_1); 

        addActionCheckbox(cbx_10_1_1, "tF_10_1_1", "ksu001.Scenario1Case1");
        addActionCheckbox(cbx_10_1_1, "tF_11_1_1", "ksu007.Scenario1Case1");
        addActionCheckbox(cbx_10_1_1, "tF_11_1_2", "ksu007.Scenario1Case2");
        addActionCheckbox(cbx_15_1_1, "tF_15_1_1", "kif001.Scenario1Case1");
        addActionCheckbox(cbx_15_1_2, "tF_15_1_2", "kif001.Scenario1Case2");
        addActionCheckbox(cbx_15_1_3, "tF_15_1_3", "kif001.Scenario1Case3");
        addActionCheckbox(cbx_15_1_4, "tF_15_1_4", "kif001.Scenario1Case4");
        addActionCheckbox(cbx_21_1_1, "tF_21_1_1", "kmw003.Scenario1Case1");
        addActionCheckbox(cbx_21_1_2, "tF_21_1_2", "kmw003.Scenario1Case2");
        addActionCheckbox(cbx_21_1_3, "tF_21_1_3", "kmw003.Scenario1Case3");
        addActionCheckbox(cbx_21_1_4, "tF_21_1_4", "kmw003.Scenario1Case4");
        addActionCheckbox(cbx_21_1_5, "tF_21_1_5", "kmw003.Scenario1Case5");
        addActionCheckbox(cbx_21_1_6, "tF_21_1_6", "kmw003.Scenario1Case6");
        addActionCheckbox(cbx_21_2_1, "tF_21_2_1", "kmw003.Scenario2Case1");
        addActionCheckbox(cbx_21_2_2, "tF_21_2_2", "kmw003.Scenario2Case2");
        addActionCheckbox(cbx_21_2_3, "tF_21_2_3", "kmw003.Scenario2Case3");
        addActionCheckbox(cbx_21_2_4, "tF_21_2_4", "kmw003.Scenario2Case4");
        addActionCheckbox(cbx_21_2_5, "tF_21_2_5", "kmw003.Scenario2Case5");
        addActionCheckbox(cbx_21_2_6, "tF_21_2_6", "kmw003.Scenario2Case6");
        addActionCheckbox(cbx_21_1_1, "tF_22_1_1", "kmw005.Scenario1Case1");
        addActionCheckbox(cbx_21_1_2, "tF_22_1_2", "kmw005.Scenario1Case2");
        addActionCheckbox(cbx_21_1_3, "tF_22_1_3", "kmw005.Scenario1Case3");
        addActionCheckbox(cbx_21_1_4, "tF_22_1_4", "kmw005.Scenario1Case4");
        addActionCheckbox(cbx_21_1_5, "tF_22_1_5", "kmw005.Scenario1Case5");
        addActionCheckbox(cbx_21_1_6, "tF_22_1_6", "kmw005.Scenario1Case6");
        addActionCheckbox(cbx_21_2_1, "tF_22_2_1", "kmw005.Scenario2Case1");
        addActionCheckbox(cbx_21_2_2, "tF_22_2_3", "kmw005.Scenario2Case3");
        addActionCheckbox(cbx_21_1_1, "tF_23_1_1", "kmw005.Scenario1Case1");
        addActionCheckbox(cbx_21_1_2, "tF_23_2_1", "kmw005.Scenario2Case1");
        addActionCheckbox(cbx_21_1_3, "tF_23_2_2", "kmw005.Scenario2Case2");
        addActionCheckbox(cbx_21_1_4, "tF_23_2_3", "kmw005.Scenario2Case3");
        addActionCheckbox(cbx_21_1_5, "tF_23_3_1", "kmw005.Scenario3Case1");
        addActionCheckbox(cbx_21_1_6, "tF_23_3_2", "kmw005.Scenario3Case2");
        addActionCheckbox(cbx_21_2_1, "tF_23_3_3", "kmw005.Scenario3Case3");
        addActionCheckbox(cbx_21_2_2, "tF_23_4_1", "kmw005.Scenario4Case1");
        addActionCheckbox(cbx_21_2_2, "tF_23_4_2", "kmw005.Scenario4Case2");
        addActionCheckbox(cbx_29_1_1, "tF_29_1_1", "cmm044.Scenario1Case1");
        addActionCheckbox(cbx_29_1_2, "tF_29_1_2", "cmm044.Scenario1Case2");
        addActionCheckbox(cbx_29_1_3, "tF_29_1_3", "cmm044.Scenario1Case3");
        addActionCheckbox(cbx_29_1_4, "tF_29_1_4", "cmm044.Scenario1Case4");
        addActionCheckbox(cbx_29_1_5, "tF_29_1_5", "cmm044.Scenario1Case5");
        addActionCheckbox(cbx_29_1_6, "tF_29_1_6", "cmm044.Scenario1Case6");
        addActionCheckbox(cbx_29_1_7, "tF_29_1_7", "cmm044.Scenario1Case7");
        addActionCheckbox(cbx_29_1_8, "tF_29_1_8", "cmm044.Scenario1Case8");
        addActionCheckbox(cbx_29_1_9, "tF_29_1_9", "cmm044.Scenario1Case9");
        addActionCheckbox(cbx_31_1_1, "tF_31_1_1", "cmm053.Scenario1Case1");
        addActionCheckbox(cbx_31_2_1, "tF_31_2_1", "cmm053.Scenario2Case1");
        addActionCheckbox(cbx_31_3_1, "tF_31_3_1", "cmm053.Scenario3Case1");
        addActionCheckbox(cbx_31_4_1, "tF_31_4_1", "cmm053.Scenario4Case1");
        addActionCheckbox(cbx_31_5_1, "tF_31_5_1", "cmm053.Scenario5Case1");
        addActionCheckbox(cbx_31_6_1, "tF_31_6_1", "cmm053.Scenario6Case1");
        addActionCheckbox(cbx_33_1_1, "tF_33_1_1", "kaf006.Scenario1Case1");
        addActionCheckbox(cbx_33_1_2, "tF_33_1_2", "kaf006.Scenario1Case2");
        addActionCheckbox(cbx_33_1_3, "tF_33_1_3", "kaf006.Scenario1Case3");
        addActionCheckbox(cbx_33_2_1, "tF_33_2_1", "kaf006.Scenario2Case1");
        addActionCheckbox(cbx_33_2_2, "tF_33_2_2", "kaf006.Scenario2Case2");
        addActionCheckbox(cbx_33_2_3, "tF_33_2_3", "kaf006.Scenario2Case3");
        addActionCheckbox(cbx_33_3_1, "tF_33_3_1", "kaf006.Scenario3Case1");
        addActionCheckbox(cbx_33_3_2, "tF_33_3_2", "kaf006.Scenario3Case2");
        addActionCheckbox(cbx_33_3_3, "tF_33_3_3", "kaf006.Scenario3Case3");
        addActionCheckbox(cbx_33_4_1, "tF_33_4_1", "kaf006.Scenario4Case1");
        addActionCheckbox(cbx_33_5_1, "tF_33_5_1", "kaf006.Scenario5Case1");
        addActionCheckbox(cbx_33_5_2, "tF_33_5_2", "kaf006.Scenario5Case2");
        addActionCheckbox(cbx_34_1_1, "tF_34_1_1", "kaf007.Scenario1Case1");
        addActionCheckbox(cbx_34_1_2, "tF_34_1_2", "kaf007.Scenario1Case2");
        addActionCheckbox(cbx_34_2_1, "tF_34_2_1", "kaf007.Scenario2Case1");
        addActionCheckbox(cbx_34_3_1, "tF_34_3_1", "kaf007.Scenario3Case1");
        addActionCheckbox(cbx_34_4_1, "tF_34_4_1", "kaf007.Scenario4Case1");
        
    } 

}