package VideoSorter;

import java.io.File;
import java.io.IOException;

public abstract class Video {

	protected String path;
	protected String name;
	protected MetaData metaData;
	public Video(String path){
		this.path = path;
		this.name = getFileName();
		this.metaData = new MetaData(name, "");
		updateMetaData();
	}
	
	public MetaData getMetaData(){
		return this.metaData;
	}
	
	private String getFileName(){
		return path.substring(path.lastIndexOf((int)'\\') + 1, path.length());
	}
	
	private void updateMetaData(){
		File metaDataDirectory = new File(System.getProperty(""));
		
		metaDataDirectory.mkdirs();
		
		if (metaDataDirectory.exists()){
			File metaDataFile = new File(System.getProperty("") + name + ".mdata");
			if (!metaDataFile.exists()){
				try{
					metaDataFile.createNewFile();
				} catch(IOException ex){
					
				}
				metaData.initiate(metaDataFile);
			}else{
				metaData.read(metaDataFile);
			}
			
		} else{
			//Gick inte att skapa/öppna mappen
		}
	}
	
	@Override
	public boolean equals(Object other){
		return this.path == ((Video)other).path;
	}
	
	@Override
	public int hashCode(){
		return this.path.hashCode();
	}
}
