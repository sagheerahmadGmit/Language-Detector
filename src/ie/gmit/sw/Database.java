package ie.gmit.sw;

import java.util.*;

public class Database {
	
	//maps languages to their ngram and frequency
	private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();
	
	//
	public void add(CharSequence s, Language lang) {
		
		//convert kmer into hashcode
		int kmer = s.hashCode();
		
		//call the getLanguageEntries method to return the language
		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);
		
		int frequency = 1;
		
		//if it contains the kmer then increment the frequency
		if (langDb.containsKey(kmer)) {
			frequency += langDb.get(kmer).getFrequency();
		}
		//re-insert into map
		//over write existing kmer with new language entry
		langDb.put(kmer, new LanguageEntry(kmer, frequency));
		
	}
	
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
		Map<Integer, LanguageEntry> langDb = null; 
		//if database contains language then return map else create new map and add to database
		if (db.containsKey(lang)) {
			langDb = db.get(lang);
		}else {
			langDb = new TreeMap<Integer, LanguageEntry>();
			db.put(lang, langDb);
		}
		return langDb;
	}
	
	public void resize(int max) {
		//for each language
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			//get mapping of integer to their language
			Map<Integer, LanguageEntry> top = getTop(max, lang);
			//re-insert into map
			db.put(lang, top);
		}
	}
	
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		//keep top 300 kmers
		//create temporary map
		Map<Integer, LanguageEntry> temp = new TreeMap<>();
		//add to list
		List<LanguageEntry> les = new ArrayList<>(db.get(lang).values());
		Collections.sort(les);
		
		
		int rank = 1;
		for (LanguageEntry le : les) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);	
			//if rank is equal to 300 then break else increment rank
			if (rank == max) break;
			rank++;
		}
		
		return temp;
	}
	
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = db.keySet();
		for (Language lang : langs) {
			//add into treeset with language name 
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		Set<LanguageEntry> les = new TreeSet<>(query.values());	
		//sorth the language entry
		for (LanguageEntry q : les) {
			//get the language entry in the database
			LanguageEntry s = subject.get(q.getKmer());
			//if it doesnt exist then set distance to 301
			if (s == null) {
				distance += subject.size() + 1;
			}else {
				//set distance to subject rank minus the query rank
				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}
	
	private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric>{
		//distance between language and query
		private Language lang;
		private int distance;
		
		//constructor
		public OutOfPlaceMetric(Language lang, int distance) {
			super();
			this.lang = lang;
			this.distance = distance;
		}

		//getter
		public Language getLanguage() {
			return lang;
		}

		//if distance is minus then remove minus sign
		public int getAbsoluteDistance() {
			return Math.abs(distance);
		}

		//gives in ascending order
		@Override
		public int compareTo(OutOfPlaceMetric o) {
			return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
		}

		//to string method
		@Override
		public String toString() {
			return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
		}
		
		
	}
	
	//to string method
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		int langCount = 0;
		int kmerCount = 0;
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			langCount++;
			sb.append(lang.name() + "->\n");
			 
			 Collection<LanguageEntry> m = new TreeSet<>(db.get(lang).values());
			 kmerCount += m.size();
			 for (LanguageEntry le : m) {
				 sb.append("\t" + le + "\n");
			 }
		}
		sb.append(kmerCount + " total k-mers in " + langCount + " languages"); 
		return sb.toString();
	}
}