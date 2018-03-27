/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.shr.com.program;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ProgramsManager
 * 
 */
public class ProgramsManager {
	
	/**
	 * CCG008A
	 */
	public static final Program CCG008A = new Program(WebAppId.COM, ProgramIdConsts.CCG008A, "CCG008_1",
			"/view/ccg/008/a/index.xhtml");
	/**
	 * CCG008B
	 */
	public static final Program CCG008B = new Program(WebAppId.COM, ProgramIdConsts.CCG008B, "CCG008_2",
			"/view/ccg/008/b/index.xhtml");
	/**
	 * CCG008C
	 */
	public static final Program CCG008C = new Program(WebAppId.COM, ProgramIdConsts.CCG008C, "CCG008_3",
			"/view/ccg/008/c/index.xhtml");
	/**
	 * CCG008D
	 */
	public static final Program CCG008D = new Program(WebAppId.COM, ProgramIdConsts.CCG008D, "",
			"/view/ccg/008/d/index.xhtml");
	/**
	 * CCG013A
	 */
	public static final Program CCG013A = new Program(WebAppId.COM, ProgramIdConsts.CCG013A, "CCG013_112",
			"/view/ccg/013/a/index.xhtml");
	/**
	 * CCG013B
	 */
	public static final Program CCG013B = new Program(WebAppId.COM, ProgramIdConsts.CCG013B, "CCG013_113",
			"/view/ccg/013/b/index.xhtml");
	/**
	 * CCG013C
	 */
	public static final Program CCG013C = new Program(WebAppId.COM, ProgramIdConsts.CCG013C, "CCG013_114",
			"/view/ccg/013/c/index.xhtml");
	/**
	 * CCG013D
	 */
	public static final Program CCG013D = new Program(WebAppId.COM, ProgramIdConsts.CCG013D, "CCG013_115",
			"/view/ccg/013/d/index.xhtml");
	/**
	 * CCG013E
	 */
	public static final Program CCG013E = new Program(WebAppId.COM, ProgramIdConsts.CCG013E, "CCG013_116",
			"/view/ccg/013/e/index.xhtml");
	/**
	 * CCG013F
	 */
	public static final Program CCG013F = new Program(WebAppId.COM, ProgramIdConsts.CCG013F, "CCG013_117",
			"/view/ccg/013/f/index.xhtml");
	/**
	 * CCG013G
	 */
	public static final Program CCG013G = new Program(WebAppId.COM, ProgramIdConsts.CCG013G, "CCG013_118",
			"/view/ccg/013/g/index.xhtml");
	/**
	 * CCG013I
	 */
	public static final Program CCG013I = new Program(WebAppId.COM, ProgramIdConsts.CCG013I, "CCG013_119",
			"/view/ccg/013/i/index.xhtml");
	/**
	 * CCG013J
	 */
	public static final Program CCG013J = new Program(WebAppId.COM, ProgramIdConsts.CCG013J, "CCG013_120",
			"/view/ccg/013/j/index.xhtml");
	/**
	 * CCG013K
	 */
	public static final Program CCG013K = new Program(WebAppId.COM, ProgramIdConsts.CCG013K, "CCG013_121",
			"/view/ccg/013/k/index.xhtml");
	/**
	 * CCG015A
	 */
	public static final Program CCG015A = new Program(WebAppId.COM, ProgramIdConsts.CCG015A, "CCG015_1",
			"/view/ccg/015/a/index.xhtml");

	/**
	 * CCG015B
	 */
	public static final Program CCG015B = new Program(WebAppId.COM, ProgramIdConsts.CCG015B, "CCG015_2",
			"/view/ccg/015/b/index.xhtml");
	/**
	 * CCG015C
	 */
	public static final Program CCG015C = new Program(WebAppId.COM, ProgramIdConsts.CCG015C, "CCG015_3",
			"/view/ccg/015/c/index.xhtml");
	/**
	 * CCG014A
	 */
	public static final Program CCG014A = new Program(WebAppId.COM, ProgramIdConsts.CCG014A, "CCG014_1",
			"/view/ccg/014/a/index.xhtml");

	/**
	 * CCG014B
	 */
	public static final Program CCG014B = new Program(WebAppId.COM, ProgramIdConsts.CCG014B, "CCG014_16",
			"/view/ccg/014/b/index.xhtml");
	/**
	 * CCG018A
	 */
	public static final Program CCG018A = new Program(WebAppId.COM, ProgramIdConsts.CCG018A, "CCG018_39",
			"/view/ccg/018/a/index.xhtml");
	/**
	 * CCG018B
	 */
	public static final Program CCG018B = new Program(WebAppId.COM, ProgramIdConsts.CCG018B, "CCG018_40",
			"/view/ccg/018/b/index.xhtml");
	/**
	 * CCG018C
	 */
	public static final Program CCG018C = new Program(WebAppId.COM, ProgramIdConsts.CCG018C, "CCG018_41",
			"/view/ccg/018/c/index.xhtml");
	/**
	 * CCG027A
	 */
	public static final Program CCG027A = new Program(WebAppId.COM, ProgramIdConsts.CCG027A, "CCG027_1",
			"/view/ccg/027/a/index.xhtml");
	
	/**
	 * CCG030A
	 */
	public static final Program CCG030A = new Program(WebAppId.COM, ProgramIdConsts.CCG030A, "CCG030_1",
			"/view/ccg/030/a/index.xhtml");

	/**
	 * CCG030B
	 */
	public static final Program CCG030B = new Program(WebAppId.COM, ProgramIdConsts.CCG030B, "CCG030_4",
			"/view/ccg/030/b/index.xhtml");

	/**
	 * CCG031A
	 */
	public static final Program CCG031A = new Program(WebAppId.COM, ProgramIdConsts.CCG031A, "CCG031_1",
			"/view/ccg/031/a/index.xhtml");
	/**
	 * CCG031B
	 */
	public static final Program CCG031B = new Program(WebAppId.COM, ProgramIdConsts.CCG031B, "CCG031_2",
			"/view/ccg/031/b/index.xhtml");
	/**
	 * CCG031C
	 */
	public static final Program CCG031C = new Program(WebAppId.COM, ProgramIdConsts.CCG031C, "CCG031_3",
			"/view/ccg/031/c/index.xhtml");
	/**
	 * CDL024
	 */
	public static final Program CDL024 = new Program(WebAppId.COM, ProgramIdConsts.CDL024, "CDL024_8",
			"/view/cdl/024/index.xhtml");
	/**
	 * CDL022A
	 */
	public static final Program CDL022A = new Program(WebAppId.COM, ProgramIdConsts.CDL022A, "CDL022_1",
			"/view/cdl/022/a/index.xhtml");
	/**
	 * CPS007A
	 */
	public static final Program CPS007A = new Program(WebAppId.COM, ProgramIdConsts.CPS007A, "CPS007_1",
			"/view/cps/007/a/index.xhtml");
	
	/**
	 * CPS007B
	 */
	public static final Program CPS007B = new Program(WebAppId.COM, ProgramIdConsts.CPS007B, "CPS007_2",
			"/view/cps/007/b/index.xhtml");
	
	/**
	 * CPS008A
	 */
	public static final Program CPS008A = new Program(WebAppId.COM, ProgramIdConsts.CPS008A, "CPS008_1",
			"/view/cps/008/a/index.xhtml");
	
	/**
	 * CPS008D
	 */
	public static final Program CPS008D = new Program(WebAppId.COM, ProgramIdConsts.CPS008D, "CPS008_41",
			"/view/cps/008/b/index.xhtml");
	/**
	 * CPS008C
	 */
	public static final Program CPS008C = new Program(WebAppId.COM, ProgramIdConsts.CPS008C, "CPS008_38",
			"/view/cps/008/c/index.xhtml");
	/**
	 * CMM044A
	 */
	public static final Program CMM044A = new Program(WebAppId.COM, ProgramIdConsts.CMM044A, "CMM044_1",
			"/view/cmm/044/a/index.xhtml");
	
	public static final Program CMM044D = new Program(WebAppId.COM, ProgramIdConsts.CMM044D, "CMM044_33",
			"/view/cmm/044/d/index.xhtml"); 
	
	/**
	 * CPS005A
	 */
	public static final Program CPS005A  = new Program(WebAppId.COM, ProgramIdConsts.CPS005A, "CPS005_36",
			"/view/cps/005/a/index.xhtml");
	
	/**
	 * CPS005B
	 */
	public static final Program CPS005B  = new Program(WebAppId.COM, ProgramIdConsts.CPS005B, "CPS005_37",
			"/view/cps/005/b/index.xhtml");
	
	/**
	 * CPS001A
	 */
	public static final Program CPS001A  = new Program(WebAppId.COM, ProgramIdConsts.CPS001A, "CPS001_1",
			"/view/cps/001/a/index.xhtml");
	
	/**
	 * CPS001B
	 */
	public static final Program CPS001B  = new Program(WebAppId.COM, ProgramIdConsts.CPS001B, "CPS001_2",
			"/view/cps/001/b/index.xhtml");
	
	/**
	 * CPS001C
	 */
	public static final Program CPS001C  = new Program(WebAppId.COM, ProgramIdConsts.CPS001C, "CPS001_3",
			"/view/cps/001/c/index.xhtml");
	
	
	/**
	 * CPS001D
	 */
	public static final Program CPS001D  = new Program(WebAppId.COM, ProgramIdConsts.CPS001D, "CPS001_94",
			"/view/cps/001/d/index.xhtml");

	/**
	 * CPS001E
	 */
	public static final Program CPS001E  = new Program(WebAppId.COM, ProgramIdConsts.CPS001E, "CPS001_95",
			"/view/cps/001/e/index.xhtml");
	
	/**
	 * CPS001F
	 */
	public static final Program CPS001F  = new Program(WebAppId.COM, ProgramIdConsts.CPS001F, "CPS001_96",
			"/view/cps/001/f/index.xhtml");
	
	/**
	 * CPS001H
	 */
	public static final Program CPS001H  = new Program(WebAppId.COM, ProgramIdConsts.CPS001H, "CPS001_112",
			"/view/cps/001/h/index.xhtml");
	
	
	/**
	 * KMK011A
	 */
	public static final Program KMK011A = new Program(WebAppId.AT, ProgramIdConsts.KMK011A, "KMK011_1",
			"/view/kmk/011/a/index.xhtml");
	/**
	 * KMK011B
	 */
	public static final Program KMK011B = new Program(WebAppId.AT, ProgramIdConsts.KMK011B, "KMK011_2",
			"/view/kmk/011/b/index.xhtml");
	/**
	 * KMK011C
	 */
	public static final Program KMK011C = new Program(WebAppId.AT, ProgramIdConsts.KMK011C, "KMK011_2",
			"/view/kmk/011/c/index.xhtml");
	/**
	 * KMK011H
	 */
	public static final Program KMK011H = new Program(WebAppId.AT, ProgramIdConsts.KMK011H, "KMK011_44",
			"/view/kmk/011/h/index.xhtml");
	/**
	 * KMK011I
	 */
	public static final Program KMK011I = new Program(WebAppId.AT, ProgramIdConsts.KMK011I, "KMK011_75",
			"/view/kmk/011/i/index.xhtml");
	/**
	 * KML001A
	 */
	public static final Program KML001A = new Program(WebAppId.AT, ProgramIdConsts.KML001A, "KML001_29",
			"/view/kml/001/a/index.xhtml");
	/**
	 * KML001B
	 */
	public static final Program KML001B = new Program(WebAppId.AT, ProgramIdConsts.KML001B, "KML001_3",
			"/view/kml/001/b/index.xhtml");
	/**
	 * KML001C
	 */
	public static final Program KML001C = new Program(WebAppId.AT, ProgramIdConsts.KML001C, "KML001_55",
			"/view/kml/001/c/index.xhtml");
	/**
	 * KML001D
	 */
	public static final Program KML001D = new Program(WebAppId.AT, ProgramIdConsts.KML001D, "KML001_56",
			"/view/kml/001/d/index.xhtml");
	/**
	 * KDL001A
	 */
	public static final Program KDL001A = new Program(WebAppId.AT, ProgramIdConsts.KDL001A, "KDL001_1",
			"/view/kdl/001/a/index.xhtml");
	/**
	 * KDL002A
	 */
	public static final Program KDL002A = new Program(WebAppId.AT, ProgramIdConsts.KDL002A, "KDL002_1",
			"/view/kdl/002/a/index.xhtml");
	/**
	 * KDL007A
	 */
	public static final Program KDL007A = new Program(WebAppId.AT, ProgramIdConsts.KDL007A, "KDL007_7",
			"/view/kdl/007/a/index.xhtml");
	/**
	 * KDL010A
	 */
	public static final Program KDL010A = new Program(WebAppId.AT, ProgramIdConsts.KDL010A, "KDL010_10",
			"/view/kdl/010/a/index.xhtml");
	/**
	 * KDL021A
	 */
	public static final Program KDL021A = new Program(WebAppId.AT, ProgramIdConsts.KDL021A, "KDL021_1",
			"/view/kdl/021/a/index.xhtml");
	/**
	 * KDL024A
	 */
	public static final Program KDL024A = new Program(WebAppId.AT, ProgramIdConsts.KDL024A, "KDL024_13",
			"/view/kdl/024/a/index.xhtml");

	/**
	 * KDL014A
	 */
	public static final Program KDL014A = new Program(WebAppId.AT, ProgramIdConsts.KDL014A, "KDL014_1",
			"/view/kdl/014/a/index.xhtml");

	/**
	 * KDL014B
	 */
	public static final Program KDL014B = new Program(WebAppId.AT, ProgramIdConsts.KDL014B, "KDL014_1",
			"/view/kdl/014/b/index.xhtml");
	////////////////////
	/**
	 * KDW001A
	 */
	public static final Program KDW001A = new Program(WebAppId.AT, ProgramIdConsts.KDW001A, "KDW001_105",
			"/view/kdw/001/a/index.xhtml");
	/**
	 * KDW001B
	 */
	public static final Program KDW001B = new Program(WebAppId.AT, ProgramIdConsts.KDW001B, "KDW001_105",
			"/view/kdw/001/b/index.xhtml");
	/**
	 * KDW001C
	 */
	public static final Program KDW001C = new Program(WebAppId.AT, ProgramIdConsts.KDW001C, "KDW001_105",
			"/view/kdw/001/c/index.xhtml");
	/**
	 * KDW001D
	 */
	public static final Program KDW001D = new Program(WebAppId.AT, ProgramIdConsts.KDW001D, "KDW001_105",
			"/view/kdw/001/d/index.xhtml");
	/**
	 * KDW001E
	 */
	public static final Program KDW001E = new Program(WebAppId.AT, ProgramIdConsts.KDW001E, "KDW001_105",
			"/view/kdw/001/e/index.xhtml");
	/**
	 * KDW001F
	 */
	public static final Program KDW001F = new Program(WebAppId.AT, ProgramIdConsts.KDW001F, "KDW001_105",
			"/view/kdw/001/f/index.xhtml");
	/**
	 * KDW001G
	 */
	public static final Program KDW001G = new Program(WebAppId.AT, ProgramIdConsts.KDW001G, "KDW001_106",
			"/view/kdw/001/g/index.xhtml");
	/**
	 * KDW001H
	 */
	public static final Program KDW001H = new Program(WebAppId.AT, ProgramIdConsts.KDW001H, "KDW001_107",
			"/view/kdw/001/h/index.xhtml");
	/**
	 * KDW001I
	 */
	public static final Program KDW001I = new Program(WebAppId.AT, ProgramIdConsts.KDW001I, "KDW001_108",
			"/view/kdw/001/i/index.xhtml");
	/**
	 * KDW001J
	 */
	public static final Program KDW001J = new Program(WebAppId.AT, ProgramIdConsts.KDW001J, "KDW001_105",
			"/view/kdw/001/j/index.xhtml");
	///////////
	public static final Program KDW006A = new Program(WebAppId.AT, ProgramIdConsts.KDW006A, "",
			"/view/kdw/006/a/index.xhtml");
	public static final Program KDW006B = new Program(WebAppId.AT, ProgramIdConsts.KDW006B, "KDW006_20",
			"/view/kdw/006/b/index.xhtml");
	public static final Program KDW006C = new Program(WebAppId.AT, ProgramIdConsts.KDW006C, "KDW006_21",
			"/view/kdw/006/c/index.xhtml");
	public static final Program KDW006D = new Program(WebAppId.AT, ProgramIdConsts.KDW006D, "KDW006_22",
			"/view/kdw/006/d/index.xhtml");
	public static final Program KDW006E = new Program(WebAppId.AT, ProgramIdConsts.KDW006E, "KDW006_23",
			"/view/kdw/006/e/index.xhtml");
	
	
	
	/**
	 * KDW009A
	 */
	public static final Program KDW009A = new Program(WebAppId.AT, ProgramIdConsts.KDW009A, "KDW009_1",
			"/view/kdw/009/a/index.xhtml");
	
	/**
	 * KSM002A
	 */
	public static final Program KSM002A = new Program(WebAppId.AT, ProgramIdConsts.KSM002A, "KSM002_1",
			"/view/ksm/002/a/index.xhtml");

	/**
	 * KSM002B
	 */
	public static final Program KSM002B = new Program(WebAppId.AT, ProgramIdConsts.KSM002B, "KSM002_1",
			"/view/ksm/002/b/index.xhtml");

	/**
	 * KSM002C
	 */
	public static final Program KSM002C = new Program(WebAppId.AT, ProgramIdConsts.KSM002C, "KSM002_7",
			"/view/ksm/002/c/index.xhtml");

	/**
	 * KSM002D
	 */
	public static final Program KSM002D = new Program(WebAppId.AT, ProgramIdConsts.KSM002D, "KSM002_2",
			"/view/ksm/002/d/index.xhtml");
	/**
	 * KSM002E
	 */
	public static final Program KSM002E = new Program(WebAppId.AT, ProgramIdConsts.KSM002E, "KSM002_3",
			"/view/ksm/002/e/index.xhtml");

	/**
	 * KSM004A
	 */
	public static final Program KSM004A = new Program(WebAppId.AT, ProgramIdConsts.KSM004A, "KSM004_55",
			"/view/ksm/004/a/index.xhtml");

	/**
	 * KSM004C
	 */
	public static final Program KSM004C = new Program(WebAppId.AT, ProgramIdConsts.KSM004C, "KSM004_56",
			"/view/ksm/004/c/index.xhtml");

	/**
	 * KSM004D
	 */
	public static final Program KSM004D = new Program(WebAppId.AT, ProgramIdConsts.KSM004D, "KSM004_57",
			"/view/ksm/004/d/index.xhtml");
	/**
	 * KSM004E
	 */
	public static final Program KSM004E = new Program(WebAppId.AT, ProgramIdConsts.KSM004E, "KSM004_58",
			"/view/ksm/004/e/index.xhtml");

	/**
	 * KSU001A
	 */
	public static final Program KSU001A = new Program(WebAppId.AT, ProgramIdConsts.KSU001A, "",
			"/view/ksu/001/a/index.xhtml");
	/**
	 * KSU001C
	 */
	public static final Program KSU001C = new Program(WebAppId.AT, ProgramIdConsts.KSU001C, "",
			"/view/ksu/001/c/index.xhtml");
	/**
	 * KSU001D
	 */
	public static final Program KSU001D = new Program(WebAppId.AT, ProgramIdConsts.KSU001D, "",
			"/view/ksu/001/d/index.xhtml");
	/**
	 * KSU001JA
	 */
	public static final Program KSU001JA = new Program(WebAppId.AT, ProgramIdConsts.KSU001JA, "KSU001_926",
			"/view/ksu/001/ja/index.xhtml");
	/**
	 * KSU001JB
	 */
	public static final Program KSU001JB = new Program(WebAppId.AT, ProgramIdConsts.KSU001JB, "KSU001_927",
			"/view/ksu/001/jb/index.xhtml");
	/**
	 * KSU001L
	 */
	public static final Program KSU001L = new Program(WebAppId.AT, ProgramIdConsts.KSU001L, "KSU001_1140",
			"/view/ksu/001/l/index.xhtml");
	/**
	 * KSU001LX
	 */
	public static final Program KSU001LX = new Program(WebAppId.AT, ProgramIdConsts.KSU001LX, "KSU001_1141",
			"/view/ksu/001/lx/index.xhtml");
	/**
	 * KSU001N
	 */
	public static final Program KSU001N = new Program(WebAppId.AT, ProgramIdConsts.KSU001N, "KSU001_1327",
			"/view/ksu/001/n/index.xhtml");
	/**
	 * KSU001R
	 */
	public static final Program KSU001R = new Program(WebAppId.AT, ProgramIdConsts.KSU001R, "",
			"/view/ksu/001/r/index.xhtml");
	/**
	 * KSU001O
	 */
	public static final Program KSU001O = new Program(WebAppId.AT, ProgramIdConsts.KSU001O, "",
			"/view/ksu/001/o/index.xhtml");
	/**
	 * KSU001Q
	 */
	public static final Program KSU001Q = new Program(WebAppId.AT, ProgramIdConsts.KSU001Q, "",
			"/view/ksu/001/q/index.xhtml");
	/**
	 * KML002A
	 */
	public static final Program KML002A = new Program(WebAppId.AT, ProgramIdConsts.KML002A, "",
			"/view/kml/002/a/index.xhtml");
	/**
	 * KML002B
	 */
	public static final Program KML002B = new Program(WebAppId.AT, ProgramIdConsts.KML002B, "KML002_26",
			"/view/kml/002/b/index.xhtml");
	/**
	 * KML002C
	 */
	public static final Program KML002C = new Program(WebAppId.AT, ProgramIdConsts.KML002C, "KML002_25",
			"/view/kml/002/c/index.xhtml");
	/**
	 * KML002D
	 */
	public static final Program KML002D = new Program(WebAppId.AT, ProgramIdConsts.KML002D, "KML002_25",
			"/view/kml/002/d/index.xhtml");
	/**
	 * KML002E
	 */
	public static final Program KML002E = new Program(WebAppId.AT, ProgramIdConsts.KML002E, "KML002_25",
			"/view/kml/002/e/index.xhtml");
	/**
	 * KML002F
	 */
	public static final Program KML002F = new Program(WebAppId.AT, ProgramIdConsts.KML002F, "KML002_25",
			"/view/kml/002/f/index.xhtml");
	/**
	 * KML002G
	 */
	public static final Program KML002G = new Program(WebAppId.AT, ProgramIdConsts.KML002G, "KML002_25",
			"/view/kml/002/g/index.xhtml");
	/**
	 * KML002H
	 */
	public static final Program KML002H = new Program(WebAppId.AT, ProgramIdConsts.KML002H, "",
			"/view/kml/002/h/index.xhtml");
	/**
	 * KML002I
	 */
	public static final Program KML002I = new Program(WebAppId.AT, ProgramIdConsts.KML002I, "KML002_112",
			"/view/kml/002/i/index.xhtml");
	/**
	 * KML002K
	 */
	public static final Program KML002K = new Program(WebAppId.AT, ProgramIdConsts.KML002K, "KML002_113",
			"/view/kml/002/k/index.xhtml");
	/**
	 * KML002L
	 */
	public static final Program KML002L = new Program(WebAppId.AT, ProgramIdConsts.KML002L, "KML002_114",
			"/view/kml/002/l/index.xhtml");
	/**
	 * KML002M
	 */
	public static final Program KML002M = new Program(WebAppId.AT, ProgramIdConsts.KML002M, "KML002_115",
			"/view/kml/002/m/index.xhtml");
	/**
	 * KML004A
	 */
	public static final Program KML004A = new Program(WebAppId.AT, ProgramIdConsts.KML004A, "",
			"/view/kml/004/a/index.xhtml");
	/**
	 * KML004B
	 */
	public static final Program KML004B = new Program(WebAppId.AT, ProgramIdConsts.KML004B, "KML004_52",
			"/view/kml/004/b/index.xhtml");
	/**
	 * KML004D
	 */
	public static final Program KML004D = new Program(WebAppId.AT, ProgramIdConsts.KML004D, "KML004_53",
			"/view/kml/004/d/index.xhtml");

	/** The Constant KDL003. */
	public static final Program KDL003A = new Program(WebAppId.AT, ProgramIdConsts.KDL003A, "KDL003_1",
			"/view/kdl/003/a/index.xhtml");
	
	/** The Constant KSM006. */
	public static final Program KSM006A = new Program(WebAppId.AT, ProgramIdConsts.KSM006A, "KSM006_1",
			"/view/ksm/006/a/index.xhtml");
	
	/** The Constant KSM003. */
	public static final Program KSM003A = new Program(WebAppId.AT, ProgramIdConsts.KSM003A, "KSM003_1",
			"/view/ksm/003/a/index.xhtml");
	
	/** The Constant KSM005A. */
	public static final Program KSM005A = new Program(WebAppId.AT, ProgramIdConsts.KSM005A, "KSM005_37",
			"/view/ksm/005/a/index.xhtml");
	
	/** The Constant KSM005B. */
	public static final Program KSM005B = new Program(WebAppId.AT, ProgramIdConsts.KSM005B, "KSM005_38",
			"/view/ksm/005/b/index.xhtml");
	
	/** The Constant KSM005C. */
	public static final Program KSM005C = new Program(WebAppId.AT, ProgramIdConsts.KSM005C, "KSM005_39",
			"/view/ksm/005/c/index.xhtml");
	
	/** The Constant KSM005E. */
	public static final Program KSM005E = new Program(WebAppId.AT, ProgramIdConsts.KSM005E, "KSM005_41",
			"/view/ksm/005/e/index.xhtml");
	
	/** The Constant KSM005F. */
	public static final Program KSM005F = new Program(WebAppId.AT, ProgramIdConsts.KSM005F, "KSM005_42",
			"/view/ksm/005/f/index.xhtml");
	
	/** The Constant KDL023. */
	public static final Program KDL023A = new Program(WebAppId.AT, ProgramIdConsts.KDL023A, "KDL023_1",
			"/view/kdl/023/a/index.xhtml");
	
	public static final Program KDL023B = new Program(WebAppId.AT, ProgramIdConsts.KDL023B, "KDL023_2",
			"/view/kdl/023/b/index.xhtml");
	
	/** The Constant KSM001. */
	public static final Program KSM001A = new Program(WebAppId.AT, ProgramIdConsts.KSM001A, "KSM001_62",
			"/view/ksm/001/a/index.xhtml");
	
	/** The Constant KMK009. */
	public static final Program KMK009A = new Program(WebAppId.AT, ProgramIdConsts.KMK009A, null ,
			"/view/kmk/009/a/index.xhtml");
	
	/** The Constant KSU006. */
	public static final Program KSU006A = new Program(WebAppId.AT, ProgramIdConsts.KSU006A, "KSU006_321",
			"/view/ksu/006/a/index.xhtml");
	
	public static final Program KSU006B = new Program(WebAppId.AT, ProgramIdConsts.KSU006B, "KSU006_322",
			"/view/ksu/006/b/index.xhtml");
	
	public static final Program KSU006C = new Program(WebAppId.AT, ProgramIdConsts.KSU006C, "KSU006_323",
			"/view/ksu/006/c/index.xhtml");
	
	/** The Constant CCG007. */
	public static final Program CCG007A = new Program(WebAppId.COM, ProgramIdConsts.CCG007A,
			"CCG007_9", "/view/ccg/007/a/index.xhtml");
	
	/** The Constant CCG007B. */
	public static final Program CCG007B = new Program(WebAppId.COM, ProgramIdConsts.CCG007B,
			"CCG007_2", "/view/ccg/007/b/index.xhtml");
	
	/** The Constant CCG007C. */
	public static final Program CCG007C = new Program(WebAppId.COM, ProgramIdConsts.CCG007C,
			"CCG007_3", "/view/ccg/007/c/index.xhtml");
	
	/** The Constant CCG007D. */
	public static final Program CCG007D = new Program(WebAppId.COM, ProgramIdConsts.CCG007D,
			"CCG007_4", "/view/ccg/007/d/index.xhtml");
	
	/** The Constant KMK004A. */
	public static final Program KMK004A  = new Program(WebAppId.AT, ProgramIdConsts.KMK004A, "KMK004_1",
			"/view/kmk/004/a/index.xhtml");
	
	/** The Constant KMK004E. */
	public static final Program KMK004E  = new Program(WebAppId.AT, ProgramIdConsts.KMK004E, "KMK004_2",
			"/view/kmk/004/e/index.xhtml");
	
	/** The Constant KMK012A. */
	public static final Program KMK012A  = new Program(WebAppId.AT, ProgramIdConsts.KMK012A, "KMK012_11",
			"/view/kmk/012/a/index.xhtml");
	
	/** The Constant KMK012D. */
	public static final Program KMK012D  = new Program(WebAppId.AT, ProgramIdConsts.KMK012D, "KMK012_34",
			"/view/kmk/012/d/index.xhtml");
	
	/** The Constant KMK012E. */
	public static final Program KMK012E  = new Program(WebAppId.AT, ProgramIdConsts.KMK012E, "KMK012_43",
			"/view/kmk/012/e/index.xhtml");
	/** The Constant KMF001A. */
	public static final Program KMF001A  = new Program(WebAppId.AT, ProgramIdConsts.KMF001A, "KMF001_1",
			"/view/kmf/001/a/index.xhtml");
	
	/** The Constant KMF001B. */
	public static final Program KMF001B  = new Program(WebAppId.AT, ProgramIdConsts.KMF001B, "KMF001_2",
			"/view/kmf/001/b/index.xhtml");
	
	/** The Constant KMF001C. */
	public static final Program KMF001C  = new Program(WebAppId.AT, ProgramIdConsts.KMF001C, "KMF001_3",
			"/view/kmf/001/c/index.xhtml");
	
	/** The Constant KMF001D. */
	public static final Program KMF001D  = new Program(WebAppId.AT, ProgramIdConsts.KMF001D, "KMF001_4",
			"/view/kmf/001/d/index.xhtml");
	
	/** The Constant KMF001F. */
	public static final Program KMF001F  = new Program(WebAppId.AT, ProgramIdConsts.KMF001F, "KMF001_6",
			"/view/kmf/001/f/index.xhtml");
	
	/** The Constant KMF001H. */
	public static final Program KMF001H  = new Program(WebAppId.AT, ProgramIdConsts.KMF001H, "KMF001_8",
			"/view/kmf/001/h/index.xhtml");
	
	/** The Constant KMF001J. */
	public static final Program KMF001J  = new Program(WebAppId.AT, ProgramIdConsts.KMF001J, "KMF001_10",
			"/view/kmf/001/j/index.xhtml");
	
	/** The Constant KMF001L. */
	public static final Program KMF001L  = new Program(WebAppId.AT, ProgramIdConsts.KMF001L, "KMF001_12",
			"/view/kmf/001/l/index.xhtml");
	
	/**
	 * KMF003A
	 */
	public static final Program KMF003A  = new Program(WebAppId.AT, ProgramIdConsts.KMF003A, "KMF003_1",
			"/view/kmf/003/a/index.xhtml");
		
	/**
	 * KMF003B
	 */
	public static final Program KMF003B  = new Program(WebAppId.AT, ProgramIdConsts.KMF003B, "KMF003_2",
			"/view/kmf/003/b/index.xhtml");
	
	/**
	 * KMF004A
	 */
	public static final Program KMF004A  = new Program(WebAppId.AT, ProgramIdConsts.KMF004A, "KMF004_112",
			"/view/kmf/004/a/index.xhtml");
	
	/**
	 * KMF004B
	 */
	public static final Program KMF004B  = new Program(WebAppId.AT, ProgramIdConsts.KMF004B, "KMF004_114",
			"/view/kmf/004/b/index.xhtml");
	
	/**
	 * KMF004C
	 */
	public static final Program KMF004C  = new Program(WebAppId.AT, ProgramIdConsts.KMF004C, "KMF004_115",
			"/view/kmf/004/c/index.xhtml");
	
	/**
	 * KMF004D
	 */
	public static final Program KMF004D  = new Program(WebAppId.AT, ProgramIdConsts.KMF004D, "KMF004_116",
			"/view/kmf/004/d/index.xhtml");
	
	/**
	 * KMF004E
	 */
	public static final Program KMF004E  = new Program(WebAppId.AT, ProgramIdConsts.KMF004E, "KMF004_117",
			"/view/kmf/004/e/index.xhtml");
	
	/**
	 * KMF004F
	 */
	public static final Program KMF004F  = new Program(WebAppId.AT, ProgramIdConsts.KMF004F, "KMF004_118",
			"/view/kmf/004/f/index.xhtml");
	
	/**
	 * KMF004G
	 */
	public static final Program KMF004G  = new Program(WebAppId.AT, ProgramIdConsts.KMF004G, "KMF004_130",
			"/view/kmf/004/g/index.xhtml");
	
	/**
	 * KMF004H
	 */
	public static final Program KMF004H  = new Program(WebAppId.AT, ProgramIdConsts.KMF004H, "KMF004_131",
			"/view/kmf/004/h/index.xhtml");
	
	/**
	 * KMK007A
	 */
	public static final Program KMK007A  = new Program(WebAppId.AT, ProgramIdConsts.KMK007A, "KMK007_63",
			"/view/kmk/007/a/index.xhtml");
	
	/**
	 * KMK007B
	 */
	public static final Program KMK007B  = new Program(WebAppId.AT, ProgramIdConsts.KMK007B, "KMK007_64",
			"/view/kmk/007/b/index.xhtml");
	
	/**
	 * KMK007C
	 */
	public static final Program KMK007C  = new Program(WebAppId.AT, ProgramIdConsts.KMK007C, "KMK007_65",
			"/view/kmk/007/c/index.xhtml");
	
	/**
	 * CAS001A
	 */
	public static final Program CAS001A  = new Program(WebAppId.COM, ProgramIdConsts.CAS001A, "CAS001_1",
			"/view/cas/001/a/index.xhtml");

	/**
	 * CAS001C
	 */
	public static final Program CAS001C  = new Program(WebAppId.COM, ProgramIdConsts.CAS001C, "CAS001_67",
			"/view/cas/001/c/index.xhtml");
	
	/**
	 * CAS001D
	 */
	public static final Program CAS001D  = new Program(WebAppId.COM, ProgramIdConsts.CAS001D, "CAS001_66",
			"/view/cas/001/d/index.xhtml");
	
	/**
	 * KMK005A
	 */
	public static final Program KMK005A  = new Program(WebAppId.AT, ProgramIdConsts.KMK005A, "KMK005_91",
			"/view/kmk/005/a/index.xhtml");
	
	/**
	 * KMK005B
	 */
	public static final Program KMK005B  = new Program(WebAppId.AT, ProgramIdConsts.KMK005B, "KMK005_5",
			"/view/kmk/005/b/index.xhtml");
	
	/**
	 * KMK005D
	 */
	public static final Program KMK005D  = new Program(WebAppId.AT, ProgramIdConsts.KMK005D, "KMK005_7",
			"/view/kmk/005/d/index.xhtml");
	
	/**
	 * KMK005E
	 */
	public static final Program KMK005E  = new Program(WebAppId.AT, ProgramIdConsts.KMK005E, "KMK005_11",
			"/view/kmk/005/e/index.xhtml");
	
	/**
	 * KMK005B
	 */
	public static final Program KMK005F  = new Program(WebAppId.AT, ProgramIdConsts.KMK005F, "KMK005_13",
			"/view/kmk/005/f/index.xhtml");
	
	/**
	 * KMK005G
	 */
	public static final Program KMK005G  = new Program(WebAppId.AT, ProgramIdConsts.KMK005G, "KMK005_92",
			"/view/kmk/005/g/index.xhtml");
	
	/**
	 * KMK005H
	 */
	public static final Program KMK005H  = new Program(WebAppId.AT, ProgramIdConsts.KMK005H, "KMK005_92",
			"/view/kmk/005/h/index.xhtml");
	
	/**
	 * KMK005H
	 */
	public static final Program KMK005I  = new Program(WebAppId.AT, ProgramIdConsts.KMK005I, "KMK005_92",
			"/view/kmk/005/i/index.xhtml");
	
	/**
	 * KMK005K
	 */
	public static final Program KMK005K  = new Program(WebAppId.AT, ProgramIdConsts.KMK005K, "KMK005_92",
			"/view/kmk/005/k/index.xhtml");
	
	/**
	 * KMK008A
	 */
	public static final Program KMK008A  = new Program(WebAppId.AT, ProgramIdConsts.KMK008A, "",
			"/view/kmk/008/a/index.xhtml");
	
	/**
	 * KMK008B
	 */
	public static final Program KMK008B  = new Program(WebAppId.AT, ProgramIdConsts.KMK008B, "",
			"/view/kmk/008/b/index.xhtml");
	
	/**
	 * KMK008C
	 */
	public static final Program KMK008C  = new Program(WebAppId.AT, ProgramIdConsts.KMK008C, "",
			"/view/kmk/008/c/index.xhtml");
	
	/**
	 * KMK008D
	 */
	public static final Program KMK008D  = new Program(WebAppId.AT, ProgramIdConsts.KMK008D, "",
			"/view/kmk/008/d/index.xhtml");
	
	/**
	 * KMK008E
	 */
	public static final Program KMK008E  = new Program(WebAppId.AT, ProgramIdConsts.KMK008E, "",
			"/view/kmk/008/e/index.xhtml");
	
	/**
	 * KMK008F
	 */
	public static final Program KMK008F  = new Program(WebAppId.AT, ProgramIdConsts.KMK008F, "",
			"/view/kmk/008/f/index.xhtml");
	
	/**
	 * KMK008G
	 */
	public static final Program KMK008G  = new Program(WebAppId.AT, ProgramIdConsts.KMK008G, "",
			"/view/kmk/008/g/index.xhtml");
	
	/**
	 * KMK008I
	 */
	public static final Program KMK008I  = new Program(WebAppId.AT, ProgramIdConsts.KMK008I, "KMK008_48",
			"/view/kmk/008/i/index.xhtml");
	
	/**
	 * KMK008J
	 */
	public static final Program KMK008J  = new Program(WebAppId.AT, ProgramIdConsts.KMK008J, "KMK008_49",
			"/view/kmk/008/j/index.xhtml");
	
	/**
	 * KMK008K
	 */
	public static final Program KMK008K  = new Program(WebAppId.AT, ProgramIdConsts.KMK008K, "KMK008_50",
			"/view/kmk/008/k/index.xhtml");
	
	/**
	 * KCP006
	 */
	public static final Program KCP006A  = new Program(WebAppId.AT, ProgramIdConsts.KCP006A, "CCGXX7_1",
			"/view/kcp/006/a/index.xhtml");
	
	/**
	 * KCP006
	 */
	public static final Program KCP006B  = new Program(WebAppId.AT, ProgramIdConsts.KCP006B, "CCGXX7_2",
			"/view/kcp/006/b/index.xhtml");
	
	/**
	 * KDW002A
	 */
	public static final Program KDW002A  = new Program(WebAppId.AT, ProgramIdConsts.KDW002A, "KDW002_1",
			"/view/kdw/002/a/index.xhtml");
	
	/**
	 * KDW002B
	 */
	public static final Program KDW002B  = new Program(WebAppId.AT, ProgramIdConsts.KDW002B, "KDW002_1",
			"/view/kdw/002/b/index.xhtml");
	
	/**
	 * KDW002C
	 */
	public static final Program KDW002C  = new Program(WebAppId.AT, ProgramIdConsts.KDW002C, "KDW002_1",
			"/view/kdw/002/c/index.xhtml");
	
	/** The Constant CCG001. */
	public static final Program CCG001  = new Program(WebAppId.COM, ProgramIdConsts.CCG001, null,
			"/view/ccg/001/index.xhtml");
	
	/** The Constant KCP001. */
	public static final Program KCP001  = new Program(WebAppId.COM, ProgramIdConsts.KCP001, null,
			"/view/kcp/001/index.xhtml");
	
	public static final Program KCP002  = new Program(WebAppId.COM, ProgramIdConsts.KCP002, null,
			"/view/kcp/002/index.xhtml");
	
	public static final Program KCP003  = new Program(WebAppId.COM, ProgramIdConsts.KCP003, null,
			"/view/kcp/003/index.xhtml");
	
	public static final Program KCP004  = new Program(WebAppId.COM, ProgramIdConsts.KCP004, null,
			"/view/kcp/004/index.xhtml");
	
	public static final Program KCP005  = new Program(WebAppId.COM, ProgramIdConsts.KCP005, null,
			"/view/kcp/005/index.xhtml");
	
	/**
	 * KDW007A
	 */
	public static final Program KDW007A  = new Program(WebAppId.AT, ProgramIdConsts.KDW007A, "",
			"/view/kdw/007/a/index.xhtml");
	
	/**
	 * KDW007B
	 */
	public static final Program KDW007B  = new Program(WebAppId.AT, ProgramIdConsts.KDW007B, "KDW007_40",
			"/view/kdw/007/b/index.xhtml");
	
	/**
	 * KDW007C
	 */
	public static final Program KDW007C  = new Program(WebAppId.AT, ProgramIdConsts.KDW007C, "KDW007_92",
			"/view/kdw/007/c/index.xhtml");

	/**
	 * KDW008A
	 */
	public static final Program KDW008A  = new Program(WebAppId.AT, ProgramIdConsts.KDW008A, "KDW008_1",
			"/view/kdw/008/a/index.xhtml");
	
	/**
	 * KDW008B
	 */
	public static final Program KDW008B = new Program(WebAppId.AT, ProgramIdConsts.KDW008B, "KDW008_1",
			"/view/kdw/008/b/index.xhtml");
	
	/**
	 * KDW008C
	 */
	public static final Program KDW008C = new Program(WebAppId.AT, ProgramIdConsts.KDW008C, "KDW008_33",
			"/view/kdw/008/c/index.xhtml");
	
	/**
	 * CPS006A
	 */
	public static final Program CPS006A  = new Program(WebAppId.COM, ProgramIdConsts.CPS006A, "CPS006_56",
			"/view/cps/006/a/index.xhtml");

	/**
	 * CPS006B
	 */
	public static final Program CPS006B  = new Program(WebAppId.COM, ProgramIdConsts.CPS006B, "CPS006_57",
			"/view/cps/006/b/index.xhtml");
	
	
	/**
	 * CPS002A
	 */
	public static final Program CPS002A  = new Program(WebAppId.COM, ProgramIdConsts.CPS002A, "CPS002_1",
			"/view/cps/002/a/index.xhtml");
	
	/**
	 * CPS002E
	 */
	public static final Program CPS002E  = new Program(WebAppId.COM, ProgramIdConsts.CPS002E, "CPS002_5",
			"/view/cps/002/e/index.xhtml");
	
	/**
	 * CPS002F
	 */
	public static final Program CPS002F  = new Program(WebAppId.COM, ProgramIdConsts.CPS002F, "CPS002_6",
			"/view/cps/002/f/index.xhtml");
	
	/**
	 * CPS002G
	 */
	public static final Program CPS002G  = new Program(WebAppId.COM, ProgramIdConsts.CPS002G, "CPS002_7",
			"/view/cps/002/g/index.xhtml");
	
	/**
	 * CPS002H
	 */
	public static final Program CPS002H  = new Program(WebAppId.COM, ProgramIdConsts.CPS002H, "CPS002_8",
			"/view/cps/002/h/index.xhtml");
	
	/**
	 * CPS002I
	 */
	public static final Program CPS002I  = new Program(WebAppId.COM, ProgramIdConsts.CPS002I, "CPS002_9",
			"/view/cps/002/i/index.xhtml");
	
	
	public static final Program CMM008A = new Program(WebAppId.COM, ProgramIdConsts.CMM008A, null,
			"/view/cmm/008/a/index.xhtml");
	
	/** The Constant CMM014. */	
	public static final Program CMM014A = new Program(WebAppId.COM, ProgramIdConsts.CMM014A, "CMM014_9",
			"/view/cmm/014/a/index.xhtml");
	
	public static final Program CDL002 = new Program(WebAppId.COM, ProgramIdConsts.CDL002, "CDL002_4",
			"/view/cdl/002/a/index.xhtml");
	
	public static final Program CDL003 = new Program(WebAppId.COM, ProgramIdConsts.CDL003, "CDL003_4",
			"/view/cdl/003/a/index.xhtml");
	
	public static final Program CMM011A = new Program(WebAppId.COM, ProgramIdConsts.CMM011A, "CMM011_1",
			"/view/cmm/011/a/index.xhtml");
	
	public static final Program CMM011B = new Program(WebAppId.COM, ProgramIdConsts.CMM011B, "CMM011_2",
			"/view/cmm/011/b/index.xhtml");
	
	public static final Program CMM011D = new Program(WebAppId.COM, ProgramIdConsts.CMM011D, "CMM011_3",
			"/view/cmm/011/d/index.xhtml");
	
	public static final Program CMM011E = new Program(WebAppId.COM, ProgramIdConsts.CMM011E, "CMM011_4",
			"/view/cmm/011/e/index.xhtml");
	
	public static final Program CMM011F = new Program(WebAppId.COM, ProgramIdConsts.CMM011F, "CMM011_5",
			"/view/cmm/011/f/index.xhtml");
	
	public static final Program CDL008 = new Program(WebAppId.COM, ProgramIdConsts.CDL008, "CDL008_1",
			"/view/cdl/008/a/index.xhtml");
	
	public static final Program CDL009 = new Program(WebAppId.COM, ProgramIdConsts.CDL009, "CDL009_1",
			"/view/cdl/009/a/index.xhtml");
	
	public static final Program KMK002A = new Program(WebAppId.AT, ProgramIdConsts.KMK002A, "KMK002_73",
			"/view/kmk/002/a/index.xhtml");
	
	public static final Program KMK002B = new Program(WebAppId.AT, ProgramIdConsts.KMK002B, "KMK002_74",
			"/view/kmk/002/b/index.xhtml");
	
	public static final Program KMK002C = new Program(WebAppId.AT, ProgramIdConsts.KMK002C, "KMK002_43",
			"/view/kmk/002/c/index.xhtml");
	
	public static final Program KMK002D = new Program(WebAppId.AT, ProgramIdConsts.KMK002D, "KMK002_42",
			"/view/kmk/002/d/index.xhtml");
	
	public static final Program KMK006A = new Program(WebAppId.AT, ProgramIdConsts.KMK006A, "KMK006_1",
			"/view/kmk/006/a/index.xhtml");
	
	public static final Program KMK006B = new Program(WebAppId.AT, ProgramIdConsts.KMK006B, "KMK006_2",
			"/view/kmk/006/b/index.xhtml");
	
	public static final Program KMK006C = new Program(WebAppId.AT, ProgramIdConsts.KMK006C, "KMK006_3",
			"/view/kmk/006/c/index.xhtml");
	
	public static final Program KMK006D = new Program(WebAppId.AT, ProgramIdConsts.KMK006D, "KMK006_4",
			"/view/kmk/006/d/index.xhtml");
	
	public static final Program KMK006E = new Program(WebAppId.AT, ProgramIdConsts.KMK006E, "KMK006_5",
			"/view/kmk/006/e/index.xhtml");
	
	public static final Program KMK010A = new Program(WebAppId.AT, ProgramIdConsts.KMK010A, "KMK010_1",
			"/view/kmk/010/a/index.xhtml");
	
	public static final Program KMK010B = new Program(WebAppId.AT, ProgramIdConsts.KMK010B, "KMK010_2",
			"/view/kmk/010/b/index.xhtml");
	
	public static final Program KMK010C = new Program(WebAppId.AT, ProgramIdConsts.KMK010C, "KMK010_3",
			"/view/kmk/010/c/index.xhtml");

	public static final Program KDL006 = new Program(WebAppId.AT, ProgramIdConsts.KDL006, "KDL006_1",
			"/view/kdl/006/a/index.xhtml");
	
	public static final Program KMW005A = new Program(WebAppId.AT, ProgramIdConsts.KMW005A, "KMW005_27",
			"/view/kmw/005/a/index.xhtml");
	
	public static final Program KMW005B = new Program(WebAppId.AT, ProgramIdConsts.KMW005B, "KMW005_28",
			"/view/kmw/005/b/index.xhtml");

	public static final Program CMM050A = new Program(WebAppId.COM, ProgramIdConsts.CMM050A, "CMM050_1",
			"/view/cmm/050/a/index.xhtml");
	
	public static final Program CMM050B = new Program(WebAppId.COM, ProgramIdConsts.CMM050B, "CMM050_2",
			"/view/cmm/050/b/index.xhtml");
	
	public static final Program CMM013A = new Program(WebAppId.COM, ProgramIdConsts.CMM013A, "CMM013_53",
			"/view/cmm/013/a/index.xhtml");
	
	public static final Program CMM013B = new Program(WebAppId.COM, ProgramIdConsts.CMM013B, "CMM013_54",
			"/view/cmm/013/b/index.xhtml");
	
	public static final Program CMM013C = new Program(WebAppId.COM, ProgramIdConsts.CMM013C, "CMM013_55",
			"/view/cmm/013/c/index.xhtml");
	
	public static final Program CMM013D = new Program(WebAppId.COM, ProgramIdConsts.CMM013D, "CMM013_56",
			"/view/cmm/013/d/index.xhtml");
	
	public static final Program CMM013E = new Program(WebAppId.COM, ProgramIdConsts.CMM013E, "CMM013_57",
			"/view/cmm/013/e/index.xhtml");
	
	public static final Program CMM013F = new Program(WebAppId.COM, ProgramIdConsts.CMM013F, "CMM013_58",
			"/view/cmm/013/f/index.xhtml");
										
	public static final Program CDL004 = new Program(WebAppId.COM, ProgramIdConsts.CDL004, "CDL004_1",
			"/view/cdl/004/a/index.xhtml");
	
	public static final Program KDW003A = new Program(WebAppId.AT, ProgramIdConsts.KDW003A, "KDW003_55",
			"/view/kdw/003/a/index.xhtml");
	
	public static final Program KDW003B = new Program(WebAppId.AT, ProgramIdConsts.KDW003B, "KDW003_54",
			"/view/kdw/003/b/index.xhtml");
	
	public static final Program KDW003C = new Program(WebAppId.AT, ProgramIdConsts.KDW003C, "KDW003_53",
			"/view/kdw/003/c/index.xhtml");
	
	public static final Program KDW003D = new Program(WebAppId.AT, ProgramIdConsts.KDW003D, "KDW003_52",
			"/view/kdw/003/d/index.xhtml");
	
	public static final Program CMM018A = new Program(WebAppId.COM, ProgramIdConsts.CMM018A, "CMM018_1",
			"/view/cmm/018/a/index.xhtml");
	
	public static final Program CMM018I = new Program(WebAppId.COM, ProgramIdConsts.CMM018I, "CMM018_9",
			"/view/cmm/018/i/index.xhtml");
	
	public static final Program CMM018J = new Program(WebAppId.COM, ProgramIdConsts.CMM018J, "CMM018_10",
			"/view/cmm/018/j/index.xhtml");
	
	public static final Program CMM018K = new Program(WebAppId.COM, ProgramIdConsts.CMM018K, "CMM018_11",
			"/view/cmm/018/k/index.xhtml");
	
	public static final Program CMM018L = new Program(WebAppId.COM, ProgramIdConsts.CMM018L, "CMM018_12",
			"/view/cmm/018/l/index.xhtml");
	
	public static final Program CMM018M = new Program(WebAppId.COM, ProgramIdConsts.CMM018M, "CMM018_13",
			"/view/cmm/018/m/index.xhtml");
	
	public static final Program CMM018N = new Program(WebAppId.COM, ProgramIdConsts.CMM018N, "CMM018_14",
			"/view/cmm/018/n/index.xhtml");
	
	public static final Program KDL032 = new Program(WebAppId.AT, ProgramIdConsts.KDL032A, "KDL032_1",
			"/view/kdl/032/a/index.xhtml");

	public static final Program CPS009A = new Program(WebAppId.COM, ProgramIdConsts.CPS009A, "CPS009_1",
			"/view/cps/009/a/index.xhtml");
	
	public static final Program CPS009B = new Program(WebAppId.COM, ProgramIdConsts.CPS009B, "CPS009_9",
			"/view/cps/009/b/index.xhtml");
	
	public static final Program CPS009C = new Program(WebAppId.COM, ProgramIdConsts.CPS009C, "CPS009_2",
			"/view/cps/009/c/index.xhtml");
	
	public static final Program CPS009D = new Program(WebAppId.COM, ProgramIdConsts.CPS009D, "CPS009_36",
			"/view/cps/009/d/index.xhtml");
	
	public static final Program CPS017A = new Program(WebAppId.COM, ProgramIdConsts.CPS017A, "CPS017_51",
			"/view/cps/017/a/index.xhtml");
	
	public static final Program CPS017B = new Program(WebAppId.COM, ProgramIdConsts.CPS017B, "CPS017_52",
			"/view/cps/017/b/index.xhtml");
	
	public static final Program CPS017C = new Program(WebAppId.COM, ProgramIdConsts.CPS017C, "CPS017_53",
			"/view/cps/017/c/index.xhtml");
	
	public static final Program CPS017D = new Program(WebAppId.COM, ProgramIdConsts.CPS017D, "CPS017_54",
			"/view/cps/017/d/index.xhtml");

	public static final Program CAS005A = new Program(WebAppId.COM, ProgramIdConsts.CAS005A, "CAS005_1",
			"/view/cas/005/a/index.xhtml");
	
	public static final Program CAS005B = new Program(WebAppId.COM, ProgramIdConsts.CAS005B, "CAS005_2",
			"/view/cas/005/b/index.xhtml");
	
	public static final Program CAS005C = new Program(WebAppId.COM, ProgramIdConsts.CAS005C, "CAS005_3",
			"/view/cas/005/c/index.xhtml");
	
	public static final Program CAS009A = new Program(WebAppId.COM, ProgramIdConsts.CAS009A, "CAS009_1",
			"/view/cas/009/a/index.xhtml");
	
	public static final Program CAS009B = new Program(WebAppId.COM, ProgramIdConsts.CAS009B, "CAS009_2",
			"/view/cas/009/b/index.xhtml");
	
	public static final Program CAS011A = new Program(WebAppId.COM, ProgramIdConsts.CAS011A, "CAS011_1",
			"/view/cas/011/a/index.xhtml");
	
	public static final Program CAS011C = new Program(WebAppId.COM, ProgramIdConsts.CAS011C, "CAS011_3",
			"/view/cas/011/c/index.xhtml");
	
	public static final Program CAS012A = new Program(WebAppId.COM, ProgramIdConsts.CAS012A, "CAS012_1",
			"/view/cas/012/a/index.xhtml");
	
	public static final Program CAS012B = new Program(WebAppId.COM, ProgramIdConsts.CAS012B, "CAS012_2",
			"/view/cas/012/b/index.xhtml");
	
	public static final Program CAS012C = new Program(WebAppId.COM, ProgramIdConsts.CAS012C, "CAS012_3",
			"/view/cas/012/c/index.xhtml");

	public static final Program CAS013A = new Program(WebAppId.COM, ProgramIdConsts.CAS013A, "CAS013_1",
			"/view/cas/013/a/index.xhtml");
	
	public static final Program CAS013B = new Program(WebAppId.COM, ProgramIdConsts.CAS013B, "CAS013_2",
			"/view/cas/013/b/index.xhtml");

	public static final Program CAS014A = new Program(WebAppId.COM, ProgramIdConsts.CAS014A, "CAS014_2",
			"/view/cas/014/a/index.xhtml");
	
	public static final Program CAS014B = new Program(WebAppId.COM, ProgramIdConsts.CAS014B, "CAS014_3",
			"/view/cas/014/b/index.xhtml");

	public static final Program CCG025 = new Program(WebAppId.COM, ProgramIdConsts.CCG025, "CCG025_1",
			"/view/ccg/025/component/index.xhtml");
	
	public static final Program CDL025 = new Program(WebAppId.COM, ProgramIdConsts.CDL025, "CDL025_1",
			"/view/cdl/025/index.xhtml");
	
	public static final Program CMM007A = new Program(WebAppId.COM, ProgramIdConsts.CMM007A, "CMM007_1",
			"/view/cmm/007/a/index.xhtml");
	
	public static final Program CMM007B = new Program(WebAppId.COM, ProgramIdConsts.CMM007B, "CMM007_1",
			"/view/cmm/007/b/index.xhtml");
	
	public static final Program CMM007C = new Program(WebAppId.COM, ProgramIdConsts.CMM007C, "CMM007_1",
			"/view/cmm/007/c/index.xhtml");
	
	public static final Program CMM007D = new Program(WebAppId.COM, ProgramIdConsts.CMM007D, "CMM007_1",
			"/view/cmm/007/d/index.xhtml");
	
	public static final Program CMM007E = new Program(WebAppId.COM, ProgramIdConsts.CMM007E, "CMM007_1",
			"/view/cmm/007/e/index.xhtml");
	
	public static final Program CMM007G = new Program(WebAppId.COM, ProgramIdConsts.CMM007G, "CMM007_1",
			"/view/cmm/007/g/index.xhtml");

	public static final Program CCG026 = new Program(WebAppId.COM, ProgramIdConsts.CCG026, "CCG025_2",
			"/view/ccg/026/component/index.xhtml");
	
	public static final Program CMM021A = new Program(WebAppId.COM, ProgramIdConsts.CMM021A, "CMM021_1",
			"/view/cmm/021/a/index.xhtml");
	
	public static final Program CMM021B = new Program(WebAppId.COM, ProgramIdConsts.CMM021B, "CMM021_1",
			"/view/cmm/021/b/index.xhtml");
	
	public static final Program CMM021C = new Program(WebAppId.COM, ProgramIdConsts.CMM021C, "CMM021_1",
			"/view/cmm/021/c/index.xhtml");
	
	public static final Program CDL023A = new Program(WebAppId.COM, ProgramIdConsts.CDL023A, "CDL023_1",
			"/view/cdl/023/a/index.xhtml");
	
	public static final Program KMF002A = new Program(WebAppId.AT, ProgramIdConsts.KMF002A, null,
			"/view/kmf/002/a/index.xhtml");
	
	public static final Program KMF002B = new Program(WebAppId.AT, ProgramIdConsts.KMF002B, "KMF002_1",
			"/view/kmf/002/b/index.xhtml");
	
	public static final Program KMF002C = new Program(WebAppId.AT, ProgramIdConsts.KMF002C, "KMF002_1",
			"/view/kmf/002/c/index.xhtml");
	
	public static final Program KMF002D = new Program(WebAppId.AT, ProgramIdConsts.KMF002D, "KMF002_1",
			"/view/kmf/002/d/index.xhtml");
	
	public static final Program KMF002E = new Program(WebAppId.AT, ProgramIdConsts.KMF002E, "KMF002_1",
			"/view/kmf/002/e/index.xhtml");
	
	public static final Program KMF002F = new Program(WebAppId.AT, ProgramIdConsts.KMF002F, "KMF002_26",
			"/view/kmf/002/f/index.xhtml");
	
	public static final Program KMK003A = new Program(WebAppId.AT, ProgramIdConsts.KMK003A, "KMK003_1",
			"/view/kmk/003/a/index.xhtml");
	public static final Program KMK003B = new Program(WebAppId.AT, ProgramIdConsts.KMK003B, "KMK003_1",
			"/view/kmk/003/b/index.xhtml");
	public static final Program KMK003C = new Program(WebAppId.AT, ProgramIdConsts.KMK003C, "KMK003_1",
			"/view/kmk/003/c/index.xhtml");
	public static final Program KMK003D = new Program(WebAppId.AT, ProgramIdConsts.KMK003D, "KMK003_1",
			"/view/kmk/003/d/index.xhtml");
	public static final Program KMK003E = new Program(WebAppId.AT, ProgramIdConsts.KMK003E, null,
			"/view/kmk/003/e/index.xhtml");
	public static final Program KMK003F = new Program(WebAppId.AT, ProgramIdConsts.KMK003F, null,
			"/view/kmk/003/f/index.xhtml");
	public static final Program KMK003G = new Program(WebAppId.AT, ProgramIdConsts.KMK003G, null,
			"/view/kmk/003/g/index.xhtml");
	public static final Program KMK003H = new Program(WebAppId.AT, ProgramIdConsts.KMK003H, null,
			"/view/kmk/003/h/index.xhtml");
	public static final Program KMK003I = new Program(WebAppId.AT, ProgramIdConsts.KMK003I, null,
			"/view/kmk/003/i/index.xhtml");
	public static final Program KMK003J = new Program(WebAppId.AT, ProgramIdConsts.KMK003J, null,
			"/view/kmk/003/j/index.xhtml");
	public static final Program KMK003K = new Program(WebAppId.AT, ProgramIdConsts.KMK003K, null,
			"/view/kmk/003/k/index.xhtml");
	public static final Program KMK003L = new Program(WebAppId.AT, ProgramIdConsts.KMK003L, null,
			"/view/kmk/003/l/index.xhtml");
	
	public static final Program KSU007A = new Program(WebAppId.AT, ProgramIdConsts.KSU007A, "KSU007_8",
			"/view/ksu/007/a/index.xhtml");
	public static final Program KSU007B = new Program(WebAppId.AT, ProgramIdConsts.KSU007B, "KSU007_23",
			"/view/ksu/007/b/index.xhtml");
	
	
	public static final Program CPS016A = new Program(WebAppId.COM, ProgramIdConsts.CPS016A, "CPS016_26",
			"/view/cps/016/a/index.xhtml");
	
	public static final Program KMK013A = new Program(WebAppId.AT, ProgramIdConsts.KMK013A, "KMK013_236",
			"/view/kmk/013/a/index.xhtml");
	public static final Program KMK013B = new Program(WebAppId.AT, ProgramIdConsts.KMK013B, "KMK013_237",
			"/view/kmk/013/b/index.xhtml");
	public static final Program KMK013C = new Program(WebAppId.AT, ProgramIdConsts.KMK013C, "KMK013_238",
			"/view/kmk/013/c/index.xhtml");
	public static final Program KMK013D = new Program(WebAppId.AT, ProgramIdConsts.KMK013D, "KMK013_239",
			"/view/kmk/013/d/index.xhtml");
	public static final Program KMK013E = new Program(WebAppId.AT, ProgramIdConsts.KMK013E, "KMK013_240",
			"/view/kmk/013/e/index.xhtml");
	public static final Program KMK013F = new Program(WebAppId.AT, ProgramIdConsts.KMK013F, null,
			"/view/kmk/013/f/index.xhtml");
	public static final Program KMK013G = new Program(WebAppId.AT, ProgramIdConsts.KMK013G, null,
			"/view/kmk/013/g/index.xhtml");
	public static final Program KMK013H = new Program(WebAppId.AT, ProgramIdConsts.KMK013H, "KMK013_241",
			"/view/kmk/013/h/index.xhtml");
	
	public static final Program KAF022A = new Program(WebAppId.AT, ProgramIdConsts.KAF022A, null,
			"/view/kaf/022/a/index.xhtml");
	public static final Program KAF022B = new Program(WebAppId.AT, ProgramIdConsts.KAF022B, null,
			"/view/kaf/022/b/index.xhtml");
	public static final Program KAF022C = new Program(WebAppId.AT, ProgramIdConsts.KAF022C, null,
			"/view/kaf/022/c/index.xhtml");
	public static final Program KAF022D = new Program(WebAppId.AT, ProgramIdConsts.KAF022D, null,
			"/view/kaf/022/d/index.xhtml");
	public static final Program KAF022E = new Program(WebAppId.AT, ProgramIdConsts.KAF022E, null,
			"/view/kaf/022/e/index.xhtml");
	public static final Program KAF022F = new Program(WebAppId.AT, ProgramIdConsts.KAF022F, null,
			"/view/kaf/022/f/index.xhtml");
	public static final Program KAF022G = new Program(WebAppId.AT, ProgramIdConsts.KAF022G, null,
			"/view/kaf/022/g/index.xhtml");
	public static final Program KAF022H = new Program(WebAppId.AT, ProgramIdConsts.KAF022H, null,
			"/view/kaf/022/h/index.xhtml");
	public static final Program KAF022I = new Program(WebAppId.AT, ProgramIdConsts.KAF022I, null,
			"/view/kaf/022/i/index.xhtml");
	public static final Program KAF022J = new Program(WebAppId.AT, ProgramIdConsts.KAF022J, null,
			"/view/kaf/022/j/index.xhtml");
	public static final Program KAF022K = new Program(WebAppId.AT, ProgramIdConsts.KAF022K, null,
			"/view/kaf/022/k/index.xhtml");
	public static final Program KAF022L = new Program(WebAppId.AT, ProgramIdConsts.KAF022L, null,
			"/view/kaf/022/l/index.xhtml");
	public static final Program KAF022M = new Program(WebAppId.AT, ProgramIdConsts.KAF022M, null,
			"/view/kaf/022/m/index.xhtml");

	public static final Program KSM011A = new Program(WebAppId.AT, ProgramIdConsts.KSM011A, null,
			"/view/ksm/011/a/index.xhtml");
	public static final Program KSM011B = new Program(WebAppId.AT, ProgramIdConsts.KSM011B, null,
			"/view/ksm/011/b/index.xhtml");
	public static final Program KSM011C = new Program(WebAppId.AT, ProgramIdConsts.KSM011C, null,
			"/view/ksm/011/c/index.xhtml");
	public static final Program KSM011E = new Program(WebAppId.AT, ProgramIdConsts.KSM011E, "KSM011_44",
			"/view/ksm/011/e/index.xhtml");
	
	/**	KSC001A	 */
	public static final Program KSC001A = new Program(WebAppId.AT, ProgramIdConsts.KSC001A, null,
			"/view/ksc/001/a/index.xhtml");
	/**	KSC001B	 */
	public static final Program KSC001B = new Program(WebAppId.AT, ProgramIdConsts.KSC001B, null,
			"/view/ksc/001/b/index.xhtml");
	/**	KSC001C	 */
	public static final Program KSC001C = new Program(WebAppId.AT, ProgramIdConsts.KSC001C, null,
			"/view/ksc/001/c/index.xhtml");
	/**	KSC001D	 */
	public static final Program KSC001D = new Program(WebAppId.AT, ProgramIdConsts.KSC001D, null,
			"/view/ksc/001/d/index.xhtml");
	/**	KSC001E	 */
	public static final Program KSC001E = new Program(WebAppId.AT, ProgramIdConsts.KSC001E, null,
			"/view/ksc/001/e/index.xhtml");
	/**	KSC001F	 */
	public static final Program KSC001F = new Program(WebAppId.AT, ProgramIdConsts.KSC001F, "KSC001_3",
			"/view/ksc/001/f/index.xhtml");
	/**	KSC001G	 */
	public static final Program KSC001G = new Program(WebAppId.AT, ProgramIdConsts.KSC001G, null,
			"/view/ksc/001/g/index.xhtml");
	/**	KSC001H	 */
	public static final Program KSC001H = new Program(WebAppId.AT, ProgramIdConsts.KSC001H, "KSC001_70",
			"/view/ksc/001/h/index.xhtml");
	/**	KSC001I	 */
	public static final Program KSC001I = new Program(WebAppId.AT, ProgramIdConsts.KSC001I, "KSC001_79",
			"/view/ksc/001/i/index.xhtml");
	/**	KSC001K	 */
	public static final Program KSC001K = new Program(WebAppId.AT, ProgramIdConsts.KSC001K, "KSC001_80",
			"/view/ksc/001/k/index.xhtml");
	/**	 KAF002A*/
	public static final Program KAF002A = new Program(WebAppId.AT, ProgramIdConsts.KAF002A, null,
			"/view/kaf/002/a/index.xhtml");
	public static final Program KAF002B = new Program(WebAppId.AT, ProgramIdConsts.KAF002B, null,
			"/view/kaf/002/b/index.xhtml");
	public static final Program KAF002C = new Program(WebAppId.AT, ProgramIdConsts.KAF002C, null,
			"/view/kaf/002/c/index.xhtml");
	/**	 KAF004A*/
	public static final Program KAF004A = new Program(WebAppId.AT, ProgramIdConsts.KAF004A, null,
			"/view/kaf/004/a/index.xhtml");
	/**	 KAF005A*/
	public static final Program KAF004B = new Program(WebAppId.AT, ProgramIdConsts.KAF004B, null,
			"/view/kaf/004/b/index.xhtml");
	/**	 KAF005A*/
	public static final Program KAF005A = new Program(WebAppId.AT, ProgramIdConsts.KAF005A, "KAF005_1",
			"/view/kaf/005/a/index.xhtml");
	/**	 KAF007A*/
	public static final Program KAF007A = new Program(WebAppId.AT, ProgramIdConsts.KAF007A, null,
			"/view/kaf/007/a/index.xhtml");
	/**	 KAF007A*/
	public static final Program KAF009A = new Program(WebAppId.AT, ProgramIdConsts.KAF009A, null,
			"/view/kaf/009/a/index.xhtml");
	/**	 KAF010A*/
	public static final Program KAF010A = new Program(WebAppId.AT, ProgramIdConsts.KAF010A, "KAF010_1",
			"/view/kaf/010/a/index.xhtml");
	// TODO: Define new programs here.
	/**
	 * CMM051A
	 */
	public static final Program CMM051A = new Program(WebAppId.COM, ProgramIdConsts.CMM051A, "CMM051_1",
			"/view/cmm/051/a/index.xhtml");
	/**	 CMM001 */
	public static final Program CMM001A = new Program(WebAppId.COM, ProgramIdConsts.CMM001A, "CMM001_39",
			"/view/cmm/001/a/index.xhtml");
	public static final Program CMM001B = new Program(WebAppId.COM, ProgramIdConsts.CMM001B, null,
			"/view/cmm/001/b/index.xhtml");
	public static final Program CMM001C = new Program(WebAppId.COM, ProgramIdConsts.CMM001C, null,
			"/view/cmm/001/c/index.xhtml");
	public static final Program CMM001D = new Program(WebAppId.COM, ProgramIdConsts.CMM001D, null,
			"/view/cmm/001/d/index.xhtml");
	/**CCG018	 */
	
	/**  KAL002 */
	public static final Program KAL002A = new Program(WebAppId.AT, ProgramIdConsts.KAL002A, "KAL002_1",
			"/view/kal/002/a/index.xhtml");
	public static final Program KAL002B = new Program(WebAppId.AT, ProgramIdConsts.KAL002B, "KAL002_12",
			"/view/kal/002/b/index.xhtml");
	/**  KAL003 */
	public static final Program KAL003A = new Program(WebAppId.AT, ProgramIdConsts.KAL003A, "KAL003_1",
			"view/kal/003/a/index.xhtml");
	public static final Program KAL003B = new Program(WebAppId.AT, ProgramIdConsts.KAL003B, "KAL003_2",
			"view/kal/003/b/index.xhtml");
	public static final Program KAL003C = new Program(WebAppId.AT, ProgramIdConsts.KAL003C, "KAL003_3",
			"view/kal/003/c/index.xhtml");
	public static final Program KAL003D = new Program(WebAppId.AT, ProgramIdConsts.KAL003D, "KAL003_4",
			"view/kal/003/d/index.xhtml");
	/**  KAL004 */
	public static final Program KAL004A = new Program(WebAppId.AT, ProgramIdConsts.KAL004A, "KAL004_1",
			"/view/kal/004/a/index.xhtml");
	public static final Program KAL004B = new Program(WebAppId.AT, ProgramIdConsts.KAL004B, "KAL004_2",
			"/view/kal/004/b/index.xhtml");
	public static final Program KAL004F = new Program(WebAppId.AT, ProgramIdConsts.KAL004F, "KAL004_2",
			"/view/kal/004/f/index.xhtml");
	/**  KDW004 */
	public static final Program KDW004A= new Program(WebAppId.AT, ProgramIdConsts.KDW004A, "KDW004_1",
			"/view/kdw/004/a/index.xhtml");
	/**CMM045*/
	public static final Program CMM045A= new Program(WebAppId.AT, ProgramIdConsts.CMM045A, "CMM045_1",
			"/view/cmm/045/a/index.xhtml");
	public static final Program CMM045B= new Program(WebAppId.AT, ProgramIdConsts.CMM045B, "CMM045_2",
			"/view/cmm/045/b/index.xhtml");
	
	
	public static final Program CPS001G= new Program(WebAppId.COM, ProgramIdConsts.CPS001G, "CPS001_97",
			"/view/cps/001/g/index.xhtml");
	
	/** KTG001 **/
	public static final Program KTG001A = new Program(WebAppId.AT, ProgramIdConsts.KTG001A, "KTG001_1",
			"/view/ktg/001/a/index.xhtml");
	/** KTG002 **/
	public static final Program KTG002A = new Program(WebAppId.AT, ProgramIdConsts.KTG002A, "KTG002_1",
			"/view/ktg/002/a/index.xhtml");
	/** KTG028 **/
	public static final Program KTG028A = new Program(WebAppId.AT, ProgramIdConsts.KTG028A, "KTG028_1",
			"/view/ktg/028/a/index.xhtml");
	/** KTG029 **/
	public static final Program KTG029A = new Program(WebAppId.AT, ProgramIdConsts.KTG029A, "KTG029_1",
			"/view/ktg/029/a/index.xhtml");
	
	/**
	 * All programs map.
	 */
	private static final Map<WebAppId, List<Program>> PROGRAMS;

	static {
		Function<Field, Optional<Program>> pg = f -> {
			try {
				if (Modifier.isStatic(f.getModifiers()))
					return Optional.of((Program) f.get(null));
				else
					return Optional.empty();
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				throw new RuntimeException(ex);
			}
		};
		PROGRAMS = Arrays.asList(ProgramsManager.class.getFields()).stream().filter(f -> f.getType() == Program.class)
				.map(f -> pg.apply(f).get()).collect(Collectors.groupingBy(Program::getAppId));
	}

	/**
	 * Finds program.
	 * 
	 * @param appId
	 *            appId.
	 * @param path
	 *            program path.
	 * @return optional program.
	 */
	public static Optional<Program> find(WebAppId appId, String path) {
		if (appId == null || path == null)
			return Optional.empty();
		Optional<Set<Program>> programsOpt = getSet(appId);
		if (!programsOpt.isPresent())
			return Optional.empty();
		return programsOpt.get().stream().filter(a -> path.equals(a.getPPath())).findFirst();
	}
	
	/**
	 * Finds program by Id.
	 * @param appId appId
	 * @param programId programId
	 * @return optional program
	 */
	public static Optional<Program> findById(WebAppId appId, String programId) {
		if (appId == null || programId == null) 
			return Optional.empty();
		Optional<Set<Program>> programsOpt = getSet(appId);
		if (!programsOpt.isPresent())
			return Optional.empty();
		return programsOpt.get().stream().filter(a -> programId.equals(a.getPId())).findFirst();
	}

	/**
	 * Finds program Id.
	 * 
	 * @param appId
	 *            appId.
	 * @param path
	 *            path.
	 * @return optional program Id.
	 */
	public static Optional<String> idOf(WebAppId appId, String path) {
		return Optional.ofNullable(find(appId, path).orElse(new Program()).getPId());
	}

	/**
	 * Finds program name.
	 * 
	 * @param appId
	 *            appId.
	 * @param path
	 *            path.
	 * @return optional program name.
	 */
	public static Optional<String> nameOf(WebAppId appId, String path) {
		return Optional.ofNullable(find(appId, path).orElse(new Program()).getPName());
	}
	
	/**
	 * Finds program name by Id.
	 * @param appId appId
	 * @param programId programId
	 * @return optional program name.
	 */
	public static Optional<String> nameById(WebAppId appId, String programId) {
		return Optional.ofNullable(findById(appId, programId).orElse(new Program()).getPName());
	}

	/**
	 * Gets predefined set.
	 * 
	 * @param appId
	 *            appId.
	 * @return optional program set.
	 */
	private static Optional<Set<Program>> getSet(WebAppId appId) {
		List<Program> programs = PROGRAMS.get(appId);
		if (programs == null || programs.size() == 0)
			return Optional.empty();
		return Optional.of(programs.stream().collect(Collectors.toSet()));
	}
}
