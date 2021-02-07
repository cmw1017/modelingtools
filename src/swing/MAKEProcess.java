package swing;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class MAKEProcess implements Runnable {
	private String load_path;
	private JLabel content;
	private JButton complete;
	private String outputText;

	public MAKEProcess(String load_path, JLabel content, JButton complete) {
		this.load_path = load_path;
		this.content = content;
		this.complete = complete;
	}

	public void exet() throws IOException, InterruptedException {
		System.out.println("make load_path : " + load_path);
		System.out.print("make_logic....");
		outputText = "<html>--- makegeo 진행 중 ---<br/>";
		content.setText(outputText);
		String LU = "", Terrain = "", OutGEO = "", ListGEO = "", LUGRID = "", TerrianGRID = "", insrc;
		double Xcor = 0.0, Ycor = 0.0, cellSize = 0.0;
		int gridNumX = 0, gridNumY = 0, ch, flag = 0, series1 = 0, series2 = 0, series3 = 0;
		String[][] value = new String[38][8];
		Process process = null;
		ProcessBuilder process2 = null;
		insrc = load_path;
		String root = insrc.substring(0, insrc.lastIndexOf("\\"));
		if (!insrc.substring(insrc.indexOf('.') + 1).equals("set")) {
			System.out.println("에러 : 확장자를 확인하여 주세요(set 형식이어야 합니다).");
			outputText += "에러 : 확장자를 확인하여 주세요(set 형식이어야 합니다).<br/>";
			content.setText(outputText);
			return;
		}

		Reader inStream = new FileReader(insrc);
		StringBuilder str = new StringBuilder();
		while (true) {
			if (series2 == 38)
				break;
			ch = inStream.read();
			if (ch != ' ' && ch != 10 && ch != 13 && ch != 9 && ch != ',') {
				str.append((char) ch);
			} else {
				if (str.length() != 0) {
					if (str.toString().equals("!") && flag == 0)
						flag = 1;
					else if (str.toString().equals("!") && flag == 1)
						flag = 0;
					if (flag == 1 && !str.toString().equals("!")) {
						switch (series1) {
						case 0: {
							LU = str.toString();
							series1++;
							break;
						}
						case 1: {
							Terrain = str.toString();
							series1++;
							break;
						}
						case 2: {
							OutGEO = str.toString();
							series1++;
							break;
						}
						case 3: {
							ListGEO = str.toString();
							series1++;
							break;
						}
						case 4: {
							LUGRID = str.toString();
							series1++;
							break;
						}
						case 5: {
							TerrianGRID = str.toString();
							series1++;
							break;
						}
						case 6: {
							Xcor = Double.parseDouble((str.toString()));
							series1++;
							break;
						}
						case 7: {
							Ycor = Double.parseDouble((str.toString()));
							series1++;
							break;
						}
						case 8: {
							gridNumX = Integer.parseInt((str.toString()));
							series1++;
							break;
						}
						case 9: {
							gridNumY = Integer.parseInt((str.toString()));
							series1++;
							break;
						}
						case 10: {
							cellSize = Double.parseDouble((str.toString()));
							series1++;
							break;
						}
						case 11: {
							series1++;
							break;
						}
						}
						if (series1 == 12) {
							value[series2][series3] = str.toString();
							series3++;
							if (series3 == 8) {
								series2++;
								series3 = 0;
							}
						}
					}
					str = new StringBuilder();
				}
			}
		}
		inStream.close();
		outputText += "--- 파일 출력 중 ---<br/>";
		content.setText(outputText);
		String outsrc = root + "\\makegeo.inp";
		String tempstr;
		BufferedWriter outStream = new BufferedWriter(new FileWriter(outsrc));
		// if(continueNum == 1) LU = inputName;
		tempstr = "MAKEGEO.INP     2.0             Snow Processing\r\n"
				+ "GEO.DAT file - System Demonstration - 99 X 99 grid\r\n"
				+ "--------------------- Run title (1 line) --------------------------------------\r\n" + "\r\n"
				+ "                 MAKEGEO PROCESSOR CONTROL FILE\r\n"
				+ "                 ------------------------------\r\n" + "\r\n"
				+ "  MAKEGEO creates the geophysical data file (GEO.DAT) for CALMET.  Using\r\n"
				+ "  the fractional land use data from CTGPROC (LU.DAT), it calculates the\r\n"
				+ "  dominant land use for each cell and computes weighted surface parameters.\r\n"
				+ "  It may also remap land use categories if desired.  Terrain data can\r\n"
				+ "  be obtained from TERREL, or provided in a file of similar format\r\n" + "  (TERR.DAT).\r\n"
				+ "\r\n" + "-------------------------------------------------------------------------------\r\n"
				+ "\r\n" + "INPUT GROUP: 0 -- Input and Output Files\r\n" + "--------------\r\n" + "\r\n"
				+ "     Default Name  Type          File Name\r\n" + "     ------------  ----          ---------\r\n"
				+ "     LU.DAT        input    ! LUDAT   =" + LU + "   !\r\n"
				+ "     LU2.DAT       input    ! LU2DAT  = !\r\n" + "     TERR.DAT      input    ! TERRDAT =" + Terrain
				+ " !\r\n" + "     SNOW.DAT      input    ! SNOWDAT = !\r\n" + "\r\n"
				+ "     GEO.DAT       output   ! GEODAT  =" + OutGEO + "  !\r\n"
				+ "     MAKEGEO.LST   output   ! RUNLST  =" + ListGEO + "  !\r\n"
				+ "     QALUSE.GRD    output   ! LUGRD   =" + LUGRID + "  !\r\n"
				+ "     QATERR.GRD    output   ! TEGRD   =" + TerrianGRID + "  !\r\n" + "\r\n"
				+ "     All file names will be converted to lower case if LCFILES = T\r\n"
				+ "     Otherwise, if LCFILES = F, file names will be converted to UPPER CASE\r\n"
				+ "     (LCFILES)                  Default: T      ! LCFILES = F !\r\n" + "        T = lower case\r\n"
				+ "        F = UPPER CASE\r\n" + "\r\n"
				+ "     NOTE: File/path names can be up to 70 characters in length\r\n" + "\r\n" + "!END!\r\n" + "\r\n"
				+ "--------------------------------------------------------------------------------\r\n" + "\r\n"
				+ "INPUT GROUP: 1 -- Run control parameters\r\n" + "--------------\r\n" + "\r\n"
				+ "   Terrain Processing Control\r\n" + "\r\n" + "     Read in a gridded terrain file?\r\n"
				+ "     (LTERR)                    Default: T      ! LTERR = T !\r\n"
				+ "        T = terrain elevations in GEO.DAT read from TERR.DAT\r\n"
				+ "        F = terrain elevations in GEO.DAT are zero\r\n" + "\r\n" + "\r\n"
				+ "   Land Use Processing Control\r\n" + "\r\n"
				+ "   A second file of fractional land use (LU2.DAT) may be provided for\r\n"
				+ "   use when a cell in the primary land use file (LU.DAT) has no indicated\r\n"
				+ "   land use.  This option allows a lower resolution dataset to supplement\r\n"
				+ "   a higher resolution dataset where the higher resolution data are\r\n" + "   unavailable.\r\n"
				+ "\r\n" + "     Read in a second fractional land use file?\r\n"
				+ "     (LLU2)                     Default: F      ! LLU2 = F !\r\n"
				+ "        T = supplemental fractional land use read from LU2.DAT\r\n"
				+ "        F = no supplemental fractional land use data are available\r\n" + "\r\n" + "\r\n"
				+ "   Daily Snow Data Processing Control\r\n" + "\r\n"
				+ "   US daily SNODAS gridded snow data can be used to modify the surface\r\n"
				+ "   landuse properties in the modeling grid to create one or more GEO.DAT\r\n"
				+ "   files that can be used in individual CALMET runs during the winter.\r\n" + "   \r\n"
				+ "     Process snow data?\r\n" + "     (LSNOW)                    Default: F      ! LSNOW = F !\r\n"
				+ "       T = Process SNODAS snow data\r\n" + "       F = Do not process SNODAS snow data\r\n" + "\r\n"
				+ "     Format for Time-Varying GEO.DAT output\r\n" + "     (Used only if LSNOW = T)\r\n"
				+ "     (IFMTGEO)                  Default: 1      ! IFMTGEO = 1 !\r\n"
				+ "       1 = One GEO.DAT file for each day. In this format the\r\n"
				+ "           date stamp will be added to the GEO.DAT file name \r\n"
				+ "           listed in Group 0.  For example, if the name chosen\r\n"
				+ "           for the output file is geo1km.dat, the name of the\r\n"
				+ "           daily file for January 10, 2008 would be\r\n" + "	        geo1km_20080110.dat\r\n"
				+ "       2 = One time-varying GEO.DAT file for all days in\r\n" + "           compressed format.\r\n"
				+ "           (Not Available at this time)\r\n" + "\r\n"
				+ "     Beginning and Ending dates (YYYYMMDD) for daily GEO.DAT\r\n"
				+ "     (Used only if LSNOW = T)\r\n"
				+ "     (IDATEBEG)                 No Default      ! IDATEBEG = 20080219 !\r\n"
				+ "     (IDATEEND)                 No Default      ! IDATEEND = 20080219 !\r\n" + "\r\n" + "\r\n"
				+ "   QA information for 1 cell in the grid can be written to the list\r\n"
				+ "   file.  Identify the cell by its grid location (IX,IY).\r\n"
				+ "   No QA output is generated if either index is outside your grid.  For\r\n"
				+ "   example, using 0 for either turns the QA output off.\r\n" + "\r\n"
				+ "     Location of grid cell for QA output\r\n"
				+ "     (IXQA)                     Default: 0      ! IXQA = 0 !\r\n"
				+ "     (IYQA)                     Default: 0      ! IYQA = 0 !\r\n" + "\r\n" + "!END!\r\n" + "\r\n"
				+ "--------------------------------------------------------------------------------\r\n" + "\r\n"
				+ "INPUT GROUP: 2 -- Map Projection and Grid Information for Output\r\n" + "--------------\r\n" + "\r\n"
				+ "     Projection\r\n" + "     ----------\r\n" + "\r\n" + "     Map projection for all X,Y (km)\r\n"
				+ "     (PMAP)                     Default: UTM    ! PMAP = UTM !\r\n" + "\r\n"
				+ "         UTM :  Universal Transverse Mercator\r\n"
				+ "         TTM :  Tangential Transverse Mercator\r\n" + "         LCC :  Lambert Conformal Conic\r\n"
				+ "         PS  :  Polar Stereographic\r\n" + "         EM  :  Equatorial Mercator\r\n"
				+ "         LAZA:  Lambert Azimuthal Equal Area\r\n" + "\r\n"
				+ "     False Easting and Northing (km) at the projection origin\r\n"
				+ "     (Used only if PMAP= TTM, LCC, or LAZA)\r\n"
				+ "     (FEAST)                    Default=0.0     ! FEAST  = 0.0 !\r\n"
				+ "     (FNORTH)                   Default=0.0     ! FNORTH = 0.0 !\r\n" + "\r\n"
				+ "     UTM zone (1 to 60)\r\n" + "     (Used only if PMAP=UTM)\r\n"
				+ "     (IUTMZN)                   No Default      ! IUTMZN = 52 !\r\n" + "\r\n"
				+ "     Hemisphere for UTM projection?\r\n" + "     (Used only if PMAP=UTM)\r\n"
				+ "     (UTMHEM)                   Default: N      ! UTMHEM = N !\r\n"
				+ "         N   :  Northern hemisphere projection\r\n"
				+ "         S   :  Southern hemisphere projection\r\n" + "\r\n"
				+ "     Latitude and Longitude (decimal degrees) of projection origin\r\n"
				+ "     (Used only if PMAP= TTM, LCC, PS, EM, or LAZA)\r\n"
				+ "     (RLAT0)                    No Default      ! RLAT0 = 40.0N !\r\n"
				+ "     (RLON0)                    No Default      ! RLON0 = 70.0W !\r\n" + "\r\n"
				+ "         TTM :  RLON0 identifies central (true N/S) meridian of projection\r\n"
				+ "                RLAT0 selected for convenience\r\n"
				+ "         LCC :  RLON0 identifies central (true N/S) meridian of projection\r\n"
				+ "                RLAT0 selected for convenience\r\n"
				+ "         PS  :  RLON0 identifies central (grid N/S) meridian of projection\r\n"
				+ "                RLAT0 selected for convenience\r\n"
				+ "         EM  :  RLON0 identifies central meridian of projection\r\n"
				+ "                RLAT0 is REPLACED by 0.0N (Equator)\r\n"
				+ "         LAZA:  RLON0 identifies longitude of tangent-point of mapping plane\r\n"
				+ "                RLAT0 identifies latitude of tangent-point of mapping plane\r\n" + "\r\n"
				+ "     Matching parallel(s) of latitude (decimal degrees) for projection\r\n"
				+ "     (Used only if PMAP= LCC or PS)\r\n"
				+ "     (RLAT1)                    No Default      ! RLAT1 = 30.0N !\r\n"
				+ "     (RLAT2)                    No Default      ! RLAT2 = 60.0N !\r\n" + "\r\n"
				+ "         LCC :  Projection cone slices through Earth's surface at RLAT1 and RLAT2\r\n"
				+ "         PS  :  Projection plane slices through Earth at RLAT1\r\n"
				+ "                (RLAT2 is not used)\r\n" + "\r\n" + "     ----------\r\n"
				+ "     Note:  Latitudes and longitudes should be positive, and include a\r\n"
				+ "            letter N,S,E, or W indicating north or south latitude, and\r\n"
				+ "            east or west longitude.  For example,\r\n" + "            35.9  N Latitude  =  35.9N\r\n"
				+ "            118.7 E Longitude = 118.7E\r\n" + "\r\n" + "\r\n" + "     Output Datum-Region\r\n"
				+ "     -------------------\r\n" + "\r\n"
				+ "     The Datum-Region for the output coordinates is identified by a character\r\n"
				+ "     string.  Many mapping products currently available use the model of the\r\n"
				+ "     Earth known as the World Geodetic System 1984 (WGS-84).  Other local\r\n"
				+ "     models may be in use, and their selection in TERREL will make its output\r\n"
				+ "     consistent with local mapping products.  The list of Datum-Regions with\r\n"
				+ "     official transformation parameters is provided by the National Imagery and\r\n"
				+ "     and Mapping Agency (NIMA).\r\n" + "\r\n" + "     Datum-region for output coordinates\r\n"
				+ "     (DATUM)                    Default: WGS-84    ! DATUM = WGS-84 !\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "     Grid\r\n" + "     ----\r\n" + "\r\n"
				+ "     Reference coordinates X,Y (km) assigned to the southwest corner \r\n"
				+ "     of grid cell (1,1)  (lower left corner of grid)\r\n"
				+ "     (XREFKM)                   No Default      ! XREFKM = " + String.format("%.3f", Xcor / 1000)
				+ "  !\r\n" + "     (YREFKM)                   No Default      ! YREFKM = "
				+ String.format("%.3f", Ycor / 1000) + " !\r\n" + "\r\n" + "     Cartesian grid definition\r\n"
				+ "     No. X grid cells (NX)      No default      ! NX =   " + gridNumX + "  !\r\n"
				+ "     No. Y grid cells (NY)      No default      ! NY =   " + gridNumY + "  !\r\n"
				+ "     Grid Spacing (DGRIDKM)     No default      ! DGRIDKM = "
				+ String.format("%.3f", cellSize / 1000) + " !\r\n" + "     in kilometers\r\n" + "\r\n" + "\r\n"
				+ "!END!\r\n" + "\r\n"
				+ "--------------------------------------------------------------------------------\r\n" + "\r\n"
				+ "INPUT GROUP: 3 -- Output Land Use\r\n" + "--------------\r\n" + "\r\n" + "-------------\r\n"
				+ "Subgroup (3a)\r\n" + "-------------\r\n" + "\r\n" + "     Number of output land use categories\r\n"
				+ "     (NOUTCAT)                  Default: 14     ! NOUTCAT = 14 !\r\n" + "\r\n"
				+ "     Output land use categories assigned to water\r\n"
				+ "     range from IWAT1 to IWAT2 (inclusive)\r\n"
				+ "     (IWAT1)                    Default: 50     ! IWAT1 = 50 !\r\n"
				+ "     (IWAT2)                    Default: 55     ! IWAT2 = 55 !\r\n" + "\r\n" + "!END!\r\n" + "\r\n"
				+ "\r\n" + "-------------\r\n" + "Subgroup (3b)\r\n" + "-------------\r\n"
				+ "                                                       a\r\n"
				+ "           OUTPUT LAND USE CATEGORIES (NOUTCAT entries)\r\n"
				+ "           --------------------------------------------\r\n" + "\r\n"
				+ "! OUTCAT =   10, 20, -20, 30, 40, 51, 54, 55    !  !END!\r\n"
				+ "! OUTCAT =   60, 61,  62, 70, 80, 90            !  !END!\r\n" + "\r\n" + "-------------\r\n"
				+ "    a\r\n" + "     List categories in ascending order (absolute value), with up to 10\r\n"
				+ "     per line.  Each line is treated as a separate input subgroup and\r\n"
				+ "     therefore must end with an input group terminator.\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "--------------------------------------------------------------------------------\r\n" + "\r\n"
				+ "INPUT GROUP: 4 -- Input Land Use (Defaults are set for USGS categories)\r\n" + "--------------\r\n"
				+ "\r\n" + "-------------\r\n" + "Subgroup (4a)\r\n" + "-------------\r\n" + "\r\n"
				+ "     Number of input land use categories\r\n"
				+ "     (NINCAT)                   Default: 38     ! NINCAT = 38 !\r\n" + "\r\n"
				+ "     Number of input water categories\r\n"
				+ "     (NUMWAT)                   Default: 5      ! NUMWAT = 5  !\r\n" + "\r\n"
				+ "     Number of input categories that are split\r\n"
				+ "     by apportioning area among the other land\r\n" + "     use categories\r\n"
				+ "     (NSPLIT)                   Default: 0      ! NSPLIT = 0  !\r\n" + "\r\n"
				+ "     Minimum fraction of cell covered by water required\r\n"
				+ "     to define the dominant land use as water\r\n"
				+ "     (CFRACT)                   Default: 0.5    ! CFRACT = 0.5  !\r\n" + "\r\n"
				+ "     Land use category assigned to cell when\r\n" + "     no land use data are found\r\n"
				+ "     (IMISS)                    Default: 55     ! IMISS = 55  !\r\n" + "\r\n"
				+ "     Minimum total fractional land use expected\r\n"
				+ "     in a cell when land use data are available\r\n"
				+ "     (FLUMIN)                   Default: 0.96   ! FLUMIN = 0.96 !\r\n" + "\r\n" + "\r\n"
				+ "     Method to obtain surface roughness when\r\n" + "     the surface is covered by snow\r\n"
				+ "     (Used only if LSNOW = T)\r\n"
				+ "     (MSRL)                     Default: 1      ! MSRL = 1 !\r\n"
				+ "       1 = Computed from effective obstacle height\r\n" + "       2 = User-defined table\r\n"
				+ "\r\n" + "     Height Scale (m) for computing effective\r\n"
				+ "     obstacle height (method MSRL=1)\r\n" + "     (Used only if LSNOW = T)\r\n"
				+ "     (HSCL)                     Default: 10.    ! HSCL = 10. !\r\n" + "\r\n"
				+ "     Method to obtain surface albedo when the\r\n" + "     surface is covered by snow\r\n"
				+ "     (Used only if LSNOW = T)\r\n"
				+ "     (MSAL)                     Default: 1      ! MSAL = 1 !\r\n"
				+ "       1 = Computed using snow ages\r\n" + "       2 = Use user-defined table\r\n" + "\r\n"
				+ "     Minimum snow depth (meters) when surface\r\n" + "     roughness is affected by snow\r\n"
				+ "     (Used only if LSNOW = T)\r\n"
				+ "     (SDPMIN)                   Default: 0.01   ! SDPMIN = 0.01 !\r\n" + "\r\n" + "!END!\r\n"
				+ "\r\n" + "\r\n" + "-------------\r\n" + "Subgroup (4b)\r\n" + "-------------\r\n"
				+ "                                                              a\r\n"
				+ "           LAND USE PROPERTIES AND OUTPUT MAP (NINCAT entries)\r\n"
				+ "           ---------------------------------------------------\r\n" + "\r\n"
				+ "     Input                                 Soil   Anthropogenic Leaf   Output\r\n"
				+ "    Category    z0     Albedo    Bowen   Heat Flux  Heat Flux   Area  Category\r\n"
				+ "       ID       (m)   (0 to 1)   Ratio   Parameter  (W/m**2)   Index     ID\r\n"
				+ "     ------   ------   ------    ------  ---------  --------   ------  ------\r\n" + "\r\n";
		for (int i = 0; i < 38; i++)
			tempstr += "! X =   " + value[i][0] + ",     " + value[i][1] + ",    " + value[i][2] + ",     "
					+ value[i][3] + ",     " + value[i][4] + ",      " + value[i][5] + ",      " + value[i][6]
					+ ",     " + value[i][7] + "  !  !END!\r\n";
		tempstr += "\r\n" + "-------------\r\n" + "    a\r\n"
				+ "     Data for each land use category are treated as a separate input\r\n"
				+ "     subgroup and therefore must end with an input group terminator.\r\n" + "\r\n" + "\r\n"
				+ "---------------------------------------------------------------------------\r\n"
				+ "Subgroup (4b) -- Example Values for WINTER Conditions Without Snow Cover --\r\n"
				+ "Alternate     --  (replace non-winter values above with similar values)  --\r\n"
				+ "---------------------------------------------------------------------------\r\n" + "\r\n"
				+ "     Input                                 Soil   Anthropogenic Leaf   Output\r\n"
				+ "    Category    z0     Albedo    Bowen   Heat Flux  Heat Flux   Area  Category\r\n"
				+ "       ID       (m)   (0 to 1)   Ratio   Parameter  (W/m**2)   Index     ID\r\n"
				+ "     ------   ------   ------    ------  ---------  --------   ------  ------\r\n" + "\r\n"
				+ "* X =   11,    0.5,     0.18,     1.0,     0.20,      0.0,      1.0,     10  *  *END*\r\n"
				+ "* X =   12,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   13,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   14,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   15,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   16,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   17,    1.0,     0.18,     1.5,     0.25,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* X =   21,   0.02,     0.18,     0.7,     0.15,      0.0,      3.0,     20  *  *END*\r\n"
				+ "* X =   22,   0.02,     0.18,     0.7,     0.15,      0.0,      3.0,     20  *  *END*\r\n"
				+ "* X =   23,   0.02,     0.18,     0.7,     0.15,      0.0,      3.0,     20  *  *END*\r\n"
				+ "* X =   24,   0.02,     0.18,     0.7,     0.15,      0.0,      3.0,     20  *  *END*\r\n"
				+ "* X =   31,   0.01,     0.20,     1.0,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* X =   32,   0.01,     0.20,     1.0,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* X =   33,   0.01,     0.20,     1.0,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* X =   41,    0.6,     0.17,     1.0,     0.15,      0.0,      7.0,     40  *  *END*\r\n"
				+ "* X =   42,    1.3,     0.12,     0.8,     0.15,      0.0,      7.0,     40  *  *END*\r\n"
				+ "* X =   43,   0.95,     0.14,     0.9,     0.15,      0.0,      7.0,     40  *  *END*\r\n"
				+ "* X =   51,  0.001,      0.1,     0.0,      1.0,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* X =   52,  0.001,      0.1,     0.0,      1.0,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* X =   53,  0.001,      0.1,     0.0,      1.0,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* X =   54,  0.001,      0.1,     0.0,      1.0,      0.0,      0.0,     54  *  *END*\r\n"
				+ "* X =   55,  0.001,      0.1,     0.0,      1.0,      0.0,      0.0,     55  *  *END*\r\n"
				+ "* X =   61,    0.6,     0.14,     0.3,     0.25,      0.0,      2.0,     61  *  *END*\r\n"
				+ "* X =   62,    0.2,     0.14,     0.1,     0.25,      0.0,      1.0,     62  *  *END*\r\n"
				+ "* X =   71,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   72,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   73,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   74,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   75,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   76,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   77,   0.05,      0.2,     1.5,     0.15,      0.0,     0.05,     70  *  *END*\r\n"
				+ "* X =   81,    0.1,      0.2,     1.0,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* X =   82,    0.1,      0.2,     1.0,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* X =   83,    0.1,      0.2,     1.0,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* X =   84,    0.1,      0.2,     1.0,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* X =   85,    0.1,      0.2,     1.0,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* X =   91,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     90  *  *END*\r\n"
				+ "* X =   92,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     90  *  *END*\r\n"
				+ "            \r\n" + "\r\n" + "-------------\r\n" + "Subgroup (4c)    (Read only when LSNOW=T)\r\n"
				+ "-------------\r\n" + "\r\n"
				+ "                                                                      a,b\r\n"
				+ "       SNOW-COVERED LAND USE PROPERTIES AND OUTPUT MAP (NINCAT entries)\r\n"
				+ "       ----------------------------------------------------------------\r\n" + "\r\n"
				+ "     Input                                 Soil   Anthropogenic Leaf   Output\r\n"
				+ "    Category    z0     Albedo    Bowen   Heat Flux  Heat Flux   Area  Category\r\n"
				+ "       ID       (m)   (0 to 1)   Ratio   Parameter  (W/m**2)   Index     ID\r\n"
				+ "     ------   ------   ------    ------  ---------  --------   ------  ------\r\n"
				+ "* XS =  11,    0.5,     0.45,     0.5,     0.15,      0.0,      1.0,     10  *  *END*\r\n"
				+ "* XS =  12,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  13,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  14,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  15,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  16,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  17,    1.0,     0.35,     0.5,     0.15,      0.0,      0.2,     10  *  *END*\r\n"
				+ "* XS =  21,   0.01,      0.7,     0.5,     0.15,      0.0,      0.0,     20  *  *END*\r\n"
				+ "* XS =  22,   0.01,      0.7,     0.5,     0.15,      0.0,      0.0,     20  *  *END*\r\n"
				+ "* XS =  23,   0.01,      0.7,     0.5,     0.15,      0.0,      0.0,     20  *  *END*\r\n"
				+ "* XS =  24,   0.01,      0.7,     0.5,     0.15,      0.0,      0.0,     20  *  *END*\r\n"
				+ "* XS =  31,  0.005,      0.7,     0.5,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* XS =  32,  0.005,      0.7,     0.5,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* XS =  33,  0.005,      0.7,     0.5,     0.15,      0.0,      0.5,     30  *  *END*\r\n"
				+ "* XS =  41,    0.5,      0.5,     0.5,     0.15,      0.0,      0.0,     40  *  *END*\r\n"
				+ "* XS =  42,    1.3,     0.35,     0.5,     0.15,      0.0,      7.0,     40  *  *END*\r\n"
				+ "* XS =  43,    0.9,     0.42,     0.5,     0.15,      0.0,      3.5,     40  *  *END*\r\n"
				+ "* XS =  51,  0.001,      0.7,     0.5,     0.15,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* XS =  52,  0.001,      0.7,     0.5,     0.15,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* XS =  53,  0.001,      0.7,     0.5,     0.15,      0.0,      0.0,     51  *  *END*\r\n"
				+ "* XS =  54,  0.001,      0.7,     0.5,     0.15,      0.0,      0.0,     54  *  *END*\r\n"
				+ "* XS =  55,  0.001,      0.7,     0.5,     0.15,      0.0,      0.0,     55  *  *END*\r\n"
				+ "* XS =  61,    0.5,      0.3,     0.5,     0.15,      0.0,      0.0,     61  *  *END*\r\n"
				+ "* XS =  62,    0.2,      0.6,     0.5,     0.15,      0.0,      0.0,     62  *  *END*\r\n"
				+ "* XS =  71,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  72,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  73,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  74,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  75,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  76,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  77,  0.002,      0.7,     0.5,     0.15,      0.0,      0.0,     70  *  *END*\r\n"
				+ "* XS =  81,  0.005,      0.7,     0.5,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* XS =  82,  0.005,      0.7,     0.5,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* XS =  83,  0.005,      0.7,     0.5,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* XS =  84,  0.005,      0.7,     0.5,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* XS =  85,  0.005,      0.7,     0.5,     0.15,      0.0,      0.0,     80  *  *END*\r\n"
				+ "* XS =  91,   0.05,      0.7,     0.5,     0.15,      0.0,      0.0,     90  *  *END*\r\n"
				+ "* XS =  92,   0.05,      0.7,     0.5,     0.15,      0.0,      0.0,     90  *  *END*\r\n" + "\r\n"
				+ "-------------\r\n" + "    a\r\n"
				+ "     Data for each land use category are treated as a separate input\r\n"
				+ "     subgroup and therefore must end with an input group terminator.\r\n" + "    b\r\n"
				+ "     Subgroup 4c is read only when LSNOW=T.  When LSNOW=F, this section\r\n"
				+ "     must not be active.  To de-activate, change the delimiters to a\r\n"
				+ "     comment marker such as '*'.  To activate, change the delimiters to\r\n"
				+ "     an exclamation point.\r\n" + "\r\n" + "\r\n" + "-------------\r\n"
				+ "Subgroup (4d)    (Read only when LSNOW=T)\r\n" + "-------------\r\n" + "\r\n"
				+ "    Parameters for Snow-Age adjustment to surface albedo\r\n"
				+ "    Snow Time-Scale (days) : Number of days from last snow when snow becomes dirty.\r\n"
				+ "    Albedo Reduction Factor: Reduces albedo when snow becomes dirty. \r\n"
				+ "                             The amount the albedo is reduced is product of this\r\n"
				+ "                             factor and the albedo difference between the surface\r\n"
				+ "                             with and without snow.\r\n" + "\r\n"
				+ "                                                   a,b\r\n"
				+ "    SNOW-AGE TIME SCALE AND ALBEDO REDUCTION FACTOR\r\n"
				+ "    -----------------------------------------------\r\n" + "                  (With snow cover)\r\n"
				+ "                                   \r\n" + "     Input         Snow            Albedo\r\n"
				+ "    Category    Time Scale        Reduction \r\n" + "       ID         (days)           Factor\r\n"
				+ "     ------       ------           ------  \r\n"
				+ "* XF =  11,         7,               0.5 *  *END*\r\n"
				+ "* XF =  12,         7,               0.5 *  *END*\r\n"
				+ "* XF =  13,         7,               0.5 *  *END*\r\n"
				+ "* XF =  14,         7,               0.5 *  *END*\r\n"
				+ "* XF =  15,         7,               0.5 *  *END*\r\n"
				+ "* XF =  16,         7,               0.5 *  *END*\r\n"
				+ "* XF =  17,         7,               0.5 *  *END*\r\n"
				+ "* XF =  21,        14,               0.2 *  *END*\r\n"
				+ "* XF =  22,        14,               0.2 *  *END*\r\n"
				+ "* XF =  23,        14,               0.2 *  *END*\r\n"
				+ "* XF =  24,        14,               0.2 *  *END*\r\n"
				+ "* XF =  31,        14,               0.2 *  *END*\r\n"
				+ "* XF =  32,        14,               0.2 *  *END*\r\n"
				+ "* XF =  33,        14,               0.2 *  *END*\r\n"
				+ "* XF =  41,        14,               0.2 *  *END*\r\n"
				+ "* XF =  42,        14,               0.2 *  *END*\r\n"
				+ "* XF =  43,        14,               0.2 *  *END*\r\n"
				+ "* XF =  51,        14,               0.2 *  *END*\r\n"
				+ "* XF =  52,        14,               0.2 *  *END*\r\n"
				+ "* XF =  53,        14,               0.2 *  *END*\r\n"
				+ "* XF =  54,        14,               0.2 *  *END*\r\n"
				+ "* XF =  55,        14,               0.2 *  *END*\r\n"
				+ "* XF =  61,        14,               0.2 *  *END*\r\n"
				+ "* XF =  62,        14,               0.2 *  *END*\r\n"
				+ "* XF =  71,        14,               0.2 *  *END*\r\n"
				+ "* XF =  72,        14,               0.2 *  *END*\r\n"
				+ "* XF =  73,        14,               0.2 *  *END*\r\n"
				+ "* XF =  74,        14,               0.2 *  *END*\r\n"
				+ "* XF =  75,        14,               0.2 *  *END*\r\n"
				+ "* XF =  76,        14,               0.2 *  *END*\r\n"
				+ "* XF =  77,        14,               0.2 *  *END*\r\n"
				+ "* XF =  81,        14,               0.2 *  *END*\r\n"
				+ "* XF =  82,        14,               0.2 *  *END*\r\n"
				+ "* XF =  83,        14,               0.2 *  *END*\r\n"
				+ "* XF =  84,        14,               0.2 *  *END*\r\n"
				+ "* XF =  85,        14,               0.2 *  *END*\r\n"
				+ "* XF =  91,        14,               0.2 *  *END*\r\n"
				+ "* XF =  92,        14,               0.2 *  *END*\r\n" + "\r\n" + "-------------\r\n" + "    a\r\n"
				+ "     Data for each land use category are treated as a separate input\r\n"
				+ "     subgroup and therefore must end with an input group terminator.\r\n" + "    b\r\n"
				+ "     Subgroup 4d is read only when LSNOW=T.  When LSNOW=F, this section\r\n"
				+ "     must not be active.  To de-activate, change the delimiters to a\r\n"
				+ "     comment marker such as '*'.  To activate, change the delimiters to\r\n"
				+ "     an exclamation point.\r\n" + "\r\n" + "\r\n" + "-------------\r\n" + "Subgroup (4e)\r\n"
				+ "-------------\r\n" + "                                                             a\r\n"
				+ "           INPUT CATEGORIES DEFINED AS WATER (NUMWAT entries)\r\n"
				+ "           --------------------------------------------------\r\n" + "\r\n"
				+ "           ! IWAT =   51  !  !END!\r\n" + "           ! IWAT =   52  !  !END!\r\n"
				+ "           ! IWAT =   53  !  !END!\r\n" + "           ! IWAT =   54  !  !END!\r\n"
				+ "           ! IWAT =   55  !  !END!\r\n" + "\r\n" + "-------------\r\n" + "    a\r\n"
				+ "     Each water category ID is read as a separate input\r\n"
				+ "     subgroup and therefore must end with an input group terminator.\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "-------------\r\n" + "Subgroup (4f)\r\n" + "-------------\r\n"
				+ "                                                         a\r\n"
				+ "           CATEGORY SPLIT INFORMATION (NSPLIT Categories)\r\n"
				+ "           ----------------------------------------------\r\n" + "\r\n"
				+ "            Split        To       Amount\r\n" + "           Category   Category   of Split\r\n"
				+ "              ID         ID        (%)\r\n" + "           --------   --------   --------\r\n"
				+ "\r\n" + "* XSPLIT =    14,        76,       15.8  *  *END*\r\n"
				+ "* XSPLIT =    14,        77,       84.2  *  *END*\r\n" + "\r\n" + "-------------\r\n" + "    a\r\n"
				+ "     Each assignment is read as a separate input subgroup and therefore\r\n"
				+ "     must end with an input group terminator.  A total of NSPLIT input\r\n"
				+ "     land use categories must be listed, and the % split from each one\r\n"
				+ "     of these to all receiving land use categories must sum to 100.0%\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "--------------------------------------------------------------------------------\r\n"
				+ "NIMA Datum-Regions  (Documentation Section)\r\n"
				+ "--------------------------------------------------------------------------------\r\n"
				+ "     WGS-84    WGS-84 Reference Ellipsoid and Geoid, Global coverage (WGS84)\r\n"
				+ "     NAS-C     NORTH AMERICAN 1927 Clarke 1866 Spheroid, MEAN FOR CONUS (NAD27)\r\n"
				+ "     NAR-C     NORTH AMERICAN 1983 GRS 80 Spheroid, MEAN FOR CONUS (NAD83)\r\n"
				+ "     NWS-84    NWS 6370KM Radius, Sphere\r\n"
				+ "     ESR-S     ESRI REFERENCE 6371KM Radius, Sphere\r\n" + "\r\n" + "";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		outStream.close();

		outputText += "--- makegeo 실행 중 ---<br/>";
		content.setText(outputText);
		File LUfile = new File(root + "\\" + LU);
		File Terrfile = new File(root + "\\" + Terrain);
		outputText += "LU path : " + root + "\\" + LU + "<br/>"
				+ "TERRAIN path " + root + "\\" + Terrain + "<br/>";
		content.setText(outputText);
		if (!LUfile.isFile()) {
			System.out.println("No such file here(" + LU + ")");
			outputText += "에러 : LandUse 파일이 없습니다.<br/>";
			content.setText(outputText);
			return;
		}
		if (!Terrfile.isFile()) {
			System.out.println("No such file here(" + Terrain + ")");
			outputText += "에러 : Terrain 파일이 없습니다.<br/>";
			content.setText(outputText);
			return;
		}

		try {
			process = new ProcessBuilder("cmd", "/c", "copy", root + "\\" + Terrain, root + "\\MAKEGEO").start();
			process = new ProcessBuilder("cmd", "/c", "copy", root + "\\" + LU, root + "\\MAKEGEO").start();
			process = new ProcessBuilder("cmd", "/c", "copy", root + "\\makegeo.inp", root + "\\MAKEGEO").start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			process.waitFor();
			process.destroy();
		}

		File make = new File(root + "\\MAKEGEO\\makegeo.bat");
		File makedir = new File(root + "\\MAKEGEO");
//
		String makesrc = make.getAbsolutePath();
		String makedirsrc = makedir.getAbsolutePath();

		if (make.isFile()) {
			try {
				List<String> cmd = new ArrayList<String>();
				cmd.add(makesrc);
				process2 = new ProcessBuilder(cmd);
				process2.directory(new File(makedirsrc));
				process2.start();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.print(" End\n");
				System.out.println("작업을 성공적으로 완료하였습니다.");
				outputText += "완료 <br/></html>";
				content.setText(outputText);
				complete.setVisible(true);
			}
		} else {
			System.out.println("에러 : MAKEGEO 프로그램이 설치 되어있지 않거나 변경하였습니다(makegeo.exe)");
			outputText += "에러 : MAKEGEO 프로그램이 설치 되어있지 않거나 변경하였습니다(makegeo.exe)<br/>";
			content.setText(outputText);
			return;
		}
	}

	@Override
	public void run() {
		try {
			exet();
		} catch (IOException e) {
			System.out.println("에러 : IOException");
			outputText += "에러 : IOException<br/>";
			content.setText(outputText);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("에러 : InterruptedException");
			outputText += "에러 : InterruptedException<br/>";
			content.setText(outputText);
			
		}
		
	}
}
