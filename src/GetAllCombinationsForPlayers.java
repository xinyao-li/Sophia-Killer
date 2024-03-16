import java.util.*;

public class GetAllCombinationsForPlayers {
    /***
     random:    0 point
     pairOne:   1 point
     pairTwo:   2 points
     three:     3 points
     straight:  4 points
     flush:     5 points
     fullHouse: 6 points
     bomb:      10 points
     sequence:  11 points
     ***/
    private final GetAllCombinationsInFiveCards getAllCombinationsInFiveCards = new GetAllCombinationsInFiveCards();
    private final Map<List<String>,Integer> rankedMap;

    private final List<List<String>> fiveCardsCombinationRankedLists;

    private final String[] deck;
    public GetAllCombinationsForPlayers(String[] deck){
        this.deck = deck;
        rankedMap = new HashMap<>();
        fiveCardsCombinationRankedLists = getAllCombinationsInFiveCards.rankCombinationInFiveCards(deck);
        for(int i = 0; i < fiveCardsCombinationRankedLists.size();i++){
            rankedMap.put(fiveCardsCombinationRankedLists.get(i),i);
        }
    }
    public List<List<String>> getRankedValidCombinationsForPlayers(List<List<String>> allCombinationsForPlayers){
        ValidateCombination validator = new ValidateCombination();
        Map<String,Integer> scoreMap = new HashMap<>();
        scoreMap.put("random",0);
        scoreMap.put("pairOne",1);
        scoreMap.put("pairTwo",2);
        scoreMap.put("three",3);
        scoreMap.put("straight",4);
        scoreMap.put("flush",5);
        scoreMap.put("fullHouse",6);
        scoreMap.put("bomb",10);
        scoreMap.put("sequence",11);

        Map<List<String>,Integer> combinationScoreMap = new HashMap<>();
        List<List<String>> validCombinationsList = new ArrayList<>();
        int[] scores = new int[3];
        for(List<String> combination: allCombinationsForPlayers){
            if(validator.checkThreeCards(combination,0,3)){
                scores[0] = scoreMap.get("three");
            }else if(validator.checkOnePair(combination,0,3)){
                scores[0] = scoreMap.get("pairOne");
            }
            impScoreMapInFiveCards(validator,scoreMap,scores,combination,3,8,1);
            impScoreMapInFiveCards(validator,scoreMap,scores,combination,8,13,2);

            int totalScores = 0;
            if(scores[0] == 1){
                totalScores++;
            }else if(scores[0] == 3){
                totalScores += 2;
            }
            totalScores += scores[2] + scores[1] +scores[0];
            if((scores[2] > scores[1] && scores[1] > scores[0]) || (scores[2] > scores[1] && scores[1] == scores[0])){
                combinationScoreMap.put(combination,totalScores);
            }else if(scores[2] == scores[1] && scores[1] > scores[0]){
                if(rankedMap.getOrDefault(combination.subList(8,13),100) > rankedMap.getOrDefault(combination.subList(3,8),100)){
                    combinationScoreMap.put(combination,totalScores);
                }
            }
        }

        Comparator<List<String>> compareCombination = new Comparator<>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                return combinationScoreMap.getOrDefault(o2,0) - combinationScoreMap.getOrDefault(o1,0);
            }
        };

        Collections.sort(allCombinationsForPlayers,compareCombination);
        return allCombinationsForPlayers;
    }

    public void impScoreMapInFiveCards(ValidateCombination validator,Map<String,Integer> scoreMap, int[] scores, List<String> combination, int start, int end, int scoreIndex){
        if(validator.checkSequence(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("sequence");
        }
        else if(validator.checkBomb(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("bomb");
        }
        else if(validator.checkFullHouse(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("fullHouse");
        }
        else if(validator.checkFlush(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("flush");
        }
        else if(validator.checkStraight(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("flush");
        }
        else if(validator.checkThreeCards(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("three");
        }
        else if(validator.checkTwoPairs(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("pairTwo");
        }
        else if(validator.checkOnePair(combination,start,end)){
            scores[scoreIndex] = scoreMap.get("pairOne");
        }
    }
    public List<List<String>> getAllCombinationsForPlayers(){
        List<List<String>> allCombinationsForPlayers = new ArrayList<>();
        for(int i = 0; i < this.fiveCardsCombinationRankedLists.size();i++){
            List<String> fiveCardsCombinationRankedList = this.fiveCardsCombinationRankedLists.get(i);
            Set<String> cardSet = new HashSet<>();
            cardSet.addAll(fiveCardsCombinationRankedList);
            for(int j = i + 1; j < this.fiveCardsCombinationRankedLists.size();j++){
                List<String> combination = new ArrayList<>();
                if(hasNoDuplicatedCard(cardSet,this.fiveCardsCombinationRankedLists.get(j))){
                    List<String> threeCardList = getThreeCardsList(cardSet,this.fiveCardsCombinationRankedLists.get(j),deck);

                    combination.addAll(threeCardList);
                    combination.addAll(this.fiveCardsCombinationRankedLists.get(j));
                    combination.addAll(fiveCardsCombinationRankedList);
                    allCombinationsForPlayers.add(new ArrayList<>(combination));
                }
            }
        }
        return allCombinationsForPlayers;
    }
    public boolean hasNoDuplicatedCard(Set<String> cardSet, List<String> cardList){
        for(String card: cardList){
            if(cardSet.contains(card)){
                return false;
            }
        }
        return true;
    }

    public List<String> getThreeCardsList(Set<String> cardSet, List<String> cardList, String[] deck){
        List<String> threeCardsList = new ArrayList<>();
        Set<String> cardSet2 = new HashSet<>();
        cardSet2.addAll(cardList);
        for(String card: deck){
            if(!cardSet.contains(card) && !cardSet2.contains(card)){
                threeCardsList.add(card);
            }
        }
        return threeCardsList;
    }
}
