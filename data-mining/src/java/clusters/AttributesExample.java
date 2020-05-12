package java.clusters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

public class AttributesExample {

	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Weather.arff"));
			Instances data = new Instances(reader);
			reader.close();

			writeArffFile(data, "NewWeather.arff");
			
			insertAttribute(data);
			writeArffFile(data, "WithNewAttrib.arff");
			
		} catch (Exception e) {
			System.out.println("Error: " +  e);
		}
	}

	/*
	 * This method creates a new attribute instance
	 */
	public static void insertAttribute(Instances data) {
		ArrayList<String> listOfValues = new ArrayList<String>();
		listOfValues.add("Freshman");
		listOfValues.add("Sophmore");
		listOfValues.add("Junior");
		listOfValues.add("Seniors");
		Attribute year = new Attribute("YearInSchool", listOfValues);
		data.insertAttributeAt(year, 1);
		for(int i = 0; i <  data.numInstances(); i++) {
			data.instance(i).setValue(1, 0.0);
		}
	}

	/*
	 * This method writes a new .arff
	 */
	public static void writeArffFile(Instances data, String fileName) throws Exception {
		try { 
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(data.toString());
			writer.flush();//Write everything out and then close the file stream (Needed if data is really long)
			writer.close();
		} catch(Exception e) {
			throw e;
		}
		
	}

}
