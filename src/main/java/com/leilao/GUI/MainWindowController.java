package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import com.leilao.servicos.ServicoUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import javafx.scene.text.Text;
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
    @FXML private Pane loginPane, masterViewPane;

    @FXML private Label nomeLabel, tipoLabel, precoLabel;
    @FXML private Text descricaoText;
    @FXML private TextField lanceField;
    @FXML private Button lanceButton;

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;
    private ServicoUsuario servicoUsuario;

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

        try {
            carregarLotes();
        } catch (Exception e) {}
    }

    @FXML
    private void carregarLotes() throws Exception {
        loteListView.getItems().setAll(servicoLote.findAll());
    }

    @FXML
    private void logarUsuario() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        usernameField.setText("");
        passwordField.setText("");

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

        Usuario userDB = servicoUsuario.get(user.getNome());
        if(userDB != null && user.getSenha().equals(userDB.getSenha()))
        {

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
        else
        {
            //TO DO: mensagem "Nome de usuário ou senha incorreto"
        }

    }

    private void updateMasterView(Lote t) {

        nomeLabel.setText(t.getNome());

        if (t instanceof Imovel)
            tipoLabel.setText("Imóvel " + ((Imovel) t).getTipo().name());
        else
            tipoLabel.setText("Lote");

        descricaoText.setText(t.getDescricao());

    }

}
