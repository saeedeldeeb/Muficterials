/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OR1_Simplex;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author dell
 */

    
     public class Simplex extends JFrame{

         int numtable  ;
 private double[][] tableaux; // tableaux
 String [][] tablevalues;
 JTable []tables ;
 private int numberOfConstraints; // number of constraints
 private int numberOfOriginalVariables; // number of original variables

 private boolean maximizeOrMinimize;

 private static final boolean MAXIMIZE = true;
 private static final boolean MINIMIZE = false;

 private int[] basis; // basis[i] = basic variable corresponding to row i
 String [] names ;
  JScrollPane[]scroll ;
 
 public Simplex(){
     
     numtable = 0 ;
 }

 public Simplex(double[][] tableaux, int numberOfConstraint,
   int numberOfOriginalVariable, boolean maximizeOrMinimize) {
  this.maximizeOrMinimize = maximizeOrMinimize;
  this.numberOfConstraints = numberOfConstraint;
  this.numberOfOriginalVariables = numberOfOriginalVariable;
  this.tableaux = tableaux;
  
    scroll = new JScrollPane[3];
  
  tablevalues = new String [numberOfConstraints + 1][numberOfOriginalVariables
     + numberOfConstraints + 1];
  
      tables = new JTable[3]; 
      
       names = new String[numberOfOriginalVariables+numberOfConstraints+1];
    int i ;
        for ( i = 0; i < numberOfOriginalVariables+numberOfConstraints; i++) {

            names[i] = "X"+(i+1) ;
        }
        names[i] = "  b" ;


  basis = new int[numberOfConstraints];
  for ( i = 0; i < numberOfConstraints; i++)
   basis[i] = numberOfOriginalVariables + i;

  solve();
      System.out.println(numtable);

 }

 
 // run simplex algorithm starting from initial BFS
 private void solve() {
  while (true) {

   show();
   numtable+=1 ;
   int q = 0;
   // find entering column q
   if (maximizeOrMinimize) {
    q = dantzig();
   } else {
    q = dantzigNegative();
   }
   if (q == -1)
    break; // optimal

   // find leaving row p
   int p = minRatioRule(q);
   if (p == -1)
    throw new ArithmeticException("Linear program is unbounded");

   // pivot
   pivot(p, q);

   // update basis
   basis[p] = q;

  }
 }

 // index of a non-basic column with most positive cost
 private int dantzig() {
  int q = 0;
  for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++)
   if (tableaux[numberOfConstraints][j] > tableaux[numberOfConstraints][q])
    q = j;

  if (tableaux[numberOfConstraints][q] <= 0)
   return -1; // optimal
  else
   return q;
 }

 // index of a non-basic column with most negative cost
 private int dantzigNegative() {
  int q = 0;
  for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++)
   if (tableaux[numberOfConstraints][j] < tableaux[numberOfConstraints][q])
    q = j;

  if (tableaux[numberOfConstraints][q] >= 0)
   return -1; // optimal
  else
   return q;
 }

 // find row p using min ratio rule (-1 if no such row)
 private int minRatioRule(int q) {
  int p = -1;
  for (int i = 0; i < numberOfConstraints; i++) {
   if (tableaux[i][q] <= 0)
    continue;
   else if (p == -1)
    p = i;
   else if ((tableaux[i][numberOfConstraints
     + numberOfOriginalVariables] / tableaux[i][q]) < (tableaux[p][numberOfConstraints
     + numberOfOriginalVariables] / tableaux[p][q]))
    p = i;
  }
  return p;
 }

 // pivot on entry (p, q) using Gauss-Jordan elimination
 private void pivot(int p, int q) {

  // everything but row p and column q
  for (int i = 0; i <= numberOfConstraints; i++)
   for (int j = 0; j <= numberOfConstraints
     + numberOfOriginalVariables; j++)
    if (i != p && j != q)
     tableaux[i][j] -= tableaux[p][j] * tableaux[i][q]
       / tableaux[p][q];

  // zero out column q
  for (int i = 0; i <= numberOfConstraints; i++)
   if (i != p)
    tableaux[i][q] = 0.0;

  // scale row p
  for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++)
   if (j != q)
    tableaux[p][j] /= tableaux[p][q];
  tableaux[p][q] = 1.0;
 }

 // return optimal objective value
 public double value() {
  return -tableaux[numberOfConstraints][numberOfConstraints+ numberOfOriginalVariables];
 }

 // return primal solution vector
 public double[] primal() {
  double[] x = new double[numberOfOriginalVariables];
  for (int i = 0; i < numberOfConstraints; i++)
   if (basis[i] < numberOfOriginalVariables)
    x[basis[i]] = tableaux[i][numberOfConstraints
      + numberOfOriginalVariables];
  return x;
 }

 // print tableaux
 public void show() {
     
     double []x = new double[basis.length];
     
  System.out.println("M = " + numberOfConstraints);
  System.out.println("N = " + numberOfOriginalVariables);
  for (int i = 0; i <= numberOfConstraints; i++) {
   for (int j = 0; j <= numberOfConstraints
     + numberOfOriginalVariables; j++) {
    System.out.printf("%7.2f ", tableaux[i][j]);
    tablevalues[i][j] = Double.toString(tableaux[i][j]);
   }
   System.out.println();
  }
  System.out.println("value = " + value());
  for (int i = 0; i < numberOfConstraints; i++)
   if (basis[i] < numberOfOriginalVariables){
    System.out.println("x_"+ basis[i]+ " = "+ tableaux[i][numberOfConstraints+ numberOfOriginalVariables]);
   x[i] = tableaux[i][numberOfConstraints+ numberOfOriginalVariables];
   }
  
  tables[numtable] = new JTable(tablevalues , names);
 scroll[numtable] = new JScrollPane(tables[numtable]);
  
      System.out.println(numtable);

  System.out.println();
 }

 // test client
 public void GetValues(double[] ObjFunc , double[][] leftside , double [] rightside , String objcase , String [] concase)  {

  double[] objectiveFunc = ObjFunc ;
  double[][] constraintLeftSide = leftside ;
  
  Constraint[] constraintOperator = new Constraint[rightside.length];
     for (int i = 0; i < constraintOperator.length; i++) {

        if(">=".equals(concase[i])) 
        constraintOperator[i] = Constraint.greatherThan ;
                else
            if("<=".equals(concase[i]))
        constraintOperator[i] = Constraint.lessThan ;
                else
                        constraintOperator[i] = Constraint.equal ;

     }
//  Constraint[] constraintOperator = { Constraint.lessThan,
//    Constraint.lessThan, Constraint.lessThan, Constraint.lessThan };
  double[] constraintRightSide = rightside ;

  Modeler model = new Modeler(constraintLeftSide, constraintRightSide,
    constraintOperator, objectiveFunc);

  Simplex simplex ;
  
  if("MAX".equals(objcase)){
       simplex = new Simplex(model.getTableaux(),model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), MAXIMIZE);
  }else{
       simplex = new Simplex(model.getTableaux(),model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), MINIMIZE);
  }
  
//       System.out.println(numtable+"here");

  
  double[] x = simplex.primal();
  for (int i = 0; i < x.length; i++)
   System.out.println("x[" + i + "] = " + x[i]);
  System.out.println("Solution: " + simplex.value());
  
     System.out.println(numtable+"here");


 }

 private enum Constraint {
  lessThan, equal, greatherThan
 }

 public static class Modeler {
  private double[][] a; // tableaux
  private int numberOfConstraints; // number of constraints
  private int numberOfOriginalVariables; // number of original variables

  public Modeler(double[][] constraintLeftSide,
    double[] constraintRightSide, Constraint[] constraintOperator,
    double[] objectiveFunction) {
   numberOfConstraints = constraintRightSide.length;
   numberOfOriginalVariables = objectiveFunction.length;
   a = new double[numberOfConstraints + 1][numberOfOriginalVariables
     + numberOfConstraints + 1];

   // initialize constraint
   for (int i = 0; i < numberOfConstraints; i++) {
    for (int j = 0; j < numberOfOriginalVariables; j++) {
     a[i][j] = constraintLeftSide[i][j];
    }
   }

   for (int i = 0; i < numberOfConstraints; i++)
    a[i][numberOfConstraints + numberOfOriginalVariables] = constraintRightSide[i];

   // initialize slack variable
   for (int i = 0; i < numberOfConstraints; i++) {
    int slack = 0;
    switch (constraintOperator[i]) {
    case greatherThan:
     slack = -1;
     break;
    case lessThan:
     slack = 1;
     break;
    default:
    }
    a[i][numberOfOriginalVariables + i] = slack;
   }

   // initialize objective function
   for (int j = 0; j < numberOfOriginalVariables; j++)
    a[numberOfConstraints][j] = objectiveFunction[j];
  }

  public double[][] getTableaux() {
   return a;
  }

  public int getNumberOfConstraint() {
   return numberOfConstraints;
  }

  public int getNumberOfOriginalVariable() {
   return numberOfOriginalVariables;
  }
 }
 
 public int NumTable(){
     System.out.println(numtable);
 return numtable ;
 
 }

 public JScrollPane[] Result(){
 
 return scroll ;
 
 }

}
    

