package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoLote;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Arthur on 30/05/2016.
 */
public class PerfilPaneController {

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoLote servicoLote;

    @FXML private Pane root, listsRoot;

    @FXML private ListView<Lote> comprasListView, vendasListView;
    @FXML private Label nomeLabel, saldoLabel;
    @FXML private Button cancelarButton;

    private Runnable onLogout;
    private Usuario user;

    @FXML
    private void initialize() {
        servicoLote = applicationContext.getBean(ServicoLote.class);

        comprasListView.setItems(FXCollections.observableArrayList());
        vendasListView.setItems(FXCollections.observableArrayList());
        comprasListView.setCellFactory(t -> new LoteCell());
        vendasListView.setCellFactory(t -> new LoteCell());

        comprasListView.prefHeightProperty().bindBidirectional(vendasListView.prefHeightProperty());
        comprasListView.prefHeightProperty().bind(listsRoot.heightProperty().subtract(70).divide(2));

        vendasListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        vendasListView.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends Lote> c) -> {
                    for(Lote l : c.getList()) {
                        if(l.isAprovado()) {
                            cancelarButton.setDisable(true);
                            break;
                        } else {
                            cancelarButton.setDisable(c.getList().isEmpty());
                        }
                    }
        });

        cancelarButton.setDisable(true);
    }

    public void setUsuario(Usuario usuario) {
        user = usuario;
        nomeLabel.setText(usuario.getNome());
        saldoLabel.setText("Saldo: " + NumberFormat.getCurrencyInstance().format(usuario.getSaldo()));
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
    private void cancelarLotes() {
        for (Lote t : vendasListView.getSelectionModel().getSelectedItems()) {
            servicoLote.delete(t.getId());
            user.deleteVenda(t);
        }
        atualizarVendas();
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
