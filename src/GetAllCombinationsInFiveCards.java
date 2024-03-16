import java.util.*;

class GetAllCombinationsInFiveCards {
    public List<List<String>> rankCombinationInFiveCards(String[] deck) {

        //rankedList is for final return for all combinations in five cards
        List<List<String>> rankedList = new ArrayList<>();
        Map<Character, List<Integer>> colorMap = new HashMap<>();
        List<Integer> numbers = new ArrayList<>();

        colorMapAndNumberImp(colorMap,numbers,deck);

        Map<Integer,List<String>> numberMap = new TreeMap<>();
        numberMapImp(numberMap,numbers,deck);

        //find all combinations of input deck
        List<List<String>> allCombinations = new ArrayList<>();
        List<String> comb = new ArrayList<>();
        List<String> deckList = Arrays.asList(deck);
        backtrackForAllCombination(allCombinations,comb,deckList,0);

        //Add sequence in rankedList
        addSequenceInRankedList(colorMap,rankedList);

        //Add bombs in rankedList
        List<Integer> numberByKey = new ArrayList<>(numberMap.keySet());
        Collections.reverse(numberByKey);
        addBombInRankedList(numberByKey,numberMap,rankedList);

        //Add fullHouse
        for(Integer num: numberByKey){
            List<String> cards = numberMap.get(num);
            if(cards.size() == 3){
                addFullHouseInList(rankedList,cards,numberByKey,numberMap,num,0,1,2);
            }
            else if(cards.size() == 4){
                addFullHouseInList(rankedList,cards,numberByKey,numberMap,num,0,1,2);
                addFullHouseInList(rankedList,cards,numberByKey,numberMap,num,0,1,3);
                addFullHouseInList(rankedList,cards,numberByKey,numberMap,num,0,2,3);
                addFullHouseInList(rankedList,cards,numberByKey,numberMap,num,1,2,3);
            }
        }
        //Add flush
        addFlushInRankedList(colorMap,rankedList);

        //Add straight
        addStraightInRankedList(numberByKey,numberMap,rankedList);

        //Add three cards without pairs
        List<List<String>> threeWithoutPairList = addThreeWithoutPair(allCombinations);
        for(List<String> threeWithoutPair: threeWithoutPairList){
            rankedList.add(new ArrayList<>(threeWithoutPair));
        }

        //Add two pairs

        List<List<String>> twoPairsList = createTwoPairsList(allCombinations);
        rankedTwoPairsList(twoPairsList);

        for(List<String> twoPairs:twoPairsList){
            rankedList.add(new ArrayList<>(twoPairs));
        }

        // Add one pair
        // Create an unsorted one Pair List
        List<List<String>> onePairList = createOnePairList(allCombinations);
        //Ranking Pair in a List
        rankedOnePairList(onePairList);

        //Add in rankedList
        for(List<String> pairList:onePairList){
            rankedList.add(new ArrayList<>(pairList));
        }

        //Add random
        List<List<String>> randomLists = getRandomDeck(allCombinations);
        rankedRandomDeck(randomLists);
        //Add in rankedList
        for(List<String> randomList:randomLists){
            rankedList.add(new ArrayList<>(randomList));
        }
        return rankedList;
    }

    public void rankedRandomDeck(List<List<String>> randomList){
        Comparator<List<String>> compareRandomList = new Comparator<>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                List<Integer> numList1 = new ArrayList<>();
                List<Integer> numList2 = new ArrayList<>();

                for (int i = 0; i < o1.size(); i++) {
                    numList1.add(Integer.parseInt(o1.get(i).substring(1)));
                    numList2.add(Integer.parseInt(o2.get(i).substring(1)));
                }

                Collections.sort(numList1);
                Collections.sort(numList2);

                for (int i = numList1.size() - 1; i >= 0; i--) {
                    int result = Integer.compare(numList2.get(i), numList1.get(i));
                    if (result != 0) {
                        return result;
                    }
                }

                return 0;
            }
        };

        Collections.sort(randomList,compareRandomList);
    }

    public List<List<String>> getRandomDeck(List<List<String>> allCombinations){
        List<List<String>> randomList = new ArrayList<>();
        for(List<String> combination: allCombinations){
            Map<Integer,Integer> numberFreqMap = new HashMap<>();
            List<Integer> numberList = new ArrayList<>();
            for(String card: combination){
                Integer num = Integer.parseInt(card.substring(1));
                numberFreqMap.put(num,numberFreqMap.getOrDefault(num,0) + 1);
                numberList.add(num);
            }

            if(numberFreqMap.size() == 5 && !checkFlush(combination) &&!checkStraight(numberList,4)){
                randomList.add(new ArrayList<>(combination));
            }
        }

        return randomList;
    }

    public boolean checkFlush(List<String> cardList){
        return cardList.get(0).charAt(0) == cardList.get(1).charAt(0)
                && cardList.get(1).charAt(0) == cardList.get(2).charAt(0)
                && cardList.get(2).charAt(0) == cardList.get(3).charAt(0)
                && cardList.get(3).charAt(0) == cardList.get(4).charAt(0);
    }
    public List<List<String>> createOnePairList(List<List<String>> allCombinations){
        List<List<String>> onePairList = new ArrayList<>();
        for(List<String> combination: allCombinations){
            if(checkHasPairs(combination,1)){
                onePairList.add(new ArrayList<>(combination));
            }
        }
        return onePairList;
    }
    public void rankedOnePairList(List<List<String>> onePairList){
        List<Map<Integer,Integer>> numberList = new ArrayList<>();
        Map<List<String>,Integer> pairListIndexMap = new HashMap<>();
        impNumberListAndPairListIndexMap(onePairList,numberList,pairListIndexMap);

        List<Map<Integer,Integer>> sortedNumberList = generateSortedCard(numberList);

        List<List<Integer>> sortedCardInNum = new ArrayList<>();
        for(Map<Integer,Integer> map:sortedNumberList) {
            List<Integer> tempList = new ArrayList<>();
            for(Map.Entry<Integer,Integer> entry:map.entrySet()){
                tempList.add(entry.getKey());
            }
            int pair = tempList.get(tempList.size() - 1);
            tempList.remove(tempList.size() - 1);
            Collections.sort(tempList);
            tempList.add(pair);

            sortedCardInNum.add(new ArrayList<>(tempList));
        }
        Comparator<List<String>> comparePair = new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                List<Integer> list1 = sortedCardInNum.get(pairListIndexMap.get(o1));
                List<Integer> list2 = sortedCardInNum.get(pairListIndexMap.get(o2));

                if(list1.get(3) > list2.get(3)){
                    return -1;
                }else if(list1.get(3) < list2.get(3)){
                    return 1;
                }
                if(list1.get(2) > list2.get(2)){
                    return -1;
                }else if(list1.get(2) < list2.get(2)){
                    return 1;
                }
                if(list1.get(1) > list2.get(1)){
                    return -1;
                }else if(list1.get(1) < list2.get(1)){
                    return 1;
                }
                if(list1.get(0) > list2.get(0)){
                    return -1;
                }else if(list1.get(0) < list2.get(0)){
                    return 1;
                }
                return 0;
            }
        };

        Collections.sort(onePairList,comparePair);
    }
    public void rankedTwoPairsList(List<List<String>> twoPairsList){
        List<Map<Integer,Integer>> numberList = new ArrayList<>();
        Map<List<String>,Integer> pairListIndexMap = new HashMap<>();
        impNumberListAndPairListIndexMap(twoPairsList,numberList,pairListIndexMap);
        List<Map<Integer,Integer>> sortedNumberList = generateSortedCard(numberList);
        List<List<Integer>> sortedCardInNum = new ArrayList<>();
        for(Map<Integer,Integer> map:sortedNumberList){
            List<Integer> tempList = new ArrayList<>();
            for(Map.Entry<Integer,Integer> entry:map.entrySet()){
                tempList.add(entry.getKey());
            }
            if(tempList.get(1) > tempList.get(2)){
                Integer temp = tempList.get(2);
                tempList.remove(2);
                tempList.add(1,temp);
            }
            sortedCardInNum.add(new ArrayList<>(tempList));
        }

        Comparator<List<String>> comparePairs = new Comparator<>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                List<Integer> list1 = sortedCardInNum.get(pairListIndexMap.get(o1));
                List<Integer> list2 = sortedCardInNum.get(pairListIndexMap.get(o2));
                if(list1.get(2) > list2.get(2)){
                    return -1;
                }else if(list1.get(2) < list2.get(2)){
                    return 1;
                }
                if(list1.get(1) > list2.get(1)){
                    return -1;
                }else if(list1.get(1) < list2.get(1)){
                    return 1;
                }
                if(list1.get(0) > list2.get(0)){
                    return -1;
                }else if(list1.get(0) < list2.get(0)) {
                    return 1;
                }

                return 0;
            }
        };
        Collections.sort(twoPairsList,comparePairs);
    }
    public List<Map<Integer,Integer>> generateSortedCard(List<Map<Integer,Integer>> numberList){
        List<Map<Integer,Integer>> sortedNumberList = new ArrayList<>();
        for (Map<Integer, Integer> numMap : numberList) {
            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(numMap.entrySet());

            // Sort the list based on the map values
            entryList.sort(Map.Entry.comparingByValue());

            // Create a new LinkedHashMap to maintain the sorted order
            Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<Integer, Integer> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            sortedNumberList.add(sortedMap);
        }
        return sortedNumberList;
    }
    public void impNumberListAndPairListIndexMap(List<List<String>> pairsList,List<Map<Integer,Integer>> numberList,Map<List<String>,Integer> pairListIndexMap){
        for(int i = 0; i < pairsList.size();i++){
            pairListIndexMap.put(pairsList.get(i),i);
        }
        for(List<String> cardList: pairsList){
            Map<Integer,Integer> tempNumberMap = new HashMap<>();
            for(String card: cardList){
                Integer num = Integer.parseInt(card.substring(1));
                tempNumberMap.put(num,tempNumberMap.getOrDefault(num,0) + 1);
            }
            numberList.add(new HashMap<>(tempNumberMap));
        }
    }
    public void backtrackForAllCombination(List<List<String>> allCombinations,List<String> oneCombination,List<String> deckList,int pos){
        if(oneCombination.size() == 5){
            allCombinations.add(new ArrayList<>(oneCombination));
            return;
        }
        for(int i = pos; i < deckList.size();i++){
            oneCombination.add(deckList.get(i));
            backtrackForAllCombination(allCombinations,oneCombination,deckList,i + 1);
            oneCombination.remove(oneCombination.size() - 1);
        }
    }
    public List<List<String>> createTwoPairsList(List<List<String>> allCombinations){
        List<List<String>> twoPairsComb = new ArrayList<>();
        for(List<String> comb: allCombinations){
            if(checkHasPairs(comb,2)){
                twoPairsComb.add(new ArrayList<>(comb));
            }
        }
        return twoPairsComb;
    }
    public boolean checkHasPairs(List<String> comb, int requiredPair){
        Map<Integer,Integer> numberFreqMap = new HashMap<>();
        for(String card: comb) {
            Integer num = Integer.parseInt(card.substring(1));
            numberFreqMap.put(num,numberFreqMap.getOrDefault(num,0) + 1);
        }

        int pairs = 0;
        for(Map.Entry<Integer,Integer> entry:numberFreqMap.entrySet()){
            if(entry.getValue() == 2){
                pairs++;
            }else if(entry.getValue() > 2){
                return false;
            }
        }
        return pairs == requiredPair;
    }
    public List<List<String>> addThreeWithoutPair(List<List<String>> allCombinations){
        List<Map<Integer,Integer>> numberList = new ArrayList<>();
        List<List<String>> threeWithoutPairList = new ArrayList<>();
        Map<List<String>,Integer> threeListIndexMap = new HashMap<>();

        for(List<String> combination: allCombinations){
            Map<Integer,Integer> tempNumberMap = new HashMap<>();
            for(String card: combination){
                Integer num = Integer.parseInt(card.substring(1));
                tempNumberMap.put(num,tempNumberMap.getOrDefault(num,0)+1);
            }
            if(tempNumberMap.size() == 3 && tempNumberMap.containsValue(3) && tempNumberMap.containsValue(1)) {
                numberList.add(new HashMap<>(tempNumberMap));
                threeWithoutPairList.add(new ArrayList<>(combination));
            }
        }

        for(int i = 0; i < threeWithoutPairList.size();i++){
            threeListIndexMap.put(threeWithoutPairList.get(i),i);
        }
        List<Map<Integer,Integer>> sortedNumberList = generateSortedCard(numberList);
        List<List<Integer>> sortedCardInNum = new ArrayList<>();
        for(Map<Integer,Integer> map:sortedNumberList){
            List<Integer> tempList = new ArrayList<>();
            for(Map.Entry<Integer,Integer> entry:map.entrySet()){
                tempList.add(entry.getKey());
            }
            if(tempList.get(0) > tempList.get(1)){
                Integer temp = tempList.get(1);
                tempList.remove(1);
                tempList.add(0,temp);
            }
            sortedCardInNum.add(new ArrayList<>(tempList));
        }

        Comparator<List<String>> compareThree = new Comparator<>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                List<Integer> list1 = sortedCardInNum.get(threeListIndexMap.get(o1));
                List<Integer> list2 = sortedCardInNum.get(threeListIndexMap.get(o2));
                if(list1.get(2) > list2.get(2)){
                    return -1;
                }else if(list1.get(2) < list2.get(2)){
                    return 1;
                }
                if(list1.get(1) > list2.get(1)){
                    return -1;
                }else if(list1.get(1) < list2.get(1)){
                    return 1;
                }
                if(list1.get(0) > list2.get(0)){
                    return -1;
                }else if(list1.get(0) < list2.get(0)) {
                    return 1;
                }

                return 0;
            }
        };
        Collections.sort(threeWithoutPairList,compareThree);
        return threeWithoutPairList;
    }
    public void addStraightInRankedList(List<Integer> numberByKey,Map<Integer,List<String>> numberMap,List<List<String>> rankedList){
        List<Integer> numberByKeyAsc = new ArrayList<>();
        for(int i = numberByKey.size() - 1; i >= 0; i--){
            numberByKeyAsc.add(numberByKey.get(i));
        }
        List<List<Integer>> straightComb = new ArrayList<>();
        List<List<Integer>> minStraightComb = new ArrayList<>();
        for(int i = numberByKeyAsc.size() -1; i >= 4; i--) {
            if (checkStraight(numberByKeyAsc,i)){
                List<Integer> stra = new ArrayList<>();
                for(int j = i; j >= i - 4;j--) {
                    stra.add(numberByKeyAsc.get(j));
                }
                straightComb.add(new ArrayList<>(stra));
            }
            if(checkMinStraight(numberByKeyAsc)){
                List<Integer> stra = new ArrayList<>();
                for(int j = 0; j < 4;j++) {
                    stra.add(numberByKeyAsc.get(j));
                }
                stra.add(numberByKeyAsc.get(numberByKeyAsc.size() - 1));
                minStraightComb.add(new ArrayList<>(stra));
            }
        }
        for(List<Integer> minComb : minStraightComb){
            straightComb.add(new ArrayList<>(minComb));
        }
        //Generate card combination
        for(List<Integer> dec: straightComb){
            List<String> straightCardComb = new ArrayList<>();
            for(Integer num: dec){
                for(String cardStr: numberMap.get(num)) {
                    straightCardComb.add(cardStr);
                }
            }
            List<List<String>> allCombinations = new ArrayList<>();
            List<String> oneCombination = new ArrayList<>();
            backtrackForStraight(allCombinations,oneCombination,straightCardComb,0);
            for(List<String> combList:allCombinations){
                rankedList.add(new ArrayList<>(combList));
            }
        }
    }
    public void backtrackForStraight(List<List<String>> allCombinations,List<String> oneCombination,List<String> straightCardComb,int pos){
        if(oneCombination.size() == 5 && !hasDuplicatedCardInNumber(oneCombination)){
            allCombinations.add(new ArrayList<>(oneCombination));
            return;
        }
        for(int i = pos; i < straightCardComb.size();i++){
            oneCombination.add(straightCardComb.get(i));
            backtrackForStraight(allCombinations,oneCombination,straightCardComb,i + 1);
            oneCombination.remove(oneCombination.size() - 1);
        }
    }
    public boolean hasDuplicatedCardInNumber(List<String> oneCombination){
        Set<Integer> numberSet = new HashSet<>();
        for(String card: oneCombination){
            numberSet.add(Integer.parseInt(card.substring(1)));
        }
        return numberSet.size() != oneCombination.size();
    }
    public void colorMapAndNumberImp(Map<Character, List<Integer>> colorMap, List<Integer> numbers,String[] deck){
        for (int i = 0; i < deck.length; i++) {
            if (colorMap.containsKey(deck[i].charAt(0))) {
                colorMap.get(deck[i].charAt(0)).add(Integer.parseInt(deck[i].substring(1)));
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(Integer.parseInt(deck[i].substring(1)));
                colorMap.put(deck[i].charAt(0), new ArrayList<>(list));
            }
            numbers.add(Integer.parseInt(deck[i].substring(1)));
        }
    }
    public void numberMapImp(Map<Integer,List<String>> numberMap,List<Integer> numbers,String[] deck){
        for(int i = 0; i < numbers.size();i++){
            if (numberMap.containsKey(numbers.get(i))){
                numberMap.get(numbers.get(i)).add(deck[i]);
            } else{
                List<String> list = new ArrayList<>();
                list.add(deck[i]);
                numberMap.put(numbers.get(i),new ArrayList<>(list));
            }
        }
    }

    public void addSequenceInRankedList(Map<Character,List<Integer>> colorMap,List<List<String>> rankedList){
        List<List<String>> minStraight = new ArrayList<>();
        for(Map.Entry<Character,List<Integer>> entry: colorMap.entrySet()){
            List<Integer> nums = entry.getValue();
            if(nums.size() >= 5){
                Collections.sort(nums);
                for(int i = nums.size() -1; i >= 4; i--){
                    if(checkStraight(nums,i)){
                        List<String> list = new ArrayList<>();
                        for(int j = i; j >= i - 4;j--) {
                            list.add(entry.getKey() + "" + nums.get(j));
                        }
                        rankedList.add(new ArrayList<>(list));
                    }
                    if(checkMinStraight(nums)){
                        List<String> list = new ArrayList<>();
                        for(int j = 0; j < 4;j++) {
                            list.add(entry.getKey() + "" + nums.get(j));
                        }
                        list.add(entry.getKey() + "" + 14);
                        minStraight.add(new ArrayList<>(list));
                    }
                }
            }
        }
        for(List<String> minSta: minStraight){
            rankedList.add(new ArrayList<>(minSta));
        }
    }

    public void addBombInRankedList(List<Integer> numberByKey, Map<Integer,List<String>> numberMap, List<List<String>> rankedList){
        for(Integer num: numberByKey){
            List<String> cards = numberMap.get(num);
            if(cards.size() == 4){
                List<String> comb = new ArrayList<>();
                for(String card: cards){
                    comb.add(card);
                }
                for(Integer num2: numberByKey){
                    if(!num2.equals(num)){
                        for(String card:numberMap.get(num2)){
                            comb.add(card);
                            rankedList.add(new ArrayList<>(comb));
                            comb.remove(comb.size()-1);
                        }
                    }
                }
            }
        }
    }
    public void addFullHouseInList(List<List<String>> rankedList,List<String> cards,List<Integer> numberByKey,
                                       Map<Integer,List<String>> numberMap, Integer num, int i1,int i2, int i3){
        List<String> comb = new ArrayList<>();
        comb.add(cards.get(i1));
        comb.add(cards.get(i2));
        comb.add(cards.get(i3));
        for(Integer numPair:numberByKey){
            List<String> cards2 = numberMap.get(numPair);
            if(!num.equals(numPair)) {
                if (cards2.size() == 2) {
                    addTwoInList(comb, cards2, rankedList, 0, 1);
                } else if (cards2.size() == 3) {
                    addTwoInList(comb, cards2, rankedList, 0, 1);
                    addTwoInList(comb, cards2, rankedList, 0, 2);
                    addTwoInList(comb, cards2, rankedList, 1, 2);
                } else if (cards2.size() == 4) {
                    addTwoInList(comb, cards2, rankedList, 0, 1);
                    addTwoInList(comb, cards2, rankedList, 0, 2);
                    addTwoInList(comb, cards2, rankedList, 0, 3);
                    addTwoInList(comb, cards2, rankedList, 1, 2);
                    addTwoInList(comb, cards2, rankedList, 1, 3);
                    addTwoInList(comb, cards2, rankedList, 2, 3);
                }
            }
        }
    }
    public void addTwoInList(List<String> comb,List<String> cards2, List<List<String>> rankedList, int i1, int i2){
        comb.add(cards2.get(i1));
        comb.add(cards2.get(i2));
        rankedList.add(new ArrayList<>(comb));
        comb.remove(comb.size() - 1);
        comb.remove(comb.size() - 1);
    }
    public boolean checkStraight(List<Integer> nums, int i){
        return (nums.get(i) - 1 == nums.get(i - 1)
                && nums.get(i - 1) - 1 == nums.get(i - 2)
                && nums.get(i - 2) - 1 == nums.get(i - 3)
                && nums.get(i - 3) - 1 == nums.get(i - 4));
    }

    public boolean checkMinStraight(List<Integer> nums){
        return (nums.get(nums.size() - 1) == 14
                && nums.get(0) == 2
                && nums.get(1) == 3
                && nums.get(2)== 4
                && nums.get(3) == 5);
    }

    public void addFlushInRankedList(Map<Character,List<Integer>> colorMap,List<List<String>> rankedList){
        for(Map.Entry<Character,List<Integer>> entry: colorMap.entrySet()){
            List<Integer> cardNum = entry.getValue();
            List<String> comb = new ArrayList<>();
            if(cardNum.size() >= 5){
                Collections.sort(cardNum);
                if(cardNum.get(0) == 1){
                    cardNum.remove(0);
                    cardNum.add(1);
                }
                for(int i = cardNum.size()-1;i >= 0;i--){
                    if(comb.size() < 5) {
                        comb.add(entry.getKey() + "" + cardNum.get(i));
                    }else{
                        rankedList.add(new ArrayList<>(comb));
                        comb.remove(0);
                        comb.add(entry.getKey() + "" + cardNum.get(i));
                    }
                }
                if(comb.size() == 5){
                    rankedList.add(new ArrayList<>(comb));
                }
            }
        }
    }
}
