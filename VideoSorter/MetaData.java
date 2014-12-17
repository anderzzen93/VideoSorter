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
	/*
	private String director;
	private String year;
	private String rating;
	private String length; */
	
	public MetaData(String name, String genre){
		this.name = name;
		this.genre = genre;
	}
/*	public MetaData(String name, String genre, String director, String year, String rating, String length){
		this.name = name;
		this.genre = genre;
		this.director = director;
		this.year = year;
		this.rating = rating;
		this.length = length; 
	}
*/	
	public String getName(){
		return name;
	}
	
	public String getGenre(){
		return genre;
	}
	
/*	
	public String getDirector(){
		return director;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getRating(){
		return rating;
	}
	
	public String getLength(){
		return length;
	}
*/
	
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
		/*	writer.write(director);
			writer.newLine();
			writer.write(year);
			writer.newLine();
			writer.write(rating);
			writer.newLine();
			writer.write(length);
			writer.newLine();
			*/
			writer.close();
			
		} catch(IOException ex){
			
		}
	}
	
	public void read(File f){
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			this.name = reader.readLine();
			this.genre = reader.readLine();
			/*this.director = reader.readLine();
			this.year = reader.readLine();
			this.rating = reader.readLine();
			this.length = reader.readLine();
			*/
			reader.close();
		} catch(IOException ex){
			
		}
	}
}
