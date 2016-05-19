package com.leilao.GUI;

import com.leilao.entidades.Lote;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Arthur on 19/05/2016.
 */
public class MainWindowController {

    @FXML private HBox loggedButtonBar, unloggedButtonBar;

    @FXML private ListView<Lote> loteListView;

    @Autowired
    private ServicoLote servicoLote;
    @Autowired
    private ServicoImovel servicoImovel;

    @FXML
    private void initialize() {

        loggedButtonBar.visibleProperty().bind(unloggedButtonBar.visibleProperty().not());

        loteListView.getItems().setAll(servicoLote.findAll());
        loteListView.getItems().addAll(servicoImovel.findAll());

        loteListView.setCellFactory(t -> new LoteCell());

    }

}
