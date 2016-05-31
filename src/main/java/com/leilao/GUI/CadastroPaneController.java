package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Arthur on 30/05/2016.
 */
public class CadastroPaneController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField, confirmPasswordField;

    @FXML private Button confirmarButton;
    
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);
    
    private ServicoUsuario servicoUsuario;

    private Consumer<Usuario> onFinish;
    @FXML
    private void initialize() {
        servicoUsuario = applicationContext.getBean(ServicoUsuario.class);
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
        u.setSenha(passwordField.getText());

        //Se usuário não existe, salvar no banco
        Usuario userDB = servicoUsuario.get(u.getNome());
        if(userDB == null)
        {
            servicoUsuario.save(u);
        }
        else
        {
            u = null;
            //TO DO: mensagem "Nome de usuário já existe"
        }
        
        if (onFinish != null)
            onFinish.accept(u);
    }

    @FXML
    private void cancelarCadastro() {
        if (onFinish != null)
            onFinish.accept(null);
    }

}
