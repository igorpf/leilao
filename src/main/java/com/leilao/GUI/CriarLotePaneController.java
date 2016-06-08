package com.leilao.GUI;

import com.leilao.PersistenceConfig;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.entidades.Usuario;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @FXML private Button terminarButton;

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

        addPermaSelectionFilter(tipoButton.getToggleGroup(), tipo1, tipo2);

        ToggleButton subtipo1 = new ToggleButton("Comercial");
        subtipo1.setUserData(Imovel.Tipo.Comercial);
        ToggleButton subtipo2 = new ToggleButton("Residencial");
        subtipo2.setUserData(Imovel.Tipo.Residencial);
        subtipoButton.getButtons().setAll(subtipo1, subtipo2);

        addPermaSelectionFilter(subtipoButton.getToggleGroup(), subtipo1, subtipo2);

        subtipo2.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());
        tipo1.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());
        tipo2.prefWidthProperty().bindBidirectional(subtipo1.prefWidthProperty());

        subtipo1.setPrefWidth(100);
        subtipoButton.visibleProperty().bind(tipo2.selectedProperty());
        quartosField.visibleProperty().bind(tipo2.selectedProperty().and(subtipo2.selectedProperty()));

        bindVisiblity();

        restrictFields();

        ValidationSupport val = new ValidationSupport();

        val.validationResultProperty().addListener(((observable, oldValue, newValue) -> {
            terminarButton.setDisable(!newValue.getErrors().isEmpty());
        }));

        val.registerValidator(nomeField, Validator.createEmptyValidator("É preciso inserir um nome."));

        val.registerValidator(lanceField, (c, newString) ->
                ValidationResult.fromErrorIf(c, "É preciso informar o lance mínimo",
                        !hasValidFloat((String) newString)));

        val.registerValidator(areaField, (c, newString) ->
                ValidationResult.fromErrorIf(c, "É necessário informar uma área",
                        tipo2.isSelected() && !hasValidFloat((String) newString)));

        val.registerValidator(banheirosField, (c, newString) ->
                ValidationResult.fromErrorIf(c, "É necessário informar a quantidade de banheiros",
                        tipo2.isSelected() && newString.equals("")));

        val.registerValidator(quartosField, (c, newString) ->
                ValidationResult.fromErrorIf(c, "É necessário a quantidade de quartos",
                        tipo2.isSelected() && subtipo2.isSelected() && newString.equals("")));

        tipo2.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            areaField.setText(newValue ? "" : "1");
            banheirosField.setText(newValue ? "" : "1");
        }));

        subtipo2.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            quartosField.setText(newValue ? "" : "1");
        }));

        tipo1.setSelected(true);
        subtipo1.setSelected(true);

        areaField.setText("1");
        banheirosField.setText("1");
        quartosField.setText("1");
    }

    private void addPermaSelectionFilter(ToggleGroup group, ToggleButton... buttons) {
        for (ToggleButton button : buttons)
            button.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    if (button.equals(group.getSelectedToggle())) {
                        mouseEvent.consume();
                    }
                }
            });
    }

    private void bindVisiblity() {

        subtipoButton.managedProperty().bind(subtipoButton.visibleProperty());

        areaField.visibleProperty().bind(subtipoButton.visibleProperty());
        areaField.managedProperty().bind(areaField.visibleProperty());

        areaLabel.visibleProperty().bind(areaField.visibleProperty());
        areaLabel.managedProperty().bind(areaLabel.visibleProperty());

        banheirosField.visibleProperty().bind(subtipoButton.visibleProperty());
        banheirosField.managedProperty().bind(banheirosField.visibleProperty());

        banheirosLabel.visibleProperty().bind(banheirosField.visibleProperty());
        banheirosLabel.managedProperty().bind(banheirosLabel.visibleProperty());

        quartosField.managedProperty().bind(quartosField.visibleProperty());

        quartosLabel.visibleProperty().bind(quartosField.visibleProperty());
        quartosLabel.managedProperty().bind(quartosLabel.visibleProperty());
    }

    private void restrictFields() {

        lanceField.textProperty().addListener((observable, oldValue,  newValue) -> {
            if (!newValue.matches("\\d*[[\\.,]\\d*]?")) {
                String[] parts = newValue.split("[,\\.]");
                String replacement = parts[0].replaceAll("[^\\d]","");
                for (int i = 1; i < parts.length; i++)
                    replacement += (i == 1 ? "." : "") + parts[i].replaceAll("[^\\d]","");;
                ((StringProperty) observable).setValue(replacement);
            }
        });

        banheirosField.textProperty().addListener((observable, oldValue,  newValue) -> {
            if (!newValue.matches("\\d*"))
                ((StringProperty) observable).setValue(newValue.replaceAll("[^\\d]", ""));
        });

        quartosField.textProperty().addListener((observable, oldValue,  newValue) -> {
                if (!newValue.matches("\\d*"))
                ((StringProperty) observable).setValue(newValue.replaceAll("[^\\d]", ""));
        });

        areaField.textProperty().addListener((observable, oldValue,  newValue) -> {
            if (!newValue.matches("\\d*[[\\.,]\\d*]?")) {
                String[] parts = newValue.split("[,\\.]");
                String replacement = parts[0].replaceAll("[^\\d]","");
                for (int i = 1; i < parts.length; i++)
                    replacement += (i == 1 ? "." : "") + parts[i].replaceAll("[^\\d]","");;
                ((StringProperty) observable).setValue(replacement);
            }
        });
    }

    private boolean hasValidFloat(String t) {
        try {
            Float.parseFloat(t);
            return !t.isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
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
            ((Imovel) t).setTipo((Imovel.Tipo) subtipoButton.getToggleGroup().getSelectedToggle().getUserData());
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
