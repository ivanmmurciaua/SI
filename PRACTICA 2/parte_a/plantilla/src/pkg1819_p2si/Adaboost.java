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
    
    final static int CDaUsar = 1;
    final static int PruAle = 3;
    
    
    class CD{
        
        private int pixel;
        private int umbral;
        private int direccion;
        private double alfa;
        
        CD(){
            this.pixel = (int)(Math.random()*784);
            this.umbral = (int)(Math.random()*255);
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
            int umbralDeLaImagen = imageData[this.pixel] + 128;
            
            //System.out.println("UMBRAL DE LA IMAGEN "+umbralDeLaImagen+" UMBRAL DEL CD "+this.umbral);
            
            if(this.direccion == 1){
                if(umbralDeLaImagen > this.umbral) res = 1;
            }    
            else{
                if(umbralDeLaImagen <= this.umbral) res = 1;
            }
            
            //System.out.println("RESULTADO "+res);
            return res;
        }        
        
        @Override
        public String toString(){
            return this.pixel+"|"+this.umbral+"|"+this.direccion+"|"+this.alfa;
        }
        
        public void escribirEnFichero(String nombrefichero){
            try(FileWriter fw = new FileWriter(nombrefichero, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)){
                out.println(this);
            }
            catch (IOException e) {}
        }
        
        public void leerDeFichero(String cf){
            //-RUN  (LECTURA FICHERO)
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
               archivo = new File (cf);
               fr = new FileReader (archivo);
               br = new BufferedReader(fr);

               String linea;
               while((linea=br.readLine())!=null)
                  System.out.println(linea);
            }
            catch(IOException e){}
            finally{
               try{                    
                  if( null != fr ){   
                     fr.close();     
                  }                  
               }catch (IOException e2){}
            }
        }

    }
    
    
    public void Adaboost(ArrayList<Imagen> X, int[] Y){
        
        //System.out.println(X.size());
        double[] D = new double[X.size()];
        Arrays.fill(D,(double)1/X.size());
        //System.out.println("TODAS LAS POSICIONES DE D INICIALIZADAS A "+D[0]);
        
        
        
        for(int t=0;t<Adaboost.CDaUsar;t++){
            double errormin = 23132;
            //System.out.println("NUEVO CD");
            CD elegido = null;
           
            for(int k=1;k<Adaboost.PruAle;k++){
                 double errorv = (double)0;
                
                CD cd = new CD();
                //System.out.println(cd);
                //System.out.println("#"+cd.hashCode());
                
                
                for(int i=0;i<D.length;i++){//D.length;i++){
                    int hxy = 0;
                    if(cd.estaDentro(X.get(i)) != Y[i]) hxy = 1;
                    errorv += D[i] * hxy;
                }
                //System.out.println("ERROR DEL CLASIFICADOR ESTE "+errorv);
                if(errorv < errormin) {elegido = new CD(cd); errormin=errorv;}
            }
            //System.out.println(elegido.estaDentro(X.get(0)));
            
            //if(errormin==0){errormin=(double)-9;}
            //System.out.println(elegido);
            //System.out.println("#"+elegido.hashCode());
            //System.out.println(errormin);
            //System.out.println(D[0]);
            
            //CALCULO DE ALFA
            
            double alf = (double)(0.5*(Math.log((1-errormin)/errormin)));
            elegido.alfa = alf;
            
            //GRABAMOS EN FICHERO DE TEXTO
            elegido.escribirEnFichero("pepo.cf");
            System.out.println(-elegido.alfa);
            double Z = 0;
            for(int i=0;i<D.length;i++){
                D[i] = (double)D[i]*(Math.pow(Math.E, -elegido.alfa*(Y[i]*elegido.estaDentro(X.get(i)))));
                Z += (double)D[i];
            }
            //System.out.println(D[0]);
            
            //for(int i=0;i<D.length;i++) D[i]=D[i]/Z;
        }
        //System.out.println("FIN");
        
        
    }
}
