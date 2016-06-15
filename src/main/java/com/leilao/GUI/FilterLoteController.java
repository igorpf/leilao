/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.GUI;
import com.leilao.PersistenceConfig;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Lote;
import com.leilao.servicos.ServicoImovel;
import com.leilao.servicos.ServicoLote;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author filipe
 */
public class FilterLoteController {
    private ServicoLote servicoLote;
    private ServicoImovel servicoImovel;
    
     @FXML private ListView<Lote> loteListView;
     @FXML private ChoiceBox<String> filterChoiceBox = new ChoiceBox<String>();
     @FXML private Button filterbutton = new Button();
     @FXML private TextField filtersearchbar = new TextField();
     
    @FXML private RadioButton loterdbtn = new RadioButton();
    @FXML private RadioButton imovelrdbtn = new RadioButton();
    @FXML private RadioButton comercialrdbtn = new RadioButton();
    @FXML private Button filtertypebutton = new Button();
     
     @FXML private HBox nomehbox = new HBox();
     @FXML private HBox tipohbox = new HBox();
     
     String nome;
     @FXML
     public void initialize(){
        loteListView.setItems(FXCollections.observableArrayList());
        loteListView.setCellFactory(t -> new LoteCell());
         
         filterChoiceBox.getItems().add("Nome");
         filterChoiceBox.getItems().add("Tipo");
         
         
         //Default value for dropdown menu
         filterChoiceBox.setValue("Nome");
              
         if(filterChoiceBox.getValue() == "Nome"){
             nomehbox.setVisible(true);
             tipohbox.setVisible(false);
             
                   
         }
         else if(filterChoiceBox.getValue() == "Tipo"){
             nomehbox.setVisible(false);
             tipohbox.setVisible(true);
         }
     
     }
     
     private void carregarLotesNome(){
         try {
            nome = filtersearchbar.getText(); 
            List<Lote> list_lote = servicoLote.findByNome(nome);
            List<Imovel> list_imovel = servicoImovel.findByNome(nome);
            
            if(!list_lote.isEmpty()){
                loteListView.getItems().setAll(list_lote);
            }
            else if(!list_imovel.isEmpty()){
                loteListView.getItems().setAll(list_imovel);
            }
            else{
                loteListView.setPlaceholder(new Label("Não existem lotes com esse nome."));
            }
            
            
         } 
         catch (Exception e) {}
     
     }
     private void carregarLotesTipo(){
         try{
             if(loterdbtn.isSelected()){
                 loteListView.getItems().setAll(servicoLote.findAll());
             }
             else if(imovelrdbtn.isSelected()){
                 loteListView.getItems().setAll(servicoImovel.getResidencial());
             }
             else if(comercialrdbtn.isSelected()){
                 loteListView.getItems().setAll(servicoImovel.getComercial());
             }
             
         }catch (Exception e){}
     }
    
     
}
