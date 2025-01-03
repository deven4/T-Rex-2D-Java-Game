import Utils.Animate;
import Utils.GameButton;
import Utils.GameConfig;
import Utils.GameFont;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {

    private GameButton btnPlay;
    private GameButton btnResume;
    private final GameFont gameFont;
    private final GamePanel gamePanel;
    private JPanel mainMenuPanel;
    private JPanel optionsMenuPanel;
    private final Animate menuPanelAnimation;
    private boolean isMenuVisible;

    GameMenuPanel(GamePanel gamePanel) {
        super();

        setOpaque(false);
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        this.gamePanel = gamePanel;
        gameFont = GameFont.getInstance();

        buildMainMenuPanel();
        buildOptionMenuPanel();
        recalculateSize();
        menuPanelAnimation = new Animate(this, Animate.SLIDE_RIGHT);
    }

    private void buildMainMenuPanel() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.setName("Main Menu Panel");
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnPlay = new GameButton("PLAY");
        btnResume = new GameButton("RESUME");
        GameButton btnOptions = new GameButton("OPTIONS");
        GameButton btnExit = new GameButton("EXIT");
        btnResume.setVisible(false);

        /* Add buttons to mainMenuPanel */
        mainMenuPanel.add(btnPlay);
        mainMenuPanel.add(Box.createVerticalStrut(10));
        mainMenuPanel.add(btnResume);
        mainMenuPanel.add(Box.createVerticalStrut(10));
        mainMenuPanel.add(btnOptions);
        mainMenuPanel.add(Box.createVerticalStrut(10));
        mainMenuPanel.add(btnExit);

        // mainMenuPanel.setBackground(new Color(255,255,255,150));
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.revalidate();
        mainMenuPanel.setSize((int) mainMenuPanel.getPreferredSize().getWidth(),
                (int) mainMenuPanel.getPreferredSize().getHeight());
        add(mainMenuPanel);

        /* Add Action listeners */
        btnPlay.addActionListener(_ -> {
            showMenu(Menu.NOMENU);
            gamePanel.startGame();
        });
        btnResume.addActionListener(_ -> {
            showMenu(Menu.RESUME);
            Game.isGamePaused = false;
        });
        btnOptions.addActionListener(_ -> showMenu(Menu.OPTIONS));
        btnExit.addActionListener(_ -> System.exit(0));
    }

    private void buildOptionMenuPanel() {
        optionsMenuPanel = new JPanel();
        optionsMenuPanel.setOpaque(false);
        optionsMenuPanel.setName("Options Menu Panel");
        optionsMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GameButton btnBack = new GameButton("< BACK");
        GameButton btnSound = new GameButton("SOUND", false);
        GameButton btnSoundToggleL = new GameButton("<");
        GameButton btnSoundToggleR = new GameButton(">");
        GameButton btnCredits = new GameButton("CREDITS");
        btnSound.setFont(GameFont.getInstance().getSuperDream().deriveFont(35f));
        btnSoundToggleL.setFont(GameFont.getInstance().getSuperDream().deriveFont(35f));
        btnSoundToggleR.setFont(GameFont.getInstance().getSuperDream().deriveFont(35f));
        btnCredits.setFont(GameFont.getInstance().getSuperDream().deriveFont(35f));

        /* SubOptionMenu Panel */
        JPanel subOptionMenuPanel = new JPanel();
        subOptionMenuPanel.setOpaque(false);
        subOptionMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Spacing between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        subOptionMenuPanel.add(btnSound, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        subOptionMenuPanel.add(btnSoundToggleL, gbc);
        JLabel label = new JLabel("OFF");
        label.setFont(GameFont.getInstance().getSuperDream().deriveFont(35f));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        subOptionMenuPanel.add(label, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        subOptionMenuPanel.add(btnSoundToggleR, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        subOptionMenuPanel.add(btnCredits, gbc);
        btnSoundToggleL.addActionListener(_ -> soundPlayback(label));
        btnSoundToggleR.addActionListener(_ -> soundPlayback(label));

        /* Credits Panel */
        JPanel creditParentPanel = new JPanel();
        creditParentPanel.setOpaque(false);
        creditParentPanel.setLayout(new BoxLayout(creditParentPanel, BoxLayout.Y_AXIS));
        JPanel creditsPanel = new JPanel();
        creditsPanel.setOpaque(false);
        creditsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 10, 5, 5);
        String[][] creditLines = {
                {"Lead UI/UX Game Designer", "UI/UX Designer", "UI Artist", "Senior 2D Artist", "Contractors-2", "Additional UI"},
                {"Level Design", "Lead Level Designer", "Senior Level Designers-2"}
        };
        int count = 0;
        for (String[] creditLine : creditLines) {
            for (String s : creditLine) {
                String[] split = s.split("-");
                JLabel lblCredit = new JLabel(split[0]);
                lblCredit.setFont(gameFont.getSuperDream().deriveFont(25f));
                lblCredit.setForeground(GameConfig.PRIMARY_COLOR);
                gbc3.gridx = 0;
                gbc3.gridy = count;
                gbc3.anchor = GridBagConstraints.CENTER;
                creditsPanel.add(lblCredit, gbc3);
                count += 1;
                if (split.length > 1) {
                    for (int i = 0; i < Integer.parseInt(split[1]); i++) {
                        JLabel lblCredit2 = new JLabel("DEVEN WARANG");
                        lblCredit2.setFont(gameFont.getSuperDream().deriveFont(20f));
                        lblCredit2.setForeground(GameConfig.SECONDARY_COLOR);
                        gbc3.gridx = 0;
                        gbc3.gridy = count;
                        gbc3.anchor = GridBagConstraints.CENTER;
                        creditsPanel.add(lblCredit2, gbc3);
                        count += 1;
                    }
                } else {
                    JLabel lblCredit2 = new JLabel("DEVEN WARANG");
                    lblCredit2.setFont(gameFont.getSuperDream().deriveFont(20f));
                    lblCredit2.setForeground(GameConfig.SECONDARY_COLOR);
                    gbc3.gridx = 0;
                    gbc3.gridy = count;
                    gbc3.anchor = GridBagConstraints.CENTER;
                    creditsPanel.add(lblCredit2, gbc3);
                }
                count += 1;
            }
            count += 1;
        }
        creditsPanel.setVisible(false);
        creditParentPanel.add(creditsPanel);
        creditParentPanel.add(creditsPanel);
        Animate creditPanelAnimate = new Animate(creditsPanel, Animate.SLIDE_DOWN);

        /* Option MENU */
        optionsMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.anchor = GridBagConstraints.WEST;
        optionsMenuPanel.add(btnBack, gbc2);
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        optionsMenuPanel.add(subOptionMenuPanel, gbc2);
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        optionsMenuPanel.add(creditParentPanel, gbc2);
        optionsMenuPanel.setSize((int) optionsMenuPanel.getPreferredSize().getWidth(), (int) optionsMenuPanel.getPreferredSize().getHeight());
        optionsMenuPanel.revalidate();
        add(optionsMenuPanel);

        btnBack.addActionListener(_ -> {
            if (creditsPanel.isVisible()) {
                creditPanelAnimate.stop();
                subOptionMenuPanel.setVisible(true);
                creditsPanel.setVisible(false);
                recalculateSize();
            } else showMenu(Menu.MAIN);
        });
        btnCredits.addActionListener(_ -> {
            subOptionMenuPanel.setVisible(false);
            creditsPanel.setVisible(true);
            creditPanelAnimate.start();
            recalculateSize();
        });
    }

    private void recalculateSize() {
        int btnX = (int) (GameConfig.WIDTH - GameConfig.WIDTH * 0.40); // subtracting 40% from the gameBoard width
        int btnY = (int) (GameConfig.HEIGHT - GameConfig.HEIGHT * 0.80) + 20;
        optionsMenuPanel.setSize((int) optionsMenuPanel.getPreferredSize().getWidth(), (int) optionsMenuPanel.getPreferredSize().getHeight());
        int width = Math.max(mainMenuPanel.getX() + mainMenuPanel.getWidth(), optionsMenuPanel.getX() + optionsMenuPanel.getWidth());
        int height = Math.max(mainMenuPanel.getY() + mainMenuPanel.getHeight(), optionsMenuPanel.getY() + optionsMenuPanel.getHeight());
        setSize(width, height);
        setLocation(btnX, btnY);
        revalidate();
    }

    private void soundPlayback(JLabel label) {
        if (label.getText().equals("OFF")) label.setText("ON ");
        else label.setText("OFF");
    }

    public void showMenu(Menu menu) {
        switch (menu) {
            case MAIN -> {
                mainMenuPanel.setVisible(true);
                optionsMenuPanel.setVisible(false);
                isMenuVisible = true;
            }
            case OPTIONS -> {
                mainMenuPanel.setVisible(false);
                optionsMenuPanel.setVisible(true);
                isMenuVisible = true;
            }
            case NOMENU -> {
                menuPanelAnimation.hide();
                isMenuVisible = false;
            }
            case PAUSE -> {
                btnPlay.setVisible(false);
                btnResume.setVisible(true);
                mainMenuPanel.setVisible(true);
                menuPanelAnimation.start();
                isMenuVisible = true;
            }
            case RESUME -> {
                btnPlay.setVisible(true);
                btnResume.setVisible(false);
                menuPanelAnimation.hide();
                isMenuVisible = false;
            }
        }
    }

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public enum Menu {
        MAIN, OPTIONS, NOMENU, PAUSE, RESUME
    }
}
