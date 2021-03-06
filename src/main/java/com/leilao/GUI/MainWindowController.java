package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Funcionario;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoFuncionario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import com.leilao.servicos.ServicoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.math.BigDecimal;
import javafx.scene.Parent;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.StringProperty;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

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

    @FXML private Label nomeLabel, tipoLabel, precoLabel, lanceMinimoLabel;
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

    private PopOver filterPopOver;
    
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
    private ServicoFuncionario servicoFuncionario;

    private PerfilPaneController perfilController;

    private Usuario usuarioLogado;
    private Lote loteSelecionado;
    
    //Filtros
    @FXML private AnchorPane filteranchorpane;
    @FXML private Button filterBtn;
    private boolean filteropen = false;

    @FXML
    private void initialize() {
        servicoLote = applicationContext.getBean(ServicoLote.class);
        servicoImovel = applicationContext.getBean(ServicoImovel.class);
        servicoUsuario = applicationContext.getBean(ServicoUsuario.class);
        servicoFuncionario = applicationContext.getBean(ServicoFuncionario.class);
        loteListView.setPlaceholder(new Label("Não existem lotes à venda"));

        loteListView.setItems(FXCollections.observableArrayList());
        loteListView.setCellFactory(t -> new LoteCell());

        loteListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            updateMasterView(newValue);
            lanceButton.setDisable(loteSelecionado == null);
        }));
        
        lanceButton.setDisable(true);

        lanceField.textProperty().addListener((observable, oldValue,  newValue) -> {
            if (!newValue.matches("\\d*[[\\.,]\\d*]?")) {
                String[] parts = newValue.split("[,\\.]");
                String replacement = parts[0].replaceAll("[^\\d]","");
                for (int i = 1; i < parts.length; i++)
                    replacement += (i == 1 ? "." : "") + parts[i].replaceAll("[^\\d]","");;
                ((StringProperty) observable).setValue(replacement);
            }
        });

        descricaoText.wrappingWidthProperty().bind(masterViewPane.widthProperty().subtract(45));

        initializePopOver();
        initializeFilterPopOver();
        usernameField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        passwordField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));
        lanceField.textProperty().addListener(((observable, oldValue, newValue) -> errorDisplay.hide()));

        tabPane.getTabs().removeAll(aprovarLotesTab, promoverUsuariosTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> carregarLotes()));

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
                else if (user.getApelido().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
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
    private void initializeFilterPopOver(){
        filterPopOver = new PopOver();
        filterPopOver.setDetachable(true);
        filterPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FilterSearch.fxml"));
        try{
            filterPopOver.setContentNode((Node)loader.load());
        }
        catch(IOException ex){System.out.println(ex);}
        
    }
    @FXML
    private void openFilterPopOver(){
        filterPopOver.show(filterBtn);
    }

    @FXML
    private void carregarLotes() {
        try {
            atualizarLotes();
            loteListView.getItems().setAll(servicoLote.getValidos());
            lotesPendentesListView.getItems().setAll(servicoLote.getNaoAprovados());
            usuariosList.setAll(servicoUsuario.findAllUsers());
        } catch (Exception e) {}
    }
    
    @FXML
    private void darLance() {
        if (usuarioLogado == null) {
            tabPane.getSelectionModel().select(perfilTab);
            loginMessageLabel.setText("É preciso estar logado para dar lances.");
            return;
        }

        initializePopOver();
        if (usuarioLogado.getId().equals(loteSelecionado.getVendedor().getId())) {
            errorLabel.setText("Você não pode dar lances no próprio lote");
            errorDisplay.show(lanceButton);
        } else if (loteSelecionado.getComprador() != null &&
                   usuarioLogado.getId().equals(loteSelecionado.getComprador().getId())) {
            errorLabel.setText("Você já tem o maior lance nesse lote");
            errorDisplay.show(lanceButton);
        } else if (lanceField.getText().isEmpty()) {
            errorLabel.setText("É necessário informar um lance");
            errorDisplay.show(lanceButton);
        } else if (loteSelecionado.getLanceMinimo().compareTo(new BigDecimal(lanceField.getText())) > 0) {
            errorLabel.setText("Lance deve ser maior que lance mínimo");
            errorDisplay.show(lanceButton);
        } else if (usuarioLogado.getSaldo().compareTo(new BigDecimal(lanceField.getText())) < 0) {
            errorLabel.setText("Saldo insuficiente para dar lance");
            errorDisplay.show(lanceButton);
        } else {
            try {
                long timeLeft = loteSelecionado.getTimeLeft();
                Long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft);
                if(minutes < 5) {
                    Calendar dataFinal = Calendar.getInstance();
                    dataFinal.add(Calendar.MINUTE, 5);
                    loteSelecionado.setDataFinal(dataFinal);
                }

                Usuario compradorAnterior = loteSelecionado.getComprador();
                if(compradorAnterior != null) {
                    compradorAnterior.addSaldo(loteSelecionado.getLanceAtual());
                    servicoUsuario.save(compradorAnterior);
                }
                loteSelecionado.setComprador(usuarioLogado);
                loteSelecionado.setLanceAtual(new BigDecimal(lanceField.getText()));
                usuarioLogado.subtractSaldo(new BigDecimal(lanceField.getText()));
                usuarioLogado.addCompra(loteSelecionado);
                
                servicoUsuario.save(usuarioLogado);
                servicoLote.save(loteSelecionado);

                perfilController.setUsuario(usuarioLogado);
            } catch (Exception e) {}
        }
        
        carregarLotes();
    }
    
    private void atualizarLotes() {
        try {
            List<Lote> lotes = servicoLote.findAll();
            for(Lote l : lotes) {
                long timeLeft = l.getTimeLeft();
                Usuario comprador = l.getComprador();

                if(timeLeft <= 0 && comprador != null) {
                    Usuario vendedor = l.getVendedor();
                    vendedor.addSaldo(l.getLanceAtual());

                    l.setVendido(true);
                    l.setFinalizado(true);
                    servicoLote.save(l);
                    servicoUsuario.save(vendedor);
                } else if (timeLeft <= 0) {
                    l.setFinalizado(true);
                    servicoLote.save(l);
                }
            }
            
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

        for (Usuario u : promoverUsuariosListView.getSelectionModel().getSelectedItems()) {
            Funcionario f = new Funcionario(u);
            servicoUsuario.delete(u.getId());
            servicoFuncionario.save(f);
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

            if (usuarioLogado instanceof Funcionario)
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
            perfilController = loader.getController();
            perfilPane.getChildren().setAll(n);

            perfilController.setUsuario(userDB);
            perfilController.setOnLogout(() -> {
                usuarioLogado = null;
                perfilPane.getChildren().setAll(loginPane);
                tabPane.getTabs().removeAll(aprovarLotesTab, promoverUsuariosTab);
            });
        }

    }

    private void updateMasterView(Lote t) {
        if (t == null) {
            nomeLabel.setText("Nome");
            tipoLabel.setText("Tipo");
            descricaoText.setText("Descrição");
            precoLabel.setText("Preço");
            lanceMinimoLabel.setText("Lance mínimo");
            return;
        }
        
        loteSelecionado = t;
        
        nomeLabel.setText(t.getNome());

        if (t instanceof Imovel)
            tipoLabel.setText("Imóvel " + ((Imovel) t).getTipo().name());
        else
            tipoLabel.setText("Lote");

        descricaoText.setText(t.getDescricao());
        precoLabel.setText(NumberFormat.getCurrencyInstance().format(t.getLanceAtual().doubleValue()));
        lanceMinimoLabel.setText("Lance mínimo: " + NumberFormat.getCurrencyInstance().format(t.getLanceMinimo().doubleValue()));
    }
    
    
    
    

}
