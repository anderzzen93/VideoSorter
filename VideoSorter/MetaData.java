package VideoSorter;

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MetaData {
	
	private String name;
	private String genre;
	
	public MetaData(String name, String genre){
		this.name = name;
		this.genre = genre;
	}
	
	public String getName(){
		return name;
	}
	
	public String getGenre(){
		return genre;
	}
	
	public boolean anyMatch(String term){
		return name.toLowerCase().contains(term) || genre.toLowerCase().contains(term);
	}
	
	public void initiate(File f){
		
		this.genre = "";
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			
			writer.write(name);
			writer.newLine();
			writer.write(genre);
			writer.newLine();
			
			writer.close();
			
		} catch(IOException ex){
			
		}
	}
	
	public void read(File f){
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			this.name = reader.readLine();
			this.genre = reader.readLine();
			
			reader.close();
		} catch(IOException ex){
			
		}
	}
}
