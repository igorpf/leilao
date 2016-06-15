package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.Consumer;

/**
 * Created by Arthur on 03/06/2016.
 */
public class EditarPerfilPaneController {

    @FXML private TextField nicknameField;
    @FXML private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;

    private PopOver errorDisplay;
    private Label errorLabel;

    private Consumer<Usuario> onFinish;
    private Usuario user;

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoUsuario servicoUsuario;

    @FXML
    private void initialize() {
        servicoUsuario = applicationContext.getBean(ServicoUsuario.class);

        nicknameField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        oldPasswordField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        newPasswordField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        confirmPasswordField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));

        initializePopOver();
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

    private boolean isModifyingPassword() {
        return (!oldPasswordField.getText().isEmpty() ||
                !newPasswordField.getText().isEmpty() ||
                !confirmPasswordField.getText().isEmpty());
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public void setOnFinish(Consumer<Usuario> function) {
        onFinish = function;
    }

    @FXML
    private void salvarMudancas(ActionEvent sender) {
        if (!nicknameField.getText().isEmpty());
            user.setApelido(nicknameField.getText());

        if (oldPasswordField.getText().equals(user.getSenha())
            && !newPasswordField.getText().isEmpty()
            && newPasswordField.getText().equals(confirmPasswordField.getText()))
            user.setSenha(newPasswordField.getText());
        else if (isModifyingPassword()){

            if (!oldPasswordField.getText().equals(user.getSenha())) {
                oldPasswordField.setText("");
                oldPasswordField.requestFocus();
                errorLabel.setText("Senha incorreta");
                errorDisplay.show(oldPasswordField);
            } else if (newPasswordField.getText().isEmpty()) {
                newPasswordField.requestFocus();
                errorLabel.setText("É preciso informar uma senha nova");
                errorDisplay.show(newPasswordField);
            } else {
                confirmPasswordField.setText("");
                confirmPasswordField.requestFocus();
                errorLabel.setText("Senhas não combinam");
                errorDisplay.show(confirmPasswordField);
            }
            return;
        }

        servicoUsuario.save(user);
        if (onFinish != null)
            onFinish.accept(user);
    }

    @FXML
    private void cancelar() {
        if (onFinish != null)
            onFinish.accept(user);
    }

    @FXML
    private void sair() {
        if (onFinish != null)
            onFinish.accept(user);
    }

}
