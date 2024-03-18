import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerDeckGUI {
    private JFrame frame;
    private JPanel panel1;

    private JPanel panel2;
    private List<JButton> cardButtons;

    private JButton executeButton;

    public String[] deck;

    public PokerDeckGUI() {
        // Initialize the winodw
        this.deck = new String[13];
        frame = new JFrame("Poker Decks");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(800, 600);

        panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel1.setPreferredSize(new Dimension(frame.getWidth(), 100));
        panel2.setPreferredSize(new Dimension(frame.getWidth(), 100));

        // Initialize the card list
        cardButtons = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            JButton button = new JButton("Empty");
            int index = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // choose the color and number of the card
                    JPanel cardSelectionPanel = new JPanel();
                    JComboBox<String> valueComboBox = new JComboBox<>(
                            new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"});
                    JComboBox<String> suitComboBox = new JComboBox<>(
                            new String[]{"♥️", "♦️", "♣️", "♠️"});

                    cardSelectionPanel.add(new JLabel("Value:"));
                    cardSelectionPanel.add(valueComboBox);
                    cardSelectionPanel.add(Box.createHorizontalStrut(15));
                    cardSelectionPanel.add(new JLabel("Suit:"));
                    cardSelectionPanel.add(suitComboBox);

                    int result = JOptionPane.showConfirmDialog(frame, cardSelectionPanel,
                            "Choose a card:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String cardValue = (String) valueComboBox.getSelectedItem();
                        String cardSuit = (String) suitComboBox.getSelectedItem();
                        button.setText(cardSuit + "" + cardValue);
                        deck[index] = cardSuit + "" + cardValue;
                    }
                }
            });
            panel1.add(button);
        }
        executeButton = new JButton("Generate Combinations");
        executeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        executeButton.setPreferredSize(new Dimension(frame.getWidth(), 50));
        executeButton.addActionListener(e -> {
            // Call getTopRankedCombination() and update the second deck
            String[] selectedCard = convertDeck(deck);
            TopNCombination topNCombination = new TopNCombination(selectedCard);
            List<String> combinations = topNCombination.getTopRankedCombination();

            // Assume we only present 1 combination
            if (!combinations.isEmpty()) {
                updateSecondDeck(combinations);
            }
        });
        frame.add(panel1);
        frame.add(executeButton);
        frame.add(panel2);

        frame.pack();

        frame.setVisible(true);
    }

    private Map<String,String> getSymbolMap(){
        Map<String,String> symbolMap = new HashMap<>();
        symbolMap.put("A","14");
        symbolMap.put("J","11");
        symbolMap.put("Q","12");
        symbolMap.put("K","13");
        symbolMap.put("10","10");
        symbolMap.put("9","9");
        symbolMap.put("8","8");
        symbolMap.put("7","7");
        symbolMap.put("6","6");
        symbolMap.put("5","5");
        symbolMap.put("4","4");
        symbolMap.put("3","3");
        symbolMap.put("2","2");
        return symbolMap;
    }

    private String[] convertDeck(String[] deck){
        String[] selectedCard = new String[13];
        Map<String,String> symbolMap = getSymbolMap();
        for(int i = 0; i < deck.length;i++){
            String card = "";
            if(deck[i].contains("♥️")){
                card += "h";
            }
            else if(deck[i].contains("♦️")){
                card += "d";
            }
            else if(deck[i].contains("♣️")){
                card += "c";
            }
            else if(deck[i].contains("♠️")){
                card += "s";
            }
            card+= symbolMap.get(deck[i].substring(2));
            selectedCard[i] = card;
        }
        return selectedCard;
    }

    private void updateSecondDeck(List<String> combination) {
        panel2.removeAll();
        List<String> combinationInUI = new ArrayList<>();
        for(String card: combination){
            String tempCard = "";
            if(card.contains("h")){
                tempCard += "♥️";
            }
            else if(card.contains("d")){
                tempCard += "♦️";
            }
            else if(card.contains("c")){
                tempCard += "♣️";
            }
            else if(card.contains("s")){
                tempCard += "♠️";
            }
            if(card.contains("J")){
                tempCard += 11;
            }else if(card.contains("Q")){
                tempCard += 12;
            }else if(card.contains("K")){
                tempCard += 13;
            }else if(card.contains("A")){
                tempCard += 14;
            }else{
                tempCard += card.substring(1);
            }
            combinationInUI.add(tempCard);
        }
        for (String card : combinationInUI) {
            panel2.add(new JLabel(card));
        }
        panel2.revalidate();
        panel2.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokerDeckGUI());
    }
}
