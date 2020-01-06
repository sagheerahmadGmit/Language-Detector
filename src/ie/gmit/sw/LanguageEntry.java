/*
 * 
 */
package ie.gmit.sw;

/**
 * The Class LanguageEntry.
 */
public class LanguageEntry implements Comparable<LanguageEntry> {
	
	/** The kmer. */
	//variables
	private int kmer;
	
	/** The frequency. */
	private int frequency;
	
	/** The rank. */
	private int rank;

	/**
	 * Instantiates a new language entry.
	 *
	 * @param kmer the kmer
	 * @param frequency the frequency
	 */
	//constructor
	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}
	
	/**
	 * Gets the kmer.
	 *
	 * @return the kmer
	 */
	//getters and setters
	public int getKmer() {
		return kmer;
	}

	/**
	 * Sets the kmer.
	 *
	 * @param kmer the new kmer
	 */
	public void setKmer(int kmer) {
		this.kmer = kmer;
	}

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequency.
	 *
	 * @param frequency the new frequency
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the rank.
	 *
	 * @param rank the new rank
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * Compare to.
	 *
	 * @param next the next
	 * @return the int
	 */
	//compareTo one ngram to another - compare by frequency in descending order
	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "[" + kmer + "/" + frequency + "/" + rank + "]";
	}
}