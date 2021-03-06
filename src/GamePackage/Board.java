package GamePackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board extends JFrame implements ActionListener {

    private final int AMOUNT_OF_BUTTONS = 16;
    JFrame win = new JFrame();
    JPanel rootPanel;
    JPanel gamePanel;
    JPanel settingsPanel;
    JButton newGameButton;
    Button holeOnBoard;
    List<Button> buttonList;

    public Board(boolean demo){
        initializeBoard();
        creatingHoleOnBoard();
        createButtons();
        placeShuffledButtonsOnBoard(buttonList, demo);

        newGameButton.addActionListener(e -> placeShuffledButtonsOnBoard(buttonList, false));
    }

    private void initializeBoard() {
        rootPanel = new JPanel();
        gamePanel = new JPanel();
        settingsPanel = new JPanel();
        newGameButton = new JButton("New Game");
        newGameButton.setFocusable(false);

        gamePanel.setLayout(new GridLayout(4, 4));
        rootPanel.setLayout(new BorderLayout());

        settingsPanel.add(newGameButton);
        rootPanel.add(gamePanel);
        rootPanel.add(settingsPanel, BorderLayout.EAST);

        this.add(rootPanel);
        this.setSize(600,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void creatingHoleOnBoard() {
        holeOnBoard = new Button("");
        holeOnBoard.setBackground(UIManager.getColor ( "Panel.background" ));
        holeOnBoard.setEnabled(false);
    }

    private void createButtons() {
        buttonList = new ArrayList<>();
        for (int i = 1; i < AMOUNT_OF_BUTTONS; i++) {
            buttonList.add(new Button(String.valueOf(i)));
        }
        buttonList.add(holeOnBoard);
    }

    private void placeShuffledButtonsOnBoard(List<Button> buttonList, boolean demo){
        if(!demo) {
            Collections.shuffle(buttonList);
        }
        int k = 0;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                Button current = buttonList.get(k);
                current.addActionListener(this);
                current.setPosition(i, j);
                gamePanel.add(current);
                k++;
                gamePanel.revalidate();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button pressedButton = (Button) e.getSource();
        if(e.getSource() == pressedButton && check(pressedButton)){
            swapButtons(pressedButton);
            checkWin();
        }
    }

    private void swapButtons(Button pressedButton) {
        String buttonText = pressedButton.getText();
        pressedButton.setText("");
        pressedButton.setBackground(UIManager.getColor ( "Panel.background" ));
        pressedButton.setEnabled(false);
        int tempIndex = pressedButton.getSpecialIndex();
        pressedButton.setSpecialIndex(holeOnBoard.getSpecialIndex());
        holeOnBoard.setSpecialIndex(tempIndex);
        holeOnBoard.setText(buttonText);
        holeOnBoard.setBackground(this.getBackground());
        holeOnBoard.setEnabled(true);
        holeOnBoard = pressedButton;
    }


    private boolean check(Button button) {
        int x = button.getxPosition() - holeOnBoard.getxPosition();
        int y = button.getyPosition() - holeOnBoard.getyPosition();
        return ((Math.abs(x) == 1 && y == 0) || (x == 0 && Math.abs(y) == 1));
    }

    private void checkWin() {
        for (int i = 0; i < AMOUNT_OF_BUTTONS-1; i++) {
            Button tempButton = (Button) gamePanel.getComponent(i);
            if(!(tempButton.getSpecialIndex() == i+1)) {
                return;
            }
        }
        victoryScreen();
    }

    private void victoryScreen(){
        JLabel victory = new JLabel(new ImageIcon("Victory.png"));
        win.setLocation((getX() +40),(getY() +80));
        win.add(victory);
        win.setVisible(true);
        win.setResizable(false);
        win.pack();

        win.addFocusListener(victoryListener);
        win.addMouseListener(victoryClick);
    }
    FocusListener victoryListener = new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            win.setVisible(false);
        }
    };
    MouseListener victoryClick = new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            super.mouseClicked(e);
            win.setVisible(false);
        }
    };

}
