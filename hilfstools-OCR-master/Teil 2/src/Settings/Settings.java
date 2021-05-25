package Settings;

import Classes.Data.DataLine;
import Classes.Data.DataLineType;
import Classes.Filters.FilterSet;
import Classes.Filters.FilterSets.EntryCounterFilterSet;
import Classes.Filters.FilterSets.LetterCounterFilterSet;
import Classes.Filters.FilterSets.TypeCounterFilterSet;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Singleton.
 * Holds all settings for the App publically.
 */
public class Settings {
	// Singleton-code
	private static Settings instance = null;

	/**
	 * Gets the current Settings instance.
	 *
	 * @return Settings instance
	 */
	public static Settings getInstance()
	{
		if(Settings.instance == null)
			Settings.instance = new Settings();
		
		return Settings.instance;
	}

	/**
	 * Initializes the Settings Object.
	 */
	private Settings()
	{
		// init dataFilters
		this.dataFilters.put("entryCounter", new EntryCounterFilterSet());
		this.dataFilters.put("typeCounter", new TypeCounterFilterSet());
		this.dataFilters.put("letterCounter", new LetterCounterFilterSet());
	}

	/**
	 * List of <Name, FilterSet> entries containing all filters to be applied to StatsLog/MovePreview.
	 * Initialized in Settings()-constructor.
	 */
	public HashMap<String, FilterSet<DataLine>> dataFilters = new HashMap<>();

	/**
	 * Distribution percentage for Train set.
	 */
	public float simpleDistributionTrainPercentage = 0.85f;

	/**
	 * List of DataLineTypes that should be fully forced into Train set.
	 */
	public LinkedList<DataLineType> forceFullTrain = new LinkedList<>();

	/**
	 * Specifies, if CharacterCompenstation should be used.
	 */
	public boolean useCharacterCompensation = false;

	/**
	 * Threshold, when a FilterResult's value is seen as notable (orange),
	 */
	public int highlightCriticalValuesNoticeThreshold = 5;

	/**
	 * Threshold, when a FilterResult's value is seen as critical (red).
	 * Also used for CharacterCompensation.
	 */
	public int highlightCriticalValuesWarningThreshold = 3;
}