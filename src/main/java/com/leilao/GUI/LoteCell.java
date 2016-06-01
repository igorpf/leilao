package com.leilao.GUI;

import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by Arthur on 19/05/2016.
 */
public class LoteCell extends ListCell<Lote> {

    private Node n;
    private LoteCellController controller;
    {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoteCell.fxml"));

        try {
            n = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.<LoteCellController>getController();

        setText(null);
        setBorder(null);
        setPadding(Insets.EMPTY);

    }

    @Override
    public void updateItem(Lote item, boolean empty) {
        super.updateItem(item, empty);
        if (empty)
            setGraphic(null);
        else {
            setGraphic(n);

            if (item instanceof Imovel)
                controller.setImovel((Imovel) item);
            else
                controller.setLote(item);
        }

        controller.prefWidthProperty().bind(getListView().widthProperty().subtract(20));
    }

}