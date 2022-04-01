import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.io.*;
import java.lang.Object.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.geom.*;

public class Life implements Runnable{
    public static CyclicBarrier barrier;
    public static int [] poblacion;
    public static ReentrantLock lock = new ReentrantLock();
    public static ReentrantLock locker = new ReentrantLock();

    // public static AtomicInteger poblacion = new AtomicInteger();//inicializado a 0

    public static int DIM=200;//Valor Minimo
    public static int Generaciones=0;
    public static String[] front= {"Nula","Cilindrica"};
    public static boolean Frontera=false;
    public int FilasInf,FilasSup;//Cada hilo tiene sus propias filas, no necesitamos columnas, divimos la matriz solo por columnas
    public int NHilo;//Multiplicamos el numero del hilo por su fila y asi sabemos que filas corresponden a que hilos
    public boolean AB;
    public int G;
    //for(int i=(NHilo-1)*Filas;i<NHilo*Filas;++i)

    //AtomicInteger????

    public static int [][] ReticulaA;
    public static int [][] ReticulaB;
    public static int [][] VecinosP;
    public static Canvas Curva;
    public static Canvas Vida;//Para pintar concurrentemente usar cyclerBArrier para indicar que todos los hilos han terminado de calcular
    public static Graphics2D Vida2D;
    public static Graphics2D Curva2D;
    public static int X1,X2;
    public static double Y1,Y2;


    public static JMenuBar BarraMenu;
    public static JMenu mFile,mFAutomata,mFTuring;
    public static JMenuItem P1,P2,mAbout,mHelp;
    public static GridBagConstraints c = new GridBagConstraints();
    public static JComboBox<String> CFrontera;
    public static JButton Start;
    public static JTextPane  TVivas,TMuertas;
    public static TextField TGeneraciones,TDIM;
    public static JLabel LVivas,LMuertas,LGeneraciones,LDIM,LFrontera;
    public static JFrame frame= new JFrame();


    public Life(long filasinf,long filassup,boolean ab,int g){
        FilasInf=(int)filasinf;
        FilasSup=(int)filassup;
        AB=ab;
        G=g;
    }

    public void run(){
        int total=0;
        //   locker.lock();
        for(int i=FilasInf;i<FilasSup;++i){
            for(int j=0;j<DIM;++j){

                if(AB){
                    if(ReticulaA[i][i]==1)total++;
                    evaluarA(i,j);
                }
                else{
                    if(ReticulaB[i][i]==1)total++;
                    evaluarB(i,j);
                }
            }

        }
        //locker.unlock();
        try{
            barrier.await();//No necesitamos reiniciarlo
        }catch(Exception ex){}

        lock.lock();
        poblacion[G]+=total;
        lock.unlock();




    }


    public static void evaluarA(int i, int j){//Cuando comprobemos que funciona todo comprobar si es necesario comprobar
        int vecinos=0;
        vecinos=VecinosA(i,j);				//el estado de las celulas en las 2 primeras comprobaciones
        if(ReticulaA[i][j]==1){
            if(vecinos>3 || vecinos<2)ReticulaB[i][j]=0;
            else ReticulaB[i][j]=1;
        }
        else{
            if(ReticulaA[i][j]==0){
                if(vecinos==3)ReticulaB[i][j]=1;
                else  ReticulaB[i][j]=0;
            }
        }

    }


    public static void evaluarB(int i, int j){//Cuando comprobemos que funciona todo comprobar si es necesario comprobar
        int vecinos=0;
        vecinos=VecinosB(i,j);				//el estado de las celulas en las 2 primeras comprobaciones
        if(ReticulaB[i][j]==1){
            if(vecinos>3 || vecinos<2)ReticulaA[i][j]=0;
            else ReticulaA[i][j]=1;
        }
        else{
            if(ReticulaB[i][j]==0){
                if(vecinos==3)ReticulaA[i][j]=1;
                else  ReticulaA[i][j]=0;
            }
        }


    }

    /////////////////////////////////
    ///  |j-1,i-1|j,i-1|j+1,i-1| ////
    ///	 |j-1, i |j, i |j+1, i | ////
    ///	 |j-1,i+1|j+1,i|j+1,i+1| ////
    /////////////////////////////////

    public static int VecinosA(int i, int j){//El metodo debe ser estatico??????
        int vivas=0;
        //Contamos primero las columnas
        if(Frontera){
            int hc,hf;
            for(int c=(j-1);c<(j+2);++c){
                hc=c;
                if(c<0)hc=DIM-1;
                if(c>=DIM)hc=0;
                for(int f=(i-1);f<(i+2);++f){
                    hf=f;
                    if(f<0)hf=DIM-1;
                    if(f>=DIM)hf=0;
                    if(c!=f){ 	 	//La propia celula no cuenta
                        if(ReticulaA[hf][hc]==1)++vivas;
                    }
                }
            }
        }
        else{
            for(int c=(j-1);c<(j+2);++c){
                if(c>=0 && c<DIM){
                    for(int f=(i-1);f<(i+2);++f){
                        if(c!=f){
                            if(f>=0 && f<DIM){//La propia celula no cuenta
                                if(ReticulaA[f][c]==1)++vivas;
                            }
                        }
                    }
                }
            }
        }
        VecinosP[i][j]=vivas;
        return vivas;
    }

    public static int VecinosB(int i, int j){//El metodo debe ser estatico??????
        int vivas=0;
        //Contamos primero las columnas
        if(Frontera){
            int hc,hf;
            for(int c=(j-1);c<(j+2);++c){
                hc=c;
                if(c<0)hc=j=DIM-1;
                if(c>=DIM)hc=0;
                for(int f=(i-1);f<(i+2);++f){
                    hf=f;
                    if(f<0)hf=DIM-1;
                    if(f>=DIM)hf=0;
                    if(c!=f){ 	 	//La propia celula no cuenta
                        if(ReticulaB[hf][hc]==1)++vivas;
                    }
                }
            }
        }
        else{
            for(int c=(j-1);c<(j+2);++c){
                if(c>=0 && c<DIM){
                    for(int f=(i-1);f<(i+2);++f){
                        if(c!=f){
                            if(f>=0 && f<DIM){//La propia celula no cuenta
                                if(ReticulaB[f][c]==1)++vivas;
                            }
                        }
                    }
                }
            }

        }
        VecinosP[i][j]=vivas;
        return vivas;
    }

    public static void Primera(){
        Random rd=new Random();
        for(int i=0;i<DIM;++i){
            for(int j=0;j<DIM;++j){
                if(rd.nextDouble()>0.5)ReticulaA[i][j]=1;
                else ReticulaA[i][j]=0;
            }
        }
        PintarVidaA();
    }

    public static void PintarVidaA(){// filalast no se pinta, es el topo
        int X=0,Y=0;//Ver el tama�o adecuado para los cuadrados

        for(int i=0;i<DIM;++i){//Columnas
            for(int j=0;j<DIM;++j){//Filas
                if(ReticulaA[i][j]==1){ //Solo pintamos si esta viva, el fondo es blanco
                    Vida2D.setColor(Color.GREEN);//Las vivas son negras XD
                    Vida2D.fillRect(X,Y,1,1);
                }
                else{
                    Vida2D.setColor(Color.BLACK);//Las vivas son negras XD
                    Vida2D.fillRect(X,Y,1,1);
                }
                X+=1;

            }
            Y+=1;
            X=0;

        }

        try{
            Thread.sleep(0);
        }catch(Exception ex){}


    }

    public static void PintarVidaB(){// filalast no se pinta, es el topo
        int X=0,Y=0;//Ver el tama�o adecuado para los cuadrados
        for(int i=0;i<DIM;++i){//Columnas
            for(int j=0;j<DIM;++j){//Filas
                if(ReticulaB[i][j]==1){
                    Vida2D.setColor(Color.GREEN);//Las vivas son negras XD
                    Vida2D.fillRect(X,Y,1,1);
                }
                else{
                    Vida2D.setColor(Color.BLACK);//Las vivas son negras XD
                    Vida2D.fillRect(X,Y,1,1);
                }
                X+=1;
            }
            Y+=1;
            X=0;
        }

        try{
            Thread.sleep(0);
        }catch(Exception ex){}


    }

    public static void PintarCurva(int g){
        int altura=Curva.getHeight();
        int ancho=Curva.getWidth();
        // 	 System.out.println("altura: "+altura+" anchura: "+ancho);
        Curva2D.setColor(Color.BLUE);
        X2+=15;
        if(g==0){
            Y1=(double)altura;
            X2=15;
            X1=0;
        }
        Y2=((double)poblacion[g]/((double)DIM*(double)DIM))*(double)altura;
        Y2=(double)altura-Y2;
        // System.out.println("X1: "+X1+" Y1: "+Y1+" X2: "+X2+" Y2: "+Y2);
        //   if((int)Y2==0)Y2=15;

        //System.out.println(Y2);
        Curva2D.draw(new Line2D.Double(X1, Y1, X2,Y2));//)(X1,(int)Y1,X2,(int)Y2);
        X1=X2;
        //if((int)Y2==0)Y1=15;
        Y1=Y2;
    }




    public static void Boton(){
        Start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // public static TextField TVivas,TMuertas,TGeneraciones,TDIM;



                String generaciones,dimensiones,frontera;
                generaciones=TGeneraciones.getText();
                dimensiones=TDIM.getText();
                frontera=(String)CFrontera.getSelectedItem();
                DIM=Integer.parseInt(dimensiones);
                Generaciones=Integer.parseInt(generaciones);
                if(DIM<200)DIM=200;
                if(frontera.equals("Cilindrica"))Frontera=true;
                int X1=0;
                int X2=0;
                int Y1=Curva.getHeight();
                int Y2=0;
                ReticulaA= new int[DIM][DIM];
                ReticulaB= new int[DIM][DIM];
                VecinosP = new int [DIM][DIM];
                poblacion= new int[Generaciones];


                Vida.update(Vida.getGraphics());
                Curva.update(Curva.getGraphics());
                Cuadricular();//A�adir Cuadricula a la curva


                Primera();//Genera por probabilidad aleatoria la primera generacion
                //mostrar(false);


                long nTareas=Runtime.getRuntime().availableProcessors();
                //long MaxnTareas=2*nTareas;//Comprobar eficiencia
                long tVentana=DIM/nTareas;
                long linf=0;
                long lsup=tVentana;
                boolean AoB;//------------------
                Y1=Curva.getHeight();

                barrier = new CyclicBarrier((int)nTareas);




                for(int g=0;g<Generaciones;++g){
                    ThreadPoolExecutor pool= new ThreadPoolExecutor((int)nTareas,(int)nTareas,0L,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
                    if(tVentana==0){
                        tVentana=DIM;
                        nTareas=1;
                    }
                    poblacion[g]=0;
                    if((g%2)==0)AoB=true;
                    else AoB=false;

                    linf=0;
                    lsup=tVentana;

                    for(int i=0;i<nTareas;++i){
                        pool.execute(new Life(linf,lsup,AoB,g));
                        linf=tVentana;
                        lsup+=tVentana;
                        if(lsup>DIM)lsup=DIM;

                    }
                    pool.shutdown();
                    try{
                        pool.awaitTermination(2, TimeUnit.SECONDS);
                    }catch(Exception ex){}

                    /**  try{
                     pool.awaitTermination(2, TimeUnit.SECONDS);
                     }catch(Exception ex){}*/

                    //mostrar(!AoB);
                    //System.out.println("g: "+poblacion[g]);
                    //mostrarVecinos();
                    PintarCurva(g);

                    if(AoB){
                        //BorrarA();
                        PintarVidaB();//el opuesto al que indica AB

                    }
                    else{
                        //BorrarB();
                        PintarVidaA();
                    }

                    //System.out.println("Generacion: "+g);
                }


                int total=poblacion[Generaciones-1];
                String vivitas=Integer.toString(total);
                vivitas=vivitas+"\n";
                TVivas.setText(vivitas);
                String amochadas=Integer.toString(DIM*DIM-total);
                amochadas= amochadas+"\n";
                TMuertas.setText(amochadas);

                System.out.println("Finish");


            }
        });

        mAbout.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null,"Lo he hecho yo : )");
            }
        });

        mHelp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null,"Los colores que representa los estados son distintas tonalidades de azul\nLa curva de poblacion tiene de rango X[0,n� generaciones] e Y[0,400]");
            }
        });

    }
    /** public static void mostrar(boolean d){
     System.out.println();
     if(d){
     for(int i=0;i<DIM;++i){
     for(int j=0;j<DIM;++j){
     System.out.print(" "+ReticulaB[i][j]);
     }
     System.out.println();
     }
     }

     else{
     for(int i=0;i<DIM;++i){
     for(int j=0;j<DIM;++j){
     System.out.print(" "+ReticulaA[i][j]);
     }
     System.out.println();
     }
     }
     }
     */
    public static void mostrarVecinos(){
        System.out.println();

        for(int i=0;i<DIM;++i){
            for(int j=0;j<DIM;++j){
                System.out.print(" "+VecinosP[i][j]);
            }
            System.out.println();
        }
    }
    /**
     public static void BorrarA(){
     for(int i=0;i<DIM;++i){
     for(int j=0;j<DIM;++j){
     ReticulaA[i][j]=0;
     }
     }

     }
     public static void BorrarB(){
     for(int i=0;i<DIM;++i){
     for(int j=0;j<DIM;++j){
     ReticulaB[i][j]=0;
     }
     }

     }*/


    public static void Marco(){
        BarraMenu = new JMenuBar();
        mFile = new JMenu("File");
        mAbout = new JMenuItem("About");
        mHelp= new JMenuItem("Help");
        mFAutomata = new JMenu("Automata Celular");
        mFTuring = new JMenu("Maquina de Turing");
        P1 = new JMenuItem("Practica 1");
        P2 = new JMenuItem("Practica 2");
        //this.init();
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
        //frame.setMinimumSize(new Dimension(350,350));
        //frame.setMaximumSize(new Dimension(1000,1000));
        //frame.setSize(500,500);
        frame.setResizable(true);
        //frame.setEnabled(true);
        mFAutomata.add(P1);
        mFAutomata.add(P2);
        mFile.add(mFAutomata);
        mFile.add(mFTuring);
        BarraMenu.add(mFile);
        for(int i=0;i<30;++i)
            BarraMenu.add(Box.createHorizontalGlue());
        BarraMenu.add(mAbout);
        BarraMenu.add(mHelp);

        frame.setJMenuBar(BarraMenu);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        //frame.add(grafico,c);
    }

    public static void Grafico(){
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=6;
        c.gridheight=6;
        c.fill=GridBagConstraints.BOTH;
        Vida= new Canvas();
        c.weightx=1.0;
        c.weighty=1.0;
        Vida.setBackground(Color.BLACK);
        frame.add(Vida,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //---------------------
        c.gridx=0;
        c.gridy=6;
        c.gridwidth=8;
        c.gridheight=3;
        c.fill=GridBagConstraints.BOTH;
        Curva= new Canvas();
        Curva.setBackground(Color.WHITE);

        c.weightx=1.0;
        c.weighty=1.0;
        frame.add(Curva,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-------------
        c.gridx=6;
        c.gridy=0;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.NONE;

        LDIM= new JLabel("Dimension:",SwingConstants.CENTER);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(LDIM,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //--------------
        c.gridx=7;
        c.gridy=0;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.HORIZONTAL;

        TDIM= new TextField("200");
        //c.weightx=1.0;
        //	c.weighty=1.0;
        frame.add(TDIM,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-----------------
        c.gridx=6;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        LGeneraciones= new JLabel("Generaciones: ",SwingConstants.CENTER);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(LGeneraciones,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-------------
        c.gridx=7;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        TGeneraciones= new TextField("500");
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(TGeneraciones,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //---------------
        c.gridx=6;
        c.gridy=2;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.BOTH;

        LFrontera= new JLabel("Frontera: ",SwingConstants.CENTER);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(LFrontera,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-------------
        c.gridx=7;
        c.gridy=2;
        c.gridwidth=1;
        c.gridheight=1;
        CFrontera= new JComboBox<>(front);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(CFrontera,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //----------------------
        c.gridx=6;
        c.gridy=3;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.HORIZONTAL;

        LVivas= new JLabel("Celulas Vivas: ",SwingConstants.CENTER);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(LVivas,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //---------------------
        c.gridx=7;
        c.gridy=3;
        c.gridwidth=1;
        c.gridheight=1;
        TVivas= new JTextPane ();
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(TVivas,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-----------------
        c.gridx=6;
        c.gridy=4;
        c.gridwidth=1;
        c.gridheight=1;
        LMuertas= new JLabel("Celulas Muertas: ",SwingConstants.CENTER);
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(LMuertas,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //-------------------
        c.gridx=7;
        c.gridy=4;
        c.gridwidth=1;
        c.gridheight=1;
        TMuertas= new JTextPane ();
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(TMuertas,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //--------------------
        c.gridx=6;
        c.gridy=5;
        c.gridwidth=2;
        c.gridheight=1;
        c.fill=GridBagConstraints.BOTH;
        Start= new JButton("Start");
        //c.weightx=1.0;
        //c.weighty=1.0;
        frame.add(Start,c);
        c.weightx=0.0;
        c.weighty=0.0;
        //----------
        Vida2D=(Graphics2D)Vida.getGraphics();
        Vida2D.setBackground(Color.WHITE);
        Curva2D=(Graphics2D)Curva.getGraphics();
        Curva2D.setBackground(Color.WHITE);
        frame.pack();

        Cuadricular();//A�adir Cuadricula a la curva

    }



    public static void Cuadricular(){
        int anchura=Curva.getWidth();
        int altura=Curva.getHeight();

        Curva2D.setColor(Color.BLACK);
        for(int i=0;i<altura;i+=15){
            Curva2D.drawLine(0,i,anchura,i);
        }
        for(int j=0;j<anchura;j+=15){
            Curva2D.drawLine(j,0,j,altura);
        }
    }

    public static void main(String[] args){
        frame.getContentPane().setLayout(new GridBagLayout());
        Marco();
        Grafico();
        Boton();
    }

}

