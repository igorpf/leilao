package com.leilao.GUI;

import com.leilao.entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

/**
 * Created by Arthur on 30/05/2016.
 */
public class CadastroPaneController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField, confirmPasswordField;

    @FXML private Button confirmarButton;

    private Consumer<Usuario> onFinish;
    @FXML
    private void initialize() {

        usernameField.textProperty().addListener(((observable, oldValue, newValue) -> updateButtons()));
        passwordField.textProperty().addListener(((observable, oldValue, newValue) -> updateButtons()));
        confirmPasswordField.textProperty().addListener(((observable, oldValue, newValue) -> updateButtons()));

    }

    private void updateButtons() {
        if (usernameField.getText().isEmpty())
            confirmarButton.setDisable(true);
        else if (passwordField.getText().isEmpty())
            confirmarButton.setDisable(true);
        else if (!passwordField.getText().equals(confirmPasswordField.getText()))
            confirmarButton.setDisable(true);
        else
            confirmarButton.setDisable(false);
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

        if (onFinish != null)
            onFinish.accept(u);
    }

    @FXML
    private void cancelarCadastro() {
        if (onFinish != null)
            onFinish.accept(null);
    }

}
