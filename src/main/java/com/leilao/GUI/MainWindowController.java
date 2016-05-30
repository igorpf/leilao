package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

/**
 * Created by Arthur on 19/05/2016.
 */
@Controller
public class MainWindowController {

    @FXML private ListView<Lote> loteListView;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private AnchorPane perfilPane;
    @FXML private Pane loginPane;
    
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;

    @FXML
    private void initialize() {
        servicoLote = applicationContext.getBean(ServicoLote.class);
        servicoImovel = applicationContext.getBean(ServicoImovel.class);
        loteListView.setPlaceholder(new Label("Não existem lotes à venda"));

        loteListView.setItems(FXCollections.observableArrayList());
        loteListView.setCellFactory(t -> new LoteCell());
    }

    @FXML
    private void carregarLotes() {
//        loteListView.getItems().setAll(servicoLote.findAll());
//        loteListView.getItems().addAll(servicoImovel.findAll());
        Lote t = new Lote();
        t.setNome("Lote 1");

        loteListView.getItems().add(t);
    }

    @FXML
    private void logarUsuario() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        usernameField.setText("");
        passwordField.setText("");

        Usuario u = new Usuario();
        u.setNome(username);
        fazerLogin(u);
    }

    @FXML
    private void abrirCadastroUsuario() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CadastroPane.fxml"));

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
        CadastroPaneController controller = loader.<CadastroPaneController>getController();
        perfilPane.getChildren().setAll(n);

        controller.setStartingUsername(usernameField.getText());
        usernameField.setText("");
        passwordField.setText("");

        controller.setOnFinish((user) -> {
            if (user == null)
                perfilPane.getChildren().setAll(loginPane);
            else
                fazerLogin(user);
        });

    }

    private void fazerLogin(Usuario user) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PerfilPane.fxml"));

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
        PerfilPaneController controller = loader.<PerfilPaneController>getController();
        perfilPane.getChildren().setAll(n);

        controller.setUsuario(user);
        controller.setOnLogout(() -> perfilPane.getChildren().setAll(loginPane));


    }

}
