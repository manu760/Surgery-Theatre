import java.io.FileInputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ApplicationRunner extends Application {

    public ClockThread clockThread = null;
    public ElapsedThread elapsedThread = null;
    public Text dayTime;
    public Text elapseTime;
    private int m_temperature = 200;
    private int m_humidity = 40;
    private int m_pressure = 70;
    private int color1 = 0;
    private int color2 = 0;
    private int color3 = 0;
    private String[] colors = {"333300","666600","979700","CCCC00","FFFF00","FFFF99","FFFFCC"};
    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Inside init() method! Perform necessary initializations here.");
    }
    private void initDayTime(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Day Time Clock ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        dayTime = new Text();
        dayTime.setText("13:50:55");
        
        dayTime.setFont(Font.font("Arial",100));
        dayTime.setFill(Color.GREEN);
        clockThread = new ClockThread(dayTime);
        clockThread.start();
        StackPane contentPane = new StackPane();        
        contentPane.getChildren().add(dayTime);        
        contentPane.setStyle("-fx-padding: 0 50 0 50;");
        
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 35 20 35 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(0, pan);
    }
    public void initElapsedTime(GridPane panel) {        
        StackPane pan = new StackPane();
        Label title = new Label(" Elapsed Time ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        elapseTime = new Text();
        elapsedThread = new ElapsedThread(elapseTime);
        elapseTime.setText("00:00:00");
        
        elapseTime.setFont(Font.font("Arial",100));
        elapseTime.setFill(Color.RED);
        
        GridPane contentPane = new GridPane();        
        contentPane.setStyle("-fx-padding: 30 30 30 30;");
        contentPane.addColumn(0, elapseTime);        
        
        GridPane buttonPane = new GridPane();
        buttonPane.setStyle("-fx-padding: 10 10 40 40;");               
        
        Button btnStart = new Button("START");        
        btnStart.setFont(Font.font("Arial",15));
        btnStart.setPrefSize(100, 40);        
        btnStart.setTextFill(Color.WHITE);
        btnStart.setStyle("-fx-background-color: #0000ff;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        btnStart.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (elapsedThread.state == 2){
                    elapsedThread.state = 1;
                    elapsedThread.resume();
                }
                else{
                    elapsedThread.start();
                    elapsedThread.state = 1;
                }                
            }
        });
        
        Button btnStop = new Button("STOP");
        btnStop.setFont(Font.font("Arial",15));
        GridPane.setMargin(btnStop, new Insets(5,0,0,0));
        
        btnStop.setPrefSize(100, 40);        
        btnStop.setTextFill(Color.WHITE);
        btnStop.setStyle("-fx-background-color: #0000ff;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");
        btnStop.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (elapsedThread.state == 1){
                    elapsedThread.suspend();
                    elapsedThread.state = 2;   
                }                
            }
        });
        Button btnReset = new Button("RESET");
        GridPane.setMargin(btnReset, new Insets(5,0,0,0));
        btnReset.setFont(Font.font("Arial",15));
        btnReset.setPrefSize(100, 40);        
        btnReset.setTextFill(Color.WHITE);
        btnReset.setStyle("-fx-background-color: #0000ff;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");
        btnReset.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){                
                elapsedThread.state = 2;
                elapsedThread.time = 0;
                elapsedThread.suspend();
                elapseTime.setText("00:00:00");
            }
        });
        buttonPane.addRow(0, btnStart);
        buttonPane.addRow(1, btnStop);
        buttonPane.addRow(2, btnReset);
        contentPane.addColumn(1, buttonPane);                
        
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 35 20 35 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(1, pan);
    }
    private void initTemperatureControl(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Temperature Control ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        
        GridPane contentPane = new GridPane();        
        GridPane pan1 = new GridPane();
        GridPane pan2 = new GridPane();
        ImageView imageview;
        FileInputStream input = null;
        try{
            input = new FileInputStream("images/thermometer-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image image = new Image(input);
        imageview = new ImageView();
        GridPane.setMargin(imageview, new Insets(0,20,0,0));
        imageview.setImage(image);
        imageview.setFitWidth(50);
        imageview.setFitHeight(65);
        
        Text temperature = new Text();
        GridPane.setMargin(temperature, new Insets(0,20,0,0));
        temperature.setText("20.0");        
        temperature.setFont(Font.font("Arial",70));
        temperature.setFill(Color.RED);       
        
        
        Text symbol = new Text();
        symbol.setText("○C");        
        symbol.setFont(Font.font("Arial",40));
        symbol.setFill(Color.WHITE);       
        symbol.setStyle("-fx-padding: 30 30 30 30;");       
        
        pan1.addColumn(0, imageview);
        pan1.addColumn(1, temperature);
        pan1.addColumn(2, symbol);
        
        contentPane.setStyle("-fx-padding: 30 30 30 30;");
        contentPane.addRow(0, pan1);
        
        
        Button btnUp = new Button();
        GridPane.setMargin(btnUp, new Insets(5,0,0,0));
        btnUp.setPrefSize(50, 50);
        btnUp.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 60;");
        btnUp.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_temperature < 275)
                    m_temperature++;
                temperature.setText("" + (float)m_temperature / 10);
            }
        });
        try{
            input = new FileInputStream("images/up-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageUp = new Image(input);
        ImageView imageviewUp = new ImageView();
        imageviewUp.setImage(imageUp);
        imageviewUp.setFitWidth(50);
        imageviewUp.setFitHeight(50);        
        btnUp.setGraphic(imageviewUp);
        
        Button btnDown = new Button();
        GridPane.setMargin(btnDown, new Insets(5,0,0,0));
        btnDown.setPrefSize(50, 50);
        btnDown.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 90;");
        btnDown.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_temperature > 100)
                    m_temperature--;             
                temperature.setText("" + (float)m_temperature / 10);
            }
        });
        try{
            input = new FileInputStream("images/down-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageDown = new Image(input);
        ImageView imageviewDown = new ImageView();
        imageviewDown.setImage(imageDown);
        imageviewDown.setFitWidth(50);
        imageviewDown.setFitHeight(50);
        btnDown.setGraphic(imageviewDown);
        
        pan2.addColumn(0, btnUp);
        pan2.addColumn(1, btnDown);
        contentPane.addRow(1, pan2);
        
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 20 41 41 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(0, pan);
    }
    private void initHumiddityControl(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Humidity Control ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        
        GridPane contentPane = new GridPane();        
        GridPane pan1 = new GridPane();
        GridPane pan2 = new GridPane();
        ImageView imageview;
        FileInputStream input = null;
        try{
            input = new FileInputStream("images/humidity-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image image = new Image(input);
        imageview = new ImageView();
        GridPane.setMargin(imageview, new Insets(0,20,0,0));
        imageview.setImage(image);
        imageview.setFitWidth(60);
        imageview.setFitHeight(60);
        
        Text humidity = new Text();
        GridPane.setMargin(humidity, new Insets(0,20,0,0));
        humidity.setText(" 40  ");        
        humidity.setFont(Font.font("Arial",70));
        humidity.setFill(Color.RED);       
        
        Text symbol = new Text();
        symbol.setText("%");        
        symbol.setFont(Font.font("Arial",40));
        symbol.setFill(Color.WHITE);       
        symbol.setStyle("-fx-padding: 30 30 30 30;");       
        
        pan1.addColumn(0, imageview);
        pan1.addColumn(1, humidity);
        pan1.addColumn(2, symbol);
        
        contentPane.setStyle("-fx-padding: 30 30 30 30;");
        contentPane.addRow(0, pan1);
        
        
        Button btnUp = new Button();
        GridPane.setMargin(btnUp, new Insets(5,0,0,0));
        btnUp.setPrefSize(50, 50);
        btnUp.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 60;");
        btnUp.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_humidity < 55)
                    m_humidity++;   
                humidity.setText(" " + m_humidity + "  ");
            }
        });
        try{
            input = new FileInputStream("images/up-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageUp = new Image(input);
        ImageView imageviewUp = new ImageView();
        imageviewUp.setImage(imageUp);
        imageviewUp.setFitWidth(50);
        imageviewUp.setFitHeight(50);        
        btnUp.setGraphic(imageviewUp);
        
        Button btnDown = new Button();
        GridPane.setMargin(btnDown, new Insets(5,0,0,0));
        btnDown.setPrefSize(50, 50);
        btnDown.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 100;");
        btnDown.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_humidity > 30)
                    m_humidity--;      
                humidity.setText(" " + m_humidity + "  ");
            }
        });
        try{
            input = new FileInputStream("images/down-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageDown = new Image(input);
        ImageView imageviewDown = new ImageView();
        imageviewDown.setImage(imageDown);
        imageviewDown.setFitWidth(50);
        imageviewDown.setFitHeight(50);
        btnDown.setGraphic(imageviewDown);
        
        pan2.addColumn(0, btnUp);
        pan2.addColumn(1, btnDown);
        contentPane.addRow(1, pan2);
        
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 20 41 41 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(1, pan);
    }
    private void initPressureControl(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Pressure Control ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        
        GridPane contentPane = new GridPane();        
        GridPane pan1 = new GridPane();
        GridPane pan2 = new GridPane();
        ImageView imageview;
        FileInputStream input = null;
        try{
            input = new FileInputStream("images/pressure-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image image = new Image(input);
        imageview = new ImageView();
        GridPane.setMargin(imageview, new Insets(0,20,0,0));
        imageview.setImage(image);
        imageview.setFitWidth(60);
        imageview.setFitHeight(60);
        
        Text pressure = new Text();
        GridPane.setMargin(pressure, new Insets(0,20,0,0));
        pressure.setText(" 70");        
        pressure.setFont(Font.font("Arial",70));
        pressure.setFill(Color.RED);       
        
        Text symbol = new Text();
        symbol.setText("kPa");        
        symbol.setFont(Font.font("Arial",40));
        symbol.setFill(Color.WHITE);       
        symbol.setStyle("-fx-padding: 30 30 30 30;");       
        
        pan1.addColumn(0, imageview);
        pan1.addColumn(1, pressure);
        pan1.addColumn(2, symbol);
        
        contentPane.setStyle("-fx-padding: 30 30 30 30;");
        contentPane.addRow(0, pan1);
        
        
        Button btnUp = new Button();
        GridPane.setMargin(btnUp, new Insets(5,0,0,0));
        btnUp.setPrefSize(50, 50);
        btnUp.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 50;");
        btnUp.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_pressure < 120)
                    m_pressure += 10;  
                if (m_pressure < 100)
                    pressure.setText(" " + m_pressure + "");
                else
                    pressure.setText(m_pressure + "");
            }
        });
        try{
            input = new FileInputStream("images/up-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageUp = new Image(input);
        ImageView imageviewUp = new ImageView();
        imageviewUp.setImage(imageUp);
        imageviewUp.setFitWidth(50);
        imageviewUp.setFitHeight(50);        
        btnUp.setGraphic(imageviewUp);
        
        Button btnDown = new Button();
        GridPane.setMargin(btnDown, new Insets(5,0,0,0));
        btnDown.setPrefSize(50, 50);
        btnDown.setStyle("-fx-padding: 0 0 0 0;"
                + "-fx-border-radius: 4px;"
                + "-fx-background-color: #644123;"
                + "-fx-translate-x: 80;");
        btnDown.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (m_pressure > 50)
                    m_pressure -= 10;             
                if (m_pressure < 100)
                    pressure.setText(" " + m_pressure);
                else
                    pressure.setText(m_pressure + "");
            }
        });
        try{
            input = new FileInputStream("images/down-icon.png");           
            
        }catch(Exception e){
            
        }       
        Image imageDown = new Image(input);
        ImageView imageviewDown = new ImageView();
        imageviewDown.setImage(imageDown);
        imageviewDown.setFitWidth(50);
        imageviewDown.setFitHeight(50);
        btnDown.setGraphic(imageviewDown);
        
        pan2.addColumn(0, btnUp);
        pan2.addColumn(1, btnDown);
        contentPane.addRow(1, pan2);
        
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 20 41 41 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(2, pan);
    }
    private void initLightRow1(GridPane panel){
        GridPane pan1 = new GridPane();
        Button btnMinus1 = new Button("-");
        GridPane.setMargin(btnMinus1, new Insets(5,5,0,0));
        btnMinus1.setFont(Font.font("Arial",20));
        btnMinus1.setPrefSize(50, 20);       
        btnMinus1.setTextFill(Color.WHITE);
        btnMinus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        Rectangle[] rect = new Rectangle[6];
        for(int i = 0;i < 6;i++)
        {
            rect[i] = new Rectangle();
            GridPane.setMargin(rect[i], new Insets(0,5,0,0));
            rect[i].setWidth(50);
            rect[i].setHeight(30);        
            rect[i].setFill(Color.valueOf("333300"));    
            pan1.addColumn(i + 1, rect[i]);        
        }    
        btnMinus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color1 > 0)
                    color1--;
                Light(color1,rect);
            }
        });
                     
        
        Button btnPlus1 = new Button("+");
        btnPlus1.setFont(Font.font("Arial",20));
        btnPlus1.setPrefSize(50, 20);       
        btnPlus1.setTextFill(Color.WHITE);
        btnPlus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        btnPlus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color1 < 6)
                    color1++;
                Light(color1,rect);
            }
        });
        pan1.addColumn(0, btnMinus1);        
        pan1.addColumn(7, btnPlus1);
        
        panel.addRow(0, pan1);
    }
    private void Light(int color,Rectangle[] rect){
        for(int i = 0;i < 6;i++){
            if (i < color)
                rect[i].setFill(Color.valueOf(colors[i + 1]));
            else
                rect[i].setFill(Color.valueOf(colors[0]));
        }
    }
    private void initLightRow2(GridPane panel){
        GridPane pan1 = new GridPane();
        Button btnMinus1 = new Button("-");
        GridPane.setMargin(btnMinus1, new Insets(5,5,0,0));
        btnMinus1.setFont(Font.font("Arial",20));
        btnMinus1.setPrefSize(50, 20);       
        btnMinus1.setTextFill(Color.WHITE);
        btnMinus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        Rectangle[] rect = new Rectangle[6];
        for(int i = 0;i < 6;i++)
        {
            rect[i] = new Rectangle();
            GridPane.setMargin(rect[i], new Insets(0,5,0,0));
            rect[i].setWidth(50);
            rect[i].setHeight(30);        
            rect[i].setFill(Color.valueOf("333300"));    
            pan1.addColumn(i + 1, rect[i]);        
        }    
        btnMinus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color2 > 0)
                    color2--;
                Light(color2,rect);
            }
        });
                     
        
        Button btnPlus1 = new Button("+");
        btnPlus1.setFont(Font.font("Arial",20));
        btnPlus1.setPrefSize(50, 20);       
        btnPlus1.setTextFill(Color.WHITE);
        btnPlus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        btnPlus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color2 < 6)
                    color2++;
                Light(color2,rect);
            }
        });
        pan1.addColumn(0, btnMinus1);        
        pan1.addColumn(7, btnPlus1);
        
        panel.addRow(1, pan1);
    }
    private void initLightRow3(GridPane panel){
        GridPane pan1 = new GridPane();
        Button btnMinus1 = new Button("-");
        GridPane.setMargin(btnMinus1, new Insets(5,5,0,0));
        btnMinus1.setFont(Font.font("Arial",20));
        btnMinus1.setPrefSize(50, 20);       
        btnMinus1.setTextFill(Color.WHITE);
        btnMinus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        Rectangle[] rect = new Rectangle[6];
        for(int i = 0;i < 6;i++)
        {
            rect[i] = new Rectangle();
            GridPane.setMargin(rect[i], new Insets(0,5,0,0));
            rect[i].setWidth(50);
            rect[i].setHeight(30);        
            rect[i].setFill(Color.valueOf("333300"));    
            pan1.addColumn(i + 1, rect[i]);        
        }    
        btnMinus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color3 > 0)
                    color3--;
                Light(color3,rect);
            }
        });
                     
        
        Button btnPlus1 = new Button("+");
        btnPlus1.setFont(Font.font("Arial",20));
        btnPlus1.setPrefSize(50, 20);       
        btnPlus1.setTextFill(Color.WHITE);
        btnPlus1.setStyle("-fx-background-color: #644123;"
                + "-fx-border-color: white;"
                + "-fx-border-radius: 3px;");        
        btnPlus1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if (color3 < 6)
                    color3++;
                Light(color3,rect);
            }
        });
        pan1.addColumn(0, btnMinus1);        
        pan1.addColumn(7, btnPlus1);
        
        panel.addRow(2, pan1);
    }
    private void initLightControl(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Light Control ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        
        
        GridPane contentPane = new GridPane();             
        contentPane.setStyle("-fx-padding: 30 60 30 60;");      
        initLightRow1(contentPane);
        initLightRow2(contentPane);
        initLightRow3(contentPane);
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 20 20 20 20;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(0, pan);
    }
    public void initMedicalGases(GridPane panel){
        StackPane pan = new StackPane();
        Label title = new Label(" Medical Gases ");        
        title.setFont(Font.font("Arial",15));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-translate-y: -10;"
                + "-fx-background-color: #644123;");
        
        StackPane.setAlignment(title, Pos.TOP_LEFT);        
        
        
        GridPane contentPane = new GridPane();        
        
        GridPane pan1 = new GridPane();
        
        Text text1 = new Text("Q2");        
        text1.setFill(Color.YELLOW);
        text1.setStyle("-fx-translate-x: 20;");
        Button rect1 = new Button("Low");
        GridPane.setMargin(rect1, new Insets(5,20,0,0));
        rect1.setFont(Font.font("Arial",15));	        
        rect1.setPrefSize(70, 70);
        rect1.setStyle("-fx-background-color: #FF4500;");        

        pan1.addRow(0, text1);
        pan1.addRow(1, rect1);
        
        GridPane pan2 = new GridPane();
        Text text2 = new Text("N20");
        text2.setFill(Color.YELLOW);
        text2.setStyle("-fx-translate-x: 20;");
        Button rect2 = new Button("High");
        GridPane.setMargin(rect2, new Insets(5,20,0,0));
        rect2.setFont(Font.font("Arial",15));	        
        rect2.setPrefSize(70, 70);
        rect2.setStyle("-fx-background-color: #80EE80;");
        
        pan2.addRow(0, text2);
        pan2.addRow(1, rect2);        
        
        GridPane pan3 = new GridPane();
        Text text3 = new Text("AIR1");                
        text3.setFill(Color.YELLOW);
        text3.setStyle("-fx-translate-x: 20;");
        Button rect3 = new Button("Norm");
        GridPane.setMargin(rect3, new Insets(5,20,0,0));
        rect3.setFont(Font.font("Arial",15));
        rect3.setPrefSize(70, 70);
        rect3.setStyle("-fx-background-color: #80EE80;");
        
        pan3.addRow(0, text3);
        pan3.addRow(1, rect3);        
        
        GridPane pan4 = new GridPane();
        Text text4 = new Text("CO2");
        text4.setFill(Color.YELLOW);
        text4.setStyle("-fx-translate-x: 20;");
        Button rect4 = new Button("Norm");
        GridPane.setMargin(rect4, new Insets(5,20,0,0));
        rect4.setFont(Font.font("Arial",15));
        rect4.setPrefSize(70, 70);
        rect4.setStyle("-fx-background-color: #FF4500;");
        
        pan4.addRow(0, text4);
        pan4.addRow(1, rect4);                
        
        GridPane pan5 = new GridPane();
        Text text5 = new Text("VAC");
        text5.setStyle("-fx-translate-x: 20;");
        text5.setFill(Color.YELLOW);
        Button rect5 = new Button("High");
        GridPane.setMargin(rect5, new Insets(5,20,0,0));
        rect5.setFont(Font.font("Arial",15));
        rect5.setPrefSize(70, 70);
        rect5.setStyle("-fx-background-color: #FF4500;");
        
        pan5.addRow(0, text5);
        pan5.addRow(1, rect5);                
        
        contentPane.setStyle("-fx-padding: 50 30 30 30;");        
        contentPane.addColumn(0, pan1);
        contentPane.addColumn(1, pan2);
        contentPane.addColumn(2, pan3);
        contentPane.addColumn(3, pan4);
        contentPane.addColumn(4, pan5);
        pan.getChildren().addAll(title, contentPane);               
        pan.setStyle("-fx-content-display: top;"
                + "-fx-border-insets: 20 40 20 40;"                
                + "-fx-border-color: white;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 8px;");        
        panel.addColumn(1, pan);
    }
    private void initComponent(GridPane root){
        GridPane panel1 = new GridPane();
        GridPane panel2 = new GridPane();
        GridPane panel3 = new GridPane();  
        
        initDayTime(panel1);
        initElapsedTime(panel1);
        
        initTemperatureControl(panel2);
        initHumiddityControl(panel2);
        initPressureControl(panel2);
        
        initLightControl(panel3);
        initMedicalGases(panel3);
        
        root.addRow(0, panel1);
        root.addRow(1, panel2);
        root.addRow(2, panel3);
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();  
        root.setBackground(new Background(new BackgroundFill(Color.rgb(100, 65, 35), CornerRadii.EMPTY, Insets.EMPTY)));              
        Scene scene = new Scene(root, 1180, 800);
        
        initComponent(root);
        
        primaryStage.setTitle("Surgery Control Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);         
        
    }

    @Override
    public void stop() throws Exception {
        clockThread.stop();
        elapsedThread.stop();
        super.stop();
        System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}