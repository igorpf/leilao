package com.leilao.GUI;

import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.Callable;

/**
 * Created by Arthur on 30/05/2016.
 */
public class PerfilPaneController {

    @FXML private Pane root;

    @FXML private ListView<Lote> comprasListView, vendasListView;
    @FXML private Label nomeLabel;

    private Runnable onLogout;

    @FXML
    private void initialize() {

        comprasListView.setItems(FXCollections.observableArrayList());
        vendasListView.setItems(FXCollections.observableArrayList());
        comprasListView.setCellFactory(t -> new LoteCell());
        vendasListView.setCellFactory(t -> new LoteCell());

        comprasListView.prefHeightProperty().bindBidirectional(vendasListView.prefHeightProperty());
        comprasListView.prefHeightProperty().bind(root.heightProperty().subtract(50).divide(2));
    }

    public void setUsuario(Usuario usuario) {

        nomeLabel.setText(usuario.getNome());

    }

    public void setOnLogout(Runnable function) {
        onLogout = function;
    }


    @FXML
    private void logout() {
        if (onLogout != null)
            onLogout.run();
    }

    @FXML
    private void editarPerfil() {

    }


}
