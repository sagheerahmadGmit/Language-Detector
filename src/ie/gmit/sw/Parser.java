package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Parser implements Runnable{
	
	//variables
	private Database db = null;
	private String file;
	private int k;
	
	//constructor
	public Parser(String file, int k)
	{
		this.file = file;
		this.k = k;
	}
	
	//set the database
	public void setDb(Database db)
	{
		this.db = db;
	}
	
	@Override
	public void run()
	{
		try {
			//use bufferReader to read the file
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			
			while((line = br.readLine()) != null)
			{
				//split each line based on the @ symbol into an arrayList
				String [] record = line.trim().split("@");
				if(record.length != 2)continue;
				parse(record[0], record[1]);
			}
			
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse(String text, String lang, int...ks )
	{
		//maps string lang to enum language - must match exactly
		Language l = Language.valueOf(lang);
		
		//loop over the sequence - chunks of k
		for(int i=0; i<text.length() - k; i++)
		{
			//get kmer
			//read the substring from i to i+k
			CharSequence kmer = text.substring(i,i+k);
			//add to map 
			db.add(kmer, l);
		}
	}

	public Language analyseQuery(String file) throws IOException{
		
		int ngram = 0;
		int freq = 1;
		String queryFile;
		
		Map<Integer, LanguageEntry> query = new HashMap<>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		//if the file is not empty keep running while loop
		while ((queryFile = br.readLine()) != null) { 

			//decides how big the ngram is - from length of the file
			for (int i = 0; i <= queryFile.length() - k; i++) {
				
				//gets the kmer - read the substring from i to i+k
				CharSequence sequenceChar = queryFile.substring(i, i + k);
				//convert sequenceChar to hashcode and save as int
				ngram = sequenceChar.hashCode();
				
				//if the hashmap contains the ngram then increment the frequency 
				if (query.containsKey(ngram)) {
					freq += query.get(ngram).getFrequency();
				}
				
				//call the language entry constructor to set the ngram and frequency
				LanguageEntry l = new LanguageEntry(ngram, freq);
				//add it to the hashmap
				query.put(ngram, l);
				
			}

		}
		//get the language and return it
		Language language = db.getLanguage(query);
		return language;
		
	}

}
