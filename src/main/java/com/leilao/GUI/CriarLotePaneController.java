package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Created by Arthur on 07/06/2016.
 */
public class CriarLotePaneController {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox vBox;

    @FXML private SegmentedButton tipoButton, subtipoButton;

    @FXML private TextField nomeField, lanceField;
    @FXML private TextArea descricaoArea;

    @FXML private Label areaLabel, banheirosLabel, quartosLabel;
    @FXML private TextField areaField, banheirosField, quartosField;

    private Usuario owner;
    private Consumer<Lote> onFinish;

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;
    @FXML
    private void initialize() {
        servicoLote = applicationContext.getBean(ServicoLote.class);
        servicoImovel = applicationContext.getBean(ServicoImovel.class);

        vBox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(7));

        ToggleButton tipo1 = new ToggleButton("Lote");
        tipo1.setUserData(Lote.class);
        ToggleButton tipo2 = new ToggleButton("Imóvel");
        tipo2.setUserData(Imovel.class);
        tipoButton.getButtons().setAll(tipo1, tipo2);

        ToggleButton subtipo1 = new ToggleButton("Residencial");
        subtipo1.setUserData(Imovel.Tipo.Residencial);
        ToggleButton subtipo2 = new ToggleButton("Comercial");
        subtipo2.setUserData(Imovel.Tipo.Comercial);
        subtipoButton.getButtons().setAll(subtipo1, subtipo2);

        subtipo2.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());
        tipo1.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());
        tipo2.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());

        subtipo1.setPrefWidth(100);

        subtipoButton.visibleProperty().bind(tipo2.selectedProperty());
        subtipoButton.managedProperty().bind(subtipoButton.visibleProperty());

        areaField.visibleProperty().bind(subtipoButton.visibleProperty());
        areaField.managedProperty().bind(areaField.visibleProperty());

        areaLabel.visibleProperty().bind(areaField.visibleProperty());
        areaLabel.managedProperty().bind(areaLabel.visibleProperty());

        banheirosField.visibleProperty().bind(subtipoButton.visibleProperty());
        banheirosField.managedProperty().bind(banheirosField.visibleProperty());

        banheirosLabel.visibleProperty().bind(banheirosField.visibleProperty());
        banheirosLabel.managedProperty().bind(banheirosLabel.visibleProperty());

        quartosField.visibleProperty().bind(subtipoButton.visibleProperty().and(subtipo1.selectedProperty()));
        quartosField.managedProperty().bind(quartosField.visibleProperty());

        quartosLabel.visibleProperty().bind(quartosField.visibleProperty());
        quartosLabel.managedProperty().bind(quartosLabel.visibleProperty());
    }

    public void setOwner(Usuario user) {
        this.owner = user;
    }

    public void setOnFinish(Consumer<Lote> onFinish) {
        this.onFinish = onFinish;
    }

    @FXML
    private void terminarCriacao() {

        boolean isImovel = tipoButton.getToggleGroup().getSelectedToggle().getUserData().equals(Imovel.class);

        Lote t;
        if (isImovel) {
            t = new Imovel();
            ((Imovel) t).setArea(Float.valueOf(areaField.getText()));
            ((Imovel) t).setNumeroBanheiros(Integer.valueOf(banheirosField.getText()));
            if (subtipoButton.getToggleGroup().getSelectedToggle().getUserData().equals(Imovel.Tipo.Residencial))
                ((Imovel) t).setNumeroQuartos(Integer.valueOf(quartosField.getText()));
        } else
            t = new Lote();

        t.setNome(nomeField.getText());
        t.setDescricao(descricaoArea.getText());
        t.setVendedor(owner);

        try {
            t.setValorMinimo(new BigDecimal(lanceField.getText()));
        } catch (Exception e) {
            System.out.println(e);
        }

        if (isImovel)
            servicoImovel.save((Imovel) t);
        else
            servicoLote.save(t);

        if (onFinish != null)
            onFinish.accept(t);

    }

    @FXML
    private void cancelar() {
        if (onFinish != null)
            onFinish.accept(null);
    }

}
