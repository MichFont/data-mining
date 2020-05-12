package java.homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class PreProcess {
	public static void main(String [] args)
	{
		String originalFileName = "CombinedDatawithGraduateYN.arff";
		String stage1FileName = "Stage1.arff";
		String stage2FileName = "Stage2.arff";
		String finalFileName = "CleanedProjectData.arff";
		boolean cleanUp = false;
		try {
			if (cleanUp)
			{
				removeDegreeExtensions(originalFileName, stage1FileName);
				System.out.println("Checking file after removing degree types");
				Instances data = check(stage1FileName);
				System.out.println("Passed...");
			}
			else
			{
				System.out.println("Checking file after removing degree types");
				Instances data = check(stage1FileName);
				System.out.println("Passed...");
				fixAcademicStatus(data, stage2FileName);
				System.out.println("Checking file after merging academic status");
				data = check(stage2FileName);
				System.out.println("Passed...");
				removeAttributes(data, finalFileName);
				System.out.println("Checking file after removing unnecessary columns");
				data = check(finalFileName);
				System.out.println("Passed all tests");
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Error: " + e);
		}
	}

	/**
	 * This method fixes the academic standing to only use one system
	 * @param data The current Instances object
	 * @param outputFileName The new arff file name
	 * @throws Exception If writing out the file fails
	 */
	public static void fixAcademicStatus(Instances data, String outputFileName) throws Exception
	{
		// Change the academic standings to use one list only
		// A1,A2 = P2
		// W1,W2 = P1
		// AA = GW
		// AD stays the same
		// GA = GS
		int T1STATUS = 57;
		for (int i= 0; i < data.numInstances(); i++)
		{
			for (int j= T1STATUS; j < 8*26+T1STATUS; j+=26)
			{
				if (data.instance(i).value(j) == 1 || data.instance(i).value(j) == 2)
					data.instance(i).setValue(j, 9);
				else if (data.instance(i).value(j) == 10 || data.instance(i).value(j) == 11)
					data.instance(i).setValue(j, 8);
				else if (data.instance(i).value(j) == 3)
					data.instance(i).setValue(j,7);
				else if (data.instance(i).value(j) == 5)
					data.instance(i).setValue(j,  6);
			}
		}
		// Replace the metadata with a simplified version
		String intermediate = data.toString();
		intermediate  = intermediate.replaceAll("0,A1,A2,AA,AD,GA,GS,GW,P1,P2,W1,W2", "0,AD,GS,GW,P1,P2");
		// Write it out to a new file
		try {
			writeDataString(intermediate, outputFileName);
		} 
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This method writes out a string to file
	 * @param dataString The data as a string that will be printed out
	 * @param fileName The name of the output file
	 * @throws IOException If writing out the file fails
	 */
	public static void writeDataString(String dataString, String fileName) throws IOException
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(dataString);
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * This method removes attributes pertaining to degree and other information we won't need as it's redundant or a string
	 * @param data The Instances object
	 * @param outputFileName The name of the new arff file
	 * @throws Exception If the removing of attributes fails
	 */
	public static void removeAttributes(Instances data, String outputFileName) throws Exception
	{
		Remove remove = new Remove();
		int [] list = {6, 7, 8, 9, 10, 11, 12, 13, 14, 38, 40, 50, 76, 102, 128, 154, 180, 206, 232};
		remove.setAttributeIndicesArray(list);
		remove.setInvertSelection(false);
		try {
			remove.setInputFormat(data);
			Instances instNew = Filter.useFilter(data, remove);
			writeDataString(instNew.toString(), outputFileName);

		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * This method reads in an arff file and returns a pointer to the new data
	 * @param fileName The name of the arff file
	 * @return The new Instances object created
	 * @throws Exception If the parsing of the file fails. 
	 */
	public static Instances check(String fileName) throws Exception
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			//Get the data
			Instances data = new Instances(reader);
			System.out.println(data.numAttributes() + " " + data.numInstances());
			for (int i= 2000; i <2010; i++)
				System.out.println(data.instance(i));
			reader.close();
			return data;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * Method to remove extensions from individual degrees and just replace with degree names
	 * @param inputFileName The original file (must be arff file)
	 * @param outputFileName The new file (will be arff file)
	 */
	public static void removeDegreeExtensions(String inputFileName, String outputFileName)
	{
		String [] stringsToRemove = {"1-AA","1-ADDW","1-BA-ART","1-BA-ARW","1-BA-ATR","1-BA-B-ACT","1-BA-B-BAA","1-BA-B-ENT","1-BA-B-FIN","1-BA-B-HRM","1-BA-B-INT","1-BA-B-MKT","1-BA-B-UND","1-BA-BIA","1-BA-BTS","1-BA-BUE","1-BA-BUP","1-BA-BUS","1-BA-C-ORG","1-BA-C-RES","1-BA-C-RHS","1-BA-CHA","1-BA-CHV","1-BA-CLV","1-BA-COA","1-BA-COH","1-BA-CPM","1-BA-CST","1-BA-DES","1-BA-E-EEN","1-BA-E-EFR","1-BA-E-EMA","1-BA-E-EPE","1-BA-E-EPP","1-BA-E-ESC","1-BA-E-ESP","1-BA-E-ESS","1-BA-ECF","1-BA-ECO","1-BA-EDE","1-BA-EDU","1-BA-ELW","1-BA-ENL","1-BA-ENR","1-BA-ENS","1-BA-ENW","1-BA-EST","1-BA-FRE","1-BA-FRW","1-BA-HIS","1-BA-INF","1-BA-INM","1-BA-INR","1-BA-JRN","1-BA-LIN","1-BA-LSV","1-BA-M-MCR","1-BA-M-PRO","1-BA-MAT","1-BA-MAV","1-BA-MEC","1-BA-MIN","1-BA-MPR","1-BA-MSM","1-BA-MUS","1-BA-O-EMP","1-BA-O-EVE","1-BA-O-STR","1-BA-ORG","1-BA-PED","1-BA-PEV","1-BA-PEW","1-BA-PHI","1-BA-PHV","1-BA-PHY","1-BA-POL","1-BA-PSY","1-BA-REC","1-BA-S-GUM","1-BA-S-HLD","1-BA-S-SCA","1-BA-S-SCM","1-BA-S-SOC","1-BA-SCS","1-BA-SOW","1-BA-SPA","1-BA-SPW","1-BA-SSV","1-BA-T-THD","1-BA-T-THM","1-BA-T-THT","1-BA-TES","1-BA-TFL","1-BA-THA","1-BA-TWS","1-BA-YOM","1-BFA-ARB","1-BMUE-M-MIE","1-BMUE-M-MVE","1-BMUE-MUW","1-BMUS-M-MPC","1-BMUS-M-MPI","1-BMUS-M-MPV","1-BMUS-MUA","1-BS-AFN","1-BS-BCS","1-BS-BIS","1-BS-CHS","1-BS-COS","1-BS-EVS","1-BS-EXS","1-BS-K-BES","1-BS-K-BHB","1-BS-NUR","1-BS-P-MCN","1-BS-PHA","1-BS-PHS","1-CIS","1-CONSORTIUM","1-NOND","1-PSEO","1-UND","2-BA-HRMP","2-CRT-CIBE","2-LIC-T-TLNG","2-MA-TEAG","2-SPECG","3-MATS-HS-SP","3-MDG-AG-SP","3-SPEC-SP"};
		String [] stringsToAdd = {"1-AA","1-ADDW","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BA","1-BFA","1-BMUE","1-BMUE","1-BMUE","1-BMUS","1-BMUS","1-BMUS","1-BMUS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-BS","1-CIS","1-CONSORTIUM","1-NOND","1-PSEO","1-UND","2-BA","2-CRT","2-LIC","2-MA","2-SPECG","3-MATS","3-MDG","3-SPEC"};
		try
		{
			Scanner input =new Scanner(new File(inputFileName));
			input.useDelimiter(",");

			String inputString = "";
			String newString = "";
			while (input.hasNext())
			{
				inputString = input.next();
				boolean flag =false;
				for (int j =0 ; j < stringsToRemove.length; j++)
				{
					if (inputString.equals(stringsToRemove[j]))
					{
						System.out.println(inputString);
						newString = newString + stringsToAdd[j] + ",";
						flag = true;
						break; 
					}
				}
				if (!flag)
					newString = newString + inputString + ",";
			}
			newString = newString.replaceAll("0,1-AA,1-ADDW,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BA,1-BFA,1-BMUE,1-BMUE,1-BMUE,1-BMUS,1-BMUS,1-BMUS,1-BMUS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-BS,1-CIS,1-CONSORTIUM,1-NOND,1-PSEO,1-UND,2-BA,2-CRT,2-LIC,2-MA,2-SPECG,3-MATS,3-MDG,3-SPEC-SP",  "0,1-AA,1-ADDW,1-BA,1-BFA,1-BMUE,1-BMUS,1-BS,1-CIS,1-CONSORTIUM,1-NOND,1-PSEO,1-UND,2-BA,2-CRT,2-LIC,2-MA,2-SPECG,3-MATS,3-MDG,3-SPEC");
			newString = newString.replace("0,FF,FR,JR,SO,SR", "FF,FR,JR,SO,SR");
			newString = newString.replace("0,N,P,R,T", "N,P,R,T");
			newString = newString.replace("0,PV,RG,SP","PV,RG,SP"); 

			writeDataString(newString, outputFileName);
					}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
}
