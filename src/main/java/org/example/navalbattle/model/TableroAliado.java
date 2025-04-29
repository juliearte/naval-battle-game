package org.example.navalbattle.model;

import javafx.geometry.Bounds;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;

public class TableroAliado {
    private Pane contenedorBarco1, contenedorSubmarino1, contenedorAircraft, contenedorDestroyer;
    private GridPane boardGridPane;

    private int fragataCount = 0;
    private int submarinoCount = 0;
    private int aircraftCount = 0;
    private int destroyerCount = 0;

    public static final int CELL_WIDTH = 35;
    public static final int CELL_HEIGHT = 40;
    public static final int GRID_COLUMNS = 10;
    public static final int GRID_ROWS = 10;

    public TableroAliado(Pane contenedorBarco1, Pane contenedorSubmarino1, Pane contenedorAircraft, Pane contenedorDestroyer, GridPane boardGridPane) {
        this.contenedorBarco1 = contenedorBarco1;
        this.contenedorSubmarino1 = contenedorSubmarino1;
        this.contenedorAircraft = contenedorAircraft;
        this.contenedorDestroyer = contenedorDestroyer;
        this.boardGridPane = boardGridPane;
    }

    public class ShipPlacement {
        String type;
        int col;
        int row;
        boolean isVertical;
        int endCol; // Nueva variable para la columna final
        int endRow; // Nueva variable para la fila final

        public ShipPlacement(String type, int col, int row, boolean isVertical, int endCol, int endRow) {
            this.type = type;
            this.col = col;
            this.row = row;
            this.isVertical = isVertical;
            this.endCol = endCol; // Inicializar columna final
            this.endRow = endRow; // Inicializar fila final
        }
    }

    private List<ShipPlacement> placedShips = new ArrayList<>();

    public void handleDragDetected(MouseEvent event) {
        Dragboard db;
        ClipboardContent content = new ClipboardContent();

        if (event.getSource() == contenedorBarco1) {
            db = contenedorBarco1.startDragAndDrop(TransferMode.MOVE);
            content.putString("Barco");
        } else if (event.getSource() == contenedorSubmarino1) {
            db = contenedorSubmarino1.startDragAndDrop(TransferMode.MOVE);
            content.putString("Submarino");
        } else if (event.getSource() == contenedorAircraft) {
            db = contenedorAircraft.startDragAndDrop(TransferMode.MOVE);
            content.putString("Aircraft");
        } else if (event.getSource() == contenedorDestroyer) {
            db = contenedorDestroyer.startDragAndDrop(TransferMode.MOVE);
            content.putString("Destroyer");
        } else {
            return;
        }

        db.setContent(content);
        event.consume();
    }

    public void handleDragOver(DragEvent event, boolean isVertical) {
        if (event.getGestureSource() != boardGridPane && event.getDragboard().hasString()) {
            Bounds gridBounds = boardGridPane.getBoundsInParent();
            double localX = event.getX() - gridBounds.getMinX();
            double localY = event.getY() - gridBounds.getMinY();

            int col = (int) Math.floor(localX / CELL_WIDTH);
            int row = (int) Math.floor(localY / CELL_HEIGHT);

            String barcoTipo = event.getDragboard().getString();
            int shipLength = getShipWidth(barcoTipo);

            int shipWidth = isVertical ? 1 : shipLength;
            int shipHeight = isVertical ? shipLength : 1;

            if (isValidPosition(col, row, shipWidth, isVertical)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        }
        event.consume();
    }
    public void handleDragDropped(DragEvent event, boolean isVertical) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            Bounds gridBounds = boardGridPane.getBoundsInParent();
            double localX = event.getX() - gridBounds.getMinX();
            double localY = event.getY() - gridBounds.getMinY();

            int col = (int) Math.floor(localX / CELL_WIDTH);
            int row = (int) Math.floor(localY / CELL_HEIGHT);

            String barcoTipo = db.getString();
            int shipLength = getShipWidth(barcoTipo);

            int shipWidth = isVertical ? 1 : shipLength;
            int shipHeight = isVertical ? shipLength : 1;

            if (isValidPosition(col, row, shipLength, isVertical)) {
                // Calcular las posiciones finales
                int colFinal = isVertical ? col : col + shipLength - 1;
                int rowFinal = isVertical ? row + shipLength - 1 : row;

                switch (barcoTipo) {
                    case "Barco":
                        if (fragataCount < 4) {
                            Figures fragata = new Figures(CELL_WIDTH, CELL_HEIGHT, "fragata", isVertical);
                            boardGridPane.add(fragata, col, row, shipWidth, shipHeight);
                            fragataCount++;
                            placedShips.add(new ShipPlacement("Barco", col, row, isVertical, colFinal, rowFinal)); // Guardar ubicación
                            System.out.println("Barco colocado en: " + col + ", " + row + " a " + colFinal + ", " + rowFinal);
                            success = true;
                        }
                        break;
                    case "Submarino":
                        if (submarinoCount < 2) {
                            Figures submarino = new Figures(CELL_WIDTH, CELL_HEIGHT * 3, "submarino", isVertical);
                            boardGridPane.add(submarino, col, row, shipWidth, shipHeight);
                            submarinoCount++;
                            placedShips.add(new ShipPlacement("Submarino", col, row, isVertical, colFinal, rowFinal)); // Guardar ubicación
                            System.out.println("Submarino colocado en: " + col + ", " + row + " a " + colFinal + ", " + rowFinal);
                            success = true;
                        }
                        break;
                    case "Aircraft":
                        if (aircraftCount < 1) {
                            Figures aircraft = new Figures(CELL_WIDTH * 4, CELL_HEIGHT, "aircraft", isVertical);
                            boardGridPane.add(aircraft, col, row, shipWidth, shipHeight);
                            aircraftCount++;
                            placedShips.add(new ShipPlacement("Aircraft", col, row, isVertical, colFinal, rowFinal)); // Guardar ubicación
                            System.out.println("Aircraft colocado en: " + col + ", " + row + " a " + colFinal + ", " + rowFinal);
                            success = true;
                        }
                        break;
                    case "Destroyer":
                        if (destroyerCount < 3) {
                            Figures destroyer = new Figures(CELL_WIDTH * 2, CELL_HEIGHT, "destroyer", isVertical);
                            boardGridPane.add(destroyer, col, row, shipWidth, shipHeight);
                            destroyerCount++;
                            placedShips.add(new ShipPlacement("Destroyer", col, row, isVertical, colFinal, rowFinal)); // Guardar ubicación
                            System.out.println("Destroyer colocado en: " + col + ", " + row + " a " + colFinal + ", " + rowFinal);
                            imprimirPosicionesBarcos(); //Prueba para ver si se colocaron correctamente
                            success = true;
                        }
                        break;
                }

            }
        }

        event.setDropCompleted(success);
        event.consume();
    }



    private int getShipWidth(String barcoTipo) {
        return switch (barcoTipo) {
            case "Barco" -> 1;
            case "Submarino" -> 3;
            case "Aircraft" -> 4;
            case "Destroyer" -> 2;
            default -> 1;
        };
    }

    private boolean isValidPosition(int col, int row, int shipLength, boolean isVertical) {
        int shipWidth = isVertical ? 1 : shipLength;
        int shipHeight = isVertical ? shipLength : 1;

        // Verificar que la posición no se salga de los límites del tablero
        if (col < 0 || row < 0 || col + shipWidth > GRID_COLUMNS || row + shipHeight > GRID_ROWS) {
            return false;
        }

        // Verificar que las celdas donde se quiere colocar el barco estén vacías
        return isCellEmpty(col, row, shipWidth, shipHeight);
    }

    private boolean isCellEmpty(int col, int row, int shipWidth, int shipHeight) {
        for (int i = 0; i < shipWidth; i++) {
            for (int j = 0; j < shipHeight; j++) {
                for (Node node : boardGridPane.getChildren()) {
                    Integer nodeCol = GridPane.getColumnIndex(node);
                    Integer nodeRow = GridPane.getRowIndex(node);
                    if (nodeCol != null && nodeRow != null) {
                        double nodeWidth = node.getBoundsInParent().getWidth() / CELL_WIDTH;
                        double nodeHeight = node.getBoundsInParent().getHeight() / CELL_HEIGHT;
                        double nodeColStart = nodeCol;
                        double nodeRowStart = nodeRow;
                        double nodeColEnd = nodeColStart + nodeWidth;
                        double nodeRowEnd = nodeRowStart + nodeHeight;

                        if (col + i >= nodeColStart && col + i < nodeColEnd && row + j >= nodeRowStart && row + j < nodeRowEnd) {
                            return false; // Ship overlaps with existing ship
                        }
                    }
                }
            }
        }
        return true; // No overlap found
    }


    public boolean todosLosBarcosColocados() {
        return fragataCount == 4 && submarinoCount == 2 && aircraftCount == 1 && destroyerCount == 3;

    }



    public void imprimirPosicionesBarcos() {
        System.out.println("Posiciones de los barcos:");
        for (ShipPlacement ship : placedShips) {
            System.out.println("Tipo: " + ship.type + ", ColumnaI: " + ship.col + ", FilaI: " + ship.row + ", ColumnaF: "+ship.endCol + ", FilaF: "+ ship.endRow + ", Vertical: " + ship.isVertical);
        }
    }



}
