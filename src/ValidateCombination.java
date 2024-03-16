import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateCombination {

    private final GetAllCombinationsInFiveCards getAllCombinationsInFiveCards = new GetAllCombinationsInFiveCards();

    public boolean checkSequence(List<String> combination, int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        List<Integer> numList = new ArrayList<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numList.add(num);
        }
        return (getAllCombinationsInFiveCards.checkStraight(numList,4)
                && getAllCombinationsInFiveCards.checkMinStraight(numList)
                && getAllCombinationsInFiveCards.checkFlush(fiveCardList));
    }

    public boolean checkStraight(List<String> combination, int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        List<Integer> numList = new ArrayList<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numList.add(num);
        }
        return (getAllCombinationsInFiveCards.checkStraight(numList,4)
                && getAllCombinationsInFiveCards.checkMinStraight(numList));
    }

    public boolean checkFlush(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        return getAllCombinationsInFiveCards.checkFlush(fiveCardList);
    }

    public boolean checkBomb(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        Map<Integer,Integer> numFreqMap = new HashMap<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numFreqMap.put(num,numFreqMap.getOrDefault(num,0) + 1);
        }
        return numFreqMap.size() == 2 && (numFreqMap.containsValue(4));
    }

    public boolean checkFullHouse(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        Map<Integer,Integer> numFreqMap = new HashMap<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numFreqMap.put(num,numFreqMap.getOrDefault(num,0) + 1);
        }
        return numFreqMap.size() == 2 && (numFreqMap.containsValue(3)) && (numFreqMap.containsValue(2));
    }

    public boolean checkThreeCards(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        Map<Integer,Integer> numFreqMap = new HashMap<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numFreqMap.put(num,numFreqMap.getOrDefault(num,0) + 1);
        }
        return numFreqMap.size() == 3 && (numFreqMap.containsValue(3)) && (numFreqMap.containsValue(1));
    }

    public boolean checkTwoPairs(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        Map<Integer,Integer> numFreqMap = new HashMap<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numFreqMap.put(num,numFreqMap.getOrDefault(num,0) + 1);
        }
        return numFreqMap.size() == 3 && (numFreqMap.containsValue(2)) && (numFreqMap.containsValue(1));
    }

    public boolean checkOnePair(List<String> combination,int start, int end){
        List<String> fiveCardList = combination.subList(start,end);
        Map<Integer,Integer> numFreqMap = new HashMap<>();
        for(String card: fiveCardList){
            Integer num = Integer.parseInt(card.substring(1));
            numFreqMap.put(num,numFreqMap.getOrDefault(num,0) + 1);
        }
        return numFreqMap.size() == 4 && (numFreqMap.containsValue(2)) && (numFreqMap.containsValue(1));
    }
}
