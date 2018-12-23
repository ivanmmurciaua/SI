/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1819_p2si;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Ivan
 */
public class Adaboost {
    
    //Cambiar el numero de CDs a usar (T)
    static int CDaUsar = 850;
    //Cambiar el numero de pruebas aleatorias a usar (A)
    static int PruAle = 8000;
    
    //Clase de Clasificador Debil
    class CD{
        private int pixel;
        private int umbral;
        private int direccion;
        private double alfa;
        
        CD(){
            this.pixel = (int)(Math.random()*784);
            this.umbral = (int)(Math.random()*255);
            //Evitamos los espacios en blanco
            do{
                this.pixel = (int)(Math.random()*784);
                this.umbral = (int)(Math.random()*255);
            }while(this.umbral==127);
            
            this.direccion = (int)(Math.random()*784);
            
            if(this.direccion % 2 == 0){
                this.direccion = 1;
            }
            else{
                this.direccion = -1;
            }
            this.alfa = 0;
        }
        
        CD(CD cd){
            this.pixel = cd.pixel;
            this.umbral = cd.umbral;
            this.direccion = cd.direccion;
            this.alfa = cd.alfa;
        }
        
        /*
        * Sacamos el umbral del pixel de la imagen pasada que sea igual al
        * calculado aleatoriamente para nuestro CD
        */
        public int estaDentro(Imagen xi){
            int res = -1;
            
            byte imageData[] = xi.getImageData();
            imageData[this.pixel] = (byte) (255 - imageData[this.pixel]);
            int umbralDeLaImagen = (int)(imageData[this.pixel] + 128);
            
            //System.out.println("UMBRAL DE LA IMAGEN "+umbralDeLaImagen+" UMBRAL DEL CD "+this.umbral);
            
            if(this.direccion == 1){
                if(umbralDeLaImagen > this.umbral) res = 1;
            }    
            else{
                if(umbralDeLaImagen <= this.umbral) res = 1;
            }
            
            return res;
        }

        public int estaDentroParaLeer(int[] imagen){
            int res = -1;
            int umbralDeLaImagen = (int)(imagen[this.pixel]);
            
            if(this.direccion == 1){
                if(umbralDeLaImagen > this.umbral) res = 1;
            }
            else{
                if(umbralDeLaImagen <= this.umbral) res = 1;
            }
            
            return res;
        }
        
        @Override
        public String toString(){
            return this.pixel+";"+this.umbral+";"+this.direccion+";"+this.alfa;
        }
        
        public void escribirEnFichero(String nombrefichero){
            try(FileWriter fw = new FileWriter(nombrefichero, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println(this);
            }
            catch (IOException e) {}
        }
        
    }
    
    
    public void Adaboost(ArrayList<Imagen> X, int[] Y, String cf){
       
        double[] D = new double[X.size()];
        Arrays.fill(D,(double)1/X.size());

        for(int t=1;t<=Adaboost.CDaUsar;t++){
            double errormin = 230.0;
            //System.out.println("NUEVO CD");
            CD elegido = null;
            double vector = 0.0;
            //for(int y=0;y<D.length;y++){
            //    vector += D[y];
            //}
            for(int k=1;k<=Adaboost.PruAle;k++){
                double errorv = 0.0;
                CD cd = new CD();
                
                for(int i=0;i<D.length;i++){
                    int hxy = 0;
                    if(cd.estaDentro(X.get(i)) != Y[i]) hxy = 1;
                    errorv += (D[i] * hxy);    
                }
                //double errortotal = errorv/vector;
                //if(errortotal < errormin) {elegido = new CD(cd); errormin=errortotal;}
                if(errorv < errormin) {elegido = new CD(cd); errormin=errorv;}
            }

            
            //CALCULO DE ALFA
            if(errormin == 0.0){break;}
            System.out.println(errormin+"% de error");
            double alf = (double)(0.5*(Math.log((1-errormin)/errormin)));
            if(alf == Double.parseDouble("Infinity")){break;}
            elegido.alfa = alf;
          

            //GRABAMOS EN FICHERO DE TEXTO
            elegido.escribirEnFichero(cf);
            System.out.println("Confianza: "+elegido.alfa);
            
            double Z = 0.0;
            for(int i=0;i<D.length;i++){
                D[i] = (double)D[i]*(Math.pow(Math.E,-elegido.alfa*(Y[i]*elegido.estaDentro(X.get(i)))));
                Z += (double)D[i];
            }
            
            for(int i=0;i<D.length;i++) D[i] /= Z;
            
        }
        
        try(FileWriter fw = new FileWriter(cf, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println("-");
            }
            catch (IOException e) {}
        
    }
    
    
    public double[] AdaboostRun(int[] img, String cf){
        
            CD cd = new CD();
            
            double[] categos = new double[8];
            Arrays.fill(categos,0);
            int cat = 0;
            int cate = 0;
            
            //-RUN  (LECTURA FICHERO)
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
               archivo = new File(cf);
               fr = new FileReader (archivo);
               br = new BufferedReader(fr);

               String linea;
               while((linea=br.readLine())!=null){
                   if("-".equals(linea)){
                       cat++;
                   }
                   else{
                      String[] partes = linea.split(";");
                      cd.pixel = Integer.parseInt(partes[0]);
                      cd.umbral = Integer.parseInt(partes[1]);
                      cd.direccion = Integer.parseInt(partes[2]);
                      cd.alfa = (double)Double.parseDouble(partes[3]);
                      categos[cat] += (double)cd.alfa*cd.estaDentroParaLeer(img);
                   }
               }
            }
            catch(IOException e){}
            finally{
               try{                    
                  if( null != fr ){   
                     fr.close();     
                  }                  
               }catch (IOException e2){}
            }
               return categos;
        }
    
        public double[] AdaboostRun(Imagen img, String cf){
        
            CD cd = new CD();
            
            double[] categos = new double[8];
            Arrays.fill(categos,0);
            int cat = 0;
            int cate = 0;
            
            //-RUN  (LECTURA FICHERO)
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
               archivo = new File(cf);
               fr = new FileReader (archivo);
               br = new BufferedReader(fr);

               String linea;
               while((linea=br.readLine())!=null){
                   if("-".equals(linea)){
                       cat++;
                   }
                   else{
                      String[] partes = linea.split(";");
                      cd.pixel = Integer.parseInt(partes[0]);
                      cd.umbral = Integer.parseInt(partes[1]);
                      cd.direccion = Integer.parseInt(partes[2]);
                      cd.alfa = (double)Double.parseDouble(partes[3]);
                      categos[cat] += (double)cd.alfa*cd.estaDentro(img);
                   }
               }
            }
            catch(IOException e){}
            finally{
               try{                    
                  if( null != fr ){   
                     fr.close();     
                  }                  
               }catch (IOException e2){}
            }
               return categos;
        }
    }
