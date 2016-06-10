package com.leilao.GUI;

import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Arthur on 30/05/2016.
 */
public class PerfilPaneController {

    @FXML private Pane root, listsRoot;

    @FXML private ListView<Lote> comprasListView, vendasListView;
    @FXML private Label nomeLabel;

    private Runnable onLogout;
    private Usuario user;

    @FXML
    private void initialize() {

        comprasListView.setItems(FXCollections.observableArrayList());
        vendasListView.setItems(FXCollections.observableArrayList());
        comprasListView.setCellFactory(t -> new LoteCell());
        vendasListView.setCellFactory(t -> new LoteCell());

        comprasListView.prefHeightProperty().bindBidirectional(vendasListView.prefHeightProperty());
        comprasListView.prefHeightProperty().bind(listsRoot.heightProperty().subtract(70).divide(2));
    }

    public void setUsuario(Usuario usuario) {
        user = usuario;
        nomeLabel.setText(usuario.getNome());
        vendasListView.getItems().setAll(usuario.getVendas());
        comprasListView.getItems().setAll(usuario.getCompras());
    }

    public void setOnLogout(Runnable function) {
        onLogout = function;
    }

    @FXML
    private void atualizarVendas() {
        vendasListView.getItems().setAll(user.getVendas());
    }

    @FXML
    private void atualizarCompras() {
        comprasListView.getItems().setAll(user.getCompras());
    }

    @FXML
    private void logout() {
        if (onLogout != null)
            onLogout.run();
    }

    @FXML
    private void editarPerfil() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EditarPerfilPane.fxml"));

        Pane n = null;
        try {
            n = loader.load();

            AnchorPane.setTopAnchor(n, 0.0);
            AnchorPane.setBottomAnchor(n, 0.0);
            AnchorPane.setLeftAnchor(n, 0.0);
            AnchorPane.setRightAnchor(n, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        EditarPerfilPaneController controller = loader.getController();
        List<Node> children = new ArrayList(root.getChildren());
        root.getChildren().setAll(n);

        controller.setUser(user);

        controller.setOnFinish((user) -> {
            root.getChildren().setAll(children);
            setUsuario(user);
        });
    }


}
