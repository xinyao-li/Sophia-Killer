import java.util.List;

public class TopNCombination {

    private final GetAllCombinationsForPlayers getAllCombinationsForPlayers;

    public TopNCombination(String[] deck){
        this.getAllCombinationsForPlayers = new GetAllCombinationsForPlayers(deck);
    }

    public List<String> getTopRankedCombination(){
        List<List<String>> allCombinationsForPlayerList = getAllCombinationsForPlayers.getAllCombinationsForPlayers();
        List<List<String>> rankedCombinations = getAllCombinationsForPlayers.getRankedValidCombinationsForPlayers(allCombinationsForPlayerList);
        return rankedCombinations.get(0);
    }
    public void printTopNCombination(int top){
        List<List<String>> allCombinationsForPlayerList = getAllCombinationsForPlayers.getAllCombinationsForPlayers();
        List<List<String>> rankedCombinations = getAllCombinationsForPlayers.getRankedValidCombinationsForPlayers(allCombinationsForPlayerList);
        System.out.println("Here is the combination in top " + top + " highest scores:");
        for(int i  = 0; i < top;i++){
            System.out.println((i + 1) + " : " + rankedCombinations.get(i));
        }
    }

}
