// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByGoal extends ToDoPerspective
implements ToDoListListener {

//   protected void computePseudoNodes() {
//     super.computePseudoNodes();
//     ToDoList list = Designer.TheDesigner.getToDoList();
//     Vector goals = list.getGoals();     // should be from decision model
//     _pseudoNodes = new Vector(goals.size());
//     java.util.Enumeration enum = goals.elements();
//     while (enum.hasMoreElements()) {
//       Predicate pred = new PredicateGoal((Goal)enum.nextElement());
//       _pseudoNodes.addElement(new ToDoPseudoNode(list, pred));
//     }
//   }

  public ToDoByGoal() {
    super("By Goal");
    addSubTreeModel(new GoListToGoalsToItems());
  }

  //  public String toString() { return "Goal"; }

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemAdded(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoItem item = tde.getToDoItem();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();
    int childIndices[] = new int[1];
    Object children[] = new Object[1];

    Poster post = item.getPoster();
    java.util.Enumeration enum = post.getSupportedGoals().elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      path[1] = g;
      System.out.println("toDoItemAdded firing new item!");
      childIndices[0] = getIndexOfChild(g, item);
      children[0] = item;
      fireTreeNodesInserted(this, path, childIndices, children);
    }
  }

  public void toDoItemRemoved(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoList list = Designer.TheDesigner.getToDoList(); //source?
    ToDoItem item = tde.getToDoItem();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Poster post = item.getPoster();
    java.util.Enumeration enum = post.getSupportedGoals().elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      System.out.println("toDoItemRemoved updating decision node!");
      path[1] = g;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


} /* end class ToDoByGoal */


