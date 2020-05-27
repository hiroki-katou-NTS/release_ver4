package common;

import static org.junit.platform.engine.discovery.DiscoverySelectors.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.runner.JUnitCore;

public class CreateMainTest {

    private static JFrame f = new JFrame(); // creating instance of JFrame
    public static TreeMap<String, String> testCases = new TreeMap<String, String>();
    JUnitCore junit = new JUnitCore();
    public static HashMap<String, Component> componentMap = new HashMap<String, Component>();
    JProgressBar progressBar = new JProgressBar();
    JCheckBox chckbxExcel = new JCheckBox("Excel出力");
    JButton button1 = new JButton("実行");
    JButton button2 = new JButton("キャンセル");
    ButtonExecute buttonExecute;
    JLayeredPane layeredPane = new JLayeredPane();
    JButton button = new JButton("リセット");

    public void create() {
        // create layout
        layeredPane.setBounds(0, 0, 1280, 450);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 1280, 450);

        tabbedPane.addTab("画面リスト1", (new Tab1()).scrollPane);
        tabbedPane.addTab("画面リスト2", (new Tab2()).scrollPane);
        tabbedPane.addTab("画面リスト3", (new Tab3()).scrollPane);
        tabbedPane.addTab("画面リスト4", (new Tab4()).scrollPane);

        // create a progressbar
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.BLUE);
        progressBar.setBounds(475, 270, 300, 46);
        progressBar.setVisible(false);

        layeredPane.add(tabbedPane, new Integer(1));
        layeredPane.add(progressBar, new Integer(2));

        // Run Button
        button1.setName("button1");
        button1.setHorizontalAlignment(SwingConstants.CENTER);
        button1.setBounds(104, 484, 67, 33); // x axis, y axis, width, height


        button.setName("button");
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBounds(183, 484, 88, 33);

        button2.setName("button2");
        button2.setHorizontalAlignment(SwingConstants.CENTER);
        button2.setBounds(283, 484, 100, 33);

        chckbxExcel.setName("excel_export");
        chckbxExcel.setBounds(8, 490, 88, 21);

        f.setSize(1300, 600);
        f.getContentPane().add(button1);
        f.getContentPane().add(button);
        f.getContentPane().add(button2);
        f.getContentPane().setLayout(null);
        f.getContentPane().add(layeredPane);
        f.getContentPane().add(chckbxExcel);
        f.setTitle("UK自動テスト");
        f.setVisible(true);

        createComponentMap();

        // create listener
        // execute
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonExecute = new ButtonExecute();
                buttonExecute.execute();
            }
        });

        // cancel
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonExecute.cancel(true);
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // reset result
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<String, Component> entry : CreateMainTest.componentMap.entrySet()) {
                    if (entry.getKey().indexOf("tF") != -1) {
                        ((JTextField)entry.getValue()).setText("");
                    }
                    if (entry.getKey().indexOf("cbx") != -1) {
                        if (((JCheckBox)entry.getValue()).isSelected()) {
                            ((JCheckBox)entry.getValue()).setSelected(false);
                        }
                    }
                }
            }
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void createComponentMap() {
        List<Component> listComponent = new ArrayList<Component>();
        Arrays.stream(((JTabbedPane)((JLayeredPane)f.getContentPane().getComponents()[3]).getComponents()[1]).getComponents()).forEach(i -> listComponent.addAll(Arrays.asList(((JPanel)(((JViewport)(((JScrollPane)i).getComponents()[0])).getComponents()[0])).getComponents())));
        Object[] components = listComponent.toArray();

        for (int i = 0; i < components.length; i++) {
            componentMap.put(((Component)components[i]).getName(), (Component)components[i]);
        }
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else
            return null;
    }

    public void writeExcel(Map<String, String> testCasesWithId) {
        List<String> testCaseList = new ArrayList<String>();
        List<String> testNameList = new ArrayList<String>();

        try {
            File jarFile = new File(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            (new File("Excel出力")).mkdir();

            for (Entry<String, String> entry : testCasesWithId.entrySet()) {
                testCaseList.add(entry.getValue());
                testNameList.add(entry.getValue().split("\\.")[0]);
            }
            testNameList.stream().distinct().collect(Collectors.toList());
            for (String testName : testNameList) {
                Workbook workbook = new XSSFWorkbook();
                for (String testCase : testCaseList) {
                    if (testCase.split("\\.")[0].equals(testName)) {
                        Sheet sheet = workbook.createSheet(testCase.split("\\.")[1]);
                        String screenshotPath = "images/" + testName + "/" + testCase.split("\\.")[1];
                        File folder = new File(screenshotPath);
                        int size = folder.list().length;
                        for (int i = 1; i <= size; i++) {
                            String fileName = "image" + i + ".png";
                            File image = new File(screenshotPath + "/" + fileName);
                            byte[] byteArray = FileUtils.readFileToByteArray(image);
                            int pictureIdx = workbook.addPicture(byteArray, Workbook.PICTURE_TYPE_PNG);
                            CreationHelper helper = workbook.getCreationHelper();

                            Drawing drawing = sheet.createDrawingPatriarch();

                            ClientAnchor anchor = helper.createClientAnchor();
                            anchor.setCol1(1);
                            anchor.setRow1(1 + 27 * (i - 1));

                            Picture pict = drawing.createPicture(anchor, pictureIdx);
                            pict.resize(20, 25);
                        }
                    }
                }

                String fileLocation = jarFile.getParent() + File.separator + "Excel出力" + File.separator + testName + "_自動テスト結果.xlsx";

                FileOutputStream outputStream = new FileOutputStream(fileLocation);
                workbook.write(outputStream);
                workbook.close();

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void addActionCheckbox(JCheckBox chkbox, String textField, String testCase) {
        chkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    CreateMainTest.testCases.put(textField, testCase);
                } else {
                    CreateMainTest.testCases.remove(textField);
                }
            }
        });
    }

    public void addActionCheckboxAll(JCheckBox chkbox) {
        chkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String name = chkbox.getName().substring(0, chkbox.getName().indexOf("_all"));
                for (Map.Entry<String, Component> entry : CreateMainTest.componentMap.entrySet()) {
                    if (entry.getKey().indexOf(name) != -1 && !entry.getKey().equals(chkbox.getName())) {
                        if (chkbox.isSelected()) {
                            ((JCheckBox)entry.getValue()).setSelected(true);
                        } else {
                            ((JCheckBox)entry.getValue()).setSelected(false);
                        }
                    }
                }
            }
        });
    }
    public class ButtonExecute extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            // set disable
            progressBar.setVisible(true);
            progressBar.setValue(0);
            button.setEnabled(false);
            button1.setEnabled(false);
            chckbxExcel.setEnabled(false);
            for (Map.Entry<String, Component> entry : CreateMainTest.componentMap.entrySet()) {
                if (entry.getKey().indexOf("cbx") != -1) {
                    ((JCheckBox)entry.getValue()).setEnabled(false);
                }
            }

            // call test class
            for (Map.Entry<String, String> testCase : testCases.entrySet()) {
                LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectClass(Class.forName(testCase.getValue()))).build();
                Launcher launcher = LauncherFactory.create();

                SummaryGeneratingListener listener = new SummaryGeneratingListener();
                launcher.registerTestExecutionListeners(listener);
                launcher.execute(request);
                TestExecutionSummary summary = listener.getSummary();
                JTextField tF = (JTextField) getComponentByName(testCase.getKey());
                if (summary.getFailures().size() == 0) {
                    tF.setText("PASS");
                } else {
                    tF.setText("FAIL");
                }
                progressBar.setValue(progressBar.getValue() + 100/testCases.size());
            }

            // export excel
            if (chckbxExcel.isSelected()) {
                writeExcel(testCases);
            }
            return null;

        }

        @Override
        protected void done() {
            for (Map.Entry<String, Component> entry : CreateMainTest.componentMap.entrySet()) {
                if (entry.getKey().indexOf("cbx") != -1) {
                    ((JCheckBox)entry.getValue()).setEnabled(true);
                }
            }
            button.setEnabled(true);
            button1.setEnabled(true);
            chckbxExcel.setEnabled(true);
            progressBar.setVisible(false);
        }

    }
}