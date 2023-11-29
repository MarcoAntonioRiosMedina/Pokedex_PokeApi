package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

public class PokemonApp {
    private static final String API_BASE_URL = "https://pokeapi.co/api/v2/";
    private static final String POKEMON_ENDPOINT = "pokemon/";
    private static final String TYPES_ENDPOINT = "type/";

    private static JFrame PokedexFrame;
    private static JComboBox<String> typeComboBox;
    private static JComboBox<String> pokemonComboBox;
    private static JTextField searchField;
    private static JButton searchButton;
    private static JLabel pokemonImageLabel;
    private static JLabel attributesLabel;
    private static JPanel bluePanel;
    private static JLabel officialArtworkLabel;

    private static int initialX;
    private static int initialY;

    public static void CrearYMostrar() {
        PokedexFrame = new JFrame("Pokedex");
        PokedexFrame.setUndecorated(true);
        PokedexFrame.setSize(820, 850);
        PokedexFrame.setLayout(null);
        PokedexFrame.getContentPane().setBackground(new Color(255, 51, 51));
        PokedexFrame.setLocationRelativeTo(null);

        PokedexFrame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        typeComboBox = new JComboBox<>();
        typeComboBox.setBounds(10, 10, 150, 25);
        typeComboBox.addItem("Todos");
        typeComboBox.setBackground(Color.YELLOW);
        typeComboBox.setForeground(Color.BLACK);

        pokemonComboBox = new JComboBox<>();
        pokemonComboBox.setBounds(170, 10, 150, 25);
        pokemonComboBox.addItem("");
        pokemonComboBox.setBackground(Color.YELLOW);
        pokemonComboBox.setForeground(Color.BLACK);

        searchField = new JTextField(20);
        searchField.setBounds(330, 10, 150, 25);
        searchField.setBackground(Color.YELLOW);
        searchField.setForeground(Color.BLACK);

        searchButton = new JButton("Buscar");
        searchButton.setBounds(490, 10, 80, 25);
        searchButton.setBackground(Color.YELLOW);
        searchButton.setForeground(Color.BLACK);

        searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MostrarImagenPokemon(searchField.getText());
            }
        });

        pokemonImageLabel = new JLabel();
        pokemonImageLabel.setBounds(10, 50, 800, 500);
        pokemonImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attributesLabel = new JLabel();
        attributesLabel.setBounds(100, 500, 400, 300);
        attributesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        officialArtworkLabel = new JLabel();
        officialArtworkLabel.setBounds(10, 550, 800, 500);
        officialArtworkLabel.setHorizontalAlignment(SwingConstants.CENTER);

        PokedexFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                PokedexFrame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                initialX = e.getX();
                initialY = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                PokedexFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        PokedexFrame.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX = PokedexFrame.getLocation().x + (e.getX() - initialX);
                int newY = PokedexFrame.getLocation().y + (e.getY() - initialY);
                PokedexFrame.setLocation(newX, newY);
            }
        });


        JButton closeButton = new JButton("Cerrar");
        closeButton.setBounds(730, 10, 80, 25);
        closeButton.setBackground(Color.YELLOW);
        closeButton.setForeground(Color.BLACK);

        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon originalIcon = new ImageIcon("src/main/java/org/example/Images/pokemon-png-from-pngfre-46.png");
                Image originalImage = originalIcon.getImage();

                int newWidth = 120;
                int newHeight = 120;
                Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);

                ImageIcon resizedIcon = new ImageIcon(resizedImage);


                UIManager.put("OptionPane.background", Color.ORANGE);
                UIManager.put("Panel.background", Color.ORANGE);
                UIManager.put("OptionPane.messageForeground", Color.BLACK);

                String[] options = {"Sí", "No"};
                int result = JOptionPane.showOptionDialog(
                        PokedexFrame,
                        "¿Estás seguro de que quieres cerrar la Pokedex?",
                        "Confirmar cierre",  // Agrega el título personalizado
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        resizedIcon,
                        options,
                        options[1]);

                UIManager.put("OptionPane.background", UIManager.getColor("OptionPane.background"));
                UIManager.put("Panel.background", UIManager.getColor("Panel.background"));
                UIManager.put("OptionPane.messageForeground", UIManager.getColor("OptionPane.messageForeground"));

                if (result == JOptionPane.YES_OPTION) {
                    closePokemonApp();
                }
            }
        });


        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                if ("Todos".equals(selectedType)) {
                    DatosTodosPokemons();
                } else if (selectedType != null && !selectedType.isEmpty()) {
                    CargarPokemonTipos(selectedType);
                }
            }
        });

        pokemonComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPokemon = (String) pokemonComboBox.getSelectedItem();
                if (selectedPokemon != null && !selectedPokemon.isEmpty()) {
                    MostrarImagenPokemon(selectedPokemon);
                }
            }
        });

        PokedexFrame.getContentPane().add(typeComboBox);
        PokedexFrame.getContentPane().add(pokemonComboBox);
        PokedexFrame.getContentPane().add(searchField);
        PokedexFrame.getContentPane().add(searchButton);
        PokedexFrame.getContentPane().add(pokemonImageLabel);
        PokedexFrame.getContentPane().add(officialArtworkLabel);
        PokedexFrame.getContentPane().add(attributesLabel);
        PokedexFrame.getContentPane().add(closeButton);


        bluePanel = new JPanel();
        bluePanel.setBounds(100, 100, 620, 400);
        bluePanel.setBackground(Color.cyan);
        bluePanel.setOpaque(true);

        bluePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));

        PokedexFrame.getContentPane().add(bluePanel);

        TiposDatos();
        DatosTodosPokemons();

        PokedexFrame.setVisible(true);
        PokedexFrame.setResizable(false);
    }

    public static void TiposDatos() {
        try {
            URL url = new URL(API_BASE_URL + "type");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonElement element = JsonParser.parseReader(reader);

                if (element.isJsonObject()) {
                    JsonObject typesObject = element.getAsJsonObject();
                    JsonArray results = typesObject.getAsJsonArray("results");

                    List<String> typeList = new ArrayList<>();

                    for (JsonElement result : results) {
                        JsonObject typeInfo = result.getAsJsonObject();
                        String typeName = typeInfo.get("name").getAsString();

                        if (isValidPokemon(typeName)) {
                            typeList.add(typeName);
                        }
                    }

                    Collections.sort(typeList);

                    typeComboBox.removeAllItems();
                    typeComboBox.addItem("Todos");

                    for (String type : typeList) {
                        typeComboBox.addItem(type);
                    }
                } else {
                    AppLogger.logError("Respuesta JSON inválida al obtener tipos de Pokémon", null);
                }
            } else {
                AppLogger.logError("Error al recuperar datos de tipos. Código de error HTTP: " + connection.getResponseCode(), null);
            }

            connection.disconnect();
        } catch (Exception e) {
            AppLogger.logError("Error al acceder PokeAPI para tipos: " + e.getMessage(), e);
        }
    }

    public static void DatosTodosPokemons() {
        try {
            URL url = new URL(API_BASE_URL + POKEMON_ENDPOINT + "?limit=1000");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonElement element = JsonParser.parseReader(reader);

                if (element.isJsonObject()) {
                    JsonObject pokemonObject = element.getAsJsonObject();
                    JsonArray results = pokemonObject.getAsJsonArray("results");

                    List<String> pokemonList = new ArrayList<>();

                    for (JsonElement result : results) {
                        JsonObject pokemonInfo = result.getAsJsonObject();
                        String pokemonName = pokemonInfo.get("name").getAsString();

                        if (isValidPokemon(pokemonName)) {
                            pokemonList.add(pokemonName);
                        }
                    }

                    Collections.sort(pokemonList);

                    pokemonComboBox.removeAllItems();
                    pokemonComboBox.addItem("");

                    for (String pokemon : pokemonList) {
                        pokemonComboBox.addItem(pokemon);
                    }
                } else {
                    System.err.println("JSON INVALIDO");
                }
            } else {
                System.err.println("ERROR Pokemon data. HTTP Error Code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("ERROR AL INTENTAR ACCEDER POKEAPI: " + e.getMessage());
        }
    }

    private static boolean isValidPokemon(String name) {
        return !("arcanine-hisui".equals(name) || "armarouge".equals(name) || "unknown".equals(name) || "shadow".equals(name) || "hisui".equals(name));
    }




    public static void CargarPokemonTipos(String type) {
        try {
            if (typeComboBox.getItemCount() > 0) {
                URL url = new URL(API_BASE_URL + TYPES_ENDPOINT + type);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonElement element = JsonParser.parseReader(reader);

                    if (element.isJsonObject()) {
                        JsonObject typeObject = element.getAsJsonObject();
                        JsonArray pokemonArray = typeObject.getAsJsonArray("pokemon");

                        if (pokemonArray != null) {
                            List<String> pokemonList = new ArrayList<>();

                            for (JsonElement pokemonEntry : pokemonArray) {
                                JsonObject pokemonInfo = pokemonEntry.getAsJsonObject();
                                String pokemonName = pokemonInfo.getAsJsonObject("pokemon").get("name").getAsString();


                                if (!"arcanine-hisui".equals(pokemonName) && !"armarouge".equals(pokemonName) && !"unknown".equals(pokemonName) && !"shadow".equals(pokemonName)) {
                                    pokemonList.add(pokemonName);
                                }
                            }

                            Collections.sort(pokemonList);

                            pokemonComboBox.removeAllItems();
                            pokemonComboBox.addItem("");

                            for (String pokemon : pokemonList) {
                                pokemonComboBox.addItem(pokemon);
                            }
                        } else {
                            System.err.println("No hay información de Pokémon para el tipo: " + type);
                        }
                    } else {
                        System.err.println("JSON INVALIDO");
                    }
                } else {
                    System.err.println("ERROR Pokemon data. HTTP Error Code: " + connection.getResponseCode());
                }

                connection.disconnect();
            }
        } catch (Exception e) {
            System.err.println("ERROR AL INTENTAR ACCEDER POKEAPI: " + e.getMessage());
        }
    }

    private static void MostrarImagenPokemon(String input) {
        if (input != null) {
            String pokemonName = input.trim().toLowerCase();
            if (!pokemonName.isEmpty()) {
                try {
                    URL url = new URL(API_BASE_URL + POKEMON_ENDPOINT + pokemonName);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        JsonElement element = JsonParser.parseReader(reader);

                        if (element.isJsonObject()) {
                            JsonObject pokemonObject = element.getAsJsonObject();
                            String tipoPokemon = obtenerTipoPokemon(pokemonObject);

                            typeComboBox.setSelectedItem(tipoPokemon);

                            pokemonComboBox.setSelectedItem(pokemonName);

                            String imageUrl = pokemonObject.getAsJsonObject("sprites").get("front_default").getAsString();
                            String officialArtworkUrl = pokemonObject.getAsJsonObject("sprites").get("other").getAsJsonObject()
                                    .getAsJsonObject("official-artwork").get("front_default").getAsString();


                            switch (tipoPokemon) {
                                case "steel":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/1024px-Pokémon_Steel_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "water":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/640px-Pokémon_Water_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "bug":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/PokemonBICHO.png", 50, 50);
                                    break;
                                case "dragon":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/dragon_type.png", 50, 50);
                                    break;
                                case "electric":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/electric.png", 50, 50);
                                    break;
                                case "ghost":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/ghost_type_symbol.png", 50, 50);
                                    break;
                                case "fire":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/FUEGO.png", 50, 50);
                                    break;
                                case "fairy":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/HADA_preview_rev_1.png", 50, 50);
                                    break;
                                case "ice":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/Pokémon_Ice_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "fighting":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/fighting_energy_card_vector_symbol_by_biochao_dezrx1z-fullview.png", 50, 50);
                                    break;
                                case "normal":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/Pokémon_Normal_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "grass":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/grass_type.png", 50, 50);
                                    break;
                                case "psychic":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/Pokémon_Psychic_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "rock":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/Pokémon_Rock_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "dark":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/Pokémon_Dark_Type_Icon.svg.png", 50, 50);
                                    break;
                                case "ground":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/1200px-Pokémon_Ground_Type.png", 50, 50);
                                    break;
                                case "flying":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/VOLADOR.png", 50, 50);
                                    break;
                                case "Poison":
                                    mostrarImagenEnPanelAzul("src/main/java/org/example/Images/poison_type_symbol_pasio_by_jormxdos_dfgbe7y-fullview.png", 50, 50);
                                    break;
                            }

                            pokemonComboBox.setSelectedItem(pokemonName);

                            MostrarImagenPokemonUrl(imageUrl, officialArtworkUrl);
                            MostrarAtributosPokemon(pokemonObject);
                        } else {
                            AppLogger.logError("Respuesta JSON inválida al buscar el Pokémon: " + pokemonName, null);
                        }
                    } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        AppLogger.logError("El Pokémon con el nombre '" + pokemonName + "' no se encuentra.", null);
                        JOptionPane.showMessageDialog(PokedexFrame, "El Pokémon con el nombre '" + pokemonName + "' no se encuentra.", "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
                        clearPokemonDetails();
                    } else {
                        AppLogger.logError("Error al acceder a la API de PokeAPI. Código de error HTTP: " + connection.getResponseCode(), null);
                        JOptionPane.showMessageDialog(PokedexFrame, "Error al acceder a la API de PokeAPI. Código de error HTTP: " + connection.getResponseCode(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
                        System.err.println("ERROR Pokemon data. HTTP Error Code: " + connection.getResponseCode());
                        clearPokemonDetails();
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    AppLogger.logError("ERROR AL INTENTAR ACCEDER POKEAPI: " + e.getMessage(), e);
                    JOptionPane.showMessageDialog(PokedexFrame, "Error: Pokémon no encontrado", "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
                    clearPokemonDetails();
                }
            } else {
                clearPokemonDetails();
            }
        }
    }

    private static void MostrarImagenPokemonUrl(String imageUrl, String officialArtworkUrl) {
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {

                URL pokemonImageUrl = new URL(imageUrl);
                ImageIcon pokemonImageIcon = new ImageIcon(pokemonImageUrl);
                Image pokemonImage = pokemonImageIcon.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT);
                ImageIcon scaledPokemonImageIcon = new ImageIcon(pokemonImage);
                pokemonImageLabel.setIcon(scaledPokemonImageIcon);
                centerImage(pokemonImageLabel);


                URL officialArtworkImageUrl = new URL(officialArtworkUrl);
                ImageIcon officialArtworkImageIcon = new ImageIcon(officialArtworkImageUrl);
                Image officialArtworkImage = officialArtworkImageIcon.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT);
                ImageIcon scaledOfficialArtworkImageIcon = new ImageIcon(officialArtworkImage);


                officialArtworkLabel.setBounds(480, 490, 250, 300);
                officialArtworkLabel.setHorizontalAlignment(SwingConstants.CENTER);
                officialArtworkLabel.setVerticalAlignment(SwingConstants.CENTER);
                officialArtworkLabel.setIcon(scaledOfficialArtworkImageIcon);
                centerImage(officialArtworkLabel);
            }
        } catch (Exception e) {
            System.err.println("Error loading Pokemon images: " + e.getMessage());
            clearPokemonDetails();
        }
    }

    private static void centerImage(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }





    private static void closePokemonApp() {
        PokedexFrame.dispose();
    }

    private static void MostrarAtributosPokemon(JsonObject pokemonObject) {
        StringBuilder attributes = new StringBuilder("<html>");

        attributes.append("<font color='black'><b><font size='5'>Nombre:</font></b></font> ").append("<font color='black' size='5'>").append(pokemonObject.get("name").getAsString()).append("</font>").append("<br>");
        attributes.append("<font color='black'><b><font size='5'>Peso:</font></b></font> ").append("<font color='black' size='5'>").append(pokemonObject.get("weight").getAsInt()).append(" kg</font>").append("<br>");
        attributes.append("<font color='black'><b><font size='5'>Altura:</font></b></font> ").append("<font color='black' size='5'>").append(pokemonObject.get("height").getAsInt()).append(" cm</font>").append("<br>");


        int baseExperience = pokemonObject.get("base_experience").getAsInt();
        attributes.append("<font color='black'><b><font size='5'>Experiencia Base:</font></b></font> ").append("<font color='black' size='5'>").append(baseExperience).append("</font>").append(" puntos<br>");


        JsonArray abilitiesArray = pokemonObject.getAsJsonArray("abilities");
        if (abilitiesArray != null && abilitiesArray.size() > 0) {
            attributes.append("<font color='black'><b><font size='5'>Habilidades:</font></b></font> ");
            for (JsonElement abilityElement : abilitiesArray) {
                JsonObject abilityObject = abilityElement.getAsJsonObject();
                String abilityName = abilityObject.getAsJsonObject("ability").get("name").getAsString();
                attributes.append("<font color='black' size='5'>").append(abilityName).append("</font>").append(", ");
            }
            attributes.delete(attributes.length() - 2, attributes.length());
            attributes.append("<br>");
        }


        JsonArray statsArray = pokemonObject.getAsJsonArray("stats");
        if (statsArray != null && statsArray.size() > 0) {
            attributes.append("<font color='black'><b><font size='5'>Estadísticas de Base:</font></b></font> ");
            for (JsonElement statElement : statsArray) {
                JsonObject statObject = statElement.getAsJsonObject();
                String statName = statObject.getAsJsonObject("stat").get("name").getAsString();
                int statValue = statObject.get("base_stat").getAsInt();
                attributes.append("<font color='black' size='5'>").append(statName).append(": ").append(statValue).append("</font>").append(", ");
            }
            attributes.delete(attributes.length() - 2, attributes.length());
            attributes.append("<br>");
        }

        JsonArray typesArray = pokemonObject.getAsJsonArray("types");
        if (typesArray != null && typesArray.size() > 0) {
            attributes.append("<font color='black'><b><font size='5'>Tipo(s):</font></b></font> ");
            for (JsonElement typeElement : typesArray) {
                JsonObject typeObject = typeElement.getAsJsonObject();
                String typeName = typeObject.getAsJsonObject("type").get("name").getAsString();
                attributes.append("<font color='black' size='5'>").append(typeName).append("</font>").append(", ");
            }
            attributes.delete(attributes.length() - 2, attributes.length());
            attributes.append("<br>");
        }


        JsonArray eggGroupsArray = pokemonObject.getAsJsonArray("egg_groups");
        if (eggGroupsArray != null && eggGroupsArray.size() > 0) {
            attributes.append("<font color='black'><b><font size='5'>Grupos de Huevos:</font></b></font> ");
            for (JsonElement eggGroupElement : eggGroupsArray) {
                String eggGroupName = eggGroupElement.getAsJsonObject().get("name").getAsString();
                attributes.append("<font color='black' size='5'>").append(eggGroupName).append("</font>").append(", ");
            }
            attributes.delete(attributes.length() - 2, attributes.length());
            attributes.append("<br>");
        }
        attributes.append("</html>");

        attributesLabel.setText(attributes.toString());
    }

    private static String obtenerTipoPokemon(JsonObject pokemonObject) {
        JsonArray tiposArray = pokemonObject.getAsJsonArray("types");
        if (tiposArray != null && tiposArray.size() > 0) {
            JsonObject tipoObject = tiposArray.get(0).getAsJsonObject();
            return tipoObject.getAsJsonObject("type").get("name").getAsString();
        }
        return "";
    }

    private static JLabel tipoPokemonLabel = new JLabel();

    private static void mostrarImagenEnPanelAzul(String rutaImagen, int ancho, int alto) {
        try {
            System.out.println("Mostrando imagen en panel azul: " + rutaImagen);

            ImageIcon tipoPokemonImageIcon = new ImageIcon(new ImageIcon(rutaImagen).getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT));
            tipoPokemonLabel.setIcon(tipoPokemonImageIcon);

            bluePanel.setLayout(new BorderLayout());
            bluePanel.add(tipoPokemonLabel, BorderLayout.NORTH);
            PokedexFrame.validate();
        } catch (Exception e) {
            System.err.println("Error loading type image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void clearPokemonDetails() {
        pokemonImageLabel.setIcon(null);
        attributesLabel.setText("");
        officialArtworkLabel.setIcon(null);
        bluePanel.removeAll();
        bluePanel.revalidate();
        bluePanel.repaint();
        tipoPokemonLabel.setIcon(null);
    }
}



