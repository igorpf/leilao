package com.leilao.GUI;

import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Arthur on 19/05/2016.
 */
public class LoteCellController {

    @FXML private AnchorPane root;

    @FXML private Label nameLabel, priceLabel, descriptionLabel;
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
        descriptionLabel.setText(t.getDescricao());
        if (t.getLanceAtual() == BigDecimal.ZERO)
            setPriceLabel(t.getValorMinimo());
        else
            setPriceLabel(t.getLanceAtual());
    }



    public DoubleProperty prefWidthProperty() {
        return root.prefWidthProperty();
    }

    private void setPriceLabel(BigDecimal price) {
        priceLabel.setText(NumberFormat.getCurrencyInstance().format(price.doubleValue()));
    }
}
