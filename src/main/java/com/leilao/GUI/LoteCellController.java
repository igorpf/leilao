package com.leilao.GUI;

import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Arthur on 19/05/2016.
 */
public class LoteCellController {

    @FXML private Label nameLabel, priceLabel, descriptionLabel;
    @FXML private Label typeLabel, areaLabel, roomsLabel, bathroomsLabel;

    public void setImovel(Imovel t) {

        nameLabel.setText(t.getNome());
        descriptionLabel.setText(t.getDescricao());
        setPriceLabel(t.getLanceAtual());


        typeLabel.setText("Imóvel " + t.getTipo().name());

        areaLabel.setText(String.format("%f m²", t.getArea()));
        bathroomsLabel.setText(String.format("%d banheiros", t.getNumeroBanheiros()));

        try {
            roomsLabel.setText(String.format("%d quartos", t.getNumeroQuartos()));
        } catch (Exception e) {
            roomsLabel.setText("");
        }
    }

    public void setLote(Lote t) {

        nameLabel.setText(t.getNome());
        descriptionLabel.setText(t.getDescricao());
        setPriceLabel(t.getLanceAtual());

        typeLabel.setText("Lote");
        areaLabel.setText("");
        roomsLabel.setText("");
        bathroomsLabel.setText("");

    }

    private void setPriceLabel(BigDecimal price) {
        priceLabel.setText(NumberFormat.getCurrencyInstance().format(price.doubleValue()));
    }
}
