package org.basicsamples;
public class Cuerpo {
   static class ThisCuerpo {
       public long idNum;
       public String nombrePara;
       public ThisCuerpo gira;

       public static  long sigID =0;
       }  
   public static void main(String[]args) {
       ThisCuerpo sol = new ThisCuerpo();
       sol.idNum=ThisCuerpo.sigID++;
       sol.nombrePara = "Sol";
       sol.gira = null;

       ThisCuerpo tierra = new ThisCuerpo();
       tierra.idNum=ThisCuerpo.sigID++;
       tierra.nombrePara = "Tierra";
       tierra.gira = sol;
       }

   }

