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
	
	private Database db = null;
	private String file;
	private int k;
	
	public Parser(String file, int k)
	{
		this.file = file;
		this.k = k;
	}
	
	public void setDb(Database db)
	{
		this.db = db;
	}
	
	@Override
	public void run()
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			
			while((line = br.readLine()) != null)
			{
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
		Language l = Language.valueOf(lang);
		
		for(int i=0; i<text.length() - k; i++)
		{
			//get 3-mer
			CharSequence kmer = text.substring(i,i+k);
			db.add(kmer, l);
		}
	}

	public Map<Integer, LanguageEntry> analyseQuery(String file) throws IOException{
		int ngram = 0;
		int freq = 1;
		String queryFile;
		
		Map<Integer, LanguageEntry> query = new HashMap<>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		while ((queryFile = br.readLine()) != null) { 

			for (int i = 0; i <= queryFile.length() - k; i++) {
				CharSequence sequenceChar = queryFile.substring(i, i + k);
				ngram = sequenceChar.hashCode();
			}
			
			if (query.containsKey(ngram)) {
				freq += query.get(ngram).getFrequency();
			}

			LanguageEntry l = new LanguageEntry(ngram, freq);
			query.put(ngram, l);
		}
		return query;
	}

}
