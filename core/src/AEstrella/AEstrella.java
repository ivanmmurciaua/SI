/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AEstrella;
import static java.lang.Math.*;
import java.util.*;


/**
 *
 * @author mirse
 */
public class AEstrella {
 
    //Mundo sobre el que se debe calcular A*
    Mundo mundo;
    
    //Camino
    public char camino[][];
    
    //Casillas expandidas
    int camino_expandido[][];
    
    //Número de nodos expandidos
    int expandidos;
    
    //Coste del camino
    float coste_total;
    
    public AEstrella(){
        expandidos = 0;
        mundo = new Mundo();
    }
    
    public AEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    //COLUMNA=X FILA=Y
    private class Nodo{
            
        //f(n)
        protected int f;
        //Total del peso hasta el momento
        protected  int g;
        //f(h) - Heurística
        private int h;
        //Coordenada x
        private int x;
        //Coordenada y
        private int y;
        //Coordenada z
        private int z;
        //El padre
        protected Nodo padre;
        
        Nodo(){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.padre = null;
        }
        
        Nodo(Coordenada c, int f, int g, int h){
            this.g = g;
            this.h = h;
            this.f = this.g + this.h;
            this.x = c.getX() - (c.getY() + (c.getY()&1)) / 2;
            this.z = c.getY();
            this.y = -this.x-this.z;
            this.padre = null;
        }
         
        Nodo(Nodo n){
            this.f = n.f;
            this.g = n.g;
            this.h = n.h;
            this.x = n.x;
            this.y = n.y;
            this.z = n.z;
            this.padre = n.padre;
        }
        
        //PARA LAS VECINAS
        Nodo(int x, int y, int z){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = x;
            this.y = y;
            this.z = z;
            this.padre = new Nodo();
        }
        
        Nodo transformarACubicas(Coordenada c){
            this.x = c.getX() - (c.getY() + (c.getY()&1)) / 2;
            this.z = c.getY();
            this.y = -this.x-this.z;
            return this;
        }

        ArrayList misVecinitas(){
            //List<Nodo> listavecinas = Arrays.asList(new Nodo(1,-1,0),new Nodo(1,0,-1),new Nodo(0,1,-1),new Nodo(-1,1,0),new Nodo(-1,0,1),new Nodo(0,-1,1));
            int vecinass[][] = new int[][]{
                {1,-1,0},
                {1,0,-1},
                {0,1,-1},
                {-1,1,0},
                {-1,0,1},
                {0,-1,1}
            };
            
            ArrayList<Nodo> vecinas;
            vecinas = new ArrayList<Nodo>();
            Nodo vecina = new Nodo();
            
            for (int[] vecinas1 : vecinass) {
                vecinas.add(new Nodo(this.x + vecinas1[0], this.y + vecinas1[1], this.z + vecinas1[2]));
            }
            
            return vecinas;
        }
        
        public Nodo obtenerMenorf(ArrayList<Nodo> lf){
            Nodo nod = new Nodo();
            if(lf.size() == 1){
                nod = lf.get(0);
            }
            else{
                nod = lf.get(0);
                for(int i=1;i<lf.size();i++){
                    if(lf.get(i).f < nod.f)
                        nod = lf.get(i);
                }
            }
            return nod;
        }
        
        public boolean esta(ArrayList<Nodo> l){
            for(int i=0;i<l.size();i++){
                if(l.get(i).equals(this)){
                    return true;
                }
            }
            return false;
        }
        
        public void setH(Nodo b){
            this.h= (abs(this.x - b.x) + abs(this.y - b.y) + abs(this.z - b.z)) / 2;
        }
        public int getH(){
            return this.h;
        }
        public void setFather(Nodo n){
            this.padre = n;
        }
        public Nodo getFather(){
            return this.padre;
        }

        public boolean equals(Nodo b){
            return (this.x == b.x && this.y == b.y && this.z == b.z);
        }
        
        public Coordenada deCubicaAOffset(){
            Coordenada c = new Coordenada();
            c.x = this.x + (this.z + (this.z & 1)) / 2;
            c.y = this.z;
            return c;
        }
        
        public void setG(){
            switch(mundo.getCelda(this.deCubicaAOffset().getX(), this.deCubicaAOffset().getX())){
                case 'c' : this.g = this.padre.g + 1;
                           break;
                case 'h' : this.g = this.padre.g + 2;
                           break;
                case 'a' : this.g = this.padre.g + 3;
                           break;
            }
        }
        
        public int Ge(Nodo a){
            int res = 0;
            switch(mundo.getCelda(this.deCubicaAOffset().getX(), this.deCubicaAOffset().getY())){
                case 'c' : res = a.g + 1;
                         break;
                case 'h' : res = a.g + 2;
                         break;
                case 'a' : res = a.g + 3;
                         break;
            }
            return res;
        }
        
        public void setF(){
            this.f = this.g + this.h;
        }
        
        @Override
        public String toString(){
            String res;
            res = "COORDENADAS: ["+this.deCubicaAOffset().getY()+","+this.deCubicaAOffset().getX()+"] ;CUBICAS: X= " + this.x + "; Z= " + this.z + "; Y= " +this.y + "; F = "+this.f+"; G= "+this.g+"; H= "+this.h+"; FATHER = "+this.padre;
            return res;
        }
        
    }
    
    public ArrayList<Nodo> eligiendoVecinas(ArrayList<Nodo> vec, ArrayList<Nodo> li){
        ArrayList<Nodo> elegidas = new ArrayList<Nodo>();
        //VECINAS
        for(int i=0;i<vec.size();i++){
            if(!vec.get(i).esta(li)){
                if(this.mundo.getCelda(vec.get(i).deCubicaAOffset().getX(), vec.get(i).deCubicaAOffset().getY()) != 'b' && this.mundo.getCelda(vec.get(i).deCubicaAOffset().getX(), vec.get(i).deCubicaAOffset().getY()) != 'p'){
                   elegidas.add(vec.get(i));
                }
            }
        }
        return elegidas;
    }
     
    //Calcula el A*
    public int CalcularAEstrella(){

        boolean encontrado = true;
        int result = 1;
       
        //AQUÍ ES DONDE SE DEBE IMPLEMENTAR A*
        Nodo n;
        Nodo end;
        
        int g = 0;
        int h = 0;
        
        end = new Nodo(this.mundo.getDragon(),0,g,h);
        n = new Nodo(this.mundo.getCaballero(),0,g,h);
        
        n.setH(end);
        //System.out.println(n.h);
        
        ArrayList<Nodo> listaInterior = new ArrayList<Nodo>();
        ArrayList<Nodo> listaFrontera = new ArrayList<Nodo>();
        listaFrontera.add(n);
        
        
        
        while(!listaFrontera.isEmpty()){
            System.out.println("LISTA INTERIOR");
            for(int i=0;i<listaInterior.size();i++){
                System.out.println(listaInterior.get(i));
            }
            System.out.println("LISTA FRONTERA");
            for(int i=0;i<listaFrontera.size();i++){
                System.out.println(listaFrontera.get(i));
            }
            n = n.obtenerMenorf(listaFrontera);
            
            //camino[deCubicaAOffset(n).getY()][deCubicaAOffset(n).getX()] = 'X';
            
            //System.out.println(n.equals(end));
            
            
            if(n.equals(end)){
                encontrado = true;
                break;
            }
            else{ //b , p
                listaFrontera.remove(n);
                listaInterior.add(n);
                ArrayList<Nodo> vecinas = n.misVecinitas();
                vecinas = eligiendoVecinas(vecinas,listaInterior);
                for(int i=0;i<vecinas.size();i++){
                    g = vecinas.get(i).Ge(n);
                 
                    
                    if(!vecinas.get(i).esta(listaFrontera)){
                        vecinas.get(i).setH(end);
                        vecinas.get(i).g = g;
                        vecinas.get(i).setF();
                        vecinas.get(i).setFather(n);
                        listaFrontera.add(vecinas.get(i));
                    }
                    else{
                        if(g < vecinas.get(i).g){
                            vecinas.get(i).setFather(n);
                            vecinas.get(i).g = g;
                            vecinas.get(i).setF();
                        }
                    }
                }
            }
            
            
            
            
        }
        
        
        
        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            Nodo solucion = new Nodo();
            solucion = n;
            while(solucion != null){
                System.out.println("\n"+solucion.deCubicaAOffset().getX()+"\n");
                camino[solucion.deCubicaAOffset().getY()][solucion.deCubicaAOffset().getX()] = 'X';
                solucion = solucion.padre;
            }
            System.out.println("Caballero está en: "+mundo.getCaballero().getY()+","+mundo.getCaballero().getX()+"\n En coordenadas cúbicas: "+n.toString());
            System.out.println("Los vecinos del caballero son: ");
            //ArrayList<Nodo> vecinas = n.misVecinitas();
            /*for(int i=0;i<vecinas.size();i++){
                System.out.println(vecinas.get(i));
            }*/
            /*for(int i=0;i<vecinas.size();i++){
                System.out.println(deCubicaAOffset(vecinas.get(i)).getY()+","+deCubicaAOffset(vecinas.get(i)).getX()+"-> "+mundo.getCelda(deCubicaAOffset(vecinas.get(i)).getX(), deCubicaAOffset(vecinas.get(i)).getY()));
            }*/
            
            //Vamos a mostrar las vecinitas
            /*for(int i=0;i<vecinas1.size();i++){
                camino[vecinas1.get(i).deCubicaAOffset().getY()][vecinas1.get(i).deCubicaAOffset().getX()] = 'X';
            }*/
            
            //Dragón
            System.out.println("El dragón se encuentra en: "+mundo.getDragon().getY()+","+mundo.getDragon().getX()+"\n En coordenadas cubicas: "+end.toString());
            //(System.out.println("f(h): "+n.(end));
           
            
            
            //Mostrar las soluciones
            System.out.println("Camino");
            mostrarCamino();

            System.out.println("Camino explorado");
            mostrarCaminoExpandido();
            
            System.out.println("Nodos expandidos: "+expandidos);
        }

        return result;
    }
   
    //Muestra la matriz que contendrá el camino después de calcular A*
    public void mostrarCamino(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                System.out.print(camino[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    //Muestra la matriz que contendrá el orden de los nodos expandidos después de calcular A*
    public void mostrarCaminoExpandido(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                    System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                if(camino_expandido[i][j]>-1 && camino_expandido[i][j]<10)
                    System.out.print(" ");
                System.out.print(camino_expandido[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    public void reiniciarAEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    public float getCosteTotal(){
        return coste_total;
    }
}


