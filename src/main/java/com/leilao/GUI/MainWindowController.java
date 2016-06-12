package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Funcionario;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import com.leilao.servicos.ServicoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.PopOver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import sun.plugin.javascript.navig.Anchor;

/**
 * Created by Arthur on 19/05/2016.
 */
@Controller
public class MainWindowController {


    @FXML private TabPane tabPane;
    @FXML private Tab mainTab, perfilTab, aprovarLotesTab, promoverUsuariosTab;

    // Leilões disponiveis
    @FXML private AnchorPane mainPane;
    @FXML private Pane masterViewPane;
    @FXML private SplitPane listViewPane;
    @FXML private ListView<Lote> loteListView;

    @FXML private Label nomeLabel, tipoLabel, precoLabel;
    @FXML private Text descricaoText;
    @FXML private TextField lanceField;
    @FXML private Button lanceButton;

    // Login
    @FXML private AnchorPane perfilPane;
    @FXML private Pane loginPane;

    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private PopOver errorDisplay;
    private Label errorLabel;

    // Aprovar lotes
    @FXML private Button aprovarLotesPendentesButton;
    @FXML private ListView<Lote> lotesPendentesListView;

    // Promover usuários
    @FXML private Button promoverUsuariosButton;
    @FXML private ListView<Usuario> promoverUsuariosListView;
    @FXML private TextField filtrarUsuariosField;
    private ObservableList<Usuario> usuariosList;
    private FilteredList<Usuario> filteredUsuariosList;

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;
    private ServicoUsuario servicoUsuario;

    private Usuario usuarioLogado;

    @FXML
    private void initialize() {
        servicoLote = applicationContext.getBean(ServicoLote.class);
        servicoImovel = applicationContext.getBean(ServicoImovel.class);
        servicoUsuario = applicationContext.getBean(ServicoUsuario.class);
        loteListView.setPlaceholder(new Label("Não existem lotes à venda"));

        loteListView.setItems(FXCollections.observableArrayList());
        loteListView.setCellFactory(t -> new LoteCell());

        loteListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            updateMasterView(newValue);
        }));

        descricaoText.wrappingWidthProperty().bind(masterViewPane.widthProperty().subtract(45));

        initializePopOver();
        usernameField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        passwordField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));

        tabPane.getTabs().removeAll(aprovarLotesTab, promoverUsuariosTab);

        lotesPendentesListView.setPlaceholder(new Label("Não existem lotes à espera de aprovação"));

        lotesPendentesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lotesPendentesListView.setCellFactory(t -> new LoteCell());
        lotesPendentesListView.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends Lote> c) -> {
                    aprovarLotesPendentesButton.setDisable(c.getList().isEmpty());
        });

        usuariosList = FXCollections.observableArrayList();
        filteredUsuariosList = new FilteredList<Usuario>(usuariosList, u -> true);
        promoverUsuariosListView.setItems(filteredUsuariosList);

        promoverUsuariosListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        promoverUsuariosListView.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends Usuario> c) -> {
                    promoverUsuariosButton.setDisable(c.getList().isEmpty());
                });

        filtrarUsuariosField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredUsuariosList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

                if (user.getNome().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
//                else if (user.getApelido().toLowerCase().contains(newValue.toLowerCase()))
//                    return true;
                else
                    return false;
            });
        }));

        carregarLotes();
    }

    private void initializePopOver() {
        errorDisplay = new PopOver();
        errorDisplay.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        errorDisplay.setDetachable(false);
        errorDisplay.setAutoHide(true);

        errorLabel = new Label();
        errorLabel.setPrefHeight(20);
        errorLabel.setPadding(new Insets(0,10,0,10));
        errorLabel.setTextFill(Color.RED);

        errorDisplay.setContentNode(errorLabel);
    }

    @FXML
    private void carregarLotes() {
        try {
            loteListView.getItems().setAll(servicoLote.getAprovados());
            lotesPendentesListView.getItems().setAll(servicoLote.getNaoAprovados());
            usuariosList.setAll(servicoUsuario.findAll());
        } catch (Exception e) {}
    }

    @FXML
    private void logarUsuario() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        Usuario u = new Usuario();
        u.setNome(username);
        u.setSenha(password);
        
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
        CadastroPaneController controller = loader.getController();
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

    @FXML
    private void criarNovoLote() {

        if (usuarioLogado == null) {
            tabPane.getSelectionModel().select(perfilTab);
            loginMessageLabel.setText("É preciso estar logado para criar novos lotes.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CriarLotePane.fxml"));

        Node n = null;
        try {
            n = loader.load();

            AnchorPane.setTopAnchor(n, 0.0);
            AnchorPane.setBottomAnchor(n, 0.0);
            AnchorPane.setLeftAnchor(n, 0.0);
            AnchorPane.setRightAnchor(n, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CriarLotePaneController controller = loader.getController();
        mainPane.getChildren().setAll(n);

        controller.setOwner(usuarioLogado);
        controller.setOnFinish(t -> {
            carregarLotes();
            mainPane.getChildren().setAll(listViewPane);
        });

    }

    @FXML
    private void aprovarLotesPendentes() {

        for (Lote t : lotesPendentesListView.getSelectionModel().getSelectedItems()) {
            t.setAprovado(true);
            servicoLote.save(t);
        }

        carregarLotes();
    }

    @FXML
    private void promoverUsuarios() {

        for (Usuario t : promoverUsuariosListView.getSelectionModel().getSelectedItems()) {
            Funcionario f = new Funcionario();
        }

        carregarLotes();
    }

    private void fazerLogin(Usuario user) {
        Usuario userDB = servicoUsuario.get(user.getNome());

        if (userDB == null) {
            usernameField.setText("");
            usernameField.requestFocus();

            errorLabel.setText("Esse usuário não existe");
            errorDisplay.show(usernameField);
        } else if (!user.getSenha().equals(userDB.getSenha())) {
            passwordField.setText("");
            passwordField.requestFocus();

            errorLabel.setText("Senha incorreta");
            errorDisplay.show(passwordField);
        } else {
            usernameField.setText("");
            passwordField.setText("");
            loginMessageLabel.setText("");

            usuarioLogado = userDB;

            if (usuarioLogado instanceof Usuario)
                tabPane.getTabs().addAll(aprovarLotesTab, promoverUsuariosTab);

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
            PerfilPaneController controller = loader.getController();
            perfilPane.getChildren().setAll(n);

            controller.setUsuario(userDB);
            controller.setOnLogout(() -> {
                usuarioLogado = null;
                perfilPane.getChildren().setAll(loginPane);
                tabPane.getTabs().removeAll(aprovarLotesTab, promoverUsuariosTab);
            });
        }

    }

    private void updateMasterView(Lote t) {
        if (t == null)
            return;

        nomeLabel.setText(t.getNome());

        if (t instanceof Imovel)
            tipoLabel.setText("Imóvel " + ((Imovel) t).getTipo().name());
        else
            tipoLabel.setText("Lote");

        descricaoText.setText(t.getDescricao());

    }

}
