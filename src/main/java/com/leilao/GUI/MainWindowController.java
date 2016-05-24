package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Lote;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
        loteListView.getItems().setAll(servicoLote.findAll());
        loteListView.getItems().addAll(servicoImovel.findAll());
    }

    @FXML
    private void logarUsuario() {

        String username = usernameField.getText();
        String password = passwordField.getText();

    }

    @FXML
    private void abrirCadastroUsuario() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Usuário");

        ButtonType loginButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField(usernameField.getText());
        username.setPromptText("Nome de Usuário");
        PasswordField password = new PasswordField();
        password.setPromptText("Senha");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirmar Senha");

        grid.add(new Label("Nome de usuário:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Senha:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Confirmar Senha:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        Supplier<Boolean> shouldEnableRegister = () -> {
            if (username.getText().isEmpty())
                return false;
            else if (password.getText().isEmpty())
                return false;
            else if (!password.getText().equals(confirmPassword.getText()))
                return false;
            else
                return true;
        };

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!shouldEnableRegister.get());
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!shouldEnableRegister.get());
        });

        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!shouldEnableRegister.get());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });

    }

}
