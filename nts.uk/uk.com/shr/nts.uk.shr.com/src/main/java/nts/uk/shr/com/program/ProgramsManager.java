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
	public static final Program CCG008D = new Program(WebAppId.COM, ProgramIdConsts.CCG008D, "PgName_CCG008D",
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
	 * CCG030A
	 */
	public static final Program CCG030A = new Program(WebAppId.COM, ProgramIdConsts.CCG030A, "CCG030_1",
			"/view/ccg/030/a/index.xhtml");

	/**
	 * CCG030B
	 */
	public static final Program CCG030B = new Program(WebAppId.COM, ProgramIdConsts.CCG030B, "CCG030_2",
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
	 * CDL022A
	 */
	public static final Program CDL022A = new Program(WebAppId.COM, ProgramIdConsts.CDL022A, "CDL022_1",
			"/view/cdl/022/a/index.xhtml");
	/**
	 * CPS007B
	 */
	public static final Program CPS007B = new Program(WebAppId.COM, ProgramIdConsts.CPS007B, "CPS007_2",
			"/view/cps/007/b/index.xhtml");
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
	public static final Program KSU001A = new Program(WebAppId.AT, ProgramIdConsts.KSU001A, "PgName_KSU001A",
			"/view/ksu/001/a/index.xhtml");

	/** The Constant KDL003. */
	public static final Program KDL003 = new Program(WebAppId.AT, ProgramIdConsts.KDL003, "KDL003_1",
			"/view/kdl/003/a/index.xhtml");
	
	/** The Constant KSM006. */
	public static final Program KSM006 = new Program(WebAppId.AT, ProgramIdConsts.KSM006, "KSM006_1",
			"/view/ksm/003/a/index.xhtml");
	
	/** The Constant KSM003. */
	public static final Program KSM003 = new Program(WebAppId.AT, ProgramIdConsts.KSM003, "KSM003_1",
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
	public static final Program KSM001 = new Program(WebAppId.AT, ProgramIdConsts.KSM001, "KSM001_62",
			"/view/ksm/001/a/index.xhtml");
	
	/** The Constant KMK009. */
	// TODO: can check lai
	public static final Program KMK009 = new Program(WebAppId.AT, ProgramIdConsts.KMK009, "KMK009_1",
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
			"CCG007_1", "/view/ccg/007/a/index.xhtml");
	
	/** The Constant CCG007B. */
	public static final Program CCG007B = new Program(WebAppId.COM, ProgramIdConsts.CCG007B,
			"CCG007_2", "/view/ccg/007/a/index.xhtml");
	
	/** The Constant CCG007C. */
	public static final Program CCG007C = new Program(WebAppId.COM, ProgramIdConsts.CCG007C,
			"CCG007_3", "/view/ccg/007/a/index.xhtml");
	
	/** The Constant CCG007D. */
	public static final Program CCG007D = new Program(WebAppId.COM, ProgramIdConsts.CCG007D,
			"CCG007_4", "/view/ccg/007/a/index.xhtml");
	
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
	
	/** The Constant KMF001A. */
	public static final Program KMF001A  = new Program(WebAppId.AT, ProgramIdConsts.KMF001A, "KMF001_1",
			"/view/kmk/001/a/index.xhtml");
	
	/** The Constant KMF001B. */
	public static final Program KMF001B  = new Program(WebAppId.AT, ProgramIdConsts.KMF001B, "KMF001_2",
			"/view/kmk/001/b/index.xhtml");
	
	/** The Constant KMF001C. */
	public static final Program KMF001C  = new Program(WebAppId.AT, ProgramIdConsts.KMF001C, "KMF001_3",
			"/view/kmk/001/c/index.xhtml");
	
	/** The Constant KMF001D. */
	public static final Program KMF001D  = new Program(WebAppId.AT, ProgramIdConsts.KMF001D, "KMF001_4",
			"/view/kmk/001/d/index.xhtml");
	
	/** The Constant KMF001F. */
	public static final Program KMF001F  = new Program(WebAppId.AT, ProgramIdConsts.KMF001F, "KMF001_6",
			"/view/kmk/001/f/index.xhtml");
	
	/** The Constant KMF001H. */
	public static final Program KMF001H  = new Program(WebAppId.AT, ProgramIdConsts.KMF001H, "KMF001_8",
			"/view/kmk/001/h/index.xhtml");
	
	/** The Constant KMF001J. */
	public static final Program KMF001J  = new Program(WebAppId.AT, ProgramIdConsts.KMF001J, "KMF001_10",
			"/view/kmk/001/j/index.xhtml");
	
	/** The Constant KMF001L. */
	public static final Program KMF001L  = new Program(WebAppId.AT, ProgramIdConsts.KMF001L, "KMF001_12",
			"/view/kmk/001/l/index.xhtml");
	
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
	public static final Program KMF004A  = new Program(WebAppId.AT, ProgramIdConsts.KMF004A, "KMF004_1",
			"/view/kmf/004/a/index.xhtml");
	
	/**
	 * KMF004B
	 */
	public static final Program KMF004B  = new Program(WebAppId.AT, ProgramIdConsts.KMF004B, "KMF004_2",
			"/view/kmf/004/b/index.xhtml");
	
	/**
	 * KMF004C
	 */
	public static final Program KMF004C  = new Program(WebAppId.AT, ProgramIdConsts.KMF004C, "KMF004_3",
			"/view/kmf/004/c/index.xhtml");
	
	/**
	 * KMF004D
	 */
	public static final Program KMF004D  = new Program(WebAppId.AT, ProgramIdConsts.KMF004D, "KMF004_4",
			"/view/kmf/004/d/index.xhtml");
	
	/**
	 * KMF004E
	 */
	public static final Program KMF004E  = new Program(WebAppId.AT, ProgramIdConsts.KMF004E, "KMF004_5",
			"/view/kmf/004/e/index.xhtml");
	
	/**
	 * KMF004F
	 */
	public static final Program KMF004F  = new Program(WebAppId.AT, ProgramIdConsts.KMF004F, "KMF004_6",
			"/view/kmf/004/f/index.xhtml");
	
	/**
	 * KMF004G
	 */
	public static final Program KMF004G  = new Program(WebAppId.AT, ProgramIdConsts.KMF004G, "KMF004_7",
			"/view/kmf/004/g/index.xhtml");
	
	/**
	 * KMF004H
	 */
	public static final Program KMF004H  = new Program(WebAppId.AT, ProgramIdConsts.KMF004H, "KMF004_8",
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
	 * CAS001C
	 */
	public static final Program CAS001C  = new Program(WebAppId.AT, ProgramIdConsts.CAS001C, "PgName_CAS001C",
			"/view/cas/001/c/index.xhtml");
	
	/**
	 * CAS001D
	 */
	public static final Program CAS001D  = new Program(WebAppId.AT, ProgramIdConsts.CAS001D, "PgName_CAS001D",
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
	public static final Program KMK008A  = new Program(WebAppId.AT, ProgramIdConsts.KMK008A, "KML001_1",
			"/view/kmk/008/a/index.xhtml");
	
	/**
	 * KMK005B
	 */
	public static final Program KMK008B  = new Program(WebAppId.AT, ProgramIdConsts.KMK008B, "KML001_1",
			"/view/kmk/008/b/index.xhtml");
	
	/**
	 * KMK005B
	 */
	public static final Program KMK008C  = new Program(WebAppId.AT, ProgramIdConsts.KMK008C, "KML001_1",
			"/view/kmk/008/c/index.xhtml");
	
	/**
	 * KMK005D
	 */
	public static final Program KMK008D  = new Program(WebAppId.AT, ProgramIdConsts.KMK008D, "KML001_1",
			"/view/kmk/008/d/index.xhtml");
	
	/**
	 * KMK005E
	 */
	public static final Program KMK008E  = new Program(WebAppId.AT, ProgramIdConsts.KMK008E, "KML001_1",
			"/view/kmk/008/e/index.xhtml");
	
	/**
	 * KMK005B
	 */
	public static final Program KMK008F  = new Program(WebAppId.AT, ProgramIdConsts.KMK008F, "KML001_1",
			"/view/kmk/008/f/index.xhtml");
	
	/**
	 * KMK005G
	 */
	public static final Program KMK008G  = new Program(WebAppId.AT, ProgramIdConsts.KMK008G, "KML001_1",
			"/view/kmk/008/g/index.xhtml");
	
	/**
	 * KMK005H
	 */
	public static final Program KMK008I  = new Program(WebAppId.AT, ProgramIdConsts.KMK008I, "KML001_2",
			"/view/kmk/008/i/index.xhtml");
	
	/**
	 * KMK005H
	 */
	public static final Program KMK008J  = new Program(WebAppId.AT, ProgramIdConsts.KMK008J, "KML001_3",
			"/view/kmk/008/j/index.xhtml");
	
	/**
	 * KMK005K
	 */
	public static final Program KMK008K  = new Program(WebAppId.AT, ProgramIdConsts.KMK008K, "KML001_4",
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
	 * KDW007A
	 */
	public static final Program KDW008B  = new Program(WebAppId.AT, ProgramIdConsts.KDW008B, "",
			"/view/kdw/008/b/index.xhtml");
	
	/**
	 * KDW007A
	 */
	public static final Program KDW008C  = new Program(WebAppId.AT, ProgramIdConsts.KDW008C, "",
			"/view/kdw/008/c/index.xhtml");
	
	
	// TODO: Define new programs here.

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
