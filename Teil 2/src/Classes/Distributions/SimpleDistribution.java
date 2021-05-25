package Classes.Distributions;

import Classes.Data.Collection;
import Classes.Data.DataLine;
import Classes.Filters.FilterResult;
import Settings.Settings;

import java.util.List;

/**
 * Distribution for automatically seperating an input Collection<DataLine> into trainSet and testSet.
 *
 * Procedure:
 * 1. Splits input into trainSet & testSet by percentage, forcing all Types set as "force 100% train" into train set.
 * 2. If CharacterCompensation is enabled, all DataLines containing chars not contained in the trainSet are moved to the trainSet.
 *    After, DataLines not affecting critical chars are moved back to testSet to maintain the distribution percentages.
 */
public class SimpleDistribution {
	/**
	 * Performs the distribution.
	 *
	 * @param input Input DataLines.
	 * @param trainSet trainSet DataLines output. May not be null. Will be cleared.
	 * @param testSet testSet DataLines output. May not be null. Will be cleared.
	 */
	public void performDistribution(Collection<DataLine> input, Collection<DataLine> trainSet, Collection<DataLine> testSet)
	{
		Settings settings = Settings.getInstance();
		Collection<DataLine> freelyDistributable = new Collection<>(input, i -> !settings.forceFullTrain.contains(i.getType()));

		trainSet.clear();
		testSet.clear();

		trainSet.add(input, i -> settings.forceFullTrain.contains(i.getType()));

		double trainPercentage = settings.simpleDistributionTrainPercentage;
		int inputSize = input.size();
		int trainSize = (int) Math.round(inputSize * trainPercentage);
		int testSize = inputSize - trainSize;

		// main distribution loop
		for (DataLine item : freelyDistributable ) {
			if(Math.random() <= trainPercentage) {
				if (trainSet.size() < trainSize)
					trainSet.add(item);
				else 
					testSet.add(item);
			} else {
				if (testSet.size() < testSize) 
					testSet.add(item);
				else
					trainSet.add(item);
			}
		}

		// compensate rare chars by moving them to the Train Set
		if(settings.useCharacterCompensation) {
			List<FilterResult> filterResults = settings.dataFilters.get("letterCounter").apply(trainSet);

			for(FilterResult result: filterResults) {
				if(result.getValue() < settings.highlightCriticalValuesWarningThreshold) {
					String chr = result.getName().substring(1);
					int currVal = result.getValue();

					for(int i = testSet.size() - 1; i >= 0; i--) {
						DataLine line =  testSet.get(i);

						if(currVal < settings.highlightCriticalValuesWarningThreshold && line.getText().contains(chr)) {
							currVal += line.getText().length() - line.getText().replace(chr, "").length();
							trainSet.add(line);
							testSet.remove(line);
						}
					}
				}
			}

			// regain initial distrib. percentage
			Collection<FilterResult> filterResultsErrorsList = new Collection<FilterResult>(settings.dataFilters.get("letterCounter").apply(trainSet), FilterResult::isSevere);
			int filterResultErrors = filterResultsErrorsList.size();
			int filterResultErrorsSevereCount = 0;
			for(FilterResult res : filterResultsErrorsList)
				filterResultErrorsSevereCount += res.getValue();

			for (DataLine item : freelyDistributable) {
				if(trainSet.size() <= trainSize)
					break;

				Collection<DataLine> probeTrainSet = new Collection<>(trainSet, i -> i != item);
				Collection<FilterResult> probeFilterResultErrorsList = new Collection<FilterResult>(settings.dataFilters.get("letterCounter").apply(probeTrainSet), FilterResult::isSevere);
				int probeFilterResultErrors = probeFilterResultErrorsList.size();
				int probeFilterResultErrorsSevereCount = 0;
				for(FilterResult res : probeFilterResultErrorsList)
					probeFilterResultErrorsSevereCount += res.getValue();

				if(probeFilterResultErrors <= filterResultErrors && filterResultErrorsSevereCount == probeFilterResultErrorsSevereCount) {
					trainSet.remove(item);
					testSet.add(item);
				}
			}
		}
	}
}