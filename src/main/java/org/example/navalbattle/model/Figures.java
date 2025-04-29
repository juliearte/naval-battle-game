package org.example.navalbattle.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static org.example.navalbattle.model.TableroAliado.CELL_HEIGHT;
import static org.example.navalbattle.model.TableroAliado.CELL_WIDTH;

public class Figures extends Canvas {
    private Image backgroundImage;
    private String tipo;
    private Image image;
    private boolean isVertical;
    private int casillas;  // Número de casillas que ocupa el barco

    public Figures(double width, double height, String tipo, boolean isVertical) {
        super(width, height);
        this.tipo = tipo;
        this.isVertical = isVertical;
        this.casillas = getCasillasForType(tipo);  // Asigna el número de casillas según el tipo de barco
        backgroundImage = new Image(getClass().getResource("/org/example/navalbattle/images/aaaa.png").toExternalForm());
        setImageForShipType();
        ajustarCanvas();
        dibujarBarco();
    }

    private void setImageForShipType() {
        switch (tipo) {
            case "fragata":
                image = new Image(getClass().getResource("/org/example/navalbattle/images/fragata.png").toExternalForm());
                break;
            case "submarino":
                image = new Image(getClass().getResource("/org/example/navalbattle/images/submarino.png").toExternalForm());
                break;
            case "aircraft":
                image = new Image(getClass().getResource("/org/example/navalbattle/images/portaviones.png").toExternalForm());
                break;
            case "destroyer":
                image = new Image(getClass().getResource("/org/example/navalbattle/images/destructor.png").toExternalForm());
                break;
        }
    }

    private int getCasillasForType(String tipo) {
        switch (tipo) {
            case "fragata": return 1;       // Fragata ocupa 1 casilla
            case "submarino": return 3;     // Submarino ocupa 3 casillas
            case "aircraft": return 4;      // Portaaviones ocupa 4 casillas
            case "destroyer": return 2;     // Destructor ocupa 2 casillas
            default: return 1;              // Valor predeterminado en caso de error
        }
    }

    // Metodo que devuelve la longitud del barco (en casillas)
    public int getLength() {
        return casillas;
    }

    // Metodo que devuelve el nombre del barco (tipo)
    public String getName() {
        return tipo;
    }

    private void ajustarCanvas() {
// Ajusta el ancho y el alto en función de la orientación y el número de casillas de cada tipo de barco
        if (isVertical) {
            setWidth(CELL_WIDTH); // Ancho de una casilla en orientación vertical
            setHeight(CELL_HEIGHT * casillas); // Altura total en función de las casillas en orientación vertical
        } else {
            setWidth(CELL_WIDTH * casillas); // Ancho total en función de las casillas en orientación horizontal
            setHeight(CELL_HEIGHT); // Altura de una casilla en orientación horizontal
        }

    }



    private void dibujarBarco() {
        ajustarCanvas();  // Ajusta el tamaño del Canvas antes de dibujar
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());  // Limpiar el Canvas antes de dibujar
        gc.setFill(Color.LIGHTSKYBLUE);
        //gc.drawImage(backgroundImage, 0, 0, getWidth(), getHeight());

        if (isVertical) {
            gc.save();
            gc.translate(getWidth() / 2, getHeight() / 2); // Se traslada para girar
            gc.rotate(90); // Gira la imagen para que se muestre en vertical
            gc.drawImage(image, -getHeight() / 2, -getWidth() / 2, getHeight(), getWidth());
            gc.restore();
        } else {
            gc.drawImage(image, 0, 0, getWidth(), getHeight());
        }
    }


    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
        dibujarBarco();  // Redibujar el barco con la nueva orientación
    }
}
