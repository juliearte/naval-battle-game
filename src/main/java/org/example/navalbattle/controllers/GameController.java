package org.example.navalbattle.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import org.example.navalbattle.model.TableroAliado;
import org.example.navalbattle.model.Player;
import org.example.navalbattle.model.Figures;
import org.example.navalbattle.views.InstructionsView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.navalbattle.model.TableroAliado.GRID_COLUMNS;
import static org.example.navalbattle.model.TableroAliado.GRID_ROWS;


public class GameController {
    private Player player;

    @FXML
    private Label nicknameLabel;

    @FXML
    private Pane contenedorBarco1, contenedorSubmarino1, contenedorAircraft1,contenedorDestroyer1;


    @FXML
    private GridPane boardGridPane;

    @FXML
    private GridPane machineBoardGridPane;


    //verificacion del juego de la mquina
    @FXML
    private Button machineGameVerification;

    private boolean machineRevealed = false;

    @FXML
    private ToggleButton verticalHorizontal;



    private boolean isVertical = false;

    private TableroAliado tableroAliado;

    //Barcos de la maquina
    private List<Figures> machineFleet = new ArrayList<>();



    public void initialize() {
        // Inicializar una fragata con las dimensiones correspondientes
        Figures barco = new Figures(50, 50, "fragata", isVertical);  // Usar tamaño basado en casillas
        contenedorBarco1.getChildren().add(barco);

        // Inicializar un submarino con las dimensiones correspondientes
        Figures submarino = new Figures(50 * 3, 50, "submarino", isVertical); // 3 casillas en horizontal
        contenedorSubmarino1.getChildren().add(submarino);


        // Inicializar un avión con las dimensiones correspondientes
        Figures avion = new Figures(50 * 4, 50, "aircraft", isVertical); // Usar tamaño basado en casillas
        contenedorAircraft1.getChildren().add(avion);

        // Inicializar un destructor con las dimensiones correspondientes
        Figures destructor = new Figures(50 * 2, 50, "destroyer", isVertical); // Usar tamaño basado en casillas
        contenedorDestroyer1.getChildren().add(destructor);

        // Inicializar el tablero con los contenedores correspondientes
        tableroAliado = new TableroAliado(contenedorBarco1, contenedorSubmarino1, contenedorAircraft1, contenedorDestroyer1, boardGridPane);


        // Mandar los objetos al tablero del aliado
        // Event handler para el ToggleButton
        updateToggleButtonText();

//          Comprobación que va en en start button
//        if (tableroAliado.todosLosBarcosColocados()) {
//            System.out.println("La colocación de barcos está completa.");
//            tableroAliado.imprimirPosicionesBarcos();
//        } else {
//            System.out.println("Aún quedan barcos por colocar.");
//        }

        //configurar flota de la maquina
        machineFleet.add(new Figures(50, 50, "fragata", isVertical));
        machineFleet.add(new Figures(50 * 3, 50, "submarino", isVertical));
        machineFleet.add(new Figures(50 * 4, 50, "aircraft", isVertical));
        machineFleet.add(new Figures(50 * 2, 50, "destroyer", isVertical));

        //colocar los barcos de la máquina aleatoriamente
        placeMachineFleet();

        // Configurar el botón de verificación de la flota de la máquina
        machineGameVerification.setOnAction(e -> handleMachineGameVerification());
    }


    //codigo maquina
    private void placeMachineFleet() {
        Random random = new Random();

        // Colocar cada barco de la máquina de forma aleatoria
        for (Figures ship : machineFleet) {
            boolean placed = false;
            while (!placed) {
                boolean isVertical = random.nextBoolean();
                int col = random.nextInt(GRID_COLUMNS);
                int row = random.nextInt(GRID_ROWS);

                if (isValidPositionForMachine(col, row, ship.getLength(), isVertical)) {
                    addShipToMachineBoard(ship, col, row, ship.getLength(), isVertical);
                    placed = true;
                }
            }
        }
    }

    private boolean isValidPositionForMachine(int col, int row, int shipLength, boolean isVertical) {
        int shipWidth = isVertical ? 1 : shipLength;
        int shipHeight = isVertical ? shipLength : 1;

        if (col < 0 || row < 0 || col + shipWidth > GRID_COLUMNS || row + shipHeight > GRID_ROWS) {
            return false;
        }

        for (int i = col; i < col + shipWidth; i++) {
            for (int j = row; j < row + shipHeight; j++) {
                if (!isCellEmptyOnMachineBoard(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }


    private boolean isCellEmptyOnMachineBoard(int col, int row) {
        for (Node node : machineBoardGridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                Button cell = (Button) node;
                // Verificar si la celda está ocupada
                if (cell.getUserData() != null) {
                    return false; // La celda está ocupada
                }
            }
        }
        return true; // La celda está vacía
    }



    private void addShipToMachineBoard(Figures ship, int col, int row, int shipLength, boolean isVertical) {
        for (int i = 0; i < shipLength; i++) {
            int targetCol = isVertical ? col : col + i;
            int targetRow = isVertical ? row + i : row;

            Button cell = getButtonAtMachineBoard(targetCol, targetRow);
            if (cell != null) {
                // Asignar el barco a la celda
                cell.setStyle("-fx-background-color: gray;");
                cell.setUserData(ship.getName()); // Almacenar el nombre del barco
            }
        }
    }


    private Button getButtonAtMachineBoard(int col, int row) {
        for (Node node : machineBoardGridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                return (Button) node;
            }
        }
        return null;
    }





    @FXML
    private void toggleOrientation() {
        isVertical = verticalHorizontal.isSelected();
        updateToggleButtonText();
    }
    private void updateToggleButtonText() {
        verticalHorizontal.setText(isVertical ? "Horizontal" : "Vertical");
    }


    @FXML
    public void handleDragDetected(MouseEvent event) { //Eventos para detectar arrastre
        tableroAliado.handleDragDetected(event);
    }

    @FXML
    public void handleDragOver(DragEvent event) { //Evento para detectar donde se pone el objeto
        tableroAliado.handleDragOver(event, isVertical);
    }

    @FXML
    public void handleDragDropped(DragEvent event) { //evento para arrastrar y solta
        tableroAliado.handleDragDropped(event, isVertical);
    }


    public void setPlayer(Player player) {
        this.player = player;
        this.setNicknameToLabel(this.player.getNickname());
    }

    public void setNicknameToLabel(String nickname) {
        this.nicknameLabel.setText("Welcome " + nickname + "!");
    }
    public void onActionGameInstructions (ActionEvent actionEvent) throws IOException{
        InstructionsView.getInstance();
    }

    //metodos para machine game verification
    @FXML
    private void handleMachineGameVerification() {
        if (!machineRevealed) {
            mostrarTableroMaquina();
            machineRevealed = true;
        } else {
            machineGameVerification.setDisable(true);
        }
    }

    private void mostrarTableroMaquina() {
        for (Node node : machineBoardGridPane.getChildren()) {
            Button cell = (Button) node;
            String shipType = (String) cell.getUserData();  // Obtener el tipo de barco desde los datos del botón
            if (shipType != null) {
                // Cambiar el estilo para mostrar los barcos de forma clara
                cell.setStyle("-fx-background-color: gray; -fx-border-color: black; -fx-border-width: 2;");
                // agregar más detalles visuales aquí si es necesario, como un texto que indique el tipo de barco
                cell.setText(shipType);
            }
        }
    }

}
