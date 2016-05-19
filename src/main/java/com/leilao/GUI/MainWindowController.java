package com.leilao.GUI;

import com.leilao.entidades.Lote;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 * Created by Arthur on 19/05/2016.
 */
public class MainWindowController {

    @FXML private HBox loggedButtonBar, unloggedButtonBar;

    @FXML private ListView<Lote> loteListView;

    @FXML
    private void initialize() {

        loggedButtonBar.visibleProperty().bind(unloggedButtonBar.visibleProperty().not());

        loteListView.setCellFactory(t -> new LoteCell());

    }

}
