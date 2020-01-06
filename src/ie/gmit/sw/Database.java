package ie.gmit.sw;

import java.util.*;

/**
 * The Class Database.
 */
public class Database {
	
	/** The db. */
	//maps languages to their ngram and frequency
	private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();
	
	/**
	 * Adds the.
	 *
	 * @param s the CharSequence
	 * @param lang the Language the user is trying to find
	 * 
	 * sets frequency to 1.
	 * if the LAnguage database contains the kmer then increment the frequency.
	 * else re-insert into map and over write existing kmer with new language entry.
	 */
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
	
	/**
	 * Gets the language entries.
	 *
	 * @param lang the Language the user is trying to find
	 * @return the language entries
	 * 
	 * if database contains language then return map.
	 * else create new map and add to database.
	 */
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
	
	/**
	 * Resize.
	 *
	 * @param max the max kmers of the language for each language
	 * 
	 * gets mapping of integer to their frequency.
	 * and inserts into the map.
	 */
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
	
	/**
	 * Gets the top.
	 *
	 * @param max the max number of kmers
	 * @param lang the language entered
	 * @return the top
	 * 
	 * creates a temporary map and add to a list.
	 * sort the list.
	 * set the rank.
	 * add to treeMap.
	 * if rank is equal to 300 then break else increment rank.
	 */
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		//keep top 300 kmers
		//create temporary map
		Map<Integer, LanguageEntry> temp = new TreeMap<>();
		//add to list
		List<LanguageEntry> langaugeEntrySorted = new ArrayList<>(db.get(lang).values());
		Collections.sort(langaugeEntrySorted);
		
		
		int rank = 1;
		for (LanguageEntry le : langaugeEntrySorted) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);	
			//if rank is equal to 300 then break else increment rank
			if (rank == max) break;
			rank++;
		}
		
		return temp;
	}
	
	/**
	 * Gets the language.
	 *
	 * @param query the query entered
	 * @return the language
	 * 
	 * create a new treeSet and add language name and query.
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = db.keySet();
		for (Language lang : langs) {
			//add into treeSet with language name 
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	/**
	 * Gets the out of place distance.
	 *
	 * @param query the query entered
	 * @param subject the subject rank
	 * @return the out of place distance
	 * 
	 * sort the language entry.
	 * get the language entry in the database.
	 * if it doesnt exist then set distance to 301.
	 * else set distance to subject rank minus the query rank.
	 */
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		Set<LanguageEntry> les = new TreeSet<>(query.values());	
		//sort the language entry
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
	
	/**
	 * The Class OutOfPlaceMetric.
	 */
	private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric>{
		
		/** The lang. */
		//distance between language and query
		private Language lang;
		
		/** The distance. */
		private int distance;
		
		/**
		 * Instantiates a new out of place metric.
		 *
		 * @param lang the language being entered
		 * @param distance the distance
		 */
		//constructor
		public OutOfPlaceMetric(Language lang, int distance) {
			super();
			this.lang = lang;
			this.distance = distance;
		}

		/**
		 * Gets the language.
		 *
		 * @return the language
		 */
		//getter
		public Language getLanguage() {
			return lang;
		}

		/**
		 * Gets the absolute distance.
		 *
		 * @return the absolute distance
		 */
		//if distance is minus then remove minus sign
		public int getAbsoluteDistance() {
			return Math.abs(distance);
		}

		/**
		 * Compare to.
		 *
		 * @param o the o
		 * @return the int
		 */
		//gives in ascending order
		@Override
		public int compareTo(OutOfPlaceMetric o) {
			return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
		}

		/**
		 * To string.
		 *
		 * @return the string
		 */
		//to string method
		@Override
		public String toString() {
			return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
		}
		
		
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
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