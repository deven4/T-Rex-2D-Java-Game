package core;

import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameMenuPanel extends JPanel {

    private boolean isMenuVisible;

    private JPanel mPanel;
    private JPanel optionsMenuPanel;
    private final GameFont gameFont;
    private final GamePanel gamePanel;
    private final GameSound gameSound;
    private final InputManager inputManager;
    private final Animate menuPanelAnimation;
    private GameButton btnPlay, btnResume, btnRestart, btnBackToMenu, btnOptions;

    GameMenuPanel(GamePanel gamePanel, InputManager inputManager) {
        super();

        setOpaque(false);
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        this.gamePanel = gamePanel;
        this.inputManager = inputManager;
        this.gameFont = GameFont.getInstance();
        this.gameSound = GameSound.getInstance();

        buildMainMenuPanel();
        buildOptionMenuPanel();
        recalculateSize();
        menuPanelAnimation = new Animate(this);
        menuPanelAnimation.setMenuListener(() -> optionsMenuPanel.setVisible(false));
    }

    private void buildMainMenuPanel() {
        mPanel = new JPanel();
        mPanel.setOpaque(false);
        mPanel.setName("Main Menu Panel");
        mPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //mainMenuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));

        btnPlay = new GameButton("PLAY");
        btnResume = new GameButton("RESUME");
        btnRestart = new GameButton("RESTART");
        btnBackToMenu = new GameButton("BACK TO MENU");
        btnOptions = new GameButton("OPTIONS");
        GameButton btnExit = new GameButton("EXIT");
        GameSound gameSound = GameSound.getInstance();

        /* Add buttons to mainMenuPanel */
        mPanel.add(btnPlay);
        mPanel.add(btnOptions);
        mPanel.add(btnExit);
        revalidateMPanel();
        add(mPanel);

        /* Add Action listeners */
        btnPlay.setOnClickListener(_ -> {
            showMenu(Menu.NOMENU);
            gameSound.stop(GameSound.TRACK.INTRO);
            gamePanel.startGame();
        });
        btnResume.setOnClickListener(_ -> {
            isMenuVisible = false;
            Game.isGamePaused = false;
            menuPanelAnimation.start(Animate.HIDE);
            gamePanel.requestFocus();
            gameSound.stop(GameSound.TRACK.INTRO);
        });
        btnRestart.setOnClickListener(e -> {
            showMenu(Menu.NOMENU);
            gamePanel.restartGame();
        });
        btnBackToMenu.setOnClickListener(e -> showMenu(Menu.MAIN));

        btnOptions.setOnClickListener(_ -> showMenu(Menu.OPTIONS));
        btnExit.setOnClickListener(_ -> System.exit(0));
    }

    private void buildOptionMenuPanel() {
        optionsMenuPanel = new JPanel();
        optionsMenuPanel.setOpaque(false);
        optionsMenuPanel.setName("Options Menu Panel");
        Font font = GameFont.getInstance().getSuperDream().deriveFont(25f);
        GameButton btnBack = new GameButton("< BACK");
        GameButton btnSound = new GameButton("SOUND", false, font);
        GameButton btnSoundToggleL = new GameButton("<", font);
        GameButton btnSoundToggleR = new GameButton(">", font);
        GameButton btnPlayer = new GameButton("PLAYER", false, font);
        GameButton btnPlayerL = new GameButton("<", font);
        GameButton btnPlayerR = new GameButton(">", font);
        GameButton btnCredits = new GameButton("CREDITS", font);

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
        JLabel lblOn_ = new JLabel("ON");
        lblOn_.setFont(font);
        lblOn_.setPreferredSize(new Dimension(80, 20));
        lblOn_.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.gridy = 0;
        subOptionMenuPanel.add(lblOn_, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        subOptionMenuPanel.add(btnSoundToggleR, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        subOptionMenuPanel.add(btnPlayer, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        subOptionMenuPanel.add(btnPlayerL, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel lblPlayerSelection = new JLabel("T-REX");
        lblPlayerSelection.setFont(font);
        lblPlayerSelection.setPreferredSize(new Dimension(80, 20));
        lblPlayerSelection.setHorizontalAlignment(SwingConstants.CENTER);
        subOptionMenuPanel.add(lblPlayerSelection, gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        subOptionMenuPanel.add(btnPlayerR, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        subOptionMenuPanel.add(btnCredits, gbc);
        btnSoundToggleL.setOnClickListener(_ -> soundPlayback(lblOn_));
        btnSoundToggleR.setOnClickListener(_ -> soundPlayback(lblOn_));
        btnPlayerL.setOnClickListener(_ -> changePlayer(lblPlayerSelection));
        btnPlayerR.setOnClickListener(_ -> changePlayer(lblPlayerSelection));

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
                {"Lead UI/UX core.Game Designer", "UI/UX Designer", "UI Artist", "Senior 2D Artist", "Contractors-2", "Additional UI"},
                {"Level Design", "Lead Level Designer", "Senior Level Designers-2"}
        };
        int count = 0;
        for (String[] creditLine : creditLines) {
            for (String s : creditLine) {
                String[] split = s.split("-");
                JLabel lblCredit = new JLabel(split[0]);
                lblCredit.setFont(gameFont.getSuperDream().deriveFont(25f));
                lblCredit.setForeground(Config.PRIMARY_COLOR);
                gbc3.gridx = 0;
                gbc3.gridy = count;
                gbc3.anchor = GridBagConstraints.CENTER;
                creditsPanel.add(lblCredit, gbc3);
                count += 1;
                if (split.length > 1) {
                    for (int i = 0; i < Integer.parseInt(split[1]); i++) {
                        JLabel lblCredit2 = new JLabel("DEVEN WARANG");
                        lblCredit2.setFont(gameFont.getSuperDream().deriveFont(20f));
                        lblCredit2.setForeground(Config.SECONDARY_COLOR);
                        gbc3.gridx = 0;
                        gbc3.gridy = count;
                        gbc3.anchor = GridBagConstraints.CENTER;
                        creditsPanel.add(lblCredit2, gbc3);
                        count += 1;
                    }
                } else {
                    JLabel lblCredit2 = new JLabel("DEVEN WARANG");
                    lblCredit2.setFont(gameFont.getSuperDream().deriveFont(20f));
                    lblCredit2.setForeground(Config.SECONDARY_COLOR);
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
        Animate creditPanelAnimate = new Animate(creditsPanel);

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

        btnBack.setOnClickListener(_ -> {
            if (creditsPanel.isVisible()) {
                creditPanelAnimate.stop();
                subOptionMenuPanel.setVisible(true);
                creditsPanel.setVisible(false);
                recalculateSize();
            } else showMenu(Menu.MAIN);
        });
        btnCredits.setOnClickListener(_ -> {
            subOptionMenuPanel.setVisible(false);
            creditsPanel.setVisible(true);
            creditPanelAnimate.start(Animate.SLIDE_DOWN);
            recalculateSize();
        });
    }

    private void changePlayer(JLabel lblPlayerSelection) {
        if (lblPlayerSelection.getText().equals("T-REX")) {
            lblPlayerSelection.setText("ROBO");
        } else {
            lblPlayerSelection.setText("T-REX");
        }
    }

    private void recalculateSize() {
        optionsMenuPanel.setSize((int) optionsMenuPanel.getPreferredSize().getWidth(),
                (int) optionsMenuPanel.getPreferredSize().getHeight());
        int width = Math.max(mPanel.getX() + mPanel.getWidth(), optionsMenuPanel.getX() + optionsMenuPanel.getWidth());
        int height = Math.max(mPanel.getY() + mPanel.getHeight(), optionsMenuPanel.getY() + optionsMenuPanel.getHeight());
        setSize(width, height);
        setLocation(Config.MENU_X, Config.MENU_Y);
        revalidate();
    }

    private void soundPlayback(JLabel label) {
        if (label.getText().equals("OFF")) {
            label.setText("ON ");
            recalculateSize();
            gameSound.setSoundOn(true);
            gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        } else {
            label.setText("OFF");
            recalculateSize();
            gameSound.setSoundOn(false);
            gameSound.stop(GameSound.TRACK.INTRO);
        }
    }

    private void revalidateMPanel() {
        mPanel.setSize((int) mPanel.getPreferredSize().getWidth(),
                (int) mPanel.getPreferredSize().getHeight());
        mPanel.repaint();
        mPanel.revalidate();
    }

    public void showMenu(Menu menu) {
        switch (menu) {
            case MAIN -> {
                mPanel.setVisible(true);
                mPanel.remove(btnResume);
                mPanel.remove(btnRestart);
                mPanel.remove(btnBackToMenu);
                mPanel.add(btnPlay, 0);
                mPanel.add(btnOptions, 1);
                revalidateMPanel();

                isMenuVisible = true;
                optionsMenuPanel.setVisible(false);
            }
            case OPTIONS -> {
                mPanel.setVisible(false);
                optionsMenuPanel.setVisible(true);
                isMenuVisible = true;
            }
            case NOMENU -> {
                menuPanelAnimation.start(Animate.HIDE);
                //optionsMenuPanel.setVisible(false);
                isMenuVisible = false;
            }
            case PAUSE -> {
                mPanel.remove(btnPlay);
                mPanel.remove(btnRestart);
                mPanel.remove(btnOptions);
                mPanel.add(btnResume, 0);
                mPanel.add(btnBackToMenu, 1);
                mPanel.setVisible(true);
                revalidateMPanel();

                isMenuVisible = true;
                optionsMenuPanel.setVisible(false);
                menuPanelAnimation.start(Animate.SLIDE_RIGHT);
            }
            case RESTART -> {
                mPanel.setVisible(true);
                mPanel.remove(btnPlay);
                mPanel.remove(btnResume);
                mPanel.remove(btnOptions);
                mPanel.add(btnRestart, 0);
                mPanel.add(btnBackToMenu, 1);
                revalidateMPanel();

                isMenuVisible = true;
                optionsMenuPanel.setVisible(false);
                menuPanelAnimation.start(Animate.SLIDE_RIGHT);
            }
        }
    }

    public void update() {
        if (inputManager.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            if (!Game.isGamePaused && Game.isGameRunning) {
                Game.isGamePaused = true;
                showMenu(GameMenuPanel.Menu.PAUSE);
            } else if (Game.isGamePaused && Game.isGameRunning) {
                Game.isGamePaused = false;
                showMenu(GameMenuPanel.Menu.NOMENU);
            }
        }
    }

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public enum Menu {
        MAIN, OPTIONS, NOMENU, PAUSE, RESTART
    }
}
