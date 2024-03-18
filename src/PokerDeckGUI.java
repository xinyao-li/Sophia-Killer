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
        frame = new JFrame("Sophia Killer");
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
                            new String[]{"♥", "♦", "♣", "♠"});

                    suitComboBox.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if ("♥".equals(value) || "♦".equals(value)) {
                                label.setText("<html><font color='red'>" + value + "</font></html>");
                            }
                            return label;
                        }
                    });

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
                        if(cardSuit.contains("♥") || cardSuit.contains("♦")){
                            String buttonText = "<html><font color='red'>" + cardSuit + "" + cardValue + "</font></html>";
                            button.setText(buttonText);
                        }else {
                            button.setText(cardSuit + "" + cardValue);
                            button.setIcon(null);
                        }
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
            List<String> combinations = null;
            if(!selectedCard[0].equals("-1")) {
                TopNCombination topNCombination = new TopNCombination(selectedCard);
                try {
                    combinations = topNCombination.getTopRankedCombination();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Assume we only present 1 combination
                if (combinations == null || !combinations.isEmpty()) {
                    updateSecondDeck(combinations);
                }
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
            if(deck[i] == null){
                selectedCard[0] = "-1";
                break;
            }
            if(deck[i].contains("♥")){
                card += "h";
            }
            else if(deck[i].contains("♦")){
                card += "d";
            }
            else if(deck[i].contains("♣")){
                card += "c";
            }
            else if(deck[i].contains("♠")){
                card += "s";
            }
            card+= symbolMap.get(deck[i].substring(1));
            selectedCard[i] = card;
        }
        return selectedCard;
    }

    private void updateSecondDeck(List<String> combination) {
        panel2.removeAll();
        List<String> combinationInUI = new ArrayList<>();
        if(combination == null){
            combinationInUI.add("Invalid Input. Please don't use duplicate cards");
        }
        else {
            for (String card : combination) {
                String tempCard = "";
                if (card.contains("h")) {
                    tempCard += "♥";
                } else if (card.contains("d")) {
                    tempCard += "♦";
                } else if (card.contains("c")) {
                    tempCard += "♣";
                } else if (card.contains("s")) {
                    tempCard += "♠";
                }
                if (card.contains("11")) {
                    tempCard += "J";
                } else if (card.contains("12")) {
                    tempCard += "Q";

                } else if (card.contains("13")) {
                    tempCard += "K";
                } else if (card.contains("14")) {
                    tempCard += "A";
                } else {
                    tempCard += card.substring(1);
                }
                combinationInUI.add(tempCard);
            }
        }
        for (String card : combinationInUI) {
            if(card.contains("♥") || card.contains("♦")){
                panel2.add(new JLabel("<html><font color='red'>" + card + "</font></html>"));
            }else {
                panel2.add(new JLabel(card));
            }
        }
        panel2.revalidate();
        panel2.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokerDeckGUI());
    }
}
