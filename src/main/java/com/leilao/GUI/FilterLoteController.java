/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.GUI;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.text.NumberFormat;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author filipe
 */
public class FilterLoteController {
    
     @FXML private ListView<Lote> loteListView;
     @FXML private ChoiceBox<String> filterChoiceBox = new ChoiceBox<String>();
     @FXML private Button filterbutton = new Button();
     @FXML private TextField filtersearchbar = new TextField();
     
     @FXML
     public void initialize(){
         filterChoiceBox.getItems().add("Nome");
         filterChoiceBox.getItems().add("Tipo");
         filterChoiceBox.getItems().add("Valor");
         
         //Default value for dropdown menu
         filterChoiceBox.setValue("Nome");
         
         if(filterChoiceBox.getValue() == "Nome"){
             filterbutton.setVisible(true);
             filtersearchbar.setVisible(true);
             
         }
         else if(filterChoiceBox.getValue() == "Tipo"){
             
         }
     
     }
    
     
}
