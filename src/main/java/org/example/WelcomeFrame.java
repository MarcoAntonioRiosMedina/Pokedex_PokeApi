package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class WelcomeFrame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WelcomeFrame welcomeFrame = new WelcomeFrame();
                welcomeFrame.setVisible(true);
            }
        });
    }
    private JButton enterButton;
    private JButton closeButton;

    private int initialX, initialY;

    public WelcomeFrame() {
        setUndecorated(true);

        String imagePath = "src/main/java/org/example/Images/PokedexClosed.png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            setContentPane(new JLabel(imageIcon));
            setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        } else {
            System.err.println("Error: No se pudo cargar la imagen. La ruta del archivo no es válida: " + imagePath);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        setupLayout();
        setupActions();
    }

    private void initComponents() {
        enterButton = new JButton("Entrar");
        closeButton = new JButton("Cerrar");
        enterButton.setBackground(Color.YELLOW);
        enterButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.YELLOW);
        closeButton.setForeground(Color.BLACK);

        enterButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void setupLayout() {
        setLayout(null);
        enterButton.setBounds(250, 450, 100, 30);
        add(enterButton);
        closeButton.setBounds(50, 450, 100, 30);
        add(closeButton);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialX = e.getX();
                initialY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX = WelcomeFrame.this.getLocation().x + (e.getX() - initialX);
                int newY = WelcomeFrame.this.getLocation().y + (e.getY() - initialY);
                WelcomeFrame.this.setLocation(newX, newY);
            }
        });
    }

    private void setupActions() {
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPokeApp();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon originalIcon = new ImageIcon("src/main/java/org/example/Images/pokemon-png-from-pngfre-46.png");
                Image originalImage = originalIcon.getImage();

                int newWidth = 110;
                int newHeight = 110;
                Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);

                ImageIcon resizedIcon = new ImageIcon(resizedImage);


                UIManager.put("OptionPane.background", Color.ORANGE);
                UIManager.put("Panel.background", Color.ORANGE);

                String[] options = {"Sí", "No"};
                int result = JOptionPane.showOptionDialog(
                        WelcomeFrame.this,
                        "¿Estás seguro de que quieres cerrar la Pokedex?",
                        null,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        resizedIcon,
                        options,
                        options[1]);


                UIManager.put("OptionPane.background", UIManager.getColor("OptionPane.background"));
                UIManager.put("Panel.background", UIManager.getColor("Panel.background"));

                if (result == JOptionPane.YES_OPTION) {
                    closeWelcomeFrame();
                }
            }
        });
    }

    private void openPokeApp() {
        PokemonApp pokemonApp = new PokemonApp();
        pokemonApp.CrearYMostrar();
        dispose();
    }

    private void closeWelcomeFrame() {
        dispose();
    }
}

