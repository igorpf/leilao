package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Arthur on 30/05/2016.
 */
public class CadastroPaneController {

    @FXML private TextField usernameField, nicknameField;
    @FXML private PasswordField passwordField, confirmPasswordField;

    @FXML private Button confirmarButton;
    
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);
    
    private ServicoUsuario servicoUsuario;

    private Consumer<Usuario> onFinish;
    @FXML
    private void initialize() {
        servicoUsuario = applicationContext.getBean(ServicoUsuario.class);

        ValidationSupport val = new ValidationSupport();

        val.validationResultProperty().addListener((o, oldValue, newValue) -> {
            confirmarButton.setDisable(!newValue.getErrors().isEmpty());
        });

        val.registerValidator(usernameField, (Control c, String newValue) -> {
            if (newValue.isEmpty())
                return ValidationResult.fromError(c, "É preciso especificar um nome de usuário");
            else
                return ValidationResult.fromErrorIf(c, "Usuário já existe", servicoUsuario.get(newValue) != null);
        });

        val.registerValidator(passwordField, Validator.createEmptyValidator("É preciso especificar uma senha"));
        val.registerValidator(confirmPasswordField, (Control c, String newValue) -> {
            if (newValue.isEmpty())
                return ValidationResult.fromError(c, "É preciso confirmar a senha");
            else
                return ValidationResult.fromErrorIf(c, "Senhas não combinam", !newValue.equals(passwordField.getText()));
        });
    }

    public void setStartingUsername(String username) {
        usernameField.setText(username);
    }

    public void setOnFinish(Consumer<Usuario> function) {
        onFinish = function;
    }

    @FXML
    private void fazerCadastro() {
        Usuario u = new Usuario();
        u.setNome(usernameField.getText());
        u.setSenha(passwordField.getText());
        if (!nicknameField.getText().isEmpty());
            //u.setApelido(nicknameField.getText());
        else;
        //u.setApelido(usernameField.getText());

        servicoUsuario.save(u);

        if (onFinish != null)
            onFinish.accept(u);
    }

    @FXML
    private void cancelarCadastro() {
        if (onFinish != null)
            onFinish.accept(null);
    }

}
