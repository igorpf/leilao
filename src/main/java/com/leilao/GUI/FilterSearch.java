/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.GUI;

import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

/**
 *
 * @author filipe
 */


public class FilterSearch {
    @FXML private TextField filtertxt;
    @FXML private ChoiceBox filterchoicebox;
    
    @FXML private HBox tipohbox;
    
    //Tipos Radio Buttons
    @FXML private ChoiceBox tipochoicebox;
    
    //Ordenamento hbox
    @FXML private HBox orderhbox;
    @FXML private CheckBox ordercheckbox;
    @FXML private ChoiceBox orderchoicebox;
    
    @FXML private Button filterbtn;
   
    
    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;
    
    @FXML
    private void initialize() {
        tipohbox.setVisible(false);
        orderhbox.setVisible(true);
        ObservableList<String> options = FXCollections.observableArrayList("Lotes","Imoveis");
        
        
        filterchoicebox.setTooltip(new Tooltip("Selecione o tipo de lote"));
       
        filterchoicebox.setValue("Lotes");
        filterchoicebox.setItems(options);
        
        ObservableList<String> options_imovel = FXCollections.observableArrayList("Residencial","Comercial");
        tipochoicebox.setValue("Residencial");
        tipochoicebox.setItems(options_imovel);
        
       
        
        filterchoicebox.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
                public void changed(ObservableValue ov, Number value, Number new_value){
                    if(new_value.intValue() == 1){
                        tipohbox.setVisible(true); 
                        ObservableList<String> options_choice = FXCollections.observableArrayList("Valor mínimo","Número de quartos", "Número de banheiros");
                        orderchoicebox.setItems(options_choice);
                        
                        tipochoicebox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
                           public void changed(ObservableValue ov, Number value1, Number new_value1){
                               if(new_value1.intValue() == 0){                               
                                   ObservableList<String> options_choice = FXCollections.observableArrayList("Valor mínimo","Número de quartos", "Número de banheiros");
                                   orderchoicebox.setItems(options_choice);
                               }
                               else{
                                   ObservableList<String> options_choice = FXCollections.observableArrayList("Valor mínimo","Área");
                                   orderchoicebox.setItems(options_choice);
                               }
                               
                           }                          
                        });
                        
                    }
                    else{
                        tipohbox.setVisible(false);
                        ObservableList<String> options_choice = FXCollections.observableArrayList("Valor mínimo");
                        orderchoicebox.setItems(options_choice);
                        
                    }
                        
                    
                }
            });
        
    }
    
    @FXML
    private void filtro(){
        if(filterchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 0){
            if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 0){
                // devolve lista de lotes ordenada por valor mínimo
                // Passar para Lista Main Window Lote findAllByOrderByValorMinimoAsc()
                
            }
            else{
                //devolve lista de lotes
                //Passar para Lista MAIN WINDOW Lote findAll()
                
            }
        }
        else{
            if(tipochoicebox.getSelectionModel().selectedIndexProperty().intValue() == 0){
                if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 0){
                    //devolve lista de residencias ordenadas por valor mínimo
                    
                }
                else if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 1){
                    //devolve lista de residencias ordenadas por numero de quartos
                }
                 else if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 2){
                    //devolve lista de residencias ordenadas por numero de banheiros
                }
                 else{
                     // devolve lista apenas com imoveis residenciais
                     // Passar para lista main window Imovel getResidenciais()
                 }
                    
            }
            else {
                if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 0){
                    //devolve lista de imoveis comerciais ordenadas por valor mínimo
                    
                }
                else if(orderchoicebox.getSelectionModel().selectedIndexProperty().intValue() == 1){
                    //devolve lista de imoveis comerciais ordenadas por tamanho de área
                }
                else{
                    //devolve lista de imoveis comerciais
                    
                }
                
            }
        }
        
    }
    
}
