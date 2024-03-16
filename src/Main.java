import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //GetAllCombinationsInFiveCards getRankedCombinationInFiveCards = new GetAllCombinationsInFiveCards();
        String[] deck1 = new String[]{"c5","s11","s13","s2","s14","d7","d6","h5","h6","c9","d14","d12","c2"};
        String[] deck2 = new String[]{"s14","s11","s13","s12","s10","s9","h8","h9","h6","s4","s5","s3","s2"};
        String[] deck3 = new String[]{"s14","s11","s13","s12","s10","s7","h8","h9","h6","c7","d9","d12","c12"};
        String[] deck4 = new String[]{"s14","s10","s8","s3","s2","s6","h8","h9","h6","c9","d9","d12","c12"};
        String[] deck5 = new String[]{"h1","h13","s6","d3","s2","c2","c12","s3","s7","s4","c5","h4","h12"};
        String[] deck6 = new String[]{"s14","s10","s8","s3","s2","s6","h2","h9","h6","c9","d9","d12","c12"};
        String[] deck7 = new String[]{"s14","s10","s9","s3","s2","s6","h14","h9","h6","c9","d9","d14","c12"};
        String[] deck8 = new String[]{"s14","s10","s9","s3","s2","s6","h14","h11","h6","c10","d4","d5","c12"};
        TopNCombination topNCombination = new TopNCombination(deck7);
        topNCombination.printTopNCombination(5);
    }
}