package com.example.navalbattlegame.controller;

import com.example.navalbattlegame.exceptions.AlreadyShotException;
import com.example.navalbattlegame.model.BoardImpl;
import com.example.navalbattlegame.model.BoardAdapter;
import com.example.navalbattlegame.model.GameStateHandler;
import com.example.navalbattlegame.model.Machine;
import com.example.navalbattlegame.model.Player;
import com.example.navalbattlegame.model.ShotResult;
import com.example.navalbattlegame.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class GameController {
    private static final int SIZE = 10;
    private static final int CELL = 40;

    @FXML private AnchorPane playerBoard;
    @FXML private AnchorPane machineBoard;
    @FXML private AnchorPane shipPort;
    @FXML private Button showButton;
    @FXML private Button instructionsButton;

    private Player player;
    private Machine machine;
    private BoardAdapter playerAdapter;
    private BoardAdapter machineAdapter;

    private boolean playerTurn = false;
    private boolean gameStarted = false;
    private int placedBoats = 0;

    private final ImagePattern bombPattern = new ImagePattern(
            new Image(getClass().getResourceAsStream(
                    "/com/example/navalbattlegame/images/bomba.png")));
    private final ImagePattern xPattern = new ImagePattern(
            new Image(getClass().getResourceAsStream(
                    "/com/example/navalbattlegame/images/boton-x.png")));
    private final ImagePattern sunkPattern = new ImagePattern(
            new Image(getClass().getResourceAsStream(
                    "/com/example/navalbattlegame/images/crucero.png")));

    /** Called by WelcomeController */
    public void initializeBoard(String nick) {
        player = new Player(nick == null || nick.isBlank() ? "Jugador" : nick);
        machine = new Machine(); // auto-places ships

        placedBoats = 0;
        playerTurn = false;
        gameStarted = false;

        createBoard(playerBoard);
        createBoard(machineBoard);

        playerAdapter = new BoardAdapter(playerBoard, player.getOwnBoard(), bombPattern, xPattern, sunkPattern, true);
        machineAdapter = new BoardAdapter(machineBoard, machine.getOwnBoard(), bombPattern, xPattern, sunkPattern, false);

        addClickEventsToMachineBoard();
        createDragShips();

        showButton.setDisable(false);
        GameStateHandler.clearSavedGame();
    }

    private void createBoard(AnchorPane board) {
        board.getChildren().clear();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Rectangle cell = new Rectangle(CELL, CELL);
                cell.setX(c * CELL);
                cell.setY(r * CELL);
                cell.setFill(Color.web("#9EECFF"));
                cell.setStroke(Color.BLACK);
                board.getChildren().add(cell);
            }
        }
    }

    private void createDragShips() {
        createShipRow(1, 0, 4, 1);
        createShipRow(3, 2, 3, 2);
        createShipRow(12, 2, 2, 3);
        createShipRow(7, 0, 1, 4);
    }
    private void createShipRow(int sx, int sy, int len, int cnt) {
        for (int i = 0; i < cnt; i++) {
            Rectangle ship = new Rectangle(len * CELL, CELL);
            ship.setFill(switch(len) {
                case 4 -> Color.SALMON;
                case 3 -> Color.YELLOW;
                case 2 -> Color.CORAL;
                default -> Color.YELLOWGREEN;
            });
            ship.setStroke(Color.BLACK);
            ship.setId(String.valueOf(len));
            ship.setX(sx * CELL);
            ship.setY(sy * CELL);
            addDragEvents(ship, sx * CELL, sy * CELL);
            shipPort.getChildren().add(ship);
            sx += len + 1;
        }
    }

    private void addDragEvents(Rectangle ship, double initX, double initY) {
        ship.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                double w = ship.getWidth();
                ship.setWidth(ship.getHeight());
                ship.setHeight(w);
            }
        });
        ship.setOnMouseDragged(e -> {
            ship.setX(e.getX()); ship.setY(e.getY()); e.setDragDetect(false);
        });
        ship.setOnMouseReleased(e -> {
            double nx = CELL * Math.floor(ship.getX()/CELL);
            double ny = CELL * Math.floor(ship.getY()/CELL);
            ship.setX(nx); ship.setY(ny);
            if (!playerBoard.localToScene(playerBoard.getBoundsInLocal()).contains(
                    ship.localToScene(ship.getBoundsInLocal()))) {
                ship.setX(initX); ship.setY(initY); return;
            }
            int size = Integer.parseInt(ship.getId());
            boolean vert = ship.getHeight()/CELL == size;
            int ok=0, r0=0, c0=0;
            for (int i=0;i<playerBoard.getChildren().size();i++) {
                Rectangle cell=(Rectangle)playerBoard.getChildren().get(i);
                if (ship.localToScene(ship.getBoundsInLocal())
                        .contains(cell.localToScene(cell.getBoundsInLocal()))) {
                    int r=i/SIZE, c=i%SIZE;
                    if (!player.freePosition(r,c)) { ok=0; break; }
                    if (ok==0) { r0=r; c0=c; }
                    ok++;
                }
            }
            if (ok==size) {
                player.boatPosition(vert,r0,c0,size);
                ship.setVisible(false);
                playerAdapter.paint();
                placedBoats++;
                if (placedBoats==10) startMatch();
            } else {
                ship.setX(initX); ship.setY(initY);
            }
        });
    }

    private void startMatch() {
        new AlertBox().showAlert("INFORMATION","¡El juego ha iniciado!","Dispara a la flota enemiga…",Alert.AlertType.INFORMATION);
        gameStarted=true;
        playerTurn=true;
        showButton.setDisable(true);
        machineAdapter.setRevealShips(false);
        machineAdapter.paint();  // Actualizar para ocultar barcos si estaban visibles
        playerBoard.toBack(); machineBoard.toFront();
    }

    private void addClickEventsToMachineBoard() {
        for (int i=0;i<machineBoard.getChildren().size();i++) {
            Rectangle cell=(Rectangle)machineBoard.getChildren().get(i);
            Rectangle ref=cell; int r=i/SIZE, c=i%SIZE;
            cell.setOnMouseClicked(e -> onPlayerShot(e,r,c,ref));
        }
    }

    private void onPlayerShot(MouseEvent e,int r,int c,Rectangle clicked) {
        if (!gameStarted||!playerTurn||e.getButton()!=MouseButton.PRIMARY) return;
        try {
            ShotResult res = machine.getOwnBoard().shoot(r,c);
            clicked.setOnMouseClicked(null);
            if (res==ShotResult.WATER) {
                machineAdapter.paintCell(r,c);
                playerTurn=false;
                save();

                // Comprobación adicional de victoria antes de pasar turno
                if (checkForVictory(machine.getOwnBoard(), true)) {
                    return;
                }

                machineTurn();
                return;
            }
            if (res==ShotResult.SUNK) machineAdapter.paint(); else machineAdapter.paintCell(r,c);

            // Verificamos victoria después de un impacto o hundimiento
            if (checkForVictory(machine.getOwnBoard(), true)) {
                return;
            }
            save();
        } catch(AlreadyShotException ex) {
            new AlertBox().showAlert("WARNING","Casilla repetida",ex.getMessage(),Alert.AlertType.WARNING);
        }
    }

    // Método auxiliar para verificar victoria y mostrarlo apropiadamente
    private boolean checkForVictory(com.example.navalbattlegame.model.Board board, boolean playerWins) {
        if (board.allBoatsSunk()) {
            finish(playerWins);
            return true;
        }
        return false;
    }

    private void machineTurn() {
        while(!playerTurn && gameStarted) {
            int[] shot = machine.nextShot(player.getOwnBoard());
            int r = shot[0], c = shot[1];
            try {
                ShotResult res = player.getOwnBoard().shoot(r,c);
                if (res == ShotResult.WATER) {
                    playerAdapter.paintCell(r,c);
                    playerTurn = true;
                    save();
                    return;
                }
                if (res == ShotResult.SUNK) {
                    playerAdapter.paint();
                } else {
                    playerAdapter.paintCell(r,c);
                }

                // Verificar si la máquina ganó después de un impacto o hundimiento
                if (checkForVictory(player.getOwnBoard(), false)) {
                    return;
                }
                save();
            } catch(AlreadyShotException ignored) {
                // Si ya se disparó en esta posición, la máquina debería intentar otra
                // No hacer nada y continuar el bucle intentando otro disparo
            }
        }
    }

    @FXML void onHandleShowButton(ActionEvent e) {
        if (!gameStarted) {
            machineAdapter.setRevealShips(true);
            machineAdapter.paint();
        }
    }

    @FXML void onHandleInstructionsButton(ActionEvent e) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO",
                "Bienvenido a BattleField.\n\n" +
                        "- Arrastra tu flota al tablero (clic derecho para rotar).\n" +
                        "- Dispara haciendo clic en el tablero enemigo.\n" +
                        "- Gana quien hunda los 10 barcos rivales.\n" +
                        "- Usa el botón «Mostrar» para ver la flota de la IA.",Alert.AlertType.INFORMATION);
    }

    private void save() {
        GameStateHandler.saveGameState(
                new GameStateHandler.GameState(
                        player.getPlayerTable(),
                        machine.getmachineTable(),
                        player.getPlayerNickName(),0,0,playerTurn,gameStarted));
    }

    public void loadGame() {
        var s=GameStateHandler.loadGameState(); if(s==null) return;
        player=new Player(s.getPlayerNickName()); machine=new Machine();
        ((BoardImpl)player.getOwnBoard()).loadFromList(s.getPlayerTable());
        ((BoardImpl)machine.getOwnBoard()).loadFromList(s.getMachineTable());
        gameStarted=s.isGameInProgress(); playerTurn=s.isPlayerTurn(); placedBoats=10;
        createBoard(playerBoard); createBoard(machineBoard);
        playerAdapter=new BoardAdapter(playerBoard,player.getOwnBoard(),bombPattern,xPattern,sunkPattern,true);
        machineAdapter=new BoardAdapter(machineBoard,machine.getOwnBoard(),bombPattern,xPattern,sunkPattern,false);
        playerAdapter.paint(); machineAdapter.paint(); addClickEventsToMachineBoard();
        showButton.setDisable(gameStarted);
        playerBoard.toBack(); machineBoard.toFront(); machineBoard.setDisable(!gameStarted);
        if(gameStarted && !playerTurn) machineTurn();
    }

    private void finish(boolean win) {
        // Mostrar mensaje de fin de juego
        String message = win ? "¡Ganaste!" : "La IA ganó :(";

        // Crear diálogo con opciones para reiniciar o salir
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Fin de partida");
        alert.setHeaderText(message);
        alert.setContentText("¿Quieres iniciar una nueva partida?");

        ButtonType newGameButton = new ButtonType("Nueva partida");
        ButtonType closeButton = new ButtonType("Cerrar");
        alert.getButtonTypes().setAll(newGameButton, closeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == newGameButton) {
            // Reiniciar juego
            String playerNickName = player.getPlayerNickName();
            initializeBoard(playerNickName);
        } else {
            // Solo terminar el juego
            machineBoard.setDisable(true);
            playerBoard.setDisable(true);
            gameStarted = false;
            playerTurn = false;
        }

        GameStateHandler.clearSavedGame();
    }
}