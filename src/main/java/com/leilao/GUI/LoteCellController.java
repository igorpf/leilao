package com.leilao.GUI;

import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import javafx.scene.paint.Paint;

/**
 * Created by Arthur on 19/05/2016.
 */
public class LoteCellController {

    @FXML private AnchorPane root;

    @FXML private Label nameLabel, priceLabel, estadoLabel, tempoLabel;
    @FXML private Label typeLabel, areaLabel, roomsLabel, bathroomsLabel;

    public void setImovel(Imovel t) {

        setBasicInfo(t);

        typeLabel.setText("Imóvel " + t.getTipo().name());

        areaLabel.setText(String.format("%.0f m²", t.getArea()));

        try {
            boolean plural = t.getNumeroBanheiros() != 1;
            bathroomsLabel.setText(String.format("%d banheiro%s", t.getNumeroBanheiros(), plural ? "s" : ""));
        } catch (Exception e) {
            bathroomsLabel.setText("");
        }

        try {
            boolean plural = t.getNumeroQuartos() != 1;
            roomsLabel.setText(String.format("%d quarto%s", t.getNumeroQuartos(), plural ? "s" : ""));
        } catch (Exception e) {
            roomsLabel.setText("");
        }
    }

    public void setLote(Lote t) {

        setBasicInfo(t);

        typeLabel.setText("Lote");
        areaLabel.setText("");
        roomsLabel.setText("");
        bathroomsLabel.setText("");
    }

    private void setBasicInfo(Lote t) {
        nameLabel.setText(t.getNome());
        setPriceLabel(t.getLanceAtual());

        if(!t.isAprovado() && !t.isFinalizado() && !t.isVendido()) {
            estadoLabel.setText("Não aprovado");
            estadoLabel.setTextFill(Paint.valueOf("ff0000"));
        } else if(t.isAprovado() && !t.isFinalizado() && !t.isVendido()) {
            estadoLabel.setText("Ativo");
            estadoLabel.setTextFill(Paint.valueOf("00ff00"));
        } else if(t.isFinalizado() && !t.isVendido()) {
            tempoLabel.setText("");
            estadoLabel.setText("Encerrado");
            estadoLabel.setTextFill(Paint.valueOf("ff0000"));
        } else if(t.isFinalizado() && t.isVendido()) {
            tempoLabel.setText("");
            estadoLabel.setText("Vendido");
            estadoLabel.setTextFill(Paint.valueOf("0000ff"));
        }

        setTimeLeft(t);
    }

    private void setTimeLeft(Lote t) {
        long timeLeft = t.getTimeLeft();

        if(timeLeft > 0){
            Long days = TimeUnit.MILLISECONDS.toDays(timeLeft);
            timeLeft = timeLeft - TimeUnit.DAYS.toMillis(days);

            Long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
            timeLeft = timeLeft - TimeUnit.HOURS.toMillis(hours);

            Long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft);
            timeLeft = timeLeft - TimeUnit.MINUTES.toMillis(minutes);

            Long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft);

            tempoLabel.setText(days.toString() + "d "
                               + hours.toString() + "h"
                               + minutes.toString() + "min"
                               + seconds.toString());
        }
    }

    public DoubleProperty prefWidthProperty() {
        return root.prefWidthProperty();
    }

    private void setPriceLabel(BigDecimal price) {
        priceLabel.setText(NumberFormat.getCurrencyInstance().format(price.doubleValue()));
    }
}
